package gtu.epub;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import nl.siegmann.epublib.browsersupport.NavigationEventListener;
import nl.siegmann.epublib.browsersupport.Navigator;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.epub.EpubReader;

public class EpubReaderTest001 {

    // http://www.siegmann.nl/static/epublib/apidocs/nl/siegmann/epublib/epub/EpubReader.html
    // http://www.siegmann.nl/epublib/android/
    // https://github.com/psiegman/epublib <--viewer here
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String filePath = "C:/Users/gtu00/OneDrive/Desktop/Sapiens-A-Brief-History-of-Humankind.epub";
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
    }

    public void addNavigationEventListener(NavigationEventListener listener) {
        navigator.addNavigationEventListener(listener);
    }

    public void startButtonAction() {
        navigator.gotoFirstSpineSection(self);
    }

    public void previousChapterButtonAction() {
        navigator.gotoPreviousSpineSection(self);
    }

    public void previousPageButtonAction() {
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

    public void nextPageButtonAction() {
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

    public void nextChapterButtonAction() {
        navigator.gotoNextSpineSection(self);
    }

    public void endButtonAction() {
        navigator.gotoLastSpineSection(self);
    }
}
