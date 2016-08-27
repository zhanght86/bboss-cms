<%@ page session="true" language="java"
	contentType="text/html; charset=utf-8"%>
<%@ include file="/sanymbp/common/jsp/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>三一集团人力资源管理系统</title>
 
<cm:request setAttribute="pagePath" value="/sanyhrm/index"/>
<%@ include file="/sanymbp/common/jsp/head.jsp"%>
</head>
<body>
	<div data-role="page" class="type-interior">
		<div data-role="header" data-position="fixed" data-theme="f"
			class="header1"></div>
		<div data-role="content" class="content1">
			<a data-ajax="false" href="${ctx}/sanyhrm/workflow/task/index.page"
				class="a1"> <span>${personnelCount}</span>
			</a>
		</div>
		<%@ include file="/sanymbp/common/jsp/footer.jsp"%>
	</div>
</body>
</html>