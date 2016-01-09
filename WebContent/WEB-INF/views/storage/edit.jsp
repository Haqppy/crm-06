<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>
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
	<form:form id="storage" action="${ctp}/storage/create" method="POST" modelAttribute="storage">
		<c:if test="${storage.id != null }">
			<input id="id" name="id" type="hidden" value="${storage.id }"/>
			<input type="hidden" name="_method" value="PUT">
		</c:if>
		<table class="query_form_table" border="0" cellPadding="3"
			cellSpacing="0">
			<tr>
				<th>
					产品 -true-
				</th>
				<td>
					<input id="product.name" name="product.name" readonly="readonly" type="text" value="${storage.product.name }"/>
					<input id="product.id" name="product.id" readonly="true" type="hidden" value="${storage.product.id }"/>
				</td>
				<th>
					仓库
				</th>
				<td>
					<input id="wareHouse" name="wareHouse" readonly="readonly" type="text" value="${storage.wareHouse }"/>
				</td>
			</tr>
			<tr>
				<th>
					货位
				</th>
				<td>
					<input id="stockWare" name="stockWare" readonly="readonly" type="text" value="${storage.stockWare }"/>
				</td>
				<th>
					
					新增数量
				</th>
				<td>
					<input id="stockCount" name="stockCount" type="hidden" value="${storage.stockCount }"/>
					<input name="incrementCount"/>
				</td>
			</tr>
			<tr>
				<th>
					备注
				</th>
				<td>
					<input id="memo" name="memo" readonly="readonly" type="text" value="${storage.memo }"/>
				</td>
			</tr>
		</table>
	</form:form>
	
</body>
</html>
