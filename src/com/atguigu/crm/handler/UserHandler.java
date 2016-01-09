package com.atguigu.crm.handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import com.atguigu.crm.entity.Role;
import com.atguigu.crm.entity.User;
import com.atguigu.crm.orm.Page;
import com.atguigu.crm.service.UserService;
import com.atguigu.crm.service.jpa.RoleService;

@RequestMapping("/user")
@Controller
public class UserHandler {

	@Autowired
	private UserService userService;
	@Autowired
	private ResourceBundleMessageSource messageSource;
	@Autowired
	private RoleService roleService;
	
	@RequestMapping(value="/shiro-login",method=RequestMethod.POST)
	public String shiroLogin(@RequestParam(value="username") String name,
			@RequestParam(value="password") String password,
			HttpSession session,RedirectAttributes attributes,Locale locale) {
		
		Subject currentUser = SecurityUtils.getSubject();
		AuthenticationException ex = null;
		if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(name, password);
            token.setRememberMe(true);
            try {
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
            	attributes.addFlashAttribute("message", "用户名不存在");
            	ex = uae;
            } catch (IncorrectCredentialsException ice) {
            	attributes.addFlashAttribute("message", "用户名和密码不匹配");
            	ex = ice;
            } catch (LockedAccountException lae) {
            	attributes.addFlashAttribute("message", "用户名被锁定");
            	ex = lae;
            }catch (AuthenticationException ae) {
            	attributes.addFlashAttribute("message", "其他登录异常");
            	ex = ae;
            }
            
        }
		if (ex != null) {
			attributes.addFlashAttribute("name", name);
			return "redirect:/index.jsp";
		}

		Object user = SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
		session.setAttribute("user", user);
		
		return "redirect:/success";
	}
	
	
	@RequestMapping(value="/create/{id}",method=RequestMethod.PUT)
	public String update(User user,RedirectAttributes attributes) {
		userService.update(user);
		attributes.addFlashAttribute("message", "修改成功!");
		
		return "redirect:/user/list";
	}
	
	@RequestMapping(value="/create/{id}",method=RequestMethod.GET)
	public String edite(@PathVariable("id")Long id,Map<String,Object> map) {
		User user = userService.getUserById(id);
		
		map.put("user", user);
		
		List<Role> roles = roleService.getRoles();
		map.put("roles", roles);
		
		Map<String, Object> allStatus = new HashMap<String, Object>();
		allStatus.put("1", "有效");
		allStatus.put("0", "无效");
		map.put("allStatus",allStatus);
		
		return "user/input";
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public String delete(@PathVariable("id")Long id,RedirectAttributes attributes) {
		
		userService.delete(id);
		attributes.addFlashAttribute("message", "删除成功");
		return "redirect:/user/list";
		
	}
	
	@RequestMapping(value="/create",method=RequestMethod.POST)
	public String save(User user) {
		userService.save(user);
		return "redirect:/user/list";
	}
	
	@RequestMapping(value="/create",method=RequestMethod.GET)
	public String input(Map<String, Object> map) {
		map.put("user", new User());
		
		List<Role> roles = roleService.getRoles();
		map.put("roles", roles);
		
		Map<String, Object> allStatus = new HashMap<String, Object>();
		allStatus.put("1", "有效");
		allStatus.put("0", "无效");
		map.put("allStatus",allStatus);
		
		return "user/input";
	}
	
	/**
	 * 带查询条件的分页
	 * @param pageNoStr
	 * @param pageSizeStr
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(@RequestParam(value="pageNo",required=false,defaultValue="1")String pageNoStr,
			@RequestParam(value="pageSize" ,required=false,defaultValue="4")String pageSizeStr,Map<String,Object> map,HttpServletRequest request) {
		Page<User> page = new Page<User>();
		page.setPageNo(pageNoStr);
		
		int pageSize = 4;
		try {
			pageSize = Integer.parseInt(pageSizeStr);
			page.setPageSize(pageSize);
		} catch (NumberFormatException e) {}
		
		//search_LIKES_name
		Map<String, Object> params = WebUtils.getParametersStartingWith(request, "search_");
		//带查询条件的分页
		page = userService.getPage(page,params);
		
		//将查询条件序列化成字符串返回
		String queryString = encodeParameterStringWithPrefix(params,"search_");
		
		map.put("page", page);
		map.put("queryString", queryString);
		
		return "user/list";
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
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String login(@RequestParam(value="username") String name,
			@RequestParam(value="password") String password,
			HttpSession session,RedirectAttributes attributes,Locale locale) {
		
		User user = userService.login(name, password);
		if (user != null) {
			session.setAttribute("user", user);
			return "redirect:/success";
		}
		
		String message = messageSource.getMessage("error.crm.user.login", null, locale);
		attributes.addFlashAttribute("message",message);
		attributes.addFlashAttribute("name",name);
		
		return "redirect:/index";
		
	}
	
	/*@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(@RequestParam(value="pageNo",required=false,defaultValue="1")String pageNoStr,
			@RequestParam(value="pageSize" ,required=false,defaultValue="4")String pageSizeStr,Map<String,Object> map) {
		Page<User> page = new Page<User>();
		page.setPageNo(pageNoStr);
		
		int pageSize = 4;
		try {
			pageSize = Integer.parseInt(pageSizeStr);
			page.setPageSize(pageSize);
		} catch (NumberFormatException e) {}
		
		page = userService.getPage(page);
		map.put("page", page);
		
		return "user/list";
	}*/
	
}
