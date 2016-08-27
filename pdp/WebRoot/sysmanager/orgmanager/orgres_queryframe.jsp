<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Organization"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>

<%
AccessControl accesscontroler = AccessControl.getInstance();
accesscontroler.checkAccess(request, response);

String orgId = (String)request.getParameter("orgId");

//根据orgId查询机构名称
OrgManager orgManager = SecurityDatabase.getOrgManager();
Organization org = orgManager.getOrgById(orgId);

String orgName = "";
if(org.getRemark5()==null && "".equals(org.getRemark5())){
	orgName = org.getOrgName();
}else{
	orgName = org.getRemark5();
}

%>
<HTML>
 <HEAD>
   <title>机构[<%=orgName%>]资源权限查询</title>
 </HEAD>
  <frameset rows="30,*" border=0>
	<frame frameborder=0  noResize scrolling="no" marginWidth=0 name="forQuery" src="../user/userres_query.jsp?orgId=<%=orgId%>"></frame>		
	<frame frameborder=0  noResize scrolling="auto" marginWidth=0 name="forDocList" src="../user/userres_querylist.jsp?orgId=<%=orgId%>"></frame>
	</frameset>	
</HTML>

