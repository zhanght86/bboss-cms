<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="java.util.*,com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager"%>
<%@ page import="com.frameworkset.util.StringUtil"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<html>
	<head>
		<script language="JavaScript" src="common.js" type="text/javascript"></script>
		<script language="javascript" src="../scripts/selectTime.js"></script>
		<SCRIPT language="JavaScript" SRC="validateForm.js"></SCRIPT>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>userInfo4</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		<style type="text/css">
<!--
.STYLE1 {color: #FF0000}
.style2 {
	font-size: medium;
	font-family: Tahoma, Verdana, Arial, Helvetica;
}
-->
        </style>
	</head>

	<script language="JavaScript">
	function goBack(){	
		document.location.href="../schedularmanage/noticList.jsp";
	}
</script>

	<body class="info" scrolling="no">

		<form name="noticForm" method="post" action="">

			<p align="center" class="detailtitle style2">
				<strong><br> 查看通知</strong>
			</p>
			<pg:beaninfo requestKey="notic">

				<table width="100%" height="25" border="0" cellpadding="0" cellspacing="2" class="thin">
					<tr>
						<td height="25" class="detailtitle" width="25%">
							<strong> 主题</strong>
						</td>
						<td height="25" width="25%">
							<input type="text" name="topic" value="<pg:cell colName="topic"  defaultValue=""/>">
						</td>
						<td height="25" class="detailtitle" width="25%">
							<strong> 地点</strong>
						</td>
						<td height="25" width="25%">
							<input type="text" name="place" value="<pg:cell colName="place"  defaultValue=""/>">
						</td>


					</tr>
					<tr>
						<td height="25" class="detailtitle" width="25%">
							<strong> 开始时间</strong>
						</td>
						<td height="25" width="25%">
							<input type="text" name="beginTime" value="<pg:cell colName="beginTime"  defaultValue=""/>">
						</td>

						<td height="25" class="detailtitle" width="25%">
							<strong> 结束时间</strong>
						</td>
						<td height="25" width="25%">
							<input type="text" name="endTime" value="<pg:cell colName="endTime"  defaultValue=""/>">
						</td>

					</tr>
					<tr>
						<td height="25" class="detailtitle" width="25%">
							<strong> 通知来源</strong>
						</td>
						<td height="25" width="25%">
							<input type="text" name="source" value="<pg:cell colName="source"  defaultValue=""/>">
						</td>
						<td height="25" class="detailtitle" width="25%">
							<strong> 通知安排人</strong>
						</td>
						<td height="25" width="25%">
							<input type="text" name="noticPlannerName" value="<pg:cell colName="noticPlannerName"  defaultValue=""/>">
						</td>

					</tr>
					<tr>
						<td height="25" class="detailtitle" width="25%">
							<strong> 通知状态</strong>
						</td>
						<td height="25" width="25%">
							<pg:equal colName="status" value="0">
						已安排
					</pg:equal>
							<pg:equal colName="status" value="1">
						未安排
					</pg:equal>
						</td>
						<td>
						</td>
						<td>
						</td>
					</tr>

					<tr>
						<td height="25" class="detailtitle" width="25%">
							<strong> 日程描述</strong>
						</td>
						<td height="25" width="25%">
							<TEXTAREA cols="30" rows="6" name="content">
								<pg:cell colName="content" defaultValue="" />
							</TEXTAREA>
						</td>
						<td>
						</td>
						<td>
						</td>
					</tr>
				</table>
			</pg:beaninfo>

		</form>

		<hr width="98%">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>

					<INPUT type="button" class="input" value="返回" onclick="goBack()">

				</td>
			</tr>
		</table>
		<%@include file="../sysMsg.jsp"%>
	</body>
</html>

