package com.atguigu.crm.handler;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Delete;
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
import com.atguigu.crm.entity.Storage;
import com.atguigu.crm.service.jpa.ProductService;
import com.atguigu.crm.service.jpa.StorageService;
import com.atguigu.crm.utils.Servlets;
import com.sun.org.apache.bcel.internal.generic.NEW;

@RequestMapping("/storage")
@Controller
public class StorageHandler {

	@Autowired
	private StorageService storageService;
	@Autowired
	private ProductService productService;
	
	@ResponseBody
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public String Delete(@PathVariable("id")Long id) {
		storageService.delete(id);
		return "1";
	}
	
	
	@RequestMapping(value="/create",method=RequestMethod.PUT)
	public String update(Storage storage,@RequestParam(value="incrementCount")Integer incrementCount) {
		storage.setStockCount(storage.getStockCount()+incrementCount);
		storageService.update(storage);
		return "redirect:/storage/list";
	}
	
	//window.location.href='${ctp}/storage/create/${item.id }
	@RequestMapping(value="/create/{id}",method=RequestMethod.GET)
	public String edit(@PathVariable(value="id")Long id,Map<String, Object> map) {
		Storage storage = storageService.getStorageById(id);
		map.put("storage", storage);
		
		return "storage/edit";
	}
	
	//${ctp}/storage/create post
	@RequestMapping(value="/create",method=RequestMethod.POST)
	public String save(Storage storage) {
		storageService.save(storage);
		return "redirect:/storage/list";
	}
	
	@RequestMapping(value="/create",method=RequestMethod.GET)
	public String create(Map<String, Object> map) {
		List<Product> products = productService.getAll();
		map.put("products", products);
		map.put("storage", new Storage());
		
		return "storage/create";
	}
	
	@RequestMapping("/list")
	public String list(@RequestParam(value="page",required=false,defaultValue="1") int pageNo,
			HttpServletRequest request,Map<String, Object> map) {
		
		Map<String, Object> parametersMap = WebUtils.getParametersStartingWith(request, "search_");
		Page<Storage> page = storageService.getPage(parametersMap, pageNo-1, 4);
		
		map.put("page", page);
		String queryString = Servlets.encodeParameterStringWithPrefix(parametersMap, "search_");
		map.put("searchParams", queryString);
		
		return "storage/list";
	}
	
}
