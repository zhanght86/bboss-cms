<%
/**
 * 
 * <p>Title: 离散用户调入页面</p>
 *
 * <p>Description: 离散用户调入机构页面</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: bboss</p>
 * @Date 2006-9-15
 * @author gao.tang
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
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.form.UserOrgManagerForm" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.config.ConfigManager,
				com.frameworkset.platform.sysmgrcore.manager.OrgAdministrator,
				com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl"%>

<html>
<head>
<title>离散用户调入机构</title>
<link rel="stylesheet" type="text/css" href="../../css/treeview.css">
<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
	OrgAdministrator orgAdmin = new OrgAdministratorImpl();
	String ids = request.getParameter("checkBoxOne");
	
	//-------------
	boolean isDelete = false;
	if("1".equals(request.getParameter("deleteOrgUser"))){
		UserManager userManager = SecurityDatabase.getUserManager();
		isDelete = userManager.deleteDisperseOrguser();
	}
	//-------------
%>

<script language="javascript">
var api = frameElement.api;
function addorg(){
   var orgValues="";
   var state = false;
   if(document.all.orgIdName){
   		if(document.all.orgIdName.length)
   		{
		   for(var i = 0;i < document.all.orgIdName.length; i++){
		   		document.all.orgIdName[i].disabled="false";	
		  		if(document.all.orgIdName[i].checked){
		  			var orgValue = document.all.orgIdName[i].value;
		  			if(state){
		  				orgValues += "," + orgValue.substring(0,orgValue.lastIndexOf(";"));
		  			}else{
		  				orgValues = "" + orgValue.substring(0,orgValue.lastIndexOf(";"));
		  				state = true;
		  			}
		  		}
		  	}
		  	
   	   }else{
	   		if(document.all.orgIdName.checked){
	   			orgValues = document.all.orgIdName.value.substring(0,document.all.orgIdName.value.lastIndexOf(";"));
	   		}
	   }
	}
	  
	  	
  	if(orgValues!=""){
  		document.OrgJobForm.target="getOrg";
	  	document.OrgJobForm.action="../user/foldDisperse.jsp?userIds=<%=ids%>&orgIds="+orgValues;
	  	document.OrgJobForm.submit();
  	}else{
  		$.dialog.alert('<pg:message code="sany.pdp.choose.move.in.organization"/>',function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		return;
  	}
}

function clears(){
	OrgJobForm.target="delDisperse";
	OrgJobForm.action="allorg2discrete.jsp?deleteOrgUser=1";
	OrgJobForm.submit();
}

if("<%=isDelete%>"=="true"){
	$.dialog.alert('<pg:message code="sany.pdp.userorgmanager.user.redundance.delete.success"/>',function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
}

function alertfun(msg1,msg2)
{
	$.dialog.alert(msg1,function(){
		W = api.opener; 
		W.reloadusers();
		 api.close();
		 
	},null,msg2);
	
}
</script>
</head> 
<body class="contentbodymargin" >
<div id="">
<center>
<form id="OrgJobForm" name="OrgJobForm" action="" method="post" >
<table width="80%" border="0" cellpadding="0" cellspacing="1">
<input type="hidden" name="userIds" value="<%=ids%>" />
  <tr class="tabletop">
    <td width="40%" align="center"></td>    
  </tr>
  <tr >
     <td >
     
    <tree:tree tree="user_org_tree111"
    	           node="user_org_tree111.node"
    	           imageFolder="../../../sysmanager/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
				   href=""
    			   target=""
    			   mode="static-dynamic"
    			   >   
    			   
    			   <tree:param name="checkBoxOne"/>
                   <%if(!ConfigManager.getInstance().getConfigBooleanValue("sys.user.enablemutiorg", true)){
                   %>
                   <tree:radio name="orgIdName" />
                   <%}else{%>  
				   <tree:checkbox name="orgIdName" />
				   <%}%>
    			   <tree:treedata treetype="com.frameworkset.platform.menu.DisperseOrgJob"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="机构树"
    	                   rootNameCode="sany.pdp.organization.tree.name"
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
  <a class="bt_1" onclick="addorg()"><span><pg:message code="sany.pdp.common.operation.ok"/></span></a>
  <a class="bt_1"   onclick="javascript:clears()" /><span><pg:message code="sany.pdp.personcenter.person.res.redundance.delete"/></span></a>	
  </td>
  </tr>
<iframe name="getOrg" width="0" height="0" ></iframe>  
</table>
</form>
</center>
</div>
<IFRAME name="delDisperse" height="0" width="0"></IFRAME>
</body>
</html>

