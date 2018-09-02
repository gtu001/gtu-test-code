package com.gtu.bo;

import java.util.List;

import com.gtu.model.Customer;
 
public interface CustomerBo{
 
	void addCustomer(Customer customer);
	
	List<Customer> findAllCustomer();
 
}