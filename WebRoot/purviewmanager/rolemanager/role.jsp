<%
/**
 * <p>Title: 角色主页面</p>
 * <p>Description: 角色主页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-18
 * @author da.wei
 * @version 1.0
 */
%>

<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenu"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuImpl"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.Menu"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.RoleType"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleTypeManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="java.util.List"%>
<%@ page import="com.frameworkset.platform.config.ConfigManager"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
	
	String roleName = request.getParameter("roleName");
	roleName = roleName == null ? "" :roleName;
	
	String roleTypeName = request.getParameter("roleTypeName");
	roleTypeName = roleTypeName == null ? "" : roleTypeName;
	
	
	String roleDesc = request.getParameter("roleDesc");
	roleDesc = roleDesc == null ? "" : roleDesc;
	String creatorName = request.getParameter("creatorName");
	creatorName=creatorName==null ? "" : creatorName;
	String rootpath = request.getContextPath();
%>

<!--
	用户成员功能改为角色授予用户，去掉此列，加入右键。
	去掉权限列，加入右键。
	将角色权限复制加入右键。
-->

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	
		<script language="JavaScript" src="../scripts/func.js" type="text/javascript"></script>
		
		<title><pg:message code="sany.pdp.purviewmanager.title"/></title>
		<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
		
		<script language="javascript">
			var roleTypeFrame;
		
			function lookRoleInfo(roleId){
				if(roleId != null && roleId != ""){
					var url="<%=request.getContextPath()%>/purviewmanager/rolemanager/lookRoleInfo.jsp?roleId=" + roleId;
				 	 $.dialog({title:'<pg:message code="sany.pdp.purviewmanager.rolemanager.role.view"/>',width:280,height:220, content:'url:'+url});  
				 	 
				 	 /*
					var ww = openWin('lookRoleInfo.jsp?roleId='+roleId,screen.availWidth-600,screen.availHeight-300);
					if(ww)
					{
						location.reload();
					}
					*/
				}
			}
			
			function roleInfo(roleId){
				if(roleId != null && roleId != ""){
					var url="<%=request.getContextPath()%>/purviewmanager/rolemanager/modifyRole.jsp?roleId=" + roleId;
				 	 $.dialog({title:'<pg:message code="sany.pdp.purviewmanager.rolemanager.role.modfiy"/>',width:280,height:220, content:'url:'+url});  
					
					/*
					var ww = openWin('modifyRole.jsp?roleId='+roleId,screen.availWidth-600,screen.availHeight-300);
					if(ww)
					{
						//window.location.href = "role.jsp" ;
						window.location.reload();
					}
					*/
				}
			}
			
			//角色资源授予
			function rolePurview(roleId){
				if(roleId != null && roleId != ""){
					var url="<%=request.getContextPath()%>/purviewmanager/grantmanager/role_resFrame.jsp?roleId=" + roleId;
					$.dialog({title:'<pg:message code="sany.pdp.sys.purview.authorize"/>',width:screen.availWidth,height:screen.availHeight, content:'url:'+url}); 
					
					//winresgrant = window.showModalDialog("../grantmanager/role_resFrame.jsp?roleId="+ roleId ,window,"dialogWidth:"+screen.availWidth+";dialogHeight:"+screen.availHeight+";help:no;scroll:auto;status:no");
				}
			}
			
			//角色批量资源授予
			function roleBatchPurview(){
				var roleIds = "";
				var arr = document.getElementsByName("ID");
				for(var i = 0; i < arr.length; i++){
					if(arr[i].checked){
						if(roleIds==""){
							roleIds = arr[i].value;
						}else{
							roleIds += "," + arr[i].value;
						}
					}
				}
				if(roleIds.split(",").length > 1){
					var url="<%=request.getContextPath()%>/purviewmanager/grantmanager/roleBatch_resFrame.jsp?roleIds=" + roleIds;
					$.dialog({title:'<pg:message code="sany.pdp.purviewmanager.rolemanager.role.purview.authorize.batch"/>',width:screen.availWidth-200,height:screen.availHeight-200, content:'url:'+url}); 
					
					//winresgrant = window.showModalDialog("../grantmanager/roleBatch_resFrame.jsp?roleIds="+roleIds ,window,"dialogWidth:"+screen.availWidth+";dialogHeight:"+screen.availHeight+";help:no;scroll:auto;status:no");
				}else{
					$.dialog.alert('<pg:message code="sany.pdp.common.operation.select.two"/>');
					return false;
				}
			}
			
			function copyPurview(roleId){
				if(roleId != null && roleId != ""){
					//winresgrant = window.showModalDialog('purviewRoleCopy.jsp?&roleId='+roleId,window,"dialogWidth:"+(screen.availWidth-200)+";dialogHeight:"+(screen.availHeight-200)+";help:no;scroll:auto;status:no");
					
					var url="<%=request.getContextPath()%>/purviewmanager/rolemanager/purviewRoleCopy.jsp?roleId=" + roleId;
					$.dialog({title:'<pg:message code="sany.pdp.sys.purview.copy"/>',width:850,height:650, content:'url:'+url}); 
					
					//window.showModalDialog("purviewRoleCopy.jsp?&roleId="+ roleId,"","dialogWidth:"+(850)+"px;dialogHeight:"+(650)+"px;help:no;scroll:auto;status:no");
				}
			}
			function reclaimPurview(roleId){
				if(roleId != null && roleId != ""){
					$.dialog.confirm('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.recycle.confirm"/>', function() {
						document.form1.action = "../reclaimManager/reclaimRoleRes_do.jsp?roleId="+roleId;
				    	document.form1.target = "hiddenFrame";
				    	document.form1.submit();
					});
				}
			}
			//2008-3-27
			//baowen.liu
			function roleResList(roleId){
				if(roleId != null && roleId != ""){
					//openWin('roleResFrame.jsp?&roleId='+roleId,screen.availWidth-300,screen.availHeight-300);
				  var typeName = "role";
				  
				  var url="<%=request.getContextPath()%>/purviewmanager/rolemanager/roleres_queryframe.jsp?roleId=" + roleId + "&typeName="+typeName;
				  $.dialog({title:'<pg:message code="sany.pdp.sys.purview.search"/>',width:1000,height:600, content:'url:'+url}); 
				  
			     //window.showModalDialog("roleres_queryframe.jsp?roleId="+ roleId+"&typeName="+typeName,"","dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no");
	
		
				
				}
			}
			
			function rightUserList(roleId){
				if(roleId != null && roleId != ""){
					var url="<%=request.getContextPath()%>/purviewmanager/rolemanager/role2user.jsp?roleId=" + roleId;
				 	 $.dialog({title:'<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user"/>',width:screen.availWidth-100,height:screen.availHeight-100, content:'url:'+url});  
				 	 
					//openWin('role2user.jsp?&roleId='+roleId,screen.availWidth-100,screen.availHeight-100);
				}
			}
			
			function rightUserGroupList(roleId){
				if(roleId != null && roleId != ""){
					var url="<%=request.getContextPath()%>/purviewmanager/rolemanager/role2group.jsp?roleId=" + roleId;
				 	 $.dialog({title:'<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.usergroup"/>',width:screen.availWidth-200,height:screen.availHeight-200, content:'url:'+url});  
					
					//openWin('role2group.jsp?&roleId='+roleId,screen.availWidth-200,screen.availHeight-200);
				}
			}
			
			function rightOrgList(roleId){
				if(roleId != null && roleId != ""){
					var url="<%=request.getContextPath()%>/purviewmanager/rolemanager/role2org.jsp?roleId=" + roleId;
				 	 $.dialog({title:'<pg:message code="sany.pdp.purviewmanager.rolemanager.authorize.role.to.org"/>',width:screen.availWidth-200,height:screen.availHeight-200, content:'url:'+url});  
					
					//openWin('role2org.jsp?&roleId='+roleId,screen.availWidth-200,screen.availHeight-200);
				}
			}
			
			function rightOrgJobList(roleId){
				if(roleId != null && roleId != ""){
					var url="<%=request.getContextPath()%>/purviewmanager/rolemanager/role2orgJob.jsp?roleId=" + roleId;
				 	 $.dialog({title:'<pg:message code="sany.pdp.purviewmanager.rolemanager.authorize.role.to.post"/>',width:screen.availWidth-200,height:screen.availHeight-200, content:'url:'+url});  
					
					//openWin('role2orgJob.jsp?&roleId='+roleId,screen.availWidth-200,screen.availHeight-200);
				}
			}
			
			
			function addRole(){
				var url="<%=request.getContextPath()%>/purviewmanager/rolemanager/addRole.jsp?flag=0";
			 	 $.dialog({title:'<pg:message code="sany.pdp.purviewmanager.rolemanager.role.add"/>',width:280,height:220, content:'url:'+url});   
			 	 
			 	 /*
				var ww = openWin('addRole.jsp?flag=0',screen.availWidth-600,screen.availHeight-300)
				if(ww)
				{
					location.reload();
				}
			 	 */
			}
			function roleTypeManage(){
				var url="<%=request.getContextPath()%>/purviewmanager/rolemanager/roletype.jsp";
				roleTypeFrame = $.dialog({title:'<pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.manager"/>',width:screen.availWidth-200,height:screen.availHeight-300, content:'url:'+url}); 
			 	 
			 	 /*
				var ww = openWin('roletype.jsp',screen.availWidth-200,screen.availHeight-300);
				if(ww)
				{
					location.reload();
				}
				*/
			}
			
			function reclaimRolesRes(){
				var IDs = document.getElementsByName("ID");
				
				var size = 0;
				
				for(var i=0; i<IDs.length; i++)
				{
					if(IDs[i].checked)
					{
						size ++ ;
					}
				}
				
				if(size > 1)
				{
					$.dialog.confirm('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.recycle.confirm"/>', function(){
						 document.form1.action = "../reclaimManager/reclaimRoleRes_do.jsp";
						 document.form1.target = "hiddenFrame";
						  document.form1.submit();
					});
					/*
					if(!confirm("确定要回收角色的资源吗?"))
					{
			        	return false;
			    	}
					
					
				    document.form1.action = "../reclaimManager/reclaimRoleRes_do.jsp";
				    document.form1.target = "hiddenFrame";
				    document.form1.submit();
					*/
				}	
				else
				{
					$.dialog.alert('<pg:message code="sany.pdp.common.operation.select.two"/>');
				}			
			}
			
			function deleteRole()
			{
				var all = document.getElementsByName("ID");
				var docidStr="";
				for(var i=0;i<all.length;i++)
				{
					if(all[i].checked == true)
					{
						if(all[i].value==1 || all[i].value==2 || all[i].value==3 || all[i].value==4)
						{
							$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.remove.cannot"/>');
							return false;			
						}
						docidStr = docidStr + all[i].value + ";";
					}
				}
				if(haveSelect('ID'))
				{		
					$.dialog.confirm('<pg:message code="sany.pdp.common.operation.remove.confirm"/>', function(){
						form1.action = "role_delete.jsp?role_id=" + docidStr;
						form1.target = "";
						form1.submit();	
					});
					
					/*
					if(confirm('您确定要删除所选的角色？！'))
					{								
						//document.all.divProcessing.style.display = "block";
					  	form1.action = "role_delete.jsp?role_id=" + docidStr;
						form1.target = "";
						form1.submit();	
					}
					else
					{
					  return false;
					}
					*/
				
				}else{
					$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.remove.select"/>');
			    	return false;
			   	}
			   	return true;
			}
			
			//角色授予查询对象（对用户、机构、用户组）
			function roleGrantList(roleId)
			{
				if(roleId != null && roleId != ""){
					var url="<%=request.getContextPath()%>/purviewmanager/rolemanager/rolegrantframe.jsp?&roleId="+roleId;
				 	 $.dialog({title:'<pg:message code="sany.pdp.purviewmanager.rolemanager.authorize.role.search"/>',width:screen.availWidth-100,height:screen.availHeight-100, content:'url:'+url}); 
					
					//openWin('rolegrantframe.jsp?&roleId='+roleId,screen.availWidth-100,screen.availHeight-100);
				}
			}
			
			function querySubmit()
			{
				
				form1.action = "role.jsp";
				form1.submit();
			}
			
			function clearInfo()
			{
				document.getElementById("roleName").value="";
				document.getElementById("roleTypeName").options[0].selected = true;
				document.getElementById("roleDesc").value="";
				document.getElementById("creatorName").value="";
			}
			
			function refreshrolecache(){
				document.forms[0].action = "refreshrolecache.jsp";
				document.forms[0].target = "hiddenFrame";
				document.forms[0].submit();
			}
		</script>
	</head>
	<body>
		<form id="form1" name="form1" method="post">
	    <div id="searchblock">
	    	<div  class="search_top">
	    		<div class="right_top"></div>
	    		<div class="left_top"></div>
      		</div>
	    <div class="search_box">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="left_box"></td>
							<td>
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="table2">
									<tr>
										<td >
											<div align="center">
												<pg:message code="sany.pdp.purviewmanager.rolemanager.role.name"/>：<input type="text" id="roleName" name="roleName" value="<%=roleName%>" size=10  class="w120"/>
											</div>
										</td>
										<td >
											<div align="center">
												<pg:message code="sany.pdp.purviewmanager.rolemanager.role.type"/>：
												<select name="roleTypeName" id="roleTypeName" >
													<option value="" >--<pg:message code="sany.pdp.common.operation.select"/>--</option>
												<%
													List typenamelist = null;
													RoleTypeManager rtm = new RoleTypeManager();
													typenamelist = rtm.getTypeNameList();
													if(typenamelist != null)
													{
														for(int i=0;i<typenamelist.size();i++)
														{
															RoleType rt = (RoleType)typenamelist.get(i);
															
												%>
													<option value="<%=rt.getRoleTypeID()%>" <%
														if(roleTypeName.equals(rt.getRoleTypeID()))
														{
														%>
															selected="selected"
														<%
														}
														%>>
															<%=rt.getTypeName()%>
													</option>
												<%				
														}
													}
												%>
												</select>
											</div>
										</td>
										<td >
											<div align="center">
												<pg:message code="sany.pdp.purviewmanager.rolemanager.role.description"/>：<input type="text" name="roleDesc" value="<%=roleDesc%>" id="roleDesc" size=30 class="w120"/>
											</div>
										</td>
										<td >
											<div align="center">
												<pg:message code="sany.pdp.purviewmanager.rolemanager.role.creator"/>：<input type="text" id="creatorName" name="creatorName" value="<%=creatorName%>" size=10 class="w120"/>
											</div>
										</td>
										<td >
											<div align="center">
												<a href="javascript:void" class="bt_1"  name="sub"  onClick="querySubmit()"><span><pg:message code="sany.pdp.common.operation.search"/></span></a>
												<a href="javascript:void" class="bt_2"  name="clear"  onClick="clearInfo()"><span><pg:message code="sany.pdp.common.operation.clear"/></span></a>
											</div>
										</td>
									</tr>
								</table>
							</td>
							<td class="right_box"></td>
						</tr>
					</table>
	    </div>
	    <div class="search_bottom">
				<div class="right_bottom"></div>
				<div class="left_bottom"></div>
		</div>
	  </div>
	  </form>
	    <div class="title_box">
		<div class="rightbtn">
			<%
			  //查看授权与否
			if (control.checkPermission("globalrole",
                          "rolemanager", AccessControl.ROLE_RESOURCE)){
			%>
					<a href="javascript:void" onclick="addRole()" class="bt_small" ><span><pg:message code="sany.pdp.purviewmanager.rolemanager.role.add"/></span></a> 
					<a href="javascript:void" onclick="deleteRole()" class="bt_small" ><span><pg:message code="sany.pdp.purviewmanager.rolemanager.role.remove"/></span></a> 
					<a href="javascript:void" onclick="roleTypeManage()" class="bt_small" ><span><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.manage"/></span></a>
					<a href="javascript:void" onclick="reclaimRolesRes()" class="bt_small" ><span><pg:message code="sany.pdp.purviewmanager.rolemanager.role.purview.recycle.batch"/></span></a>
					<a href="javascript:void" onclick="roleBatchPurview()" class="bt_small" ><span><pg:message code="sany.pdp.purviewmanager.rolemanager.role.purview.authorize.batch"/></span></a>
			<%
			}
			if(control.isAdmin()){
			%>
				<a href="javascript:void" onclick="refreshrolecache()" class="bt_small" ><span><pg:message code="sany.pdp.purviewmanager.rolemanager.role.cache.ref"/></span></a>
			<%} %>
		</div>
		<strong><pg:message code="sany.pdp.common.data.list"/></strong>
	</div>
	
	<div id="changeColor">
		<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.purviewmanager.tag.RoleSearchList" keyName="RoleSearchList" />
		<!--分页显示开始,分页标签初始化-->
		<pg:pager maxPageItems="10" id="RoleSearchList" scope="request" data="RoleSearchList" isList="false">
			<pg:param name="roleId"/>
			<pg:param name="roleName" value="<%=roleName%>"/> 
			<pg:param name="roleTypeName" value="<%=roleTypeName%>"/>
			<pg:param name="roleDesc" value="<%=roleDesc%>"/>
			<pg:param name="creatorName" />
			<pg:equal actual="${RoleSearchList.itemCount}" value="0" >
				<div class="nodata">
				<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
			</pg:equal>
			<pg:notequal actual="${RoleSearchList.itemCount}"  value="0">
			  
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb" >
			 <pg:header>
	       		<th><input type="checkbox" name="checkBoxAll" onclick="checkAll('checkBoxAll','ID')"></th>
	       		<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.name"/></th>
	       		<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type"/></th>
	       		<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.description"/></th>
	       		<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.creator"/></th>
       		</pg:header>
			  
			<%ContextMenu contextmenu = new ContextMenuImpl();%>
			<!--list标签循环输出每条记录-->
			<pg:list>
				<%
					String roleID = dataSet.getString("roleId");
					boolean isDisabled = (roleID.equals("1") || roleID.equals("3"));
					String owner_Id=(String)dataSet.getString("owner_id");
					String curuserId=control.getUserID();
					
					//增加角色的创建人登陆名和实名
					//baowen.liu 2008-4-14
					UserManager usermanager=SecurityDatabase.getUserManager();
					User user=usermanager.getUserById(owner_Id);
					String username=user.getUserName();
					String userrealname=user.getUserRealname();
					
					Menu menu = new Menu();		
					menu.setIdentity("oproletype_" + roleID);

					//角色基本信息查看
					Menu.ContextMenuItem baseitem = menu.addContextMenuItem(RequestContextUtils.getI18nMessage("sany.pdp.sys.info.manage", request),"javascript:void(0)",Menu.icon_ok);
					baseitem.addSubContextMenuItem(RequestContextUtils.getI18nMessage("sany.pdp.sys.info.view", request),"javascript:lookRoleInfo('"+roleID+"')",Menu.icon_search);
					//Menu.ContextMenuItem menuitem1 = new Menu.ContextMenuItem();
					//menuitem1.setName("基本信息查看");
					//menuitem1.setLink("javascript:lookRoleInfo('"+roleID+"')");
					//menuitem1.setIcon("../../sysmanager/images/piechart.gif");
					//menu.addContextMenuItem(baseitem);
					
					Menu.ContextMenuItem authitem = menu.addContextMenuItem(RequestContextUtils.getI18nMessage("sany.pdp.sys.purview.manage", request),"javascript:void(0)",Menu.icon_ok);
					//menu.addContextMenuItem(authitem);
					//不是自己创建的角色,将不显示一下右键菜单,具有超级管理员角色的用户除外.
					//2008-4-9 baowen.liu
					if(owner_Id.equals(curuserId)||control.isAdmin()){
						if(control.checkPermission("globalrole","rolemanager",AccessControl.ROLE_RESOURCE)){
							//角色基本信息修改
							//Menu.ContextMenuItem menuitem2 = new Menu.ContextMenuItem();
							//menuitem2.setName("基本信息修改");
							//menuitem2.setLink("javascript:roleInfo('"+roleID+"')");
							//menuitem2.setIcon("../../sysmanager/images/project_close.gif");
							//menu.addContextMenuItem(menuitem2);
							baseitem.addSubContextMenuItem(RequestContextUtils.getI18nMessage("sany.pdp.sys.info.modfiy", request)
									,"javascript:roleInfo('"+roleID+"')",Menu.icon_edit);
							
							//权限
							if(!"1".equals(roleID) && !"3".equals(roleID)){
								authitem.addSubContextMenuItem(RequestContextUtils.getI18nMessage("sany.pdp.sys.purview.authorize", request)
										,"javascript:rolePurview('"+roleID+"')",Menu.icon_ok);
								//Menu.ContextMenuItem menuitem7 = new Menu.ContextMenuItem();
								//menuitem7.setName("权限授予");
								//menuitem7.setLink("javascript:rolePurview('"+roleID+"')");
								//menuitem7.setIcon("../../sysmanager/images/Realm.gif");
								//menu.addContextMenuItem(menuitem7);
							}
				
							//权限复制
							if("4".equals(roleID) || "1".equals(roleID) || "3".equals(roleID) || "2".equals(roleID)){
							    //1:admin
							    //3 部门管理员角色
							    //2 普通角色
							    //4 部门管理员角色模板
							    //不允许权限回收
							}else{
								authitem.addSubContextMenuItem(RequestContextUtils.getI18nMessage("sany.pdp.sys.purview.copy", request)
										,"javascript:copyPurview('"+roleID+"')",Menu.icon_ok);
								//Menu.ContextMenuItem menuitem8 = new Menu.ContextMenuItem();
								//menuitem8.setName("权限复制");
								//menuitem8.setLink("javascript:copyPurview('"+roleID+"')");
								//menuitem8.setIcon("../../sysmanager/images/Roles.gif");
								//menu.addContextMenuItem(menuitem8);
							}
							
							//权限回收
							if("1".equals(roleID) || "3".equals(roleID)){
							    //1:admin
							    //3 部门管理员角色
							    //不允许权限回收
							}else{					    
							    authitem.addSubContextMenuItem(RequestContextUtils.getI18nMessage("sany.pdp.sys.purview.recycle", request)
							    		,"javascript:reclaimPurview('"+roleID+"')",Menu.icon_cut);
								//Menu.ContextMenuItem menuitem9 = new Menu.ContextMenuItem();
								//menuitem9.setName("权限回收");
								//menuitem9.setLink("javascript:reclaimPurview('"+roleID+"')");
								//menuitem9.setIcon("../../sysmanager/images/icons/16x16/recycle.gif");
								//menu.addContextMenuItem(menuitem9);
							}
							
						}
					}
					menu.addSeperate();
					//角色授予用户
					if(!"2".equals(roleID) && !"3".equals(roleID) && !"4".equals(roleID)){
						if (ConfigManager.getInstance().getConfigBooleanValue("enableorgrole", false))
	                    {			
							//角色授予机构
							if(!"2".equals(roleID) && !"3".equals(roleID) && !"4".equals(roleID)){
								Menu.ContextMenuItem menuitem5 = new Menu.ContextMenuItem();
								menuitem5.setName(RequestContextUtils.getI18nMessage("sany.pdp.sys.organization.authorize", request)
										);
								menuitem5.setLink("javascript:rightOrgList('"+roleID+"')");
								menuitem5.setIcon("../../sysmanager/images/procedure.gif");
								menu.addContextMenuItem(menuitem5);
							}
						}
						Menu.ContextMenuItem menuitem3 = new Menu.ContextMenuItem();
						menuitem3.setName(RequestContextUtils.getI18nMessage("sany.pdp.sys.user.authorize", request)
								);
						menuitem3.setLink("javascript:rightUserList('"+roleID+"')");
						menuitem3.setIcon("../../sysmanager/images/profile.gif");
						menu.addContextMenuItem(menuitem3);
						if(ConfigManager.getInstance().getConfigBooleanValue("enablejobfunction", false)){
							//角色授予机构岗位
							Menu.ContextMenuItem menuitem6 = new Menu.ContextMenuItem();
							menuitem6.setName(RequestContextUtils.getI18nMessage("sany.pdp.sys.post.authorize", request)
									);
							menuitem6.setLink("javascript:rightOrgJobList('"+roleID+"')");
							menuitem6.setIcon("../../sysmanager/images/Valve.gif");
							menu.addContextMenuItem(menuitem6);
						}
					}
					
					//设置用户组开关
					if (!"2".equals(roleID) && !"3".equals(roleID) && !"4".equals(roleID) 
						&& ConfigManager.getInstance().getConfigBooleanValue("enablergrouprole", false))
                    {
						//角色授予用户组
						if((!"2".equals(roleID) && !"3".equals(roleID) && !"4".equals(roleID) && owner_Id.equals(curuserId))
							|| control.isAdmin()){
							Menu.ContextMenuItem menuitem4 = new Menu.ContextMenuItem();
							menuitem4.setName(RequestContextUtils.getI18nMessage("sany.pdp.sys.usergroup.authorize", request)
									);
							menuitem4.setLink("javascript:rightUserGroupList('"+roleID+"')");
							menuitem4.setIcon("../../sysmanager/images/members.gif");
							menu.addContextMenuItem(menuitem4);
						}
					}
					
					
					
					
					menu.addSeperate();
					//权限查询
					 authitem.addSubContextMenuItem(RequestContextUtils.getI18nMessage("sany.pdp.sys.purview.search", request)
							 ,"javascript:roleResList('"+roleID+"')",Menu.icon_search);
					//Menu.ContextMenuItem menuitem10 = new Menu.ContextMenuItem();
					//menuitem10.setName("权限查询");
					//menuitem10.setLink("javascript:roleResList('"+roleID+"')");
					//menuitem10.setIcon("../../sysmanager/images/markread-16x16.gif");
					//menu.addContextMenuItem(menuitem10);
					
					//角色授予情况查询。查询角色的授予给用户，机构，用户组，机构下的岗位。roleofeveryone不需要查询
					if(!roleID.equals("2")){
						authitem.addSubContextMenuItem(RequestContextUtils.getI18nMessage("sany.pdp.sys.role.authorize.search", request)
								,"javascript:roleGrantList('"+roleID+"')",Menu.icon_search);
						//Menu.ContextMenuItem menuitem11 = new Menu.ContextMenuItem();
						//menuitem11.setName("角色授予查询");
						//menuitem11.setLink("javascript:roleGrantList('"+roleID+"')");
						//menuitem11.setIcon("../../sysmanager/images/milestone.gif");
						//menu.addContextMenuItem(menuitem11);
					}
					
					
					contextmenu.addContextMenu(menu);
				%>
				<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'">
					<td class="td_center"  nowrap="true">
						     <!-- 不是超级管理员或不是自己创建的角色将不可以选择 2008-4-8 baowen.liu -->
							<input type="checkbox" name="ID" <%if(((!control.isAdmin())&&(!owner_Id.equals(curuserId))) || isDisabled) {%> disabled='true' <%}%> onclick="checkOne('checkBoxAll','ID')" value='<pg:cell colName="roleId" defaultValue=""/>' >						
					</td>
					<td id="oproletype_<%=roleID%>"  bgcolor="#F6FFEF">
						<pg:cell colName="roleName" defaultValue="" />
					</td>		
					<td>
						<pg:cell colName="roleType" defaultValue="" />
					</td>
					<td>
						<pg:cell colName="roleDesc" defaultValue=""  maxlength="50" replace="..."/>
					</td>
					<td>
						<%=username%>【<%=userrealname%>】
					</td>		
				</tr>
			</pg:list>
			</table>
			<%request.setAttribute("oproletype", contextmenu);%>
			<pg:contextmenu enablecontextmenu="true" context="oproletype" scope="request" />
			<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
			</pg:notequal>
			</pg:pager>
			
		</form>
	</div>
	</body>
<iframe name="hiddenFrame" id="hiddenFrame" src="" width=0 height=0 ></iframe>
</html>

