package com.iisigroup.ris;

import gtu.swing.util.JListUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListModel;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iisigroup.ris.WebFileScanUtil.PageInfo;

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
public class WebFileScanUtilBrowserUI extends javax.swing.JFrame {
    private static final long serialVersionUID = 8977349525132155197L;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        WebFileScanUtilBrowserUI ui = WebFileScanUtilBrowserUI.getInstance();
        List<PageInfo> list = new ArrayList<PageInfo>();
        // PageInfo(File orignWebFile, String fileName, String fullPath, String
        // unfixFullPath, File sourceFile)
        File f = new File("C:\\WINDOWS\\desktop.ini");
        //		PageInfo page = new PageInfo(f, f.getName(), f.getAbsolutePath(), f.getAbsolutePath(), f);
        //		list.add(page);
        ui.sourceFileList(list);
        ui.setVisible(true);
    }

    public static WebFileScanUtilBrowserUI getInstance() {
        return INSTANCE;
    }

    private WebFileScanUtilBrowserUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        final SwingActionUtil swingUtil = (SwingActionUtil) SwingActionUtil.newInstance(this);
        try {
            BorderLayout thisLayout = new BorderLayout();
            getContentPane().setLayout(thisLayout);
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.setTitle("browser source code");
            {
                informationMenu = new JPopupMenu();
                setComponentPopupMenu(this, informationMenu);
            }
            {
                ListModel openFileListModel = new DefaultListModel();
                openFileList = new JList();
                openFileList.setModel(openFileListModel);
                getContentPane().add(openFileList, BorderLayout.NORTH);
                openFileList.setPreferredSize(new java.awt.Dimension(663, 281));
                openFileList.addListSelectionListener(new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent evt) {
                        swingUtil.invokeAction("openFileList.valueChanged", evt);
                    }
                });
                openFileList.addMouseMotionListener(new MouseMotionAdapter() {
                    public void mouseMoved(MouseEvent evt) {
                        swingUtil.invokeAction("openFileList.mouseMoved", evt);
                    }
                });
                openFileList.addKeyListener(new KeyAdapter() {
                    public void keyPressed(KeyEvent evt) {
                        swingUtil.invokeAction("openFileList.keyPressed", evt);
                    }
                });
            }
            {
                jPanel1 = new JPanel();
                GroupLayout jPanel1Layout = new GroupLayout((JComponent) jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                getContentPane().add(jPanel1, BorderLayout.SOUTH);
                jPanel1.setPreferredSize(new java.awt.Dimension(478, 35));
                {
                    openSelected = new JButton();
                    openSelected.setText("open all");
                    openSelected.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            swingUtil.invokeAction("openSelected.actionPerformed", evt);
                        }
                    });
                }
                jPanel1Layout.setHorizontalGroup(jPanel1Layout.createSequentialGroup().addContainerGap(178, 178)
                        .addComponent(openSelected, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE).addContainerGap(174, Short.MAX_VALUE));
                jPanel1Layout.setVerticalGroup(jPanel1Layout.createSequentialGroup().addGap(7).addComponent(openSelected, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 6, Short.MAX_VALUE));
            }
            {
                informationMenu = new JPopupMenu();
            }

            this.setSize(486, 350);
            this.setLocationRelativeTo(null);

            //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            // 可以快速顯示tooltip
            //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            int initialDelay = ToolTipManager.sharedInstance().getInitialDelay();
            ToolTipManager.sharedInstance().setInitialDelay(0);
            //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            // 可以快速顯示tooltip
            //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

            swingUtil.addAction("openFileList.mouseClicked", new Action() {
                public void action(EventObject evt) throws Exception {
                    DefaultListModel model = (DefaultListModel) openFileList.getModel();
                    int pos = openFileList.getLeadSelectionIndex();
                    if (pos == -1) {
                        return;
                    }
                    setOpenFileListToolTip();
                    MouseEvent eeev = (MouseEvent) evt;
                    if (eeev.getClickCount() != 2) {
                        return;
                    }
                    MFile file = (MFile) model.elementAt(pos);
                    openSource(file.file);
                }
            });
            final JListUtil jlistUtil = JListUtil.newInstance(openFileList);
            swingUtil.addAction("openFileList.keyPressed", new Action() {
                public void action(EventObject evt) throws Exception {
                    jlistUtil.defaultJListKeyPressed((KeyEvent) evt);
                    setOpenFileListToolTip();
                }
            });
            swingUtil.addAction("openSelected.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    DefaultListModel model = (DefaultListModel) openFileList.getModel();
                    for (Enumeration<?> enu = model.elements(); enu.hasMoreElements();) {
                        MFile file = (MFile) enu.nextElement();
                        openSource(file.file);
                    }
                }
            });
            swingUtil.addAction("openFileList.mouseMoved", new Action() {
                public void action(EventObject evt) throws Exception {
                    setOpenFileListToolTip();
                }
            });
            swingUtil.addAction("openFileList.valueChanged", new Action() {
                public void action(EventObject evt) throws Exception {
                    System.out.println(evt);
                    setOpenFileListToolTip();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setOpenFileListToolTip() {
        int pos = openFileList.getLeadSelectionIndex();
        if (pos == -1) {
            return;
        }
        DefaultListModel model = (DefaultListModel) openFileList.getModel();
        MFile file = (MFile) model.getElementAt(pos);
        openFileList.setToolTipText(file.file.getAbsolutePath());
    }

    public WebFileScanUtilBrowserUI sourceFileList(List<PageInfo> list) {
        if (list == null || list.isEmpty()) {
            return this;
        }
        List<MFile> lll = new ArrayList<MFile>();
        for (PageInfo pageinfo : list) {
            if (pageinfo.getOrignWebFile() != null) {
                lll.add(new MFile(pageinfo.getOrignWebFile(), "WEB"));
            }
            if (pageinfo.getSourceFile() != null) {
                lll.add(new MFile(pageinfo.getSourceFile(), "SRC"));
            }
        }
        DefaultListModel model = new DefaultListModel();
        for (MFile f : lll) {
            model.addElement(f);
        }
        openFileList.setModel(model);
        return this;
    }

    void openSource(File file) {
        String fileAbsPath = file.getAbsolutePath();
        String fileName = file.getName();
        if (fileName.endsWith(".java") || fileName.endsWith(".xhtml") || fileName.endsWith(".jsp")) {
            try {
                String command = "cmd /c call \"" + getEclipseExe() + "\" \"" + fileAbsPath + "\"";
                log.trace("exec ==> " + command);
                Runtime.getRuntime().exec(command);
            } catch (Exception e) {
                log.error("!!open source error!!", e);
            }
        } else if (fileName.endsWith(".class")) {
            try {
                String command = "cmd /c call \"" + JD_EXE + "\" \"" + fileAbsPath + "\"";
                log.trace("exec ==> " + command);
                Runtime.getRuntime().exec(command);
            } catch (Exception e) {
                log.error("!!open source error!!", e);
            }
        } else {
            try {
                String command = "cmd /c call \"" + NOTE_PAD_EXE + "\" \"" + fileAbsPath + "\"";
                log.trace("exec ==> " + command);
                Runtime.getRuntime().exec(command);
            } catch (Exception e) {
                log.error("!!open source error!!", e);
            }
        }
    }

    private static final String ECLIPSE_HOME = Config.ECLIPSE_HOME;
    private static final String ECLIPSE_COMPANY = Config.ECLIPSE_COMPANY;

    private static final String JD_EXE = Config.JD_EXE;
    private static final String NOTE_PAD_EXE = Config.NOTE_PAD;

    private static String eclipseExe;
    private JButton openSelected;
    private JPanel jPanel1;
    private JList openFileList;
    private JPopupMenu informationMenu;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    static class MFile {
        File file;
        String fileName;
        String description;

        MFile(File file, String description) {
            this.file = file;
            fileName = file.getName();
            this.description = description;
        }

        @Override
        public String toString() {
            return fileName + "  (" + description + ")";
        }
    }

    private String getEclipseExe() {
        if (StringUtils.isNotEmpty(eclipseExe)) {
            return eclipseExe;
        }
        try {
            if (InetAddress.getByName("192.168.2.14").isReachable(3000)) {
                eclipseExe = ECLIPSE_COMPANY;
            } else {
                eclipseExe = ECLIPSE_HOME;
            }
            log.trace("init eclipse ==> " + eclipseExe);
        } catch (Exception e) {
            log.error("getEclipseExe", e);
        }
        if (StringUtils.isEmpty(eclipseExe)) {
            return ECLIPSE_HOME;
        } else {
            return eclipseExe;
        }
    }

    /**
     * Auto-generated method for setting the popup menu for a component
     */
    private void setComponentPopupMenu(final java.awt.Component parent, final javax.swing.JPopupMenu menu) {
        parent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (e.isPopupTrigger())
                    menu.show(parent, e.getX(), e.getY());
            }

            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (e.isPopupTrigger())
                    menu.show(parent, e.getX(), e.getY());
            }
        });
    }

    private static final WebFileScanUtilBrowserUI INSTANCE = new WebFileScanUtilBrowserUI();
}
