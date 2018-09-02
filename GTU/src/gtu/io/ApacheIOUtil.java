package gtu.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

import org.apache.commons.io.IOUtils;

/**
 * @author Troy 2009/10/5
 * 
 */
public class ApacheIOUtil {

    public void readNwrite() throws IOException {
        InputStream in = new FileInputStream("NOTICE.txt");
        OutputStream out = new FileOutputStream("NOTICE1.txt");
        IOUtils.copy(in, out);
        IOUtils.closeQuietly(in);// 完成了文件读写
    }

    public void readInConsole() throws IOException {
        Reader reader = new FileReader("NOTICE1.txt");
        IOUtils.copy(reader, System.out);// 完成了文件输出到控制台
        IOUtils.closeQuietly(reader);
    }

    public String readToString() throws IOException {
        InputStream in = new FileInputStream("NOTICE.txt");
        return IOUtils.toString(in);
    }
}
