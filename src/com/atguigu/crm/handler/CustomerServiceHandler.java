package com.atguigu.crm.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import com.atguigu.crm.entity.Customer;
import com.atguigu.crm.entity.CustomerService;
import com.atguigu.crm.entity.Dict;
import com.atguigu.crm.entity.User;
import com.atguigu.crm.orm.Page;
import com.atguigu.crm.service.CustomerSerService;
import com.atguigu.crm.service.CustomersService;
import com.atguigu.crm.service.DictsService;
import com.atguigu.crm.service.UserService;
import com.atguigu.crm.utils.Servlets;

@RequestMapping("/service")
@Controller
public class CustomerServiceHandler {

	@Autowired
	private DictsService dictsService;
	@Autowired
	private CustomersService customersService;
	@Autowired
	private CustomerSerService customerSerService;
	@Autowired
	private UserService userService;
	
	//${ctp}/service/delete/"+id
	@ResponseBody
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public String deleteService(@PathVariable("id")Long id) {
		customerSerService.delete(id);
		return "1";
	}
	
	//var url = "${ctp}/service/allot";
	//var args = {"id":id,"allotId":val,"_method":"PUT","time":new Date()};
	/**
	 * 分配
	 * @param id
	 * @param allotId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/allot",method=RequestMethod.PUT)
	public String allot(@RequestParam("id")Long id,
			@RequestParam("allotId")Long allotId) {
		CustomerService customerService = new CustomerService();
		customerService.setId(id);
		
		User user = new User();
		user.setId(allotId);
		
		customerService.setAllotTo(user);
		customerService.setServiceState("已分配");
		customerSerService.updateAllot(customerService);
		
		return "1";
	}
	
	//${ctp}/service/archive/${cs.id }
	@RequestMapping(value="/archive/{id}",method=RequestMethod.GET)
	public String showArchive(@PathVariable("id")Long id,Map<String, Object> map) {
		
		CustomerService customerService = customerSerService.get(id);
		map.put("customerService", customerService);
		
		return "service/archive/show";
	}
	
	///service/archive/list
	//服务归档 全部否有
	@RequestMapping(value = "/archive/list", method = RequestMethod.GET)
	public String archiveList(
			@RequestParam(value = "pageNo", required = false, defaultValue = "1") String pageNoStr,
			@RequestParam(value = "pageSize", required = false, defaultValue = "4") String pageSizeStr,
			Map<String, Object> map, HttpServletRequest request,
			HttpSession session) {
		// 获取当前登录用户
		User user = (User) session.getAttribute("user");

		Page<CustomerService> page = new Page<CustomerService>();
		page.setPageNo(pageNoStr);

		int pageSize = 4;
		try {
			pageSize = Integer.parseInt(pageSizeStr);
		} catch (NumberFormatException e) {
		}

		page.setPageSize(pageSize);

		Map<String, Object> parametersMap = WebUtils.getParametersStartingWith(
				request, "search_");
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		
		conditionMap.put("allotToId", "allotToId");
		conditionMap.put("serviceDeal", "serviceDeal");
		conditionMap.put("dealResult", "dealResult");
		
		page = customerSerService.getPage(user, page, parametersMap,
				conditionMap);

		// 将查询条件序列化成字符串返回
		String queryString = Servlets.encodeParameterStringWithPrefix(
				parametersMap, "search_");

		map.put("allotTos", userService.getAll());

		map.put("page", page);
		map.put("queryString", queryString);
		// 所有的
		return "service/archive/list";
	}
	
	//crm_/service/feedback
	@RequestMapping(value="/feedback",method=RequestMethod.PUT)
	public String updateDealResult(@RequestParam("id") Long id,
			CustomerService customerService, RedirectAttributes attributes) {
		
		customerSerService.updateDealResult(customerService);
		attributes.addFlashAttribute("message", "已处理请求!");
		
		return "redirect:/service/archive/list";
	}
	
	//service/feedback/${cs.id }
	@RequestMapping(value="/feedback/{id}",method=RequestMethod.GET)
	public String feedbackEdit(@PathVariable("id")Long id,Map<String, Object> map) {
		
		CustomerService customerService = customerSerService.get(id);
		List<Dict> satisfies = dictsService.getItems("满意度");
		
		map.put("customerService", customerService);
		map.put("satisfies", satisfies);
		
		return "service/feedback/edit";
	}
	
	// feedback/list
	// 服务反馈 没有处理结果 有 allotToId 和 服务处理
	@RequestMapping(value = "/feedback/list", method = RequestMethod.GET)
	public String feedbackList(
			@RequestParam(value = "pageNo", required = false, defaultValue = "1") String pageNoStr,
			@RequestParam(value = "pageSize", required = false, defaultValue = "4") String pageSizeStr,
			Map<String, Object> map, HttpServletRequest request,
			HttpSession session) {
		// 获取当前登录用户
		User user = (User) session.getAttribute("user");

		Page<CustomerService> page = new Page<CustomerService>();
		page.setPageNo(pageNoStr);

		int pageSize = 4;
		try {
			pageSize = Integer.parseInt(pageSizeStr);
		} catch (NumberFormatException e) {
		}

		page.setPageSize(pageSize);

		Map<String, Object> parametersMap = WebUtils.getParametersStartingWith(
				request, "search_");
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("allotToId", "allotToId");
		conditionMap.put("serviceDeal", "serviceDeal");
		
		page = customerSerService.getPage(user, page, parametersMap,
				conditionMap);

		// 将查询条件序列化成字符串返回
		String queryString = Servlets.encodeParameterStringWithPrefix(
				parametersMap, "search_");

		map.put("page", page);
		map.put("queryString", queryString);

		return "service/feedback/list";
	}

	@RequestMapping(value = "/deal", method = RequestMethod.PUT)
	public String update(@RequestParam("id") Long id,
			CustomerService customerService, RedirectAttributes attributes) {
		
		customerService.setServiceState("已处理");
		customerSerService.updateServiceDeal(customerService);
		attributes.addFlashAttribute("message", "添加服务处理!");

		return "redirect:/service/deal/list";
	}

	// ${ctp}/service/deal/${cs.id}
	@RequestMapping(value = "/deal/{id}", method = RequestMethod.GET)
	public String deal(@PathVariable("id") Long id, Map<String, Object> map) {

		CustomerService customerService = customerSerService.get(id);
		map.put("customerService", customerService);

		return "service/deal/edit";
	}

	// 服务处理 没有服务处理 但有allotoId
	@RequestMapping(value = "/deal/list", method = RequestMethod.GET)
	public String dealList(
			@RequestParam(value = "pageNo", required = false, defaultValue = "1") String pageNoStr,
			@RequestParam(value = "pageSize", required = false, defaultValue = "4") String pageSizeStr,
			Map<String, Object> map, HttpServletRequest request,
			HttpSession session) {

		User user = (User) session.getAttribute("user");

		Page<CustomerService> page = new Page<CustomerService>();
		page.setPageNo(pageNoStr);

		int pageSize = 4;
		try {
			pageSize = Integer.parseInt(pageSizeStr);
		} catch (NumberFormatException e) {
		}

		page.setPageSize(pageSize);

		Map<String, Object> parametersMap = WebUtils.getParametersStartingWith(
				request, "search_");

		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("allotToId", "allotToId");

		page = customerSerService.getPage(user, page, parametersMap,
				conditionMap);

		// 将查询条件序列化成字符串返回
		String queryString = Servlets.encodeParameterStringWithPrefix(
				parametersMap, "search_");

		map.put("page", page);
		map.put("queryString", queryString);

		return "service/deal/list";

	}

	// 服务分配 没有alltoId
	@RequestMapping(value = "/allot/list", method = RequestMethod.GET)
	public String allotList(
			@RequestParam(value = "pageNo", required = false, defaultValue = "1") String pageNoStr,
			@RequestParam(value = "pageSize", required = false, defaultValue = "4") String pageSizeStr,
			Map<String, Object> map, HttpServletRequest request,
			HttpSession session) {
		// 获取当前登录用户
		User user = (User) session.getAttribute("user");

		Page<CustomerService> page = new Page<CustomerService>();
		page.setPageNo(pageNoStr);

		int pageSize = 4;
		try {
			pageSize = Integer.parseInt(pageSizeStr);
		} catch (NumberFormatException e) {
		}

		page.setPageSize(pageSize);

		Map<String, Object> parametersMap = WebUtils.getParametersStartingWith(
				request, "search_");
		Map<String, Object> conditionMap = new HashMap<String, Object>();

		page = customerSerService.getPage(user, page, parametersMap,
				conditionMap);

		// 将查询条件序列化成字符串返回
		String queryString = Servlets.encodeParameterStringWithPrefix(
				parametersMap, "search_");

		map.put("allotTos", userService.getAll());

		map.put("page", page);
		map.put("queryString", queryString);
		// 所有的
		return "service/allot/list";
	}

	/**
	 * 服务创建
	 * @param customerService
	 * @param session
	 * @param attributes
	 * @return
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String save(CustomerService customerService, HttpSession session,
			RedirectAttributes attributes) {

		User user = (User) session.getAttribute("user");
		customerService.setCreatedby(user);
		
		customerSerService.save(customerService);

		return "redirect:/service/allot/list";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Map<String, Object> map) {
		// 服务类型
		List<Dict> services = dictsService.getItems("服务类型");
		// 全有的客户
		List<Customer> customers = customersService.getAll();

		map.put("customersService", new CustomerService());
		map.put("services", services);
		map.put("customers", customers);

		return "service/create";
	}
}
