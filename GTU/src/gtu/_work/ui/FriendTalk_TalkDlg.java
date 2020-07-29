package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.apache.commons.lang.StringUtils;

import gtu._work.ui.FriendTalk_EditFriendDlg.MyFileAcceptGtu001;
import gtu._work.ui.FriendTalk_EditFriendDlg.MyFriendGtu001;
import gtu._work.ui.FriendTalk_EditFriendDlg.MyFriendTalkGtu001;
import gtu.log.Logger2File;
import gtu.swing.util.JColorUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JTextPaneUtil;

public class FriendTalk_TalkDlg extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextPane talkPane;
    private JLabel friendNameLbl;
    private JLabel friendIpLbl;
    private JTextArea talkArea;
    private MyFriendGtu001 mMyFriendGtu001;
    private JButton sendBtn;
    private Logger2File logger = FriendTalkUI.logger;

    /**
     * Launch the application.
     */
    public static FriendTalk_TalkDlg newInstance(MyFriendGtu001 mMyFriendGtu001) {
        try {
            FriendTalk_TalkDlg dialog = new FriendTalk_TalkDlg(mMyFriendGtu001);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
            return dialog;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Create the dialog.
     */
    public FriendTalk_TalkDlg(final MyFriendGtu001 mMyFriendGtu001) {
        {
            this.mMyFriendGtu001 = mMyFriendGtu001;
        }
        setBounds(100, 100, 545, 425);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.NORTH);
            {
                friendNameLbl = new JLabel("                   ");
                panel.add(friendNameLbl);
            }
            {
                friendIpLbl = new JLabel("                   ");
                panel.add(friendIpLbl);
            }
        }
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.WEST);
        }
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.EAST);
        }
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.SOUTH);
            panel.setLayout(new BorderLayout(0, 0));

            talkArea = new JTextArea();
            sendBtn = new JButton("送出");

            sendBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    sendMessage();
                }
            });

            panel.add(JCommonUtil.createScrollComponent(talkArea), BorderLayout.CENTER);
            panel.add(sendBtn, BorderLayout.EAST);

            talkArea.addKeyListener(new KeyAdapter() {

                long latestClickEnter = -1;

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER && (System.currentTimeMillis() - latestClickEnter) < 800) {
                        sendMessage();
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        latestClickEnter = System.currentTimeMillis();
                    }
                }
            });
        }
        {
            talkPane = new JTextPane();
            talkPane.setEditable(false);
            JCommonUtil.applyDropFiles(talkPane, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    List<File> fileLst = (List<File>) e.getSource();
                    if (!fileLst.isEmpty()) {
                        sendFile(fileLst.get(0));
                    }
                }
            });
            contentPanel.add(JCommonUtil.createScrollComponent(talkPane), BorderLayout.CENTER);
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                // buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                // buttonPane.add(cancelButton);
            }
        }

        {
            if (this.mMyFriendGtu001 != null) {
                friendNameLbl.setText(this.mMyFriendGtu001.getName());
                friendIpLbl.setText(this.mMyFriendGtu001.getIp());

                // 初次update
                updateMessageDlg();

                // 設定已讀
                setIsAlreadyReading();
            }
            JCommonUtil.setJFrameCenter(this);
        }
    }

    private void sendFile(final File file) {
        if (file == null || !file.exists()) {
            JCommonUtil._jOptionPane_showMessageDialog_error("檔案有問題!");
            return;
        }

        if ((file.length() / (1024 * 1024)) > 20) {
            JCommonUtil._jOptionPane_showMessageDialog_error("檔案必須小於20mb");
            return;
        }

        if (mMyFriendGtu001 != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BufferedReader in = null;
                    BufferedWriter out = null;
                    try {
                        Socket s1 = new Socket(mMyFriendGtu001.getIp(), 6666);
                        in = new BufferedReader(new InputStreamReader(s1.getInputStream()));
                        out = new BufferedWriter(new OutputStreamWriter(s1.getOutputStream(), FriendTalkUI.ENCODING));

                        if (s1.isConnected()) {
                            MyFileAcceptGtu001 sendFile = new MyFileAcceptGtu001();
                            sendFile.setFileName(file.getName());
                            sendFile.setFileLength(file.length());
                            sendFile.setFile(file);
                            sendFile.setSendIp(FriendTalkUI.MY_IP);
                            sendFile.setAcceptIp(mMyFriendGtu001.getIp());
                            FriendTalkUI.sendFile.set(sendFile);

                            String fileMsg = sendFile.getSendMessage();
                            out.write(mMyFriendGtu001.getPrefix() + fileMsg);
                            out.flush();
                            talkArea.setText("");

                            mMyFriendGtu001.getMessageLst().add(MyFriendTalkGtu001.ofMyself(fileMsg));

                            logger.debug("我:" + fileMsg);

                            updateMessageDlg();

                            // 設定已讀
                            setIsAlreadyReading();
                        }
                        s1.close();
                    } catch (IOException e) {
                        JTextPaneUtil.newInstance(talkPane).append("ERROR : " + e);
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
    }

    private void sendMessage() {
        if (StringUtils.isBlank(talkArea.getText())) {
            return;
        }
        final String talkMsg = talkArea.getText();
        if (mMyFriendGtu001 != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BufferedReader in = null;
                    BufferedWriter out = null;
                    try {
                        Socket s = new Socket(mMyFriendGtu001.getIp(), 6666);
                        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), FriendTalkUI.ENCODING));

                        if (s.isConnected()) {
                            out.write(mMyFriendGtu001.getPrefix() + talkMsg);
                            out.flush();
                            talkArea.setText("");

                            mMyFriendGtu001.getMessageLst().add(MyFriendTalkGtu001.ofMyself(talkMsg));

                            logger.debug("我:" + talkMsg);

                            updateMessageDlg();

                            // 設定已讀
                            setIsAlreadyReading();
                        }
                        s.close();
                    } catch (IOException e) {
                        JTextPaneUtil.newInstance(talkPane).append("連線失敗 : " + e.getMessage());
                        e.printStackTrace();
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
            }).start();
        }
    }

    public void setIsAlreadyReading() {
        mMyFriendGtu001.readMessageCount = mMyFriendGtu001.getMessageLst().size();
    }

    public void updateMessageDlg() {
        if (this.mMyFriendGtu001 != null) {
            talkPane.setText("");
            for (MyFriendTalkGtu001 msg : this.mMyFriendGtu001.getMessageLst()) {
                String[] message2 = msg.getFixMessage();
                if (!msg.isMe) {
                    SimpleAttributeSet attributes = new SimpleAttributeSet();
                    StyleConstants.setAlignment(attributes, StyleConstants.ALIGN_LEFT);

                    StyleContext sc = StyleContext.getDefaultStyleContext();
                    AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Background, JColorUtil.rgb("EEEEEE"));
                    aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
                    aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

                    JTextPaneUtil.newInstance(talkPane).append(message2[0], attributes, aset);
                    JTextPaneUtil.newInstance(talkPane).append(message2[1], attributes);
                } else {
                    SimpleAttributeSet attributes = new SimpleAttributeSet();
                    StyleConstants.setAlignment(attributes, StyleConstants.ALIGN_RIGHT);

                    StyleContext sc = StyleContext.getDefaultStyleContext();
                    AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Background, JColorUtil.rgb("DDDDDD"));
                    aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
                    aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

                    JTextPaneUtil.newInstance(talkPane).append(message2[0], attributes, aset);
                    JTextPaneUtil.newInstance(talkPane).append(message2[1], attributes);
                }
            }
        }
    }

    public boolean isFocus() {
        if (this.isVisible() && //
                (talkPane.isFocusOwner() || //
                        talkArea.isFocusOwner())) {
            return true;
        }
        return false;
    }
}
