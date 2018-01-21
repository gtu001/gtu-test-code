package gtu.swing.util;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

/**
 * 
 * JComboBox with an autocomplete drop down menu. This class is hard-coded for
 * String objects, but can be
 * 
 * altered into a generic form to allow for any searchable item.
 * 
 * @author G. Cope
 */
public class AutocompleteJComboBox extends JComboBox {

    static final long serialVersionUID = 4321421L;
    
    public static void main(String[] args) {
        List<String> searchList = new ArrayList<String>();
        for(int ii = (int)'a'; ii <= (int)'z'; ii ++) {
            String v = String.valueOf((char)ii);
            searchList.add(v + v + v + "1");
            searchList.add(v + v + v + "2");
            searchList.add(v + v + v + "3");
        }
        StringSearchable s = new StringSearchable(searchList);
        AutocompleteJComboBox v1 = new AutocompleteJComboBox(s);
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(v1);
        f.pack();
        f.setVisible(true);
    }

    public static class StringSearchable<T> {
        private List<String> terms = new ArrayList<String>();

        /**
         * Constructs a new object based upon the parameter terms.
         * 
         * @param terms
         *            The inventory of terms to search.
         */
        public StringSearchable(List<String> terms) {
            this.terms.addAll(terms);
        }

        public Collection<String> search(String value) {
            List<String> founds = new ArrayList<String>();
            for (String s : terms) {
                if (s.indexOf(value) == 0) {
                    founds.add(s);
                }
            }
            return founds;
        }
    }

    private final StringSearchable searchable;

    /**
     * 
     * Constructs a new object based upon the parameter searchable
     * 
     * @param s
     * 
     */
    public AutocompleteJComboBox(StringSearchable s) {
        super();
        this.searchable = s;
        setEditable(true);

        Component c = getEditor().getEditorComponent();
        if (c instanceof JTextComponent) {
            final JTextComponent tc = (JTextComponent) c;
            tc.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void changedUpdate(DocumentEvent arg0) {
                }

                @Override
                public void insertUpdate(DocumentEvent arg0) {
                    update();
                }

                @Override

                public void removeUpdate(DocumentEvent arg0) {
                    update();
                }

                public void update() {
                    // perform separately, as listener conflicts between the
                    // editing component
                    // and JComboBox will result in an IllegalStateException due
                    // to editing
                    // the component when it is locked.
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            List<String> founds = new ArrayList<String>(searchable.search(tc.getText()));
                            Set<String> foundSet = new HashSet<String>();
                            for (String s : founds) {
                                foundSet.add(s.toLowerCase());
                            }

                            Collections.sort(founds);// sort alphabetically
                            setEditable(false);
                            removeAllItems();

                            // if founds contains the search text, then only add
                            // once.
                            if (!foundSet.contains(tc.getText().toLowerCase())) {
                                addItem(tc.getText());
                            }

                            for (String s : founds) {
                                addItem(s);
                            }
                            setEditable(true);
                            setPopupVisible(true);
                        }
                    });
                }
            });

            // When the text component changes, focus is gained
            // and the menu disappears. To account for this, whenever the focus
            // is gained by the JTextComponent and it has searchable values, we
            // show the popup.
            tc.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent arg0) {
                    if (tc.getText().length() > 0) {
                        setPopupVisible(true);
                    }
                }

                @Override
                public void focusLost(FocusEvent arg0) {
                }
            });
        } else {
            throw new IllegalStateException("Editing component is not a JTextComponent!");
        }
    }
}
