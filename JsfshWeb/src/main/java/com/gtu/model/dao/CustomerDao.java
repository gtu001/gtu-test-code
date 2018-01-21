package com.gtu.model.dao;
 
import java.util.List;
 

import com.gtu.model.Customer;
 
public interface CustomerDao{
 
	void addCustomer(Customer customer);
 
	List<Customer> findAllCustomer();
 
}