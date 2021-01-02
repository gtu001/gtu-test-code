package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventObject;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.mouse.NativeMouseEvent;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.constant.PatternCollection;
import gtu.file.FileUtil;
import gtu.image.ImageUtil;
import gtu.keyboard_mouse.JnativehookKeyboardMouseHelper;
import gtu.keyboard_mouse.JnativehookKeyboardMouseHelper.MyNativeKeyAdapter;
import gtu.properties.PropertiesUtilBean;
import gtu.runtime.DesktopUtil;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JLabelUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JScrollPaneUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;

public class ImagesViewerUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private HideInSystemTrayHelper hideInSystemTrayHelper;
    private JFrameRGBColorPanel jFrameRGBColorPanel;
    private SwingActionUtil swingUtil;
    private JPanel panel_3;
    private JPanel panel_4;
    private JPanel panel_5;
    private JPanel panel_6;
    private JLabel ImageViewLbl;
    private JScrollPane mImageViewLblJScrollPane;
    private ShowImageHandler mShowImageHandler;
    private PropertiesUtilBean config = new PropertiesUtilBean(ImagesViewerUI.class);

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance_delable(ImagesViewerUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ImagesViewerUI frame = new ImagesViewerUI();
                    frame.applyArgs(args);
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
    public ImagesViewerUI() {
        swingUtil = SwingActionUtil.newInstance(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0));

        contentPane.add(panel, BorderLayout.CENTER);

        panel_3 = new JPanel();
        panel.add(panel_3, BorderLayout.NORTH);

        panel_4 = new JPanel();
        panel.add(panel_4, BorderLayout.WEST);

        panel_5 = new JPanel();
        panel.add(panel_5, BorderLayout.EAST);

        panel_6 = new JPanel();
        panel.add(panel_6, BorderLayout.SOUTH);

        ImageViewLbl = new JLabel("");
        JLabelUtil.alignCenter(ImageViewLbl);
        mImageViewLblJScrollPane = JCommonUtil.createScrollComponent(ImageViewLbl);
        panel.add(mImageViewLblJScrollPane, BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();

        ImageViewLbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                swingUtil.invokeAction("ImageViewLbl.click", e);
            }
        });

        {
            mShowImageHandler = new ShowImageHandler();

            // jframe resize
            this.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent evt) {
                    ImageViewLbl.setPreferredSize(JScrollPaneUtil.getVisibleSize(mImageViewLblJScrollPane));
                    mShowImageHandler.showImage();
                }
            });

            JCommonUtil.applyDropFiles(this, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    List<File> files = (List<File>) e.getSource();
                    if (files.size() == 1) {
                        mShowImageHandler.apply(files.get(0), null);
                    } else {
                        mShowImageHandler.apply(null, files);
                    }
                    mShowImageHandler.showImage();
                }
            });

            // 掛載鍵盤事件
            initKeyBoard();

            // 掛載所有event
            applyAllEvents();

            JCommonUtil.setJFrameCenter(this);
            JCommonUtil.setJFrameIcon(this, "resource/images/ico/image_viewer.ico");
            hideInSystemTrayHelper = HideInSystemTrayHelper.newInstance();
            hideInSystemTrayHelper.apply(this);
            jFrameRGBColorPanel = new JFrameRGBColorPanel(this);
            this.applyAppMenu();
            JCommonUtil.defaultToolTipDelay();
            this.setTitle("You Set My World On Fire");
        }
    }

    public void applyArgs(String[] args) {
        if (args != null && args.length != 0) {
            List<File> lst = new ArrayList<File>();
            for (String str : args) {
                File file = new File(str);
                lst.add(file);
            }
            if (lst.size() == 1) {
                File file = lst.get(0);
                if (file.exists()) {
                    mShowImageHandler.apply(file, null);
                }
            } else {
                mShowImageHandler.apply(null, lst);
            }
        }
    }

    private enum ActionDefine {
        TEST_DEFAULT_EVENT, //
        JTabbedPane_ChangeIndex, //
        ;
    }

    private void applyAllEvents() {
        swingUtil.addActionHex("ImageViewLbl.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                if (JMouseEventUtil.buttonLeftClick(2, evt)) {
                    applyFullScreen(null);
                }
                if (JMouseEventUtil.buttonRightClick(1, evt)) {
                    JPopupMenuUtil.newInstance(ImageViewLbl).applyEvent(evt)//
                            .addJMenuItem("開啟檔案位置", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    mShowImageHandler.showFile();
                                }
                            })//
                            .addJMenuItem("符合視窗高度", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    mShowImageHandler.setMatchHeight(true);
                                    mShowImageHandler.showImage();
                                }
                            })//
                            .addJMenuItem("符合視窗寬度", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    mShowImageHandler.setMatchHeight(false);
                                    mShowImageHandler.showImage();
                                }
                            })//
                            .addJMenuItem("符合視窗寬度", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    mShowImageHandler.setMatchHeight(false);
                                    mShowImageHandler.showImage();
                                }
                            })//
                            .addJMenuItem("符合視窗填滿", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    mShowImageHandler.setMatchHeight(null);
                                    mShowImageHandler.showImage();
                                }
                            })//
                            .addJMenuItem("依照檔名排序[重新整理]", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    mShowImageHandler.currentFilesLst = null;
                                    mShowImageHandler.setCurrentComparator(COMPARE_BY_NAME);
                                    mShowImageHandler.apply(null, null);
                                    mShowImageHandler.showImage();
                                }
                            })//
                            .addJMenuItem("依照修改時間排序[重新整理]", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    mShowImageHandler.currentFilesLst = null;
                                    mShowImageHandler.setCurrentComparator(COMPARE_BY_CREATETIME);
                                    mShowImageHandler.apply(null, null);
                                    mShowImageHandler.showImage();
                                }
                            })//
                            .addJMenuItem("開啟目錄", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    mShowImageHandler.showDirectory();
                                }
                            })//
                            .show();
                }
            }
        });

    }

    public void applyFullScreen(Boolean toFullScreen) {
        JFrame mJFrame = (JFrame) ImagesViewerUI.this;
        if (mJFrame.getExtendedState() != JFrame.MAXIMIZED_BOTH || (toFullScreen != null && true == toFullScreen)) {
            // mJFrame.setUndecorated(true);
            mJFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else if (mJFrame.getExtendedState() == JFrame.MAXIMIZED_BOTH || (toFullScreen != null && false == toFullScreen)) {
            // mJFrame.setUndecorated(false);
            mJFrame.setExtendedState(JFrame.NORMAL);
        }
    }

    private static final Comparator<File> COMPARE_BY_NAME = new Comparator<File>() {
        @Override
        public int compare(File o1, File o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    private static final Comparator<File> COMPARE_BY_CREATETIME = new Comparator<File>() {
        @Override
        public int compare(File o1, File o2) {
            return Long.valueOf(o1.lastModified()).compareTo(o2.lastModified());
        }
    };

    private class ShowImageHandler {
        private File file;
        private BufferedImage image;
        private File currentDir;
        private List<File> currentFilesLst;
        int currentIndex = 0;
        Boolean isMatchHeight = true;
        Comparator<File> currentComparator;

        ShowImageHandler() {
        }

        public void setMatchHeight(Boolean isMatchHeight) {
            this.isMatchHeight = isMatchHeight;
        }

        public void setCurrentComparator(Comparator<File> currentComparator) {
            this.currentComparator = currentComparator;
        }

        private List<File> resetCurrentFileLst(List<File> currentFileLst1) {
            List<File> currentFileLst2 = new ArrayList<File>();
            for (File f : currentFileLst1) {
                if (f != null && f.exists()) {
                    currentFileLst2.add(f);
                }
            }
            return currentFileLst2;
        }

        public void apply(File file, List<File> currentFileLst1) {
            try {
                if (currentComparator == null) {
                    currentComparator = COMPARE_BY_NAME;
                }
                if (file == null && (currentFileLst1 == null || currentFileLst1.isEmpty())) {
                    boolean findOk = false;
                    if ((this.currentFilesLst != null && !this.currentFilesLst.isEmpty())) {
                        List<File> lst = resetCurrentFileLst(this.currentFilesLst);
                        if (!lst.isEmpty()) {
                            currentFileLst1 = lst;
                            findOk = true;
                        }
                    }
                    if (!findOk && this.currentDir != null && this.currentDir.exists()) {
                        file = this.currentDir;
                        findOk = true;
                    }
                    if (!findOk) {
                        JCommonUtil._jOptionPane_showMessageDialog_error("檔案不存在[789]");
                        return;
                    }
                }
                if (file == null && currentFileLst1 != null && !currentFileLst1.isEmpty()) {
                    for (File tmpFile : currentFileLst1) {
                        if (tmpFile.isFile()) {
                            file = tmpFile.getParentFile();
                            break;
                        }
                    }
                    if (file == null && currentFileLst1 != null && !currentFileLst1.isEmpty()) {
                        file = currentFileLst1.get(0);
                    }

                }
                if (!file.exists()) {
                    file = file.getParentFile();
                }
                if (file.isFile()) {
                    this.file = file;
                    this.currentDir = this.file.getParentFile();
                    this.currentFilesLst = null;
                    beforeCheck(currentFileLst1);
                } else {
                    this.currentDir = file;
                    this.currentFilesLst = null;
                    beforeCheck(currentFileLst1);
                    if (currentFilesLst != null && !currentFilesLst.isEmpty()) {
                        this.file = currentFilesLst.get(0);
                        this.currentIndex = 0;
                    }
                }
                if (this.file == null) {
                    JCommonUtil._jOptionPane_showMessageDialog_error("目前無任何圖片[123]");
                    return;
                }
                if (currentFilesLst == null || currentFilesLst.isEmpty()) {
                    JCommonUtil._jOptionPane_showMessageDialog_error("目前無任何圖片[456]");
                    return;
                }
                this.image = ImageUtil.getInstance().getBufferedImage(this.file);
                String message = "[" + (currentIndex + 1) + "/" + currentFilesLst.size() + "]";
                ImagesViewerUI.this.setTitle(message + " " + this.file.getName());

                saveConfig();
            } catch (Exception ex) {
                ex.printStackTrace();
                if (ex.getMessage() != null && ex.getMessage().contains("系統找不到指定的檔案")) {
                    if (currentFilesLst != null && !currentFilesLst.isEmpty()) {
                        currentFilesLst.remove(this.file);
                        this.apply(null, currentFilesLst);
                    }
                } else {
                    this.apply(null, null);
                }
            }
        }

        private static final String COURRENT_DIR_CONFIG = "currentDir";

        private void saveConfig() {
            if (this.currentDir != null && this.currentDir.exists() && this.currentDir.isDirectory()) {
                config.getConfigProp().setProperty(COURRENT_DIR_CONFIG, this.currentDir.getAbsolutePath());
                config.store();
            }
        }

        private void loadLatestConfig() {
            config.reload();
            String path = config.getConfigProp().getProperty(COURRENT_DIR_CONFIG);
            File currentDir = new File(path);
            if (currentDir != null && currentDir.exists() && currentDir.isDirectory()) {
                this.apply(currentDir, null);
                this.showImage();
            }
        }

        private void beforeCheck(List<File> currentFileLst1) {
            if (currentFileLst1 != null && !currentFileLst1.isEmpty()) {
                this.currentFilesLst = currentFileLst1;
            }
            if (currentFilesLst == null || currentFilesLst.isEmpty()) {
                if (this.currentDir != null) {
                    currentFilesLst = new ArrayList<File>();
                    for (File f : currentDir.listFiles()) {
                        if (f.getName().matches(".*\\." + PatternCollection.PICTURE_PATTERN)) {
                            currentFilesLst.add(f);
                        }
                    }
                }
            }
            if (currentFilesLst != null && !currentFilesLst.isEmpty()) {
                Collections.sort(currentFilesLst, currentComparator);
            }
            if (this.file != null && this.file.exists()) {
                for (int ii = 0; ii < currentFilesLst.size(); ii++) {
                    if (this.file.equals(currentFilesLst.get(ii))) {
                        currentIndex = ii;
                        break;
                    }
                }
            }
        }

        private void next() {
            beforeCheck(null);
            if (currentFilesLst != null && !currentFilesLst.isEmpty()) {
                File findFile = null;
                for (int ii = 0; ii < currentFilesLst.size(); ii++) {
                    if (file.equals(currentFilesLst.get(ii))) {
                        if (ii + 1 <= currentFilesLst.size() - 1) {
                            findFile = currentFilesLst.get(ii + 1);
                            currentIndex = ii + 1;
                            break;
                        }
                    }
                }
                if (findFile == null) {
                    findFile = currentFilesLst.get(0);
                }
                apply(findFile, currentFilesLst);
                showImage();
            }
        }

        private void previous() {
            beforeCheck(null);
            if (currentFilesLst != null && !currentFilesLst.isEmpty()) {
                File findFile = null;
                for (int ii = 0; ii < currentFilesLst.size(); ii++) {
                    if (file.equals(currentFilesLst.get(ii))) {
                        if (ii - 1 >= 0) {
                            findFile = currentFilesLst.get(ii - 1);
                            currentIndex = ii - 1;
                            break;
                        }
                    }
                }
                if (findFile == null) {
                    findFile = currentFilesLst.get(currentFilesLst.size() - 1);
                }
                apply(findFile, currentFilesLst);
                showImage();
            }
        }

        private void showImage() {
            if (image == null) {
                return;
            }
            try {
                int matchLength = ImageViewLbl.getHeight();
                if (isMatchHeight != null && !isMatchHeight) {
                    matchLength = ImageViewLbl.getWidth();
                }
                Image image2 = null;
                if (isMatchHeight != null) {
                    image2 = ImageUtil.getInstance().resizeImageByIndicateWH(image, isMatchHeight, matchLength);
                } else {
                    image2 = ImageUtil.getInstance().resizeImage(image, ImageViewLbl.getWidth(), ImageViewLbl.getHeight());
                }
                Icon image3 = ImageUtil.getInstance().imageToIcon(image2);
                ImageViewLbl.setIcon(image3);
            } catch (Exception e) {
                JCommonUtil.handleException(e);
            }
        }

        private void deleteImage() {
            if (this.file == null) {
                System.out.println("刪除檔案不存在 : " + this.file);
            } else if (this.file != null && !this.file.exists()) {
                System.out.println("刪除檔案不存在 : " + this.file);
                currentFilesLst.remove(this.file);
                this.apply(null, currentFilesLst);
                this.showImage();
            } else if (this.file != null) {
                boolean confirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("是否刪除圖片" + file.getName(), "刪除圖片");
                if (confirm) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                FileUtil.deleteFileToRecycleBin(file, null);
                            } catch (Exception ex) {
                                JCommonUtil.handleException(ex);
                            }
                        }
                    }).start();
                    if (this.file != null) {
                        currentFilesLst.remove(this.file);
                    }
                    System.out.println("[deleteImage] currentFilesLst = " + currentFilesLst);
                    this.apply(null, currentFilesLst);
                    this.showImage();
                }
            }
        }

        private void showFile() {
            DesktopUtil.browseFileDirectory(file);
        }

        private void showDirectory() {
            if (this.currentDir != null && this.currentDir.exists()) {
                DesktopUtil.openDir(this.currentDir);
            }
        }
    }

    private void initKeyBoard() {
        JnativehookKeyboardMouseHelper.startNativeKeyboardAndMouse(new MyNativeKeyAdapter() {
            @Override
            public void nativeMouseReleased(NativeMouseEvent e) {
            }

            @Override
            public void nativeKeyReleased(NativeKeyEvent e) {
                System.out.println("isOnTop = " + JCommonUtil.isOnTop(ImagesViewerUI.this));
                if (JCommonUtil.isOnTop(ImagesViewerUI.this)) {
                    if (NativeKeyEvent.VC_LEFT == e.getKeyCode()) {
                        mShowImageHandler.previous();
                    } else if (NativeKeyEvent.VC_RIGHT == e.getKeyCode()) {
                        mShowImageHandler.next();
                    } else if (NativeKeyEvent.VC_DELETE == e.getKeyCode()) {
                        mShowImageHandler.deleteImage();
                    }
                }
            }
        });
    }

    private void applyAppMenu() {
        JMenu menu1 = JMenuAppender.newInstance("child_item")//
                .addMenuItem("detail1", (ActionListener) ActionAdapter.ActionListener.create(ActionDefine.TEST_DEFAULT_EVENT.name(), getSwingUtil()))//
                .getMenu();
        JMenu mainMenu = JMenuAppender.newInstance("file")//
                .addMenuItem("item1", null)//
                .addMenuItem("讀取最後目錄", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mShowImageHandler.loadLatestConfig();
                    }
                })//
                .addChildrenMenu(menu1)//
                .getMenu();
        JMenuBarUtil.newInstance().addMenu(mainMenu).apply(this);
    }

    public SwingActionUtil getSwingUtil() {
        return swingUtil;
    }
}
