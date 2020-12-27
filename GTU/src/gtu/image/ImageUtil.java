package gtu.image;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileSystemView;

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

    public Image getIcoImage(String resourcePath) {
        try {
            URL imgURL = this.getClass().getClassLoader().getResource(resourcePath);
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
        InputStream in = null;
        try {
            in = this.getClass().getClassLoader().getResourceAsStream(resourceUrl);
            BufferedImage bufferedImage = ImageIO.read(in);
            return bufferedImage;
        } catch (Exception ex) {
            throw new RuntimeException("getBufferedImage ERR : " + ex.getMessage(), ex);
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public BufferedImage getBufferedImage(File file) {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            BufferedImage bufferedImage = ImageIO.read(in);
            return bufferedImage;
        } catch (Exception ex) {
            throw new RuntimeException("getBufferedImage ERR : " + ex.getMessage(), ex);
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public BufferedImage resizeImageByIndicateWH(BufferedImage bimg, boolean isMatchHeight, int matchLength) {
        int width = bimg.getWidth();
        int height = bimg.getHeight();
        if (matchLength <= 0) {
            throw new RuntimeException("長度不可小於零:" + matchLength);
        }
        double rate = 1;
        if (isMatchHeight) {
            rate = ((double) matchLength) / ((double) height);
        } else {
            rate = ((double) matchLength) / ((double) width);
        }
        width = (int) (width * rate);
        height = (int) (height * rate);
        return resizeImage2(bimg, width, height);
    }

    public static BufferedImage resizeImage2(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return dimg;
    }

    public Image resizeImage(BufferedImage img, int width, int height) {
        Image dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return dimg;
    }

    /**
     * 這是*.ICO 執行檔圖示 在用
     * 
     * @param image
     * @return
     */
    public Image getIcoImage(BufferedImage image) {
        try {
            Image imgData = image.getScaledInstance(32, -1, Image.SCALE_SMOOTH);
            BufferedImage bufferedImage = new BufferedImage(imgData.getWidth(null), imgData.getHeight(null), BufferedImage.TYPE_INT_RGB);
            bufferedImage.getGraphics().drawImage(imgData, 0, 0, null);
            return imgData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public javafx.scene.image.WritableImage getImageForJavaFx(BufferedImage capture) {
        return javafx.embed.swing.SwingFXUtils.toFXImage(capture, null);
    }

    public void showImage(BufferedImage bufferedImage) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JLabel(new ImageIcon(bufferedImage)));
        frame.setLocationRelativeTo(null);
        frame.pack();
        gtu.swing.util.JFrameUtil.setVisible(true, frame);
    }

    public void showImage(Icon icon) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JLabel(icon));
        frame.setLocationRelativeTo(null);
        frame.pack();
        gtu.swing.util.JFrameUtil.setVisible(true, frame);
    }

    public Icon imageToIcon(Image image) {
        return new ImageIcon(image);
    }

    public Image iconToImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon) icon).getImage();
        } else {
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            BufferedImage image = gc.createCompatibleImage(w, h);
            Graphics2D g = image.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
            return image;
        }
    }

    public Icon getIconFromExe(File file) {
        try {
            int opt = 1;
            Icon icon = null;
            switch (opt) {
            case 1:
                sun.awt.shell.ShellFolder sf = sun.awt.shell.ShellFolder.getShellFolder(file);
                icon = new ImageIcon(sf.getIcon(true));
                break;
            case 2:
                icon = FileSystemView.getFileSystemView().getSystemIcon(file);
                break;
            }
            return icon;
        } catch (Exception e) {
            throw new RuntimeException("getIconFromExe ERR : " + e.getMessage(), e);
        }
    }

    public BufferedImage createTransparentImage(final int width, final int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public Icon createTransparentIcon(final int width, final int height) {
        return new ImageIcon(createTransparentImage(width, height));
    }

    public static void main(String[] args) {
    }
}
