package gtu.velocity.iisigroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class XhtmlMaker {

    public static void main(String[] args) {
        XhtmlMaker ss = new XhtmlMaker();
        System.out.println(ss.getXhtmls());
    }

    List<XhtmlInfo> getXhtmls() {
        List<XhtmlInfo> list = new ArrayList<XhtmlInfo>();
        for (Field f : XhtmlMaker.Xhtml.class.getDeclaredFields()) {
            if (f.getType() == Xhtml.class) {
                try {
                    list.add(new XhtmlInfo(f.getName(), (Xhtml) f.get(XhtmlMaker.class)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    static class XhtmlInfo {
        String name;
        Xhtml xhtml;

        public XhtmlInfo(String name, Xhtml xhtml) {
            super();
            this.name = name;
            this.xhtml = xhtml;
        }

        public String toString() {
            return name;
        }
    }

    static class Xhtml {
        static final Xhtml TWO_CALENDAR;
        static final Xhtml SINGLE_CALENDAR;
        static final Xhtml INPUTTEXT_DEFAULT;
        static final Xhtml INPUT_YEAR_MONTH;
        static final Xhtml SELECT_ONE_MENU;
        static final Xhtml AUTO_COMPLETE_VILLAGE;
        static final Xhtml SELECT_ONE_RADIO;
        static final Xhtml REPORT_REPORT_TYPE;

        final String tag;

        Xhtml(String tag) {
            this.tag = tag;
        }

        static StringBuilder sb;
        static {
            sb = new StringBuilder();
            sb.append("                                 <tr>                                                                                                                      \r\n");
            sb.append("                                             <th width=\"25%\"><ris:requiredSymbol /><h:outputText value=\"${COLUMN.get(#INDEX#).get(\"TITLE1\")}：\" /></th>                        \r\n");
            sb.append("                                             <td width=\"75%\">                                                                                                \r\n");
            sb.append("                                                     <ris:calendar showCalendar=\"true\" calDateValue=\"#{${LOWER_ID}Controller.dto.${COLUMN.get(#INDEX#).get(\"FIELD1\")}}\" />～             \r\n ");
            sb.append("                                                     <ris:calendar showCalendar=\"true\" calDateValue=\"#{${LOWER_ID}Controller.dto.${COLUMN.get(#INDEX#).get(\"FIELD2\")}}\" /></td>           \r\n");
            sb.append("                                     </tr>                                                                                                                     \r\n");
            TWO_CALENDAR = new Xhtml(sb.toString());

            sb = new StringBuilder();
            sb.append("                                 <tr>                                                                                                                      \r\n");
            sb.append("                                             <th width=\"25%\"><ris:requiredSymbol /><h:outputText value=\"${COLUMN.get(#INDEX#).get(\"TITLE1\")}：\" /></th>                        \r\n");
            sb.append("                                             <td width=\"75%\">                                                                                                \r\n");
            sb.append("                                                     <ris:calendar showCalendar=\"true\" calDateValue=\"#{${LOWER_ID}Controller.dto.${COLUMN.get(#INDEX#).get(\"FIELD1\")}}\" /></td>           \r\n");
            sb.append("                                     </tr>                                                                                                                     \r\n");
            SINGLE_CALENDAR = new Xhtml(sb.toString());

            sb = new StringBuilder();
            sb.append("                                     <tr>                                                                                                                      \r\n");
            sb.append("                                             <th width=\"25%\"><ris:requiredSymbol /><h:outputText value=\"${COLUMN.get(#INDEX#).get(\"TITLE1\")}：\" /></th>                                \r\n");
            sb.append("                                             <td width=\"75%\">                                                                                                \r\n");
            sb.append("                                                     <p:inputText value=\"#{${LOWER_ID}Controller.dto.${COLUMN.get(#INDEX#).get(\"FIELD1\")}}\" maxlength=\"10\" style=\"width:150px;\" /></td> \r\n");
            sb.append("                                     </tr>                                                                                                                     \r\n");
            INPUTTEXT_DEFAULT = new Xhtml(sb.toString());

            sb = new StringBuilder();
            sb.append("                                     <tr>                                                                                                                      \r\n");
            sb.append("                                             <th width=\"25%\"><ris:requiredSymbol /><h:outputText value=\"${COLUMN.get(#INDEX#).get(\"TITLE1\")}：\" /></th>                                \r\n");
            sb.append("                                             <td width=\"75%\">                                                                                                \r\n");
            sb.append("                                                     <p:inputText value=\"#{${LOWER_ID}Controller.dto.${COLUMN.get(#INDEX#).get(\"FIELD1\")}}\" maxlength=\"3\" style=\"width:80px;\" />年 \r\n");
            sb.append("                                                     <p:inputText value=\"#{${LOWER_ID}Controller.dto.${COLUMN.get(#INDEX#).get(\"FIELD2\")}}\" maxlength=\"2\" style=\"width:80px;\" />月</td> \r\n");
            sb.append("                                     </tr>                                                                                                                     \r\n");
            INPUT_YEAR_MONTH = new Xhtml(sb.toString());

            sb = new StringBuilder();
            sb.append("                                     <tr>                                                                                                                      \r\n");
            sb.append("                                             <th width=\"25%\"><h:outputText value=\"${COLUMN.get(#INDEX#).get(\"TITLE1\")}：\" /></th>                                                  \r\n");
            sb.append("                                             <td width=\"75%\"><p:autoComplete maxlength=\"8\"                                                                 \r\n");
            sb.append("                                                             style=\"width:120px;\" dropdown=\"true\"                                                          \r\n");
            sb.append("                                                             value=\"#{${LOWER_ID}Controller.dto.${COLUMN.get(#INDEX#).get(\"FIELD1\")}}\"                                                      \r\n");
            sb.append("                                                             completeMethod=\"#{rl04110Controller.villageComplete}\" />村里<p:inputText                       \r\n");
            sb.append("                                                             value=\"#{${LOWER_ID}Controller.dto.${COLUMN.get(#INDEX#).get(\"FIELD2\")}}\" maxlength=\"3\"                                      \r\n");
            sb.append("                                                             style=\"width:100px;\" />鄰</td>                                                                  \r\n");
            sb.append("                                     </tr>                                                                                                                     \r\n");
            AUTO_COMPLETE_VILLAGE = new Xhtml(sb.toString());

            sb = new StringBuilder();
            sb.append("                                     <tr>                                                                                                                      \r\n");
            sb.append("                                             <th width=\"25%\"><h:outputText value=\"${COLUMN.get(#INDEX#).get(\"TITLE1\")}：\" /></th>                                                  \r\n");
            sb.append("                                             <td width=\"75%\">                                                                                                \r\n");
            sb.append("                                                     <h:selectOneRadio value=\"#{${LOWER_ID}Controller.dto.${COLUMN.get(#INDEX#).get(\"FIELD1\")}}\">                                           \r\n");
            sb.append("                                                             <f:selectItem itemValue=\"Y\" itemLabel=\"Y\" />                                                  \r\n");
            sb.append("                                                             <f:selectItem itemValue=\"N\" itemLabel=\"N\" />                                                  \r\n");
            sb.append("                                                     </h:selectOneRadio></td>                                                                                  \r\n");
            sb.append("                                     </tr>                                                                                                                     \r\n");
            SELECT_ONE_RADIO = new Xhtml(sb.toString());

            sb = new StringBuilder();
            sb.append("                                     <tr>                                                                                                                      \r\n");
            sb.append("                                             <th width=\"25%\"><ris:requiredSymbol /><h:outputText value=\"${COLUMN.get(#INDEX#).get(\"TITLE1\")}：\" /></th>                                \r\n");
            sb.append("                                             <td width=\"75%\">                                                                                                \r\n");
            sb.append("                                                     <h:selectOneMenu value=\"#{${LOWER_ID}Controller.dto.${COLUMN.get(#INDEX#).get(\"FIELD1\")}}\">                                            \r\n");
            sb.append("                                                             <f:selectItem itemValue=\"A\" itemLabel=\"全部\" />                                               \r\n");
            sb.append("                                                             <f:selectItem itemValue=\"B\" itemLabel=\"Label1\" />                                               \r\n");
            sb.append("                                                             <f:selectItem itemValue=\"C\" itemLabel=\"Label2\" />                                               \r\n");
            sb.append("                                                     </h:selectOneMenu></td>                                                                                   \r\n");
            sb.append("                                     </tr>                                                                                                                     \r\n");
            SELECT_ONE_MENU = new Xhtml(sb.toString());

            //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX for report
            sb = new StringBuilder();
            sb.append("                                     <tr>                                                                                                                      \r\n");
            sb.append("                                             <th width=\"25%\"><h:outputText value=\"另存檔案格式：\" /></th>                                                  \r\n");
            sb.append("                                             <td width=\"75%\">                                                                                                \r\n");
            sb.append("                                                     <h:selectOneRadio value=\"#{${LOWER_ID}Controller.dto.${COLUMN.get(#INDEX#).get(\"FIELD1\")}}\">                                           \r\n");
            sb.append("                                                             <f:selectItem itemLabel=\"CSV\" itemValue=\"CSV\" />                                                  \r\n");
            sb.append("                                                             <f:selectItem itemLabel=\"XLS\" itemValue=\"XLS\" />                                                  \r\n");
            sb.append("                                                             <f:selectItem itemLabel=\"PDF\" itemValue=\"PDF\" />                                                  \r\n");
            sb.append("                                                             <f:selectItem itemLabel=\"DOC\" itemValue=\"DOC\" />                                                  \r\n");
            sb.append("                                                             <f:selectItem itemLabel=\"ODF\" itemValue=\"ODF\" />                                                  \r\n");
            sb.append("                                                     </h:selectOneRadio></td>                                                                                  \r\n");
            sb.append("                                     </tr>                                                                                                                     \r\n");
            REPORT_REPORT_TYPE = new Xhtml(sb.toString());
        }
    }
}
