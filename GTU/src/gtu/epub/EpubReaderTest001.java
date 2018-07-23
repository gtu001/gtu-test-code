package gtu.epub;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.epub.EpubReader;

public class EpubReaderTest001 {

    // http://www.siegmann.nl/static/epublib/apidocs/nl/siegmann/epublib/epub/EpubReader.html
    // http://www.siegmann.nl/epublib/android/
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // read epub file
        EpubReader epubReader = new EpubReader();

        // FileInputStream fis = new FileInputStream(new File());
        String filePath = "C:/Users/gtu00/OneDrive/Desktop/Sapiens-A-Brief-History-of-Humankind.epub";
        Book book = epubReader.readEpubLazy(filePath, "UTF8");

        Spine spine = book.getSpine();
        List<SpineReference> spineList = spine.getSpineReferences();
    }

}
