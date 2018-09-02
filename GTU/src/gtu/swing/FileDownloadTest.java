package gtu.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

/**
 * @author 2012/1/8 简单的文件下载器
 */
public class FileDownloadTest {
    public static void main(String[] args) {
        // 創建表單類變數
        DemoWindow dw = new DemoWindow("網路檔下載");

        // 將表單的寬度和高度分別設置為螢幕寬度和螢幕高度的1/3，左上角位置也設置為螢幕寬度和螢幕高度的1/3處
        Toolkit theKit = dw.getToolkit();
        Dimension wndSize = theKit.getScreenSize();
        dw.setBounds(wndSize.width / 3, wndSize.height / 3, wndSize.width / 3, wndSize.height / 3);

        // 點擊關閉按鈕可以退出程式
        dw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 設置表單為可見
        dw.setVisible(true);
    }
}

// 介面表單
class DemoWindow extends JFrame implements ActionListener {
    // 輸入網路檔URL的文字方塊
    JTextField jtf = new JTextField(25);

    // 操作按鈕
    JButton jb = new JButton("下載");

    // 顯示網路檔資訊的文本區
    JTextArea jta = new JTextArea();

    // 設置文本區的捲軸
    int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
    int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
    JScrollPane jsp = new JScrollPane(jta, v, h);

    // 佈局面板
    JPanel jp = new JPanel();

    // 網路檔下載
    Downloader downloader;

    // 構造函數
    public DemoWindow(String title) {
        super(title);

        // 表單佈局
        jp.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp.add(jtf);
        jp.add(jb);
        add(jp, BorderLayout.NORTH);
        add(jsp, BorderLayout.CENTER);

        // 添加事件監聽器
        jtf.addActionListener(this);
        jb.addActionListener(this);
    }

    // 回應按一下按鈕
    public void actionPerformed(ActionEvent e) {
        // 創建網路檔下載類變數
        downloader = new Downloader(jtf.getText(), jta);

        // 啟動下載執行緒
        Thread thread = new Thread(downloader);
        thread.start();
    }
}

// 網路檔下載類
class Downloader implements Runnable {
    // 網路檔的URL
    String urlString;

    // 顯示網路檔資訊的文本區
    JTextArea jta;

    // 構造函數
    public Downloader(String urlString, JTextArea jta) {
        // 設置屬性
        this.urlString = urlString;
        this.jta = jta;
    }

    // 下載網路檔的執行緒方法
    public void run() {
        // 網路檔的相關資訊
        StringBuffer info = new StringBuffer();
        try {
            // 網路檔的URL
            URL url = new URL(urlString);

            // 打開該網路檔的URL連接
            URLConnection urlConn = url.openConnection();

            // 添加網路檔的相關資訊
            info.append("主機: " + url.getHost() + "\n");
            info.append("埠: " + url.getDefaultPort() + "\n");
            info.append("網路檔的類型: " + urlConn.getContentType() + "\n");
            info.append("長度: " + urlConn.getContentLength() + "\n");
            info.append("正在下載...");

            // 顯示網路檔的相關資訊
            jta.setText(info.toString());

            // 創建網路檔的輸入流
            InputStream is = urlConn.getInputStream();

            // 獲取網路檔的檔案名稱
            String localFileName = url.getFile().substring(url.getFile().lastIndexOf("/") + 1);

            // 創建本地檔輸出流
            FileOutputStream fos = new FileOutputStream(localFileName);

            // 讀取網路檔到本地檔
            int data;
            while ((data = is.read()) != -1) {
                fos.write(data);
            }

            // 關閉流
            is.close();
            fos.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        jta.append("下載完畢！");
    }
}
