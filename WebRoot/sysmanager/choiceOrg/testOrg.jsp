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
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.util.StringUtil"%>
<%@page import="java.util.*,com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>
<%
%>
<html>
	<head>
		<script language="JavaScript" src="common.js" type="text/javascript"></script>
		<script language="javascript" src="../scripts/selectTime.js"></script>
		<SCRIPT language="JavaScript" SRC="validateForm.js"></SCRIPT>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>安排日程</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		<style type="text/css">
<!--
.STYLE1 {color: #FF0000}
.style3 {font-size: medium}
.style4 {
	font-family: Tahoma, Verdana, Arial, Helvetica;
	font-weight: bold;
}
-->
        </style>
	</head>

	<script language="JavaScript">


	function addorg(){	    	
	    	window.open('<%=rootpath%>/sysmanager/choiceOrg/org2suborg.jsp?displayNameInput=executorName&displayValueInput=executors','newWin','scrollbars=no,status=no,titlebar=no,toolbar=no,z-lock=yes,width=616,height=500,top=150,left=250')
			
		}	
</script>

	<body class="info"  scrolling="no">
		<form name="SchedularForm" action="" method="post">
			

				<table width="100%" height="206" border="0" cellpadding="0" cellspacing="2">
					<tr>
						<td height="23" class="detailtitle" width="20%">
							<strong> 选择机构</strong>
						</td>
					</tr>					
					<tr>
						<td height="23" width="20%">
							<input name="executors" type="hidden" value="">
							<textarea name="executorName" cols="35" rows="6" value="" readonly="true">
							</textarea>
							<INPUT type="button" value="选机构" onclick="addorg()">
						</td>
					</tr>
				</table>
			
<!--			<table width="100%" border="0" cellspacing="0" cellpadding="0">-->
<!--				<tr>-->
<!--					<td>-->
<!--						<INPUT type="button" value="提交" onclick="submitSD()">-->
<!---->
<!--						<INPUT type="button" value="返回" onclick="javaScript:history.back()">-->
<!--					</td>-->
<!--				</tr>-->
<!--			</table>-->
		</form>
	</body>
</html>

