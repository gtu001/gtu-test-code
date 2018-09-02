package gtu.sort;

/**
 * @author gtu 　　因為要 implement Comparable，所以得實作 compareTo() 這個 method。 有了這個
 *         method，那麼 Arrays.sort() 就能依照這個 method 的回傳值來作 sort 的依據。 事實上，primitive
 *         data type 也都有 implement Comparable
 *         [註]，所以才能夠這樣子使用。一般而言，我們習慣的排序結果是由小到大， 所以在 compareTo()
 *         當中「大於」是回傳正值，如果你希望得到的結果是由大到小， 那麼只要 return
 *         的部分改一下，也就可以了。不過不建議這樣子作，會降低程式可讀性； 況且，只要把陣列逆向 traversal 不就得到一樣的結果啦...
 *         那麼... 如果想要用兩種以上的排序方式，那該怎麼辦呢？ 沒關係，還有 Arrays.sort(Object, Comparator)
 *         這個 method， 讓我們可以透過傳遞一個實作 Comparator 的自訂物件，來依照我們的需求排序。 以下是痞子撰寫的
 *         comparator，很機車地以 PsObj.name 的第二個 char 來當作排序的依據：
 * 
 *         2009/02/02
 */
public class PsObj implements Comparable {
    int value;
    String name;

    PsObj(int v, String s) {
        value = v;
        name = s;
    }

    public String toString() {
        return name + "：" + value;
    }

    public int compareTo(Object o1) {
        // 物件本身與 o1 相比較，如果 retrun 正值，就表示比 o1 大
        if (this.value > ((PsObj) o1).value) {
            return 1;
        } else {
            return -1;
        }
    }
}
