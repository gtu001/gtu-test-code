package com.gtu.action;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.gtu.model.bo.CustomerBo;

//@ManagedBean
//@Controller(value = "sqlQueryBean")
@Named("sqlQueryBean")
@RequestScoped
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SqlQueryBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(SqlQueryBean.class);

    // DI via Spring
    @Resource(name = "customerBo")
    CustomerBo customerBo;

    private String name;
    private String editText;

    @PostConstruct
    public void init() {
    }

    public String test() {
        logger.info("# test");
        logger.info("name = " + name);
        logger.info("editText = " + editText);
        return "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEditText() {
        return editText;
    }

    public void setEditText(String editText) {
        this.editText = editText;
    }
}
