package gtu.swing.util;

import java.awt.EventQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyledDocument;

import org.apache.commons.lang.StringUtils;

public class JTextPaneUtil {
    private JTextComponent textArea;

    public static JTextPaneUtil newInstance(JTextComponent textArea) {
        return new JTextPaneUtil(textArea);
    }

    public JTextPaneUtil(JTextComponent textArea) {
        this.textArea = textArea;
    }

    public void removeStyles() {
        MutableAttributeSet mas = ((JTextPane) textArea).getInputAttributes();
        System.out.println("before: " + mas);
        mas.removeAttributes(mas);
        System.out.println("after: " + mas);
    }

    public AtomicBoolean insertStart(final String text) {
        final AtomicBoolean success = new AtomicBoolean(false);
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

    public AtomicBoolean append(final String text, final AttributeSet[] attrSet, final int[] attrSetType) {
        final AtomicBoolean success = new AtomicBoolean(false);
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    StyledDocument doc = ((JTextPane) textArea).getStyledDocument();
                    int offset = textArea.getDocument().getLength();
                    doc.insertString(offset, text, null);
                    for (int ii = 0; ii < attrSet.length; ii++) {
                        AttributeSet attr = attrSet[ii];
                        int style = attrSetType[ii];
                        switch (style) {
                        case 0:
                            doc.setParagraphAttributes(offset, StringUtils.length(text), attr, false);
                            break;
                        case 1:
                            doc.setCharacterAttributes(offset, StringUtils.length(text), attr, false);
                            break;
                        }
                    }
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
        final AtomicBoolean success = new AtomicBoolean(false);
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

    public AtomicBoolean remove(final int start, final int length) {
        final AtomicBoolean success = new AtomicBoolean(false);
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
