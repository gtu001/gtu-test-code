package gtu.binary;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * 將檔案編碼轉換
 * 
 * @author Troy 2012/1/7
 */
public class RandomAccessFileTransCode {

    static public void main(String args[]) throws Exception {
        File infile = new File("C:/Users/gtu001/Desktop/811D-高雄楠梓-0207.txt");
        File outfile = new File("C:/Users/gtu001/Desktop/test_(2).txt");

        RandomAccessFile inraf = new RandomAccessFile(infile, "r");
        RandomAccessFile outraf = new RandomAccessFile(outfile, "rw");

        FileChannel finc = inraf.getChannel();
        FileChannel foutc = outraf.getChannel();

        MappedByteBuffer inmbb = finc.map(FileChannel.MapMode.READ_ONLY, 0, (int) infile.length());

        Charset inCharset = Charset.forName("UTF8");
        Charset outCharset = Charset.forName("UTF16");

        CharsetDecoder inDecoder = inCharset.newDecoder();
        CharsetEncoder outEncoder = outCharset.newEncoder();

        CharBuffer cb = inDecoder.decode(inmbb);
        ByteBuffer outbb = outEncoder.encode(cb);

        foutc.write(outbb);

        inraf.close();
        outraf.close();
    }
    
    public static void main2(String[] args) throws Exception {
        final int BUFFER_SIZE = 0x300000; // 緩衝區為3M
        File f = new File("c:/某超大檔案.txt");
        int len = 0;
        Long start = System.currentTimeMillis();
        for (int z = 8; z >0; z--) {
            MappedByteBuffer inputBuffer = new RandomAccessFile(f, "r")
                    .getChannel().map(FileChannel.MapMode.READ_ONLY,
                            f.length() * (z-1) / 8, f.length() * 1 / 8);
            byte[] dst = new byte[BUFFER_SIZE];// 每次讀出3M的內容
            for (int offset = 0; offset < inputBuffer.capacity(); offset += BUFFER_SIZE) {
                if (inputBuffer.capacity() - offset >= BUFFER_SIZE) {
                    for (int i = 0; i < BUFFER_SIZE; i++)
                        dst[i] = inputBuffer.get(offset + i);
                } else {
                    for (int i = 0; i < inputBuffer.capacity() - offset; i++)
                        dst[i] = inputBuffer.get(offset + i);
                }
                int length = (inputBuffer.capacity() % BUFFER_SIZE == 0) ? BUFFER_SIZE
                        : inputBuffer.capacity() % BUFFER_SIZE;
                len += new String(dst, 0, length).length();
                System.out.println(new String(dst, 0, length).length()+"-"+(z-1)+"-"+(8-z+1));
            }
        }
        System.out.println(len);
        long end = System.currentTimeMillis();
        System.out.println("讀取文件文件花費：" + (end - start) + "毫秒");
    }
}
