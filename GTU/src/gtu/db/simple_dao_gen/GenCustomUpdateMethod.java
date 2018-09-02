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

public class GenCustomUpdateMethod {

    public static void main(String[] args) {
        GenCustomUpdateMethod t = new GenCustomUpdateMethod();

        String sql = "update user_info \n" + //
                " set user_name = :user_name \n" + //
                " where id = :id \n"; //

        Map<String, Class<?>> map = new LinkedHashMap<String, Class<?>>();
        map.put("id", String.class);
        map.put("user_name", String.class);

        String resultStr = t.execute("updateUserName", sql, map);
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
        
        sb.append("    public int {0}(                                                       \n");
        sb.append("                {1}                                                       \n");
        sb.append("                , Connection conn) '{'                                      \n");
        sb.append("        PreparedStatement stmt = null;                                    \n");
        sb.append("        try '{'                                                             \n");
        sb.append("            conn.setAutoCommit(false);                                    \n");
        sb.append("            String sql = {2};                                               \n");
        sb.append("            stmt = conn.prepareStatement(sql);                            \n");
        sb.append("            {3}                                                             \n");
        sb.append("            int result = stmt.executeUpdate();                            \n");
        sb.append("            System.out.println(\"update result : \" + result);            \n");
        sb.append("            // conn.commit();                                             \n");
        sb.append("            return result;                                                \n");
        sb.append("        } catch (Exception e) '{'                                           \n");
        sb.append("            e.printStackTrace();                                          \n");
        sb.append("            try '{'                                                         \n");
        sb.append("                conn.rollback();                                          \n");
        sb.append("            } catch (SQLException e1) '{'                                   \n");
        sb.append("                e1.printStackTrace();                                     \n");
        sb.append("            }                                                             \n");
        sb.append("            throw new RuntimeException(e);                                \n");
        sb.append("        } finally '{'                                                       \n");
        sb.append("            try'{'                                                          \n");
        sb.append("                stmt.close();                                             \n");
        sb.append("            }catch(Exception ex)'{'                                         \n");
        sb.append("                ex.printStackTrace();                                     \n");
        sb.append("            }                                                             \n");
        sb.append("        }                                                                 \n");
        sb.append("    }                                                                     \n");
        
        METHOD_STR = sb.toString();
    }
}
