package gtu.util;

/**
 * Created by wistronits on 2018/8/8.
 */

public class ClassUtil {

    public static boolean isPrimitiveOrWrapper(Class<?> clz) {
        Class<?>[] clzs = new Class[]{ //
                int.class, Integer.class, //
                long.class, Long.class, //
                short.class, Short.class, //
                float.class, Float.class, //
                double.class, Double.class, //
                byte.class, Byte.class, //
                boolean.class, Boolean.class,//
        };
        for (Class<?> c : clzs) {
            if (c == clz) {
                return true;
            }
        }
        return false;
    }
}
