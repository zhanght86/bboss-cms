
<%
	/*
	 * <p>Title: 岗位所属机构查询</p>
	 * <p>Description:岗位所属机构查询</p>
	 * <p>Copyright: Copyright (c) 2008</p>
	 * <p>Company: chinacreator</p>
	 * @Date 2008-3-18
	 * @author liangbing.tao
	 * @version 1.0
	 */
%>


<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page
	import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page
	import="com.frameworkset.platform.sysmgrcore.manager.JobManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Job"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request, response);

	String jobId = request.getParameter("jobId");

	JobManager jm = SecurityDatabase.getJobManager();
	Job job = jm.getJobById(jobId);

	String orgName = request.getParameter("orgName");
	String orgnumber = request.getParameter("orgnumber");
	if (orgName == null) {
		orgName = "";
	}

	if (orgnumber == null) {
		orgnumber = "";
	}
%>
<html>
	<head>
		<TITLE>岗位隶属机构查询</TITLE>
		<script language="JavaScript" src="../scripts/common.js"
			type="text/javascript"></script>
		<script language="JavaScript" src="../../include/pager.js"
			type="text/javascript"></script>

		<script language="JavaScript">
		
			function sub()
			{
				var orgName = document.all("orgName").value;
				var orgnumber = document.all("orgnumber").value;
				var jobId = document.all("jobId").value;
				document.all.orgJobList.src="joborglist.jsp?orgName="+orgName+"&orgnumber="+orgnumber+"&jobId="+<%=jobId%>;
			}

			function userResList(orgId)
			{
				var url = "jobList_ajax.jsp?jobId=<%=jobId%>&orgId=" + orgId;
				//alert(url);
				window.showModalDialog(url,"","dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no");
			}

			function resetForm()
			{
				document.all("orgName").value = "";
				document.all("orgnumber").value = "";
			}
		</script>
	</head>
	<body class="contentbodymargin">
		<div style="height: 10px">&nbsp;</div>
		<div id="" align="center">
			<div id="searchblock">
				<div class="search_top">
					<div class="right_top"></div>
					<div class="left_top"></div>
				</div>
				<div class="search_box">
					<form name="joborgForm" action="" method="post">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="left_box"></td>
								<td>
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="table2">
										<input type="hidden" name="jobId" value="<%=jobId%>" />
										<tr>
											<th>
												<pg:message code="sany.pdp.role.organization.name"/>:
											</th>
											<td>
												<input type="text" name="orgName" value="<%=orgName%>" />
											</td>
											<th>
												<pg:message code="sany.pdp.role.organization.number"/>:
											</th>
											<td>
												<input type="text" name="orgnumber" value="<%=orgnumber%>" />
											</td>
											<td height='20' class="tablecells" align=center>
												<a class="bt_1" onClick="sub()"><span><pg:message code="sany.pdp.common.operation.search"/></span>
												</a>
												<a class="bt_2" onClick="resetForm()"><span><pg:message code="sany.pdp.common.operation.reset"/></span>
												</a>
												<a class="bt_2" onClick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span>
												</a>
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
								<iframe width="100%" height=410px frameborder=0 noResize
									scrolling="false" marginWidth=0 name="orgJobList"
									src="joborglist.jsp?jobId=<%=jobId%>"></iframe>
							</div>
	</body>
</html>

