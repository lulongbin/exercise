package com.crm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.crm.pojo.BaseDict;



public interface BaseDictService {

	List<BaseDict> getDictListByTypeCode(String typeCode);
}
