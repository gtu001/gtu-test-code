package gtu.qrcode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import gtu.file.FileUtil;

public class QRCodeUtil {
    public static void main(String[] args) {
        try {
            String content = FileUtil.loadFromFile(new File("C:\\Users\\gtu00\\OneDrive\\Desktop\\做過的工具.txt"), "utf8");

            System.out.println("[[[" + content + "]]]");

            // String qrCodeData = "Hello World!";
            String qrCodeData = content;
            File filePath = new File(FileUtil.DESKTOP_PATH, "QRCode.png");

            QRCodeUtil.getInstance().createQRCode(qrCodeData, "png", filePath, 300);
            System.out.println("QR Code image created successfully!");

            System.out.println("Data read from QR Code: " + QRCodeUtil.getInstance().readQRCode(filePath));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("done...");
        }
    }

    private QRCodeUtil() {
    }

    private static final QRCodeUtil _INST = new QRCodeUtil();

    public static QRCodeUtil getInstance() {
        return _INST;
    }

    private Map getHintMap() {
        Map hintMap = new HashMap();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hintMap.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        return hintMap;
    }

    public void createQRCode(String qrCodeData, String subname, File filePath, int widthAndHeight) throws WriterException, IOException {
        int qrCodewidth = widthAndHeight;
        int qrCodeheight = widthAndHeight;
        BitMatrix matrix = new MultiFormatWriter().encode(qrCodeData, BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, getHintMap());
        MatrixToImageWriter.writeToFile(matrix, subname, filePath);
    }
    
    public void createQRCode(String qrCodeData, String subname, OutputStream filePath, int widthAndHeight) throws WriterException, IOException {
        int qrCodewidth = widthAndHeight;
        int qrCodeheight = widthAndHeight;
        BitMatrix matrix = new MultiFormatWriter().encode(qrCodeData, BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, getHintMap());
        MatrixToImageWriter.writeToStream(matrix, subname, filePath);
    }
    
    public String readQRCode(File filePath) throws FileNotFoundException, IOException, NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath)))));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, getHintMap());
        return qrCodeResult.getText();
    }
    
    public String readQRCode(InputStream inputStream) throws IOException, NotFoundException  {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(inputStream))));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, getHintMap());
        return qrCodeResult.getText();
    }
}
