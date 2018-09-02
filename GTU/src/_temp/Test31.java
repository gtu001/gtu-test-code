package _temp;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

import gtu.log.finder.DebugMointerMappingFieldForMethod.DebugMointerUIMapping;

public class Test31 {
    
    @Retention(RetentionPolicy.RUNTIME)
    @interface TestMapping {
    }

    public void testMethod(String noAnnotation, @DebugMointerUIMapping(index=1) String withAnnotation) {
    }

    public static void main(String[] args) throws Exception {
        Method method = Test31.class.getDeclaredMethod("testMethod", String.class, String.class);
        Test31 t = new Test31();
        
        DebugMointerUIMapping val = (DebugMointerUIMapping)t.getDebugMointerUIMappingDefine(method).get(1);
        System.out.println(val.index());
    }
    
    /**
     * 取得method 定義
     */
    private Map<Integer, DebugMointerUIMapping> getDebugMointerUIMappingDefine(Method method){
        Map<Integer, DebugMointerUIMapping> defMap = new TreeMap<Integer, DebugMointerUIMapping>();
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int ii = 0 ; ii < annotations.length ; ii ++) {
            Annotation[] ann = annotations[ii];
            if(ann.length!=0){
                for(int jj = 0 ; jj < ann.length ; jj ++){
                    if(ann[jj].annotationType() == DebugMointerUIMapping.class){
                        defMap.put(ii, (DebugMointerUIMapping) ann[jj]);
                    }
                }
            }
        }
        return defMap;
    }
}