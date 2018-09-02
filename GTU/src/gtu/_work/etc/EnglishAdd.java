package gtu._work.etc;

import gtu._work.etc.EnglishTester_Diectory.WordInfo;
import gtu.file.FileUtil;
import gtu.swing.util.JCommonUtil;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
public class EnglishAdd extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JTextArea showChineseArea;
    private JCheckBox netChkBox;
    private JButton setFileBtn;
    private JTextArea wordTextArea;
    private JPanel jPanel3;
    private JTextField showwordText;

    EnglishTester_Diectory diectory = new EnglishTester_Diectory();
    
    File currentFile = null;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                EnglishAdd inst = new EnglishAdd();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public EnglishAdd() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            BorderLayout thisLayout = new BorderLayout();
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getContentPane().setLayout(thisLayout);
            this.setPreferredSize(new java.awt.Dimension(400, 211));
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                jTabbedPane1.addChangeListener(new ChangeListener() {
                    public void stateChanged(ChangeEvent evt) {
                        if (jTabbedPane1.getSelectedIndex() == 2) {
                            // XXX
                        }
                    }
                });
                {
                    jPanel1 = new JPanel();
                    FlowLayout jPanel1Layout = new FlowLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("add", null, jPanel1, null);
                    jPanel1.setPreferredSize(new java.awt.Dimension(387, 233));
                    {
                        showwordText = new JTextField();
                        jPanel1.add(showwordText);
                        showwordText.setPreferredSize(new java.awt.Dimension(271, 23));
                        showwordText.addKeyListener(new KeyAdapter() {
                            public void keyPressed(KeyEvent evt) {
                                if(evt.getKeyCode() == KeyEvent.VK_ENTER){
                                    scanWord();
                                }
                            }
                        });
                        showwordText.addFocusListener(new FocusAdapter() {
                            public void focusLost(FocusEvent evt) {
                                scanWord();
                            }
                        });
                    }
                    {
                        netChkBox = new JCheckBox();
                        jPanel1.add(netChkBox);
                        netChkBox.setSelected(true);
                    }
                    {
                        jScrollPane2 = new JScrollPane();
                        jPanel1.add(jScrollPane2);
                        jScrollPane2.setPreferredSize(new java.awt.Dimension(364, 80));
                        {
                            showChineseArea = new JTextArea();
                            jScrollPane2.setViewportView(showChineseArea);
                            showChineseArea.setPreferredSize(new java.awt.Dimension(364, 80));
                        }
                    }
                    {
                        setFileBtn = new JButton();
                        jPanel1.add(setFileBtn);
                        setFileBtn.setText("set file");
                        setFileBtn.setPreferredSize(new java.awt.Dimension(261, 30));
                        setFileBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                File file = JCommonUtil._jFileChooser_selectFileOnly();
                                if (file == null) {
                                    JCommonUtil._jOptionPane_showMessageDialog_error("檔案有問題!");
                                    return;
                                }
                                currentFile = file;

                                StringBuffer sb = new StringBuffer();
                                try {
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                                            new FileInputStream(currentFile), "BIG5"));
                                    for (String line = null; (line = reader.readLine()) != null;) {
                                        sb.append(line + "\r\n");
                                    }
                                    reader.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                wordTextArea.setText(sb.toString());
                            }
                        });
                    }
                }
                {
                    jPanel3 = new JPanel();
                    BorderLayout jPanel3Layout = new BorderLayout();
                    jPanel3.setLayout(jPanel3Layout);
                    jTabbedPane1.addTab("word", null, jPanel3, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel3.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(387, 224));
                        {
                            wordTextArea = new JTextArea();
                            jScrollPane1.setViewportView(wordTextArea);
                        }
                    }
                }
            }
            
            setDefaultSave();
            
            pack();
            this.setSize(400, 211);
        } catch (Exception e) {
            // add your error handling code here
            e.printStackTrace();
        }
    }

    void setDefaultSave() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(null, "確定關閉?") == JOptionPane.YES_OPTION) {
                    String words = wordTextArea.getText();
                    if (StringUtils.isNotBlank(words) && currentFile != null) {
                        FileUtil.saveToFile(currentFile, words, "BIG5");
                    }
                    
                    EnglishAdd.this.setVisible(false);
                    EnglishAdd.this.dispose();
                }
            }
        });
    }
    
    String currentWord = null;
    
    void scanWord(){
        String word = StringUtils.defaultString(showwordText.getText());
        word = StringUtils.trim(word);
        if(StringUtils.isBlank(word) || StringUtils.equals(currentWord, word)){
            //showChineseLabel.setText("");
            return;
        }
        
        if(netChkBox.isSelected()){
            currentWord = word;
            
            WordInfo wordInfo = null;
            try{
                wordInfo = diectory.parseToWordInfo(word);
            }catch(Exception ex){
                showChineseArea.setText(word + " 找不到!!!");
                return;
            }
            
            showChineseArea.setText(word + "  " + wordInfo.getPronounce() + "\n" + wordInfo.getMeaning());
            showwordText.setText("");
        }else{
            showChineseArea.setText(word + "\n" + "=> 免查詢新增");
            showwordText.setText("");
        }
        
        addWordTable(word);
    }
    

    void addWordTable(String word) {
        try {
            StringBuffer sb = new StringBuffer(wordTextArea.getText());

            List<String> contains = new ArrayList<String>();
            BufferedReader reader = new BufferedReader(new StringReader(sb.toString()));
            try {
                for (String line = null; (line = reader.readLine()) != null;) {
                    contains.add(line);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (contains.contains(word)) {
                return;
            }

            if (sb.length() != 0 && sb.charAt(sb.length() - 1) != '\n') {
                sb.append("\r\n");
            }
            sb.append(word + "\r\n");
            wordTextArea.setText(sb.toString());

            if (sb.length() > 0 && currentFile != null) {
                FileUtil.saveToFile(currentFile, sb.toString(), "BIG5");
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }
}
