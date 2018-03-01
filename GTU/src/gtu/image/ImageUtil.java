package gtu.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import net.sf.image4j.codec.ico.ICODecoder;

public class ImageUtil {

    private static final ImageUtil _INST = new ImageUtil();

    private ImageUtil() {
    }

    public static ImageUtil getInstance() {
        return _INST;
    }

    public void showSupportImageType() {
        String[] types = ImageIO.getReaderFileSuffixes();
        System.out.println("This JRE supports image types:");
        for (String type : types) {
            System.out.println("Type: " + type);
        }
    }

    public void writeImageToFile(URL url, File file) throws IOException {
        BufferedImage img = ImageIO.read(url);
        String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        ImageIO.write(img, ext, file);
    }

    public Image getImageAutoChoice(String imagePath) {
        if (imagePath.endsWith(".ico")) {
            return getIcoImage(imagePath);
        }
        return getDefaultImage(imagePath);
    }

    public Image getIcoImage(String imagePath) {
        try {
            URL imgURL = this.getClass().getClassLoader().getResource(imagePath);
            List<BufferedImage> images = ICODecoder.read(imgURL.openStream());
            for (int ii = 0; ii < images.size(); ii++) {
                // System.out.println(String.format("%d - h:%d,w:%d", ii,
                // images.get(ii).getHeight(), images.get(ii).getWidth()));
            }
            BufferedImage image = images.get(0);
            Image imgData = image.getScaledInstance(32, -1, Image.SCALE_SMOOTH);
            BufferedImage bufferedImage = new BufferedImage(imgData.getWidth(null), imgData.getHeight(null), BufferedImage.TYPE_INT_RGB);
            bufferedImage.getGraphics().drawImage(imgData, 0, 0, null);
            return imgData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    public BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);// TYPE_INT_RGB

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public Image getDefaultImage(String imagePath) {
        URL imgURL = this.getClass().getClassLoader().getResource(imagePath);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image img = tk.getImage(imgURL);
        System.out.println("icon : " + img);
        return img;
    }

    public BufferedImage getBufferedImage(String resourceUrl) {
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(resourceUrl);
            BufferedImage bufferedImage = ImageIO.read(in);
            return bufferedImage;
        } catch (Exception ex) {
            throw new RuntimeException("getBufferedImage ERR : " + ex.getMessage(), ex);
        }
    }
}
