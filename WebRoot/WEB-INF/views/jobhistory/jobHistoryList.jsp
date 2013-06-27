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
			<th>ID</th>
			<th>结束时间</th>
			<th>执行服务器</th>
			<th>创建时间</th>
			<th>修改时间</th>
			<th>说明</th>
			<th>作业ID</th>
			<th>日志</th>
			<th>执行人</th>
			<th>属性</th>
			<th>开始时间</th>
			<th>状态</th>
			<th>触发类型</th>
		<th>管理</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${dataantJobHistorys.content}" var="dataantJobHistory">
			<tr>
				<td><a href="${ctx}/dataantjobhistory/update/${dataantJobHistory.id}">${dataantJobHistory.id}</a></td>
				<td><a href="${ctx}/dataantjobhistory/update/${dataantJobHistory.id}">${dataantJobHistory.endTime}</a></td>
				<td><a href="${ctx}/dataantjobhistory/update/${dataantJobHistory.id}">${dataantJobHistory.executeHost}</a></td>
				<td><a href="${ctx}/dataantjobhistory/update/${dataantJobHistory.id}">${dataantJobHistory.gmtCreate}</a></td>
				<td><a href="${ctx}/dataantjobhistory/update/${dataantJobHistory.id}">${dataantJobHistory.gmtModified}</a></td>
				<td><a href="${ctx}/dataantjobhistory/update/${dataantJobHistory.id}">${dataantJobHistory.illustrate}</a></td>
				<td><a href="${ctx}/dataantjobhistory/update/${dataantJobHistory.id}">${dataantJobHistory.jobId}</a></td>
				<td><a href="${ctx}/dataantjobhistory/update/${dataantJobHistory.id}">${dataantJobHistory.log}</a></td>
				<td><a href="${ctx}/dataantjobhistory/update/${dataantJobHistory.id}">${dataantJobHistory.operator}</a></td>
				<td><a href="${ctx}/dataantjobhistory/update/${dataantJobHistory.id}">${dataantJobHistory.properties}</a></td>
				<td><a href="${ctx}/dataantjobhistory/update/${dataantJobHistory.id}">${dataantJobHistory.startTime}</a></td>
				<td><a href="${ctx}/dataantjobhistory/update/${dataantJobHistory.id}">${dataantJobHistory.status}</a></td>
				<td><a href="${ctx}/dataantjobhistory/update/${dataantJobHistory.id}">${dataantJobHistory.triggerType}</a></td>
				<td><a href="${ctx}/dataantjobhistory/delete/${dataantJobHistory.id}">删除</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
	<tags:pagination page="${dataantJobHistorys}" paginationSize="5"/>

	<div><a class="btn" href="${ctx}/dataantjobhistory/create">创建**</a></div>
</body>
</html>
