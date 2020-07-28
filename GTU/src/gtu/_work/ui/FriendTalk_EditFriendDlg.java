package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.net.NetTool;
import gtu.swing.util.JCommonUtil;

public class FriendTalk_EditFriendDlg extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextField friendNameText;
    private JTextField friendIPText;
    private MyFriendGtu001 mMyFriendGtu001;
    private ActionListener okActionListener;

    /**
     * Launch the application.
     */
    public static FriendTalk_EditFriendDlg newInstance(MyFriendGtu001 mMyFriendGtu001, ActionListener okActionListener) {
        try {
            FriendTalk_EditFriendDlg dialog = new FriendTalk_EditFriendDlg(mMyFriendGtu001, okActionListener);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
            return dialog;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public MyFriendGtu001 getFriend() {
        MyFriendGtu001 fnd = new MyFriendGtu001();
        if (mMyFriendGtu001 != null) {
            fnd = this.mMyFriendGtu001;
        }
        fnd.ip = StringUtils.trimToEmpty(friendIPText.getText());
        fnd.name = StringUtils.trimToEmpty(friendNameText.getText());
        return fnd;
    }

    /**
     * Create the dialog.
     */
    public FriendTalk_EditFriendDlg(MyFriendGtu001 mMyFriendGtu001, final ActionListener okActionListener) {
        {
            this.mMyFriendGtu001 = mMyFriendGtu001;
            this.okActionListener = okActionListener;
        }
        setBounds(100, 100, 361, 217);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));
        {
            JLabel lblNewLabel = new JLabel("別名");
            contentPanel.add(lblNewLabel, "2, 2, right, default");
        }
        {
            friendNameText = new JTextField();
            contentPanel.add(friendNameText, "4, 2, fill, default");
            friendNameText.setColumns(10);
        }
        {
            JLabel lblNewLabel_1 = new JLabel("IP");
            contentPanel.add(lblNewLabel_1, "2, 4, right, default");
        }
        {
            friendIPText = new JTextField();
            friendIPText.setColumns(10);
            contentPanel.add(friendIPText, "4, 4, fill, default");
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            JCommonUtil.isBlankErrorMsg(friendIPText.getText(), "IP未填");
                            if (okActionListener != null) {
                                okActionListener.actionPerformed(e);
                            }
                            dispose();
                        } catch (Exception ex) {
                            JCommonUtil.handleException(ex);
                        }
                    }
                });
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
            }
        }
        {
            if (this.mMyFriendGtu001 != null) {
                friendIPText.setText(StringUtils.trimToEmpty(this.mMyFriendGtu001.ip));
                friendNameText.setText(StringUtils.trimToEmpty(this.mMyFriendGtu001.name));
            }
            JCommonUtil.setJFrameCenter(this);
        }
    }

    public static class MyFriendGtu001 {
        String ip;
        String name;

        int readMessageCount;
        AtomicReference<List<MyFriendTalkGtu001>> messageLst = new AtomicReference();

        public MyFriendGtu001() {
            messageLst.set(new ArrayList<MyFriendTalkGtu001>());
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String toString() {
            String unreadMsg = "";
            if (messageLst.get().size() > readMessageCount) {
                unreadMsg = "　<font color=red>" + String.valueOf(messageLst.get().size() - readMessageCount) + "</font>";
            }
            return "<html>" + name + "　 " + ip + unreadMsg + "</html>";
        }

        public String getPrefix() {
            return "#[" + FriendTalkUI.MY_NAME + "|" + FriendTalkUI.MY_IP + "|" + DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSSSS") + "]#";
        }

        public List<MyFriendTalkGtu001> getMessageLst() {
            return messageLst.get();
        }

        public void setMessageLst(List<MyFriendTalkGtu001> messageLst) {
            this.messageLst.set(messageLst);
        }

        public int getReadMessageCount() {
            return readMessageCount;
        }

        public void setReadMessageCount(int readMessageCount) {
            this.readMessageCount = readMessageCount;
        }

        public void setMessageLst(AtomicReference<List<MyFriendTalkGtu001>> messageLst) {
            this.messageLst = messageLst;
        }
    }

    public static class MyFriendTalkGtu001 {
        String ip;
        String name;
        String message;
        String time;
        boolean isMe = false;

        public static MyFriendTalkGtu001 ofMyself(String talkMsg) {
            MyFriendTalkGtu001 talk = new MyFriendTalkGtu001();
            talk.setIp("127.0.0.1");
            talk.setMessage(talkMsg);
            talk.setName("我");
            talk.setTime(DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSSSS"));
            talk.setMe(true);
            return talk;
        }

        public String getFixMessage() {
            return name + "(" + ip + ")[" + time + "] \n" + message + "\n";
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public boolean isMe() {
            return isMe;
        }

        public void setMe(boolean isMe) {
            this.isMe = isMe;
        }
    }
}
