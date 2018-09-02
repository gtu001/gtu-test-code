package gtu._work.etc;
import gtu.number.RandomUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JMouseEventUtil;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class HotnoteMakerUI extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JTextField checkListTitle;
    private JTextArea checkListArea;

    /**
    * Auto-generated main method to display this JFrame
    */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                HotnoteMakerUI inst = new HotnoteMakerUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }
    
    public HotnoteMakerUI() {
        super();
        initGUI();
    }
    
    private void initGUI() {
        try {
            ToolTipManager.sharedInstance().setInitialDelay(0);
            BorderLayout thisLayout = new BorderLayout();
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getContentPane().setLayout(thisLayout);
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel1 = new JPanel();
                    BorderLayout jPanel1Layout = new BorderLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("hott notes - checklist", null, jPanel1, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(612, 348));
                        {
                            checkListArea = new JTextArea();
                            jScrollPane1.setViewportView(checkListArea);
                            checkListArea.addMouseListener(new MouseAdapter() {
                                
                                String randomColor(){
                                    StringBuilder sb = new StringBuilder().append("#");
                                    for(int ii = 0 ; ii < 6 ; ii ++){
                                        sb.append(RandomUtil.randomChar('a','b','c','d','f', '0','1','2','3','4','5','6','7','8','9'));
                                    }
                                    return sb.toString();
                                }
                                
                                void saveXml(Document document, File file){
                                    OutputFormat format = OutputFormat.createPrettyPrint();
                                    format.setEncoding("utf-16");
                                    XMLWriter writer = null;
                                    try {
                                        writer = new XMLWriter(new FileWriter(file), format);
                                        writer.write(document);
                                    } catch (IOException e) {
                                        JCommonUtil.handleException(e);
                                    } finally {
                                        if (writer != null) {
                                            try {
                                                writer.close();
                                            } catch (IOException e) {
                                                JCommonUtil.handleException(e);
                                            }
                                        }
                                    }
                                }
                                
                                public void mouseClicked(MouseEvent evt) {
                                    if(!JMouseEventUtil.buttonLeftClick(2, evt)){
                                        return;
                                    }
                                    
                                    if(StringUtils.isEmpty(checkListArea.getText())){
                                        JCommonUtil._jOptionPane_showMessageDialog_error("checklist area is empty!");
                                        return;
                                    }
                                    
                                    File file = JCommonUtil._jFileChooser_selectFileOnly_saveFile();
                                    if(file == null){
                                        JCommonUtil._jOptionPane_showMessageDialog_error("file is not correct!");
                                        return;
                                    }
                                    
                                    //XXX
                                    StringTokenizer tok = new StringTokenizer(checkListArea.getText(), "\t\n\r\f");
                                    List<String> list = new ArrayList<String>();
                                    String tmp = null;
                                    for(;tok.hasMoreElements();){
                                        tmp = ((String)tok.nextElement()).trim();
                                        System.out.println(tmp);
                                        list.add(tmp);
                                    }
                                    //XXX
                                    
                                    Document document = DocumentHelper.createDocument();
                                    Element rootHot = document.addElement("hottnote");
                                    rootHot.addAttribute("creationtime", new Timestamp(System.currentTimeMillis()).toString());
                                    rootHot.addAttribute("lastmodified", new Timestamp(System.currentTimeMillis()).toString());
                                    rootHot.addAttribute("type", "checklist");
                                    //appearence
                                    Element appearenceE = rootHot.addElement("appearence");
                                    appearenceE.addAttribute("alpha", "204");
                                    Element fontE = appearenceE.addElement("font");
                                    fontE.addAttribute("face", "Default");
                                    fontE.addAttribute("size", "0");
                                    Element styleE = appearenceE.addElement("style");
                                    styleE.addElement("bg2color").addAttribute("color", randomColor());
                                    styleE.addElement("bgcolor").addAttribute("color", randomColor());
                                    styleE.addElement("textcolor").addAttribute("color", randomColor());
                                    styleE.addElement("titlecolor").addAttribute("color", randomColor());
                                    //behavior
                                    rootHot.addElement("behavior");
                                    //content
                                    Element contentE = rootHot.addElement("content");
                                    Element checklistE = contentE.addElement("checklist");
                                    for(String val : list){
                                        checklistE.addElement("item").addCDATA(val);
                                    }
                                    //desktop
                                    Element desktopE = rootHot.addElement("desktop");
                                    desktopE.addElement("position").addAttribute("x", RandomUtil.numberStr(3)).addAttribute("y", RandomUtil.numberStr(3));
                                    desktopE.addElement("size").addAttribute("height", "200").addAttribute("width", "200");
                                    //title
                                    Element titleE = rootHot.addElement("title");
                                    titleE.addCDATA(StringUtils.defaultIfEmpty(checkListTitle.getText(), DateFormatUtils.format(System.currentTimeMillis(), "dd/MM/yyyy")));
                                    
                                    if(!file.getName().toLowerCase().endsWith(".hottnote")){
                                        file = new File(file.getParentFile(), file.getName() + ".hottnote");
                                    }
                                    
                                    saveXml(document, file);
                                    JCommonUtil._jOptionPane_showMessageDialog_info("completed!\n" + file);
                                }
                            });
                        }
                    }
                    {
                        checkListTitle = new JTextField();
                        checkListTitle.setToolTipText("title");
                        jPanel1.add(checkListTitle, BorderLayout.NORTH);
                    }
                }
            }
            pack();
            this.setSize(633, 415);
        } catch (Exception e) {
            //add your error handling code here
            e.printStackTrace();
        }
    }

}
