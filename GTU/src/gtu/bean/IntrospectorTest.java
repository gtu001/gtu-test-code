package gtu.bean;

import gtu.reflect.ToStringUtil;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;

public class IntrospectorTest {

    private Logger log = Logger.getLogger(getClass());

    @Ignore()
    public static void main(String[] args) {
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
    }

    @After
    public void tearDown() throws Exception {
        // log.debug("# tearDown ...");
    }

    // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

    // PUBLIC
    // @Ignore
    @org.junit.Test
    public void testGetBeanInfo1() throws Exception {
        log.debug("# testGetBeanInfo1 ...");
        Test t = new Test();
        this.showPropertyDescriptor(t);
    }

    @org.junit.Test
    public void testGetBeanInfo2() throws Exception {
        log.debug("# testGetBeanInfo2 ...");
        Test2 t = new Test2();
        this.showPropertyDescriptor(t);
    }

    /**
     * 用來抓class 的field 的 getter setter的 method
     * 
     * @param obj
     * @throws IntrospectionException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private void showPropertyDescriptor(Object obj) throws IntrospectionException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        BeanInfo bi = Introspector.getBeanInfo(obj.getClass(), Object.class);

        log.debug("\tbi.getAdditionalBeanInfo = " + ToStringUtil.toString(bi.getAdditionalBeanInfo()));
        log.debug("\tbi.getBeanDescriptor = " + ToStringUtil.toString(bi.getBeanDescriptor()));
        log.debug("\tbi.getDefaultEventIndex = " + ToStringUtil.toString(bi.getDefaultEventIndex()));
        log.debug("\tbi.getDefaultPropertyIndex = " + ToStringUtil.toString(bi.getDefaultPropertyIndex()));
        log.debug("\tbi.getEventSetDescriptors = " + ToStringUtil.toString(bi.getEventSetDescriptors()));
        log.debug("\tbi.getMethodDescriptors = " + ToStringUtil.toString(bi.getMethodDescriptors()));
        log.debug("\tbi.getPropertyDescriptors = " + ToStringUtil.toString(bi.getPropertyDescriptors()));

        PropertyDescriptor[] props = bi.getPropertyDescriptors();
        log.debug("\tprops.length = " + props.length);
        PropertyDescriptor test = null;
        for (int i = 0; i < props.length; i++) {
            test = props[i];
            log.debug("---------------------------------------------------------");
            // log.debug("\tgetPropertyEditorClass = " +
            // test.getPropertyEditorClass());
            log.debug("\tgetPropertyType = " + test.getPropertyType());
            log.debug("\tgetReadMethod = " + test.getReadMethod());
            log.debug("\tgetWriteMethod = " + test.getWriteMethod());
            // log.debug("\tisBound = " + test.isBound());
            // log.debug("\tisConstrained = " + test.isConstrained());
            log.debug("---------------------------------------------------------");
        }
    }

    // @Ignore
    @org.junit.Test
    public void testGetBeanInfoSearchPath() throws Exception {
        log.debug("# testGetBeanInfoSearchPath ...");
        log.debug("\tgetBeanInfoSearchPath = " + Arrays.toString(Introspector.getBeanInfoSearchPath()));
    }

    // @Ignore
    @org.junit.Test
    public void testFlushCaches() throws Exception {
        log.debug("# testFlushCaches ...");
        Introspector.flushCaches();
    }

    // @Ignore
    @org.junit.Test
    public void testFlushFromCaches() throws Exception {
        log.debug("# testFlushFromCaches ...");
        Introspector.flushFromCaches(Class.class);
    }

    // @Ignore
    @org.junit.Test
    public void testDecapitalize() throws Exception {
        log.debug("# testDecapitalize ...");
        log.debug("\tdecapitalize = " + Introspector.decapitalize("DECAPITALIZE"));// 連續兩個大寫忽略
        log.debug("\tdecapitalize = " + Introspector.decapitalize("Decapitalize"));// 首字變小寫
        log.debug("\tdecapitalize = " + Introspector.decapitalize("DEcapitalize"));
    }

    // @Ignore
    @org.junit.Test
    public void testSetBeanInfoSearchPath() throws Exception {
        log.debug("# testSetBeanInfoSearchPath ...");
        Introspector.setBeanInfoSearchPath(new String[0]);
    }

    class Test {
        private String str;
        private int ii;
        private boolean bool;

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }

        public int getIi() {
            return ii;
        }

        public void setIi(int ii) {
            this.ii = ii;
        }

        public boolean isBool() {
            return bool;
        }

        public void setBool(boolean bool) {
            this.bool = bool;
        }
    }

    class Test2 {

        public String getStrXXXX() {
            return null;
        }

        public void setStr(String str) {
        }

        public int getIi() {
            return 0;
        }

        public void setIiXXXX(int ii) {
        }

        public boolean isBool() {
            return false;
        }

        public void setBool(boolean bool) {
        }
    }
}
