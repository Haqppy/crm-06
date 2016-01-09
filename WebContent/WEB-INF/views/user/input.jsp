<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>增加用户</title>
  </head>

  <body class="main">
  
  	
  	<form:form id="user" action="${ctp}/user/create/${user.id }" method="POST" modelAttribute="user">
  		
  		<c:if test="${user.id != null }">
  			<input type="hidden" name="_method" value="PUT"/>
  		</c:if>
  		
	  	<span class="page_title">用户管理　&gt;　新建用户</span>

		<div class="button_bar">
			<button class="common_button" onclick="javascript:history.go(-1);">返回</button>
			<button class="common_button" onclick="document.forms[0].submit()">保存</button>
		</div>
		
		<table class="query_form_table" border="0" cellPadding="3" cellSpacing="0">
			<tr>
				<th class="input_title">用户名</th>
				<td class="input_content">
					<form:input path="name"/>
					<div id='divCheck'></div>
				</td>
				
				<th class="input_title">密码</th>
				<td class="input_content">
					<form:password path="password"/>
				</td>
			</tr>
			
			<tr>
				<th class="input_title">角色</th>
				<td class="input_content">
					<form:select path="role.id" 
								 items="${roles }" 
								 itemLabel="name"
								 itemValue="id"></form:select>
				</td>
				<th class="input_title">状态</th>
				<td class="input_content">
					<span>
						<form:radiobuttons path="enabled" items="${allStatus }"/>
					</span>
				</td>
			</tr>
			
		</table>
  	</form:form>
	
  </body>
</html>
