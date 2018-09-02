package com.gtu.action;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.appfuse.webapp.util.FacesUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.gtu.model.Customer;
import com.gtu.model.bo.CustomerBo;

@ManagedBean
@Controller(value = "customer")
@RequestScoped
// @SessionScoped
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(CustomerBean.class);

    // DI via Spring
    @Resource(name = "customerBo")
    CustomerBo customerBo;

    @ManagedProperty("#{customer.name}")
    public String name;
    @ManagedProperty("#{customer.address}")
    public String address;

    /**
     * 與@Controller(value = "customer")不相容
     */
    @PostConstruct
    private void init() {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            ServletContext servletContext = (ServletContext) externalContext.getContext();
            WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext).getAutowireCapableBeanFactory().autowireBean(this);
        } catch (Exception ex) {
        }
    }

    public List<Customer> getCustomerList() {
        logger.info("#. getCustomerList");
        return customerBo.findAllCustomer();
    }

    // add a new customer data into database
    public String addCustomer() {
        logger.info("#. addCustomer");
        Customer cust = new Customer();
        cust.setName(getName());
        cust.setAddress(getAddress());
        customerBo.addCustomer(cust);
        clearForm();
        return "";
    }

    public String test() {
        logger.info("#. test");
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        logger.info("f1 " + FacesContext.getCurrentInstance());
        logger.info("f2 " + externalContext);

        FacesUtils.addInfoMessage("ddddddddddddd");

        return "";
    }

    // clear form values
    private void clearForm() {
        setName("");
        setAddress("");
    }

    // GetSet--------------------------------------------------------------------
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCustomerBo(CustomerBo customerBo) {
        this.customerBo = customerBo;
    }
}
