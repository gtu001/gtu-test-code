package gtu.ireport.ex1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.fill.JRTemplatePrintText;
import net.sf.jasperreports.engine.fill.JRTemplateText;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

public class JasperReportUtil {

    private static Logger logger = LoggerFactory.getLogger(JasperReportUtil.class);

    private static final String FONT_LIB_PATH = "fonts/"; // TODO

    /**
     * change report JasperPrint's font and gen report to FileOutputStream
     * 
     * @param mainReport
     *            : main report file(jasper) path to gen JasperPrint
     * @param output
     *            : report output file path to gen FileOutputStream
     * @param params
     *            : report params to gen JasperPrint
     */
    public static void changeFontGenReport(String mainReport, String output, Map<String, Object> params) {
        changeFontGenReport(mainReport, output, params, null);
    }

    /**
     * change report JasperPrint's font and gen report to FileOutputStream
     * 
     * @param mainReport
     *            : main report file(jasper) path to gen JasperPrint
     * @param output
     *            : report output file path to gen FileOutputStream
     * @param params
     *            : report params to gen JasperPrint
     * @param defultFont
     *            : default font name for change font
     */
    public static void changeFontGenReport(String mainReport, String output,
            Map<String, Object> params, String defultFont) {
        InputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(mainReport);
            os = new FileOutputStream(output);
            changeFontGenReportPassEncode(is, os, params, defultFont);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    // fpn std, all encode
    /**
     * get add passwrod's report InputStream
     * 
     * @param is
     *            : report InputStream
     * @param params
     *            : report params to gen JasperPrint
     * @param loginUser
     *            : login AdmUsers
     * @param cusIdn
     *            : String to [treat] as password(as trim first 2 char)
     * @return
     */
    public static InputStream genReportToStream(InputStream is, Map<String, Object> params, String loginUser,
            String cusIdn) {
        InputStream res = null;
        try {
            // jasperPrint
            JasperPrint jasperPrint = genJasperPrint(is, params);

            // 轉換 jasper 中所有字型
            changeFont(jasperPrint, null);

            // export report by JRPdfExporter, default encode
            res = genReportInputStream(jasperPrint, true, loginUser, cusIdn);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            closeStream(is);
        }
        return res;
    }

    /**
     * change report JasperPrint's font,add report passwrod and gen report to
     * FileOutputStream
     * 
     * @param is
     *            : report InputStream
     * @param output
     *            : report output file path to gen FileOutputStream
     * @param params
     *            : report params to gen JasperPrint
     * @param loginUser
     *            : login AdmUsers
     * @param cusIdn
     *            : String to [treat] as password(as trim first 2 char)
     */
    public static void changeFontGenReport(InputStream is, String output,
            Map<String, Object> params, String loginUser, String cusIdn) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(output);
            changeFontGenReport(is, os, params, null, true, loginUser, cusIdn);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static void changeFontGenReport(InputStream is, FileOutputStream os,
            Map<String, Object> params, String loginUser, String cusIdn) {
        try {
            changeFontGenReport(is, os, params, null, true, loginUser, cusIdn);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    // private method
    private static void changeFontGenReport(InputStream is, OutputStream os, Map<String, Object> params,
            String defultFont, boolean isEncode, String loginUser, String cusIdn) {

        try {
            // jasperPrint
            JasperPrint jasperPrint = genJasperPrint(is, params);

            // 轉換 jasper 中所有字型
            changeFont(jasperPrint, defultFont);

            // export report by JRPdfExporter
            exportReport(jasperPrint, os, isEncode, loginUser, cusIdn);
        } catch (Exception e) {
            logger.error(e.getMessage(), new RuntimeException(e));
            throw new RuntimeException(e);
        } finally {
            closeStream(is);
            closeStream(os);
        }
    }

    public static JasperPrint changeFontGenReport(InputStream is, Map<String, Object> params) {
        JasperPrint jasperPrint = null;
        try {
            // jasperPrint
            jasperPrint = genJasperPrint(is, params);

            // 轉換 jasper 中所有字型
            changeFont(jasperPrint, null);

        } catch (Exception e) {
            logger.error(e.getMessage(), new RuntimeException(e));
            throw new RuntimeException(e);
        } finally {
            closeStream(is);
        }

        return jasperPrint;
    }

    public static JasperPrint mergeJasperPrint(List<JasperPrint> listPrint) {
        if (listPrint.size() == 0) {
            return null;
        }
        if (listPrint.size() == 1) {
            return listPrint.get(0);
        }

        JasperPrint mainPrint = listPrint.get(0);
        for (int i = 1; i < listPrint.size(); i++) {
            JasperPrint pt = listPrint.get(i);
            List<JRPrintPage> pages = pt.getPages();
            for (JRPrintPage jrp : pages) {
                mainPrint.addPage(jrp);
            }
        }

        return mainPrint;
    }

    private static void changeFontGenReportPassEncode(InputStream is, FileOutputStream os, Map<String, Object> params,
            String defultFont) {
        try {
            changeFontGenReport(is, os, params, defultFont, false, null, null);// pass encode
        } catch (Exception e) {
            logger.error(e.getMessage(), new RuntimeException(e));
            throw new RuntimeException(e);
        }
    }

    public static JasperPrint exportReport(JasperPrint jasperPrint, OutputStream os) {
        JasperPrint res = null;
        try {
            // export report by JRPdfExporter
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));

            // fpn std setting : option by isEncode
            loadJRPdfExporterSetting(exporter, false, null, null);

            exporter.exportReport();
        } catch (JRException e) {
            logger.error(e.getMessage(), new RuntimeException(e));
        } finally {
            closeStream(os);
        }
        return res;
    }

    private static JasperPrint exportReport(JasperPrint jasperPrint, OutputStream os, boolean isEncode,
            String loginUser, String cusIdn) {
        JasperPrint res = null;
        try {
            // export report by JRPdfExporter
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));

            // fpn std setting : option by isEncode
            loadJRPdfExporterSetting(exporter, isEncode, loginUser, cusIdn);

            exporter.exportReport();
        } catch (JRException e) {
            logger.error(e.getMessage(), new RuntimeException(e));
        } finally {
            closeStream(os);
        }
        return res;
    }

    private static JasperPrint genJasperPrint(InputStream is, Map<String, Object> params) {
        JasperPrint res = null;
        try {
            res = JasperFillManager.fillReport(is, params, new JREmptyDataSource());
        } catch (JRException e) {
            logger.error(e.getMessage(), new RuntimeException(e));
        } finally {
            closeStream(is);
        }
        return res;
    }

    private static InputStream genReportInputStream(JasperPrint jasperPrint, boolean isEncode, String loginUser,
            String cusIdn) {
        ByteArrayInputStream res = null;
        ByteArrayOutputStream bos = null;
        SimpleOutputStreamExporterOutput output = null;
        try {
            bos = new ByteArrayOutputStream();
            output = new SimpleOutputStreamExporterOutput(bos);

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(output);

            // exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            // exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, bos);

            // fpn std setting : option by isEncode
            loadJRPdfExporterSetting(exporter, isEncode, loginUser, cusIdn);

            exporter.exportReport();
            // set ByteArrayInputStream by bos
            byte[] data = bos.toByteArray();
            res = new ByteArrayInputStream(data);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeStream(bos);
            if (output != null) {
                try {
                    output.close();
                } catch (Exception e) {

                }
            }
        }

        return res;
    }

    private static void loadJRPdfExporterSetting(JRPdfExporter exporter, boolean isEncode, String loginUser,
            String cusIdn) {
        if (isEncode) {
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            exporter.setConfiguration(configuration);
            // IS_ENCRYPTED：是否加密PDF文件，值為true或false。
            configuration.setEncrypted(true);
            // IS_128_BIT_KEY：使用預設的40 bits的key或是128 bits的key來加密，值為true或false。
            configuration.set128BitKey(true);
            // PERMISSIONS：設定一般使用者的權限，例如允許列印、允許複製內文等等，值為整數。
            configuration.setPermissions(new Integer(PdfWriter.ALLOW_PRINTING));
            // USER_PASSWORD：設定一般使用者的密碼，值為字串。
            configuration.setUserPassword(PdfPwdUtil.getPdfUserPasswordByCusIdn(cusIdn));
            // OWNER_PASSWORD：設定檔案擁有者的密碼，值為字串。
            configuration.setOwnerPassword(PdfPwdUtil.getPdfOwnerPassword(loginUser));
        }
    }

    /**
     * 處理PDF中難字的部份
     * 
     * @param element
     */
    private static void convertRareWordsToStyledString(JRTemplatePrintText element) {
        StringBuilder sb = new StringBuilder();
        char[] charArr = element.getOriginalText().toCharArray();
        for (int i = 0; i < charArr.length; i++) {
            char c = charArr[i];
            if (c >= 0xE000 && c <= 0xF6B0) {
                sb.append("<style ").append("pdfFontName=\"")
                    .append(JasperReportUtil.FONT_LIB_PATH).append("EUDC.TTF\">")
                    .append(c).append("</style>");
            } else {
                sb.append(c);
            }
        }

        element.setText(sb.toString());
    }

    private static void changeFont(JasperPrint jasperPrint, String defultFont) {
        List<JRPrintPage> pages = jasperPrint.getPages();
        for (int i = 0, n = pages.size(); i < n; i++) {
            List<JRPrintElement> elements = ((JRPrintPage) pages.get(i)).getElements();
            for (Iterator<JRPrintElement> ei = elements.iterator(); ei.hasNext();) {
                Object obj = ei.next();
                if (obj instanceof JRTemplatePrintText) {
                    JRTemplatePrintText element = (JRTemplatePrintText) obj;
                    JRTemplateText template = (JRTemplateText) element.getTemplate();

                    // 難字處理
                    convertRareWordsToStyledString(element);

                    // get fonr tff by font name
                    String elemPdfFontName = element.getPdfFontName();
                    String tempFontName = template.getFontName();
                    String fontTtf = getFontTtf(elemPdfFontName, tempFontName, defultFont);

                    // template set font name and encodeing
                    if (StringUtils.isNotEmpty(fontTtf)) {
                        template.setPdfFontName(JasperReportUtil.FONT_LIB_PATH + fontTtf);
                        template.setPdfEncoding("Identity-H");
                    }
                }
            }
        }
    }

    private static String getFontTtf(String elemPdfFontName, String tempFontName, String defultFont) {
        String res = null;
        if (("MHei-Medium").equals(elemPdfFontName)
                || ("微軟正黑體").equals(tempFontName) || ("微軟正黑體").equals(defultFont)
                || ("新細明體").equals(tempFontName) || ("新細明體").equals(defultFont)) {// 微軟正黑體
            res = "msjh.ttf";
        } else if (("MHei-MediumB").equals(elemPdfFontName)) {// 微軟正黑體粗體
            res = "msjhbd.ttf";
        } else if (("MSung-Light").equals(elemPdfFontName) || ("標楷體").equals(tempFontName)
                || ("DFKai-SB").equals(tempFontName) || ("標楷體").equals(defultFont)) {// 標楷體
            res = "kaiu.ttf";
        }
        return res;
    }

    private static void closeStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void closeStream(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}