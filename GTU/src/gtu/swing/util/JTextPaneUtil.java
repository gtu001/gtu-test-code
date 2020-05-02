package gtu.swing.util;

import java.awt.EventQueue;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;

import gtu.file.OsInfoUtil;

public class JTextPaneUtil {
    private JTextPane textArea;

    public static JTextPaneUtil newInstance(JTextPane textArea) {
        return new JTextPaneUtil(textArea);
    }

    public JTextPaneUtil(JTextPane textArea) {
        this.textArea = textArea;
    }

    public void setTextReset(final String text) {
        Runnable runnable = new Runnable() {
            public void run() {
                SimpleAttributeSet keyWord = new SimpleAttributeSet();
                textArea.setText("");
                try {
                    textArea.getDocument().insertString(0, text, keyWord);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        };
//        if (OsInfoUtil.isWindows()) {
//            runnable.run();
//        } else {
            EventQueue.invokeLater(runnable);
//        }
    }

    public void insertStart(final String text) {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    Document doc = textArea.getDocument();
                    doc.insertString(0, text, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        if (OsInfoUtil.isWindows()) {
            runnable.run();
        } else {
            EventQueue.invokeLater(runnable);
        }
    }

    public void append(final String text) {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    Document doc = textArea.getDocument();
                    doc.insertString(doc.getLength(), text, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        if (OsInfoUtil.isWindows()) {
            runnable.run();
        } else {
            EventQueue.invokeLater(runnable);
        }
    }
}
