
<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>


<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.config.ConfigManager,
                 com.frameworkset.platform.sysmgrcore.purviewmanager.db.PurviewManagerImpl,
                 com.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager"%>
                 
<%
    AccessControl control = AccessControl.getInstance();
   	control.checkManagerAccess(request,response);
    String userIds =  request.getParameter("userIds") == null ? "":request.getParameter("userIds");
    PurviewManager manager = new PurviewManagerImpl();
    String userNames = manager.getUserNamesByUserIds(userIds);
%>
<html>
	<head>
		<title><pg:message code="sany.pdp.sys.purview.recycle"/></title> 
		<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
	</head>
	<body class="contentbodymargin" >
		<br>
		<form name="form1" id="form1" method="post" target="hiddenFrame" >
		<input type="hidden" name="userIds" value="<%=userIds%>">
				<br>
				<table cellspacing="1" cellpadding="0" border="0" bordercolor="#EEEEEE" width=100% >
					<tr align="left">
					    <td class="td"><b><pg:message code="sany.pdp.choose.authority.recover"/>:</b></td>				    
					</tr>
					<tr>
					    <td></td>				    
					</tr>
				</table>
				<table cellspacing="1" cellpadding="0" border="0" align="center"  bordercolor="#EEEEEE" width=80% >					
					<tr>
					    <td class="td"><input type="checkbox" name="directRes" value="directRes"><pg:message code="sany.pdp.user.authority"/></td>
					</tr>
					<tr>
					    <td class="td"><input type="checkbox" name="userRoleRes" value="userRoleRes"><pg:message code="sany.pdp.user.role.authority"/></td>				    
					</tr>
					<% 
						if(ConfigManager.getInstance().getConfigBooleanValue("enablejobfunction",false)){
					%>  
					<tr>
					    <td class="td"><input type="checkbox" name="userOrgJobRes" value="userOrgJobRes"><pg:message code="sany.pdp.job.authority"/></td>				    
					</tr>
					<% 
						}
					%>
					<% 
						if(ConfigManager.getInstance().getConfigBooleanValue("enablergrouprole",false)){
					%>
					<tr>
					    <td class="td"><input type="checkbox" name="userGroupRes" value="userGroupRes"><pg:message code="sany.pdp.group.authority"/></td>				    
					</tr>				
					<% 
						}
					%>
					<tr>
			    <table cellspacing="1" cellpadding="0" border="0" align="center"  bordercolor="#EEEEEE" width=50% >					
			<tr>
			    <td class="td">
			    <a href="javascript:void(0)" class="bt_1" id="addButton"
						onclick="reclaim()"><span><pg:message code="sany.pdp.recover"/></span></a> 
				<a href="javascript:void(0)" class="bt_2" id="addButton"
						onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.close"/></span></a> 
			</td>
			</tr>			
		</table>
			</tr>	
				</table>
				<br>			
		
		</form>
	</body>
	<script language="javascript">
	var api = frameElement.api, W = api.opener;
	    function reclaim(){
	        var directRes = "";
	        var userRoleRes = "";
	        var userOrgJobRes = "";	   
	        var userGroupRes = "";//用户的用户组权限     
	        if(document.all.userRoleRes && document.all.userRoleRes.checked){
	            directRes = document.all.userRoleRes.value;
	        }
	        if(document.all.directRes && document.all.directRes.checked){
	            userRoleRes = document.all.directRes.value;
	        }
	        if(document.all.userOrgJobRes && document.all.userOrgJobRes.checked){
	            userOrgJobRes = document.all.userOrgJobRes.value;
	        }
	        if(document.all.userGroupRes && document.all.userGroupRes.checked){
	        	userGroupRes = document.all.userGroupRes.value;
	        }
	        if(directRes == "" && userRoleRes == "" && userOrgJobRes == "" && userGroupRes == ""){
	            parent.$.dialog.alert("<pg:message code='sany.pdp.common.operation.select'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	            return false;
	        }else{	   
		       // document.form1.target = "hiddenFrame";
		        //document.form1.action = "reclaimUserRes_do.jsp";
		        //document.form1.submit();
		        
		        $.ajax({
					   type: "POST",
						url : "<%=request.getContextPath()%>/usermanager/reclaimUserRes_do.page",
						data :formToJson("#form1"),
						dataType : 'json',
						async:false,
						beforeSend: function(XMLHttpRequest){
								 
						      		blockUI();	
						      		XMLHttpRequest.setRequestHeader("RequestType", "ajax");
						      	 		 	
							},
						success : function(response){
							//去掉遮罩
							unblockUI();
							if(response.code=="success"){
								var msg = response.errormessage;
								 
								W.$.dialog.alert(msg,function(){	
										
										api.close();
								},api);													
							}else{
								W.$.dialog.alert("操作结果："+response.errormessage,function(){	
									 
									},api);	
								 
							}
						}
					  });
	        }
	    }
	</script>
</html>

