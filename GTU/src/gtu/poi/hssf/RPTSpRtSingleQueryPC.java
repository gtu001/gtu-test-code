package gtu.poi.hssf;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.fet.csims.common.util.internal.BaseCommonReportPC;
import com.omniwise.util.DBUtils;
import com.omniwise.util.excel.ExcelReporter;

/**
 * SP_REPAIR_TICKET 單筆查詢結果報表
 * 
 * @author Troy
 * @version 2010/9/14
 */
public class RPTSpRtSingleQueryPC extends BaseCommonReportPC {

    private static final long serialVersionUID = 1L;

    private static final String REPORT_TITLE = "送修清單";
    
    private static final int REPORT_COLS = 12;

    public static void main(String[] args) throws Exception {
        Object[][] mmmp = new Object[][] { 
                new Object[] { "req.eform_REPORT_APP_ID_1", new String[] { "ANO3" } }, 
                new Object[] { "req.eform_SP_REPAIR_TICKET_REPAIR_TICKET_ID_1", new String[] { "RT20100823002" } }, 
        };
        
        RPTSpRtSingleQueryPC pc = new RPTSpRtSingleQueryPC();
        Map<?, ?> inputMap = ArrayUtils.toMap(mmmp);
        pc.reportProcess(null, inputMap, new ExcelReporter());
        System.out.println("done...");
    }

