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
				<label>运行日期：</label> <input type="text" name="search_LIKE_runDate" class="input-medium" value="${param.search_LIKE_runDate}"> 
				<button type="submit" class="btn" id="search_btn">Search</button>
		    </form>
	    </div>
	    <tags:sort/>
	</div>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
			<th>历史记录ID</th>
			<th>规则ID</th>
			<th>规则编码</th>
			<th>运行日期</th>
			<th>序列号</th>
			<th>开始时间</th>
			<th>结束时间</th>
			<th>状态</th>
			<th>APP结果数量</th>
			<th>URL结果数量</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${firstAnalyzeRuleHistorys.content}" var="firstAnalyzeRuleHistory">
			<tr>
				<td><a href="${ctx}/firstanalyzerulehistory/update/${firstAnalyzeRuleHistory.id}">${firstAnalyzeRuleHistory.id}</a></td>
				<td><a href="${ctx}/firstanalyzerulehistory/update/${firstAnalyzeRuleHistory.id}">${firstAnalyzeRuleHistory.ruleId}</a></td>
				<td><a href="${ctx}/firstanalyzerulehistory/update/${firstAnalyzeRuleHistory.id}">${firstAnalyzeRuleHistory.ruleCode}</a></td>
				<td><a href="${ctx}/firstanalyzerulehistory/update/${firstAnalyzeRuleHistory.id}">${firstAnalyzeRuleHistory.runDate}</a></td>
				<td><a href="${ctx}/firstanalyzerulehistory/update/${firstAnalyzeRuleHistory.id}">${firstAnalyzeRuleHistory.seq}</a></td>
				<td><a href="${ctx}/firstanalyzerulehistory/update/${firstAnalyzeRuleHistory.id}">${firstAnalyzeRuleHistory.startTime}</a></td>
				<td><a href="${ctx}/firstanalyzerulehistory/update/${firstAnalyzeRuleHistory.id}">${firstAnalyzeRuleHistory.endTime}</a></td>
				<td><a href="${ctx}/firstanalyzerulehistory/update/${firstAnalyzeRuleHistory.id}">${firstAnalyzeRuleHistory.status}</a></td>
				<td><a href="${ctx}/firstanalyzerulehistory/update/${firstAnalyzeRuleHistory.id}">${firstAnalyzeRuleHistory.appNum}</a></td>
				<td><a href="${ctx}/firstanalyzerulehistory/update/${firstAnalyzeRuleHistory.id}">${firstAnalyzeRuleHistory.urlNum}</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<tags:pagination page="${firstAnalyzeRuleHistorys}" paginationSize="10"/>
</body>
</html>
