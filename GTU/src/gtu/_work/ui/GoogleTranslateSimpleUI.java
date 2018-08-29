package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URLEncoder;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.StringUtils;

import gtu.runtime.DesktopUtil;
import gtu.swing.util.JCommonUtil;

public class GoogleTranslateSimpleUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GoogleTranslateSimpleUI frame = new GoogleTranslateSimpleUI();
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
    public GoogleTranslateSimpleUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 492, 331);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.NORTH);

        JPanel panel_1 = new JPanel();
        contentPane.add(panel_1, BorderLayout.WEST);

        JPanel panel_2 = new JPanel();
        contentPane.add(panel_2, BorderLayout.EAST);

        JPanel panel_3 = new JPanel();
        contentPane.add(panel_3, BorderLayout.SOUTH);

        final JTextArea textArea = new JTextArea();
        contentPane.add(JCommonUtil.createScrollComponent(textArea), BorderLayout.CENTER);

        JButton btnNewButton = new JButton("google translate");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String text = StringUtils.trimToEmpty(textArea.getText());
                    text = URLEncoder.encode(text, "UTF-8");
                    DesktopUtil.browse("https://translate.google.com.tw/?hl=zh-TW#en/zh-TW/" + text);
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        panel_3.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("clear");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        });
        panel_3.add(btnNewButton_1);
        
        JCommonUtil.setJFrameCenter(getOwner());
    }

}
