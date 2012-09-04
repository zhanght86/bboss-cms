<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.form.UserOrgManagerForm" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Organization" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager" %>
<%@ page import="com.frameworkset.platform.security.AccessControl" %> 
<html>
<head>
<script>
</script>
<title>用户调入机构</title>
<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>
<%
	String uid=request.getParameter("uid");
	String logid = request.getParameter("logid");
	UserManager userManager = SecurityDatabase.getUserManager();
	User user = null;
	String realname = "";
	String pwd = "";
	String logname = "";
	if(uid!=null && uid.length()>0){
	    try{		    		    
		    user = userManager.getUserById(logid);		    
		    realname = user.getUserRealname();
		    pwd = user.getUserPassword();
		    logname = user.getUserName();	    	        
	        AccessControl.getInstance().login(request, response,logname, pwd);
	    }catch(Exception e){
	        e.printStackTrace();
	    }
	}
	
	//由用户id得到用户主机构id和当前机构id,传入roleconferorg_ajax.jsp进行处理,da.wei
	String currentOrgId = request.getParameter("currentOrgId");
	String mainOrgId = request.getParameter("mainOrgId");
	  
%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ include file="../include/global1.jsp"%>
<script>
function closed(){
	window.returnValue = "ok";
	window.close();
}
</script>
</head> 
<frameset name="frame1" cols="30,70" frameborder="no" border="0" framespacing="0" >
  <frame src="orgTree1.jsp?uid=<%=uid%>&logid=<%=logid%>&currentOrgId=<%=currentOrgId%>&mainOrgId=<%=mainOrgId%>" name="orgTree" id="orgTree" />
  <frame src="roleconferorg_ajax.jsp?uid=<%=uid%>&logid=<%=logid%>&currentOrgId=<%=currentOrgId%>&mainOrgId=<%=mainOrgId%>" name="orgList" scrolling="No" noresize="noresize" id="orgList" />
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>

