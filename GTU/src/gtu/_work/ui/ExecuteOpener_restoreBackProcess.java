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

import gtu.file.FileUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.runtime.DesktopUtil;
import gtu.runtime.ProcessWatcher;
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
        List<Pair<File, File>> lst = new ArrayList<Pair<File, File>>();
        lst.add(Pair.of(new File("D:/eclipse/artifacts.xml"), new File("D:/eclipse/artifacts - 複製.xml")));
        ExecuteOpener_restoreBackProcess.newInstance(lst);
    }

    public static ExecuteOpener_restoreBackProcess newInstance(List<Pair<File, File>> fromToList) {
        try {
            ExecuteOpener_restoreBackProcess dialog = new ExecuteOpener_restoreBackProcess();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);

            dialog.modelHandler.initListModel(fromToList, dialog.restoreLst);

            return dialog;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

        public String toString() {
            if (IS_FROM) {
                return from.getAbsolutePath();
            } else {
                return to.getAbsolutePath();
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
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        RestoreInfo current = JListUtil.getLeadSelectionObject(restoreLst);
                        if (current != null) {
                            System.out.println(ReflectionToStringBuilder.toString(current, ToStringStyle.MULTI_LINE_STYLE));
                        }

                        if (JMouseEventUtil.buttonRightClick(1, e)) {

                            JPopupMenuUtil popUtl = JPopupMenuUtil.newInstance(restoreLst);

                            final List<RestoreInfo> lst = JListUtil.getLeadSelectionArry(restoreLst);

                            if (lst.size() == 1) {
                                final RestoreInfo info = lst.get(0);

                                popUtl.addJMenuItem("compare git", new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        String tortoiseMergeFormat = "cmd /c call " + txtTortoisegitmergebasestheirss.getText();
                                        final String command = String.format(tortoiseMergeFormat, info.from, info.to);

                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    System.out.println(command);
                                                    ProcessWatcher watcher = ProcessWatcher.newInstance(Runtime.getRuntime().exec(command));
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
