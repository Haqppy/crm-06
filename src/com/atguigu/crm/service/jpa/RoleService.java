package com.atguigu.crm.service.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.atguigu.crm.entity.Role;
import com.atguigu.crm.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;
	
	public List<Role> getRoles() {
		return roleRepository.findAll();
	}
	
	public Role get(Long id) {
		return roleRepository.findOne(id);
	}
	
	public void delete(Long id) {
		roleRepository.delete(id);
	}
	
	public void save(Role role) {
		roleRepository.save(role);
	}
	
	public void update(Role role){
		roleRepository.saveAndFlush(role);
	}
	
	public Page<Role> getPage(int pageNo,int pageSize) {
		PageRequest pageable = new PageRequest(pageNo, pageSize);
		
		return roleRepository.findAll(pageable);
	}

}
