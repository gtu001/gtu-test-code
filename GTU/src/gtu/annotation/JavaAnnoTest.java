package gtu.annotation;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class JavaAnnoTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        JavaAnnoTest test = new JavaAnnoTest();
    }

    @PostConstruct
    private void testPostConstruct() {
        System.out.println("# PostConstruct ...");
    }

    @PreDestroy
    private void testPreDestroy() {
        System.out.println("# PostConstruct ...");
    }
}
