package gtu.runtime;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EnvHelper {

    private static final EnvHelper _INST = new EnvHelper();

    public static EnvHelper getInstance() {
        return _INST;
    }

    public void put(String key, String value) {
        Map<String, String> envMap = System.getenv();
        Map<String, String> cloneMap = new HashMap<String, String>();
        cloneMap.putAll(envMap);
        cloneMap.put(key, value);
        try {
            setEnv(cloneMap);
//            System.out.println(">>> after put = " + key + "\t" + System.getenv().get(key));
        } catch (Exception e) {
            throw new RuntimeException("EnvHelper.put Err : " + e.getMessage(), e);
        }
    }

    public void setUpEnvironment(ProcessBuilder builder, String key, String value) {
        Map<String, String> env = builder.environment();
        env.put(key, value);
    }
    
    protected void setEnv(Map<String, String> newenv) throws Exception {
        try {
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(newenv);
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(newenv);
        } catch (NoSuchFieldException e) {
            Class[] classes = Collections.class.getDeclaredClasses();
            Map<String, String> env = System.getenv();
            for (Class cl : classes) {
                if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                    Field field = cl.getDeclaredField("m");
                    field.setAccessible(true);
                    Object obj = field.get(env);
                    Map<String, String> map = (Map<String, String>) obj;
                    map.clear();
                    map.putAll(newenv);
                }
            }
        }
    }
}
