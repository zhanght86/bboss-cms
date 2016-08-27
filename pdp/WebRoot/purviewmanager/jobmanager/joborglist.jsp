
<%@page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager"%>
<%@page import="java.util.Map"%><%
/*
 * <p>Title: 岗位所属机构列表</p>
 * <p>Description:岗位所属机构列表</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-18
 * @author liangbing.tao
 * @version 1.0
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.JobManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Job"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuTag"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenu"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuImpl"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.Menu"%>
<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>


<%
			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkManagerAccess(request,response);
			
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			//当前用户可管理机构ID，包含自己所属机构
			Map map = orgManager.getSubOrgId(accesscontroler.getUserID(),accesscontroler.isAdmin());
			String jobId = request.getParameter("jobId");
			String orgName = request.getParameter("orgName");
			String orgnumber = request.getParameter("orgnumber");
			
			Job job = new Job();
			String jobName = "";
			if(jobId==null || jobId.equals(""))
			{
				jobId = "";
			}
			else
			{
				JobManager jm = SecurityDatabase.getJobManager();
				job = jm.getJobById(jobId);
				jobName = job.getJobName();
			}			
			if (orgName == null) 
			{
				orgName = "";
			}
			if (orgnumber == null) 
			{
				orgnumber = "";
			}
%>
<html>
	<head>
		<TITLE>岗位机构</TITLE>
		<script language="JavaScript" src="../scripts/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../scripts/pager.js" type="text/javascript"></script>
		<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
		<script language="JavaScript">
			function userResList(orgId)
			{
				var url = "../purviewmanager/jobmanager/jobList_ajax.jsp?jobId=<%=jobId%>&orgId=" + orgId;
				parent.parent.$.dialog({title:'<pg:message code="sany.pdp.jobmanage.role.setting"/>',width:760,height:560, content:'url:'+url,lock: true});
			}
		</script>
	</head>

	<body class="contentbodymargin">
		<div id="" align="center">
			<form name="joborgForm" action="" method="post">
				<input type=hidden name=orgName value="<%=orgName%>" />
				<input type=hidden name=orgnumber value="<%=orgnumber%>"/>
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
					<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.JobOrgList" keyName="JobOrgList" />
					<!--分页显示开始,分页标签初始化-->
					<pg:pager maxPageItems="10" scope="request" data="JobOrgList" isList="false">
					<pg:equal actual="${JobOrgList.itemCount}" value="0" >
						<div class="nodata">
						<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
					</pg:equal> 
					<pg:notequal actual="${JobOrgList.itemCount}"  value="0">
					<pg:header>
						<th><pg:message code="sany.pdp.role.organization.name"/></th>
						<th><pg:message code="sany.pdp.role.organization.number"/></th>
						<th><pg:message code="sany.pdp.role.organization.sort"/></th>
						<th><pg:message code="sany.pdp.role.organization.description"/></th>
					</pg:header>
						<pg:param name="jobId" />
						<pg:param name="orgnumber" />
						<pg:param name="orgName" />
						<%ContextMenu contextmenu = new ContextMenuImpl();%>
						<!--list标签循环输出每条记录-->
						<pg:list>
							<%
								String orgIdm = dataSet.getString("orgId");
								String bgcolor = "#BDD5F9";
								if(map.get(orgIdm) != null){
									bgcolor = "#F6FFEF";
									Menu menu = new Menu();
									menu.setIdentity("opuser_" + orgIdm);
									Menu.ContextMenuItem menuitem1 = new Menu.ContextMenuItem();
									menuitem1.setName(RequestContextUtils.getI18nMessage("sany.pdp.jobmanage.role.setting", request));
									menuitem1.setLink("javascript:userResList('" + orgIdm + "')");
									menuitem1.setIcon("../images/rightmenu_images/doc_fbyl.gif");
									menu.addContextMenuItem(menuitem1);
									contextmenu.addContextMenu(menu);
								}
							%>
							<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'">
								<td id="opuser_<%=orgIdm%>" height='20' class="tablecells" align=left bgcolor="<%=bgcolor %>">
									<pg:null colName="remark5">
										<pg:cell colName="orgName" />
									</pg:null>
									<pg:notnull colName="remark5">
										<pg:equal colName="remark5" value="">
											<pg:cell colName="remark5" />
										</pg:equal>
										<pg:notequal colName="remark5" value="">
											<pg:cell colName="remark5" />
										</pg:notequal>
									</pg:notnull>
								</td>
								<td class="tablecells" align=left height='20'>
									<pg:cell colName="orgnumber" defaultValue="" />
								</td>
								<td class="tablecells" align=left height='20'>
									<pg:cell colName="orgSn" defaultValue="" />
								</td>
								<td class="tablecells" align=left height='20'>
									<pg:equal colName="orgdesc" value=""></pg:equal>
									<pg:notequal colName="orgdesc" value="">
										<pg:cell colName="orgdesc" />
									</pg:notequal>
								</td>
							</tr>
						</pg:list>
						<%request.setAttribute("opuser", contextmenu);%>
						<pg:contextmenu enablecontextmenu="true" context="opuser" scope="request" />
				</table>
				<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
						</pg:notequal>
					</pg:pager>
			</form>
		</div>
	</body>
</html>

