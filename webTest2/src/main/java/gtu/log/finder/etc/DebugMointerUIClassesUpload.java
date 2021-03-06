package gtu.log.finder.etc;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;

import gtu.file.FileUtil;

public class DebugMointerUIClassesUpload {

    StringBuilder sb;
    HttpServletRequest request;
    List<File> tempFileArry = new ArrayList<File>();
    private static final File tempDir = new File(FileUtil.TEMP_DIR);
    private Map<String, String> parameterMap = new HashMap<String, String>();
    
    public DebugMointerUIClassesUpload(HttpServletRequest request, StringBuilder sb) {
        this.request = request;
        this.sb = sb;
    }

    public boolean isUploadClassesZip() {
        return !tempFileArry.isEmpty();
    }
    
    public String getParameter(String key) {
        if(request.getParameter(key) != null) {
            return request.getParameter(key);
        }
        return parameterMap.get(key);
    }

    public void extractClasspath(String targetDir, String unzipOptions) {
        try {
            File zipFile = tempFileArry.get(0);
            
            File dir = new File(targetDir);

            switch(UnzipMode.valueOf(unzipOptions)) {
            case no:
                DebugMointerUIHotServlet.logH("extractClasspath [不更動class目錄]", sb);
                break;
            case all:
                DebugMointerUIHotServlet.logH("extractClasspath [淨空目錄]", sb);
                if (dir.exists() && dir.isDirectory()) {
                    FileUtils.cleanDirectory(dir);
                }
                break;
            case clazz:
                DebugMointerUIHotServlet.logH("extractClasspath [只清除class]", sb);
                if (dir.exists() && dir.isDirectory()) {
                    List<File> clzList = new ArrayList<File>();
                    FileUtil.searchFileMatchs(dir, ".*\\.class", clzList);
                    for(File f : clzList) {
                        FileUtils.forceDelete(f);
                    }
                }
                break;
            default:
                DebugMointerUIHotServlet.logH("extractClasspath [不更動class目錄]", sb);
                break;
            }
            

            DebugMointerUIZipUtils zipUtil = new DebugMointerUIZipUtils();
            zipUtil.unzipFile(zipFile, dir);

            DebugMointerUIHotServlet.logH("extractClasspath 成功!", sb);
        } catch (Exception e1) {
            DebugMointerUIHotServlet.exceptioinHandler(e1, sb);
        }
    }

    public void uploadFile() {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (!isMultipart) {
            DebugMointerUIHotServlet.logH("form 不支援檔案上傳!", sb);
            return;
        }

        try {
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

            for (FileItem item : items) {
                if (item.isFormField()) {
                    // Process regular form field (input
                    // type="text|radio|checkbox|etc", select, etc).
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString();
                    // ... (do your job here)
                    
                    parameterMap.put(fieldName, fieldValue);

                } else {
                    try {
                        String fieldName = item.getFieldName();
                        String fileName = FilenameUtils.getName(item.getName());

                        if (StringUtils.isBlank(fileName)) {
                            continue;
                        }

                        File tempFile = new File(tempDir, fileName);

                        DebugMointerUIHotServlet.logH(">>檔案 -- " + fieldName + " --- " + tempFile, sb);

                        BOMInputStream fileStream = new BOMInputStream(item.getInputStream());

                        IOUtils.copy(fileStream, new FileOutputStream(tempFile));

                        tempFileArry.add(tempFile);
                    } catch (Exception e) {
                        throw new RuntimeException("uploadFile failed : " + e.getMessage(), e);
                    }
                }
            }
        } catch (Exception e1) {
            DebugMointerUIHotServlet.exceptioinHandler(e1, sb);
        }
    }
    
    enum UnzipMode {
        no("不更動", true), clazz("僅刪除class", false), all("刪除全部", false);
        
        String label;
        boolean isSelected = false;

        UnzipMode(String label, boolean isSelected) {
            this.label = label;
            this.isSelected = isSelected;
        }
        
        static String generateHtml(String tagName) {
            final String tag = "<input name=\"%1$s\" type=\"radio\" value=\"%2$s\" %4$s />%3$s";
            StringBuilder sb= new StringBuilder();
            for(UnzipMode e : UnzipMode.values()) {
                String selected = e.isSelected ? " checked=\"checked\" " : "";
                sb.append(String.format(tag, tagName, e.name(), e.label, selected));
            }
            return sb.toString();
        }
    }
}
