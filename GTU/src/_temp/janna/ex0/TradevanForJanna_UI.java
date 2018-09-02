package _temp.janna.ex0;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.file.FileUtil;
import gtu.swing.util.JCommonUtil;

public class TradevanForJanna_UI extends JFrame {

    private JPanel contentPane;
    private JTextField srcText;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TradevanForJanna_UI frame = new TradevanForJanna_UI();
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
    public TradevanForJanna_UI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
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
                FormFactory.DEFAULT_ROWSPEC,}));
        
        JLabel label = new JLabel("檔案或目錄");
        panel.add(label, "2, 2, right, default");
        
        srcText = new JTextField();
        panel.add(srcText, "4, 2, fill, default");
        srcText.setColumns(10);
        
        JCommonUtil.jTextFieldSetFilePathMouseEvent(srcText, true);
        
        JButton button = new JButton("執行");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try{
                    File dirOrFile = JCommonUtil.filePathCheck(srcText.getText(), "請設定路徑", false);
                    List<File> searchList = new ArrayList<File>();
                    if(dirOrFile.isDirectory()){
                        FileUtil.searchFileMatchs(dirOrFile, ".*\\.docx", searchList);
                    }else{
                        searchList.add(dirOrFile);
                    }
                    
                    File output = new File(FileUtil.DESKTOP_DIR, "janna_關貿sql");
                    output.mkdirs();
                    
                    int count = 0;
                    
                    for(File f : searchList){
                        TradevanForJanna_MssqlGenerator t = new TradevanForJanna_MssqlGenerator();
                        t.execute(f);
                        
                        TradevanForJanna_MssqlGenerator_CreateSQL t2 = new TradevanForJanna_MssqlGenerator_CreateSQL();
                        String sql = t2.execute(t.tabMap);
                        FileUtils.write(new File(output, FileUtil.getNameNoSubName(f) + ".sql"), sql, "utf8");
                        count ++;
                    }
                    
                    JCommonUtil._jOptionPane_showMessageDialog_error("完成! 檔案數 : " + count);
                }catch(Exception ex){
                    JCommonUtil.handleException(ex);
                }
            }
        });
        panel.add(button, "2, 4");
        
        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_1, null);
    }

}
