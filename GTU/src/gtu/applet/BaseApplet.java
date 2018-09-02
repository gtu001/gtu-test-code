package gtu.applet;

import gtu.file.FileUtil;
import gtu.string.StringUtil_;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Label;
import java.awt.List;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.TextListener;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * 將清單中的程式檔案 匯出至目的資料夾
 * 
 * @author gtu 2009/02/02
 * 
 */
@Deprecated
public class BaseApplet extends Frame implements MouseListener, WindowListener, ItemListener, TextListener, KeyListener {

    protected String configFilePath;
    protected HashMap<String, String> configFile;
    protected float FONTWIDTH = 6.25F;

    private boolean bMouseListener = true;
    private boolean bWindowListener = true;
    private boolean bItemListener = true;
    private boolean bTextListener = true;
    private boolean bKeyListener = true;

    protected void IsShowObjectMessage(boolean bMouseListener, boolean bWindowListener, boolean bItemListener,
            boolean bTextListener, boolean bKeyListener) {
        this.bItemListener = bItemListener;
        this.bKeyListener = bKeyListener;
        this.bMouseListener = bMouseListener;
        this.bTextListener = bTextListener;
        this.bWindowListener = bWindowListener;
    }

    /**
     * 新增視窗物件 處理物件位置長寬
     * 
     * @param o
     *            物件instances
     * @param x
     *            物件位置 x軸
     * @param y
     *            物件位置 y軸
     * @param name
     *            物件名稱
     * @param value
     *            物件標籤 或 內容
     */
    protected void addObject(Object o, int x, int y, String name, String value) {
        Rectangle reg = new Rectangle(x, y, 100, 25);
        reg.width = 100;
        if (o instanceof Label) {
            if (value != null && !value.equals(""))
                reg.width = (int) (FONTWIDTH * (float) value.getBytes().length);
        } else if (o instanceof TextField || o instanceof Button || o instanceof Checkbox) {
            if (value != null && !value.equals(""))
                reg.width = (int) (FONTWIDTH * (float) value.getBytes().length + 50);
        } else if (o instanceof TextArea) {
            reg.width = this.WIDTH - 50;
        } else if (o instanceof Choice || o instanceof CheckboxGroup || o instanceof List) {
            if (value != null && !value.equals("")) {
                String[] _value = value.split(",");
                int len = 0;
                for (int ii = 0, tmp = 0; ii < _value.length; ii++) {
                    tmp = _value[ii].getBytes().length;
                    if (tmp > len)
                        len = tmp;
                }
                reg.width = (int) (FONTWIDTH * (float) len) + 30;
                if (o instanceof List)
                    reg.height = 100;
            }
        }
        addObject(o, reg, name, value);
    }

