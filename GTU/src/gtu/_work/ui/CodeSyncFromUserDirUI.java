package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu._work.CodeSyncFromUserDir;
import gtu.swing.util.JCommonUtil;

public class CodeSyncFromUserDirUI extends JFrame {

    private JPanel contentPane;
    private JTextField fromDirText;
    private JTextField toDirText;
    private JTextArea logTextArea;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CodeSyncFromUserDirUI frame = new CodeSyncFromUserDirUI();
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
    public CodeSyncFromUserDirUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 502, 391);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);
        
        JPanel panel = new JPanel();
        tabbedPane.addTab("New tab", null, panel, null);
        panel.setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.RELATED_GAP_COLSPEC,
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,}));
        
        JLabel label = new JLabel("來源");
        panel.add(label, "2, 2, right, default");
        
        fromDirText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(fromDirText, true);
        panel.add(fromDirText, "4, 2, fill, default");
        fromDirText.setColumns(10);
        
        JLabel label_1 = new JLabel(" 目的");
        panel.add(label_1, "2, 4, right, default");
        
        toDirText = new JTextField();
        toDirText.setColumns(10);
        toDirText.setText("D:\\workspace\\TWSP\\twsp");
        JCommonUtil.jTextFieldSetFilePathMouseEvent(toDirText, true);
        panel.add(toDirText, "4, 4, fill, default");
        
        final JButton btnNewButton = new JButton("執行");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    final File fromDir = JCommonUtil.filePathCheck(fromDirText.getText(), "來源sync目錄", true);
                    final File toDir = JCommonUtil.filePathCheck(toDirText.getText(), "目的sync目錄", true);
                    
                    final PrintStream out = JCommonUtil.getNewPrintStream2JTextArea(logTextArea, -1, true);
                    
                    btnNewButton.setEnabled(false);
                    
                    final CodeSyncFromUserDir t = new CodeSyncFromUserDir();
                    
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            t.exceute(fromDir, toDir, out);
                            btnNewButton.setEnabled(true);
                            JCommonUtil._jOptionPane_showMessageDialog_info("完成");
                        }}).start();
                }catch(Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        panel.add(btnNewButton, "2, 24");
        
        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));
        
        logTextArea = new JTextArea();
        JCommonUtil.createScrollComponent(panel_1, logTextArea);
    }
}
