<%
/*
 * <p>Title: 角色授予用户组的显示处理页面</p>
 * <p>Description: 角色授予用户组的显示处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-25
 * @author liangbing.tao
 * @version 1.0
 */
%>


<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.GroupManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Group" %>
<%@ page import="java.util.List,
				java.util.ArrayList" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>


<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	
	String curUserId = accessControl.getUserID(); 
	
	String roleId = request.getParameter("roleId");
	String groupId = request.getParameter("groupId");
	
	
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	Role role = roleManager.getRoleById(roleId);
	
	String roleName = role.getRoleName();
	
	GroupManager groupManager = SecurityDatabase.getGroupManager();
	Group group = groupManager.getGroupByID(groupId);
	
	List allGroup;
	List existGroup;
	if (groupManager.isContainChildGroup(group)) 
	{
		allGroup = groupManager
				.getGroupList("select * from td_sm_Group o where o.PARENT_ID='"
						+ groupId + "' or o.GROUP_ID='" + groupId + "'");
		existGroup = groupManager
							.getGroupList("select * from td_sm_group o,td_sm_grouprole b where (o.PARENT_ID='"
									+ groupId + "' or o.GROUP_ID='" + groupId + "') and o.group_id=b.group_id"
									+ " and b.role_id='"+roleId+"'");				
	}
	else
	{
		allGroup = groupManager
				.getGroupList("select * from td_sm_Group o where o.GROUP_ID='"
						+ groupId + "'");
		existGroup = groupManager
							.getGroupList("select * from td_sm_group o,td_sm_grouprole b where o.GROUP_ID='"
									+ groupId + "' and o.group_id=b.group_id and b.role_id='"+roleId+"'");					
	}
	
	//List existGroup = groupManager.getGroupList(role);
	List usergroupsetGroup = null;
	if(allGroup.size() > 0){
		usergroupsetGroup = new ArrayList();
		for(int i = 0; i < allGroup.size(); i++){
			Group setGroup = (Group)allGroup.get(i);
			String groupIdSelf = String.valueOf(setGroup.getGroupId()); 
			//if(accessControl.checkPermission(groupIdSelf,"usergroupset",AccessControl.GROUP_RESOURCE)
			//	|| curUserId.equals(String.valueOf(setGroup.getOwner_id()))){
			if(curUserId.equals(String.valueOf(setGroup.getOwner_id()))){//自己创建的组才能设置给角色
				usergroupsetGroup.add(setGroup);
			}
		}
	}
	if(accessControl.isAdmin()){
		request.setAttribute("allGroup", allGroup);
	}else{
		request.setAttribute("allGroup", usergroupsetGroup);
	}
	request.setAttribute("existGroup", existGroup);
			
