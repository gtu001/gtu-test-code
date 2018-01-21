package gtu.model.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import gtu.model.entity.User;

@Repository
public class UserDaoImpl {
    
    private static Logger logger = Logger.getLogger(UserDaoImpl.class);
    
    @PersistenceContext(unitName="mySqlUnit")
    private EntityManager entityManager;
    
    public int getTotalSize(){
        Query query = entityManager.createNativeQuery("select count(*) as CNT from user_info");
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP); 
        List<Map<String,Object>> resultList = query.getResultList();
        BigInteger val = (BigInteger)resultList.get(0).get("CNT");
        return val.intValue();
    }
    
    public int querySize(String userId, String password){
        Query query = entityManager.createNativeQuery("select count(*) as CNT from user_info where user_id = :userId and password = :password");
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP); 
        query.setParameter("userId", userId);
        query.setParameter("password", password);
        List<Map<String,Object>> resultList = query.getResultList();
        BigInteger val = (BigInteger)resultList.get(0).get("CNT");
        return val.intValue();
    }
    
    public User findByUserId(String userId){
        Query query = entityManager.createQuery("from user_info where userId = :userId");
        query.setParameter("userId", userId);
        return (User)query.getSingleResult();
    }

    @Transactional
    public List<User> findAll(){
        Query query = entityManager.createQuery("from user_info");
        List<User> resultList = query.getResultList();
        return resultList;
    }
    
    @Transactional
    public void insert(User user){
        EntityTransaction trans = entityManager.getTransaction();
        trans.begin();
        entityManager.persist(user);
        trans.commit();
    }
}
