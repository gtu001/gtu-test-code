package gtu.reflect;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

public class Bean2Bean {

    /**
     * 從一個來源bean list 複製到另一個classPath的 bean list
     * @param classPathTo
     * @param mappList
     * @param fromList
     * @return
     */
    public static <F,T> List<T> copyList(String classPathTo, List<Class<?>> mappList, List<F> fromList){
        List<T> list = new ArrayList<T>();
        try{
            for(F f : fromList){
                Class tClz = Class.forName(classPathTo + "." + f.getClass().getSimpleName());
                T tIns = (T)tClz.newInstance();
                BeanUtils.copyProperties(tIns, f);
                
                for(Field ff : f.getClass().getDeclaredFields()){
                    if(!mappList.contains(ff.getType())){
                        continue;
                    }
                    ff.setAccessible(true);
                    Class tClz2 = Class.forName(classPathTo + "." + ff.getType().getSimpleName());
                    Object tIns2 = tClz2.newInstance();
                    BeanUtils.copyProperties(tIns2, ff.get(f));
                    
                    Field toField = tClz.getDeclaredField(ff.getName());
                    toField.setAccessible(true);
                    toField.setAccessible(true);
                    toField.set(tIns, tIns2);
                }
                list.add(tIns);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return list;
    }
}
