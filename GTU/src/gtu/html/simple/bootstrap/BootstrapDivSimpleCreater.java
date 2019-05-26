package gtu.html.simple.bootstrap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
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

        Pattern ptnLineDef = Pattern.compile("(.*?)[\\s\\t]+?(.*)");
        Pattern ptnTagDef = Pattern.compile("(\\w+)\\[(\\w+)\\]");

        List<List<Div>> totalLst = new ArrayList<List<Div>>();

        Matcher mth = null;
        Matcher mth2 = null;
        List<Div> tmpLst = null;
        for (String line : lst) {
            if (tmpLst == null) {
                tmpLst = new ArrayList<Div>();
            }
            mth = ptnLineDef.matcher(line);
            if (mth.find()) {
                Div d1 = new Div();
                d1.label = mth.group(1);
                String others = mth.group(2);
                if (StringUtils.isNotBlank(others)) {
                    mth2 = ptnTagDef.matcher(others);
                    while (mth2.find()) {
                        DivDtl d2 = new DivDtl();
                        d2.name = mth2.group(1);
                        d2.tagId = mth2.group(2);
                        d1.divLst.add(d2);
                    }
                }
                tmpLst.add(d1);
            } else if (StringUtils.isNotBlank(line)) {
                Div d = new Div();
                d.label = StringUtils.trimToEmpty(line);
                setCustomColRowConfig(d.label, d);
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

            int size = caculateSize(tmpLst);
            for (int jj = 0; jj < tmpLst.size(); jj++) {
                Div div = tmpLst.get(jj);

                if ("授權註記".equals(div.label)) {
                    System.out.println("ddddddddddddddd");
                }

                AtomicInteger bootstrap = new AtomicInteger(12);

                if (!div.divLst.isEmpty()) {
                    int left = getDivNumber(size, jj, false, bootstrap);
                    int right = getDivNumber(size, jj + 1, false, bootstrap);
                    List<String> inputLst = new ArrayList<String>();
                    for (DivDtl d2 : div.divLst) {
                        String htmlTag = boot.getTagTemplate(d2.tagId);
                        String realHtmlTag = String.format(htmlTag, d2.name, div.label);
                        inputLst.add(realHtmlTag);
                    }
                    String divTag = boot.getTD(BOOTSTRAP_DIV_PREFIX + left, BOOTSTRAP_DIV_PREFIX + right);
                    String realDivTag = String.format(divTag, div.label, StringUtils.join(inputLst, "\n"));

                    sb.append(realDivTag);
                } else {
                    int col = 0;
                    if (div.col != 0) {
                        col = div.col;
                    } else {
                        col = getDivNumber(size, jj, true, bootstrap);
                    }
                    String bt1 = BOOTSTRAP_DIV_PREFIX + col;
                    String divFormat = "\t<div class=\"%1$s\">%2$s</div> \n";
                    String realDivTag = String.format(divFormat, bt1, div.label);

                    sb.append(realDivTag);
                }
            }
            sb.append("</div>\n");
        }
        return sb.toString();
    }

    private void setCustomColRowConfig(String str, ColRowConfig div) {
        Pattern ptnCol = Pattern.compile("\\[[cC](\\d+)\\]");
        Pattern ptnRow = Pattern.compile("\\[[rR](\\d+)\\]");
        str = StringUtils.defaultString(str);
        Matcher mth = ptnCol.matcher(str);
        if (mth.find()) {
            div.col = Integer.valueOf(mth.group(1));
        }
        mth = ptnRow.matcher(str);
        if (mth.find()) {
            div.row = Integer.valueOf(mth.group(1));
        }
    }

    private int caculateSize(List<Div> tmpLst) {
        int total = 0;
        for (Div d : tmpLst) {
            if (d.divLst.isEmpty()) {
                total += 1;
            } else {
                total += 2;
            }
        }
        return total;
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
                    String matchTemplate = h.getTemplate();
                    if (StringUtils.isBlank(matchTemplate)) {
                        throw new RuntimeException("TagId : " + tagId2 + ", template 為空錯誤!!");
                    }
                    return matchTemplate;
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

    private int getDivNumber(int size, int index, boolean isLabelOnly, final AtomicInteger bootstrap) {
        if (size > 12) {
            throw new RuntimeException("欄位不可超過12個 ! ,  index : " + index + ", size : " + size);
        }
        if (index + 1 > size) {
            throw new RuntimeException("index不可超過size , index : " + index + ", size : " + size);
        }

        int rtnVal = 0;
        switch (size) {
        case 1:
            rtnVal = 12;
            break;
        case 2:
            rtnVal = 6;
            break;
        case 3:
            rtnVal = 4;
            break;
        case 4:
            rtnVal = 3;
            break;
        case 5:
            if (isLabelOnly) {
                rtnVal = 4;
            } else {
                rtnVal = 2;
            }
            break;
        case 6:
            rtnVal = 2;
            break;
        default:
            int fix = (12 - size) % 6;
            if (index + 1 > fix) {
                rtnVal = 1;
            } else {
                rtnVal = 2;
            }
            break;
        }

        if (rtnVal > bootstrap.get()) {
            rtnVal = bootstrap.get();
        }
        bootstrap.set(bootstrap.get() - rtnVal);
        return rtnVal;
    }

    private static class ColRowConfig {
        int col = 0;
        int row = 0;
    }

    private static class Div extends ColRowConfig {
        String label;
        List<DivDtl> divLst = new ArrayList<DivDtl>();

    }

    private static class DivDtl extends ColRowConfig {
        String name;
        String tagId;
        String customValue = "";
    }
}
