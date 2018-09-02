package gtu.bean;

import java.beans.beancontext.BeanContextSupport;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
//import org.junit.Test;
import org.junit.Ignore;
import org.apache.log4j.Logger;

import gtu.reflect.ToStringUtil;

@Ignore
public class BeanContextSupportTest {

    private Logger log = Logger.getLogger(getClass());
    private BeanContextSupport test = null;

    public static void main(String[] args) throws Exception {
        BeanContextSupportTest test = new BeanContextSupportTest();
        test.writeClassInfo();
    }

    @Ignore
    public void writeClassInfo() throws Exception {
        BeanContextSupport test = new BeanContextSupport();

        //PUBLIC
        test.clear();
        log.debug("\ttest.isEmpty = " + ToStringUtil.toString(test.isEmpty()));
        log.debug("\ttest.iterator = " + ToStringUtil.toString(test.iterator()));
        log.debug("\ttest.size = " + ToStringUtil.toString(test.size()));
        log.debug("\ttest.toArray = " + ToStringUtil.toString(test.toArray()));
        log.debug("\ttest.avoidingGui = " + ToStringUtil.toString(test.avoidingGui()));
        test.dontUseGui();
        log.debug("\ttest.getBeanContextPeer = " + ToStringUtil.toString(test.getBeanContextPeer()));
        log.debug("\ttest.getLocale = " + ToStringUtil.toString(test.getLocale()));
        log.debug("\ttest.isDesignTime = " + ToStringUtil.toString(test.isDesignTime()));
        log.debug("\ttest.isSerializing = " + ToStringUtil.toString(test.isSerializing()));
        log.debug("\ttest.needsGui = " + ToStringUtil.toString(test.needsGui()));
        test.okToUseGui();
        //-------------------------------------------------------------------------
        log.debug("\ttest.add = " + ToStringUtil.toString(test.add(java.lang.Object)));
        log.debug("\ttest.contains = " + ToStringUtil.toString(test.contains(java.lang.Object)));
        log.debug("\ttest.addAll = " + ToStringUtil.toString(test.addAll(java.util.Collection)));
        log.debug("\ttest.getResource = " + ToStringUtil.toString(test.getResource(java.lang.String, java.beans.beancontext.BeanContextChild)));
        log.debug("\ttest.getResourceAsStream = " + ToStringUtil.toString(test.getResourceAsStream(java.lang.String, java.beans.beancontext.BeanContextChild)));
        log.debug("\ttest.toArray = " + ToStringUtil.toString(test.toArray([Ljava.lang.Object;)));
        log.debug("\ttest.remove = " + ToStringUtil.toString(test.remove(java.lang.Object)));
        log.debug("\ttest.containsKey = " + ToStringUtil.toString(test.containsKey(java.lang.Object)));
        log.debug("\ttest.containsAll = " + ToStringUtil.toString(test.containsAll(java.util.Collection)));
        log.debug("\ttest.removeAll = " + ToStringUtil.toString(test.removeAll(java.util.Collection)));
        log.debug("\ttest.retainAll = " + ToStringUtil.toString(test.retainAll(java.util.Collection)));
        test.addBeanContextMembershipListener(java.beans.beancontext.BeanContextMembershipListener);
        log.debug("\ttest.instantiateChild = " + ToStringUtil.toString(test.instantiateChild(java.lang.String)));
        test.propertyChange(java.beans.PropertyChangeEvent);
        test.readChildren(java.io.ObjectInputStream);
        test.removeBeanContextMembershipListener(java.beans.beancontext.BeanContextMembershipListener);
        test.setDesignTime(boolean);
        test.setLocale(java.util.Locale);
        test.vetoableChange(java.beans.PropertyChangeEvent);
        test.writeChildren(java.io.ObjectOutputStream);
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
        test = new BeanContextSupport();
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
    public void testGetBeanContextPeer() throws Exception {
        log.debug("# testGetBeanContextPeer ...");
        log.debug("\tgetBeanContextPeer = " + test.getBeanContextPeer());
    }

    // @Ignore
    @org.junit.Test
    public void testContainsKey() throws Exception {
        log.debug("# testContainsKey ...");
        log.debug("\tcontainsKey = " + test.containsKey(java.lang.Object));
    }

    // @Ignore
    @org.junit.Test
    public void testGetLocale() throws Exception {
        log.debug("# testGetLocale ...");
        log.debug("\tgetLocale = " + test.getLocale());
    }

    // @Ignore
    @org.junit.Test
    public void testDontUseGui() throws Exception {
        log.debug("# testDontUseGui ...");
        test.dontUseGui();
    }

    // @Ignore
    @org.junit.Test
    public void testReadChildren() throws Exception {
        log.debug("# testReadChildren ...");
        test.readChildren(java.io.ObjectInputStream);
    }

    // @Ignore
    @org.junit.Test
    public void testAddBeanContextMembershipListener() throws Exception {
        log.debug("# testAddBeanContextMembershipListener ...");
        test.addBeanContextMembershipListener(java.beans.beancontext.BeanContextMembershipListener);
    }

    // @Ignore
    @org.junit.Test
    public void testIsDesignTime() throws Exception {
        log.debug("# testIsDesignTime ...");
        log.debug("\tisDesignTime = " + test.isDesignTime());
    }

    // @Ignore
    @org.junit.Test
    public void testIsSerializing() throws Exception {
        log.debug("# testIsSerializing ...");
        log.debug("\tisSerializing = " + test.isSerializing());
    }

    // @Ignore
    @org.junit.Test
    public void testNeedsGui() throws Exception {
        log.debug("# testNeedsGui ...");
        log.debug("\tneedsGui = " + test.needsGui());
    }

    // @Ignore
    @org.junit.Test
    public void testGetResourceAsStream() throws Exception {
        log.debug("# testGetResourceAsStream ...");
        log.debug("\tgetResourceAsStream = "
                + test.getResourceAsStream(java.lang.String, java.beans.beancontext.BeanContextChild));
    }

    // @Ignore
    @org.junit.Test
    public void testAddAll() throws Exception {
        log.debug("# testAddAll ...");
        log.debug("\taddAll = " + test.addAll(java.util.Collection));
    }

    // @Ignore
    @org.junit.Test
    public void testToArray() throws Exception {
        log.debug("# testToArray ...");
        log.debug("\ttoArray = " + test.toArray([Ljava.lang.Object;));
        log.debug("\ttoArray = " + test.toArray());
    }

    // @Ignore
    @org.junit.Test
    public void testIterator() throws Exception {
        log.debug("# testIterator ...");
        log.debug("\titerator = " + test.iterator());
    }

    // @Ignore
    @org.junit.Test
    public void testSetLocale() throws Exception {
        log.debug("# testSetLocale ...");
        test.setLocale(java.util.Locale);
    }

    // @Ignore
    @org.junit.Test
    public void testSize() throws Exception {
        log.debug("# testSize ...");
        log.debug("\tsize = " + test.size());
    }

    // @Ignore
    @org.junit.Test
    public void testWriteChildren() throws Exception {
        log.debug("# testWriteChildren ...");
        test.writeChildren(java.io.ObjectOutputStream);
    }

    // @Ignore
    @org.junit.Test
    public void testOkToUseGui() throws Exception {
        log.debug("# testOkToUseGui ...");
        test.okToUseGui();
    }

    // @Ignore
    @org.junit.Test
    public void testIsEmpty() throws Exception {
        log.debug("# testIsEmpty ...");
        log.debug("\tisEmpty = " + test.isEmpty());
    }

    // @Ignore
    @org.junit.Test
    public void testRemoveBeanContextMembershipListener() throws Exception {
        log.debug("# testRemoveBeanContextMembershipListener ...");
        test.removeBeanContextMembershipListener(java.beans.beancontext.BeanContextMembershipListener);
    }

    // @Ignore
    @org.junit.Test
    public void testClear() throws Exception {
        log.debug("# testClear ...");
        test.clear();
    }

    // @Ignore
    @org.junit.Test
    public void testVetoableChange() throws Exception {
        log.debug("# testVetoableChange ...");
        test.vetoableChange(java.beans.PropertyChangeEvent);
    }

    // @Ignore
    @org.junit.Test
    public void testRemoveAll() throws Exception {
        log.debug("# testRemoveAll ...");
        log.debug("\tremoveAll = " + test.removeAll(java.util.Collection));
    }

    // @Ignore
    @org.junit.Test
    public void testPropertyChange() throws Exception {
        log.debug("# testPropertyChange ...");
        test.propertyChange(java.beans.PropertyChangeEvent);
    }

    // @Ignore
    @org.junit.Test
    public void testSetDesignTime() throws Exception {
        log.debug("# testSetDesignTime ...");
        test.setDesignTime(boolean);
    }

    // @Ignore
    @org.junit.Test
    public void testGetResource() throws Exception {
        log.debug("# testGetResource ...");
        log.debug("\tgetResource = " + test.getResource(java.lang.String, java.beans.beancontext.BeanContextChild));
    }

    // @Ignore
    @org.junit.Test
    public void testRetainAll() throws Exception {
        log.debug("# testRetainAll ...");
        log.debug("\tretainAll = " + test.retainAll(java.util.Collection));
    }

    // @Ignore
    @org.junit.Test
    public void testRemove() throws Exception {
        log.debug("# testRemove ...");
        log.debug("\tremove = " + test.remove(java.lang.Object));
    }

    // @Ignore
    @org.junit.Test
    public void testAvoidingGui() throws Exception {
        log.debug("# testAvoidingGui ...");
        log.debug("\tavoidingGui = " + test.avoidingGui());
    }

    // @Ignore
    @org.junit.Test
    public void testInstantiateChild() throws Exception {
        log.debug("# testInstantiateChild ...");
        log.debug("\tinstantiateChild = " + test.instantiateChild(java.lang.String));
    }

    // @Ignore
    @org.junit.Test
    public void testAdd() throws Exception {
        log.debug("# testAdd ...");
        log.debug("\tadd = " + test.add(java.lang.Object));
    }

    // @Ignore
    @org.junit.Test
    public void testContains() throws Exception {
        log.debug("# testContains ...");
        log.debug("\tcontains = " + test.contains(java.lang.Object));
    }

    // @Ignore
    @org.junit.Test
    public void testContainsAll() throws Exception {
        log.debug("# testContainsAll ...");
        log.debug("\tcontainsAll = " + test.containsAll(java.util.Collection));
    }
}
