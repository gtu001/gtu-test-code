package gtu.ocr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import gtu.image.ImageUtil;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.util.LoadLibs;

public class TesseractHelper {

    public static void main(String[] args) throws IOException {
        BufferedImage bufferImage = ImageUtil.getInstance().getBufferedImage("gtu/ocr/Tesseract_test_chsTw.png");
        String result = TesseractHelper.newInstance()//
                .language("chi_tra").languageFile(new File("E:/workstuff/workspace/gtu-test-code/chi_tra.traineddata"))//
                .bufferedImage(bufferImage).generateOCRString();
        System.out.println("result = " + result);
        System.out.println("done...");
    }

    private static TesseractHelper newInstance() {
        return new TesseractHelper();
    }

    private TesseractHelper() {
    }

    private BufferedImage bufferedImage;
    private File imageFile;
    private File languageFile;
    private ITesseract instance = new Tesseract();// JNA Interface Mapping
    private Tesseract instance2 = new Tesseract();// JNA Direct Mapping
    private Tesseract1 instance4 = new Tesseract1(); // JNA Direct Mapping
    private String language;

    public TesseractHelper bufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        return this;
    }

    public TesseractHelper imageFile(File imageFile) {
        this.imageFile = imageFile;
        return this;
    }

    public TesseractHelper language(String language) {
        this.language = language;
        return this;
    }

    public TesseractHelper languageFile(File languageFile) {
        this.languageFile = languageFile;
        return this;
    }

    public enum Language {
        eng, //
        chi_sim, //
        chi_tram//
        ;//
    }

    public String generateOCRString() {
        /**
         * You either set your own tessdata folder with your custom language
         * pack or use LoadLibs to load the default tessdata folder for you.
         **/
        // 語系檔放在  https://github.com/tesseract-ocr/tessdata
        File tessDir = LoadLibs.extractTessResources("tessdata");
        System.out.println("Datapath : " + tessDir);
        instance.setDatapath(tessDir.getParent());
        if (!tessDir.exists()) {
            tessDir.mkdirs();
        }

        if (language != null) {
            instance.setLanguage(language);
        }

        if (languageFile != null) {
            try {
                File languageDestFile = new File(tessDir, language + ".traineddata");
                if (!languageDestFile.exists()) {
                    FileUtils.copyFile(languageFile, languageDestFile);
                }
                if (!languageDestFile.exists()) {
                    throw new Exception(languageDestFile + " not found!");
                }
            } catch (Exception e1) {
                throw new RuntimeException("語系檔複製失敗!", e1);
            }
        }

        try {
            // this.copyLanguage(language, tessDir);
            /**
             * HOCR | Set the HOCR option in order to get the desired result
             * from the doOCR method.
             **/
            instance2.setHocr(true);

            String result = null;

            if (bufferedImage != null) {
                result = instance.doOCR(bufferedImage);
            } else if (imageFile != null) {
                result = instance.doOCR(imageFile);
            } else {
                System.err.println("請設定來源圖檔 !");
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("generateOCRString ERR : " + e.getMessage(), e);
        }
    }
}