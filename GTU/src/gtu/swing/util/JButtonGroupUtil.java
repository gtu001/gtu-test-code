package gtu.swing.util;

import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

public class JButtonGroupUtil {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }
    
    /**
     * 將幾個RadioButton設定為一起互動
     */
    public static ButtonGroup createRadioButtonGroup(AbstractButton... btn){
        ButtonGroup btnGroup = new ButtonGroup();
        for(AbstractButton b : btn){
            btnGroup.add(b);
        }
        return btnGroup;
    }

    public static AbstractButton getSelectedButton(ButtonGroup group) {
        for (Enumeration<AbstractButton> enu = group.getElements(); enu.hasMoreElements();) {
            AbstractButton abs = enu.nextElement();
            if (abs.isSelected()) {
                return abs;
            }
        }
        return null;
    }
}
