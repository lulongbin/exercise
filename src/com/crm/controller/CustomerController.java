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
 * �ͻ�����controller
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
		// ���봦��
		if(StringUtils.isNotBlank(queryVo.getCustName())){
			queryVo.setCustName(new String(queryVo.getCustName().getBytes("iso-8859-1"),"utf-8"));
		}
		
		// ��ʼ���ͻ���Ϣ��Դ
		List<BaseDict> sourceList = baseDictService.getDictListByTypeCode(custSorceCode);
		// �ͻ���ҵ
		List<BaseDict> industoryList = baseDictService.getDictListByTypeCode(custIndustoryCode);
		// �ͻ�����
		List<BaseDict> levelList = baseDictService.getDictListByTypeCode(custLevelCode);
		
		// ���ֵ���Ϣ���ݸ�ҳ��
		model.addAttribute("fromType", sourceList);
		model.addAttribute("industryType", industoryList);
		model.addAttribute("levelType", levelList);
		
		// ���ݲ�ѯ������ѯ�ͻ��б�
		Page<Customer> page = customerService.getCustomerList(queryVo);
		model.addAttribute("page",page);
		
		// ��������
		model.addAttribute("custName", queryVo.getCustName());
		model.addAttribute("custSource", queryVo.getCustSource());
		model.addAttribute("custIndustry", queryVo.getCustIndustory());
		model.addAttribute("custLevel", queryVo.getCustLevel());
		
		return "customer";
	}


	// ���Կͻ���Ϣ
	@RequestMapping("/customer/edit")
	@ResponseBody
	public Customer getCustomerById (Long id){
		Customer customer = customerService.getCustomerById(id);
		return customer;
	}
	
	// �����û���Ϣ
	@RequestMapping(value="/customer/update",method=RequestMethod.POST)
	@ResponseBody
	public String updateCustomer(Customer customer){
		customerService.updateCustomer(customer);
		return "OK";
	}
	
	// ɾ���û�
	@RequestMapping("/customer/delete.action")
	@ResponseBody
	public String deleteCustomer(Long id){
		customerService.deleteCustomer(id);
		
		return "OK";
	}
	
	// �����û���Ϣ
	@RequestMapping("/export/excel")
	@ResponseBody
	public void exportCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException{
		// ��ѯ�� ���㵱ǰ���� �������
		List<Customer> customerList = customerService.exportCustomer();

				// ����Excel�ļ�
				HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
				HSSFSheet sheet = hssfWorkbook.createSheet("�ͻ���Ϣ");
				sheet.setDefaultColumnWidth(10);  
				sheet.setDefaultRowHeightInPoints(20);
				
				// ��ͷ
				HSSFRow headRow = sheet.createRow(0);
				headRow.createCell(0).setCellValue("�ͻ�id");
				headRow.createCell(1).setCellValue("�ͻ�����");
				headRow.createCell(2).setCellValue("�ͻ���Դ");
				headRow.createCell(3).setCellValue("�ͻ�������ҵ");
				headRow.createCell(4).setCellValue("�ͻ�����");
				headRow.createCell(5).setCellValue("�̶��绰");
				headRow.createCell(6).setCellValue("�ֻ���");
			
				// �������
				for (Customer customer : customerList) {
					HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
					dataRow.createCell(0).setCellValue(customer.getCust_id());// �ͻ�id
					dataRow.createCell(1).setCellValue(customer.getCust_name());// �ͻ�����
					dataRow.createCell(2).setCellValue(customer.getCust_source());// �ͻ���Դ
					dataRow.createCell(3).setCellValue(customer.getCust_industry());// �ͻ�������ҵ
					dataRow.createCell(4).setCellValue(customer.getCust_linkman());    // �ͻ�����
					dataRow.createCell(5).setCellValue(customer.getCust_phone());  // �̶��绰
					dataRow.createCell(6).setCellValue(customer.getCust_mobile()); // �ֻ���
				}

				// ���ص���
				// ����ͷ��Ϣ
				response.setContentType("application/vnd.ms-excel");
				String filename = "�ͻ���Ϣ.xls";
				String agent = request.getHeader("user-agent");
				filename = FileUtils.encodeDownloadFilename(filename, agent);
				response.setHeader("Content-Disposition",
						"attachment;filename=" + filename);
				
				ServletOutputStream outputStream = response.getOutputStream();
				hssfWorkbook.write(outputStream);
	}
}
