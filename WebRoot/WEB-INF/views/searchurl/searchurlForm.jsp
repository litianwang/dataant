<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>任务管理</title>
	
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#searchurl_title").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate();
		});
	</script>
</head>

<body>
	<form id="inputForm" action="${ctx}/searchurl/${action}" method="post" class="form-horizontal">
		<input type="hidden" name="id" value="${searchUrl.id}"/>
		<fieldset>
			<legend><small>管理任务</small></legend>
			<div class="control-group">
				<label for="description" class="control-label">序号:</label>
				<div class="controls">
					<textarea id="id" name="id" class="input-large">${searchUrl.id}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">手机号码:</label>
				<div class="controls">
					<textarea id="userNo" name="userNo" class="input-large">${searchUrl.userNo}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">HOST = A:</label>
				<div class="controls">
					<textarea id="hostA" name="hostA" class="input-large">${searchUrl.hostA}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">URI = C:</label>
				<div class="controls">
					<textarea id="uriC" name="uriC" class="input-large">${searchUrl.uriC}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">一级分类:</label>
				<div class="controls">
					<textarea id="urlClassId" name="urlClassId" class="input-large">${searchUrl.urlClassId}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">二级分类:</label>
				<div class="controls">
					<textarea id="urlInterestId" name="urlInterestId" class="input-large">${searchUrl.urlInterestId}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">分区（YYYYMMDD）:</label>
				<div class="controls">
					<textarea id="partitionId" name="partitionId" class="input-large">${searchUrl.partitionId}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">输出最大记录数:</label>
				<div class="controls">
					<textarea id="maxCnt" name="maxCnt" class="input-large">${searchUrl.maxCnt}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">[1:待运行,2:运行中,3:结束,4:无效]:</label>
				<div class="controls">
					<textarea id="status" name="status" class="input-large">${searchUrl.status}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">插入时间:</label>
				<div class="controls">
					<textarea id="insertTime" name="insertTime" class="input-large">${searchUrl.insertTime}</textarea>
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
