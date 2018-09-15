package gtu.applet;

import gtu.date.DateUtil;
import gtu.file.FileUtil;
import gtu.string.StringUtil_;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * 將清單中的程式檔案 匯出至目的資料夾
 * 
 * @author gtu 2009/02/02
 * 
 */
public class UpdateCode extends Frame implements MouseListener, WindowListener, ItemListener {

    private final static String PROGRAM = "program";
    private final static String EXPORT = "export";
    private final static String EXPORT_LIST = "export_list";

    private final static String EXPORT_IN_ROOT = "export in root";
    private final static String EXPORT_BY_TREE = "export by tree";

    private <T> T appObject(T o, Rectangle reg, String name, String value) {
        if (o instanceof Label) {
            ((Label) o).setText(value);
            ((Label) o).setName(name);
            ((Label) o).setBounds(reg);
            ((Label) o).addMouseListener(this);
        } else if (o instanceof TextField) {
            ((TextField) o).setText(value);
            ((TextField) o).setName(name);
            ((TextField) o).setBounds(reg);
            ((TextField) o).addMouseListener(this);
        } else if (o instanceof Button) {
            ((Button) o).setLabel(value);
            ((Button) o).setName(name);
            ((Button) o).setBounds(reg);
            ((Button) o).addMouseListener(this);
        } else if (o instanceof TextArea) {
            ((TextArea) o).setText(value);
            ((TextArea) o).setName(name);
            ((TextArea) o).setBounds(reg);
            ((TextArea) o).addMouseListener(this);
        } else if (o instanceof Choice) {
            String[] values = value.split(",");
            for (int ii = 0; ii < values.length; ii++)
                ((Choice) o).addItem(values[ii]);
            ((Choice) o).setName(name);
            ((Choice) o).setBounds(reg);
            ((Choice) o).addMouseListener(this);
            ((Choice) o).addItemListener(this);
        }
        return o;
    }

    private Choice[] addClock(int x, int y) {
        Choice[] clk = new Choice[2];
        for (int ii = 0; ii < 2; ii++) {
            clk[ii] = new Choice();
            clk[ii].setName("clock" + ii);
            clk[ii].setBounds(x + ii * 50, y, 50, 25);
            clk[ii].addMouseListener(this);
            clk[ii].addItemListener(this);
        }
        for (int ii = 0; ii < 24; ii++) {
            String value = String.valueOf(ii);
            clk[0].addItem(value.length() == 1 ? "0" + value : value);
        }
        for (int ii = 0; ii < 60; ii++) {
            String value = String.valueOf(ii);
            clk[1].addItem(value.length() == 1 ? "0" + value : value);
        }
        return clk;
    }

