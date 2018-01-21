package gtu.mock;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockitoUtil {

    private static Logger log = LoggerFactory.getLogger(MockitoUtil.class);

    /**
     * @param args
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println("done...");
    }

    public static Object createMockitoObject(String classpath) throws ClassNotFoundException {
        classpath = classpath.trim();
        log.debug("classpath = [{}]", classpath);
        return Mockito.mock(Class.forName(classpath));
    }

    public static Object createMockitoObject(String classpath, String jarPath) throws ClassNotFoundException, MalformedURLException {
        classpath = classpath.trim();
        jarPath = jarPath.trim();
        log.debug("classpath = [{}]", classpath);
        log.debug("jarPath = [{}]", jarPath);
        URLClassLoader loader = new URLClassLoader(new URL[] { new File(jarPath).toURL() });
        Class<?> clz = loader.loadClass(classpath);
        return Mockito.mock(clz);
    }
}
