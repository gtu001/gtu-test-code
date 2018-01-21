package gtu.db.simple_dao_gen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class GenCustomSelectMethod {

    public static void main(String[] args) {
        GenCustomSelectMethod t = new GenCustomSelectMethod();

        String sql = "select * from user_info \n" + //
                " where id = :id \n" + //
                " and id != :not_id \n" + //
                " and user_name like :user_name || '%' \n" + //
                " and create_date between :create_date_start and :create_date_end ";//

        Map<String, Class<?>> map = new LinkedHashMap<String, Class<?>>();
        map.put("id", String.class);
        map.put("not_id", String.class);
        map.put("user_name", String.class);
        map.put("create_date_start", Timestamp.class);
        map.put("create_date_end", Timestamp.class);

        String resultStr = t.execute("queryUserTest", sql, map);
        System.out.println(resultStr);
        System.out.println("done...");
    }

    private String methodName;
    private String fromSql;
    private Map<String, Class<?>> params;

    private List<String> keyLst;
    private String toSql;

    public String execute(String methodName, String fromSql, Map<String, Class<?>> params) {
        this.methodName = methodName;
        this.fromSql = fromSql;
        this.params = params;

        this.initKeyLst();
        this.checkKeyLst();
        String rtnMethodStr = this.parseToMethod();
        return rtnMethodStr;
    }
    
    private void checkKeyLst() {
        Set<String> k1 = new TreeSet<String>(keyLst);
        Set<String> k2 = new TreeSet(params.keySet());
        if(!k1.equals(k2)) {
            throw new RuntimeException(k1 + " <> " + k2);
        }
    }

    private void initKeyLst() {
        Pattern ptn = Pattern.compile("\\:(\\w+)", Pattern.MULTILINE | Pattern.DOTALL);
        Matcher mth = ptn.matcher(fromSql);
        StringBuffer sb = new StringBuffer();
        keyLst = new ArrayList<String>();
        while (mth.find()) {
            String key = mth.group(1);
            mth.appendReplacement(sb, "?");
            keyLst.add(key);
        }
        mth.appendTail(sb);
        toSql = getToSql(sb.toString());
    }

    private String getToSql(String questionSql) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new StringReader(questionSql));
            for (String line = null; (line = reader.readLine()) != null;) {
                sb.append("\" " + line + " \" + //\n");
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String parseToMethod() {
        List<String> methodParamLst = new ArrayList<String>();
        for (String key : params.keySet()) {
            methodParamLst.add(params.get(key).getName() + " " + key);
        }

        StringBuilder sb2 = new StringBuilder();
        for (int ii = 0; ii < keyLst.size(); ii++) {
            sb2.append("stmt.setObject(" + (ii + 1) + ", " + keyLst.get(ii) + ");\n");
        }

        String resultMethodStr = MessageFormat.format(METHOD_STR, new Object[] { methodName, StringUtils.join(methodParamLst, ","), toSql, sb2 });
        return resultMethodStr;
    }

    private static String METHOD_STR;

    {
        StringBuilder sb = new StringBuilder();
        sb.append("    public List<Map<String, Object>> {0} (                                                 \n");
        sb.append("                {1}                                                                       \n");
        sb.append("                , Connection conn)  '{'                                  \n");
        sb.append("                                                                                          \n");
        sb.append("        List<Map<String, Object>> rsList = new ArrayList<Map<String, Object>>();          \n");
        sb.append("        java.sql.ResultSet rs = null;                                                     \n");
        sb.append("        PreparedStatement stmt = null;                                                    \n");
        sb.append("        try '{'                                                                             \n");
        sb.append("            String sql = {2};                                                         \n");
        sb.append("            stmt = conn.prepareStatement(sql);                                            \n");
        sb.append("            {3}                                                                             \n");
        sb.append("            rs = stmt.executeQuery();                                                     \n");
        sb.append("            java.sql.ResultSetMetaData mdata = rs.getMetaData();                          \n");
        sb.append("            int cols = mdata.getColumnCount();                                            \n");
        sb.append("            List<String> colList = new ArrayList<String>();                               \n");
        sb.append("            for (int i = 1; i <= cols; i++) '{'                                             \n");
        sb.append("                colList.add(mdata.getColumnName(i).toUpperCase());                        \n");
        sb.append("            }                                                                             \n");
        sb.append("                                                                                          \n");
        sb.append("            while (rs.next()) '{'                                                           \n");
        sb.append("                Map<String, Object> map = new LinkedHashMap<String, Object>();            \n");
        sb.append("                for (String col : colList) '{'                                              \n");
        sb.append("                    map.put(col, rs.getObject(col));                                      \n");
        sb.append("                }                                                                         \n");
        sb.append("                rsList.add(map);                                                          \n");
        sb.append("            }                                                                             \n");
        sb.append("        } catch (Exception e) '{'                                                        \n");
        sb.append("            throw RuntimeException(e);                                                                      \n");
        sb.append("        } finally '{'                                                                       \n");
        sb.append("            try '{'                                                                         \n");
        sb.append("                rs.close();                                                               \n");
        sb.append("            } catch (Exception ex) '{'                                                      \n");
        sb.append("                ex.printStackTrace();                                                     \n");
        sb.append("            }                                                                             \n");
        sb.append("            try '{'                                                                         \n");
        sb.append("                stmt.close();                                                             \n");
        sb.append("            } catch (Exception ex) '{'                                                      \n");
        sb.append("                ex.printStackTrace();                                                     \n");
        sb.append("            }                                                                             \n");
        sb.append("        }                                                                                 \n");
        sb.append("        return rsList;                                                                    \n");
        sb.append("    }                                                                                     \n");
        METHOD_STR = sb.toString();
    }
}
