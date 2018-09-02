package gtu.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageReadAndWrite {

    public static void main(String[] args) {
        System.out.println("done...");
    }

    public static byte[] read(File imageFile) throws IOException{
        File originalImgFile = imageFile;
        BufferedImage bufferedImage = ImageIO.read(originalImgFile);
        // convert BufferedImage to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", baos);
        baos.flush();
        byte[] originalImgByte = baos.toByteArray();
        baos.close();
        return originalImgByte;
    }
    
    public static void write(byte[] imageArray, File toImageFile) throws IOException{
        // Get the byte array of the image.
        // Construct the file.
        // Write the image bytes to file.
        FileOutputStream fos = new FileOutputStream(toImageFile);
        fos.write(imageArray);
        fos.close();
    }
}
