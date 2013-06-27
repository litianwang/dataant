<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>任务管理</title>
	
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#dataantgroup_title").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate();
		});
	</script>
</head>

<body>
	<form id="inputForm" action="${ctx}/dataantgroup/${action}" method="post" class="form-horizontal">
		<input type="hidden" name="id" value="${dataantGroup.id}"/>
		<fieldset>
			<legend><small>管理任务</small></legend>
			<div class="control-group">
				<label for="description" class="control-label">ID:</label>
				<div class="controls">
					<textarea id="id" name="id" class="input-large">${dataantGroup.id}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">配置项:</label>
				<div class="controls">
					<textarea id="configs" name="configs" class="input-large">${dataantGroup.configs}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">描述:</label>
				<div class="controls">
					<textarea id="descr" name="descr" class="input-large">${dataantGroup.descr}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">目录:</label>
				<div class="controls">
					<textarea id="directory" name="directory" class="input-large">${dataantGroup.directory}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">创建时间:</label>
				<div class="controls">
					<textarea id="gmtCreate" name="gmtCreate" class="input-large">${dataantGroup.gmtCreate}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">修改时间:</label>
				<div class="controls">
					<textarea id="gmtModified" name="gmtModified" class="input-large">${dataantGroup.gmtModified}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">名称:</label>
				<div class="controls">
					<textarea id="name" name="name" class="input-large">${dataantGroup.name}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">管理员:</label>
				<div class="controls">
					<textarea id="owner" name="owner" class="input-large">${dataantGroup.owner}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">父组:</label>
				<div class="controls">
					<textarea id="parent" name="parent" class="input-large">${dataantGroup.parent}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">资源项:</label>
				<div class="controls">
					<textarea id="resources" name="resources" class="input-large">${dataantGroup.resources}</textarea>
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
