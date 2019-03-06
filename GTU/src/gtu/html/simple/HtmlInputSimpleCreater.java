package gtu.html.simple;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;

import gtu.console.SystemInUtil;
import gtu.string.StringUtil_;
import gtu.yaml.util.YamlMapUtil;

public class HtmlInputSimpleCreater {

    public static void main(String[] args) {
        HtmlInputSimpleCreater t = new HtmlInputSimpleCreater();
        String fromData = SystemInUtil.readContent();
        InputStream is = HtmlInputSimpleCreater.class.getResourceAsStream("HtmlInputSimpleCreater_Template.yaml");
        String result = t.execute(is, fromData);
        System.out.println(result);
        System.out.println("done...");
    }

    public String execute(InputStream template, String fromData) {
        HtmlInputSimpleCreater_HtmlTypeHandler htmlTypeHandler = HtmlInputSimpleCreater_HtmlTypeHandler.load(template);
        List<String> lst = StringUtil_.readContentToList(fromData, true, false, false);
        StringBuffer sb = new StringBuffer();
        sb.append("<tr>");
        for (int ii = 0; ii < lst.size(); ii++) {
            String line = lst.get(ii);
            if (StringUtils.isNotBlank(line)) {
                String chineseLabel = getChineseLabel(line);
                Map<String, HtmlInputSimpleCreater_HtmlType> tagMap = getInputGroup(line, htmlTypeHandler);
                String html = getTagGroupHtml(tagMap, chineseLabel);
                sb.append(String.format(htmlTypeHandler.getTD(), chineseLabel, html));
            } else {
                sb.append("</tr>\n<tr>");
            }
        }
        sb.append("</tr>");
        System.out.println(sb.toString());
        return sb.toString();
    }

    private String getChineseLabel(String line) {
        Pattern ptn = Pattern.compile("(.*?)\\s");
        Matcher mth = ptn.matcher(line);
        if (mth.find()) {
            return mth.group(1);
        }
        return "";
    }

    private Map<String, HtmlInputSimpleCreater_HtmlType> getInputGroup(String line, HtmlInputSimpleCreater_HtmlTypeHandler htmlTypeHandler) {
        Pattern ptn = Pattern.compile("(\\w+?)\\[(\\w+)\\]");
        Map<String, HtmlInputSimpleCreater_HtmlType> map = new LinkedHashMap<String, HtmlInputSimpleCreater_HtmlType>();
        Matcher mth = ptn.matcher(line);
        while (mth.find()) {
            String tagName = mth.group(1);
            HtmlInputSimpleCreater_HtmlType tagType = htmlTypeHandler.valueOfType(mth.group(2));
            map.put(tagName, tagType);
        }
        return map;
    }

    private String getTagGroupHtml(Map<String, HtmlInputSimpleCreater_HtmlType> map, String title) {
        StringBuilder sb = new StringBuilder();
        for (String tagName : map.keySet()) {
            HtmlInputSimpleCreater_HtmlType tag = map.get(tagName);
            String html = tag.toHtml(tagName, title);
            sb.append(html);
        }
        return sb.toString();
    }

    public static class HtmlInputSimpleCreater_HtmlTypeHandler {
        String td1;
        String td2;
        List<HtmlInputSimpleCreater_HtmlType> tagLst = new ArrayList<HtmlInputSimpleCreater_HtmlType>();

        private static HtmlInputSimpleCreater_HtmlTypeHandler load(InputStream is) {
            Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
            classMap.put("tagLst", HtmlInputSimpleCreater_HtmlType.class);
            HtmlInputSimpleCreater_HtmlTypeHandler self = YamlMapUtil.getInstance().loadFromFile(is, HtmlInputSimpleCreater_HtmlTypeHandler.class, classMap);
            System.out.println("##########");
            System.out.println(ReflectionToStringBuilder.toString(self, ToStringStyle.MULTI_LINE_STYLE));
            System.out.println("##########");
            return self;
        }

        private HtmlInputSimpleCreater_HtmlType valueOfType(String type) {
            for (HtmlInputSimpleCreater_HtmlType e : tagLst) {
                if (StringUtils.equalsIgnoreCase(e.tagId, type)) {
                    return e;
                }
            }
            throw new RuntimeException("無法判定Html type : " + type);
        }

        public String getTD() {
            td1 = StringUtils.trimToEmpty(td1);
            td2 = StringUtils.trimToEmpty(td2);
            if (StringUtils.isNotBlank(td1)) {
                td1 = " " + td1;
            }
            if (StringUtils.isNotBlank(td2)) {
                td2 = " " + td2;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("<td" + td1 + ">%1$s</td>                                    \n");
            sb.append("<td" + td2 + ">                                          \n");
            sb.append("%2$s");
            sb.append("</td>                                                                       \n");
            return sb.toString();
        }

        public String getTd1() {
            return td1;
        }

        public void setTd1(String td1) {
            this.td1 = td1;
        }

        public String getTd2() {
            return td2;
        }

        public void setTd2(String td2) {
            this.td2 = td2;
        }

        public List<HtmlInputSimpleCreater_HtmlType> getTagLst() {
            return tagLst;
        }

        public void setTagLst(List<HtmlInputSimpleCreater_HtmlType> tagLst) {
            this.tagLst = tagLst;
        }
    }

    public static class HtmlInputSimpleCreater_HtmlType {
        String tagId;
        String template;

        public String toHtml(String tagName, String title) {
            try {
                return String.format(template, tagName, title);
            } catch (Exception ex) {
                throw new RuntimeException("錯誤 : " + tagId + "\n" + template + "\nERR : " + ex.getMessage(), ex);
            }
        }

        public String getTagId() {
            return tagId;
        }

        public void setTagId(String tagId) {
            this.tagId = tagId;
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }
    }
}