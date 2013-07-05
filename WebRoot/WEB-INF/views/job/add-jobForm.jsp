<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>任务管理</title>
	<%@include file="/common/meta.jsp"%>
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#name").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate();
		});
		$.fn.serializeObject = function() {
			  var o = {};
			  var a = this.serializeArray();
			  $.each(a, function() {
			    if (o[this.name]) {
			      if (!o[this.name].push) {
			        o[this.name] = [ o[this.name] ];
			      }
			      o[this.name].push(this.value || '');
			    } else {
			      o[this.name] = this.value || '';
			    }
			  });
			  return o;
			};
		function submitForm(dialog){
			 var jsonuserinfo = $.toJSON($('#inputForm').serializeObject());
		        jQuery.ajax( {
		          type : 'POST',
		          contentType : 'application/json',
		          url : '${ctx}/job/${action}',
		          data : jsonuserinfo,
		          dataType : 'json',
		          success : function(response) {
			          if(response.success){
			            alert("新增成功！");
				        parent.parent.reloadTree();
			            dialog.close();
			          }
		          },
		          error : function(response) {
		            alert("error");
		          }
		        });
			//$("#inputForm").submit();
			//dialog.close();
		}
	</script>
</head>

<body>
	<form id="inputForm" action="${ctx}/job/${action}" method="post" target="_parent" class="form-horizontal">
		<input type="hidden" id="groupId" name="groupId" value="${job.groupId}"/>
		<fieldset>
			<div class="control-group">
				<label for="name" class="control-label">任务名称:</label>
					<div class="controls">
						<input id="name" name="name" class="input-xlarge"/>
					</div>
			</div>	
			<div class="control-group">
				<label for="runType" class="control-label">任务类型:</label>
					<div class="controls">
						<select class="input-xlarge" id="runType" name="runType">
							<option value=""></option>
							<option value="hive">hive 脚本</option>
							<option  value="shell">shell 脚本</option>
							<option  value="main">MapReduce 程序</option>
						</select>
					</div>
			</div>	
		</fieldset>
	</form>
</body>
</html>
