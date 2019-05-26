package gtu.lombok.ex1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lombok.Cleanup;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class LombokTest001 {

    public static void main(String[] args) throws IOException {
        @Cleanup
        InputStream in = new FileInputStream(args[0]);
        @Cleanup
        OutputStream out = new FileOutputStream(args[1]);
        byte[] b = new byte[10000];
        while (true) {
            int r = in.read(b);
            if (r == -1)
                break;
            out.write(b, 0, r);
        }
    }

    private static @Data class TestLombokBean01 {
        String strVal;
        int intVal;
    }

    private static class TestLombokBean02 {
        @Getter
        @Setter
        String strVal;
        @Getter
        @Setter
        int intVal;
    }
}
