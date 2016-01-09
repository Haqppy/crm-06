package com.atguigu.crm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Update;

import com.atguigu.crm.entity.CustomerDrain;

public interface CustomerDrainMapper {

	@Update("{call drain_procedure2}")
	public void callProcedure();
	
	/**
	 * 准备分页的一些信息
	 */
	
	Long getTotalElements(Map<String, Object> map);
	
	List<CustomerDrain> getContent(Map<String, Object> map);
	
	
	
	
}
