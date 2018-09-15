package gtu.db.sqlMaker;
import gtu.swing.util.JCommonUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang.Validate;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class DbSqlCreaterUI extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JButton executeBtn;
    private JTextField tablenameText;
    private JTextField passwordText;
    private JLabel jLabel2;
    private JTextField usernameText;
    private JTextField urlText;
    private JLabel jLabel1;

    /**
    * Auto-generated main method to display this JFrame
    */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DbSqlCreaterUI inst = new DbSqlCreaterUI();
                inst.setLocationRelativeTo(null);
                 gtu.swing.util.JFrameUtil.setVisible(true,inst);
            }
        });
    }
    
    public DbSqlCreaterUI() {
        super();
        initGUI();
    }
    
    private void initGUI() {
        try {
            BorderLayout thisLayout = new BorderLayout();
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getContentPane().setLayout(thisLayout);
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel1 = new JPanel();
                    jTabbedPane1.addTab("jPanel1", null, jPanel1, null);
                    {
                        jLabel1 = new JLabel();
                        jPanel1.add(jLabel1);
                        jLabel1.setText("URL");
                    }
                    {
                        urlText = new JTextField();
                        jPanel1.add(urlText);
                        urlText.setPreferredSize(new java.awt.Dimension(484, 24));
                        urlText.setText("jdbc:sqlserver://192.168.233.130:1433;DatabaseName=AAA03");
                    }
                    {
                        jLabel2 = new JLabel();
                        jPanel1.add(jLabel2);
                        jLabel2.setText("username");
                        jLabel2.setPreferredSize(new java.awt.Dimension(79, 17));
                    }
                    {
                        usernameText = new JTextField();
                        jPanel1.add(usernameText);
                        usernameText.setPreferredSize(new java.awt.Dimension(424, 24));
                        usernameText.setText("sa");
                    }
                    {
                        jLabel3 = new JLabel();
                        jPanel1.add(jLabel3);
                        jLabel3.setText("password");
                        jLabel3.setPreferredSize(new java.awt.Dimension(80, 17));
                    }
                    {
                        passwordText = new JTextField();
                        jPanel1.add(passwordText);
                        passwordText.setPreferredSize(new java.awt.Dimension(427, 24));
                        passwordText.setText("1234");
                    }
                    {
                        jLabel4 = new JLabel();
                        jPanel1.add(jLabel4);
                        jLabel4.setText("tablename");
                        jLabel4.setPreferredSize(new java.awt.Dimension(65, 17));
                    }
                    {
                        tablenameText = new JTextField();
                        jPanel1.add(tablenameText);
                        tablenameText.setPreferredSize(new java.awt.Dimension(434, 24));
                        tablenameText.setText("AAA03.dbo.address");
                    }
                    {
                        executeBtn = new JButton();
                        jPanel1.add(executeBtn);
                        executeBtn.setText("產生");
                        executeBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                executeBtnActionPerformed(evt);
                            }
                        });
                    }
                }
            }
            pack();
            this.setSize(558, 398);
            System.out.println("init ok!!");
        } catch (Exception e) {
            JCommonUtil.handleException(e);
        }
    }
    
    private void executeBtnActionPerformed(ActionEvent evt) {
        try{
            String url = urlText.getText();
            String username = usernameText.getText();
            String password = passwordText.getText();
            String tablename = tablenameText.getText();
            Validate.notEmpty(url, "url未填");
            Validate.notEmpty(username, "username未填");
            Validate.notEmpty(password, "password未填");
            Validate.notEmpty(tablename, "tablename未填");
            DbSqlCreater janna = new DbSqlCreater(//
                    "com.microsoft.sqlserver.jdbc.SQLServerDriver", url, username, password);
            janna.execute(tablename);
            JCommonUtil._jOptionPane_showMessageDialog_info(tablename + "產生完成");
        }catch(Exception ex){
            JCommonUtil.handleException(ex);
        }
    }
}
