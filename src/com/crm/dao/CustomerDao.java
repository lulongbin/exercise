package com.crm.dao;

import java.util.List;

import com.crm.pojo.BaseDict;
import com.crm.pojo.Customer;
import com.crm.pojo.QueryVo;

public interface CustomerDao {

	
	List<Customer> getCustomerList(QueryVo queryVo);
	// ��ѯ���ܵ���������
	Integer getCustomerListCount(QueryVo queryVo);
	// �û���Ϣ����
	Customer getCustomerById(Long id);
	// �����û���Ϣ
	void updataCustomer(Customer customer);
	// ɾ���û�
	void deleteCustomer(Long id);
	// �����û���Ϣ
	List<Customer> exportCustomer();
}
