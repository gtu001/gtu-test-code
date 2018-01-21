package java_.net;

import java.security.AccessController;
import java.security.PrivilegedAction;

public class PrivClassLoader implements PrivilegedAction<ClassLoader> {
    private final Class<?> c;

    public static ClassLoader get(Class<?> c) {
        PrivClassLoader action = new PrivClassLoader(c);
        if (System.getSecurityManager() != null) {
            return ((ClassLoader) AccessController.doPrivileged(action));
        }
        return action.run();
    }

    private PrivClassLoader(Class<?> c) {
        this.c = c;
    }

    public ClassLoader run() {
        if (this.c != null) {
            return this.c.getClassLoader();
        }
        return Thread.currentThread().getContextClassLoader();
    }
}