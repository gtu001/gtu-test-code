package gtu.swing.util;

import java.util.List;

import javax.swing.JComboBox;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;

public class JAutocompleteUtil {

    public static void apply1(JComboBox comboBox){
        AutoCompleteDecorator.decorate(comboBox);
    }
    
    public static void apply1(JComboBox comboBox, List<?> elements){
        AutoCompleteSupport.install(comboBox, GlazedLists.eventListOf(elements));
    }
}
