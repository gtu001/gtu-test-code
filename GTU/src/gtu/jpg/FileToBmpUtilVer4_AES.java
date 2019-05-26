package gtu.jpg;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import gtu.binary.EncryptAndDecryptHandler;
import gtu.file.FileUtil;

public class FileToBmpUtilVer4_AES {
    
    private static EncryptAndDecryptHandler encryptAndDecryptHandler = new EncryptAndDecryptHandler("EVIL_GTU001_GOGO");
    
    public static void main(String[] args) throws IOException {
        FileToBmpUtilVer4_AES t = FileToBmpUtilVer4_AES.getInstance();

        int width = t.getWidth(new File(FileUtil.DESKTOP_DIR, "AlwaysOpenUI.exe"));
        int fileLength = t.buildImageFromFile(new File(FileUtil.DESKTOP_DIR, "AlwaysOpenUI.exe"), new File(FileUtil.DESKTOP_DIR, "ttttt.bmp"), false, width);
        System.out.println(fileLength);

        t.getFileFromImage(new File(FileUtil.DESKTOP_DIR, "ttttt.bmp"), //
                new File(FileUtil.DESKTOP_DIR, "AlwaysOpenUI____2.exe"));

        System.out.println("done...");
    }

    private static final FileToBmpUtilVer4_AES _INST = new FileToBmpUtilVer4_AES();

    public static FileToBmpUtilVer4_AES getInstance() {
        return _INST;
    }

    /**
     * 取得最適合寬度
     */
    public int getWidth(File file) {
        BigDecimal val = new BigDecimal(Math.sqrt(file.length()));
        System.out.println(val);
        val = val.setScale(0, BigDecimal.ROUND_UP);
        return val.intValue();
    }

    private static class ImageByteArryHeaderHandler {
        private static byte[] toImageBytes(byte[] bs) {
            System.out.println("[toImageBytes] orign size : " + bs.length);
            
            bs = encryptAndDecryptHandler.encrypt(bs);
            
            byte[] bsAndLengthMark = new byte[bs.length + 8];
            byte[] marks = ByteUtils.longToBytes(bs.length);
            
            System.arraycopy(marks, 0, bsAndLengthMark, 0, 8);
            System.arraycopy(bs, 0, bsAndLengthMark, 8, bs.length);
            System.out.println("[toImageBytes] fix size : " + bsAndLengthMark.length);
            
            return bsAndLengthMark;
        }

        private static byte[] getBytesFromImg(byte[] bsAndLengthMark) {
            System.out.println("[getBytesFromImg] fix size : " + bsAndLengthMark.length);
            byte[] marks = new byte[8];
            System.arraycopy(bsAndLengthMark, 0, marks, 0, 8);
            int length = (int) ByteUtils.bytesToLong(marks);
            byte[] bs = new byte[length];
            System.arraycopy(bsAndLengthMark, 8, bs, 0, bs.length);
            System.out.println("[getBytesFromImg] orign size : " + bs.length);
            return encryptAndDecryptHandler.decrypt(bs);
        }
    }

    /**
     * 建立Image檔案
     */
    public int buildImageFromFile(File fromFile, File toBmpFile, boolean fixFileName, int width) {
        try {
            byte[] bs = FileUtil.loadFileToByte(fromFile);
            int orignLength = bs.length;
            bs = ImageByteArryHeaderHandler.toImageBytes(bs);

            BigDecimal val = new BigDecimal(bs.length);
            int height = val.divide(new BigDecimal(width), BigDecimal.ROUND_UP).intValue();

            BufferedImage image = RawImageUtil.byte2Buffered(bs, width, height);

            if (fixFileName) {
                Pattern ptn = Pattern.compile("(.*)(\\.bmp)");
                Matcher mth = ptn.matcher(toBmpFile.getName());
                mth.find();
                toBmpFile = new File(toBmpFile.getParentFile(), mth.group(1) + "_" + orignLength + ".bmp");
            }
            
            ImageIO.write(image, "bmp", toBmpFile);
            return bs.length;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 將Image轉回檔案
     */
    public void getFileFromImage(File fromImageFile, File toFile) {
        try {
            BufferedImage image = ImageIO.read(fromImageFile);
            byte[] imgData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            
//            byte[] newArry = new byte[imgData.length - 1];
//            System.arraycopy(imgData, 0, newArry, 0, newArry.length);
            byte[] newArry = ImageByteArryHeaderHandler.getBytesFromImg(imgData);

            FileUtil.saveToFile(toFile, newArry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}