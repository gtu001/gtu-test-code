package gtu.reflect;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.reflect.FieldUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cub.inv.query.ui.dto.web.bond.BondQryETFBDPQ04WebResponseDto;

public class FakeBeanXmlFiller {

    public static void main(String[] args) {
        File xmlFile = new File("D:/workstuff/gtu-test-code/GTU/src/gtu/reflect/FakeBeanXmlFiller_PQ04電文轉出.xml");

        Map<String, String> map = new HashMap<>();
        map.put("BuyDate", "buyDate");
        map.put("TrustId", "trustId");
        map.put("BondsId", "bondsId");
        map.put("TrustType", "trustType");
        map.put("TrustCcy", "trustCcy");
        map.put("TrustAmt", "trustAmt");
        map.put("TrustUnit", "trustUnit");
        map.put("AllocateAmt", "allocateAmt");
        map.put("RedeemUnit", "redeemUnit");
        map.put("GetAmt", "getAmt");
        map.put("PriceDate", "priceDate");
        map.put("PriceAmt", "priceAmt");
        map.put("TrustAmtNT", "trustAmtNT");
        map.put("RefAmtNT", "refAmtNT");
        map.put("AllocateAmtNT", "allocateAmtNT");
        map.put("GetAmtNT", "getAmtNT");
        map.put("BondsName", "bondsName");
        map.put("SetFlag", "setFlag");
        map.put("CourtFlag", "setFlag");

        Class<?> beanClz = BondQryETFBDPQ04WebResponseDto.class;
        Object parentInst = null;

        List rtnLst = new ArrayList();

        FakeBeanXmlFiller.fillLst(xmlFile, beanClz, parentInst, map, rtnLst);
    }

    public static void fillLst(File xmlFile, Class<?> beanClz, Object parentInst, Map<String, String> formXmlToFieldMap,
            List rtnLst) {
        try {
            SAXReader reader = new SAXReader();

            Document doc = reader.read(xmlFile);
            Element rootElemet = doc.getRootElement();

            if (rtnLst == null) {
                rtnLst = new ArrayList();
            }

            // 客製化路徑 ↓↓↓↓↓↓ TODO
            List lst = doc.selectNodes("/*[name()='CUBXML']/*[name()='TRANRS']/*[name()='Records']/*");
            // 客製化路徑 ↑↑↑↑↑↑ TODO

            System.out.println("xmlLst.size = " + lst.size());

            for (int ii = 0; ii < lst.size(); ii++) {
                Element element = (Element) lst.get(ii);

                // 客製化路徑 ↓↓↓↓↓↓ TODO
                List els = element.selectNodes("*");
                // 客製化路徑 ↑↑↑↑↑↑ TODO

                Object beanObj = newInstanceDefault(beanClz, parentInst, false);

                // System.out.println("====================================");
                for (int jj = 0; jj < els.size(); jj++) {
                    Element el = (Element) els.get(jj);

                    // System.out.println(el.getName());
                    // System.out.println(el.getTextTrim());

                    if (!formXmlToFieldMap.containsKey(el.getName())) {
                        throw new RuntimeException("找不到對應 : " + el.getName());
                    } else {
                        String fieldName = formXmlToFieldMap.get(el.getName());
                        fillBeanFieldByStringValue(beanClz, fieldName, beanObj, el.getTextTrim());
                    }
                }
                rtnLst.add(beanObj);
            }

            System.out.println("done...");
            for (Object bean : rtnLst) {
                System.out.println(ReflectionToStringBuilder.toString(bean));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
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
