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
			<th>自动调度</th>
			<th>配置项</th>
			<th>定时表达式</th>
			<th>依赖作业</th>
			<th>描述</th>
			<th>创建时间</th>
			<th>修改时间</th>
			<th>分组ID</th>
			<th>历史记录ID</th>
			<th>名称</th>
			<th>管理员</th>
			<th>后置处理</th>
			<th>前置处理</th>
			<th>READY_DEPENDENCY</th>
			<th>资源项</th>
			<th>任务类型</th>
			<th>调度类型</th>
			<th>脚本</th>
			<th>状态</th>
		<th>管理</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${dataantJobs.content}" var="dataantJob">
			<tr>
				<td><a href="${ctx}/dataantjob/update/${dataantJob.id}">${dataantJob.id}</a></td>
				<td><a href="${ctx}/dataantjob/update/${dataantJob.id}">${dataantJob.auto}</a></td>
				<td><a href="${ctx}/dataantjob/update/${dataantJob.id}">${dataantJob.configs}</a></td>
				<td><a href="${ctx}/dataantjob/update/${dataantJob.id}">${dataantJob.cronExpression}</a></td>
				<td><a href="${ctx}/dataantjob/update/${dataantJob.id}">${dataantJob.dependencies}</a></td>
				<td><a href="${ctx}/dataantjob/update/${dataantJob.id}">${dataantJob.descr}</a></td>
				<td><a href="${ctx}/dataantjob/update/${dataantJob.id}">${dataantJob.gmtCreate}</a></td>
				<td><a href="${ctx}/dataantjob/update/${dataantJob.id}">${dataantJob.gmtModified}</a></td>
				<td><a href="${ctx}/dataantjob/update/${dataantJob.id}">${dataantJob.groupId}</a></td>
				<td><a href="${ctx}/dataantjob/update/${dataantJob.id}">${dataantJob.historyId}</a></td>
				<td><a href="${ctx}/dataantjob/update/${dataantJob.id}">${dataantJob.name}</a></td>
				<td><a href="${ctx}/dataantjob/update/${dataantJob.id}">${dataantJob.owner}</a></td>
				<td><a href="${ctx}/dataantjob/update/${dataantJob.id}">${dataantJob.postProcessers}</a></td>
				<td><a href="${ctx}/dataantjob/update/${dataantJob.id}">${dataantJob.preProcessers}</a></td>
				<td><a href="${ctx}/dataantjob/update/${dataantJob.id}">${dataantJob.readyDependency}</a></td>
				<td><a href="${ctx}/dataantjob/update/${dataantJob.id}">${dataantJob.resources}</a></td>
				<td><a href="${ctx}/dataantjob/update/${dataantJob.id}">${dataantJob.runType}</a></td>
				<td><a href="${ctx}/dataantjob/update/${dataantJob.id}">${dataantJob.scheduleType}</a></td>
				<td><a href="${ctx}/dataantjob/update/${dataantJob.id}">${dataantJob.script}</a></td>
				<td><a href="${ctx}/dataantjob/update/${dataantJob.id}">${dataantJob.status}</a></td>
				<td><a href="${ctx}/dataantjob/delete/${dataantJob.id}">删除</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
	<tags:pagination page="${dataantJobs}" paginationSize="5"/>

	<div><a class="btn" href="${ctx}/dataantjob/create">创建**</a></div>
</body>
</html>
