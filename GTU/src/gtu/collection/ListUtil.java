package gtu.collection;

import gtu.reflect.ReflectUtil;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;

public class ListUtil {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("done...");
    }

    /**
     * 建立一個lazyList當get超出範圍時會動態建立到符合的範圍
     * Ex : list = [] -> list.get(0) -> [T]
     *      list = [] -> list.get(2) -> [null, null, T]
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

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<? super T>, A> List<A> getSortList(Iterator<T> iterator, Class<A> clz) {
        return (List<A>) getSortList(iterator);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<? super T>, A> List<A> getSortList(Enumeration<T> enumeration, Class<A> clz) {
        return (List<A>) getSortList(enumeration);
    }

    public static <T extends Comparable<? super T>> List<T> getSortList(Iterator<T> iterator) {
        List<T> list = getList(iterator);
        Collections.sort(list);
        return list;
    }

    public static <T extends Comparable<? super T>> List<T> getSortList(Enumeration<T> enumeration) {
        List<T> list = getList(enumeration);
        Collections.sort(list);
        return list;
    }

    @SuppressWarnings("unchecked")
    public static <T, A> List<A> getList(Iterator<T> iterator, Class<A> clz) {
        return (List<A>) getList(iterator);
    }

    @SuppressWarnings("unchecked")
    public static <T, A> List<A> getList(Enumeration<T> enumeration, Class<A> clz) {
        return (List<A>) getList(enumeration);
    }

    /**
     * 呼叫list的Type的filed的getter為新的List的Type
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

    public static List<?> getListField(List<?> list, String field) throws Exception {
        List<Object> rtn = new ArrayList<Object>();
        for (Object obj : list) {
            rtn.add(ReflectUtil.getField(field, obj));
        }
        return rtn;
    }

    /**
     * TODO base method
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

    public static <T> void showListInfo(Enumeration<T> enu) {
        showListInfo(getList(enu));
    }

    public static <T> void showListInfo(Iterator<T> enu) {
        showListInfo(getList(enu));
    }

    public static void removeIgnorecase(String value, List<String> list) {
        for (int ii = 0; ii < list.size(); ii++) {
            String val = list.get(ii);
            if (StringUtils.equalsIgnoreCase(val, value)) {
                list.remove(ii);
                ii--;
            }
        }
    }

    public static boolean constainIgnorecase(String value, List<String> list) {
        for (int ii = 0; ii < list.size(); ii++) {
            String val = list.get(ii);
            if (StringUtils.equalsIgnoreCase(val, value)) {
                return true;
            }
        }
        return false;
    }
}
