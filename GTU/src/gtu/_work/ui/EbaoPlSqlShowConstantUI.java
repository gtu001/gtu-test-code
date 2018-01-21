package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.StringUtils;

import gtu.swing.util.JCommonUtil;

public class EbaoPlSqlShowConstantUI extends JFrame {

    private JPanel contentPane;
    private JTextArea sqlArea;
    private JTextArea resultArea;
    private JTextArea parseArea;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    EbaoPlSqlShowConstantUI frame = new EbaoPlSqlShowConstantUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public EbaoPlSqlShowConstantUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 584, 437);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);
        
        JPanel panel = new JPanel();
        tabbedPane.addTab("原始sql", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));
        
        JPanel panel_1 = new JPanel();
        panel.add(panel_1, BorderLayout.NORTH);
        
        JButton btnConvert = new JButton("convert");
        btnConvert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                try{
                    String text = StringUtils.defaultString(sqlArea.getText());
                    Pattern ptn = Pattern.compile("PKG\\w+\\.\\w+", Pattern.MULTILINE | Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
                    Matcher mth = ptn.matcher(text);
                    StringBuilder sb = new StringBuilder();
                    sb.append("declare\n");
                    sb.append("begin\n");
                    sb.append("pkg_pub_app_context.P_SET_APP_USER_ID(401);\n");
                    while(mth.find()){
                        String var = mth.group();
                        sb.append(String.format("dbms_output.put_line('%1$s%2$s' || %1$s);", var, "\t") + "\n");
                    }
                    sb.append("end;\n");
                    
                    resultArea.setText(sb.toString());
                }catch(Exception ex){
                    JCommonUtil.handleException(ex);
                }
            }
        });
        panel_1.add(btnConvert);
        
        sqlArea = new JTextArea();
//        panel.add(sqlArea, BorderLayout.CENTER);
        JCommonUtil.createScrollComponent(panel, sqlArea);
        
        JPanel panel_2 = new JPanel();
        tabbedPane.addTab("變數script", null, panel_2, null);
        panel_2.setLayout(new BorderLayout(0, 0));
        
        resultArea = new JTextArea();
//        panel_2.add(resultArea, BorderLayout.CENTER);
        JCommonUtil.createScrollComponent(panel_2, resultArea);
        
        JPanel panel_3 = new JPanel();
        tabbedPane.addTab("貼變數結果", null, panel_3, null);
        panel_3.setLayout(new BorderLayout(0, 0));
        
        JPanel panel_4 = new JPanel();
        panel_3.add(panel_4, BorderLayout.NORTH);
        
        JButton btnParseBack = new JButton("parse back");
        btnParseBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                try{
                    String text = StringUtils.defaultString(parseArea.getText());
                    
                    Map<String, String> map = new HashMap<String,String>();
                    Pattern ptn = Pattern.compile("(PKG\\w+\\.\\w+)\\s+(.*)", Pattern.CASE_INSENSITIVE);
                    BufferedReader reader = new BufferedReader(new StringReader(text));
                    for(String line = null; (line = reader.readLine())!=null;){
                        Matcher mth = ptn.matcher(line);
                        if(mth.find()){
                            String pkg = mth.group(1);
                            String val = mth.group(2);
                            map.put(pkg, val);
                        }
                    }
                    reader.close();
                    
                    String text2 = StringUtils.defaultString(sqlArea.getText());
                    for(String pkg : map.keySet()){
                        text2 = text2.replaceAll(pkg, map.get(pkg));
                    }
                    sqlArea.setText(text2);
                }catch(Exception ex){
                    JCommonUtil.handleException(ex);
                }
            }
        });
        panel_4.add(btnParseBack);
        
        parseArea = new JTextArea();
//        panel_3.add(parseArea, BorderLayout.CENTER);
        JCommonUtil.createScrollComponent(panel_3, parseArea);
    }

}
