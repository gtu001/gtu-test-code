package gtu.ireport.ex1;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provide simple cipher utility functions. Only supports CBC padding. RSA not tested.
 * @author Steven
 * @since 1.0.1
 */
public final class CipherUtil {

    private static final Logger log = LoggerFactory.getLogger(CipherUtil.class);
//    private static final Charset UTF8 = Charset.forName("UTF-8");
    
    private CipherUtil() {
    }
    
    /**
     * Creates a Cipher with AES algorithem.
     * @return AES cipher
     */ 
    public static Cipher createAESCipher() {
        return createCipher("AES/ECB/PKCS5Padding");        
    }
    
    /**
     * Create a key for AES algorithem.
     * @return a AES key
     */
    public static Key createAESKey() {
        return createKey("AES");
    }
    
    private static final Cipher createCipher(String algorithem) {
        try {
            //"algorithm/mode/padding"
            return Cipher.getInstance(algorithem);
        }
        catch(Exception e) {
            //should never happen, unless DES is not supported in runtime environment
            throw new RuntimeException(e);
        }
    }
    
    private static final Key createKey(String algorithem) {
        
        try {
            return KeyGenerator.getInstance(algorithem).generateKey();
        }
        catch (Exception e) {
            // should never happen, unless DES is not supported in runtime environment
            throw new RuntimeException(e);
        }
    }

    /**
     * Use provided cipher and key to encode.<br>
     * Synchronization will be placed on cipher object.
     * @param cipher cipher object to encode
     * @param key key for encryption
     * @param toBeEncoded string to be encoded
     * @return encoded string
     */
    public static String syncEncode(final Cipher cipher, Key key, String toBeEncoded) {

        String encoded = null;
        try {
            byte[] bytes;
            synchronized (cipher) {
                cipher.init(Cipher.ENCRYPT_MODE, key);
                bytes = cipher.doFinal(toBeEncoded.getBytes("UTF-8"));
            }
            encoded = byteArray2HexString(bytes);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return encoded;
    }

    /**
     * Use provided cipher and key to decode encoded string.<b>
     * Synchronization will be placed on cipher object.<br>
     * 
     * @param cipher object to decode
     * @param key key for decryption
     * @param encoded string to be decoded
     * @return decoded string, null if decrypt fails
     */
    public static String syncDecode(final Cipher cipher, Key key, String encoded) {

        String decoded = null;
        try {
            byte[] bytes = hexString2ByteArray(encoded);
            if (bytes != null) {
                synchronized (cipher) {
                    cipher.init(Cipher.DECRYPT_MODE, key);
                    bytes = cipher.doFinal(bytes);
                }
                decoded = new String(bytes, "UTF-8");
            }
        }
        catch (Exception e) {
            // failed to decode return null
            log.debug("failed to decode: " + encoded, e);
            decoded = null;
        }
        return decoded;
    }

    /**
     * Turn byte array into hex string.
     * @param raw raw bytes to be turned into string
     * @return string representing the bytes
     */
    public static String byteArray2HexString(byte[] raw) {

//        StringBuilder sHex = new StringBuilder("");
//        for (int i = 0; i < raw.length; i++) {
//            sHex.append(byte2HexString(raw[i]));
//        }
//        return sHex.toString();
        return Base64.encodeBase64String(raw);
    }

    /**
     * Turn a single byte into hex string.
     * @param raw byte
     * @return hex string representation of the raw byte
     */
    public static String byte2HexString(byte raw) {

        StringBuilder sHex = new StringBuilder();
        int iByte = (raw & 0xf0) >> 4;
        sHex.append(Character.forDigit(iByte, 16));
        iByte = raw & 0xf;
        sHex.append(Character.forDigit(iByte, 16));
        return sHex.toString();
    }

    /**
     * Turn hex string into byte array.
     * @param sData string to be transformed to byte array
     * @return byte array reprensented by the input string
     */
    public static byte[] hexString2ByteArray(String sData) {

        if (StringUtils.isEmpty(sData)) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }
//        if (sData.startsWith("x") || sData.startsWith("X")) {
//            int start = sData.indexOf('\'');
//            int end = sData.lastIndexOf('\'');
//            sData = sData.substring(start + 1, end);
//        }
//        else if (sData.startsWith("0x") || sData.startsWith("0X")) {
//            sData = sData.substring(2);
//        }
//        if (sData.length() % 2 != 0) {
//            return (byte[]) null;
//        }
//        byte[] raw = new byte[sData.length() / 2];
//        try {
//            for (int i = 0; i < sData.length() / 2; i++)
//                raw[i] = (byte) Integer.parseInt(sData.substring(i * 2, i * 2 + 2), 16);
//
//        }
//        catch (Exception e) {
//            raw = (byte[]) null;
//        }
//        return raw;
        return Base64.decodeBase64(sData);
    }

}
