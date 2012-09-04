<%
/*
 * <p>Title: 角色机构列表查询</p>
 * <p>Description:角色机构列表查询</p>
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

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	request.setAttribute("action","org");
	String roleId = request.getParameter("roleId");	
	
	String orgName = request.getParameter("orgName");
	String remark5 = request.getParameter("remark5");
	//String creator = request.getParameter("creator");
	
	orgName = orgName == null ? "" : orgName; 
	remark5 = remark5 == null ? "" : remark5 ;
	
	UserManager usermanager=SecurityDatabase.getUserManager();

	
%>
<html>
	<head>
		<title>属性容器</title>
		<script language="javascript">	
			function clearQueryInfo()
			{
				orgList.orgName.value = '';
				orgList.remark5.value = '';
			}
			
			function querySub(){
				var orgName = orgList.orgName.value;
				var remark5 = orgList.remark5.value;
				document.orgList.action = "roleorgquery.jsp?orgName="+orgName+"&remark5="+remark5+"&type=org&roleId=<%=roleId%>";
				//alert(document.orgList.action)
				document.orgList.submit();
			}
			
			function doreset() {
				document.all.orgName.value = "";
				document.all.remark5.value  = "";
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
	      				<form name="orgList" method="post" >
	      					<table width="98.6%" border="0" cellspacing="0" cellpadding="0">
	      						<tr>
			      					<td class="left_box"></td>
			      					<td>
			      						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
			      							<tr>
			      								<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.org.name" />：</th>
												<td><input type="text" name="orgName" value="<%=orgName%>" size="30" class="w120" /></td>
			      								<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.org.displayname" />：</th>
			      								<td><input type="text" name="remark5" value="<%=remark5%>" size="30"  class="w120"/></td>
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
						<pg:param name="orgName"/>
						<pg:param name="remark5"/>
						<pg:param name="type" value="org"/>
						<pg:param name="creator"/>
						
						<pg:equal actual="${RoleGrantSearchList.itemCount}" value="0" >
							<div class="nodata">
							<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
						</pg:equal>
						<pg:notequal actual="${RoleGrantSearchList.itemCount}"  value="0">
							<table width="98.7%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
							<pg:header>
								<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.org.name" /></th>
								<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.org.displayname" /></th>
								<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.org.number" /></th>
								<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.org.areacode" /></th>
								<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.org.creator" /></th>
							</pg:header>
							<pg:list>
									<tr>	      	
										<td>
											<pg:cell colName="orgName" defaultValue="" />
										</td>							
										<td >
											<pg:cell colName="remark5" defaultValue="" />
											<pg:null colName="remark5"><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.org.displayname.null" /></pg:null>
											<pg:equal colName="remark5" value=""><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.org.displayname.null" /></pg:equal>
										</td>
										<td>
											<pg:cell colName="orgnumber" defaultValue="" />
											<pg:null colName="orgnumber"><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.org.number.null" /></pg:null>
											<pg:equal colName="orgnumber" value=""><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.org.number.null" /></pg:equal>
										</td>
										<td>
											<pg:notnull colName="org_xzqm">
												<pg:cell colName="org_xzqm" defaultValue=" "/>
											</pg:notnull>
											<pg:null colName="org_xzqm"><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.org.areacode.null" /></pg:null>
											<pg:equal colName="org_xzqm" value=""><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.org.areacode.null" /></pg:equal>
										</td>
										<td>
											<%
												//增加角色的创建人登陆名和实名
												//baowen.liu 2008-4-14
												String creatorId = dataSet.getString("creator");
												if(creatorId == null || "".equals(creatorId)){
													out.print(RequestContextUtils.getI18nMessage("sany.pdp.sys.unknow", request));
												}else{
													User user=usermanager.getUserById(creatorId);
													String username=user.getUserName();
													String userrealname=user.getUserRealname();
													out.print(username + "【" + userrealname + "】");
												}
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
</html>
