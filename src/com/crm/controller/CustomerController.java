package com.crm.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crm.pojo.BaseDict;
import com.crm.pojo.Customer;
import com.crm.pojo.QueryVo;
import com.crm.service.BaseDictService;
import com.crm.service.CustomerService;
import com.crm.util.FileUtils;
import com.crm.util.Page;

/**
 * 客户管理controller
 * 
 * @author Administrator
 *
 */

@Controller
public class CustomerController {

	@Autowired
	private BaseDictService baseDictService;
	@Autowired
	private CustomerService customerService;
	@Value("${customer.source.code}")
	private String custSorceCode;
	@Value("${customer.industory.code}")
	private String custIndustoryCode;
	@Value("${customer.level.code}")
	private String custLevelCode;

	
	@RequestMapping("/customer/list")
	public String showCustomerList(QueryVo queryVo,Model model) throws Exception{
		// 乱码处理
		if(StringUtils.isNotBlank(queryVo.getCustName())){
			queryVo.setCustName(new String(queryVo.getCustName().getBytes("iso-8859-1"),"utf-8"));
		}
		
		// 初始化客户信息来源
		List<BaseDict> sourceList = baseDictService.getDictListByTypeCode(custSorceCode);
		// 客户行业
		List<BaseDict> industoryList = baseDictService.getDictListByTypeCode(custIndustoryCode);
		// 客户级别
		List<BaseDict> levelList = baseDictService.getDictListByTypeCode(custLevelCode);
		
		// 把字典信息传递给页面
		model.addAttribute("fromType", sourceList);
		model.addAttribute("industryType", industoryList);
		model.addAttribute("levelType", levelList);
		
		// 根据查询条件查询客户列表
		Page<Customer> page = customerService.getCustomerList(queryVo);
		model.addAttribute("page",page);
		
		// 参数回显
		model.addAttribute("custName", queryVo.getCustName());
		model.addAttribute("custSource", queryVo.getCustSource());
		model.addAttribute("custIndustry", queryVo.getCustIndustory());
		model.addAttribute("custLevel", queryVo.getCustLevel());
		
		return "customer";
	}


	// 回显客户信息
	@RequestMapping("/customer/edit")
	@ResponseBody
	public Customer getCustomerById (Long id){
		Customer customer = customerService.getCustomerById(id);
		return customer;
	}
	
	// 更新用户信息
	@RequestMapping(value="/customer/update",method=RequestMethod.POST)
	@ResponseBody
	public String updateCustomer(Customer customer){
		customerService.updateCustomer(customer);
		return "OK";
	}
	
	// 删除用户
	@RequestMapping("/customer/delete.action")
	@ResponseBody
	public String deleteCustomer(Long id){
		customerService.deleteCustomer(id);
		
		return "OK";
	}
	
	// 导出用户信息
	@RequestMapping("/export/excel")
	@ResponseBody
	public void exportCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException{
		// 查询出 满足当前条件 结果数据
		List<Customer> customerList = customerService.exportCustomer();

				// 生成Excel文件
				HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
				HSSFSheet sheet = hssfWorkbook.createSheet("客户信息");
				sheet.setDefaultColumnWidth(10);  
				sheet.setDefaultRowHeightInPoints(20);
				
				// 表头
				HSSFRow headRow = sheet.createRow(0);
				headRow.createCell(0).setCellValue("客户id");
				headRow.createCell(1).setCellValue("客户姓名");
				headRow.createCell(2).setCellValue("客户来源");
				headRow.createCell(3).setCellValue("客户所属行业");
				headRow.createCell(4).setCellValue("客户级别");
				headRow.createCell(5).setCellValue("固定电话");
				headRow.createCell(6).setCellValue("手机号");
			
				// 表格数据
				for (Customer customer : customerList) {
					HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
					dataRow.createCell(0).setCellValue(customer.getCust_id());// 客户id
					dataRow.createCell(1).setCellValue(customer.getCust_name());// 客户姓名
					dataRow.createCell(2).setCellValue(customer.getCust_source());// 客户来源
					dataRow.createCell(3).setCellValue(customer.getCust_industry());// 客户所属行业
					dataRow.createCell(4).setCellValue(customer.getCust_linkman());    // 客户级别
					dataRow.createCell(5).setCellValue(customer.getCust_phone());  // 固定电话
					dataRow.createCell(6).setCellValue(customer.getCust_mobile()); // 手机号
				}

				// 下载导出
				// 设置头信息
				response.setContentType("application/vnd.ms-excel");
				String filename = "客户信息.xls";
				String agent = request.getHeader("user-agent");
				filename = FileUtils.encodeDownloadFilename(filename, agent);
				response.setHeader("Content-Disposition",
						"attachment;filename=" + filename);
				
				ServletOutputStream outputStream = response.getOutputStream();
				hssfWorkbook.write(outputStream);
	}
}
