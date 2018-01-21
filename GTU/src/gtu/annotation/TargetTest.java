package gtu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class TargetTest {

    @Target(value = { ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
            ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.PACKAGE })
    @Retention(RetentionPolicy.SOURCE)
    private @interface ReOrg {
        public enum Level {NONE, UNIT, INTEGRATION, FUNCTION};
        Level level() default Level.NONE;
        String name() default "name";
        String tester() default "tester";
    }

    @ReOrg
    private TargetTest() {
    }

    @ReOrg
    private String testReOrg;

    @ReOrg
    private void testReOrg(@ReOrg String test2) {
        @ReOrg
        String test = null;
        System.out.println("testReOrg...");
    }

    public static void main(String[] args) {
        System.out.println("done...");
    }
}
