package gtu.html.simple;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import gtu.console.SystemInUtil;

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
        List<String> lst = SystemInUtil.readContentToList(true, false, false);
        StringBuffer sb = new StringBuffer();
        sb.append("<tr>");
        for (int ii = 0; ii < lst.size(); ii++) {
            String line = lst.get(ii);
            if (StringUtils.isNotBlank(line)) {
                String chineseLabel = t.getChineseLabel(line);
                Map<String, HtmlType> tagMap = t.getInputGroup(line);
                String html = t.getTagGroupHtml(tagMap, chineseLabel);
                sb.append(String.format(TD, chineseLabel, html));
            } else {
                sb.append("</tr>\n<tr>");
            }
        }
        System.out.println(sb.toString());
        System.out.println("done...");
    }

    private String getChineseLabel(String line) {
        Pattern ptn = Pattern.compile("(.*?)\\s");
        Matcher mth = ptn.matcher(line);
        if (mth.find()) {
            return mth.group(1);
        }
        return "";
    }

    private Map<String, HtmlType> getInputGroup(String line) {
        Pattern ptn = Pattern.compile("(\\w+?)\\[(\\w+)\\]");
        Map<String, HtmlType> map = new LinkedHashMap<String, HtmlType>();
        Matcher mth = ptn.matcher(line);
        while (mth.find()) {
            String tagName = mth.group(1);
            HtmlType tagType = HtmlType.valueOfType(mth.group(2));
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

    private enum HtmlType {
        Text("t") {
            @Override
            String toHtml(String tagName, String title) {
                return String.format(
                        "<input id=\"%1$s\" name=\"%1$s\" title=\"%2$s\" type=\"text\" class=\"textBox2\" value=\"${rtnMap.%1$s}\" />",
                        tagName, title);
            }
        }, //
        Hidden("h") {
            @Override
            String toHtml(String tagName, String title) {
                return String.format(
                        "<input id=\"%1$s\" name=\"%1$s\" title=\"%2$s\" type=\"hidden\" class=\"textBox2\" value=\"${rtnMap.%1$s}\" />",
                        tagName, title);
            }
        }, //
        Label("L") {
            @Override
            String toHtml(String tagName, String title) {
                return String.format("<span id=\"%1$s_span\" name=\"%1$s_span\">${rtnMap.%1$s}</span>", tagName, title);
            }
        }, //
        Radio("R") {
            @Override
            String toHtml(String tagName, String title) {
                StringBuilder sb = new StringBuilder();
                sb.append(
                        "<c:forEach var=\"data\" items=\"${XXXXXXXX_List}\" varStatus=\"status\">                                 \n");
                sb.append(
                        "        <input type=\"radio\" id=\"%1$s_${status.index}\" name=\"%1$s\" value=\"${data.XXXXXX_value}\"   \n");
                sb.append(
                        "                <c:if test=\"${data.XXXXXX_value eq rtnMap.%1$s}\">checked</c:if> />                    \n");
                sb.append(
                        "        <label for=\"%1$s_${status.index}\">${data.XXXXXX_label}</label>                                \n");
                sb.append(
                        "</c:forEach>                                                                                             \n");
                return String.format(sb.toString(), tagName, title);
            }
        }, //
        Radio_Simple("Rs") {
            @Override
            String toHtml(String tagName, String title) {
                StringBuilder sb = new StringBuilder();
                sb.append("<input type=\"radio\" id=\"%1$s_0\" name=\"%1$s\" value=\"1\" checked />\n");
                sb.append("<label for=\"%1$s_0\">Radio1</label>\n");
                sb.append("<input type=\"radio\" id=\"%1$s_1\" name=\"%1$s\" value=\"2\" checked />\n");
                sb.append("<label for=\"%1$s_1\">Radio2</label>\n");
                return String.format(sb.toString(), tagName, title);
            }
        }, //
        Radio_MAP("Rm") {
            @Override
            String toHtml(String tagName, String title) {
                StringBuilder sb = new StringBuilder();
                sb.append(
                        "<c:forEach var=\"map\" items=\"${XXXXXXXX_MapList}\" varStatus=\"status\">                                 \n");
                sb.append(
                        "        <input type=\"radio\" id=\"%1$s_${status.index}\" name=\"%1$s\" value=\"${map.key}\"   \n");
                sb.append(
                        "                <c:if test=\"${map.key eq rtnMap.%1$s}\">checked</c:if> />                    \n");
                sb.append(
                        "        <label for=\"%1$s_${status.index}\">${map.value}</label>                                \n");
                sb.append(
                        "</c:forEach>                                                                                             \n");
                return String.format(sb.toString(), tagName, title);
            }
        }, //
        Select("s") {
            @Override
            String toHtml(String tagName, String title) {
                StringBuilder sb = new StringBuilder();
                sb.append(
                        "<select id=\"%1$s\"                                                                       \n");
                sb.append(" name=\"%1$s\" title=\"%2$s\" value=\"${rtnMap.%1$s}\">                                                          \n");
                sb.append(" <option value=''>請選擇</option>                                                   \n");
                sb.append(
                        "   <c:forEach var=\"data\" items=\"${XXXXXXXX_List}\" varStatus=\"status\">                    \n");
                sb.append("     <option value=\"<c:out value='${data.XXXXXX_value}' />\"                     \n");
                sb.append("         <c:if test=\"${data.XXXXXX_value eq rtnMap.%1$s}\">selected</c:if>>  \n");
                sb.append("     <c:out value='${data.XXXXXX_label}' /></option>                            \n");
                sb.append(" </c:forEach>                                                                       \n");
                sb.append("</select>                                                                               \n");
                return String.format(sb.toString(), tagName, title);
            }
        }, //
        Select_Simple("ss") {
            @Override
            String toHtml(String tagName, String title) {
                StringBuilder sb = new StringBuilder();
                sb.append(
                        "<select id=\"%1$s\"                                                                       \n");
                sb.append(" name=\"%1$s\" title=\"%2$s\" value=\"${rtnMap.%1$s}\">                                                          \n");
                sb.append(" <option value=''>請選擇</option>                                                   \n");
                sb.append(" <option value='1'>1</option>                                                   \n");
                sb.append(" <option value='2'>2</option>                                                   \n");
                sb.append(" <option value='3'>3</option>                                                   \n");
                sb.append("</select>                                                                               \n");
                return String.format(sb.toString(), tagName, title);
            }
        }, //
        Select_MAP("sm") {
            @Override
            String toHtml(String tagName, String title) {
                StringBuilder sb = new StringBuilder();
                sb.append(
                        "<select id=\"%1$s\"                                                                       \n");
                sb.append(" name=\"%1$s\" title=\"%2$s\" value=\"${rtnMap.%1$s}\">                                                          \n");
                sb.append(" <option value=''>請選擇</option>                                                   \n");
                sb.append(
                        "   <c:forEach var=\"map\" items=\"${XXXXXXXX_MapList}\" varStatus=\"status\">                    \n");
                sb.append("     <option value=\"<c:out value='${map.key}' />\"                     \n");
                sb.append("         <c:if test=\"${map.key eq rtnMap.%1$s}\">selected</c:if>>  \n");
                sb.append("     <c:out value='${map.value}' /></option>                            \n");
                sb.append(" </c:forEach>                                                                       \n");
                sb.append("</select>                                                                               \n");
                return String.format(sb.toString(), tagName, title);
            }
        }, //
        Checkbox("c") {
            @Override
            String toHtml(String tagName, String title) {
                StringBuilder sb = new StringBuilder();
                sb.append("<c:forEach var=\"data\" items=\"${XXXXXXXX_List}\" varStatus=\"status\">        \n");
                sb.append(" <input type=\"checkbox\"                                                 \n");
                sb.append("         name=\"%1$s\"                                                    \n");
                sb.append("         id=\"%1$s_${status.index}\"                   \n");
                sb.append("     value=\"<c:out value='${data.XXXXXX_value}'/>\" title=\"%2$s\" />  \n");
                sb.append(" <c:out value='${data.XXXXXX_label}' />                                   \n");
                sb.append("</c:forEach>                                                                  \n");
                return String.format(sb.toString(), tagName, title);
            }
        }, //
        Checkbox_Simple("cs") {
            @Override
            String toHtml(String tagName, String title) {
                StringBuilder sb = new StringBuilder();
                sb.append("<input type=\"checkbox\" name=\"%1$s\" id=\"%1$s_0\" value=\"1'/>\" title=\"%2$s\" />chk1\n");
                sb.append("<input type=\"checkbox\" name=\"%1$s\" id=\"%1$s_1\" value=\"2'/>\" title=\"%2$s\" />chk2\n");
                sb.append("<input type=\"checkbox\" name=\"%1$s\" id=\"%1$s_2\" value=\"3'/>\" title=\"%2$s\" />chk3\n");
                return String.format(sb.toString(), tagName, title);
            }
        }, //
        Checkbox_MAP("cm") {
            @Override
            String toHtml(String tagName, String title) {
                StringBuilder sb = new StringBuilder();
                sb.append("<c:forEach var=\"map\" items=\"${XXXXXXXX_MapList}\" varStatus=\"status\">        \n");
                sb.append(" <input type=\"checkbox\"                                                 \n");
                sb.append("         name=\"%1$s\"                                                    \n");
                sb.append("         id=\"%1$s_${status.index}\"                   \n");
                sb.append("     value=\"<c:out value='${map.key}'/>\" title=\"%2$s\" />  \n");
                sb.append(" <c:out value='${map.value}' />                                   \n");
                sb.append("</c:forEach>                                                                  \n");
                return String.format(sb.toString(), tagName, title);
            }
        }, //
        Textarea("area") {
            @Override
            String toHtml(String tagName, String title) {
                return String.format(
                        "<textarea id=\"%1$s\" name=\"%1$s\" cols=\"90\" rows=\"5\" title=\"%2$s\">${rtnMap.%1$s}</textarea>",
                        tagName, title);
            }
        },//
        ;

        final String type;

        HtmlType(String type) {
            this.type = type;
        }

        abstract String toHtml(String tagName, String title);

        private static HtmlType valueOfType(String type) {
            for (HtmlType e : HtmlType.values()) {
                if (StringUtils.equalsIgnoreCase(e.type, type)) {
                    return e;
                }
            }
            throw new RuntimeException("無法判定Html type : " + type);
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
