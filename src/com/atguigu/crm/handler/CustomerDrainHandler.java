package com.atguigu.crm.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import com.atguigu.crm.entity.CustomerDrain;
import com.atguigu.crm.orm.Page;
import com.atguigu.crm.service.CustomerDrainService;
import com.atguigu.crm.utils.Servlets;

@RequestMapping("/drain")
@Controller
public class CustomerDrainHandler {

	@Autowired
	private CustomerDrainService customerDrainService;
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(@RequestParam(value="pageNo",required=false,defaultValue="1")String pageNoStr,
			@RequestParam(value="pageSize",required=false,defaultValue="4")String pageSizeStr,
			HttpServletRequest request,Map<String, Object> map) {
		Map<String, Object> parametersMap = WebUtils.getParametersStartingWith(request, "search_");
		
		Page<CustomerDrain> page = new Page<CustomerDrain>();
		page.setPageNo(pageNoStr);
		
		int pageSize = 4;
		try {
			pageSize = Integer.parseInt(pageSizeStr);
		} catch (NumberFormatException e) {}
		
		page.setPageSize(pageSize);
		page = customerDrainService.getPage(page, parametersMap);
		map.put("page", page);
		
		String queryString = Servlets.encodeParameterStringWithPrefix(parametersMap, "search_");
		map.put("queryString", queryString);
		
		return "customer/drain/list";
	}
	
}
