package gtu.swing.util;

import java.awt.EventQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;

import org.apache.commons.lang.StringUtils;

public class JTextPaneUtil {
    private JTextPane textArea;

    public static JTextPaneUtil newInstance(JTextPane textArea) {
        return new JTextPaneUtil(textArea);
    }

    public JTextPaneUtil(JTextPane textArea) {
        this.textArea = textArea;
    }

    public void removeStyles() {
        MutableAttributeSet mas = textArea.getInputAttributes();
        System.out.println("before: " + mas);
        mas.removeAttributes(mas);
        System.out.println("after: " + mas);
    }

    public AtomicBoolean insertStart(final String text) {
        AtomicBoolean success = new AtomicBoolean(false);
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    Document doc = textArea.getDocument();
                    doc.insertString(0, text, null);
                    success.set(true);
                } catch (Exception e) {
                    success.set(true);
                    e.printStackTrace();
                }
            }
        };
        EventQueue.invokeLater(runnable);
        return success;
    }

    public AtomicBoolean append(final String text) {
        AtomicBoolean success = new AtomicBoolean(false);
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    Document doc = textArea.getDocument();
                    doc.insertString(doc.getLength(), text, null);
                    success.set(true);
                } catch (Exception e) {
                    success.set(true);
                    e.printStackTrace();
                }
            }
        };
        EventQueue.invokeLater(runnable);
        return success;
    }

    public AtomicBoolean remove(final int start, int length) {
        AtomicBoolean success = new AtomicBoolean(false);
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    Document doc = textArea.getDocument();
                    doc.remove(start, length);
                    success.set(true);
                } catch (Exception e) {
                    success.set(true);
                    e.printStackTrace();
                }
            }
        };
        SwingUtilities.invokeLater(runnable);
        return success;
    }

    public AtomicBoolean clear() {
        return remove(0, StringUtils.length(textArea.getText()));
    }
}
