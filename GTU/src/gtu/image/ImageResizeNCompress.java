package gtu.image;

import gtu.file.FileUtil;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.ImageIcon;

import org.apache.commons.lang3.StringUtils;




public class ImageResizeNCompress {

    public static void main(String[] args)  {
        new ImageResizeNCompress().execute("xxxxx");
        System.out.println("done...");
    }
    
    public void execute(String dirName){
        File srcDir = new File(FileUtil.DESKTOP_DIR, dirName);
        File destDir = new File(FileUtil.DESKTOP_DIR, "壓縮後001");
        for(File f : srcDir.listFiles()){
            String name = f.getName();
            if(name.toLowerCase().endsWith(".jpg")){
                continue;
            }
            System.out.println("----" + name);
            name = name.substring(0, name.lastIndexOf(".")) + ".jpg";
            File renameToFile = new File(f.getParent(), name);
            int index = 0;
            while(renameToFile.exists()){
                String name1 = renameToFile.getName();
                name1 = name1.substring(0, name1.lastIndexOf(".")) + index + ".jpg";
                index ++;
                renameToFile = new File(renameToFile.getParent(), name1);
            }
            f.renameTo(renameToFile);
        }
        for(File f : srcDir.listFiles()){
            resizeImageCmmandline(f, new File(destDir, f.getName()));
        }
        for(File f : srcDir.listFiles()){
            System.out.println(f.getName() + "\t" + getImageInfo(f, new File(destDir, f.getName())));
        }
    }

    
    private String getImageInfo(File srcFile, File destFile){
        Image icon = new ImageIcon(FileUtil.getCanonicalPath(srcFile)).getImage();
        int height = icon.getHeight(null);
        int width = icon.getWidth(null);
        Image icon2 = new ImageIcon(FileUtil.getCanonicalPath(destFile)).getImage();
        int height2 = icon2.getHeight(null);
        int width2 = icon2.getWidth(null);
        String sizeStr = width + "x" + height;
        if(height!=height2 || width!=width2){
            sizeStr = sizeStr + "->" + width2 + "x" + height2;
        }
        String lengthStr = FileUtil.getSizeDescription(srcFile.length());
        String destLengthStr = FileUtil.getSizeDescription(destFile.length());
        if(StringUtils.equals(lengthStr, destLengthStr)){
            lengthStr = lengthStr + "->" + destLengthStr;
        }
        return sizeStr + "/" + lengthStr;
    }
    
    
    /**
     * 直接command,簡單好用
     */
    private void resizeImageCmmandline(File srcFile, File destFile){
        final int MAX_HEIGHT = 1050;
        String path = FileUtil.getCanonicalPath(srcFile);
        Image icon = new ImageIcon(path).getImage();
        int height = icon.getHeight(null);
        int width = icon.getWidth(null);
        BigDecimal big = new BigDecimal(height);
        big = big.divide(BigDecimal.valueOf(MAX_HEIGHT), 5, RoundingMode.HALF_UP);
        double differentDivide = big.doubleValue();
        System.out.println(differentDivide);
        int newWidth = (int) ((double) width / differentDivide);
        if (differentDivide > 1) {
            height = MAX_HEIGHT;
            width = newWidth;
        }
        if(!destFile.getParentFile().exists()){
            destFile.getParentFile().mkdirs();
        }
        try {
            String command = "cmd /c call \"I:/apps/ImageMagick-7.0.6-1-portable-Q16-x86/convert\" -quality %d -resize %s \"%s\" \"%s\" ";
            command = String.format(command, 50, width, srcFile, destFile);
            System.out.println(command);
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 效果很糟 , 未解決
     */
    /*
    static void resizeImage_forJavaApi(File file) throws MagickException {
        // String javaLibraryPath = System.getProperty ("java.library.path") +
        // ";D:/workspace/Gtu/WebContent/WEB-INF/lib;";
        // System.setProperty("java.library.path", javaLibraryPath);
        // if (System.getProperty("jmagick.systemclassloader") == null) {
        // System.setProperty("jmagick.systemclassloader", "no");
        // }

        final int MAX_HEIGHT = 1050;
        String path = FileUtil.getCanonicalPath(file);

        ImageInfo info = new ImageInfo(path);
//        info.setCompression(CompressionType.ZipCompression);
        System.out.println("getDensity = " + info.getDensity());
        System.out.println("getColorspace = " + info.getColorspace());
//        info.setDensity("300");
        
        
        Image icon = new ImageIcon(path).getImage();

        int height = icon.getHeight(null);
        int width = icon.getWidth(null);

        BigDecimal big = new BigDecimal(height);
        big = big.divide(BigDecimal.valueOf(MAX_HEIGHT), 5, RoundingMode.HALF_UP);
        double differentDivide = big.doubleValue();
        System.out.println(differentDivide);
        int newWidth = (int) ((double) width / differentDivide);
        if (differentDivide > 1) {
            height = MAX_HEIGHT;
            width = newWidth;
        }

        MagickImage image = new MagickImage(info);
        image = image.scaleImage(width, height);
        
        int orignColors = image.getNumberColors();
        System.out.println("orginal Colors: " + image.getNumberColors());
        System.out.println("orginal Depth: " + image.getDepth());
        System.out.println("orginal Colorspace: " + image.getColorspace());
        System.out.println("orginal Total colors " + image.getTotalColors());
        System.out.println("orginal Resolution units: " + image.getUnits());
        
        // clear all the metadata
//        image.profileImage("*", null);
        image.setImageFormat("jpg");
        
        image.setDepth(1);

        QuantizeInfo quantizeInfo = new QuantizeInfo();
        quantizeInfo.setColorspace(ColorspaceType.CMYKColorspace);//XYZColorspace
        quantizeInfo.setNumberColors(orignColors);
        quantizeInfo.setTreeDepth(1);
        quantizeInfo.setColorspace(0);
        quantizeInfo.setDither(0);

        image.quantizeImage(quantizeInfo);
        image.setCompression(CompressionType.ZipCompression);

        System.out.println("last Colors: " + image.getNumberColors());
        System.out.println("last Depth: " + image.getDepth());
        System.out.println("last Colorspace: " + image.getColorspace());
        System.out.println("last Total colors " + image.getTotalColors());
        System.out.println("last Resolution units: " + image.getUnits());
        
        image.setFileName("C:/Users/Troy/Desktop/imperialist_#.jpg");
        image.writeImage(info);
    }
    */
}
