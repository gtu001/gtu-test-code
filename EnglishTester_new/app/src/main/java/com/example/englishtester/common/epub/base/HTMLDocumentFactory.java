package com.example.englishtester.common.epub.base;

import com.example.englishtester.common.Log;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import nl.siegmann.epublib.browsersupport.NavigationEvent;
import nl.siegmann.epublib.browsersupport.NavigationEventListener;
import nl.siegmann.epublib.browsersupport.Navigator;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.service.MediatypeService;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates swing HTML documents from resources.
 * <p>
 * Between books the init(Book) function needs to be called in order for images to appear correctly.
 *
 * @author paul.siegmann
 */
public class HTMLDocumentFactory implements NavigationEventListener {

    private static final String TAG = HTMLDocumentFactory.class.getSimpleName();

    private static final Logger log = LoggerFactory.getLogger(HTMLDocumentFactory.class);

    // After opening the book we wait a while before we starting indexing the rest of the pages.
    // This way the book opens, everything settles down, and while the user looks at the cover page
    // the rest of the book is indexed.
    public static final int DOCUMENT_CACHE_INDEXER_WAIT_TIME = 500;

    private ImageLoaderCache imageLoaderCache;
    private ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();
    private Lock cacheReadLock = cacheLock.readLock();
    private Lock cacheWriteLock = cacheLock.writeLock();
    private Map<String, HTMLDocument> documentCache = new HashMap<String, HTMLDocument>();
    private MyHtmlEditorKit editorKit;

    //gtu001 custom ↓↓↓↓↓↓
    private Navigator navigator;
    private Object self;
    private NavigationEventListener gtuCustomListener;
    //gtu001 custom ↑↑↑↑↑↑

    public HTMLDocumentFactory(Navigator navigator, MyHtmlEditorKit editorKit, Object self, NavigationEventListener gtuCustomListener) {
        //gtu001 custom ↓↓↓↓↓↓
        this.navigator = navigator;
        this.self = self;
        this.gtuCustomListener = gtuCustomListener;
        //gtu001 custom ↑↑↑↑↑↑

        this.editorKit = editorKit;
        this.imageLoaderCache = new ImageLoaderCache(this.navigator);
        init(this.navigator.getBook());
        this.navigator.addNavigationEventListener(this);
    }

    public void init(Book book) {
        if (book == null) {
            return;
        }
        imageLoaderCache.initBook(book);
        initDocumentCache(book);

        //gtu001 custom ↓↓↓↓↓↓
        //this.initNavigator(this.navigator, book);
        //gtu001 custom ↑↑↑↑↑↑
    }

    //gtu001 custom ↓↓↓↓↓↓
    private void initNavigator(Navigator navigator, Book book) {
        try {
            FieldUtils.writeDeclaredField(navigator, "book", book, true);
            FieldUtils.writeDeclaredField(navigator, "currentSpinePos", 0, true);
            FieldUtils.writeDeclaredField(navigator, "currentResource", book.getCoverPage(), true);
            FieldUtils.writeDeclaredField(navigator, "currentPagePos", 0, true);
        } catch (Exception e) {
            throw new RuntimeException("initNavigator ERR : " + e.getMessage(), e);
        }
    }
    //gtu001 custom ↑↑↑↑↑↑

    private void putDocument(Resource resource, HTMLDocument document) {
        if (document == null) {
            return;
        }
        cacheWriteLock.lock();
        try {
            documentCache.put(resource.getHref(), document);
        } finally {
            cacheWriteLock.unlock();
        }
    }


