package gtu.reflect;

import java.lang.reflect.Method;

public class ReflectionInvokeSameMethod {
    
    private final static ReflectionInvokeSameMethod INST = new ReflectionInvokeSameMethod();
    private ReflectionInvokeSameMethod(){
    }
    
    public static ReflectionInvokeSameMethod getInstance(){
        return INST;
    }
    
    public Method getMappingEnclosingMethod(Object indicateObject, Class<?> clz){
        Method method = indicateObject.getClass().getEnclosingMethod();
        for(Method mth : clz.getMethods()){
            if(mth.getName().equals(method.getName()) && //
                    checkParamClz(mth.getParameterTypes(), method.getParameterTypes()) && //
                    checkParamClz(new Class[]{mth.getReturnType()}, new Class[]{method.getReturnType()})){//
                return mth;
            }
        }
        return null;
    }
    
    private boolean checkParamClz(Class<?>[] clz1, Class<?>[] clz2){
        if(clz1.length == clz2.length){
            for(int ii = 0 ; ii < clz1.length ; ii ++){
                if(clz1[ii] != clz2[ii]){
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    //--------------------------------------------------以下是測試
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
        T t = new ReflectionInvokeSameMethod().new T();
        t.xxxxx();
        System.out.println("done...");
    }
}
