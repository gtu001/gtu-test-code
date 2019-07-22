package gtu.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.reflect.FieldUtils;

public class FakeBeanFiller {

    private static long START_DATE_LONG = getBeginDateLong();

    public static String getRandomString(String... strings) {
        int index = new Random().nextInt(strings.length);
        return strings[index];
    }
    
    public static void fillBean_Random(Object bean) {
        for (Field f : bean.getClass().getDeclaredFields()) {
            String forString = String.valueOf(new java.util.Random().nextInt(10000));
            String forInt = String.valueOf(new java.util.Random().nextInt(10000));
            if (f.getType() == String.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), forString, true);
                } catch (Exception e) {
                }
            } else if (f.getType() == int.class || f.getType() == Integer.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), Integer.parseInt(forInt), true);
                } catch (Exception e) {
                }
            } else if (f.getType() == byte.class || f.getType() == Byte.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), Byte.parseByte(forInt), true);
                } catch (Exception e) {
                }
            } else if (f.getType() == short.class || f.getType() == Short.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), Short.valueOf(forInt), true);
                } catch (Exception e) {
                }
            } else if (f.getType() == boolean.class || f.getType() == Boolean.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), true, true);
                } catch (Exception e) {
                }
            } else if (f.getType() == long.class || f.getType() == Long.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), Long.parseLong(forInt), true);
                } catch (Exception e) {
                }
            } else if (f.getType() == float.class || f.getType() == Float.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), Float.parseFloat(forInt), true);
                } catch (Exception e) {
                }
            } else if (f.getType() == double.class || f.getType() == Double.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), Double.parseDouble(forInt), true);
                } catch (Exception e) {
                }
            } else if (f.getType() == char.class || f.getType() == Character.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), StringUtils.substring(forString, 0, 1).charAt(0),
                        true);
                } catch (Exception e) {
                }
            } else if (f.getType() == BigDecimal.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), new BigDecimal(forInt),
                        true);
                } catch (Exception e) {
                }
            } else if (f.getType() == Date.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), new Date(getRandomDateLong()), true);
                } catch (Exception e) {
                }
            } else if (f.getType() == java.sql.Date.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), new java.sql.Date(getRandomDateLong()), true);
                } catch (Exception e) {
                }
            } else if (f.getType() == Timestamp.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), new Timestamp(getRandomDateLong()), true);
                } catch (Exception e) {
                }
            } else if (Collection.class.isAssignableFrom(f.getType())) {
                // createFakeList(bean, f.getName(), size);
                System.err.println("欄位錯誤 : " + f.getName() + "\t" + f.getType());
            } else if (Map.class.isAssignableFrom(f.getType())) {
                // throw new UnsupportedOperationException("不支援Map操作!");
                System.err.println("欄位錯誤 : " + f.getName() + "\t" + f.getType());
            } else {
                System.err.println("欄位錯誤 : " + f.getName() + "\t" + f.getType());
            }
        }
    }

    private static long getRandomDateLong() {
        return getRandomNumberInRange(START_DATE_LONG, System.currentTimeMillis());
    }

    private static long getRandomNumberInRange(long min, long max) {
        return new BigDecimal(Math.random()).multiply(new BigDecimal((max - min) + 1)).add(new BigDecimal(min))
            .longValue();
    }

    private static long getBeginDateLong() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1990);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static void fillBean(Object bean, String forString, int _forInt, boolean forBoolean) {
        Date d = new Date();
        java.sql.Date d2 = new java.sql.Date(d.getTime());
        Timestamp d3 = new Timestamp(d.getTime());
        String forInt = String.valueOf(_forInt);
        for (Field f : bean.getClass().getDeclaredFields()) {
            if (f.getType() == String.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), forString, true);
                } catch (Exception e) {
                }
            } else if (f.getType() == int.class || f.getType() == Integer.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), Integer.parseInt(forInt), true);
                } catch (Exception e) {
                }
            } else if (f.getType() == byte.class || f.getType() == Byte.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), Byte.parseByte(forInt), true);
                } catch (Exception e) {
                }
            } else if (f.getType() == short.class || f.getType() == Short.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), Short.valueOf(forInt), true);
                } catch (Exception e) {
                }
            } else if (f.getType() == boolean.class || f.getType() == Boolean.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), forBoolean, true);
                } catch (Exception e) {
                }
            } else if (f.getType() == long.class || f.getType() == Long.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), Long.parseLong(forInt), true);
                } catch (Exception e) {
                }
            } else if (f.getType() == float.class || f.getType() == Float.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), Float.parseFloat(forInt), true);
                } catch (Exception e) {
                }
            } else if (f.getType() == double.class || f.getType() == Double.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), Double.parseDouble(forInt), true);
                } catch (Exception e) {
                }
            } else if (f.getType() == char.class || f.getType() == Character.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), StringUtils.substring(forString, 0, 1).charAt(0),
                        true);
                } catch (Exception e) {
                }
            } else if (f.getType() == BigDecimal.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), new BigDecimal(forInt),
                        true);
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
            } else if (f.getType() == Timestamp.class) {
                try {
                    FieldUtils.writeDeclaredField(bean, f.getName(), d3, true);
                } catch (Exception e) {
                }
            } else if (Collection.class.isAssignableFrom(f.getType())) {
                // createFakeList(bean, f.getName(), size);
                System.err.println("欄位錯誤 : " + f.getName() + "\t" + f.getType());
            } else if (Map.class.isAssignableFrom(f.getType())) {
                // throw new UnsupportedOperationException("不支援Map操作!");
                System.err.println("欄位錯誤 : " + f.getName() + "\t" + f.getType());
            } else {
                System.err.println("欄位錯誤 : " + f.getName() + "\t" + f.getType());
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
