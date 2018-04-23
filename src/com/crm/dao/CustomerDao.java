package com.crm.dao;

import java.util.List;

import com.crm.pojo.BaseDict;
import com.crm.pojo.Customer;
import com.crm.pojo.QueryVo;

public interface CustomerDao {

	
	List<Customer> getCustomerList(QueryVo queryVo);
	// 查询出总的数据条数
	Integer getCustomerListCount(QueryVo queryVo);
	// 用户信息回显
	Customer getCustomerById(Long id);
	// 更新用户信息
	void updataCustomer(Customer customer);
	// 删除用户
	void deleteCustomer(Long id);
	// 导出用户信息
	List<Customer> exportCustomer();
}
