package gtu.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Troy 2009/10/20
 * 
 */
public class LineNumberReaderTest {

    private static LineNumberReaderTest lineNumberReaderTest = null;

    private LineNumberReaderTest() {
        super();
    }

    public static LineNumberReaderTest getInstance() {
        if (lineNumberReaderTest == null) {
            lineNumberReaderTest = new LineNumberReaderTest();
        }
        return lineNumberReaderTest;
    }

    private File getLastModifyFromThesameFiles(String prefix, String suffix, File[] list) {
        List<File> check = new ArrayList<File>();
        for (File f : list) {
            boolean prefixOk = false;
            boolean suffixOk = false;
            if (f.getAbsoluteFile().getName().startsWith(prefix)) {
                prefixOk = true;
            }
            if (f.getAbsoluteFile().getName().endsWith(suffix)) {
                suffixOk = true;
            }
            if (prefixOk && suffixOk) {
                check.add(f);
            }
        }
        long lastmodify = 0L;
        int index = -1;
        for (int ii = 0; ii < check.size(); ii++) {
            File f = check.get(ii);
            if (f.lastModified() > lastmodify) {
                lastmodify = f.lastModified();
                index = ii;
            }
        }
        if (index != -1) {
            return check.get(index);
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        Integer lineNumber = 1;
        String fileName = "E:\\11_AboveE_OW\\defaultroot\\WEB-INF\\logs\\";
        File file = LineNumberReaderTest.getInstance().getLastModifyFromThesameFiles("system", ".log",
                new File(fileName).listFiles());

        int number = LineNumberReaderTest.getInstance().read(lineNumber, file, new LineNumberReaderOperation() {
            public boolean doSomethingIfTrueExitLoop(String theStringOfThisLine, Integer lineNumber,
                    LineNumberReader lineReader) {
                String str = theStringOfThisLine;
                if (str.indexOf("SQLString") != -1) {
                    if (str.indexOf("SELECT SUM(RECORDCOUNT) AS RECORDCOUNT") != -1) {
                        return false;
                    }
                    int subPos = 0;
                    if (str.indexOf("SQL = ") != -1) {
                        String rep = "SQL = ";
                        subPos = str.indexOf(rep);
                        subPos += rep.length();
                    } else if (str.indexOf("SQLString: ") != -1) {
                        String rep = "SQLString: ";
                        subPos = str.indexOf(rep);
                        subPos += rep.length();
                    }
                    str = str.substring(subPos) + ";";
                    System.out.println(lineReader.getLineNumber() + "\t" + str);
                }
                return false;
            }
        });

        FileOutputStream fos = new FileOutputStream(fileName + "line.txt");
        fos.write(String.valueOf(number).getBytes());
        fos.close();
    }

    public interface LineNumberReaderOperation {
        public boolean doSomethingIfTrueExitLoop(String theStringOfThisLine, Integer lineNumber,
                LineNumberReader lineReader);
    }

    public int read(Integer startLineNumber, File file, LineNumberReaderOperation lineNumberReaderOperation)
            throws IOException {
        LineNumberReader lineReader = new LineNumberReader(new FileReader(file));
        String str = null;
        int countLineNumber = 0;
        do {
            countLineNumber = lineReader.getLineNumber() + 1;
            if (startLineNumber >= countLineNumber) {
                continue;
            }
            str = lineReader.readLine();
            if (str == null) {
                break;
            }
            if (lineNumberReaderOperation.doSomethingIfTrueExitLoop(str, startLineNumber, lineReader)) {
                break;
            }
        } while (str != null);
        lineReader.close();
        return countLineNumber;
    }
}
