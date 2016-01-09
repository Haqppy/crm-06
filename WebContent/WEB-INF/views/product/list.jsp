<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>产品查询</title>
	<script type="text/javascript">
		$(function(){
			//window.location.href='${ctp}/product/delete/${item.id }
			$("img[id ^='delete-']").click(function(){
				var id = $(this).prev().val();
				var label = $(this).next().val();
				var flag = confirm("确定要删除"+label+"吗？");
				if(flag){
					var url = "${ctp}/product/delete/"+id;
					var args = {"time":new Date(),"_method":"DELETE"};
					$.post(url,args,function(data){
						if(data == '1'){
							alert("删除成功");
							window.location.href="${ctp}/product/list";
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
		产品管理
	</div>
	<div class="button_bar">
		<button class="common_button" onclick="window.location.href='${ctp}/product/create'">
			产品添加
		</button>
		<button class="common_button" onclick="document.forms[0].submit();">
			查询
		</button>
	</div>

	<form action="${ctp}/product/list" method="get">
		<table class="query_form_table" border="0" cellPadding="3"
			cellSpacing="0">
			<tr>
				<th>
					名称
				</th>
				<td>
					<input type="text" name="search_LIKES_name" />
				</td>
				<th>
					型号
				</th>
				<td>
					<input type="text" name="search_LIKES_type" />
				</td>
				<th>
					批次
				</th>
				<td>
					<input type="text" name="search_LIKES_batch" />
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
				名称
			</th>
			<th>
				型号
			</th>
			<th>
				等级/批次
			</th>
			<th>
				单位
			</th>
			<th>
				单价（元）
			</th>
			<th>
				备注
			</th>
			<th>
				操作
			</th>
		</tr>
		<c:if test="${empty page.content }">
			没有数据!
		</c:if>
		<c:if test="${!empty page.content }">
			<c:forEach items="${page.content }" var="item">
				<tr>
					<td class="list_data_number">
						${item.id }
					</td>
					<td class="list_data_ltext">
						${item.name }
					</td>
					<td class="list_data_ltext">
						${item.type }
					</td>
					<td class="list_data_ltext">
						${item.batch }
					</td>
					<td class="list_data_ltext">
						${item.unit }
					</td>
					<td class="list_data_ltext">
						${item.price }
					</td>
					<td class="list_data_ltext">
						${item.memo }
					</td>
					<td class="list_data_op">
					
						<img onclick="window.location.href='${ctp}/product/create?id=1'" 
							title="编辑" src="${ctp}/static/images/bt_edit.gif" class="op_button" />
					    <input type="hidden" value="${item.id }"/>
						<img title="删除" src="${ctp}/static/images/bt_del.gif" id="delete-"+${item.id } />
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