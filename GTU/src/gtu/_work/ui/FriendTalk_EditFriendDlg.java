package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import gtu.swing.util.JCommonUtil;

public class FriendTalk_EditFriendDlg extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextField friendNameText;
    private JTextField friendIPText;
    private MyFriendGtu001 mMyFriendGtu001;
    private ActionListener okActionListener;
    private ActionListener deleteActionListener;

    /**
     * Launch the application.
     */
    public static FriendTalk_EditFriendDlg newInstance(MyFriendGtu001 mMyFriendGtu001, ActionListener okActionListener, final ActionListener deleteActionListener) {
        try {
            FriendTalk_EditFriendDlg dialog = new FriendTalk_EditFriendDlg(mMyFriendGtu001, okActionListener, deleteActionListener);
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
    public FriendTalk_EditFriendDlg(MyFriendGtu001 mMyFriendGtu001, final ActionListener okActionListener, final ActionListener deleteActionListener) {
        {
            this.mMyFriendGtu001 = mMyFriendGtu001;
            this.okActionListener = okActionListener;
            this.deleteActionListener = deleteActionListener;
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
                JButton deleteBtn = new JButton("刪除聯絡人");
                deleteBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (deleteActionListener != null) {
                            deleteActionListener.actionPerformed(e);
                        }
                    }
                });
                deleteBtn.setActionCommand("OK");
                buttonPane.add(deleteBtn);
            }
            {
                JButton okButton = new JButton("確定");
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
                JButton cancelButton = new JButton("取消");
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

        public String[] getFixMessage() {
            String prefix = name + "(" + ip + ")[" + time + "] \n";
            String message2 = message + "\n\n";
            return new String[] { prefix, message2 };
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

    /**
     * @author wistronits
     *
     */
    public static class MyFileAcceptGtu001 {
        String fileName;
        Boolean isAccept;
        long approveTime;
        long fileLength;
        File file;
        String acceptIp;
        String sendIp;

        public String getSendMessage() {
            String acceptStr = "NA";
            if (isAccept != null) {
                acceptStr = isAccept ? "Y" : "N";
            }
            return "#[file:" + fileLength + "|" + sendIp + "|" + acceptStr + "|" + fileName + "]#";
        }

        public static MyFileAcceptGtu001 ofSendMessage(String sendMessage) {
            MyFileAcceptGtu001 f = new MyFileAcceptGtu001();
            Pattern ptn = Pattern.compile("\\#\\[file\\:(\\d+)\\|([\\d\\.]+)\\|(Y|N|NA)\\|(.*?)\\]\\#");
            Matcher mth = ptn.matcher(sendMessage);
            if (mth.find()) {
                f.fileLength = Long.parseLong(mth.group(1));
                f.sendIp = mth.group(2);
                f.fileName = mth.group(4);
                String acceptStr = mth.group(3);
                if (StringUtils.equals(acceptStr, "NA")) {
                    f.isAccept = null;
                } else if (StringUtils.equals(acceptStr, "Y")) {
                    f.isAccept = true;
                } else if (StringUtils.equals(acceptStr, "N")) {
                    f.isAccept = false;
                }
                return f;
            }
            return null;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public Boolean getIsAccept() {
            return isAccept;
        }

        public void setIsAccept(Boolean isAccept) {
            this.isAccept = isAccept;
        }

        public long getApproveTime() {
            return approveTime;
        }

        public void setApproveTime(long approveTime) {
            this.approveTime = approveTime;
        }

        public long getFileLength() {
            return fileLength;
        }

        public void setFileLength(long fileLength) {
            this.fileLength = fileLength;
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public String getAcceptIp() {
            return acceptIp;
        }

        public void setAcceptIp(String acceptIp) {
            this.acceptIp = acceptIp;
        }

        public String getSendIp() {
            return sendIp;
        }

        public void setSendIp(String sendIp) {
            this.sendIp = sendIp;
        }
    }
}
