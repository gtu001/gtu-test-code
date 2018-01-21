package gtu.log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public abstract class PrintStreamAdapter extends OutputStream {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    String charset = "utf8";

    public PrintStreamAdapter() {
    }
    public PrintStreamAdapter(String charset) {
        this.charset = charset;
    }

    @Override
    public void write(int b) throws IOException {
        if (b == 13 || b == 10) {
            if (b == 10) {
                println();
            }
        } else {
            baos.write(b);
        }
    }

    public abstract void println(String message);
    
    public void println() throws UnsupportedEncodingException {
        println(new String(baos.toByteArray(), charset));
        baos.reset();
    }
    
    public static void main(String[] args) {
        PrintStream out = new PrintStream(new PrintStreamAdapter() {
            @Override
            public void println(String message) {
                System.out.println(message);
            }
        });
        out.println("你好帥");
        out.println("你超帥");
        out.println("done...");
    }
}
