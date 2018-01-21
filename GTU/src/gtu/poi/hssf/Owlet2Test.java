package gtu.poi.hssf;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.Region;

public class Owlet2Test {

    public static void main(String[] args) {
        HSSFSheet sheet = null;
        // set comment

        HSSFPatriarch patr = sheet.createDrawingPatriarch();
        HSSFComment comment1 = patr.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 5, 8));
        comment1.setString(new HSSFRichTextString("請填入 \n專案驗收日期 \n逐件驗收日期\n逐件收貨日期\nPO最後收貨日期"));
        comment1.setColumn((short) 1);

        // 合併cell
        sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) 19));
    }

    private void printSetup(HSSFSheet sheet) {
        HSSFPrintSetup setup = sheet.getPrintSetup();
        setup.setLandscape(true); // 衡像列印
        setup.setScale((short) 75); // 列印縮放比
        setup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE); // A4

        sheet.setMargin(HSSFSheet.TopMargin, 0.6); // 邊界 上1.5
        sheet.setMargin(HSSFSheet.BottomMargin, 0.4); // 邊界 下1.0
        sheet.setMargin(HSSFSheet.LeftMargin, 0);
        sheet.setMargin(HSSFSheet.RightMargin, 0);
        sheet.setHorizontallyCenter(true); // 水平置中
    }
}
