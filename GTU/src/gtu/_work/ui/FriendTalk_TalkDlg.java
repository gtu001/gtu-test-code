package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.apache.commons.lang.StringUtils;

import gtu._work.ui.FriendTalk_EditFriendDlg.MyFriendGtu001;
import gtu._work.ui.FriendTalk_EditFriendDlg.MyFriendTalkGtu001;
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

            panel.add(JCommonUtil.createScrollComponent(talkArea), BorderLayout.CENTER);
            panel.add(sendBtn, BorderLayout.EAST);

            sendBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (StringUtils.isBlank(talkArea.getText())) {
                        return;
                    }

                    final String talkMsg = talkArea.getText();

                    if (mMyFriendGtu001 != null) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Socket s = new Socket(mMyFriendGtu001.getIp(), 6666);
                                    in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                                    out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), FriendTalkUI.ENCODING));

                                    if (s.isConnected()) {
                                        out.write(mMyFriendGtu001.getPrefix() + talkMsg);
                                        out.flush();
                                        talkArea.setText("");

                                        mMyFriendGtu001.getMessageLst().add(MyFriendTalkGtu001.ofMyself(talkMsg));

                                        updateMessageDlg();

                                        // 設定已讀
                                        setIsAlreadyReading();
                                    }
                                    s.close();
                                } catch (IOException e) {
                                    JTextPaneUtil.newInstance(talkPane).append("Exception in line 52 : " + e);
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
            });
        }
        {
            talkPane = new JTextPane();
            talkPane.setEditable(false);
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

    private void setIsAlreadyReading() {
        mMyFriendGtu001.readMessageCount = mMyFriendGtu001.getMessageLst().size();
    }

    BufferedReader in;
    BufferedWriter out;

    public void updateMessageDlg() {
        if (this.mMyFriendGtu001 != null) {
            talkPane.setText("");
            for (MyFriendTalkGtu001 msg : this.mMyFriendGtu001.getMessageLst()) {
                if (!msg.isMe) {
                    SimpleAttributeSet attributes = new SimpleAttributeSet();
                    StyleConstants.setAlignment(attributes, StyleConstants.ALIGN_LEFT);
                    JTextPaneUtil.newInstance(talkPane).append(msg.getFixMessage(), attributes);
                } else {
                    SimpleAttributeSet attributes = new SimpleAttributeSet();
                    StyleConstants.setAlignment(attributes, StyleConstants.ALIGN_RIGHT);
                    JTextPaneUtil.newInstance(talkPane).append(msg.getFixMessage(), attributes);
                }
            }
        }
    }
}
