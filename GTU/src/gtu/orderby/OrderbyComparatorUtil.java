package gtu.orderby;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cub.inv.query.ui.dto.web.WebResponseDto;
import cub.inv.query.ui.dto.web.bond.BondQryRoiWebResponseDto;
import gtu.number.NumberUtil;
import gtu.reflect.FakeBeanFiller;

public class OrderbyComparatorUtil {

    private static final Logger logger = LoggerFactory.getLogger(OrderbyComparatorUtil.class);

    public interface IOrderByEnum {
        public List<String> getColumnLst();

        public List<String> getNumberColumns();

        public int ordinal();
    }

    private static String getMatchType(Object o1, String fieldName) {
        if (o1 != null) {
            for (Field f : o1.getClass().getDeclaredFields()) {
                if (StringUtils.equals(fieldName, f.getName())) {
                    return "Field";
                }
            }
            for (Method m : o1.getClass().getDeclaredMethods()) {
                if (StringUtils.equals("get" + StringUtils.capitalize(fieldName), m.getName())) {
                    return "get" + StringUtils.capitalize(fieldName);
                } else if (StringUtils.equals("is" + StringUtils.capitalize(fieldName), m.getName())) {
                    return "is" + StringUtils.capitalize(fieldName);
                }
            }
        }
        return "";
    }

    public static void main(String[] args) {
        List<WebResponseDto> dataList = new ArrayList<WebResponseDto>();
        IntStream.range(0, 200).forEach(ii -> {
            BondQryRoiWebResponseDto dt = new BondQryRoiWebResponseDto();
            FakeBeanFiller.fillBean_Random(dt);
            dt.setBondsId("0099" + new Random().nextInt(1000));
            dt.setTrustCcy(FakeBeanFiller.getRandomString("TWD", "USD", "EUR"));
            dataList.add(dt);
        });

        List<Triple<String[], String, String>> orderColumnLst = new ArrayList<Triple<String[], String, String>>();
        orderColumnLst.add(Triple.of(new String[] { "trustStartDate" }, "desc", "int"));
        orderColumnLst.add(Triple.of(new String[] { "trustId" }, "asc", "string"));

        OrderbyComparatorUtil.orderbyProcess(orderColumnLst, dataList);
    }

    public static <T> Comparator orderbyProcess(final List<Triple<String[], String, String>> orderColumnLst,
            List<T> lst) {

        Comparator comparator = new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {

                for (Triple<String[], String, String> column : orderColumnLst) {

                    int orderVal = 1;
                    if ("desc".equalsIgnoreCase(column.getMiddle())) {
                        orderVal = -1;
                    }

                    // 數字排序
                    if (isNumberType(column.getRight())) {
                        for (String fieldName : column.getLeft()) {
                            try {

                                String o1Type = getMatchType(o1, fieldName);
                                String o2Type = getMatchType(o2, fieldName);
                                if (StringUtils.isBlank(o1Type) || StringUtils.isBlank(o2Type)) {
                                    continue;
                                }

                                BigDecimal b1 = null;
                                BigDecimal b2 = null;

                                if ("Field".equals(o1Type)) {
                                    b1 = NumberUtil.getBigDecimal(FieldUtils.readDeclaredField(o1, fieldName, true));
                                } else if (StringUtils.isNotBlank(o1Type)) {
                                    b1 = NumberUtil.getBigDecimal(MethodUtils.invokeMethod(o1, o1Type, new Object[0]));
                                }
                                if ("Field".equals(o2Type)) {
                                    b2 = NumberUtil.getBigDecimal(FieldUtils.readDeclaredField(o2, fieldName, true));
                                } else if (StringUtils.isNotBlank(o2Type)) {
                                    b2 = NumberUtil.getBigDecimal(MethodUtils.invokeMethod(o2, o2Type, new Object[0]));
                                }

                                int compareResult = ObjectUtils.compare(b1, b2) * orderVal;
                                if (compareResult != 0) {
                                    return compareResult;
                                }
                            } catch (Exception e) {
                                logger.error("orderbyProcess ERR : " + fieldName, e);
                            }
                        }
                    } else {
                        // 字串排序
                        {
                            String v11 = "";
                            String v22 = "";
                            for (String fieldName : column.getLeft()) {
                                try {
                                    String o1Type = getMatchType(o1, fieldName);
                                    String o2Type = getMatchType(o2, fieldName);
                                    if (StringUtils.isBlank(o1Type) || StringUtils.isBlank(o2Type)) {
                                        continue;
                                    }

                                    Object v1 = null;
                                    Object v2 = null;

                                    if ("Field".equals(o1Type)) {
                                        v1 = FieldUtils.readDeclaredField(o1, fieldName, true);
                                    } else if (StringUtils.isNotBlank(o1Type)) {
                                        v1 = MethodUtils.invokeMethod(o1, o1Type, new Object[0]);
                                    }
                                    if ("Field".equals(o2Type)) {
                                        v2 = FieldUtils.readDeclaredField(o2, fieldName, true);
                                    } else if (StringUtils.isNotBlank(o2Type)) {
                                        v2 = MethodUtils.invokeMethod(o2, o2Type, new Object[0]);
                                    }

                                    v11 += (v1 == null ? "" : String.valueOf(v1));
                                    v22 += (v2 == null ? "" : String.valueOf(v2));
                                } catch (Exception e) {
                                    logger.error("orderbyProcess ERR : " + fieldName, e);
                                }
                            }

                            int compareResult = v11.compareTo(v22) * orderVal;
                            if (compareResult != 0) {
                                return compareResult;
                            }
                        }
                    }
                }

                return 0;
            }
        };

