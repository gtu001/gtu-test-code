package gtu.net;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;

import sun.net.www.ParseUtil;

//@Ignore
public class ParseUtilTest {

    private Logger log = Logger.getLogger(getClass());

    //@Ignore
    @org.junit.Test
    public void testDecode() throws Exception {
        log.debug("# testDecode ...");
        URL url = this.getClass().getResource("");
        String test = url.toString();
        log.debug("\tdecode = " + ParseUtil.decode(test));
    }

    //@Ignore
    @org.junit.Test
    public void testCanonizeString() throws Exception {
        log.debug("# testCanonizeString ...");
        URL url = this.getClass().getResource("");
        String test = url.toString();
        log.debug("\tcanonizeString = " + new ParseUtil().canonizeString(test));
    }

    //@Ignore
    @org.junit.Test
    public void testFileToEncodedURL() throws Exception {
        log.debug("# testFileToEncodedURL ...");
        URL url = this.getClass().getResource("");
        File file = new File(url.getFile());
        log.debug("\tfileToEncodedURL = " + ParseUtil.fileToEncodedURL(file));
    }

    //@Ignore
    @org.junit.Test
    public void testEncodePath() throws Exception {
        log.debug("# testEncodePath ...");
        URL url = this.getClass().getResource("");
        String test = url.toString();
        log.debug("\tencodePath = " + ParseUtil.encodePath(test));
        log.debug("\tencodePath = " + ParseUtil.encodePath(test, true));
        log.debug("\tencodePath = " + ParseUtil.encodePath(test, false));
    }

    //@Ignore
    @org.junit.Test
    public void testToURI() throws Exception {
        log.debug("# testToURI ...");
        URL url = this.getClass().getResource("");
        log.debug("\ttoURI = " + ParseUtil.toURI(url));
    }
}
