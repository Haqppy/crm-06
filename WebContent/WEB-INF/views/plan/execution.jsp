<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>执行计划</title>
    <script type="text/javascript">
    
    	$(function(){
    		$("button[id ^='saveresult']").click(function(){
    			var id=$(this).next(".hiddenId").val();
    		    var url = "${ctp}/plan/executeWithAjax/"+id;
    		    var result = $(this).prev(".result").val();
    		    var args = {"time2":new Date(),"result":result,"_method":"PUT"};
    		    var $re = $(this);
    		    $.post(url,args,function(data){
    		    	if(data == "1"){
    		    		alert("保存成功!");
    		    		$re.prev(".result").attr("disabled","disabled");
    		    		$re.remove();
    		    	}
    		    	
    		    },"json");
    			return false;
    		});
    	})
    	
    </script>
  </head>

	
  <body class="main">
	<span class="page_title">执行计划</span>
	<div class="button_bar">
		<button class="common_button" onclick="window.location.href='${ctp}/chance/stop/${chance.id }'">终止开发</button>
		<button class="common_button" onclick="window.location.href='${ctp}/plan/make/${chance.id }'">制定计划</button>
		<button class="common_button" onclick="javascript:history.go(-1);">返回</button>			
		<button class="common_button" onclick="window.location.href='${ctp}/chance/finish/${chance.id }'">开发成功</button>
	</div>
  	<form action="${ctp}/plan/execute" method="post">
		<table class="query_form_table" border="0" cellPadding="3" cellSpacing="0">
			<tr>
				<th>编号</th>
				<td>${chance.id }&nbsp;</td>
				
				<th>机会来源</th>
				<td>${chance.source }&nbsp;</td>
			</tr>
			<tr>
				<th>客户名称</th>
				<td>${chance.custName }&nbsp;</td>
				
				<th>成功机率（%）</th>
				<td>${chance.rate }&nbsp;</td>
			</tr>
			
			<tr><th>概要</th>
				<td colspan="3">${chance.title }&nbsp;</td>
			</tr>
			
			<tr>
				<th>联系人</th>
				<td>${chance.contact }&nbsp;</td>
				<th>联系人电话</th>
				<td>${chance.contactTel }&nbsp;</td>
			</tr>
			<tr>
				<th>机会描述</th>
				<td colspan="3">${chance.description }&nbsp;</td>
			</tr>
			<tr>
				<th>创建人</th>
				<td>${chance.createBy.name }&nbsp;</td>
				<th>创建时间</th>
				<td>
					<fmt:formatDate value="${chance.createDate }"/>
				    &nbsp;
				</td>
			</tr>		
			<tr>					
				<th>指派给</th>
				<td>${chance.designee.name }&nbsp;</td>
			</tr>
		</table>
	
	<br />
	
		<table class="data_list_table" border="0" cellPadding="3" cellSpacing="0">
			<tr>
				<th width="200px">日期</th>
				<th>计划</th>
				<th>执行效果</th>
			</tr>
				<c:if test="${! empty chance.salesPlans }">
					<c:forEach items="${chance.salesPlans }" var="plan">
						<tr>
							<c:if test="${plan.result != null }">
								<td class="list_data_text">
									<fmt:formatDate value="${plan.date }" pattern="yyyy-MM-dd"/>
								</td>
								<td class="list_data_ltext">${plan.todo }</td>
								<td>
									<input class="result" id="result-${plan.id }" type="text" value="${plan.result }" size="50" readonly="readonly"/>
								</td>
							</c:if>
							<c:if test="${plan.result == null }">
								<td class="list_data_text">
									<fmt:formatDate value="${plan.date }" pattern="yyyy-MM-dd"/>
								</td>
								<td class="list_data_ltext">${plan.todo }</td>
								<td>
									<input class="result" id="result-${plan.id }" type="text" size="50" />
									<button class="common_button" id="saveresult-${plan.id }">保存</button>
									<input type="hidden" value="${plan.id }" class="hiddenId"/>
								</td>
							</c:if>
						</tr>
					</c:forEach>
				</c:if>
				<c:if test="${empty chance.salesPlans }">
					还没有制定任何计划
				</c:if>
		</table>	
		
  </form>
  </body>
</html>
