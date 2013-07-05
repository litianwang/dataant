<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>分组管理</title>
	
	<script>
		$(document).ready(function() {
			$("#sch-group-tab").addClass("active");
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
			<th>ID</th>
			<th>目录</th>
			<th>创建时间</th>
			<th>修改时间</th>
			<th>名称</th>
			<th>管理员</th>
			<th>父组</th>
		<th>管理</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${dataantGroups.content}" var="dataantGroup">
			<tr>
				<td><a href="${ctx}/group/update/${dataantGroup.id}">${dataantGroup.id}</a></td>
				<td><a href="${ctx}/group/update/${dataantGroup.id}">${dataantGroup.directory}</a></td>
				<td><a href="${ctx}/group/update/${dataantGroup.id}">${dataantGroup.gmtCreate}</a></td>
				<td><a href="${ctx}/group/update/${dataantGroup.id}">${dataantGroup.gmtModified}</a></td>
				<td><a href="${ctx}/group/update/${dataantGroup.id}">${dataantGroup.name}</a></td>
				<td><a href="${ctx}/group/update/${dataantGroup.id}">${dataantGroup.owner}</a></td>
				<td><a href="${ctx}/group/update/${dataantGroup.id}">${dataantGroup.parent}</a></td>
				<td><a href="${ctx}/group/delete/${dataantGroup.id}">删除</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
	<tags:pagination page="${dataantGroups}" paginationSize="5"/>

	<div><a class="btn" href="${ctx}/group/create">创建**</a></div>
</body>
</html>
