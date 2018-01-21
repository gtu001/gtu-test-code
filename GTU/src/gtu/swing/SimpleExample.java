package gtu.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.Collator;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.border.TitledBorder;

/**
 * FileMenuMnemonic
 * 
 * ComponentOrientation
 * 类封装语言敏感的方向，用于排序组件或文本内容。使用它可反映西方字母表、中东字母表（如希伯来语）和远东字母表（如日语）之间此种排序的差别。
 * 
 * @author 2012/1/8
 */
public class SimpleExample extends JPanel {

    static JFrame frame;
    static Font smallFont;
    static Font mediumFont;
    static Font bigFont;

    private ResourceBundle resources;
    private ComponentOrientation co;

    private static void applyComponentOrientation(Component c, ComponentOrientation o) {

        c.setComponentOrientation(o);

        if (c instanceof JMenu) {
            JMenu menu = (JMenu) c;
            int ncomponents = menu.getMenuComponentCount();
            for (int i = 0; i < ncomponents; ++i) {
                applyComponentOrientation(menu.getMenuComponent(i), o);
            }
        } else if (c instanceof Container) {
            Container container = (Container) c;
            int ncomponents = container.getComponentCount();
            for (int i = 0; i < ncomponents; ++i) {
                applyComponentOrientation(container.getComponent(i), o);
            }
        }
    }

    private void loadResources() {
        try {
            resources = ResourceBundle.getBundle("resource.Simple", Locale.getDefault());
        } catch (MissingResourceException mre) {
            mre.printStackTrace();
            System.exit(1);
        }
    }

    private static JFrame getFrame() {
        return frame;
    }

