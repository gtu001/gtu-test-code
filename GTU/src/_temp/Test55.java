package _temp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import gtu.file.FileUtil;

public class Test55 {

    public static void main(String[] args) throws IOException {
        File testMp4 = new File(FileUtil.DESKTOP_DIR, "Chinese Girl is Playing with her Teddy Bear Part2 - Pornhub.com_720P_1500K_145725732.mp4");
        OutputStream fos = new FileOutputStream(testMp4, true);
        for (int i = 0; i < 2000; i++)
            fos.write(1);
        fos.flush();
        fos.close();
        System.out.println("done...");
    }

}
