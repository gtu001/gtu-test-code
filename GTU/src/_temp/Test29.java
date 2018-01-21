package _temp;

import com.google.api.GoogleAPI;
import com.google.api.GoogleAPIException;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;

public class Test29 {

    public static void main(String[] args) throws GoogleAPIException {
        GoogleAPI.setHttpReferrer("http://code.google.com/p/google-api-translate-java/");
        String v = Translate.DEFAULT.execute("hello", Language.ENGLISH, Language.CHINESE_TRADITIONAL);
        System.out.println(v);
    }

}
