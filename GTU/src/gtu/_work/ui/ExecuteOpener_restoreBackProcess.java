package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import gtu.file.FileUtil;
import gtu.file.OsInfoUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.runtime.DesktopUtil;
import gtu.runtime.ProcessWatcher;
import gtu.runtime.RuntimeBatPromptModeUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JPopupMenuUtil;

public class ExecuteOpener_restoreBackProcess extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JList restoreLst;
    private ModelHandler modelHandler = new ModelHandler();
    private PropertiesUtilBean config = new PropertiesUtilBean(ExecuteOpener_restoreBackProcess.class);
    private JTextField txtTortoisegitmergebasestheirss;
    private JCheckBox showFromChk;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
    }

    public static ExecuteOpener_restoreBackProcess newInstance(File logFile) {
        try {
            ExecuteOpener_restoreBackProcess dialog = new ExecuteOpener_restoreBackProcess();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);

            List<Pair<File, File>> fromToList = dialog.transferLogFileToFromToList(logFile);
            dialog.modelHandler.initListModel(fromToList, dialog.restoreLst);

            dialog.showFromChk.setSelected(true);

            return dialog;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private File getRealFile(File logFile, File orignFile, List<String> errLogLst) {
        if (orignFile.exists()) {
            return orignFile;
        } else {
            orignFile = new File(logFile.getParentFile(), orignFile.getName());
            if (orignFile.exists()) {
                return orignFile;
            }
        }
        errLogLst.add(orignFile.getAbsolutePath());
        return null;
    }

    public List<Pair<File, File>> transferLogFileToFromToList(File logFile) {
        List<String> lst = FileUtil.loadFromFile_asList(logFile, "UTF8");
        Pattern ptn = Pattern.compile("from\\:(.*?)\tto\\:(.*?)\t[\\w]");

        List<String> errLogLst = new ArrayList<String>();
        List<Pair<File, File>> fromToLst = new ArrayList<Pair<File, File>>();
        for (String line : lst) {
            Matcher mth = ptn.matcher(line);
            System.out.println("-------------------------------------------------");
            if (mth.find()) {
                File from = new File(mth.group(1));
                File to = new File(mth.group(2));

                from = getRealFile(logFile, from, errLogLst);
                to = getRealFile(logFile, to, errLogLst);

                if (from == null || to == null) {
                    continue;
                }

                System.out.println("from : " + from);
                System.out.println("to : " + to);
                fromToLst.add(Pair.of(from, to));
            }
            System.out.println("-------------------------------------------------");
        }

        if (!errLogLst.isEmpty()) {
            JCommonUtil._jOptionPane_showMessageDialog_error("找不到檔案 : \n" + StringUtils.join(errLogLst, "\n"));
        }
        return fromToLst;
    }

    private class ModelHandler {
        List<Pair<File, File>> fromToList;

        private void initListModel(List<Pair<File, File>> fromToList, JList restoreLst) {
            this.fromToList = fromToList;
            DefaultListModel model = new DefaultListModel();
            for (Pair<File, File> p : fromToList) {
                model.addElement(new RestoreInfo(p));
            }
            restoreLst.setModel(model);
        }
    }

    private static class RestoreInfo {
        private static boolean IS_FROM = true;
        File from;
        File to;

        RestoreInfo(Pair<File, File> bean) {
            from = bean.getLeft();
            to = bean.getRight();
        }

        private String getColor(int val, Triple arry) {
            String format = "<font color='%s'>%s</font>";
            if (val == 0) {
                return String.format(format, "#C7EDCC", arry.getMiddle());
            } else if (val == -1) {
                return String.format(format, "red", arry.getLeft());
            } else {
                return String.format(format, "blue", arry.getRight());
            }
        }

        public String toString() {
            String format = "<html>%s %s &nbsp;&nbsp; %s</html>";

            int sizeCompare = new Long(from.length()).compareTo(to.length());
            int modifyCompare = new Long(from.lastModified()).compareTo(to.lastModified());

            if (IS_FROM) {
                String sizeDesc = getColor(sizeCompare, Triple.of("小", "同", "大"));
                String timeDesc = getColor(modifyCompare, Triple.of("早", "同", "晚"));
                return String.format(format, sizeDesc, timeDesc, from.getAbsolutePath());
            } else {
                String sizeDesc = getColor(sizeCompare, Triple.of("大", "同", "小"));
                String timeDesc = getColor(modifyCompare, Triple.of("晚", "同", "早"));
                return String.format(format, sizeDesc, timeDesc, to.getAbsolutePath());
            }
        }
    }

    /**
     * Create the dialog.
     */
    public ExecuteOpener_restoreBackProcess() {
        setBounds(100, 100, 604, 507);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.NORTH);
            {
                JLabel lblNewLabel = new JLabel("設定比對format [1s:from, 2s:to]");
                panel.add(lblNewLabel);
            }
            {
                txtTortoisegitmergebasestheirss = new JTextField();
                txtTortoisegitmergebasestheirss.setText("TortoiseGitMerge /base:\"%s\" /theirs:\"%s\"");
                panel.add(txtTortoisegitmergebasestheirss);
                txtTortoisegitmergebasestheirss.setColumns(20);
            }
            {
                JButton saveConfigBtn = new JButton("儲存設定");
                saveConfigBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            config.reflectSetConfig(ExecuteOpener_restoreBackProcess.this);
                            config.store();
                            JCommonUtil._jOptionPane_showMessageDialog_info("儲存完成!");
                        } catch (Exception ex) {
                            JCommonUtil.handleException(ex);
                        }
                    }
                });
                panel.add(saveConfigBtn);
            }
            {
                showFromChk = new JCheckBox("顯示from");
                showFromChk.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        RestoreInfo.IS_FROM = showFromChk.isSelected();
                        restoreLst.updateUI();
                    }
                });
                panel.add(showFromChk);
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
        }
        {
            restoreLst = new JList();
            contentPanel.add(JCommonUtil.createScrollComponent(restoreLst), BorderLayout.CENTER);

            restoreLst.addMouseListener(new MouseAdapter() {
                private void compareDiff(final RestoreInfo info) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String command = "";
                                if (OsInfoUtil.isWindows()) {
                                    String tortoiseMergeFormat = "cmd /c call " + txtTortoisegitmergebasestheirss.getText();
                                    command = String.format(tortoiseMergeFormat, info.from, info.to);
                                } else {
                                    String tortoiseMergeFormat = txtTortoisegitmergebasestheirss.getText();
                                    command = String.format(tortoiseMergeFormat, info.from, info.to);
                                }

                                RuntimeBatPromptModeUtil shInst = RuntimeBatPromptModeUtil.newInstance();
                                shInst.command(command);

                                System.out.println(command);
                                ProcessWatcher watcher = ProcessWatcher.newInstance(shInst.apply());
                                watcher.getStreamSync();
                                String errMsg = watcher.getErrorStreamToString();
                                if (StringUtils.isNotBlank(errMsg)) {
                                    Validate.isTrue(false, errMsg);
                                }
                            } catch (Exception e1) {
                                JCommonUtil.handleException(e1);
                            }
                        }
                    }).start();
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        RestoreInfo current = JListUtil.getLeadSelectionObject(restoreLst);
                        if (current != null) {
                            System.out.println(ReflectionToStringBuilder.toString(current, ToStringStyle.MULTI_LINE_STYLE));
                        }

                        if (JMouseEventUtil.buttonLeftClick(2, e)) {
                            final List<RestoreInfo> lst = JListUtil.getLeadSelectionArry(restoreLst);
                            final RestoreInfo info = lst.get(0);
                            compareDiff(info);
                        }

                        if (JMouseEventUtil.buttonRightClick(1, e)) {

                            JPopupMenuUtil popUtl = JPopupMenuUtil.newInstance(restoreLst);
                            final List<RestoreInfo> lst = JListUtil.getLeadSelectionArry(restoreLst);

                            if (lst.size() == 1) {
                                final RestoreInfo info = lst.get(0);
                                popUtl.addJMenuItem("compare git", new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        compareDiff(info);
                                    }
                                });

                                popUtl.addJMenuItem("--------------------------------------------");
                                popUtl.addJMenuItem("from : " + info.from, new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            DesktopUtil.browse(info.from.toURL().toString());
                                        } catch (Exception ex) {
                                            JCommonUtil.handleException(ex);
                                        }
                                    }
                                });
                                popUtl.addJMenuItem("to   : " + info.to, new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            DesktopUtil.browse(info.to.toURL().toString());
                                        } catch (Exception ex) {
                                            JCommonUtil.handleException(ex);
                                        }
                                    }
                                });
                                popUtl.addJMenuItem("--------------------------------------------");
                            }

                            if (lst.size() >= 1) {
                                popUtl.addJMenuItem("copy to [to] : " + lst.size(), new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            List<String> flst = new ArrayList<String>();
                                            for (RestoreInfo info : lst) {
                                                flst.add(info.to.getName());
                                            }

                                            boolean confirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("確定要覆蓋以下檔案 :\n" + StringUtils.join(flst, "\n"), "覆蓋到TO");
                                            if (confirm) {
                                                List<String> msglst = new ArrayList<String>();
                                                for (RestoreInfo info : lst) {
                                                    boolean ok = FileUtil.copyFile(info.from, info.to);
                                                    msglst.add(info.to + " -> " + (ok ? "成功" : "失敗"));
                                                }
                                                JCommonUtil._jOptionPane_showMessageDialog_info(StringUtils.join(msglst, "\n"));
                                            } else {
                                                JCommonUtil._jOptionPane_showMessageDialog_info("操作取消");
                                            }
                                        } catch (Exception e1) {
                                            JCommonUtil.handleException(e1);
                                        }
                                    }
                                });

                                popUtl.addJMenuItem("copy to [from] : " + lst.size(), new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            List<String> flst = new ArrayList<String>();
                                            for (RestoreInfo info : lst) {
                                                flst.add(info.from.getName());
                                            }

                                            boolean confirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("確定要覆蓋以下檔案 :\n" + StringUtils.join(flst, "\n"), "覆蓋到From");
                                            if (confirm) {
                                                List<String> msglst = new ArrayList<String>();
                                                for (RestoreInfo info : lst) {
                                                    boolean ok = FileUtil.copyFile(info.to, info.from);
                                                    msglst.add(info.from + " -> " + (ok ? "成功" : "失敗"));
                                                }
                                                JCommonUtil._jOptionPane_showMessageDialog_info(StringUtils.join(msglst, "\n"));
                                            } else {
                                                JCommonUtil._jOptionPane_showMessageDialog_info("操作取消");
                                            }
                                        } catch (Exception e1) {
                                            JCommonUtil.handleException(e1);
                                        }
                                    }
                                });
                            }

                            if (!popUtl.getMenuList().isEmpty()) {
                                popUtl.applyEvent(e);
                                popUtl.show();
                            }
                        }
                    } catch (Exception ex) {
                        JCommonUtil.handleException(ex);
                    }
                }
            });
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
                        dispose();
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

        JCommonUtil.setJFrameCenter(ExecuteOpener_restoreBackProcess.this);
        config.reflectInit(ExecuteOpener_restoreBackProcess.this);
    }

}
