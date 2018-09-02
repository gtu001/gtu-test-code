package gtu.db.jdbc;

import gtu.console.SystemInUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CallableStatementGenUtil {

    public static void main(String[] args) throws IOException {
        CallableStatementGenUtil t = new CallableStatementGenUtil();

        Pattern ptn = Pattern.compile("(\\w+)\\s+(in|out)\\s+(\\w+)\\,?\\s*(-*\\s*[\u4e00-\u9fa5]*)", Pattern.DOTALL | Pattern.MULTILINE);

        String text = SystemInUtil.readContent();

        List<CallParam> list = new ArrayList<CallParam>();

        Matcher mth = ptn.matcher(text);
        while (mth.find()) {
            String param = mth.group(1);
            String inOut = mth.group(2);
            String type = mth.group(3);
            String comment = mth.group(4);
            CallParam p = new CallParam();
            p.param = param;
            p.inOut = inOut;
            p.type = type;
            p.comment = comment;
            list.add(p);
        }

        for (int ii = 0; ii < list.size(); ii++) {
            CallParam p = list.get(ii);
            if ("in".equals(p.inOut)) {
                System.out.println("call.set" + t.getInType(p.type) + "(" + (ii + 1) + ", xxxxxxx);//" + p.comment + " " + p.param);
            } else if ("out".equals(p.inOut)) {
                System.out.println("call.registerOutParameter(" + (ii + 1) + ", " + t.getOutType(p.type) + ");//" + p.comment + " " + p.param);
            } else {
                System.out.println("call.set" + t.getInType(p.type) + "(" + (ii + 1) + ", xxxxxxx);//" + p.comment + " " + p.param);
                System.out.println("call.registerOutParameter(" + (ii + 1) + ", " + t.getOutType(p.type) + ");//" + p.comment + " " + p.param);
            }
        }
        
        for (int ii = 0; ii < list.size(); ii++) {
            CallParam p = list.get(ii);
            String realType = t.getRealJavaType(p.type);
            System.out.println(realType + " " + p.param + " = call.get" + realType + "("+(ii + 1)+"); //" + p.comment);
        }
        System.out.println("done...");
    }

    private String getOutType(String type) {
        if ("varchar2".equals(type)) {
            return "Types.VARCHAR";
        } else if ("date".equals(type)) {
            return "Types.TIMESTAMP";
        } else if ("number".equals(type)) {
            return "Types.NUMERIC";
        } else if ("char".equals(type)) {
            return "Types.CHAR";
        }
        throw new RuntimeException("查無 OutType : " + type);
    }
    
    private String getRealJavaType(String type){
        if ("varchar2".equals(type)) {
            return "String";
        } else if ("date".equals(type)) {
            return "Timestamp";
        } else if ("number".equals(type)) {
            return "BigDecimal";
        } else if ("char".equals(type)) {
            return "String";
        }
        throw new RuntimeException("查無 realType : " + type);
    }

    private String getInType(String type) {
        if ("varchar2".equals(type)) {
            return "String";
        } else if ("date".equals(type)) {
            return "Date";
        } else if ("number".equals(type)) {
            return "Long";
        }
        throw new RuntimeException("查無 inType : " + type);
    }

    private static class CallParam {
        String param;
        String inOut;
        String type;
        String comment;
    }
}
