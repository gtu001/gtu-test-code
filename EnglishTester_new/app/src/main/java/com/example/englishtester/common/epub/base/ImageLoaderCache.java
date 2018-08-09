package com.example.englishtester.common.epub.base;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.englishtester.common.OOMHandler;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import nl.siegmann.epublib.browsersupport.Navigator;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.util.CollectionUtil;

/**
 * This class is a trick to get the JEditorKit to load its Bitmaps from the epub file instead of from the given url.
 * <p>
 * This class is installed as the JEditorPane's Bitmap cache.
 * Whenever it is requested an Bitmap it will try to load that Bitmap from the epub.
 * <p>
 * Can be shared by multiple documents but can only be <em>used</em> by one document at the time because of the currentFolder issue.
 *
 * @author paul
 */
class ImageLoaderCache extends Dictionary<String, Bitmap> {

    public static final String Bitmap_URL_PREFIX = "http:/";

    private static final String TAG = ImageLoaderCache.class.getSimpleName();

    private Map<String, Bitmap> cache = new HashMap<String, Bitmap>();
    private Book book;
    private String currentFolder = "";
    private Navigator navigator;

    public ImageLoaderCache(Navigator navigator) {
        this.navigator = navigator;
        initBook(navigator.getBook());
    }

    public void initBook(Book book) {
        if (book == null) {
            return;
        }
        this.book = book;
        cache.clear();
        this.currentFolder = "";
    }

    public void setContextResource(Resource resource) {
        if (resource == null) {
            return;
        }
        if (StringUtils.isNotBlank(resource.getHref())) {
            int lastSlashPos = resource.getHref().lastIndexOf('/');
            if (lastSlashPos >= 0) {
                this.currentFolder = resource.getHref().substring(0, lastSlashPos + 1);
            }
        }
    }

    public void initImageLoader(HTMLDocument document) {
        try {
            document.setBase(new URL(ImageLoaderCache.Bitmap_URL_PREFIX));
        } catch (MalformedURLException e) {
            Log.e(TAG, "initImageLoader ERR : " + e.getMessage(), e);
        }
        setContextResource(navigator.getCurrentResource());
        document.getDocumentProperties().put("BitmapCache", this);
    }


    private String getResourceHref(String requestUrl) {
        Log.v(TAG, "# -- getResourceHref -- start");
        Log.v(TAG, "currentFolder = " + currentFolder);
        Log.v(TAG, "requestUrl = " + requestUrl);
        if (requestUrl.toString().startsWith(Bitmap_URL_PREFIX)) {
            String resourceHref = requestUrl.toString().substring(Bitmap_URL_PREFIX.length());
            resourceHref = currentFolder + resourceHref;
            resourceHref = FilenameUtils.normalize(resourceHref);
            // normalize uses the SYSTEM_SEPARATOR, which on windows is a '\'
            // replace with '/' to make it href '/'
            resourceHref = resourceHref.replaceAll("\\\\", "/");
            return resourceHref;
        } else {
            //gtu001 custom ↓↓↓↓↓↓
            String tmpDir = currentFolder;
            if (tmpDir.endsWith("/")) {
                tmpDir = StringUtils.substring(tmpDir, 0, -1);
            }
            while (requestUrl.startsWith("../")) {
                requestUrl = requestUrl.substring("../".length());
                if (tmpDir.lastIndexOf("/") != -1) {
                    tmpDir = tmpDir.substring(0, tmpDir.lastIndexOf("/"));
                } else {
                    tmpDir = "";
                }
            }
            String rtnPath = FilenameUtils.normalize(tmpDir + "/" + requestUrl);
            if (rtnPath.startsWith("/")) {
                rtnPath = rtnPath.substring(1);
            }
            Log.v(TAG, "rtnPath = " + rtnPath);
            //gtu001 custom ↑↑↑↑↑↑
            return rtnPath;
        }
    }

    /**
     * Create an Bitmap from the data of the given resource.
     *
     * @param BitmapResource
     * @return
     */
    private Bitmap createBitmap(Resource BitmapResource) {
        Bitmap result = null;
        try {
            result = OOMHandler.new_decode(BitmapResource.getInputStream());
        } catch (IOException e) {
            Log.e(TAG, "createBitmap ERR : " + e.getMessage(), e);
        }
        return result;
    }

    public Bitmap get(Object key) {
        if (book == null) {
            return null;
        }

        String BitmapURL = key.toString();

        // see if the Bitmap is already in the cache
        Bitmap result = cache.get(BitmapURL);
        if (result != null) {
            return result;
        }

        // get the Bitmap resource href
        String resourceHref = getResourceHref(BitmapURL);

        //For Debug -----------------------------------------------------------
//        for (Resource res : book.getResources().getAll()) {
//            Log.v(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + res.getHref());
//        }
        //For Debug -----------------------------------------------------------

        // find the Bitmap resource in the book resources
        Resource BitmapResource = book.getResources().getByHref(resourceHref);
        if (BitmapResource == null) {
            return result;
        }

        // create an Bitmap from the resource and add it to the cache
        result = createBitmap(BitmapResource);
        if (result != null) {
            cache.put(BitmapURL.toString(), result);
        }

        return result;
    }

    public int size() {
        return cache.size();
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }

    public Enumeration<String> keys() {
        return CollectionUtil.createEnumerationFromIterator(cache.keySet().iterator());
    }

    public Enumeration<Bitmap> elements() {
        return CollectionUtil.createEnumerationFromIterator(cache.values().iterator());
    }

    public Bitmap put(String key, Bitmap value) {
        return cache.put(key.toString(), (Bitmap) value);
    }

    public Bitmap remove(Object key) {
        return cache.remove(key);
    }

    /**
     * Clears the Bitmap cache.
     */
    public void clear() {
        cache.clear();
    }

    public String toString() {
        return cache.toString();
    }
}