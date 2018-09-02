package gtu.reflect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

public class ToStringDeserialize {

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");

        ToStringDeserialize t = new ToStringDeserialize();
        TestClass bean = t.new TestClass();
        bean.testBig = new BigDecimal(1);
        bean.testInt = 1;
        bean.testfloat = 1;
        bean.testdouble = 1;
        bean.testLst = Arrays.asList("1", "2", "3");
        bean.testShort = 3;
        bean.testStr = "xxx";
        bean.testMap = map;
        bean.testDate = new Date();

        TestClass bean2 = t.new TestClass();
        String toStringContent = "gtu.reflect.ToStringDeserialize$TestClass@f6f4d33[testStr=xxx,testStr2=<null>,testInt=1,testInt2=0,testBig=1,testBig2=<null>,testShort=3,testShort2=0,testlong=0,testlong2=0,testdouble=1.0,testdouble2=0.0,testfloat=1.0,testfloat2=0.0,testDate=Wed Dec 06 23:08:06 CST 2017,testDate2=<null>,testMap={a=1, b=2, c=3},testMap2=<null>,testLst=[1, 2, 3],testLst2=<null>]";
        t.toStringToBean(toStringContent, bean2);
        System.out.println(ReflectionToStringBuilder.toString(bean2, ToStringStyle.MULTI_LINE_STYLE));
        System.out.println("done..");
    }

    private static final ToStringDeserialize _INST = new ToStringDeserialize();

    private ToStringDeserialize() {
    }

    public static ToStringDeserialize getInstance() {
        return _INST;
    }

    public String readToStringFile(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf8"));
            StringBuilder sb = new StringBuilder();
            for (String line = null; (line = reader.readLine()) != null;) {
                sb.append(line + "\n");
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
            }
        }
    }

    public <T> T toStringToBean(String toStringContent, T bean) {
        String inputToString = toStringContent;
        Class<?> clz = bean.getClass();
        for (Field f : clz.getDeclaredFields()) {
            if (f.getType() == String.class) {
                TypeMatch.STRING.setToBean(inputToString, bean, f.getName());
            } else if (f.getType() == Integer.class || f.getType() == int.class) {
                TypeMatch.INT.setToBean(inputToString, bean, f.getName());
            } else if (f.getType() == Byte.class || f.getType() == byte.class) {
                TypeMatch.BYTE.setToBean(inputToString, bean, f.getName());
            } else if (f.getType() == Short.class || f.getType() == short.class) {
                TypeMatch.SHORT.setToBean(inputToString, bean, f.getName());
            } else if (f.getType() == Long.class || f.getType() == long.class) {
                TypeMatch.LONG.setToBean(inputToString, bean, f.getName());
            } else if (f.getType() == Double.class || f.getType() == double.class) {
                TypeMatch.DOUBLE.setToBean(inputToString, bean, f.getName());
            } else if (f.getType() == Float.class || f.getType() == float.class) {
                TypeMatch.FLOAT.setToBean(inputToString, bean, f.getName());
            } else if (f.getType() == Boolean.class || f.getType() == boolean.class) {
                TypeMatch.BOOLEAN.setToBean(inputToString, bean, f.getName());
            } else if (f.getType() == BigDecimal.class) {
                TypeMatch.BIGDECIMAL.setToBean(inputToString, bean, f.getName());
            } else if (f.getType() == Date.class) {
                TypeMatch.DATE.setToBean(inputToString, bean, f.getName());
            } else if (f.getType() == List.class && isGenericTypeAllString(f)) {
                TypeMatch.LIST_OF_STRING.setToBean(inputToString, bean, f.getName());
            } else if (f.getType() == Map.class && isGenericTypeAllString(f)) {
                TypeMatch.MAP_OF_STRING.setToBean(inputToString, bean, f.getName());
            } else {
                System.out.println("目前不支援處理此欄位 : " + f.getName() + " : " + f.getType());
            }
        }
        return bean;
    }

    private boolean isGenericTypeAllString(Field f) {
        Type[] ts = ((ParameterizedType) f.getGenericType()).getActualTypeArguments();
        for (Type c : ts) {
            if (((Class) c) != String.class) {
                return false;
            }
        }
        return true;
    }

    private enum TypeMatch {
        STRING("%s=(.*?)[,\\]}]{1}", 1) {
            @Override
            Object toValue(String orignVal) {
                return orignVal;
            }
        },
        INT("%s=(.*?)[,\\]}]{1}", 1) {
            @Override
            Object toValue(String orignVal) {
                return Integer.parseInt(orignVal);
            }
        },
        BYTE("%s=(.*?)[,\\]}]{1}", 1) {
            @Override
            Object toValue(String orignVal) {
                return Byte.parseByte(orignVal);
            }
        },
        SHORT("%s=(.*?)[,\\]}]{1}", 1) {
            @Override
            Object toValue(String orignVal) {
                return Short.parseShort(orignVal);
            }
        },
        LONG("%s=(.*?)[,\\]}]{1}", 1) {
            @Override
            Object toValue(String orignVal) {
                return Long.parseLong(orignVal);
            }
        },
        DOUBLE("%s=(.*?)[,\\]}]{1}", 1) {
            @Override
            Object toValue(String orignVal) {
                return Double.parseDouble(orignVal);
            }
        },
        FLOAT("%s=(.*?)[,\\]}]{1}", 1) {
            @Override
            Object toValue(String orignVal) {
                return Float.parseFloat(orignVal);
            }
        },
        BOOLEAN("%s=(.*?)[,\\]}]{1}", 1) {
            @Override
            Object toValue(String orignVal) {
                return Boolean.parseBoolean(orignVal);
            }
        },
        DATE("%s=(.*?)[,\\]}]{1}", 1) {
            @Override
            Object toValue(String orignVal) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                try {
                    return sdf.parse(orignVal);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        },
        BIGDECIMAL("%s=(.*?)[,\\]}]{1}", 1) {
            @Override
            Object toValue(String orignVal) {
                int scale = 0;
                if (orignVal.contains(".")) {
                    scale = orignVal.substring(orignVal.lastIndexOf(".") + 1).length();
                }
                BigDecimal val = new BigDecimal(Double.parseDouble(orignVal));
                if (scale != 0) {
                    val = val.setScale(scale, RoundingMode.HALF_UP);
                }
                return val;
            }
        },
        LIST_OF_STRING("%s=\\[(.*?)\\]", 1, true) {
            @Override
            Object toValue(String orignVal) {
                if (StringUtils.isNotBlank(orignVal)) {
                    String[] vals = orignVal.split(",", -1);
                    List<String> lst = new ArrayList<String>();
                    for (String v : vals) {
                        v = StringUtils.trim(v);
                        if (StringUtils.isNotBlank(v)) {
                            lst.add(v);
                        }
                    }
                    return lst;
                }
                return null;
            }
        },
        MAP_OF_STRING("%s=\\{(.*?)\\}", 1, true) {
            @Override
            Object toValue(String orignVal) {
                if (StringUtils.isNotBlank(orignVal)) {
                    Map<String, String> valMap = new LinkedHashMap<String, String>();
                    String[] vals = orignVal.split(",", -1);
                    for (String v : vals) {
                        String[] kv = v.split("=", -1);
                        valMap.put(StringUtils.trim(kv[0]), StringUtils.trim(kv[1]));
                    }
                    return valMap;
                }
                return null;
            }
        },;
        final String ptnStr;
        final int groupIndex;
        final boolean tryNullMode;

        TypeMatch(String ptnStr, int groupIndex, boolean tryNullMode) {
            this.ptnStr = ptnStr;
            this.groupIndex = groupIndex;
            this.tryNullMode = tryNullMode;
        }

        TypeMatch(String ptnStr, int groupIndex) {
            this(ptnStr, groupIndex, false);
        }

        abstract Object toValue(String orignVal);

        private boolean setToBean(String inputToString, String formatPattern, int groupIndex, Object bean, String fieldName, boolean onlyTryNullMode) {
            Pattern ptn = Pattern.compile(String.format(formatPattern, fieldName), Pattern.DOTALL | Pattern.MULTILINE);
            Matcher mth = ptn.matcher(inputToString);
            if (mth.find()) {
                String orignVal = mth.group(groupIndex);
                if ("<null>".equals(orignVal) || "null".equals(orignVal)) {
                    return true;
                } else if (onlyTryNullMode) {
                    return false;
                }
                try {
                    Object val = toValue(orignVal);
                    FieldUtils.writeDeclaredField(bean, fieldName, val, true);
                    return true;
                } catch (Exception e) {
                    throw new RuntimeException(this + " -> Set " + fieldName + " error!, value = " + orignVal, e);
                }
            } else {
                System.out.println("Not found - " + fieldName);
                return false;
            }
        }

        public void setToBean(String inputToString, Object bean, String fieldName) {
            // 是否先測試是否為null
            if (tryNullMode) {
                boolean tryNullModeResult = setToBean(inputToString, "%s=(.*?)[,\\]}]{1}", groupIndex, bean, fieldName, true);
                if (tryNullModeResult) {
                    // 若為null則返回無須設質
                    return;
                }
            }
            // 正常處理
            setToBean(inputToString, ptnStr, groupIndex, bean, fieldName, false);
        }
    }

    private class TestClass {
        String testStr;
        String testStr2;
        int testInt;
        int testInt2;
        BigDecimal testBig;
        BigDecimal testBig2;
        short testShort;
        short testShort2;
        long testlong;
        long testlong2;
        double testdouble;
        double testdouble2;
        float testfloat;
        float testfloat2;
        Date testDate;
        Date testDate2;
        Map<String, String> testMap;
        Map<String, String> testMap2;
        List<String> testLst;
        List<String> testLst2;
    }
}