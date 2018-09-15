package gtu._work.etc;

import gtu.file.FileUtil;
import gtu.number.RandomUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFileChooserUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JOptionPaneUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTreeUtil;
import gtu.swing.util.JTreeUtil.JFile;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventObject;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class _3DSMovieRenamer extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JButton renameBtn;
    private JTextField renameText;
    private JList vidList;
    private JButton openDir;
    private JPanel jPanel2;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                _3DSMovieRenamer inst = new _3DSMovieRenamer();
                inst.setLocationRelativeTo(null);
                 gtu.swing.util.JFrameUtil.setVisible(true,inst);
            }
        });
    }

    public _3DSMovieRenamer() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            final SwingActionUtil swingUtil = (SwingActionUtil) SwingActionUtil.newInstance(this);

            BorderLayout thisLayout = new BorderLayout();
            getContentPane().setLayout(thisLayout);
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.setTitle("3DS Rename");
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel1 = new JPanel();
                    BorderLayout jPanel1Layout = new BorderLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("vid list", null, jPanel1, null);
                    {
                        openDir = new JButton();
                        jPanel1.add(openDir, BorderLayout.NORTH);
                        openDir.setText("open dir");
                        openDir.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                swingUtil.invokeAction("openDir.actionPerformed", evt);
                            }
                        });
                    }
                    {
                        ListModel vidListModel = new DefaultListModel();
                        vidList = new JList();
                        jPanel1.add(vidList, BorderLayout.CENTER);
                        vidList.setModel(vidListModel);
                        vidList.addMouseListener(new MouseAdapter() {
                            public void mouseClicked(MouseEvent evt) {
                                swingUtil.invokeAction("vidList.mouseClicked", evt);
                            }
                        });
                        vidList.addKeyListener(new KeyAdapter() {
                            public void keyPressed(KeyEvent evt) {
                                swingUtil.invokeAction("vidList.keyPressed", evt);
                            }
                        });
                    }
                    {
                        jPanel3 = new JPanel();
                        jPanel1.add(jPanel3, BorderLayout.SOUTH);
                        jPanel3.setPreferredSize(new java.awt.Dimension(445, 34));
                        {
                            renameText = new JTextField();
                            jPanel3.add(renameText);
                            renameText.setPreferredSize(new java.awt.Dimension(187, 24));
                        }
                        {
                            renameBtn = new JButton();
                            jPanel3.add(renameBtn);
                            renameBtn.setText("rename");
                            renameBtn.setPreferredSize(new java.awt.Dimension(106, 24));
                            renameBtn.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    swingUtil.invokeAction("renameBtn.mouseClicked", evt);
                                }
                            });
                        }
                        {
                            forceChange = new JCheckBox();
                            jPanel3.add(forceChange);
                            forceChange.setText("force");
                            forceChange.setPreferredSize(new java.awt.Dimension(64, 21));
                        }
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("copy", null, jPanel2, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel2.add(jScrollPane1, BorderLayout.CENTER);
                        {
                            ListModel copyToListModel = new DefaultListModel();
                            copyToList = new JList();
                            jScrollPane1.setViewportView(copyToList);
                            copyToList.setModel(copyToListModel);
                            copyToList.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    swingUtil.invokeAction("copyToList.mouseClicked", evt);
                                }
                            });
                        }
                    }
                }
                {
                    jPanel4 = new JPanel();
                    BorderLayout jPanel4Layout = new BorderLayout();
                    jPanel4.setLayout(jPanel4Layout);
                    jTabbedPane1.addTab("BT Movie", null, jPanel4, null);
                    {
                        jScrollPane2 = new JScrollPane();
                        jPanel4.add(jScrollPane2, BorderLayout.WEST);
                        jScrollPane2.setPreferredSize(new java.awt.Dimension(254, 355));
                        {
                            btDirTree = new JTree();
                            jScrollPane2.setViewportView(btDirTree);
                            btDirTree.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    swingUtil.invokeAction(evt);
                                }
                            });
                            btDirTree.addPropertyChangeListener(new PropertyChangeListener() {
                                public void propertyChange(PropertyChangeEvent evt) {
                                    swingUtil.invokeAction(evt);
                                }
                            });
                            JTreeUtil.newInstance(btDirTree).fileSystem(DEFAULT_BT_DIR);
                        }
                    }
                    {
                        jPanel5 = new JPanel();
                        BorderLayout jPanel5Layout = new BorderLayout();
                        jPanel5.setLayout(jPanel5Layout);
                        jPanel4.add(jPanel5, BorderLayout.CENTER);
                        {
                            jScrollPane3 = new JScrollPane();
                            jPanel5.add(jScrollPane3, BorderLayout.CENTER);
                            jScrollPane3.setPreferredSize(new java.awt.Dimension(427, 355));
                            {
                                DefaultListModel btMovListModel = new DefaultListModel();
                                btMovList = new JList();
                                jScrollPane3.setViewportView(btMovList);
                                btMovList.setModel(btMovListModel);
                                btMovList.addMouseListener(new MouseAdapter() {
                                    public void mouseClicked(MouseEvent evt) {
                                        swingUtil.invokeAction(evt);
                                    }
                                });
                                btMovList.addKeyListener(new KeyAdapter() {
                                    public void keyPressed(KeyEvent evt) {
                                        JListUtil.newInstance(btMovList).defaultJListKeyPressed(evt);
                                    }
                                });
                            }
                        }
                    }
                }
                {
                    jPanel6 = new JPanel();
                    FlowLayout jPanel6Layout = new FlowLayout();
                    jTabbedPane1.addTab("common", null, jPanel6, null);
                    jPanel6.setLayout(jPanel6Layout);
                    {
                        execute3dsVidTransfer = new JButton();
                        jPanel6.add(execute3dsVidTransfer);
                        execute3dsVidTransfer.setText("execute 3ds video transfer");
                        execute3dsVidTransfer.setPreferredSize(new java.awt.Dimension(207, 42));
                        execute3dsVidTransfer.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                String bat = "C:/apps/_movie/3DSVideov1.00/3DS Video.exe";
                                try {
                                    Runtime.getRuntime().exec(String.format("cmd /c call \"%s\"", bat));
                                } catch (IOException e) {
                                    JCommonUtil.handleException(e);
                                }
                            }
                        });
                    }
                    {
                        openMovieAppDir = new JButton();
                        jPanel6.add(openMovieAppDir);
                        openMovieAppDir.setText("open movie app dir");
                        openMovieAppDir.setPreferredSize(new java.awt.Dimension(207, 42));
                        openMovieAppDir.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                try {
                                    Desktop.getDesktop().open(new File("C:/apps/_movie"));
                                } catch (IOException e) {
                                    JCommonUtil.handleException(e);
                                }
                            }
                        });
                    }
                }
                {
                    jPanel7 = new JPanel();
                    BorderLayout jPanel7Layout = new BorderLayout();
                    jTabbedPane1.addTab("fake rename", null, jPanel7, null);
                    jPanel7.setLayout(jPanel7Layout);
                    {
                        openFakeRenameDir = new JButton();
                        jPanel7.add(openFakeRenameDir, BorderLayout.NORTH);
                        openFakeRenameDir.setText("open dir");
                        openFakeRenameDir.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                File file = JCommonUtil._jFileChooser_selectDirectoryOnly();
                                if (file != null) {
                                    DefaultListModel model = new DefaultListModel();
                                    for (File f : file.listFiles()) {
                                        model.addElement(f);
                                    }
                                    openFakeRenameDirList.setModel(model);
                                }
                            }
                        });
                    }
                    {
                        DefaultListModel openFakeRenameDirListModel = new DefaultListModel();
                        openFakeRenameDirList = new JList();
                        jPanel7.add(openFakeRenameDirList, BorderLayout.CENTER);
                        openFakeRenameDirList.setModel(openFakeRenameDirListModel);
                        openFakeRenameDirList.addMouseListener(new MouseAdapter() {
                            public void mouseClicked(MouseEvent evt) {
                                File file = (File) JListUtil.getLeadSelectionObject(openFakeRenameDirList);
                                try {
                                    Process process = Runtime.getRuntime().exec(String.format("cmd /c call \"%s\"", file));
                                    InputStream ins = process.getInputStream();
                                    while (ins.read() != -1) {
                                        //TODO
                                    }
                                    ins.close();
                                    System.out.println("done...");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        openFakeRenameDirList.addKeyListener(new KeyAdapter() {
                            public void keyPressed(KeyEvent evt) {
                                JListUtil.newInstance(openFakeRenameDirList).defaultJListKeyPressed(evt);
                            }
                        });
                    }
                    {
                        jPanel8 = new JPanel();
                        jPanel7.add(jPanel8, BorderLayout.SOUTH);
                        jPanel8.setPreferredSize(new java.awt.Dimension(681, 43));
                        {
                            openFakeRenameDir_newName = new JTextField();
                            jPanel8.add(openFakeRenameDir_newName);
                            openFakeRenameDir_newName.setPreferredSize(new java.awt.Dimension(287, 27));
                        }
                        {
                            fakeRenameExecute = new JButton();
                            jPanel8.add(fakeRenameExecute);
                            fakeRenameExecute.setText("execute");
                            fakeRenameExecute.setPreferredSize(new java.awt.Dimension(95, 27));
                            fakeRenameExecute.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    DefaultListModel model = (DefaultListModel) openFakeRenameDirList.getModel();
                                    //TODO
                                }
                            });
                        }
                    }
                }

                swingUtil.addAction("copyToList.mouseClicked", new Action() {
                    public void action(EventObject evt) throws Exception {
                        try {
                            if (((MouseEvent) evt).getButton() == 3) {
                                JMenuItem reloadMenu = new JMenuItem();
                                reloadMenu.setText("reload SD card directory");
                                reloadMenu.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        DefaultListModel copyToListModel = new DefaultListModel();
                                        for (final File f : load3DSDir.listFiles(new FilenameFilter() {
                                            public boolean accept(File paramFile, String paramString) {
                                                if (paramFile.isDirectory() && paramString.matches(CUSTOM_3DS_DIR_PATTERN)) {
                                                    return true;
                                                }
                                                return false;
                                            }
                                        })) {
                                            copyToListModel.addElement(f);
                                        }
                                        copyToList.setModel(copyToListModel);
                                    }
                                });
                                JMenuItem copyAllToMenu = new JMenuItem();
                                {
                                    copyAllToMenu.setText(String.format("move %d vids to...", vidList.getModel().getSize()));
                                    final File toDir = (File) JListUtil.getLeadSelectionObject(copyToList);
                                    if (toDir == null || !toDir.exists() || !toDir.isDirectory()) {
                                        copyAllToMenu.setEnabled(false);
                                    }
                                    if (vidList.getModel().getSize() == 0) {
                                        copyAllToMenu.setText("copy no file...");
                                        copyAllToMenu.setEnabled(false);
                                    }
                                    copyAllToMenu.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            DefaultListModel model = JListUtil.newInstance(vidList).getModel();
                                            if (JOptionPaneUtil.ComfirmDialogResult.YES_OK_OPTION != JOptionPaneUtil.newInstance().iconWaringMessage().confirmButtonYesNo()
                                                    .showConfirmDialog("are you sure copy files : " + model.getSize() + "\n to dir : " + toDir, "COPY VIDS")) {
                                                return;
                                            }
                                            for (int ii = 0; ii < model.getSize(); ii++) {
                                                File src = (File) model.getElementAt(ii);
                                                src.renameTo(new File(toDir, src.getName()));
                                            }
                                            JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog("copy completed!", "SUCCESS");
                                            loadDirVids();
                                        }
                                    });
                                }

                                JPopupMenuUtil.newInstance(copyToList).applyEvent((MouseEvent) evt).addJMenuItem(reloadMenu, copyAllToMenu).show();
                            }
                        } catch (Exception ex) {
                            JCommonUtil.handleException(ex);
                        }
                    }
                });
                swingUtil.addAction("openDir.actionPerformed", new Action() {
                    public void action(EventObject evt) throws Exception {
                        File file = JFileChooserUtil.newInstance().selectDirectoryOnly().showOpenDialog().getApproveSelectedFile();
                        if (file == null) {
                            JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("dir not corrent!, set desktop", getTitle());
                            loadDir = FileUtil.DESKTOP_DIR;
                        } else {
                            loadDir = file;
                        }
                        loadDirVids();
                    }
                });
                swingUtil.addAction("vidList.mouseClicked", new Action() {
                    //                    final String player = "C:/Program Files (x86)/GRETECH/GomPlayer/GOM.EXE";

                    public void action(EventObject evt) throws Exception {
                        int pos = -1;
                        if ((pos = vidList.getLeadSelectionIndex()) == -1) {
                            return;
                        }
                        MouseEvent mevt = (MouseEvent) evt;

                        final File selectItem = (File) vidList.getModel().getElementAt(pos);

                        List<JMenuItem> menuList = new ArrayList<JMenuItem>();
                        JMenuItem simpleRenamer = new JMenuItem();
                        final String simpleRenamePrefix = RandomUtil.upperCase(3);
                        simpleRenamer.setText("Rename : " + simpleRenamePrefix);
                        simpleRenamer.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent paramActionEvent) {
                                String value = StringUtils.defaultString(JOptionPaneUtil.newInstance().showInputDialog("3 char prefix?", "AVI PREFIX"), simpleRenamePrefix);
                                if (value != null && value.matches("[a-zA-Z]{3}")) {
                                    selectItem.renameTo(getNewFile(selectItem.getParentFile(), value));
                                    loadDirVids();
                                } else {
                                    JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("prefix is not correct!", "ERROR");
                                }
                            }
                        });
                        menuList.add(simpleRenamer);
                        if (load3DSDir == null) {
                            reload3DSDir();
                            if (load3DSDir == null) {
                                JMenuItem disable = new JMenuItem();
                                disable.setText("MOVE : SD card is not set!");
                                disable.setEnabled(false);
                                menuList.add(disable);
                            }
                        } else {
                            for (final File f : load3DSDir.listFiles(new FilenameFilter() {
                                public boolean accept(File paramFile, String paramString) {
                                    if (paramFile.isDirectory() && paramString.matches(CUSTOM_3DS_DIR_PATTERN)) {
                                        return true;
                                    }
                                    return false;
                                }
                            })) {
                                JMenuItem copyTo = new JMenuItem();
                                copyTo.setText("MOVE : " + f.getName());
                                copyTo.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent paramActionEvent) {
                                        if (JOptionPaneUtil.ComfirmDialogResult.YES_OK_OPTION == JOptionPaneUtil.newInstance().iconQuestionMessage().confirmButtonYesNo().showConfirmDialog(//
                                                "are you sure move file\n" + //
                                                        selectItem + "\n" + //
                                                        "to\n" + //
                                                        "dir : " + f + "  ??"//
                                                , "COPY FILE")) {

                                            File copyToNewDirFile = new File(f, selectItem.getName());
                                            if (copyToNewDirFile.exists()) {
                                                JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("target dir file already exist!, need rename!", "FILE ALREADY EXIST");
                                                return;
                                            }
                                            selectItem.renameTo(copyToNewDirFile);
                                            loadDirVids();

                                            JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("move completed!", "MOVE FILE");
                                        }
                                    }
                                });
                                menuList.add(copyTo);
                            }
                        }
                        JPopupMenuUtil.newInstance(vidList).applyEvent(mevt).addJMenuItem(menuList.toArray(new JMenuItem[menuList.size()])).show();

                        if (mevt.getClickCount() != 2) {
                            return;
                        }
                        String clkItemPath = selectItem.getAbsolutePath();
                        String command = "cmd /c call \"" + clkItemPath + "\"";
                        System.out.println(command);
                        Runtime.getRuntime().exec(command);
                    }
                });
                swingUtil.addAction("vidList.keyPressed", new Action() {
                    public void action(EventObject evt) throws Exception {
                        JListUtil.newInstance(vidList).defaultJListKeyPressed(evt);
                    }
                });
                swingUtil.addAction("renameBtn.mouseClicked", new Action() {

                    Pattern aviNamePattern = Pattern.compile("^([a-zA-Z]{3})_\\d{4}\\.[aA][vV][iI]$");

                    public void action(EventObject evt) throws Exception {
                        String name = StringUtils.defaultIfEmpty(renameText.getText(), RandomUtil.upperCase(3));
                        System.out.println("name = " + name + ", force : " + forceChange.isSelected());
                        if (!name.matches("[a-zA-Z]{3}")) {
                            renameText.setText("");
                            JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("rename must eng 3 char!", "ERROR");
                            return;
                        }
                        DefaultListModel model = (DefaultListModel) vidList.getModel();
                        boolean matchOk = false;
                        if (model.size() != 0) {
                            File oldFile = null;
                            Matcher matcher = null;
                            for (int ii = 0; ii < model.getSize(); ii++) {
                                oldFile = (File) model.getElementAt(ii);

                                if (!oldFile.exists()) {
                                    JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("file not exeist : \n" + oldFile.getAbsolutePath(), getTitle());
                                    return;
                                }
                                matcher = aviNamePattern.matcher(oldFile.getName());
                                matchOk = matcher.find();
                                System.out.println("matchOk = " + matchOk);
                                if (matchOk && !forceChange.isSelected()) {
                                    oldFile.renameTo(getNewFile(oldFile.getParentFile(), matcher.group(1)));
                                } else {
                                    oldFile.renameTo(getNewFile(oldFile.getParentFile(), name));
                                }
                            }
                            JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog("success!", getTitle());
                        }
                        loadDirVids();
                    }
                });

                ToolTipManager.sharedInstance().setInitialDelay(0);
                swingUtil.addAction(btMovList, MouseEvent.class, new Action() {
                    public void action(EventObject evt) throws Exception {
                        final File file = (File) JListUtil.getLeadSelectionObject(btMovList);
                        if (JMouseEventUtil.buttonLeftClick(1, evt)) {
                            btMovList.setToolTipText(DateFormatUtils.format(file.lastModified(), "yyyy/MM/dd HH:mm:ss") + " length:" + (file.length() / 1024) + "k");
                        }

                        final Object[] objects = btMovList.getSelectedValues();
                        JPopupMenuUtil.newInstance(btMovList).applyEvent(evt)//
                                .addJMenuItem("move out", (objects != null && objects.length > 0), new ActionListener() {
                                    public void actionPerformed(ActionEvent paramActionEvent) {
                                        List<File> list = new ArrayList<File>();
                                        for (Object val : objects) {
                                            list.add((File) val);
                                        }
                                        if (!JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("sure move file from\n" + list.toString().replace(',', '\n') + "\nto\n" + DEFAULT_BT_DIR, "MOVE")) {
                                            return;
                                        }
                                        StringBuilder sb = new StringBuilder();
                                        File moveTo = null;
                                        for (File file : list) {
                                            sb.append((file.renameTo(moveTo = new File(DEFAULT_BT_DIR, file.getName())) && moveTo.exists()) ? "" : file + "\n");
                                        }
                                        JCommonUtil._jOptionPane_showMessageDialog_info(sb.length() == 0 ? "move success!" : "move failed!\n" + sb);
                                    }
                                }).addJMenuItem("delete this", file.exists(), new ActionListener() {
                                    public void actionPerformed(ActionEvent paramActionEvent) {
                                        if (!JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("sure delete file \n" + file, "DELETE")) {
                                            return;
                                        }
                                        boolean result = file.delete();
                                        System.out.println("!!!!!" + result + "..." + file.exists());
                                        JCommonUtil._jOptionPane_showMessageDialog_info(result ? "delete success!" : "delete failed!");
                                    }
                                }).show();

                        if (JMouseEventUtil.buttonLeftClick(2, evt)) {
                            Runtime.getRuntime().exec(String.format("cmd /c call \"%s\"", file));
                        }
                    }
                });
                swingUtil.addAction(btDirTree, MouseEvent.class, new Action() {

                    File getSingleFile() {
                        return ((JFile) JTreeUtil.newInstance(btDirTree).getSelectItem().getUserObject()).getFile();
                    }

                    public void action(EventObject evt) throws Exception {
                        int selectCount = btDirTree.getSelectionModel().getSelectionCount();
                        if (selectCount == 1) {
                            final File file = getSingleFile();
                            JPopupMenuUtil.newInstance(btDirTree).applyEvent(evt).addJMenuItem("delete this", selectCount == 1 && file.exists(), new ActionListener() {
                                public void actionPerformed(ActionEvent paramActionEvent) {
                                    if (file.isFile()) {
                                        if (JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("sure delete FILE : \n" + file, "WARNING")) {
                                            file.delete();
                                            JCommonUtil._jOptionPane_showMessageDialog_info((file.exists() ? "delete failed!" : "delete success!"));
                                        }
                                    }
                                    if (file.isDirectory()) {
                                        StringBuilder sb = new StringBuilder();
                                        if (JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("sure delete DIR : \n" + file, "WARNING")) {
                                            List<Boolean> delL = new ArrayList<Boolean>();
                                            for (File f : file.listFiles()) {
                                                if (fileExtensionPattern.matcher(f.getName()).find() || f.length() > 1000000L) {
                                                    if (!JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("delete this : \n" + f, "CHECK AGAIN")) {
                                                        continue;
                                                    }
                                                    delL.add(f.delete());
                                                }
                                                delL.add(f.delete());
                                            }
                                            for (File f : file.listFiles()) {
                                                if (f.exists()) {
                                                    sb.append(f + "\n");
                                                }
                                            }
                                            System.out.println("delL.contains(false)==================>" + delL.contains(false));
                                        }
                                        if (!file.delete()) {
                                            sb.append(file + "\n");
                                        }
                                        JCommonUtil._jOptionPane_showMessageDialog_info(sb.length() > 0 ? "delete failed!\nlist:\n" + sb : "delete success!");
                                        if (sb.length() == 0) {
                                            DefaultMutableTreeNode node = JTreeUtil.newInstance(btDirTree).getSelectItem();
                                            System.out.println(JTreeUtil.newInstance(btDirTree).removeNode(node));
                                        }
                                    }
                                }
                            }).addJMenuItem("open dir", new ActionListener() {
                                public void actionPerformed(ActionEvent paramActionEvent) {
                                    File openTarget = file;
                                    if (file.isFile()) {
                                        openTarget = file.getParentFile();
                                    }
                                    try {
                                        Desktop.getDesktop().open(openTarget);
                                    } catch (IOException e) {
                                        JCommonUtil.handleException(e);
                                    }
                                }
                            }).show();
                        }
                    }
                });
                swingUtil.addAction(btDirTree, PropertyChangeEvent.class, new Action() {
                    public void action(EventObject evt) throws Exception {
                        List<File> list = new ArrayList<File>();
                        for (DefaultMutableTreeNode node : JTreeUtil.newInstance(btDirTree).getSelectItems()) {
                            JFile jfile = (JFile) node.getUserObject();
                            if (jfile.getFile().isDirectory()) {
                                for (File f : jfile.getFile().listFiles(new FilenameFilter() {
                                    public boolean accept(File paramFile, String paramString) {
                                        return fileExtensionPattern.matcher(paramString).find();
                                    }
                                })) {
                                    System.out.println(f.getName() + "...." + f.length());
                                    list.add(f);
                                }
                            }
                        }
                        Collections.sort(list, new Comparator<File>() {
                            public int compare(File paramT1, File paramT2) {
                                return paramT1.lastModified() > paramT2.lastModified() ? -1 : 1;
                            }
                        });
                        btMovList.setModel(JListUtil.createModel(list.iterator()));
                    }
                });
            }
            this.setSize(702, 422);

            loadDirVids();
            reload3DSDir();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static final File DEFAULT_BT_DIR = new File("D:\\Downloads");

    File getNewFile(File parent, String name) {
        File newFile = null;
        for (newFile = new File(parent, newName(name)); newFile.exists(); //
        newFile = new File(parent, newName(name)))
            ;
        return newFile;
    }

    String newName(String prefix) {
        String rightNum = String.valueOf((int) (Math.random() * 10000));
        System.out.println(rightNum);
        rightNum = StringUtils.leftPad(rightNum, 4, '0');
        return prefix + "_" + rightNum + ".avi";
    }

    static File loadDir = FileUtil.DESKTOP_DIR;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JButton execute3dsVidTransfer;
    private JPanel jPanel6;
    private JScrollPane jScrollPane3;
    private JList btMovList;
    private JPanel jPanel5;
    private JTree btDirTree;
    private JButton fakeRenameExecute;
    private JTextField openFakeRenameDir_newName;
    private JPanel jPanel8;
    private JList openFakeRenameDirList;
    private JButton openFakeRenameDir;
    private JPanel jPanel7;
    private JButton openMovieAppDir;
    private JPanel jPanel4;
    private JCheckBox forceChange;
    private JPanel jPanel3;
    private JList copyToList;
    static File load3DSDir;

    static Pattern fileExtensionPattern = Pattern.compile("\\.(?=avi|mp4|flv|jpg)\\w+", Pattern.CASE_INSENSITIVE);
    static String CUSTOM_3DS_DIR_PATTERN = "999\\w{5}";

    static {
        reload3DSDir();
    }

    static void reload3DSDir() {
        File tmp = null;
        for (File f : File.listRoots()) {
            if ((tmp = new File(f, "DCIM")).exists()) {
                load3DSDir = tmp;
                break;
            }
        }
    }

    void loadDirVids() {
        if (!loadDir.isDirectory()) {
            return;
        }
        DefaultListModel vidListModel = new DefaultListModel();
        for (File f : loadDir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                if (pathname.getName().toLowerCase().endsWith(".avi")) {
                    return true;
                }
                return false;
            }
        })) {
            vidListModel.addElement(f);
        }
        if (vidListModel.size() == 0) {
            JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("no avi file", getTitle());
        }
        vidList.setModel(vidListModel);
    }
}
