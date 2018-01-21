package gtu.itext.iisi;

import gtu.itext.iisi.CHTFontFactory.RISFont;
import gtu.itext.iisi.data.CellConst;
import gtu.itext.iisi.data.CellDataSource;
import gtu.itext.iisi.header.AbatractPageHeader.PagePattern;
import gtu.itext.iisi.header.HeaderPosition;
import gtu.itext.iisi.header.PageHeaderCH;
import gtu.itext.iisi.header.StringHeader;
import gtu.itext.iisi.marker.MarkInfo;
import gtu.itext.iisi.table.CellFormat;
import gtu.itext.iisi.table.NormalTableMetadata;
import gtu.itext.iisi.table.NormalTableMetadata.ColumnDefine;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.gov.moi.common.SystemConfig;

import com.itextpdf.text.Element;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;

public class _MainTest {

    public static void main(String[] args) throws DocumentException, IOException {
        XPDF xpdf = new XPDF();
        File outputFile = new File("C:/Users/gtu001/Desktop/test.pdf");
        xpdf.generate(new PDFDocumentManager(), outputFile);
        System.out.println("done...");
    }
    
    enum Define implements ColumnDefine {
        AAA("aaa", "bbb", 10),//
        BBB("aaa", "bbb", 10),//
        ;
        
        String title; String field; int width;
        Define(String title, String field, int width){
            this.title = title;
            this.field = field;
            this.width = width; 
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public CellDataSource getField() {
            return new CellDataSource() {
                @Override
                public void reset() {
                }
                @Override
                public Object eval(Object dataObj) {
                    return field;
                }
            };
        }

        @Override
        public float getWidth() {
            return 20;
        }
    }
    
    
    static class XPDF extends AbstractPDFGenerator {
        private XPDF(){
            final LayoutInfo layoutInfo = new LayoutInfo(//
                    Coordinate.CM.trans(1.0f) //
                    , Coordinate.CM.trans(1.0f) //
                    , 60f // 上 17 + ((0.3*140/2)) + 9 + 3 留空 = 60
                    , 60f // 下 17 + ((0.3*140/2)) + 14 + 3 留空 = 48 + 17 = 65
            );

            layoutInfo.setHeader(HeaderPosition.LeftHeader, "哈哈哈" + "\n　", 12);

            SystemConfig systemConfig = new SystemConfig();
            final CHTFontFactory chtFontFactory = RISFont.SUNG.getFactory(systemConfig);
            final FontInfo text14 = chtFontFactory.createFontInfo(14, FontStyle.BOLD, Color.BLACK); // ,
                                                                                                    // Color.LIGHT_GRAY
            final FontInfo text12 = chtFontFactory.createFontInfo(12, FontStyle.NORMAL, Color.BLACK); //
            layoutInfo.setHeader(HeaderPosition.CenterHeader, new StringHeader(//
                    new SubPhrase(text14, "嘿嘿嘿" + "\n") //
                    , new SubPhrase(text12, "呼呼呼") //
                    ) //
                    );

            layoutInfo.setHeader(HeaderPosition.CenterFooter,
                    new PageHeaderCH(PagePattern.BOTH, 12).setPrefix("顆顆" + "第"));

            final Map<String, String> params = new HashMap<String, String>();
            params.put("siteName", "戶所");
            
            // 紙張大小
            Rectangle rectangle = PageSize.A4;//PageSize.A3
            String watermark = null;
            if (PageSize.A3.equals(rectangle)) {
                watermark = "RSWM00A3L";
            } else {
                watermark = "RSWM00A4R";
            }
            
            super.setup(rectangle.rotate(), layoutInfo,
                    Arrays.asList(MarkInfo.watermark(watermark, params), MarkInfo.seal("RSSS00000")));
        }
        
        @Override
        protected void generateContent(PDFDocument pdfDocument) throws DocumentException, InterruptedException {

            // 1. 準備資料對應
            final NormalTableMetadata table = new NormalTableMetadata("");
            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            //
            // 1.a FORMAT
            //
            final CellFormat defaultHeaderFormat = table.getDefaultHeaderFormat();
            defaultHeaderFormat.setBackgroundColor(null);

            //
            // 1.b METADATA
            //
            int dataStart = 0;
            table.newSubColumn("序號", CellConst.newCounter(dataStart), 4);
            for (ColumnDefine columnDefine : Define.values()) {
                table.newSubColumn(columnDefine);
            }
            //
            // 1.c 計算表格WIDTH
            //
            float totalWidth = 0;
            float[] widths = table.getWidths();
            for (float f : widths) {
                totalWidth += f;
            }
            table.setWidth(pdfDocument.estimateTableWidthPercentage(Coordinate.Default, totalWidth * 12)); // 表格width,

            // 2 貼上表格.
            int sizePerFile = 10000;
            final PDFSimpleTableTransfer t = new PDFSimpleTableTransfer(pdfDocument, table);
            t.setOuterBorder(true);
            t.setMaxDataCount(sizePerFile);
            // 2.a 貼上表格
            List<Object> valList = new ArrayList<Object>();
            valList.add(new Object[]{"1", "2"});
            valList.add(new Object[]{"3", "4"});
            valList.add(new Object[]{"5", "6"});
            t.transTable(valList);

            pdfDocument.paragraphBuilder().addText("總計：" + "xxxxx").appendMe();
            pdfDocument.writeText("");
            pdfDocument.paragraphBuilder().addText("編製機關：" + "xxxxx").appendMe();
            pdfDocument.paragraphBuilder().addText("製表日期：民國" + "xxxxx").appendMe();
        }
    }
}
