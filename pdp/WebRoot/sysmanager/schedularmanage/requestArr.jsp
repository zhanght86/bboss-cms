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
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String userId = accesscontroler.getUserID();
	Date date = new Date();
	String dateStr = StringUtil.getFormatDate(date,"yyyy-MM-dd HH:mm:ss");
%>
<html>
	<head>
		<script language="JavaScript" src="common.js" type="text/javascript"></script>
		<script language="javascript" src="../scripts/selectTime.js"></script>
		<SCRIPT language="JavaScript" SRC="validateForm.js"></SCRIPT>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>添加日程</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		<style type="text/css">
<!--
.STYLE1 {color: #FF0000}
.style5 {font-size: medium}
-->
</style>
	</head>
	<script language="JavaScript">
	function goBack(){		
		document.location.href="../schedularmanage/daySchedularList.jsp";
	}
	function submitSD(){
		if(document.all.item("plannerID").value == "" )
		{
			alert("请选择安排人!!!");
			return;
		}
		document.forms[0].action="../schedularmanage/schManager.do?method=addSchedular";
		document.forms[0].submit();	
	}
	function adduser(){	    	
	    	window.open('<%=rootpath%>/sysmanager/schedularmanage/orgtwouser.jsp?displayNameInput=plannerName&displayValueInput=plannerID','newWin','scrollbars=no,status=no,titlebar=no,toolbar=no,z-lock=yes,width=616,height=500,top=150,left=250')
	}
</script>

	<body class="info" scrolling="no">

		<form name="SchedularForm" action="" method="post">

			<p align="center" class="detailtitle"><strong><strong><br>
            </strong><span class="style5">请求他人安排&nbsp; </span></strong></p>
			<pg:beaninfo requestKey="newSchedular">
				<input type="hidden" name="schedularID" value="<pg:cell colName="schedularID"  defaultValue=""/>" />
				<input type="hidden" name="executorID" value="<pg:cell colName="executorID"  defaultValue="<%=userId%>"/>" />
				<input type="hidden" name="requestID" value="<pg:cell colName="requestID"  defaultValue="0"/>" />
				<input type="hidden" name="status" value="1" />
				<input type="hidden" name="type" value="<pg:cell colName="type"  defaultValue="他人安排"/>" />
				<input type="hidden" name="isHistory" value="<pg:cell colName="isHistory"  defaultValue="0"/>" />
				<input type="hidden" name="assignmentTime" value="<pg:cell colName="assignmentTime"  defaultValue=""/>" />

				<table width="100%" height="206" border="0" cellpadding="0" cellspacing="2" class="thin">
					
					<tr>
						<td height="23" class="detailtitle" width="20%">
							<strong> 主题</strong>
						</td>
						<td>
							<input name="topic" type="text" value="<pg:cell colName="topic"  defaultValue=""/>">

						</td>
						<td height="23" class="detailtitle" width="20%">
							<strong> 地点</strong>
						</td>
						<td height="23" width="29%">
							<input name="place" type="text" value="<pg:cell colName="place"  defaultValue=""/>">
						</td>

					</tr>
					<tr>
						<td height="23" class="detailtitle">
							<strong> 开始时间</strong>
						</td>
						<td height="23" width="30%">
							<input name="beginTime" type="text" value="<pg:cell colName="beginTime"  defaultValue=""/>">
							<INPUT type="button" class="input" value="时间" onclick="selectTime('SchedularForm.beginTime',0)">
						</td>
						<td height="23" class="detailtitle">
							<strong> 结束时间</strong>
						</td>
						<td height="23" width="30%">
							<input name="endTime" type="text" value="<pg:cell colName="endTime"  defaultValue=""/>">
							<INPUT type="button" class="input" value="时间" onclick="selectTime('SchedularForm.endTime',0)">
						</td>
					</tr>
					<tr>
						<td height="24" class="detailtitle">
							<strong> 重要性</strong>
						</td>
						<td height="24" width="30%">

							<select name="essentiality" class="select">
								<option value="0">
									<pg:equal colName="essentiality" value="0">selected</pg:equal>
									重要
								</option>
								<option value="1">
									<pg:equal colName="essentiality" value="1">selected</pg:equal>
									一般
								</option>
								<option value="2">
									<pg:equal colName="essentiality" value="2">selected</pg:equal>
									不重要
								</option>
							</select>
						</td>

						<td height="24" class="detailtitle">
							<strong> 是否空闲</strong>
						</td>
						<td height="24" width="30%">
							<select name="isLeisure" class="select">
								<option value="0">
									<pg:equal colName="isLeisure" value="0">selected</pg:equal>
									忙
								</option>
								<option value="1">
									<pg:equal colName="isLeisure" value="1">selected</pg:equal>
									空闲
								</option>
							</select>
						</td>

					</tr>
					<tr>
						<td height="24" class="detailtitle">
							<strong> 是否公事</strong>
						</td>
						<td height="24" width="30%">
							<select name="isPublicAffair" class="select">
								<option value="0">
									<pg:equal colName="isPublicAffair" value="0">selected</pg:equal>
									公事
								</option>
								<option value="1">
									<pg:equal colName="isPublicAffair" value="1">selected</pg:equal>
									私事
								</option>
							</select>
						</td>
						<td height="24" class="detailtitle">
							<strong> 部门公开</strong>
						</td>
						<td height="24" width="30%">
							<select name="isOpen" class="select">
								<option value="1">
									<pg:equal colName="isOpen" value="1">selected</pg:equal>
									公开
								</option>
								<option value="0">
									<pg:equal colName="isOpen" value="0">selected</pg:equal>
									保密
								</option>
								
							</select>
						</td>
					</tr>
					<tr>
						<td height="55" class="detailtitle">
							<strong> 日程描述</strong>
						</td>
						<td height="55" width="30%">
							<textarea name="content" cols="30" rows="6"></textarea>
						</td>
						<td>
						</td>
						<td>
						</td>
					</tr>
					<tr>
						<td height="24" class="detailtitle">
							<strong> 安排人</strong>
						</td>
						<td height="23" >
							<input name="plannerID" type="hidden" value="<pg:cell colName="plannerID"  defaultValue=""/>">
							<input name="plannerName" type="text" width="30%" value="<pg:cell colName="plannerName"  defaultValue=""/>" readonly="true">
							<INPUT type="button" class="input" value="选安排人" onclick="adduser()">
						</td>
						<td>
						</td>
						<td>
						</td>
					</tr>

				</table>
			</pg:beaninfo>
			<hr width="98%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<INPUT type="button" class="input" value="提交" onclick="submitSD()">
					</td>
				</tr>
			</table>
		</form>
			<%@include file="../sysMsg.jsp" %>
	</body>
</html>

