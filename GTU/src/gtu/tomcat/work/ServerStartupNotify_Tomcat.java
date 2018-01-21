package gtu.tomcat.work;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.Properties;

import javax.swing.JOptionPane;

public class ServerStartupNotify_Tomcat {

    public static void main(String[] args) {
        String message = "";
//        String message = "Server startup in " + ((t2 - t1) / 1000000L) + " ms";
//        log.info(message);
        try {
            File file = new File(File.listRoots()[0], "tomcat_start_config.properties");
            if (!file.exists()) {
                file.createNewFile();
            }
            Properties prop = new Properties();
            prop.load(new FileInputStream(file));
            String today = String.format("%tY%<tm%<td", System.currentTimeMillis());
            int time = 0;
            if(prop.containsKey(today)){
                time = Integer.parseInt(prop.getProperty(today));
            }
            time ++;
            prop.setProperty(today, String.valueOf(time));
            String strMessage = "老爺\nServer起好了!!\n本日共起" + time + "次!";
            prop.store(new FileOutputStream(file), "");
            
            final String OPEN_KEY = "open";
            final String URL_KEY = "url";
            
            String openMode = "";
            if(prop.containsKey(OPEN_KEY)){
                openMode = prop.getProperty(OPEN_KEY);
            }
            if(openMode == null || openMode.length() == 0){
                openMode = "cmd";
            }
            
            if("cmd".equalsIgnoreCase(openMode)){
                Runtime.getRuntime().exec("cmd /c start echo " + strMessage);
                System.out.println("## 開啟cmd");
            }else if("dialog".equalsIgnoreCase(openMode)){
                JOptionPane.showMessageDialog(null, strMessage, "恭賀老爺賀喜夫人", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("## 開啟dialog");
            }
            
            if(prop.containsKey(URL_KEY)){
                String url = prop.getProperty(URL_KEY);
                if(url == null || url.length() == 0){
                    url = "http://localhost:8080/fet_estore_search_engie_revamp/admin/login.do";
                }
                Desktop.getDesktop().browse(new URI(url));
                System.out.println("## 開啟URL : " + url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
