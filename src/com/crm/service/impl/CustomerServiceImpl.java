package com.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.dao.CustomerDao;
import com.crm.pojo.Customer;
import com.crm.pojo.QueryVo;
import com.crm.service.CustomerService;
import com.crm.util.Page;

/***
 * �ͻ�����Service
 * @author Administrator
 *
 */
@Service
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	private CustomerDao customerDao;
	
	@Override
	public Page<Customer> getCustomerList(QueryVo queryVo) {
		// �����ҳ��ʼ��¼
		if(queryVo.getPage() != null){
			// ÿҳ�������Ѿ���sql�����limit���ˣ���ʱֻҪ��ʼ����������
			queryVo.setStart((queryVo.getPage() - 1 ) * queryVo.getSize());
		}
		
		List<Customer> customerList = customerDao.getCustomerList(queryVo);
		
		// ����һ��page����
		Page<Customer> page = new Page<Customer>();
		page.setRows(customerList);
		
		// ��ѯ�ܼ�¼��
		Integer count = customerDao.getCustomerListCount(queryVo);
		page.setTotal(count);
		page.setSize(queryVo.getSize());
		page.setPage(queryVo.getPage());
		
		// ���ؽ��
		return page;
	}

	// ���Կͻ���Ϣ
	@Override
	public Customer getCustomerById(Long id) {
		return customerDao.getCustomerById(id);
	}

	// �����û���Ϣ
	@Override
	public void updateCustomer(Customer customer) {
		
		customerDao.updataCustomer(customer);
		
	}

	// ɾ���û�
	@Override
	public void deleteCustomer(Long id) {
		customerDao.deleteCustomer(id);
	}

	// �����û���Ϣ
	@Override
	public List<Customer> exportCustomer() {
		return customerDao.exportCustomer();
	}

}
