package com.cathay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;

import gtu.console.SystemInUtil;

public class CathayMethodCreater {

    InputStream in;
    PrintStream out;

    static final String METHOD_STR;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("    /**                                             \n");
        sb.append("{4}                                                 \n");
        sb.append("     */                                             \n");
        // sb.append(" @SuppressWarnings(\"unchecked\") \n");
        sb.append("    public {1} {0}({2}) throws ModuleException '{'  \n");
        sb.append("        {5}                                         \n");
        sb.append("        {3}                                         \n");
        sb.append("    }                                               \n");
        METHOD_STR = sb.toString();
    }

    public static void main(String[] args) throws Exception {
        CathayMethodCreater t = new CathayMethodCreater();
        t.execute(System.in, System.out);
        System.out.println("done...");
    }

    public void execute(InputStream in, PrintStream out) throws IOException {
        this.in = in;
        this.out = out;
        BufferedReader reader = new BufferedReader(new StringReader(SystemInUtil.readContent(in)));
        StringBuilder sb = new StringBuilder();
        MethodClass clzObj = new MethodClass();
        int ii = 0;
        int descIndex = -1;
        int paramIndex = -1;
        int returnIndex = -1;
        String tmpDesc = "";
        for (String line = null; (line = reader.readLine()) != null;) {
            ii++;
            sb.append(line + '\n');

            // 處理
            if (ii == 1) {
                clzObj.methodName = getMatchStr(line, "\\w+", 0);
                if (line.contains(")")) {
                    tmpDesc = line.substring(line.indexOf(")") + 1);
                }
            }

            if (descIndex == -1 && StringUtils.trim(line).startsWith("功能說明")) {
                String desc = StringUtils.trim(line).replaceAll("功能說明[\\s\\t]*", "");
                clzObj.desc.add(desc);
            } else if (descIndex != -1 && !StringUtils.trim(line).contains("類型")) {
                clzObj.desc.add(StringUtils.trim(line));
            } else if (descIndex != -1 && StringUtils.trim(line).contains("類型")) {
                descIndex = -1;
            }

            if (returnIndex == -1) {
                if (paramIndex == -1 && StringUtils.trim(line).startsWith("項次")) {
                    paramIndex = paramIndex + 1;
                } else if (paramIndex != -1 && ii >= paramIndex) {
                    ParamClass p = new ParamClass();
                    p.name = getMatchStr(line, "\\d+\\.[\\t\\s]+(\\w+)[\\s\\t]+([\\w\\<\\>]+)[\\s\\t]+(.+)", 1);
                    p.type = getMatchStr(line, "\\d+\\.[\\t\\s]+(\\w+)[\\s\\t]+([\\w\\<\\>]+)[\\s\\t]+(.+)", 2);
                    p.desc = getMatchStr(line, "\\d+\\.[\\t\\s]+(\\w+)[\\s\\t]+([\\w\\<\\>]+)[\\s\\t]+(.+)", 3);
                    if (StringUtils.isNotBlank(p.name)) {
                        clzObj.params.add(p);
                    } else {
                        ParamClass p1 = new ParamClass();
                        p1.name = getMatchStr(line, "\\d+\\.[\\t\\s]+(\\w+)[\\s\\t]+([\\w\\<\\>]+)", 1);
                        p1.type = getMatchStr(line, "\\d+\\.[\\t\\s]+(\\w+)[\\s\\t]+([\\w\\<\\>]+)", 2);
                        p1.desc = "";
                        if (StringUtils.isNotBlank(p1.name)) {
                            clzObj.params.add(p1);
                        }
                    }
                } else if (paramIndex != -1 && StringUtils.trim(line).contains("輸出參數")) {
                    paramIndex = -1;
                }
            }

            if (returnIndex == -1 && StringUtils.trim(line).contains("輸出參數")) {
                returnIndex = paramIndex + 1;
            } else if (returnIndex != -1 && ii >= returnIndex && clzObj.rtnObj == null) {
                ParamClass p = new ParamClass();
                p.name = getMatchStr(line, "\\d+\\.[\\t\\s]+(\\w+)[\\s\\t]+([\\w\\<\\>]+)[\\s\\t]+(.+)", 1);
                p.type = getMatchStr(line, "\\d+\\.[\\t\\s]+(\\w+)[\\s\\t]+([\\w\\<\\>]+)[\\s\\t]+(.+)", 2);
                p.desc = getMatchStr(line, "\\d+\\.[\\t\\s]+(\\w+)[\\s\\t]+([\\w\\<\\>]+)[\\s\\t]+(.+)", 3);
                if (StringUtils.isNotBlank(p.name)) {
                    clzObj.rtnObj = p;
                }
            } else if (returnIndex != -1 && StringUtils.trim(line).contains("輸出參數")) {
                returnIndex = -1;
            }
        }
        if (clzObj.desc.isEmpty() && StringUtils.isNotBlank(tmpDesc)) {
            clzObj.desc.add(tmpDesc);
        }
        reader.close();

        this.out.println(ReflectionToStringBuilder.toString(clzObj, ToStringStyle.MULTI_LINE_STYLE));
        this.out.println(clzObj.getMethodString());
        this.out.println();
        this.out.println(new TestCaseMethodCreater(clzObj).getResult());
        this.out.println("done...");
    }

    private static class ParamClass {
        String name;
        String type;
        String desc;

        @Override
        public String toString() {
            return "ParamClass [name=" + name + ", type=" + type + ", desc=" + desc + "]";
        }
    }

    private static class MethodClass {
        String methodName;
        List<String> desc = new ArrayList<String>();
        List<ParamClass> params = new ArrayList<ParamClass>();
        ParamClass rtnObj;
        private ValidateCheckClass validateCheckClass = new ValidateCheckClass();

        @Override
        public String toString() {
            return "MethodClass [methodName=" + methodName + ", desc=" + desc + ", params=" + params + ", rtnObj=" + rtnObj + "]";
        }

        private String getParamStr() {
            List<String> lst = new ArrayList<String>();
            for (ParamClass p : params) {
                lst.add(fixType(p.type) + " " + p.name);
            }
            return StringUtils.join(lst, ", ");
        }

        private String fixType(String type) {
            if ("string".equalsIgnoreCase(type)) {
                return "String";
            }
            return type;
        }

        private String getReturnStr() {
            if (!"void".equals(rtnObj.type)) {
                String str = fixType(rtnObj.type) + " " + rtnObj.name + " = new " + fixType(rtnObj.type) + "();" + "\n";
                str += "return " + rtnObj.name + ";";
                return str;
            }
            return "";
        }

        private String getJavaDoc() {
            List<String> docLst = new ArrayList<String>();
            for (String str : desc) {
                docLst.add(String.format("	 * %s                                 \n", str));
            }
            for (ParamClass p : params) {
                docLst.add(String.format("	 * @param %s %s                                 \n", p.name, p.desc));
            }
            docLst.add(String.format("	 * @return %s %s                                    \n", rtnObj.name, rtnObj.type));
            return StringUtils.join(docLst, "");
        }

        private String getValidateCheck() {
            List<String> lst = new ArrayList<String>();
            if (params.size() != 1) {
                lst.add(validateCheckClass.getPrefix());
            }
            for (ParamClass p : params) {
                String chkStr = "";
                if ("string".equalsIgnoreCase(p.type)) {
                    chkStr = " StringUtils.isBlank(" + p.name + ") ";
                } else if (p.type.toLowerCase().startsWith("map") || p.type.toLowerCase().startsWith("list")) {
                    chkStr = String.format(" %1$s == null || %1$s.isEmpty() ", p.name);
                } else {
                    chkStr = p.name + " == null ";
                }
                if (params.size() == 1) {
                    lst.add(MessageFormat.format(validateCheckClass.getSingleFmt(), new Object[] { chkStr, StringUtils.defaultIfBlank(p.desc, "輸入參數") }));
                } else {
                    lst.add(MessageFormat.format(validateCheckClass.getMulitFmt(), new Object[] { chkStr, StringUtils.defaultIfBlank(p.desc, "輸入參數") }));
                }
            }
            if (params.size() != 1) {
                lst.add(validateCheckClass.getSuffix());
            }
            return StringUtils.join(lst, "");
        }

        public String getMethodString() {
            if (rtnObj == null) {
                rtnObj = new ParamClass();
                rtnObj.name = "";
                rtnObj.type = "void";
                rtnObj.desc = "";
            }
            return MessageFormat.format(METHOD_STR, new Object[] { methodName, fixType(rtnObj.type), getParamStr(), getReturnStr(), getJavaDoc(), getValidateCheck() });
        }
    }

    private static class ValidateCheckClass {
        private String prefix = "        ErrorInputException eie = null; \r\n";
        private String singleFmt;
        private String mulitFmt;
        private String suffix;

        private ValidateCheckClass() {
            StringBuilder sb = new StringBuilder();
            sb.append("        if ({0}) '{'          \r\n");
            sb.append("            throw new ErrorInputException(\"{1}參數錯誤\"); \r\n");
            sb.append("        }                                                  \r\n");
            singleFmt = sb.toString();
            sb.setLength(0);

            sb.append("        if ({0}) '{'                                                 \r\n");
            sb.append("            eie = getErrorInputException(eie, \"{1}參數錯誤\");      \r\n");
            sb.append("        }                                                          \r\n");
            mulitFmt = sb.toString();
            sb.setLength(0);

            sb.append("        if (eie != null) {           \r\n");
            sb.append("            throw eie;               \r\n");
            sb.append("        }                            \r\n");
            suffix = sb.toString();
            sb.setLength(0);
        }

        public String getPrefix() {
            return prefix;
        }

        public String getSingleFmt() {
            return singleFmt;
        }

        public String getMulitFmt() {
            return mulitFmt;
        }

        public String getSuffix() {
            return suffix;
        }
    }

    private static class TestCaseMethodCreater {
        String format;
        String result;

        private TestCaseMethodCreater(MethodClass mth) {
            StringBuilder sb = new StringBuilder();
            sb.append("                                                                                     \r\n");
            sb.append("    public void test_{0}() '{'                                                         \r\n");
            sb.append("        log.debug(\"====test_{0} 1.負向(P)=====\");                                    \r\n");
            sb.append("        try '{'                                                                        \r\n");
            sb.append("            {1}                                                                      \r\n");
            sb.append("            {4} mXXXXXXXXXXX.{0}({2});                                        \r\n");
            sb.append("            fail();                                                                  \r\n");
            sb.append("        } catch (ErrorInputException e1) '{'                                           \r\n");
            sb.append("            log.debug(\"====test_{0} 1.負向(成功)=====\" + e1.getMessage());           \r\n");
            sb.append("        } catch (ModuleException e1) '{'                                               \r\n");
            sb.append("            log.debug(\"====test_{0} 1.負向(成功)=====\" + e1.getMessage());           \r\n");
            sb.append("        } catch (Exception e) '{'                                                      \r\n");
            sb.append("            log.debug(\"====test_{0} 1.負向(失敗)=====\" + e.getMessage(), e);         \r\n");
            sb.append("            fail();                                                                  \r\n");
            sb.append("        }                                                                            \r\n");
            sb.append("                                                                                     \r\n");
            sb.append("        log.debug(\"====test_{0} 1.正向(P)=====\");                                    \r\n");
            sb.append("        try '{'                                                                        \r\n");
            sb.append("            {3}                                                                      \r\n");
            sb.append("            {4} mXXXXXXXXXXX.{0}({2});                                        \r\n");
            sb.append("            if ({5}) '{'                                \r\n");
            sb.append("                fail();                                                              \r\n");
            sb.append("            } else '{'                                                                 \r\n");
            sb.append("                log.debug(\"====test_{0} 1.正向成功=====\");                           \r\n");
            sb.append("            }                                                                        \r\n");
            sb.append("        } catch (Exception e) '{'                                                      \r\n");
            sb.append("            log.debug(\"====test_{0} 1.正向失敗(其他錯誤)=====\", e);                  \r\n");
            sb.append("            fail();                                                                  \r\n");
            sb.append("        }                                                                            \r\n");
            sb.append("    }                                                                                \r\n");
            format = sb.toString();
            sb.setLength(0);

            result = MessageFormat.format(format, new Object[] { mth.methodName, getFailParams(mth), getNormalParams(mth), getSuccessParams(mth), getReturnVal(mth), getReturnValidStr(mth) });
        }

        public String getResult() {
            return result;
        }

        private String getReturnValidStr(MethodClass mth) {
            if ("void".equals(mth.rtnObj.type)) {
                return "false";
            }
            ParamClass p = mth.rtnObj;
            String chkStr = "";
            if ("string".equalsIgnoreCase(p.type)) {
                chkStr = " StringUtils.isBlank(" + p.name + ") ";
            } else if (p.type.toLowerCase().startsWith("map") || p.type.toLowerCase().startsWith("list")) {
                chkStr = String.format(" %1$s == null || %1$s.isEmpty() ", p.name);
            } else {
                chkStr = p.name + " == null ";
            }
            return chkStr;
        }

        private String getReturnVal(MethodClass mth) {
            if ("void".equals(mth.rtnObj.type)) {
                return "";
            }
            return StringUtils.capitalize(mth.rtnObj.type) + " " + mth.rtnObj.name + " = ";
        }

        private String getSuccessParams(MethodClass mth) {
            List<String> lst = new ArrayList<String>();
            for (ParamClass p : mth.params) {
                String chkStr = "";
                if ("string".equalsIgnoreCase(p.type)) {
                    chkStr = "String " + p.name + " = \"x\"; ";
                } else if (p.type.toLowerCase().startsWith("map")) {
                    chkStr = String.format(" Map %s = new HashMap(); %s.put(\"XXXX\",\"XXXX\");", p.name);
                } else if (p.type.toLowerCase().startsWith("list")) {
                    chkStr = String.format(" List %s = new ArrayList(); %s.add(\"XXXX\");", p.name);
                } else {
                    chkStr = p.type + " " + p.name + " = new " + p.type + "(); ";
                }
                lst.add(chkStr);
            }
            return StringUtils.join(lst, "\r\n");
        }

        private String getNormalParams(MethodClass mth) {
            List<String> lst = new ArrayList<String>();
            for (ParamClass p : mth.params) {
                lst.add(p.name);
            }
            return StringUtils.join(lst, " , ");
        }

        private String getFailParams(MethodClass mth) {
            List<String> lst = new ArrayList<String>();
            for (ParamClass p : mth.params) {
                String chkStr = "";
                if ("string".equalsIgnoreCase(p.type)) {
                    chkStr = "String " + p.name + " = \"\"; ";
                } else if (p.type.toLowerCase().startsWith("map")) {
                    chkStr = String.format(" Map %s = new HashMap(); ", p.name);
                } else if (p.type.toLowerCase().startsWith("list")) {
                    chkStr = String.format(" List %s = new ArrayList(); ", p.name);
                } else {
                    chkStr = p.type + " " + p.name + " = new " + p.type + "(); ";
                }
                lst.add(chkStr);
            }
            return StringUtils.join(lst, "\r\n");
        }
    }

    private String getMatchStr(String line, String patternStr, int index) {
        Pattern ptn = Pattern.compile(patternStr);
        Matcher mth = ptn.matcher(line);
        if (mth.find()) {
            return mth.group(index);
        }
        this.out.println("<<" + Arrays.asList(line, patternStr, index));
        return "";
    }
}
