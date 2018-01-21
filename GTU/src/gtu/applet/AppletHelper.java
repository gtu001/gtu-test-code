package gtu.applet;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Label;
import java.awt.List;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

/**
 * AppletHelper
 * 
 * @author admin
 */
@Deprecated
public class AppletHelper {

    private static float FONTWIDTH = 7.5F; // 字寬

    private final Container container;

    private AppletHelper(Container container) {
        this.container = container;
    }

    public static AppletHelper of(Container container) {
        return new AppletHelper(container);
    }

    public AppletHelper addButton(String name, String value, Rectangle reg, MouseListener mouselis) {
        this.container.add(component().addButton(name, value, reg, mouselis));
        return this;
    }

    public AppletHelper addCheckbox(String name, String value, Rectangle reg, MouseListener mouselis,
            ItemListener itemlis) {
        this.container.add(component().addCheckbox(name, value, reg, mouselis, itemlis));
        return this;
    }

    public AppletHelper addCheckboxGroup(String name, String value, Rectangle reg, MouseListener mouselis,
            ItemListener itemlis) {
        for (Component c : component().addCheckboxGroup(name, value, reg, mouselis, itemlis)) {
            this.container.add(c);
        }
        return this;
    }

    public AppletHelper addChoice(String name, String value, Rectangle reg, MouseListener mouselis, ItemListener itemlis) {
        this.container.add(component().addChoice(name, value, reg, mouselis, itemlis));
        return this;
    }

    public AppletHelper addClock(String name, String value, int x, int y, MouseListener mouselis, ItemListener itemlis) {
        for (Component c : component().addClock(name, x, y, mouselis, itemlis)) {
            this.container.add(c);
        }
        return this;
    }

    public AppletHelper addLabel(String name, String value, Rectangle reg, MouseListener mouselis) {
        this.container.add(component().addLabel(name, value, reg, mouselis));
        return this;
    }

    public AppletHelper addList(String name, String value, Rectangle reg, MouseListener mouselis, ItemListener itemlis) {
        this.container.add(component().addList(name, value, reg, mouselis, itemlis));
        return this;
    }

    public AppletHelper addTextArea(String name, String value, Rectangle reg, MouseListener mouselis,
            TextListener textlis, KeyListener keylis) {
        this.container.add(component().addTextArea(name, value, reg, mouselis, textlis, keylis));
        return this;
    }

    public AppletHelper addTextField(String name, String value, Rectangle reg, boolean isPassword,
            MouseListener mouselis, TextListener textlis, KeyListener keylis) {
        this.container.add(component().addTextField(name, value, reg, isPassword, mouselis, textlis, keylis));
        return this;
    }

    public static class Listener {
        public interface LItemListener extends ItemListener {
        }

        public interface LTextListener extends TextListener {
        }

        public interface LMouseListener extends MouseListener {
        }

        public interface LKeyListener extends KeyListener {
        }

        public interface LWindowListener extends WindowListener {
        }
    }

    public static class Adapter {
        public static class AMouseAdapter extends MouseAdapter {
        }

        public static class AKeyAdapter extends KeyAdapter {
        }

        public static class AWindowAdapter extends WindowAdapter {
        }
    }

    public static DefaultListener getDefaultListener() {
        return DefaultListener.get();
    }

    public static class DefaultListener {
        private static final DefaultListener INSTANCE = new DefaultListener();

        private DefaultListener() {
        }

        private static DefaultListener get() {
            return INSTANCE;
        }

