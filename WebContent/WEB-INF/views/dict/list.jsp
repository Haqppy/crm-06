<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>管理</title>
	<script type="text/javascript">
		$(function(){
			//onclick="window.location.href='${ctp}/dict/delete?id=10401'"
			$("img[id ^='delete-']").click(function(){
				var id = $(this).prev().val();
				var label = $(this).next().val();
				var flag = confirm("确定要删除"+label+"条目吗?");
				if(flag){
					var url = "${ctp}/dict/delete/"+id;
					var args = {"time":new Date(),"_method":"DELETE"};
					$.post(url,args,function(data){
						if(data == '1'){
							alert("删除成功");
							window.location.href="${ctp}/dict/list";
						}
					},"json");
				}
				return false;
			});		
		})
	</script>
</head>
<body>
	<div class="page_title">
		基础数据管理
	</div>
	<div class="button_bar">
		<button class="common_button" onclick="window.location.href='${ctp}/dict/create'">
			新建
		</button>
		<button class="common_button" onclick="document.forms[0].submit();">
			查询
		</button>
	</div>
	
	<form action="${ctp}/dict/list" method="get">
		<table class="query_form_table" border="0" cellPadding="3"
			cellSpacing="0">
			<tr>
				<th>
					类别
				</th>
				<td>
					<input type="text" name="search_LIKES_type" />
				</td>
				<th>
					条目
				</th>
				<td>
					<input type="text" name="search_LIKES_item" />
				</td>
				<th>
					值
				</th>
				<td>
					<input type="text" name="search_LIKES_value" />
				</td>
			</tr>
		</table>
	</form>
	<!-- 列表数据 -->
	<br />
		<table class="data_list_table" border="0" cellPadding="3"
			cellSpacing="0">
			<tr>
				<th>
					编号
				</th>
				<th>
					类别
				</th>
				<th>
					条目
				</th>
				<th>
					值
				</th>
				<th>
					操作
				</th>
			</tr>
			<c:if test="${empty page.content }">
				没有数据!
			</c:if>
			<c:if test="${!empty page.content}">
				
			  <c:forEach items="${page.content }" var="item">
			  	<tr>
					<td class="list_data_number">
						${item.id }
					</td>
					<td class="list_data_text">
						${item.type }
					</td>
					<td class="list_data_text">
						${item.item }
					</td>
					<td class="list_data_text">
						${item.value }
					</td>
					<c:if test="${item.editable }">
						<td class="list_data_op">
							<img onclick="window.location.href='${ctp}/dict/create/${item.id }'" 
								title="编辑" src="${ctp}/static/images/bt_edit.gif" class="op_button" />
						    <input type="hidden" value="${item.id }"/>
							<img title="删除" src="${ctp}/static/images/bt_del.gif" id="delete-"+${item.id } />
							<input type="hidden" value="${item.item }"/>
						</td>
					</c:if>
					<c:if test="${! item.editable }">
						<td class="list_data_op">
						</td>
					</c:if>
				</tr>
			  </c:forEach>
			</c:if>
		</table>
	<tags:pagination paginationSize="4" page="${page }"></tags:pagination>
</body>
</html>