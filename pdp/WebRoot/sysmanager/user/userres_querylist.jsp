<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.config.ConfigManager"%>
<%@ page import="java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%
AccessControl accesscontroler = AccessControl.getInstance();
accesscontroler.checkAccess(request, response);
//System.out.println("dddddddddddd =  " + request.getParameter("typeName"));
%>
<html>
<head>    
 <title>属性容器</title>
<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
<SCRIPT language="javascript">

function getOperateType(id){
    getopergroup.location.href = "../user/resChange.jsp?restypeId="+id;
}
</SCRIPT>
<body class="contentbodymargin">
<div id="changeColor">
			<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.BrowseResList" keyName="BrowseResList"/> 
			<!--分页显示开始,分页标签初始化-->
			<pg:pager maxPageItems="10" scope="request" data="BrowseResList" isList="false">
				<pg:param name="restypeId"/>
				<pg:param name="resId"/>
				<pg:param name="resName"/>
				<pg:param name="opId"/>
				<pg:param name="userId"/>
				<pg:param name="id"/>
				<pg:param name="orgjob"/>
				<pg:param name="type"/>
				<pg:param name="name"/>
				<pg:param name="typeName"/>
				<pg:param name="resResource"/>
				<pg:param name="orgId"/>
				<pg:param name="auto"/>
				
				<pg:equal actual="${BrowseResList.itemCount}" value="0" >
					<div class="nodata">
					<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
				</pg:equal>
				<pg:notequal actual="${BrowseResList.itemCount}"  value="0">
					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb" >
						<th><pg:message code="sany.pdp.personcenter.person.res.type" /></th>
			       		<th><pg:message code="sany.pdp.personcenter.person.res.token" /></th>
			       		<th><pg:message code="sany.pdp.personcenter.person.res.name" /></th>
			       		<th><pg:message code="sany.pdp.personcenter.person.res.operation.type" /></th>
			       		<th><pg:message code="sany.pdp.personcenter.person.res.source" /></th>
			       		<th><pg:message code="sany.pdp.personcenter.person.res.authorize.time" /></th>
			       		<% 
						if(ConfigManager.getInstance().getConfigBooleanValue("enablerresmanaer", false))
						{
						%>
							<th><pg:message code="sany.pdp.personcenter.person.res.class" /></th>
						<% 
							}
						%>
						
						<pg:list>
							<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'">	      	
								<td><pg:cell colName="resTypeName" defaultValue=""/></td>							
								<td><pg:cell colName="resId" defaultValue=""/></td>
								<td><pg:cell colName="resName" defaultValue=""/></td>
								<td><pg:cell colName="opName" defaultValue=""/></td>
								<td><pg:cell colName="resResource" defaultValue=""/></td>
								<td><pg:cell colName="sDate" defaultValue="" dateformat="yyyy-MM-dd HH:mm:ss"/></td>
								<% 
									if(ConfigManager.getInstance().getConfigBooleanValue("enablerresmanaer", false))
									{
								%>
									<td>
										<pg:equal colName="auto" value="0" ><pg:message code="sany.pdp.personcenter.person.res.class.system" /></pg:equal>
										<pg:equal colName="auto" value="1" ><font color="#0000FF"><pg:message code="sany.pdp.personcenter.person.res.class.custom" /></font></pg:equal>
									</td>
								<% 
									}
								%>
								<!-- <td class="tablecells" align=left nowrap><pg:cell colName="roleId" defaultValue=""/></td> -->
							</tr>
						</pg:list>
					</table>
					<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10"/></div>
				</pg:notequal>
			</pg:pager>
	</form>	
</div>
<iframe id="getopergroup" src="" border="0" height="0" width="0"></iframe>
</body>

</html>
