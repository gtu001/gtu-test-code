package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.EventObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang3.StringUtils;

import gtu._work.ui.FriendTalk_EditFriendDlg.MyFriendGtu001;
import gtu._work.ui.FriendTalk_EditFriendDlg.MyFriendTalkGtu001;
import gtu._work.ui.JMenuBarUtil.JMenuAppender;
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
    private FriendTalk_TalkDlg mFriendTalk_TalkDlg;
    private FriendTalk_EditFriendDlg mFriendTalk_EditFriendDlg;

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
        tabbedPane.addTab("New tab", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        panel_3 = new JPanel();
        panel.add(panel_3, BorderLayout.NORTH);

        addFriendBtn = new JButton("加入");
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

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_1, null);

        panel_2 = new JPanel();
        tabbedPane.addTab("其他設定", null, panel_2, null);
        panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        {
            initFriendsLst();

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
            this.setTitle("You Set My World On Fire");
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
                    }
                });
            }
        });
        swingUtil.addActionHex("friendsList.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                MouseEvent e = (MouseEvent) evt;
                if (JMouseEventUtil.buttonLeftClick(2, evt)) {
                    MyFriendGtu001 friend = (MyFriendGtu001) JListUtil.getLeadSelectionObject(friendsList);
                    if (mFriendTalk_TalkDlg != null) {
                        mFriendTalk_TalkDlg.dispose();
                    }
                    mFriendTalk_TalkDlg = FriendTalk_TalkDlg.newInstance(friend);
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

    public void execute(int port, PrintStream serverLogOut) {
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

                        Thread.sleep(500);
                    }
                } catch (Exception e) {
                    serverLogOut.println("Exception in line 19 : " + e);
                }
            }
        }).start();
    }

    private class SocketServer_Thread extends Thread {
        private Socket socket;

        public SocketServer_Thread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(),
                        true /* autoFlush */);
                boolean done = false;
                while (!done) {
                    String strVal = in.readLine();
                    if (strVal == null) {
                        done = true;
                    } else {
                        System.out.println(">>>>" + strVal);
                        processFriendMessage(strVal);
                        out.println(strVal);
                    }
                }
            } catch (Exception e) {
                JCommonUtil.handleException(e);
            }
        }
    }

    private void processFriendMessage(String strVal) {
        Pattern ptn = Pattern.compile("\\#\\[(.*)\\]\\#");
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
                if (StringUtils.equals(fn.name, name) && StringUtils.equals(fn.ip, ip)) {
                    talkFn = fn;
                    break;
                }
            }

            if (talkFn != null) {
                talkFn.getMessageLst().add(talk);
            }
        }
    }
}
