package gtu.ireport.ex1;
/**
 * Provides a uniform interface for any cipher process that only provides encode
 * ability.
 * @author Steven
 * @since 0.0.1
 */
public interface OneWayCipherProcessor {

    /**
     * Encodes a raw data string into a scrabbled string according to the
     * implement methodology.
     * @param toBeEncoded
     * @return encoded string
     * @throws BICipherException when error occurs while encoding
     * @throws IllegalArgumentException if input is null
     */
    String encode(String toBeEncoded) throws IllegalArgumentException;
}
