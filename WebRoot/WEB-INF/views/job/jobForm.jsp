<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>作业管理</title>
	<%@include file="/common/meta.jsp"%>
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#job_title").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate();
			setTimeout("$('.alert').alert('close')",10000);
		});
	</script>
</head>

<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" action="${ctx}/job/${action}" method="post" class="form-horizontal">
		<input type="hidden" id="gmtCreate" name="gmtCreate" class="input-large" value="${job.gmtCreate}"/>
		<input type="hidden" id="gmtModified" name="gmtModified" class="input-large" value="${job.gmtModified}"/>
		<input type="hidden" id="groupId" name="groupId" class="input-large" value="${job.groupId}"/>
		<input type="hidden" id="dependencies" name="dependencies" class="input-xlarge" value="${job.dependencies}"/>
		<input type="hidden" id="historyId" name="historyId" class="input-large" value="${job.historyId}"/>
		<input type="hidden" id="preProcessers" name="preProcessers" class="input-xlarge" value="${job.preProcessers}"/>
		<input type="hidden" id="postProcessers" name="postProcessers" class="input-xlarge" value="${job.postProcessers}"/>
		<input type="hidden" id="readyDependency" name="readyDependency" class="input-xlarge" value="${job.readyDependency}"/>
		<input type="hidden" id="status" name="status" class="input-xlarge" value="${job.status}"/>
		<input type="hidden" id="auto" name="auto" readonly="readonly" class="input-large" value="${job.auto}"/>
		<fieldset>
			<div class="form-actions">
				<input id="submit_btn" class="btn btn-primary" type="submit" value="保存"/>&nbsp;
				<a class="btn btn-success" href="${ctx}/job/run/${job.id}/1"><i class="icon-play icon-white"></i>手动运行</a>&nbsp;	
				<a class="btn btn-success" href="${ctx}/job/run/${job.id}/2"><i class="icon-repeat icon-white"></i>手动恢复</a>&nbsp;	
				<a class="btn btn-danger" href="${ctx}/job/swtich/${job.id}/${job.auto eq '0'?true:false}"><i class="icon-adjust icon-white"></i>开启/关闭</a>&nbsp;
				<a class="btn btn-success" href="${ctx}/jobhistory?search_EQ_jobId=${job.id}"><i class="icon-info-sign icon-white"></i>运行日志</a>&nbsp;		
			</div>
			<div class="control-group">
				<label for="description" class="control-label">ID:</label>
				<div class="controls">
					<input id="id" name="id" class="input-large" readonly="readonly" value="${job.id}"/>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">名称:</label>
				<div class="controls">
					<input id="name" name="name" class="input-xlarge" value="${job.name}"/>
				</div>
			</div>		
			<div class="control-group">
				<label for="description" class="control-label">管理员:</label>
				<div class="controls">
					<input id="owner" name="owner" readonly="readonly" class="input-xlarge" value="${job.owner}"/>
				</div>
			</div>		
			<div class="control-group">
				<label for="description" class="control-label">任务类型:</label>
				<div class="controls">
					<input id="runType" name="runType" class="input-xlarge" value="${job.runType}"/>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">调度类型:</label>
				<div class="controls">
					<select class="input-xlarge" id="scheduleType" name="scheduleType" >
						<option ${job.scheduleType eq '0'? selected :''} value="0">定时调度</option>
						<option ${job.scheduleType eq '1'? selected :''} value="1">依赖调度</option>
					</select>
				</div>
			</div>		
			<div class="control-group">
				<label for="description" class="control-label">自动调度:</label>
				<div class="controls">
					<input readonly="readonly" class="input-large" value="${job.auto eq '1'?'开启':'关闭'}"/>
				</div>
			</div>
			<div class="control-group">
				<label for="description" class="control-label">定时表达式:</label>
				<div class="controls">
					<input id="cronExpression" name="cronExpression" class="input-xlarge" value="${job.cronExpression}"/>
				</div>
			</div>
			<div class="control-group">
				<label for="description" class="control-label">描述:</label>
				<div class="controls">
					<textarea id="descr" name="descr" class="input-xxlarge">${job.descr}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">配置项:</label>
				<div class="controls">
					<textarea id="configs" name="configs" class="input-xxlarge">${job.configs}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">脚本:</label>
				<div class="controls">
					<textarea id="script" name="script" class="input-xxlarge">${job.script}</textarea>
				</div>
			</div>	
			<div class="control-group">
				<label for="description" class="control-label">资源项:</label>
				<div class="controls">
					<textarea id="resources" name="resources" class="input-xxlarge">${job.resources}</textarea>
				</div>
			</div>
		</fieldset>
	</form>
</body>
</html>
