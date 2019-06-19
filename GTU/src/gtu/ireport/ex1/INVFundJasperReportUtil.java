package gtu.ireport.ex1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class INVFundJasperReportUtil {
    @Value("${backfound.file.auth.group}")
    private String backfoundFileAuthGroup;

    @Value("${backfound.file.auth.name}")
    private String backfoundFileAuthName;

    @Value("${backfound.file.auth.password}")
    private String backfoundFileAuthPassword;

    public static void mergeFiles(byte[][] files, OutputStream result)
            throws IOException, DocumentException, com.lowagie.text.DocumentException {
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, result);
        document.open();
        com.itextpdf.text.pdf.PdfReader[] reader = new com.itextpdf.text.pdf.PdfReader[files.length];
        for (int i = 0; i < files.length; i++) {
            reader[i] = new com.itextpdf.text.pdf.PdfReader(files[i]);
            copy.addDocument(reader[i]);
            copy.freeReader(reader[i]);
            reader[i].close();
        }
        document.close();
    }

    private ByteArrayOutputStream printModelListToByteOutputStream(List<PrintModel> listPrint)
            throws Exception {
        ByteArrayOutputStream printFile = new ByteArrayOutputStream();
        List<byte[]> inputList = new ArrayList<>();
        for (PrintModel model : listPrint) {
            byte[] bytes = null;
            if (StringUtils.equals(model.getDocNo(), "DOC026") && StringUtils.isNotBlank(model.getFilePath())) {
                try {
                    SmbFile smbFile = new SmbFile("smb:" + StringUtils.replace(model.getFilePath(), "\\", "/"),
                            getDefaultAuth());
                    if (smbFile.exists()) {
                        try (InputStream input = smbFile.getInputStream()) {
                            bytes = IOUtils.toByteArray(input);
                        }
                    }
                } catch (Exception e) {
                    throw new Exception("DOC026取檔失敗");
                }
            } else {
                try (ByteArrayOutputStream outArr = new ByteArrayOutputStream()) {
                    JasperReportUtil.exportReport(model.getPrint(), outArr);
                    bytes = outArr.toByteArray();
                }
            }
            inputList.add(bytes);
        }
        byte[][] inputArr = inputList.toArray(new byte[inputList.size()][]);
        mergeFiles(inputArr, printFile);
        return printFile;
    }

    private ByteArrayInputStream toByteArrayInputStream(InputStream inputStream) throws IOException {
        return new ByteArrayInputStream(IOUtils.toByteArray(inputStream));
    }

    private String getValueYn(String value) {
        if (StringUtils.isNotBlank(value)) {
            return "✔";
        } else {
            return "";
        }
    }

    public PrintModel generatePrintModel(String docNo, Map<String, Object> appendMap) throws Exception {
        Map<String, Object> dataMap = new HashMap<>();
        ClassPathResource res = new ClassPathResource(String.format("report/%s/%s.jasper", docNo, docNo));
        if (res.exists() == false) {
            throw new Exception("報表" + docNo + "不存在!");
        }
        ClassPathResource bgImg = new ClassPathResource(String.format("report/%s/%s.jpg", docNo, docNo));
        dataMap.put("BG_IMG", toByteArrayInputStream(bgImg.getInputStream()));
        dataMap.put("PAGE", 0);

        if (appendMap != null) {
            dataMap.putAll(appendMap);
        }

        JasperPrint print = JasperReportUtil.changeFontGenReport(res.getInputStream(), dataMap);
        return new PrintModel(docNo, print);
    }

    public byte[] generateByteArray(String filename, List<PrintModel> listPrint) throws Exception {
        ByteArrayOutputStream printFile = printModelListToByteOutputStream(listPrint);

        String fileName = URLEncoder.encode(filename + ".pdf", "UTF-8");

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_PDF);
        header.set(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=" + fileName.replace(" ", "_"));
        header.setContentLength(printFile.size());

        return printFile.toByteArray();
    }

    private NtlmPasswordAuthentication getDefaultAuth() {
        String name = getAuthName();
        String pass = getAuthPass();
        return new NtlmPasswordAuthentication(null, name, pass);
    }

    private String getAuthName() {
        String authName = null;
        if (StringUtils.isNotBlank(backfoundFileAuthGroup)) {
            authName = backfoundFileAuthGroup + "\\" + backfoundFileAuthName;
        } else {
            authName = backfoundFileAuthName;
        }
        return authName;
    }

    private String getAuthPass() {
        return backfoundFileAuthPassword;
    }

    public static class PrintModel {
        private String docNo;
        private String filePath;
        private JasperPrint print;

        public PrintModel(String docNo, String filePath) {
            this.docNo = docNo;
            this.filePath = filePath;
        }

        public PrintModel(String docNo, JasperPrint print) {
            this.docNo = docNo;
            this.print = print;
        }

        public String getDocNo() {
            return docNo;
        }

        public void setDocNo(String docNo) {
            this.docNo = docNo;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public JasperPrint getPrint() {
            return print;
        }

        public void setPrint(JasperPrint print) {
            this.print = print;
        }
    }
}
