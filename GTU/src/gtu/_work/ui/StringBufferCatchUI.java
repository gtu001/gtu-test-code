package gtu._work.ui;

import gtu.swing.util.JCommonUtil;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.StringReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class StringBufferCatchUI extends JFrame {

    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    StringBufferCatchUI frame = new StringBufferCatchUI();
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
    public StringBufferCatchUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);
        
        JPanel panel = new JPanel();
        tabbedPane.addTab("原始文字", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));
        
        final JTextArea textArea = new JTextArea();
//        panel.add(textArea, BorderLayout.CENTER);
        JCommonUtil.createScrollComponent(panel, textArea);
        
        
        JPanel panel2 = new JPanel();
        panel.add(panel2, BorderLayout.SOUTH);
        
        JButton btnNewButton = new JButton("格式化");
        panel2.add(btnNewButton);
        
        JButton button = new JButton("清除");
        panel2.add(button);
        
        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("轉化字串", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));
        
        final JTextArea textArea_1 = new JTextArea();
//        panel_1.add(textArea_1, BorderLayout.CENTER);
        JCommonUtil.createScrollComponent(panel_1, textArea_1);
        
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    Validate.notBlank(textArea.getText(), "必須要有內容");
                    String text = textArea.getText();
                    
                    StringBuffer sb = new StringBuffer();
                    BufferedReader reader = new BufferedReader(new StringReader(text));
                    for(String line = null; (line = reader.readLine())!=null;){
                        try{
                            if(StringUtils.isNotBlank(line)){
                                String str = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                                sb.append(str + "\n");
                            }
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                    reader.close();
                    textArea_1.setText(sb.toString());
                    System.out.println("done...");
                }catch(Exception ex){
                    JCommonUtil.handleException(ex);
                }
            }
        });
        
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
                textArea_1.setText("");
            }
        });
    }

}
