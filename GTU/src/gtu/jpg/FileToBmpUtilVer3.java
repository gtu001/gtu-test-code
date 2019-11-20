package gtu.jpg;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferShort;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import gtu.file.FileUtil;

public class FileToBmpUtilVer3 {
    public static void main(String[] args) throws IOException {
        FileToBmpUtilVer3 t = FileToBmpUtilVer3.getInstance();

        int width = t.getWidth(new File(gtu.file.FileUtil.DESKTOP_DIR, "AlwaysOpenUI.exe"));
        int fileLength = t.buildImageFromFile(new File(gtu.file.FileUtil.DESKTOP_DIR, "AlwaysOpenUI.exe"), new File(gtu.file.FileUtil.DESKTOP_DIR, "ttttt.bmp"), false, width);
        System.out.println(fileLength);

        t.getFileFromImage(new File(gtu.file.FileUtil.DESKTOP_DIR, "ttttt.bmp"), //
                new File(gtu.file.FileUtil.DESKTOP_DIR, "AlwaysOpenUI____2.exe"));

        System.out.println("done...");
    }

    private static final FileToBmpUtilVer3 _INST = new FileToBmpUtilVer3();

    public static FileToBmpUtilVer3 getInstance() {
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
            return bs;
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

            // byte[] newArry = new byte[imgData.length - 1];
            // System.arraycopy(imgData, 0, newArry, 0, newArry.length);
            byte[] newArry = ImageByteArryHeaderHandler.getBytesFromImg(imgData);

            FileUtil.saveToFile(toFile, newArry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static class RawImageUtil {
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

    private static class FileUtil {
        private static byte[] loadFileToByte(File file) throws IOException {
            byte[] arrayOfByte = new byte[4096];
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int i;
            while ((i = input.read(arrayOfByte, 0, arrayOfByte.length)) != -1) {
                baos.write(arrayOfByte, 0, i);
            }
            baos.close();
            input.close();
            return baos.toByteArray();
        }

        private static void saveToFile(File file, byte[] data) {
            try {
                // 廢棄
                // FileOutputStream out = new FileOutputStream(file);
                // out.write(data);
                // out.close();
                BufferedInputStream buffIn = new BufferedInputStream(new ByteArrayInputStream(data));
                BufferedOutputStream buffOut = new BufferedOutputStream(new FileOutputStream(file));
                byte[] arr = new byte[1024 * 1024];
                int available = -1;
                while ((available = buffIn.read(arr)) > 0) {
                    buffOut.write(arr, 0, available);
                }
                buffOut.flush();
                buffOut.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private static class ByteUtils {
        public static byte[] longToBytes(long l) {
            byte[] result = new byte[8];
            for (int i = 7; i >= 0; i--) {
                result[i] = (byte) (l & 0xFF);
                l >>= 8;
            }
            return result;
        }

        public static long bytesToLong(byte[] b) {
            long result = 0;
            for (int i = 0; i < 8; i++) {
                result <<= 8;
                result |= (b[i] & 0xFF);
            }
            return result;
        }
    }
}