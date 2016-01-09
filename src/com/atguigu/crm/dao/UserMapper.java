package com.atguigu.crm.dao;

import java.util.List;
import java.util.Map;

import org.apache.catalina.authenticator.SavedRequest;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.atguigu.crm.entity.User;

public interface UserMapper {

	//select id,ENABLED,NAME,PASSWORD,ROLE_ID,SALT from users;
	@Select("Select id,enabled,name,role_id from users")
	List<User> getAll();
	
	//update users set enabled,name,password,role_id where id=
	@Update("update users "
		  + " set enabled=#{enabled} , name=#{name},"
		  + " password=#{password}, role_id=#{role.id}"
		  + " where id = #{id}")
	void update(User user);
	
	@Select("select * from users where id=#{id}")
	User get(Long id);
	
	@Delete("delete from users where id=#{id}")
	void delete(Long id);
	
	@Insert("insert into users(id,enabled,name,password,role_id,salt) "
			+ " values(crm_seq.nextval,#{enabled},#{name},#{password},#{role.id},#{salt}) ")
	void save(User user);
	
	/*@Select("select u.id,u.enabled,u.name,u.password,u.salt,r.name as \"role.name\""
			+ " from users u"
			+ " left outer join roles r "
			+ " on u.role_id=r.id "
			+ " where u.name=#{name}")*/
	User getUserByName(@Param("name") String name);
	
	
	/**
	 * 分页查询 page 中所需的各个属性 
	 */
	
	long getTotalElements(Map<String, Object> map);
	
	List<User> getContent(Map<String, Object> map);
	
}
