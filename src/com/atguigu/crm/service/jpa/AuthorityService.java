package com.atguigu.crm.service.jpa;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.aspectj.weaver.bcel.BcelAccessForInlineMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.atguigu.crm.entity.Authority;
import com.atguigu.crm.repository.AuthorityRespository;

@Service
public class AuthorityService {

	@Autowired
	private AuthorityRespository authorityRespository;
	
	public List<Authority> getAll() {
		return authorityRespository.findAll();
	}
	
	/**
	 * 获取父权限位空的
	 */
	public List<Authority> getByIsNull(final String propertyName) {
		
		Specification<Authority> spec = new Specification<Authority>() {

			@Override
			public Predicate toPredicate(Root<Authority> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				Path path = root.get(propertyName);
				Predicate predicate = cb.isNull(path);
				
				return predicate;
			}
		};
		
		return authorityRespository.findAll(spec);
	}
	/**
	 * 获取父权限不为空的
	 */
	public List<Authority> getByIsNotNull(final String propertyName) {
		
		Specification<Authority> spec = new Specification<Authority>() {

			@Override
			public Predicate toPredicate(Root<Authority> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path path = root.get(propertyName);
				Predicate predicate = cb.isNotNull(path);
				
				return predicate;
			}
		};
		return authorityRespository.findAll(spec);
	}
}
