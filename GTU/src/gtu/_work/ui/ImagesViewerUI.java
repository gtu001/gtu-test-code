package gtu._work.ui;

import java.awt.BorderLayout;
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

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.mouse.NativeMouseEvent;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.constant.PatternCollection;
import gtu.file.FileUtil;
import gtu.image.ImageUtil;
import gtu.keyboard_mouse.JnativehookKeyboardMouseHelper;
import gtu.keyboard_mouse.JnativehookKeyboardMouseHelper.MyNativeKeyAdapter;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JMouseEventUtil;
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
    private ShowImageHandler mShowImageHandler;

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
        panel.add(ImageViewLbl, BorderLayout.CENTER);

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
                    mShowImageHandler.showImage();
                }
            });

            JCommonUtil.applyDropFiles(this, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    List<File> files = (List<File>) e.getSource();
                    mShowImageHandler.apply(files.get(0));
                    mShowImageHandler.showImage();
                }
            });

            // 掛載鍵盤事件
            initKeyBoard();

            // 掛載所有event
            applyAllEvents();

            JCommonUtil.setJFrameCenter(this);
            JCommonUtil.setJFrameIcon(this, "resource/images/ico/tk_aiengine.ico");
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
            for (String str : args) {
                File file = new File(str);
                if (file.exists()) {
                    mShowImageHandler.apply(file);
                    break;
                }
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

    private class ShowImageHandler {
        private File file;
        private BufferedImage image;
        private File currentDir;
        private List<File> currentFilesLst;

        ShowImageHandler() {
        }

        public void apply(File file) {
            if (file == null || !file.exists()) {
                ImageViewLbl.setIcon(null);
                JCommonUtil._jOptionPane_showMessageDialog_error("檔案不存在:" + file.getName());
                return;
            }
            this.file = file;
            this.image = ImageUtil.getInstance().getBufferedImage(file);
            this.currentDir = this.file.getParentFile();
            ImagesViewerUI.this.setTitle(this.file.getName());
        }

        private void beforeCheck() {
            if (currentFilesLst == null || currentFilesLst.isEmpty()) {
                if (this.currentDir != null) {
                    currentFilesLst = new ArrayList<File>();
                    for (File f : currentDir.listFiles()) {
                        if (f.getName().matches(".*\\." + PatternCollection.PICTURE_PATTERN)) {
                            currentFilesLst.add(f);
                        }
                    }
                    Collections.sort(currentFilesLst, new Comparator<File>() {
                        @Override
                        public int compare(File o1, File o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                }
            }
        }

        private void next() {
            beforeCheck();
            if (currentFilesLst != null && !currentFilesLst.isEmpty()) {
                File findFile = null;
                for (int ii = 0; ii < currentFilesLst.size(); ii++) {
                    if (file.equals(currentFilesLst.get(ii))) {
                        if (ii + 1 <= currentFilesLst.size() - 1) {
                            findFile = currentFilesLst.get(ii + 1);
                            break;
                        }
                    }
                }
                if (findFile == null) {
                    findFile = currentFilesLst.get(0);
                }
                apply(findFile);
                showImage();
            }
        }

        private void previous() {
            beforeCheck();
            if (currentFilesLst != null && !currentFilesLst.isEmpty()) {
                File findFile = null;
                for (int ii = 0; ii < currentFilesLst.size(); ii++) {
                    if (file.equals(currentFilesLst.get(ii))) {
                        if (ii - 1 >= 0) {
                            findFile = currentFilesLst.get(ii - 1);
                            break;
                        }
                    }
                }
                if (findFile == null) {
                    findFile = currentFilesLst.get(currentFilesLst.size() - 1);
                }
                apply(findFile);
                showImage();
            }
        }

        private void showImage() {
            if (image == null) {
                return;
            }
            try {
                Image image2 = ImageUtil.getInstance().resizeImage(image, ImageViewLbl.getWidth(), ImageViewLbl.getHeight());
                Icon image3 = ImageUtil.getInstance().imageToIcon(image2);
                ImageViewLbl.setIcon(image3);
            } catch (Exception e) {
                JCommonUtil.handleException(e);
            }
        }

        private void deleteImage() {
            if (this.file != null && this.file.exists()) {
                boolean confirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("是否刪除圖片" + file.getName(), "刪除圖片");
                if (confirm) {
                    FileUtil.deleteFileToRecycleBin(file);
                    this.next();
                }
            }
        }
    }

    private void initKeyBoard() {
        JnativehookKeyboardMouseHelper.getInstance().startNativeKeyboardAndMouse(new MyNativeKeyAdapter() {
            @Override
            public void nativeMouseReleased(NativeMouseEvent e) {
            }

            @Override
            public void nativeKeyReleased(NativeKeyEvent e) {
                if (NativeKeyEvent.VC_LEFT == e.getKeyCode()) {
                    mShowImageHandler.previous();
                } else if (NativeKeyEvent.VC_RIGHT == e.getKeyCode()) {
                    mShowImageHandler.next();
                } else if (NativeKeyEvent.VC_DELETE == e.getKeyCode()) {
                    mShowImageHandler.deleteImage();
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
                .addMenuItem("item2", (ActionListener) ActionAdapter.ActionListener.create(ActionDefine.TEST_DEFAULT_EVENT.name(), getSwingUtil()))//
                .addChildrenMenu(menu1)//
                .getMenu();
        JMenuBarUtil.newInstance().addMenu(mainMenu).apply(this);
    }

    public SwingActionUtil getSwingUtil() {
        return swingUtil;
    }
}