    /**
     * Get the HTMLDocument representation of the resource.
     * If the resource is not an XHTML resource then it returns null.
     * It first tries to get the document from the cache.
     * If the document is not in the cache it creates a document from
     * the resource and adds it to the cache.
     *
     * @param resource
     * @return the HTMLDocument representation of the resource.
     */
    public HTMLDocument getDocument(Resource resource) {
        if (resource == null) {
            new Throwable("<<<<error here>>>>>").printStackTrace();
            Log.e(TAG, "<resource is null>");
            return null;
        }

        HTMLDocument document = null;

        // try to get the document from  the cache
        cacheReadLock.lock();
        try {
            document = documentCache.get(resource.getHref());
        } finally {
            cacheReadLock.unlock();
        }

        // document was not in the cache, try to create it and add it to the cache
        if (document == null) {
            document = createDocument(resource);
            putDocument(resource, document);
        }

        // initialize the imageLoader for the specific document
        if (document != null) {
            imageLoaderCache.initImageLoader(document);
        }

        return document;
    }

    private String stripHtml(String input) {
        String result = removeControlTags(input);
//		result = result.replaceAll("<meta\\s+[^>]*http-equiv=\"Content-Type\"[^>]*>", "");
        return result;
    }

    /**
     * Quick and dirty stripper of all &lt;?...&gt; and &lt;!...&gt; tags as
     * these confuse the html viewer.
     *
     * @param input
     * @return the input stripped of control characters
     */
    private static String removeControlTags(String input) {
        StringBuilder result = new StringBuilder();
        boolean inControlTag = false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (inControlTag) {
                if (c == '>') {
                    inControlTag = false;
                }
            } else if (c == '<' // look for &lt;! or &lt;?
                    && i < input.length() - 1
                    && (input.charAt(i + 1) == '!' || input.charAt(i + 1) == '?')) {
                inControlTag = true;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Creates a swing HTMLDocument from the given resource.
     * <p>
     * If the resources is not of type XHTML then null is returned.
     *
     * @param resource
     * @return a swing HTMLDocument created from the given resource.
     */
    private HTMLDocument createDocument(Resource resource) {
        HTMLDocument result = null;
        if (resource.getMediaType() != MediatypeService.XHTML) {
            return result;
        }
        try {
            HTMLDocument document = (HTMLDocument) editorKit.createDefaultDocument();
            MyParserCallback parserCallback = new MyParserCallback(document.getReader(0));
            Parser parser = editorKit.getParser();
            String pageContent = IOUtils.toString(resource.getReader());
            pageContent = stripHtml(pageContent);
            document.remove(0, document.getLength());
            Reader contentReader = new StringReader(pageContent);
            parser.parse(contentReader, parserCallback, true);
            parserCallback.flush();
            //gtu001 custom ↓↓↓↓↓↓
            document.setHref(resource.getHref());
            document.setTitle(resource.getTitle());
            document.setId(resource.getId());
            //gtu001 custom ↑↑↑↑↑↑
            result = document;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }

    private void initDocumentCache(Book book) {
        if (book == null) {
            return;
        }
        documentCache.clear();
        Thread documentIndexerThread = new Thread(new DocumentIndexer(book), "DocumentIndexer");
        documentIndexerThread.setPriority(Thread.MIN_PRIORITY);
        documentIndexerThread.start();

//		addAllDocumentsToCache(book);
    }


    private class DocumentIndexer implements Runnable {
        private Book book;

        public DocumentIndexer(Book book) {
            this.book = book;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(DOCUMENT_CACHE_INDEXER_WAIT_TIME);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
            addAllDocumentsToCache(book);
        }

        private void addAllDocumentsToCache(Book book) {
            for (Resource resource : book.getResources().getAll()) {
                getDocument(resource);
            }
        }
    }


    @Override
    public void navigationPerformed(NavigationEvent navigationEvent) {
        if (navigationEvent.isBookChanged() || navigationEvent.isResourceChanged()) {
            imageLoaderCache.clear();
        }

        //gtu001 custom ↓↓↓↓↓↓
        if (this.gtuCustomListener != null) {
            this.gtuCustomListener.navigationPerformed(navigationEvent);
        }
        //gtu001 custom ↑↑↑↑↑↑
    }
}
