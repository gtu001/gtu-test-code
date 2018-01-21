package gtu.properties;

import gtu.ant.ExcludeBuild;
import gtu.collection.ListUtil;
import gtu.reflect.ToStringUtil;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class PropertiesTest {

    private Logger log = Logger.getLogger(getClass());
    private Properties prop = null;

    @Before
    public void setUp() throws Exception {
        //log.debug("# setUp ...");                           
        prop = new Properties();
        try {
            prop.load(new FileInputStream(ExcludeBuild.class.getResource("needadd.properties").getFile()));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    @Test
    public void testShowClassInfo() {
        log.debug("# testShowClassInfo ...");
        log.debug(ToStringUtil.toString(Properties.class));
    }

    //@Ignore
    @org.junit.Test
    public void testPropertyNames() throws Exception {
        log.debug("# testPropertyNames ...");
        log.debug("\tpropertyNames = " + prop.propertyNames());
        ListUtil.showListInfo(prop.propertyNames());
    }

    //@Ignore
    @org.junit.Test
    public void testToString() throws Exception {
        log.debug("# testToString ...");
        log.debug("\ttoString = " + prop.toString());
    }

    //@Ignore
    @org.junit.Test
    public void testKeys() throws Exception {
        log.debug("# testKeys ...");
        log.debug("\tkeys = " + prop.keys());
        ListUtil.showListInfo(prop.keys());
    }

    //@Ignore
    @org.junit.Test
    public void testKeySet() throws Exception {
        log.debug("# testKeySet ...");
        log.debug("\tkeySet = " + prop.keySet());
        ListUtil.showListInfo(prop.keySet());
    }

    //@Ignore
    @org.junit.Test
    public void testStringPropertyNames() throws Exception {
        log.debug("# testStringPropertyNames ...");
        log.debug("\tstringPropertyNames = " + prop.stringPropertyNames());
        ListUtil.showListInfo(prop.stringPropertyNames());
    }

    //@Ignore
    @org.junit.Test
    public void testValues() throws Exception {
        log.debug("# testValues ...");
        log.debug("\tvalues = " + prop.values());
        ListUtil.showListInfo(prop.values());
    }

    //@Ignore
    @org.junit.Test
    public void testElements() throws Exception {
        log.debug("# testElements ...");
        log.debug("\telements = " + prop.elements());
        ListUtil.showListInfo(prop.elements());
    }

    //@Ignore
    @org.junit.Test
    public void testEntrySet() throws Exception {
        log.debug("# testEntrySet ...");
        log.debug("\tentrySet = " + prop.entrySet());
        ListUtil.showListInfo(prop.entrySet());
    }
}
