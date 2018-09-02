package gtu.bean;

import gtu.reflect.ToStringUtil;

import java.beans.PropertyDescriptor;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;

@Ignore
public class PropertyDescriptorTest {

    private Logger log = Logger.getLogger(getClass());
    private PropertyDescriptor test = null;

    public static void main(String[] args) throws Exception {
        PropertyDescriptorTest test = new PropertyDescriptorTest();
        test.writeClassInfo();
    }

    @Ignore
    public void writeClassInfo() throws Exception {
        PropertyDescriptor test = new PropertyDescriptor("str", TestBean.class);

        // PUBLIC
        log.debug("\ttest.getPropertyEditorClass = " + ToStringUtil.toString(test.getPropertyEditorClass()));
        log.debug("\ttest.getPropertyType = " + ToStringUtil.toString(test.getPropertyType()));
        log.debug("\ttest.getReadMethod = " + ToStringUtil.toString(test.getReadMethod()));
        log.debug("\ttest.getWriteMethod = " + ToStringUtil.toString(test.getWriteMethod()));
        log.debug("\ttest.isBound = " + ToStringUtil.toString(test.isBound()));
        log.debug("\ttest.isConstrained = " + ToStringUtil.toString(test.isConstrained()));
    }

    class TestBean {
        private String str;

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }
    }

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
        test = new PropertyDescriptor("str", TestBean.class);
    }

    @After
    public void tearDown() throws Exception {
        // log.debug("# tearDown ...");
        test = null;
    }

    // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    // PUBLIC
    // @Ignore
    @org.junit.Test
    public void testGetPropertyType() throws Exception {
        log.debug("# testGetPropertyType ...");
        log.debug("\tgetPropertyType = " + test.getPropertyType());
    }

    // @Ignore
    @org.junit.Test
    public void testIsConstrained() throws Exception {
        log.debug("# testIsConstrained ...");
        log.debug("\tisConstrained = " + test.isConstrained());
    }

    // @Ignore
    @org.junit.Test
    public void testCreatePropertyEditor() throws Exception {
        log.debug("# testCreatePropertyEditor ...");
        log.debug("\tcreatePropertyEditor = " + test.createPropertyEditor(new Object()));
    }

    // @Ignore
    @org.junit.Test
    public void testGetPropertyEditorClass() throws Exception {
        log.debug("# testGetPropertyEditorClass ...");
        log.debug("\tgetPropertyEditorClass = " + test.getPropertyEditorClass());
    }

    // @Ignore
    @org.junit.Test
    public void testSetConstrained() throws Exception {
        log.debug("# testSetConstrained ...");
        test.setConstrained(true);
    }

    // @Ignore
    @org.junit.Test
    public void testSetPropertyEditorClass() throws Exception {
        log.debug("# testSetPropertyEditorClass ...");
        test.setPropertyEditorClass(String.class);
    }

    // @Ignore
    @org.junit.Test
    public void testGetWriteMethod() throws Exception {
        log.debug("# testGetWriteMethod ...");
        log.debug("\tgetWriteMethod = " + test.getWriteMethod());
    }

    // @Ignore
    @org.junit.Test
    public void testSetReadMethod() throws Exception {
        log.debug("# testSetReadMethod ...");
        test.setReadMethod(TestBean.class.getDeclaredMethod("getStr", new Class[0]));
    }

    // @Ignore
    @org.junit.Test
    public void testSetWriteMethod() throws Exception {
        log.debug("# testSetWriteMethod ...");
        test.setWriteMethod(TestBean.class.getDeclaredMethod("setStr", new Class[0]));
    }

    // @Ignore
    @org.junit.Test
    public void testIsBound() throws Exception {
        log.debug("# testIsBound ...");
        log.debug("\tisBound = " + test.isBound());
    }

    // @Ignore
    @org.junit.Test
    public void testSetBound() throws Exception {
        log.debug("# testSetBound ...");
        test.setBound(false);
    }

    // @Ignore
    @org.junit.Test
    public void testGetReadMethod() throws Exception {
        log.debug("# testGetReadMethod ...");
        log.debug("\tgetReadMethod = " + test.getReadMethod());
    }
}
