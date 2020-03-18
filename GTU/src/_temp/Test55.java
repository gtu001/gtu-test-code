package _temp;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class Test55 {

    public static void main(String[] args) throws IOException {
        File file = new File("d:/games/roms/sega/Gens32_Surreal_v1_88_Std/Gens32_Surreal_v1_88_Std/Gens32 Surreal.exe");
        
        Desktop.getDesktop().open(file);
        
        System.out.println("done...");
    }
}
