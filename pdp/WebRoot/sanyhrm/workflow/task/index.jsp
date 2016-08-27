<%@ page session="true" language="java"
	contentType="text/html; charset=utf-8"%>
<%@ include file="/sanymbp/common/jsp/taglibs.jsp"%>

 <cm:request setAttribute="pagePath" value="/sanyhrm/workflow/task/index"/>
 <cm:request setAttribute="h1_title" value="人事事务待办"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>三一集团人力资源管理系统:${h1_title}</title>
<%@ include file="/sanymbp/common/jsp/head.jsp"%>
</head>
<body>
	<div data-role="page" class="type-interior">
		<%@ include file="/sanymbp/common/jsp/top.jsp"%>
		<div data-role="content" class="content1">
			<ul data-role="listview">
				<pg:list requestKey="taskList" >
				<li data-role="list-divider"><pg:rowid increament="1"/></li>
				<li ><a data-ajax="false"
					href="${ctx}/sanyhrm/personnel/dimission/index.page?taskId=<pg:cell colName="TASK_ID_"/>">
						<h3>申请人：<pg:cell colName="CREATE_PE_NA"/></h3>
						<p><pg:cell colName="message"/></p>
				</a></li>
				</pg:list>
			</ul>
		</div>
		<%@ include file="/sanymbp/common/jsp/footer.jsp"%>
	</div>
</body>
</html>