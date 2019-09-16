package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Triple;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.file.FileUtil;
import gtu.file.OsInfoUtil;
import gtu.properties.PropertiesGroupUtils;
import gtu.properties.PropertiesUtil;
import gtu.runtime.ProcessWatcher;
import gtu.runtime.RuntimeBatPromptModeUtil;
import gtu.string.StringUtil_;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;

public class GitConflictDetectUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private HideInSystemTrayHelper hideInSystemTrayHelper;
    private JFrameRGBColorPanel jFrameRGBColorPanel;
    private SwingActionUtil swingUtil;
    private JTabbedPane tabbedPane;
    private JPanel panel_2;
    private JLabel gitProjectLabel;
    private JTextField gitFolderPathText;
    private JPanel panel_3;
    private JButton gitStatusBtn;
    private JButton gitResetBtn;
    private JPanel panel_4;
    private JPanel panel_5;
    private JPanel panel_6;
    private JPanel panel_7;
    private JList gitConflictList;
    private JTextField gitExePathText;
    private JLabel lblNewLabel_1;
    private JPanel panel_8;
    private JButton saveConfigBtn;
    private JButton nextConfigBtn;
    private JButton delConfigBtn;
    private JButton resolveConflictBtn;
    private JLabel lblNewLabel_2;
    private JTextField gitCmdEncodingText;
    private JTextField gitBranchNameText;
    private JLabel lblNewLabel_3;
    private JLabel lblNewLabel_4;
    private JLabel lblNewLabel_5;
    private JTextField gitUsernameText;
    private JPasswordField gitPasswordText;

    private File configFile = new File(PropertiesUtil.getJarCurrentPath(GitConflictDetectUI.class), GitConflictDetectUI.class.getSimpleName() + "_config.properties");
    private PropertiesGroupUtils config = new PropertiesGroupUtils(configFile);
    private static final String DIFF_PATH_KEY = "diff_path_key";
    private static final String ENCODING_KEY = "encoding_key";
    private static final String PROJECT_KEY = "project_key";
    private static final String GIT_USERNAME_KEY = "git_username_key";
    private static final String GIT_PASSWORD_KEY = "git_password_key";
    private ResolveConflictFileProcess mResolveConflictFileProcess;
    private List<GitFile> statusFileLstBak = new ArrayList<GitFile>();
    private JButton gitPullBtn;
    private JButton gitStashAndPullBtn;
    private JButton gitCommitBtn;
    private JButton gitPushBtn;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance(GitConflictDetectUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GitConflictDetectUI frame = new GitConflictDetectUI();
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
    public GitConflictDetectUI() {
        swingUtil = SwingActionUtil.newInstance(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 701, 559);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener((ChangeListener) ActionAdapter.ChangeListener.create(ActionDefine.JTabbedPane_ChangeIndex.name(), swingUtil));
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("Git", null, panel, null);
        panel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

        gitProjectLabel = new JLabel("專案目錄");
        gitProjectLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                swingUtil.invokeAction("gitProjectLabel.Click", e);
            }
        });
        panel.add(gitProjectLabel, "2, 2, right, default");

        gitFolderPathText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(gitFolderPathText, true);
        panel.add(gitFolderPathText, "4, 2, fill, default");
        gitFolderPathText.setColumns(10);

        panel_3 = new JPanel();
        panel.add(panel_3, "4, 4, fill, fill");

        gitStatusBtn = new JButton("status 清單");
        gitStatusBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("gitStatusBtn.Click", e);
            }
        });
        panel_3.add(gitStatusBtn);

        gitResetBtn = new JButton("清除");
        gitResetBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("gitResetBtn.Click", e);
            }
        });

        gitPullBtn = new JButton("pull");
        gitPullBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("gitPullBtn.Click", e);
            }
        });
        panel_3.add(gitPullBtn);

        gitStashAndPullBtn = new JButton("stash＆pull");
        gitStashAndPullBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("gitStashAndPullBtn.Click", e);
            }
        });
        panel_3.add(gitStashAndPullBtn);
        panel_3.add(gitResetBtn);

        lblNewLabel_1 = new JLabel("執行指令");
        panel.add(lblNewLabel_1, "2, 6, right, default");

        gitExePathText = new JTextField();
        panel.add(gitExePathText, "4, 6, fill, default");
        gitExePathText.setColumns(10);

        panel_8 = new JPanel();
        panel.add(panel_8, "4, 8, fill, fill");

        nextConfigBtn = new JButton("下一組");
        nextConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("nextConfigBtn.Click", e);
            }
        });
        panel_8.add(nextConfigBtn);

        saveConfigBtn = new JButton("儲存設定");
        saveConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("saveConfigBtn.Click", e);
            }
        });
        panel_8.add(saveConfigBtn);

        delConfigBtn = new JButton("刪除設定");
        delConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("delConfigBtn.Click", e);
            }
        });
        panel_8.add(delConfigBtn);

        lblNewLabel_2 = new JLabel("編碼");
        panel.add(lblNewLabel_2, "2, 10, right, default");

        gitCmdEncodingText = new JTextField();
        panel.add(gitCmdEncodingText, "4, 10, fill, default");
        gitCmdEncodingText.setColumns(10);

        lblNewLabel_3 = new JLabel("branch");
        panel.add(lblNewLabel_3, "2, 12, right, default");

        gitBranchNameText = new JTextField();
        panel.add(gitBranchNameText, "4, 12, fill, default");
        gitBranchNameText.setColumns(10);

        lblNewLabel_4 = new JLabel("Git帳號");
        panel.add(lblNewLabel_4, "2, 16, right, default");

        gitUsernameText = new JTextField();
        panel.add(gitUsernameText, "4, 16, fill, default");
        gitUsernameText.setColumns(10);

        lblNewLabel_5 = new JLabel("Git密碼");
        panel.add(lblNewLabel_5, "2, 18, right, default");

        gitPasswordText = new JPasswordField();
        panel.add(gitPasswordText, "4, 18, fill, default");
        gitPasswordText.setColumns(10);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("清單", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        panel_4 = new JPanel();
        panel_1.add(panel_4, BorderLayout.NORTH);

        panel_5 = new JPanel();
        panel_1.add(panel_5, BorderLayout.WEST);

        panel_6 = new JPanel();
        panel_1.add(panel_6, BorderLayout.SOUTH);

        resolveConflictBtn = new JButton("解決衝突");
        resolveConflictBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("resolveConflictBtn.Click", e);
            }
        });
        panel_6.add(resolveConflictBtn);
        gitCommitBtn = new JButton("commit");
        gitCommitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("gitCommitBtn.Click", e);
            }
        });
        panel_6.add(gitCommitBtn);

        gitPushBtn = new JButton("push");
        gitPushBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("gitPushBtn.Click", e);
            }
        });
        panel_6.add(gitPushBtn);

        panel_7 = new JPanel();
        panel_1.add(panel_7, BorderLayout.EAST);

        gitConflictList = new JList();
        gitConflictList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                swingUtil.invokeAction("gitConflictList.Click", e);
            }
        });
        panel_1.add(JCommonUtil.createScrollComponent(gitConflictList), BorderLayout.CENTER);

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
        swingUtil.addActionHex("gitResetBtn.Click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                DefaultListModel model = JListUtil.createModel();
                gitConflictList.setModel(model);
            }
        });
        swingUtil.addActionHex("gitConflictList.Click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                final GitFile gitFile = (GitFile) JListUtil.getLeadSelectionObject(gitConflictList);
                mResolveConflictFileProcess = null;
                System.out.println(gitFile.file);

                final File projectDir = new File(gitFolderPathText.getText());
                String exePath = gitExePathText.getText();

                if (JMouseEventUtil.buttonLeftClick(2, (MouseEvent) evt)) {
                    Validate.notBlank(gitExePathText.getText(), "未輸入diff執行檔pattern");
                    Validate.isTrue(gitFile.mGitLeftRight.isCheck, "檔案未檢核");

                    if (gitFile.mGitLeftRight.isConflictFile) {
                        File leftFile = File.createTempFile("REPO_", ".txt");
                        FileUtil.saveToFile(leftFile, gitFile.mGitLeftRight.left.toString(), "UTF8");
                        File rightFile = File.createTempFile("LOCAL_", ".txt");
                        FileUtil.saveToFile(rightFile, gitFile.mGitLeftRight.right.toString(), "UTF8");
                        String command = String.format(exePath, leftFile, rightFile);
                        RuntimeBatPromptModeUtil run = RuntimeBatPromptModeUtil.newInstance();
                        run.command(command);
                        run.apply();

                        mResolveConflictFileProcess = new ResolveConflictFileProcess(rightFile, gitFile);
                    } else {
                        File leftFile = File.createTempFile("LOCALREPO_", ".txt");

                        String localBranchName = StringUtils.trimToEmpty(gitBranchNameText.getText());
                        FileUtil.saveToFile(leftFile, GitUtil.getLocalRepoContent(projectDir, localBranchName, gitFile.orignName, getEncoding()), "UTF8");
                        String command = String.format(exePath, leftFile, gitFile.file);
                        RuntimeBatPromptModeUtil run = RuntimeBatPromptModeUtil.newInstance();
                        run.command(command);
                        run.apply();
                    }
                } else if (JMouseEventUtil.buttonRightClick(1, (MouseEvent) evt)) {
                    JPopupMenuUtil.newInstance(gitConflictList)//
                            .addJMenuItem("stage", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    GitUtil.stage(projectDir, gitFile.orignName);
                                    new GitCheckProc(projectDir, gitFile.orignName);
                                }
                            })//
                            .addJMenuItem("unstage", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    GitUtil.unstage(projectDir, gitFile.orignName);
                                    new GitCheckProc(projectDir, gitFile.orignName);
                                }
                            })//
                            .addJMenuItem("discard change", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    boolean confirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("是否要回覆到未改變：" + gitFile.file, "回覆到未改變");
                                    if (confirm) {
                                        GitUtil.discardChange(projectDir, gitFile.orignName);
                                        new GitCheckProc(projectDir, gitFile.orignName);
                                    }
                                }
                            })//
                            .addJMenuItem("delete file", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    boolean confirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("是否要刪除：" + gitFile.file, "刪除");
                                    if (confirm) {
                                        gitFile.file.delete();
                                        new GitCheckProc(projectDir, gitFile.orignName);
                                    }
                                }
                            })//
                            .addJMenuItem("reload", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    new GitCheckProc(projectDir, "");
                                }
                            })//
                            .applyEvent(evt)//
                            .show();
                }
            }
        });

        swingUtil.addActionHex("nextConfigBtn.Click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                config.next();
                Map<String, String> map = config.loadConfig();
                gitExePathText.setText(StringUtils.trimToEmpty(map.get(DIFF_PATH_KEY)));
                gitCmdEncodingText.setText(StringUtils.trimToEmpty(map.get(ENCODING_KEY)));
                gitFolderPathText.setText(StringUtils.trimToEmpty(map.get(PROJECT_KEY)));
                gitUsernameText.setText(StringUtils.trimToEmpty(map.get(GIT_USERNAME_KEY)));
                gitPasswordText.setText(StringUtils.trimToEmpty(map.get(GIT_PASSWORD_KEY)));
            }
        });
        swingUtil.addActionHex("saveConfigBtn.Click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                if (StringUtils.isNotBlank(gitExePathText.getText())) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(DIFF_PATH_KEY, gitExePathText.getText());
                    map.put(ENCODING_KEY, gitCmdEncodingText.getText());
                    map.put(PROJECT_KEY, gitFolderPathText.getText());
                    map.put(GIT_USERNAME_KEY, gitUsernameText.getText());
                    map.put(GIT_PASSWORD_KEY, gitPasswordText.getText());
                    config.saveConfig(map, true);
                    JCommonUtil._jOptionPane_showMessageDialog_info("儲存成功！");
                }
            }
        });
        swingUtil.addActionHex("delConfigBtn.Click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                config.removeConfig();
                JCommonUtil._jOptionPane_showMessageDialog_info("刪除成功！");
            }
        });
        swingUtil.addActionHex("resolveConflictBtn.Click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                if (mResolveConflictFileProcess != null) {
                    boolean confirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("請問是否蓋掉此檔案：" + mResolveConflictFileProcess.gitFile.file.getName(), "解決衝突！");
                    if (confirm) {
                        boolean result = mResolveConflictFileProcess.resolve();
                        JCommonUtil._jOptionPane_showMessageDialog_info("解決" + (result ? "成功" : "失敗") + "：" + mResolveConflictFileProcess.gitFile.file);
                    }
                }
            }
        });
        swingUtil.addActionHex("gitStatusBtn.Click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                File gitFolder = new File(gitFolderPathText.getText());
                new GitCheckProc(gitFolder, "");

                // 取得branch
                gitBranchNameText.setText(GitUtil.getCurrentBranch(gitFolder, getEncoding()));
                JCommonUtil._jOptionPane_showMessageDialog_info("完成！");
            }
        });
        swingUtil.addActionHex("gitPullBtn.Click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                Validate.notBlank(gitFolderPathText.getText(), "請輸入專案目錄");
                File projectDir = new File(gitFolderPathText.getText());
                String resultString = GitUtil.pull(projectDir);
                JCommonUtil._jOptionPane_showMessageDialog_InvokeLater_Html(resultString);
            }
        });
        swingUtil.addActionHex("gitStashAndPullBtn.Click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                Validate.notBlank(gitFolderPathText.getText(), "請輸入專案目錄");
                File projectDir = new File(gitFolderPathText.getText());
                String resultString = GitUtil.stashAndPull(projectDir);
                JCommonUtil._jOptionPane_showMessageDialog_InvokeLater_Html(resultString);
            }
        });
        swingUtil.addActionHex("gitCommitBtn.Click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                Validate.notBlank(gitFolderPathText.getText(), "請輸入專案目錄");
                String message = JCommonUtil._jOptionPane_showInputDialog("請輸入Message", "");
                Validate.notBlank(message, "message不可為空!");
                File projectDir = new File(gitFolderPathText.getText());
                String resultString = GitUtil.commit(projectDir, message);
                JCommonUtil._jOptionPane_showMessageDialog_InvokeLater_Html(resultString);
            }
        });
        swingUtil.addActionHex("gitPushBtn.Click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                Validate.notBlank(gitFolderPathText.getText(), "請輸入專案目錄");
                Validate.notBlank(gitUsernameText.getText(), "Git帳號未輸入");
                Validate.notBlank(gitPasswordText.getText(), "Git密碼未輸入");
                File projectDir = new File(gitFolderPathText.getText());
                String resultString = GitUtil.push(projectDir, //
                        StringUtils.trimToEmpty(gitUsernameText.getText()), //
                        StringUtils.trimToEmpty(gitPasswordText.getText()));
                JCommonUtil._jOptionPane_showMessageDialog_InvokeLater_Html(resultString);
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

    private enum GitPtnEnum {
        LEFT, RIGHT, NONE;
    }

    private class GitLeftRight {
        // <<<<<<< HEAD
        // <div>我是 Cat</div>
        // =======
        // <div>我是 Dog</div>
        // >>>>>>> dog

        Pattern startPtn = Pattern.compile("^[\\s\\t]*\\<{7}");
        Pattern middlePtn = Pattern.compile("^[\\s\\t]*\\={7}");
        Pattern rightPtn = Pattern.compile("^[\\s\\t]*\\>{7}");

        boolean isCheck = false;
        boolean isUntracked = false;
        boolean isConflictFile = false;
        StringBuffer left = new StringBuffer();
        StringBuffer right = new StringBuffer();

        private boolean isMatch(String strVal, Pattern ptn) {
            return ptn.matcher(strVal).find();
        }

        private void setConflictFile() {
            if (!isConflictFile) {
                isConflictFile = true;
            }
        }

        private void __load(String content) {
            isCheck = true;
            List<String> lst = StringUtil_.readContentToList(content, false, false, false);
            GitPtnEnum status = GitPtnEnum.NONE;
            String changeLine = "\r\n";

            for (int ii = 0; ii < lst.size(); ii++) {
                String line = lst.get(ii);
                if (ii == lst.size() - 1) {
                    changeLine = "";
                }

                if (status == GitPtnEnum.NONE && isMatch(line, startPtn)) {
                    status = GitPtnEnum.LEFT;
                    continue;
                } else if (status == GitPtnEnum.LEFT && isMatch(line, middlePtn)) {
                    status = GitPtnEnum.RIGHT;
                    continue;
                } else if (status == GitPtnEnum.RIGHT && isMatch(line, rightPtn)) {
                    status = GitPtnEnum.NONE;
                    setConflictFile();
                    continue;
                }

                switch (status) {
                case NONE:
                    left.append(line + changeLine);
                    right.append(line + changeLine);
                    break;
                case LEFT:
                    left.append(line + changeLine);
                    break;
                case RIGHT:
                    right.append(line + changeLine);
                    break;
                }
            }
        }

        private void load(File file) {
            if (!file.isFile()) {
                System.out.println("BLANK [isDir] : " + file);
                return;
            }
            String content = gtu.file.FileUtil.loadFromFile(file, "UTF8");
            if (StringUtils.isBlank(content)) {
                System.out.println("BLANK : " + file);
                return;
            }
            if (isUntracked) {
                right.append(content);
            } else {
                __load(content);
            }
        }
    }

    private class GitFile {
        String orignName;
        File file;
        GitLeftRight mGitLeftRight = new GitLeftRight();

        String stageDesc = "";
        String stageColor = "";
        String statusDesc = "";
        String statusColor = "";

        GitFile(String orignName, File file) {
            this.orignName = orignName;
            this.file = file;
        }

        public void load() {
            mGitLeftRight.load(file);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            String string = String.format("<font color='%s'>%s</font> " + //
                    "<font color='%s'>%s</font> " + //
                    "<font color='%s'>%s</font> ", //
                    stageColor, stageDesc, //
                    statusColor, statusDesc, //
                    (mGitLeftRight.isConflictFile ? "red" : ""), orignName);//
            sb.append("<html>\n");
            sb.append(string);
            sb.append("</html>\n");
            return sb.toString();
        }
    }

    private class GitCheckProc {
        File projectDir;
        List<GitFile> statusFileLst = new ArrayList<GitFile>();

        private void applyStatus(GitFile g, String status) {
            g.statusDesc = status;
            if ("modified".equalsIgnoreCase(status)) {
                g.statusColor = "green";
            } else if ("new file".equalsIgnoreCase(status)) {
                g.statusColor = "yellow";
            } else if ("deleted".equalsIgnoreCase(status)) {
                g.statusColor = "red";
            }
        }

        private void copyFromOld(GitFile f, String reloadGitFilePath) {
            if (statusFileLstBak == null && statusFileLstBak.isEmpty()) {
                return;
            }
            if (StringUtils.isNotBlank(reloadGitFilePath)) {
                String ignoreOriginName = GitUtil.getGitOrignPathName(reloadGitFilePath);
                if (StringUtils.equals(f.orignName, ignoreOriginName)) {
                    return;
                }
            }
            for (GitFile g : statusFileLstBak) {
                if (StringUtils.equals(g.orignName, f.orignName)) {
                    f.mGitLeftRight = g.mGitLeftRight;
                    break;
                }
            }
        }

        GitCheckProc(File projectDir, String reloadGitFilePath) {
            this.projectDir = projectDir;
            Triple<List<String[]>, List<String[]>, List<String[]>> statusInfo = GitUtil.getStatusInfo(projectDir, getEncoding());
            for (String[] line : statusInfo.getLeft()) {
                GitFile g = new GitFile(line[1], new File(projectDir, line[1]));
                g.stageColor = "blue";
                g.stageDesc = "commit";
                this.applyStatus(g, line[0]);
                copyFromOld(g, reloadGitFilePath);
                statusFileLst.add(g);
            }
            for (String[] line : statusInfo.getMiddle()) {
                GitFile g = new GitFile(line[1], new File(projectDir, line[1]));
                g.stageColor = "green";
                g.stageDesc = "uncommit";
                this.applyStatus(g, line[0]);
                copyFromOld(g, reloadGitFilePath);
                if (!g.mGitLeftRight.isCheck) {
                    g.load();
                }
                statusFileLst.add(g);
            }
            for (String[] line : statusInfo.getRight()) {
                GitFile g = new GitFile(line[1], new File(projectDir, line[1]));
                g.stageColor = "yellow";
                g.stageDesc = "untracted";
                this.applyStatus(g, line[0]);
                copyFromOld(g, reloadGitFilePath);
                statusFileLst.add(g);
            }

            statusFileLstBak = new ArrayList<GitFile>(statusFileLst);

            DefaultListModel model = JListUtil.createModel();
            gitConflictList.setModel(model);
            for (GitFile f : statusFileLst) {
                model.addElement(f);
            }
            // model.addElement(new GitFile("TEST----", new
            // File("C:\\Users\\wistronits\\Desktop\\新文字文件.txt")));//TODO
        }
    }

    private String getEncoding() {
        if (StringUtils.isNotBlank(gitCmdEncodingText.getText())) {
            return StringUtils.trimToEmpty(gitCmdEncodingText.getText());
        }
        gitCmdEncodingText.setText("BIG5");
        return "BIG5";
    }

    private class ResolveConflictFileProcess {
        File resolveFile;
        GitFile gitFile;

        ResolveConflictFileProcess(File resolveFile, GitFile gitFile) {
            this.resolveFile = resolveFile;
            this.gitFile = gitFile;
        }

        private boolean resolve() {
            return resolveFile.renameTo(gitFile.file);
        }
    }

    public SwingActionUtil getSwingUtil() {
        return swingUtil;
    }

    private static class GitUtil {

        private static void addProjectCommand(File projectDir, RuntimeBatPromptModeUtil inst) {
            inst.command("cd " + projectDir);
            inst.runInBatFile(false);
            String rootFile = FileUtil.getFileRoot(projectDir);
            if (rootFile != null) {
                inst.command("" + rootFile);
            }
        }

        private static String getCurrentBranch(File projectDir, String encoding) {
            RuntimeBatPromptModeUtil inst = RuntimeBatPromptModeUtil.newInstance();
            addProjectCommand(projectDir, inst);
            inst.command("git branch");
            ProcessWatcher p = ProcessWatcher.newInstance(inst.apply());
            p.encode(encoding);
            p.getStreamSync();
            String resultStr = p.getInputStreamToString();
            System.out.println("branch start------------------------------");
            System.out.println(resultStr);
            System.out.println("branch end  ------------------------------");
            List<String> lst = StringUtil_.readContentToList(resultStr, true, true, true);
            for (String b : lst) {
                b = StringUtils.trimToEmpty(b);
                if (b.startsWith("*")) {
                    return StringUtils.trimToEmpty(b.substring(1));
                }
            }
            return "";
        }

        private static String getGitOrignStatus(String gitOrignPathName) {
            Pattern ptn = Pattern.compile("(modified|new\\sfile|deleted)\\:\\s*(.*)");
            Matcher mth = ptn.matcher(gitOrignPathName);
            if (mth.find()) {
                return StringUtils.trimToEmpty(mth.group(1));// 檔名(為git目錄後開始)
            }
            return "";
        }

        private static String getGitOrignPathName(String gitOrignPathName) {
            Pattern ptn = Pattern.compile("(?:modified|new\\sfile|deleted)\\:\\s*(.*)");
            Matcher mth = ptn.matcher(gitOrignPathName);
            if (mth.find()) {
                return StringUtils.trimToEmpty(mth.group(1));// 檔名(為git目錄後開始)
            }
            return StringUtils.trimToEmpty(gitOrignPathName);
        }

        private static String getLocalRepoContent(File projectDir, String localBranchName, String gitOrignPathName, String encoding) {
            String fileGitPath = getGitOrignPathName(gitOrignPathName);// 檔名(為git目錄後開始)
            RuntimeBatPromptModeUtil run = RuntimeBatPromptModeUtil.newInstance();
            addProjectCommand(projectDir, run);
            String startTagCommand = String.format("git show %s:%s", localBranchName, fileGitPath);
            run.command(startTagCommand);

            ProcessWatcher p = ProcessWatcher.newInstance(run.apply());
            p.encode(encoding);
            p.getStreamSync();
            String fileContent = p.getInputStreamToString();
            if (OsInfoUtil.isWindows()) {
                fileContent = RuntimeBatPromptModeUtil.getFixBatInputString(fileContent, 3 * 2, 0);
            }
            return fileContent;
        }

        private static void stage(File projectDir, String gitOrignPathName) {
            String fileGitPath = getGitOrignPathName(gitOrignPathName);// 檔名(為git目錄後開始)
            RuntimeBatPromptModeUtil run = RuntimeBatPromptModeUtil.newInstance();
            addProjectCommand(projectDir, run);
            run.command("git add " + fileGitPath);
            ProcessWatcher p = ProcessWatcher.newInstance(run.apply());
            p.getStreamSync();
        }

        private static void unstage(File projectDir, String gitOrignPathName) {
            String fileGitPath = getGitOrignPathName(gitOrignPathName);// 檔名(為git目錄後開始)
            RuntimeBatPromptModeUtil run = RuntimeBatPromptModeUtil.newInstance();
            addProjectCommand(projectDir, run);
            run.command("git reset " + fileGitPath);
            ProcessWatcher p = ProcessWatcher.newInstance(run.apply());
            p.getStreamSync();
        }

        private static void discardChange(File projectDir, String gitOrignPathName) {
            String fileGitPath = getGitOrignPathName(gitOrignPathName);// 檔名(為git目錄後開始)
            RuntimeBatPromptModeUtil run = RuntimeBatPromptModeUtil.newInstance();
            addProjectCommand(projectDir, run);
            run.command("git checkout HEAD " + fileGitPath);
            ProcessWatcher p = ProcessWatcher.newInstance(run.apply());
            p.getStreamSync();
        }

        /**
         * Left : stage// middle : unstage// right : untraced
         * 
         * @param projectDir
         * @param encoding
         * @return
         */
        private static Triple<List<String[]>, List<String[]>, List<String[]>> getStatusInfo(File projectDir, String encoding) {
            List<String[]> stageLst = new ArrayList<String[]>();
            List<String[]> unstageLst = new ArrayList<String[]>();
            List<String[]> untractedLst = new ArrayList<String[]>();

            String startTag = "Changes to be committed:";
            String endTag = "Changes not staged for commit:";
            String finalTag = "Untracked files:";
            List<String> ignoreLst = new ArrayList<String>();
            ignoreLst.add("no changes added to commit (use \"git add\" and/or \"git commit -a\")");

            RuntimeBatPromptModeUtil run = RuntimeBatPromptModeUtil.newInstance();
            addProjectCommand(projectDir, run);
            run.command("git status");

            ProcessWatcher p = ProcessWatcher.newInstance(run.apply());
            p.encode(encoding);
            p.getStreamSync();
            String statusContent = p.getInputStreamToString();
            if (OsInfoUtil.isWindows()) {
                statusContent = RuntimeBatPromptModeUtil.getFixBatInputString(statusContent, 3 * 2, 0);
            }

            List<String> lst = StringUtil_.readContentToList(statusContent, true, true, false);
            int linePos = -1;
            String type = "";
            A: for (int ii = 0; ii < lst.size(); ii++) {
                String line = lst.get(ii);
                if (line.contains(startTag)) {
                    linePos = ii + 1;
                    type = "stage";
                } else if (line.contains(endTag)) {
                    linePos = ii + 2;
                    type = "unstage";
                } else if (line.contains(finalTag)) {
                    linePos = ii + 1;
                    type = "untracked";
                }

                for (String ignoreStr : ignoreLst) {
                    if (line.contains(ignoreStr)) {
                        continue A;
                    }
                }

                if (ii > linePos && "stage".equals(type)) {
                    stageLst.add(new String[] { getGitOrignStatus(line), getGitOrignPathName(line) });
                } else if (ii > linePos && "unstage".equals(type)) {
                    unstageLst.add(new String[] { getGitOrignStatus(line), getGitOrignPathName(line) });
                } else if (ii > linePos && "untracked".equals(type)) {
                    untractedLst.add(new String[] { getGitOrignStatus(line), getGitOrignPathName(line) });
                }
            }
            return Triple.of(stageLst, unstageLst, untractedLst);
        }

        private static String commit(File projectDir, String message) {
            RuntimeBatPromptModeUtil run = RuntimeBatPromptModeUtil.newInstance();
            addProjectCommand(projectDir, run);
            run.command("git commit -m \"" + StringUtils.trimToEmpty(message) + "\"");
            ProcessWatcher p = ProcessWatcher.newInstance(run.apply());
            p.getStreamSync();
            String resultString = p.getInputStreamToString();
            if (OsInfoUtil.isWindows()) {
                resultString = RuntimeBatPromptModeUtil.getFixBatInputString(resultString, 3 * 2, 0);
            }
            System.out.println(resultString);
            return resultString;
        }

        private static String push(File projectDir, String username, String password) {
            RuntimeBatPromptModeUtil run = RuntimeBatPromptModeUtil.newInstance();
            addProjectCommand(projectDir, run);
            run.command("git push");
            run.command(username);
            run.command(password);
            ProcessWatcher p = ProcessWatcher.newInstance(run.apply());
            p.getStreamSync();
            String resultString = p.getInputStreamToString();
            if (OsInfoUtil.isWindows()) {
                resultString = RuntimeBatPromptModeUtil.getFixBatInputString(resultString, 3 * 2, 0);
            }
            System.out.println(resultString);
            return resultString;
        }

        private static String pull(File projectDir) {
            RuntimeBatPromptModeUtil run = RuntimeBatPromptModeUtil.newInstance();
            addProjectCommand(projectDir, run);
            run.command("git pull");
            ProcessWatcher p = ProcessWatcher.newInstance(run.apply());
            p.getStreamSync();
            String resultString = p.getInputStreamToString();
            if (OsInfoUtil.isWindows()) {
                resultString = RuntimeBatPromptModeUtil.getFixBatInputString(resultString, 3 * 2, 0);
            }
            System.out.println(resultString);
            return resultString;
        }

        private static String stashAndPull(File projectDir) {
            RuntimeBatPromptModeUtil run = RuntimeBatPromptModeUtil.newInstance();
            addProjectCommand(projectDir, run);
            run.command("git stash");
            run.command("git pull");
            run.command("git stash pop");
            ProcessWatcher p = ProcessWatcher.newInstance(run.apply());
            p.getStreamSync();
            String resultString = p.getInputStreamToString();
            if (OsInfoUtil.isWindows()) {
                resultString = RuntimeBatPromptModeUtil.getFixBatInputString(resultString, 3 * 2, 0);
            }
            System.out.println(resultString);
            return resultString;
        }
    }
}
