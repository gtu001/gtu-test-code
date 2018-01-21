package gtu.junit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringInitContextLoader.class)
//@org.testng.annotations.Test
public class SpringInitTest {

    @Test
    public void testTest(){
        System.out.println("done...");
    }
}
