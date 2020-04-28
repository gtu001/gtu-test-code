package gtu.itext.html;

import java.io.File;
import java.io.IOException;

import org.dom4j.DocumentException;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;

import gtu.file.FileUtil;

public class Html2Pdf {

    public boolean convertHtmlToPdf(String inputFile, String outputFile) throws Exception {
        ConverterProperties properties = new ConverterProperties();
        com.itextpdf.layout.font.FontProvider fontProvider = new DefaultFontProvider();
        FontProgram fontProgram = FontProgramFactory.createFont("C:/Users/wistronits/Desktop/stsong/chinese.stsong.ttf");//宋體
        fontProvider.addFont(fontProgram);
        properties.setFontProvider(fontProvider);
        HtmlConverter.convertToPdf(new File(inputFile), new File(outputFile), properties);
        return true;
    }

    public boolean convertHtmlToPdf_fontDir(String inputFile, String outputFile) throws Exception {
        String FONTS = "src/main/resources/fonts/cardo/";
        ConverterProperties properties = new ConverterProperties();
        com.itextpdf.layout.font.FontProvider fontProvider = new DefaultFontProvider();
        fontProvider.addDirectory(FONTS); // 添加路径而不是添加字体
        properties.setFontProvider(fontProvider);
        HtmlConverter.convertToPdf(new File(inputFile), new File(outputFile), properties);
        return true;
    }

    /**
     * @param args
     * @throws DocumentException
     * @throws IOException
     */
    public static void main(String[] args) throws DocumentException, IOException {
        String inputFile = FileUtil.DESKTOP_PATH + "test.html";
        String outputFile = FileUtil.DESKTOP_PATH + "test.pdf";

        Html2Pdf html2Pdf = new Html2Pdf();
        try {
            html2Pdf.convertHtmlToPdf(inputFile, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}