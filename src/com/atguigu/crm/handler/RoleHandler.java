package com.atguigu.crm.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.crm.entity.Authority;
import com.atguigu.crm.entity.Role;
import com.atguigu.crm.service.jpa.AuthorityService;
import com.atguigu.crm.service.jpa.RoleService;

@RequestMapping("/role")
@Controller
public class RoleHandler {

	@Autowired
	private RoleService roleService;
	@Autowired
	private AuthorityService authorityService;
	
	@ModelAttribute
	public void getModel(@RequestParam(value="id",required=false)Long id,Map<String, Object> map) {
		if (id != null) {
			map.put("role", roleService.get(id));
		}
	}
	
	@RequestMapping(value="/saveAssign",method=RequestMethod.PUT)
	public String saveAssign(Role role) {
		
		//role.setAuthorities(authorities);
		roleService.update(role);
		return "redirect:/role/list";
	}
	
	//${ctp}/role/assign/${item.id}
	@RequestMapping(value="/assign/{id}",method=RequestMethod.GET)
	public String assign(@PathVariable("id")Long id,Map<String, Object> map) {
		Role role = roleService.get(id);
		
		List<Authority> subAuthorities = authorityService.getByIsNotNull("parentAuthority");
		List<Authority> parentAuthorities = authorityService.getByIsNull("parentAuthority");
		
		List<Authority> pAuthorities = new ArrayList<Authority>();
		
		for (Authority parent : parentAuthorities) {
			Long parentId = parent.getId();
			List<Authority> sub = new ArrayList<Authority>();
			
			for (int i = 0; i < subAuthorities.size(); i++) {
				if (subAuthorities.get(i).getParentAuthority().getId() == parentId) {
					sub.add(subAuthorities.get(i));
				}
			}
			parent.setSubAuthorities(sub);
			pAuthorities.add(parent);
		}
		
		map.put("pAuthorities", pAuthorities);
		map.put("role", role);
		
		return "role/assign";
	}
	
	//create
	@RequestMapping(value="/create",method=RequestMethod.POST)
	public String save(Role role) {
		roleService.save(role);
		return "redirect:/role/list";
	}
	
	// /role/delete/"+id
	@ResponseBody
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public String delete(@PathVariable("id")Long id){
		roleService.delete(id);
		return "1";
	}
	
	//onclick="window.location.href='${ctp}/role/input'
	@RequestMapping(value="/input",method=RequestMethod.GET)
	public String input(Map<String, Object> map) {
		map.put("role", new Role());
		return "role/input";
	}
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(@RequestParam(value="page",required=false,defaultValue="1")Integer pageNo,
			HttpServletRequest request,Map<String, Object> map) {
		
		Page<Role> page = roleService.getPage(pageNo-1, 4);
		map.put("page", page);
		
		return "role/list";
	}
}
