package _temp;

import gtu.reflect.ReflectionInvokeSameMethod;

import java.lang.reflect.Method;

public class Test25 {
    
    interface VVV {
        void xxxxx();
    }
    
    class T {
        void xxxxx(){
            Method method = ReflectionInvokeSameMethod.getInstance().getMappingEnclosingMethod(new Object(){}, VVV.class);
            System.out.println("<<<<<<" + method);
        }
    }

    public static void main(String[] args) throws Exception {
        T t = new Test25().new T();
        t.xxxxx();
        System.out.println("done...");
    }
}
