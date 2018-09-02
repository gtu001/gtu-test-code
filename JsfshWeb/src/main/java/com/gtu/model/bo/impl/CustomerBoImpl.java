package com.gtu.model.bo.impl;
 
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gtu.model.Customer;
import com.gtu.model.bo.CustomerBo;
import com.gtu.model.dao.CustomerDao;
 
@Repository
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