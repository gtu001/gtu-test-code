package gtu.jpg;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferShort;

public class RawImageUtil {
    public static BufferedImage short2Buffered(short[] pixels, int width, int height) throws IllegalArgumentException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);
        short[] imgData = ((DataBufferShort) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(pixels, 0, imgData, 0, pixels.length);
        return image;
    }

    public static BufferedImage byte2Buffered(byte[] pixels, int width, int height) throws IllegalArgumentException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        byte[] imgData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(pixels, 0, imgData, 0, pixels.length);
        return image;
    }
}