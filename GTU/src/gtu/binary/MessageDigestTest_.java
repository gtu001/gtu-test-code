package gtu.binary;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;

/**
 * @author Troy 2009/02/02
 * 
 */
public class MessageDigestTest_ {

    public static void main(String[] args) {
        try {
            System.out.println(MessageDigestTest_.digest("12345678", "MD5"));
            System.out.println(MessageDigestTest_.toMd5("12345678".getBytes("ISO8859-1")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 字串轉16進位
     * 
     * @param b
     * @return
     */
    public static String toHexString(byte[] b) {
        StringBuffer hexString = new StringBuffer();
        String plainText;
        for (int ii = 0; ii < b.length; ii++) {
            plainText = Integer.toHexString(0xFF & b[ii]);
            if (plainText.length() < 2) {
                plainText = "0" + plainText;
            }
            hexString.append(plainText);
        }
        return hexString.toString();
    }

    /**
     * 將Byte轉為md5字串
     * 
     * @param b
     * @return
     */
    public static String toMd5(byte[] b) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(b);
            b = md.digest();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return toHexString(b);
    }

    /**
     * 將Byte轉為sha字串
     * 
     * @param b
     * @return
     */
    public static String toSha(byte[] b) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(b);
            b = md.digest();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return toHexString(b);
    }

    /**
     * 加密
     * 
     * @param cleartext
     *            字串
     * @param algorithm
     *            編碼
     * @return 加密後字串
     * @throws Exception
     */
    public static String digest(String cleartext, String algorithm) throws Exception {
        char hex[] = "0123456789abcdef".toCharArray();
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(cleartext.getBytes());
        byte digest[] = md.digest();
        StringBuffer sb = new StringBuffer(2 * digest.length);
        for (int i = 0; i < digest.length; i++) {
            int high = (digest[i] & 0xf0) >> 4;
            int low = digest[i] & 0xf;
            sb.append(hex[high]);
            sb.append(hex[low]);
        }
        return sb.toString();
    }

    /**
     * 將檔案加密或解密
     * 
     * @param encrypt
     *            true:加密 ;false:解密
     * @param mode
     *            加密模式 ECB/DES/ECB/PKCS#5
     * @param pwd
     *            金鑰
     * @param srcFile
     *            來源檔
     * @param destFile
     *            目的檔
     * @throws Exception
     */
    public static void crypt(boolean encrypt, String mode, String pwd, String srcFile, String destFile)
            throws Exception {
        Key key;
        KeyGenerator generator = KeyGenerator.getInstance("DES");
        generator.init(new SecureRandom(pwd.getBytes()));
        key = generator.generateKey();
        Cipher cipher = Cipher.getInstance(mode); // "ECB/DES/ECB/PKCS#5"
        if (encrypt)
            cipher.init(Cipher.ENCRYPT_MODE, key);
        else
            cipher.init(Cipher.DECRYPT_MODE, key);
        FileInputStream in = new FileInputStream(srcFile);
        FileOutputStream fileOut = new FileOutputStream(destFile);
        CipherOutputStream out = new CipherOutputStream(fileOut, cipher);
        byte[] buffer = new byte[8192];
        int length;
        while ((length = in.read(buffer)) != -1)
            out.write(buffer, 0, length);
        in.close();
        out.close();
    }
}
