<%
/*
 * <p>Title: 资源选择树</p>
 * <p>Description: 资源选择树页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-19
 * @author liangbing.tao
 * @version 1.0
 */
%>


<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>



<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);

	String roleId = request.getParameter("roleId");
	session.setAttribute("currRoleId", roleId);
	session.setAttribute("role_type", "role");
%>

<html>
	<head>
		<title>属性容器</title>
		<script language="javascript" src="../scripts/changeView.js" type="text/javascript"></script>
<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/treeview.css">
	<body class="contentbodymargin">
		<div id="contentborder">
			<table class="table" width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">

				<tr class="tr">
					<td width="25%" class="td">
						<select class="select" name="select" onChange="changeView('select')">
							<option selected>
								--请选择资源--
							</option>
							<option value="site" link="resSiteTree.jsp">
								站点资源
							</option>
							<option value="channel" link="resChannelTree.jsp">
								频道资源
							</option>
							<option value="column" link="../userorgmanager/user/resColumnTree.jsp">
								菜单资源
							</option>
							<option value="res" link="resResourcTree.jsp">
								资源类资源
							</option>
						</select>
					</td>
				</tr>
				<tr height="100%" width="100%">
					<td height="100%" width="100%">
						<iframe id="resource_bridge" frameborder="0" 
							name="resource_bridge" src="" height="100%" width="100%" />
					</td>
				</tr>

			</table>

		</div>
	</body>
</html>

