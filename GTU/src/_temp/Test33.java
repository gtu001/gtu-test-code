package _temp;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class Test33 {

    public static void main(String[] args) throws IOException {
//        System.out.println(true ^ true);
//        System.out.println(false ^ false);
//        System.out.println(true ^ false);
        File editor = new File("I:\\apps\\notepad\\MadEdit-0.2.9.1\\MadEdit.exe");
        File file2 = new File("c:/Users/gtu00/OneDrive/Desktop/DebugMointerUI_config.properties");
        String command = String.format("cmd /c call \"%s\" \"%s\"", editor, file2);
        Runtime.getRuntime().exec(command);
        System.out.println(command);
        System.out.println("done...");
    }

}
