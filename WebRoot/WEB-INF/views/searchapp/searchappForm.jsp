<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>任务管理</title>
	
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#searchapp_title").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate();
		});
	</script>
</head>

<body>
	<form id="inputForm" action="${ctx}/searchapp/${action}" method="post" class="form-horizontal">
		<input type="hidden" name="id" value="${searchApp.id}"/>
		<fieldset>
			<legend><small>管理任务</small></legend>
			<div class="control-group">
				<label for="description" class="control-label">序号:</label>
				<div class="controls">
					<textarea id="id" name="id" class="input-large">${searchApp.id}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">手机号码:</label>
				<div class="controls">
					<textarea id="userNo" name="userNo" class="input-large">${searchApp.userNo}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">小类业务ID:</label>
				<div class="controls">
					<textarea id="appId" name="appId" class="input-large">${searchApp.appId}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">分区（YYYYMMDD）:</label>
				<div class="controls">
					<textarea id="partitionId" name="partitionId" class="input-large">${searchApp.partitionId}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">输出最大记录数:</label>
				<div class="controls">
					<textarea id="maxCnt" name="maxCnt" class="input-large">${searchApp.maxCnt}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">状态[1:待运行,2:运行中,3:结束,4:无效]:</label>
				<div class="controls">
					<textarea id="status" name="status" class="input-large">${searchApp.status}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">插入时间:</label>
				<div class="controls">
					<textarea id="insertTime" name="insertTime" class="input-large">${searchApp.insertTime}</textarea>
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
