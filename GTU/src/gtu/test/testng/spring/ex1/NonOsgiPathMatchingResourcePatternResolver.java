package gtu.test.testng.spring.ex1;

import java.lang.reflect.Field;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * in the some application server builded on osgi,such as websphere,the
 * PathMatchingResourcePatternResolver can't be normal work,so,we force the
 * equinoxResolveMethod is null,and the resources in osgi will be ignored.
 *
 * create by aximo.yang in iCP at Dec 2, 2009
 */
public class NonOsgiPathMatchingResourcePatternResolver extends PathMatchingResourcePatternResolver {

    protected static String OSGI_FIELD_NAME = "equinoxResolveMethod";

    /**
     * @param classLoader
     */
    public NonOsgiPathMatchingResourcePatternResolver(ClassLoader classLoader) {
        super(classLoader);
        try {
            Field f_equinoxResolveMethod = this.getClass().getSuperclass().getDeclaredField(OSGI_FIELD_NAME);
            f_equinoxResolveMethod.setAccessible(true);
            f_equinoxResolveMethod.set(this, null);
        } catch (Exception e) {
            System.out.println("the spring jar is supported although it's a lower version");
            // e.printStackTrace();
            // do nothing
        }
    }

    public NonOsgiPathMatchingResourcePatternResolver() {
        this(NonOsgiPathMatchingResourcePatternResolver.class.getClassLoader());
    }
}