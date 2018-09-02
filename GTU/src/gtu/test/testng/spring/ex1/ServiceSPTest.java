package gtu.test.testng.spring.ex1;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = IcpInitContextLoader.class)
@org.testng.annotations.Test
public class ServiceSPTest extends AbstractTestNGSpringContextTests {

//    @Resource(name = BatchAction.BEAN_DEFAULT)
//    private BatchAction batch;
    private Logger logger = Logger.getLogger(ServiceSPTest.class);
    
    @Test
    public void testOK() {
        logger.debug("######## testOk");
        System.out.println("done...");
    }
}