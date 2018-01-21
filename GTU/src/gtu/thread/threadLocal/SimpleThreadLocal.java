package gtu.thread.threadLocal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 我們知道Spring通過各種DAO範本類降低了開發者使用各種資料持久技術的難度。這些範本類都是執行緒安全的，也就是說，
 * 多個DAO可以複用同一個範本實例而不會發生衝突。
 * 我們使用範本類訪問底層資料，根據持久化技術的不同，範本類需要綁定資料連接或會話的資源。但這些資源本身是非執行緒安全的
 * ，也就是說它們不能在同一時刻被多個執行緒共用。
 * 雖然範本類通過資源池獲取資料連接或會話，但資源池本身解決的是資料連接或會話的緩存問題，並非資料連接或會話的執行緒安全問題。
 * 按照傳統經驗，如果某個物件是非執行緒安全的
 * ，在多執行緒環境下，對物件的訪問必須採用synchronized進行執行緒同步。但Spring的DAO範本類並未採用執行緒同步機制
 * ，因為執行緒同步限制了併發訪問，會帶來很大的性能損失。
 * 此外，通過代碼同步解決性能安全問題挑戰性很大，可能會增強好幾倍的實現難度。那範本類究竟仰丈何種魔法神功
 * ，可以在無需同步的情況下就化解執行緒安全的難題呢？答案就是ThreadLocal！
 * ThreadLocal在Spring中發揮著重要的作用，在管理request作用域的Bean
 * 、事務管理、任務調度、AOP等模組都出現了它們的身影，起著舉足輕重的作用
 * 。要想瞭解Spring事務管理的底層技術，ThreadLocal是必須攻克的山頭堡壘。 ThreadLocal是什麼 早在JDK
 * 1.2的版本中就提供java.lang.ThreadLocal，ThreadLocal為解決多執行緒程式的併發問題提供了一種新的思路。
 * 使用這個工具類可以很簡潔地編寫出優美的多執行緒程式。
 * ThreadLocal很容易讓人望文生義，想當然地認為是一個“本地執行緒”。其實，ThreadLocal並不是一個Thread
 * ，而是Thread的區域變數，也許把它命名為ThreadLocalVariable更容易讓人理解一些。
 * 當使用ThreadLocal維護變數時，ThreadLocal為每個使用該變數的執行緒提供獨立的變數副本
 * ，所以每一個執行緒都可以獨立地改變自己的副本，而不會影響其它執行緒所對應的副本。
 * 從執行緒的角度看，目標變數就像是執行緒的本地變數，這也是類名中“Local”所要表達的意思。 執行緒區域變數並不是Java的新發明，很多語言（如IBM
 * IBM XL FORTRAN）在語法層面就提供執行緒區域變數。在Java中沒有提供在語言級支援，而是變相地通過ThreadLocal的類提供支援。
 * 所以，在Java中編寫執行緒區域變數的代碼相對來說要笨拙一些，因此造成執行緒區域變數沒有在Java開發者中得到很好的普及。
 * ThreadLocal的介面方法 ThreadLocal類介面很簡單，只有4個方法，我們先來瞭解一下：
 * 
 * • void set(Object value) 設置當前執行緒的執行緒區域變數的值。
 * 
 * • public Object get() 該方法返回當前執行緒所對應的執行緒區域變數。
 * 
 * • public void remove() 將當前執行緒區域變數的值刪除，目的是為了減少記憶體的佔用，該方法是JDK
 * 5.0新增的方法。需要指出的是，當執行緒結束後，對應該執行緒的區域變數將自動被垃圾回收
 * ，所以顯式調用該方法清除執行緒的區域變數並不是必須的操作，但它可以加快記憶體回收的速度。
 * 
 * • protected Object initialValue()
 * 返回該執行緒區域變數的初始值，該方法是一個protected的方法，顯然是為了讓子類覆蓋而設計的。這個方法是一個延遲調用方法，
 * 在執行緒第1次調用get()或set(Object)時才執行，並且僅執行1次。ThreadLocal中的缺省實現直接返回一個null。
 * 值得一提的是，在JDK5.0中，ThreadLocal已經支持泛型，該類的類名已經變為ThreadLocal<T>。API方法
 * 也相應進行了調整，新版本的API方法分別是void set(T value)、T get()以及T initialValue()。
 * ThreadLocal是如何做到為每一個執行緒維護變數的副本的呢？其實實現的思路很簡單
 * ：在ThreadLocal類中有一個Map，用於存儲每一個執行緒的變數副本
 * ，Map中元素的鍵為執行緒物件，而值對應執行緒的變數副本。我們自己就可以提供一個簡單的實現版本：
 * 
 * @author Troy 2011/12/30
 */
public class SimpleThreadLocal {

    private Map valueMap = Collections.synchronizedMap(new HashMap());

    public void set(Object newValue) {
        valueMap.put(Thread.currentThread(), newValue);// ①键为线程对象，值为本线程的变量副本
    }

    public Object get() {
        Thread currentThread = Thread.currentThread();
        Object o = valueMap.get(currentThread);// ②返回本线程对应的变量
        if (o == null && !valueMap.containsKey(currentThread)) {// ③如果在Map中不存在，放到Map中保存起来。
            o = initialValue();
            valueMap.put(currentThread, o);
        }
        return o;
    }

    public void remove() {
        valueMap.remove(Thread.currentThread());
    }

    public Object initialValue() {
        return null;
    }

}