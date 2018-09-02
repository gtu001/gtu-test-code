package _temp.janna.ex2;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import _temp.janna.ex2.Test32_forJannaNew.Test32Region;
import gtu.poi.hssf.ExcelUtil;
import gtu.swing.util.JCommonUtil;

public class IISI_Report_For_Janna extends JFrame {

    private JPanel contentPane;
    private JTextField xlsFileText;
    private JTextField sheetAtNumText;
    private JTextField posLeftTopText;
    private JTextField posRightButtomText;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    IISI_Report_For_Janna frame = new IISI_Report_For_Janna();
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
    public IISI_Report_For_Janna() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 607, 433);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        
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
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,}));
        
        JLabel lblxls = new JLabel("請選擇檔案xls");
        panel.add(lblxls, "2, 2, right, default");
        
        xlsFileText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(xlsFileText, false);
        panel.add(xlsFileText, "4, 2, fill, default");
        xlsFileText.setColumns(10);
        
        JLabel lbln = new JLabel("請輸入頁簽代號(0-N)");
        panel.add(lbln, "2, 4, right, default");
        
        sheetAtNumText = new JTextField();
        sheetAtNumText.setColumns(10);
        panel.add(sheetAtNumText, "4, 4, fill, default");
        
        JLabel lblexB = new JLabel("請輸入左上角欄位(Ex: B2)");
        panel.add(lblexB, "2, 6, right, default");
        
        posLeftTopText = new JTextField();
        posLeftTopText.setColumns(10);
        panel.add(posLeftTopText, "4, 6, fill, default");
        
        JLabel lblexAa = new JLabel("請輸入右下角欄位(Ex: AA10)");
        panel.add(lblexAa, "2, 8, right, default");
        
        JButton button = new JButton("執行");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                thisActionPerformed(e);
            }
        });
        
        posRightButtomText = new JTextField();
        posRightButtomText.setColumns(10);
        panel.add(posRightButtomText, "4, 8, fill, default");
        panel.add(button, "2, 28");
        
        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_1, null);
    }

    private void thisActionPerformed(ActionEvent e) {
        try {
            File xlsFile = JCommonUtil.filePathCheck(xlsFileText.getText(), "請選擇xls", "xls");
            Integer sheetAt = Integer.parseInt(sheetAtNumText.getText());
            String startPos = posLeftTopText.getText();
            String endPos = posRightButtomText.getText();
            Test32_forJannaNew t = new Test32_forJannaNew();
            
            HSSFWorkbook wbook = ExcelUtil.getInstance().readExcel(xlsFile);
            
            Test32Region region = new Test32Region(startPos, endPos);
            t.execute(wbook, region, sheetAt);
            
            JCommonUtil._jOptionPane_showMessageDialog_info("done...");
        }catch(Exception ex) {
            ex.printStackTrace();
            JCommonUtil.handleException(ex);
        }
    }
}
