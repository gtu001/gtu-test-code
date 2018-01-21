package gtu.db.oracle;

import java.text.MessageFormat;

public class OracleNewPK {
    
    private static final String GET_LASTEST_APPLY_ID;

    public static void main(String[] args) {
        String prefix = "FRM";
        Integer seq = 3;
        Integer seq2 = seq-1;
        String tableName = "TX_FORM";
        String pk = "TX_FORM_ID";
        
        Object[] obj = new Object[]{prefix,seq,seq2,tableName,pk};
        String vvv = MessageFormat.format(GET_LASTEST_APPLY_ID, obj);
        System.out.println(vvv);
    }
    
    public String getSequenceIdSQL(){
        return "select AD_SEQUENCE.Nextval as seq from dual";
    }
    
    static {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ''{0}'' || to_char(sysdate, ''yyyyMMdd'') ||                                             ");
        sql.append("        LPAD(substr(t.{4}, length(t.{4}) - {2}, length(t.{4})) + 1, {1}, ''0'')                  ");
        sql.append("   from (select ''{0}'' || to_char(sysdate, ''yyyyMMdd'') || LPAD(''0'',3,''0'') as {4}          ");
        sql.append("           from dual                                                                             ");
        sql.append("         union                                                                                   ");
        sql.append("         select {4}                                                                              ");
        sql.append("           from (select {4}                                                                      ");
        sql.append("                   from {3}                                                                      ");
        sql.append("                  where {4} like                                                                 ");
        sql.append("                        ''{0}'' || to_char(sysdate, ''yyyyMMdd'') || ''%''                       ");
        sql.append("                  order by {4} desc) t                                                           ");
        sql.append("          where rownum = 1) t                                                                    ");
        GET_LASTEST_APPLY_ID = sql.toString();
    }
}
