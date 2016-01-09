package com.atguigu.crm.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import com.atguigu.crm.entity.SalesPlan;
import com.atguigu.crm.orm.Page;


public interface SalesPlanMapper {
	
	//不是由销售自身触发的业务，是辅助销售机会的相关查询，可以看做一个帮助类
	Set<SalesPlan> getSalesPlansByChanceId(Long chanceId);
	
	@Select(value="select count(id) from sales_plan")
	long getTotalElements(Page<SalesPlan> page);
	
	List<SalesPlan> getContent(Map<String, Object> map);
	
	@SelectKey(statement="select crm_seq.nextval from dual ",keyProperty="id",before=true,resultType=Long.class)
	@Insert("insert into sales_plan(id,plan_date,todo,chance_id) "
		  + "values(#{id},#{date},#{todo},#{chance.id}) ")
	long save(SalesPlan salesPlan);
	
	@Delete("delete from sales_plan where id = #{id}")
	int delete(Long id);
	
	@Update("update sales_plan set todo=#{todo}"
			+ " where id=#{id} ")
	void update(SalesPlan salesPlan);
	
	@Update("update sales_plan set plan_result=#{result} where id=#{id}")
	void execute(SalesPlan salesPlan);
	
}
