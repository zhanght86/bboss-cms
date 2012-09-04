<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.form.UserOrgManagerForm" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@page import="com.frameworkset.common.poolman.DBUtil,
				com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl,
				java.util.*"%>
<%
	 AccessControl accesscontroler = AccessControl.getInstance();
	 accesscontroler.checkAccess(request, response);
	 String[] id =(String[])request.getAttribute("id");
     UserManager userManager = SecurityDatabase.getUserManager();
     OrgManagerImpl orgImpl = new OrgManagerImpl();	
     String idStr = "";
     String usern = "";
     String userna = "";
     for(int i = 0;i < id.length; i++)
     {
     	idStr += id[i]+",";
     	
     	User user = userManager.getUserById(id[i]);
     	usern= user.getUserRealname();
     	userna += usern + ",";
     }
	 if(idStr.length() > 1)
     idStr = idStr.substring(0,idStr.length()-1);
     if(userna.length() > 1)
     userna = userna.substring(0,userna.length()-1); 
     
     
     //如果选择的用户有系统管理员，则弹出提示请用户重新选择。
     String orgId = request.getParameter("orgId");
     DBUtil db = new DBUtil();
     
     
     boolean tag = false;
     boolean tagSelf = false;
     String adminUsers = "以下用户不能进行资源授予:\\n";
     
     for(int j=0;j<id.length; j++){
     	String sql ="select count(*) from td_sm_userrole where user_id ="+ id[j] +" and role_id='1'";
		db.executeSelect(sql);
		User adminUser = userManager.getUserById(id[j]);
		
		if(db.getInt(0,0)>0){//有管理员角色		    
			tag = true;						
			adminUsers += adminUser.getUserName() + " 是超级管理员\\n";
		}
		
		//没有管理员角色, 但是给自己授权
		if(accesscontroler.getUserID().equals(id[j])){
			tag = true;
			adminUsers += adminUser.getUserName() + " 不能给自己授予角色\\n";
		}
		
		 //是部门管理员, 也不允许授权
	     List managerOrgs = orgImpl.getUserManageOrgs(id[j]);
	     if(managerOrgs.size()>0){
	         tag = true;
		     adminUsers += adminUser.getUserName() + " 是部门管理员\\n";
	     }
     }
     
     if(tag)
     {
     	%>
     	<SCRIPT LANGUAGE="JavaScript">
		alert("<%=adminUsers%>"); 
		getPropertiesContent().location.href="../user/userManager.do?method=getUserList&orgId=<%=orgId%>";
		</script>
		<%
     }
     else
     {
     
%>
<html>
<head>
<title>批量资源操作授予</title>
<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>

</head> 
<frameset name="frame1" cols="30,70" frameborder="no" border="0" framespacing="0" >
  <frame src="batch_resdefault.jsp?userId=<%=idStr%>" name="orgTree" id="orgTree" />
  <frameset name="userId" rows="30%,70%" frameborder="no" border="0" framespacing="0" >
  	<frame src="../accessmanager/role/operList_global.jsp" name="globalOperList" id="globalOperList" scrolling="No" noresize="noresize" />
  	<frame src="user_operdefault.jsp" name="operList" scrolling="No" noresize="noresize" id="orgList" />
  </frameset>
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>

	<%
	}
	%>
