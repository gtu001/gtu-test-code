package gtu.applet;

import gtu.file.FileUtil;

import java.awt.Button;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.TextField;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * @author Troy 2009/02/02
 * 
 */
public class Complier extends BaseApplet {

    public Complier() {
        super();
        this.setBounds(300, 300, 300, 300);
        this.addObject(new Label(), 25, 50, "L1", "root dir:");// 根目錄
        this.addObject(new TextField(), new Rectangle(100, 50, 125, 25), "T1", "");
        this.addObject(new Button(), new Rectangle(225, 50, 50, 25), "B1", "open");// 開啟
        this.addObject(new Label(), 25, 75, "L2", "complier:");// 編譯檔案
        this.addObject(new TextField(), new Rectangle(100, 75, 125, 25), "T2", "");
        this.addObject(new Button(), new Rectangle(225, 75, 50, 25), "B2", "open");// 開啟
        this.addObject(new Button(), new Rectangle(75, 100, 100, 25), "B3", "compile");// 編譯
        this.addObject(new TextArea(), new Rectangle(37, 132, 240, 150), "A1", "");
    }

    public static void main(String[] args) {
        new Complier();
    }

    public void mouseClicked(java.awt.event.MouseEvent arg0) {
        System.out.println(arg0.getX() + "," + arg0.getY());
        String comp = arg0.getComponent().getName();
        if (comp.equalsIgnoreCase("B1")) {
            ArrayList<String> list = new ArrayList<String>();
            list.add(".java,JAVA程式");
            ((TextField) searchComponent("T1")).setText(showOpenDialog(list).getAbsolutePath());
        }

        if (comp.equalsIgnoreCase("B2")) {
            ArrayList<String> list = new ArrayList<String>();
            list.add(".java,JAVA程式");
            ((TextField) searchComponent("T2")).setText(showOpenDialog(list).getAbsolutePath());
        }

        StringBuffer bat = new StringBuffer();
        if (comp.equalsIgnoreCase("B3")) {
            String root = ((TextField) searchComponent("T1")).getText();
            if (new File(root).exists() == true) {
                if (new File(root).isDirectory() == false) {
                    System.out.println(new File(root).isDirectory() + root);
                    root = root.substring(0, root.lastIndexOf("\\"));
                }
                bat.append("cd\\\r\n" + root.substring(0, root.indexOf(":") + 1) + "\r\n" + "cd\\\r\n" + "cd " + root
                        + "\r\n");
                String value = ((TextField) searchComponent("T2")).getText();
                if (!value.equals("")) {
                    bat.append("javac -encoding UTF8 " + value + "\r\n");
                } else {
                    bat.append(printFile(root));
                }
                System.out.println(bat);
                try {
                    File save = showSaveDialog("*.bat,BAT檔");
                    FileUtil.saveToFile(save.getAbsolutePath() + ".bat", bat.toString().getBytes());
                    // File nn = File.createTempFile("temp_", ".bat");
                    // FileUtil.saveToFile(nn.getAbsolutePath(),
                    // bat.toString().getBytes());
                    // Process ps =
                    // Runtime.getRuntime().exec(nn.getAbsolutePath());
                    // String msg = loadStream(ps.getInputStream());
                    // msg += loadStream(ps.getErrorStream());
                    // ((TextArea)searchComponent("A1")).setText(msg);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private String loadStream(InputStream in) throws IOException {
        int ptr = 0;
        in = new BufferedInputStream(in);
        StringBuffer buffer = new StringBuffer();
        while ((ptr = in.read()) != -1) {
            buffer.append((char) ptr);
        }
        return buffer.toString();
    }

    private String printFile(String fileName) {
        return printFile(fileName, new StringBuffer());
    }

    private String printFile(String fileName, StringBuffer sb) {
        try {
            File file = new File(fileName);
            if (file.isDirectory()) {
                File[] subFile = file.listFiles();
                for (int i = 0; i < subFile.length; i++) {
                    String fff = subFile[i].getAbsolutePath();
                    if (fff.indexOf(".java") != -1) {
                        sb.append("javac -encoding UTF8 " + fff + "\r\n");
                    }
                    printFile(subFile[i].getAbsolutePath(), sb);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
