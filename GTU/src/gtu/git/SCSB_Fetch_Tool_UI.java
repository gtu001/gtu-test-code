package gtu.git;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.properties.PropertiesUtilBean;
import gtu.swing.util.JCommonUtil;

public class SCSB_Fetch_Tool_UI extends JFrame {

    private JPanel contentPane;
    private JTextField accountText;
    private JTextField dirText;
    
    private PropertiesUtilBean config = new PropertiesUtilBean(SCSB_Fetch_Tool_UI.class);

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SCSB_Fetch_Tool_UI frame = new SCSB_Fetch_Tool_UI();
                     gtu.swing.util.JFrameUtil.setVisible(true,frame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public SCSB_Fetch_Tool_UI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
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
                RowSpec.decode("default:grow"),}));
        
        JLabel lblNewLabel = new JLabel("帳號");
        panel.add(lblNewLabel, "2, 2, right, default");
        
        accountText = new JTextField();
        panel.add(accountText, "4, 2, fill, default");
        accountText.setColumns(10);
        
        JLabel label = new JLabel("專案目錄");
        panel.add(label, "2, 4, right, default");
        
        dirText = new JTextField();
        dirText.setColumns(10);
        JCommonUtil.jTextFieldSetFilePathMouseEvent(dirText, true);
        panel.add(dirText, "4, 4, fill, default");
        
        if(config.getConfigProp().containsKey("account")) {
            accountText.setText(config.getConfigProp().getProperty("account"));
        }
        if(config.getConfigProp().containsKey("dir")) {
            dirText.setText(config.getConfigProp().getProperty("dir"));
        }
        
        JPanel panel_1 = new JPanel();
        panel.add(panel_1, "4, 14, fill, fill");
        
        JButton btnFetch = new JButton("Fetch");
        btnFetch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String account = accountText.getText();
                    String dir = dirText.getText();
                    
                    String[] args = new String[] {dir, account};
                    GitAutoFetchNMerge.main(args);
                    
                    config.getConfigProp().setProperty("account", account);
                    config.getConfigProp().setProperty("dir", dir);
                    config.store();
                    
                    JCommonUtil._jOptionPane_showMessageDialog_info("ok!");
                }catch(Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        panel_1.add(btnFetch);
    }

}
