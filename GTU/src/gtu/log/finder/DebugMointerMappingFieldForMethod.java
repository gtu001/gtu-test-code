package gtu.log.finder;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.DefaultListModel;

import gtu.class_.ClassUtil;
import gtu.log.Logger2File;

public class DebugMointerMappingFieldForMethod {

    @Target({ java.lang.annotation.ElementType.PARAMETER })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DebugMointerUIMapping {
        int index() default -1;
    }

    private DebugMointerUI _this;
    private Object[] mointerObjects;
    private Method method;
    private Logger2File logger;

    public DebugMointerMappingFieldForMethod(DebugMointerUI _this, Object[] mointerObjects, Method method) {
        super();
        this._this = _this;
        this.mointerObjects = mointerObjects;
        this.method = method;
        this.logger = _this.getLogger();
    }

    /**
     * 取得method參數
     */
    public List<Object> getExactExecuteParameterList() {
        List<Object> rtnList = new ArrayList<Object>();
        Class<?>[] paramClzs = method.getParameterTypes();
        //已預設的搜尋參數
        for (int ii = 0; ii < paramClzs.length; ii++) {
            Object val = getParameter(ii, paramClzs[ii]);
            rtnList.add(val);
        }
        
        //取得強制對應參數
        Map<Integer, DebugMointerUIMapping> defMap = getDebugMointerUIMappingDefine(method);
        if(!defMap.isEmpty()){
            replaceIndicateMapping(rtnList, defMap, paramClzs);
        }
        
        return rtnList;
    }
    
    /**
     * 替換指定參數
     */
    private void replaceIndicateMapping(List<Object> rtnList, Map<Integer, DebugMointerUIMapping> defMap, Class<?>[] paramClzs){
        for(Integer index : defMap.keySet()){
            DebugMointerUIMapping def = defMap.get(index);
            if(def.index() != -1){
                try{
                    Object o = mointerObjects[def.index()];
                    Class<?> clz = paramClzs[index];
                    if(o != null){
                        if(ClassUtil.isAssignFrom(clz, o.getClass())){
                            rtnList.set(index, o);
                            logger.debug("使用指定index成功  -> index : " + index + " , obj = " + DebugMointerMappingField.getObjSimpleStr(o));
                        }else{
                            logger.error("使用指定index型別不符 : " + def + " index = " + index + ", " + DebugMointerMappingField.getObjSimpleStr(o));
                        } 
                    }
                }catch(Exception ex){
                    logger.error("使用指定index 錯誤 : " + def, ex);
                }
            }
        }
    }
    
    /**
     * 取得method 定義
     */
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

    /**
     * 取得參數
     */
    private Object getParameter(int index, Class<?> clz) {
        // 直接抓物件
        for (int ii = 0; ii < mointerObjects.length; ii++) {
            Object o = mointerObjects[ii];
            if (o != null && ClassUtil.isAssignFrom(clz, o.getClass())) {
                logger.debug("取得參數 param[" + index + "] = mointerObjects[" + ii + "]" + " -> " + DebugMointerMappingField.getObjSimpleStr(o));
                return o;
            }
        }

        // 若物件為array
        for (int ii = 0; ii < mointerObjects.length; ii++) {
            Object o = mointerObjects[ii];
            if (o.getClass().isArray()) {
                for (int jj = 0; jj < Array.getLength(o); jj++) {
                    Object o2 = Array.get(o, jj);
                    if (o2 != null && ClassUtil.isAssignFrom(clz, o2.getClass())) {
                        logger.debug("取得參數 param[" + index + "] = mointerObjects[" + ii + "][" + jj + "] -> " + DebugMointerMappingField.getObjSimpleStr(o2));
                        return o2;
                    }
                }
            }
        }

        // 取得物件內的field
        for (int ii = 0; ii < mointerObjects.length; ii++) {
            Object o = mointerObjects[ii];
            for (Field f : o.getClass().getDeclaredFields()) {
                Object o2 = getObject(f, o);
                if (o2 != null && ClassUtil.isAssignFrom(clz, f.getType())) {
                    logger.debug("取得參數 param[" + index + "] = mointerObjects[" + ii + "]." + f.getName() + " -> " + DebugMointerMappingField.getObjSimpleStr(o2));
                    return o2;
                }
            }
        }

        // 取得暫存區物件
        Object rtn = this.getFromTempData(index, clz);
        if (rtn != null) {
            return rtn;
        }

        logger.debug("無法取得參數 param[" + index + "]");
        return null;
    }

    /**
     * 取得暫存區資料
     */
    private Object getFromTempData(int index, Class<?> clz) {
        DefaultListModel model = _this.getTempModel();
        // 直接抓物件
        for (int ii = 0; ii < model.getSize(); ii++) {
            Object o = model.getElementAt(ii);
            if (o != null && ClassUtil.isAssignFrom(clz, o.getClass())) {
                logger.debug("取得參數 param[" + index + "] = 暫存區[" + ii + "]" + " -> " + DebugMointerMappingField.getObjSimpleStr(o));
                return o;
            }
        }

        // 若物件為array
        for (int ii = 0; ii < model.getSize(); ii++) {
            Object o = model.getElementAt(ii);
            if (o.getClass().isArray()) {
                for (int jj = 0; jj < Array.getLength(o); jj++) {
                    Object o2 = Array.get(o, jj);
                    if (o2 != null && ClassUtil.isAssignFrom(clz, o2.getClass())) {
                        logger.debug("取得參數 param[" + index + "] = 暫存區[" + ii + "][" + jj + "] -> " + DebugMointerMappingField.getObjSimpleStr(o));
                        return o;
                    }
                }
            }
        }

        // 取得物件內的field
        for (int ii = 0; ii < model.getSize(); ii++) {
            Object o = model.getElementAt(ii);
            for (Field f : o.getClass().getDeclaredFields()) {
                Object o2 = getObject(f, o);
                if (o2 != null && ClassUtil.isAssignFrom(clz, f.getType())) {
                    logger.debug("取得參數 param[" + index + "] = 暫存區[" + ii + "]." + f.getName() + " -> " + DebugMointerMappingField.getObjSimpleStr(o2));
                    return o2;
                }
            }
        }
        return null;
    }

    /**
     * 取得物件field
     */
    private Object getObject(Field f, Object obj) {
        try {
            f.setAccessible(true);
            return f.get(obj);
        } catch (Exception ex) {
            return null;
        }
    }
}
