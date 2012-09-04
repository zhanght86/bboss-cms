<%
/*
 * <p>Title: 角色授予用户页面</p>
 * <p>Description: 角色授予用户页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-24
 * @author liangbing.tao
 * @version 1.0
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%@ page import="com.frameworkset.platform.security.*"  %>
<%@ page import="java.util.List,
				java.util.ArrayList,
				com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
				com.frameworkset.platform.sysmgrcore.manager.UserManager,
				com.frameworkset.platform.sysmgrcore.entity.*,
				com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl"%>
				
<%
		AccessControl accesscontrol = AccessControl.getInstance();
    	accesscontrol.checkManagerAccess(request,response);
    	
    	String curUserOrgId = accesscontrol.getChargeOrgId();
    	
    	String userId = accesscontrol.getUserID();
    	
	   	String roleId = (String) request.getParameter("roleId");

		String orgid = request.getParameter("orgId");
		
		//System.out.println(roleId + "             " + orgid);
		
		UserManager userManager = SecurityDatabase.getUserManager();
		OrgManagerImpl orgImpl = new OrgManagerImpl();
		List existUser = userManager.getUsersListOfRoleInOrg(roleId, orgid);
		List noAdminUser = null;
		if (existUser == null) {
			noAdminUser = new ArrayList();
		}else{
			if(accesscontrol.isAdmin()){
				noAdminUser = existUser;
			}else{
				noAdminUser = new ArrayList();
				User user = null;
				for(int i = 0; i < existUser.size(); i++){
					user = (User)existUser.get(i);
					String userIdI = String.valueOf(user.getUserId());
					boolean isCurOrgManager = orgImpl.isCurOrgManager(curUserOrgId,userIdI);
					boolean isAdmin = accesscontrol.isAdminByUserid(userIdI); 
					//System.out.println(!isAdmin && !isCurOrgManager);
					if(!isAdmin && !isCurOrgManager){
						noAdminUser.add(user);
					}
				}
			}
		}
		
		request.setAttribute("existUser", noAdminUser);

		if (roleId != null && !roleId.equals("") && orgid != null && !orgid.equals("")) {
			//获取机构下的用户列表，并且安照机构/岗位序号和机构/岗位/人员序号排序
			List allUser = userManager.getOrgUserList(orgid, userId);			
			List noAdminOrCoequalityOrgmanager = null;
			if(accesscontrol.isAdmin()){
				noAdminOrCoequalityOrgmanager = allUser;
			}else{
				if(allUser.size() > 0){
					User user = null;
					noAdminOrCoequalityOrgmanager = new ArrayList();
					for(int i = 0; i < allUser.size(); i++){
						user = (User)allUser.get(i);
						String userIdI = String.valueOf(user.getUserId());
						boolean isCurOrgManager = orgImpl.isCurOrgManager(curUserOrgId,userIdI);
						boolean isAdmin = accesscontrol.isAdminByUserid(userIdI); 
						if(!isAdmin && !isCurOrgManager){
							noAdminOrCoequalityOrgmanager.add(user);
						}
					}
				}
			}
			request.setAttribute("allUser", noAdminOrCoequalityOrgmanager);			
		}
%>
<html>
	<head>
		<title>属性容器</title>
		<SCRIPT LANGUAGE="JavaScript"> 	
		var api = parent.frameElement.api, W = api.opener;
		
			function addRole(){	
			   var n=document.all("userIds").options.length-1;
			    var selectusers = "";

			    var existuser = document.all("userIds").options;
			    var exist = "";
			    
			    if(document.all("allist").options.length < 1){
			    	W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.add.null"/>');
			    	return;
			    }
			   for(var i=0;i<document.all("allist").options.length;i++){
				   var op=document.all("allist").options[i];
				   if(op.selected)
				   {	
				   		if(op.value=="<%=userId%>" && op.value!="1"){
				   			W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.not"/>');
				   			return;
				   		}
				   		var flag2 = false;
				   		for(var j = 0; j < existuser.length; j++){
				   			if(op.text==existuser.options[j].text){
				   				flag2 = true;
				   			}
				   		}
				   		if(!flag2){
					   		if(selectusers==""){ 
					   			selectusers = op.value;
					   		}else {
					   			selectusers += "," + op.value;
					   		}
					   		addone(op.text,op.value,n);		
				   		}else{
				   			if(exist == ""){
				   				exist = op.text;
				   			}else{
				   				exist += "\n" + op.text;
				   			}
				   		}   	
				    	//var retvalue =addone(op.text,op.value,n);
				    	//if(retvalue)
				    	//{
				    	//	if(selectusers == "" )
				    	//	{
					    	//	selectusers  += retvalue;
					    //	}
					    	//else
					    	//{
					    //		selectusers  +="," + retvalue;
					    	//}
					    		
				    //	}else{
				    	//	alert("已授予的用户！");
				    	//	return;
				    	//}
				    	
				   }
			   
			  }
			  if(selectusers != "")
			  {
			  	send_request('savaRoleUser.jsp?roleId='+<%=roleId%>+'&userId='+selectusers+'&tag=add');
			  }else if(exist != ""){
			   		W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.user.had"/>'+"：\n" + exist);
		  			return;
			   }else{
			   		W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.select"/>');
			   		return;
			   }
			  
			}
			function addone(name,value,n){
			   for(var i=n;i>=0;i--){
					if(value==document.all("userIds").options[i].value){
					  return;
					}
				}
			   var op=new Option(name,value);
			   document.all("userIds").add(op);
			   return value;
			   
			}
			function deleteall(){
				var selectusers = "";
				if(document.all("userIds").options.length < 1){
			    	W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.remove.null"/>');
			    	return;
			    }
				for (var m=document.all("userIds").options.length-1;m>=0;m--)
				{
					var retvalue = document.all("userIds").options[m].value
					if(retvalue=="1" && "<%=roleId%>"=="1"){
						W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.remove.cannot.admin"/>');
						return;
					}
					if(retvalue=="<%=userId%>" && retvalue!="1"){
				   		W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.remove.cannot.myself"/>');
				   		return;
				   	}
					if(selectusers == "" )
					{
			    		selectusers  += retvalue;
			    	}
			    	else
			    	{
			    		selectusers  += "," + retvalue;
			    	}
					document.all("userIds").options[m]=null;
				}
				if(selectusers != "")
				{
					send_request('savaRoleUser.jsp?roleId='+<%=roleId%>+'&userId='+selectusers+'&tag=delete');
				}
				else
				{
					W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.remove.select"/>');
				}
			    //changebox();
			}
			function addall(){
				var n=document.all("userIds").options.length-1;
				if(document.all("allist").options.length < 1){
			    	W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.add.null"/>');
			    	return;
			    }
			    var selectusers = "";
			    var existuser = document.all("userIds").options;
			    var exist = "";
			     for(var i=0;i<document.all("allist").options.length;i++){     
				     var op=document.all("allist").options[i];
				     if(op.value=="<%=userId%>" && op.value!="1"){
				   		W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.add.cannot.myself"/>');
				   		return;
				   	 }
				    
				   		var flag2 = false;
				   		for(var j = 0; j < existuser.length; j++){
				   			if(op.text==existuser.options[j].text){
				   				flag2 = true;
				   			}
				   		}
				   		if(!flag2){
					   		if(selectusers==""){ 
					   			selectusers = op.value;
					   		}else {
					   			selectusers += "," + op.value;
					   		}
					   		addone(op.text,op.value,n);		
				   		}else{
				   			if(exist == ""){
				   				exist = op.text;
				   			}else{
				   				exist += "\n" + op.text;
				   			}
				   		}   	
				   //  var retvalue = addone(op.text,op.value,n); 
				   //  if(retvalue)
			    	//{
			    	//	if(selectusers == "" )
			    	//	{
				    //		selectusers  += retvalue;
				    //	}
				    //	else
				    	//{
				    	//	selectusers  +="," + retvalue;
				    	//}	
			    	//}
			    	 
				}
				
				if(selectusers != "")
				{
					send_request('savaRoleUser.jsp?roleId='+<%=roleId%>+'&userId='+selectusers+'&tag=add');
				}
				else if(exist != ""){
			   		W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.had"/>'+"：\n" + exist);
		  			return;
			   }
			   
			}
			function deleterole()
			{
				var selectusers = "";
				if(document.all("userIds").options.length < 1){
			    	W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.remove.null"/>');
			    	return;
			    }
				 for (var m=document.all("userIds").options.length-1;m>=0;m--)
				 {
				      if(document.all("userIds").options[m].selected)
				      {
					    var retvalue = document.all("userIds").options[m].value
					    if(retvalue=="1" && "<%=roleId%>"=="1"){
					    	W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.remove.cannot.admin"/>');
					    	return;
					    }
					    if(retvalue=="<%=userId%>" && retvalue!="1"){
				   			W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.remove.cannot.myself"/>');
				   			return;
				   		}
					   	if(selectusers == "" )
						{
				    		selectusers  += retvalue;
				    	}
				    	else
				    	{
				    		selectusers  += "," + retvalue;
				    	}
				    	document.all("userIds").options[m]=null;
				     }
					      
					 
				  }
				  if(selectusers != "")
				  {
				  	send_request('savaRoleUser.jsp?roleId='+<%=roleId%>+'&userId='+selectusers+'&tag=delete');
				  }
				  else
				  {
				  	W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.remove.select"/>');
				  }
			      
			}
						
			function send_request(url){
				//document.all.divProcessing.style.display = "block";
				
				//document.all("button1").disabled = true;
				//document.all("button2").disabled = true;
				//document.all("button3").disabled = true;
				//document.all("button4").disabled = true;
				//document.all("back").disabled = true;
				
				document.OrgJobForm.action = url;
				document.OrgJobForm.target = "hiddenFrame";
				
				
				document.OrgJobForm.submit();
			}			
		</SCRIPT>
	<body class="contentbodymargin" >
		<div id="contentborder" align="center">
			<center>
				<form name="OrgJobForm" action="" method="post">
					<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table">
					<tr class="tabletop">
						    <td width="45%" align="center">&nbsp;</td>
						    <td width="10%" align="center">&nbsp;</td>
						    <td width="45%" align="center">&nbsp;</td>
						  </tr>
						<tr class="tr">
							<td class="td" width="45%" align="center">
								<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.can"/>
							</td>
							<td width="10%" class="td" align="center">
								&nbsp;
							</td>
							<td width="45%" align="center" class="td">
								<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.had"/>
							</td>
						</tr>
						<tr class="tabletop">
						    <td width="45%" align="center">&nbsp;</td>
						    <td width="10%" align="center">&nbsp;</td>
						    <td width="45%" align="center">&nbsp;</td>
						  </tr>
						<tr class="tr">
							<td class="td" align="center" ><div class="win" id="dd_1" align="left">
								<select class="select" name="allist" multiple style="width:98%" onDBLclick="addRole()" size="18">
									<pg:list requestKey="allUser" needClear="false">
										<option value="<pg:cell colName="userId"/>">
											<pg:cell colName="userRealname" />(<pg:cell colName="userName" />)
										</option>
									</pg:list>
								</select></div>
							</td>

							<td align="center" class="td">
								<table class="table" width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr class="tr">
										<td align="center" class="td">
											<a href="javascript:void(0)" class="bt_2" name="button1" onclick="addRole()"><span>&gt;</span></a>
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											<a href="javascript:void(0)" class="bt_2" name="button2" onclick="addall()"><span>&gt;&gt;</span></a>
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											<a href="javascript:void(0)" class="bt_2" name="button3" onclick="deleteall()"><span>&lt;&lt;</span></a>
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											<a href="javascript:void(0)" class="bt_2" name="button4" onclick="deleterole()"><span>&lt;</span></a>
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
								</table>
							</td>
							<td class="td" align="center" ><div class="win" id="dd_2" align="left">
								<select class="select" name="userIds" multiple style="width:98%" onDBLclick="deleterole()" size="18">
									<pg:list requestKey="existUser" needClear="false">
										<option value="<pg:cell colName="userId"/>">
											<pg:cell colName="userRealname" />(<pg:cell colName="userName" />)
										</option>
									</pg:list>
								</select></div>
							</td>

						</tr>
						<tr class="tabletop">
						    <td  align="center">&nbsp;</td>
						  </tr>
					</table>
				</form>
			</center>
		</div>
	</body>
	<iframe name="hiddenFrame" width=0 height=0></iframe>
</html>
