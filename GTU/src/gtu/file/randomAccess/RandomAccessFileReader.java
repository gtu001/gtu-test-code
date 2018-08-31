package gtu.file.randomAccess;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.TreeMap;

import org.apache.commons.lang3.tuple.Pair;

public abstract class RandomAccessFileReader {

    private RandomAccessFile sFile;
    private int lineNumber = 0;
    private long currentPos = 0;
    private String encoding;
    private TreeMap<Integer, Long[]> linePosMap = new TreeMap<Integer, Long[]>();

    public RandomAccessFileReader(String path, String encoding) {
        try {
            this.sFile = new RandomAccessFile(path, "r");
            this.encoding = encoding;
            this.lineNumber = 0;
            this.currentPos = 0;
            this.linePosMap = new TreeMap<Integer, Long[]>();
        } catch (Exception e) {
            throw new RuntimeException("init ERR : " + e.getMessage(), e);
        }
    }

    public abstract boolean readLine(String line, int lineNumber, long currentPos);

    public void start() {
        try {
            sFile.seek(0);
            while (currentPos < sFile.length()) {
                
                long lineStartPos = sFile.getFilePointer();
                
                String line = sFile.readLine();

                line = new String(line.getBytes("ISO-8859-1"), encoding);

                long lineEndPos = sFile.getFilePointer();

                currentPos = lineEndPos;

                lineNumber++;

                linePosMap.put(lineNumber, new Long[] { lineStartPos, lineEndPos });

                boolean isContinue = this.readLine(line, lineNumber, currentPos);
                if (!isContinue) {
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("start ERR : " + e.getMessage(), e);
        } finally {
            try {
                sFile.close();
            } catch (Exception e) {
            }
        }
    }

    public Pair<Long, Long> getLineStartEnd(int lineNumber) {
        if (linePosMap.containsKey(lineNumber)) {
            Long[] arry = linePosMap.get(lineNumber);
            return Pair.of(arry[0], arry[1]);
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        RandomAccessFileReader reader = new RandomAccessFileReader("C:/Users/wistronits/Desktop/test.txt", "UTF8") {
            @Override
            public boolean readLine(String line, int lineNumber, long currentPos) {
                System.out.println(lineNumber + " " + currentPos + " " + line);
                return true;
            }
        };
        reader.start();

        Pair<Long, Long> line = reader.getLineStartEnd(367);
        
        RandomAccessFile rs = new RandomAccessFile("C:/Users/wistronits/Desktop/test.txt", "r");
        rs.seek(line.getLeft());
        
        byte[] bytes = new byte[(int) (line.getRight() - line.getLeft() + 1)];
        
        rs.read(bytes);
        
        String vvvvv  = new String(bytes, "UTF8");
        
        System.out.println(String.format("<<<<<%1$s>>>>>%2$s", vvvvv, line));
        
        System.out.println("done...");
    }
}