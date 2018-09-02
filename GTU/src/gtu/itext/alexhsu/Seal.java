package gtu.itext.alexhsu;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class Seal extends PdfPageEventHelper {

    /**
     * 總頁數
     */
    public int allPage;

    //本機
    //	public final String IP = "http://127.0.0.1";

    //測試機
    //	public final String IP = "http://140.92.86.164";

    //正式機
    //	public final String IP = "http://192.1.0.58";

    public final String port = "9080";

    public void onEndPage(PdfWriter writer, Document document) {

        Image img;

        try {
            //本機沒起AP Server路徑
            //			img = Image.getInstance("D:/Workspace/KfrAppletV1.2_SVN/stamp/stamp_hi5.jpg");
            //            img = Image.getInstance("stamp_hi5.jpg");
            img = Image.getInstance("C:/Users/gtu001/Desktop/workspace/KfrApplet/src/stamp_hi5.jpg");

            //			byte[] bytes = toByte(file);			
            //			img = Image.getInstance(bytes);

            //取圖片路徑
            //			img = Image.getInstance("http://127.0.0.1:9080/images/stamp_hi5.jpg");
            //			System.out.println(IP+":"+port+"/images/stamp_hi5.jpg");
            //			img = Image.getInstance(IP+":"+port+"/images/stamp_hi5.jpg");

            //PdfContentByte content = writer.getDirectContent();
            //將此層放至下一個圖層
            PdfContentByte content = writer.getDirectContentUnder();

            int n = writer.getPageNumber();
            System.out.println("Page No = " + n);
            //			System.out.println("printDate = " + printDate);
            System.out.println("getAllPage = " + getAllPage());
            sealLocate(content, document, n, img);

            content.saveState();
            content.beginText();
            content.endText();
            content.restoreState();

        } catch (BadElementException e) {
            System.out.println("BadElementException = " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException = " + e.getMessage());
        }
    }

    /**
     * 設定騎縫章所印位置
     * 
     * @param content
     * @param document
     * @param time
     * @param page
     * @param img
     */
    public void sealLocate(PdfContentByte content, Document document, int page, Image img) {
        if (getAllPage() != 1) {
            switch (5) {
            case 0:
                setSealLocate(content, document, page, img, 0);
                break;
            case 1:
                setSealLocate(content, document, page, img, img.getWidth());
                break;
            case 2:
                setSealLocate(content, document, page, img, img.getWidth() * 2);
                break;
            case 3:
                setSealLocate(content, document, page, img, img.getWidth() * 3);
                break;
            case 4:
                setSealLocate(content, document, page, img, img.getWidth() * 4);
                break;
            case 5:
                setSealLocate(content, document, page, img, img.getWidth() * 5);
                break;
            case 6:
                setSealLocate(content, document, page, img, img.getWidth() * 6);
                break;
            case 7:
                setSealLocate(content, document, page, img, document.getPageSize().getWidth() - img.getWidth());
                break;
            case 8:
                setSealLocate(content, document, page, img, img.getWidth() * 3 / 2);
                break;
            case 9:
                setSealLocate(content, document, page, img, img.getWidth() * 5 / 2);
                break;
            case 10:
                setSealLocate(content, document, page, img, img.getWidth() * 7 / 2);
                break;
            case 11:
                setSealLocate(content, document, page, img, img.getWidth() * 9 / 2);
                break;
            }
        }
    }

    public void setSealLocate(PdfContentByte content, Document document, int page, Image img, float setX) {
        System.out.println("page = " + page);
        System.out.println("getAllPage = " + getAllPage());

        try {
            if (page == 1) {
                content.addImage(img, img.getWidth(), 0, 0, img.getHeight(), setX, 0 - img.getHeight() / 2);
            } else if (page > 1) {
                //當getAllPage傳最後一頁進來時，此時n也等於最後一頁時，就只印上方的騎縫章
                if (page != getAllPage()) {
                    content.addImage(img, img.getWidth(), 0, 0, img.getHeight(), setX, 0 - img.getHeight() / 2);
                }
                content.addImage(img, img.getWidth(), 0, 0, img.getHeight(), setX, document.getPageSize().getHeight() - img.getHeight() / 2);
            } else {
                content.addImage(img, img.getWidth(), 0, 0, img.getHeight(), setX, document.getPageSize().getHeight() - img.getHeight() / 2);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    /**
     * 將圖片轉成byte列出
     * 
     * @param file
     * @return
     */
    public byte[] toByte(File file) {
        BufferedInputStream br;
        try {
            br = new BufferedInputStream(new FileInputStream(file));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(baos);

            int ch;
            while ((ch = br.read()) != -1) {
                bos.write(ch);
            }

            bos.flush();
            bos.close();
            br.close();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @return the allPage
     */
    public int getAllPage() {
        return allPage;
    }

    /**
     * @param allPage
     *            the allPage to set
     */
    public void setAllPage(int allPage) {
        this.allPage = allPage;
    }
}
