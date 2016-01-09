package com.atguigu.crm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.crm.dao.ContactsMapper;
import com.atguigu.crm.entity.Contact;
import com.atguigu.crm.orm.Page;
import com.sun.org.apache.regexp.internal.recompile;
import com.sun.swing.internal.plaf.metal.resources.metal;

@Service
public class ContactsService {

	@Autowired
	private ContactsMapper contactsMapper;
	
	@Transactional
	public void delete(Long id){
		contactsMapper.delete(id);
	}
	
	@Transactional(readOnly=true)
	public List<Contact> getByCustId(Long customerId) {
	
		return contactsMapper.getByCustId(customerId);
	}
	
	@Transactional
	public void update(Contact contact){
		contactsMapper.update(contact);
	}
	
	@Transactional(readOnly=true)
	public Contact get(Long id){
		return contactsMapper.get(id);
	}
	
	@Transactional
	public void saveWithCust(Contact contact) {
		contactsMapper.saveWithCust(contact);
	}
	
	@Transactional(readOnly=true)
	public Page<Contact> getPage(Long id,Page<Contact> page){
		
		long totalElements = contactsMapper.getTotalElements(id);
		page.setTotalElements((int)totalElements);
		
		int fromIndex = (page.getPageNo()-1)*page.getPageSize()+1;
		int endIndex = fromIndex + page.getPageSize();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("fromIndex", fromIndex);
		paramMap.put("endIndex", endIndex);
		
		paramMap.put("customerId", id);
		
		List<Contact> content = contactsMapper.getContentByCustId(paramMap);
		page.setContent(content);
		
		return page;
	}
	
	@Transactional
	public void save(Contact contact){
		contactsMapper.save(contact);
	}
	
	@Transactional(readOnly=true)
	public List<Contact> getContactsByCustId(Long customerId){
		return contactsMapper.getContactsByCustId(customerId);
	}
	
}
