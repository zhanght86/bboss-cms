<%
/*
 * <p>Title: 角色用户组列表查询</p>
 * <p>Description:角色用户组列表查询</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-4-14
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
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>




<%	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	
	String roleId = request.getParameter("roleId");	
	request.setAttribute("action","group");
	
	String groupName = request.getParameter("groupName");
	String groupDesc = request.getParameter("groupDesc");
	//String creator = request.getParameter("creator");
	
	groupName = groupName == null ? "" : groupName ;
	groupDesc = groupDesc == null ? "" : groupDesc ;
	
	UserManager usermanager=SecurityDatabase.getUserManager();
	
%>

<html>
	<head>
		<title>属性容器</title>
		<script language="javascript">
			function clearInfo()
			{
				//document.getElementById("groupName").value = '';
				//document.getElementById("groupDesc").value = '';
				//document.getElementById("creator").value = '';
				document.groupForm.groupName.value = '';
				document.groupForm.groupDesc.value = '';
				
			}
			function querySub(){
				var groupName = document.groupForm.groupName.value;
				var groupDesc = document.groupForm.groupDesc.value;
				document.groupForm.action = "rolegroupquery.jsp?groupName="+groupName+"&groupDesc="+groupDesc+"&type=group&roleId=<%=roleId%>";
				document.groupForm.submit();
			}
			
			function doreset() {
				document.all.groupName.value = "";
				document.all.groupDesc.value = "";
		   	}
		</script>
		
		<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.purviewmanager.tag.RoleGrantSearchList" keyName="RoleGrantSearchList" />
		<!--分页显示开始,分页标签初始化-->
		<pg:pager maxPageItems="15" id="RoleGrantSearchList" scope="request" data="RoleGrantSearchList" isList="false">
		
		<body>	
			<div class="mcontent">
				<div id="searchblock">
					<div  class="search_top">
			    		<div class="right_top"></div>
			    		<div class="left_top"></div>
	      			</div>
	      			<div class="search_box">
	      				<form name="groupForm" method="post" >
	      					<table width="98.6%" border="0" cellspacing="0" cellpadding="0">
	      						<tr>
			      					<td class="left_box"></td>
			      					<td>
			      						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
			      							<tr>
			      								<th><pg:message code="sany.pdp.workflow.group.name"/>：</th>
												<td><input type="text" name="groupName" value="<%=groupName%>"  class="w120" /></td>
			      								<th><pg:message code="sany.pdp.workflow.group.description"/>：</th>
			      								<td><input type="text" name="groupDesc" value="<%=groupDesc%>"  class="w120" /></td>
			      								<th>&nbsp;</th>
			      								<td>
			      									<a href="javascript:void(0)" class="bt_1" id="search"  onclick="querySub()"><span><pg:message code="sany.pdp.common.operation.search"/></span> </a>
			      									<a href="javascript:void(0)" class="bt_2" id="reset1" onclick="doreset()"><span><pg:message code="sany.pdp.common.operation.reset"/></span> </a>
			      									<input type="reset" id="reset" style="display: none" />
			      								</td>
			      							</tr>
			      						</table>
			      					</td>
			      					<td class="right_box"></td>
			      				</tr>
	      					</table>
	      				</form>
	      			</div>
	      			<div class="search_bottom">
						<div class="right_bottom"></div>
						<div class="left_bottom"></div>
					</div>
				</div>
				
				<div id="changeColor">
					<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.purviewmanager.tag.RoleGrantSearchList" keyName="RoleGrantSearchList" />
					<pg:pager maxPageItems="15" id="RoleGrantSearchList" scope="request" data="RoleGrantSearchList" isList="false">
						<pg:param name="groupName"/>
						<pg:param name="groupDesc"/>
						<pg:param name="roleId"/>
						<pg:param name="type" value="group"/>
						<pg:param name="groupName" />
						<pg:param name="groupDesc" />
						<pg:param name="creator"/>
						
						<pg:equal actual="${RoleGrantSearchList.itemCount}" value="0" >
							<div class="nodata">
							<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
						</pg:equal>
						<pg:notequal actual="${RoleGrantSearchList.itemCount}"  value="0">
							<table width="98.7%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb" >
								<pg:header>
									<th><pg:message code="sany.pdp.workflow.group.name"/>	</th>
									<th><pg:message code="sany.pdp.workflow.group.description"/></th>
									<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.usergroup.creator"/></th>
								</pg:header>
								<pg:list>
									<tr>	      	
										<td>
											<pg:cell colName="groupName" defaultValue=""/>
										</td>							
										<td >
											<pg:cell colName="groupDesc" defaultValue="" />
										</td>
										<td>
											<%
												//增加角色的创建人登陆名和实名
												//baowen.liu 2008-4-14
												String creatorId = dataSet.getString("owner_id");
												User user=usermanager.getUserById(creatorId);
												String username=user.getUserName();
												String userrealname=user.getUserRealname();
												out.print(username + "【" + userrealname + "】");
											%>
										</td>
									</tr>
								</pg:list>
							</table>
							<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
						</pg:notequal>
					</pg:pager>
				</div>
			</div>
	</body>
	</pg:pager>
</html>
