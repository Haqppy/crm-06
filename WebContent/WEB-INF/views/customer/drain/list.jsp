<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>客户流失管理</title>
</head>
<body>

	<div class="page_title">
		客户流失管理
	</div>
	<div class="button_bar">
		<button class="common_button" onclick="document.forms[0].submit();">
			查询
		</button>
	</div>
	
	<form action="${ctp}/drain/list" method="get"> 
		<table class="query_form_table" border="0" cellPadding="3"
			cellSpacing="0">
			<tr>
				<th>
					客户名称
				</th>
				<td>
					<input type="text" name="search_LIKES_customerName" />
				</td>
				<th>
					客户经理
				</th>
				<td>
					<input type="text" name="search_LIKES_customerManagerName" />
				</td>
				<th>
					&nbsp;
				</th>
				<td>
					&nbsp;
				</td>
			</tr>
		</table>
		<!-- 列表数据 -->
		<br />
		
			<table class="data_list_table" border="0" cellPadding="3"
				cellSpacing="0">
				<tr>
					<th>
						编号
					</th>
					<th>
						客户名称
					</th>
					<th>
						客户经理
					</th>
					<th>
						上次下单时间
					</th>
					<th>
						确认流失时间
					</th>
					<th>
						流失原因
					</th>
					<th>
						状态
					</th>
					<th>
						操作
					</th>
				</tr>
				<c:if test="${empty page.content  }">
					没有任何流失信息!
				</c:if>
				<c:if test="${!empty page.content  }">
				
					<c:forEach items="${page.content }" var="item">
						<tr>
							<td class="list_data_ltext">
								${item.id }
							</td>
							<td class="list_data_ltext">
								${item.customer.name }
							</td>
							<td class="list_data_ltext">
								${item.customer.manager.name }
							</td>
							<td class="list_data_ltext">
								<fmt:formatDate value="${item.lastOrderDate }" pattern="yyyy-MM-dd"/>
							</td>
							<td class="list_data_ltext">
								<fmt:formatDate value="${item.drainDate }" pattern="yyyy-MM-dd"/>
							</td>
							<td class="list_data_ltext">
								${item.reason }
							</td>
							<td class="list_data_ltext">
								${item.status }
							</td>
							<c:if test="${item.status == '流失' }">
								<td class="list_data_op">
							
								</td>
							</c:if>
							<c:if test="${item.status != '流失' }">
								<td class="list_data_op">
									<img onclick="window.location.href='${ctp}/drain/confirm/${item.id }'" 
										title="确认流失" src="${ctp}/static/images/bt_confirm.gif" class="op_button" />
									<img onclick="window.location.href='${ctp}/drain/delay?id=10361'" 
										title="暂缓流失" src="${ctp}/static/images/bt_relay.gif" class="op_button" />
									
								</td>
							</c:if>
						</tr>
					</c:forEach>
				</c:if>
			</table>

	<div style="text-align:right; padding:6px 6px 0 0;">
				共 ${page.totalElements } 条记录 
				&nbsp;&nbsp;
				
				当前第 ${page.pageNo } 页/共 ${page.totalPages }  页
				&nbsp;&nbsp;
				
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
						if(pageNo2 < 1 || pageNo2 > parseInt("${page.totalPages}")){
							$(this).val("");
							alert("输入的页码不合法");
							return;
						}
						
						//查询条件需要放入到 class='condition' 的隐藏域中. 
						window.location.href = window.location.pathname + "?pageNo=" + pageNo2+"&${queryString }";
					});
				})
			</script>
		
	</form>
	
</body>
</html>
