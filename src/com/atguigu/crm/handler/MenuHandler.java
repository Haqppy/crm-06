package com.atguigu.crm.handler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.navigator.menu.MenuComponent;
import net.sf.navigator.menu.MenuRepository;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.atguigu.crm.entity.Authority;
import com.atguigu.crm.entity.User;
import com.atguigu.crm.service.UserService;

@Controller
public class MenuHandler {
	
	@Autowired
	private UserService userService ;
	//${ctp}/menu
	
	@RequestMapping(value="/menu",method=RequestMethod.GET)
	public String menu(Map<String, Object> map,HttpServletRequest request,HttpSession session) {
		
		MenuRepository repository = new MenuRepository();
		// Get the repository from the application scope - and copy the
		// DisplayerMappings from it.
		
		ServletContext servletContext = request.getSession().getServletContext();
		String contextPath = servletContext.getContextPath();
		
		MenuRepository defaultRepository = (MenuRepository)
		        servletContext.getAttribute(MenuRepository.MENU_REPOSITORY_KEY);
		repository.setDisplayers(defaultRepository.getDisplayers());

		MenuComponent topMenu = new MenuComponent();
		topMenu.setName("TOP-MENU");
		topMenu.setTitle("客户关系管理系统");
		repository.addMenu(topMenu);
		
		//获取当前用户所对应的权限
	    User shiroUser = (User) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
		User user = userService.getUserByName(shiroUser.getName());
		List<Authority> authorities = user.getRole().getAuthorities();
		
		Map<Authority, MenuComponent> parentMenuMap = new LinkedHashMap<Authority, MenuComponent>();
		
		for (Authority authority : authorities) {
			MenuComponent menu = new MenuComponent();
			String name = authority.getName();
			String displayName = authority.getDisplayName();
			String url = authority.getUrl();
			menu.setName(name);
			menu.setTitle(displayName);
			menu.setLocation(contextPath+url);
			
			MenuComponent parentMenu = parentMenuMap.get(authority.getParentAuthority());
			if (parentMenu == null) {
				parentMenu = new MenuComponent();
				parentMenu.setName(authority.getParentAuthority().getName());
				parentMenu.setTitle(authority.getParentAuthority().getDisplayName());
				
				parentMenu.setParent(topMenu);
				parentMenuMap.put(authority.getParentAuthority(), parentMenu);
			}
			
			menu.setParent(parentMenu);
		}
		
		session.setAttribute("repository", repository);
		
		return "home/menu";
	}
	
}
