<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>客户服务分析</title>
  <body>
	<div class="page_title">客户服务分析</div>
	<div class="button_bar">
		<button class="common_button" onclick="document.forms[0].submit();">查询</button>  
	</div>
  	<form action="${ctp}/report/service">
		<div id="listView" style="display:block;">
			<table class="query_form_table" border="0" cellPadding="3" cellSpacing="0">
				<tr>
					<th>
						日期
					</th>
					<td>
						<input type="text" name="search_date1" size="10" />
						-
						<input type="text" name="search_date2" size="10" />
					</td>
					<th>&nbsp;</th>
					<td>&nbsp;</td>
				</tr>
			</table>
			<!-- 列表数据 -->
			<br />
			<table class="data_list_table" border="0" cellPadding="3" cellSpacing="0">
					<tr>
						<th>序号</th>
						<th>条目</th>
						<th>客户数量</th>
					</tr>
						<c:if test="${empty page.content }">
					      没有任何数据
					</c:if> 
					<c:if test="${!empty page.content }" >
						<c:forEach items="${page.content }" var="objects" varStatus="status">
							<tr>
								<td class="list_data_number" >${status.index + 1 }</td>
								<td class="list_data_ltext">${objects[0] }</td>
								<td class="list_data_number">${objects[1] }</td>
							</tr>
						</c:forEach>
					</c:if>
			</table>
		<tags:pagination paginationSize="4" page="${page }"></tags:pagination>	
	</form>	
  </body>
</html>