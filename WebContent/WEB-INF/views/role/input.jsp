<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
	<head>
		<title>添加角色</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	</head>
	<body>
		<div class="page_title">
			系统角色添加
		</div>
		<form:form action="${ctp }/role/create" method="post" modelAttribute="role">
			<div class="button_bar">
				<button class="common_button" onclick="javascript:history.back(-1);">
					返回
				</button>
				<button class="common_button" onclick="document.forms[0].submit();">
					保存
				</button>
			</div>
			
			<table class="query_form_table">
				<tr>
					<th>
						角色名称
					</th>
					<td>
						<form:input path="name"/>
					</td>
					<th>
						角色描述
					</th>
					<td>
						<form:input path="description"/>
					</td>
				</tr>
				<tr>
					<th>
						状态
					</th>
					<td>
						<select name="enabled">
							<option value="1">有效</option>
							<option value="0">无效</option>
						</select>
						<span class="red_star">*</span>
					</td>
				</tr>
			</table>
	</form:form>
	</body>
</html>
