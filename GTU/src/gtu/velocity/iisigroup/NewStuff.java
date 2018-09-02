
import java.util.Arrays;

/**
 * ${chineseShortName}
 */
public class NewStuff_${UPPER_ID} {

    public static void main(String[] args) {
        Arrays.<Class<?>>asList(//
        #foreach( $var in $NEW_STUFF )
        $var, //
        #end
        );//
        
        //${XHTML_URL} 
        // to menu.xhtml
    }
}
