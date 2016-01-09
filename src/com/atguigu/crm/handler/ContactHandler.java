package com.atguigu.crm.handler;

import java.util.List;
import java.util.Map;

import org.apache.catalina.authenticator.SavedRequest;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sun.font.CreatedFontTracker;

import com.atguigu.crm.entity.Contact;
import com.atguigu.crm.entity.Customer;
import com.atguigu.crm.orm.Page;
import com.atguigu.crm.service.ContactsService;
import com.atguigu.crm.service.CustomersService;
import com.sun.xml.internal.bind.v2.util.EditDistance;

@RequestMapping("/contact")
@Controller
public class ContactHandler {

	@Autowired
	private CustomersService customersService;
	@Autowired
	private ContactsService contactsService;
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.DELETE)
	public String delete(@RequestParam("id")Long id,
			@RequestParam("customerId")Long customerId){
		List<Contact> contacts = contactsService.getByCustId(customerId);
		if (contacts.size() <= 1) {
			return "2";
		}else{
			contactsService.delete(id);
			return "1";
		}
	}
	
	@ModelAttribute
	public void getModel(@RequestParam(value="id",required=false)Long id,Map<String, Object> map) {
		if (id != null) {
			Contact contact = contactsService.get(id);
			map.put("contact", contact);
		}
	}
	
	@RequestMapping(value="/create",method=RequestMethod.PUT)
	public String update(@RequestParam("id")Long id,
			@RequestParam("customerId")Long customerId,
			Contact contact,RedirectAttributes attributes){
		
		Customer customer = customersService.getCustomer(customerId);
		customer.setId(customerId);
		contact.setCustomer(customer);
		
		contactsService.update(contact);
		attributes.addFlashAttribute("message", "修改成功!");
		
		return "redirect:/contact/list?id="+customerId;
		
	}
	
//window.location.href='${ctp}/contact/create?id=${contact.id }&customerId=${customer.id}
	@RequestMapping("/create")
	public String edit(@RequestParam("id")Long id,
			@RequestParam("customerId")Long customerId,Map<String, Object> map) {
		
		Contact contact = contactsService.get(id);
		map.put("customerId", customerId);
		map.put("contact", contact);
		
		return "contact/input";
		
	}
	
	@RequestMapping(value="/create",method=RequestMethod.POST)
	public String save(@RequestParam("customerId")Long customerId,Contact contact,RedirectAttributes attributes) {
		Customer customer = customersService.getCustomer(customerId);
		customer.setId(customerId);
		contact.setCustomer(customer);
		
		contactsService.saveWithCust(contact);
		attributes.addFlashAttribute("message", "添加成功!");
		
		return "redirect:/contact/list?id="+customerId;
		
	}
	
	@RequestMapping(value="/create/{id}",method=RequestMethod.GET)
	public String create(@PathVariable(value="id")Long id,Map<String, Object> map) {
		
		map.put("customerId", id);
		Contact contact = new Contact();
		map.put("contact", contact);
		return "contact/input";
	}
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(
			@RequestParam(value="id")Long id,
			@RequestParam(value="pageNo",required=false,defaultValue="1")String pageNoStr,
			@RequestParam(value="pageSize",required=false,defaultValue="4") String pageSizeStr,
			Map<String, Object> map){
		
		Page<Contact> page = new Page<Contact>();
		page.setPageNo(pageNoStr);
		
		int pageSize = 4;
		try {
			pageSize = Integer.parseInt(pageSizeStr);
		} catch (NumberFormatException e) {}
		
		page.setPageSize(pageSize);
		
		page = contactsService.getPage(id, page);
		
		Customer customer = customersService.getCustomer(id);
		customer.setId(id);
		
		map.put("page", page);
		map.put("customer", customer);
		
		return "contact/list";
	}
	
}
