<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>制定计划</title>
	<script type="text/javascript">

		$(function(){
			//删除
			$("button[id ^= 'delete']").click(function(){
			
				alert("hello");
				
				var id=$(this).prev(":hidden").val();
				var url = "${ctp}/plan/delete/"+id;
				var args = {"time2":new Date()+"" ,"id":id, "_method":"DELETE"};
			
				$.post(url,args,function(data){
					if(data == '1'){
						alert("删除成功");
						
						$("#plan-"+id).remove();
					}
					
				},"json");
				
				return false;
			
			});
			//修改
			$("button[id ^= 'save']").click(function(){
				alert("修改");
				var id=$(this).prev(":hidden").val();
				var todo = $("#todo-"+id).val();
				var url = "${ctp}/plan/update/"+id;
				var args = {"time2":new Date()+"" ,"todo":todo, "_method":"PUT"};
				
				$.post(url,args,function(data1){
					
					alert("修改成功!");
					
				},"json");
			   return false;
			});
			
			$(".common_button").click(function(){
				//所需要的参数
				var date = $("#date").val();
				var todo = $("#todo").val();
				var chanceId = $("#hiddenId").val();
				
				var url = "${ctp}/plan/make";
				var args = {"time2":new Date()+"","date":date,"todo":todo,"chance.id":chanceId};
				
				$.post(url,args,function(data){
					alert(data);
					
					var $tr = $("<tr id='plan-"+data+"'></tr>");
					var $td1 = $("<td class='list_data_text'>"+date+"</td>");
					var $td2 = $("<td class='list_data_ltext'></td>")
							  .append("<input type='text' size='50' value='"+todo+"' id='todo-"+data+"'/>")
							  .append("<button class='common_button' id='save-"+data+"'>保存</button>")
							  .append("<button class='common_button' id='delete-"+data+"'>删除</button>");
					
					$tr.append($td1)
					   .append($td2);
					$(".data_list_table").append($tr);
					
					$("#delete-"+data).click(function(){
						alert("hello");
						var url2 = "${ctp}/plan/delete/"+data;
						var args2 = {"time2":new Date()+"" , "_method":"DELETE"};
						
						var del = data;
						$.post(url2,args2,function(data){
							if(data == '1'){
								alert("删除成功");
								
								$("#plan-"+del).remove();
								//$(".data_list_table").empty()
								//.append("<tr><th width='200px'>日期</th><th>计划项</th></tr>");
							}
							
						},"json");
						
						return false;
					});
					
					$("#save-"+data).click(function(){
						
						alert("hello");
						
						var url3 = "${ctp}/plan/update/"+data;
						var todo = $("#todo-"+data).val();
						
						var args3 = {"time2":new Date()+"" ,"todo":todo, "_method":"PUT"};
						
						$.post(url3,args3,function(data1){
							
							alert("修改成功!");
							
						},"json");
						
						return false;
					});
					
				},"json");
				
			   return false;
			   
			});
			
			$("#execute").click(function(){
				//onclick="window.location.href='${ctp}/plan/execute/${chance.id}'
				var id = $("#ChanceId").val();
				window,location.href="${ctp}/plan/execute/"+id;

				return false;
			});
		})
	</script>
</head>

<body class="main">
	<span class="page_title">制定计划</span>
	<div class="button_bar">
		<button class="common_button" id="execute">
			执行计划
		</button>
		<button class="common_button" onclick="javascript:history.go(-1);">
			返回
		</button>
	</div>
	
	 <form:form action="${ctp}/plan/make" method="post" modelAttribute="chance">
	 	<input type="hidden" value="${chance.id}" id="ChanceId"/>
		<table class="query_form_table" border="0" cellPadding="3"
			cellSpacing="0">
			<tr>
				<th>编号</th>

				<td>${chance.id}</td>
				<th>机会来源</th>

				<td>${chance.source }</td>
			</tr>
			<tr>
				<th>客户名称</th>
				<td>${chance.custName }</td>
				<th>成功机率（%）</th>

				<td>${chance.rate }</td>
			</tr>
			<tr>
				<th>概要</th>
				<td colspan="3">${chance.title }</td>
			</tr>
			<tr>
				<th>联系人</th>

				<td>${chance.contact }</td>
				<th>联系人电话</th>

				<td>${chance.contactTel }</td>
			</tr>
			<tr>
				<th>机会描述</th>
				<td colspan="3">${chance.description }</td>
			</tr>
			<tr>
				<th>创建人</th>
				<td>${chance.createBy.name }</td>
				<th>创建时间</th>
				<td>
					<fmt:formatDate value="${chance.createDate }" pattern="yyyy-MM-dd"/>
				</td>
			</tr>
			<tr>
				<th>指派给</th>
				<td>${chance.designee.name }</td>

			</tr>
		</table>

		<br />

		<table class="data_list_table" border="0" cellPadding="3"
			cellSpacing="0">
			<tr>
				<th width="200px">日期</th>
				<th>计划项</th>
			</tr>
			
			<c:if test="${!empty chance.salesPlans }">
				<c:forEach items="${chance.salesPlans }" var="plan">
					<tr id="plan-${plan.id }">
						<td class="list_data_text">
							<fmt:formatDate value="${plan.date }" pattern="yyyy-MM-dd"/>
							&nbsp;
						</td>
						<td class="list_data_ltext">
						  <c:if test="${plan.result == null }">
								<input type="text" size="50"
										value="${plan.todo }" id="todo-${plan.id }"/>
								<input type="hidden"  value="${plan.id }"/>		
								<button class="common_button" id="save-${plan.id }">
										保存
								</button>
								<input type="hidden" value="${plan.id }"/>
								<button class="common_button" id="delete-${plan.id }">
										删除
								</button>
						   </c:if>
						   
							<c:if test="${plan.result != null }">
								<input type="text" size="50"
									value="${plan.todo}" readonly="readonly"/>
								<input type="text" size="50"
									value="${plan.result}" readonly="readonly"/>
							</c:if>		
						</td>
						
					</tr>
				</c:forEach>
			</c:if>
			
		</table>
		
		<div class="button_bar">
			<button class="common_button">
				新建</button>
			
		</div>
		
		<table class="query_form_table" border="0" cellPadding="3"
			cellSpacing="0">
			<tr>
				<th>日期 <br /> (格式: yyyy-mm-dd)
				</th>
				<td><input type="text" name="date" id="date" /> &nbsp;</td>
				<th>计划项</th>
				<td><input type="text" name="todo" size="50" id="todo" />
					&nbsp;</td>
				<td><input type="hidden" name="chance.id" value="${chance.id }" id="hiddenId"/></td>
			</tr>
		</table>
	 </form:form>
	 
</body>
</html>
