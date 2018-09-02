package gtu.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate的基本特徵是完成面向物件的程式設計語言到關聯資料庫的映射, 在Hibernate中使用持久化物件PO(Persistent
 * Object)完成持久化操作, 對PO的操作必須在Session管理下才能同步到資料庫，但是這裏的Session並非指HttpSession, 可以
 * 理解為基於JDBC的Connnection,Session是Hibernate運作的中心， 物件的生命週期、事務的管理、資料庫的存取都與
 * Session息息相關, 首先，我們需要知道，SessionFactory負責創建Session，SessionFactory是線程安全的， 多個並
 * 發線程可以同時訪問一個SessionFactory 並從中獲取Session實例。
 * 而Session並非線程安全，也就是說，如果多個線程同時使用一個Session實例進行資料存取， 則將會導致 Session
 * 資料存取邏輯混亂.因此創建的Session實例必須在本地存取空上運行， 使之總與當前的線程相關。
 * 這裏就需要用到ThreadLocal,在很多種Session 管理方案中都用到了它. ThreadLocal
 * 是Java中一種較為特殊的線程綁定機制，通過ThreadLocal存取的資料， 總是與當前線程相關，也就是說，JVM
 * 為每個運行的線程，綁定了私有的本地實例存取空間， 從而為多線程環境常出現的併發訪問問題提供了一種隔離機制,ThreadLocal並不是線程本地化的 實現,
 * 而是線程局部變數。也就是說每個使用該變數的線程都必須為該變數提供一個副本, 每個線程改變該變數的值僅僅是改變該副本的值,而不會影響其他線程的 該變數的值,
 * ThreadLocal是隔離多個線程的資料共用，不存在多個線程之間共用資源,因此不再需要對線程同步。
 */
public class HibernateUtil {

    public static final SessionFactory sessionFactory;
    public static final ThreadLocal session = new ThreadLocal();

    static {
        try {
            Configuration configuration = new Configuration().configure();
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session currentSession() throws HibernateException {
        Session s = (Session) session.get();
        if (s == null) {
            s = sessionFactory.openSession();
            session.set(s);
        }
        return s;
    }

    public static void closeSession() throws HibernateException {
        Session s = (Session) session.get();
        if (s != null)
            s.close();
        session.set(null);
    }
}