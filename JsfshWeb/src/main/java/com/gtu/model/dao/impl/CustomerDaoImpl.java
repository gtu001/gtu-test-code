package com.gtu.model.dao.impl;
 
import java.util.Date;
import java.util.List;
 


import com.gtu.model.Customer;
import com.gtu.model.dao.CustomerDao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
 
public class CustomerDaoImpl extends 
       HibernateDaoSupport implements CustomerDao{
 
	public void addCustomer(Customer customer){
		customer.setCreatedDate(new Date());
		getHibernateTemplate().save(customer);
	}
 
	public List<Customer> findAllCustomer(){
		return (List<Customer>)getHibernateTemplate().find("from Customer");
	}
}