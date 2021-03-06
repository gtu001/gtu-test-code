package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;

import org.rr.mobi4java.MobiDocument;
import org.rr.mobi4java.MobiReader;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.epub.viewer.DesktopUtil;
import gtu.mobi.MobiReaderTest001.MobiBookHandler;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;

public class MobiReaderUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private HideInSystemTrayHelper hideInSystemTrayHelper;
    private JFrameRGBColorPanel jFrameRGBColorPanel;
    private SwingActionUtil swingUtil;
    private JTabbedPane tabbedPane;
    private JPanel panel_2;
    private JPanel panel_3;
    private JPanel panel_4;
    private JPanel panel_5;
    private JPanel panel_6;
    private JEditorPane htmlEditPane;
    private JButton previousBtn;
    private JButton nextBtn;
    private JButton openFileBtn;
    private MobiBookHandler mMobiBookHandler;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance_delable(MobiReaderUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MobiReaderUI frame = new MobiReaderUI();
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
    public MobiReaderUI() {
        swingUtil = SwingActionUtil.newInstance(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 592, 547);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener((ChangeListener) ActionAdapter.ChangeListener.create(ActionDefine.JTabbedPane_ChangeIndex.name(), swingUtil));
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("New tab", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        panel_3 = new JPanel();
        panel.add(panel_3, BorderLayout.NORTH);

        panel_4 = new JPanel();
        panel.add(panel_4, BorderLayout.WEST);

        panel_5 = new JPanel();
        panel.add(panel_5, BorderLayout.EAST);

        panel_6 = new JPanel();
        panel.add(panel_6, BorderLayout.SOUTH);

        previousBtn = new JButton("<");
        previousBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("previousBtn.click", e);
            }
        });

        openFileBtn = new JButton("開檔");
        openFileBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("openFileBtn.click", e);
            }
        });
        panel_6.add(openFileBtn);
        panel_6.add(previousBtn);

        nextBtn = new JButton(">");
        nextBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("nextBtn.click", e);
            }
        });
        panel_6.add(nextBtn);

        htmlEditPane = new JEditorPane();
        {
            htmlEditPane.setBackground(Color.white);
            htmlEditPane.setEditable(false);
            HTMLEditorKit htmlKit = new HTMLEditorKit();
            htmlEditPane.setEditorKit(htmlKit);
            htmlEditPane.addHyperlinkListener(new HyperlinkListener() {

                @Override
                public void hyperlinkUpdate(HyperlinkEvent event) {
                    if (event.getEventType() != HyperlinkEvent.EventType.ACTIVATED) {
                        return;
                    }
                    final URL url = event.getURL();
                    if (url.getProtocol().toLowerCase().startsWith("http") && !"".equals(url.getHost())) {
                        try {
                            DesktopUtil.launchBrowser(event.getURL());
                            return;
                        } catch (DesktopUtil.BrowserLaunchException ex) {
                            System.out.println("hyperlinkUpdate ERR : " + ex.getMessage());
                        }
                    }
                }
            });

            htmlEditPane.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    swingUtil.invokeAction("htmlEditPane.keyPress", keyEvent);
                }
            });
        }

        panel.add(JCommonUtil.createScrollComponent(htmlEditPane), BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_1, null);

        panel_2 = new JPanel();
        tabbedPane.addTab("其他設定", null, panel_2, null);
        panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        {
            // 掛載所有event
            applyAllEvents();

            JCommonUtil.setJFrameCenter(this);
            JCommonUtil.setJFrameIcon(this, "resource/images/ico/tk_aiengine.ico");
            hideInSystemTrayHelper = HideInSystemTrayHelper.newInstance();
            hideInSystemTrayHelper.apply(this);
            jFrameRGBColorPanel = new JFrameRGBColorPanel(this);
            panel_2.add(jFrameRGBColorPanel.getToggleButton(false));
            panel_2.add(hideInSystemTrayHelper.getToggleButton(false));
            this.applyAppMenu();
            JCommonUtil.defaultToolTipDelay();
        }
    }

    private enum ActionDefine {
        TEST_DEFAULT_EVENT, //
        JTabbedPane_ChangeIndex, //
        ;
    }

    private void applyAllEvents() {
        swingUtil.addActionHex(ActionDefine.TEST_DEFAULT_EVENT.name(), new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                System.out.println("====Test Default Event!!====");
            }
        });
        swingUtil.addActionHex(ActionDefine.JTabbedPane_ChangeIndex.name(), new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                System.out.println("tabbedPane : " + tabbedPane.getSelectedIndex());
            }
        });
        swingUtil.addActionHex("openFileBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                File bookFile = JCommonUtil._jFileChooser_selectFileOnly();
                if (bookFile == null) {
                    return;
                }
                MobiDocument doc = new MobiReader().read(bookFile);
                mMobiBookHandler = new MobiBookHandler(doc);
            }
        });
        swingUtil.addActionHex("previousBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                mMobiBookHandler.previous();
                String content = mMobiBookHandler.getPage();
                htmlEditPane.setText(content);
            }
        });
        swingUtil.addActionHex("nextBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                mMobiBookHandler.next();
                String content = mMobiBookHandler.getPage();
                htmlEditPane.setText(content);
            }
        });

        swingUtil.addActionHex("htmlEditPane.keyPress", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                KeyEvent keyEvent = (KeyEvent) evt;
                if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                    JCommonUtil.triggerButtonActionPerformed(nextBtn);
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                    JCommonUtil.triggerButtonActionPerformed(previousBtn);
                } else if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
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
