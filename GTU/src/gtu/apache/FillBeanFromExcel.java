package gtu.apache;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.ibm.icu.math.BigDecimal;

import gtu.poi.hssf.ExcelUtil_Xls97;

public class FillBeanFromExcel {

    private class XXXXX {
        String aaaa;
        String bbbb;
        String cccc;
    }

    public static void main(String[] args) {
        List<XXXXX> lst = new ArrayList<XXXXX>();
        FillBeanFromExcel t = new FillBeanFromExcel();
        File file = new File("/home/gtu001/桌面/無題 1.xls");
        FillBeanFromExcel.fillLst(file, XXXXX.class, t, lst);
        for (XXXXX b : lst) {
            System.out.println(ReflectionToStringBuilder.toString(b));
        }
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
                String fieldName = defineMap.get(jj);
                Field field = FieldUtils.getDeclaredField(beanClz, fieldName, true);
                if (field == null) {
                    continue;
                }

                String value = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj));

                try {
                    if (field.getType() == String.class) {
                        FieldUtils.writeDeclaredField(beanObj, fieldName, value, true);
                    } else if (field.getType() == BigDecimal.class) {
                        FieldUtils.writeDeclaredField(beanObj, fieldName, new BigDecimal(value), true);
                    } else if (field.getType() == Integer.class || field.getType() == int.class) {
                        FieldUtils.writeDeclaredField(beanObj, fieldName, Integer.parseInt(value), true);
                    } else if (field.getType() == Long.class || field.getType() == long.class) {
                        FieldUtils.writeDeclaredField(beanObj, fieldName, Long.parseLong(value), true);
                    } else if (field.getType() == Double.class || field.getType() == double.class) {
                        FieldUtils.writeDeclaredField(beanObj, fieldName, Double.parseDouble(value), true);
                    } else if (field.getType() == Float.class || field.getType() == float.class) {
                        FieldUtils.writeDeclaredField(beanObj, fieldName, Float.parseFloat(value), true);
                    } else if (field.getType() == Byte.class || field.getType() == byte.class) {
                        FieldUtils.writeDeclaredField(beanObj, fieldName, Byte.parseByte(value), true);
                    } else if (field.getType() == Short.class || field.getType() == short.class) {
                        FieldUtils.writeDeclaredField(beanObj, fieldName, Short.parseShort(value), true);
                    } else if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                        FieldUtils.writeDeclaredField(beanObj, fieldName, Boolean.parseBoolean(value), true);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            lst.add(beanObj);
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
