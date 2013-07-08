<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>任务日志</title>
	<%@include file="/common/meta.jsp"%>
	<c:if test="${jobHistory.status eq 'running'}">
		<%--定时刷新页面 --%>
		<meta http-equiv="refresh"content="5;url=${ctx}/jobhistory/log/${jobHistory.jobId}/${jobHistory.id}/">
	</c:if>
	<script>
		$(document).ready(function() {
		});
	</script>
</head>

<body>
	<form id="inputForm" method="get" class="form-horizontal">
		<fieldset>
				<div class="span10">
					<textarea id="log" name="log" rows="16" class="span10"  onfocus="this.scrollTop = this.scrollHeight">${jobHistory.log}</textarea>
				</div>
		</fieldset>
	</form>
</body>
</html>
