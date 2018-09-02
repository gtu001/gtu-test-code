package com.iisigroup.ris;

import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@RequestScoped
//@Scope("request")
@Controller(value = "applicationUtil")
public class JSFApplicationUtil {

    private static Logger log = LoggerFactory.getLogger(JSFApplicationUtil.class);

    //        org.apache.myfaces.config.RuntimeConfig
    //        org.apache.myfaces.application.ApplicationImpl

    javax.faces.application.Application application; //interface

    @PostConstruct
    boolean createAppImpl() {
        log.debug("# createAppImpl ..");
        if (application == null) {
            try {
                application = FacesContext.getCurrentInstance().getApplication();
                return true;
            } catch (Exception ex) {
                log.error("application init error!", ex);
            }
        }
        if (application == null) {
            log.debug("error application == null !");
        } else {
            return true;
        }
        return false;
    }

    public void gc() {
        log.debug("# gc ..");
        System.gc();
    }
}
