<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.config.ConfigManager"%>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);
	UserManager userManager = SecurityDatabase.getUserManager();
	String CuuserId = control.getUserID();
	String ids = request.getParameter("checkBoxOne");
	String[] userid = ids.split(",");
	String script = "";
	String script2 = "";
	String userReturnInfo = "";
	for(int i = 0; i < userid.length; i++){
		String userId = userid[i];
		if(userId.equals(CuuserId)){
			script = "alert('不能将自己调入到其他机构！'); window.close();window.returnValue='ok';";
		}
		//System.out.println("userReturninfo = " + userManager.userOrgInfo(userId));
		User user = userManager.getUserById(userId);
		if(!userManager.userOrgInfo(control,userId).equals("")){
			if(userReturnInfo.equals("")){
				userReturnInfo = "不能将以下用户调入其他机构。" + "\\n"+user.getUserRealname() + "(" + user.getUserName() + "):" + userManager.userOrgInfo(control,userId);
			}else{
				userReturnInfo += "\\n" + user.getUserRealname() + "(" + user.getUserName() + "):" + userManager.userOrgInfo(control,userId);
			}
		}
	}
	if(!userReturnInfo.equals("")){
		script2 = "alert('" + userReturnInfo + "'); window.close(); window.returnValue='ok';";
	}
	String orgId = request.getParameter("orgId");
	OrgManager orgManager = SecurityDatabase.getOrgManager();
	Organization org = orgManager.getOrgById(orgId);
	String orgName = org.getOrgName();
%>
<html>
<head>
<title>从机构【<%=orgName%>】将用户调出到机构</title>
<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>
<script language="javascript">
function addorg(){
	var orgValues="";
	var state = false;
	var orgold = "<%=orgId%>";
	if(document.all.orgIdName){
		if(document.all.orgIdName.length){
		  	for(var i = 0; i < document.all.orgIdName.length; i++){
		  		if(document.all.orgIdName[i].checked){
		  			var orgValue = document.all.orgIdName[i].value;
		  			var orgValueId = orgValue.split(";");
		  			if(orgold==orgValueId[0]){
		  				alert("用户已经存在机构"+orgValueId[1]+"下，不需再调入！");
		  				return;
		  			}
		  			if(state){
		  				orgValues += "," + orgValueId[0];
		  			}else{
		  				orgValues = "" + orgValueId[0];
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
  	<%if(!ConfigManager.getInstance().getConfigBooleanValue("sys.user.enablemutiorg", true)){%>
	  	if(orgValues!=""){
	  		document.OrgJobForm.target="getOrg";
		  	document.OrgJobForm.action="../user/foldDisperse.jsp?userIds=<%=ids%>&orgIds="+orgValues+"&orgId=<%=orgId%>&flag=0";
		  	document.OrgJobForm.submit();
	  	}else{
	  		alert("请选择要调入的机构！");
			return;
	  	}
  	<%}else{%>
	  	if(orgValues!=""){
	  		if(confirm("是否保留所选用户与该机构的关系！（保留点确定；删除点取消）！")){
		  		document.OrgJobForm.target="getOrg";
			  	document.OrgJobForm.action="../user/foldDisperse.jsp?userIds=<%=ids%>&orgIds="+orgValues+"&orgId=<%=orgId%>&flag=1";
			  	document.OrgJobForm.submit();
		  	}else{
		  		document.OrgJobForm.target="getOrg";
			  	document.OrgJobForm.action="../user/foldDisperse.jsp?userIds=<%=ids%>&orgIds="+orgValues+"&orgId=<%=orgId%>&flag=0";
			  	document.OrgJobForm.submit();		  	
		  	}
	  	}else{
	  		alert("请选择要调入的机构！");
			return;
	  	}
  	<%}%>
}

</script>
</head> 
<body class="contentbodymargin" onload="checkFold()">
<div id="contentborder">
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
    	           imageFolder="../../sysmanager/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
				   href=""
    			   target=""
    			   mode="static-dynamic"
    			   >   
    			   <tree:param name="checkBoxOne"/>
    			   <tree:param name="orgId"/>
                   <%if(!ConfigManager.getInstance().getConfigBooleanValue("sys.user.enablemutiorg", true)){%>
                   <tree:radio name="orgIdName" />   
                   <%}else{%>  
				   <tree:checkbox name="orgIdName" />
				   <%}%>
    			   <tree:treedata treetype="com.frameworkset.platform.menu.DisperseOrgJob"
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
  <input name="ced" type="button" class="input" value="关闭" onclick="window.close();window.returnValue='ok';">
  </td>
  </tr>
<iframe name="getOrg" width="0" height="0" ></iframe>  
</table>
</form>
</center>
</div>
<IFRAME name="delDisperse" height="0" width="0"></IFRAME>
</body>
<script>
function checkFold(){
<%if(!script.equals("")){%>
<%=script%>
<%}else if(!script2.equals("")){%>
<%=script2%>
<%}%>
}
</script>
</html>

