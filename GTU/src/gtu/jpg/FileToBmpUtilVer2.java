package gtu.jpg;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import gtu.file.FileUtil;

public class FileToBmpUtilVer2 {
    public static void main(String[] args) throws IOException {
        FileToBmpUtilVer2 t = FileToBmpUtilVer2.getInstance();

        int width = t.getWidth(new File("E:/my_tool/EnglishSearchUI/EnglishSearchUI.exe"));
        int fileLength = t.buildImageFromFile(new File("E:/my_tool/EnglishSearchUI/EnglishSearchUI.exe"), new File(FileUtil.DESKTOP_DIR, "ttttt.bmp"), true, width);
        System.out.println(fileLength);

        t.getFileFromImage_FixName(new File(FileUtil.DESKTOP_DIR, "ttttt_7687994.bmp"), //
                new File("C:/Users/gtu00/OneDrive/Desktop/xxxxx.jar"));

        System.out.println("done...");
    }

    private static final FileToBmpUtilVer2 _INST = new FileToBmpUtilVer2();

    public static FileToBmpUtilVer2 getInstance() {
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

    /**
     * 建立Image檔案
     */
    public int buildImageFromFile(File fromFile, File toBmpFile, boolean fixFileName, int width) {
        try {
            byte[] bs = FileUtil.loadFileToByte(fromFile);

            BigDecimal val = new BigDecimal(bs.length);
            int height = val.divide(new BigDecimal(width), BigDecimal.ROUND_UP).intValue();

            BufferedImage image = RawImageUtil.byte2Buffered(bs, width, height);

            if (fixFileName) {
                Pattern ptn = Pattern.compile("(.*)(\\.bmp)");
                Matcher mth = ptn.matcher(toBmpFile.getName());
                mth.find();
                toBmpFile = new File(toBmpFile.getParentFile(), mth.group(1) + "_" + bs.length + ".bmp");
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
    public void getFileFromImage(File fromImageFile, int fileLength, File toFile) {
        try {
            BufferedImage image = ImageIO.read(fromImageFile);
            byte[] imgData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

            byte[] newArry = new byte[fileLength];
            System.arraycopy(imgData, 0, newArry, 0, newArry.length);

            FileUtil.saveToFile(toFile, newArry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 將Image轉回檔案
     */
    public void getFileFromImage_FixName(File fromImageFile, File toFile) {
        Pattern ptn = Pattern.compile(".*\\_(\\d+)\\.bmp");
        Matcher mth = ptn.matcher(fromImageFile.getName());
        mth.find();
        int fileSize = Integer.parseInt(mth.group(1));

        getFileFromImage(fromImageFile, fileSize, toFile);
    }
}