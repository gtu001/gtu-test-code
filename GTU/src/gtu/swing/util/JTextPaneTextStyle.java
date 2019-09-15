package gtu.swing.util;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class JTextPaneTextStyle {
    JTextPane tp;
    Integer startPos;
    Integer endPos;
    Integer offset;
    Color foregroundColor;
    Color backgroundColor;
    boolean bold;
    boolean italic;
    Integer fondSize;
    boolean underline;

    public JTextPaneTextStyle(JTextPane tp) {
        this.tp = tp;
    }

    public static JTextPaneTextStyle of(JTextPane tp) {
        return new JTextPaneTextStyle(tp);
    }

    public JTextPaneTextStyle startEnd(Integer startPos, Integer endPos) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.offset = endPos - startPos + 1;
        return this;
    }

    public JTextPaneTextStyle foregroundColor(Color c) {
        this.foregroundColor = c;
        return this;
    }

    public JTextPaneTextStyle backgroundColor(Color c) {
        this.backgroundColor = c;
        return this;
    }

    public JTextPaneTextStyle bold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public JTextPaneTextStyle italic(boolean italic) {
        this.italic = italic;
        return this;
    }

    public JTextPaneTextStyle fondSize(Integer fondSize) {
        this.fondSize = fondSize;
        return this;
    }

    public JTextPaneTextStyle underline(boolean underline) {
        this.underline = underline;
        return this;
    }

    public void apply() {
        if (this.startPos == null && this.endPos == null) {
            int endPos = tp.getText() != null ? tp.getText().length() - 1 : 0;
            startEnd(0, endPos);
        }

        if (this.foregroundColor != null) {
            StyleContext sc = StyleContext.getDefaultStyleContext();
            AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, this.foregroundColor);

            aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
            aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

            tp.getStyledDocument().setCharacterAttributes(startPos, offset, aset, false);
        }

        if (this.backgroundColor != null) {
            StyleContext sc = StyleContext.getDefaultStyleContext();
            AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Background, this.backgroundColor);

            aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
            aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

            tp.getStyledDocument().setCharacterAttributes(startPos, offset, aset, false);
        }

        if (bold) {
            SimpleAttributeSet attributes = new SimpleAttributeSet();
            attributes.addAttribute(StyleConstants.CharacterConstants.Bold, Boolean.TRUE);
            tp.getStyledDocument().setCharacterAttributes(startPos, offset, attributes, false);
        }

        if (italic) {
            SimpleAttributeSet attributes = new SimpleAttributeSet();
            attributes.addAttribute(StyleConstants.CharacterConstants.Italic, Boolean.TRUE);
            tp.getStyledDocument().setCharacterAttributes(startPos, offset, attributes, false);
        }

        if (fondSize != null) {
            Style style = tp.getStyledDocument().addStyle("fondSize" + fondSize, null);
            StyleConstants.setFontSize(style, fondSize);
            tp.getStyledDocument().setCharacterAttributes(startPos, offset, style, false);
        }

        if (underline) {
            SimpleAttributeSet attributeSet = new SimpleAttributeSet();
            StyleConstants.setUnderline(attributeSet, true);
            tp.getStyledDocument().setCharacterAttributes(startPos, offset, attributeSet, false);
        }
    }
}