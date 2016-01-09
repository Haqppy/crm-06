<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>库存查询</title>
	<script type="text/javascript">
	
		$(function(){
			//onclick="window.location.href='${ctp}/storage/delete?id=1'" 
			$("img[id ^='delete-']").click(function(){
				var id = $(this).prev().val();
				var label = $(this).next().val();
				var flag = confirm("确定要删除"+label+"产品吗?");
				if(flag){
					var url = "${ctp}/storage/delete/"+id;
					var args = {"time":new Date,"_method":"DELETE"};
					
					$.post(url,args,function(data){
						if(data == "1"){
							alert("删除成功!");
							window.location.href="${ctp}/storage/list";
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
		库存管理
	</div>
	<div class="button_bar">
		<button class="common_button"
			onclick="window.location.href='${ctp}/storage/create'">
			库存添加
		</button>
		<button class="common_button" onclick="document.forms[0].submit();">
			查询
		</button>
	</div>
	
	<form action="${ctp}/storage/list" method="get">
		<table class="query_form_table" border="0" cellPadding="3"
			cellSpacing="0">
			<tr>
				<th>
					产品
				</th>
				<td>
					<input type="text" name="search_LIKES_product.name" />
				</td>
				<th>
					仓库
				</th>
				<td>
					<input type="text" name="search_LIKES_wareHouse" />
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
					产品
				</th>
				<th>
					仓库
				</th>
				<th>
					货位
				</th>
				<th>
					件数
				</th>
				<th>
					备注
				</th>
				<th>
					操作
				</th>
			</tr>
			<c:if test="${empty page.content  }">
				没有任何数据
			</c:if>
			
			<c:if test="${!empty page.content  }">
				<c:forEach items="${page.content }" var="item">
					<tr>
						<td class="list_data_number">
							${item.id }
						</td>
						<td class="list_data_ltext">
							${item.product.name }
						</td>
						<td class="list_data_ltext">
							${item.wareHouse }
						</td>
						<td class="list_data_ltext">
							${item.stockWare }
						</td>
						<td class="list_data_ltext">
							${item.stockCount }
						</td>
						<td class="list_data_ltext">
							${item.memo }
						</td>
						<td class="list_data_op">
							<img onclick="window.location.href='${ctp}/storage/create/${item.id }'" 
								title="修改" src="${ctp}/static/images/bt_edit.gif" class="op_button" />
							<input type="hidden" value="${item.id }"/>
							<img title="删除" src="${ctp}/static/images/bt_del.gif" id="delete-"+${item.id } />
							<input type="hidden" value="${item.product.name }"/>
						</td>
					</tr>
				</c:forEach>
			</c:if>
		</table>

		<tags:pagination paginationSize="4" page="${page }"></tags:pagination>
	</form>
</body>
