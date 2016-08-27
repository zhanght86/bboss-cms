<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.cms.customform.*"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
%>
<html>
	<head>
		<title>分次保存</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		
<script language="JavaScript" src="common.js" type="text/javascript"></script>
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
<script src="../inc/js/func.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/public/datetime/calender_date.js" ></script>
		<script language="JavaScript" src="../sysmanager/scripts/selectTime.js" type="text/javascript"></script>
		<script type="text/javascript" src="../public/datetime/calender.js" language="javascript"></script>
		<script type="text/javascript" src="../public/datetime/calender_date.js" language="javascript"></script>
<SCRIPT language="javascript">
	function radioChoose(r){
		if(r.checked){
			if(r.id=="1"){
				saveForm.startTime.disabled = "";
				saveForm.endTime.disabled = "";
			}else{
				saveForm.degree.disabled = "";
				if(saveForm.degree.value == "") saveForm.degree.value = "1";
			}
		}else {
			if(r.id=="1"){
				saveForm.startTime.disabled = "disabled";
				saveForm.endTime.disabled = "disabled";
			}else{
				saveForm.degree.disabled = "disabled";
			}
		}
	}
	
	function subForm(){
		
	}
	
	//是否已经选择了用户/角色,是则放入hidden,否则关闭Dialog
	function loadChooseObj(){
		var treeForm = window.dialogArguments; //得到树页面的form
		alert(treeForm.roleids.value);
	}
</SCRIPT>		
<body scroll="no" onload="">
	<form name="saveForm" target="" method="post" >
		<input type="hidden" name="idString" id="idString" value=""/>
		<input type="hidden" name="type" id="type" value=""/>
	<table id="table1" width="95%" cellspacing="10%" cellpadding="10%" border="0">
		<tr><td><fieldset>
			<LEGEND align=left>
				<input type="checkbox" name="saveType" checked onclick="radioChoose(this)" id="1" />&nbsp;按时间保存
			</LEGEND>
			<table id="table1" width="100%" cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						生效时间:
					</td>
					<td align="left">
						<input type="text" id="startTime" name="startTime" width="20" onclick="showdate(document.all('startTime'))"/>
					</td>
				</tr>
				<tr>
					<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						失效时间:
					</td>
					<td align="left">
						<input type="text" width="20" id="endTime" name="endTime" onclick="showdate(document.all('endTime'))"/>
					</td>
				</tr>
   			</table>
		</fieldset></td></tr>
		<tr><td><fieldset>
			<LEGEND align=left>
				<input type="checkbox" name="saveType" onclick="radioChoose(this)" id="2" />&nbsp;按次数保存
			</LEGEND>
			<table id="table1" width="100%" cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						生效次数:
					</td>
					<td align="left">
						<input type="text" width="20" id="degree" disabled = "disabled"/>
					</td>
				</tr>
   			</table>
		</fieldset></td></tr>
		<tr><td align="right">
			<input type="button" name="save" value="确定保存" onclick="loadChooseObj()" class="input">
			<input type="button" name="close" value="关闭" class="input" onclick="window.close();">
		</td></tr>
		</table>
	</form>
</body>
</html>

