package gtu.epub;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.testng.log4testng.Logger;

import nl.siegmann.epublib.browsersupport.NavigationEventListener;
import nl.siegmann.epublib.browsersupport.Navigator;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Guide;
import nl.siegmann.epublib.domain.GuideReference;
import nl.siegmann.epublib.epub.EpubReader;

public class EpubReaderTest001 {

    private static Logger logger = Logger.getLogger(EpubReaderTest001.class);

    // http://www.siegmann.nl/static/epublib/apidocs/nl/siegmann/epublib/epub/EpubReader.html
    // http://www.siegmann.nl/epublib/android/
    // https://github.com/psiegman/epublib <--viewer here
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String filePath = "D:/gtu001_dropbox/Dropbox/guava/電子書/Sapiens-A-Brief-History-of-Humankind.epub";
        EpubReaderTest001 t = new EpubReaderTest001();
        t.applyBook(filePath);
        System.out.println("done...");
    }

    private Navigator navigator = new Navigator();
    private String filePath;
    private String title;
    private Book book;
    private EpubReaderTest001 self;

    public void applyBook(String filePath) throws IOException {
        this.filePath = filePath;
        self = this;

        EpubReader epubReader = new EpubReader();

        book = epubReader.readEpubLazy(filePath, "UTF8");
        title = book.getTitle();

        navigator.gotoBook(book, self);

        this.createTableData();
    }

    private Object[][] createTableData() {
        Guide guide = navigator.getBook().getGuide();
        List<String[]> result = new ArrayList<String[]>();
        for (GuideReference guideReference : guide.getReferences()) {
            result.add(new String[] { guideReference.getType(), guideReference.getTitle() });
        }
        Object[][] rtnVal = result.toArray(new Object[result.size()][2]);
        Stream.of(rtnVal).forEach(arry -> {
            logger.info("createTableData - " + Arrays.toString(rtnVal));
        });
        return rtnVal;
    }

    public void addNavigationEventListener(NavigationEventListener listener) {
        navigator.addNavigationEventListener(listener);
    }

    public void action_startButton() {
        navigator.gotoFirstSpineSection(self);
    }

    public void action_previousChapterButton() {
        navigator.gotoPreviousSpineSection(self);
    }

    public void action_previousPageButton() {
        // Point viewPosition = scrollPane.getViewport().getViewPosition();
        // if (viewPosition.getY() <= 0) {
        // navigator.gotoPreviousSpineSection(this);
        // return;
        // }
        // int viewportHeight = scrollPane.getViewport().getHeight();
        // int newY = (int) viewPosition.getY();
        // newY -= viewportHeight;
        // newY = Math.max(0, newY - viewportHeight);
        // scrollPane.getViewport().setViewPosition(
        // new Point((int) viewPosition.getX(), newY));
    }

    public void action_nextPageButton() {
        // Point viewPosition = scrollPane.getViewport().getViewPosition();
        // int viewportHeight = scrollPane.getViewport().getHeight();
        // int scrollMax = scrollPane.getVerticalScrollBar().getMaximum();
        // if (viewPosition.getY() + viewportHeight >= scrollMax) {
        // navigator.gotoNextSpineSection(this);
        // return;
        // }
        // int newY = ((int) viewPosition.getY()) + viewportHeight;
        // scrollPane.getViewport().setViewPosition(
        // new Point((int) viewPosition.getX(), newY));
    }

    public void action_nextChapterButton() {
        navigator.gotoNextSpineSection(self);
    }

    public void action_endButton() {
        navigator.gotoLastSpineSection(self);
    }
}
