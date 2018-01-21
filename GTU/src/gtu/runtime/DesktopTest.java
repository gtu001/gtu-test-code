package gtu.runtime;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 執行開檔列印等效果
 * 
 * @author Troy
 */
public class DesktopTest {

    /**
     * 開少女時代 genie日文版
     * 
     * @throws IOException
     * @throws URISyntaxException
     */
    public static void browseSnsd() {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("http://www.youtube.com/watch?v=XWVBicAvsNs"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private Logger log = Logger.getLogger(getClass());
    private Desktop test = null;

    // public static void main(String[] args){
    // }

    // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // log.debug("# setUpBeforeClass ...");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // log.debug("# tearDownAfterClass ...");
    }

    @Before
    public void setUp() throws Exception {
        // log.debug("# setUp ...");
        if (Desktop.isDesktopSupported()) {
            test = Desktop.getDesktop();
        }
    }

    @After
    public void tearDown() throws Exception {
        // log.debug("# tearDown ...");
        test = null;
    }

    // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    /**
     * 會用預設瀏覽器開啟網頁
     * 
     * @throws Exception
     */
    @Test
    public void testBrowse() throws Exception {
        log.debug("# testBrowse ...");
        URI url = new URI("www.gamer.com.tw");
        test.browse(url);
    }

    @Test
    public void testMail() throws Exception {
        log.debug("# testMail ...");
        URI url = new URI("mailto:gtu001@gmail.com");
        test.mail(url);
        test.mail();
    }

    /**
     * 開啟檔案
     * 
     * @throws Exception
     */
    @Test
    public void testOpen() throws Exception {
        log.debug("# testOpen ...");
        File file = new File("c:\\url.txt");
        test.open(file);
    }

    @Ignore
    @Test
    public void testIsSupported() throws Exception {
        log.debug("# testIsSupported ...");
        // log.debug("isSupported = " +
        // test.isSupported(java.awt.Desktop$Action));
    }

    @Test
    public void testPrint() throws Exception {
        log.debug("# testPrint ...");
        File file = new File("c:\\url.txt");
        test.print(file);
    }

    @Test
    public void testGetDesktop() throws Exception {
        log.debug("# testGetDesktop ...");
        log.debug("getDesktop = " + Desktop.getDesktop());
    }

    @Test
    public void testIsDesktopSupported() throws Exception {
        log.debug("# testIsDesktopSupported ...");
        log.debug("isDesktopSupported = " + Desktop.isDesktopSupported());
    }

    @Test
    public void testEdit() throws Exception {
        log.debug("# testEdit ...");
        File file = new File("c:\\url.txt");
        test.edit(file);
    }
}
