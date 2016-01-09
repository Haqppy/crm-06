<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>联系人管理</title>
	<script type="text/javascript">
		$(function(){
			//onclick="window.location.href='${ctp}/contact/delete?id=${contact.id }&customerId=${customer.id}
			$(".delete").click(function(){
				var id = $(this).prev().val();
				var customerId = $(this).next().val();
				
				var url = "${ctp}/contact/delete";
				var args = {"id":id,"customerId":customerId,"time2":new Date(),"_method":"DELETE"};
				
				$.post(url,args,function(data){
					if(data == "1"){
						alert("删除成功!");
						window.location.href="${ctp}/contact/list?id="+customerId;
					}else{
						alert("当前只有一个联系人,不可删除!");
					}
				},"json");
				
				return false;
			});
		})
	</script>
</head>

<body>

	<div class="page_title">
		联系人管理
	</div>
	<div class="button_bar">

		<button class="common_button" onclick="window.location.href='${ctp}/contact/create/${customer.id}'">
			新建
		</button>
		<button class="common_button" onclick="javascript:history.go(-1);">
			返回
		</button>
	</div>
	
	${customer.id}
	<form action="${ctp}/contact/list" method="post">
		<table class="query_form_table" border="0" cellPadding="3"
			cellSpacing="0">
			<tr>
				<th>
					客户编号
				</th>
				<td>${customer.no }</td>
				<th>
					客户名称
				</th>
				<td>${customer.name }</td>
			</tr>
		</table>
		<!-- 列表数据 -->
		<br />
		
			<table class="data_list_table" border="0" cellPadding="3"
				cellSpacing="0">
				<tr>
					<th>
						姓名
					</th>
					<th>
						性别
					</th>
					<th>
						职位
					</th>
					<th>
						办公电话
					</th>
					<th>
						手机
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
					
					<c:forEach items="${page.content }" var="contact">
						<tr>
						    <td class="list_data_text">
							 	${contact.name }
							</td>
						    <td class="list_data_text">
								 ${contact.sex }
							</td>
						    <td class="list_data_text">
								 ${contact.position }
							</td>
						    <td class="list_data_text">
								 ${contact.tel }
							</td>
						    <td class="list_data_text">
								 ${contact.mobile }
							</td>
						    <td class="list_data_text">
								 ${contact.memo }
							</td>
							<td class="list_data_op">
								<img onclick="window.location.href='${ctp}/contact/create?id=${contact.id }&customerId=${customer.id}'" 
									title="编辑" src="${ctp}/static/images/bt_edit.gif" class="op_button" />
								<input type="hidden" name="id" value="${contact.id }"/>
								<img class="delete"
									title="删除" src="${ctp}/static/images/bt_del.gif" class="op_button" />
								<input type="hidden" name="customerId" value="${customer.id}"/>
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
	
	<c:if test="${page.hasPrev}">
			<a href='${ctp }/contact/list?pageNo=1&id=${customer.id }'>首页</a>
			&nbsp;&nbsp;
			<a href='${ctp }/contact/list?pageNo=${page.prev }&id=${customer.id }'>上一页</a>
			&nbsp;&nbsp;
	</c:if>
	<c:if test="${page.hasNext }">
		<a href='${ctp }/contact/list?pageNo=${page.next }&id=${customer.id }'>下一页</a>
		&nbsp;&nbsp;
		<a href='${ctp }/contact/list?pageNo=${page.totalPages }&id=${customer.id }'>末页</a>
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
				+ "?page=" + pageNo2 + "&id=${customer.id }";
			
		});
	})
</script>
		
		
	</form>
</body>
</html>