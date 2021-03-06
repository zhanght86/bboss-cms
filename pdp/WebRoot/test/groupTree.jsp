<%
/*
 * <p>Title: 用户组树页面</p>
 * <p>Description: 用户组树显示页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-18
 * @author liangbing.tao
 * @version 1.0
 */
%>


<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	String showhref = "" + accesscontroler.checkPermission("addrootgroup", "addgroup", AccessControl.GROUP_RESOURCE);
%>
<html>
	<head>
		<title>属性容器</title>
		<script src="../scripts/func.js"></script>
		<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>
		<script language="javascript">
			function newFirstGroup()
			{
				winuser = window.showModalDialog("groupInfo_add.jsp",
										 window,"dialogWidth:"+(550)+"px;dialogHeight:"+(350)+"px;help:no;scroll:auto;status:no");
				
				if(winuser)
				{
					parent.location = parent.location;
				}
			}
			function findUserGroup()
			{
				parent.groupinfo.location.href = "groupInfo.jsp";
			}
			
			function refreshgroupcache(){
				document.forms[0].action = "refreshcache_handle.jsp";
				document.forms[0].target = "hiddenFrame";
				document.forms[0].submit();
			}
		</script>
	<body class="contentbodymargin" scroll="no">
		<div>
			<center>
				<form name="OrgJobForm" action="" method="post">
					<table class="table" width="100%" border="0" cellpadding="0" cellspacing="1">
						<tr class="tr" height=30>
							<td class="td" >
							<td align="center">
									<a href="javascript:void(0)" onclick="findUserGroup()">用户组查询</a>
							</td>
							<% 
								if (accesscontroler.checkPermission("globalgroup",
		                            	"groupmanager", AccessControl.GROUP_RESOURCE)){	
							%>
								
								
								<td align="center">
									<a href="javascript:void(0)" onclick="newFirstGroup()">新增一级组</a>
								</td>
							<% 
								}
							%>
							</td>
						</tr>
					</table>
					<table class="table" width="100%" border="0" cellpadding="0" cellspacing="1">
						<tr class="tr" align=left>
							<td class="td">
								<tree:tree tree="group_role_tree" 
									node="group_role_tree.node" 
									imageFolder="/sysmanager/images/tree_images/" 
									collapse="true" 
									includeRootNode="true" 
									href="userInfo.jsp" 
									target="groupinfo"
									 mode="static-dynamic">
									
									<tree:treedata treetype="com.frameworkset.platform.sysmgrcore.purviewmanager.menu.GroupAddTree" 
										scope="request" rootid="0" 
										rootName="用户组树" expandLevel="1" 
										showRootHref="false" 
										needObserver="false"
										enablecontextmenu="true"  />
								</tree:tree>
							</td>
						</tr>
					</table>
				</form>
			</center>
		</div>
<iframe name="hiddenFrame" width=0 height=0></iframe>
	</body>
</html>

