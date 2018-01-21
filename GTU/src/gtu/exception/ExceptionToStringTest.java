package gtu.exception;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class ExceptionToStringTest {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        ExceptionToStringTest test = new ExceptionToStringTest();
        System.out.println("=====start");
        System.out.println(test.exceptionToString(new Exception("test...")));
        System.out.println("=====end");
    }

    /**
     * 将异常信息转化成字符串
     * 
     * @param t
     * @return
     * @throws IOException
     */
    private String exceptionToString(Throwable t) throws IOException {
        if (t == null)
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            t.printStackTrace(new PrintStream(baos));
        } finally {
            baos.flush();
            baos.close();
        }
        return baos.toString();
    }
}
