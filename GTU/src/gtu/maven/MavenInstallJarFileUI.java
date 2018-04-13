package gtu.maven;

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
import gtu.runtime.ProcessRuntimeExec;
import gtu.swing.util.JCommonUtil;

public class MavenInstallJarFileUI extends JFrame {

    private JPanel contentPane;
    private JTextField mavenBinDirText;
    private JTextField targetJarPathText;
    private JTextField groupIdText;
    private JTextField artifactIdText;
    private JTextField versionText;
    private JTextField targetRespositoryDirText;

    private PropertiesUtilBean config = new PropertiesUtilBean(MavenInstallJarFileUI.class);

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MavenInstallJarFileUI frame = new MavenInstallJarFileUI();
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
    public MavenInstallJarFileUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 560, 419);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("New tab", null, panel, null);
        panel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));

        JLabel lblMavenBinDir = new JLabel("maven bin dir");
        panel.add(lblMavenBinDir, "2, 2, right, default");

        mavenBinDirText = new JTextField();
        panel.add(mavenBinDirText, "4, 2, fill, default");
        mavenBinDirText.setColumns(10);

        JLabel lblNewLabel = new JLabel("target Jar Path");
        panel.add(lblNewLabel, "2, 4, right, default");

        targetJarPathText = new JTextField();
        panel.add(targetJarPathText, "4, 4, fill, default");
        targetJarPathText.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("groupId");
        panel.add(lblNewLabel_1, "2, 6, right, default");

        groupIdText = new JTextField();
        panel.add(groupIdText, "4, 6, fill, default");
        groupIdText.setColumns(10);

        JLabel lblNewLabel_2 = new JLabel("artifactId");
        panel.add(lblNewLabel_2, "2, 8, right, default");

        artifactIdText = new JTextField();
        panel.add(artifactIdText, "4, 8, fill, default");
        artifactIdText.setColumns(10);

        JLabel lblNewLabel_3 = new JLabel("version");
        panel.add(lblNewLabel_3, "2, 10, right, default");

        versionText = new JTextField();
        panel.add(versionText, "4, 10, fill, default");
        versionText.setColumns(10);

        JLabel lblNewLabel_4 = new JLabel("targetRespositoryDir");
        panel.add(lblNewLabel_4, "2, 12, right, default");

        targetRespositoryDirText = new JTextField();
        panel.add(targetRespositoryDirText, "4, 12, fill, default");
        targetRespositoryDirText.setColumns(10);

        JPanel panel_1 = new JPanel();
        panel.add(panel_1, "4, 24, fill, fill");

        JCommonUtil.jTextFieldSetFilePathMouseEvent(mavenBinDirText, true);
        JCommonUtil.jTextFieldSetFilePathMouseEvent(targetRespositoryDirText, true);
        JCommonUtil.jTextFieldSetFilePathMouseEvent(targetJarPathText, false);
        
        config.init(this);

        JButton btnNewButton = new JButton("執行");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String command = MavenInstallJarFile.newInstance()//
                            .mavenBinDir(mavenBinDirText.getText())//
                            .artifactId(artifactIdText.getText())//
                            .groupId(groupIdText.getText())//
                            .version(versionText.getText())//
                            .targetJarPath(targetJarPathText.getText())//
                            .targetRespositoryDir(targetRespositoryDirText.getText())//
                            .buildCommand_singleLine();
                    
                    System.out.println(command);
                    boolean result = ProcessRuntimeExec.runCommandForWin(command);
                    
                    if (result) {
                        config.setConfig(MavenInstallJarFileUI.this);
                        config.store();
                    }
                    
                    JCommonUtil._jOptionPane_showMessageDialog_info("執行 " + (result ? "成功" : "失敗") + "!");
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        panel_1.add(btnNewButton);
    }
}
