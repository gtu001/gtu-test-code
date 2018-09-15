package gtu._work.ui;

import gtu.string.StringUtilForDb;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

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
public class DbFieldJavaFieldUI extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane4;
    private JTextArea getSetRelArea;
    private JPanel jPanel4;
    private ButtonGroup buttonGroup1;
    private JRadioButton javaToDbRadio;
    private JRadioButton dbToJavaRadio;
    private JPanel jPanel3;
    private JScrollPane jScrollPane3;
    private JScrollPane jScrollPane1;
    private JTextArea replaceBeforeArea;
    private JTextArea replaceAfterArea;
    private JPanel jPanel2;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DbFieldJavaFieldUI inst = new DbFieldJavaFieldUI();
                inst.setLocationRelativeTo(null);
                 gtu.swing.util.JFrameUtil.setVisible(true,inst);
            }
        });
    }

    public DbFieldJavaFieldUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            BorderLayout thisLayout = new BorderLayout();
            getContentPane().setLayout(thisLayout);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel1 = new JPanel();
                    BorderLayout jPanel1Layout = new BorderLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("原始資料", null, jPanel1, null);
                    {
                        jScrollPane3 = new JScrollPane();
                        jPanel1.add(jScrollPane3, BorderLayout.CENTER);
                        jScrollPane3.setPreferredSize(new java.awt.Dimension(379, 209));
                        {
                            replaceBeforeArea = new JTextArea();
                            jScrollPane3.setViewportView(replaceBeforeArea);
                        }
                    }
                    {
                        jPanel3 = new JPanel();
                        jPanel1.add(jPanel3, BorderLayout.NORTH);
                        jPanel3.setPreferredSize(new java.awt.Dimension(379, 28));
                        {
                            dbToJavaRadio = new JRadioButton();
                            jPanel3.add(dbToJavaRadio);
                            dbToJavaRadio.setText("DB->Java");
                        }
                        {
                            javaToDbRadio = new JRadioButton();
                            jPanel3.add(javaToDbRadio);
                            javaToDbRadio.setText("Java->DB");
                        }
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("轉換結果", null, jPanel2, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel2.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(379, 233));
                        {
                            jScrollPane2 = new JScrollPane();
                            jScrollPane1.setViewportView(jScrollPane2);
                            jScrollPane2.setPreferredSize(new java.awt.Dimension(376, 230));
                            {
                                replaceAfterArea = new JTextArea();
                                jScrollPane2.setViewportView(replaceAfterArea);
                            }
                        }
                    }
                }
                {
                    jPanel4 = new JPanel();
                    BorderLayout jPanel4Layout = new BorderLayout();
                    jPanel4.setLayout(jPanel4Layout);
                    jTabbedPane1.addTab("setter", null, jPanel4, null);
                    {
                        jScrollPane4 = new JScrollPane();
                        jPanel4.add(jScrollPane4, BorderLayout.CENTER);
                        jScrollPane4.setPreferredSize(new java.awt.Dimension(379, 233));
                        {
                            getSetRelArea = new JTextArea();
                            jScrollPane4.setViewportView(getSetRelArea);
                        }
                    }
                }
            }
            buttonGroup1 = new ButtonGroup();
            buttonGroup1.add(dbToJavaRadio);
            dbToJavaRadio.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    System.out.println("dbToJavaRadio.actionPerformed, event=" + evt);
                    //TODO add your code for dbToJavaRadio.actionPerformed
                    execute(DbJava.DB_TO_JAVA);
                }
            });
            buttonGroup1.add(javaToDbRadio);
            javaToDbRadio.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    System.out.println("javaToDbRadio.actionPerformed, event=" + evt);
                    //TODO add your code for javaToDbRadio.actionPerformed
                    execute(DbJava.JAVA_TO_DB);
                }
            });
            pack();
            setSize(400, 300);
        } catch (Exception e) {
            //add your error handling code here
            e.printStackTrace();
        }
    }

    public void execute(DbJava dbJava) {
        replaceAfterArea.setText(dbJava.operation(replaceBeforeArea.getText()));
        getSetRelArea.setText(DbJava.GETTER_SETTER.operation(replaceBeforeArea.getText()));
    }

    private static enum DbJava {
        DB_TO_JAVA() {
            @Override
            public String operation(String content) {
                content = StringUtils.defaultString(content);
                StringBuffer sb = new StringBuffer();
                Pattern ptn = Pattern.compile("[\\w-_]*");
                StringTokenizer tok = new StringTokenizer(content, "\n");
                while (tok.hasMoreElements()) {
                    String value = (String) tok.nextElement();
                    Matcher matcher = ptn.matcher(value);
                    while (matcher.find()) {
                        if (StringUtils.isNotBlank(matcher.group())) {
                            sb.append("private String ");
                            sb.append(StringUtilForDb.dbFieldToJava(matcher.group()));
                            sb.append("; //");
                            String tmp = value.substring(0, matcher.start()) + value.substring(matcher.end());
                            sb.append(tmp.trim());
                            sb.append("\n");
                        }
                    }
                }
                return sb.toString();
            }
        },
        JAVA_TO_DB() {
            @Override
            public String operation(String content) {
                content = StringUtils.defaultString(content);
                StringBuffer sb = new StringBuffer();
                Pattern ptn = Pattern.compile("[\\w-_]*");
                Matcher matcher = ptn.matcher(content);
                while (matcher.find()) {
                    matcher.appendReplacement(sb, StringUtilForDb.javaToDbField(matcher.group()));
                }
                matcher.appendTail(sb);
                return sb.toString();
            }
        },
        GETTER_SETTER() {
            @Override
            public String operation(String content) {
                content = StringUtils.defaultString(content);
                StringBuffer sb = new StringBuffer();
                Pattern ptn = Pattern.compile("[\\w]*");
                StringTokenizer tok = new StringTokenizer(content, "\n");
                String field = null;

                while (tok.hasMoreElements()) {
                    String value = (String) tok.nextElement();
                    Matcher matcher = ptn.matcher(value);
                    String comment = null;
                    int pos = -1;
                    if ((pos = value.indexOf("//")) != -1) {
                        comment = value.substring(pos);
                    }
                    while (matcher.find()) {
                        if (StringUtils.isNotBlank(field = matcher.group()) && !ArrayUtils.contains(IGNORE_GETTER_SETTER, field)) {
                            field = field.substring(0, 1).toUpperCase() + field.substring(1);
                            sb.append("entity1.set" + field + "(StringUtils.trim(entity2.get" + field + "())); " + comment);
                            sb.append("\n");
                        }
                    }
                }
                return sb.toString();
            }
        };

        static String[] IGNORE_GETTER_SETTER = //
        { "private", "int", "Integer", "List", "Map", "Set", "String", "Boolean", "boolean", "Double", "double", "Float", "float", "Date", "Long", "long", "Byte", "byte", "Short", "short" };

        DbJava() {
        }

        public abstract String operation(String content);
    }
}
