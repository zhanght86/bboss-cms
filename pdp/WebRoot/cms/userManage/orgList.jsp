<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);	
	String orgIdName = (String) request.getParameter("orgIdName");
%>
<html>
<head>    
  <title>选择机构</title>
  <script language="JavaScript"> 
  function addorg(){	
  	var chkbx = document.all("orgIdName");
	//没有选择
  	if(!chkbx){  	
  		alert("您没有选择机构");
  		return;
  	}
	var n=document.all("orgIdName").length;
	
	var backstring = "";	
	if(!n)
	{
		backstring = chkbx.value;
	}
	else
	{
	
		for(var i= 0;i<n;i++){
			if(chkbx[i].checked){
				var tmp = chkbx[i].value;
				backstring =tmp;
				break;		
			}
		}
	}
	
	
	var id = backstring.slice(0,backstring.indexOf(";"));
	var name = backstring.slice(backstring.indexOf(";")+1,backstring.length);
	
	//alert(backstring);
	//alert(window.dialogArguments.document.URL);
	window.dialogArguments.document.forms[0].ou.value=name;
	//alert(window.dialogArguments.document.forms[0].ou.value);
	//alert(id);
	window.dialogArguments.document.forms[0].orgId.value=id;	
	window.close();
	
}
</script>
  <link rel="stylesheet" type="text/css" href="../../sysmanager/css/windows.css">
  <link rel="stylesheet" type="text/css" href="../../sysmanager/css/treeview.css"> 
<body class="contentbodymargin">
<div id="contentborder">
<center>
<form name="OrgJobForm" action="" method="post" >
<table width="80%" border="0" cellpadding="0" cellspacing="1">
  <tr class="tabletop">
    <td width="40%" align="center"></td>    
  </tr>
  <tr >
     <td >
     
    <tree:tree tree="user_org_tree111"
    	           node="user_org_tree111.node"
    	           imageFolder="../../sysmanager/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
				   href=""
    			   target=""
    			   dynamic="false"
    			   >                         
                   <tree:param name="uid"/>
                   <tree:radio name="orgIdName" defaultValue="<%=orgIdName%>"/>     


    			   <tree:treedata treetype="OrgTreeCheckbox"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="机构树"
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                   refreshNode="false"
    	                   />

    	</tree:tree>
	</td>			 
				  
  </tr>
  <tr>
  <td align="center" >
  <input name="add" type="button" class="input" value="确定" onclick="addorg()">
  </td>
  </tr>
<iframe name="getOrg" width="0" height="0"></iframe>  
</table>
</form>
</center>
</div>
</body>
</html>
