package gtu.jsf;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

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

}
