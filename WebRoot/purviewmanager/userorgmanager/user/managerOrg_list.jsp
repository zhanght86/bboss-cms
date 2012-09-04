<%
/*
* <p>Title: 用户管理机构列表查询页面</p>
* <p>Description: 用户管理机构列表查询页面</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: chinacreator</p>
* @Date 2008-3-22
* @author liangbing.tao
* @version 1.0
*/
%>


<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%
			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkManagerAccess(request,response);

			String userId = request.getParameter("userId");
			String userRealName = "";
			String userName = "";
			if (userId != null) {
				userId = request.getParameter("userId");
				UserManager userManager = SecurityDatabase.getUserManager();
				User user = userManager.getUserById(userId);
				userRealName = user.getUserRealname();
				userName = user.getUserName();
			}

			String remark5 = request.getParameter("remark5");
			String orgnumber = request.getParameter("orgnumber");
			if (remark5 == null) {
				remark5 = "";
			}

			if (orgnumber == null) {
				orgnumber = "";
			}
%>
<html>
	<head>
		<title>用户【<%=userRealName%>】可管理的部门列表</title>
		<script language="JavaScript" src="../../scripts/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../../../include/pager.js" type="text/javascript"></script>
		<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
		<script language="JavaScript">
		function resetSearch()
		{
			document.all.remark5.value = "";
			document.all.orgnumber.value = "";
		}
		
		function sub(){
			var orgName = document.all.remark5.value;
			var orgnumber = document.all.orgnumber.value;
			var tablesFrame= document.getElementsByName("orgList");
			tablesFrame[0].src = "managerOrg_list_iframe.jsp?userId=<%=userId%>&orgName=" + orgName + "&orgnumber=" + orgnumber;
		}
	</script>
	<body class="contentbodymargin" scroll="no">
		<div id="searchblock">
				<div class="search_top">
					<div class="right_top"></div>
					<div class="left_top"></div>
				</div>
				<div class="search_box">
			<form name="Org" action="" method="post">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="left_box"></td>
								<td>
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="table2">
					<tr>
						<th>
							<pg:message code="sany.pdp.role.organization.name"/>：
						</th>
						<td>
							<input type="text" name="remark5" value="<%=remark5%>" size="35">
						</td>
						<th>
							<pg:message code="sany.pdp.role.organization.number"/>：
						</th>
						<td>
							<input type="text" name="orgnumber" value="<%=orgnumber%>" size="35">
						</td>
						<td style="text-align:right">
							<a href="#" class="bt_1" id="addButton" onclick="javascript:sub()"><span><pg:message code="sany.pdp.common.operation.search"/></span></a>
				<a href="#" class="bt_2" id="delBatchButton" onclick="javascript:resetSearch()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
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
				<div class="title_box">
				<b><pg:message code="sany.pdp.organization.list"/></b>
				
			</div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">

					<tr>
						<td colspan="4">
							<iframe name="orgList" src="managerOrg_list_iframe.jsp?userId=<%=userId%>" style="width:100%" height="100%" scrolling="no" frameborder="0" marginwidth="1" marginheight="1">
							</iframe>
						</td>
					</tr>
				</table>
		</div>
	</body>

</html>

