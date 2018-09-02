package gtu.thread.ui;

import gtu.log.Log;
import gtu.swing.util.JOptionPaneUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class ThreadMointerUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private void initGUI() {
        try {
            final SwingActionUtil swingUtil = (SwingActionUtil) SwingActionUtil.newInstance(this);

            GroupLayout thisLayout = new GroupLayout((JComponent) getContentPane());
            getContentPane().setLayout(thisLayout);
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            {
                activeCount = new JTextField();
            }
            {
                jLabel = new JLabel();
                jLabel.setText("activeCount");
            }
            {
                jLabel5 = new JLabel();
                jLabel5.setText("reload time");
            }
            {
                jScrollPane1 = new JScrollPane();
                {
                    stack = new JTextArea();
                    jScrollPane1.setViewportView(stack);
                }
            }
            {
                DefaultComboBoxModel jComboBox1Model = new DefaultComboBoxModel();
                reloadTime = new JComboBox();
                reloadTime.setModel(jComboBox1Model);
                reloadTime.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        swingUtil.invokeAction("reloadTime", evt);
                    }
                });
                for (Reload r : Reload.values()) {
                    jComboBox1Model.addElement(r);
                }
                reloadTime.setSelectedItem(DEFAULT_RELOAD);
            }
            {
                DefaultComboBoxModel changePriorityModel = new DefaultComboBoxModel();
                changePriority = new JComboBox();
                changePriority.setModel(changePriorityModel);
                changePriority.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        swingUtil.invokeAction("changePriority", evt);
                    }
                });
                for (PriorityEnum p : PriorityEnum.values()) {
                    changePriorityModel.addElement(p);
                }
                changePriorityActionPerformed();
            }
            {
                holdsLock = new JButton();
                holdsLock.setText("holdsLock");
                holdsLock.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        swingUtil.invokeAction("holdsLock", evt);
                    }
                });
            }
            {
                yield = new JButton();
                yield.setText("yield");
                yield.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        swingUtil.invokeAction("yield", evt);
                    }
                });
            }
            {
                sleep = new JButton();
                sleep.setText("sleep");
                sleep.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        swingUtil.invokeAction("sleep", evt);
                    }
                });
            }
            {
                sleepValue = new JTextField();
            }
            {
                dumpStack = new JButton();
                dumpStack.setText("dumpStack");
                dumpStack.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        swingUtil.invokeAction("dumpStack", evt);
                    }
                });
            }
            {
                start = new JButton();
                start.setText("start");
                start.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        swingUtil.invokeAction("start", evt);
                    }
                });
            }
            {
                joinValueBtn = new JButton();
                joinValueBtn.setText("join");
                joinValueBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        swingUtil.invokeAction("joinValueBtn", evt);
                    }
                });
            }
            {
                joinValue = new JTextField();
            }
            {
                join = new JButton();
                join.setText("join");
                join.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        swingUtil.invokeAction("join", evt);
                    }
                });
            }
            {
                interrupt = new JButton();
                interrupt.setText("interrupt");
                interrupt.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        swingUtil.invokeAction("interrupt", evt);
                    }
                });
            }
            {
                interrupted = new JTextField();
            }
            {
                jLabel14 = new JLabel();
                jLabel14.setText("isInterrupted");
            }
            {
                daemon = new JTextField();
            }
            {
                jLabel12 = new JLabel();
                jLabel12.setText("isDaemon");
            }
            {
                alive = new JTextField();
            }
            {
                jLabel_ = new JLabel();
                jLabel_.setText("isAlive");
            }
            {
                threadGroup = new JTextField();
            }
            {
                jLabel4 = new JLabel();
                jLabel4.setText("getThreadGroup");
            }
            {
                priority = new JTextField();
            }
            {
                jLabel10 = new JLabel();
                jLabel10.setText("getPriority");
            }
            {
                name = new JTextField();
            }
            {
                jLabel3 = new JLabel();
                jLabel3.setText("getName");
            }
            {
                state = new JTextField();
            }
            {
                jLabel2 = new JLabel();
                jLabel2.setText("getState");
            }
            {
                id = new JTextField();
            }
            {
                jLabel1 = new JLabel();
                jLabel1.setText("getId");
            }
            thisLayout.setHorizontalGroup(thisLayout
                    .createSequentialGroup()
                    .addContainerGap(12, 12)
                    .addGroup(
                            thisLayout
                                    .createParallelGroup()
                                    .addComponent(jScrollPane1, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 472, GroupLayout.PREFERRED_SIZE)
                                    .addGroup(
                                            thisLayout
                                                    .createSequentialGroup()
                                                    .addGap(8)
                                                    .addGroup(
                                                            thisLayout.createParallelGroup()
                                                                    .addComponent(jLabel14, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel12, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel_, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel4, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel10, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel2, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel3, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel1, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel5, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(
                                                            thisLayout.createParallelGroup()
                                                                    .addComponent(interrupted, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(daemon, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(alive, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(threadGroup, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(priority, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(state, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(name, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(id, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(activeCount, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(reloadTime, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE))
                                                    .addGap(41)
                                                    .addGroup(
                                                            thisLayout
                                                                    .createParallelGroup()
                                                                    .addGroup(
                                                                            thisLayout
                                                                                    .createSequentialGroup()
                                                                                    .addGroup(
                                                                                            thisLayout
                                                                                                    .createParallelGroup()
                                                                                                    .addComponent(sleepValue, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 77,
                                                                                                            GroupLayout.PREFERRED_SIZE)
                                                                                                    .addComponent(joinValue, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 77,
                                                                                                            GroupLayout.PREFERRED_SIZE))
                                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                    .addGroup(
                                                                                            thisLayout
                                                                                                    .createParallelGroup()
                                                                                                    .addComponent(sleep, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 75,
                                                                                                            GroupLayout.PREFERRED_SIZE)
                                                                                                    .addComponent(joinValueBtn, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 75,
                                                                                                            GroupLayout.PREFERRED_SIZE)))
                                                                    .addComponent(changePriority, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(holdsLock, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(yield, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(start, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(join, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(interrupt, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(dumpStack, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE))))
                    .addContainerGap(12, 12));
            thisLayout.setVerticalGroup(thisLayout
                    .createSequentialGroup()
                    .addContainerGap(12, 12)
                    .addGroup(
                            thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(reloadTime, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                            thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(dumpStack, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(activeCount, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                            thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(interrupt, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(id, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                            thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(join, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(name, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                            thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(joinValueBtn, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(joinValue, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(state, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                            thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(start, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(priority, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                            thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(sleep, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(sleepValue, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(threadGroup, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                            thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(yield, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(alive, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel_, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                            thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(holdsLock, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(daemon, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                            thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(changePriority, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(interrupted, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE).addContainerGap(12, 12));
            this.setSize(512, 482);
            setLocationRelativeTo(null);
            this.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    swingUtil.invokeAction("jframeClick", evt);
                }
            });

            swingUtil.addAction("dumpStack", new Action() {
                public void action(EventObject evt) throws Exception {
                    Log.debug("Thread.dumpStack();");
                    Thread.dumpStack();
                }
            }).addAction("interrupt", new Action() {
                public void action(EventObject evt) throws Exception {
                    Log.debug("thread.interrupt();");
                    thread.interrupt();
                }
            }).addAction("join", new Action() {
                public void action(EventObject evt) throws Exception {
                    Log.debug("thread.join();");
                    thread.join();
                }
            }).addAction("joinValueBtn", new Action() {
                public void action(EventObject evt) throws Exception {
                    Log.debug("thread.join();");
                    thread.join(Long.parseLong(joinValue.getText()));
                }
            }).addAction("start", new Action() {
                public void action(EventObject evt) throws Exception {
                    Log.debug("thread.start();");
                    thread.start();
                }
            }).addAction("sleep", new Action() {
                public void action(EventObject evt) throws Exception {
                    Log.debug("Thread.sleep();");
                    Thread.sleep(Long.parseLong(sleepValue.getText()));
                }
            }).addAction("yield", new Action() {
                public void action(EventObject evt) throws Exception {
                    Log.debug("Thread.yield();");
                    Thread.yield();
                }
            }).addAction("holdsLock", new Action() {
                public void action(EventObject evt) throws Exception {
                    JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog("holdLock = " + Thread.holdsLock(holdLockObject), getTitle());
                }
            }).addAction("changePriority", new Action() {
                public void action(EventObject evt) throws Exception {
                    Log.debug("thread.setPriority();");
                    PriorityEnum p = (PriorityEnum) changePriority.getSelectedItem();
                    thread.setPriority(p.value);
                }
            }).addAction("reloadTime", new Action() {
                public void action(EventObject evt) throws Exception {
                    reload = (Reload) reloadTime.getSelectedItem();
                    if (reload == Reload.CLOSE) {
                        reloadTime.setEnabled(false);
                    }
                    Log.debug("reload = " + reload);
                }
            }).addAction("jframeClick", new Action() {
                public void action(EventObject evt) throws Exception {
                    reloadThreadProperties();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JTextField activeCount;
    private JTextField alive;
    private JTextField state;
    private JComboBox changePriority;
    private JScrollPane jScrollPane1;
    private JTextArea stack;
    private JComboBox reloadTime;
    private JTextField sleepValue;
    private JTextField name;
    private JTextField threadGroup;
    private JTextField id;
    private JTextField priority;
    private JTextField interrupted;
    private JTextField daemon;
    private JTextField joinValue;

    private JButton sleep;
    private JButton start;
    private JButton holdsLock;
    private JButton yield;
    private JLabel jLabel;
    private JLabel jLabel_;
    private JLabel jLabel2;
    private JLabel jLabel10;
    private JLabel jLabel3;
    private JLabel jLabel1;
    private JLabel jLabel4;
    private JButton joinValueBtn;
    private JButton join;
    private JButton interrupt;
    private JButton dumpStack;
    private JLabel jLabel14;
    private JLabel jLabel12;
    private JLabel jLabel5;

    private final Thread thread;
    private final ThreadGroup threadGroupReal;

    private Object holdLockObject;

    private final static Reload DEFAULT_RELOAD = Reload.FIFTY;
    private static Reload reload = DEFAULT_RELOAD;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        ThreadMointerUI.newInstance(Thread.currentThread()).startWatch();
    }

    public static ThreadMointerUI newInstance(Thread thread) {
        return new ThreadMointerUI(thread, null);
    }

    public static ThreadMointerUI newInstance(Thread thread, ThreadGroup threadGroup) {
        return new ThreadMointerUI(thread, threadGroup);
    }

    private ThreadMointerUI(Thread thread, ThreadGroup threadGroup) {
        super();
        this.thread = thread;
        this.threadGroupReal = threadGroup;
        initGUI();
    }

    public ThreadMointerUI setHoldLock(Object lock) {
        this.holdLockObject = lock;
        return this;
    }

    public enum Reload {
        CLOSE(1), // 
        FIFTY(50), //
        HUNDRED(100), //
        FIVE_HUNDRED(500), //
        ONE_SECOND(1000), //
        TWO_SECOND(2000), //
        THREE_SECOND(3000), //
        FIVE_SECOND(5000), //
        TEN_SECOND(10000), //
        ;//

        final long value;
        static long custom = CLOSE.value;

        Reload(long value) {
            this.value = value;
        }

        long apply() {
            reload = this;
            if (this == CLOSE) {
                return custom;
            }
            return value;
        }

        static void setTime(long value) {
            custom = value;
        }

        public String toString() {
            return name() + "(" + value + ")";
        }
    }

    enum PriorityEnum {
        MIN_PRIORITY(Thread.MIN_PRIORITY), //
        NORM_PRIORITY(Thread.NORM_PRIORITY), //
        MAX_PRIORITY(Thread.MAX_PRIORITY), //
        ONE(1), TWO(2), THREE(3), FOUR(4), SIX(6), SEVEN(7), EIGHT(8), NIGHT(9), ;

        final int value;

        PriorityEnum(int value) {
            this.value = value;
        }

        static PriorityEnum getPriorityEnum(int value) {
            for (PriorityEnum p : PriorityEnum.values()) {
                if (p.value == value) {
                    return p;
                }
            }
            return null;
        }

        public String toString() {
            return this.name() + "(" + value + ")";
        }
    }

    /**
     * 設定Priority給下拉
     */
    private void changePriorityActionPerformed() {
        if (thread != null) {
            this.changePriority.setSelectedItem(PriorityEnum.getPriorityEnum(thread.getPriority()));
        }
    }

    private boolean stopWatch;
    private Thread mointerThread;

    public ThreadMointerUI startWatch() {
        Log.debug("startWatch thread : " + thread);
        this.setVisible(true);
        mointerThread = new Thread(threadGroupReal, new Runnable() {
            public void run() {
                for (;;) {
                    if (thread != null) {
                        reloadThreadProperties();
                    }
                    if (stopWatch) {
                        break;
                    }
                    try {
                        if (reload == Reload.CLOSE) {
                            break;
                        } else {
                            Thread.sleep(reload.apply());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "mointer : " + thread.toString());
        mointerThread.setDaemon(true);
        mointerThread.start();
        return this;
    }

    private void reloadThreadProperties() {
        activeCount.setText(String.valueOf(Thread.activeCount()));
        alive.setText(String.valueOf(thread.isAlive()));
        state.setText(thread.getState().toString());
        name.setText(thread.getName());
        threadGroup.setText((thread.getThreadGroup() == null ? "null" : thread.getThreadGroup().getName()));
        id.setText(String.valueOf(thread.getId()));
        priority.setText(String.valueOf(thread.getPriority()));
        interrupted.setText(String.valueOf(thread.isInterrupted()));
        daemon.setText(String.valueOf(thread.isDaemon()));
        stack.setText(getTreadStack());
        this.setTitle(thread + "#" + mointerThread + "#");
    }

    private String getTreadStack() {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] sts = Thread.getAllStackTraces().get(thread);
        if (sts != null) {
            for (StackTraceElement s : sts) {
                sb.append(s + "\r\n");
            }
        }
        return sb.toString();
    }

    public ThreadMointerUI closeDispose() {
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        return this;
    }

    public Thread getThread() {
        return thread;
    }

    public Thread getReloadThread() {
        return mointerThread;
    }
}
