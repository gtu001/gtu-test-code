package gtu.html.simple;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import gtu.console.SystemInUtil;
import gtu.string.StringUtil_;
import gtu.yaml.util.YamlUtil;

public class HtmlInputSimpleCreater {

    private static final String TD;
    static {
        StringBuilder sb = new StringBuilder();
        sb.append("<td class=\"tbYellow\" width=\"10%%\">%1$s</td>                                    \n");
        sb.append("<td class=\"tbYellow2\" width=\"40%%\">                                          \n");
        sb.append("%2$s");
        sb.append("</td>                                                                       \n");
        TD = sb.toString();
    }

    public static void main(String[] args) {
        HtmlInputSimpleCreater t = new HtmlInputSimpleCreater();
        String fromData = SystemInUtil.readContent();
        InputStream is = HtmlInputSimpleCreater.class.getResourceAsStream("HtmlInputSimpleCreater_Template.yaml");
        String result = t.execute(is, fromData);
        System.out.println(result);
        System.out.println("done...");
    }

    public String execute(InputStream template, String fromData) {
        HtmlTypeHandler htmlTypeHandler = new HtmlTypeHandler(template);
        List<String> lst = StringUtil_.readContentToList(fromData, true, false, false);
        StringBuffer sb = new StringBuffer();
        sb.append("<tr>");
        for (int ii = 0; ii < lst.size(); ii++) {
            String line = lst.get(ii);
            if (StringUtils.isNotBlank(line)) {
                String chineseLabel = getChineseLabel(line);
                Map<String, HtmlType> tagMap = getInputGroup(line, htmlTypeHandler);
                String html = getTagGroupHtml(tagMap, chineseLabel);
                sb.append(String.format(TD, chineseLabel, html));
            } else {
                sb.append("</tr>\n<tr>");
            }
        }
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

    private Map<String, HtmlType> getInputGroup(String line, HtmlTypeHandler htmlTypeHandler) {
        Pattern ptn = Pattern.compile("(\\w+?)\\[(\\w+)\\]");
        Map<String, HtmlType> map = new LinkedHashMap<String, HtmlType>();
        Matcher mth = ptn.matcher(line);
        while (mth.find()) {
            String tagName = mth.group(1);
            HtmlType tagType = htmlTypeHandler.valueOfType(mth.group(2));
            map.put(tagName, tagType);
        }
        return map;
    }

    private String getTagGroupHtml(Map<String, HtmlType> map, String title) {
        StringBuilder sb = new StringBuilder();
        for (String tagName : map.keySet()) {
            HtmlType tag = map.get(tagName);
            String html = tag.toHtml(tagName, title);
            sb.append(html);
        }
        return sb.toString();
    }

    private class HtmlTypeHandler {
        List<HtmlType> tagLst = new ArrayList<HtmlType>();

        private HtmlTypeHandler(InputStream is) {
            List<Map<String, String>> lst = (List<Map<String, String>>) YamlUtil.loadInputStream(is);
            for (Map<String, String> e : lst) {
                HtmlType vo = new HtmlType();
                vo.tagId = e.get("tagId");
                vo.template = e.get("template");
                tagLst.add(vo);
            }
        }

        private HtmlType valueOfType(String type) {
            for (HtmlType e : tagLst) {
                if (StringUtils.equalsIgnoreCase(e.tagId, type)) {
                    return e;
                }
            }
            throw new RuntimeException("無法判定Html type : " + type);
        }
    }

    private class HtmlType {
        String tagId;
        String template;

        public String toHtml(String tagName, String title) {
            try {
                return String.format(template, tagName, title);
            } catch (Exception ex) {
                throw new RuntimeException("錯誤 : " + tagId + "\n" + template + "\nERR : " + ex.getMessage(), ex);
            }
        }
    }

    // TODO 將這個放到 controller
    // -------------------------------------------------------------------------------------
    public static Map<String, String> getHtmlMapLst() {
        Map<String, String> m1 = new LinkedHashMap<String, String>();
        m1.put("m1", "map_val_1");
        m1.put("m2", "map_val_2");
        m1.put("m3", "map_val_3");
        return m1;
    }

    public List<HtmlBean> getHtmlBeanLst() {
        List<HtmlBean> lst = new ArrayList<HtmlBean>();
        HtmlBean b1 = new HtmlBean();
        b1.setXXXXXX_value("1");
        b1.setXXXXXX_label("lbl1");
        HtmlBean b2 = new HtmlBean();
        b2.setXXXXXX_value("2");
        b2.setXXXXXX_label("lbl2");
        HtmlBean b3 = new HtmlBean();
        b3.setXXXXXX_value("3");
        b3.setXXXXXX_label("lbl3");
        lst.add(b1);
        lst.add(b2);
        lst.add(b3);
        return lst;
    }

    public static class HtmlBean {
        String XXXXXX_value;

        String XXXXXX_label;

        public String getXXXXXX_value() {
            return XXXXXX_value;
        }

        public void setXXXXXX_value(String xxxxxx_value) {
            XXXXXX_value = xxxxxx_value;
        }

        public String getXXXXXX_label() {
            return XXXXXX_label;
        }

        public void setXXXXXX_label(String xxxxxx_label) {
            XXXXXX_label = xxxxxx_label;
        }
    }
}