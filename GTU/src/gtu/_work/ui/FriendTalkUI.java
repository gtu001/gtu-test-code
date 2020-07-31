package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.StringUtils;

import gtu._work.ui.FriendTalk_EditFriendDlg.MyFileAcceptGtu001;
import gtu._work.ui.FriendTalk_EditFriendDlg.MyFriendGtu001;
import gtu._work.ui.FriendTalk_EditFriendDlg.MyFriendTalkGtu001;
import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.file.FileUtil;
import gtu.log.Logger2File;
import gtu.music.SoundUtils;
import gtu.net.NetTool;
import gtu.properties.PropertiesUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;

public class FriendTalkUI extends JFrame {

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
    private JList friendsList;
    private JButton addFriendBtn;
    private JButton fixIPBtn;
    private JButton encidingBtn;
    private JButton fixMyNameBtn;
    private FriendTalk_TalkDlg mFriendTalk_TalkDlg;
    private FriendTalk_EditFriendDlg mFriendTalk_EditFriendDlg;
    public static String MY_IP;
    public static String ENCODING = "BIG5";
    public static String MY_NAME = "未命名";
    private PropertiesUtilBean config = new PropertiesUtilBean(FriendTalkUI.class);
    public static Logger2File logger = new Logger2File(PropertiesUtil.getJarCurrentPath(FriendTalkUI.class).getAbsolutePath(), FriendTalkUI.class.getSimpleName());
    public static AtomicReference<MyFileAcceptGtu001> sendFile = new AtomicReference<MyFileAcceptGtu001>();
    public static AtomicReference<MyFileAcceptGtu001> acceptFile = new AtomicReference<MyFileAcceptGtu001>();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance_delable(FriendTalkUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FriendTalkUI frame = new FriendTalkUI();
                    gtu.swing.util.JFrameUtil.setVisible(true, frame);
                    frame.execute(6666, System.out);
                    frame.execute_File(6667, System.out);
                    frame.updateFriendList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public FriendTalkUI() {
        swingUtil = SwingActionUtil.newInstance(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 408, 569);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener((ChangeListener) ActionAdapter.ChangeListener.create(ActionDefine.JTabbedPane_ChangeIndex.name(), swingUtil));
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("聯絡人", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        panel_3 = new JPanel();
        panel.add(panel_3, BorderLayout.NORTH);

        fixMyNameBtn = new JButton("MyName");
        fixMyNameBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("fixMyNameBtn.click", e);
            }
        });
        panel_3.add(fixMyNameBtn);

