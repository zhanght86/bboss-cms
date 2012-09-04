<%
/**
 * <p>Title: 机构和用户管理主页面</p>
 * <p>Description: 机构和用户管理主页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-17
 * @author da.wei
 * @version 1.0
 **/
 %>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkManagerAccess(request,response);

			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", -1);
			response.setDateHeader("max-age", 0);

			String flag = request.getParameter("flag");

		%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@ include file="/common/jsp/css.jsp"%>
		<title>组织机构管理</title>
	</head>
	<frameset name="userOrgFrame" cols="20%,*" border=0 framespacing="0">
		<frame frameborder=0 scrolling="auto" name="org_tree" src="org_tree.jsp" marginWidth=0 marginHeight=0 >
		</frame>
		<frame frameborder=0 scrolling="auto" name="org_userlist" src="properties_content.jsp" marginWidth=0 marginHeight=0 >
		</frame>
	</frameset>
</html>

