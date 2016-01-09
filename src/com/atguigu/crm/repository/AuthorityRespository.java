package com.atguigu.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.atguigu.crm.entity.Authority;

public interface AuthorityRespository extends JpaRepository<Authority, Long>,
		JpaSpecificationExecutor<Authority> {

	/*@Query(value="select a from Authority a where a.name is null")
	List<Authority> getByIsNull(String propertyName);*/
	
	/*
	@Query(value="select id,name,"
			+ "display_name as displayName,"
			+ "permissions,"
			+ "parent_authority_id as \"parentAuthority.id\" "
			+ " where parent_authority_id=id",nativeQuery=true)
	List<Authority> getByParentAuthorityId(Long id);*/
	
}
