package com.atguigu.crm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

import com.atguigu.crm.entity.SalesChance;

/**
 * 营销机会
 * @author Java
 *
 */
public interface SalesChanceMapper {

	
	@Select("select s.id,s.contact,s.contact_tel,"
			+ " s.create_date,s.cust_name,s.description,s.rate,s.source,s.status, s.title,"
			+ "u.name as \"createBy.name\", d.name as \"designee.name\" ,r.name as \"createBy.role.name\" "
			+ "from sales_chances s "
			+ "Left outer join users u "
			+ "on s.created_user_id=u.id "
			+ "Left outer join users d "
			+ "on s.designee_id=d.id "
			+ "Left outer join roles r "
			+ "on u.role_id = r.id "
			+ "where s.id=#{id}")
	@Results({
		@Result(property="salesPlans",column="s.id",many=@Many(select="com.atguigu.crm.dao.SalesPlanMapper.getSalesPlansByChanceId"))
	})
	SalesChance getSalesChanceById(@Param("id") Long id);
	
	/**
	 * 修改
	 */
	@Update("update sales_chances "
		  + "set "
		  + "contact=#{contact}, "
		  + "contact_tel=#{contactTel}, "
		  + "create_date=#{createDate}, "
		  + "cust_name=#{custName}, "
		  + "description=#{description}, "
		  + "rate=#{rate}, "
		  + "source=#{source}, "
		  + "status=#{status}, "
		  + "title=#{title},"
		  + "designee_id=#{designee.id}, "
		  + "designee_date=#{designeeDate} "
		  + "where id=#{id}")
	void update(SalesChance salesChance);
	
	void updateStatus(SalesChance salesChance);
	
	/**
	 * 删除
	 */
	@Delete("delete from sales_chances where id = #{id}")
	void delete(Long id);
	
	@Insert("insert into "
			+ "sales_chances(ID, CONTACT, CONTACT_TEL,"
			+ " CREATE_DATE, CUST_NAME, DESCRIPTION, "
			+ " RATE, SOURCE, STATUS, "
			+ " TITLE, CREATED_USER_ID) "
			+ " values(crm_seq.nextval,#{contact}, #{contactTel}, "
			+ " #{createDate}, #{custName}, #{description},"
			+ " #{rate}, #{source}, #{status},#{title} , #{createBy.id})")
	void save(SalesChance salesChance);
	
	/**
	 * 分页 需要用的一些信息 封装 就在 service 中 封装吧
	 */
	long getTotalElements(Map<String, Object> map);
	
	List<SalesChance> getContent(Map<String, Object> map);
	
}
