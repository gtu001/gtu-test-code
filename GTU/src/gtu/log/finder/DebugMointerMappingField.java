package gtu.log.finder;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import javax.swing.JCheckBox;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import gtu.class_.ClassUtil;

public class DebugMointerMappingField {

    private Object newObject;
    private DebugMointerUI _this;
    private Object[] mointerObjects;
    private JCheckBox tempModelCheckBox;
    private SpringContextHolder springHolder;

    public DebugMointerMappingField(Object newObject, DebugMointerUI _this, Object[] mointerObjects, JCheckBox tempModelCheckBox) {
        super();
        this.newObject = newObject;
        this._this = _this;
        this.mointerObjects = mointerObjects;
        this.tempModelCheckBox = tempModelCheckBox;
        this.springHolder = new SpringContextHolder(mointerObjects);
    }

    private StringBuilder okSb = new StringBuilder();
    private StringBuilder errSb = new StringBuilder();

    public void executeMapping() {
        A: for (Field fld : newObject.getClass().getDeclaredFields()) {
            String msg1 = "";
            String indicateFieldNameMessage = "";
            try {
                // 特別指定綁定的field name
                String indicateFieldName = "";
                Object indicatePathObject = null;
                if (fld.isAnnotationPresent(javax.annotation.Resource.class)) {
                    javax.annotation.Resource anna = (javax.annotation.Resource) fld.getAnnotation(javax.annotation.Resource.class);
                    if (StringUtils.isNotBlank(anna.name())) {
                        indicateFieldName = anna.name();
                        indicateFieldNameMessage = " - 指定resource[" + anna.name() + "]";
                        if (StringUtils.isNumeric(indicateFieldName)) {
                            indicatePathObject = mointerObjects[Integer.parseInt(indicateFieldName)];
                        } else if (DebugMointerGetParseObject.isQuoteParameter(indicateFieldName)) {
                            try {
                                indicatePathObject = _this.getParseObject(indicateFieldName);
                            } catch (Exception ex) {
                                errSb.append(fld.getName() + "路徑取得失敗 : " + indicateFieldName + ", " + ex.getMessage() + "\n");
                            }
                        }
                    }
                }

                // 開始綁定
                boolean bindOk = false;
                fld.setAccessible(true);

                Object currentObj = fld.get(newObject);
                if (currentObj != null) {
                    msg1 = fld.getName() + " = [物件不為空無須注入]";
                    bindOk = true;
                    okSb.append(msg1 + "\n");
                    continue A;

                } else if (currentObj == null) {
                    // 檢查供給與需求 classpath是否相同
                    this.checkSameClassSimpleNameButClasspathDifferent(fld, indicatePathObject);

                    // 使用路徑綁定物件[自定]
                    if (indicatePathObject != null && //
                            ClassUtil.isAssignFrom(fld.getType(), indicatePathObject.getClass())) {
                        msg1 = fld.getName() + " = [取得指定物件 : " + getObjSimpleStr(indicatePathObject) + "]";
                        fld.set(newObject, indicatePathObject);
                        bindOk = true;
                        okSb.append(msg1 + indicateFieldNameMessage + "\n");
                        continue A;
                    }

                    // 使用springContext注入
                    if (this.springHolder.isReady()) {
                        try {
                            Object springBean = this.springHolder.getBean(fld.getType());
                            msg1 = fld.getName() + " = [spring注入物件 : " + getObjSimpleStr(springBean) + "]";
                            fld.set(newObject, springBean);
                            bindOk = true;
                            okSb.append(msg1 + getObjSimpleStr(springBean) + "\n");
                            continue A;
                        } catch (Exception ex) {
                            errSb.append("[spring注入失敗]" + fld.getType() + " " + fld.getName() + " -> " + ex.getMessage() + "\n");
                        }
                    }

                    // 使用搜尋
                    for (int ii = 0; ii < mointerObjects.length; ii++) {
                        Object condidate = mointerObjects[ii];
                        if (condidate == newObject) {
                            continue;
                        }

                        // 檢查供給與需求 classpath是否相同
                        this.checkSameClassSimpleNameButClasspathDifferent(fld, condidate);

                        if (condidate != null && ClassUtil.isAssignFrom(fld.getType(), condidate.getClass())) {
                            // 以監控物件直接綁定
                            msg1 = fld.getName() + " = mointerObjects[" + ii + "] , " + getObjSimpleStr(condidate);
                            fld.set(newObject, condidate);
                            okSb.append(msg1 + "\n");
                            bindOk = true;
                            continue A;

                        } else if (condidate != null && condidate.getClass().isArray()) {
                            // 綁定來源為陣列物件
                            for (int jj = 0; jj < Array.getLength(condidate); jj++) {
                                Object arrayObj = Array.get(condidate, jj);
                                if (arrayObj != null && ClassUtil.isAssignFrom(fld.getType(), arrayObj.getClass())) {
                                    msg1 = fld.getName() + " = mointerObjects[" + ii + "][" + jj + "]" + getObjSimpleStr(arrayObj);
                                    fld.set(newObject, arrayObj);
                                    okSb.append(msg1 + "\n");
                                    bindOk = true;
                                    continue A;
                                }
                            }

                        } else if (condidate != null) {
                            // 取得物件內部物件來綁定
                            for (Class<?> tempClass = condidate.getClass(); tempClass != Object.class; tempClass = tempClass.getSuperclass()) {
                                for (Field fld2 : tempClass.getDeclaredFields()) {
                                    String msg2 = fld.getName() + " = mointerObjects[" + ii + "]." + fld2.getName();
                                    try {
                                        fld2.setAccessible(true);
                                        Object innerObj = fld2.get(condidate);
                                        if (innerObj != null && //
                                                ClassUtil.isAssignFrom(fld.getType(), innerObj.getClass()) //
                                        ) {
                                            fld.set(newObject, innerObj);
                                            okSb.append(msg2 + "\n");
                                            bindOk = true;
                                            continue A;
                                        }
                                    } catch (Exception ex) {
                                        errSb.append(msg2 + "設定錯誤:" + ex.getMessage() + "\n");
                                    }
                                }
                            }
                        }
                    }
                    // 啟用暫存區綁定
                    if (!bindOk && tempModelCheckBox.isSelected()) {
                        for (int ii = 0; ii < _this.getTempModel().getSize(); ii++) {
                            Object tempObj = _this.getTempModel().getElementAt(ii);
                            if (tempObj != null && ClassUtil.isAssignFrom(fld.getType(), tempObj.getClass())) {
                                String msg2 = fld.getName() + " = 暫存區物件[" + ii + "]";
                                fld.set(newObject, tempObj);
                                okSb.append(msg2 + "\n");
                                bindOk = true;
                                continue A;
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                errSb.append("[ERROR]自動注入field失敗 : " + fld.getName() + "\n");
                ex.printStackTrace();
            }
        }
    }

    // 檢查供給與需求 classpath是否相同
    private void checkSameClassSimpleNameButClasspathDifferent(Field fld, Object provideObj) {
        if (provideObj != null && //
                StringUtils.equals(fld.getType().getSimpleName(), provideObj.getClass().getSimpleName()) && //
                !ClassUtil.isAssignFrom(fld.getType(), provideObj.getClass())) {
            errSb.append("[警告:classpath不同] " + fld.getName() + " -> " + "要求為 :" + fld.getType().getName() + ", 來源供給為:" + provideObj.getClass().getName() + "\n");
        }
    }

    public static String getObjSimpleStr(Object value) {
        if (value == null) {
            return "<null>";
        }
        String massage = "";
        try {
            massage = String.valueOf(value);
        } catch (Throwable ex) {
            massage = "<Error : >" + ex.getMessage();
            ex.printStackTrace();
        }
        if (massage.length() < 100) {
            return massage;
        }
        return StringUtils.substring(massage, 0, 100) + "...etc.";
    }

    public StringBuilder getOkSb() {
        return okSb;
    }

    public StringBuilder getErrSb() {
        return errSb;
    }

    private class SpringContextHolder {
        boolean isSpringReady = false;
        Object context;
        Class<?> springClz;

        private SpringContextHolder(Object[] mointerObjects) {
            try {
                springClz = Class.forName("org.springframework.context.ApplicationContext");
                if (mointerObjects != null) {
                    A: for (Object obj : mointerObjects) {
                        if (obj != null && ClassUtil.isAssignFrom(springClz, obj.getClass())) {
                            context = obj;
                            isSpringReady = true;
                            break A;
                        }
                        if (obj != null && obj.getClass().isArray()) {
                            for (int ii = 0; ii < Array.getLength(obj); ii++) {
                                Object objInArry = Array.get(obj, ii);
                                if (objInArry != null && ClassUtil.isAssignFrom(springClz, objInArry.getClass())) {
                                    context = objInArry;
                                    isSpringReady = true;
                                    break A;
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
            }
        }

        public boolean isReady() {
            return isSpringReady && context != null;
        }

        public <T> T getBean(Class<?> clz) {
            try {
                return (T) MethodUtils.invokeMethod(context, "getBean", new Object[] { clz });
            } catch (Exception e) {
                throw new RuntimeException("SpringContextHolder.getBean ERROR : " + e.getMessage(), e);
            }
        }
    }
}
