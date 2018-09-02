package gtu.jsf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

/**
 * @author Troy 2009/02/02
 * 
 */
public class Download {

    private void downloadFile(String strfileName, byte[] fileStream) {
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance()
                    .getExternalContext().getResponse();
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            httpServletResponse.setHeader("Content-disposition", "attachment; filename=" + strfileName);

            httpServletResponse.setContentLength(fileStream.length);
            httpServletResponse.setContentType("application/x-download");
            servletOutputStream.write(fileStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void downloadFile2(HttpServletResponse response, InputStream inputStream, String fileName) throws IOException {
        // 下面兩行要先寫(setContentType, setHeader)，否則檔案名稱可能會隨機變成網址名稱
        response.setContentType("application/octet-stream");
        //response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");//excel
        response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");

        OutputStream out = response.getOutputStream();
        IOUtils.copy(inputStream, out);
        inputStream.close();

        response.setHeader("Set-Cookie", "fileDownload=true; path=/");

        response.getOutputStream().close();
        response.flushBuffer();
    }
}
