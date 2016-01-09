package com.atguigu.crm.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.criteria.From;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import com.atguigu.crm.entity.Customer;

public interface CustomersMapper {

	//Cusomers 数据表中插入 3 个字段：name，no（随机字符串） 和 state（正常）.
	@SelectKey(statement="select crm_seq.nextval from dual",keyProperty="id",before=true,resultType=Long.class)
	@Insert("Insert into customers(id,name,no,state) values(#{id},#{name},#{no},#{state})")
	void save(Customer customer);
	
	List<Customer> getContent(Map<String, Object> map);
	
	Long getTotalElements(Map<String, Object> map);
	
	Customer getCustomerById(Long id);
	
	void updateCustomer(Customer customer);
	
	@Select("select id,name from customers where state = '正常'")
	List<Customer> getAll();
	
	@Select("select id,name from customers where name = #{name}")
	Customer getCustomerByName(String name);
}
