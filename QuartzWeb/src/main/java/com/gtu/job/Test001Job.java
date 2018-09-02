package com.gtu.job;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.gtu.dao.AnotherBean;

@Component("test001Job")
public class Test001Job extends QuartzJobBean{
    
    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private AnotherBean anotherBean; 
    
    @Override
    protected void executeInternal(JobExecutionContext arg0)
            throws JobExecutionException {
        logger.info("Test001Job running ... start");
        try {
            anotherBean.test();    
        }catch(Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        logger.info("Test001Job running ... end");
    }
 
    public void setAnotherBean(AnotherBean anotherBean) {
        this.anotherBean = anotherBean;
    }
}
