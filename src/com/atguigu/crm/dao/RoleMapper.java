package com.atguigu.crm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.atguigu.crm.entity.Role;

public interface RoleMapper {

	@Select("select id,name from roles where enabled=1")
	List<Role> getRoles();
	void save();
	
}
