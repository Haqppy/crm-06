package com.atguigu.crm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.crm.dao.CustomerDrainMapper;
import com.atguigu.crm.entity.CustomerDrain;
import com.atguigu.crm.orm.Page;
import com.atguigu.crm.orm.PropertyFilter;
import com.atguigu.crm.orm.PropertyFilter.MatchType;
import com.atguigu.crm.utils.ReflectionUtils;

@Service
public class CustomerDrainService {

	@Autowired
	private CustomerDrainMapper customerDrainMapper;
	
	@Transactional
	public void checkCustomerDrain() {
		customerDrainMapper.callProcedure();
	}
	
	@Transactional(readOnly=true)
	public Page<CustomerDrain> getPage(Page<CustomerDrain> page,Map<String, Object> map) {
		
		List<PropertyFilter> filters = PropertyFilter.getPropertyFilters(map);
		
		Map<String, Object> myBatisMap = parsePropertyFilters2MybatisParams(filters);
		
		long totalElements = customerDrainMapper.getTotalElements(myBatisMap);
		page.setTotalElements((int) totalElements);
		
		int fromIndex = (page.getPageNo() - 1)* page.getPageSize()+1;
		int endIndex = fromIndex + page.getPageSize();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("fromIndex", fromIndex);
		paramMap.put("endIndex", endIndex);
		
		myBatisMap.putAll(paramMap);
		
		List<CustomerDrain> content = customerDrainMapper.getContent(myBatisMap);
		page.setContent(content);
		
		return page;
	}
	
	@Transactional(readOnly=true)
	public Page<CustomerDrain> getPage(Page<CustomerDrain> page,Map<String, Object> map,String drainDate ) {
		
		List<PropertyFilter> filters = PropertyFilter.getPropertyFilters(map);
		
		Map<String, Object> myBatisMap = parsePropertyFilters2MybatisParams(filters);
		
		myBatisMap.put("drainDate", drainDate);
		
		long totalElements = customerDrainMapper.getTotalElements(myBatisMap);
		page.setTotalElements((int) totalElements);
		
		int fromIndex = (page.getPageNo() - 1)* page.getPageSize()+1;
		int endIndex = fromIndex + page.getPageSize();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("fromIndex", fromIndex);
		paramMap.put("endIndex", endIndex);
		
		myBatisMap.putAll(paramMap);
		
		List<CustomerDrain> content = customerDrainMapper.getContent(myBatisMap);
		page.setContent(content);
		
		return page;
	}

	private Map<String, Object> parsePropertyFilters2MybatisParams(
			List<PropertyFilter> filters) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		for (PropertyFilter filter : filters) {
			String propertyName = filter.getPropertyName();
			Object propertyValue = filter.getPropertyValue();
			Class propertyType = filter.getPropertyType();
			propertyValue = ReflectionUtils.convertValue(propertyValue, propertyType);
			
			MatchType matchType = filter.getMatchType();
			if (matchType == MatchType.LIKE) {
				propertyValue = "%"+propertyValue+"%";
			}
			resultMap.put(propertyName, propertyValue);
		}
		return resultMap;
	}
	
}
