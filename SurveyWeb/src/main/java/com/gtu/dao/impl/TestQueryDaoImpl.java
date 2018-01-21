package com.gtu.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

@Repository
public class TestQueryDaoImpl {
    
    private Logger logger = Logger.getLogger(getClass());
    
    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    public void testQuery(){
        Session session = sessionFactory.getCurrentSession();
        SQLQuery query = session.createSQLQuery("select * from user_info");
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String,Object>> list = query.list();
        for(Map<String,Object> m : list){
            logger.info("" + m);
        }
    }
}
