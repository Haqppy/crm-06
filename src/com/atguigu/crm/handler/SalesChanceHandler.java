package com.atguigu.crm.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import com.atguigu.crm.entity.Contact;
import com.atguigu.crm.entity.Customer;
import com.atguigu.crm.entity.SalesChance;
import com.atguigu.crm.entity.User;
import com.atguigu.crm.orm.Page;
import com.atguigu.crm.service.ContactsService;
import com.atguigu.crm.service.CustomersService;
import com.atguigu.crm.service.SalesChanceService;
import com.atguigu.crm.service.UserService;

@RequestMapping("/chance")
@Controller
public class SalesChanceHandler {

	@Autowired
	private SalesChanceService salesChanceService;
	@Autowired
	private UserService userService;
	@Autowired
	private ContactsService contactsService;
	@Autowired
	private CustomersService customersService;
	
	/**
	 * 终止开发
	 * /chance/stop/{chanceId}
	 */
	@RequestMapping(value="/stop/{chanceId}",method=RequestMethod.GET)
	public String stop(@PathVariable(value="chanceId")Long chanceId) {
		
		//修改状态
		SalesChance salesChance = new SalesChance();
		salesChance.setId(chanceId);
		salesChance.setStatus(4);
		salesChanceService.updateStatus(salesChance);
		//salesChanceService.update(salesChance);
		
		return "redirect:/plan/list";
	}
	
	//chance/finish/{chanceId} PUT 请求
	@RequestMapping(value="/finish/{chanceId}",method=RequestMethod.GET)
	public String finish(@PathVariable("chanceId")Long chanceId) {
		//stauts 字段的值修改为 3
		SalesChance salesChance = salesChanceService.get(chanceId);
		/**
		 *  向 customers 和 contacts 数据表中各插入一条记录。
		 *  Cusomers 数据表中插入 3 个字段：name，no（随机字符串） 和 state（正常）.
		 *  向 contacts 数据表也插入 3 个字段：name、tel、customer_id.
		 *  注意：在插入 contacts 记录时，
		 *       需要用到插入到 customers 数据表中的 id，使用 selectKey 来获取 customers 中的 id
		 */
		salesChance.setStatus(3);
		salesChanceService.updateStatus(salesChance);
		
		String custName = salesChance.getCustName();
		String custNo = UUID.randomUUID().toString().replaceAll("-", "");
		Customer customer = new Customer();
		customer.setName(custName);
		customer.setNo(custNo);
		customer.setState("正常");
		customersService.save(customer);
		
		String contactName = salesChance.getContact();
		String contactTel = salesChance.getContactTel();
		Contact contact = new Contact();
		contact.setCustomer(customer);
		contact.setName(contactName);
		contact.setTel(contactTel);
		
		contactsService.save(contact);
		
		return "redirect:/plan/list";
		
	}
	
	@ModelAttribute
	public void getSalesChance(@RequestParam(value="id",required=false) Long id, 
			Map<String, Object> map){
		if(id != null){
			//@ModelAttribute 的value 值  为 类名首字母小写
			map.put("salesChance", salesChanceService.get(id));
		}
	}
	
	@RequestMapping(value="/dispatch/{id}",method=RequestMethod.PUT)
	public String dispatch(SalesChance chance,RedirectAttributes attributes) {
		
		salesChanceService.update(chance);
		attributes.addFlashAttribute("message", "指派成功!");
		return "redirect:/chance/list";
	}
	
	@RequestMapping(value="/dispatch/{id}",method=RequestMethod.GET)
	public String dispatch(@PathVariable(value="id") Long id,Map<String, Object> map) {
		SalesChance salesChance = salesChanceService.get(id);
		map.put("chance", salesChance);
		//把所有用户放进去
		map.put("users", userService.getAll());
		return "chance/dispatch";
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public String update(SalesChance salesChance) {
		salesChanceService.update(salesChance);
		return "redirect:/chance/list";
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public String edit(@PathVariable(value="id") Long id,Map<String, Object> map) {
		//查询  把 查询 到的saleChance 放入进map中
		SalesChance chance = salesChanceService.get(id);
		map.put("chance", chance);
		
		return "chance/input";
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public String delete(@PathVariable("id")Long id,RedirectAttributes attributes) {
		salesChanceService.delete(id);
		attributes.addFlashAttribute("message", "删除成功!");
		return "redirect:/chance/list";
	}
	@RequestMapping(value="/",method=RequestMethod.POST)
	public String save(SalesChance salesChance,HttpSession session,RedirectAttributes attributes) {
		//SaleChance中 private User createBy;需要设置
		User user = (User) session.getAttribute("user");
		salesChance.setCreateBy(user);
		
		salesChanceService.save(salesChance);
		attributes.addFlashAttribute("message", "添加成功!");
		
		return "redirect:/chance/list";
	}
	
	@RequestMapping(value="/",method=RequestMethod.GET)
	public String input(Map<String, Object> map){
		map.put("chance", new SalesChance());
		return "chance/input";
	}
	
	/**
	 * 带查询条件的分页
	 * 
	 */
	@RequestMapping(value="/list")
	public String list2(@RequestParam(value="pageNo",required=false,defaultValue="1") String pageNoStr,
			@RequestParam(value="pageSize",required=false,defaultValue="4") String pageSizeStr, 
			HttpServletRequest request, Map<String, Object> map){
		//1. 获取 search_ 开头的请求参数
		Map<String, Object> params = WebUtils.getParametersStartingWith(request, "search_");
		
		Page<SalesChance> page = new Page<>();
		page.setPageNo(pageNoStr);
		
		int pageSize = 4;
		try {
			pageSize = Integer.parseInt(pageSizeStr);
			page.setPageSize(pageSize);
		} catch (NumberFormatException e) {}
		
		//2. 调用 getPage 方法时, 把带查询条件的请求参数传入到 service 方法中. 
		List<Integer> statuses = new ArrayList<Integer>();
		statuses.add(1);
		page = salesChanceService.getPage(page, params,statuses);
		map.put("page", page);
		
		//3. 把 params 在序列化为一个查询字符串
		String queryString = encodeParameterStringWithPrefix(params, "search_");
		//String queryString = parseParamsMapToQueryString(params);
		//4. 把这个字符串在传回到页面上.
		map.put("queryString", queryString);
		
		return "chance/list";
	}
	

	public static String encodeParameterStringWithPrefix(Map<String, Object> params, String prefix) {
		if ((params == null) || (params.size() == 0)) {
			return "";
		}

		if (prefix == null) {
			prefix = "";
		}

		StringBuilder queryStringBuilder = new StringBuilder();
		Iterator<Entry<String, Object>> it = params.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			queryStringBuilder.append(prefix).append(entry.getKey()).append('=').append(entry.getValue());
			if (it.hasNext()) {
				queryStringBuilder.append('&');
			}
		}
		return queryStringBuilder.toString();
	}
	
	/*@RequestMapping("/list")
	public String list(@RequestParam(value="pageNo",required=false,defaultValue="1")String pageNoStr,
			@RequestParam(value="pageSize",required=false,defaultValue="4") String pageSizeStr,
			Map<String, Object> map) {
		
		int pageNo = 1;
		int pageSize = 4;
		
		try {
			pageNo = Integer.parseInt(pageNoStr);
		} catch (NumberFormatException e) {}
		
		try {
			pageSize = Integer.parseInt(pageSizeStr);
		} catch (NumberFormatException e) {}
		
		Page<SalesChance> page = salesChanceService.getPage(pageNo, pageSize);
		
		//将page 放置进map 中
		map.put("page", page);
		
		return "chance/list";
	}*/
}
