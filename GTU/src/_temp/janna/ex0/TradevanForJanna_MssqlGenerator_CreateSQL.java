package _temp.janna.ex0;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import _temp.janna.ex0.TradevanForJanna_MssqlGenerator.ColumnDef;
import _temp.janna.ex0.TradevanForJanna_MssqlGenerator.TableDef;

public class TradevanForJanna_MssqlGenerator_CreateSQL {

    public static void main(String[] args) {
        File file = new File("C:/Users/gtu001/Desktop/J122-SDD-001(資料庫及檔案規格)_V1.0_出租管理作業_20170528.docx");
        TradevanForJanna_MssqlGenerator test = new TradevanForJanna_MssqlGenerator();
        test.execute(file);

        TradevanForJanna_MssqlGenerator_CreateSQL test2 = new TradevanForJanna_MssqlGenerator_CreateSQL();
        test2.execute(test.tabMap);
    }

    public String execute(Map<String, TableDef> tabMap) {
        StringBuffer sb = new StringBuffer();
        for (String k : tabMap.keySet()) {
            String sql = executeSingleTable(tabMap.get(k));
            System.out.println(sql);
            sb.append(sql);
            sb.append("\n");
        }
        return sb.toString();
    }

    private String executeSingleTable(TableDef def) {
        StringBuffer sb = new StringBuffer();
        sb.append("-- " + def.tabName + " / " + def.tabNameChs + "\n");
        sb.append("CREATE TABLE [dbo].[" + StringUtils.trim(def.tabName) + "] ( \n");
        for (int ii = 0; ii < def.colList.size(); ii++) {
            ColumnDef col = def.colList.get(ii);
            boolean isFinal = (ii == def.colList.size() - 1);
            sb.append(toColString(col, isFinal) + " \n");
            
            if(def.colList.size() - 1 == ii){
                sb.append(getPkConfigStr(def));
            }
        }
        sb.append(");\n");
        sb.append(getTabComment(def) + "\n");
        for (int ii = 0; ii < def.colList.size(); ii++) {
            ColumnDef col = def.colList.get(ii);
            sb.append(getColComment(def.tabName, col) + "\n");
        }
        return sb.toString();
    }

    // CREATE TABLE [dbo].[AS_Agent_Flow_Detail](
    // [ID] [int] IDENTITY(1,1) NOT NULL,
    // [Agent_Flow_Version_ID] [int] NOT NULL,
    // [Step_No] [numeric](1, 0) NULL,
    // [Flow_Role_ID] [int] NOT NULL,
    // [EndPoint] [bit] NULL,
    // [IsEmail] [bit] NULL,
    // CONSTRAINT [PK_AS_AgentFlow_Detail] PRIMARY KEY CLUSTERED
    // );

    private String toColString(ColumnDef col, boolean isFinal) {
        // String comma = isFinal ? " " : " , ";
        String comma = " , ";
        String nullAble = StringUtils.isNotBlank(col.nn) ? " not null " : " null ";
        String str = "    [" + StringUtils.trim(col.english) + "] " + getDataType(col) + " " + nullAble + " " + comma; // + " -- " + col.chinese;
        return str;
    }

    private String getPkConfigStr(TableDef def) {
        StringBuilder sb = new StringBuilder();
        List<String> pkList = new ArrayList<String>();
        for (ColumnDef d : def.colList) {
            if (StringUtils.isNotBlank(d.key)) {
                pkList.add(d.english);
            }
        }
        sb.append("  CONSTRAINT [PK_" + StringUtils.trim(def.tabName) + "] PRIMARY KEY CLUSTERED \n");
        sb.append(" ( \n");
        for (int ii = 0; ii < pkList.size(); ii++) {
            String comma = ", ";
            if(ii == pkList.size() - 1){
                comma = " ";
            }
            sb.append("     [" + pkList.get(ii) + "] ASC " + comma + "  \n");
        }
        sb.append(" )WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]  \n");
        return sb.toString();
    }

    private String getDataType(ColumnDef col) {
        if (StringUtils.isNotBlank(col.key)) {
            return " IDENTITY(1,1) ";
        } else {
            return " " + col.type + " ";
        }
    }

    private String getTabComment(TableDef def) {
        return String.format("comment on table %s is '%s';", def.tabName, def.tabNameChs);
    }

    private String getColComment(String tableName, ColumnDef col) {
        return String.format("comment on column \"%s\".\"%s\" is '%s';", tableName, col.english, col.chinese);
    }
}
