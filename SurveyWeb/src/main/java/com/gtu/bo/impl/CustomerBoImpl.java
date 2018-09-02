package com.gtu.bo.impl;

import java.util.List;

import com.gtu.bo.CustomerBo;
import com.gtu.dao.CustomerDao;
import com.gtu.model.Customer;
 
public class CustomerBoImpl implements CustomerBo{
 
	CustomerDao customerDao;
	
	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	public void addCustomer(Customer customer){
		
		customerDao.addCustomer(customer);

	}
	
	public List<Customer> findAllCustomer(){
		
		return customerDao.findAllCustomer();
	}
 
}