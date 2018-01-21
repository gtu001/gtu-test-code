package gtu.jsf;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FetchImage {

    private Logger log = LoggerFactory.getLogger(getClass());

    //    頁面
    //    
    //    <!-- 放大歷史影像 -->
    //    <p:dialog id="dialog" header="歷史影像" widgetVar="imgDialog" resizable="false" modal="true" height="570">  
    //        <p:graphicImage value="#{rc03300Controller.imgStreamed}" height="550" cache="FALSE">
    //            <f:param name="filePath" value="#{rc03300Controller.selectedDto.imgFilePath}" />
    //        </p:graphicImage>
    //    </p:dialog>

    /**
     * 取得影像StreamedContent.
     * 
     * @return the img streamed
     */
    public StreamedContent getImgStreamed() {
        StreamedContent streamedContent = null;
        try {
            final FacesContext context = FacesContext.getCurrentInstance();
            final HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            final String filePath = (String) request.getParameter("filePath");

            if (context.getRenderResponse() || StringUtils.isBlank(filePath)) {

                /* JSF第一次呼叫，回傳任何StreamedContent，避免exception */
                final BufferedImage bufferedImg = new BufferedImage(100, 15, BufferedImage.TYPE_INT_RGB);
                final Graphics2D g = bufferedImg.createGraphics();
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
                g.drawString("無影像圖片", 0, 10);
                ImageIO.write(bufferedImg, "png", os);
                streamedContent = new DefaultStreamedContent(new ByteArrayInputStream(os.toByteArray()), "image/jpeg");
            } else {

                /* JSF第二次呼叫，取得真正resource streamedContent */
                final FileInputStream fis = new FileInputStream(new File(filePath));
                final ByteArrayOutputStream bos = new ByteArrayOutputStream();
                final byte[] buf = new byte[1024];
                for (int readNum; (readNum = fis.read(buf)) != -1;) {
                    bos.write(buf, 0, readNum);
                }
                streamedContent = new DefaultStreamedContent(new ByteArrayInputStream(bos.toByteArray()), "image/jpeg");
            }
        } catch (Exception e) {
            log.debug("==Rc03300Controller== 取得影像StreamedContent錯誤(" + e + ")");
        }

        return streamedContent;
    }
}