    /**
     * 新增視窗物件 設定物件屬性 內容
     * 
     * @param o
     *            物件instances
     * @param reg
     *            物件位置
     * @param name
     *            物件名稱
     * @param value
     *            物件標籤 或 內容
     */
    protected void addObject(Object o, Rectangle reg, String name, String value) {
        if (o instanceof Label) {
            ((Label) o).setText(value);
            ((Label) o).setName(name);
            ((Label) o).setBounds(reg);
            ((Label) o).addMouseListener(this);
            this.add((Label) o);
        } else if (o instanceof TextField) {
            ((TextField) o).setText(value);
            ((TextField) o).setName(name);
            ((TextField) o).setBounds(reg);
            ((TextField) o).addMouseListener(this);
            ((TextField) o).addTextListener(this);
            ((TextField) o).addKeyListener(this);
            if (value.equalsIgnoreCase("password"))
                ((TextField) o).setEchoChar('*');
            this.add((TextField) o);
        } else if (o instanceof Button) {
            int len = (int) ((float) reg.width / FONTWIDTH);
            ((Button) o).setLabel(StringUtil_.splitStringSpace(value, len));
            ((Button) o).setName(name);
            ((Button) o).setBounds(reg);
            ((Button) o).addMouseListener(this);
            this.add((Button) o);
        } else if (o instanceof TextArea) {
            ((TextArea) o).setText(value);
            ((TextArea) o).setName(name);
            ((TextArea) o).setBounds(reg);
            ((TextArea) o).addMouseListener(this);
            ((TextArea) o).addTextListener(this);
            ((TextArea) o).addKeyListener(this);
            this.add((TextArea) o);
        } else if (o instanceof Choice) {
            String[] values = value.split(",");
            for (int ii = 0; ii < values.length; ii++)
                ((Choice) o).addItem(values[ii]);
            ((Choice) o).setName(name);
            ((Choice) o).setBounds(reg);
            ((Choice) o).addMouseListener(this);
            ((Choice) o).addItemListener(this);
            this.add((Choice) o);
        } else if (o instanceof Checkbox) {
            ((Checkbox) o).setLabel(value);
            ((Checkbox) o).setName(name);
            ((Checkbox) o).setBounds(reg);
            ((Checkbox) o).addMouseListener(this);
            ((Checkbox) o).addItemListener(this);
            this.add((Checkbox) o);
        } else if (o instanceof CheckboxGroup) {
            String[] values = value.split(",");
            for (int ii = 0; ii < values.length; ii++) {
                Checkbox chk = new Checkbox();
                chk.setLabel(values[ii]);
                chk.setName(name + ii);
                chk.setBounds(reg);
                reg.y += 25;
                chk.addMouseListener(this);
                chk.addItemListener(this);
                chk.setCheckboxGroup((CheckboxGroup) o);
                this.add(chk);
            }
        } else if (o instanceof List) {
            String[] values = value.split(",");
            ((List) o).setName(name);
            ((List) o).setBounds(reg);
            ((List) o).addMouseListener(this);
            ((List) o).addItemListener(this);
            for (int ii = 0; ii < values.length; ii++)
                ((List) o).add(values[ii]);
            this.add(((List) o));
        }
    }

    /**
     * 增加時鐘物件
     * 
     * @param x
     *            物件x軸
     * @param y
     *            物件y軸
     */
    protected void addClock(int x, int y) {
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
        for (int ii = 0; ii < 2; ii++) {
            this.add(clk[ii]);
        }
    }

    public static void main(String[] args) {
        new BaseApplet();
    }

    /**
     * 子繼承建構子必須宣告super(); 以啟用各項功能
     */
    public BaseApplet() {
        this.setLayout(null);
        this.setBounds(300, 300, 300, 300);
        this.addWindowListener(this);
        this.addMouseListener(this);
        this.addKeyListener(this);
        this.setVisible(true);
    }

    /**
     * 開啟檔案對話視窗
     * 
     * @param filter
     *            副檔名,檔案敘述 Ex:".jpg,圖片檔(JPG)"
     * @return
     */
    public File showOpenDialog(ArrayList<String> filter) {
        JFileChooser fc = new JFileChooser();
        for (int ii = 0; ii < filter.size(); ii++) {
            String[] filters = filter.get(ii).split(",");
            fc.addChoosableFileFilter(new OpenFileFilter(filters[0], filters[1]));
        }
        fc.showOpenDialog(null);
        return fc.getSelectedFile();
    }

    /**
     * 開啟檔案對話視窗
     * 
     * @param filter
     *            副檔名,檔案敘述 Ex:".jpg,圖片檔(JPG)"
     * @return
     */
    public File showOpenDialog(String filter) {
        JFileChooser fc = new JFileChooser();
        String[] filters = filter.split(",");
        fc.addChoosableFileFilter(new OpenFileFilter(filters[0], filters[1]));
        fc.showOpenDialog(null);
        return fc.getSelectedFile();
    }

    /**
     * 儲存檔案對話視窗
     * 
     * @param filter
     *            副檔名,檔案敘述 Ex:".jpg,圖片檔(JPG)"
     * @return
     */
    public File showSaveDialog(ArrayList<String> filter) {
        JFileChooser fc = new JFileChooser();
        for (int ii = 0; ii < filter.size(); ii++) {
            String[] filters = filter.get(ii).split(",");
            fc.addChoosableFileFilter(new OpenFileFilter(filters[0], filters[1]));
        }
        fc.showOpenDialog(null);
        return fc.getSelectedFile();
    }

