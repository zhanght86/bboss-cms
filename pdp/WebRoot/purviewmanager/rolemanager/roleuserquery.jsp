<%
/*
 * <p>Title: 角色用户列表查询</p>
 * <p>Description:角色用户列表查询</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
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
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>


<%@ page import="com.frameworkset.platform.security.AccessControl"%>




<%	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	request.setAttribute("action","user");
	String roleId = request.getParameter("roleId");	
	
	
	String userName = request.getParameter("userName")==null?"":request.getParameter("userName");
	String userRealName = request.getParameter("userRealName")==null?"":request.getParameter("userRealName");
	
	//userName = userName == null ? "" : userName; 
	//userRealName = userRealName == null ? "" : userRealName ;

	
%>
<html>
	<head>
		<title>属性容器</title>
		<script language="javascript">	
			function clearQueryInfo()
			{
				userList.userName.value = '';
				userList.userRealName.value = '';
			}
			
			function querySub(){
				var userName = userList.userName.value;
				var userRealName = userList.userRealName.value;
				document.userList.action="roleuserquery.jsp?userName="+userName+"&userRealName="+userRealName+"&type=user&roleId=<%=roleId%>";
				document.userList.submit();
			}
			
			function doreset() {
				document.all.userName.value = "";
				document.all.userRealName.value = "";
		   	}
		</script>
		<body>
			<div class="mcontent">
				<div id="searchblock">
					<div  class="search_top">
			    		<div class="right_top"></div>
			    		<div class="left_top"></div>
	      			</div>
	      			<div class="search_box">
	      				<form name="userList" action="" method="post" >
	      					<table width="98.6%" border="0" cellspacing="0" cellpadding="0">
	      						<tr>
			      					<td class="left_box"></td>
			      					<td>
			      						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
			      							<tr>
			      								<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.loginname" />：</th>
												<td><input type="text" name="userName" value="<%=userName%>" size="30" class="w120" /></td>
			      								<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.realname" />：</th>
			      								<td><input type="text" name="userRealName" value="<%=userRealName%>" size="30" class="w120" /></td>
			      								<th>&nbsp;</th>
			      								<td>
			      									<a href="javascript:void(0)" class="bt_1" id="search"  onclick="querySub()"><span><pg:message code="sany.pdp.common.operation.search" /></span> </a>
			      									<a href="javascript:void(0)" class="bt_2" id="reset1" onclick="doreset()"><span><pg:message code="sany.pdp.common.operation.reset" /></span> </a>
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
						<pg:param name="roleId" value="<%=roleId%>"/>
						<pg:param name="userName"/>
						<pg:param name="userRealName"/>
						<pg:param name="type" value="user"/>
						
						<pg:equal actual="${RoleGrantSearchList.itemCount}" value="0" >
							<div class="nodata">
							<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
						</pg:equal>
						<pg:notequal actual="${RoleGrantSearchList.itemCount}"  value="0">
							<table width="98.7%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb" >
								<pg:header>
									<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.loginname" /></th>
									<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.realname" /></th>
									<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.belongorg" /></th>
									<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.mobile" /></th>
								</pg:header>
								<pg:list>
									<tr>	      	
										<td>
											<pg:cell colName="userName" defaultValue="" />
											<pg:null colName="userName"><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.loginname.null" /></pg:null>
											<pg:equal colName="userName" value=""><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.loginname.null" /></pg:equal>
										</td>							
										<td >
											<pg:cell colName="userRealname" defaultValue="" />
											<pg:null colName="userRealname"><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.realname.null" /></pg:null>
											<pg:equal colName="userRealname" value=""><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.realname.null" /></pg:equal>
										</td>
										<td>
											<pg:cell colName="orgName" defaultValue="" />
											<pg:null colName="orgName"><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.belongorg.null" /></pg:null>
											<pg:equal colName="orgName" value=""><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.belongorg.null" /></pg:equal>
										</td>
										<td>
											<pg:notnull colName="userMobiletel1"><pg:cell colName="userMobiletel1" defaultValue=" "/></pg:notnull>
											<pg:null colName="userMobiletel1"><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.mobile.null" /></pg:null>
											<pg:equal colName="userMobiletel1" value=""><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.mobile.null" /></pg:equal>
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
</html>
