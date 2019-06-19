package gtu.ireport.ex1;

import java.nio.charset.Charset;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Encrypt and decrypt using javax.crypto.Cipher. Strictly using "Blowfish" algorithm.
 * @author Steven
 * @version 1.1
 *
 */
public class BlowfishCipherProcessor implements CipherProcessor {

    private final JavaCryptoCipherProcessor processor;

    /**
     * Constructing a CipherProcessor using Blowfish algorithm with custom key.
     * @param keyString key used to encrypt or decrypt. Key must be 8 or 16 ascii characters. 
     */
    public BlowfishCipherProcessor(String keyString) {
        try {
            final Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
            final Key key = new SecretKeySpec(keyString.getBytes(Charset.forName("UTF-8")), "Blowfish");
            processor = new JavaCryptoCipherProcessor(cipher, key);
        }
        catch (NoSuchPaddingException e) {
            throw new RuntimeException("no such padding", e);
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Blowfish is not supported on this JVM", e);
        }
    }
    
    /**
     * Constructing a CipherProcessor using Blowfish algorithm with predefined key. 
     * Instances created with this constructor can be used to encrypt or decrypt values created by another instance created with same constructor.
     */
    public BlowfishCipherProcessor() {
        this("Soft8I^Corp0rate");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String encode(String toBeEncoded) throws IllegalArgumentException {
        return processor.encode(toBeEncoded);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String decode(String encoded) {
        return processor.decode(encoded);
    }
    
    
    /**
     * 
     * A static method provide direct access to BiSimpleCipherProcessor's decode.
     * @param input encoded string
     * @return decoded string
     */
    public static String staticDecode(String input) {
        
        CipherProcessor bcp = new BlowfishCipherProcessor();
        return bcp.decode(input);
    }

    /**
     * 
     * Usage : {@code java com.bi.base.cipher.BlowfishCipherProcessor [encode|decode] <string>}
     * @param args [0] encode or decode, [1] string to be encoded or decoded
     */
    public static void main(String[] args) {

        
        if (args.length < 2) {
            System.out.println("Usage : java " + BlowfishCipherProcessor.class.getName() + " [encode|decode] <string>");
        }

        CipherProcessor bcp = new BlowfishCipherProcessor();
        String encStr = "";
        if (args[0].equals("encode")) {
            encStr = bcp.encode(args[1]);
        }
        else if (args[0].equals("decode")) {
            encStr = bcp.decode(args[1]);
        }
        System.out.println(encStr);
    }
}
