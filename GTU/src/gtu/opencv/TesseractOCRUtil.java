package gtu.opencv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;

import common.ReferenceConf;  
  
public class TesseractOCRUtil{  
    
    private static final String TESSERACT_EXE = "e:/apps/tesseract-Win64/tesseract.exe";
    private static final String TESSDATA_PREFIX = "E:/apps/tesseract-Win64/shared/";
  
    private static final String LANG_OPTION = "-l";  
    private static final String EOL = System.getProperty("line.separator");  
  
    /**  
     * @param imageFile  
     *            传入的图像文件  
     * @return 识别后的字符串  
     */  
    public static String recognizeText(File imageFile) throws Exception {  
        /**  
         * 设置输出文件的保存的文件目录  
         */  
        File outputFile = new File(imageFile.getParentFile(), "output");  
  
        StringBuffer strB = new StringBuffer();  
        
        ProcessBuilder proc = new ProcessBuilder();
        
        proc.command(ReferenceConf.TESSERACT_EXE, 
                "\"" + imageFile.getPath() + "\"",
                "\"" + outputFile.getPath() + "\""
                );
        
        proc.environment().put("TESSDATA_PREFIX", ReferenceConf.TESSDATA_PREFIX);
        
        Process  pro = proc.start();
        
        /*
        Process  pro = Runtime.getRuntime().exec(  
                         new String[]{  
//                            "C:/Program Files (x86)/Tesseract-OCR/tesseract.exe",  
                                 ReferenceConf.TESSERACT_EXE,
                            "\"" + imageFile.getPath() + "\"",  
                            "\"" + outputFile.getPath() + "\""}  
                         );  
        */
        
       int w = pro.waitFor();  
        if (w == 0) // 0代表正常退出  
        {  
            BufferedReader in = new BufferedReader(new InputStreamReader(  
                    new FileInputStream(outputFile.getAbsolutePath() + ".txt"),  
                    "UTF-8"));  
            String str;  
  
            while ((str = in.readLine()) != null)  
            {  
                strB.append(str).append(EOL);  
            }  
            in.close();  
        } else  
        {  
            String msg;  
            switch (w)  
            {  
                case 1:  
                    msg = "Errors accessing files. There may be spaces in your image's filename.";  
                    break;  
                case 29:  
                    msg = "Cannot recognize the image or its selected region.";  
                    break;  
                case 31:  
                    msg = "Unsupported image format.";  
                    break;  
                default:  
                    msg = "Errors occurred.";  
            }  
            
            StringWriter sw = new StringWriter();
            IOUtils.copy(pro.getErrorStream(), sw);
            System.out.println("-----------------------------------------------");
            System.out.println(sw);
            System.out.println("-----------------------------------------------");
            
            throw new RuntimeException(msg);  
        }  
        new File(outputFile.getAbsolutePath() + ".txt").delete();  
        return strB.toString(); //.replaceAll("\\s*", "");  
  
  
        
        
    }  
  
  
    public static void main(String[] args) {  
  
  
        try {  
            String result =  recognizeText(new File("E:/resImage.jpg"));  
            System.out.println(result);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
  
    }  
}  
