package gtu.ireport.ex1;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

/**
 * Encrypt and decrypt using javax.crypto.Cipher. Only supports ECB padding. RSA
 * not tested. <br>
 * For CBC, API needs to be changed, see
 * http://stackoverflow.com/questions/6669181/why-does-my-aes-encryption-throws-an-invalidkeyexception.
 * 
 * @author Steven
 * @since 1.0.1
 * @see javax.crypto.Cipher
 */
public class JavaCryptoCipherProcessor implements CipherProcessor {

    private final Key key;
    private final Cipher cipher;

    /**
     * Constructor. Default uses AES algorithm.
     * 
     */
    public JavaCryptoCipherProcessor() {

        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            // "algorithm/mode/padding"
            this.cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            this.key = generator.generateKey();
        } catch (Exception e) {
            // should never happen, unless AES is not supported in runtime environment
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructor with self defined cipher and key.
     * 
     * @param cipher
     *            specified cipher
     * @param key
     *            key to pair and use with cipher
     */
    public JavaCryptoCipherProcessor(Cipher cipher, Key key) {
        this.key = key;
        this.cipher = cipher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String encode(String toBeEncoded) {
        return CipherUtil.syncEncode(cipher, key, toBeEncoded);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String decode(String encoded) {
        return CipherUtil.syncDecode(cipher, key, encoded);
    }

}
