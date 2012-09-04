<%
/*
 * <p>Title: 机构资源查询列表</p>
 * <p>Description: 机构资源查询列表</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-26
 * @author liangbing.tao
 * @version 1.0
 *
 */
 %>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.config.ConfigManager"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	String typeName = request.getParameter("typeName");
	//System.out.println("typeName = " + typeName);
%>

<html>
	<head>    
		<title>属性容器</title>
		<script language="JavaScript" src="../../scripts/common.js" type="text/javascript"></script>		
		<script language="JavaScript" src="../../scripts/pager.js" type="text/javascript"></script>
		<SCRIPT language="javascript">
			function getOperateType(id)
			{
			    getopergroup.location.href = "resChange.jsp?restypeId="+id;
			}
		</SCRIPT>
		<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
	</head>
	<body class="contentbodymargin">
		<div align="center">
				<div id="changeColor">
					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table3" id="tb">	
					 <pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.BrowseResList" 
					 													keyName="BrowseResList"/> 
					 													
					<!--分页显示开始,分页标签初始化-->
					
					<pg:pager maxPageItems="20" scope="request" data="BrowseResList" isList="false">
					<pg:equal actual="${BrowseResList.itemCount}" value="0" >
				 	
						<div class="nodata">
						<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
						
				</pg:equal> 
				<pg:notequal actual="${BrowseResList.itemCount}"  value="0">
					<pg:header>
						<th><pg:message code="sany.pdp.role.resource.type"/></th>
						<th><pg:message code="sany.pdp.role.resource.identifying"/></th>
						<th><pg:message code="sany.pdp.role.resource.name"/></th>
						<th><pg:message code="sany.pdp.role.operation.type"/></th>
						<th><pg:message code="sany.pdp.role.resource.source"/></th>
						<th><pg:message code="sany.pdp.role.empower.time"/></th>
						<% 
							if(ConfigManager.getInstance().getConfigBooleanValue("enablerresmanaer", false))
							{
						%>
						<th><pg:message code="sany.pdp.role.resource.category"/></th>
						<% 
							}
						%>
						
					</pg:header>
					
						<!-- <td height='20' class="headercolor" nowrap>角色标识</td> -->
					
					
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
		
							      
					<!--list标签循环输出每条记录-->			      
					<pg:list>
					<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'">	      	
						<td class="tablecells" align=left  nowrap>
							<pg:cell colName="resTypeName" defaultValue=""/>
						</td>							
						<td class="tablecells" align=left nowrap>
							<pg:cell colName="resId" defaultValue=""/>
						</td>
						<td class="tablecells" align=left nowrap>
							<pg:cell colName="resName" defaultValue=""/>
						</td>
						<td class="tablecells" align=left>
							<pg:cell colName="opName" defaultValue=""/>
						</td>
						<td class="tablecells" align=left>
							<pg:cell colName="resResource" defaultValue=""/>
						</td>
						<td class="tablecells" align=left>
							<pg:cell colName="sDate" defaultValue="" dateformat="yyyy-MM-dd HH:mm:ss"/>
						</td>
						<% 
							if(ConfigManager.getInstance().getConfigBooleanValue("enablerresmanaer", false))
							{
						%>
							<td class="tablecells" align=left>
								<pg:equal colName="auto" value="0" ><pg:message code="sany.pdp.role.system.resource"/></pg:equal>
								<pg:equal colName="auto" value="1" ><font color="#0000FF"><pg:message code="sany.pdp.role.user-defined.resource"/></font></pg:equal>
							</td>
						<% 
							}
						%>
						<!-- <td class="tablecells" align=left nowrap><pg:cell colName="roleId" defaultValue=""/></td> -->
					</tr>
					</pg:list>
					
				</table>
				<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="5,10,20,50,100"/></div>
					</pg:notequal>
					</pg:pager>
			</form>	
		</div>
	<iframe id="getopergroup" src="" border="0" height="0" width="0"></iframe>
	</body>

</html>
