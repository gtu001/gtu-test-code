package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;

import gtu.file.FileUtil;
import gtu.log.Log;
import gtu.log.PrintStreamAdapter;
import gtu.net.urlConn.work.DropboxUtilV2;
import gtu.net.urlConn.work.DropboxUtilV2.DropboxUtilV2_DropboxFile;
import gtu.properties.PropertiesUtilBean;
import gtu.string.SystemEncodeChange;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JTreeUtil;

public class DropboxTestUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private static PropertiesUtilBean config = new PropertiesUtilBean(DropboxTestUI.class);
    // private static PropertiesUtilBean config = new PropertiesUtilBean(new
    // File("/media/gtu001/OLD_D/my_tool/DropboxTestUI_config.properties"));
    private static final String TOKEN;
    static {
        TOKEN = StringUtils.trimToEmpty(config.getConfigProp().getProperty("token"));
        if (StringUtils.isBlank(TOKEN)) {
            JCommonUtil._jOptionPane_showMessageDialog_error("無法取得token屬性");
            try {
                Runtime.getRuntime().exec(String.format("cmd /c start notepad \"%s\"", config.getPropFile()));
                Desktop.getDesktop().browse(new URI("https://www.dropbox.com/developers/apps"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.exit(1);
        }
    }

    private static final Logger logger = Logger.getLogger(DropboxTestUI.class);
    private final PrintStream out = new PrintStream(new PrintStreamAdapter("big5") {
        public void println(String message) {
            Log.debug(message);
            // if(StringUtils.length(logArea.getText()) > 500){
            // logArea.setText("");
            // }
            logArea.append(message + "\n");
        }
    });

    private JPanel contentPane;
    private JTree downloadTree;
    private JTreeUtil jTreeUtil;
    private JTextArea logArea;

    private DropUtil util = new DropUtil();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        SystemEncodeChange.setSystemEncode("utf-8");
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DropboxTestUI frame = new DropboxTestUI();
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
    public DropboxTestUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 529, 397);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("download", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        JPanel panel_2 = new JPanel();
        panel.add(panel_2, BorderLayout.NORTH);

        JButton reloadBtn = new JButton("reload");
        reloadBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                initReloadBtnAction();
            }
        });
        panel_2.add(reloadBtn);

        JButton button = new JButton("download");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    DefaultMutableTreeNode selectItem = jTreeUtil.getSelectItem();
                    JFile jfile = (JFile) selectItem.getUserObject();
                    util.download(jfile.name, jfile.path);
                    JCommonUtil._jOptionPane_showMessageDialog_info("檔案下載完成 : " + jfile.name);
                } catch (Exception e1) {
                    logger.error(e1.getMessage(), e1);
                    JCommonUtil.handleException(e1);
                }
            }
        });
        panel_2.add(button);

        JButton button_1 = new JButton("upload");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    DefaultMutableTreeNode selectItem = jTreeUtil.getSelectItem();
                    JFile jfile = (JFile) selectItem.getUserObject();
                    File file = JCommonUtil._jFileChooser_selectFileOnly();
                    if (file == null || !file.exists()) {
                        JCommonUtil._jOptionPane_showMessageDialog_error("檔案為空或不存在!");
                        return;
                    }
                    if (jfile == null || !jfile.isFolder) {
                        JCommonUtil._jOptionPane_showMessageDialog_error("請選擇上傳目錄");
                        return;
                    }

                    if (util.beforeUpload(file, jfile.path)) {
                        util.upload(file, jfile.path);
                        JCommonUtil._jOptionPane_showMessageDialog_info("上傳 : " + jfile.path + " -> " + file.getName());
                        initReloadBtnAction();
                    } else {
                        JCommonUtil._jOptionPane_showMessageDialog_info("取消上傳 : " + jfile.path + " -> " + file.getName());
                    }
                } catch (Exception e2) {
                    logger.error(e2.getMessage(), e2);
                    JCommonUtil.handleException(e2);
                }
            }
        });
        panel_2.add(button_1);

        JButton btnNewButton = new JButton("delete");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                try {
                    DefaultMutableTreeNode selectItem = jTreeUtil.getSelectItem();
                    JFile jfile = (JFile) selectItem.getUserObject();

                    if (jfile.isFolder) {
                        JCommonUtil._jOptionPane_showMessageDialog_error("不提供目錄刪除!");
                        return;
                    }

                    StringBuilder sb = new StringBuilder();
                    sb.append("確定刪除!!!\n");
                    sb.append("name : " + jfile.name + "\n");
                    sb.append("path : " + jfile.path + "\n");
                    boolean chkDelete = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption(sb.toString(), "確定刪除!!!");

                    if (chkDelete) {
                        util.delete(jfile.path);
                        JCommonUtil._jOptionPane_showMessageDialog_info("已刪除 : " + jfile.path);
                        initReloadBtnAction();
                    } else {
                        JCommonUtil._jOptionPane_showMessageDialog_info("已取消刪除!");
                    }
                } catch (Exception e2) {
                    logger.error(e2.getMessage(), e2);
                    JCommonUtil.handleException(e2);
                }
            }
        });
        panel_2.add(btnNewButton);

        downloadTree = new JTree();
        downloadTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent paramMouseEvent) {
                try {
                    DefaultMutableTreeNode selectItem = jTreeUtil.getSelectItem();
                    JFile jfile = (JFile) selectItem.getUserObject();
                    if (selectItem.getChildCount() == 0 && jfile.isFolder) {
                        util.addFileSystemTreeNode(jfile, selectItem, util.getClient());
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    JCommonUtil.handleException(e);
                }
            }
        });
        JCommonUtil.createScrollComponent(panel, downloadTree);
        // panel.add(downloadTree, BorderLayout.CENTER);
        jTreeUtil = JTreeUtil.newInstance(downloadTree);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("log", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        JLabel label = new JLabel("New label");
        panel_1.add(label, BorderLayout.NORTH);

        logArea = new JTextArea();
        panel_1.add(logArea, BorderLayout.CENTER);

        setTitle(SystemEncodeChange.getSystemEncode());

        initReloadBtnAction();

        JCommonUtil.setJFrameCenter(this);
    }

    private void debug(String message) {
        // logger.info(message);
        Log.debug(message);
        // out.println(message);
    }

    private void initReloadBtnAction() {
        try {
            downloadTree.setModel(util.getRootFileModel());
        } catch (DbxException e1) {
            logger.error(e1.getMessage(), e1);
            JCommonUtil.handleException(e1);
        }
    }

    private class DropUtil {

        protected DbxClientV2 getClient() {
            return DropboxUtilV2.getClient(TOKEN);
        }

        protected boolean beforeUpload(File inputFile, String basePath) {
            String uploadPath = basePath + "/" + inputFile.getName();
            if (DropboxUtilV2.exists(uploadPath, getClient())) {
                if (JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("是否先刪除已存在檔案 : " + uploadPath, "檔案已存在")) {
                    DropboxUtilV2.delete(uploadPath, getClient());
                    return true;
                } else {
                }
            }
            return false;
        }

        protected void upload(File inputFile, String basePath) throws DbxException, IOException {
            String uploadPath = basePath + "/" + inputFile.getName();
            FileInputStream inputStream = new FileInputStream(inputFile);
            System.out.println("upload >>" + uploadPath);
            DropboxUtilV2.upload(uploadPath, inputStream, getClient());
        }

        protected void download(String name, String path) throws DbxException, IOException {
            FileOutputStream outputStream = new FileOutputStream(new File(FileUtil.DESKTOP_DIR, name));
            DropboxUtilV2.download(path, outputStream, getClient());
        }

        protected void delete(String path) {
            DropboxUtilV2.delete(path, getClient());
        }

        protected DefaultTreeModel getRootFileModel() throws DbxException {
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new JFile("", ""));
            DefaultTreeModel model = new DefaultTreeModel(rootNode);
            addFileSystemTreeNode(new JFile("", ""), rootNode, getClient());
            return model;
        }

        private void addFileSystemTreeNode(JFile jfile, DefaultMutableTreeNode loot, DbxClientV2 client) throws DbxException {
            DefaultMutableTreeNode node = null;
            if (jfile.isFolder) {
                List<DropboxUtilV2_DropboxFile> list = DropboxUtilV2.listFilesV2(jfile.path, client);
                for (DropboxUtilV2_DropboxFile f : list) {
                    JFile f2 = new JFile(f);
                    node = new DefaultMutableTreeNode(f2);
                    // ////addFileSystemTreeNode(f2, node, client);//不要打開
                    loot.add(node);
                }
            }
        }
    }

    private class JFile {
        String path;
        String name;
        boolean isFolder;

        JFile(String name, String path) {
            this.name = name;
            this.path = path;
            isFolder = true;
            debug(toAllString());
        }

        JFile(DropboxUtilV2_DropboxFile val) {
            this.name = val.getName();
            this.path = val.getFullPath();
            isFolder = val.isFolder();
            debug(toAllString());
        }

        public String toAllString() {
            return "JFile [path=" + path + ", name=" + name + ", isFolder=" + isFolder + "]";
        }

        @Override
        public String toString() {
            return "" + name + "";
        }
    }
}
