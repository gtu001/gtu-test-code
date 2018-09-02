package gtu.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.time.DateFormatUtils;

public class CollectionsTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // 查找替换
        // fill——使用指定元素替换指定列表中的所有元素。
        // frequency——返回指定 collection 中等于指定对象的元素数。
        // indexOfSubList—— 返回指定源列表中第一次出现指定目标列表的起始位置，如果没有出现这样的列表，则返回 -1。
        // lastIndexOfSubList——返回指定源列表中最后一次出现指定目标列表的起始位置，如果没有出现这样的列表，则返回-1。
        // max—— 根据元素的自然顺序，返回给定 collection 的最大元素。
        // min——根据元素的自然顺序 返回给定 collection 的最小元素。
        // replaceAll——使用另一个值替换列表中出现的所有某一指定值。
        //
        // 集合排序
        // Collections还提供了集中对集合进行排序的方法。
        // reverse——对List中的元素倒序排列
        // shuffle——对List中的元素随即排列，这个方法让我想到了Apple的iPod Shuffle
        // sort——对List中的元素排序
        // swap——交换List中某两个指定下标位元素在集合中的位置。
        // rotate——循环移动。循环移动这个方法让人比较难以理解，下面的例子就会让你一下子就理解这个方法的含义。
        // ——————————————————————————————————————————————————————————————————————
        // 假设 list 包含 [t, a, n, k, s]。在调用 Collections.rotate(list, 1)（或
        // Collections.rotate(list, -4)）之后，list 将包含 [s, t, a, n, k]。
        // ——————————————————————————————————————————————————————————————————————
        //
        // 其他方法
        // binarySearch——使用二进制搜索算法来搜索指定列表，以获得指定对象。
        // addAll——将所有指定元素添加到指定 collection 中。
        // copy——将所有元素从一个列表复制到另一个列表。
        // disjoint——如果两个指定 collection 中没有相同的元素，则返回 true。
        // nCopies——返回由指定对象的 n 个副本组成的不可变列表。
        System.out.println("done...");
    }

    private void testListAndEnumeration() {
        Properties prop = System.getProperties();
        Enumeration enu = prop.propertyNames();
        List list = Collections.list(enu);
        System.out.println(list);

        enu = Collections.enumeration(list);
    }

    private void testSynchronizedList() {
        final List<String> list = Collections.synchronizedList(new ArrayList<String>());
        class Run implements Runnable {
            public void run() {
                for (int ii = 0; ii < 10; ii++) {
                    try {
                        String threadName = Thread.currentThread().getName();
                        list.add(threadName.replaceAll("Thread-", ""));
                        long sleepTime = (long) (Math.random() * 5000);
                        System.out.println(threadName + "_"
                                + DateFormatUtils.format(System.currentTimeMillis(), "HH:mm:ss.SSS") + " = " + list
                                + "\tsleep:" + sleepTime);
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        new Thread(new Run()).start();
        new Thread(new Run()).start();
    }

    private void testUnmodifiableList() {
        List<String> list = new ArrayList<String>();
        list = Collections.unmodifiableList(list);
        try {
            list.add("xxxx");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void testSingletonList() {
        List<String> list = null;
        list = Collections.singletonList("ABCDE");
        try {
            list.add("test");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            list.get(2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("get(0)=" + list.get(0));
        System.out.println(list);
    }

    private void testCheckList() {
        List list = new ArrayList();
        list = Collections.checkedList(list, String.class);
        try {
            list.add(new Integer(3));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
