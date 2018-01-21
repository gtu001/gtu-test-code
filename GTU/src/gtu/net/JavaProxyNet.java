package gtu.net;

/**
 * 保存代理服务器地址到 c:\proxy.htm 文件
 * @web http://blog.csdn.net/cqq
 * @author 慈勤强
 * @version 1.00 05/02/01
 */
import gtu.file.FileUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Date;
import java.util.regex.Pattern;

public class JavaProxyNet {

    public static void main(String[] args) throws Exception {

        System.out.println("正在生成代理列表...\r\n");
        JavaProxyNet ou = new JavaProxyNet();
        File f = new File(FileUtil.DESKTOP_PATH + "proxy.htm");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));

        String str = ou.getProxy1();
        Pattern p = Pattern.compile("<li>");

        String[] ss = p.split(str);
        String strTmp = "";
        String str1 = "";

        if (ss.length > 1) {
            bw.write("<table width=\"90%\" border=\"0\" align=\"center\" bgcolor=\"#F9F9F9\"><tr><td>");
            bw.write("<b>Last Modified:" + new Date().toLocaleString() + "</b><br>&nbsp;<br>");
            for (int i = 1; i < ss.length; i++) {
                strTmp = ss[i].substring(10, 12); // Country
                if (strTmp.equals("CN"))
                    strTmp = "<font color=red><b>" + strTmp + "</b></font>";
                str1 = removeAllTag(ss[i]);
                bw.write("" + i + " " + strTmp + " " + str1 + "<br>");

            }
            bw.write("</td></tr></table>");
        }
        bw.close();
        System.out.println("完成");
        System.exit(0);

    }

    private String getProxy1() {
        int i = 0;
        try {
            URL url = new URL("http://www.cybersyndrome.net/plr5.html");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String s = "";
            StringBuffer sb = new StringBuffer("");
            while ((s = br.readLine()) != null) {
                i++;
                if (i > 80 && i < 89) {
                    sb.append(s + "\r\n");
                }
            }
            br.close();
            return sb.toString();
        } catch (Exception e) {
            return "error open url" + e.toString();
        }
    }

    public static String removeAllTag(String src) {
        return src.replaceAll("<[^>]*>", "");
    }
}