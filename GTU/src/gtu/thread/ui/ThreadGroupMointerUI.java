package gtu.thread.ui;

import gtu.log.Log;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

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
public class ThreadGroupMointerUI extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    private JLabel jLabel5;
    private JComboBox reloadTime;
    private JLabel jLabel9;
    private JTree groupTree;
    private JButton list_;
    private JButton interrupt;
    private JButton destroy;
    private JLabel jLabel8;
    private JTextArea toString;
    private JTextArea name;
    private JLabel jLabel7;
    private JTextField destroyed;
    private JLabel jLabel6;
    private JTextField daemon;
    private JLabel jLabel4;
    private JTextField parent;
    private JTextField maxPriority;
    private JLabel jLabel3;
    private JTextField activeGroupCount;
    private JLabel jLabel2;
    private JTextField activeCount;
    private JLabel jLabel1;
    private JPopupMenu jPopupMenu1;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ThreadGroupMointerUI inst = new ThreadGroupMointerUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public ThreadGroupMointerUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            final SwingActionUtil swingUtil = (SwingActionUtil) SwingActionUtil.newInstance(this);

            GroupLayout thisLayout = new GroupLayout((JComponent) getContentPane());
            getContentPane().setLayout(thisLayout);
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            this.setTitle("ThreadGroupTree");
            this.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    swingUtil.invokeAction("jframeClick", evt);
                }
            });
            {
                daemon = new JTextField();
            }
            {
                jLabel6 = new JLabel();
                jLabel6.setText("isDaemon");
            }
            {
                maxPriority = new JTextField();
            }
            {
                jLabel4 = new JLabel();
                jLabel4.setText("maxPriority");
            }
            {
                activeCount = new JTextField();
            }
            {
                jLabel2 = new JLabel();
                jLabel2.setText("activeCount");
            }
            {
                jLabel1 = new JLabel();
                jLabel1.setText("name");
            }
            {
                destroyed = new JTextField();
            }
            {
                jLabel7 = new JLabel();
                jLabel7.setText("isDestroyed");
            }
            {
                parent = new JTextField();
            }
            {
                jLabel5 = new JLabel();
                jLabel5.setText("parent");
            }
            {
                activeGroupCount = new JTextField();
            }
            {
                jLabel3 = new JLabel();
                jLabel3.setText("activeGroupCount");
            }
            {
                toString = new JTextArea();
                toString.setLineWrap(true);
                jLabel8 = new JLabel();
                jLabel8.setText("toString");
                name = new JTextArea();
                name.setLineWrap(true);
            }
            {
                groupTree = new JTree();
                groupTree.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent evt) {
                        swingUtil.invokeAction("groupTreeClick", evt);
                    }
                });
                reloadThreadGroupTree();
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
                destroy = new JButton();
                destroy.setText("destroy");
                interrupt = new JButton();
                interrupt.setText("interrupt");
                list_ = new JButton();
                list_.setText("list");
                destroy.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        swingUtil.invokeAction("destroy", evt);
                    }
                });
                interrupt.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        swingUtil.invokeAction("interrupt", evt);
                    }
                });
                list_.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        swingUtil.invokeAction("list", evt);
                    }
                });
            }
            thisLayout.setVerticalGroup(thisLayout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                            thisLayout
                                    .createParallelGroup()
                                    .addGroup(
                                            GroupLayout.Alignment.LEADING,
                                            thisLayout
                                                    .createSequentialGroup()
                                                    .addGroup(
                                                            thisLayout
                                                                    .createParallelGroup()
                                                                    .addComponent(name, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                                                    .addGroup(
                                                                            GroupLayout.Alignment.LEADING,
                                                                            thisLayout.createSequentialGroup()
                                                                                    .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                                    .addGap(18)))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(
                                                            thisLayout
                                                                    .createParallelGroup()
                                                                    .addGroup(
                                                                            GroupLayout.Alignment.LEADING,
                                                                            thisLayout.createSequentialGroup()
                                                                                    .addComponent(jLabel8, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                                    .addGap(39))
                                                                    .addComponent(toString, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(
                                                            thisLayout
                                                                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                    .addComponent(parent, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel5, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                                            GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(
                                                            thisLayout
                                                                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                    .addComponent(activeCount, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel2, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                                            GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(
                                                            thisLayout
                                                                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                    .addComponent(activeGroupCount, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel3, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                                            GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(
                                                            thisLayout
                                                                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                    .addComponent(maxPriority, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel4, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                                            GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(
                                                            thisLayout
                                                                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                    .addComponent(daemon, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel6, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                                            GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(
                                                            thisLayout
                                                                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                    .addComponent(destroyed, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel7, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                                            GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addGroup(
                                                            thisLayout
                                                                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                    .addComponent(destroy, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                                            GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(interrupt, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                                            GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(list_, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                                                    .addGap(0, 82, Short.MAX_VALUE)
                                                    .addGroup(
                                                            thisLayout
                                                                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                    .addComponent(reloadTime, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                                            GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(getJLabel9(), GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                                            GroupLayout.PREFERRED_SIZE))).addComponent(groupTree, GroupLayout.Alignment.LEADING, 0, 406, Short.MAX_VALUE))
                    .addContainerGap());
            thisLayout.setHorizontalGroup(thisLayout
                    .createSequentialGroup()
                    .addContainerGap(18, 18)
                    .addComponent(groupTree, GroupLayout.PREFERRED_SIZE, 377, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                            thisLayout
                                    .createParallelGroup()
                                    .addGroup(
                                            thisLayout
                                                    .createSequentialGroup()
                                                    .addGroup(
                                                            thisLayout.createParallelGroup()
                                                                    .addComponent(getJLabel9(), GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel7, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel6, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel4, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel3, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel2, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel5, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel8, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(jLabel1, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(
                                                            thisLayout
                                                                    .createParallelGroup()
                                                                    .addComponent(reloadTime, GroupLayout.Alignment.LEADING, 0, 132, Short.MAX_VALUE)
                                                                    .addComponent(destroyed, GroupLayout.Alignment.LEADING, 0, 132, Short.MAX_VALUE)
                                                                    .addGroup(
                                                                            thisLayout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE)
                                                                                    .addComponent(daemon, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE))
                                                                    .addGroup(
                                                                            thisLayout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE)
                                                                                    .addComponent(maxPriority, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE))
                                                                    .addGroup(
                                                                            thisLayout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE)
                                                                                    .addComponent(activeGroupCount, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE))
                                                                    .addGroup(
                                                                            thisLayout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE)
                                                                                    .addComponent(activeCount, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE))
                                                                    .addGroup(
                                                                            thisLayout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE)
                                                                                    .addComponent(parent, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE))
                                                                    .addGroup(
                                                                            thisLayout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE)
                                                                                    .addComponent(toString, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE))
                                                                    .addGroup(
                                                                            thisLayout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE)
                                                                                    .addComponent(name, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(
                                            GroupLayout.Alignment.LEADING,
                                            thisLayout
                                                    .createSequentialGroup()
                                                    .addGroup(
                                                            thisLayout.createParallelGroup()
                                                                    .addComponent(destroy, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(list_, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(interrupt, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE)
                                                    .addGap(0, 0, Short.MAX_VALUE))).addContainerGap());
            this.setSize(684, 401);

            swingUtil.addAction("groupTreeClick", new Action() {
                public void action(EventObject evt) throws Exception {
                    MouseEvent mouse = (MouseEvent) evt;
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) groupTree.getLastSelectedPathComponent();
                    if (node != null) {
                        Object object = node.getUserObject();
                        if (object instanceof ThreadGroup) {
                            currentGroup = (ThreadGroup) object;
                            resetThreadGroup();
                        }
                    }

                    if (mouse.getButton() == 3) {
                        treePopupSet();
                        jPopupMenu1.show(groupTree, mouse.getX(), mouse.getY());
                    }
                }
            }).addAction("reloadTime", new Action() {
                public void action(EventObject evt) throws Exception {
                    reload = (Reload) reloadTime.getSelectedItem();
                    if (reload == Reload.CLOSE) {
                        reloadTime.setEnabled(false);
                    }
                    Log.debug("reload = " + reload);
                }
            }).addAction("destroy", new Action() {
                public void action(EventObject evt) throws Exception {
                    currentGroup.destroy();
                }
            }).addAction("interrupt", new Action() {
                public void action(EventObject evt) throws Exception {
                    currentGroup.interrupt();
                }
            }).addAction("list", new Action() {
                public void action(EventObject evt) throws Exception {
                    currentGroup.list();
                }
            }).addAction("jframeClick", new Action() {
                public void action(EventObject evt) throws Exception {
                    reloadThreadGroupTree();
                }
            });

            new Thread(getMointerThreadGroup(), new Runnable() {
                public void run() {
                    for (;;) {
                        reloadThreadGroupTree();
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
            }, "reload " + this.getClass().getSimpleName() + " tree").start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ThreadGroup currentGroup;
    private ThreadGroup mointerThreadGroup;

    private final static Reload DEFAULT_RELOAD = Reload.FIVE_SECOND;
    private static Reload reload = DEFAULT_RELOAD;

    private void resetThreadGroup() {
        if (currentGroup == null) {
            return;
        }
        destroyed.setText(String.valueOf(currentGroup.isDestroyed()));
        daemon.setText(String.valueOf(currentGroup.isDaemon()));
        parent.setText(currentGroup.getParent() == null ? "null" : currentGroup.getParent().getName());
        maxPriority.setText(String.valueOf(currentGroup.getMaxPriority()));
        activeGroupCount.setText(String.valueOf(currentGroup.activeGroupCount()));
        activeCount.setText(String.valueOf(currentGroup.activeCount()));
        name.setText(currentGroup.getName());
        toString.setText(currentGroup.toString());
    }

    private void reloadThreadGroupTree() {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode();
        DefaultTreeModel model = new DefaultTreeModel(node);
        ThreadGroup rootGroup = null;
        for (ThreadGroup tgroup = Thread.currentThread().getThreadGroup(); //
        tgroup.getParent() != null; //
        rootGroup = tgroup.getParent(), tgroup = tgroup.getParent()) {
        }
        node.setUserObject(rootGroup);
        addChieldNode(rootGroup, node);
        groupTree.setModel(model);
    }

    private void addChieldNode(ThreadGroup threadGroup, DefaultMutableTreeNode node) {
        ThreadGroup[] glist = new ThreadGroup[threadGroup.activeGroupCount()];
        threadGroup.enumerate(glist, true);
        DefaultMutableTreeNode chield = null;
        for (ThreadGroup group : glist) {
            chield = new DefaultMutableTreeNode();
            chield.setUserObject(group);
            addChieldNode(group, chield);
            node.add(chield);
        }
    }

    private ThreadGroup getMointerThreadGroup() {
        if (mointerThreadGroup == null) {
            mointerThreadGroup = new ThreadGroup(ThreadGroupMointerUI.class.getSimpleName());
            mointerThreadGroup.setMaxPriority(5);
        }
        return mointerThreadGroup;
    }

    List<ThreadMointerUI> mointerList = new ArrayList<ThreadMointerUI>();

    private void treePopupSet() {
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) groupTree.getLastSelectedPathComponent();
        if (treeNode == null) {
            return;
        }
        Object treeObject = treeNode.getUserObject();

        jPopupMenu1 = new JPopupMenu();
        JMenuItem menu = null;
        ThreadGroup group = (ThreadGroup) treeObject;
        Thread[] treadList = new Thread[group.activeCount()];
        group.enumerate(treadList, true);
        for (final Thread thread : treadList) {
            menu = new JMenuItem();
            menu.setText(thread.toString());
            menu.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent paramActionEvent) {

                    boolean findExistsUi = false;
                    for (ThreadMointerUI uui : mointerList) {
                        if (uui.getThread() == thread || uui.getReloadThread() == thread) {
                            Log.debug("!!!!! exists thread mointer : " + uui);
                            uui.setVisible(true);
                            findExistsUi = true;
                        }
                    }

                    if (!findExistsUi) {
                        Log.debug("!!!!! create mointer");
                        ThreadMointerUI ui = ThreadMointerUI.newInstance(thread, getMointerThreadGroup()).startWatch().closeDispose();
                        mointerList.add(ui);
                    }

                    reloadThreadGroupTree();
                }
            });

            jPopupMenu1.add(menu);
        }
    }

    private JLabel getJLabel9() {
        if (jLabel9 == null) {
            jLabel9 = new JLabel();
            jLabel9.setText("reload");
        }
        return jLabel9;
    }

    public enum Reload {
        CLOSE(1), //
        ONE_SECOND(1000), //
        TWO_SECOND(2000), //
        THREE_SECOND(3000), //
        FIVE_SECOND(5000), //
        TEN_SECOND(10000), //
        TWENTY_SECOND(20000), //
        ;//

        final long value;

        Reload(long value) {
            this.value = value;
        }

        long apply() {
            reload = this;
            return value;
        }

        public String toString() {
            return name() + "(" + value + ")";
        }
    }
}