        public ItemListener itemListener() {
            return new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    System.out.println("getID=" + e.getID());
                    System.out.println("getStateChange=" + e.getStateChange());
                    System.out.println("paramString=" + e.paramString());
                    System.out.println("getItem=" + e.getItem());
                    System.out.println("getItemSelectable=" + e.getItemSelectable());
                    System.out.println("getSource=" + e.getSource());
                }
            };
        }

        public TextListener textListener() {
            return new TextListener() {
                public void textValueChanged(TextEvent e) {
                    System.out.println("textValueChanged=" + e.getSource());
                }
            };
        }

        public MouseAdapter mouseLister() {
            return new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent event) {
                    System.out.println(event.getComponent().getName() + " " + event.getX() + "," + event.getY());
                }
            };
        }

        public KeyAdapter keyListener() {
            return new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent event) {
                    System.out.println("keyTyped=" + event.getKeyChar() + ":" + event.getComponent().getName());
                }
            };
        }

        public WindowAdapter windowAdapter() {
            return new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent event) {
                    new ConfirmDialog() {
                        @Override
                        protected void cancel() {
                        }

                        @Override
                        protected void no() {
                        }

                        @Override
                        protected void yes() {
                            this.exit();
                        }
                    }.showConfirmDialog("是否關閉?", "關閉確認");
                }
            };
        }
    }

    public static abstract class ConfirmDialog {
        protected abstract void yes();

        protected abstract void no();

        protected abstract void cancel();

        public void exit() {
            Runtime.getRuntime().exit(0);
        }

        public void showConfirmDialog(String message, String title) {
            int theChoice;
            theChoice = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null);
            if (theChoice == JOptionPane.YES_OPTION) {
                this.yes();
            } else if (theChoice == JOptionPane.NO_OPTION) {
                this.no();
            } else if (theChoice == JOptionPane.CANCEL_OPTION) {
                this.cancel();
            }
        }
    }

    public static RectangleUtil rectangle() {
        return RectangleUtil.getInstance();
    }

    public static class RectangleUtil {
        private static RectangleUtil instance;
        private Rectangle reg = new Rectangle();

        private RectangleUtil() {
        }

        public static RectangleUtil getInstance() {
            if (instance == null) {
                instance = new RectangleUtil();
            }
            return instance;
        }

        private static final int LEFT = 30;
        private static final int TOP = 30;
        private static final int[] WIDTH = { 100, 150, 100, 150, 100, 150, 100, 150, 100, 150 };
        private static final int HIDTH = 30;

        public Rectangle set(int rows, int cols) {
            reg.x = this.getLeft(rows);
            reg.y = this.getTop(cols);
            reg.width = WIDTH[rows];
            reg.height = HIDTH;
            System.out.println(reg);
            return reg;
        }

        public Rectangle set(int rows, int cols, int width, int height) {
            reg.x = this.getLeft(rows);
            reg.y = this.getTop(cols);
            reg.width = width;
            reg.height = height;
            System.out.println(reg);
            return reg;
        }

        private int getLeft(int rows) {
            int left = LEFT;
            for (int ii = 0; ii < rows; ii++) {
                left += WIDTH[ii];
            }
            return left;
        }

        private int getTop(int cols) {
            return TOP + HIDTH * cols;
        }

        private int getWidth(String value) {
            return (int) (FONTWIDTH * (float) value.getBytes().length);
        }
    }

    public static DefaultComponent component() {
        return DefaultComponent.getInstance();
    }

    public static class DefaultComponent {
        private static final DefaultComponent INSTANCE = new DefaultComponent();

        private DefaultComponent() {
        }

        public static DefaultComponent getInstance() {
            return INSTANCE;
        }

        public Label addLabel(String name, String value, Rectangle reg, MouseListener mouselis) {
            Label o = new Label();
            o.setText(value);
            o.setName(name);
            // o.setBackground(Color.yellow);
            o.setBackground(Color.LIGHT_GRAY);
            o.setBounds(reg);
            if (mouselis == null) {
                mouselis = getDefaultListener().mouseLister();
            }
            o.addMouseListener(mouselis);
            return o;
        }

        public TextField addTextField(String name, String value, Rectangle reg, boolean isPassword,
                MouseListener mouselis, TextListener textlis, KeyListener keylis) {
            TextField o = new TextField();
            o.setText(value);
            o.setName(name);
            o.setBounds(reg);
            if (mouselis == null) {
                mouselis = getDefaultListener().mouseLister();
            }
            if (textlis == null) {
                textlis = getDefaultListener().textListener();
            }
            if (keylis == null) {
                keylis = getDefaultListener().keyListener();
            }
            o.addMouseListener(mouselis);
            o.addTextListener(textlis);
            o.addKeyListener(keylis);
            if (isPassword) {
                o.setEchoChar('*');
            }
            return o;
        }

        public Button addButton(String name, String value, Rectangle reg, MouseListener mouselis) {
            Button o = new Button();
            o.setLabel(value);
            o.setName(name);
            o.setBounds(reg);
            if (mouselis == null) {
                mouselis = getDefaultListener().mouseLister();
            }
            o.addMouseListener(mouselis);
            return o;
        }

        public Checkbox addCheckbox(String name, String value, Rectangle reg, MouseListener mouselis,
                ItemListener itemlis) {
            Checkbox o = new Checkbox();
            o.setLabel(value);
            o.setName(name);
            o.setBounds(reg);
            if (mouselis == null) {
                mouselis = getDefaultListener().mouseLister();
            }
            if (itemlis == null) {
                itemlis = getDefaultListener().itemListener();
            }
            o.addMouseListener(mouselis);
            o.addItemListener(itemlis);
            return o;
        }

        public Choice addChoice(String name, String value, Rectangle reg, MouseListener mouselis, ItemListener itemlis) {
            Choice o = new Choice();
            if (StringUtils.isNotEmpty(value)) {
                this.addObjectSetup(reg, value);
                String[] values = value.split(",");
                for (int ii = 0; ii < values.length; ii++) {
                    o.addItem(values[ii]);
                }
            }
            o.setName(name);
            o.setBounds(reg);
            if (mouselis == null) {
                mouselis = getDefaultListener().mouseLister();
            }
            if (itemlis == null) {
                itemlis = getDefaultListener().itemListener();
            }
            o.addMouseListener(mouselis);
            o.addItemListener(itemlis);
            return o;
        }

        public Checkbox[] addCheckboxGroup(String name, String value, Rectangle reg, MouseListener mouselis,
                ItemListener itemlis) {
            CheckboxGroup o = new CheckboxGroup();
            this.addObjectSetup(reg, value);
            String[] values = value.split(",");
            Checkbox[] chks = new Checkbox[values.length];
            int index = 0;
            for (Checkbox chk : chks) {
                chk = new Checkbox();
                chk.setLabel(values[index]);
                chk.setName(name + index);
                chk.setBounds(reg);
                reg.y += 25;
                if (mouselis == null) {
                    mouselis = getDefaultListener().mouseLister();
                }
                if (itemlis == null) {
                    itemlis = getDefaultListener().itemListener();
                }
                chk.addMouseListener(mouselis);
                chk.addItemListener(itemlis);
                chk.setCheckboxGroup(o);
                index++;
            }
            return chks;
        }

        public List addList(String name, String value, Rectangle reg, MouseListener mouselis, ItemListener itemlis) {
            List o = new List();
            if (StringUtils.isNotEmpty(value)) {
                this.addObjectSetup(reg, value);
                String[] values = value.split(",");
                for (int ii = 0; ii < values.length; ii++) {
                    o.add(values[ii]);
                }
            }
            o.setName(name);
            o.setBounds(reg);
            if (mouselis == null) {
                mouselis = getDefaultListener().mouseLister();
            }
            if (itemlis == null) {
                itemlis = getDefaultListener().itemListener();
            }
            o.addMouseListener(mouselis);
            o.addItemListener(itemlis);
            return o;
        }

        public TextArea addTextArea(String name, String value, Rectangle reg, MouseListener mouselis,
                TextListener textlis, KeyListener keylis) {
            TextArea o = new TextArea();
            o.setText(value);
            o.setName(name);
            o.setBounds(reg);
            if (mouselis == null) {
                mouselis = getDefaultListener().mouseLister();
            }
            if (textlis == null) {
                textlis = getDefaultListener().textListener();
            }
            if (keylis == null) {
                keylis = getDefaultListener().keyListener();
            }
            o.addMouseListener(mouselis);
            o.addTextListener(textlis);
            o.addKeyListener(keylis);
            return o;
        }

        /**
         * 增加時鐘物件
         * 
         * @param x
         *            物件x軸
         * @param y
         *            物件y軸
         */
        public Choice[] addClock(String name, int x, int y, MouseListener mouselis, ItemListener itemlis) {
            Choice[] clk = new Choice[2];
            for (int ii = 0; ii < 2; ii++) {
                clk[ii] = new Choice();
                clk[ii].setName(name + ii);
                clk[ii].setBounds(x + ii * 50, y, 50, 25);
                if (mouselis == null) {
                    mouselis = getDefaultListener().mouseLister();
                }
                if (itemlis == null) {
                    itemlis = getDefaultListener().itemListener();
                }
                clk[ii].addMouseListener(mouselis);
                clk[ii].addItemListener(itemlis);
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

        public void addObjectSetup(Rectangle reg, String value) {
            String[] _value = value.split(",");
            int len = 0;
            for (int ii = 0, tmp = 0; ii < _value.length; ii++) {
                tmp = _value[ii].getBytes().length;
                if (tmp > len)
                    len = tmp;
            }
            reg.width = (int) (FONTWIDTH * (float) len) + 30;
        }
    }

    public void setAllKeyListener(Container container, KeyListener listener, Object component) {
        Component[] allComponents = container.getComponents();
        for (Component comp : allComponents) {
            if (comp.getClass() == component.getClass() || component == null) {
                comp.addKeyListener(listener);
            }
        }
    }

    public void setAllMouseListener(Container container, MouseListener listener, Object component) {
        Component[] allComponents = container.getComponents();
        for (Component comp : allComponents) {
            if (comp.getClass() == component.getClass() || component == null) {
                comp.addMouseListener(listener);
            }
        }
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
    public Component searchComponent(String name, Container container) {
        Component[] com = container.getComponents();
        for (int ii = 0; ii < com.length; ii++)
            if (com[ii].getName().equals(name))
                return com[ii];
        return null;
    }

    public Component getComponent(String name) {
        Component[] com = container.getComponents();
        for (int ii = 0; ii < com.length; ii++)
            if (com[ii].getName().equals(name)) {
                return com[ii];
            }
        System.out.println("component : " + name + " is not found!");
        return null;
    }

    /**
     * 取得物件 內含值
     * 
     * @param name
     *            物件
     * @return
     */
    public String getComponentValue(Component o) {
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
    public boolean isEmptyTextFieldAlertReturnFalse(String field) {
        return this.isEmptyTextFieldAlertReturnFalse(field, container);
    }

    private boolean isEmptyTextFieldAlertReturnFalse(String field, Container container) {
        TextField tmp = ((TextField) this.searchComponent(field, container));
        String value = tmp.getText();
        if (StringUtils.isEmpty(value)) {
            JOptionPane.showMessageDialog(null, "欄位[" + field + "]不可為空", "error", JOptionPane.ERROR_MESSAGE, null);
            return false;
        }
        return true;
    }

    private static class Message {
        private static final Message instance = new Message();

        private Message() {
        }

        public static Message getInstance() {
            return instance;
        }

        public void showErrorMessage(String title, Object message) {
            this.showDialog(title, message, JOptionPane.ERROR_MESSAGE);
        }

        public void showPlainMessage(String title, Object message) {
            this.showDialog(title, message, JOptionPane.PLAIN_MESSAGE);
        }

        public void showQuestionMessage(String title, Object message) {
            this.showDialog(title, message, JOptionPane.QUESTION_MESSAGE);
        }

        public void showWarningMessage(String title, Object message) {
            this.showDialog(title, message, JOptionPane.WARNING_MESSAGE);
        }

        private void showDialog(String title, Object message, int type) {
            JOptionPane.showMessageDialog(null, message, title, type, null);
        }
    }
}
