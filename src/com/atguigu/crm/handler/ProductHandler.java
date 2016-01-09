package com.atguigu.crm.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.authenticator.SavedRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.atguigu.crm.entity.Product;
import com.atguigu.crm.service.jpa.ProductService;
import com.atguigu.crm.utils.Servlets;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.xml.internal.bind.v2.util.EditDistance;

@RequestMapping("/product")
@Controller
public class ProductHandler {

	@Autowired
	private ProductService productService;
	
	@RequestMapping(value="/create",method=RequestMethod.PUT)
	public String update(Product product) {
		productService.update(product);
		return "redirect:/product/list";
	}
	
	@RequestMapping(value="/create/{id}",method=RequestMethod.GET)
	public String edit(@PathVariable("id")Long id,Map<String, Object> map) {
		Product product = productService.get(id);
		map.put("product", product);
		return "product/create";
	}
	
	@RequestMapping(value="/create",method=RequestMethod.POST)
	public String save(Product product) {
		productService.save(product);
		return "redirect:/product/list";
	}
	
	@RequestMapping(value="/create",method=RequestMethod.GET)
	public String create(Map<String, Object> map) {
		
		map.put("product", new Product());
		return "product/create";
	}
	
	@ResponseBody
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public String delete(@PathVariable("id")Long id) {
		productService.delete(id);
		return "1";
	}
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(@RequestParam(value="page",required=false,defaultValue="1")Integer pageNo,
			Map<String, Object> map,HttpServletRequest request) {
		
		Map<String, Object> paramsMap = WebUtils.getParametersStartingWith(request, "search_");
		
		Page<Product> page = productService.getPage(paramsMap, pageNo-1, 4);
		map.put("page", page);
		
		String queryString = Servlets.encodeParameterStringWithPrefix(paramsMap, "search_");
		map.put("searchParams", queryString);
		
		return "product/list";
	}
	
}
