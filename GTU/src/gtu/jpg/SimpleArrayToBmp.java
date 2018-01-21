package gtu.jpg;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang.ArrayUtils;

public class SimpleArrayToBmp {
    public static void main(String[] args) throws IOException {
        int[][] sources = new int[5][5];
        sources[1][3] = 1;
        sources[2][2] = 1;
        sources[4][1] = 1;

        try {
            buildImage(sources, new File("d:/image.bmp"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        int[][] compares = getImageFromBmp(new File("d:/image.bmp"));
        System.out.println(ArrayUtils.isEquals(sources, compares));
    }

    private static int[][] getImageFromBmp(File file) throws IOException {
        BufferedImage im = ImageIO.read(file);
        int[][] rtnVal = new int[im.getHeight()][im.getWidth()];
        for (int r = 0; r < im.getHeight(); r++) {
            for (int c = 0; c < im.getWidth(); c++) {
                int val = im.getRGB(c, r);
                if (val == Color.WHITE.getRGB()) {
                } else if (val == Color.BLACK.getRGB()) {
                    rtnVal[r][c] = 1;
                }
            }
        }
        return rtnVal;
    }

    private static void buildImage(int[][] sources, File file) throws IOException {
        final int height = sources.length;
        final int width = sources[0].length;
        BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                im.setRGB(c, r, Color.WHITE.getRGB());
                if (sources[r][c] == 1) {
                    im.setRGB(c, r, Color.BLACK.getRGB());
                }
            }
        }
        ImageIO.write(im, "bmp", file);
    }
}