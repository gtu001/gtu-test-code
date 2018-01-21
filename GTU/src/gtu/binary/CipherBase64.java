package gtu.binary;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author Troy 2009/02/02
 * 
 */
public class CipherBase64 {

    public String decode(String str) {
        String sRtn = null;
        try {
            BASE64Decoder b64d = new BASE64Decoder();
            sRtn = new String(b64d.decodeBuffer(str));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sRtn;

    }

    public String encode(String str) {
        BASE64Encoder b64e = new BASE64Encoder();
        return (b64e.encode(str.getBytes()));
    }

    public boolean verify(String originStr, String encryptionStr) {
        return (originStr.compareTo(decode(encryptionStr)) == 0 ? true : false);
    }
}
