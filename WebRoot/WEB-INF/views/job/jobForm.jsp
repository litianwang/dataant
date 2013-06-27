<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>任务管理</title>
	
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#dataantjob_title").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate();
		});
	</script>
</head>

<body>
	<form id="inputForm" action="${ctx}/dataantjob/${action}" method="post" class="form-horizontal">
		<input type="hidden" name="id" value="${dataantJob.id}"/>
		<fieldset>
			<legend><small>管理任务</small></legend>
			<div class="control-group">
				<label for="description" class="control-label">ID:</label>
				<div class="controls">
					<textarea id="id" name="id" class="input-large">${dataantJob.id}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">自动调度:</label>
				<div class="controls">
					<textarea id="auto" name="auto" class="input-large">${dataantJob.auto}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">配置项:</label>
				<div class="controls">
					<textarea id="configs" name="configs" class="input-large">${dataantJob.configs}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">定时表达式:</label>
				<div class="controls">
					<textarea id="cronExpression" name="cronExpression" class="input-large">${dataantJob.cronExpression}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">依赖作业:</label>
				<div class="controls">
					<textarea id="dependencies" name="dependencies" class="input-large">${dataantJob.dependencies}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">描述:</label>
				<div class="controls">
					<textarea id="descr" name="descr" class="input-large">${dataantJob.descr}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">创建时间:</label>
				<div class="controls">
					<textarea id="gmtCreate" name="gmtCreate" class="input-large">${dataantJob.gmtCreate}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">修改时间:</label>
				<div class="controls">
					<textarea id="gmtModified" name="gmtModified" class="input-large">${dataantJob.gmtModified}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">分组ID:</label>
				<div class="controls">
					<textarea id="groupId" name="groupId" class="input-large">${dataantJob.groupId}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">历史记录ID:</label>
				<div class="controls">
					<textarea id="historyId" name="historyId" class="input-large">${dataantJob.historyId}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">名称:</label>
				<div class="controls">
					<textarea id="name" name="name" class="input-large">${dataantJob.name}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">管理员:</label>
				<div class="controls">
					<textarea id="owner" name="owner" class="input-large">${dataantJob.owner}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">后置处理:</label>
				<div class="controls">
					<textarea id="postProcessers" name="postProcessers" class="input-large">${dataantJob.postProcessers}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">前置处理:</label>
				<div class="controls">
					<textarea id="preProcessers" name="preProcessers" class="input-large">${dataantJob.preProcessers}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">READY_DEPENDENCY:</label>
				<div class="controls">
					<textarea id="readyDependency" name="readyDependency" class="input-large">${dataantJob.readyDependency}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">资源项:</label>
				<div class="controls">
					<textarea id="resources" name="resources" class="input-large">${dataantJob.resources}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">任务类型:</label>
				<div class="controls">
					<textarea id="runType" name="runType" class="input-large">${dataantJob.runType}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">调度类型:</label>
				<div class="controls">
					<textarea id="scheduleType" name="scheduleType" class="input-large">${dataantJob.scheduleType}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">脚本:</label>
				<div class="controls">
					<textarea id="script" name="script" class="input-large">${dataantJob.script}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">状态:</label>
				<div class="controls">
					<textarea id="status" name="status" class="input-large">${dataantJob.status}</textarea>
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
