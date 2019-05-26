package gtu.epub;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

public class EpubBookFrontImageGetter {

    public static void main(String[] args) {
        String filePath = "D:/gtu001_dropbox/Dropbox/guava/電子書/Sapiens-A-Brief-History-of-Humankind.epub";
        EpubBookFrontImageGetter t = new EpubBookFrontImageGetter();
        InputStream is = t.getCoverImage(filePath);
        t.imshow("xxx", is);
        System.out.println("done...");
    }

    public void imshow(String title, InputStream is) {
        try {
            BufferedImage bufImage = ImageIO.read(is);
            JFrame frame = new JFrame(title);
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            gtu.swing.util.JFrameUtil.setVisible(true, frame);
        } catch (Exception e) {
            throw new RuntimeException("imshow ERR : " + e.getMessage(), e);
        }
    }

    public InputStream getCoverImage(String filePath) {
        try {
            EpubReader epubReader = new EpubReader();
            Book book = epubReader.readEpubLazy(filePath, "UTF8");
            return book.getCoverImage().getInputStream();
        } catch (Exception ex) {
            throw new RuntimeException("getBookFirstImage ERR : " + ex.getMessage(), ex);
        }
    }

    public static String getFirstAuthor(Book book) {
        if (book == null || book.getMetadata().getAuthors().isEmpty())
            return null;

        // Loop through authors to get first non-empty one.
        for (Author author : book.getMetadata().getAuthors()) {
            String fName = author.getFirstname();
            String lName = author.getLastname();
            // Skip this author now if it doesn't have a non-null, non-empty
            // name.
            if ((fName == null || fName.isEmpty()) && (lName == null || lName.isEmpty()))
                continue;

            // Return the name, which might only use one of the strings.
            if (fName == null || fName.isEmpty())
                return lName;
            if (lName == null || lName.isEmpty())
                return fName;
            return fName + " " + lName;
        }
        return null;
    }
}
