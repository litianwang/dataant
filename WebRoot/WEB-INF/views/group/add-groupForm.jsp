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
		        alert(jsonuserinfo);
		        jQuery.ajax( {
		          type : 'POST',
		          contentType : 'application/json',
		          url : '${ctx}/group/${action}',
		          data : jsonuserinfo,
		          dataType : 'json',
		          success : function(data) {
		            alert("新增成功！");
		          },
		          error : function(data) {
		            alert("error");
		          }
		        });
			//$("#inputForm").submit();
			//dialog.close();
		}
	</script>
</head>

<body>
	<form id="inputForm" action="${ctx}/group/${action}" method="post" target="_parent" class="form-horizontal">
		<input type="hidden" id="parent" name="parent" class="input-large" value="${dataantGroup.parent}"/>
		<fieldset>
			<div class="control-group">
				<label for="name" class="control-label">名称:</label>
					<div class="controls">
						<input id="name" name="name" class="input-xlarge" value="${dataantGroup.name}"/>
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
		</fieldset>
	</form>
</body>
</html>
