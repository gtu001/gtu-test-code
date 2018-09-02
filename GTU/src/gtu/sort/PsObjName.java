package gtu.sort;

import java.util.Comparator;

/**
 * @author gtu 2009/02/02 其中的細節其實跟上頭沒什麼兩樣，就不再贅述。以下是執行程式與執行結果。
 */
public class PsObjName implements Comparator {
    public int compare(Object obj1, Object obj2) {
        PsObj o1 = (PsObj) obj1;
        PsObj o2 = (PsObj) obj2;

        if (o1.name.charAt(1) > o2.name.charAt(1)) {
            return 1;
        }

        if (o1.name.charAt(1) < o2.name.charAt(1)) {
            return -1;
        }
        return 0;
    }
}
