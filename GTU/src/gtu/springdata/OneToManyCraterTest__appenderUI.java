package gtu.springdata;

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

public class OneToManyCraterTest__appenderUI extends JFrame {

    private JPanel contentPane;
    private JTextField repositoryText;
    private JTextField javaNameText;
    private JTextField refToTypeText;
    private JTextField listNameText;
    private JTextField methodText;
    private JLabel lblRefFromType;
    private JTextField refFromTypeText;
    private JTextField projectFolderText;
    private JLabel lblNewLabel_1;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    OneToManyCraterTest__appenderUI frame = new OneToManyCraterTest__appenderUI();
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
    public OneToManyCraterTest__appenderUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

        JLabel lblNewLabel = new JLabel("repository");
        contentPane.add(lblNewLabel, "2, 2, right, default");

        repositoryText = new JTextField();
        contentPane.add(repositoryText, "4, 2, fill, default");
        repositoryText.setColumns(10);

        repositoryText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                try {
                    String text = StringUtils.trimToEmpty(repositoryText.getText());
                    // MaterialDefinitionPropertyRepository
                    Pattern ptn = Pattern.compile("(.*Property)Repository");
                    Matcher mth = ptn.matcher(text);
                    if (mth.matches()) {
                        String toType = mth.group(1);
                        String fromType = toType.replaceAll("Property$", "");
                        String javaName = toJavaListName(toType) + "Id";
                        String listName = toJavaListName(toType);
                        String method = "findRelation4" + toType;

                        javaNameText.setText(javaName);
                        refToTypeText.setText(toType);
                        refFromTypeText.setText(fromType);
                        listNameText.setText(listName);
                        methodText.setText(method);
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        }));

        lblRefFromType = new JLabel("ref from type");
        contentPane.add(lblRefFromType, "2, 4, right, default");

        refFromTypeText = new JTextField();
        refFromTypeText.setColumns(10);
        contentPane.add(refFromTypeText, "4, 4, fill, default");

        JLabel lblRefType = new JLabel("ref to type");
        contentPane.add(lblRefType, "2, 6, right, default");

        refToTypeText = new JTextField();
        refToTypeText.setColumns(10);
        refToTypeText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                try {
                    String $refToTypeText = StringUtils.trimToEmpty(refToTypeText.getText());
                    if (StringUtils.isNotBlank($refToTypeText)) {
                        String javaName = toJavaListName($refToTypeText) + "Id";
                        String listName = toJavaListName($refToTypeText);
                        String method = "findRelation4" + $refToTypeText;
                        String repository = $refToTypeText + "Repository";

                        javaNameText.setText(javaName);
                        listNameText.setText(listName);
                        methodText.setText(method);
                        repositoryText.setText(repository);
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        }));

        contentPane.add(refToTypeText, "4, 6, fill, default");

        JLabel lblJavaName = new JLabel("java name");
        contentPane.add(lblJavaName, "2, 8, right, default");

        javaNameText = new JTextField();
        javaNameText.setColumns(10);
        contentPane.add(javaNameText, "4, 8, fill, default");

        JLabel lblMethod = new JLabel("method");
        contentPane.add(lblMethod, "2, 10, right, default");

        methodText = new JTextField();
        methodText.setColumns(10);
        contentPane.add(methodText, "4, 10, fill, default");

        JButton btnNewButton = new JButton("GO");
        btnNewButton.addActionListener(new ActionListener() {

            private int findPos(List<String> lst, Pattern ptn) {
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

            private int findPosFromEnd(List<String> lst) {
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

            private String appendBlock(File file, String blockContent) throws IOException {
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

            private boolean compare(String fileName, String compareName) {
                String name = fileName.replaceAll("\\.java", "");
                boolean result = name.equalsIgnoreCase(compareName);
                // System.out.println(result + "\t" + name + " --- " +
                // compareName);
                return result;
            }

            private String fixName(String fileName) {
                return fileName.replaceAll("\\.java", "");
            }

            public void actionPerformed(ActionEvent e) {
                try {
                    OneToManyCreaterTest t = new OneToManyCreaterTest();

                    File dir = JCommonUtil.filePathCheck(projectFolderText.getText(), "專案目錄", true);

                    List<File> fileLst = new ArrayList<File>();
                    FileUtil.searchFilefind(dir, ".*\\.java", fileLst);

                    AtomicReference<String> repository = new AtomicReference<>();
                    AtomicReference<String> refFromType = new AtomicReference<>();
                    AtomicReference<String> refToType = new AtomicReference<>();

                    repository.set(repositoryText.getText());
                    refFromType.set(refFromTypeText.getText());
                    refToType.set(refToTypeText.getText());

                    System.out.println(repository.get());
                    System.out.println(refFromType.get());
                    System.out.println(refToType.get());
                    System.out.println(fileLst.size());

                    File repositoryFile = fileLst.stream().filter(f -> compare(f.getName(), repository.get())).findAny()//
                            .orElseThrow(() -> {
                                throw new RuntimeException("找不到repository");
                            });
                    File refFromTypeFile = fileLst.stream().filter(f -> compare(f.getName(), refFromType.get())).findAny()//
                            .orElseThrow(() -> {
                                throw new RuntimeException("找不到refFromType");
                            });
                    File refToTypeFile = fileLst.stream().filter(f -> compare(f.getName(), refToType.get())).findAny()//
                            .orElseThrow(() -> {
                                throw new RuntimeException("找不到refToType");
                            });

                    repository.set(fixName(repositoryFile.getName()));
                    refFromType.set(fixName(refFromTypeFile.getName()));
                    refToType.set(fixName(refToTypeFile.getName()));

                    String javaName = javaNameText.getText();
                    String listName = listNameText.getText();
                    String methodName = methodText.getText();

                    Map<String, Object> root = new HashMap<String, Object>();
                    root.put("ref_db_column", StringUtilForDb.javaToDbField(javaName));
                    root.put("java_lst_name", listName);
                    root.put("ref_java_type", refToType);
                    root.put("ref_java_name", javaName);
                    root.put("setter", "set" + StringUtils.capitalise(listName));
                    root.put("repository", repository);
                    root.put("method", methodName);

                    t.execute(root);

                    String content1 = appendBlock(refToTypeFile, t.getDetailBlock());
                    String content2 = appendBlock(refFromTypeFile, t.getMasterBlock());
                    String content3 = appendBlock(repositoryFile, t.getRepositoryBlock());

                    System.out.println("-----------------------------------------------------------------------");
                    System.out.println(content1);
                    System.out.println("-----------------------------------------------------------------------");
                    System.out.println(content2);
                    System.out.println("-----------------------------------------------------------------------");
                    System.out.println(content3);
                    System.out.println("-----------------------------------------------------------------------");

                    FileUtil.saveToFile(refToTypeFile, content1, "UTF8");
                    FileUtil.saveToFile(refFromTypeFile, content2, "UTF8");
                    FileUtil.saveToFile(repositoryFile, content3, "UTF8");

                    JCommonUtil._jOptionPane_showMessageDialog_info("成功!");
                } catch (Exception e1) {
                    JCommonUtil.handleException(e1);
                }
            }
        });

        JLabel lblListName = new JLabel("list name");
        contentPane.add(lblListName, "2, 12, right, default");

        listNameText = new JTextField();
        listNameText.setColumns(10);
        contentPane.add(listNameText, "4, 12, fill, default");

        lblNewLabel_1 = new JLabel("baseDir");
        contentPane.add(lblNewLabel_1, "2, 16, right, default");

        projectFolderText = new JTextField();
        contentPane.add(projectFolderText, "4, 16, fill, default");
        projectFolderText.setColumns(10);
        contentPane.add(btnNewButton, "2, 18");
    }

    private String toJavaListName(String formType) {
        formType = StringUtils.uncapitalize(formType);
        if (formType.endsWith("y")) {
            return formType.replaceAll("y$", "ies");
        } else {
            return formType + "s";
        }
    }
}
