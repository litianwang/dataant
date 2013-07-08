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


		/**
		*查看日志
		*/
        function view_log(jobId, historyId)
        {
            $.ligerDialog.open({ url: '${ctx}/jobhistory/log/' + jobId + "/" + historyId, 
            	title:'任务ID：' + jobId + " 日志ID：" + historyId,
            	showMax:true,
            	isHidden:false,
                height: 500, 
                width: 960, 
                buttons: [
                          { text: '关闭', 
                            onclick: function (item, dialog) { dialog.close(); } }
                       ],
                isResize: true
            });
        }
	</script>
</head>

<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<div class="row">
		<div class="span8 offset3">
			<form class="form-search" action="#">
				<label>名称：</label> <input type="text" name="search_LIKE_title" class="input-medium" value="${param.search_LIKE_title}"> 
				<button type="submit" class="btn" id="search_btn">Search</button>
				<a class="btn" href="${ctx}/job/update/${param.search_EQ_jobId}">返回</a>
		    </form>
	    </div>
	    <tags:sort/>
	</div>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
			<th>ID</th>
			<th>开始时间</th>
			<th>结束时间</th>
			<th>执行服务器</th>
			<th>说明</th>
			<th>作业ID</th>
			<th>执行人</th>
			<th>状态</th>
			<th>触发类型</th>
			<th>操作</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${dataantJobHistorys.content}" var="JobHistory">
			<tr>
				<td><a href="${ctx}/jobhistory/update/${JobHistory.id}">${JobHistory.id}</a></td>
				<td>${JobHistory.startTime}</td>
				<td>${JobHistory.endTime}</td>
				<td>${JobHistory.executeHost}</td>
				<td>${JobHistory.illustrate}</td>
				<td>${JobHistory.jobId}</td>
				<td>${JobHistory.operator}</td>
				<td>${JobHistory.status}</td>
				<td>${JobHistory.triggerType}</td>
				<td>
					<a href="#" onclick="view_log('${JobHistory.jobId}','${JobHistory.id}')">查看日志</a>
					<c:if test="${JobHistory.status eq 'running'}">
						<a href="${ctx}/jobhistory/cancel/${JobHistory.jobId}/${JobHistory.id}/">取消</a>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
	<tags:pagination page="${dataantJobHistorys}" paginationSize="5"/>

</body>
</html>
