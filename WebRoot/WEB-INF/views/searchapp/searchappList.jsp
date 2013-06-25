<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>APP详单查询管理</title>
	<script>
		$(document).ready(function() {
			$("#searchapp-tab").addClass("active");
		});
	</script>
</head>

<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<div class="row">
		<div class="span4 offset7">
			<form class="form-search" action="#">
				<label>名称：</label> <input type="text" name="search_LIKE_title" class="input-medium" value="${param.search_LIKE_title}"> 
				<button type="submit" class="btn" id="search_btn">Search</button>
		    </form>
	    </div>
	    <tags:sort/>
	</div>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
			<th>序号</th>
			<th>手机号码</th>
			<th>状态</th>
			<th>插入时间</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${searchApps.content}" var="searchApp">
			<tr>
				<td><a href="${ctx}/searchapp/update/${searchApp.id}">${searchApp.id}</a></td>
				<td><a href="${ctx}/searchapp/update/${searchApp.id}">${searchApp.userNo}</a></td>
				<td><a href="${ctx}/searchapp/update/${searchApp.id}">${searchApp.status}</a></td>
				<td><a href="${ctx}/searchapp/update/${searchApp.id}">${searchApp.insertTime}</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
	<tags:pagination page="${searchApps}" paginationSize="5"/>
</body>
</html>
