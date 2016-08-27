<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>


<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,
                 com.frameworkset.platform.config.ConfigManager,
                 com.frameworkset.platform.sysmgrcore.purviewmanager.db.PurviewManagerImpl,

                 com.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager"%>
<%    
    AccessControl control = AccessControl.getInstance();
   	control.checkManagerAccess(request,response);
   	String orgId = request.getParameter("orgId")==null?"":request.getParameter("orgId");
   	PurviewManager manager = new PurviewManagerImpl();
   	String orgName = manager.getOrgNameByOrgId(orgId);
%>

<html>
	<head>
		<title>回收机构【<%=orgName%>】权限</title>
	</head>
	<body class="contentbodymargin" >
		<br>
		<form name="form1" method="post" target="hiddenFrame" >
		<input type="hidden" name="orgId" value="<%=orgId%>">
		<%
			if (ConfigManager.getInstance().getConfigBooleanValue("enableorgrole", false)
				&& control.checkPermission(orgId,
                        "orgroleset", AccessControl.ORGUNIT_RESOURCE))
            {
        %>
			<fieldset>
			<legend class="td"><font size="2"><pg:message code="sany.pdp.recover.organization.authority"/></font></legend>	
					<br>
					<table cellspacing="1" cellpadding="0" border="0" bordercolor="#EEEEEE" width=100% >
						<tr align="left">
						    <td class="td"><b><pg:message code="sany.pdp.choose.authority.recover"/>：</b></td>
						</tr>
					</table>
					<table cellspacing="1" cellpadding="0" border="0" align="center"  bordercolor="#EEEEEE" width=80% >	
						<tr>
							<td class="td"><input type="checkbox" name="isOrgRecursion" value="isOrgRecursion"><pg:message code="sany.pdp.userorgmanager.org.purview.recycle.recursion"/></td>
						</tr>				
						<tr>
						    <td class="td"><input type="checkbox" name="directOrgRes" value="directOrgRes"><pg:message code="sany.pdp.userorgmanager.org.purview.authorize.direct"/></td>
						</tr>
						<tr>
						    <td class="td"><input type="checkbox" name="orgRoleRes" value="orgRoleRes"><pg:message code="sany.pdp.userorgmanager.org.purview.role"/></td>				    
						</tr>
					</table>							
			</fieldset>
			<br>
		<%
		    }
		%>
		<fieldset>
		<legend class="td"><font size="2"><pg:message code="sany.pdp.recover.organization.user.authority"/></font></legend>	
				<br>
				<table cellspacing="1" cellpadding="0" border="0" bordercolor="#EEEEEE" width=100% >
					<tr align="left">
					    <td class="td"><b><pg:message code="sany.pdp.choose.authority.recover"/>：</b></td>	
					</tr>
				</table>
				<table cellspacing="1" cellpadding="0" border="0" align="center"   width=80% >	
					<tr>
						 <td class="td"><input type="checkbox" name="isRecursion" value="isRecursion"><pg:message code="sany.pdp.userorgmanager.user.purview.recycle.recursion"/></td>		
					</tr>				
					<tr>
					    <td class="td"><input type="checkbox" name="directRes" value="directRes"><pg:message code="sany.pdp.userorgmanager.user.purview.authorize.direct"/></td>
					</tr>
					<tr>
					    <td class="td"><input type="checkbox" name="userRoleRes" value="userRoleRes"><pg:message code="sany.pdp.userorgmanager.user.purview.role"/></td>				    
					</tr>
					<% 
						if(ConfigManager.getInstance().getConfigBooleanValue("enablejobfunction",false)){
					%> 
					<tr>
					    <td class="td"><input type="checkbox" name="userOrgJobRes" value="userOrgJobRes"><pg:message code="sany.pdp.userorgmanager.user.purview.post"/></td>				    
					</tr>
					<% 
						}
					%>	
					<% 
						if(ConfigManager.getInstance().getConfigBooleanValue("enablergrouprole",false)){
					%>
					<tr>
					    <td class="td"><input type="checkbox" name="userGroupRes" value="userGroupRes"><pg:message code="sany.pdp.userorgmanager.user.purview.usergroup"/></td>				    
					</tr>				
					<% 
						}
					%>
				</table>							
		</fieldset>
		<br>
		
				
		<div align="center"><a class="bt_1" onclick="reclaim()"><span><pg:message code="sany.pdp.recover"/></span></a></div>
		</form>
	</body>
	<script language="javascript">
	    function reclaim(){
	        var directOrgRes = "";
	        var orgRoleRes = "";
	        var directRes = "";
	        var userRoleRes = "";
	        var userOrgJobRes = "";	
	        var userGroupRes = "";//用户的用户组权限  
	        var isRecursion = ""; 
	        var isOrgRecursion = "";
	        if(document.all.directOrgRes && document.all.directOrgRes.checked){
	            directRes = document.all.directOrgRes.value;
	        }
	        if(document.all.orgRoleRes && document.all.orgRoleRes.checked){
	            userRoleRes = document.all.orgRoleRes.value;
	        }
	        if(document.all.isOrgRecursion && document.all.isOrgRecursion.checked){
	            isOrgRecursion = document.all.isOrgRecursion.value;
	        }		             
	        if(document.all.userRoleRes && document.all.userRoleRes.checked){
	            directRes = document.all.userRoleRes.value;
	        }
	        if(document.all.directRes && document.all.directRes.checked){
	            userRoleRes = document.all.directRes.value;
	        }
	        if(document.all.userOrgJobRes && document.all.userOrgJobRes.checked){
	            userOrgJobRes = document.all.userOrgJobRes.value;
	        }	        
	        if(document.all.isRecursion.checked){
	            userOrgJobRes = document.all.isRecursion.value;
	        }
	        if(document.all.userGroupRes && document.all.userGroupRes.checked){
	        	userGroupRes = document.all.userGroupRes.value;
	        }
	        if(directOrgRes=="" && orgRoleRes=="" && directRes=="" && userRoleRes=="" && userOrgJobRes=="" && userGroupRes==""){
	            $.dialog.alert('<pg:message code="sany.pdp.userorgmanager.purview.recycle.null"/>');
	            return false;
	        }else{
	            document.form1.target = "hiddenFrame";
	        	document.form1.action = "reclaimOrgUserRes_do.jsp";	
	        	document.form1.submit();        	
	        }	        
	        
	    }
	</script>
	<iframe id="hiddenFrame" name="hiddenFrame" src="" width=0 height=0></iframe>
</html>

