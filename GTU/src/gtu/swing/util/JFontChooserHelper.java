package gtu.swing.util;

import java.awt.Font;

import javax.swing.text.JTextComponent;

import say.swing.JFontChooser;

public class JFontChooserHelper {

    public static Font showChooser(JTextComponent settings) {
        JFontChooser fontChooser = new JFontChooser();
        fontChooser.setSelectedFont(settings.getFont());
        int result = fontChooser.showDialog(settings);
        if (result == JFontChooser.OK_OPTION) {
            Font font = fontChooser.getSelectedFont();
            settings.setFont(font);
            return font;
        }
        return null;
    }
}
