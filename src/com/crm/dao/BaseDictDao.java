package com.crm.dao;

import java.util.List;

import com.crm.pojo.BaseDict;

public interface BaseDictDao {

	
	List<BaseDict> getDictListByTypeCode(String typeCode);
	
}
