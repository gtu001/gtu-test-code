package gtu.qrcode.fuco;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.util.Base64Utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码生成google zxing
 * 
 * @author X-rapido
 * 
 */
public class QRCodeGenerater {

    public static void main(String[] args) throws WriterException {
        String content = "http://www.baidu.com";
        // 初始化LogoConfig
        LogoConfig lc = new LogoConfig();
        lc.setBorder(2);
        lc.setBorderColor(Color.white);
        lc.setLogoPart(5);

        // 初始化QRCodeConfig
        QRCodeConfig config = new QRCodeConfig();
        config.setHints(QRCodeGenerater.getDecodeHintType());// QRCode基礎參數
        config.setContent(content);// QRCode內容
        config.setLogoPath("E:\\lego.jpg");// Logo圖位置
        config.setLogoConfig(lc);// Logo圖參數
        config.setLogoFlg(true);// 是否加上Logo圖
        config.setPicType(QRCodeConfig.PicType_JPEG);// 產生圖片類型
        config.setColor4Code(Color.BLACK);
        config.setColor4Back(Color.WHITE);

        // try {
        // QRCodeGenerater.writeToFile(config);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        try {
            String result = QRCodeGenerater.writeToBase64(config);
            System.out.println("result:" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private QRCodeGenerater() {
    }

    public static QRCodeGenerater getInstance() {
        return new QRCodeGenerater();
    }

    public String processQRCode(String content) {
        // 初始化LogoConfig
        LogoConfig lc = new LogoConfig();
        lc.setBorder(2);
        lc.setBorderColor(Color.white);
        lc.setLogoPart(5);

        // 初始化QRCodeConfig
        QRCodeConfig config = new QRCodeConfig();
        config.setHints(QRCodeGenerater.getDecodeHintType());// QRCode基礎參數
        config.setContent(content);// QRCode內容
        config.setLogoPath("E:\\lego.jpg");// Logo圖位置
        config.setLogoConfig(lc);// Logo圖參數
        config.setLogoFlg(true);// 是否加上Logo圖
        config.setPicType(QRCodeConfig.PicType_JPEG);// 產生圖片類型
        config.setColor4Code(Color.BLACK);
        config.setColor4Back(Color.WHITE);

        try {
            return QRCodeGenerater.writeToBase64(config);
        } catch (Exception e) {
            throw new RuntimeException("processQRCode Err : " + e.getMessage(), e);
        }
    }

    /**
     * 在QRCode寫入Logo圖片
     * 
     * @param bim
     *            既有QRCode串流
     * @param logoPic
     *            寫入LOGO圖的實體位置
     * @param logoConfig
     *            Logo圖相關設定
     * @throws Exception
     *             异常上抛
     */
    private void addLogoQRCode(BufferedImage bim, File logoPic, LogoConfig logoConfig) throws Exception {
        try {
            BufferedImage image = bim;
            Graphics2D g = image.createGraphics();

            // 取得Logo圖
            BufferedImage logo = ImageIO.read(logoPic);

            // set logo大小
            int widthLogo = logo.getWidth(null) > image.getWidth() * 1 / logoConfig.getLogoPart() ? (image.getWidth() * 1 / logoConfig.getLogoPart()) : logo.getWidth(null),
                    heightLogo = logo.getHeight(null) > image.getHeight() * 1 / logoConfig.getLogoPart() ? (image.getHeight() * 1 / logoConfig.getLogoPart()) : logo.getWidth(null);

            // 計算Logo位置並把其放到政中心
            int x = (image.getWidth() - widthLogo) / 2;
            int y = (image.getHeight() - heightLogo) / 2;

            // 繪圖
            g.drawImage(logo, x, y, widthLogo, heightLogo, null);
            g.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
            g.setStroke(new BasicStroke(logoConfig.getBorder()));
            g.setColor(logoConfig.getBorderColor());
            g.drawRect(x, y, widthLogo, heightLogo);

            g.dispose();
            logo.flush();
            image.flush();

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 產生QRCode
     * 
     * @param iQrCodeConfig
     *            QRCode產生相關參數
     * @return BufferedImage
     * @throws Exception
     */
    public BufferedImage getQRCODEBufferedImage(QRCodeConfig iQrCodeConfig) throws Exception {
        // 初始化
        MultiFormatWriter multiFormatWriter = null;
        BitMatrix bm = null;
        BufferedImage image = null;
        try {
            multiFormatWriter = new MultiFormatWriter();

            // 參數順序：內容，QRCode，生成圖片寬度，生成圖片高度，QRCode參數
            bm = multiFormatWriter.encode(iQrCodeConfig.getContent(), BarcodeFormat.QR_CODE, iQrCodeConfig.getWidth(), iQrCodeConfig.getHeight(), iQrCodeConfig.getHints());

            int w = bm.getWidth();
            int h = bm.getHeight();
            image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

            // QRCode繪製
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    image.setRGB(x, y, bm.get(x, y) ? iQrCodeConfig.getColor4Code().getRGB() : iQrCodeConfig.getColor4Back().getRGB());
                }
            }

            // 是否加入logo圖
            if (iQrCodeConfig.isLogoFlg()) {
                // 加入logo圖
                this.addLogoQRCode(image, new File(iQrCodeConfig.getLogoPath()), iQrCodeConfig.getLogoConfig());
            }
        } catch (WriterException e) {
            throw e;
        }
        return image;
    }

    /**
     * 回傳QRCode基礎參數
     * 
     * @return Map<EncodeHintType, Object>
     */
    public static Map<EncodeHintType, Object> getDecodeHintType() {
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();

        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);// 檢核級別
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");// 內容編碼
        hints.put(EncodeHintType.MARGIN, 0);
        hints.put(EncodeHintType.MAX_SIZE, 500);
        hints.put(EncodeHintType.MIN_SIZE, 100);

        return hints;
    }

    public static void writeToFile(QRCodeConfig iQRCodeConfig) throws IOException {
        try {
            // 初始化Generater
            QRCodeGenerater theQRCodeGenerater = new QRCodeGenerater();

            // 產生QRCode
            BufferedImage bim = theQRCodeGenerater.getQRCODEBufferedImage(iQRCodeConfig);// 生成QRCode

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            File file = new File("D:/new.png");
            ImageIO.write(bim, iQRCodeConfig.getPicType(), file); // 图片写出
            Thread.sleep(500); // 缓冲
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String writeToBase64(QRCodeConfig iQRCodeConfig) throws IOException {
        String result = null;
        try {
            // 初始化Generater
            QRCodeGenerater theQRCodeGenerater = new QRCodeGenerater();

            // 產生QRCode
            BufferedImage bim = theQRCodeGenerater.getQRCODEBufferedImage(iQRCodeConfig);// 生成QRCode

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bim, iQRCodeConfig.getPicType(), baos);
            result = Base64Utils.encodeToString(baos.toByteArray());

            Thread.sleep(500); // 缓冲
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
