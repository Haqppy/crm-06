package com.atguigu.crm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.atguigu.crm.entity.Contact;

public interface ContactsMapper {

	@Delete("delete from contacts where id = #{id}")
	void delete(Long id);
	
	@Select("select * from contacts where customer_id = #{customerId}")
	List<Contact> getByCustId(Long customerId);
	
	@Update("update contacts "
		 + " set memo=#{memo,jdbcType=VARCHAR},"
		 + "     mobile=#{mobile,jdbcType=VARCHAR},"
		 + "     name=#{name,jdbcType=VARCHAR},"
		 + "     position=#{position,jdbcType=VARCHAR},"
		 + "     sex=#{sex,jdbcType=VARCHAR},"
		 + "     tel=#{tel,jdbcType=VARCHAR},"
		 + "     customer_id=#{customer.id,jdbcType=NUMERIC} "
		 + " where id = #{id}")
	void update(Contact contact);
	
	@Select("select id,name,memo,mobile,position,sex,tel,customer_id as \"customer.id\" "
		  + " from contacts"
		  + " where id = #{id}")
	Contact get(Long id);
	
	@Insert("insert into contacts(id,memo,mobile,name,position,sex,tel,customer_id) "
			+ " values(crm_seq.nextval,#{memo},#{mobile},#{name},#{position},#{sex},#{tel},#{customer.id})")
	void saveWithCust(Contact contact);
	
	/**
	 * 向 contacts 数据表也插入 3 个字段：name、tel、customer_id.
	 * @param contact
	 */
	@Insert("insert into contacts(id,name,tel,customer_id) values(crm_seq.nextval,#{name},#{tel},#{customer.id})")
	void save(Contact contact);
	
	@Select("select id,name from contacts where id = #{id}")
	Contact getContactById(Long id);
	
	@Select("select id,name from contacts where name = #{name}")
	Contact getContactByName(String name);
	//select ID,MEMO,MOBILE,NAME,POSITION,SEX,TEL
	@Select("select id,name,memo,mobile,position,sex,tel from contacts where customer_id = #{customerId}")
	List<Contact> getContactsByCustId(Long customerId);
	
	//对联系人 的分页
	@Select("Select count(*) from contacts where customer_id=#{customerId}")
	Long getTotalElements(Long customerId);
	
	@Select("select * from "
			+ " (select rownum rn, c.id,c.name,c.memo,c.mobile,c.position,c.sex,c.tel,"
			+ "  cust.no ,cust.id as \"customer.id\",cust.name as \"customer.name\" "
			+ "  from contacts c"
			+ "  left outer join customers cust "
			+ "  on c.customer_id = cust.id"
			+ "  where c.customer_id = #{customerId}) t "
			+ " where t.rn >= #{fromIndex} and t.rn < #{endIndex}")
	List<Contact> getContentByCustId(Map<String, Object> map);
	
}
