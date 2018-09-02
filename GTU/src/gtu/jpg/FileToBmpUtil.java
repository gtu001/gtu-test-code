package gtu.jpg;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang.ArrayUtils;

import gtu.file.FileUtil;

public class FileToBmpUtil {
    public static void main(String[] args) throws IOException {
        FileToBmpUtil t = FileToBmpUtil.getInstance();
        
        t.buildImageFromFile(new File("E:/my_tool/EnglishSearchUI/EnglishSearchUI.exe"), new File(FileUtil.DESKTOP_DIR, "ttttt.bmp"), t.getWidth(new File("E:/my_tool/EnglishSearchUI/EnglishSearchUI.exe")));
        t.getFileFromImage(new File(FileUtil.DESKTOP_DIR, "ttttt.bmp"), new File("C:/Users/gtu001/Desktop/xxxxx.jar"));
    }
    
    private static final FileToBmpUtil _INST = new FileToBmpUtil();
    public static FileToBmpUtil getInstance(){
        return _INST;
    }

    private void testCase() throws IOException {
        FileToBmpUtil t = new FileToBmpUtil();
        byte[] aa = new byte[256];
        for (int j = Byte.MIN_VALUE, i = 0; j <= Byte.MAX_VALUE; j++, i++) {
            aa[i] = (byte) j;
            if (j == Byte.MAX_VALUE) {
                break;
            }
        }

        Integer[][] vals = t.getIntArrayFromByteArray(aa, 50);
        debugArray("buildImg ", vals);

        buildImage(vals, new File("d:/test_bmp2.bmp"));

        Integer[][] newVals = t.getImageFromBmp(new File("d:/test_bmp2.bmp"));
        debugArray("imgToArry", newVals);

        System.out.println(ArrayUtils.isEquals(vals, newVals));

        byte[] bytes = t.getByteArrayFromIntArray(newVals);

        System.out.println(Arrays.toString(bytes));
    }
    
    /**
     * 取得最適合寬度
     */
    public int getWidth(File file){
        BigDecimal val = new BigDecimal(Math.sqrt(file.length()));
        System.out.println(val);
        val = val.setScale(0, BigDecimal.ROUND_UP);
        return val.intValue();
    }

    /**
     * 建立Image檔案
     */
    public void buildImageFromFile(File fromFile, File toBmpFile, int width) {
        try {
            byte[] bs = FileUtil.loadFileToByte(fromFile);
            Integer[][] vals = getIntArrayFromByteArray(bs, width);
            buildImage(vals, toBmpFile);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 將Image轉回檔案
     */
    public void getFileFromImage(File fromImageFile, File toFile) {
        try {
            Integer[][] newVals = getImageFromBmp(fromImageFile);
            byte[] bytes = getByteArrayFromIntArray(newVals);
            FileUtil.saveToFile(toFile, bytes);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void debugArray(String type, Integer[][] newVals) {
        for (int ii = 0; ii < newVals.length; ii++) {
            for (int jj = 0; jj < newVals[0].length; jj++) {
                System.out.println(type + " : " + ii + " ," + jj + " - " + newVals[ii][jj]);
            }
        }
    }

    private byte[] getByteArrayFromIntArray(Integer[][] array) {
        List<Byte> rtnList = new ArrayList<Byte>();
        for (int ii = 0; ii < array.length; ii++) {
            for (int jj = 0; jj < array[0].length; jj++) {
                Integer val = array[ii][jj];
                if (val != null) {
                    rtnList.add(Byte.valueOf(String.valueOf(val)));
                }
            }
        }
        return ArrayUtils.toPrimitive(rtnList.toArray(new Byte[0]));
    }

    private Integer[][] getIntArrayFromByteArray(byte[] bs, int width) {
        List<List<Integer>> intList = new ArrayList<List<Integer>>();
        List<Integer> arry = new ArrayList<Integer>();
        for (int ii = 0; ii < bs.length; ii++) {
            arry.add((int) bs[ii]);
            if (arry.size() == width) {
                intList.add(arry);
                arry = new ArrayList<Integer>();
            }
        }
        if (arry.size() != 0) {
            intList.add(arry);
        }
        Integer[][] rtnVal = new Integer[intList.size()][width];
        for (int ii = 0; ii < intList.size(); ii++) {
            for (int jj = 0; jj < width; jj++) {
                if (intList.get(ii).size() > jj) {
                    rtnVal[ii][jj] = intList.get(ii).get(jj);
                }
            }
        }
        return rtnVal;
    }

    private Integer[][] getImageFromBmp(File file) throws IOException {
        BufferedImage im = ImageIO.read(file);
        Integer[][] rtnVal = new Integer[im.getHeight()][im.getWidth()];
        for (int r = 0; r < im.getHeight(); r++) {
            for (int c = 0; c < im.getWidth(); c++) {
                Integer val = im.getRGB(c, r);// XXX 重要!!!
                if(val == -256){
                    val = null;
                }
                if(val != null && val < Byte.MIN_VALUE){
                    val = val - -16777216;
                }
                rtnVal[r][c] = val;
                
            }
        }
        return rtnVal;
    }

    private static void buildImage(Integer[][] sources, File file) throws IOException {
        final int height = sources.length;
        final int width = sources[0].length;
        BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (sources[r][c] != null) {
                    im.setRGB(c, r, sources[r][c]);
                }else{
                    im.setRGB(c, r, Integer.MAX_VALUE - 255);//null的預設值 XXX
                }
            }
        }
        ImageIO.write(im, "bmp", file);
    }
}