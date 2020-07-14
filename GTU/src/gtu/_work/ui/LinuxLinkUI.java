package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.file.FileUtil;
import gtu.runtime.RuntimeBatPromptModeUtil;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.swing.util.JFrameUtil;

public class LinuxLinkUI extends JFrame {

    private JPanel contentPane;
    private HideInSystemTrayHelper hideInSystemTrayHelper;
    private JTextField targetFileText;
    private JTextField linkFileText;
    private JTextField linkFileDirText;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance_delable(LinuxLinkUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LinuxLinkUI frame = new LinuxLinkUI();
                    gtu.swing.util.JFrameUtil.setVisible(true, frame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public LinuxLinkUI() {
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
        panel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

        JLabel label = new JLabel("要連結的檔");
        panel.add(label, "2, 2, right, default");

        targetFileText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(targetFileText, true);
        panel.add(targetFileText, "4, 2, fill, default");
        targetFileText.setColumns(10);
        targetFileText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                try {
                    String text = targetFileText.getText();
                    if (StringUtils.isNotBlank(text)) {
                        File f = new File(text);
                        String lnkname = FileUtil.getNameNoSubName(f);
                        linkFileText.setText(lnkname + ".lnk");
                    }
                } catch (Exception ex) {
                }
            }
        }));

        JLabel lblNewLabel = new JLabel("產出link的檔名");
        panel.add(lblNewLabel, "2, 4, right, default");

        linkFileText = new JTextField();
        panel.add(linkFileText, "4, 4, fill, default");
        linkFileText.setColumns(10);

        JButton btnNewButton = new JButton("產生link");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeLinkAction();
            }
        });

        JLabel lbllink = new JLabel("產出link的目錄");
        panel.add(lbllink, "2, 6, right, default");

        linkFileDirText = new JTextField();
        panel.add(linkFileDirText, "4, 6, fill, default");
        linkFileDirText.setColumns(10);
        linkFileDirText.setText(FileUtil.DESKTOP_PATH);
        JCommonUtil.jTextFieldSetFilePathMouseEvent(linkFileDirText, true);
        panel.add(btnNewButton, "2, 14");

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_1, null);

        {
            JCommonUtil.setJFrameCenter(this);
            JCommonUtil.setJFrameIcon(this, "resource/images/ico/tk_aiengine.ico");
            hideInSystemTrayHelper = HideInSystemTrayHelper.newInstance();
            hideInSystemTrayHelper.apply(this);
            this.applyAppMenu();
        }
    }

    private File getLinkDir() {
        if (StringUtils.isNotBlank(linkFileDirText.getText())) {
            File tmpDir = new File(linkFileDirText.getText());
            if (tmpDir.exists() && tmpDir.isDirectory()) {
                return tmpDir;
            }
        }
        JCommonUtil._jOptionPane_showMessageDialog_error("link產生目錄有問題,使用預設目錄 : " + FileUtil.DESKTOP_DIR);
        return FileUtil.DESKTOP_DIR;
    }

    private void makeLinkAction() {
        try {
            String targetName = linkFileText.getText();
            Validate.notBlank(targetName, "link檔名不可為空");
            Validate.notBlank(targetFileText.getText(), "目的檔案不可為空");

            File target = new File(targetFileText.getText());
            File linkFile = new File(getLinkDir(), targetName);
            if (!target.exists()) {
                Validate.isTrue(false, "檔案不存在 : " + target);
            }
            if (linkFile.exists()) {
                Validate.isTrue(false, "目的檔案以存在 : " + linkFile);
            }

            String exeCommand = String.format(" ln -s \"%s\" \"%s\" ", target, linkFile);
            RuntimeBatPromptModeUtil.newInstance().command(exeCommand).apply();

            JCommonUtil._jOptionPane_showMessageDialog_info("done...");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void applyAppMenu() {
        JMenu menu1 = JMenuAppender.newInstance("child_item").addMenuItem("detail1", null).getMenu();
        JMenu mainMenu = JMenuAppender.newInstance("file").addMenuItem("item1", null).addMenuItem("item2", null).addChildrenMenu(menu1).getMenu();
        JMenuBarUtil.newInstance().addMenu(mainMenu).apply(this);
    }
}
