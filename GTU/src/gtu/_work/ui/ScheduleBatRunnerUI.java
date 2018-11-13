package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang3.StringUtils;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.runtime.ProcessWatcher;
import gtu.runtime.RuntimeBatPromptModeUtil;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JTextAreaUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;

public class ScheduleBatRunnerUI extends JFrame {

    private JPanel contentPane;
    private HideInSystemTrayHelper hideInSystemTrayHelper;
    private SwingActionUtil swingUtil;
    private JTabbedPane tabbedPane;
    private JPanel panel_2;
    private JPanel panel_3;
    private JPanel panel_4;
    private JPanel panel_5;
    private JList batList;
    private JLabel lblNewLabel;
    private JButton addBatBtn;
    private JButton goBtn;
    private DefaultListModel listModel = JListUtil.createModel();
    private JButton cleanBtn;
    private JTextArea batTextArea;
    private Thread batRunningThread = null;
    private JPanel panel_6;
    private JPanel panel_7;
    private JPanel panel_8;
    private JPanel panel_9;
    private JTextArea logArea;
    private PrintStream logPrinter;
    private LinkedBlockingQueue<CommandBean> allList = new LinkedBlockingQueue<CommandBean>();
    private JButton clearLogBtn;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance(ScheduleBatRunnerUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ScheduleBatRunnerUI frame = new ScheduleBatRunnerUI();
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
    public ScheduleBatRunnerUI() {
        swingUtil = SwingActionUtil.newInstance(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 367);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener((ChangeListener)ActionAdapter.ChangeListener.create(ActionDefine.JTabbedPane_ChangeIndex.name(), swingUtil));
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("Add Bat", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        panel_2 = new JPanel();
        panel.add(panel_2, BorderLayout.NORTH);

        lblNewLabel = new JLabel("加入指令");
        panel_2.add(lblNewLabel);

        addBatBtn = new JButton("加入");
        addBatBtn.addActionListener((ActionListener)SwingActionUtil.ActionAdapter.ActionListener.create(ActionDefine.addBatBtn_Click.name(), swingUtil));

        batTextArea = new JTextArea();
        batTextArea.setColumns(30);
        batTextArea.setRows(3);
        JCommonUtil.jTextFieldSetFilePathMouseEvent(batTextArea, true);
        panel_2.add(JCommonUtil.createScrollComponent(batTextArea));
        panel_2.add(addBatBtn);

        goBtn = new JButton("Go");
        goBtn.addActionListener((ActionListener)SwingActionUtil.ActionAdapter.ActionListener.create(ActionDefine.goBtn_Click.name(), swingUtil));

        cleanBtn = new JButton("清空");
        cleanBtn.addActionListener((ActionListener)SwingActionUtil.ActionAdapter.ActionListener.create(ActionDefine.cleanBtn_Click.name(), swingUtil));
        panel_2.add(cleanBtn);
        panel_2.add(goBtn);

        panel_3 = new JPanel();
        panel.add(panel_3, BorderLayout.WEST);

        panel_4 = new JPanel();
        panel.add(panel_4, BorderLayout.EAST);

        panel_5 = new JPanel();
        panel.add(panel_5, BorderLayout.SOUTH);

        batList = new JList();
        panel.add(JCommonUtil.createScrollComponent(batList), BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("Log", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        panel_6 = new JPanel();
        panel_1.add(panel_6, BorderLayout.NORTH);

        panel_7 = new JPanel();
        panel_1.add(panel_7, BorderLayout.EAST);

        panel_8 = new JPanel();
        panel_1.add(panel_8, BorderLayout.WEST);

        panel_9 = new JPanel();
        panel_1.add(panel_9, BorderLayout.SOUTH);

        clearLogBtn = new JButton("clear log");
        clearLogBtn.addActionListener((ActionListener)SwingActionUtil.ActionAdapter.ActionListener.create(ActionDefine.clearLogBtn_Click.name(), swingUtil));
        panel_9.add(clearLogBtn);

        logArea = new JTextArea();
        panel_1.add(JCommonUtil.createScrollComponent(logArea), BorderLayout.CENTER);

        logPrinter = JTextAreaUtil.getNewPrintStream2JTextArea(logArea, -1, true);

        {
            // 掛載所有event
            applyAllEvents();

            JCommonUtil.setJFrameCenter(this);
            JCommonUtil.setJFrameIcon(this, "resource/images/ico/tk_aiengine.ico");
            hideInSystemTrayHelper = HideInSystemTrayHelper.newInstance();
            hideInSystemTrayHelper.apply(this);
            this.applyAppMenu();
            JCommonUtil.defaultToolTipDelay();

            batList.setModel(listModel);
        }
    }

    private enum ActionDefine {
        TEST_DEFAULT_EVENT, //
        JTabbedPane_ChangeIndex, //
        addBatBtn_Click, //
        goBtn_Click, //
        cleanBtn_Click, //
        clearLogBtn_Click, //
        ;
    }

    private void applyAllEvents() {
        swingUtil.addAction(ActionDefine.TEST_DEFAULT_EVENT.name(), new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                System.out.println("====Test Default Event!!====");
            }
        });
        swingUtil.addAction(ActionDefine.JTabbedPane_ChangeIndex.name(), new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                System.out.println("tabbedPane : " + tabbedPane.getSelectedIndex());
            }
        });
        swingUtil.addAction(ActionDefine.addBatBtn_Click.name(), new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                CommandBean bean = new CommandBean();
                bean.command.setLength(0);
                bean.command.append(batTextArea.getText());
                batTextArea.setText("");

                boolean findOk = false;
                for (int ii = 0; ii < listModel.getSize(); ii++) {
                    CommandBean comm = (CommandBean) listModel.getElementAt(ii);
                    if (comm.equals(bean)) {
                        findOk = true;
                        break;
                    }
                }

                if (findOk) {
                    JCommonUtil._jOptionPane_showMessageDialog_error("此指令以存在");
                    return;
                }

                listModel.addElement(bean);
                batList.updateUI();
            }
        });
        swingUtil.addAction(ActionDefine.goBtn_Click.name(), new Action() {
            private void updateLst() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        batList.updateUI();
                    }
                });
            }

            private void logData(final CommandBean bean) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        logPrinter.println("command : " + bean.command);
                        logPrinter.println("Message : " + bean.okMsg);
                        logPrinter.println("Error : " + bean.errMsg);
                    }
                });
            }

            @Override
            public void action(EventObject evt) throws Exception {
                List<CommandBean> lst = new ArrayList<CommandBean>();
                for (int ii = 0; ii < listModel.getSize(); ii++) {
                    CommandBean bean = (CommandBean) listModel.getElementAt(ii);
                    if (bean.commandBeanStatue == CommandBeanStatue.WAITING) {
                        lst.add(bean);
                    }
                }

                if (lst.isEmpty()) {
                    JCommonUtil._jOptionPane_showMessageDialog_error("no waiting command!");
                    return;
                }

                allList.addAll(lst);

                if (batRunningThread == null || batRunningThread.getState() == Thread.State.TERMINATED) {
                    batRunningThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (; !allList.isEmpty();) {
                                CommandBean bean = allList.poll();
                                try {
                                    RuntimeBatPromptModeUtil inst = RuntimeBatPromptModeUtil.newInstance();
                                    inst.command(bean.command);
                                    ProcessWatcher watcher = ProcessWatcher.newInstance(inst.apply());
                                    bean.commandBeanStatue = CommandBeanStatue.RUNNING;
                                    updateLst();
                                    watcher.getStreamSync();
                                    String errMsg = watcher.getErrorStreamToString();
                                    String okMsg = watcher.getInputStreamToString();
                                    if (StringUtils.isNotBlank(errMsg)) {
                                        bean.errMsg.append(errMsg);
                                        bean.commandBeanStatue = CommandBeanStatue.FAILED;
                                    } else if (StringUtils.isNotBlank(okMsg)) {
                                        bean.okMsg.append(okMsg);
                                        bean.commandBeanStatue = CommandBeanStatue.SUCCESS;
                                    }
                                    bean.commandBeanStatue = CommandBeanStatue.SUCCESS;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    bean.commandBeanStatue = CommandBeanStatue.FAILED;
                                    bean.errMsg.append(e.getMessage());
                                } finally {
                                    logData(bean);
                                    updateLst();
                                }
                            }
                            JCommonUtil._jOptionPane_showMessageDialog_error("done..");
                        }
                    });
                    batRunningThread.start();
                } else {
                    JCommonUtil._jOptionPane_showMessageDialog_error("thread is running!");
                    return;
                }
            }
        });
        swingUtil.addAction(ActionDefine.cleanBtn_Click.name(), new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                for (int ii = 0; ii < listModel.getSize(); ii++) {
                    CommandBean bean = (CommandBean) listModel.getElementAt(ii);
                    if (bean.commandBeanStatue == CommandBeanStatue.SUCCESS || bean.commandBeanStatue == CommandBeanStatue.FAILED) {
                        listModel.removeElementAt(ii);
                        ii--;
                    }
                }
                batList.updateUI();
            }
        });
        swingUtil.addAction(ActionDefine.clearLogBtn_Click.name(), new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                logArea.setText("");
            }
        });
    }

    private enum CommandBeanStatue {
        WAITING("blue", "waiting"), //
        RUNNING("blue", "running..."), //
        SUCCESS("green", "done"), //
        FAILED("red", "failed"),//
        ;

        String color;
        String label;

        CommandBeanStatue(String color, String label) {
            this.color = color;
            this.label = label;
        }
    }

    private class CommandBean {
        CommandBeanStatue commandBeanStatue = CommandBeanStatue.WAITING;

        StringBuffer errMsg = new StringBuffer();
        StringBuffer okMsg = new StringBuffer();
        StringBuffer command = new StringBuffer();

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((command == null) ? 0 : command.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            CommandBean other = (CommandBean) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (command == null) {
                if (other.command != null)
                    return false;
            } else if (!StringUtils.equals(command.toString(), other.command.toString()))
                return false;
            return true;
        }

        public String toString() {
            String color = "";
            String label = "";
            String message = command.toString();
            String format = "<html><font color='%s'>%s</font>&nbsp;&nbsp;&nbsp;%s</html>";
            return String.format(format, commandBeanStatue.color, commandBeanStatue.label, message);
        }

        private ScheduleBatRunnerUI getOuterType() {
            return ScheduleBatRunnerUI.this;
        }
    }

    private void applyAppMenu() {
        JMenu menu1 = JMenuAppender.newInstance("child_item")//
                .addMenuItem("detail1", (ActionListener)ActionAdapter.ActionListener.create(ActionDefine.TEST_DEFAULT_EVENT.name(), getSwingUtil()))//
                .getMenu();
        JMenu mainMenu = JMenuAppender.newInstance("file")//
                .addMenuItem("item1", null)//
                .addMenuItem("item2", (ActionListener)ActionAdapter.ActionListener.create(ActionDefine.TEST_DEFAULT_EVENT.name(), getSwingUtil()))//
                .addChildrenMenu(menu1)//
                .getMenu();
        JMenuBarUtil.newInstance().addMenu(mainMenu).apply(this);
    }

    public SwingActionUtil getSwingUtil() {
        return swingUtil;
    }
}
