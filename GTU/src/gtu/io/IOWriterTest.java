package gtu.io;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * @author Troy 2012/1/4
 * 
 */
public class IOWriterTest {

    public void testBufferedWriter() {
        String filePath = null;
        BufferedWriter bw = null;
        try {
            FileOutputStream fileout = new FileOutputStream(filePath);
            bw = new BufferedWriter(new OutputStreamWriter(fileout, "UTF-8"));
            // TODO
            bw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
