package com.example.gtu001.qrcodemaker.common;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;



public class QRCodeUtil {

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

    /**
    * 不支援android
     */
    @Deprecated
    public void createQRCode(String qrCodeData, String subname, File filePath, int widthAndHeight) throws WriterException, IOException {
        int qrCodewidth = widthAndHeight;
        int qrCodeheight = widthAndHeight;
        BitMatrix matrix = new MultiFormatWriter().encode(qrCodeData, BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, getHintMap());
        MatrixToImageWriter.writeToFile(matrix, subname, filePath);
    }

    /**
     * 不支援android
     */
    @Deprecated
    public void createQRCode(String qrCodeData, String subname, OutputStream filePath, int widthAndHeight) throws WriterException, IOException {
        int qrCodewidth = widthAndHeight;
        int qrCodeheight = widthAndHeight;
        BitMatrix matrix = new MultiFormatWriter().encode(qrCodeData, BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, getHintMap());
        MatrixToImageWriter.writeToStream(matrix, subname, filePath);
    }

    public String readQRCode(File filePath) throws FileNotFoundException, IOException, NotFoundException {
        Bitmap bitmap = OOMHandler.new_decode(new FileInputStream(filePath));
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new RGBLuminanceSource(bitmap)));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, getHintMap());
        return qrCodeResult.getText();
    }

    public String readQRCode(InputStream inputStream) throws IOException, NotFoundException {
        Bitmap bitmap = OOMHandler.new_decode(inputStream);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new RGBLuminanceSource(bitmap)));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, getHintMap());
        return qrCodeResult.getText();
    }
}
