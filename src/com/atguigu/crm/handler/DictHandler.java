package com.atguigu.crm.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.atguigu.crm.entity.Dict;
import com.atguigu.crm.service.jpa.DictService;
import com.atguigu.crm.utils.Servlets;

@RequestMapping("/dict")
@Controller
public class DictHandler {

	@Autowired
	private DictService dictService;
	
	@RequestMapping(value="/create",method=RequestMethod.POST)
	public String save(Dict dict) {
		dictService.save(dict);
		return "redirect:/dict/list";
	}

	@RequestMapping(value="/create",method=RequestMethod.PUT)
	public String update(Dict dict) {
		dictService.update(dict);
		return "redirect:/dict/list";
	}
	
	@ResponseBody
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public String delete(@PathVariable("id")Long id) {
		dictService.delete(id);
		return "1";
	}
	
	
	@RequestMapping(value="/create/{id}",method=RequestMethod.GET)
	public String edit(@PathVariable("id")Long id,Map<String,Object> map) {
		Dict dict = dictService.get(id);
		map.put("dict", dict);
		return "dict/create";
	}
	
	//${ctp}/dict/create
	@RequestMapping(value="/create",method=RequestMethod.GET)
	public String create(Map<String, Object> map) {
		map.put("dict", new Dict());
		return "dict/create";
	}
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(@RequestParam(value="page",required=false,defaultValue="1")int pageNo,
			Map<String, Object> map,HttpServletRequest request) {
		
		Map<String, Object> paramsMap = WebUtils.getParametersStartingWith(request, "search_");
		
		Page<Dict> page = dictService.getPage(paramsMap, pageNo-1, 4);
		
		map.put("page", page);
		
		String queryString = Servlets.encodeParameterStringWithPrefix(paramsMap, "search_");
		map.put("searchParams", queryString);
		
		return "dict/list";
	}
	
	
}
