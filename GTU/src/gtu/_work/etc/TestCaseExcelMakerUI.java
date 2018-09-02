package gtu._work.etc;

import gtu.poi.hssf.ExcelUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTableUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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
public class TestCaseExcelMakerUI extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JButton executeBtn;
    private JTable jTable1;
    private JPanel jPanel2;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                TestCaseExcelMakerUI inst = new TestCaseExcelMakerUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public TestCaseExcelMakerUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
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
                    jTabbedPane1.addTab("jPanel1", null, jPanel1, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(573, 364));
                        {
                            DefaultTableModel model = JTableUtil.createModel(false, "作業名", "中文名", "覆蓋率1", "覆蓋率2");
                            model.addRow(new Object[] { "", "", "", "" });
                            jTable1 = new JTable();
                            JTableUtil.defaultSetting(jTable1);
                            jScrollPane1.setViewportView(jTable1);
                            jTable1.setModel(model);
                            jTable1.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    System.out.println("jTable1.mouseClicked, event=" + evt);
                                    List<JMenuItem> menuList = JTableUtil.newInstance(jTable1).getDefaultJMenuItems();
                                    JPopupMenuUtil.newInstance(jTable1).addJMenuItem(menuList).applyEvent(evt).show();
                                }
                            });
                        }
                    }
                }
                final HSSFWorkbook workBook = readFile();
                {
                    jPanel2 = new JPanel();
                    jTabbedPane1.addTab("jPanel2", null, jPanel2, null);
                    {
                        executeBtn = new JButton();
                        jPanel2.add(executeBtn);
                        executeBtn.setText("execute");
                        executeBtn.setPreferredSize(new java.awt.Dimension(117, 45));
                        executeBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                File outputDir = JCommonUtil._jFileChooser_selectDirectoryOnly();
                                if (outputDir == null) {
                                    JCommonUtil._jOptionPane_showMessageDialog_error("dir is not correct!");
                                    return;
                                }

                                HSSFSheet sheet = workBook.getSheetAt(0);
                                HSSFCell cell_RC = sheet.getRow(0).getCell(1);
                                HSSFCell cell_model = sheet.getRow(1).getCell(1);
                                HSSFCell cell_model_Chn = sheet.getRow(1).getCell(5);
                                HSSFCell cell_Controller = sheet.getRow(4).getCell(0);
                                HSSFCell cell_Controller_persent = sheet.getRow(4).getCell(4);
                                HSSFCell cell_Controller_test = sheet.getRow(4).getCell(7);
                                HSSFCell cell_Controller_pic = sheet.getRow(4).getCell(8);
                                HSSFCell cell_Service = sheet.getRow(5).getCell(0);
                                HSSFCell cell_Service_persent = sheet.getRow(5).getCell(4);
                                HSSFCell cell_Service_test = sheet.getRow(5).getCell(7);
                                HSSFCell cell_Service_pic = sheet.getRow(5).getCell(8);

                                DefaultTableModel model = JTableUtil.newInstance(jTable1).getModel();

                                for (int ii = 0; ii < model.getRowCount(); ii++) {
                                    String opName = (String) model.getValueAt(ii, 0);
                                    String opChName = (String) model.getValueAt(ii, 1);
                                    String persent1 = (String) model.getValueAt(ii, 2);
                                    String persent2 = (String) model.getValueAt(ii, 3);
                                    String operation = opName.substring(0, 2).toUpperCase();
                                    opName = opName.replaceAll("[-_]", "").toLowerCase();
                                    opName = opName.substring(0, 1).toUpperCase() + opName.substring(1);
                                    String modelName = "FNM_" + operation + "_FR_" + opName.toUpperCase().substring(2);
                                    String fileName = "FNM_" + operation + "_FR_" + opName.toUpperCase().substring(2);

                                    cell_RC.setCellValue(operation);
                                    cell_model.setCellValue(modelName);
                                    cell_model_Chn.setCellValue(opChName);
                                    cell_Controller.setCellValue(opName + "Controller");
                                    cell_Controller_persent.setCellValue(persent1);
                                    cell_Controller_test.setCellValue(opName + "ControllerTest");
                                    cell_Controller_pic.setCellValue(opName + "ControllerReport.jpg");
                                    cell_Service.setCellValue(opName + "ServiceImpl");
                                    cell_Service_persent.setCellValue(persent2);
                                    cell_Service_test.setCellValue(opName + "ServiceImplTest");
                                    cell_Service_pic.setCellValue(opName + "ServiceImplReport.jpg");

                                    try {
                                        ExcelUtil.getInstance().writeExcel(new File(outputDir, fileName + ".xls"), workBook);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        });
                    }
                }
            }
            pack();
            this.setSize(594, 431);
            loadInitExcel();
        } catch (Exception e) {
            //add your error handling code here
            e.printStackTrace();
        }
    }

    void loadInitExcel() throws Exception {
        File file = new File("C:/Users/gtu001/Desktop/RL-10月底應完成清單.xls");
        HSSFWorkbook book = ExcelUtil.getInstance().readExcel(file);
        Map<String, String> map = new HashMap<String, String>();
        for (int ii = 0; ii < book.getNumberOfSheets(); ii++) {
            HSSFSheet sheet = book.getSheetAt(ii);
            for (int jj = 0; jj <= sheet.getLastRowNum(); jj++) {
                if (sheet.getRow(jj).getCell(0) == null) {
                    continue;
                }
                if (sheet.getRow(jj).getCell(1) == null) {
                    continue;
                }
                String key = ExcelUtil.getInstance().readHSSFCell(sheet.getRow(jj).getCell(0)).toUpperCase();//title num
                String value = ExcelUtil.getInstance().readHSSFCell(sheet.getRow(jj).getCell(1));//title chn
                map.put(key, value);
            }
        }
        File dir = new File("C:/Users/gtu001/Desktop/新增資料夾 (2)");
        Set<String> list = new HashSet<String>();
        for (File f : dir.listFiles()) {
            list.add(f.getName().replaceAll("\\.jpg", "").replaceAll("[a-zA-Z]$", "").toUpperCase());
        }
        JTableUtil util = JTableUtil.newInstance(jTable1);
        StringBuilder sb = new StringBuilder();
        for (Iterator<String> it = list.iterator(); it.hasNext();) {
            String key = it.next();
            if (map.containsKey(key)) {
                util.getModel().addRow(new Object[] { "rl" + key, map.get(key), "", "" });
            } else {
                sb.append(key + "\n");
            }
        }
        if (sb.length() != 0) {
            JCommonUtil._jOptionPane_showMessageDialog_error(sb.toString());
        }
    }

    HSSFWorkbook readFile() throws IOException {
        URL urlFile = this.getClass().getResource("9-2-PDS-RC0C180.xls");
        InputStream inputFile = urlFile.openStream();

        // read entire stream into byte array:
        ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count;
        while ((count = inputFile.read(buffer)) != -1)
            byteOS.write(buffer, 0, count);
        byteOS.close();
        byte[] allBytes = byteOS.toByteArray();

        // create workbook from array:
        InputStream byteIS = new ByteArrayInputStream(allBytes);
        HSSFWorkbook workBook = new HSSFWorkbook(byteIS);
        return workBook;
    }
}
