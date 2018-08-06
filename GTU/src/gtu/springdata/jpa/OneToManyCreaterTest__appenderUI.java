package gtu.springdata.jpa;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.file.FileUtil;
import gtu.string.StringUtilForDb;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import javax.swing.JCheckBox;

public class OneToManyCreaterTest__appenderUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField refToTypeText;
    private JLabel lblRefFromType;
    private JTextField refFromTypeText;
    private JTextField projectFolderText;
    private JLabel lblNewLabel_1;
    private JPanel panel;
    private JButton resetBtn;
    private JButton btnNewButton;
    private JButton testBtn;
    private JTextField javaListNameText;
    private JLabel lblListName;
    private JLabel lblNewLabel;
    private JTextField dbColumnNameText;
    private JCheckBox cleanerChk;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    OneToManyCreaterTest__appenderUI frame = new OneToManyCreaterTest__appenderUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public OneToManyCreaterTest__appenderUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));

        lblRefFromType = new JLabel("ref from type");
        contentPane.add(lblRefFromType, "2, 2, right, default");

        refFromTypeText = new JTextField();
        refFromTypeText.setColumns(10);
        contentPane.add(refFromTypeText, "4, 2, fill, default");
        refFromTypeText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                try {
                    String $refFromTypeText = StringUtils.trimToEmpty(refFromTypeText.getText());
                    if (StringUtils.isNotBlank($refFromTypeText)) {
                        String resultStr = StringUtilForDb.javaToDbField($refFromTypeText) + "_id";
                        dbColumnNameText.setText(resultStr);
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        }));

        JLabel lblRefType = new JLabel("ref to type");
        contentPane.add(lblRefType, "2, 4, right, default");

        refToTypeText = new JTextField();
        refToTypeText.setColumns(10);
        refToTypeText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                try {
                    String $refToTypeText = StringUtils.trimToEmpty(refToTypeText.getText());
                    if (StringUtils.isNotBlank($refToTypeText)) {
                        String listName = __JpaRelation_PerformExecute.toJavaListName($refToTypeText);
                        javaListNameText.setText(listName);
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        }));

        contentPane.add(refToTypeText, "4, 4, fill, default");

        lblListName = new JLabel("list name");
        contentPane.add(lblListName, "2, 6, right, default");

        javaListNameText = new JTextField();
        contentPane.add(javaListNameText, "4, 6, fill, default");
        javaListNameText.setColumns(10);

        lblNewLabel = new JLabel("db column name");
        contentPane.add(lblNewLabel, "2, 8, right, default");

        dbColumnNameText = new JTextField();
        contentPane.add(dbColumnNameText, "4, 8, fill, default");
        dbColumnNameText.setColumns(10);

        cleanerChk = new JCheckBox("cleaner");
        contentPane.add(cleanerChk, "4, 14");

        lblNewLabel_1 = new JLabel("baseDir");
        contentPane.add(lblNewLabel_1, "2, 16, right, default");

        projectFolderText = new JTextField();
        contentPane.add(projectFolderText, "4, 16, fill, default");
        projectFolderText.setColumns(10);

        btnNewButton = new JButton("執行");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PerformExecuteClz tt = new PerformExecuteClz();
                try {

                    String refFromType1 = refFromTypeText.getText(); //
                    String refToType1 = refToTypeText.getText(); //
                    String javaListName = javaListNameText.getText(); //
                    String dbColumnName = dbColumnNameText.getText(); //
                    boolean doCleanser = cleanerChk.isSelected(); //
                    String projectDir = projectFolderText.getText(); //

                    tt.actionPerformed(refFromType1, refToType1, javaListName, dbColumnName, doCleanser, projectDir);

                    JCommonUtil._jOptionPane_showMessageDialog_info("ok!!");
                } catch (Exception e1) {
                    JCommonUtil.handleException(e1);
                }
            }
        });
        contentPane.add(btnNewButton, "2, 18");

        panel = new JPanel();
        contentPane.add(panel, "4, 18, fill, fill");

        resetBtn = new JButton("reset");
        resetBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refFromTypeText.setText("");
                refToTypeText.setText("");
                javaListNameText.setText("");
                dbColumnNameText.setText("");
            }
        });
        panel.add(resetBtn);

        testBtn = new JButton("test");
        testBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCommonUtil._jOptionPane_showMessageDialog_info("TODO");
            }
        });
        panel.add(testBtn);

        this.setTitle("OneToMany");
    }

    public static class __JpaRelation_PerformExecute {

        protected int findPos(List<String> lst, Pattern ptn) {
            int maxPos = -1;
            for (int ii = 0; ii < lst.size(); ii++) {
                String line = lst.get(ii);
                Matcher mth = ptn.matcher(line);
                if (mth.find()) {
                    maxPos = ii;
                }
            }
            return maxPos;
        }

        protected int findPosFromEnd(List<String> lst) {
            for (int ii = lst.size() - 1; ii > 0; ii--) {
                String val = lst.get(ii);
                if (StringUtils.isBlank(val)) {
                    continue;
                }
                if (StringUtils.trimToEmpty(val).equals("}")) {
                    return ii - 1;
                }
            }
            return -1;
        }

        protected String appendBlock(File file, String blockContent) throws IOException {
            List<String> lst = (FileUtils.readLines(file));

            int maxPos = findPos(lst, Pattern.compile("[↑]+"));
            if (maxPos == -1) {
                maxPos = findPos(lst, Pattern.compile("private\\s(?:String|Long)\\s\\w+\\;"));
            }
            if (maxPos == -1) {
                maxPos = findPosFromEnd(lst);
            }

            System.out.println(file + " -- " + maxPos);

            List<String> a1 = lst.subList(0, maxPos + 1);
            System.out.println("a1 ------------" + StringUtils.join(a1, "\r\n"));
            List<String> a2 = lst.subList(maxPos + 1, lst.size());
            System.out.println("a0 ------------" + blockContent);
            System.out.println("a2 ------------" + StringUtils.join(a2, "\r\n"));

            List<String> all = new ArrayList<String>();
            all.addAll(a1);
            all.add("\r\n");
            all.add(blockContent);
            all.add("\r\n");
            all.addAll(a2);
            return StringUtils.join(all, "\r\n");
        }

        protected boolean compare(String fileName, String compareName) {
            String name = fileName.replaceAll("\\.java", "");
            boolean result = name.equalsIgnoreCase(compareName);
            // System.out.println(result + "\t" + name + " --- " +
            // compareName);
            return result;
        }

        protected String fixName(String fileName) {
            return fileName.replaceAll("\\.java", "");
        }

        protected void doCleanserFile(File file) {
            String content1 = FileUtil.loadFromFile(file, "UTF8");

            StringBuffer sb = new StringBuffer();

            Pattern ptn = Pattern.compile("\\/\\/\\s+relation\\s+↓+(?:(\r|\n|.)*?)\\/\\/\\s+relation\\s+↑+", Pattern.MULTILINE | Pattern.DOTALL);
            Matcher mth = ptn.matcher(content1);
            while (mth.find()) {
                mth.appendReplacement(sb, "");
            }
            mth.appendTail(sb);

            FileUtil.saveToFile(file, sb.toString(), "UTF8");
        }

        protected static String toJavaListName(String formType) {
            formType = StringUtils.uncapitalize(formType);
            if (formType.endsWith("y")) {
                return formType.replaceAll("y$", "ies");
            } else if (formType.endsWith("s")) {
                return formType + "es";
            } else {
                return formType + "s";
            }
        }
    }

    private class PerformExecuteClz extends __JpaRelation_PerformExecute {
        private void actionPerformed(//
                String refFromType1, //
                String refToType1, //
                String javaListName, //
                String dbColumnName, //
                boolean doCleanser, //
                String projectDir) throws IOException {//
            OneToManyCreaterTest t = new OneToManyCreaterTest();

            File dir = JCommonUtil.filePathCheck(projectDir, "專案目錄", true);

            List<File> fileLst = new ArrayList<File>();
            FileUtil.searchFilefind(dir, ".*\\.java", fileLst);

            AtomicReference<String> refFromType = new AtomicReference<>();
            AtomicReference<String> refToType = new AtomicReference<>();

            refFromType.set(refFromType1);
            refToType.set(refToType1);

            System.out.println(refFromType.get());
            System.out.println(refToType.get());
            System.out.println(fileLst.size());

            File refFromTypeFile = fileLst.stream().filter(f -> compare(f.getName(), refFromType.get())).findAny()//
                    .orElseThrow(() -> {
                        throw new RuntimeException("找不到refFromType");
                    });
            File refToTypeFile = fileLst.stream().filter(f -> compare(f.getName(), refToType.get())).findAny()//
                    .orElseThrow(() -> {
                        throw new RuntimeException("找不到refToType");
                    });

            if (doCleanser) {
                doCleanserFile(refFromTypeFile);
            }

            refFromType.set(fixName(refFromTypeFile.getName()));
            refToType.set(fixName(refToTypeFile.getName()));

            // 請勿砍掉 ↓↓↓↓↓↓↓
            Map<String, Object> root = new HashMap<String, Object>();
            root.put("ref_db_column", dbColumnName);
            root.put("ref_entity_detail_type", refToType.get());
            root.put("java_lst_name", javaListName);
            // 請勿砍掉 ↑↑↑↑↑↑↑

            t.execute(root);

            String content2 = appendBlock(refFromTypeFile, t.getMasterBlock());

            System.out.println("-----------------------------------------------------------------------");
            System.out.println(content2);
            System.out.println("-----------------------------------------------------------------------");

            FileUtil.saveToFile(refFromTypeFile, content2, "UTF8");
        }
    }
}