    public UpdateCode() {
        this.setLayout(null);
        int posY = 50;
        this.add(appObject(new Label(), new Rectangle(25, posY, 100, 25), "L1", "File dir:"));// 程式所在目錄
        this.add(appObject(new TextField(), new Rectangle(125, posY, 300, 25), "T1", ""));
        posY += 25;
        this.add(appObject(new Label(), new Rectangle(25, posY, 100, 25), "L2", "Export dir:"));// 匯出目錄
        this.add(appObject(new TextField(), new Rectangle(125, posY, 300, 25), "T2", ""));
        posY += 25;
        this.add(appObject(new Label(), new Rectangle(25, posY, 100, 25), "L3", "Export list:"));// 匯出程式清單檔
        this.add(appObject(new TextField(), new Rectangle(125, posY, 300, 25), "T3", ""));
        this.add(appObject(new Button(), new Rectangle(425, posY, 50, 25), "B1", "Load file"));// 讀取
        posY += 25;
        this.add(appObject(new Label(), new Rectangle(25, posY, 100, 25), "L5", "Last modiy:"));// 檔案修改日期
        this.add(appObject(new TextField(), new Rectangle(125, posY, 100, 25), "T4", DateUtil.getCurrentDate(true)));
        this.add(appObject(new TextField(), new Rectangle(225, posY, 100, 25), "T7", DateUtil.getCurrentDate(true)));
        this.add(appObject(new Button(), new Rectangle(425, posY, 50, 25), "B3", "Load file"));// 讀取
        posY += 25;
        this.add(appObject(new Label(), new Rectangle(25, posY, 100, 25), "L6", "File key word:"));// 檔名關鍵字
        this.add(appObject(new TextField(), new Rectangle(125, posY, 300, 25), "T5", ""));
        this.add(appObject(new Button(), new Rectangle(425, posY, 50, 25), "B4", "Load file"));// 讀取
        posY += 25;
        this.add(appObject(new Label(), new Rectangle(25, posY, 100, 25), "L7", "Sub file name:"));// 副檔名(N)
        this.add(appObject(new TextField(), new Rectangle(125, posY, 300, 25), "T6", ""));
        posY += 25;
        this.add(appObject(new Label(), new Rectangle(25, posY, 100, 25), "L8", "Export mode:"));// 匯出方式
        this.add(appObject(new Choice(), new Rectangle(125, posY, 100, 25), "C1", EXPORT_IN_ROOT + "," + EXPORT_BY_TREE));// "匯出於根目錄,按照目錄格式匯出"
        posY += 25;
        this.add(appObject(new Label(), new Rectangle(25, posY += 25, 100, 25), "L4", "Export list:"));// 匯出程式清單
        this.add(appObject(new TextArea(), new Rectangle(25, posY += 25, 450, 300), "A1", ""));
        this.add(appObject(new Button(), new Rectangle(25, posY += 300, 450, 25), "B2", "Export"));// 匯出
        posY += 25;
        // java.awt.Label[L4,25,125,100x25,align=left,text=匯出程式清單:]
        this.setBounds(100, 50, 500, posY += 25);
        this.setBackground(Color.LIGHT_GRAY);
        this.addWindowListener(this);
        this.addMouseListener(this);
        this.setTitle("更新程式匯出工具");
         gtu.swing.util.JFrameUtil.setVisible(true,this);
    }

    public void itemStateChanged(java.awt.event.ItemEvent arg0) {
    }

    private Component searchComponent(Component[] com, String name) {
        for (int ii = 0; ii < com.length; ii++) {
            if (com[ii].getName().equals(name)) {
                return com[ii];
            }
        }
        return null;
    }

    private boolean errMsg(String field, String msg) {
        if (field == null && field.equals("")) {
            JOptionPane.showMessageDialog(null, field + msg, "error", JOptionPane.ERROR_MESSAGE, null);
            return false;
        }
        return true;
    }

    private void errMsg(String msg) {
        JOptionPane.showMessageDialog(null, msg, "error", JOptionPane.ERROR_MESSAGE, null);
    }

    private ArrayList<File> fileList = new ArrayList<File>();

