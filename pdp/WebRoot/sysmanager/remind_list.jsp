<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%AccessControl accesscontroler = AccessControl.getInstance();
            accesscontroler.checkAccess(request, response);
           %>
<html>
	<head>
		<title>消息列表</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="css/tab.winclassic.css">
		<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="include/pager.js" type="text/javascript"></script>
		<script language="JavaScript" src="scripts/common.js" type="text/javascript"></script>
		<script language="JavaScript">


	var jsAccessControl = new JSAccessControl("#ff0000","#ffffff","#eeeeee");
	var schedularID;
</script>
	<body class="contentbodymargin" scrolling="no">
		<div id="contentborder" align="center">
			<form name="RemindForm" action="" method="post">
				<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolorlight="#3B4D61" bordercolordark="#ffffff" style="padding:2">
					<!--分页显示开始,分页标签初始化-->
					<TR>
						<TD height="30" class="detailtitle" align="center" colspan="3">
							<B><div align="center" class="detailtitle style2">
									<strong>提醒信息</strong>
							</B>
						</TD>
					</TR>
					<pg:listdata dataInfo="RemindList" keyName="RemindList" />

					<pg:pager maxPageItems="15" scope="request" data="RemindList" isList="false">


						<td class="headercolor">
							时间
						</td>
						<Td class="headercolor">
							类型
						</Td>
						<Td class="headercolor">
							内容
						</Td>
						<pg:list>
							<tr class="labeltable_middle_tr_01" onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'">
								
								<td width="20%" nowrap="nowrap"  class="tablecells">
									<pg:equal colName="time" value="">&nbsp</pg:equal>
									<pg:cell colName="time" dateformat="yyyy-MM-dd HH:mm:ss" defaultValue="" />
								</td>
								<td width="20%" class="tablecells"  nowrap="nowrap">
									<pg:equal colName="source" value="">&nbsp</pg:equal>
									<pg:cell colName="source" defaultValue="" />
								</td>
								<td width="20%" class="tablecells" nowrap="nowrap">
									<pg:equal colName="content" value="">&nbsp</pg:equal>
									<pg:cell colName="content" defaultValue="" />
								</td>

							</tr>
						</pg:list>
						<tr height="18px" class="labeltable_middle_tr_01">
							<td colspan=3 align='center' nowrap="nowrap">
								<pg:index />
							</td>
						</tr>
						<input name="queryString" value="<pg:querystring/>" type="hidden">
					</pg:pager>
				</TABLE>
			</FORM>
		</div>
	</body>
</html>

