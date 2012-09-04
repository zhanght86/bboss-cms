<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Organization" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager" %>
<%@ page import="com.frameworkset.platform.security.AccessControl" %> 
<%
	String uid = request.getParameter("uid");
	String logid = request.getParameter("logid");
	String currentOrgId = request.getParameter("currentOrgId");
	String mainOrgId = request.getParameter("mainOrgId");
	String href = "../user/userManager.do?method=getOrgList&uid="+uid + "&logid=" + logid + "&currentOrgId=" + currentOrgId + "&mainOrgId=" + mainOrgId; 
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
	        //AccessControl.getInstance().login(request, response,logname, pwd);
	    }catch(Exception e){
	        e.printStackTrace();
	    }
	}
	
%>
<html>
<head>    
  <title>属性容器</title>
  <%@ include file="/include/css.jsp"%>
  <link rel="stylesheet" type="text/css" href="../css/treeview.css"> 
<body class="contentbodymargin" scroll="no">
<div id="contentborder">

<form name="OrgJobForm" action="" method="post" >
<table class="table" width="100%" border="0" cellpadding="0" cellspacing="1"> 
  <tr class="tr" >
     <td  class="td">
     
    <tree:tree tree="role_org_tree"
    	           node="role_org_tree.node"
    	           imageFolder="../images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href="<%=href%>"
    			   target="orgList"
    			   dynamic="false"
    			   >                         
                   <tree:param name="uid"/>
                   <tree:param name="logid"/>

    			   <tree:treedata treetype="OrgTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="机构树"
    	                   expandLevel="1"
    	                   showRootHref="true"
    	                   needObserver="false"
    	                   refreshNode="false"
    	                   />
    	</tree:tree>
	</td>				  
  </tr>  
</table>
</form>

</div>
</body>
</html>

