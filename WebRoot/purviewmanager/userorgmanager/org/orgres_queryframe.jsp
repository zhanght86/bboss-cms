<%
/*
 * <p>Title: 机构资源查询框架</p>
 * <p>Description: 机构资源查询框架</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-26
 * @author liangbing.tao
 * @version 1.0
 *
 */
 %>
   <%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Organization"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>


<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	
	String orgId = (String)request.getParameter("orgId");

	//根据orgId查询机构名称
	OrgManager orgManager = SecurityDatabase.getOrgManager();
	Organization org = orgManager.getOrgById(orgId);

	String orgName = "";
	
	if(org.getRemark5()==null && "".equals(org.getRemark5()))
	{
		orgName = org.getOrgName();
	}
	else
	{
		orgName = org.getRemark5();
	}

%>

<HTML>
 <HEAD>
   <title>机构[<%=orgName%>]资源权限查询</title>
 </HEAD>
 <frameset rows="90,*" border=0>
	<frame frameborder=0  noResize scrolling="no" 
			marginWidth=0 name="forQuery" src="../user/userres_query.jsp?orgId=<%=orgId%>&typeName=org">
	</frame>		
	<frame frameborder=0  noResize scrolling="auto" 
			marginWidth=0 name="forDocList" src="../user/userres_querylist.jsp?orgId=<%=orgId%>&typeName=org">
	</frame>
 </frameset>	
</HTML>

