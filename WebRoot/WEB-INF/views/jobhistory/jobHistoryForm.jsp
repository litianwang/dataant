<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>任务管理</title>
	
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#dataantjobhistory_title").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate();
		});
	</script>
</head>

<body>
	<form id="inputForm" action="${ctx}/dataantjobhistory/${action}" method="post" class="form-horizontal">
		<input type="hidden" name="id" value="${dataantJobHistory.id}"/>
		<fieldset>
			<legend><small>管理任务</small></legend>
			
			<div class="control-group">
				<label for="id" class="control-label">ID:</label>
					<div class="controls">
						<input id="id" name="id" class="input-large" value="${dataantJobHistory.id}"/>
					</div>
			</div>	
			<div class="control-group">
				<label for="endTime" class="control-label">结束时间:</label>
					<div class="controls">
						<input id="endTime" name="endTime" class="input-large" value="${dataantJobHistory.endTime}"/>
					</div>
			</div>	
			<div class="control-group">
				<label for="executeHost" class="control-label">执行服务器:</label>
					<div class="controls">
						<input id="executeHost" name="executeHost" class="input-xlarge" value="${dataantJobHistory.executeHost}"/>
					</div>
			</div>	
			<div class="control-group">
				<label for="gmtCreate" class="control-label">创建时间:</label>
					<div class="controls">
						<input id="gmtCreate" name="gmtCreate" class="input-large" value="${dataantJobHistory.gmtCreate}"/>
					</div>
			</div>	
			<div class="control-group">
				<label for="gmtModified" class="control-label">修改时间:</label>
					<div class="controls">
						<input id="gmtModified" name="gmtModified" class="input-large" value="${dataantJobHistory.gmtModified}"/>
					</div>
			</div>	
			<div class="control-group">
				<label for="illustrate" class="control-label">说明:</label>
					<div class="controls">
						<input id="illustrate" name="illustrate" class="input-xlarge" value="${dataantJobHistory.illustrate}"/>
					</div>
			</div>	
			<div class="control-group">
				<label for="jobId" class="control-label">作业ID:</label>
					<div class="controls">
						<input id="jobId" name="jobId" class="input-large" value="${dataantJobHistory.jobId}"/>
					</div>
			</div>	
			<div class="control-group">
				<label for="log" class="control-label">日志:</label>
					<div class="controls">
						<textarea id="log" name="log" rows="12" class="input-xxlarge">${dataantJobHistory.log}</textarea>
					</div>
			</div>	
			<div class="control-group">
				<label for="operator" class="control-label">执行人:</label>
					<div class="controls">
						<input id="operator" name="operator" class="input-xlarge" value="${dataantJobHistory.operator}"/>
					</div>
			</div>	
			<div class="control-group">
				<label for="properties" class="control-label">属性:</label>
					<div class="controls">
						<input id="properties" name="properties" class="input-xlarge" value="${dataantJobHistory.properties}"/>
					</div>
			</div>	
			<div class="control-group">
				<label for="startTime" class="control-label">开始时间:</label>
					<div class="controls">
						<input id="startTime" name="startTime" class="input-large" value="${dataantJobHistory.startTime}"/>
					</div>
			</div>	
			<div class="control-group">
				<label for="status" class="control-label">状态:</label>
					<div class="controls">
						<input id="status" name="status" class="input-xlarge" value="${dataantJobHistory.status}"/>
					</div>
			</div>	
			<div class="control-group">
				<label for="triggerType" class="control-label">触发类型:</label>
					<div class="controls">
						<input id="triggerType" name="triggerType" class="input-large" value="${dataantJobHistory.triggerType}"/>
					</div>
			</div>	
			<div class="form-actions">
				<input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
			</div>
		</fieldset>
	</form>
</body>
</html>
