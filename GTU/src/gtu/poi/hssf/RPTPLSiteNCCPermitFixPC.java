package gtu.poi.hssf;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;


/**
 * 柝站總表
 * 
 * 取代原本 RPTPLSiteNCCPermitPC.java報表檔案
 * 
 * @author Troy
 */
public class RPTPLSiteNCCPermitFixPC {
	
    private static final long serialVersionUID = 1L;
    
    
    private StringBuffer getFullSql(Map inputMap){
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from ow_user t where rownum<100 ");
        return sql;
    }
    
    /**
     * 測試用...
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
    	RPTPLSiteNCCPermitFixPC pc = new RPTPLSiteNCCPermitFixPC();
    	pc.displayClassPath();
    	Map inputMap = new HashMap();
    	inputMap.put("req.eform_REPORT_APP_ID_1",new String[]{"PT20080908003"});
    	StringBuffer sb = pc.getFullSql(inputMap);
//    	System.out.println(sb);
		pc.reportProcess(pc.getDatasource(), inputMap);
		System.out.println("done...");
    }
    
    
	public void displayClassPath() {
		try {
			System.out.println(System.getProperty("java.class.path"));
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
    
    public DataSource getDatasource(){
		BasicDataSource datasource = new BasicDataSource();
		datasource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		datasource.setUrl("jdbc:oracle:thin:@192.168.156.140:1521:CSIMS");
		datasource.setUsername("OWLET25");
		datasource.setPassword("OWLET25");
		datasource.setMaxActive(100);
		datasource.setMinIdle(30);
		return datasource;
	}
    
    

	
	public void debug(String message) throws Exception{
		System.out.println(message);
	}
	
	public void reportProcess(DataSource dataSource, Map inputMap) throws Exception {
		ExcelWriter writer = ExcelWriter.getInstance();
		
		StringBuffer sb = this.getFullSql(inputMap);
		
		for(Iterator it = inputMap.entrySet().iterator(); it.hasNext();){
			Map.Entry entry = (Map.Entry)it.next();
			if(entry.getValue() instanceof String[]){
//					this.debug(entry.getKey() + "\t" + ArrayUtils.toString(entry.getValue()));
				this.debug(entry.getKey() + "\t" + entry.getValue());
			}else{
				this.debug(entry.getKey() + "\t" + entry.getValue());
			}
		}
		
		this.debug("SQL:"+sb.toString());
        Connection c = dataSource.getConnection();
        Statement s = c.createStatement();
        
        this.debug("## start...query");
        long startQuery = System.currentTimeMillis();
        ResultSet rs = s.executeQuery(sb.toString());
        long duringQuery = System.currentTimeMillis() - startQuery;
        this.debug("## query use time (millis) : " + duringQuery);
        
        this.debug("## start...execute excel!");
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("NCC電子轉檔(架許函)");
        
        this.writeExcelContent(rs, sheet, workbook);
        
        writer.writeExcel(System.getProperty("user.home") + "\\桌面\\test.xls", workbook);
        this.debug("## excel done!");
    }
    
    
    private void writeExcelContent(ResultSet rs, HSSFSheet sheet, HSSFWorkbook workbook) throws Exception{
    	ExcelWriter.getInstance();
    	ExcelWriter.SheetHandler.newInstance(sheet).setResultSet(rs).setAutoCellWidths(true).execute();
    	
    	System.out.println("## rows = "+sheet.getLastRowNum());
    	
    	HSSFFont font = ExcelWriter.FontHandler.newInstance(workbook.createFont()).setColor(
				new HSSFColor.DARK_RED().getIndex()).setFontSize((short) 10)
				.setItalic(true).setStrikeout(true).setUnderline((byte) 123).getFont();
    	
    	ExcelWriter.CellStyleHandler.newInstance(workbook.createCellStyle())
				.setBorder(new HSSFColor.BLACK())
//				.setForegroundColor(new HSSFColor.YELLOW())
				.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION)
//				.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER)
				.setWrapText(true)
				.setSheet(sheet)
//				.setFont(font)
				.applyStyle(0, sheet.getLastRowNum(), 0, 7);
    }
}