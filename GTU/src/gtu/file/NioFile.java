package gtu.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NioFile {

    public static void main(String[] args) throws IOException {
        String dir = System.getProperty("user.dir") + File.separator + "test001" + File.separator;
        Path path = Paths.get(dir);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        System.out.println(path);
    }

}
