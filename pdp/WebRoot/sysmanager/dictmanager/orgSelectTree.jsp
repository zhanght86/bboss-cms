<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.form.UserOrgManagerForm" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@page import="com.frameworkset.platform.security.AccessControl"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>


<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<html>
<head>
<title>选择机构</title>
<%
	AccessControl control = AccessControl.getAccessControl();
	control.checkAccess(request,response);
	String orgNames = request.getParameter("orgNames");

%>

<%
	request.setAttribute("rootName", RequestContextUtils.getI18nMessage("sany.pdp.organization.tree.name", request));
%>
    <script src="${pageContext.request.contextPath}/include/jquery-1.4.2.min.js"></script>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<script language="javascript">
var api = frameElement.api, W = api.opener;

function addorg(){
	var orgValues="";
	var orgIds = "";
	var state = false;
	var orgs = document.all.orgIdName;	 
	if(orgs){
		if(orgs.length){
		  	for(var i = 0;i < orgs.length; i++){
		  		if(orgs[i].checked){
		  		    var values = orgs[i].value
		  		    
		  		    var tmp = values.split(" ");
		  			if(state){  			    
		  				orgValues += "," + values;
		  				if(tmp.length>0){
		  					orgIds += "," + tmp[0];
		  				}
		  			}else{
		  				orgValues = values;
		  				if(tmp.length>0){
		  					orgIds = tmp[0];
		  				}
		  				state = true;
		  			}
		  		}
		  	}
		}
		else
		{
			if(orgs.checked){
		  		    var values = orgs.value
		  		    
		  		    var tmp = values.split(" ");
		  			if(state){  			    
		  				orgValues += "," + values;
		  				if(tmp.length>0){
		  					orgIds += "," + tmp[0];
		  				}
		  			}else{
		  				orgValues = values;
		  				if(tmp.length>0){
		  					orgIds = tmp[0];
		  				}
		  				state = true;
		  			}
		  		}
		}
  	}else{
  		$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.org.select.null"/>');
  		return;
  	}
	
	W.document.getElementById("${param.tag1}").value = orgValues;
	W.document.getElementById("${param.tag2}").value = orgIds;
	
	var win=api.config["currentwindow"];
	if (win != null) {
		win.document.getElementById("${param.tag1}").value= orgValues;
		win.document.getElementById("${param.tag2}").value= orgIds;
	}
	
	api.close();
    
  	//window.returnValue=orgValues + "^" + orgIds;
  	//window.close();
}
</script>
</head> 
<body class="contentbodymargin">
<div class="btnarea" >
	<a href="javascript:void(0)" class="bt_1" id="add" name="add" onclick="addorg()"><span><pg:message code="sany.pdp.common.operation.ok"/></span></a>
	<a href="javascript:void(0)" class="bt_2" id="cancel" name="cancel" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.cancel"/></span></a>
</div>

<div id="contentborder">
<center>
<form id="OrgJobForm" name="OrgJobForm" action="" method="post" >
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
    			   mode="static-dynamic"
    			   >   
    			   <tree:param name="checkBoxOne"/>
				   <tree:radio name="orgIdName" />
    			   <tree:treedata treetype="com.frameworkset.platform.menu.DictOrgSelect"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="${rootName}"
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                   refreshNode="false"
    	                   />

    	</tree:tree>
	</td>			 
				  
  </tr>
<iframe name="getOrg" width="0" height="0" ></iframe>  
</table>
<script>
	var name = "<%=orgNames%>";
	var names = name.split(",");
	for(var i = 0;document.all.orgIdName && i < document.all.orgIdName.length; i++){
  		var org = document.all.orgIdName[i];
  		for(var n = 0; n < names.length; n++){
			if(names[n]==org.value){
				org.checked="true";
			}
		}
  	}
</script>
</form>
</center>
</div>
<IFRAME name="delDisperse" height="0" width="0"></IFRAME>
</body>
</html>

