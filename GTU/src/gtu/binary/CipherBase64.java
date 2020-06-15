package gtu.binary;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

/**
 * @author Troy 2009/02/02
 * 
 */
public class CipherBase64 {

    public static String decode(String str, String encode) {
        try {
            // BASE64Decoder b64d = new BASE64Decoder();
            // return new String(b64d.decodeBuffer(str), encode);
            final Base64 base64 = new Base64();
            final String text = StringUtils.defaultString(str);
            return new String(base64.decode(text), encode);
        } catch (Exception e) {
            throw new RuntimeException("decode ERR : " + e.getMessage(), e);
        }
    }

    public static String encode(String str, String encode) {
        try {
            // BASE64Encoder b64e = new BASE64Encoder();
            // return (b64e.encode(str.getBytes(encode)));
            final Base64 base64 = new Base64();
            final String text = StringUtils.defaultString(str);
            final byte[] textByte = text.getBytes(encode);
            final String encodedText = base64.encodeToString(textByte);
            return encodedText;
        } catch (Exception e) {
            throw new RuntimeException("encode ERR : " + e.getMessage(), e);
        }
    }

    public static byte[] decodeToByteArry(String str) {
        try {
            // BASE64Decoder b64d = new BASE64Decoder();
            // return b64d.decodeBuffer(str);
            final Base64 base64 = new Base64();
            final String text = StringUtils.defaultString(str);
            return base64.decode(text);
        } catch (Exception e) {
            throw new RuntimeException("decode ERR : " + e.getMessage(), e);
        }
    }

    public static String encode(byte[] arry) {
        try {
            // BASE64Encoder b64e = new BASE64Encoder();
            // return (b64e.encode(arry));
            final Base64 base64 = new Base64();
            final String encodedText = base64.encodeToString(arry);
            return encodedText;
        } catch (Exception e) {
            throw new RuntimeException("encode ERR : " + e.getMessage(), e);
        }
    }
}
