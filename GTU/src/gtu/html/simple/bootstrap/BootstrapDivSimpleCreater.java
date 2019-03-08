package gtu.html.simple.bootstrap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;

import gtu.console.SystemInUtil;
import gtu.html.simple.HtmlInputSimpleCreater.HtmlInputSimpleCreater_HtmlType;
import gtu.html.simple.HtmlInputSimpleCreater.HtmlInputSimpleCreater_HtmlTypeHandler;
import gtu.string.StringUtil_;
import gtu.yaml.util.YamlMapUtil;

public class BootstrapDivSimpleCreater {

    public static void main(String[] args) throws FileNotFoundException {
        BootstrapDivSimpleCreater t = new BootstrapDivSimpleCreater();
        FileInputStream is = new FileInputStream("D:/workstuff/gtu-test-code/GTU/src/gtu/html/simple/HtmlInputSimpleCreater_Thymeleaf.yaml");
        String htmlContent = SystemInUtil.readContent();
        String result = t.execute(is, htmlContent);
        System.out.println(result);
        System.out.println("done...");
    }

    private static final String BOOTSTRAP_DIV_PREFIX = "col-sm-";
    
    public String execute(InputStream yamlConfigInputStream, String htmlContent) {
        BootstrapDivCrater_DivRoot boot = BootstrapDivCrater_DivRoot.load(yamlConfigInputStream);
        List<String> lst = StringUtil_.readContentToList(htmlContent, true, false, false);

        Pattern ptn = Pattern.compile("(.*?)\\s+(\\w+)\\[(\\w+)\\]");

        List<List<Div>> totalLst = new ArrayList<List<Div>>();

        Matcher mth = null;
        List<Div> tmpLst = null;
        for (String line : lst) {
            if (tmpLst == null) {
                tmpLst = new ArrayList<Div>();
            }
            mth = ptn.matcher(line);
            if (mth.find()) {
                Div d = new Div();
                d.label = mth.group(1);
                d.name = mth.group(2);
                d.tagId = mth.group(3);
                tmpLst.add(d);
            } else if (StringUtils.isNotBlank(line)) {
                Div d = new Div();
                d.label = StringUtils.trimToEmpty(line);
                d.name = "";
                d.tagId = "";
                tmpLst.add(d);
            } else {
                if (tmpLst != null && !tmpLst.isEmpty()) {
                    totalLst.add(tmpLst);
                    tmpLst = null;
                }
            }
        }
        if (tmpLst != null && !tmpLst.isEmpty()) {
            totalLst.add(tmpLst);
            tmpLst = null;
        }

        StringBuilder sb = new StringBuilder();
        for (int ii = 0; ii < totalLst.size(); ii++) {
            sb.append("<!-- row[" + (ii + 1) + "] -->\n");
            sb.append("<div class=\"row\">\n");
            tmpLst = totalLst.get(ii);
            for (int jj = 0, size = tmpLst.size() * 2; jj < tmpLst.size(); jj++) {
                Div div = tmpLst.get(jj);

                if (div.isNoInputTag == false) {
                    int left = getDivNumber(size, jj);
                    int right = getDivNumber(size, jj + 1);
                    String htmlTag = boot.getTagTemplate(div.tagId);
                    String realHtmlTag = String.format(htmlTag, div.name, div.label);
                    String divTag = boot.getTD(BOOTSTRAP_DIV_PREFIX + left, BOOTSTRAP_DIV_PREFIX + right);
                    String realDivTag = String.format(divTag, div.label, realHtmlTag);

                    sb.append(realDivTag).append("\n\n");
                } else {
                    String bt1 = BOOTSTRAP_DIV_PREFIX + 12;
                    String divFormat = "\t<div class=\"%1$s\">%2$s</div> \n";
                    String realDivTag = String.format(divFormat, bt1, div.label);

                    sb.append(realDivTag).append("\n\n");
                }
            }
            sb.append("</div>\n");
        }
        return sb.toString();
    }

    public static class BootstrapDivCrater_DivRoot extends HtmlInputSimpleCreater_HtmlTypeHandler {
        public static BootstrapDivCrater_DivRoot load(InputStream is) {
            Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
            classMap.put("tagLst", HtmlInputSimpleCreater_HtmlType.class);
            BootstrapDivCrater_DivRoot self = YamlMapUtil.getInstance().loadFromFile(is, BootstrapDivCrater_DivRoot.class, classMap);
            System.out.println("##########");
            System.out.println(ReflectionToStringBuilder.toString(self, ToStringStyle.MULTI_LINE_STYLE));
            System.out.println("##########");
            return self;
        }

        public String getTagTemplate(String tagId) {
            tagId = StringUtils.trimToEmpty(tagId);
            for (HtmlInputSimpleCreater_HtmlType h : getTagLst()) {
                String tagId2 = StringUtils.trimToEmpty(h.getTagId());
                if (StringUtils.equalsIgnoreCase(tagId, tagId2)) {
                    return h.getTemplate();
                }
            }
            throw new RuntimeException("不支援tagId : " + tagId);
        }

        public String getTD() {
            throw new UnsupportedOperationException();
        }

        public String getTD(String bt1, String bt2) {
            if (StringUtils.isBlank(bt1) || StringUtils.isBlank(bt2)) {
                throw new RuntimeException("bt1, bt2 不可為空");
            }
            bt1 = StringUtils.trimToEmpty(bt1);
            bt2 = StringUtils.trimToEmpty(bt2);
            td1 = StringUtils.trimToEmpty(td1);
            td2 = StringUtils.trimToEmpty(td2);
            if (StringUtils.isNotBlank(td1)) {
                td1 = " " + td1;
            }
            if (StringUtils.isNotBlank(td2)) {
                td2 = " " + td2;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("\t<div class=\"" + bt1 + td1 + "\">%1$s</div>                                    \n");
            sb.append("\t<div class=\"" + bt2 + td2 + "\">                                          \n");
            sb.append("\t%2$s");
            sb.append("\t</div>                                                                       \n");
            return sb.toString();
        }
    }

    private int getDivNumber(int size, int index) {
        if (size > 12) {
            throw new RuntimeException("欄位不可超過12個 ! ,  index : " + index + ", size : " + size);
        }
        if (index + 1 > size) {
            throw new RuntimeException("index不可超過size , index : " + index + ", size : " + size);
        }
        switch (size) {
        case 1:
            return 12;
        case 2:
            return 6;
        case 3:
            return 4;
        case 4:
            return 3;
        case 5:
            switch (index) {
            case 0:
            case 1:
                return 3;
            default:
                return 2;
            }
        case 6:
            return 2;
        default:
            int fix = (12 - size) % 6;
            if (index + 1 > fix) {
                return 1;
            }
            return 2;
        }
    }

    private static class Div {
        String label;
        String name;
        String tagId;
        boolean isNoInputTag = false;
    }
}
