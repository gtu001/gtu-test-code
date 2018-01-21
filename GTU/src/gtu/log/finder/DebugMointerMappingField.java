package gtu.log.finder;

import gtu.class_.ClassUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import javax.swing.JCheckBox;

import org.apache.commons.lang3.StringUtils;

public class DebugMointerMappingField {
    
    private Object newObject;
    private DebugMointerUI _this;    private Object[] mointerObjects;
    private JCheckBox tempModelCheckBox;
    
    public DebugMointerMappingField(Object newObject, DebugMointerUI _this, Object[] mointerObjects, JCheckBox tempModelCheckBox) {
        super();
        this.newObject = newObject;
        this._this = _this;
        this.mointerObjects = mointerObjects;
        this.tempModelCheckBox = tempModelCheckBox;
    }

    private StringBuilder okSb = new StringBuilder();
    private StringBuilder errSb = new StringBuilder();

    public void executeMapping(){
        for (Field fld : newObject.getClass().getDeclaredFields()) {
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
                fld.setAccessible(true);
                if (fld.get(newObject) == null || indicatePathObject != null || StringUtils.isNotBlank(indicateFieldName)) {
                    boolean bindOk = false;
                    if (DebugMointerUI.class.isAssignableFrom(fld.getType())) {
                        msg1 = fld.getName() + " = [DebugMointerUI 監控工具]";
                        fld.set(newObject, this);
                        bindOk = true;
                        okSb.append(msg1 + indicateFieldNameMessage + "\n");
                        continue;
                    } else if (indicatePathObject != null && //
                            ClassUtil.isAssignFrom(fld.getType(), indicatePathObject.getClass())) {
                        // 使用路徑綁定物件[自定]
                        msg1 = fld.getName() + " = [取得指定物件 : " + indicatePathObject + "]";
                        fld.set(newObject, indicatePathObject);
                        bindOk = true;
                        okSb.append(msg1 + indicateFieldNameMessage + "\n");
                        continue;
                    }
                    for (int ii = 0; ii < mointerObjects.length; ii++) {
                        Object condidate = mointerObjects[ii];
                        if (condidate == newObject) {
                            continue;
                        }
                        if (condidate != null && ClassUtil.isAssignFrom(fld.getType(), condidate.getClass()) && StringUtils.isBlank(indicateFieldName)) {
                            // 以監控物件直接綁定
                            msg1 = fld.getName() + " = [" + ii + "] , " + getObjSimpleStr(condidate);
                            fld.set(newObject, condidate);
                            okSb.append(msg1 + "\n");
                            bindOk = true;
                            break;
                        } else if (condidate != null && condidate.getClass().isArray() && StringUtils.isBlank(indicateFieldName)) {
                            // 綁定來源為陣列物件
                            for (int jj = 0; jj < Array.getLength(condidate); jj++) {
                                Object arrayObj = Array.get(condidate, jj);
                                if (arrayObj != null && ClassUtil.isAssignFrom(fld.getType(), arrayObj.getClass())) {
                                    msg1 = fld.getName() + " = [" + ii + "] , [" + jj + "]" + getObjSimpleStr(arrayObj);
                                    fld.set(newObject, arrayObj);
                                    okSb.append(msg1 + "\n");
                                    bindOk = true;
                                    break;
                                }
                            }
                        } else if (condidate != null) {
                            // 取得物件內部物件來綁定
                            for (Class<?> tempClass = condidate.getClass(); tempClass != Object.class; tempClass = tempClass.getSuperclass()) {
                                for (Field fld2 : tempClass.getDeclaredFields()) {
                                    String msg2 = fld.getName() + " = [" + ii + "] , "//
                                            + condidate.getClass().getSimpleName() + "." + fld2.getName() //
                                            + indicateFieldNameMessage;
                                    try {
                                        fld2.setAccessible(true);
                                        Object condidateInnerObject = fld2.get(condidate);
                                        if (condidateInnerObject != null && //
                                                ClassUtil.isAssignFrom(fld.getType(), condidateInnerObject.getClass()) //
                                        ) {
                                            // 未指定
                                            if (StringUtils.isBlank(indicateFieldName)) {
                                                fld.set(newObject, condidateInnerObject);
                                                okSb.append(msg2 + "\n");
                                                bindOk = true;
                                                break;
                                            } else {
                                                // 指定物件為綁定物件[自定]
                                                if (StringUtils.equals(indicateFieldName, fld2.getName())) {
                                                    fld.set(newObject, condidateInnerObject);
                                                    okSb.append(msg2 + "\n");
                                                    bindOk = true;
                                                    break;
                                                }
                                            }
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
                                break;
                            }
                        }
                    }
                    if (!bindOk) {
                        Object checkBind = fld.get(newObject);
                        if (checkBind != null) {
                            errSb.append(fld.getName() + "已存在資料未綁定  = " + getObjSimpleStr(checkBind) + "\n");
                        } else {
                            errSb.append(fld.getName() + "未找到對象!\n");
                        }
                    }
                }
            } catch (Exception ex) {
                errSb.append(msg1 + "\n");
            }
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
}
