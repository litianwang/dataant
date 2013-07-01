<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>作业日志</title>
	<%@include file="/common/meta.jsp"%>
	<script>
		$(document).ready(function() {
			$("#sch-jobhistory-tab").addClass("active");
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
			<th>结束时间</th>
			<th>执行服务器</th>
			<th>说明</th>
			<th>作业ID</th>
			<th>执行人</th>
			<th>开始时间</th>
			<th>状态</th>
			<th>触发类型</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${dataantJobHistorys.content}" var="JobHistory">
			<tr>
				<td><a href="${ctx}/jobhistory/update/${JobHistory.id}">${JobHistory.id}</a></td>
				<td><a href="${ctx}/jobhistory/update/${JobHistory.id}">${JobHistory.endTime}</a></td>
				<td><a href="${ctx}/jobhistory/update/${JobHistory.id}">${JobHistory.executeHost}</a></td>
				<td><a href="${ctx}/jobhistory/update/${JobHistory.id}">${JobHistory.illustrate}</a></td>
				<td><a href="${ctx}/jobhistory/update/${JobHistory.id}">${JobHistory.jobId}</a></td>
				<td><a href="${ctx}/jobhistory/update/${JobHistory.id}">${JobHistory.operator}</a></td>
				<td><a href="${ctx}/jobhistory/update/${JobHistory.id}">${JobHistory.startTime}</a></td>
				<td><a href="${ctx}/jobhistory/update/${JobHistory.id}">${JobHistory.status}</a></td>
				<td><a href="${ctx}/jobhistory/update/${JobHistory.id}">${JobHistory.triggerType}</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
	<tags:pagination page="${dataantJobHistorys}" paginationSize="5"/>

</body>
</html>
