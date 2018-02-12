package gtu.charset;

import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 將此package包成檔案 CP937_FUCO.jar
 * 放到 C:\Program Files\Java\jdk1.8.0_73\jre\lib\ext\CP937_FUCO.jar
 * 
 * Example : new String(bytearry, "CP937-FUCO")
 */
public class CustomCharsetProvider extends CharsetProvider {

    public CustomCharsetProvider() {
    }

    private List<Charset> charsets = new ArrayList<Charset>();

    @Override
    public Iterator<Charset> charsets() {
        return charsets.iterator();
    }

    @Override
    public Charset charsetForName(String charsetName) {
        charsetName = charsetName.toUpperCase();
        for (Iterator<Charset> iter = charsets.iterator(); iter.hasNext();) {
            Charset charset = iter.next();
            if (charset.name().equals(charsetName))
                return charset;
        }
        for (Iterator<Charset> iter = charsets.iterator(); iter.hasNext();) {
            Charset charset = iter.next();
            if (charset.aliases().contains(charsetName))
                return charset;
        }
        return null;
    }
}
