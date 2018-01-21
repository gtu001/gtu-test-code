package gtu.apache;

import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

/**
 * @author Troy 2009/02/02
 * 
 */
public class ArrayUtilsUsage {
    public static void main(String[] args) {
        // data setup
        int[] intArray1 = { 2, 4, 8, 16 };
        int[][] intArray2 = { { 1, 2 }, { 2, 4 }, { 3, 8 }, { 4, 16 } };
        Object[][] notAMap = { { "A", new Double(100) }, { "B", new Double(80) }, { "C", new Double(60) }, { "D", new Double(40) }, { "E", new Double(20) } };

        // printing arrays
        System.out.println("intArray1: " + ArrayUtils.toString(intArray1));
        System.out.println("intArray2: " + ArrayUtils.toString(intArray2));
        System.out.println("notAMap: " + ArrayUtils.toString(notAMap));

        // finding items
        System.out.println("intArray1 contains '8'? " + ArrayUtils.contains(intArray1, 8));
        System.out.println("intArray1 index of '8'? " + ArrayUtils.indexOf(intArray1, 8));
        System.out.println("intArray1 last index of '8'? " + ArrayUtils.lastIndexOf(intArray1, 8));

        // cloning and resversing
        int[] intArray3 = ArrayUtils.clone(intArray1);
        System.out.println("intArray3: " + ArrayUtils.toString(intArray3));
        ArrayUtils.reverse(intArray3);
        System.out.println("intArray3 reversed: " + ArrayUtils.toString(intArray3));

        // primitive to Object array
        Integer[] integerArray1 = ArrayUtils.toObject(intArray1);
        System.out.println("integerArray1: " + ArrayUtils.toString(integerArray1));

        // build Map from two dimensional array
        Map map = ArrayUtils.toMap(notAMap);
        Double res = (Double) map.get("C");
        System.out.println("get 'C' from map: " + res);
    }

