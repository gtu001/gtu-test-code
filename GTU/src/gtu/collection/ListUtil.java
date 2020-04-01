package gtu.collection;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;

import gtu.reflect.ReflectUtil;

public class ListUtil {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("done...");
    }

    public static void setJdk16SortSetting() {
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
    }

    /**
     * 建立一個lazyList當get超出範圍時會動態建立到符合的範圍 Ex : list = [] -> list.get(0) -> [T]
     * list = [] -> list.get(2) -> [null, null, T]
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getLazyList(List<T> list, final T clz) {
        list = ListUtils.lazyList(list, new Factory() {
            @Override
            public Object create() {
                return clz;
            }
        });
        return list;
    }

    /**
     * 將Array轉List
     * 
     * @param array
     * @param clz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, A> List<A> getList(Iterator<T> iterator, Class<A> clz) {
        return (List<A>) getList(iterator);
    }

    /**
     * 將Array轉List
     * 
     * @param array
     * @param clz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, A> List<A> getList(Enumeration<T> enumeration, Class<A> clz) {
        return (List<A>) getList(enumeration);
    }

    /**
     * 取得bean某個field組成List
     * 
     * @param list
     * @param listType
     * @param keyField
     * @param valueField
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getListField(List<?> list, String field, Class<T> newType) throws Exception {
        return (List<T>) getListField(list, field);
    }

    /**
     * 取得bean某個field組成List
     * 
     * @param list
     * @param field
     * @return
     * @throws Exception
     */
    public static List<?> getListField(List<?> list, String field) throws Exception {
        List<Object> rtn = new ArrayList<Object>();
        for (Object obj : list) {
            rtn.add(ReflectUtil.getField(field, obj));
        }
        return rtn;
    }

    /**
     * 將Array轉List
     * 
     * @param iterator
     * @return
     */
    public static <T> List<T> getList(Iterator<T> iterator) {
        List<T> list = new ArrayList<T>();
        for (; iterator.hasNext();) {
            list.add(iterator.next());
        }
        return list;
    }

    /**
     * TODO base method
     * 
     * @param iterator
     * @return
     */
    public static <T> List<T> getList(Enumeration<T> enumeration) {
        return Collections.list(enumeration);
    }

    /**
     * 將Array轉List
     * 
     * @param array
     * @param clz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getList(Object[] array, Class<T> clz) {
        List<T> list = new ArrayList<T>();
        int index = 0;
        for (Object o : array) {
            if (clz.isInstance(o)) {
                list.add((T) o);
            } else {
                throw new ClassCastException(String.format("index : %d, %s can't not be cast %s", index, o.getClass(), clz));
            }
            index++;
        }
        return list;
    }

    /**
     * 將Array轉List
     * 
     * @param array
     * @param clz
     * @return
     */
    public static <T, A> List<T> getList(Collection<A> array, Class<T> clz) {
        return getList(array.toArray(), clz);
    }

    /**
     * 印出此list
     * 
     * @param list
     */
    public static <T> void showListInfo(Collection<T> list, PrintStream out) {
        int index = 0;
        for (Iterator<T> it = list.iterator(); it.hasNext();) {
            T t = it.next();
            Class<?> valClz = t == null ? null : t.getClass();
            out.println("no." + index + " : (" + valClz.getName() + ")[" + t + "]");
            index++;
        }
        out.println("size = " + list.size());
    }

    /**
     * 印出此list
     * 
     * @param list
     */
    public static <T> void showListInfo(Collection<T> list) {
        showListInfo(list, System.out);
    }

    /**
     * 印出此list
     * 
     * @param list
     */
    public static <T> void showListInfo(Enumeration<T> enu) {
        showListInfo(getList(enu));
    }

    /**
     * 印出此list
     * 
     * @param list
     */
    public static <T> void showListInfo(Iterator<T> enu) {
        showListInfo(getList(enu));
    }

    /**
     * 移除元素且忽略大小寫
     * 
     * @param value
     * @param list
     */
    public static void removeIgnorecase(String value, List<String> list) {
        for (int ii = 0; ii < list.size(); ii++) {
            String val = list.get(ii);
            if (StringUtils.equalsIgnoreCase(val, value)) {
                list.remove(ii);
                ii--;
            }
        }
    }

    /**
     * 是否包含字串且忽略大小寫
     * 
     * @param value
     * @param list
     * @return
     */
    public static boolean constainIgnorecase(String value, List<String> list) {
        return -1 != indexOfIgnorecase(value, list);
    }

    public static int indexOfIgnorecase(String value, List<String> list) {
        for (int ii = 0; ii < list.size(); ii++) {
            String val = list.get(ii);
            if (StringUtils.equalsIgnoreCase(val, value)) {
                return ii;
            }
        }
        return -1;
    }

    /**
     * 是否所有元素相同
     * 
     * @param valueLst
     * @return
     */
    public static boolean isAllEquals(List<Object> valueLst) {
        if (valueLst.size() <= 1) {
            return true;
        } else {
            for (int ii = 1; ii < valueLst.size(); ii++) {
                if (valueLst.get(0) != null) {
                    if (!valueLst.get(0).equals(valueLst.get(ii))) {
                        return false;
                    }
                } else if (valueLst.get(ii) != null) {
                    if (!valueLst.get(ii).equals(valueLst.get(0))) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    /**
     * 轉成字串排序且忽略大小寫
     * 
     * @param lst
     */
    @SuppressWarnings("unchecked")
    public static <T> void sortIgnoreCase(List<T> lst) {
        Collections.sort(lst, new Comparator<T>() {
            @Override
            public int compare(Object o1, Object o2) {
                String o1s = o1 == null ? "" : String.valueOf(o1).toLowerCase();
                String o2s = o2 == null ? "" : String.valueOf(o2).toLowerCase();
                return o1s.compareTo(o2s);
            }
        });
    }

    /**
     * 只取第一筆
     * 
     * @param lst
     * @param clz
     * @return
     */
    public static <T> T getFirstOne(List<T> lst, Class<T> clz) {
        if (lst != null && !lst.isEmpty()) {
            return lst.get(0);
        }
        return ReflectUtil.newInstance(clz);
    }

    /**
     * 取得百分比區間的資料
     * 
     * @param orignLst
     * @param startPercent
     * @param endPercent
     * @param comp
     * @return
     */
    public static <T> List<T> getRangePercentLst(List<T> orignLst, double startPercent, double endPercent, Comparator<T> comp) {
        int startPos = (int) ((double) orignLst.size() * startPercent);
        int endPos = (int) ((double) orignLst.size() * endPercent);
        if (endPos >= orignLst.size() - 1) {
            endPos = orignLst.size() - 1;
        }
        List<T> resultLst = orignLst.subList(startPos, endPos + 1);
        if (comp != null) {
            Collections.sort(resultLst, comp);
        }
        return resultLst;
    }

    /**
     * 安全subList
     */
    public static <T> List<T> subList(List<T> lst, int startInclude, int endExclude) {
        if (startInclude < 0) {
            startInclude = 0;
        } else if (startInclude > lst.size()) {
            startInclude = lst.size() - 1;
        }
        if (endExclude < 0) {
            endExclude = 0;
        } else if (endExclude > lst.size()) {
            endExclude = lst.size();
        }
        return lst.subList(startInclude, endExclude);
    }

    public static class PageListUtil {
        /**
         * 取得第幾頁
         * 
         * @param pageNo
         * @param pageSize
         * @param lst
         * @return
         */
        public static <T> List<T> getPageList(int pageNo, int pageSize, List<T> lst) {
            if (lst == null || lst.isEmpty()) {
                return Collections.emptyList();
            }
            int fromIndexInclude = 0;
            int toIndexExclude = 0;
            if (pageNo >= 1) {
                fromIndexInclude = (pageNo - 1) * pageSize;
                toIndexExclude = pageNo * pageSize;
            }
            if (fromIndexInclude > lst.size()) {
                return new ArrayList<T>();
            }
            if (toIndexExclude > lst.size()) {
                toIndexExclude = lst.size();
            }
            return new ArrayList<T>(lst.subList(fromIndexInclude, toIndexExclude));
        }

        /**
         * 是否為最後一頁
         * 
         * @param pageNo
         * @param pageSize
         * @param lst
         * @return
         */
        public static boolean isFinalPage(int pageNo, int pageSize, List<?> lst) {
            if (lst == null || lst.isEmpty()) {
                return false;
            }
            int fromIndexInclude = 0;
            int toIndexExclude = 0;
            if (pageNo >= 1) {
                fromIndexInclude = (pageNo - 1) * pageSize;
                toIndexExclude = pageNo * pageSize;
            }
            if (fromIndexInclude > lst.size()) {
                fromIndexInclude = lst.size() - 1;
                return false;
            }
            if (toIndexExclude > lst.size()) {
                toIndexExclude = lst.size();
            }
            return (toIndexExclude == lst.size());
        }
    }
}
