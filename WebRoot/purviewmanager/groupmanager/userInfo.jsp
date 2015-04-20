<%
/*
 * <p>Title: 用户组用户操作界面</p>
 * <p>Description:对用户组中用户操作</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-18
 * @author liangbing.tao
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
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.Menu"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenu"%>
<%@ page import="com.frameworkset.platform.config.ConfigManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Group"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.GroupManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuTag"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuImpl"%>
<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	
	String groupId =(String)request.getParameter("groupId");
	//得到当前用户的ID
	String curUserId=accesscontroler.getUserID();
	//得到用户组的创建人
	GroupManager groupManager=SecurityDatabase.getGroupManager();
    Group group=groupManager.getGroupByID(groupId);
    
    String groupowner_id = String.valueOf(group.getOwner_id());
    
	String sql = "";	
	
	String userRealName = request.getParameter("userRealName");
	String userName = request.getParameter("userName");
	
	
	
	if(groupId == null || "".equals(groupId))
	{
		groupId="-1";
		userName = "";
		userRealName = "";
	}
	else
	{		
		//获取用户信息
		sql = "select distinct u.user_id,u.user_name,u.user_realname,u.user_mobiletel1,o.org_name ";
			sql +="from td_sm_group g,td_sm_usergroup ug,td_sm_user u,td_sm_userjoborg ujo,td_sm_organization o ";
			sql +="where g.group_id = ug.group_id and ug.user_id = u.user_id and u.user_id = ujo.user_id and ujo.org_id = o.org_id "; 
			sql +="and g.group_id = '" + groupId +"'";
			
			if(userName != null && !userName.equals(""))
			{
				sql +=" and u.user_name like '%" + userName +"%'";
			}
			else
			{
				userName = "";
			}
			
			if(userRealName != null && !userRealName.equals(""))
			{
				sql +=" and u.user_realname like '%" + userRealName +"%'" ;
			}	
			else
			{
				userRealName = "";
			}			
	}
%>
<html>
	<head>
		<title>属性容器</title>
		
		<script language="JavaScript" src="../scripts/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../scripts/pager.js" type="text/javascript"></script>
		<script type="text/javascript" src="../../include/jquery-1.4.2.min.js"></script>
		<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
		<script language="javascript">
		
			var groupId = "<%=groupId%>";
			
			//增加子用户组
			function addsonGroup()
			{
				if(groupId == -1)
				{
					parent.$.dialog.alert("<pg:message code='sany.pdp.groupmanage.please.choose.group'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return false ;
				}
				var url = "${pageContext.request.contextPath}/purviewmanager/groupmanager/groupInfo_addson.jsp?groupId=" + groupId;
				parent.$.dialog({title:'<pg:message code="sany.pdp.groupmanage.new.child.group"/>',width:760,height:560, content:'url:'+url,lock: true});

					//parent.group_tree.location.href = "groupTree.jsp?collapse="+ winuser +"&request_scope=request&selectedNode="+winuser;

				//	parent.groupinfo.location.reload();
			}
			
			//修改用户组
			function editGroup()
			{
				if(groupId == -1)
				{
					parent.$.dialog.alert("<pg:message code='sany.pdp.groupmanage.please.choose.group'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return false;
				}
				var url = "${pageContext.request.contextPath}/purviewmanager/groupmanager/groupInfo_edit.jsp?groupId=" + groupId;
				parent.$.dialog({title:'<pg:message code="sany.pdp.groupmanage.modify.group"/>',width:760,height:560, content:'url:'+url,lock: true});
				
			}
			
			
			//删除用户组
			function delGroup()
			{
				if(groupId == -1)
				{
					parent.$.dialog.alert("<pg:message code='sany.pdp.groupmanage.please.choose.group'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return false;
				}
				
				parent.$.dialog.confirm("<pg:message code='sany.pdp.common.operation.remove.confirm'/>",function(){
					groupForm.action= "submitGroup.jsp?flag=3&groupId=" + groupId;
					groupForm.target= "hiddenFrame";
					groupForm.submit();
					return true;
				},null,null,"<pg:message code='sany.pdp.common.alert'/>")
				
			}
			
			function changeRole()
			{
				if(groupId == -1)
				{
					parent.$.dialog.alert("<pg:message code='sany.pdp.groupmanage.please.choose.group'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return false;
				}
				var url = "${pageContext.request.contextPath}/purviewmanager/groupmanager/changeRole_ajax.jsp?groupId=" + groupId;
				parent.$.dialog({title:'<pg:message code="sany.pdp.groupmanage.correlation.role"/>',width:760,height:560, content:'url:'+url,lock: true});
				
			}
			
			function addRole()
			{
				if(groupId == -1)
				{
					parent.$.dialog.alert("<pg:message code='sany.pdp.groupmanage.please.choose.group'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return false;
				}
				
				var url = "${pageContext.request.contextPath}/purviewmanager/groupmanager/group2user.jsp?groupId=" + groupId;
				parent.$.dialog({close:refresh,title:'<pg:message code="sany.pdp.groupmanage.add.role"/>',width:760,height:560, content:'url:'+url,lock: true});
			}
			
			function lookInfo()
			{
				if(groupId == -1)
				{
					parent.$.dialog.alert("<pg:message code='sany.pdp.groupmanage.please.choose.group'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return false;
				}	
				var url = "${pageContext.request.contextPath}/purviewmanager/groupmanager/userGroupInfo.jsp?groupId=" + groupId;
				parent.$.dialog({title:'<pg:message code="sany.pdp.groupmanage.group.information"/>',width:760,height:560, content:'url:'+url,lock: true});
			}
			
			function clearInfo()
			{
				document.getElementById("userName").value = '';
				document.getElementById("userRealName").value = '';
			}
			
			
			function subTeam(userId)
			{
			//用户隶属组
				var url = "${pageContext.request.contextPath}/purviewmanager/groupmanager/user2group.jsp?userId="+ userId;
				parent.$.dialog({title:'<pg:message code="sany.pdp.groupmanage.group.setting"/>',width:760,height:560, content:'url:'+url,lock: true});
			}
			
			function viewUserInfo(userId)
			{
			//用户基本信息
				var url = "${pageContext.request.contextPath}/purviewmanager/groupmanager/userInfo1.jsp?userId="+ userId;
				parent.$.dialog({title:'<pg:message code="sany.pdp.personcenter.person.info"/>',width:760,height:560, content:'url:'+url,lock: true});
				
			}
			
			function queryUser()
			{	
				//if(document.userList.userName.value.length < 1 && document.userList.userRealname.value.length < 1){
					//alert("用户名称和用户实名必须输入一个!!!");
					//return;
				//}
				var userName = groupForm.userName.value;
				var userRealname = groupForm.userRealName.value;
				
				if((userName == "" || userName.length<1 || userName.replace(/\s/g,"")=="") && (userRealname == "" || userRealname.length<1 || userRealname.replace(/\s/g,"")==""))
					{
						//parent.$.dialog.alert("查询条件不能为空!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
						//return false;
					}
				
				if(userName == "" || userName.length<1 || userName.replace(/\s/g,"")=="")
				{
					
				}
				else
				{
						var re_ = /^\w+$/; 
						if(!re_.test(userName))
						{
						//parent.$.dialog.alert("登陆名称中不能有非数字、字母、下划线的字符!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
						//return false; 
						}
				}
				
				if(userRealname == "" || userRealname.length<1 || userRealname.replace(/\s/g,"")=="")
					{
					
					}
				else
					{
						var re =  /^[A-Za-z0-9\u4e00-\u9fa5]+$/; 
						if(!re.test(userRealname))
						{
						//parent.$.dialog.alert("用户实名中不能有非数字、中文、字母的字符!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
						//return false; 
						}
					}
				
				groupForm.action="../groupmanager/userInfo.jsp?groupId=<%=groupId%>";
				groupForm.submit();	
			}
			function refresh(){
				window.location.reload();
			}
			
		</script>
		
		
		<body>	
			<div id="" align="center">
				<iframe name="hiddenFrame" width=0 height=0></iframe>
				<form name="groupForm" method="post" >	
				<div id="searchblock">
				<div class="search_top">
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
										<th>
											<div align="center">
												<pg:message code="sany.pdp.user.real.name"/>
											</div>
										</th>
										<td width="21%">
											<div align="left">
												<input type="text" name="userRealName" value="<%=userRealName%>">
											</div>
										</th>
										<th>
											<div align="center">
												<pg:message code="sany.pdp.user.login.name"/>
											</div>
										</th>
										<td width="21%">
											<div align="left">
												<input type="text" name="userName" value="<%=userName%>" >
											</div>
										</td>
										<td width="21%">
											<div align="center">
												<a class="bt_1" onClick="queryUser()"><span><pg:message code="sany.pdp.common.operation.search"/></span></a>
												<a class="bt_2" onclick="clearInfo()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
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
				<div class="title_box">
					<div class="rightbtn">
					<%if(accesscontroler.isAdmin() || 
								(curUserId.equals(groupowner_id) && accesscontroler.checkPermission("globalgroup",
		                           "groupmanager", AccessControl.GROUP_RESOURCE))){%>
					<a href="javascript:void(0)" class="bt_small" id="addButton" onclick="addRole();"><span><pg:message code="sany.pdp.groupmanage.add.role"/></span></a>
					<a href="javascript:void(0)" class="bt_small" id="addButton" onclick="changeRole();"><span><pg:message code="sany.pdp.groupmanage.correlation.role"/></span></a>
					<a href="javascript:void(0)" class="bt_small" id="addButton" onclick="editGroup();"><span><pg:message code="sany.pdp.groupmanage.modify.group"/></span></a>
					<a href="javascript:void(0)" class="bt_small" id="addButton" onclick="delGroup();"><span><pg:message code="sany.pdp.groupmanage.delte.group"/></span></a>
					<a href="javascript:void(0)" class="bt_small" id="addButton" onclick="lookInfo();"><span><pg:message code="sany.pdp.groupmanage.group.information"/></span></a>
					<a href="javascript:void(0)" class="bt_small" id="addButton" onclick="addsonGroup();"><span><pg:message code="sany.pdp.groupmanage.new.child.group"/></span></a>
					<%
							}else if(accesscontroler.checkPermission("globalgroup",
		                           "groupmanager", AccessControl.GROUP_RESOURCE)){//查看时候授权用户组全局操作的权限
					%>
					<a href="javascript:void(0)" class="bt_small" id="addButton" onclick="addsonGroup();"><span><pg:message code="sany.pdp.groupmanage.new.child.group"/></span></a>
					<% 
								}
					%>
					</div>
				</div>
				</div>
					<input type="hidden" name="groupId" value="<%=groupId%>"/>
					<pg:pager statement="<%=sql%>" dbname="bspf" isList="false" maxPageItems="10">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">		
						<!--设置分页表头-->
						<pg:header>
							<th>
								<pg:message code="sany.pdp.user.real.name"/>
							</th>
							<th>
								<pg:message code="sany.pdp.user.login.name"/>
							</th>
							<th>
								<pg:message code="sany.pdp.workflow.organization"/>
							</th>
							<th>
								<pg:message code="sany.pdp.moblie.telephone"/>
							</th>
						</pg:header>
						
						<%
						if(!sql.equals(""))
						{
						%>
						
							<pg:param name="groupId"/>
							<pg:param name="userName"/>
							<pg:param name="userRealName"/>
							
							<!--检测当前页面是否有记录-->
							<pg:notify>
								<tr height='25' class="labeltable_middle_tr_01">
									<td colspan=100 align='center'>
										<div class="nodata">
						<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
									</td>
								</tr>
							</pg:notify>
						
						
							<%
								ContextMenu contextmenu = new ContextMenuImpl();
					 		%>
					 		
							<!--list标签循环输出每条记录-->
							<pg:list>
								<%
									
									String userId = dataSet.getString("user_id");
									
									Menu menu = new Menu();
									menu.setIdentity("operUser_" + userId);		
									//不是当前用户的创建的组,禁止查看用户基本信息,2008-4-8 baowen.liu
									if(curUserId.equals(groupowner_id)||accesscontroler.isAdmin()) {
									//用户基本信息查看
									//2008-3-24 baowen.liu
									Menu.ContextMenuItem menuitem1 = new Menu.ContextMenuItem();
									menuitem1.setName(RequestContextUtils.getI18nMessage("sany.pdp.personcenter.person.info", request));
									menuitem1.setLink("javascript:viewUserInfo("+userId+")");
									menuitem1.setIcon("../images/rightmenu_images/edit.gif");
									menu.addContextMenuItem(menuitem1);
												
									//隶属组设置，为用户设置（新加入组或者去掉组）隶属组；只有admin才能有此项操作
									if(accesscontroler.isAdmin()){
										Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
										menuitem.setName(RequestContextUtils.getI18nMessage("sany.pdp.groupmanage.group.setting", request));
										menuitem.setLink("javascript:subTeam("+userId+")");
										menuitem.setIcon("../images/rightmenu_images/edit.gif");
										menu.addContextMenuItem(menuitem);
									}
													
									contextmenu.addContextMenu(menu);
									
									}	
									
									
									
								%>
								
							
								
								<tr class="labeltable_middle_td"  
									onmouseover="this.className='mouseover'"
									onmouseout="this.className= 'mouseout'">
									<td  id="operUser_<%=userId%>" class="tablecells" height='20' align=left bgcolor="#F6FFEF">
											<pg:cell colName="user_realname" defaultValue=""/>
									</td>
									<td height='20' align=left class="tablecells">
										<pg:cell colName="user_name" defaultValue="" />
									</td>
									<td height='20' align=left class="tablecells">
										<pg:cell colName="org_name" defaultValue="" />
									</td>
									<td height='20' align=left class="tablecells">
										<pg:notnull colName="user_mobiletel1">
											<pg:cell colName="user_mobiletel1" defaultValue=" "/></pg:notnull>
									</td>
								</tr>
							</pg:list>
							<%
							request.setAttribute("operUser",contextmenu);  
							%>
							<pg:contextmenu enablecontextmenu="true" context="operUser" scope="request"/> 
							
					
					<%		
						}
						else
						{
					%>
						<tr height="18px">
								<td class="detailcontent" colspan=4 align='center'>
									<pg:message code="sany.pdp.groupmanage.please.choose.group"/>
								</td>
							</tr>	
					<%
						}
					%>
				</table>
				<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="5,10,20,50,100"/></div>
			</pg:pager>
			</form>
		</div>
	</body>
</html>
