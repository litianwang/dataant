<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>组管理</title>
	<%@include file="/common/meta.jsp"%>
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#dataantgroup_title").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate();
			alert('${treeJson}');
		});
        function f_open2()
        {
            $.ligerDialog.open({ url: '${ctx}/group/add/parent/${dataantGroup.id}', 
            	title:'添加组',
                height: 300, 
                width: 500, 
                buttons: [
                          { text: '确定', onclick: function (item, dialog) {
                              	// 
								dialog.frame.submitForm(dialog);
                              }
                          },
                          { text: '取消', onclick: function (item, dialog) { dialog.close(); } }
                       ],
                isResize: true
            });
        }
	</script>
</head>

<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" action="${ctx}/group/${action}" method="post" class="form-horizontal">
		<fieldset>
			<div class="form-actions">
				<input id="submit_btn" class="btn btn-primary" type="submit" value="保存"/>&nbsp;
				<c:if test="${!(empty dataantGroup.id)}">
					<c:if test="${dataantGroup.directory eq '0'}">
						<a class="btn btn-success" href="#" onclick="f_open2()"><i class="icon-folder-close icon-white"></i>添加组</a>&nbsp;	
					</c:if>
					<c:if test="${dataantGroup.directory eq '1'}">
						<a class="btn btn-success" href="${ctx}/job/create/group/${dataantGroup.id}"><i class="icon-file icon-white"></i>添加任务</a>&nbsp;	
					</c:if>
				</c:if>
				<input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
			</div>
			<div class="control-group">
				<label for="id" class="control-label">ID:</label>
					<div class="controls">
						<input id="id" name="id" class="input-large" value="${dataantGroup.id}"/>
					</div>
			</div>	
			<div class="control-group">
				<label for="configs" class="control-label">配置项:</label>
					<div class="controls">
						<input id="configs" name="configs" class="input-xlarge" value="${dataantGroup.configs}"/>
					</div>
			</div>	
			<div class="control-group">
				<label for="descr" class="control-label">描述:</label>
					<div class="controls">
						<input id="descr" name="descr" class="input-xlarge" value="${dataantGroup.descr}"/>
					</div>
			</div>	
			<div class="control-group">
				<label for="directory" class="control-label">目录:</label>
					<div class="controls">
						<input id="directory" name="directory" class="input-large" value="${dataantGroup.directory}"/>
					</div>
			</div>	
			<div class="control-group">
				<label for="gmtCreate" class="control-label">创建时间:</label>
					<div class="controls">
						<input id="gmtCreate" name="gmtCreate" class="input-large" value="${dataantGroup.gmtCreate}"/>
					</div>
			</div>	
			<div class="control-group">
				<label for="gmtModified" class="control-label">修改时间:</label>
					<div class="controls">
						<input id="gmtModified" name="gmtModified" class="input-large" value="${dataantGroup.gmtModified}"/>
					</div>
			</div>	
			<div class="control-group">
				<label for="name" class="control-label">名称:</label>
					<div class="controls">
						<input id="name" name="name" class="input-xlarge" value="${dataantGroup.name}"/>
					</div>
			</div>	
			<div class="control-group">
				<label for="owner" class="control-label">管理员:</label>
					<div class="controls">
						<input id="owner" name="owner" class="input-xlarge" value="${dataantGroup.owner}"/>
					</div>
			</div>	
			<div class="control-group">
				<label for="parent" class="control-label">父组:</label>
					<div class="controls">
						<input id="parent" name="parent" class="input-large" value="${dataantGroup.parent}"/>
					</div>
			</div>	
			<div class="control-group">
				<label for="resources" class="control-label">资源项:</label>
					<div class="controls">
						<input id="resources" name="resources" class="input-xlarge" value="${dataantGroup.resources}"/>
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
