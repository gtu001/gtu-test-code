package gtu._work.ui;

import java.awt.BorderLayout;
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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

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

import org.apache.log4j.Logger;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;

import gtu.file.FileUtil;
import gtu.log.Log;
import gtu.log.PrintStreamAdapter;
import gtu.string.SystemEncodeChange;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JTreeUtil;

public class DropboxTestUI extends JFrame {
    
    private static final Logger logger = Logger.getLogger(DropboxTestUI.class);
    private final PrintStream out = new PrintStream(new PrintStreamAdapter("big5") {
        @Override
        public void println(String message) {
            Log.debug(message);
//            if(StringUtils.length(logArea.getText()) > 500){
//                logArea.setText("");
//            }
            logArea.append(message+"\n");
        }
    }, true);

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
                try {
                    downloadTree.setModel(util.getRootFileModel());
                } catch (DbxException e1) {
                    logger.error(e1.getMessage(), e1);
                    JCommonUtil.handleException(e1);
                }
            }
        });
        panel_2.add(reloadBtn);
        
        JButton button = new JButton("download");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    DefaultMutableTreeNode selectItem = jTreeUtil.getSelectItem();
                    JFile jfile = (JFile)selectItem.getUserObject();
                    util.download(util.getClient(), jfile.name, jfile.path);
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
                    JFile jfile = (JFile)selectItem.getUserObject();
                    File file = JCommonUtil._jFileChooser_selectFileOnly();
                    if(file == null || !file.exists()){
                        JCommonUtil._jOptionPane_showMessageDialog_error("檔案為空或不存在!");
                        return;
                    }
                    if(jfile == null || !jfile.isFolder){
                        JCommonUtil._jOptionPane_showMessageDialog_error("請選擇上傳目錄");
                        return;
                    }
                    util.upload(util.getClient(), file, jfile.path);
                    JCommonUtil._jOptionPane_showMessageDialog_info("上傳 : " + jfile.path + " -> " + file.getName());
                } catch (Exception e2) {
                    logger.error(e2.getMessage(), e2);
                    JCommonUtil.handleException(e2);
                }
            }
        });
        panel_2.add(button_1);
        
        downloadTree = new JTree();
        downloadTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent paramMouseEvent) {
                try {
                    DefaultMutableTreeNode selectItem = jTreeUtil.getSelectItem();
                    JFile jfile = (JFile)selectItem.getUserObject();
                    util.addFileSystemTreeNode(jfile, selectItem, util.getClient());
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    JCommonUtil.handleException(e);
                }
            }
        });
        JCommonUtil.createScrollComponent(panel, downloadTree);
        //panel.add(downloadTree, BorderLayout.CENTER);
        jTreeUtil = JTreeUtil.newInstance(downloadTree);
        
        
        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("log", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));
        
        JLabel label = new JLabel("New label");
        panel_1.add(label, BorderLayout.NORTH);
        
        logArea = new JTextArea();
        panel_1.add(logArea, BorderLayout.CENTER);
        
        setTitle(SystemEncodeChange.getSystemEncode());
    }
    
    private void debug(String message){
//        logger.info(message);
        Log.debug(message);
//        out.println(message);
    }
    
    private class DropUtil {
        private static final String TOKEN = "3y3nIS0p6KoAAAAAAAAzTZcBK7T3it-Yvf-AjbOy-czPn9KMTwYWj3psCcPdX37U";
        
        protected DbxClient getClient(){
            DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0", Locale.getDefault().toString());
            DbxClient client = new DbxClient(config, TOKEN);
            return client;
        }
        
        protected void upload(DbxClient client, File inputFile, String basePath) throws DbxException, IOException{
            FileInputStream inputStream = new FileInputStream(inputFile);
            try {
                DbxEntry.File uploadedFile = client.uploadFile(basePath + "/" + inputFile.getName(),
                    DbxWriteMode.add(), inputFile.length(), inputStream);
                debug("Uploaded: " + uploadedFile.toString());
            } finally {
                inputStream.close();
            }
        }
        
        protected void download(DbxClient client, String name, String path) throws DbxException, IOException{
            FileOutputStream outputStream = new FileOutputStream(new File(FileUtil.DESKTOP_DIR, name));
            try {
                DbxEntry.File downloadedFile = client.getFile(path, null,
                    outputStream);
                debug("Metadata: " + downloadedFile.toString());
            } finally {
                outputStream.close();
            }
        }
        
        protected DefaultTreeModel getRootFileModel() throws DbxException{
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new JFile(null));
            DefaultTreeModel model = new DefaultTreeModel(rootNode);
            addFileSystemTreeNode(new JFile(null), rootNode, getClient());
            return model;
        }
        
        private void addFileSystemTreeNode(JFile jfile, DefaultMutableTreeNode loot, DbxClient client) throws DbxException {
            DefaultMutableTreeNode node = null;
            if (jfile.isFolder) {
                List<DbxEntry> list = new ArrayList<DbxEntry>();
                DbxEntry.WithChildren listing = client.getMetadataWithChildren(jfile.path);
                if (listing != null) {
                    for (DbxEntry ch : listing.children) {
                        list.add(ch);
                    }
                }
                Collections.sort(list, new Comparator<DbxEntry>() {
                    @Override
                    public int compare(DbxEntry o1, DbxEntry o2) {
                        return o1.name.compareTo(o2.name);
                    }
                });
                for (DbxEntry f : list) {
                    JFile f2 = new JFile(f);
                    node = new DefaultMutableTreeNode(f2);
                    //addFileSystemTreeNode(f2, node, client);
                    loot.add(node);
                }
            }
        }
    }
    
    private class JFile {
        String path;
        String name;
        boolean isFolder;
        DbxEntry child;
        JFile(DbxEntry child){
            if(child == null){
                path = "/";
                name = "/";
                isFolder = true;
                return;
            }
            path = child.path;
            name = child.name;
            isFolder = child.isFolder();
            this.child = child;
            debug(toAllString());
        }
        public String toAllString() {
            return "JFile [path=" + path + ", name=" + name + ", isFolder=" + isFolder + ", child=" + child + "]";
        }
        @Override
        public String toString() {
            return "" + name + "";
        }
    }
}
