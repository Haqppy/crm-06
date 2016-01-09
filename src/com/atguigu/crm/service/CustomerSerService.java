package com.atguigu.crm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.crm.dao.CustomerServiceMapper;
import com.atguigu.crm.entity.CustomerService;
import com.atguigu.crm.entity.User;
import com.atguigu.crm.orm.Page;
import com.atguigu.crm.orm.PropertyFilter;
import com.atguigu.crm.orm.PropertyFilter.MatchType;
import com.atguigu.crm.utils.ReflectionUtils;

@Service
public class CustomerSerService {

	@Autowired
	private CustomerServiceMapper customerServiceMapper;
	
	@Transactional
	public void delete(Long id) {
		customerServiceMapper.delete(id);
	}
	
	@Transactional
	public void updateAllot(CustomerService customerService) {
		customerServiceMapper.updateAllot(customerService);
	}
	
	@Transactional
	public void updateDealResult(CustomerService customerService) {
		customerServiceMapper.updateDealResult(customerService);
	}
	
	@Transactional
	public void updateServiceDeal(CustomerService customerService) {
		Long id = customerService.getId();
		String serviceDeal = customerService.getServiceDeal();
		
		customerServiceMapper.updateServiceDeal(customerService);
	}
	
	@Transactional(readOnly=true)
	public CustomerService get(Long id) {
		return customerServiceMapper.get(id);
	}
	
	@Transactional(readOnly=true)
	public Page<CustomerService> getPage(User user , Page<CustomerService> page,Map<String, Object> map,Map<String, Object> conditionMap) {
		
		List<PropertyFilter> filters = PropertyFilter.getPropertyFilters(map);
		Map<String, Object> mybatisMap = parsePropertyFilters2MybatisParams(filters);
		mybatisMap.put("userId", user.getId());
		mybatisMap.putAll(conditionMap);
		long totalElements = customerServiceMapper.getTotalElements(mybatisMap);
		page.setTotalElements((int) totalElements);
		
		int fromIndex = (page.getPageNo()-1)*page.getPageSize()+1;
		int endIndex = fromIndex + page.getPageSize();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("fromIndex",fromIndex);
		paramMap.put("endIndex", endIndex);
		mybatisMap.putAll(paramMap);
		
		List<CustomerService> content = customerServiceMapper.getContentCustomerServices(mybatisMap);
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
			
			//search_LIKE_serviceTitle
			//search_EQ_customer
			//search_GTE_createDate1
			//search_LTE_createDate2
			propertyValue = ReflectionUtils.convertValue(propertyValue, propertyType);
			
			if (propertyName.equals("customer.name")) {
				propertyName = "customerName";
			}
			
			MatchType matchType = filter.getMatchType();
			if (matchType == MatchType.LIKE) {
				propertyValue = "%"+propertyValue+"%";
			}
			resultMap.put(propertyName, propertyValue);
		}
		return resultMap;
	}

	@Transactional
	public void save(CustomerService customerService) {
		customerServiceMapper.save(customerService);
	}
	
}
