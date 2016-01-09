package com.atguigu.crm.shiro;

import java.util.Collection;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.crm.dao.UserMapper;
import com.atguigu.crm.entity.User;

@Component
public class ShiroRealm extends AuthorizingRealm{

	@Autowired
	private UserMapper userMapper;
	
	/*@Autowired
	private UserService UserService = null;*/
	
	@Transactional
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		String username = usernamePasswordToken.getUsername();
		User user = userMapper.getUserByName(username);
		
		if (user == null) {
			throw new UnknownAccountException();
		}
		if (user.getEnabled() != 1) {
			throw new LockedAccountException();
		}
		Object principal = user;
		String hashedCredentials = user.getPassword();
		ByteSource credentialsSalt = ByteSource.Util.bytes(user.getSalt());
		String realmName = getName();
		
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, hashedCredentials, credentialsSalt, realmName);
		
		return info;
		
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		User user = (User) principals.getPrimaryPrincipal();
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		
		Collection<String> roleList = user.getRoleList();
		info.addRoles(roleList);
		
		return info;
	}

	@Autowired
	@Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		super.setCredentialsMatcher(credentialsMatcher);
	}
	/**
	 * 盐值
	 */
	
	/*@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
		matcher.setHashAlgorithmName("MD5");
		matcher.setHashIterations(1024);
		
		setCredentialsMatcher(matcher);
		
	}*/
	
	public static void main(String[] args) {
		String algorithmName = "MD5";
		String source = "123456";
		ByteSource salt = ByteSource.Util.bytes("db314a8d91bd6f83");
		int hashIterations = 1024;
		
		SimpleHash result = new SimpleHash(algorithmName, source, salt, hashIterations);
		System.out.println(result);
	}

}
