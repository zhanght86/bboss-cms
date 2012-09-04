<%
/*
 * <p>Title: 批量岗位授予框架页面</p>
 * <p>Description: 批量岗位授予框架页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-20
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

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,
				 com.frameworkset.platform.sysmgrcore.entity.*,
				 com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
				 com.frameworkset.platform.sysmgrcore.manager.UserManager,
				 com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl,
				 java.util.List,
				 java.util.ArrayList"%>
		
				
				
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	//当前用户所属机构ID
     String curUserOrgId = accessControl.getChargeOrgId(); 
	String checks = request.getParameter("checks");
	
	String orgId = request.getParameter("orgId");
	String[] id =checks.split(",");
	
	//管理员与自己不能授予角色
    UserManager userManager = SecurityDatabase.getUserManager();
    OrgManagerImpl orgImpl = new OrgManagerImpl();	
     boolean tag = false;
     boolean tagSelf = false;
     String adminUsers = "以下用户不能进行岗位授予:\\n";
     for(int j=0;j<id.length; j++){
		User adminUser = userManager.getUserById(id[j]);
		
		if(accessControl.isAdminByUserid(id[j])){//有管理员角色		    
			tag = true;						
			adminUsers += adminUser.getUserName() + " 是超级管理员\\n";
		}
		
		//没有管理员角色, 但是给自己授权
		if(accessControl.getUserID().equals(id[j])){
			tag = true;
			adminUsers += adminUser.getUserName() + " 不能给自己授予岗位\\n";
		}
		
		 //是部门管理员, 也不允许授权
		 // 允许部门管理员批量设置岗位  baowen.liu 2008-3-21
		 boolean managerOrgs = accessControl.isOrganizationManager(id[j],orgId);
	     //boolean managerOrgs = orgImpl.isCurOrgManager(orgId, id[j]);
	      if(managerOrgs){
	         tag = true;
		     adminUsers += adminUser.getUserName() + " 是部门管理员不能进行岗位设置\\n";
	     }
     }
     
     if(tag)
     {
     	%>
     	<SCRIPT LANGUAGE="JavaScript">
     	parent.$.dialog.alert("<%=adminUsers%>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		</script>
		<%
     }
%>

<html>
  <head>
	  <title>隶属岗位</title>
  </head>
	<frameset rows="*" cols="100%" framespacing="3" frameborder="yes" border="0" >
		<frame src="allJob_ajax.jsp?orgId=<%=orgId%>&checks=<%=checks%>" name="lusersys1" id="lusersys1">
	</frameset>
</html>
