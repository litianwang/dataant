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
				<label for="description" class="control-label">ID:</label>
				<div class="controls">
					<textarea id="id" name="id" class="input-large">${dataantJobHistory.id}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">结束时间:</label>
				<div class="controls">
					<textarea id="endTime" name="endTime" class="input-large">${dataantJobHistory.endTime}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">执行服务器:</label>
				<div class="controls">
					<textarea id="executeHost" name="executeHost" class="input-large">${dataantJobHistory.executeHost}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">创建时间:</label>
				<div class="controls">
					<textarea id="gmtCreate" name="gmtCreate" class="input-large">${dataantJobHistory.gmtCreate}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">修改时间:</label>
				<div class="controls">
					<textarea id="gmtModified" name="gmtModified" class="input-large">${dataantJobHistory.gmtModified}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">说明:</label>
				<div class="controls">
					<textarea id="illustrate" name="illustrate" class="input-large">${dataantJobHistory.illustrate}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">作业ID:</label>
				<div class="controls">
					<textarea id="jobId" name="jobId" class="input-large">${dataantJobHistory.jobId}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">日志:</label>
				<div class="controls">
					<textarea id="log" name="log" class="input-large">${dataantJobHistory.log}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">执行人:</label>
				<div class="controls">
					<textarea id="operator" name="operator" class="input-large">${dataantJobHistory.operator}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">属性:</label>
				<div class="controls">
					<textarea id="properties" name="properties" class="input-large">${dataantJobHistory.properties}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">开始时间:</label>
				<div class="controls">
					<textarea id="startTime" name="startTime" class="input-large">${dataantJobHistory.startTime}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">状态:</label>
				<div class="controls">
					<textarea id="status" name="status" class="input-large">${dataantJobHistory.status}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">触发类型:</label>
				<div class="controls">
					<textarea id="triggerType" name="triggerType" class="input-large">${dataantJobHistory.triggerType}</textarea>
				</div>
			</div>	
			<div class="form-actions">
				<input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;	
				<input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
			</div>
		</fieldset>
	</form>
</body>
</html>
