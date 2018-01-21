package gtu.itext.alexhsu;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 浮水印
 * 
 * @author alex 100/6/14
 */
public class WaterMark extends PdfPageEventHelper {
    //public int fontSize;
    /**
     * 浮水印字型
     */
    public Font font;
    /**
     * 點字型
     */
    public Font pointFont;
    /**
     * 戶所名稱
     */
    public String siteName;

    //public BaseFont baseFont;
    /**
     * 列印日期
     */
    //    public String printDate;

    public void onEndPage(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(writer.getDirectContentUnder(), Element.ALIGN_CENTER, new Phrase(siteName, font), 450, 300, 20);
    }

    /**
     * @return the font
     */
    public Font getFont() {
        return font;
    }

    /**
     * @param font
     *            the font to set
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * @return the siteName
     */
    public String getSiteName() {
        return siteName;
    }

    /**
     * @param siteName
     *            the siteName to set
     */
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    /**
     * @return the pointFont
     */
    public Font getPointFont() {
        return pointFont;
    }

    /**
     * @param pointFont
     *            the pointFont to set
     */
    public void setPointFont(Font pointFont) {
        this.pointFont = pointFont;
    }
}
