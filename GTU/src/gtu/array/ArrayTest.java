package gtu.array;

import java.lang.reflect.Array;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.apache.log4j.Logger;

public class ArrayTest {

    private Logger log = Logger.getLogger(getClass());
    private Array test = null; 


    //public static void main(String[] args){
    //}


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
         test = new Array();
    }                                                                  
    @After                                                             
    public void tearDown() throws Exception {                          
        //log.debug("# tearDown ...");                        
         test = null;
    }                                                                  
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX



        //PUBLIC
    @Test
    public void testSetByte() throws Exception {
        log.debug("# testSetByte ...");
        Array.setByte(java.lang.Object, int, byte);
    }
    @Test
    public void testSetShort() throws Exception {
        log.debug("# testSetShort ...");
        Array.setShort(java.lang.Object, int, short);
    }
    @Test
    public void testSetDouble() throws Exception {
        log.debug("# testSetDouble ...");
        Array.setDouble(java.lang.Object, int, double);
    }
    @Test
    public void testGetFloat() throws Exception {
        log.debug("# testGetFloat ...");
        log.debug("getFloat = " + Array.getFloat(java.lang.Object, int));
    }
    @Test
    public void testNewInstance() throws Exception {
        log.debug("# testNewInstance ...");
        log.debug("newInstance = " + Array.newInstance(java.lang.Class, int));
        log.debug("newInstance = " + Array.newInstance(java.lang.Class, [I));
    }
    @Test
    public void testSetLong() throws Exception {
        log.debug("# testSetLong ...");
        Array.setLong(java.lang.Object, int, long);
    }
    @Test
    public void testGetLength() throws Exception {
        log.debug("# testGetLength ...");
        log.debug("getLength = " + Array.getLength(java.lang.Object));
    }
    @Test
    public void testSetInt() throws Exception {
        log.debug("# testSetInt ...");
        Array.setInt(java.lang.Object, int, int);
    }
    @Test
    public void testGetChar() throws Exception {
        log.debug("# testGetChar ...");
        log.debug("getChar = " + Array.getChar(java.lang.Object, int));
    }
    @Test
    public void testGetBoolean() throws Exception {
        log.debug("# testGetBoolean ...");
        log.debug("getBoolean = " + Array.getBoolean(java.lang.Object, int));
    }
    @Test
    public void testGetByte() throws Exception {
        log.debug("# testGetByte ...");
        log.debug("getByte = " + Array.getByte(java.lang.Object, int));
    }
    @Test
    public void testSet() throws Exception {
        log.debug("# testSet ...");
        Array.set(java.lang.Object, int, java.lang.Object);
    }
    @Test
    public void testGetInt() throws Exception {
        log.debug("# testGetInt ...");
        log.debug("getInt = " + Array.getInt(java.lang.Object, int));
    }
    @Test
    public void testGet() throws Exception {
        log.debug("# testGet ...");
        log.debug("get = " + Array.get(java.lang.Object, int));
    }
    @Test
    public void testGetDouble() throws Exception {
        log.debug("# testGetDouble ...");
        log.debug("getDouble = " + Array.getDouble(java.lang.Object, int));
    }
    @Test
    public void testGetShort() throws Exception {
        log.debug("# testGetShort ...");
        log.debug("getShort = " + Array.getShort(java.lang.Object, int));
    }
    @Test
    public void testSetChar() throws Exception {
        log.debug("# testSetChar ...");
        Array.setChar(java.lang.Object, int, char);
    }
    @Test
    public void testSetFloat() throws Exception {
        log.debug("# testSetFloat ...");
        Array.setFloat(java.lang.Object, int, float);
    }
    @Test
    public void testSetBoolean() throws Exception {
        log.debug("# testSetBoolean ...");
        Array.setBoolean(java.lang.Object, int, boolean);
    }
    @Test
    public void testGetLong() throws Exception {
        log.debug("# testGetLong ...");
        log.debug("getLong = " + Array.getLong(java.lang.Object, int));
    }
}
