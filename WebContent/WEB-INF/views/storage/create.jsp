<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>编辑库存</title>
</head>

<body class="main">

	<span class="page_title">编辑库存</span>
	<div class="button_bar">
		<button class="common_button" onclick="javascript:history.go(-1);">
			返回
		</button>
		<button class="common_button" onclick="document.forms[0].submit();">
			保存
		</button>
	</div>
	<form:form action="${ctp}/storage/create" method="post" modelAttribute="storage">
	
		<table class="query_form_table" border="0" cellPadding="3"
			cellSpacing="0">
			<tr>
				<th>
					产品 -false-
				</th>
				<td>
					<form:select path="product.id">
						<option value="">请选择...</option>
						<c:forEach items="${products }" var="product">
							<form:option value="${product.id }">${product.name }</form:option>
						</c:forEach>
					</form:select>
				</td>
				<th>
					仓库
				</th>
				<td>
					<form:input path="wareHouse"/>
				</td>
			</tr>
			<tr>
				<th>
					货位
				</th>
				<td>
					<form:input path="stockWare"/>
				</td>
				<th>
					数量
					
				</th>
				<td>
					<form:input path="stockCount"/>
				</td>
			</tr>
			<tr>
				<th>
					备注
				</th>
				<td>
					<form:input path="memo"/>
				</td>
			</tr>
		</table>
	</form:form>
	
</body>
</html>
