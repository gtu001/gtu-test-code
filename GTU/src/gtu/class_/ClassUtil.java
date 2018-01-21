package gtu.class_;

import gtu.properties.PropertiesUtil;

import java.io.File;
import java.net.URL;


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
}