    /**
     * 儲存檔案對話視窗
     * 
     * @param filter
     *            副檔名,檔案敘述 Ex:".jpg,圖片檔(JPG)"
     * @return
     */
    public File showSaveDialog(String filter) {
        JFileChooser fc = new JFileChooser();
        String[] filters = filter.split(",");
        fc.addChoosableFileFilter(new OpenFileFilter(filters[0], filters[1]));
        fc.showSaveDialog(null);
        // if (returnVal == JFileChooser.APPROVE_OPTION) {
        // System.out.println("You chose to open this file: " +
        // fc.getSelectedFile().getName());
        // }
        return fc.getSelectedFile();
    }

    public File showDirectoryDialog() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.addChoosableFileFilter(new OpenFileFilter("", ""));
        fc.showDialog(null, "選擇目錄");
        return fc.getSelectedFile();
    }

    private class OpenFileFilter extends javax.swing.filechooser.FileFilter {
        private String description;
        private String extension;

        public OpenFileFilter(String ext, String des) {
            this.extension = ext;
            this.description = des;
        }

        public boolean accept(File arg0) {
            if (arg0.getAbsolutePath().indexOf(extension) != -1)
                return true;
            if (arg0.isDirectory())
                return true;
            return false;
        }

        public String getDescription() {
            System.out.println(this.description);
            return description;
        }
    }

    /**
     * 從config黨取得物件參數
     * 
     * @param key
     *            物件的名稱
     * @return
     */
    protected String getProperty(String key) {
        if (configFile.containsKey(key)) {
            return configFile.get(key);
        }
        return "";
    }

    /**
     * 搜尋物件
     * 
     * @param com
     *            物件容器
     * @param name
     *            物件名稱
     * @return
     */
    public Component searchComponent(Component[] com, String name) {
        for (int ii = 0; ii < com.length; ii++)
            if (com[ii].getName().equals(name))
                return com[ii];
        return null;
    }

    /**
     * 搜尋物件
     * 
     * @param name
     *            物件名稱
     * @return
     */
    protected Component searchComponent(String name) {
        for (int ii = 0; ii < this.getComponentCount(); ii++)
            if (this.getComponent(ii).getName().equals(name))
                return this.getComponent(ii);
        return null;
    }

    /**
     * 取得物件 內含值
     * 
     * @param name
     *            物件名稱
     * @return
     */
    protected String getComponentValue(String name) {
        for (int ii = 0; ii < this.getComponentCount(); ii++)
            if (this.getComponent(ii).getName().equals(name)) {
                Component o = this.getComponent(ii);
                if (o instanceof Label) {
                    return ((Label) o).getText();
                } else if (o instanceof TextField) {
                    return ((TextField) o).getText();
                } else if (o instanceof Button) {
                    return ((Button) o).getLabel();
                } else if (o instanceof TextArea) {
                    return ((TextArea) o).getText();
                } else if (o instanceof Choice) {
                    return ((Choice) o).getSelectedItem();
                } else if (o instanceof Checkbox) {
                    return ((Checkbox) o).getLabel();
                }
            }
        return "";
    }

    /**
     * 取得物件 內含值
     * 
     * @param name
     *            物件
     * @return
     */
    protected String getComponentValue(Component o) {
        if (o instanceof Label) {
            return ((Label) o).getText();
        } else if (o instanceof TextField) {
            return ((TextField) o).getText();
        } else if (o instanceof Button) {
            return ((Button) o).getLabel();
        } else if (o instanceof TextArea) {
            return ((TextArea) o).getText();
        } else if (o instanceof Choice) {
            return ((Choice) o).getSelectedItem();
        } else if (o instanceof Checkbox) {
            return ((Checkbox) o).getLabel();
        }
        return "";
    }

    /**
     * TextField 文字方塊輸入確認 ,若為空白則顯示錯誤訊息 並回傳false
     * 
     * @param field
     *            TextField物件名稱
     * @param msg
     *            錯誤訊息
     * @return
     */
    public boolean errTextFieldMsg(String field, String msg) {
        TextField tmp = ((TextField) searchComponent(field));
        if (tmp.getText() == null || tmp.getText().equals("")) {
            JOptionPane.showMessageDialog(null, msg, "error", JOptionPane.ERROR_MESSAGE, null);
            return false;
        }
        return true;
    }

    /**
     * 顯示錯誤訊息
     * 
     * @param msg
     *            錯誤訊息
     */
    public void errMsg(String msg) {
        JOptionPane.showMessageDialog(null, msg, "error", JOptionPane.ERROR_MESSAGE, null);
    }

    /**
     * 判斷字串 若為空 則顯示錯誤訊息
     * 
     * @param field
     *            字串
     * @param msg
     *            錯誤訊息
     * @return
     */
    public boolean errMsg(String field, String msg) {
        if (field == null || field.equals("")) {
            JOptionPane.showMessageDialog(null, msg, "error", JOptionPane.ERROR_MESSAGE, null);
            return false;
        }
        return true;
    }

    // #######################################################
    // implements Listener area
    // #######################################################

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(java.awt.event.ItemEvent arg0) {
        if (bItemListener) {
            System.out.println("getID=" + arg0.getID());
            System.out.println("getStateChange=" + arg0.getStateChange());
            System.out.println("paramString=" + arg0.paramString());
            System.out.println("getItem=" + arg0.getItem());
            System.out.println("getItemSelectable=" + arg0.getItemSelectable());
            System.out.println("getSource=" + arg0.getSource());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(java.awt.event.MouseEvent arg0) {
        if (bMouseListener) {
            System.out.println(arg0.getComponent().getName() + " " + arg0.getX() + "," + arg0.getY());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(java.awt.event.MouseEvent arg0) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(java.awt.event.MouseEvent arg0) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(java.awt.event.MouseEvent arg0) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(java.awt.event.MouseEvent arg0) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
     */
    public void windowOpened(java.awt.event.WindowEvent arg0) {
        if (bWindowListener)
            System.out.println("windowOpened=" + arg0.getSource());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
     */
    public void windowClosed(java.awt.event.WindowEvent arg0) {
        if (bWindowListener)
            System.out.println("windowClosed=" + arg0.getSource());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
     */
    public void windowClosing(java.awt.event.WindowEvent arg0) {
        if (bWindowListener)
            System.out.println("windowClosing=" + arg0.getSource());
        int theChoice;
        theChoice = JOptionPane.showConfirmDialog(null, "關閉時是否儲存參數", "關閉", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null);
        if (theChoice == JOptionPane.YES_OPTION) {
            StringBuffer sb = new StringBuffer();
            for (int ii = 0; ii < this.getComponentCount(); ii++)
                sb.append(this.getComponents()[ii].getName() + "=" + getComponentValue(this.getComponents()[ii])
                        + System.getProperty("line.separator"));
            FileUtil.saveToFile(configFilePath, sb.toString().getBytes());
            Runtime.getRuntime().exit(0);
        } else if (theChoice == JOptionPane.NO_OPTION) {
            Runtime.getRuntime().exit(0);
        } else if (theChoice == JOptionPane.CANCEL_OPTION) {
            Runtime.getRuntime().exit(0);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
     */
    public void windowIconified(java.awt.event.WindowEvent arg0) {
        if (bWindowListener)
            System.out.println("windowIconified=" + arg0.getSource());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent
     * )
     */
    public void windowDeiconified(java.awt.event.WindowEvent arg0) {
        if (bWindowListener)
            System.out.println("windowDeiconified=" + arg0.getSource());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
     */
    public void windowActivated(java.awt.event.WindowEvent arg0) {
        if (bWindowListener)
            System.out.println("windowActivated=" + arg0.getSource());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent
     * )
     */
    public void windowDeactivated(java.awt.event.WindowEvent arg0) {
        if (bWindowListener)
            System.out.println("windowDeactivated=" + arg0.getSource());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.TextListener#textValueChanged(java.awt.event.TextEvent)
     */
    public void textValueChanged(java.awt.event.TextEvent arg0) {
        if (bTextListener)
            System.out.println("textValueChanged=" + arg0.getSource());
    }

    /**
     * implements KeyListener
     * 
     * @param arg0
     */
    public void keyTyped(java.awt.event.KeyEvent arg0) {
        if (bKeyListener)
            System.out.println("keyTyped=" + arg0.getKeyChar() + ":" + arg0.getComponent().getName());
    }

    /**
     * implements KeyListener
     * 
     * @param arg0
     */
    public void keyPressed(java.awt.event.KeyEvent arg0) {
    }

    /**
     * implements KeyListener
     * 
     * @param arg0
     */
    public void keyReleased(java.awt.event.KeyEvent arg0) {
    }

}
