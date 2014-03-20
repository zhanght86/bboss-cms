<%@ page session="true" language="java"
	contentType="text/html; charset=utf-8"%>
<%@ include file="/sanymbp/common/jsp/taglibs.jsp"%>
<%@page import="com.frameworkset.platform.framework.ItemQueue"%>
<%@page import="com.frameworkset.platform.framework.Item"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>移动应用平台</title>
<cm:request setAttribute="pagePath" value="/sanymbp/index"/>
<%@ include file="/sanymbp/common/jsp/head.jsp"%>
</head>
<body
	onload="getPending('<%=AccessControl.getAccessControl().getUserAccount() %>','${ctx}');">
	<div data-role="page" data-theme="c">
		<h1 class="logo">
			<img src="${ctx}/sanymbp/common/images/logo.jpg" class="logo"><span
				class="logotext">移动应用平台</span>
		</h1>
		<div data-role="content">
			<ul class="gallery-entries">
				<%
					ItemQueue subitems = (ItemQueue) request.getAttribute("itemQueue");
					for (int j = 0; subitems != null && j < subitems.size(); j++) {
						Item item = subitems.getItem(j);
						String rolesid = item.getId();
				%>
				<li class="gallery-item"><a data-ajax="false" 
					href="${ctx}/<%=item.getWorkspaceContent() %>"><img
						src="<%=item.getHeadimg()%>"><div id="pend<%=item.getId()%>"
								class="to_do" style="display: none;">
								<img src="${ctx}/sanymbp/common/images/circle.png"><div
										id="num<%=item.getId()%>" class="num_box">0</div>
							</div>
						<h3><%=item.getName()%></h3></a></li>

				<%
					}
				%>

			</ul>
		</div>
	</div>

</body>
</html>