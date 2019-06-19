package gtu.ireport.ex1;
/**
 * Provides a uniform interface for any cipher process that provides both encode
 * and decode ability.
 * @author Steven
 * @since 0.0.1
 */
public interface CipherProcessor extends OneWayCipherProcessor {

    /**
     * Decodes the encode/scrambled string. Decoded value must be equal to the
     * String before encode. {@code AssertEquals(str, decode(encode(str)))}
     * @param encoded
     * @return original string before encoded
     * @throws BICipherException when error occurs while decoding
     * @throws @throws IllegalArgumentException if input is null
     */
    String decode(String encoded);
}