    private Map<String, Object> queryTitleMap(Map<?, ?> inputMap, DataSource dataSource) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select v.name as VENDOR,                                                                           ");
        sql.append("        t.repair_ticket_id as SLIP_NO,                                                              ");
        sql.append("        '' as AWB_NO,                          													 ");
        sql.append("        t.CREATION_DATE as FILL_DATE,          													 ");
        sql.append("        '' as HAWB,                            													 ");
        sql.append("        (select name from v_sp_rt_sp_or_wh sw where sw.id = t.sp_or_wh) as WH_SLIP_NO,              ");
        sql.append("        v.contact as CONTACT,                  													 ");
        sql.append("        v.tel1 as TEL,                         											         ");
        sql.append("        v.Fax as FAX,                          												     ");
        sql.append("        v.contact_address_1 as ADDRESS,        												     ");
        sql.append("        v.contact_email as EMAIL,               													 ");
        sql.append("        owutil.Getparamlabel('REGION', t.REGION) as REGION,        								 ");
        sql.append("        t.ISSUER as ISSUER               			        										 ");
        sql.append("   from sp.sp_repair_ticket t, vc.vc_vendors v 												     ");
        sql.append("  where 1 = 1                                                          							 ");
        sql.append("    and t.vendor_id = v.vendor_id(+)          														 ");
        sql.append("    and t.repair_ticket_id = '%s'            														 ");
        String repairTicketId = (String) this.getDataMapValue("REPAIR_TICKET_ID", "SP_REPAIR_TICKET", inputMap);
        String ssql = String.format(sql.toString(), repairTicketId);
        this.debug("SQL[TITLE] : " + ssql);
        Map<String, Object> rec = this.queryMap(ssql, dataSource);
        return rec;
    }

    private void excelSetTitle(Map<?, ?> inputMap, HSSFSheet sheet, final ExcelReporter writer, DataSource dataSource) throws SQLException {
        Map<String, Object> rec = this.queryTitleMap(inputMap, dataSource);

        HSSFCellStyle headTitileStyle = IIStyle.of(writer).fontBold().fontSize(14).alignCenterSelection().verticalCenter().getStyle();
        HSSFCellStyle headLeft = IIStyle.of(writer).fontSize(12).verticalCenter().getStyle();
        HSSFCellStyle headRight = IIStyle.of(writer).fontSize(12).alignRight().verticalCenter().getStyle();

        CellCreate cellList = new CellCreate();

        cellList.initRow(0);
        cellList.colSpan("遠傳電信股份有限公司", 0, REPORT_COLS, headTitileStyle).nextRow();
        cellList.colSpan("送修單(REPAIR ORDER)", 0, REPORT_COLS, headTitileStyle).nextRow();

        cellList.cellCreate("廠商 Vendor：", 2, headRight);
        cellList.cellCreate((String) rec.get("VENDOR"), 3, headLeft);
        cellList.cellCreate("Region：", 5, headRight);
        cellList.cellCreate((String) rec.get("REGION"), 6, headLeft);
        cellList.cellCreate("送修單號 Form NO：", 10, headRight);
        cellList.cellCreate((String) rec.get("SLIP_NO"), 11, headLeft).nextRow();

        cellList.cellCreate("SP / 倉庫 WH：", 2, headRight);
        cellList.cellCreate((String) rec.get("WH_SLIP_NO"), 3, headLeft);
        cellList.cellCreate("填單人工號：", 5, headRight);
        cellList.cellCreate((String) rec.get("ISSUER"), 6, headLeft);
        cellList.cellCreate("送修日期/時間 Send Date/Time：", 10, headRight);
        cellList.cellCreate(((Timestamp) rec.get("FILL_DATE")).toString(), 11, headLeft).nextRow();

        cellList.cellCreate("聯絡人 Contact：", 2, headRight);
        cellList.cellCreate((String) rec.get("CONTACT"), 3, headLeft);
        cellList.cellCreate("廠商電話TEL：", 5, headRight);
        cellList.cellCreate((String) rec.get("TEL"), 6, headLeft);
        cellList.cellCreate("廠商傳真 FAX：", 10, headRight);
        cellList.cellCreate((String) rec.get("FAX"), 11, headLeft).nextRow();

        cellList.cellCreate("廠商地址 Address：", 2, headRight);
        cellList.cellCreate((String) rec.get("ADDRESS"), 3, headLeft);
        cellList.cellCreate("廠商郵件地址 Email Address：", 10, headRight);
        cellList.cellCreate((String) rec.get("EMAIL"), 11, headLeft).nextRow();

        this.processExcelColumn(cellList.list, sheet);
    }

    private static class CellCreate {
        private List<Cell> list = new ArrayList<Cell>();
        private int row;

        private CellCreate initRow(int row) {
            this.row = row;
            return this;
        }

        private CellCreate cellCreate(String value, int col, HSSFCellStyle cellStyle) {
            list.add(Cell.create(value, row, row, col, col, cellStyle, false));
            return this;
        }

        private CellCreate colSpan(String value, int start, int end, HSSFCellStyle cellStyle) {
            list.add(Cell.colspan(row, value, start, end, cellStyle));
            return this;
        }

        private CellCreate nextRow() {
            this.row++;
            return this;
        }
    }

    private void excelSetBody(Map<?, ?> inputMap, HSSFSheet sheet, final ExcelReporter writer, DataSource dataSource) throws Exception {
        String[] title = new String[] { "項次\n No. ", "組織別", "遠傳維修號碼\nFET RMA No.", "料號\n Item No.", "名稱\n Description", "序號\n Serial No.", "數量\n Qt'y", "來源儲位", "來源Site", "故障描述\nFault Desc", "版本\n R-State", "維修合約\nContract", "備註欄\n Note" };
        String[] columns = new String[] { "SNO", "ORG", "RMA", "ITEM_CODE", "ITEM_NAME", "SERIAL_NO", "QUANTITY", "SRC_LOCATOR", "SRC_SITE_ID", "DESCRIPTION", "ITEM_VERSION", "REPAIR_CONTRACT", "NOTE" };
        short[] widths = { 1500, 1500, 5000, 5000, 7000, 4000, 1500, 3000, 3000, 3000, 3000, 3000, 4000};

        writer.setSheetWidth(sheet, widths);

        this.setRowColumns(title, 6, sheet);

        StringBuilder sql = new StringBuilder();
        sql.append(" select to_char(ROWNUM) as SNO,                                      		");
        sql.append("        owutil.Getparamlabel('ERP_ORG_NAME', s.ORG) as ORG,          		");
        sql.append("        s.RMA,                                                       		");
        sql.append("        s.EQUIP_ID,                                                  		");
        sql.append("        s.ITEM_CODE,                                                 		");
        sql.append("        (select m.item_name from sp.sp_material_main m               		");
        sql.append("          where m.org = s.org and m.item_code = s.item_code          		");
        sql.append("            and m.delete_by is null and rownum = 1) as ITEM_NAME,    		");
        sql.append("        s.SERIAL_NO,                                                 		");
        sql.append("        s.ITEM_UNIT,                                                 		");
        sql.append("        s.QUANTITY,                                                  		");
        sql.append("        s.SRC_LOCATOR,                                               		");
        sql.append("        '' as FAULT_REPORT_NO,                                       		");
        sql.append("        s.DESCRIPTION,                                               		");
        sql.append("        s.DEST_LOCATOR,                                              		");
        sql.append("        s.ITEM_VERSION,                                              		");
        sql.append("        s.EXPECT_OUT_DATE,                                           		");
        sql.append("        s.NOTE,                                                      		");
        sql.append("        s.SRC_SITE_ID,                                                 		");
        sql.append("        (select name 	                                             		");
        sql.append("           from (select contract_id, name                            		");
        sql.append("                   from vc_ma_contract                               		");
        sql.append("                 union                                               		");
        sql.append("                 select contract_id, name from vc_pc_contract) mp    		");
        sql.append("          where s.repair_contract_id = mp.contract_id) as REPAIR_CONTRACT	");
        sql.append("   from sp_repair_ticket t, sp_repair_rma s                          		");
        sql.append("  where 1 = 1                                                        		");
        sql.append("    and t.repair_ticket_id = s.repair_ticket_id                      		");
        sql.append("    and t.repair_ticket_id = '%s'                                    		");

        String repairTicketId = (String) this.getDataMapValue("REPAIR_TICKET_ID", "SP_REPAIR_TICKET", inputMap);

        String ssql = String.format(sql.toString(), repairTicketId);
        this.debug("SQL[BODY] : " + ssql);

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(ssql);
            writer.setContent(sheet, rs, columns);
        } catch (Exception ex) {
            this.debug("Connection close failed : " + ex.getMessage());
        } finally {
            DBUtils.close(rs, stmt, conn);
        }

        IIStyle style1 = IIStyle.of(writer).verticalCenter().warpText().boderBlock();
        IIStyle centerStyle = IIStyle.of(writer).verticalCenter().warpText().boderBlock().alignCenter();

        writer.setCellStyle(sheet, 6, 0, 6, REPORT_COLS, centerStyle);
        writer.setCellStyle(sheet, 7, 0, sheet.getLastRowNum(), REPORT_COLS, style1);
        this.colStyleSet(0, writer, sheet, centerStyle);
        this.colStyleSet(1, writer, sheet, centerStyle);
        this.colStyleSet(6, writer, sheet, centerStyle);
    }
    
    private void colStyleSet(int col, ExcelReporter writer, HSSFSheet sheet, IIStyle style) {
        writer.setCellStyle(sheet, 7, col, sheet.getLastRowNum(), col, style);
    }

    @SuppressWarnings("deprecation")
    private void excelSetFoot(Map<?, ?> inputMap, HSSFSheet sheet, final ExcelReporter writer, DataSource dataSource) throws Exception {
        int rownum = sheet.getLastRowNum() + 3;
        List<Cell> list = new ArrayList<Cell>();

        HSSFCellStyle underLineStyle = IIStyle.of(writer).underline().fontSize(12).getStyle();
        HSSFCellStyle cellSize12 = IIStyle.of(writer).fontSize(12).alignRight().getStyle();
        HSSFCellStyle cellSize10 = IIStyle.of(writer).fontSize(10).getStyle();

        list.add(Cell.colspan(rownum, "表單填寫人：", 0, 2, cellSize12));
        list.add(Cell.colspan(rownum, "", 3, 4, underLineStyle));
        list.add(Cell.colspan(rownum, "廠商簽名：", 5, 6, cellSize12));
        list.add(Cell.colspan(rownum, "", 7, 8, underLineStyle));
        list.add(Cell.colspan(rownum, "身份證字號：", 9, 10, cellSize12));
        list.add(Cell.colspan(rownum, "", 11, 12, underLineStyle));
        list.add(Cell.colspan(rownum + 1, "", 0, 0, cellSize12));
        
        this.processExcelColumn(list, sheet);

        String[] foot = new String[] {
                "遠傳電信股份有限公司_FarEasTone Tele Co.Ltd",
                "板橋倉庫_Banciao Warehouse // 台北縣板橋市22063四川路一段334號_No.334, Sec. 1, Sihchuan Rd., Banciao City, Taipei County 220, Taiwan (R.O.C.) //  Tel:02-2950-7363 & 02-2950-5325 //  Fax:02-2950-5396",
                "桃園倉庫_Taoyuan Warehouse  // 桃園縣蘆竹鄉33860內新路28號 _No.28, Neisin Rd., Lujhu Township, Taoyuan County 338, Taiwan (R.O.C.) // Tel:03-324-9836 // Fax:03-324-9835",
                "大園倉庫_Taoyuan Warehouse // 桃園縣大園鄉33743橫楠路235號_No.235, Hengnan Road, Dayuan Township, Taoyuan County 337, Taiwan (R.O.C.) // Tel:03-386-5427 / Fax:03-386-5427",
                "台中倉庫1_Taichung Warehouse 1 // 臺中市西屯區40768工業區34路25之1號_No.25-1, Gongyecyu 34th Rd., Situn District, Taichung City 407, Taiwan (R.O.C.) // Tel:04-374-1845 / Fax:04--374-1899",
                "台中倉庫2_Taichung Warehouse 2 // 臺中市西屯區40768工業區37路43號_No.43, Gongyecyu 37th Rd., Situn District, Taichung City 407, Taiwan (R.O.C.) // Tel:04-374-1805 // Fax:04--374-1899",
                "高雄倉庫_ Kaohsiung Warehouse  // 高雄市前鎮區80673新衙路355號_No.355, Sinya Rd., Cianjhen District, Kaohsiung City 806, Taiwan (R.O.C.) // Tel:07-971-0400 // Fax:07-971-6293", 
              };

        for (String f : foot) {
            HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
            HSSFCell cell = row.createCell((short) 0);
            cell.setCellValue(f);
            cell.setCellStyle(cellSize10);
        }
    }

    @Override
    public void writeExcelContent(DataSource dataSource, Map<?, ?> inputMap, ExcelReporter writer) throws Exception {
        HSSFSheet sheet = this.createSheet(REPORT_TITLE, writer);
        // 處理標題
        this.excelSetTitle(inputMap, sheet, writer, dataSource);
        // 處理資料
        this.excelSetBody(inputMap, sheet, writer, dataSource);
        // 處理表尾
        this.excelSetFoot(inputMap, sheet, writer, dataSource);
        
        this.printSetup(sheet);
        
        writer.getWorkBook().setRepeatingRowsAndColumns(0, 0, 12, 0, 6); // 設定重複標提列
    }
    
    private void printSetup(HSSFSheet sheet) {
        HSSFPrintSetup setup = sheet.getPrintSetup();
        setup.setLandscape(true);   //衡像列印
        setup.setScale((short)75);  //列印縮放比
        setup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE); //A4
        
        sheet.setMargin(HSSFSheet.TopMargin, 0.6);      //邊界  上1.5
        sheet.setMargin(HSSFSheet.BottomMargin, 0.4);   //邊界  下1.0
        sheet.setMargin(HSSFSheet.LeftMargin, 0);
        sheet.setMargin(HSSFSheet.RightMargin, 0);
        sheet.setHorizontallyCenter(true);              //水平置中
    }
}
