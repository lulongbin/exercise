package com.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.dao.BaseDictDao;
import com.crm.pojo.BaseDict;
import com.crm.service.BaseDictService;

/**
 * ×Öµä±í´¦Àíservice
 * @author Administrator
 *
 */
@Service
public class BaseDictServiceImpl implements BaseDictService{

	
	@Autowired 
	private BaseDictDao baseDictDao;
	@Override
	public List<BaseDict> getDictListByTypeCode(String typeCode) {
		List<BaseDict> list = baseDictDao.getDictListByTypeCode(typeCode);
		return list;
	}

}