%>
<html>
<head>    
  <title>属性容器</title>
	<SCRIPT LANGUAGE="JavaScript"> 
	var api = parent.frameElement.api, W = api.opener;
	
	function addGroup()
	{	
	   var n=document.all("groupId").options.length-1;
	   
	    var selectgroups = "";
	    var existgroup = document.all("groupId").options;
	    var exist = "";
	    
	    if(document.all("allist").options.length < 1)
	    {
	    	W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.usergroup.add.null"/>');
	    	return;
	    }
	    
	    for(var i=0;i<document.all("allist").options.length;i++)
	    {
		   var op=document.all("allist").options[i];
		   if(op.selected)
		   {	
			   var flag2 = false;
		   		for(var j = 0; j < existgroup.length; j++){
		   			if(op.text==existgroup.options[j].text){
		   				flag2 = true;
		   			}
		   		}
		   		if(!flag2){
			   		if(selectgroups==""){ 
			   			selectgroups = op.value;
			   		}else {
			   			selectgroups += "," + op.value;
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
		    //	if(retvalue)
		    //	{
		    	//	if(selectgroups == "" )
		    	//	{
			    	//	selectgroups  += retvalue;
			    	//}
			    //	else
			    	//{
			    		//selectgroups  +="," + retvalue;
			    	//}
			    		
		    	//}
		    	//else
		    	//{
		    	//	alert("已授予的用户组！");
		    	//	return;
		    	//}	
		   }
	   
	  }
	  if(selectgroups != "")
	  {
	  	send_request('savaRoleGroup.jsp?roleId='+<%=roleId%>+'&groupId='+selectgroups+'&tag=add');
	  }
	  else if(exist != ""){
	   		W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.usergroup.role.had"/>'+"：\n" + exist);
			return;
	   }else{
	   		W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.usergroup.select"/>');
	   		return;
	   }		  
	}
	
	function addone(name,value,n)
	{
	   for(var i=n;i>=0;i--)
	   {
			if(value==document.all("groupId").options[i].value)
			{
			  return;
			}
		}
	   var op=new Option(name,value);
	   document.all("groupId").add(op);
	   return value;	   
	}
	
	function deleteall()
	{
		var leftLen = document.all("allist").length;
		var selectgroups= "";
		if(document.all("groupId").options.length < 1){
	    	W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.usergroup.remove.null"/>');
	    	return;
	    }
	    var infos = "";
		for (var m=document.all("groupId").options.length-1;m>=0;m--)
		{
			var flag1 = false;
			var retvalue = document.all("groupId").options[m].value ;
			var optionText = document.all("groupId").options[m].text;
			for(var i = 0; i < leftLen; i++){
				if(retvalue==document.all("allist").options[i].value){
					if(selectgroups== "" ){
			    		selectgroups  += retvalue;
			    	}else{
			    		selectgroups  += "," + retvalue;
			    	}
					document.all("groupId").options[m]=null;
					flag1 = true;
				}
			}
			if(!flag1){
				if(infos!=""){
					infos += "\n" + optionText; 
				}else{
					infos = optionText;
				}
			}
		}
		if(selectgroups != ""){
			send_request('savaRoleGroup.jsp?roleId='+<%=roleId%>+'&groupId='+selectgroups+'&tag=delete');
		}else{
			if(infos!=""){
				W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.usergroup.nopurview"/>'+"：\n"+infos);
				return false;
			}else{
				W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.usergroup.remove.select"/>');
				return false;
			}
		}
	}
	
	
	function addall()
	{
		var n=document.all("groupId").options.length-1;
		
		if(document.all("allist").options.length < 1)
		{
	    	W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.usergroup.add.null"/>');
	    	return;
	    }
	    var selectgroups = "";
	    
	    for(var i=0;i<document.all("allist").options.length;i++)
	     {     
		     var op=document.all("allist").options[i];
		     var existgroup = document.all("groupId").options;
			    var exist = "";

		     var flag2 = false;
		   		for(var j = 0; j < existgroup.length; j++){
		   			if(op.text==existgroup.options[j].text){
		   				flag2 = true;
		   			}
		   		}
		   		if(!flag2){
			   		if(selectgroups==""){ 
			   			selectgroups = op.value;
			   		}else {
			   			selectgroups += "," + op.value;
			   		}
			   		addone(op.text,op.value,n);		
		   		}else{
		   			if(exist == ""){
		   				exist = op.text;
		   			}else{
		   				exist += "\n" + op.text;
		   			}
		   		}   	
		     
		     //var retvalue = addone(op.text,op.value,n); 
		   // if(retvalue)
	    	//{
	    		//if(selectgroups == "" )
	    		//{
		    	//	selectgroups  += retvalue;
		    	//}
		    	//else
		    	//{
		    	//	selectgroups  +="," + retvalue;
		    	//}	
	    	//}
	    	 
		}
		
		if(selectgroups != "")
		{
			send_request('savaRoleGroup.jsp?roleId='+<%=roleId%>+'&groupId='+selectgroups+'&tag=add');
		}else if(exist != ""){
	   		W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.usergroup.role.had"/>'+"：\n" + exist);
			return;
	   }
	   
	}
	
	function deleteGroup()
	{
		var leftLen = document.all("allist").length;
		var selectgroups = "";
		if(document.all("groupId").options.length < 1){
	    	W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.usergroup.remove.null"/>');
	    	return;
	    }
		if(document.all("groupId").selectedIndex==-1){
			W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.usergroup.remove.select"/>');
			return;
		}
	    var infos = "";
		for (var m=document.all("groupId").options.length-1;m>=0;m--){
			var flag1 = false;		 	
    		if(document.all("groupId").options[m].selected){
   				var retvalue = document.all("groupId").options[m].value;
				var optionText = document.all("groupId").options[m].text;
				for(var i = 0; i < leftLen; i++){
					if(retvalue==document.all("allist").options[i].value){
		  				if(selectgroups == "" ){
		  					selectgroups  += retvalue;
		  				}else{
		  					selectgroups  += "," + retvalue;
		  				}
		  				document.all("groupId").options[m]=null;
		  				flag1 = true;
		  			}
  				}
  				if(!flag1){
  					if(infos!=""){
  						infos += "\n" + optionText;
  					}else{
  						infos = optionText;
  					}
  				}
     		}
		}
		  
		if(selectgroups != ""){
		  	send_request('savaRoleGroup.jsp?roleId='+<%=roleId%>+'&groupId='+selectgroups+'&tag=delete');
		}else{
			if(infos!=""){
				W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.usergroup.nopurview"/>'+"：\n"+infos);
				return false;
			}else{
		  		W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.usergroup.remove.select"/>');
		  	}
		}
	      
	}
					
	function send_request(url)
	{
		//document.all.divProcessing.style.display = "block";
		
		//document.all("button1").disabled = true;
		//document.all("button2").disabled = true;
		//document.all("button3").disabled = true;
		//document.all("button4").disabled = true;
		
		//document.all("back").disabled = true;
		
		document.all.RoleGroupForm.action = url;
		document.all.RoleGroupForm.target = "hiddenFrame";
		document.all.RoleGroupForm.submit();
	}	
	</SCRIPT>
 </head>
<body class="contentbodymargin" scroll="no">
	<div id="contentborder">
		<center>
			<form name="RoleGroupForm"  method="post" >
				<table width="80%" border="0" cellpadding="0" cellspacing="1">
					<tr class="tabletop">
					    <td width="40%" align="center">&nbsp;</td>
					    <td width="20%" align="center">&nbsp;</td>
					    <td width="40%" align="center">&nbsp;</td>
					 </tr>
					  <tr class="tabletop">
					    <td width="40%" align="center"><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.usergroup.can"/></td>
					    <td width="20%" align="center">&nbsp;</td>
					    <td width="40%" align="center"><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.usergroup.had"/></td>
					  </tr>
					  <tr class="tabletop">
					    <td width="40%" align="center">&nbsp;</td>
					    <td width="20%" align="center">&nbsp;</td>
					    <td width="40%" align="center">&nbsp;</td>
					  </tr>
					  <tr>
					     <td  align="right" >
					     	<select name="allist"  multiple style="width:80%" onDBLclick="addGroup()" size="18">
									  <pg:list requestKey="allGroup">
										<option value="<pg:cell colName="groupId"/>"><pg:cell colName="groupName"/></option>
									  </pg:list>			
							</select>
						 </td>				  	  	
					     <td align="center">
					     	<table width="100%" border="0" cellspacing="0" cellpadding="0">
					      		<tr>
					        		<td align="center">
					        			<a href="javascript:void(0)" class="bt_2" name="button1" onclick="addGroup()"><span>&gt;</span></a>
					        		</td>
					      		</tr>
							    <tr>
							       <td align="center">&nbsp;</td>
							    </tr>
						      	<tr>
						        	<td align="center">
						        		<a href="javascript:void(0)" class="bt_2" name="button2" onclick="addall()"><span>&gt;&gt;</span></a>
						        	</td>
						      	</tr>
   						        <tr>
						        	<td align="center">&nbsp;</td>
						        </tr>
							    <tr>
							        <td align="center">
							        	<a href="javascript:void(0)" class="bt_2" name="button3" onclick="deleteall()"><span>&lt;&lt;</span></a>
							        </td>
							    </tr>
							    <tr>
							        <td align="center">&nbsp;</td>
							    </tr>
							    <tr>
				        			<td align="center">
				        				<a href="javascript:void(0)" class="bt_2" name="button4" onclick="deleteGroup()"><span>&lt;</span></a>
				        			</td>
				      			</tr>
							    <tr>
							        <td align="center">&nbsp;</td>
							    </tr>
				    		</table>
				    	</td>
					    <td >
					     <select name="groupId"  multiple style="width:80%" onDBLclick="deleteGroup()" size="18">
									  <pg:list requestKey="existGroup">
										<option value="<pg:cell colName="groupId"/>"><pg:cell colName="groupName"/></option>
									  </pg:list>			
						 </select>														
						</td>				 								  
				  	</tr>
					<tr class="tabletop">
					    <td width="40%" align="center">&nbsp;</td>
					</tr>
				</table>
			</form>
		</center>
	</div>

</body>
	<iframe name="hiddenFrame" width=0 height=0></iframe>
</html>
