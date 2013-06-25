<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>规则管理</title>
	<script>
		$(document).ready(function() {
			$("#firstanalyze-tab").addClass("active");
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
				<label>名称：</label> <input type="text" name="search_LIKE_ruleName" class="input-medium" value="${param.search_LIKE_ruleName}"> 
				<button type="submit" class="btn" id="search_btn">Search</button>
		    </form>
	    </div>
	    <tags:sort/>
	</div>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr><th>规则名称</th>
			<th>状态</th>
			<th>操作</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${firstanalyzes.content}" var="firstanalyze">
			<tr>
				<td><a href="${ctx}/firstanalyze/update/${firstanalyze.id}">${firstanalyze.ruleName}</a></td>
				<td>${firstanalyze.runStatus}</td>
				<td><a href="${ctx}/firstanalyzerulehistory/${firstanalyze.id}">查看日志</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<tags:pagination page="${firstanalyzes}" paginationSize="5"/>
</body>
</html>
