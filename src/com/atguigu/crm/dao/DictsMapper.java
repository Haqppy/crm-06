package com.atguigu.crm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.atguigu.crm.entity.Dict;

public interface DictsMapper {

	@Select("select id,type,item from dicts where type=#{type}")
	List<Dict> getItem(String type);
}
