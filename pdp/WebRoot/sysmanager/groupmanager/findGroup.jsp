<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>用户组基本信息</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">

		<script language="javascript">
			function loadPage() {
				var form = document.forms[0];
				var act = form.action.value;

				if (act == 2) {
					form.groupId.value = "";
					form.groupName.value = "";
					form.groupDesc.value = "";
				}
				
				if (act == 1 || act == 2) {
					getNavigatorContent().location.href="../groupList.do";
				}

				form.action.value = 0;
			}
		</script>
	</head>

	<body class="contentbodymargin" onload="loadPage();">
		<div id="contentborder" align="center">
			<form action="">
				<table width="100%" height="25" border="0" cellpadding="0" cellspacing="2">
					<tr>
						<td width="16%" height="25" class="detailtitle">
							<P align="center">
								<STRONG>用户组名称：</STRONG>
							</P>
						</td>
						<td width="907" height="25">
							<input type="text" name="groupName" style="width:30%">
							<input name="b1" type="submit" class="input" value="查询">
						</td>
					</tr>
				</table>

				<hr width="98%">

				<table width="100%" cellpadding="1" cellspacing="1">
					<tr>
						<td class="detailtitle" align=center colspan=4>
							<b>组织列表</b>
						</td>
					</tr>
					<pg:listdata dataInfo="GroupList" keyName="GroupList" />
					<!--分页显示开始,分页标签初始化-->
					<pg:pager maxPageItems="5" scope="request" data="GroupList" isList="false">
						<tr>
							<!--设置分页表头-->
							<pg:header>
								<pg:title title="用户组名称" />
								<pg:title title="用户组描述" />
							</pg:header>
						</tr>
						<pg:param name="orgName" />
						<pg:param name="code" />
						<!--检测当前页面是否有记录-->
						<pg:notify>
							<tr height="18px">
								<td colspan=100 align='center'>
									暂时没有用户组
								</td>
							</tr>
						</pg:notify>

						<!--list标签循环输出每条记录-->
						<pg:list>
							<tr height="18px">
								<td align=center class="detailcontent">
									<pg:cell colName="groupName" defaultValue="" />
								</td>
								<td align=center class="detailcontent">
									<pg:cell colName="groupDesc" defaultValue="" />
								</td>
							</tr>
						</pg:list>
						<tr height="18px">
							<td colspan=100 align='center' class="detailcontent">
								<pg:index />
								<input type="hidden" name="queryString" value="<pg:querystring/>">
							</td>
						</tr>
					</pg:pager>
				</table>
			</form>
			<p></p>
		</div>
<%@ include file="../sysMsg.jsp"%>
	</body>
</html>

