<%@page import="com.frameworkset.platform.ca.CaProperties"%>
<%
/*
 * <p>Title:机构下用户列表显示页面</p>
 * <p>Description: 机构下用户列表显示页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-21
 * @author liangbing.tao
 * @version 1.0
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>


<%@ page
	import="
				 com.frameworkset.common.tag.contextmenu.Menu,
				 com.frameworkset.common.tag.contextmenu.ContextMenuImpl,
				 com.frameworkset.common.tag.contextmenu.ContextMenu,
				 com.frameworkset.platform.security.AccessControl,
				 com.frameworkset.platform.config.ConfigManager,
				 com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl,
				 com.frameworkset.platform.resource.ResourceManager,
				 org.frameworkset.web.servlet.support.WebApplicationContextUtils,
				 org.frameworkset.spi.support.MessageSource,
				 org.frameworkset.web.servlet.support.RequestContextUtils"%>


<%
	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	ResourceManager resManager = new ResourceManager();
    String resId = resManager.getGlobalResourceid(AccessControl.ORGUNIT_RESOURCE);
	String curOrgId = request.getParameter("orgId");
	String currentOrgId = accesscontroler.getChargeOrgId();
	//当前登陆用户ID
	String curUserId = accesscontroler.getUserID();
	if(curOrgId == null)
		curOrgId = (String)request.getAttribute("orgId");
	String reFlush = "false";
	if (request.getAttribute("reFlush") != null) 
	{
		reFlush = "true";
	}
	
	String userNamesNo = String.valueOf(session.getAttribute("promptString"));
	session.removeAttribute("promptString");
	//System.out.println(userNamesNo);
	Integer currUserId = (Integer)session.getAttribute("currUserId");
	
	if(currUserId == null)
	{
		currUserId = Integer.valueOf("-1");
	}
	
	//String curOrgId = (String)session.getAttribute("orgId");
	String desc = (String)request.getParameter("pager.desc");	
	String intervalType = (String)request.getParameter("intervalType");
	String ischecked="";
	if((String)request.getAttribute("ischecked")==null)
	{
		ischecked ="";
	}
	else
	{
		ischecked =(String)request.getAttribute("ischecked");
	}
	
	OrgAdministratorImpl orgAdministratorImpl = new OrgAdministratorImpl();
	
	//岗位功能开关
	boolean isJobOpen = ConfigManager.getInstance().getConfigBooleanValue("enablejobfunction", false);
	
	//是否允许用户存在多个机构下
	boolean isUserOrgs = ConfigManager.getInstance().getConfigBooleanValue("sys.user.enablemutiorg", false);
	//用户组开关
	boolean isGroup = ConfigManager.getInstance().getConfigBooleanValue("enablergrouprole", false);
	boolean userupdate = accesscontroler.checkPermission("orgunit","userupdate",AccessControl.ORGUNIT_RESOURCE);
	boolean isPurset = accesscontroler.checkPermission("orgunit","purset",AccessControl.ORGUNIT_RESOURCE);        
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>属性容器</title>

<%--
		<script language="JavaScript" src="../../scripts/pager.js" type="text/javascript"></script>
		--%>
<script language="JavaScript" src="../../scripts/common.js"
	type="text/javascript"></script>
	<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<script language="javascript">	
		function sortBy(byName){
			var desc = "false";
		
			if ( "<%=desc%>"  == desc ) desc = "true";
			userList.action="org_userlistIframe.jsp?orgId=<%=curOrgId%>&pager.offset=0&pager.sortKey="+byName+"&pager.desc="+desc;
			userList.submit();
		}
			
		function actionOnmouseover(e){	
			e.style.backgroundColor = "#8080FF";
		}
		
		function actionOnmouseup(e){
			e.style.backgroundColor = "#BEC0C2";
		}
		
		function advQueryUser()
		{	
			history.back();	
		}
		
		var winOpen
		function orderUser(){
		    winOpen = window.open("about:blank","win","scrollbars=no,status=no,titlebar=no,toolbar=no,z-lock=yes,width=616,height=500,top=130,left=210");                   
		    document.all.form2.target = "win";
		    document.all.form2.action = "user_order_ajax.jsp?orgId=<%=curOrgId%>";
		    timer = window.setInterval("isClosed()",500);
		    document.all.form2.submit();
		}
		var timer;
		function isClosed(){
		    if(winOpen.closed==true){
		        window.location.href = window.location;
		        window.clearInterval(timer);
		    }
		}
		
		<!-- gao.tang 2007.11.05 start 右键菜单跳转路径  -->
		function userInfoquery(userId){ 
			//用户基本信息查看 userInfo_tab.jsp
			var url="${pageContext.request.contextPath}/purviewmanager/userorgmanager/user/userInfo_tab_query.jsp?orgId=<%=curOrgId%>&userId="+userId;
			$.dialog({title:'<pg:message code="sany.pdp.purviewmanager.rolemanager.role.view"/>',width:760,height:560, content:'url:'+url,lock: true});
			
		}
		
		function userInfoupdate(userId){ 
			//用户基本信息修改 userInfo_tab.jsp
			var url="${pageContext.request.contextPath}/purviewmanager/userorgmanager/user/userInfo_tab_update.jsp?orgId=<%=curOrgId%>&userId="+userId;
			$.dialog({title:'<pg:message code="sany.pdp.purviewmanager.rolemanager.role.modfiy"/>',width:760,height:560, content:'url:'+url,lock: true});
		}
		
		function subJob(userId,orgId){
			//用户隶属岗位  userjoborg.jsp
			var url = "${pageContext.request.contextPath}/purviewmanager/userorgmanager/user/userjoborg.jsp?userId="+userId+"&orgId="+<%=curOrgId%>;
			$.dialog({title:'<pg:message code="sany.pdp.role.organization.job.setting"/>',width:760,height:560, content:'url:'+url,lock: true});
		}
		
		function subOrg(userId){
			//用户隶属机构
			var winsuborg;
			winsuborg = window.showModalDialog("../user/subjectionOrg.jsp?userId="+ userId + "&orgId=<%=curOrgId%>",window,"dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0");
			//if(winsuborg=="ok"){
			//	window.location.reload();
			//}
		}
		
		function subTeam(userId){
			//用户隶属组
			var url ="${pageContext.request.contextPath}/purviewmanager/userorgmanager/user/user2group.jsp?userId="+ userId + "&orgId=<%=curOrgId%>";
			$.dialog({title:'<pg:message code="sany.pdp.role.group.setting"/>',width:760,height:560, content:'url:'+url,lock: true});
			//if(winsubteam=="ok"){
			//	window.location.reload();
			//}
		}
		
		function roleGrant(userId){
			//机构下的用户角色授予
			var url="${pageContext.request.contextPath}/purviewmanager/userorgmanager/user/changeRole_ajax.jsp?userId="+ userId + "&orgId=<%=curOrgId%>";
			$.dialog({title:'<pg:message code="sany.pdp.role.setting"/>',width:760,height:560, content:'url:'+url,lock: true});
		}
		
		function resGrant(userId){
			//用户资源操作授予
			var winresgrant;
			var url="${pageContext.request.contextPath}/purviewmanager/grantmanager/user_resFrame.jsp?currRoleId="+ userId + "&orgId=<%=curOrgId%>"
			$.dialog({title:'<pg:message code="sany.pdp.empower"/>',width:760,height:560, content:'url:'+url,lock: true});
		}
		
		function purviewCopy(userId){
			//权限复制
			var url = "${pageContext.request.contextPath}/purviewmanager/userorgmanager/user/purview_copy.jsp?userId="+ userId + "&orgId=<%=curOrgId%>";
			$.dialog({title:'<pg:message code="sany.pdp.jurisdiction.copy"/>',width:760,height:560, content:'url:'+url,lock: true});
		}
		
		function purviewReclaim(userId){
		    //权限回收
		    var winpurviewReclaim;
		    var url = "${pageContext.request.contextPath}/purviewmanager/reclaimManager/reclaimUserRes.jsp?userIds="+ userId;
			$.dialog({title:'<pg:message code="sany.pdp.role.recover"/>',width:760,height:560, content:'url:'+url,lock: true});
		}
		
		function userOrg(userId){
			//用户可管理的机构列表信息
			var url = "${pageContext.request.contextPath}/purviewmanager/userorgmanager/user/managerOrg_list.jsp?orgId=<%=curOrgId%>&userId="+userId;
			$.dialog({title:'<pg:message code="sany.pdp.dept.manage"/>',width:760,height:560, content:'url:'+url,lock: true});
		}
		
		function userResList(userId){
			//用户权限查询
			var typeName = "USER";
			var url ="${pageContext.request.contextPath}/purviewmanager/userorgmanager/user/userres_queryframe.jsp?orgId=<%=curOrgId%>&userId="+ userId+"&typeName="+typeName;
			$.dialog({title:'<pg:message code="sany.pdp.sys.purview.search"/>',width:760,height:560, content:'url:'+url,lock: true});
		}
		
		//用户任职情况列表
		function userHisJob(userId){
			var url = "${pageContext.request.contextPath}/purviewmanager/userorgmanager/user/userHisJobQuery.jsp?userId="+userId;
			$.dialog({title:'<pg:message code="sany.pdp.his.post"/>',width:760,height:560, content:'url:'+url,lock: true});
		}
		
		function usersx(){
			document.location = document.location;
		}
		
		function userResetCa(userName){
			document.userList.action="../user/resetCa.jsp?userName="+userName;
			document.userList.target="deluser";
			document.userList.submit();
		}
		<!-- gao.tang 2007.11.05 end    -->
		
		
		</SCRIPT>
<body>




	<form name="userList" method="post">
		<input type="hidden" name="orgId" value="<%=curOrgId%>" />
   		<div id="changeColor">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table3">
				<pg:listdata
					dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.Org_UserListSn"
					keyName="UserListSn" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="5" scope="request" data="UserListSn"
					isList="false">
						<pg:param name="orgId" />
					<pg:param name="userName" />
					<pg:param name="userRealname" />
					<pg:param name="taxmanager" />
					<pg:param name="intervalType" />
					<pg:param name="userIsvalid" />
					<pg:param name="userSex" />
					<pg:param name="userType" />
					<pg:param name="isOrgManager" />
					<pg:equal actual="${UserListSn.itemCount}" value="0" >
						<div class="nodata">
						<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
					</pg:equal> 
					<pg:notequal actual="${UserListSn.itemCount}"  value="0">
						<tr>
							<!--设置分页表头-->
							<th width="30">
							<input type="checkbox" name="checkBoxAll"
								onclick="checkAll('checkBoxAll','checkBoxOne')" value="on" />
							</th>								
							</th>
							<th onclick="sortBy('userName')"><pg:message code="sany.pdp.user.login.name"/></th>
							<th onclick="sortBy('userName')"><pg:message code="sany.pdp.user.real.name"/></th>
							<th onclick="sortBy('userName')"><pg:message code="sany.pdp.sex"/></th>
							<th onclick="sortBy('userName')"><pg:message code="sany.pdp.user.category"/></th>
							<th onclick="sortBy('userName')"><pg:message code="sany.pdp.common.status"/></th>
							<th onclick="sortBy('userName')"><pg:message code="sany.pdp.workflow.organization"/></th>
							<th onclick="sortBy('userName')">密码过期时间</th>
							<th onclick="sortBy('userName')">密码有效期</th>
						</tr>
					
						
						
						
						<%
							ContextMenu contextmenu = new ContextMenuImpl();
						MessageSource messageSource = WebApplicationContextUtils.getWebApplicationContext();
						%>
						<!--list标签循环输出每条记录-->
						<pg:list>
							
							<%	
								
								int userId = dataSet.getInt("userId");
								String userIdStr = String.valueOf(userId);
								String userName = dataSet.getString("userName");
								String org_id = dataSet.getString("orgId");
								//System.out.println("orgId = " + org_id);
								//是否是管理员
								boolean isOrgManager = orgAdministratorImpl.isOrgManager(userIdStr);
								//boolean isOrgManager = false;
								//是否拥有超级管理员角色
								boolean isRoleAdmin = accesscontroler.isAdminByUserid(userIdStr);
								//boolean isRoleAdmin =  false;
								
								//列表中的用户是否是自己
								boolean isSelf = String.valueOf(userId).equals(curUserId);
								//列表中的用户是否是同级部门管理员
								boolean isSisterOrgManager = currentOrgId.equals(org_id) && isOrgManager;
								String isRoleAdminOrOrgManager = "";//是否拥有超级管理员角色与部门管理员角色
								if(isOrgManager && isRoleAdmin){
									isRoleAdminOrOrgManager = "("+messageSource.getMessage("sany.pdp.dept.administrator",RequestContextUtils.getRequestContextLocal(request))
																+";"+messageSource.getMessage("sany.pdp.super.administrator",RequestContextUtils.getRequestContextLocal(request))+")";
								}else if(isRoleAdmin){
									isRoleAdminOrOrgManager ="("+ messageSource.getMessage("sany.pdp.super.administrator",RequestContextUtils.getRequestContextLocal(request))+")";
								}else if(isOrgManager){
									isRoleAdminOrOrgManager = "("+messageSource.getMessage("sany.pdp.dept.administrator",RequestContextUtils.getRequestContextLocal(request))+")";
								}
								
								Menu menu = new Menu();
								menu.setIdentity("opuser_"+userId);
								//--Menu Add -----------------------Starting---------------------------------
								//用户基本信息查看，这里不做权限控制，在打开的页面控制
								Menu.ContextMenuItem menuitemquery = new Menu.ContextMenuItem();
								menuitemquery.setName(messageSource.getMessage("sany.pdp.show.basic.info",RequestContextUtils.getRequestContextLocal(request)));
								menuitemquery.setLink("javascript:userInfoquery("+userId+")");
								menuitemquery.setIcon("../../../sysmanager/images/issue/status_reopened.gif");
								menu.addContextMenuItem(menuitemquery);
								
								if(CaProperties.CA_LOGIN_SERVER){
									//使用数字认证中心，重置key的序列号
									Menu.ContextMenuItem menuitemca = new Menu.ContextMenuItem();
									menuitemca.setName("重置CA序列");
									menuitemca.setLink("javascript:userResetCa('"+userName+"')");
									menuitemca.setIcon("../../../sysmanager/images/issue/priority_blocker.gif");
									menuitemca.setDisabled(!"1".equals(curUserId));
									menu.addContextMenuItem(menuitemca);
								}
								
								if("1".equals(curUserId)){//如果当前登陆用户为admin
									Menu.ContextMenuItem menuitem2 = new Menu.ContextMenuItem();
									menuitem2.setName(messageSource.getMessage("sany.pdp.modify.basic.info",RequestContextUtils.getRequestContextLocal(request)));
									menuitem2.setLink("javascript:userInfoupdate("+userId+")");
									menuitem2.setIcon("../../images/edit.gif");
									menuitem2.setDisabled(isSelf);
									menuitem2.setDisableMsg("超级管理员不能修改自己的信息");
									menu.addContextMenuItem(menuitem2);
									menu.addSeperate();
									
									Menu.ContextMenuItem menuitem7 = new Menu.ContextMenuItem();
									menuitem7.setName(messageSource.getMessage("sany.pdp.sys.purview.authorize",RequestContextUtils.getRequestContextLocal(request)));
									menuitem7.setLink("javascript:resGrant("+userId+")");
									menuitem7.setIcon("../../../sysmanager/images/tool-reply.gif");
									if(isRoleAdmin){//如果选中的用户是拥有超级管理员角色
										menuitem7.setDisabled(isRoleAdmin);
										menuitem7.setDisableMsg("管理员不能被授予权限");
									}else{
										menuitem7.setDisabled(isSelf);
									}
										menu.addContextMenuItem(menuitem7);
									
									Menu.ContextMenuItem menuitem8 = new Menu.ContextMenuItem();
									menuitem8.setName(messageSource.getMessage("sany.pdp.sys.purview.recycle",RequestContextUtils.getRequestContextLocal(request)));
									menuitem8.setLink("javascript:purviewReclaim("+userId+")");
									menuitem8.setIcon("../../../sysmanager/images/icons/16x16/recycle.gif");
									menuitem8.setDisabled(isSelf);
									menu.addContextMenuItem(menuitem8);
									
									Menu.ContextMenuItem menuitem9 = new Menu.ContextMenuItem();
									menuitem9.setName(messageSource.getMessage("sany.pdp.sys.purview.copy",RequestContextUtils.getRequestContextLocal(request)));
									menuitem9.setLink("javascript:purviewCopy("+userId+")");
									menuitem9.setIcon("../../../sysmanager/images/tool-reply_all.gif");
									if(isRoleAdmin || isOrgManager){
										menuitem9.setDisabled(true);
										menuitem9.setDisableMsg("管理员的权限不能被复制");
									}else{
										menuitem9.setDisabled(isSelf);
									}
										menu.addContextMenuItem(menuitem9);
									menu.addSeperate();
									
									if(isJobOpen){
										Menu.ContextMenuItem menuitem3 = new Menu.ContextMenuItem();
										menuitem3.setName(messageSource.getMessage("sany.pdp.job.setting",RequestContextUtils.getRequestContextLocal(request)));
										menuitem3.setLink("javascript:subJob("+userId+","+org_id+")");
										menuitem3.setIcon("../../../sysmanager/images/tree_images/channel_closedFolder.gif");
										if(isRoleAdmin || isOrgManager){
											menuitem3.setDisabled(true);
											menuitem3.setDisableMsg("管理员不能被赋予岗位");
										}else{
											menuitem3.setDisabled(isSelf);
										}
										menu.addContextMenuItem(menuitem3);
									}
									
									if(isUserOrgs){
										Menu.ContextMenuItem menuitem4 = new Menu.ContextMenuItem();
										menuitem4.setName(messageSource.getMessage("sany.pdp.main.organization.setting",RequestContextUtils.getRequestContextLocal(request)));
										menuitem4.setLink("javascript:subOrg("+userId+")");
										menuitem4.setIcon("../../../sysmanager/images/Groups.gif");
										menuitem4.setDisabled(isSelf);
										menu.addContextMenuItem(menuitem4);
									}
									
									Menu.ContextMenuItem menuitem6 = new Menu.ContextMenuItem();
									menuitem6.setName(messageSource.getMessage("sany.pdp.role.setting",RequestContextUtils.getRequestContextLocal(request)));
									menuitem6.setLink("javascript:roleGrant("+userId+")");
									menuitem6.setIcon("../../../sysmanager/images/tool-forward16.gif");
									menuitem6.setDisabled(isSelf);
									menu.addContextMenuItem(menuitem6);
									
									if (isGroup){
										Menu.ContextMenuItem menuitem5 = new Menu.ContextMenuItem();
										menuitem5.setName(messageSource.getMessage("sany.pdp.group.setting",RequestContextUtils.getRequestContextLocal(request)));
										menuitem5.setLink("javascript:subTeam("+userId+")");
										menuitem5.setIcon("../../../sysmanager/images/online-users.gif");
										menuitem5.setDisabled(isSelf);
										menu.addContextMenuItem(menuitem5);
									}
									menu.addSeperate();
									
								}else if(!"1".equals(curUserId) && accesscontroler.isAdmin()){//如果当前登陆用户为拥有超级管理员的用户且不是admin
									Menu.ContextMenuItem menuitem2 = new Menu.ContextMenuItem();
									menuitem2.setName(messageSource.getMessage("sany.pdp.modify.basic.info",RequestContextUtils.getRequestContextLocal(request)));
									menuitem2.setLink("javascript:userInfoupdate("+userId+")");
									menuitem2.setIcon("../../images/edit.gif");
									if(isRoleAdmin){
										menuitem2.setDisabled(isRoleAdmin);
										menuitem2.setDisableMsg("超级管理员不能修改自己的信息");
									}else{
										menuitem2.setDisabled(isSelf);
										menuitem2.setDisableMsg("不能修改自己的信息");
									}
										menu.addContextMenuItem(menuitem2);
									menu.addSeperate();
									
									Menu.ContextMenuItem menuitem7 = new Menu.ContextMenuItem();
									menuitem7.setName(messageSource.getMessage("sany.pdp.sys.purview.authorize",RequestContextUtils.getRequestContextLocal(request)));
									menuitem7.setLink("javascript:resGrant("+userId+")");
									menuitem7.setIcon("../../../sysmanager/images/tool-reply.gif");
									if(isRoleAdmin){
										menuitem7.setDisabled(isRoleAdmin);
										menuitem7.setDisableMsg("超级管理员不能被授予权限");
									}else{
										menuitem7.setDisabled(isSelf);
									}
										menu.addContextMenuItem(menuitem7);
									
									Menu.ContextMenuItem menuitem8 = new Menu.ContextMenuItem();
									menuitem8.setName(messageSource.getMessage("sany.pdp.sys.purview.recycle",RequestContextUtils.getRequestContextLocal(request)));
									menuitem8.setLink("javascript:purviewReclaim("+userId+")");
									menuitem8.setIcon("../../../sysmanager/images/icons/16x16/recycle.gif");
									if(isRoleAdmin){
										menuitem8.setDisabled(isRoleAdmin);
										menuitem8.setDisableMsg("超级管理员的权限不能被回收");
									}else{
										menuitem8.setDisabled(isSelf);
									}
										menu.addContextMenuItem(menuitem8);
									
									Menu.ContextMenuItem menuitem9 = new Menu.ContextMenuItem();
									menuitem9.setName(messageSource.getMessage("sany.pdp.sys.purview.copy",RequestContextUtils.getRequestContextLocal(request)));
									menuitem9.setLink("javascript:purviewCopy("+userId+")");
									menuitem9.setIcon("../../../sysmanager/images/tool-reply_all.gif");
									if(isRoleAdmin || isOrgManager){
										menuitem9.setDisabled(true);
										menuitem9.setDisableMsg("超级管理员的权限不能被复制");
									}else{
										menuitem9.setDisabled(isSelf);
									}
										menu.addContextMenuItem(menuitem9);
									menu.addSeperate();
									
									if(isJobOpen){
										Menu.ContextMenuItem menuitem3 = new Menu.ContextMenuItem();
										menuitem3.setName(messageSource.getMessage("sany.pdp.job.setting",RequestContextUtils.getRequestContextLocal(request)));
										menuitem3.setLink("javascript:subJob("+userId+","+org_id+")");
										menuitem3.setIcon("../../../sysmanager/images/tree_images/channel_closedFolder.gif");
										if(isRoleAdmin || isOrgManager){
											menuitem3.setDisabled(true);
											menuitem3.setDisableMsg("超级管理员所属岗位不能改变");
										}else{
											menuitem3.setDisabled(isSelf);
											menu.addContextMenuItem(menuitem3);
										}
									}
									if(isUserOrgs){
										Menu.ContextMenuItem menuitem4 = new Menu.ContextMenuItem();
										menuitem4.setName(messageSource.getMessage("sany.pdp.main.organization.setting",RequestContextUtils.getRequestContextLocal(request)));
										menuitem4.setLink("javascript:subOrg("+userId+")");
										menuitem4.setIcon("../../../sysmanager/images/Groups.gif");
										if(isRoleAdmin){
											menuitem4.setDisabled(isRoleAdmin);
											menuitem4.setDisableMsg("超级管理员所属机构不能改变");
										}else{
											menuitem4.setDisabled(isSelf);
										menu.addContextMenuItem(menuitem4);
										}
									}
									
									Menu.ContextMenuItem menuitem6 = new Menu.ContextMenuItem();
									menuitem6.setName(messageSource.getMessage("sany.pdp.role.setting",RequestContextUtils.getRequestContextLocal(request)));
									menuitem6.setLink("javascript:roleGrant("+userId+")");
									menuitem6.setIcon("../../../sysmanager/images/tool-forward16.gif");
									if(isRoleAdmin){
										menuitem6.setDisabled(isRoleAdmin);
										menuitem6.setDisableMsg("超级管理员角色不能改变");
									}else{
										menuitem6.setDisabled(isSelf);
									}
									menu.addContextMenuItem(menuitem6);
									
									if (isGroup){
										Menu.ContextMenuItem menuitem5 = new Menu.ContextMenuItem();
										menuitem5.setName(messageSource.getMessage("sany.pdp.group.setting",RequestContextUtils.getRequestContextLocal(request)));
										menuitem5.setLink("javascript:subTeam("+userId+")");
										menuitem5.setIcon("../../../sysmanager/images/online-users.gif");
										if(isRoleAdmin){
											menuitem5.setDisabled(isRoleAdmin);
											menuitem5.setDisableMsg("超级管理员隶属组不能改变");
										}else{
											menuitem5.setDisabled(isSelf);
										}
										menu.addContextMenuItem(menuitem5);
									}
									menu.addSeperate();
								}else{//如果登陆用户为部门管理员时
									boolean state = false;
									if(isSisterOrgManager || isRoleAdmin){
										state = true;
									}else{
										state = isSelf;
									}
									Menu.ContextMenuItem menuitem2 = new Menu.ContextMenuItem();
									menuitem2.setName(messageSource.getMessage("sany.pdp.modify.basic.info",RequestContextUtils.getRequestContextLocal(request)));
									menuitem2.setLink("javascript:userInfoupdate("+userId+")");
									menuitem2.setIcon("../../images/edit.gif");
									if(userupdate){
										menuitem2.setDisabled(state);
									}else{
										menuitem2.setDisabled(true);
										menuitem2.setDisableMsg("部门管理员信息不可以修改");
									}
									menu.addContextMenuItem(menuitem2);
									menu.addSeperate();
									
									
									Menu.ContextMenuItem menuitem7 = new Menu.ContextMenuItem();
									menuitem7.setName(messageSource.getMessage("sany.pdp.sys.purview.authorize",RequestContextUtils.getRequestContextLocal(request)));
									menuitem7.setLink("javascript:resGrant("+userId+")");
									menuitem7.setIcon("../../../sysmanager/images/tool-reply.gif");
									if(isPurset){
										menuitem7.setDisabled(state);	
									}else{
										menuitem7.setDisabled(true);
										menuitem7.setDisableMsg("部门管理员不能被授予权限");
									}
									menu.addContextMenuItem(menuitem7);
									
									Menu.ContextMenuItem menuitem8 = new Menu.ContextMenuItem();
									menuitem8.setName(messageSource.getMessage("sany.pdp.sys.purview.recycle",RequestContextUtils.getRequestContextLocal(request)));
									menuitem8.setLink("javascript:purviewReclaim("+userId+")");
									menuitem8.setIcon("../../../sysmanager/images/icons/16x16/recycle.gif");
									if(isPurset){
										menuitem8.setDisabled(state);	
									}else{
										menuitem8.setDisabled(true);
										menuitem8.setDisableMsg("部门管理员权限不可以被回收");
									}
									menu.addContextMenuItem(menuitem8);
									
									Menu.ContextMenuItem menuitem9 = new Menu.ContextMenuItem();
									menuitem9.setName(messageSource.getMessage("sany.pdp.sys.purview.copy",RequestContextUtils.getRequestContextLocal(request)));
									menuitem9.setLink("javascript:purviewCopy("+userId+")");
									menuitem9.setIcon("../../../sysmanager/images/tool-reply_all.gif");
									if(isPurset && !isOrgManager){
										menuitem9.setDisabled(state);	
									}else{
										menuitem9.setDisabled(true);
										menuitem9.setDisableMsg("部门管理员权限不可以被复制");
									}
									menu.addContextMenuItem(menuitem9);
									menu.addSeperate();
									
									
									if(isJobOpen){
										Menu.ContextMenuItem menuitem3 = new Menu.ContextMenuItem();
										menuitem3.setName(messageSource.getMessage("sany.pdp.job.setting",RequestContextUtils.getRequestContextLocal(request)));
										menuitem3.setLink("javascript:subJob("+userId+","+org_id+")");
										menuitem3.setIcon("../../../sysmanager/images/tree_images/channel_closedFolder.gif");
										if(!isOrgManager){
											menuitem3.setDisabled(state);	
										}else{
											menuitem3.setDisabled(true);
											menuitem3.setDisableMsg("部门管理员岗位不可以被改变");
										}
										menu.addContextMenuItem(menuitem3);
									}
									if(isUserOrgs){
										Menu.ContextMenuItem menuitem4 = new Menu.ContextMenuItem();
										menuitem4.setName(messageSource.getMessage("sany.pdp.main.organization.setting",RequestContextUtils.getRequestContextLocal(request)));
										menuitem4.setLink("javascript:subOrg("+userId+")");
										menuitem4.setIcon("../../../sysmanager/images/Groups.gif");
										menuitem4.setDisabled(isSelf);
										menu.addContextMenuItem(menuitem4);
									}
									
									Menu.ContextMenuItem menuitem6 = new Menu.ContextMenuItem();
									menuitem6.setName(messageSource.getMessage("sany.pdp.role.setting",RequestContextUtils.getRequestContextLocal(request)));
									menuitem6.setLink("javascript:roleGrant("+userId+")");
									menuitem6.setIcon("../../../sysmanager/images/tool-forward16.gif");
									menuitem6.setDisabled(state);
									menu.addContextMenuItem(menuitem6);
									
									if (isGroup){
										Menu.ContextMenuItem menuitem5 = new Menu.ContextMenuItem();
										menuitem5.setName(messageSource.getMessage("sany.pdp.group.setting",RequestContextUtils.getRequestContextLocal(request)));
										menuitem5.setLink("javascript:subTeam("+userId+")");
										menuitem5.setIcon("../../../sysmanager/images/online-users.gif");
										menuitem5.setDisabled(state);
										menu.addContextMenuItem(menuitem5);
									}
									menu.addSeperate();
								}
								
								
								//刷新页面
								Menu.ContextMenuItem menuitemsx = new Menu.ContextMenuItem();
								menuitemsx.setName(messageSource.getMessage("sany.pdp.refresh",RequestContextUtils.getRequestContextLocal(request)));
								menuitemsx.setLink("javascript:usersx()");
								menuitemsx.setIcon("../../../sysmanager/images/dialog-reset.gif");
								menu.addContextMenuItem(menuitemsx);
								
								//用户可管理的机构查看，不做控制
								Menu.ContextMenuItem menuitem_org = new Menu.ContextMenuItem();
								menuitem_org.setName(messageSource.getMessage("sany.pdp.dept.manage",RequestContextUtils.getRequestContextLocal(request)));
								menuitem_org.setLink("javascript:userOrg("+userId+")");
								menuitem_org.setIcon("../../../sysmanager/images/rightMemu/doc_sh.gif");
								menu.addContextMenuItem(menuitem_org);
								
								//用户资源列表
								Menu.ContextMenuItem menuitem1 = new Menu.ContextMenuItem();
								menuitem1.setName(messageSource.getMessage("sany.pdp.sys.purview.search",RequestContextUtils.getRequestContextLocal(request)));
								menuitem1.setLink("javascript:userResList("+userId+")");
								menuitem1.setIcon("../../../sysmanager/images/listQuery.gif");
								menu.addContextMenuItem(menuitem1);
								
								Menu.ContextMenuItem menuitem91 = new Menu.ContextMenuItem();
								menuitem91.setName(messageSource.getMessage("sany.pdp.his.post",RequestContextUtils.getRequestContextLocal(request)));
								menuitem91.setLink("javascript:userHisJob("+userId+")");
								menuitem91.setIcon("../../../sysmanager/images/about.gif");
								menu.addContextMenuItem(menuitem91);
								
								contextmenu.addContextMenu(menu);
								
										
								//----------------------------Menu Add ----End--------------------------------------
							%>
	
							
							<%
									     //String backColor="";
									     //if (  )
									     //	backColor="bgcolor=#ff0000";
									     //}
									%>
							<%if(userId!=1){%>
							<tr>
								<td>							
										<INPUT type="checkbox" name="checkBoxOne" class="checkBoxOne"
											onclick="checkOne('checkBoxAll','checkBoxOne')"
											value='<pg:cell colName="userId" defaultValue=""/>'>								
								</td>
								<td><pg:cell colName="userName"
										defaultValue="" /></td>
								<td id="opuser_<%=userId%>" bgcolor="#F6FFEF">
									<pg:cell colName="userRealname" defaultValue="" /><%=isRoleAdminOrOrgManager %>
								</td>
								<td><dict:itemname type="sex"
										expression="{userSex}" /></td>
								<td><dict:itemname type="userType" expression="{userType}" />
								</td>
								<td><dict:itemname type="isvalid"
										expression="{user_isvalid}" /></td>
								<td><pg:cell colName="org_Name"
										defaultValue="" /></td>
										
								<td><pg:cell colName="passwordExpiredTime" dateformat="yyyy-MM-dd HH:mm:ss"
										defaultValue="" /></td>	
								<td><pg:cell colName="passwordDualedTime" 
										defaultValue="0" />天</td>				
							</tr>
							<%}else{%>
							<tr>
								<td>
									 <INPUT type="checkbox"
											name="checkBoxOne"
											onclick="checkOne('checkBoxAll','checkBoxOne')"
											value='<pg:cell colName="userId" defaultValue=""/>'>									
								</td>
								<td><pg:cell
											colName="userName" defaultValue="" /></td>
								<td id="opuser_<%=userId%>" bgcolor="#F6FFEF">
									<font color="red"><pg:cell colName="userRealname" defaultValue="" /><%=isRoleAdminOrOrgManager%></font>
								</td>
								<td><dict:itemname type="sex" expression="{userSex}" /></td>
								<td><dict:itemname type="userType" expression="{userType}" /></td>
								<td><dict:itemname type="isvalid" expression="{user_isvalid}" /></td>
								<td><pg:cell colName="org_Name" defaultValue="" /></td>
								<td><pg:cell colName="passwordExpiredTime" dateformat="yyyy-MM-dd HH:mm:ss"
										defaultValue="" /></td>	
								<td><pg:cell colName="passwordDualedTime" 
										defaultValue="0" />天</td>				
							</tr>
							<%}%>
						</pg:list>
						<%
								request.setAttribute("opuser",contextmenu);  
								%>
						<pg:contextmenu enablecontextmenu="true" context="opuser"
							scope="request" />
	</table>
		</div>
						<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="5"/></div>
						
						
					</pg:notequal>	
				</pg:pager>
			
	</form>

	
	<form name="form2" method="POST"></form>
	<script language="JavaScript">	
	var thisUserId = "<%=currUserId.toString()%>";	
	var intervalType = "<%=intervalType%>";
    
</script>
	<iframe name="deluser" height="0" width="0"></iframe>
</body>
<center>
</html>