    public SimpleExample() {

        // Load our resource bundle
        loadResources();

        JRadioButton oneButton, twoButton, threeButton;
        JButton button;

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.getAllFonts();

        // Setup the fonts
        smallFont = new Font("Bitstream Cyberbit", Font.PLAIN, 14);
        mediumFont = new Font("Bitstream Cyberbit", Font.PLAIN, 18);
        bigFont = new Font("Bitstream Cyberbit", Font.PLAIN, 20);

        co = ComponentOrientation.getOrientation(Locale.getDefault());

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        String language = Locale.getDefault().getLanguage();

        // Create the buttons
        button = new JButton(resources.getString("Hello"));
        button.setToolTipText(resources.getString("HelloToolTip"));
        button.setFont(mediumFont);

        // Setup the buttons
        oneButton = new JRadioButton(resources.getString("One"));
        oneButton.setFont(mediumFont);
        oneButton.setMnemonic(resources.getString("OneMnemonic").charAt(0));
        oneButton.setHorizontalAlignment(JButton.TRAILING);
        oneButton.setHorizontalTextPosition(JButton.TRAILING);

        twoButton = new JRadioButton(resources.getString("Two"));
        twoButton.setFont(mediumFont);
        twoButton.setMnemonic(resources.getString("TwoMnemonic").charAt(0));
        twoButton.setHorizontalAlignment(JButton.TRAILING);
        twoButton.setHorizontalTextPosition(JButton.TRAILING);

        threeButton = new JRadioButton(resources.getString("Three"));
        threeButton.setFont(mediumFont);
        threeButton.setMnemonic(resources.getString("ThreeMnemonic").charAt(0));
        threeButton.setHorizontalAlignment(JButton.TRAILING);
        threeButton.setHorizontalTextPosition(JButton.TRAILING);

        // Group the radio buttons
        ButtonGroup group = new ButtonGroup();
        group.add(oneButton);
        group.add(twoButton);
        group.add(threeButton);

        // Register a listener for the radio buttons
        RadioListener myListener = new RadioListener();
        oneButton.addActionListener(myListener);
        twoButton.addActionListener(myListener);
        threeButton.addActionListener(myListener);

        // Setup the button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 100));
        TitledBorder tb = new TitledBorder(resources.getString("Numbers"));
        tb.setTitleFont(smallFont);
        tb.setTitleJustification(co.isLeftToRight() ? TitledBorder.LEFT : TitledBorder.RIGHT);

        buttonPanel.setBorder(tb);
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(button);
        buttonPanel.add(oneButton);
        buttonPanel.add(twoButton);
        buttonPanel.add(threeButton);

        add(buttonPanel, BorderLayout.CENTER);

        // Setup the date panel
        JPanel datePanel = new JPanel();
        datePanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 100));
        tb = new TitledBorder(resources.getString("Dates"));
        tb.setTitleFont(smallFont);
        tb.setTitleJustification(co.isLeftToRight() ? TitledBorder.LEFT : TitledBorder.RIGHT);

        datePanel.setBorder(tb);
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.X_AXIS));
        datePanel.add(Box.createRigidArea(new Dimension(5, 1)));

        DateFormatSymbols dfs = new DateFormatSymbols();

        JComboBox months = new JComboBox(dfs.getMonths());
        months.setFont(mediumFont);

        String weekDays[] = dfs.getWeekdays();
        JComboBox days = new JComboBox();
        days.setFont(mediumFont);

        // Determine what day is the first day of the week
        GregorianCalendar cal = new GregorianCalendar();

        int firstDayOfWeek = cal.getFirstDayOfWeek();
        int dayOfWeek;

        for (dayOfWeek = firstDayOfWeek; dayOfWeek < weekDays.length; dayOfWeek++)
            days.addItem(weekDays[dayOfWeek]);

        for (dayOfWeek = 0; dayOfWeek < firstDayOfWeek; dayOfWeek++)
            days.addItem(weekDays[dayOfWeek]);

        if (!co.isLeftToRight()) {
            datePanel.add(days);
            datePanel.add(Box.createRigidArea(new Dimension(5, 1)));

            datePanel.add(months);
            datePanel.add(Box.createRigidArea(new Dimension(5, 1)));
        } else {
            datePanel.add(months);
            datePanel.add(Box.createRigidArea(new Dimension(5, 1)));

            datePanel.add(days);
            datePanel.add(Box.createRigidArea(new Dimension(5, 1)));
        }
        add(datePanel);

        // Setup the formatting panel
        JPanel formatPanel = new JPanel();
        formatPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 100));
        tb = new TitledBorder(resources.getString("Formats"));
        tb.setTitleFont(smallFont);
        tb.setTitleJustification(co.isLeftToRight() ? TitledBorder.LEFT : TitledBorder.RIGHT);

        formatPanel.setBorder(tb);
        formatPanel.setLayout(new BoxLayout(formatPanel, BoxLayout.X_AXIS));
        formatPanel.add(Box.createRigidArea(new Dimension(5, 1)));

        double theNumber = 1234.56;
        NumberFormat nFormat = NumberFormat.getInstance();
        NumberFormat cFormat = NumberFormat.getCurrencyInstance();
        NumberFormat pFormat = NumberFormat.getPercentInstance();
        DateFormat dFormat = DateFormat.getDateInstance();

        JLabel numberLabel = new JLabel(nFormat.format(theNumber));
        numberLabel.setForeground(Color.black);
        numberLabel.setFont(bigFont);

        JLabel percentLabel = new JLabel(pFormat.format(theNumber));
        percentLabel.setForeground(Color.black);
        percentLabel.setFont(bigFont);

        JLabel currencyLabel = new JLabel(cFormat.format(theNumber));
        currencyLabel.setForeground(Color.black);
        currencyLabel.setFont(bigFont);

        JLabel dateLabel = new JLabel(dFormat.format(new Date()));
        dateLabel.setForeground(Color.black);
        dateLabel.setFont(bigFont);

        formatPanel.add(Box.createRigidArea(new Dimension(25, 1)));

        if (co.isLeftToRight()) {
            formatPanel.add(numberLabel);
            formatPanel.add(Box.createRigidArea(new Dimension(25, 1)));
            formatPanel.add(percentLabel);
            formatPanel.add(Box.createRigidArea(new Dimension(25, 1)));
            formatPanel.add(currencyLabel);
            formatPanel.add(Box.createRigidArea(new Dimension(25, 1)));
            formatPanel.add(dateLabel);
        } else {
            formatPanel.add(dateLabel);
            formatPanel.add(Box.createRigidArea(new Dimension(25, 1)));
            formatPanel.add(currencyLabel);
            formatPanel.add(Box.createRigidArea(new Dimension(25, 1)));
            formatPanel.add(percentLabel);
            formatPanel.add(Box.createRigidArea(new Dimension(25, 1)));
            formatPanel.add(numberLabel);
        }
        formatPanel.add(Box.createRigidArea(new Dimension(25, 1)));

        add(formatPanel);
    }

    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu file = (JMenu) menuBar.add(new JMenu(resources.getString("FileMenu")));
        file.setFont(mediumFont);
        file.setMnemonic(resources.getString("FileMenuMnemonic").charAt(0));

        JMenuItem exitItem = (JMenuItem) file.add(new JMenuItem(resources.getString("FileMenuExit")));
        exitItem.setFont(mediumFont);
        exitItem.setMnemonic(resources.getString("FileMenuExitMnemonic").charAt(0));
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menuBar.add(new LocaleChanger());

        return menuBar;
    }

    public void reloadResources() {
        try {
            resources = ResourceBundle.getBundle("resources.Simple", Locale.getDefault());
        } catch (MissingResourceException mre) {
            mre.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * An ActionListener that listens to the radio buttons
     */
    class RadioListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String lnfName = e.getActionCommand();

            Object[] options = { resources.getString("OK"), resources.getString("CANCEL") };
            Object[] arguments = { new Integer(3), lnfName };

            JOptionPane.showOptionDialog(null, MessageFormat.format(resources.getString("WarningMsg"), arguments),
                    resources.getString("WarningTitle"), JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                    options, options[0]);
            try {
            } catch (Exception exc) {
                JRadioButton button = (JRadioButton) e.getSource();
                button.setEnabled(false);
            }
        }
    }

    /**
     * A class to change the locale for the application
     */
    class LocaleChanger extends JMenu implements ItemListener {

        public LocaleChanger() {
            super();
            setText(resources.getString("LanguageMenu"));
            setFont(mediumFont);
            setMnemonic(resources.getString("LanguageMenuMnemonic").charAt(0));

            ButtonGroup langGroup = new ButtonGroup();
            String language = Locale.getDefault().getLanguage();

            // Sort the language names according to the rules specific to each
            // locale
            RuleBasedCollator rbc = (RuleBasedCollator) Collator.getInstance();
            ArrayList al = new ArrayList();
            al.add(resources.getString("Arabic"));
            al.add(resources.getString("Chinese"));
            al.add(resources.getString("English"));
            al.add(resources.getString("German"));
            al.add(resources.getString("Italian"));
            al.add(resources.getString("French"));
            al.add(resources.getString("Hebrew"));
            al.add(resources.getString("Japanese"));
            al.add(resources.getString("Russian"));

            Collections.sort(al, rbc);

            String langName = Locale.getDefault().getDisplayLanguage();
            for (int i = 0; i < al.size(); i++) {
                JRadioButtonMenuItem mi;
                mi = (JRadioButtonMenuItem) add(new JRadioButtonMenuItem((String) al.get(i)));
                mi.setFont(mediumFont);
                if (langName.equalsIgnoreCase((String) al.get(i)))
                    mi.setSelected(true);
                mi.addItemListener(this);
                langGroup.add(mi);
            }
        }

        public void itemStateChanged(ItemEvent e) {
            JRadioButtonMenuItem rb = (JRadioButtonMenuItem) e.getSource();
            if (rb.isSelected()) {
                String selected = rb.getText();
                if (selected.equals(resources.getString("Arabic"))) {
                    Locale.setDefault(new Locale("ar", "EG"));
                    co = ComponentOrientation.RIGHT_TO_LEFT;
                } else if (selected.equals(resources.getString("English"))) {
                    Locale.setDefault(Locale.US);
                    co = ComponentOrientation.LEFT_TO_RIGHT;
                } else if (selected.equals(resources.getString("German"))) {
                    Locale.setDefault(Locale.GERMANY);
                    co = ComponentOrientation.LEFT_TO_RIGHT;
                } else if (selected.equals(resources.getString("Italian"))) {
                    Locale.setDefault(Locale.ITALY);
                    co = ComponentOrientation.LEFT_TO_RIGHT;
                } else if (selected.equals(resources.getString("French"))) {
                    Locale.setDefault(Locale.FRANCE);
                    co = ComponentOrientation.LEFT_TO_RIGHT;
                } else if (selected.equals(resources.getString("Hebrew"))) {
                    Locale.setDefault(new Locale("iw", "IL"));
                    co = ComponentOrientation.RIGHT_TO_LEFT;
                } else if (selected.equals(resources.getString("Chinese"))) {
                    Locale.setDefault(Locale.CHINA);
                    co = ComponentOrientation.LEFT_TO_RIGHT;
                } else if (selected.equals(resources.getString("Japanese"))) {
                    Locale.setDefault(Locale.JAPAN);
                    co = ComponentOrientation.LEFT_TO_RIGHT;
                } else if (selected.equals(resources.getString("Russian"))) {
                    Locale.setDefault(new Locale("ru", "RU"));
                    co = ComponentOrientation.LEFT_TO_RIGHT;
                }
            }

            SimpleExample panel = new SimpleExample();
            SimpleExample.frame.setVisible(false);
            SimpleExample.frame.getContentPane().removeAll();
            SimpleExample.frame.setJMenuBar(panel.createMenuBar());
            SimpleExample.frame.getContentPane().add("Center", panel);
            SimpleExample.frame.pack();
            SimpleExample.frame.show();
            SimpleExample.applyComponentOrientation(SimpleExample.getFrame(), co);
        }
    }

    public static void main(String[] argv) {

        SimpleExample panel = new SimpleExample();

        frame = new JFrame("Simple Example");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setJMenuBar(panel.createMenuBar());
        frame.getContentPane().add("Center", panel);
        frame.pack();
        frame.setVisible(true);
    }
}