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
 * 客户管理Service
 * @author Administrator
 *
 */
@Service
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	private CustomerDao customerDao;
	
	@Override
	public Page<Customer> getCustomerList(QueryVo queryVo) {
		// 计算分页起始记录
		if(queryVo.getPage() != null){
			// 每页的数据已经在sql语句中limit过了，此时只要起始的条数即可
			queryVo.setStart((queryVo.getPage() - 1 ) * queryVo.getSize());
		}
		
		List<Customer> customerList = customerDao.getCustomerList(queryVo);
		
		// 创建一个page对象
		Page<Customer> page = new Page<Customer>();
		page.setRows(customerList);
		
		// 查询总记录数
		Integer count = customerDao.getCustomerListCount(queryVo);
		page.setTotal(count);
		page.setSize(queryVo.getSize());
		page.setPage(queryVo.getPage());
		
		// 返回结果
		return page;
	}

	// 回显客户信息
	@Override
	public Customer getCustomerById(Long id) {
		return customerDao.getCustomerById(id);
	}

	// 更新用户信息
	@Override
	public void updateCustomer(Customer customer) {
		
		customerDao.updataCustomer(customer);
		
	}

	// 删除用户
	@Override
	public void deleteCustomer(Long id) {
		customerDao.deleteCustomer(id);
	}

	// 导出用户信息
	@Override
	public List<Customer> exportCustomer() {
		return customerDao.exportCustomer();
	}

}