    /**
     * 非常讚的ArrayUtils
     */
    private void testArrayUtils() {

        //toMap  第一個元素為key,第二個為value
        Object[] weightArray = new Object[][] { { "H", new Double(1.007) }, { "He", new Double(4.002) }, { "Li", new Double(6.941) }, { "Be", new Double(9.012) }, { "B", new Double(10.811) },
                { "C", new Double(12.010) }, { "N", new Double(14.007) }, { "O", new Double(15.999) }, { "F", new Double(18.998) }, { "Ne", new Double(20.180) } };
        Map weights = ArrayUtils.toMap(weightArray);
        Double hydrogenWeight = (Double) weights.get("H");
        System.out.println(weights);

        // 1.列印陣列
        ArrayUtils.toString(new int[] { 1, 4, 2, 3 });// {1,4,2,3}
        ArrayUtils.toString(new Integer[] { 1, 4, 2, 3 });// {1,4,2,3}
        ArrayUtils.toString(null, "I'm nothing!");// I'm nothing!

        // 2.判斷兩個陣列是否相等,採用EqualsBuilder進行判斷
        // 只有當兩個陣列的資料類型,長度,數值順序都相同的時候,該方法才會返回True
        // 2.1 兩個陣列完全相同
        ArrayUtils.isEquals(new int[] { 1, 2, 3 }, new int[] { 1, 2, 3 });// true
        // 2.2 資料類型以及長度相同,但各個Index上的資料不是一一對應
        ArrayUtils.isEquals(new int[] { 1, 3, 2 }, new int[] { 1, 2, 3 });// true
        // 2.3 陣列的長度不一致
        ArrayUtils.isEquals(new int[] { 1, 2, 3, 3 }, new int[] { 1, 2, 3 });// false
        // 2.4 不同的資料類型
        ArrayUtils.isEquals(new int[] { 1, 2, 3 }, new long[] { 1, 2, 3 });// false
        ArrayUtils.isEquals(new Object[] { 1, 2, 3 }, new Object[] { 1, (long) 2, 3 });// false
        // 2.5 Null處理,如果輸入的兩個陣列都為null時候則返回true
        ArrayUtils.isEquals(new int[] { 1, 2, 3 }, null);// false
        ArrayUtils.isEquals(null, null);// true

        // 3.將一個陣列轉換成Map
        // 如果陣列裏是Entry則其Key與Value就是新Map的Key和Value,如果是Object[]則Object[0]為KeyObject[1]為Value
        // 對於Object[]陣列裏的元素必須是instanceof Object[]或者Entry,即不支援基本資料類型陣列
        // 如:ArrayUtils.toMap(new Object[]{new int[]{1,2},new int[]{3,4}})會出異常
        ArrayUtils.toMap(new Object[] { new Object[] { 1, 2 }, new Object[] { 3, 4 } });// {1=2,
        // 3=4}
        ArrayUtils.toMap(new Integer[][] { new Integer[] { 1, 2 }, new Integer[] { 3, 4 } });// {1=2,
        // 3=4}

        // 4.拷貝陣列
        ArrayUtils.clone(new int[] { 3, 2, 4 });// {3,2,4}

        // 5.截取陣列
        ArrayUtils.subarray(new int[] { 3, 4, 1, 5, 6 }, 2, 4);// {1,5}
        // 起始index為2(即第三個資料)結束index為4的陣列
        ArrayUtils.subarray(new int[] { 3, 4, 1, 5, 6 }, 2, 10);// {1,5,6}
        // 如果endIndex大於陣列的長度,則取beginIndex之後的所有資料

        // 6.判斷兩個陣列的長度是否相等
        ArrayUtils.isSameLength(new Integer[] { 1, 3, 5 }, new Long[] { 2L, 8L, 10L });// true

        // 7.獲得陣列的長度
        ArrayUtils.getLength(new long[] { 1, 23, 3 });// 3

        // 8.判段兩個陣列的類型是否相同
        ArrayUtils.isSameType(new long[] { 1, 3 }, new long[] { 8, 5, 6 });// true
        ArrayUtils.isSameType(new int[] { 1, 3 }, new long[] { 8, 5, 6 });// false

        // 9.陣列反轉
        int[] array = new int[] { 1, 2, 5 };
        ArrayUtils.reverse(array);// {5,2,1}

        // 10.查詢某個Object在陣列中的位置,可以指定起始搜索位置,找不到返回-1
        // 10.1 從正序開始搜索,搜到就返回當前的index否則返回-1
        ArrayUtils.indexOf(new int[] { 1, 3, 6 }, 6);// 2
        ArrayUtils.indexOf(new int[] { 1, 3, 6 }, 2);// -1
        // 10.2 從逆序開始搜索,搜到就返回當前的index否則返回-1
        ArrayUtils.lastIndexOf(new int[] { 1, 3, 6 }, 6);// 2

        // 11.查詢某個Object是否在陣列中
        ArrayUtils.contains(new int[] { 3, 1, 2 }, 1);// true
        // 對於Object資料是調用該Object.equals方法進行判斷
        ArrayUtils.contains(new Object[] { 3, 1, 2 }, 1L);// false

        // 12.基本資料類型陣列與外包型資料類型陣列互轉
        ArrayUtils.toObject(new int[] { 1, 2 });// new Integer[]{Integer,Integer}
        ArrayUtils.toPrimitive(new Integer[] { new Integer(1), new Integer(2) });// new int[]{1,2}

        // 13.判斷陣列是否為空(null和length=0的時候都為空)
        ArrayUtils.isEmpty(new int[0]);// true
        ArrayUtils.isEmpty(new Object[] { null });// false

        // 14.合併兩個陣列
        ArrayUtils.addAll(new int[] { 1, 3, 5 }, new int[] { 2, 4 });// {1,3,5,2,4}

        // 15.添加一個資料到陣列
        ArrayUtils.add(new int[] { 1, 3, 5 }, 4);// {1,3,5,4}

        // 16.刪除陣列中某個位置上的資料
        ArrayUtils.remove(new int[] { 1, 3, 5 }, 1);// {1,5}

        // 17.刪除陣列中某個物件(從正序開始搜索,刪除第一個)
        ArrayUtils.removeElement(new int[] { 1, 3, 5 }, 3);// {1,5}
    }
}
