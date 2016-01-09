package com.atguigu.crm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.atguigu.crm.entity.CustomerService;

public interface CustomerServiceMapper {
	
	void save(CustomerService customerService);
	
	Long getTotalElements(Map<String, Object> map);
	
	List<CustomerService> getContentCustomerServices(Map<String, Object> map);
	
	@Select("select cs.id,allot_date,"
		 + " create_date,deal_date,"
		 + " deal_result,satisfy,"
		 + " service_deal, service_request,"
		 + " service_state, service_title,"
		 + " service_type, "
		 + " allot_id as \"allotTo.id\", "
		 + " created_id as \"createdby.id\", "
		 + " customer_id as \"customer.id\","
		 + " a.name as \"allotTo.name\","
		 + " c.name as \"createdby.name\",cust.name as \"customer.name\" "
		 + " from customer_services cs "
		 + " left outer join users a"
		 + " on a.id = cs.allot_id "
		 + " left outer join users c"
		 + " on c.id = cs.created_id "
		 + " left outer join customers cust"
		 + " on cust.id = cs.customer_id "
		 + " where cs.id = #{id} ")
	CustomerService get(Long id);
	
	@Update("update customer_services "
		 + " set service_deal=#{serviceDeal},"
		 + " service_state=#{serviceState},"
		 + " deal_date=#{dealDate} "
		 + " where id = #{id}")
	void updateServiceDeal(CustomerService customerService);
	
	@Update("update customer_services "
		  + "set deal_result = #{dealResult},"
		  + "satisfy=#{satisfy} "
		  + " where id = #{id}")
	void updateDealResult(CustomerService customerService);
	
	@Update("update customer_services "
		  + "set service_state=#{serviceState} , "
		  + "    allot_id=#{allotTo.id},"
		  + "    allot_date=#{allotDate} "
		  + "where id = #{id}")
	void updateAllot(CustomerService customerService);
	
	@Delete("delete from customer_services where id = #{id}")
	void delete(Long id);
}
