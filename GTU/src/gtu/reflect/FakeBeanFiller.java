package gtu.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;

public class FakeBeanFiller {

    public static void createFakeData(Object bean, int size) {
        fillBean(bean);
        for (Field f : bean.getClass().getDeclaredFields()) {
            if (Collection.class.isAssignableFrom(f.getType())) {
                createFakeList(bean, f.getName(), size);
            }
        }
    }

    public static void createFakeList(Object bean, String fieldName, int size) {
        try {
            List lst = new ArrayList();
            Field field = bean.getClass().getDeclaredField(fieldName);
            Class clz = getFieldGenericType_4Collection(field);
            for (int ii = 0; ii < size; ii++) {
                Object inst = BeanUtils.instantiate(clz);
                fillBean(inst);
                lst.add(inst);
            }
            FieldUtils.writeDeclaredField(bean, fieldName, lst, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void fillBean(Object bean) {
        Date d = new Date();
        java.sql.Date d2 = new java.sql.Date(d.getTime());
        BigDecimal b = BigDecimal.ZERO;
        for (Field f : bean.getClass().getDeclaredFields()) {
            if (f.getType() == String.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), "XXXXX", true);
                } catch (Exception e) {
                }
            } else if (f.getType() == int.class || f.getType() == Integer.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), 1, true);
                } catch (Exception e) {
                }
            } else if (f.getType() == byte.class || f.getType() == Byte.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), 2, true);
                } catch (Exception e) {
                }
            } else if (f.getType() == short.class || f.getType() == Short.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), 3, true);
                } catch (Exception e) {
                }
            } else if (f.getType() == boolean.class || f.getType() == Boolean.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), false, true);
                } catch (Exception e) {
                }
            } else if (f.getType() == long.class || f.getType() == Long.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), 100L, true);
                } catch (Exception e) {
                }
            } else if (f.getType() == float.class || f.getType() == Float.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), 1.1f, true);
                } catch (Exception e) {
                }
            } else if (f.getType() == double.class || f.getType() == Double.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), 2.2f, true);
                } catch (Exception e) {
                }
            } else if (f.getType() == char.class || f.getType() == Character.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), 'A', true);
                } catch (Exception e) {
                }
            } else if (f.getType() == Date.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), d, true);
                } catch (Exception e) {
                }
            } else if (f.getType() == java.sql.Date.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), d2, true);
                } catch (Exception e) {
                }
            } else if (f.getType() == BigDecimal.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), b, true);
                } catch (Exception e) {
                }
            } else {
                System.err.println("æçç¡ : " + f.getName() + "\t" + f.getType());
            }
        }
    }

    public static Class<?> getFieldGenericType(Field field) {
        // Type type = null;
        // try {
        // type = ((ParameterizedType)
        // fld.getGenericType()).getActualTypeArguments()[0];
        // } catch (Exception ex) {
        // }
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        System.out.println(ReflectionToStringBuilder.toString(type, ToStringStyle.MULTI_LINE_STYLE));
        return (Class<?>) type.getRawType();
    }

    public static Class<?> getFieldGenericType_4Collection(Field field) {
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        Class<?> genericType = (Class<?>) type.getActualTypeArguments()[0];
        return genericType;
    }
}
