package gtu.net.httpclient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;

import com.amazonaws.util.IOUtils;

import gtu.zip.UnZipBean;

public class HttpClientPostTest_bigFile {

    private static final Logger logger = Logger.getLogger(HttpClientPostTest_bigFile.class);

    public static void main(String[] args) {
    }
    
    public void receiveZipFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.warn("# receiveZipFile start !");
        try {
            File targetPath = new File("d:/"); // TODO
            
            // 收 Zip 檔
            List<FileItem> items = getMultipartFileList(request);

            // 沒有東西
            Iterator<FileItem> iter = items.iterator();
            if (!iter.hasNext()) {
                logger.error("ERR : receiveZipFile iter has no Next");
                throw new RuntimeException("receiveZipFile iter has no Next");
            }

            FileItem item = iter.next();
            // 不是表單欄位
            if (item.isFormField()) {
                logger.error("ERR : receiveZipFile item isFormField");
                throw new RuntimeException("receiveZipFile item isFormField");
            }

            InputStream fileStream = item.getInputStream();
            String savepath = targetPath.getAbsolutePath().concat("/");
            String zipfilename = savepath.concat(item.getName()); // 寫到存帳單的地方
            logger.warn(">> receiveBillFile filename:" + zipfilename);
            IOUtils.copy(fileStream, new FileOutputStream(zipfilename));

            // 解壓縮
            UnZipBean unzip = new UnZipBean(zipfilename, savepath);
            unzip.unzip();

            logger.warn(">> UnZipBean . unzip DONE ! ");

            // 刪除 zip 檔
            new File(zipfilename).delete();
        } catch (Exception ex) {
            logger.error("receiveZipFile ERROR : " + ex.getMessage(), ex);
        } finally {
            logger.warn("# receiveZipFile end !");
        }
    }
    
    private List<FileItem> getMultipartFileList(HttpServletRequest request) throws FileUploadException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            throw new RuntimeException("getMultipartFileList isMultipart false");
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(10 * 1024 * 1024 * 1024);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload = new ServletFileUpload(factory);

        List<FileItem> items = null;
        items = upload.parseRequest(request);
        return items;
    }

    @Async
    public String sendRequestAttachFile(String uploadUrl, File file) {
        logger.info("sendRequestAttachFile Url:" + uploadUrl);
        try {
            StringBuilder sb = new StringBuilder();

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost uploadFile = new HttpPost(uploadUrl);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, file.getName());
            HttpEntity multipart = builder.build();

            uploadFile.setEntity(multipart);

            CloseableHttpResponse response = httpClient.execute(uploadFile);
            HttpEntity responseEntity = response.getEntity();
            ByteArrayOutputStream fos = new ByteArrayOutputStream();
            responseEntity.writeTo(fos);
            fos.flush();
            fos.close();
            sb.append(new String(fos.toByteArray()));
            sb.append("\n");

            return sb.toString();
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
    }
}
