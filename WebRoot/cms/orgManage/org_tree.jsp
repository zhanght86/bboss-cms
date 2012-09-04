<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>
<%
AccessControl accessControl = AccessControl.getInstance();
accessControl.checkAccess(request,response);
	                            
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机构树</title>
<script language="javascript">
function action11(){
	getPropertiesContent().location.href="<%=rootpath%>/sysmanager/orgmanager/organsearch.jsp";   
}
function neworg()
{
	openWin("new_org.jsp?flag=1",520,400);
}
</script>
<script src="../inc/js/func.js"></script>
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/treeview.css">
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/windows.css">
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/tab.winclassic.css">

</head>
<body class="contentbodymargin" scroll="auto">

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table" >
  <tr>
  	<%
  	ResourceManager resManager = new ResourceManager();
	String resId = resManager.getGlobalResourceid(AccessControl.ORGUNIT_RESOURCE);
	if (resId != null && !resId.equals("") && accessControl.checkPermission(resId,
					"addorg", AccessControl.ORGUNIT_RESOURCE))
	{   
		//只有admin才可以新建一级的组织 
		//baowen.liu 2008-4-23
		//if("1".equals(accessControl.getUserID())) {
	  	%>
	    <td align="center"><a href="#" onclick="neworg()">新增机构</a></td>
	   	<%
	   	//}
   	}
   	%>   	
  </tr>
</table>
    <table >
        <tr><td align="left">
         <tree:tree tree="org_tree"
    	           node="org_tree.node"
    	           imageFolder="../../sysmanager/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href="org_userlist.jsp" 
    			   target="org_userlist" 
    			   dynamic="false"
    			   > 
    			    
    			   <tree:treedata treetype="CMSOrgChargeTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="机构树"
    	                   expandLevel="1"
    	                   showRootHref="true"
    	                   needObserver="false"
    	                   enablecontextmenu="false" 
    	                   />
					
    	</tree:tree>
         </td></tr>
    </table>
<script language="javascript">
</script>

</body>
</html>