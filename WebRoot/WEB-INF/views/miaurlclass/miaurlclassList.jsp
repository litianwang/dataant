<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>**管理</title>
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
			<th>类别id</th>
			<th>名称</th>
			<th>时间</th>
		<th>管理</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${miaUrlClasss.content}" var="miaUrlClass">
			<tr>
				<td><a href="${ctx}/miaurlclass/update/${miaUrlClass.id}">${miaUrlClass.id}</a></td>
				<td><a href="${ctx}/miaurlclass/update/${miaUrlClass.id}">${miaUrlClass.name}</a></td>
				<td><a href="${ctx}/miaurlclass/update/${miaUrlClass.id}">${miaUrlClass.insertTime}</a></td>
				<td><a href="${ctx}/miaurlclass/delete/${miaUrlClass.id}">删除</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
	<tags:pagination page="${miaUrlClasss}" paginationSize="5"/>

	<div><a class="btn" href="${ctx}/miaurlclass/create">创建**</a></div>
</body>
</html>
