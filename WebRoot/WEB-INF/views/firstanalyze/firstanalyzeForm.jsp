<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>任务管理</title>
	
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#firstanalyze_title").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate();
		});
	</script>
</head>

<body>
	<form id="inputForm" action="${ctx}/firstanalyze/${action}" method="post" class="form-horizontal">
		<input type="hidden" name="id" value="${firstanalyze.id}"/>
		<fieldset>
			<legend><small>管理任务</small></legend>
			<div class="control-group">
				<label for="ruleName" class="control-label">规则名称:</label>
				<div class="controls">
					<input type="text" id="ruleName" name="ruleName"  value="${firstanalyze.ruleName}" class="input-large required" minlength="3"/>
				</div>
			</div>	
			<div class="control-group">
				<label for="ruleCode" class="control-label">规则编码:</label>
				<div class="controls">
					<input type="text" id="ruleCode" name="ruleCode"  value="${firstanalyze.ruleCode}" class="input-large required" minlength="3"/>
				</div>
			</div>		
			<div class="control-group">
				<label for="crontab" class="control-label">执行周期:</label>
				<div class="controls">
					<input type="text" id="crontab" name="crontab"  value="${firstanalyze.cron}" class="input-large required" minlength="3"/>
				</div>
			</div>	
			<div class="control-group">
				<label for="status" class="control-label">状态:</label>
				<div class="controls">
					<select class="span3" id="runStatus" name="runStatus" value="${firstanalyze.runStatus}" >
						<option ${firstanalyze.runStatus eq '2'? 'selected':''} value="0">无效</option>
						<option ${firstanalyze.runStatus eq '1'? 'selected':''} value="1">有效</option>
						<option ${firstanalyze.runStatus eq '3'? 'selected':''} value="2">运行中</option>
					</select>
				</div>
			</div>	
			<div class="control-group">
				<label for="loadInfo" class="control-label">备注:</label>
				<div class="controls">
					<textarea id="remarks" name="remarks" rows="12" class="input-xxlarge">${firstanalyze.remarks}</textarea>
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
