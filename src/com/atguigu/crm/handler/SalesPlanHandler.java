package com.atguigu.crm.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.atguigu.crm.entity.SalesChance;
import com.atguigu.crm.entity.SalesPlan;
import com.atguigu.crm.orm.Page;
import com.atguigu.crm.service.SalesChanceService;
import com.atguigu.crm.service.SalesPlanService;

@RequestMapping("/plan")
@Controller
public class SalesPlanHandler {

	@Autowired
	private SalesPlanService salesPlanService;
	@Autowired
	private SalesChanceService salesChanceService; 
	
	@ResponseBody
	@RequestMapping(value="/executeWithAjax/{id}",method=RequestMethod.PUT)
	public String executeWithAjax(@PathVariable("id")Long id,
			@RequestParam("result") String result) {
		SalesPlan salesPlan = new SalesPlan();
		salesPlan.setId(id);
		salesPlan.setResult(result);
		salesPlanService.execute(salesPlan);
		
		return "1";
	}
	
	@RequestMapping(value="execute/{id}",method=RequestMethod.GET)
	public String execute(@PathVariable("id")Long id,Map<String, Object> map) {
		
		SalesChance salesChance = salesChanceService.get(id);
		Set<SalesPlan> salesPlans = salesPlanService.getSalesPlans(id);
		salesChance.setSalesPlans(salesPlans);
		map.put("chance", salesChance);
		
		return "plan/execution";
	}
	
	/**
	 * 4.对于开发成功 或 失败的销售机会可以查看其详细信息. URL 为：plan/details?chanceId=xxx GET
	 */
	@RequestMapping(value="/details",method=RequestMethod.GET)
	public String details(@RequestParam("id") Long chanceId,Map<String, Object> map) {
		
		SalesChance salesChance = salesChanceService.get(chanceId);
		Set<SalesPlan> salesPlans = salesPlanService.getSalesPlans(chanceId);
		salesChance.setSalesPlans(salesPlans);
		map.put("chance", salesChance);
		
		return "plan/details";
	}
		
	@ResponseBody
	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT)
	public String update(@PathVariable("id")Long id,
						 @RequestParam("todo")String todo) {
		
		SalesPlan salesPlan = new SalesPlan();
		salesPlan.setTodo(todo);
		salesPlan.setId(id);
		
		salesPlanService.update(salesPlan);
		
		return "1";
	}
	
	@ResponseBody
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public String delete(@PathVariable(value="id")Long id) {
		System.out.println(id);
		
		salesPlanService.delete(id);
		return "1";
	}
	
	/*@ResponseBody
	@RequestMapping(value="/make",method=RequestMethod.POST)
	public String make(@RequestParam("date") String date, 
			@RequestParam(value="todo")String todo,
			@RequestParam(value="chance.id")String chanceId,
			HttpServletResponse response){
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SalesPlan salesPlan = new SalesPlan();
		try {
			Date d = dateFormat.parse(date);
			
			salesPlan.setTodo(todo);
			salesPlan.setDate(d);
			SalesChance salesChance = salesChanceService.get(Long.parseLong(chanceId));
			salesPlan.setChance(salesChance);
			salesPlanService.save(salesPlan);
		
			Long id = salesPlan.getId();
			return id+"";
		} catch (Exception e) {}
		
		return null;
	}*/
	
	@ResponseBody
	@RequestMapping(value="/make",method=RequestMethod.POST)
	public String make(SalesPlan salesPlan){
		
		salesPlanService.save(salesPlan);
		
		return salesPlan.getId()+"";
	}
	
	@RequestMapping(value="/make/{chanceId}",method=RequestMethod.GET)
	public String make(@PathVariable("chanceId")Long chanceId,Map<String, Object> map) {

		SalesChance salesChance = salesChanceService.get(chanceId);
		
		Set<SalesPlan> salesPlans = salesPlanService.getSalesPlans(salesChance.getId());
	
		salesChance.setSalesPlans(salesPlans);
		
		map.put("chance", salesChance);
		return "plan/make";
		
	}
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(@RequestParam(value="pageNo",required=false,defaultValue="1")String pageNoStr,
			@RequestParam(value="pageSize",required=false,defaultValue="4")String pageSizeStr,
			Map<String, Object> map,HttpServletRequest request) {
		
		Page<SalesChance> page = new Page<SalesChance>();
		page.setPageNo(pageNoStr);
		//search_LIKE_custName
		Map<String, Object> paramsMap = WebUtils.getParametersStartingWith(request,"search_");
		int pageSize = 4;
		try {
			pageSize = Integer.parseInt(pageSizeStr);
			page.setPageSize(pageSize);
		} catch (NumberFormatException e) {}
		
		List<Integer> statuses = new ArrayList<Integer>();
		statuses.add(2);
		statuses.add(3);
		statuses.add(4);
		
		page = salesChanceService.getPage(page, paramsMap, statuses);
		
		String queryString = parseParamMap2QueryString(paramsMap);
		map.put("page", page);
		
		return "plan/list";
	}

	private String parseParamMap2QueryString(Map<String, Object> paramsMap) {
		StringBuilder stringBuilder = new StringBuilder();
		
		for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			
			stringBuilder.append("search_")
					     .append(key)
					     .append("=")
					     .append(value)
					     .append("&");
		}
		if (stringBuilder.length() > 0) {
			stringBuilder.replace(stringBuilder.length()-1, stringBuilder.length(), "");
		}
		return stringBuilder.toString();
	}
	
}