        if (lst != null) {
            Collections.sort(lst, comparator);

            for (Object v : lst) {
                List<String> columns = new ArrayList<String>();
                List<Object> valLst = new ArrayList<Object>();
                for (Triple<String[], String, String> columnDef : orderColumnLst) {
                    for (String column : columnDef.getLeft()) {
                        try {
                            columns.add(column);
                            valLst.add(FieldUtils.readDeclaredField(v, column, true));
                        } catch (Exception e1) {
                        }
                    }
                }
                logger.debug("sort result : " + columns + " -> " + valLst);
            }
        }
        return comparator;
    }

    private static boolean isListCoverEachOther(List<String> lst1, List<String> lst2) {
        for (int ii = 0; ii < lst1.size(); ii++) {
            if (lst2.contains(lst1.get(ii))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNumberType(String value) {
        value = StringUtils.trimToEmpty(value);
        if (value.equalsIgnoreCase("int") || value.equalsIgnoreCase("number")) {
            return true;
        }
        return false;
    }

    public static Comparator orderbyProcess4Map(final List<Triple<String[], String, String>> orderColumnLst,
            List<Map<String, Object>> lst) {

        Comparator comparator = new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {

                for (Triple<String[], String, String> column : orderColumnLst) {

                    int orderVal = 1;
                    if ("desc".equalsIgnoreCase(column.getMiddle())) {
                        orderVal = -1;
                    }

                    // 數字排序
                    if (isNumberType(column.getRight())) {
                        for (String fieldName : column.getLeft()) {
                            try {

                                BigDecimal b1 = NumberUtil.getBigDecimal(MapUtils.getObject(o1, fieldName));
                                BigDecimal b2 = NumberUtil.getBigDecimal(MapUtils.getObject(o2, fieldName));

                                int compareResult = b1.compareTo(b2) * orderVal;
                                if (compareResult != 0) {
                                    return compareResult;
                                }
                            } catch (Exception e) {
                                logger.error("orderbyProcess ERR : " + fieldName, e);
                            }
                        }
                    } else {
                        // 字串排序
                        {

                            String v11 = "";
                            String v22 = "";
                            for (String fieldName : column.getLeft()) {
                                try {
                                    Object v1 = MapUtils.getObject(o1, fieldName);
                                    Object v2 = MapUtils.getObject(o2, fieldName);

                                    v11 += (v1 == null ? "" : String.valueOf(v1));
                                    v22 += (v2 == null ? "" : String.valueOf(v2));
                                } catch (Exception e) {
                                    logger.error("orderbyProcess ERR : " + fieldName, e);
                                }
                            }

                            int compareResult = v11.compareTo(v22) * orderVal;
                            if (compareResult != 0) {
                                return compareResult;
                            }
                        }
                    }
                }

                return 0;
            }
        };

        if (lst != null) {
            Collections.sort(lst, comparator);

            for (Map<String, Object> v : lst) {
                List<String> columns = new ArrayList<String>();
                List<Object> valLst = new ArrayList<Object>();
                for (Triple<String[], String, String> columnDef : orderColumnLst) {
                    for (String column : columnDef.getLeft()) {
                        try {
                            columns.add(column);
                            valLst.add(v.get(column));
                        } catch (Exception e1) {
                        }
                    }
                }
                logger.debug("sort result : " + columns + " -> " + valLst);
            }
        }
        return comparator;
    }
}
