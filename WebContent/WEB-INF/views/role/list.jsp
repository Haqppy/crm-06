<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>角色管理</title>
	<script type="text/javascript">
		$(function(){
			//onclick="window.location.href='${ctp}/role/delete/${item.id }'"
			$("img[id ^='delete-']").click(function(){
				var id = $(this).prev().val();
				var label = $(this).next().val();
				var flag = confirm("确定要删除"+label+"吗？");
				if(flag){
					var url = "${ctp}/role/delete/"+id;
					var args = {"time":new Date(),"_method":"DELETE"};
					$.post(url,args,function(data){
						if(data == '1'){
							alert("删除成功");
							window.location.href="${ctp}/role/list";
						}
					},"json");
				}
				return false;
			});		
		})
	</script>
</head>

<body class="main">

	<div class="page_title">
		角色管理
	</div>
	
	<div class="button_bar">
		<button class="common_button" onclick="window.location.href='${ctp}/role/input'">
			新建
		</button>
	</div>
	
	<form action="${ctp}/role/list" method="get">
		<!-- 列表数据 -->
		<br />
			<table class="data_list_table" border="0" cellPadding="3"
				cellSpacing="0">
				<tr>
					<th class="data_title" >
						编号
					</th>
					<th class="data_title" >
						角色名
					</th>
					<th class="data_title" >
						角色描述
					</th>
					<th class="data_title">
						状态
					</th>
					<th class="data_title">
						操作
					</th>
				</tr>
				<c:if test="${empty page.content }">
					没有任何数据!
				</c:if>
				<c:if test="${!empty page.content }">
					<c:forEach items="${page.content }" var="item">
						<tr>
							<td class="data_cell" style="text-align:right;padding:0 10px;">
							 	${item.id }
							</td>
							<td class="data_cell" style="text-align:center;">
								${item.name }
							</td>
							<td class="data_cell" style="text-align:left;">
								${item.description }
							</td>
							<td class="data_cell" style="text-align:center;">
								${item.enabled ? "有效":"无效"}
							</td>
							<td class="data_cell">
								<img onclick="window.location.href='${ctp}/role/assign/${item.id }'" 
								     title="分配权限" 
								     src="${ctp}/static/images/bt_linkman.gif" 
								     class="op_button" />
								<input type="hidden" value="${item.id }"/>
								<img  title="删除"
								      src="${ctp}/static/images/bt_del.gif" id="delete-"+${item.id } />
								<input type="hidden" value="${item.name }"/>
							</td>
						</tr>
					</c:forEach>
				</c:if>
			</table>
			
			<tags:pagination paginationSize="4" page="${page }"></tags:pagination>
	</form>
</body>
</html>
