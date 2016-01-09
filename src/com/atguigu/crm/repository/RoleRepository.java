package com.atguigu.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.atguigu.crm.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long>,
		JpaSpecificationExecutor<Role> {

}
