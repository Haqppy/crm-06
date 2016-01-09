package com.atguigu.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.atguigu.crm.entity.Storage;

public interface StorageRepository extends JpaRepository<Storage, Long>,
		JpaSpecificationExecutor<Storage> {

}
