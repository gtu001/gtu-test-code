package _temp;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import gtu.file.FileUtil;

public class Test52 {

    public static void main(String[] args) throws IOException {
        String outputfile = "C:/Users/wistronits/Desktop/" + FileUtil.escapeFilename_replaceToFullChar(
                "Let's Play Darkwood, Katz Plays | ep1 | Welcome to Darkwood_Let's Play Darkwood, Katz Plays - ep1 - Welcome to Darkwood.mp4", false);
        System.out.println(outputfile);
        BufferedOutputStream outstream = new BufferedOutputStream(new FileOutputStream(outputfile));
        outstream.write(new byte[1]);
        outstream.flush();
        outstream.close();
        System.out.println("done...");
    }

}
