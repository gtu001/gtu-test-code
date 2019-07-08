package gtu.orderby;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gtu.number.NumberUtil;

public class OrderbyUtil {

    private static final Logger logger = LoggerFactory.getLogger(OrderbyUtil.class);

    // 使用範例 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    private enum _TEST__OrderByEnum implements IOrderByEnum {
        O1("OrderDate_Res"), //
        O2("TrustAcct_Res"), //
        O3("ProjectDesc"), //
        O4("DealType_Res"), //
        O5("FundID"), //
        O6("FundCName"), //
        O7("CurrencyCode_Res", "TrustCapital"), //
        O8("Units"), //
        O9("Nav"), //
        O10("ExRate"), //
        O11("Fee"), //
        O12("TrustFee"), //
        O13("SettleAmt"), //
        O14("ActualTrustCapital"), //
        O15("DistributeAcct_Res"),//
        ;

        List<String> columnLst;

        _TEST__OrderByEnum(String... columns) {
            columnLst = Stream.of(columns).collect(Collectors.toList());
        }

        @Override
        public List<String> getColumnLst() {
            return columnLst;
        }

        @Override
        public List<String> getNumberColumns() {
            return Arrays.asList("Fee", "TrustFee", "TrustFee", "SettleAmt");
        }
    }

    private Comparator getOrderbyComparator(Map<String, Object> reqTranrq) {
        String orderColumn = MapUtils.getString(reqTranrq, "orderColumn");
        String orderType = MapUtils.getString(reqTranrq, "orderType");
        if (StringUtils.isBlank(orderColumn) || StringUtils.isBlank(orderType)) {
            return null;
        }
        return OrderbyUtil.orderbyProcess4Map(Integer.parseInt(orderColumn), orderType, null, _TEST__OrderByEnum.values());
    }

    // 使用範例 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    public interface IOrderByEnum {
        public List<String> getColumnLst();

        public List<String> getNumberColumns();

        public int ordinal();
    }

    public static <T> Comparator orderbyProcess(int orderColumnIndex, String orderType, List<T> lst, IOrderByEnum[] values) {
        if (orderColumnIndex < 0 || StringUtils.isBlank(orderType)) {
            return null;
        }

        AtomicInteger orderVal = new AtomicInteger();
        if ("asc".equalsIgnoreCase(orderType)) {
            orderVal.set(1);
        } else if ("desc".equalsIgnoreCase(orderType)) {
            orderVal.set(-1);
        }

        AtomicReference<IOrderByEnum> setting = new AtomicReference();
        for (IOrderByEnum e : values) {
            if (e.ordinal() == orderColumnIndex) {
                setting.set(e);
                break;
            }
        }

        if (setting.get() == null) {
            return null;
        }

        AtomicReference<List<String>> columnsBak = new AtomicReference<>();

        Comparator comparator = new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {

                List<String> columns = new ArrayList<>(setting.get().getColumnLst());
                columnsBak.set(columns);

                if (CollectionUtils.isNotEmpty(setting.get().getNumberColumns()) && isListCoverEachOther(columns, setting.get().getNumberColumns())) {
                    columns.retainAll(setting.get().getNumberColumns());

                    for (String fieldName : columns) {
                        try {
                            BigDecimal b1 = NumberUtil.getBigDecimal(FieldUtils.readDeclaredField(o1, fieldName, true));
                            BigDecimal b2 = NumberUtil.getBigDecimal(FieldUtils.readDeclaredField(o2, fieldName, true));

                            int compareResult = b1.compareTo(b2) * orderVal.get();
                            if (compareResult != 0) {
                                return compareResult;
                            }
                        } catch (Exception e) {
                            logger.error("orderbyProcess ERR : " + fieldName, e);
                        }
                    }
                    return 0;
                }

                String v11 = "";
                String v22 = "";
                for (String fieldName : columns) {
                    try {
                        Object v1 = FieldUtils.readDeclaredField(o1, fieldName, true);
                        Object v2 = FieldUtils.readDeclaredField(o2, fieldName, true);

                        v11 += (v1 == null ? "" : String.valueOf(v1));
                        v22 += (v2 == null ? "" : String.valueOf(v2));
                    } catch (Exception e) {
                        logger.error("orderbyProcess ERR : " + fieldName, e);
                    }
                }

                return v11.compareTo(v22) * orderVal.get();
            }
        };

        logger.info("sort column : " + columnsBak.get());

        if (lst != null) {
            Collections.sort(lst, comparator);

            for (Object v : lst) {
                List<Object> valLst = new ArrayList<>();
                for (String column : columnsBak.get()) {
                    try {
                        valLst.add(FieldUtils.readDeclaredField(v, column, true));
                    } catch (IllegalAccessException e1) {
                    }
                }
                logger.info("sort result : " + columnsBak.get() + " -> " + valLst);
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

    public static Comparator orderbyProcess4Map(int orderColumnIndex, String orderType, List<Map<String, Object>> lst, IOrderByEnum[] values) {
        if (orderColumnIndex < 0 || StringUtils.isBlank(orderType)) {
            return null;
        }

        AtomicInteger orderVal = new AtomicInteger();
        if ("asc".equalsIgnoreCase(orderType)) {
            orderVal.set(1);
        } else if ("desc".equalsIgnoreCase(orderType)) {
            orderVal.set(-1);
        }

        AtomicReference<IOrderByEnum> setting = new AtomicReference();
        for (IOrderByEnum e : values) {
            if (e.ordinal() == orderColumnIndex) {
                setting.set(e);
                break;
            }
        }

        if (setting.get() == null) {
            return null;
        }

        AtomicReference<List<String>> columnsBak = new AtomicReference<>();

        Comparator comparator = new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {

                List<String> columns = new ArrayList<>(setting.get().getColumnLst());
                columnsBak.set(columns);

                if (CollectionUtils.isNotEmpty(setting.get().getNumberColumns()) && isListCoverEachOther(columns, setting.get().getNumberColumns())) {
                    columns.retainAll(setting.get().getNumberColumns());

                    for (String fieldName : columns) {
                        try {
                            BigDecimal b1 = NumberUtil.getBigDecimal(MapUtils.getObject(o1, fieldName));
                            BigDecimal b2 = NumberUtil.getBigDecimal(MapUtils.getObject(o2, fieldName));

                            int compareResult = b1.compareTo(b2) * orderVal.get();
                            if (compareResult != 0) {
                                return compareResult;
                            }
                        } catch (Exception e) {
                            logger.error("orderbyProcess ERR : " + fieldName, e);
                        }
                    }
                    return 0;
                }

                String v11 = "";
                String v22 = "";
                for (String fieldName : columns) {
                    try {
                        Object v1 = MapUtils.getObject(o1, fieldName);
                        Object v2 = MapUtils.getObject(o2, fieldName);

                        v11 += (v1 == null ? "" : String.valueOf(v1));
                        v22 += (v2 == null ? "" : String.valueOf(v2));
                    } catch (Exception e) {
                        logger.error("orderbyProcess ERR : " + fieldName, e);
                    }
                }

                return v11.compareTo(v22) * orderVal.get();
            }
        };

        logger.info("sort column : " + columnsBak.get());

        if (lst != null) {
            Collections.sort(lst, comparator);

            for (Map<String, Object> v : lst) {
                List<Object> valLst = new ArrayList<>();
                for (String column : columnsBak.get()) {
                    valLst.add(v.get(column));
                }
                logger.info("sort result : " + columnsBak.get() + " -> " + valLst);
            }
        }
        return comparator;
    }
}
