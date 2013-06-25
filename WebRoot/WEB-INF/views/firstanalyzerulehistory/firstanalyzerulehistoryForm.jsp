<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>任务管理</title>
	
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#firstanalyzerulehistory_title").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate();
		});
	</script>
</head>

<body>
	<form id="inputForm" action="${ctx}/firstanalyzerulehistory/${action}" method="post" class="form-horizontal">
		<input type="hidden" name="id" value="${firstAnalyzeRuleHistory.id}"/>
		<fieldset>
			<legend><small>管理任务</small></legend>
			<div class="control-group">
				<label for="description" class="control-label">历史记录ID:</label>
				<div class="controls">
					<textarea id="id" name="id" class="input-large">${firstAnalyzeRuleHistory.id}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">规则ID:</label>
				<div class="controls">
					<textarea id="ruleId" name="ruleId" class="input-large">${firstAnalyzeRuleHistory.ruleId}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">规则编码:</label>
				<div class="controls">
					<textarea id="ruleCode" name="ruleCode" class="input-large">${firstAnalyzeRuleHistory.ruleCode}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">运行日期YYYYMMDD:</label>
				<div class="controls">
					<textarea id="runDate" name="runDate" class="input-large">${firstAnalyzeRuleHistory.runDate}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">序列号4位:</label>
				<div class="controls">
					<textarea id="seq" name="seq" class="input-large">${firstAnalyzeRuleHistory.seq}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">开始时间:</label>
				<div class="controls">
					<textarea id="startTime" name="startTime" class="input-large">${firstAnalyzeRuleHistory.startTime}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">结束时间:</label>
				<div class="controls">
					<textarea id="endTime" name="endTime" class="input-large">${firstAnalyzeRuleHistory.endTime}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">状态:</label>
				<div class="controls">
					<textarea id="status" name="status" class="input-large">${firstAnalyzeRuleHistory.status}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">触发方式:</label>
				<div class="controls">
					<textarea id="triggerType" name="triggerType" class="input-large">${firstAnalyzeRuleHistory.triggerType}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">工作路径:</label>
				<div class="controls">
					<textarea id="workDir" name="workDir" class="input-large">${firstAnalyzeRuleHistory.workDir}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">结果路径:</label>
				<div class="controls">
					<textarea id="resultDir" name="resultDir" class="input-large">${firstAnalyzeRuleHistory.resultDir}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">APP结果数量:</label>
				<div class="controls">
					<textarea id="appNum" name="appNum" class="input-large">${firstAnalyzeRuleHistory.appNum}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">URL结果数量:</label>
				<div class="controls">
					<textarea id="urlNum" name="urlNum" class="input-large">${firstAnalyzeRuleHistory.urlNum}</textarea>
				</div>
			</div>	
			<div class="form-actions">
				<input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
			</div>
		</fieldset>
	</form>
</body>
</html>
