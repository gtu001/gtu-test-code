package gtu.swing.util;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;

public class JTextPaneUtil {
    private JTextPane textArea;

    public static JTextPaneUtil newInstance(JTextPane textArea) {
        return new JTextPaneUtil(textArea);
    }

    public JTextPaneUtil(JTextPane textArea) {
        this.textArea = textArea;
    }

    public void setTextReset(String text) {
        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        textArea.setText("");
        try {
            textArea.getDocument().insertString(0, text, keyWord);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void insertStart(String text) {
        try {
            Document doc = textArea.getDocument();
            doc.insertString(0, text, null);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void append(String text) {
        try {
            Document doc = textArea.getDocument();
            doc.insertString(doc.getLength(), text, null);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
