package gtu.opencv;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

public class HighGuiHelper {

    private static final HighGuiHelper _INST = new HighGuiHelper();

    static {
    }

    public static HighGuiHelper getInstance() {
        return _INST;
    }
    
    public void imshow(String title, String path) {
        try {
            FileInputStream in = new FileInputStream(path);
            BufferedImage bufImage = ImageIO.read(in);
            JFrame frame = new JFrame(title);
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            throw new RuntimeException("imshow ERR : " + e.getMessage(), e);
        }
    }
    
    public void imshow(String title, Mat m) {
        HighGui.imshow(getPrefixTitle() + title, m);
        HighGui.waitKey();
    }

    public void imshow(String title, BufferedImage m) {
        try {
            HighGui.imshow(getPrefixTitle() + title, BufferedImage2Mat(m));
            HighGui.waitKey();
        } catch (IOException e) {
            throw new RuntimeException("imshow ERR : " + e.getMessage(), e);
        }
    }

    private String getPrefixTitle() {
        return "";
    }

    public Mat BufferedImage2Mat(BufferedImage image) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
    }

    public BufferedImage Mat2BufferedImage(Mat m) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }

    public BufferedImage matToBufferedImage(Mat matrix, BufferedImage bimg) {
        if (matrix != null) {
            int cols = matrix.width(); // matrix.cols();
            int rows = matrix.height(); // matrix.rows();
            System.out.println(cols + " " + rows);
            int elemSize = (int) matrix.elemSize();
            byte[] data = new byte[cols * rows * elemSize];
            int type;
            matrix.get(0, 0, data);
            switch (matrix.channels()) {
            case 1:
                type = BufferedImage.TYPE_BYTE_GRAY;
                break;
            case 3:
                type = BufferedImage.TYPE_3BYTE_BGR;
                // bgr to rgb
                byte b;
                for (int i = 0; i < data.length; i = i + 3) {
                    b = data[i];
                    data[i] = data[i + 2];
                    data[i + 2] = b;
                }
                break;
            default:
                return null;
            }

            // Reuse existing BufferedImage if possible
            if (bimg == null || bimg.getWidth() != cols || bimg.getHeight() != rows || bimg.getType() != type) {
                bimg = new BufferedImage(cols, rows, type);
            }
            bimg.getRaster().setDataElements(0, 0, cols, rows, data);
        } else { // mat was null
            bimg = null;
        }
        return bimg;
    }
}
