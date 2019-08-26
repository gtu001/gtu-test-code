package gtu.html.simple;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import gtu.file.FileUtil;

public class HtmlSimpleTableCreater {

    private static final String HTML;
    static {
        StringBuilder sb = new StringBuilder();
        sb.append(" <html>  \n");
        sb.append(" <head>  \n");
        sb.append("     <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />  \n");
        sb.append("     <style> \n");
        sb.append("     table '{'   \n");
        sb.append("         border:1px solid #000;   \n");
        sb.append("         font-family: 微軟正黑體;   \n");
        sb.append("         font-size:16px;   \n");
        sb.append("         width:80%;  \n");
        sb.append("         border:1px solid #000;  \n");
        sb.append("         text-align:center;  \n");
        sb.append("         border-collapse:collapse;  \n");
        sb.append("     }   \n");
        sb.append("     th '{'   \n");
        sb.append("         background-color: #009FCC;  \n");
        sb.append("         padding:10px;  \n");
        sb.append("         border:1px solid #000;  \n");
        sb.append("         color:#fff;  \n");
        sb.append("         font-size:10px;  \n");
        sb.append("     }   \n");
        sb.append("     td '{'   \n");
        sb.append("         border:1px solid #000;  \n");
        sb.append("         padding:5px;  \n");
        sb.append("         font-size:14px;  \n");
        sb.append("     }  \n");
        sb.append(" </style>  \n");
        sb.append("   \n");
        sb.append(" <script type=\"text/javascript\">  \n");
        sb.append(" </script>  \n");
        sb.append("   ");
        sb.append(" </head>  \n");
        sb.append(" <body>  \n");
        sb.append("   \n");
        sb.append("  {0} ");
        sb.append("   ");
        sb.append(" </body>  \n");
        sb.append(" </html>  \n");
        HTML = sb.toString();
    }

    private List<String> ths = new ArrayList<String>();

    private List<String> tds = new ArrayList<String>();
    private List<Integer> tdCols = new ArrayList<Integer>();
    private List<Object[]> tdsLst = new ArrayList<Object[]>();

    public HtmlSimpleTableCreater addTh(String... ths) {
        for (String str : ths) {
            this.ths.add(str);
        }
        return this;
    }

    public HtmlSimpleTableCreater addTd(String... tds) {
        for (String str : tds) {
            this.tds.add(str);
            this.tdCols.add(1);
        }
        return this;
    }

    public HtmlSimpleTableCreater addTd(String td) {
        return addTd(td, 1);
    }

    public HtmlSimpleTableCreater addTd(String td, int colspan) {
        if (colspan < 1) {
            colspan = 1;
        }
        this.tds.add(td);
        this.tdCols.add(colspan);
        return this;
    }

    public HtmlSimpleTableCreater newTr() {
        tdsLst.add(new Object[] { tds, tdCols });
        tds = new ArrayList<String>();
        tdCols = new ArrayList<Integer>();
        return this;
    }

    public String createHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table>\n");
        sb.append("<tr>");
        for (String str : ths) {
            sb.append("<th>");
            sb.append(StringUtils.trimToEmpty(str));
            sb.append("</th>");
        }
        sb.append("</tr>");
        sb.append("\n");
        for (Object[] tdConf : tdsLst) {
            List<String> tds = (List<String>) tdConf[0];
            List<Integer> tdCols = (List<Integer>) tdConf[1];
            sb.append("<tr>");
            for (int ii = 0; ii < tds.size(); ii++) {
                String str = tds.get(ii);
                sb.append("<td colspan='" + tdCols.get(ii) + "'>");
                sb.append(StringUtils.trimToEmpty(str));
                sb.append("</td>");
            }
            sb.append("</tr>");
            sb.append("\n");
        }
        sb.append("</table>\n");
        return MessageFormat.format(HTML, new Object[] { sb });
    }

    public File createFile(String name) {
        String html = createHtml();
        File file = getFile(name);
        saveToFile(file, html, "UTF8");
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File getFile(String name) {
        File file = new File(FileUtil.DESKTOP_DIR, name);
        System.out.println(">>>>>> " + file);
        return file;
    }

    public static void saveToFile(File file, String content, String encode) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encode));
            writer.write(content);
            writer.close();
        } catch (Exception ex) {
            throw new RuntimeException("saveToFile ERR : " + ex.getMessage(), ex);
        } finally {
            try {
                writer.close();
            } catch (Exception ex1) {
            }
        }
    }
}
