<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>客户服务管理</title>
</head>
<body>
 
	<div class="page_title">
		客户服务管理
	</div>
	<div class="button_bar">
		<button class="common_button" onclick="">
			新建
		</button>
		<button class="common_button" onclick="document.forms[0].submit();">
			查询
		</button>
	</div>
	
	<form action="${ctp}/service/feedback/list" method="get">
		<table class="query_form_table" border="0" cellPadding="3" cellSpacing="0">
			<tr>
				<th>
					服务类型
				</th>
				<td>
					<input type="text" name="search_LIKE_serviceType" />
				</td>
				<th>
					概要
				</th>
				<td>
					<input type="text" name="search_LIKE_serviceTitle" />
				</td>
			</tr>
			<tr>
				<th>
					客户
				</th>
				<td>
					<input type="text" name="search_EQ_customer.name" />
				</td>
				<th>
					创建时间
				</th>
				<td>
					<input type="text" name="search_GTE_createDate" size="10" />
					-
					<input type="text" name="search_LTE_createDate" size="10" />
				</td>
			</tr>
		</table>
		
		<!-- 列表数据 -->
		<br />
		
		
			<table class="data_list_table" border="0" cellPadding="3" cellSpacing="0">
				<tr>
					<th>
						编号
					</th>
					<th>
						服务类型
					</th>
					<th>
						概要
					</th>
					<th>
						客户
					</th>
					<th>
						创建人
					</th>
					<th>
						创建时间
					</th>
					<th>
						操作
					</th>
				</tr>
				
					<c:if test="${empty page.content }">
					没有任何数据!
				</c:if >
				
				<c:if test="${! empty page.content }">
					<c:forEach items="${page.content }" var="cs">
						<tr>
						    <td class="list_data_number">
								${cs.id }
							</td>
							<td class="list_data_number">
								${cs.serviceType }
							</td>
							<td class="list_data_number">
								${cs.serviceTitle }
							</td>
							<td class="list_data_number">
								${cs.customer.name }
							</td>
							<td class="list_data_number">
								${cs.createdby.name }
							</td>
							<td class="list_data_number">
								<fmt:formatDate value="${cs.createDate }" pattern="yyyy-MM-dd"/>
							</td>
							<td class="list_data_op">
								<img onclick="window.location.href='${ctp}/service/feedback/${cs.id }'" 
									title="反馈" src="${ctp}/static/images/bt_feedback.gif" class="op_button" />
							</td>
						</tr>
					</c:forEach>
				</c:if>
			</table>

	<div style="text-align:right; padding:6px 6px 0 0;">
	
		共 ${page.totalElements } 条记录 
		&nbsp;&nbsp;
		
		当前第 ${page.pageNo } 页/共 ${page.totalPages } 页
		&nbsp;&nbsp;
		
		<c:if test="${page.hasPrev }">
			<a href='?pageNo=1&${queryString }'>首页</a>
			&nbsp;&nbsp;
			<a href='?pageNo=${page.prev }&${queryString }'>上一页</a>
			&nbsp;&nbsp;
		</c:if>
		<c:if test="${page.hasNext }">
			<a href='?pageNo=${page.next }&${queryString }'>下一页</a>
			&nbsp;&nbsp;
			<a href='?pageNo=${page.totalPages }&${queryString }'>末页</a>
			&nbsp;&nbsp;
		</c:if>
		
		转到 <input id="pageNo" size='1'/> 页
		&nbsp;&nbsp;
	
	</div>
		<script type="text/javascript" src="${ctp}/static/jquery/jquery-1.9.1.min.js"></script>
		<script type="text/javascript">
			$(function(){
				
				$("#pageNo").change(function(){
					
					var pageNo = $(this).val();
					var reg = /^\d+$/;
					if(!reg.test(pageNo)){
						$(this).val("");
						alert("输入的页码不合法");
						return;
					}
					
					var pageNo2 = parseInt(pageNo);
					if(pageNo2 < 1 || pageNo2 > parseInt("1")){
						$(this).val("");
						alert("输入的页码不合法");
						return;
					}
					
					//查询条件需要放入到 class='condition' 的隐藏域中. 
					window.location.href = window.location.pathname
						+ "?page=" + pageNo2 +"&${queryString }";
					
				});
			})
		</script>
	</form>
</body>
</html>