    public void mouseClicked(java.awt.event.MouseEvent arg0) {
        String clickObj = ((Component) arg0.getSource()).getName();
        System.out.println("敲擊元件:" + clickObj + "==>" + searchComponent(this.getComponents(), clickObj));

        String program = ((TextField) searchComponent(this.getComponents(), "T1")).getText(); // 程式所在目錄
        String export = ((TextField) searchComponent(this.getComponents(), "T2")).getText(); // 匯出目錄
        String[] subName = ((TextField) searchComponent(this.getComponents(), "T6")).getText().split(","); // 不顯示的副檔名

        ((TextField) searchComponent(this.getComponents(), "T1")).setText(program);
        ((TextField) searchComponent(this.getComponents(), "T2")).setText(export);

        if (clickObj.equals("B1")) { // 讀取 檔案清單
            String filename = ((TextField) searchComponent(this.getComponents(), "T3")).getText();
            File fff = new File(filename);
            if (fff.exists()) {
                List<File> total = new ArrayList<File>();
                FileUtil.traceFileList(new File(program), total);
                StringBuffer sb = new StringBuffer();
                try {
                    Scanner scn = new Scanner(fff);
                    while (scn.hasNextLine()) {
                        String tmp = scn.nextLine();
                        for (int ii = 0; ii < total.size(); ii++)
                            if (total.get(ii).getName().indexOf(tmp) != -1)
                                fileList.add(total.get(ii));
                        sb.append(tmp + "\r\n");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                ((TextArea) searchComponent(this.getComponents(), "A1")).setText(sb.toString());
            } else {
                errMsg(filename + "檔案不存在");
            }
        }

        if (clickObj.equals("B3")) { // 讀取 日期
            String time1 = ((TextField) searchComponent(this.getComponents(), "T4")).getText().replaceAll("/", "")
                    + "000000";
            String time2 = ((TextField) searchComponent(this.getComponents(), "T7")).getText().replaceAll("/", "")
                    + "235959";
            FileUtil.traceFileList(new File(program), fileList);
            for (int ii = 0; ii < fileList.size(); ii++) {
                boolean bRemove = false;
                if (fileList.get(ii).getName().indexOf(".") == -1) {
                    bRemove = true;
                }
                bRemove = getIndexOfInList(fileList.get(ii), subName);
                System.out.println(fileList.get(ii) + "......" + bRemove);
                if (bRemove == true || fileList.get(ii).getName().indexOf(".") == -1) {
                    fileList.remove(ii);
                    ii--;
                }
            }
            StringBuffer sb = new StringBuffer();
            for (File file : fileList)
                sb.append(file.getName() + "\r\n");
            ((TextArea) searchComponent(this.getComponents(), "A1")).setText(sb.toString());
            JOptionPane.showMessageDialog(null, "操作完成", "結果", JOptionPane.NO_OPTION, null);
        }

        if (clickObj.equals("B4")) { // 讀取 檔名關鍵字
            String[] keyword = ((TextField) searchComponent(this.getComponents(), "T5")).getText().split(",");
            FileUtil.traceFileList(new File(program), fileList);
            StringBuffer sb = new StringBuffer();
            for (int ii = 0; ii < fileList.size(); ii++) {
                boolean bHaveInList = false;
                boolean bRemove = false;
                bHaveInList = getIndexOfInList(fileList.get(ii), keyword);
                bRemove = getIndexOfInList(fileList.get(ii), subName);
                if (bHaveInList == false || bRemove == true || fileList.get(ii).getName().indexOf(".") == -1) {
                    fileList.remove(ii);
                    ii--;
                } else {
                    sb.append(fileList.get(ii).getName() + "\r\n");
                }
            }
            ((TextArea) searchComponent(this.getComponents(), "A1")).setText(sb.toString());
        }

        if (clickObj.equals("B2")) { // 匯出
            String c1 = ((Choice) searchComponent(this.getComponents(), "C1")).getSelectedItem();
            boolean chkFolder = false;
            if (c1.equals(EXPORT_IN_ROOT)) {
                chkFolder = false;
            } else if (c1.equals(EXPORT_BY_TREE)) {
                chkFolder = true;
            }

            if (errMsg(program, "請輸入 程式所在目錄") == false) { // T1
                return;
            } else if (new File(program).exists() == false) {
                errMsg(program + "程式所在目錄 不存在");
                return;
            }
            if (errMsg(export, "請輸入 匯出目錄") == false) { // T2
                return;
            } else if (new File(export).exists() == false) {
                int theChoice;
                theChoice = JOptionPane.showConfirmDialog(null, "匯出目錄不存在 是否創建?", "創建?", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null);
                if (theChoice == JOptionPane.YES_OPTION) {
                    checkFilePath(export);
                } else if (theChoice == JOptionPane.NO_OPTION) {
                    return;
                } else if (theChoice == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
            try {
                StringBuffer copyinfo = new StringBuffer();
                StringBuffer loginfo = new StringBuffer();

                if (fileList == null || fileList.size() == 0) {
                    String filename = ((TextField) searchComponent(this.getComponents(), "T1")).getText();
                    File fff = new File(filename);
                    if (fff.exists()) {
                        FileUtil.traceFileList(new File(program), fileList);
                        ArrayList<String> total = getStringToList(((TextArea) searchComponent(this.getComponents(),
                                "A1")).getText());
                        try {
                            for (int ii = 0; ii < fileList.size(); ii++) {
                                boolean bHave = false;
                                for (String sublist : total)
                                    if (fileList.get(ii).getName().toLowerCase().equals(sublist.toLowerCase())) {
                                        bHave = true;
                                    }
                                if (bHave == false) {
                                    fileList.remove(ii);
                                    ii--;
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        errMsg(filename + "檔案不存在");
                    }
                }
                for (int ii = 0; ii < fileList.size(); ii++) {
                    String export2 = new String();
                    if (chkFolder) {
                        String subtmp = fileList.get(ii).getAbsolutePath().substring(program.length());
                        if (!subtmp.substring(0, 1).equals("\\")) {
                            subtmp = "\\" + subtmp;
                        }
                        export2 = export + subtmp;
                        copyinfo.append("mkdir " + export2.substring(0, export2.lastIndexOf("\\")) + "\r\n");
                    } else {
                        export2 = export;
                    }
                    copyinfo.append("copy " + fileList.get(ii).getAbsolutePath() + " " + export2 + "\r\n");
                    loginfo.append(getFileInfo(fileList.get(ii).toString()));
                }
                copyinfo.append("pause;\r\n");
                saveToFileCheckDir(export, "\\exe.bat", copyinfo.toString().getBytes());
                saveToFileCheckDir(export, "\\log.txt", loginfo.toString().getBytes());
                JOptionPane.showMessageDialog(null, "匯出成功,請執行" + export + "\\exe.bat檔案", "匯出",
                        JOptionPane.INFORMATION_MESSAGE, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void checkFilePath(String basePath) {
        File file = new File(basePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private void saveToFileCheckDir(String basePath, String fileName, byte[] content) {
        File file = new File(basePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileUtil.saveToFile(basePath + "\\" + fileName, content);
    }

    private ArrayList<String> getStringToList(String str) {
        ArrayList<String> list = new ArrayList<String>();
        Scanner scn = new Scanner(str);
        while (scn.hasNextLine()) {
            String tmp = scn.nextLine();
            list.add(tmp);
        }
        return list;
    }

    private boolean getIndexOfInList(File file, String[] remove) {
        if (remove == null || remove.length == 0)
            return false;
        if (remove[0].equals("") && remove.length == 1)
            return false;
        for (String remv : remove) {
            if (file.getName().toLowerCase().indexOf(remv.toLowerCase()) != -1)
                return true;
        }
        return false;
    }

    private String getFileInfo(String path) {
        File tmpf = new File(path);
        String fff = tmpf.getAbsolutePath();
        String fsize = "\t" + StringUtil_.formatNumber(String.valueOf(tmpf.length()));
        String fdate = "\t" + chzDate(DateUtil.getMillins(tmpf.lastModified()));
        return fff + fsize + fdate + "\r\n";
    }

    private static String chzDate(String d) {
        return d.substring(0, 4) + "/" + d.substring(4, 6) + "/" + d.substring(6, 8) + " "
                + (Integer.parseInt(d.substring(8, 10)) > 12 ? "下午" : "上午") + d.substring(8, 10) + ":"
                + d.substring(10, 12);
    }

    public void mousePressed(java.awt.event.MouseEvent arg0) {
    }

    public void mouseReleased(java.awt.event.MouseEvent arg0) {
    }

    public void mouseEntered(java.awt.event.MouseEvent arg0) {
    }

    public void mouseExited(java.awt.event.MouseEvent arg0) {
    }

    public void windowOpened(java.awt.event.WindowEvent arg0) {
    }

    public void windowClosed(java.awt.event.WindowEvent arg0) {
    }

    public void windowClosing(java.awt.event.WindowEvent arg0) {
        int theChoice;
        theChoice = JOptionPane.showConfirmDialog(null, "關閉時是否儲存參數", "關閉", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null);
        if (theChoice == JOptionPane.YES_OPTION) {
            StringBuffer sb = new StringBuffer();
            sb.append(PROGRAM + "=" + ((TextField) searchComponent(this.getComponents(), "T1")).getText() + "\r\n"
                    + EXPORT + "=" + ((TextField) searchComponent(this.getComponents(), "T2")).getText() + "\r\n"
                    + EXPORT_LIST + "=" + ((TextField) searchComponent(this.getComponents(), "T3")).getText() + "\r\n");
            FileUtil.saveToFile(System.getProperty("user.dir") + File.separator + this.getClass().getSimpleName()
                    + ".cfg", sb.toString().getBytes());
            Runtime.getRuntime().exit(0);
        } else if (theChoice == JOptionPane.NO_OPTION) {
            Runtime.getRuntime().exit(0);
        } else if (theChoice == JOptionPane.CANCEL_OPTION) {
            Runtime.getRuntime().exit(0);
        }
    }

    public void windowIconified(java.awt.event.WindowEvent arg0) {
    }

    public void windowDeiconified(java.awt.event.WindowEvent arg0) {
    }

    public void windowActivated(java.awt.event.WindowEvent arg0) {
    }

    public void windowDeactivated(java.awt.event.WindowEvent arg0) {
    }

    public static void main(String[] args) {
        new UpdateCode();
    }
}
