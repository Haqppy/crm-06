<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		$(function(){
			$("#new").click(function(){
				window.location.href="${ctp}" + "/user/create";
				return false;
			});
			
			$(".delete").click(function(){
				var id = $(this).prev().val();
				var url = "${ctp}"+"/user/delete/"+id;
				var label = $(this).next().val();
				
				var flag = confirm("确定要删除"+label+"的信息吗?");
				if(flag){
					$("#hiddenForm").attr("action",url).submit();
				}
				
			});
			
		})
	</script>
</head>

<body class="main">
	------------------${queryString }----------------
	<form action="" method="post" id="hiddenForm">
		<input type="hidden" name="_method" value="DELETE"/>
	</form>
	
	<form action="${ctp}/user/list">
		<div class="page_title">
			用户管理
		</div>
		<div class="button_bar">
			<button class="common_button" id="new">新建</button>
			<button class="common_button" onclick="document.forms[1].submit();">
				查询
			</button>
		</div>
		<table class="query_form_table" border="0" cellPadding="3"
			cellSpacing="0">
			<tr>
				<th class="input_title">
					用户名
				</th>
				<td class="input_content">
					<input type="text" name="search_LIKES_name" />
				</td>
				<th class="input_title">
					状态
				</th>
				<td class="input_content">
					<select name="search_EQI_enabled">
						<option value="">
							全部
						</option>
						<option value="1">
							正常
						</option>
						<option value="0">
							已删除
						</option>
					</select>
				</td>
			</tr>
		</table>
		<!-- 列表数据 -->
		<br />
			
		<c:if test="${empty page.content }">
			没有任何数据可显示.
		</c:if>
		
		<c:if test="${!empty page.content }">
			
			<table class="data_list_table" border="0" cellPadding="3"
				cellSpacing="0">
				<tr>
					<th class="data_title" style="width: 40px;">
						编号
					</th>
					<th class="data_title" style="width: 50%;">
						用户名
					</th>
					<th class="data_title" style="width: 20%;">
						状态
					</th>
					<th class="data_title">
						操作
					</th>
				</tr>
					<c:forEach items="${page.content }" var="item">
						<tr>
							<td class="data_cell" style="text-align: right; padding: 0 10px;">
								${item.id }
							</td>
							<td class="data_cell" style="text-align: center;">
								${item.name }
							</td>
							<td class="data_cell">
							   <c:choose>
							   		<c:when test="${item.enabled == 1 }">
							   			有效
							   		</c:when>
							   		<c:when test="${item.enabled == 2 }">
							   			无效
							   		</c:when>
							   </c:choose>
							</td>
							<td class="data_cell">
								<input type="hidden" value="${item.id }"/>
									<img title="删除" src="${ctp}/static/images/bt_del.gif" class="delete" />
								<input type="hidden" value="${item.name }"/>
								
								<img onclick="window.location.href='${ctp }/user/create/${item.id}'" 
									class="op_button" src="${ctp}/static/images/bt_edit.gif" title="编辑" />
							</td>
						</tr>
				 </c:forEach>
			</table>
		</c:if>

<div style="text-align:right; padding:6px 6px 0 0;">

	共 ${page.totalElements } 条记录 
	&nbsp;&nbsp;
	
	当前第 ${page.pageNo } 页/共 ${page.totalPages } 页
	&nbsp;&nbsp;
	
		<c:if test="${page.hasPrev}">
			<a href='?pageNo=1&${queryString }'>首页</a>
			&nbsp;&nbsp;
			<a href='?&pageNo=${page.prev }&${queryString }'>上一页</a>
			&nbsp;&nbsp;
		</c:if>
		<c:if test="${page.hasNext }">
			<a href='?pageNo=${page.next }&${queryString }'>下一页</a>
			&nbsp;&nbsp;
			<a href='?pageNo=${page.totalPages}&${queryString }'>末页</a>
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
			if(pageNo2 < 1 || pageNo2 > parseInt("2")){
				$(this).val("");
				alert("输入的页码不合法");
				return;
			}
			
			//查询条件需要放入到 class='condition' 的隐藏域中. 
			window.location.href = window.location.pathname
				+ "?pageNo=" + pageNo2+"&${queryString }";
			
		});
	})
</script>
		
		
	</form>
</body>
</html>
