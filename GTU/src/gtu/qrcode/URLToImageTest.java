package gtu.qrcode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class URLToImageTest {
    public static void main(String[] args) {
        BufferedImage buggimg = new BufferedImage(1200, 1200, BufferedImage.TYPE_INT_RGB);
        // 指定寫出的資料夾 可由設定檔設定 位置
        File outFule = new File("D:/Qrcode.png");
        // 建立QRCODE 相關設定。
        Hashtable<EncodeHintType, Object> hitmap = new Hashtable<EncodeHintType, Object>();
        QRCodeWriter qrwrite = new QRCodeWriter();
        hitmap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 建立圖片的繪版
        Graphics2D g2d = buggimg.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 1200, 1200);
        g2d.setColor(Color.black);
        try {
            // 取得圖片的 點 的大小 圖片大小設定 1200 1200 px s 圖片
            String writeUrl = "http://tw.yahoo.com";
            BitMatrix x = qrwrite.encode(writeUrl, BarcodeFormat.QR_CODE, 1200, 1200, hitmap);
            for (int i = 0; i < 1200; i++) {
                for (int j = 0; j < 1200; j++) {
                    if (x.get(i, j)) {
                        g2d.fillRect(i, j, 1, 1);
                    }
                }
            }
            // 寫出圖片..
            ImageIO.write(buggimg, "png", outFule);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}