<%
/*
 * <p>Title: 用户调入框架主页面</p>
 * <p>Description: 用户调入框架主页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-21
 * @author liangbing.tao
 * @version 1.0
 */
%>
<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>

<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Organization"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	
	String orgId = request.getParameter("orgId");
	Organization org = OrgCacheManager.getInstance().getOrganization(orgId);
	String orgName = "";
	
	if(org.getRemark5() != null && !"".equals(org.getRemark5())){
		orgName = org.getRemark5();
	}else{
		orgName = org.getOrgName();
	}
%>
<HTML>
 <HEAD>
   <title>机构【<%=orgName%>】调入用户</title>
 </HEAD>
  <frameset name="framefold" cols="30,70" frameborder="no" border="0" framespacing="0">
	<frame src="orguserTree.jsp?oid=<%=orgId%>" name="orgTree" id="orgTree"></frame>		
	<frame src="userList_ajax.jsp?oid=<%=orgId%>" name="userList" scrolling="No" noresize="noresize" id="userList"></frame>
  </frameset>	
</HTML>