        fixIPBtn = new JButton("MyIP");
        fixIPBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("fixIPBtn.click", e);
            }
        });
        panel_3.add(fixIPBtn);

        encidingBtn = new JButton("編碼");
        encidingBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("encidingBtn.click", e);
            }
        });
        panel_3.add(encidingBtn);

        addFriendBtn = new JButton("加入聯絡人");
        addFriendBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("addFriendBtn.click", e);
            }
        });
        panel_3.add(addFriendBtn);

        panel_4 = new JPanel();
        panel.add(panel_4, BorderLayout.WEST);

        panel_5 = new JPanel();
        panel.add(panel_5, BorderLayout.EAST);

        panel_6 = new JPanel();
        panel.add(panel_6, BorderLayout.SOUTH);

        friendsList = new JList();
        panel.add(friendsList, BorderLayout.CENTER);
        friendsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                swingUtil.invokeAction("friendsList.click", e);
            }
        });

        // JPanel panel_1 = new JPanel();
        // tabbedPane.addTab("New tab", null, panel_1, null);

        panel_2 = new JPanel();
        tabbedPane.addTab("其他設定", null, panel_2, null);
        panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        {
            initFriendsLst();

            // 掛載所有event
            applyAllEvents();

            JCommonUtil.setJFrameCenter(this);
            hideInSystemTrayHelper = HideInSystemTrayHelper.newInstance();
            hideInSystemTrayHelper.apply(this);
            jFrameRGBColorPanel = new JFrameRGBColorPanel(this);
            panel_2.add(jFrameRGBColorPanel.getToggleButton(false));
            panel_2.add(hideInSystemTrayHelper.getToggleButton(false));
            this.applyAppMenu();
            JCommonUtil.defaultToolTipDelay();
            // this.setTitle("You Set My World On Fire");

            try {
                MY_IP = NetTool.getLocalHostLANAddress().getHostAddress();
                setTitle("我的IP:" + MY_IP);
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            }

            updateICON();
            reflectInfo();
        }
    }

    private void updateICON() {
        int unreadCount = 0;
        if (friendsList != null) {
            DefaultListModel model = (DefaultListModel) friendsList.getModel();
            for (int ii = 0; ii < model.getSize(); ii++) {
                MyFriendGtu001 fnd = (MyFriendGtu001) model.getElementAt(ii);
                if (fnd.getMessageLst().size() != fnd.getReadMessageCount()) {
                    unreadCount += Math.abs(fnd.getMessageLst().size() - fnd.getReadMessageCount());
                }
            }
        }
        if (unreadCount > 0) {
            JCommonUtil.setJFrameIcon(this, "resource/images/ico/line_warning.ico");
            setTitle(unreadCount + "封未讀訊息!");
            try {
                SoundUtils.tone(5000, 200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JCommonUtil.setJFrameIcon(this, "resource/images/ico/line.ico");
            setInfo();
        }
    }

    private enum ActionDefine {
        TEST_DEFAULT_EVENT, //
        JTabbedPane_ChangeIndex, //
        ;
    }

    private void initFriendsLst() {
        DefaultListModel model = JListUtil.createModel();
        friendsList.setModel(model);
    }

    private void storeInfo() {
        Properties prop = config.getConfigProp();
        prop.setProperty("IP", MY_IP);
        prop.setProperty("NAME", MY_NAME);
        prop.setProperty("ENCODING", ENCODING);
        DefaultListModel model = (DefaultListModel) friendsList.getModel();
        for (int ii = 0; ii < model.getSize(); ii++) {
            MyFriendGtu001 fnd = (MyFriendGtu001) model.getElementAt(ii);
            prop.setProperty("friend" + ii, fnd.getName() + "|" + fnd.getIp());
        }
        config.store();
    }

    private void reflectInfo() {
        Properties prop = config.getConfigProp();
        if (prop.containsKey("IP")) {
            MY_IP = prop.getProperty("IP");
        }
        if (prop.containsKey("NAME")) {
            MY_NAME = prop.getProperty("NAME");
        }
        if (prop.containsKey("ENCODING")) {
            ENCODING = prop.getProperty("ENCODING");
        }
        DefaultListModel model = JListUtil.createModel();
        friendsList.setModel(model);
        for (Enumeration enu = prop.keys(); enu.hasMoreElements();) {
            String key = (String) enu.nextElement();
            if (key.startsWith("friend")) {
                String strVal = prop.getProperty(key);
                String[] arry = strVal.split("\\|", -1);
                MyFriendGtu001 fnd = new MyFriendGtu001();
                fnd.setIp(arry[1]);
                fnd.setName(arry[0]);
                model.addElement(fnd);
            }
        }
    }

    private ActionListener delUserActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            MyFriendGtu001 fnd = mFriendTalk_EditFriendDlg.getFriend();
            boolean delConfirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("是否要刪除:" + fnd.getName() + " " + fnd.getIp(), "刪除USER");
            if (!delConfirm) {
                return;
            }
            DefaultListModel model = (DefaultListModel) friendsList.getModel();
            for (int ii = 0; ii < model.getSize(); ii++) {
                MyFriendGtu001 fn1 = (MyFriendGtu001) model.getElementAt(ii);
                if (StringUtils.equals(fn1.getIp(), fnd.getIp()) && StringUtils.equals(fn1.getName(), fnd.getName())) {
                    model.removeElementAt(ii);
                    ii--;
                }
            }
            Properties prop = config.getConfigProp();
            for (Enumeration enu = prop.keys(); enu.hasMoreElements();) {
                String key = (String) enu.nextElement();
                if (key.startsWith("friend")) {
                    String strVal = prop.getProperty(key);
                    String[] arry = strVal.split("\\|", -1);
                    if (StringUtils.equals(arry[0], fnd.getName()) && StringUtils.equals(arry[1], fnd.getIp())) {
                        prop.remove(key);
                    }
                }
            }
            config.store();
        }
    };

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
        swingUtil.addActionHex("fixIPBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                String ip = JCommonUtil._jOptionPane_showInputDialog("請輸入你的IP", MY_IP);
                if (StringUtils.isNotBlank(ip) && ip.matches("[0-9\\.]+")) {
                    MY_IP = ip;
                    updateICON();
                    storeInfo();
                }
            }
        });
        swingUtil.addActionHex("encidingBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                String encoding = JCommonUtil._jOptionPane_showInputDialog("請輸入編碼", ENCODING);
                if (StringUtils.isNotBlank(encoding)) {
                    ENCODING = encoding;
                    updateICON();
                    storeInfo();
                }
            }
        });
        swingUtil.addActionHex("fixMyNameBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                String myName = JCommonUtil._jOptionPane_showInputDialog("請輸入姓名", MY_NAME);
                if (StringUtils.isNotBlank(myName)) {
                    MY_NAME = myName;
                    updateICON();
                    storeInfo();
                }
            }
        });

        swingUtil.addActionHex("addFriendBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                if (mFriendTalk_EditFriendDlg != null) {
                    mFriendTalk_EditFriendDlg.dispose();
                }
                mFriendTalk_EditFriendDlg = FriendTalk_EditFriendDlg.newInstance(new MyFriendGtu001(), new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        MyFriendGtu001 fnd = mFriendTalk_EditFriendDlg.getFriend();
                        DefaultListModel model = (DefaultListModel) friendsList.getModel();
                        model.addElement(fnd);
                        storeInfo();
                    }
                }, delUserActionListener);
            }
        });
        swingUtil.addActionHex("friendsList.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                MouseEvent e = (MouseEvent) evt;

                if (JMouseEventUtil.buttonRightClick(1, evt)) {
                    if (mFriendTalk_EditFriendDlg != null) {
                        mFriendTalk_EditFriendDlg.dispose();
                    }
                    final MyFriendGtu001 friend = (MyFriendGtu001) JListUtil.getLeadSelectionObject(friendsList);
                    mFriendTalk_EditFriendDlg = FriendTalk_EditFriendDlg.newInstance(friend, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            MyFriendGtu001 fnd = mFriendTalk_EditFriendDlg.getFriend();
                            friend.setIp(fnd.getIp());
                            friend.setName(fnd.getName());
                            storeInfo();
                        }
                    }, delUserActionListener);
                }

                if (JMouseEventUtil.buttonLeftClick(2, evt)) {
                    MyFriendGtu001 friend = (MyFriendGtu001) JListUtil.getLeadSelectionObject(friendsList);
                    if (mFriendTalk_TalkDlg != null) {
                        mFriendTalk_TalkDlg.dispose();
                    }
                    mFriendTalk_TalkDlg = FriendTalk_TalkDlg.newInstance(friend);
                    // 更新icon
                    updateICON();
                }
            }
        });
    }

    private void setInfo() {
        setTitle("你的IP:" + MY_IP + "(" + MY_NAME + ")" + ",編碼:" + ENCODING);
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

    public void updateFriendList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        friendsList.repaint();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();
    }

    public void execute(final int port, final PrintStream serverLogOut) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 1;
                try {
                    ServerSocket s = new ServerSocket(port);
                    for (;;) {
                        Socket incoming = s.accept();
                        System.out.println("<------------------ accept");
                        new SocketServer_Thread(incoming).start();
                        i++;

                        // Thread.sleep(500);
                    }
                } catch (Exception e) {
                    serverLogOut.println("Exception in line 19 : " + e);
                }
            }
        }).start();
    }

    public void execute_File(final int port, final PrintStream serverLogOut) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 1;
                try {
                    ServerSocket s = new ServerSocket(port);
                    for (;;) {
                        Socket incoming = s.accept();
                        System.out.println("<------------------ accept file");
                        new SocketServer_File_Thread(incoming).start();
                        i++;

                        // Thread.sleep(500);
                    }
                } catch (Exception e) {
                    serverLogOut.println("Exception in line 19 : " + e);
                }
            }
        }).start();
    }

    private class SocketServer_File_Thread extends Thread {
        private Socket socket;

        public SocketServer_File_Thread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                boolean startReceiveFile = false;
                if (acceptFile.get() != null) {
                    // 二十分鐘內接受
                    if ((System.currentTimeMillis() - acceptFile.get().getApproveTime()) < 20 * 60 * 1000) {
                        startReceiveFile = true;
                    }
                }

                if (startReceiveFile) {
                    BufferedInputStream buffIn = null;
                    BufferedOutputStream buffOut = null;
                    try {
                        long fileLength = acceptFile.get().getFileLength();
                        long transferSize = 0;

                        buffIn = new BufferedInputStream(socket.getInputStream());
                        buffOut = new BufferedOutputStream(new FileOutputStream(new File(FileUtil.DESKTOP_PATH, acceptFile.get().getFileName())));
                        byte[] arr = new byte[1024 * 1024];
                        int available = -1;
                        while ((available = buffIn.read(arr)) > 0) {
                            buffOut.write(arr, 0, available);

                            transferSize += available;
                            setTransferUpdateTitle(transferSize, fileLength);
                        }
                        buffOut.flush();
                        buffOut.close();
                    } catch (Exception ex) {
                        JCommonUtil.handleException(ex);
                    } finally {
                        buffIn.close();
                        buffOut.close();
                        setTitle("下載完成!");
                        JCommonUtil._jOptionPane_showMessageDialog_info("檔案下載完成:" + acceptFile.get().getFileName());
                    }
                }
            } catch (Exception e) {
                JCommonUtil.handleException(e);
            }
        }
    }

    long updateTitleTime = 0;

    private void setTransferUpdateTitle(long transferSize, long fileLength) {
        BigDecimal t1 = new BigDecimal(transferSize);
        BigDecimal f1 = new BigDecimal(fileLength);
        BigDecimal val = t1.divide(f1, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
        String updateTitleMessage = "傳檔進度 : " + val + "%";
        System.out.println(updateTitleMessage);
        if ((System.currentTimeMillis() - updateTitleTime) > 1000) {
            setTitle(updateTitleMessage);
            updateTitleTime = System.currentTimeMillis();
        }
    }

    private void sendRealFile() {
        if (FriendTalkUI.sendFile.get() == null) {
            System.out.println("發送檔案紀錄為空!");
            return;
        } else {
            System.out.println("發送檔案紀錄 : " + ReflectionToStringBuilder.toString(FriendTalkUI.sendFile.get()));
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MyFileAcceptGtu001 sendFile = FriendTalkUI.sendFile.get();
                    Socket s = new Socket(sendFile.getAcceptIp(), 6667);
                    if (s.isConnected()) {
                        BufferedInputStream buffIn = null;
                        BufferedOutputStream buffOut = null;
                        try {
                            buffIn = new BufferedInputStream(new FileInputStream(sendFile.getFile()));
                            buffOut = new BufferedOutputStream(s.getOutputStream());
                            byte[] arr = new byte[1024 * 1024];
                            int available = -1;
                            while ((available = buffIn.read(arr)) > 0) {
                                buffOut.write(arr, 0, available);
                            }
                            buffOut.flush();
                        } catch (Exception ex) {
                            JCommonUtil.handleException(ex);
                        } finally {
                            buffOut.close();
                            JCommonUtil._jOptionPane_showMessageDialog_info("檔案已傳送:" + sendFile.getFileName());
                        }
                    }
                    s.close();
                } catch (Exception ex) {
                    // JTextPaneUtil.newInstance(talkPane).append("ERROR : " +
                    // ex);
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public void sendFileReceiverSendBack(final boolean isAccept, final MyFileAcceptGtu001 sendMsg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader in = null;
                BufferedWriter out = null;
                try {
                    Socket s1 = new Socket(sendMsg.getSendIp(), 6666);
                    in = new BufferedReader(new InputStreamReader(s1.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(s1.getOutputStream(), FriendTalkUI.ENCODING));

                    if (s1.isConnected()) {
                        MyFriendGtu001 sender = new MyFriendGtu001();
                        sender.setIp(sendMsg.getSendIp());
                        sender.setName("SENDER");

                        sendMsg.setIsAccept(isAccept);
                        sendMsg.setApproveTime(System.currentTimeMillis());
                        String fileMsg = sendMsg.getSendMessage();
                        out.write(sender.getPrefix() + fileMsg);
                        out.flush();

                        logger.debug("我:" + fileMsg);
                    }
                    s1.close();
                } catch (IOException e) {
                    // JTextPaneUtil.newInstance(talkPane).append("ERROR : "
                    // + e);
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                    try {
                        out.close();
                    } catch (IOException e) {
                    }
                }
            }
        }).start();
    }

    private class SocketServer_Thread extends Thread {
        private Socket socket;

        public SocketServer_Thread(final Socket socket) {
            this.socket = socket;
        }

        private boolean isProcessFile(final String strVal) {
            MyFileAcceptGtu001 sendMsg = MyFileAcceptGtu001.ofSendMessage(strVal);
            if (sendMsg != null) {
                if (sendMsg.getIsAccept() == null) {
                    sendMsg.setApproveTime(System.currentTimeMillis());
                    boolean isAcceptFile = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption(//
                            "發送端 :" + sendMsg.getSendIp() + //
                                    ", 傳送檔案 : " + sendMsg.getFileName() + //
                                    ", 大小 : " + FileUtil.getSizeDescription(sendMsg.getFileLength()) + //
                                    " 是否接受?",
                            "傳送檔案");
                    acceptFile.set(sendMsg);
                    sendFileReceiverSendBack(isAcceptFile, acceptFile.get());
                    return true;
                } else if (sendMsg.getIsAccept() == true) {
                    if (StringUtils.equals(sendMsg.getFileName(), FriendTalkUI.sendFile.get().getFileName())) {
                        System.out.println("SEND-RECEIVE 檔名相同 : " + sendMsg.getFileName());
                        sendRealFile();
                    } else {
                        System.out.println("SEND-RECEIVE 檔名不相同 : " + sendMsg.getFileName() + " / " + FriendTalkUI.sendFile.get().getFileName());
                    }
                    return true;
                } else if (sendMsg.getIsAccept() == false) {
                    System.out.println("SEND-RECEIVE 檔案傳送駁回");
                    return true;
                }
            }
            return false;
        }

        private boolean isCommand(final String strVal) {
            Pattern ptn = Pattern.compile("\\#\\[command\\:(.*?)\\]\\#");
            Matcher mth = ptn.matcher(strVal);
            if (mth.find()) {
                String commandStrVal = mth.group(1);
                if ("RING".equalsIgnoreCase(commandStrVal)) {
                    commandRingProcess(strVal);
                    return true;
                }
            }
            return false;
        }

        private void commandRingProcess(final String strVal) {
            MyFriendGtu001 talkFn = getFriendByStrVal(strVal);
            if (talkFn != null) {
                if (mFriendTalk_TalkDlg != null) {
                    mFriendTalk_TalkDlg.dispose();
                }
                mFriendTalk_TalkDlg = FriendTalk_TalkDlg.newInstance(talkFn);
                JCommonUtil.setFrameAtop(mFriendTalk_TalkDlg, true);
                // 更新icon
                updateICON();
            }
        }

        public void run() {
            BufferedReader in = null;
            PrintWriter out = null;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), ENCODING));
                out = new PrintWriter(socket.getOutputStream(),
                        true /* autoFlush */);

                boolean isFirstLine = true;
                StringBuffer sb = new StringBuffer();
                for (String line = null; (line = in.readLine()) != null;) {
                    sb.append((!isFirstLine ? "\n" : "") + StringUtils.trimToEmpty(line));
                    isFirstLine = false;
                }

                String strVal = sb.toString();

                System.out.println(">>>>" + strVal);
                out.println(strVal);

                boolean isIgnoreAfter = isProcessFile(strVal);
                if (isIgnoreAfter) {
                    return;
                }

                isIgnoreAfter = isCommand(strVal);
                if (isIgnoreAfter) {
                    return;
                }

                // 處理對話訊息
                processFriendMessage(strVal);
            } catch (Exception e) {
                JCommonUtil.handleException(e);
            } finally {
                try {
                    in.close();
                } catch (Exception e) {
                }
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    private void processFriendMessage(final String strVal) {
        Pattern ptn = Pattern.compile("\\#\\[friend\\:(.*)\\]\\#");
        Matcher mth = ptn.matcher(strVal);
        if (mth.find()) {
            String groupStr = mth.group(1);
            String[] arry = groupStr.split("\\|", -1);
            String name = arry[0];
            String ip = arry[1];
            String time = arry[2];
            String message = strVal.substring(mth.end(0));

            MyFriendTalkGtu001 talk = new MyFriendTalkGtu001();
            talk.setName(name);
            talk.setIp(ip);
            talk.setTime(time);
            talk.setMessage(message);

            MyFriendGtu001 talkFn = null;
            DefaultListModel model = (DefaultListModel) friendsList.getModel();
            for (int ii = 0; ii < model.getSize(); ii++) {
                MyFriendGtu001 fn = (MyFriendGtu001) model.getElementAt(ii);
                if (StringUtils.equals(fn.ip, ip)) {
                    talkFn = fn;
                    break;
                }
            }

            String youAssignName = "";
            if (talkFn != null) {
                talkFn.getMessageLst().add(talk);
                youAssignName = talkFn.getName();
            } else {
                talkFn = new MyFriendGtu001();
                talkFn.setName("未知");
                talkFn.setIp(ip);
                talkFn.getMessageLst().add(talk);
                ((DefaultListModel) friendsList.getModel()).addElement(talkFn);
                youAssignName = "未知";
            }
            System.out.println("size----" + talkFn.getMessageLst().size() + "/" + talkFn.getReadMessageCount());

            // 送出右下角訊息
            if (mFriendTalk_TalkDlg != null && !mFriendTalk_TalkDlg.isFocus()) {
                hideInSystemTrayHelper.displayMessage(youAssignName + "傳送了訊息!", youAssignName + ":" + message, MessageType.INFO);
                // mFriendTalk_TalkDlg.setIsAlreadyReading();
            }

            // log
            logger.debug(youAssignName + ":" + message);

            // 刷新UI
            friendsList.repaint();
            if (mFriendTalk_TalkDlg != null) {
                mFriendTalk_TalkDlg.updateMessageDlg();
            }

            // 更新icon
            updateICON();
        }
    }

    private MyFriendGtu001 getFriendByStrVal(final String strVal) {
        MyFriendGtu001 talkFn = null;
        Pattern ptn = Pattern.compile("\\#\\[friend\\:(.*)\\]\\#");
        Matcher mth = ptn.matcher(strVal);
        if (mth.find()) {
            String groupStr = mth.group(1);
            String[] arry = groupStr.split("\\|", -1);
            String name = arry[0];
            String ip = arry[1];
            String time = arry[2];
            String message = strVal.substring(mth.end(0));

            DefaultListModel model = (DefaultListModel) friendsList.getModel();
            for (int ii = 0; ii < model.getSize(); ii++) {
                MyFriendGtu001 fn = (MyFriendGtu001) model.getElementAt(ii);
                if (StringUtils.equals(fn.ip, ip)) {
                    talkFn = fn;
                    break;
                }
            }
        }
        return talkFn;
    }
}
