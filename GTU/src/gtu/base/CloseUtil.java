package gtu.base;

import java.lang.reflect.Method;

public class CloseUtil {

    public static void close(Object is) {
        try {
            if (is != null) {
                Method mth = is.getClass().getMethod("close", new Class[0]);
                mth.invoke(is, new Object[0]);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
