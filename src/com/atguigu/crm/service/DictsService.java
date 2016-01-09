package com.atguigu.crm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.crm.dao.DictsMapper;
import com.atguigu.crm.entity.Dict;

@Service
public class DictsService {

	@Autowired
	private DictsMapper dictsMapper;
	
	@Transactional(readOnly=true)
	public List<Dict> getItems(String type) {
		
		return dictsMapper.getItem(type);
		
	}
}
