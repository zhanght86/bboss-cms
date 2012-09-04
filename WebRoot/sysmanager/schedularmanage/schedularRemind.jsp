<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkAccess(request, response);
			String userId = accesscontroler.getUserID();
			
%>	
<html>
	<head>
		<title>日程提醒列表</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
		<script language="JavaScript" src="../scripts/common.js" type="text/javascript"></script>
		<script language="JavaScript">


	var jsAccessControl = new JSAccessControl("#ff0000","#ffffff","#eeeeee");
	var schedularID;
</script>
	<body class="contentbodymargin" scrolling="no">
		<div id="contentborder" align="center">
			<FORM name="Org" action="" method="post">
				<TABLE width="100%" cellpadding="1" cellspacing="1" bordercolor="#EEEEEE" class="thin">
					<!--分页显示开始,分页标签初始化-->
					<TR>
						<TD height="30" class="detailtitle" align="center" colspan="8">
							<B>提醒日程列表</B>
						</TD>
					</TR>
					<pg:listdata dataInfo="SchedularRemindList" keyName="SchedularRemindList" />

					<pg:pager maxPageItems="15" scope="request" data="SchedularRemindList" isList="false">
						<Td class="headercolor">
							主题
						</Td>
						<Td class="headercolor">
							地点
						</Td>
						<Td class="headercolor">
							日程开始时间
						</Td>
						<Td class="headercolor">
							日程结束时间
						</Td>
						<Td class="headercolor">
							日程类型
						</Td>
						<pg:list>
							<tr class="labeltable_middle_tr_01" onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'">
								<td class="tablecells" width="20%" nowrap="nowrap">

									<pg:equal colName="topic" value="">无主题</pg:equal>
									<pg:cell colName="topic" defaultValue="无主题" />
								</td>
								<td class="tablecells" width="20%" nowrap="nowrap">

									<pg:equal colName="place" value="">无地点</pg:equal>
									<pg:cell colName="place" defaultValue="无地点" />

								</td>
								<td class="tablecells" width="20%" nowrap="nowrap">

									<pg:equal colName="beginTime" value="">未安排</pg:equal>
									<pg:cell colName="beginTime" dateformat="yyyy-MM-dd HH:mm:ss" defaultValue="未安排" />
								</td>
								<td class="tablecells" width="20%" nowrap="nowrap">

									<pg:equal colName="endTime" value="">未安排</pg:equal>
									<pg:cell colName="endTime" dateformat="yyyy-MM-dd HH:mm:ss" defaultValue="未安排" />
								</td>

								<td class="tablecells" width="15%" nowrap="nowrap">
									<pg:equal colName="type" value="">类型不确定</pg:equal>
									<pg:cell colName="type" defaultValue="无" />
								</td>

							</tr>
						</pg:list>
						<tr height="18px" class="labeltable_middle_tr_01">
							<td colspan=5 align='center' nowrap="nowrap">
								<pg:index />
							</td>
						</tr>
						<input name="queryString" value="<pg:querystring/>" type="hidden">
					</pg:pager>
				</TABLE>
			</FORM>
		</div>
		<%@include file="sysMsg.jsp" %>
	</body>
</html>

