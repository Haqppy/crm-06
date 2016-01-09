package com.atguigu.crm.handler;

import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import com.atguigu.crm.entity.Customer;
import com.atguigu.crm.entity.CustomerDrain;
import com.atguigu.crm.service.CustomerDrainService;
import com.atguigu.crm.service.jpa.ReportService;
import com.atguigu.crm.utils.Servlets;

@RequestMapping("/report")
@Controller
public class ReportHandler {

	@Autowired
	private ReportService reportService;
	
	@Autowired
	private CustomerDrainService customerDrainService;

	///crm-04/report/drain
	
	@RequestMapping(value="/drain",method=RequestMethod.GET)
	public String list(@RequestParam(value="pageNo",required=false,defaultValue="1")String pageNoStr,
			@RequestParam(value="pageSize",required=false,defaultValue="4")String pageSizeStr,
			HttpServletRequest request,Map<String, Object> map) {
		Map<String, Object> parametersMap = WebUtils.getParametersStartingWith(request, "search_");
		
		com.atguigu.crm.orm.Page<CustomerDrain> page = new com.atguigu.crm.orm.Page<CustomerDrain>();
		page.setPageNo(pageNoStr);
		
		int pageSize = 4;
		try {
			pageSize = Integer.parseInt(pageSizeStr);
		} catch (NumberFormatException e) {}
		
		page.setPageSize(pageSize);
		page = customerDrainService.getPage(page, parametersMap,"drainDate");
		
		map.put("page", page);
		
		String queryString = Servlets.encodeParameterStringWithPrefix(parametersMap, "search_");
		map.put("queryString", queryString);
		
		return "customer/drain/list";
	}
	
	// /crm-04/report/service
	@RequestMapping(value="/service",method=RequestMethod.GET)
	public String service(@RequestParam(value="page",required=false,defaultValue="1")int pageNo,
			Map<String, Object> map,HttpServletRequest request) throws ParseException {
		
		Map<String, Object> paramsMap = WebUtils.getParametersStartingWith(request, "search_");
		Page<Object[]> page = reportService.getCustomerService(paramsMap, pageNo, 4);
		
		String queryString = Servlets.encodeParameterStringWithPrefix(paramsMap, "search_");
		
		map.put("page", page);
		map.put("searchParams", queryString);
		
		return "report/service/list";
	}
	
	// /crm-04/report/consist
	@RequestMapping(value="/consist",method=RequestMethod.GET)
	public String consist(@RequestParam(value="page",required=false,defaultValue="1")int pageNo,
			Map<String, Object> map,HttpServletRequest request) {
		
		Map<String, Object> paramsMap = WebUtils.getParametersStartingWith(request, "search_");	
		String type = (String) paramsMap.get("type");
		if (type == null) {
			type = "level";
		}
		Page<Object[]> page = reportService.getCustomerConsist(type, pageNo, 4);
		
		String queryString = Servlets.encodeParameterStringWithPrefix(paramsMap, "search_");
		map.put("page", page);
		map.put("searchParams", queryString);
		map.put("type", type);
		return "report/consist/list";
	}
	
	// /crm-04/report/pay
	@RequestMapping(value="/pay",method=RequestMethod.GET)
	public String pay(@RequestParam(value="page",required=false,defaultValue="1")Integer pageNo,
			Map<String, Object> map,HttpServletRequest request) throws ParseException {
		Map<String, Object> paramsMap = WebUtils.getParametersStartingWith(request, "search_");
		
		Page<Customer> page = reportService.getCustomerPage(pageNo, 4, paramsMap);
		
		String queryString = Servlets.encodeParameterStringWithPrefix(paramsMap, "search_");
		
		map.put("page", page);
		map.put("searchParams", queryString);
		
		return "report/pay/list";
	}
	
	
	
}
