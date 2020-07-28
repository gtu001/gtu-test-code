package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

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
    public FriendTalk_TalkDlg(MyFriendGtu001 mMyFriendGtu001) {
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

                    String talkMsg = talkArea.getText();

                    if (mMyFriendGtu001 != null) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Socket s = new Socket(mMyFriendGtu001.getIp(), 6666);
                                    in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                                    out = new PrintWriter(s.getOutputStream(), true);

                                    if (s.isConnected()) {
                                        out.write(mMyFriendGtu001.getPrefix() + talkMsg);
                                        out.flush();
                                        talkArea.setText("");

                                        mMyFriendGtu001.getMessageLst().add(MyFriendTalkGtu001.ofMyself(talkMsg));

                                        updateMessageDlg();
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

                messageSize = this.mMyFriendGtu001.getMessageLst().size();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                if (mMyFriendGtu001.getMessageLst().size() != messageSize) {
                                    messageSize = mMyFriendGtu001.getMessageLst().size();
                                    updateMessageDlg();
                                }
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
            JCommonUtil.setJFrameCenter(this);
        }
    }

    int messageSize = -1;
    BufferedReader in;
    PrintWriter out;

    private void updateMessageDlg() {
        if (this.mMyFriendGtu001 != null) {
            talkPane.setText("");
            for (MyFriendTalkGtu001 msg : this.mMyFriendGtu001.getMessageLst()) {
                if (!msg.isMe) {
                    JTextPaneUtil.newInstance(talkPane).append(msg.getFixMessage());
                } else {
                    SimpleAttributeSet attributes = new SimpleAttributeSet();
                    StyleConstants.setAlignment(attributes, StyleConstants.ALIGN_RIGHT);
                    attributes.addAttribute(StyleConstants.CharacterConstants.Italic, Boolean.TRUE);
                    JTextPaneUtil.newInstance(talkPane).append(msg.getFixMessage(), attributes);
                }
            }
        }
    }
}