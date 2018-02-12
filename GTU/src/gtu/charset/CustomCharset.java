package gtu.charset;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;

public class CustomCharset extends Charset {

    private static Map<Integer, Integer> CP937_UTF16 = new HashMap<Integer, Integer>();
    private static Map<Integer, Integer> UTF16_CP937 = new HashMap<Integer, Integer>();

    static {
        InputStream is = null;
        try {
            is = CustomCharset.class.getResourceAsStream("CP937UTF16.TXT");;
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "ISO-8859-1"));
            String r = null;
            while ((r = br.readLine()) != null) {
                if (!(r.startsWith("#"))) {
                    String[] ary = r.split(" ");
                    if (ary.length >= 2) {
                        int cp937 = Integer.parseInt(ary[0], 16);
                        int utf16 = Integer.parseInt(ary[1], 16);
                        CP937_UTF16.put(Integer.valueOf(cp937), Integer.valueOf(utf16));
                        UTF16_CP937.put(Integer.valueOf(utf16), Integer.valueOf(cp937));
                    } else {
                        System.out.println("[CP937UTF16][WARN] " + r);
                    }
                }
            }
            is.close();
            br.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException localIOException3) {
                }
            }
        }
    }

    protected CustomCharset() {
        super("CP937", new String[] { "CP937-FUCO" });
    }

    @Override
    public boolean contains(Charset cs) {
        return cs.contains(this);
    }

    @Override
    public CharsetDecoder newDecoder() {
        return new Decoder(this);
    }

    @Override
    public CharsetEncoder newEncoder() {
        return new Encoder(this);
    }

    private class Decoder extends DBCS_IBM_EBCDIC_Decoder {
        protected Decoder(Charset cs) {
            super(cs);
        }

        @Override
        protected char remapping(char v, char outputChar) {
            return super.remapping(v, outputChar);
        }
    }

    private class Encoder extends DBCS_IBM_EBCDIC_Encoder {
        protected Encoder(Charset cs) {
            super(cs);
        }

        @Override
        protected int remapping(int c, int v) {
            return super.remapping(c, v);
        }
    }
}
