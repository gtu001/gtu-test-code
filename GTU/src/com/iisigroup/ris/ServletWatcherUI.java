package com.iisigroup.ris;

import gtu.reflect.ToStringUtil;
import gtu.swing.util.JFileChooserUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.EventObject;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

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
public class ServletWatcherUI extends javax.swing.JFrame {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JComboBox serverMode;
    private JComboBox logbackLevel;
    private JTextField loggerName;
    private JButton browserSelf;
    private JComboBox logbackRootLevel;
    private JTextArea messages;
    private JButton requestBtn;
    private JButton spring;
    private JButton logback;

    private InitServlet initServlet;

    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        ServletWatcherUI inst = new ServletWatcherUI();
        inst.setVisible(true);
    }

    public ServletWatcherUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            final SwingActionUtil swingUtil = (SwingActionUtil) SwingActionUtil
                    .newInstance(this);
            BorderLayout thisLayout = new BorderLayout();
            getContentPane().setLayout(thisLayout);
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            {
                jPanel1 = new JPanel();
                GroupLayout jPanel1Layout = new GroupLayout((JComponent) jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                getContentPane().add(jPanel1, BorderLayout.NORTH);
                jPanel1.setPreferredSize(new java.awt.Dimension(576, 91));
                {
                    logback = new JButton();
                    logback.setText("logback");
                    logback.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            swingUtil.invokeAction("logback", evt);
                        }
                    });
                }
                {
                    spring = new JButton();
                    spring.setText("spring");
                    spring.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            swingUtil.invokeAction("spring", evt);
                        }
                    });
                }
                {
                    requestBtn = new JButton();
                    requestBtn.setText("request");
                    requestBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            swingUtil.invokeAction("request", evt);
                        }
                    });
                }
                {
                    browserSelf = new JButton();
                    browserSelf.setText("path");
                    browserSelf.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            swingUtil.invokeAction("path", evt);
                        }
                    });
                }
                {
                    ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(new Boolean[] { true, false });
                    serverMode = new JComboBox();
                    serverMode.setModel(jComboBox1Model);
                    serverMode.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            swingUtil.invokeAction("serverModeChange", evt);
                        }
                    });
                }
                {
                    DefaultComboBoxModel jComboBox1Model = new DefaultComboBoxModel();
                    for (LogbackLevel l : LogbackLevel.values()) {
                        jComboBox1Model.addElement(l);
                    }
                    logbackLevel = new JComboBox();
                    logbackLevel.setModel(jComboBox1Model);
                    logbackLevel.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            swingUtil.invokeAction("logbackLevelChange", evt);
                        }
                    });
                }
                {
                    loggerName = new JTextField();
                }
                {
                    DefaultComboBoxModel logbackRootLevelModel = new DefaultComboBoxModel();
                    for (LogbackLevel l : LogbackLevel.values()) {
                        logbackRootLevelModel.addElement(l);
                    }
                    logbackRootLevel = new JComboBox();
                    logbackRootLevel.setModel(logbackRootLevelModel);
                    logbackRootLevel.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            swingUtil.invokeAction("logbackRootLevelChange", evt);
                        }
                    });
                }
                jPanel1Layout.setHorizontalGroup(jPanel1Layout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                                jPanel1Layout
                                        .createParallelGroup()
                                        .addGroup(
                                                GroupLayout.Alignment.LEADING,
                                                jPanel1Layout
                                                        .createSequentialGroup()
                                                        .addComponent(logbackRootLevel, GroupLayout.PREFERRED_SIZE,
                                                                123, GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(logback, GroupLayout.PREFERRED_SIZE, 79,
                                                                GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 1,
                                                                Short.MAX_VALUE)
                                                        .addComponent(spring, GroupLayout.PREFERRED_SIZE, 79,
                                                                GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(requestBtn, GroupLayout.PREFERRED_SIZE, 79,
                                                                GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(browserSelf, GroupLayout.PREFERRED_SIZE, 79,
                                                                GroupLayout.PREFERRED_SIZE))
                                        .addGroup(
                                                GroupLayout.Alignment.LEADING,
                                                jPanel1Layout
                                                        .createSequentialGroup()
                                                        .addComponent(loggerName, GroupLayout.PREFERRED_SIZE, 251,
                                                                GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(logbackLevel, GroupLayout.PREFERRED_SIZE, 123,
                                                                GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(serverMode, GroupLayout.PREFERRED_SIZE, 93,
                                                                GroupLayout.PREFERRED_SIZE)
                                                        .addGap(0, 0, Short.MAX_VALUE))).addContainerGap(85, 85));
                jPanel1Layout.setVerticalGroup(jPanel1Layout
                        .createSequentialGroup()
                        .addContainerGap(17, Short.MAX_VALUE)
                        .addGroup(
                                jPanel1Layout
                                        .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(logbackLevel, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(loggerName, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(serverMode, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(
                                jPanel1Layout
                                        .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(logbackRootLevel, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(logback, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(spring, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(requestBtn, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(browserSelf, GroupLayout.Alignment.BASELINE,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE)).addContainerGap());
            }
            {
                jScrollPane1 = new JScrollPane();
                getContentPane().add(jScrollPane1, BorderLayout.CENTER);
                jScrollPane1.setPreferredSize(new java.awt.Dimension(576, 270));
                {
                    messages = new JTextArea();
                    jScrollPane1.setViewportView(messages);
                    messages.setText("");
                    messages.setFont(new java.awt.Font("Consolas", 0, 10));
                }
            }
            this.setSize(592, 356);
            setLocationRelativeTo(null);

            swingUtil.addAction("logback", new Action() {
                public void action(EventObject evt) throws Exception {
                    showThreadInfo();
                    File file = JFileChooserUtil.newInstance().selectFileOnly().showOpenDialog()
                            .getApproveSelectedFile();
                    if (file != null) {
                        initServlet.resetSlf4jConfig(file);
                    }
                }
            });
            swingUtil.addAction("spring", new Action() {
                public void action(EventObject evt) throws Exception {
                    showThreadInfo();
                    initServlet.message = new StringBuilder();
                    initServlet.showSpringInfo(initServlet.request, initServlet.response);
                    messages.setText(initServlet.message.toString());
                }
            });
            swingUtil.addAction("request", new Action() {
                public void action(EventObject evt) throws Exception {
                    showThreadInfo();
                    initServlet.message = new StringBuilder();
                    initServlet.showRequestSession(initServlet.request, initServlet.response);
                    messages.setText(initServlet.message.toString());
                }
            });
            swingUtil.addAction("logbackRootLevelChange", new Action() {
                public void action(EventObject evt) throws Exception {
                    showThreadInfo();
                    initServlet.setSlf4jRootLevel(((LogbackLevel) logbackRootLevel.getSelectedItem()).level);
                }
            });
            swingUtil.addAction("logbackLevelChange", new Action() {
                public void action(EventObject evt) throws Exception {
                    showThreadInfo();
                    String loggerText = loggerName.getText();
                    initServlet.setSlf4jLevel(loggerText, ((LogbackLevel) logbackLevel.getSelectedItem()).level);
                }
            });
            swingUtil.addAction("serverModeChange", new Action() {
                public void action(EventObject evt) throws Exception {
                    showThreadInfo();
                    initServlet.setServerMode(((Boolean) serverMode.getSelectedItem()), initServlet.request,
                            initServlet.response);
                }
            });
            swingUtil.addAction("path", new Action() {
                public void action(EventObject evt) throws Exception {
                    showThreadInfo();
                    messages.setText(ToStringUtil.toString(ServletWatcherUI.class.getResource("")));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    enum LogbackLevel {
        OFF(Level.OFF) {
        },
        ERROR(Level.ERROR) {
        },
        WARN(Level.WARN) {
        },
        INFO(Level.INFO) {
        },
        DEBUG(Level.DEBUG) {
        },
        TRACE(Level.TRACE) {
        },
        ALL(Level.ALL) {
        },
        ;
        final Level level;

        LogbackLevel(Level level) {
            this.level = level;
        }
    }

    void showThreadInfo() {
        String currentThread = Thread.currentThread().toString();
        this.setTitle(currentThread);
        log.debug(currentThread);
    }

    public void setInitServlet(InitServlet initServlet) {
        this.initServlet = initServlet;
    }
}
