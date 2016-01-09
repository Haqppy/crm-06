package com.atguigu.crm.handler;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import com.atguigu.crm.entity.Contact;
import com.atguigu.crm.entity.Customer;
import com.atguigu.crm.entity.Dict;
import com.atguigu.crm.orm.Page;
import com.atguigu.crm.service.ContactsService;
import com.atguigu.crm.service.CustomersService;
import com.atguigu.crm.service.DictsService;
import com.atguigu.crm.utils.Servlets;
import com.sun.accessibility.internal.resources.accessibility;
import com.sun.xml.internal.bind.v2.util.EditDistance;

@RequestMapping("/customer")
@Controller
public class CustomerHandler {

	@Autowired
	private CustomersService customersService;
	@Autowired
	private DictsService dictsService;
	@Autowired
	private ContactsService contactsService;
	
	@ModelAttribute
	public void getModel(@RequestParam(value="id",required=false)Long id,
			Map<String, Object> map){
		if (id != null) {
			Customer customer = customersService.getCustomer(id);
			map.put("customer", customer);
		}
	}
	
	@RequestMapping(value="/update",method=RequestMethod.PUT)
	public String update(@RequestParam("id")Long id,
			   Customer customer,RedirectAttributes attributes) {
		
		customersService.update(customer);
		attributes.addFlashAttribute("message", "修改成功!");
		return "redirect:/customer/list";
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public String edit(@PathVariable("id")Long id,Map<String, Object> map) {
		Customer customer = customersService.getCustomer(id);
		customer.setId(id);
		List<Contact> contacts = contactsService.getContactsByCustId(id);
		customer.setContacts(new HashSet<Contact>(contacts));
		
		List<Dict> regions = dictsService.getItems("地区");
		List<Dict> levels = dictsService.getItems("客户等级");
		List<Dict> satifies = dictsService.getItems("满意度");
		List<Dict> credits = dictsService.getItems("信用度");
		
		map.put("customer", customer);
		map.put("regions", regions);
		map.put("levels", levels);
		map.put("satifies", satifies);
		map.put("credits", credits);
		
		return "customer/edit";
	}
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(@RequestParam(value="pageNo",required=false,defaultValue="1")String pageNoStr,
			@RequestParam(value="pageSize",required=false,defaultValue="4") String pageSizeStr,
			Map<String, Object> map,
			HttpServletRequest request) {
		
		Map<String, Object> parametersMap = WebUtils.getParametersStartingWith(request, "search_");
		//LIKES_name
		
		Page<Customer> page = new Page<Customer>();
		page.setPageNo(pageNoStr);
		
		int pageSize = 4;
		try {
			pageSize = Integer.parseInt(pageSizeStr);
		} catch (NumberFormatException e) {}
		
		page.setPageSize(pageSize);
		
		page = customersService.getPage(page,parametersMap);
		
		List<Dict> regions = dictsService.getItems("地区");
		List<Dict> levels = dictsService.getItems("客户等级");
		
		map.put("page", page);
		map.put("regions", regions);
		map.put("levels", levels);
		
		//把条件序列化成字符串返回
		String queryString = Servlets.encodeParameterStringWithPrefix(parametersMap, "search_");
		map.put("queryString", queryString);
		
		return "customer/list";
		
	}
	
	
}
