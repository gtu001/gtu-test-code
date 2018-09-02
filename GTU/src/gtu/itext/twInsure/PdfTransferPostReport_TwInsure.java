package gtu.itext.twInsure;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import gtu.file.FileUtil;

public class PdfTransferPostReport_TwInsure {

    public static void main(String[] args) throws Exception {
        PdfTransferPostReport_TwInsure v = new PdfTransferPostReport_TwInsure(__createTestForm__());
        v.createDocument(new FileOutputStream(new File(FileUtil.DESKTOP_PATH, "test001.pdf")));
        System.out.println("done...");
    }

    private static final Logger logger = Logger.getLogger(PdfTransferPostReport_TwInsure.class);

    public PdfTransferPostReport_TwInsure(Form form) {
        this.form = form;
    }

    private Form form;
    private Document document;
    private PdfWriter writer;

    public void createDocument(OutputStream ba) throws Exception {
        document = new Document(new Rectangle(PageSize.A4), 30, 30, 100, 40);
        writer = PdfWriter.getInstance(document, ba);
        document.open();
        doReportBody();
        document.close();
        writer.flush();
        writer.close();
    }

    private void _doReportHeader() throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new int[] { 25, 25, 25, 25 });

        addHeaderCell("轉檔日期：", table);
        addHeaderCell(form.getTrdateStart() + "~" + form.getTrdateEnd(), table);
        addHeaderCell("傳送筆數:", table);
        addHeaderCell(form.getCntY(), table);

        addHeaderCell("保單總筆數:", table);
        addHeaderCell(form.getAppCntO(), table);
        addHeaderCell("重送筆數:", table);
        addHeaderCell(form.getCntA(), table);
        document.add(table);
    }

    private void _doReportTitle() throws DocumentException {
        PdfPTable table = new PdfPTable(CellDef.values().length);
        table.setWidthPercentage(100);
        table.setWidths(CellDef.getWidths());

        for (CellDef e : CellDef.values()) {
            String strVal = e.title;
            addBodyCell(strVal, table);
        }

        document.add(table);
    }

    private void _doReportBody() throws DocumentException {
        CheckBoxPolicy[] result = this.form.getResult();

        for (int i = 0; i < result.length; i++) {
            CheckBoxPolicy vo = result[i];

            PdfPTable table = new PdfPTable(CellDef.values().length);
            table.setWidthPercentage(100);
            table.setWidths(CellDef.getWidths());

            for (CellDef e : CellDef.values()) {
                String strVal = getCellStrValue(e.name(), vo);
                addBodyCell(strVal, table);
            }

            document.add(table);
        }
    }

    public void doReportBody() throws Exception {
        _doReportHeader();
        createSpaceTable();
        _doReportTitle();
        _doReportBody();
    }

    public void createSpaceTable() throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.setWidths(new int[] { 100 });
        Font fontCh8 = PdfReportUtil_TwInsure.FontCreater.newInstance().size(8).build();
        PdfReportUtil_TwInsure.PColumnAppender.newInstance().table(table).icolspan(1)//
                .font(fontCh8).strValue(" ")//
                .hAlign(Element.ALIGN_LEFT).vAlign(Element.ALIGN_CENTER).rectangle(Rectangle.NO_BORDER).build();
        document.add(table);
    }

    private void addHeaderCell(String strVal, PdfPTable table) {
        Font fontCh10 = PdfReportUtil_TwInsure.FontCreater.newInstance().size(10).build();
        PdfReportUtil_TwInsure.PColumnAppender.newInstance().table(table).icolspan(1)//
                .font(fontCh10).strValue(strVal)//
                .hAlign(Element.ALIGN_LEFT).vAlign(Element.ALIGN_CENTER).rectangle(Rectangle.NO_BORDER).build();
    }

    private void addBodyCell(String strVal, PdfPTable table) {
        Font fontCh10 = PdfReportUtil_TwInsure.FontCreater.newInstance().size(10).build();
        PdfReportUtil_TwInsure.PColumnAppender.newInstance().table(table).icolspan(1)//
                .font(fontCh10).strValue(strVal)//
                .padding(5).borderWidth(1).hAlign(Element.ALIGN_CENTER).vAlign(Element.ALIGN_CENTER).rectangle(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP | Rectangle.BOTTOM).build();
    }

    private String getCellStrValue(String name, CheckBoxPolicy vo) {
        try {
            Field field = CheckBoxPolicy.class.getDeclaredField(name);
            field.setAccessible(true);
            Object val = field.get(vo);
            String strVal = "";
            if (val != null) {
                strVal = String.valueOf(val);
            }
            return strVal;
        } catch (Exception e) {
            throw new RuntimeException("取得欄位失敗 :" + name, e);
        }
    }

    public enum CellDef {
        trtime("轉檔時間", 20), //
        apno("受理號碼", 20), //
        apseq("受理序號", 20), //
        billno("送金單號", 20), //
        trflag("轉檔狀態", 20), //
        trdate("轉檔日期", 20), //
        sendstatusLabel("寄送轉檔狀態", 20), //
        senddate("寄送轉檔日期", 20), //
        ;
        final String title;
        final float pdfWidth;

        CellDef(String title, int pdfWidth) {
            this.title = title;
            this.pdfWidth = pdfWidth;
        }

        public static float[] getWidths() {
            List<Float> lst = new ArrayList<Float>();
            for (CellDef e : CellDef.values()) {
                lst.add(e.pdfWidth);
            }
            return ArrayUtils.toPrimitive(lst.toArray(new Float[0]));
        }
    }

    private static class Form {
        String trdateStart;
        String trdateEnd;
        String cntY;
        String cntA;
        String appCntO;
        CheckBoxPolicy[] result;

        public String getTrdateStart() {
            return trdateStart;
        }

        public void setTrdateStart(String trdateStart) {
            this.trdateStart = trdateStart;
        }

        public String getTrdateEnd() {
            return trdateEnd;
        }

        public void setTrdateEnd(String trdateEnd) {
            this.trdateEnd = trdateEnd;
        }

        public String getCntY() {
            return cntY;
        }

        public void setCntY(String cntY) {
            this.cntY = cntY;
        }

        public String getCntA() {
            return cntA;
        }

        public void setCntA(String cntA) {
            this.cntA = cntA;
        }

        public String getAppCntO() {
            return appCntO;
        }

        public void setAppCntO(String appCntO) {
            this.appCntO = appCntO;
        }

        public CheckBoxPolicy[] getResult() {
            return result;
        }

        public void setResult(CheckBoxPolicy[] result) {
            this.result = result;
        }
    }

    private static class CheckBoxPolicy {
        String trtime;
        String apno;
        Short apseq;
        String billno;
        String trflag;
        String trdate;
        String sendstatusLabel;
        String senddate;
    }

    private static Form __createTestForm__() throws Exception {
        List<CheckBoxPolicy> lst = new ArrayList<CheckBoxPolicy>();
        for (int ii = 0; ii < 10; ii++) {
            CheckBoxPolicy vo = new CheckBoxPolicy();
            for (CellDef e : CellDef.values()) {
                Field f = CheckBoxPolicy.class.getDeclaredField(e.name());
                f.setAccessible(true);
                try {
                    if (f.getType() == String.class) {
                        f.set(vo, f.getName() + "_" + ii);
                    } else if (f.getType() == Short.class || f.getType() == short.class) {
                        f.set(vo, (short) ii);
                    }
                } catch (Exception e1) {
                    throw new RuntimeException(f.getName(), e1);
                }
            }
            lst.add(vo);
        }
        Form form = new Form();
        form.setResult(lst.toArray(new CheckBoxPolicy[0]));
        form.setCntA("1a");
        form.setCntY("1y");
        form.setAppCntO("ACntO");
        form.setTrdateStart("101/1/1");
        form.setTrdateEnd("101/1/2");
        return form;
    }
}