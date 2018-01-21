package gtu.thread.threadLocal;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 不同的執行緒在使用TopicDao時，先判斷connThreadLocal.get()是否是null，如果是null，則說明當前執行緒還沒有對
 * 應的Connection物件，這時創建一個Connection物件並添加到本地執行緒變數中；如果不為null，則說明當前的執行緒已經擁有了
 * Connection物件，直接使用就可以了。這樣，就保證了不同的執行緒使用執行緒相關的Connection，而不會使用其它執行緒的
 * Connection。因此，這個TopicDao就可以做到singleton共用了。
 * 當然，這個例子本身很粗糙，將Connection的ThreadLocal直接放在DAO只能做到本DAO的多個方法共用Connection時
 * 不發生執行緒安全問題，但無法和其它DAO共用同一個Connection，要做到同一事務多DAO共用同一Connection，必須在一個共同的外部類
 * 使用ThreadLocal保存Connection。但這個實例基本上說明了Spring對有狀態類執行緒安全化的解決思路。
 * 
 * @author Troy 2011/12/30
 */
public class TopicDao {

    // ①使用ThreadLocal保存Connection变量
    private static ThreadLocal<Connection> connThreadLocal = new ThreadLocal<Connection>();

    public static Connection getConnection() {
        // ②如果connThreadLocal没有本线程对应的Connection创建一个新的Connection，并将其保存到线程本地变量中。
        if (connThreadLocal.get() == null) {
            Connection conn = null;
            // Connection conn = ConnectionManager.getConnection(); //不能run
            // 看得懂就好
            connThreadLocal.set(conn);
            return conn;
        } else {
            return connThreadLocal.get();// ③直接返回线程本地变量
        }
    }

    public void addTopic() throws SQLException {
        // ④从ThreadLocal中获取线程对应的Connection
        Statement stat = getConnection().createStatement();
    }
}