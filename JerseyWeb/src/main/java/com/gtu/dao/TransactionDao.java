package com.gtu.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionDao {
    
    private static final Logger logger = Logger.getLogger(TransactionDao.class);

    public boolean save(){
        logger.info("#. save");
        return true;
    }
}
