package com.crm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.crm.pojo.BaseDict;
import com.crm.pojo.Customer;
import com.crm.pojo.QueryVo;
import com.crm.util.Page;



public interface CustomerService {

	Page<Customer> getCustomerList(QueryVo queryVo);
	
	Customer getCustomerById(Long id);
	
	void updateCustomer(Customer customer);
	
	void deleteCustomer(Long id);
	
	List<Customer> exportCustomer();
}
