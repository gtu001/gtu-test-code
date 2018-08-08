package gtu.class_;

public class ClassUtil {

    public static void main(String[] args) {
        System.out.println(ClassUtil.isAssignFrom(Character.class, char.class));
        System.out.println("done...");
    }

    public static boolean isAssignFrom(Class<?> left, Class<?> right){
        if(left.isPrimitive() && right.isPrimitive()){
            return left.isAssignableFrom(right);
        }else if(left.isPrimitive()){
            try {
                Class<?> pm = (Class<?>) right.getDeclaredField("TYPE").get(right);
                return pm == left;
            } catch (Exception e) {
                return false;
            }
        }else if(right.isPrimitive()){
            try {
                Class<?> pm = (Class<?>) left.getDeclaredField("TYPE").get(left);
                return pm == right;
            } catch (Exception e) {
                return false;
            }
        }else{
            return left.isAssignableFrom(right);
        }
    }
    
    public static boolean isPrimitiveOrWrapper(Class<?> clz) {
        Class<?>[] clzs = new Class[] { //
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
