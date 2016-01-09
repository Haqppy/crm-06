package com.atguigu.crm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.crm.dao.ContactsMapper;
import com.atguigu.crm.dao.CustomersMapper;
import com.atguigu.crm.entity.Contact;
import com.atguigu.crm.entity.Customer;
import com.atguigu.crm.orm.Page;
import com.atguigu.crm.orm.PropertyFilter;
import com.atguigu.crm.orm.PropertyFilter.MatchType;
import com.atguigu.crm.utils.ReflectionUtils;

@Service
public class CustomersService {

	@Autowired
	private CustomersMapper customersMapper;
	@Autowired
	private ContactsMapper contactsMapper;
	
	@Transactional(readOnly=true)
	public List<Customer> getAll() {
		return customersMapper.getAll();
	}
	
	@Transactional
	public void update(Customer customer) {
		customersMapper.updateCustomer(customer);
	}
	
	@Transactional(readOnly=true)
	public Customer getCustomer(Long id) {
		Customer customer = customersMapper.getCustomerById(id);
		return customer;
	}
	
	@Transactional(readOnly=true)
	public Page<Customer> getPage(Page<Customer> page,Map<String, Object> map) {
		//解析Map
		List<PropertyFilter> filters = PropertyFilter.getPropertyFilters(map);
		Map<String, Object> mybatisMap = parsePropertyFilters2MybatisParams(filters);
				
		long totalElements = customersMapper.getTotalElements(mybatisMap);
		page.setTotalElements((int)totalElements);
		
		int fromIndex = (page.getPageNo()-1)*page.getPageSize()+1;
		int endIndex = page.getPageSize() + fromIndex;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("fromIndex", fromIndex);
		paramMap.put("endIndex", endIndex);
		mybatisMap.putAll(paramMap);
		
		List<Customer> content = customersMapper.getContent(mybatisMap);
		page.setContent(content);
		
		return page;
	}
	
	private Map<String, Object> parsePropertyFilters2MybatisParams(
			List<PropertyFilter> filters) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		for (PropertyFilter filter : filters) {
			String propertyName = filter.getPropertyName();
			Object propertyValue = filter.getPropertyValue();
			Class propertyType = filter.getPropertyType();
			
			propertyValue = ReflectionUtils.convertValue(propertyValue, propertyType);
			MatchType matchType = filter.getMatchType();
			
			if (propertyName.equals("manager.name")) {
				propertyName = "manager_name";
			}
			
			if (matchType == MatchType.LIKE) {
				propertyValue = "%"+propertyValue+"%";
			}
			
			map.put(propertyName, propertyValue);
		}
		
		return map;
	}

	@Transactional
	public void save(Customer customer) {
		customersMapper.save(customer);
	}
	
}
