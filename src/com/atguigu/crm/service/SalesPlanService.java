package com.atguigu.crm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.crm.dao.SalesPlanMapper;
import com.atguigu.crm.entity.SalesPlan;
import com.atguigu.crm.orm.Page;

@Service
public class SalesPlanService {

	@Autowired
	private SalesPlanMapper salesPlanMapper;
	
	@Transactional
	public void execute(SalesPlan salesPlan) {
		salesPlanMapper.execute(salesPlan);
	}
	
	@Transactional(readOnly=true)
	public Set<SalesPlan> getSalesPlans(Long chanceId) {
		return salesPlanMapper.getSalesPlansByChanceId(chanceId);
	}
	
	@Transactional
	public void delete(long id){
		salesPlanMapper.delete(id);
	}
	
	@Transactional
	public void update(SalesPlan salesPlan){
		salesPlanMapper.update(salesPlan);
	}
	
	@Transactional
	public long save(SalesPlan salesPlan) {
		return salesPlanMapper.save(salesPlan);
	}
	
	
	@Transactional(readOnly=true)
	public Page<SalesPlan> getPage(Page<SalesPlan> page) {
		
		long totalElements = salesPlanMapper.getTotalElements(page);
		
		int pageNo = page.getPageNo();
		int pageSize = page.getPageSize();
		int fromIndex = (pageNo-1)*pageSize+1;
		int endIndex = fromIndex + pageSize;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fromIndex", fromIndex);
		params.put("endIndex", endIndex);
		List<SalesPlan> content = salesPlanMapper.getContent(params);
		page.setTotalElements((int)totalElements);
		page.setContent(content);
		
		return page;
	}
}
