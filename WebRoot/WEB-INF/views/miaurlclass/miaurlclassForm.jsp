<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>任务管理</title>
	
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#miaurlclass_title").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate();
		});
	</script>
</head>

<body>
	<form id="inputForm" action="${ctx}/miaurlclass/${action}" method="post" class="form-horizontal">
		<input type="hidden" name="id" value="${miaUrlClass.id}"/>
		<fieldset>
			<legend><small>管理任务</small></legend>
			<div class="control-group">
				<label for="description" class="control-label">类别id:</label>
				<div class="controls">
					<textarea id="id" name="id" class="input-large">${miaUrlClass.id}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">名称:</label>
				<div class="controls">
					<textarea id="name" name="name" class="input-large">${miaUrlClass.name}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">时间:</label>
				<div class="controls">
					<textarea id="insertTime" name="insertTime" class="input-large">${miaUrlClass.insertTime}</textarea>
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
