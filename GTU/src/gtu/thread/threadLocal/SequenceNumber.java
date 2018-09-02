package gtu.thread.threadLocal;

/**
 * 
 * 通常我們通過匿名內部類的方式定義ThreadLocal的子類，提供初始的變數值，如例子中①處所示。TestClient執行緒產生一組序號，
 * 在③處，我們生成3個TestClient，它們共用同一個SequenceNumber實例。運行以上代碼，在控制台上輸出以下的結果：
 * 
 * -->請執行看console
 * 
 * 考察輸出的結果資訊，我們發現每個執行緒所產生的序號雖然都共用同一個SequenceNumber實例，但它們並沒有發生相互干擾的情況，而是各自產生獨立的序號
 * ，這是因為我們通過ThreadLocal為每一個執行緒提供了單獨的副本。 Thread同步機制的比較
 * ThreadLocal和執行緒同步機制相比有什麼優勢呢？ThreadLocal和執行緒同步機制都是為了解決多執行緒中相同變數的訪問衝突問題。
 * 在同步機制中，通過物件的鎖機制保證同一時間只有一個執行緒訪問變數。這時該變數是多個執行緒共用的，使用同步機制要求程式慎密地分析什麼時候對變數進行讀寫，
 * 什麼時候需要鎖定某個物件，什麼時候釋放物件鎖等繁雜的問題，程式設計和編寫難度相對較大。
 * 而ThreadLocal則從另一個角度來解決多執行緒的併發訪問。ThreadLocal會為每一個執行緒提供一個獨立的變數副本，從而隔離了多個線
 * 程對資料的訪問衝突。因為每一個執行緒都擁有自己的變數副本，從而也就沒有必要對該變數進行同步了。ThreadLocal提供了執行緒安全的共用物件，在編
 * 寫多執行緒代碼時，可以把不安全的變數封裝進ThreadLocal。
 * 由於ThreadLocal中可以持有任何類型的物件，低版本JDK所提供的get()返回的是Object物件，需要強制類型轉換。但JDK
 * 5.0通過泛型很好的解決了這個問題，在一定程度地簡化ThreadLocal的使用，代碼清單 9 2就使用了JDK
 * 5.0新的ThreadLocal<T>版本。
 * 概括起來說，對於多執行緒資源分享的問題，同步機制採用了“以時間換空間”的方式，而ThreadLocal採用了“
 * 以空間換時間”的方式。前者僅提供一份變數，讓不同的執行緒排隊訪問，而後者為每一個執行緒都提供了一份變數，因此可以同時訪問而互不影響。
 * Spring使用ThreadLocal解決執行緒安全問題
 * 我們知道在一般情況下，只有無狀態的Bean才可以在多執行緒環境下共用，在Spring中，絕大部分Bean都可以聲明為singleton作用
 * 域。就是因為Spring對一些Bean（如RequestContextHolder、
 * TransactionSynchronizationManager、LocaleContextHolder等）中非執行緒安全狀態採用
 * ThreadLocal進行處理，讓它們也成為執行緒安全的狀態，因為有狀態的Bean就可以在多執行緒中共用了。
 * 一般的Web應用劃分為展現層、服務層和持久層三個層次
 * ，在不同的層中編寫對應的邏輯，下層通過介面向上層開放功能調用。在一般情況下，從接收請求到返回回應所經過的所有程式調用都同屬於一個執行緒，如圖9‑2所示：
 * 
 * 
 * 
 * 有繼承ThreadLocal的資料物件
 * 
 * @author Troy 2011/12/30
 */
public class SequenceNumber {

    // ①通过匿名内部类覆盖ThreadLocal的initialValue()方法，指定初始值
    private static ThreadLocal<Integer> seqNum = new ThreadLocal<Integer>() {
        public Integer initialValue() {
            return 0;
        }
    };

    // ②获取下一个序列值
    public int getNextNum() {
        seqNum.set(seqNum.get() + 1);
        return seqNum.get();
    }

    public static void main(String[] args) {
        SequenceNumber sn = new SequenceNumber();
        // ③ 3个线程共享sn，各自产生序列号

        TestClient t1 = new TestClient(sn);
        TestClient t2 = new TestClient(sn);
        TestClient t3 = new TestClient(sn);

        t1.start();
        t2.start();
        t3.start();
    }

    private static class TestClient extends Thread {

        private SequenceNumber sn;

        public TestClient(SequenceNumber sn) {
            this.sn = sn;
        }

        public void run() {
            for (int i = 0; i < 3; i++) {// ④每个线程打出3个序列值
                System.out.println("thread[" + Thread.currentThread().getName() + "] sn[" + sn.getNextNum() + "]");
            }
        }
    }
}