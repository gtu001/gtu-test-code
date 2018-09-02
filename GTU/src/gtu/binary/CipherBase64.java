package gtu.binary;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author Troy 2009/02/02
 * 
 */
public class CipherBase64 {

    public static String decode(String str, String encode) {
        try {
            BASE64Decoder b64d = new BASE64Decoder();
            return new String(b64d.decodeBuffer(str), encode);
        } catch (Exception e) {
            throw new RuntimeException("decode ERR : " + e.getMessage(), e);
        }
    }

    public static String encode(String str, String encode) {
        try {
            BASE64Encoder b64e = new BASE64Encoder();
            return (b64e.encode(str.getBytes(encode)));
        } catch (Exception e) {
            throw new RuntimeException("encode ERR : " + e.getMessage(), e);
        }
    }
}
