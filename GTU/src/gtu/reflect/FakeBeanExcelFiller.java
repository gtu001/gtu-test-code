package gtu.reflect;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gtu.poi.hssf.ExcelUtil_Xls97;

public class FakeBeanExcelFiller {

    private static Logger logger = LoggerFactory.getLogger(FakeBeanExcelFiller.class);

    private class XXXXX {
        int aaaa;
        String bbbb;
        String cccc;

        public int getAaaa() {
            return aaaa;
        }

        public void setAaaa(int aaaa) {
            this.aaaa = aaaa;
        }

        public String getBbbb() {
            return bbbb;
        }

        public void setBbbb(String bbbb) {
            this.bbbb = bbbb;
        }

        public String getCccc() {
            return cccc;
        }

        public void setCccc(String cccc) {
            this.cccc = cccc;
        }
    }

    public static void main(String[] args) {
        List<XXXXX> lst = new ArrayList<XXXXX>();
        FakeBeanExcelFiller t = new FakeBeanExcelFiller();
        File file = new File("/home/gtu001/桌面/無題 1.xls");
        FakeBeanExcelFiller.fillLst(file, XXXXX.class, t, lst);
        for (XXXXX b : lst) {
            System.out.println(ReflectionToStringBuilder.toString(b));
        }
    }

    public static <T> void fillLst(File xlsFile, Class<T> beanClz, List lst) {
        fillLst(xlsFile, beanClz, null, lst);
    }

    public static <T> void fillLst(File xlsFile, Class<T> beanClz, Object parentInst, List lst) {
        Workbook wb = ExcelUtil_Xls97.getInstance().readExcel(xlsFile);
        Sheet sheet = wb.getSheetAt(0);

        Map<Integer, String> defineMap = new LinkedHashMap<Integer, String>();
        {
            Row row = sheet.getRow(0);
            for (int jj = 0; jj < row.getLastCellNum(); jj++) {
                String fieldName = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj));
                defineMap.put(jj, fieldName);
            }
        }

        for (int ii = 1; ii <= sheet.getLastRowNum(); ii++) {
            Row row = sheet.getRow(ii);
            if (row == null) {
                continue;
            }

            Object beanObj = newInstanceDefault(beanClz, parentInst, false);

            for (int jj = 0; jj < row.getLastCellNum(); jj++) {
                String value = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj));
                String fieldName = defineMap.get(jj);
                fillBeanFieldByStringValue(beanClz, fieldName, beanObj, value);
            }

            lst.add(beanObj);
        }
    }

    // ----------------------------------------------------------------------------------------------------------
    // 底下是公用
    // ----------------------------------------------------------------------------------------------------------

    private static void fillBeanFieldByStringValue(Class<?> beanClz, String fieldName, Object beanObj,
            String strValue) {
        Field field = FieldUtils.getDeclaredField(beanClz, fieldName, true);
        Method method = getSetterMethod(beanClz, fieldName);

        boolean findOk = false;

        if (method != null && !findOk) {
            Class<?> paramType = method.getParameterTypes()[0];
            Object realValue = getValue(paramType, strValue);
            try {
                method.invoke(beanObj, new Object[] { realValue });
                findOk = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (field != null && !findOk) {
            Object realValue = getValue(field.getType(), strValue);
            try {
                FieldUtils.writeDeclaredField(beanObj, fieldName, realValue, true);
                findOk = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Method getSetterMethod(Class<?> clz, String fieldName) {
        String methodName = "set" + StringUtils.capitalise(fieldName);
        for (Method mth : clz.getMethods()) {
            if (mth.getName().equals(methodName) && mth.getParameterTypes() != null
                    && mth.getParameterTypes().length == 1) {
                return mth;
            }
        }
        return null;
    }

    private static Object getValue(Class<?> fieldType, String value) {
        if (fieldType == String.class) {
            return value;
        } else if (fieldType == BigDecimal.class) {
            return new BigDecimal(value);
        } else if (fieldType == Integer.class || fieldType == int.class) {
            return Integer.parseInt(value);
        } else if (fieldType == Long.class || fieldType == long.class) {
            return Long.parseLong(value);
        } else if (fieldType == Double.class || fieldType == double.class) {
            return Double.parseDouble(value);
        } else if (fieldType == Float.class || fieldType == float.class) {
            return Float.parseFloat(value);
        } else if (fieldType == Byte.class || fieldType == byte.class) {
            return Byte.parseByte(value);
        } else if (fieldType == Short.class || fieldType == short.class) {
            return Short.parseShort(value);
        } else if (fieldType == Boolean.class || fieldType == boolean.class) {
            return Boolean.parseBoolean(value);
        } else {
            throw new RuntimeException("無法處理的型別 : " + value + " -> " + fieldType);
        }
    }

    private static Object newInstanceDefault(Class entityClz, Object parentInst, boolean debug) {
        Object entity = null;
        try {
            entity = entityClz.newInstance();
        } catch (Exception ex) {
            if (debug)
                ex.printStackTrace();
            try {
                Constructor cons = entityClz.getConstructor(new Class[0]);
                entity = cons.newInstance(new Object[0]);
            } catch (Exception e1) {
                if (debug)
                    e1.printStackTrace();
                try {
                    Constructor cons = entityClz.getDeclaredConstructor(new Class[0]);
                    cons.setAccessible(true);
                    entity = cons.newInstance(new Object[0]);
                } catch (Exception e2) {
                    if (debug)
                        e2.printStackTrace();
                    try {
                        Constructor cons = entityClz.getDeclaredConstructor(parentInst.getClass());
                        cons.setAccessible(true);
                        entity = cons.newInstance(parentInst);
                    } catch (Exception e3) {
                        System.err.println("若private class且有建構子時, 須建立一個空的建構子!!!!");
                        throw new RuntimeException("newInstanceDefault ERR : " + e3.getMessage(), e3);
                    }
                }
            }
        }
        return entity;
    }
}
