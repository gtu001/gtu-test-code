package gtu.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.input.BOMInputStream;
import org.testng.log4testng.Logger;

import com.amazonaws.util.IOUtils;

public class UploadFileTest {

    private static final Logger logger = Logger.getLogger(UploadFileTest.class);

    // 收抽樣名單檔
    public void uploadFile(HttpServletRequest request, String writeFilepath) throws FileUploadException, IOException {
        List<FileItem> items = getMultipartFileList(request);
        Iterator<FileItem> iter = items.iterator();
        if (!iter.hasNext()) {
            throw new RuntimeException("uploadFile iter has no Next");
        }
        FileItem item = iter.next();
        if (item.isFormField()) {
            throw new RuntimeException("uploadFile item isFormField");
        }
        try {
            BOMInputStream fileStream = new BOMInputStream(item.getInputStream());
            IOUtils.copy(fileStream, new FileOutputStream(writeFilepath));
        } catch (Exception e) {
            logger.error("uploadFile", e);
        }
    }

    private List<FileItem> getMultipartFileList(HttpServletRequest request) throws FileUploadException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            throw new RuntimeException("getMultipartFileList isMultipart false");
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(10 * 1024 * 1024);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload = new ServletFileUpload(factory);

        List<FileItem> items = null;
        items = upload.parseRequest(request);
        return items;
    }
}
