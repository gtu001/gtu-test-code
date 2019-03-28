package gtu.binary;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class EncryptAndDecryptHandler {

    // Objects required for encryption/decryption
    private final SecretKey secretKey;
    private final Base64.Encoder encoder;
    private final Base64.Decoder decoder;

    //key長度必須為 16, 24,32 byte
    public EncryptAndDecryptHandler(String key) {
        // In constructor
        try {
            this.secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            this.encoder = Base64.getUrlEncoder();
            this.decoder = Base64.getUrlDecoder();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("EncryptAndDecryptHandler ERR : " + e.getMessage(), e);
        }
    }

    public byte[] encrypt(byte[] plainTextByte) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(plainTextByte);
        } catch (Exception e) {
            throw new RuntimeException("encrypt ERR : " + e.getMessage(), e);
        }
    }

    public String encrypt(String plainText) {
        try {
            // Get byte array which has to be encrypted.
            byte[] plainTextByte = toByteArray(plainText);

            // Encrypt the bytes using the secret key
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedByte = cipher.doFinal(plainTextByte);

            // Use Base64 encoder to encode the byte array
            // into Base 64 representation. Requires Java 8.
            return encoder.encodeToString(encryptedByte);

        } catch (Exception e) {
            throw new RuntimeException("encrypt ERR : " + e.getMessage(), e);
        }
    }

    public byte[] decrypt(byte[] encryptedByte) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(encryptedByte);
        } catch (Exception e) {
            throw new RuntimeException("decrypt ERR : " + e.getMessage(), e);
        }
    }

    public String decrypt(String encrypted) {
        try {
            // Decode Base 64 String into bytes array.
            byte[] encryptedByte = decoder.decode(encrypted);

            // Do the decryption
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedByte = cipher.doFinal(encryptedByte);

            // Get hexadecimal string from the byte array.
            return toHexString(decryptedByte);
        } catch (Exception e) {
            throw new RuntimeException("decrypt ERR : " + e.getMessage(), e);
        }
    }

    private byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    private String toHexString(byte[] array) {
        return DatatypeConverter.printHexBinary(array).toLowerCase();
    }
}