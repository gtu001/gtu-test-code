package gtu.bean;

import java.beans.Beans;
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
public class BeansTest {

    private Logger log = Logger.getLogger(getClass());
    private Beans bean = null; 


    public static void main(String[] args) throws Exception{
        BeansTest test = new BeansTest();
        test.writeClassInfo();
    }

    @Ignore
    public void writeClassInfo() throws Exception {
        Beans bean = new Beans();

        //PUBLIC
        //null
        log.debug("\tBeans.isDesignTime = " + ToStringUtil.toString(Beans.isDesignTime()));
        //null
        log.debug("\tBeans.isGuiAvailable = " + ToStringUtil.toString(Beans.isGuiAvailable()));
        //-------------------------------------------------------------------------
        //null
        log.debug("\tBeans.getInstanceOf = " + ToStringUtil.toString(Beans.getInstanceOf(java.lang.Object, java.lang.Class)));
        //null
        log.debug("\tBeans.instantiate = " + ToStringUtil.toString(Beans.instantiate(java.lang.ClassLoader, java.lang.String, java.beans.beancontext.BeanContext, java.beans.AppletInitializer)));
        //null
        log.debug("\tBeans.instantiate = " + ToStringUtil.toString(Beans.instantiate(java.lang.ClassLoader, java.lang.String)));
        //null
        log.debug("\tBeans.instantiate = " + ToStringUtil.toString(Beans.instantiate(java.lang.ClassLoader, java.lang.String, java.beans.beancontext.BeanContext)));
        //null
        log.debug("\tBeans.isInstanceOf = " + ToStringUtil.toString(Beans.isInstanceOf(java.lang.Object, java.lang.Class)));
        //null
        Beans.setDesignTime(boolean);
        //null
        Beans.setGuiAvailable(boolean);
    }



    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    @BeforeClass                                                       
    public static void setUpBeforeClass() throws Exception {           
        //log.debug("# setUpBeforeClass ...");                
    }                                                                  
    @AfterClass                                                        
    public static void tearDownAfterClass() throws Exception {         
        //log.debug("# tearDownAfterClass ...");              
    }                                                                  
    @Before                                                            
    public void setUp() throws Exception {                             
        //log.debug("# setUp ...");                           
         bean = new Beans();
    }                                                                  
    @After                                                             
    public void tearDown() throws Exception {                          
        //log.debug("# tearDown ...");                        
         bean = null;
    }                                                                  
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX



        //PUBLIC
    /**
    * null
    */
    //@Ignore
    @org.junit.Test
    public void testIsInstanceOf() throws Exception {
        log.debug("# testIsInstanceOf ...");
        log.debug("\tisInstanceOf = " + Beans.isInstanceOf(java.lang.Object, java.lang.Class));
    }
    /**
    * null
    */
    //@Ignore
    @org.junit.Test
    public void testIsGuiAvailable() throws Exception {
        log.debug("# testIsGuiAvailable ...");
        log.debug("\tisGuiAvailable = " + Beans.isGuiAvailable());
    }
    /**
    * null
    */
    //@Ignore
    @org.junit.Test
    public void testInstantiate() throws Exception {
        log.debug("# testInstantiate ...");
        log.debug("\tinstantiate = " + Beans.instantiate(java.lang.ClassLoader, java.lang.String, java.beans.beancontext.BeanContext, java.beans.AppletInitializer));
    /**
    * null
    */
        log.debug("\tinstantiate = " + Beans.instantiate(java.lang.ClassLoader, java.lang.String));
    /**
    * null
    */
        log.debug("\tinstantiate = " + Beans.instantiate(java.lang.ClassLoader, java.lang.String, java.beans.beancontext.BeanContext));
    }
    /**
    * null
    */
    //@Ignore
    @org.junit.Test
    public void testGetInstanceOf() throws Exception {
        log.debug("# testGetInstanceOf ...");
        log.debug("\tgetInstanceOf = " + Beans.getInstanceOf(java.lang.Object, java.lang.Class));
    }
    /**
    * null
    */
    //@Ignore
    @org.junit.Test
    public void testIsDesignTime() throws Exception {
        log.debug("# testIsDesignTime ...");
        log.debug("\tisDesignTime = " + Beans.isDesignTime());
    }
    /**
    * null
    */
    //@Ignore
    @org.junit.Test
    public void testSetGuiAvailable() throws Exception {
        log.debug("# testSetGuiAvailable ...");
        Beans.setGuiAvailable(boolean);
    }
    /**
    * null
    */
    //@Ignore
    @org.junit.Test
    public void testSetDesignTime() throws Exception {
        log.debug("# testSetDesignTime ...");
        Beans.setDesignTime(boolean);
    }
}
