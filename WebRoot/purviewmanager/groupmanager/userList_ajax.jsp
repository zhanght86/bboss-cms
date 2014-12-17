<%
/*
 * <p>Title: 隶属用户处理的前台界面</p>
 * <p>Description: 隶属用户处理的前台界面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-18
 * @author liangbing.tao
 * @version 1.0
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.GroupManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Group,
				com.frameworkset.platform.sysmgrcore.entity.User" %>
<%@ page import="java.util.List,
				java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl"  %>				

<%
	AccessControl accesscontrol = AccessControl.getInstance();
    accesscontrol.checkManagerAccess(request,response);
    
    String curUserOrgId = accesscontrol.getChargeOrgId(); 
    OrgManagerImpl orgImpl = new OrgManagerImpl();
	String groupId = (String)request.getSession().getAttribute("curGroupId");
	String orgid = request.getParameter("orgId");
	GroupManager groupManager=SecurityDatabase.getGroupManager();
	List allUserList=groupManager.getUserList(groupId,orgid,null);
	Group group = groupManager.getGroupByID(groupId);
	UserManager userManager = SecurityDatabase.getUserManager();
	List existUser = null;
	StringBuffer existUserSql = new StringBuffer()
		.append("select * from td_sm_user a,td_sm_usergroup b where a.user_id=b.user_id and b.group_id='")
		.append(group.getGroupId()).append("' and a.user_id in(")
		.append("select user_id from td_sm_userjoborg where org_id='").append(orgid)
		.append("')"); 
	
	//if(accesscontrol.isAdmin()){
	//	existUser = userManager.getUserList(group);
	//}else{
		existUser = userManager.getUserList(existUserSql.toString());
	//}
	List userList = null;
	if(accesscontrol.isAdmin()){
		userList = allUserList;
	}else{
		if(allUserList.size() > 0){
			userList = new ArrayList();
			User user = null;
			for(int i = 0; i < allUserList.size(); i++){
				user = (User)allUserList.get(i);
				String userIdI = String.valueOf(user.getUserId());
				boolean isCurOrgManager = orgImpl.isCurOrgManager(curUserOrgId,userIdI);
				boolean isAdmin = accesscontrol.isAdminByUserid(userIdI); 
				if(!isAdmin && !isCurOrgManager){
					userList.add(user);
				}
			}
		}
	}
	List existUserList = null;
	if(accesscontrol.isAdmin()){
		existUserList = existUser;
	}else{
		if(existUser.size() > 0){
			existUserList = new ArrayList();
			User user = null;
			for(int i = 0; i < existUser.size(); i++){
				user = (User)existUser.get(i);
				String userIdI = String.valueOf(user.getUserId());
				boolean isCurOrgManager = orgImpl.isCurOrgManager(curUserOrgId,userIdI);
				boolean isAdmin = accesscontrol.isAdminByUserid(userIdI); 
				if(!isAdmin && !isCurOrgManager){
					existUserList.add(user);
				}
			}
		}
	}
	request.setAttribute("existUser",existUserList);
	request.setAttribute("allUser",userList);
%>
<html>
	<head>
		<title>属性容器</title>
		<script language="javascript" src="<%=request.getContextPath()%>/include/dragdiv.js"></script>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
		<script language="JavaScript">
		var api = parent.frameElement.api, W = api.opener;
  		var allowSubmit=false; 
		//添加选中的角色
		function addUser()
		{	
			var alreadyUser = document.all("userIds");
   			var selectUserIds="";
   			var ch = false;
   			var selectedDrop=document.all("userIds");
   			var count=document.all("allist").options.length;
   			var already = "";
   			for(var i=0;i<count;i++)
   			{
   				//
   				var op=document.all("allist").options[i];
   				if(op.selected)
   				{
   	   				var flag = false;
   	   				for(var j=0;j< alreadyUser.length;j++)
   	   				{
   	   	   				if(op.text == alreadyUser.options[j].text)
   	   	   				{
   	   	   			        flag = true;
   	   	   				}
   	   	   	   				
   	   				}
   					//	
   					if(!flag)
   					{
   						addone(op.text,op.value);
   					
					
					ch = true;
					//
					if(allowSubmit)selectUserIds+=","+op.value;
   					}
   					else
   					{
   						if(already=="") already = already + op.text;
   						else already = already + "\n" + op.text;
   					}
				}
   			}
   		//	if(!ch )
	  //	{
	//  		alert("没有选择要添加的用户名称");
	  //		return;
	//  	}
   			
   			if(selectUserIds.length>0)send_request(selectUserIds,'add');
   			else if (already != "")
   			{
   				W.$.dialog.alert("<pg:message code='sany.pdp.groupmanage.some.user.exist'/>：\n" + already,function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
   			  	return;
   			}
   			else
   			{
   				W.$.dialog.alert("<pg:message code='sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.add.null'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
   		  		return;
   			}
		}
		
		//添加所有的角色
		function addall()
		
		{
    		var selectUserIds="";
    		var alreadyUser = document.all("userIds");
    		var already = "";
			var count=document.all("allist").options.length;	
			if(count == 0)
	{
		parent.parent.$.dialog.alert("<pg:message code='sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.add.null'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	}
			
    		for(var i=0;i<count;i++)
    		{
     			var op=document.all("allist").options[i];
     			var flag = false;
	   				for(var j=0;j< alreadyUser.length;j++)
	   				{
	   	   				if(op.text == alreadyUser.options[j].text)
	   	   				{
	   	   			        flag = true;
	   	   				}
	   	   	   				
	   				}
					//	
					if(!flag)
					{
						addone(op.text,op.value);
					
				
				//ch = true;
				//
				if(allowSubmit)selectUserIds+=","+op.value;
					}
					else
					{
						if(already=="") already = already + op.text;
						else already = already + "\n" + op.text;
					}
     			addone(op.text,op.value);  
     			if(allowSubmit)selectUserIds+=","+op.value;
    		}
   			if(selectUserIds.length>0)send_request(selectUserIds,'add');
   			else if (already != "")
   			{
   				parent.parent.parent.$.dialog.alert("<pg:message code='sany.pdp.groupmanage.some.user.exist'/>：\n" + already,function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
   			  	return;
   			}
		}
		//将列表框中的选项进行追加
		function addone(name,value)
		{
   			allowSubmit=true;
   			for(var i=0;i<document.all("userIds").options.length;i++)
  			{
	 			if(value==document.all("userIds").options[i].value)
	 			{
	 				allowSubmit=false;
	 				break;
	 			}
  			}
   			if(allowSubmit)document.all("userIds").add(new Option(name,value));
		}
		//删除选中的角色
		function deleteUser()
		{
    		var selectUserIds="";
    		var ch = false;
 			for(var m=document.all("userIds").options.length-1;m>=0;m--)
 			{
	    		if(document.all("userIds").options[m].selected)
	    		{
	    			selectUserIds+=","+document.all("userIds").options[m].value;
	    			document.all("userIds").options.remove(m);
	    		}
			}
			if(selectUserIds.length>0)send_request(selectUserIds,'delete');
			else
			{
			W.$.dialog.alert("<pg:message code='sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.remove.select'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			}
		}
		//删除所有的角色
		function deleteall()
		{
    		var selectUserIds="";
    		var count=document.all("userIds").options.length;
    		if(count == 0)
	{
		W.$.dialog.alert("<pg:message code='sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.remove.null'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	}
    		
			for (var m=0;m<count;m++)
			{
	    		selectUserIds+=","+document.all("userIds").options[m].value;
			}
			if(selectUserIds.length>0)
			{
				document.all("userIds").innerHTML="";
    			send_request(selectUserIds,'delete');
   			}	
		}
		//将参数提交至隐藏帧进行处理
		function send_request(selectUserIds,flag)
		{
    		selectUserIds=selectUserIds.substring(1,selectUserIds.length);
			document.OrgJobForm.action = 'editgroupuser.jsp?userId='+selectUserIds+'&groupId='+<%=groupId%>+'&flag='+flag;
			document.OrgJobForm.target = "hiddenFrame";
			document.OrgJobForm.submit();
		}
		</script>
	<body class="contentbodymargin" onload="loadDragDiv()">
		<div id="" align="center">
			<center>
				<form name="OrgJobForm" action="" method="post">
					<table width="80%" border="0" cellpadding="0" cellspacing="1" class="table">
						<tr class="tabletop">
						    <td width="40%" align="center">&nbsp;</td>
						    <td width="20%" align="center">&nbsp;</td>
						    <td width="40%" align="center">&nbsp;</td>
						  </tr>
						<tr class="tr">
							<td class="td" width="40%" align="center">
								<pg:message code="sany.pdp.workflow.wait.user.list"/>
							</td>
							<td width="20%" class="td" align="center">
								&nbsp;
							</td>
							<td width="40%" align="center" class="td">
								<pg:message code="sany.pdp.workflow.choose.user.list"/>
							</td>
						</tr>
						<tr class="tabletop">
						    <td width="40%" align="center">&nbsp;</td>
						    <td width="20%" align="center">&nbsp;</td>
						    <td width="40%" align="center">&nbsp;</td>
						  </tr>
						<tr class="tr">
							<td class="td" align="right"><div class="win" id="dd_1" align="left">
								<select class="select" name="allist" multiple style="width:98%" onDBLclick="addUser()" size="18">
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
											<a class="bt_2" onclick="addUser()"><span>&gt;</span></a>
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											<a class="bt_2"  onclick="addall()"><span>&gt;&gt;</span></a>
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											<a class="bt_2"  onclick="deleteall()"><span>&lt;&lt;</span></a>
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											<a class="bt_2"  onclick="deleteUser()"><span>&lt;</span></a>
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
								</table>
							</td>
							<td class="td"><div class="win" id="dd_2" align="left">
								<select class="select" name="userIds" multiple style="width:98%" onDBLclick="deleteUser()" size="18">
									<pg:list requestKey="existUser" needClear="false">
										<option value="<pg:cell colName="userId"/>">
											<pg:cell colName="userRealname" />(<pg:cell colName="userName" />)
										</option>
									</pg:list>
								</select></div>
							</td>
						</tr>						
					</table>
					<input type="hidden" name="orgId" value="<%=orgid%>" />
					<div align="center">
						<br/>
						<br/>
						<a class="bt_2" onclick="parent.closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
					</div>
				</form>
			</center>
		</div>
		<div id=divProcessing style="width:200px;height:30px;position:absolute;left:150px;top:450px;display:none">
			<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
			    <tr>
				    <td bgcolor=#3A6EA5>
					    <marquee align="middle" behavior="alternate" scrollamount="5">
						    <font color=#FFFFFF>...处理中...请等待...</font>
						</marquee>
					</td>
				</tr>
			</table>
		</div>
	</body>
	<iframe name="hiddenFrame" width=0 height=0></iframe>
</html>

