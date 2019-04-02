package gtu.mobi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.rr.mobi4java.MobiDocument;
import org.rr.mobi4java.MobiReader;

public class MobiReaderTest001 {

    public static void main(String[] args) throws FileNotFoundException, IOException {
//        https://github.com/rrauschenbach/mobi-api4java
        
        MobiDocument mobiDoc = new MobiReader().read(new File("C:/Users/wistronits/Desktop/The-Three-Body-Problem-Remembrance-of-Earth-s-Past-.mobi"));
        String text = mobiDoc.getTextContent();
        System.out.println(text);
    }

}
