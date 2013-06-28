<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div id="leftbar" class="span2">
	<h1>一次分析</h1>
	<div class="submenu">
		<a id="firstanalyze-tab"href="${ctx}/firstanalyze/">规则管理</a>
	</div>
	<h1>详单查询</h1>
	<div class="submenu">
		<a id="searchurl-tab" href="${ctx}/searchurl">URL详单查询</a>
		<a id="searchapp-tab"href="${ctx}/searchapp">APP详单查询</a>
	</div>
	<h1>调度中心</h1>
	<div class="submenu">
		<a id="sch-group-tab"href="${ctx}/group">分组管理</a>
		<a id="sch-job-tab"href="${ctx}/job">作业管理</a>
		<a id="sch-jobhistory-tab"href="${ctx}/jobhistory">作业日志</a>
	</div>
	<h1>UI组件参考</h1>
	<div class="submenu">
		<a id="bootstrap-tab" target="_blank" href="http://www.bootcss.com/">Bootstrap</a>
		<a id="ligerui-tab" target="_blank" href="http://www.ligerui.com/">LigerUI</a>
		
	</div>
</div>