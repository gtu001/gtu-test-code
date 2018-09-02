package gtu.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * @author Troy 2012/1/2
 * 
 */
public class IOReaderTest {

    private String loadStream(InputStream in) throws IOException {
        int ptr = 0;
        in = new BufferedInputStream(in);
        StringBuffer buffer = new StringBuffer();
        while ((ptr = in.read()) != -1) {
            buffer.append((char) ptr);
        }
        return buffer.toString();
    }

    private String input() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String keyin = br.readLine();
        return keyin;
    }

    public static String loadStream2(InputStream ins) {
        String str = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] temp = new byte[1024];
            int pos = 0;
            while ((pos = ins.read(temp)) != -1) {
                baos.write(temp, 0, pos);
            }
            baos.close();
            ins.close();
            str = new String(baos.toByteArray(), "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return str;
    }

    private HSSFWorkbook readExcel(File file) throws Exception {
        int size = (int) (file.length() - file.length() % 512);
        byte[] buffer = new byte[size];
        InputStream inputFile = new FileInputStream(file);
        inputFile.read(buffer, 0, size);
        inputFile.close();
        InputStream byteIS = new ByteArrayInputStream(buffer);
        byteIS.close();
        return new HSSFWorkbook(byteIS);
    }

    private HSSFWorkbook readExcel2(File file) throws Exception {
        InputStream inputFile = new FileInputStream(file);

        // read entire stream into byte array:
        ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count;
        while ((count = inputFile.read(buffer)) != -1)
            byteOS.write(buffer, 0, count);
        byteOS.close();
        byte[] allBytes = byteOS.toByteArray();

        // create workbook from array:
        InputStream byteIS = new ByteArrayInputStream(allBytes);
        HSSFWorkbook workBook = new HSSFWorkbook(byteIS);
        return workBook;
    }

    private StringBuffer loadStream3(InputStream is) {
        StringBuffer sb = new StringBuffer();
        try {
            DataInputStream dis = new DataInputStream(is);

            byte[] buf = new byte[8 * 1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int ret = 0;
            byte[] content;
            while ((ret = dis.read(buf, 0, buf.length)) > -1) {
                content = new byte[ret];
                System.arraycopy(buf, 0, content, 0, ret);
                baos.write(content);
            }
            baos.flush();
            baos.close();
            sb.append(new String(baos.toByteArray(), "MS950")); // UTF-8
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }

    private String loadFile(String path) {
        File thefile = new File(path);
        BufferedReader bufferedreader = null;
        StringBuffer sb = new StringBuffer();
        try {
            bufferedreader = new BufferedReader(new FileReader(thefile));
            int i = 0;
            while (bufferedreader.ready()) {
                i++;
                String tmpLine = bufferedreader.readLine();
                if (i > 5) {
                    sb.append(tmpLine);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                bufferedreader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private String readTemplateFile(String path) throws IOException {
        // spring resource
        Resource resource = new FileSystemResource(path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(resource.getFile());
            int x = fis.available();
            byte b[] = new byte[x];
            fis.read(b);
            return new String(b, "Big5");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fis.close();
        }
        return null;
    }

    private void charArrayReader() throws IOException {
        String s = "test\ntest2\ntest3";
        char buf[] = new char[s.length()];
        s.getChars(0, s.length(), buf, 0);

        CharArrayReader in = new CharArrayReader(buf);
        BufferedReader f = new BufferedReader(in);

        String tempStr = f.readLine();
        while (tempStr != null) {
            System.out.println("readLine1: " + tempStr);
            tempStr = f.readLine();
        }
    }
}
