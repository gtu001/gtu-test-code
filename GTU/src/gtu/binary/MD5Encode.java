package gtu.binary;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Steven 2012/1/11
 */
public class MD5Encode {
    private static final String MD5 = "MD5";

    public static String getMD5Digest(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance(MD5);
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            return number.toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getMD5HexDigest(String input) {
        byte[] defaultBytes = input.getBytes();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(MD5);
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();

            StringBuffer sbHexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                sbHexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            // System.out.println("input: "+input+" md5 version is "+sbHexString.toString());
            // input=sbHexString+"";
            /*
             * for (byte b : algorithm.digest()) System.out.printf("%02x",
             * 0xFF&b);
             */
            return sbHexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

}
