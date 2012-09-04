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
<%AccessControl accesscontroler = AccessControl.getInstance();
            accesscontroler.checkAccess(request, response);
%>
<html>
	<head>
		<script language="JavaScript" src="common.js" type="text/javascript"></script>
		<script language="javascript" src="../scripts/selectTime.js"></script>
		<SCRIPT language="JavaScript" SRC="validateForm.js"></SCRIPT>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>发送消息</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/tab.winclassic.css">
		<style type="text/css">
<!--
.STYLE1 {color: #FF0000}
.style2 {
	font-family: Tahoma, Verdana, Arial, Helvetica;
	font-weight: bold;
	font-size: medium;
}
.style3 {font-size: medium}
-->
        </style>
	</head>
	<script language="JavaScript">
	function selectfun(){
	    	window.open('<%=rootpath%>/pages/tree/fun_tree.jsp','newWin','scrollbars=no,status=no,titlebar=no,toolbar=no,z-lock=yes,width=616,height=500,top=150,left=250')
		}
	function send(){
		if (document.all.item("executors").value == "")
			{
				alert("请填写接收人");
	    		return;
			}
		check('checkBox','isSys');
    	check('checkBox','isEmail');
    	check('checkBox','isMessage');
		var isSys = document.forms[0].isSys.value;
		var isEmail = document.forms[0].isEmail.value;
		var isMessage = document.forms[0].isMessage.value;
		
		document.forms[0].action="../remindinfomanage/remindinfomanage.do?method=sendRemindInfo&isSys="+isSys+"&isEmail="+isEmail+"&isMessage="+isMessage+"";
		document.forms[0].submit();	
	}
	function check(totalCheck,checkName){
	
		var o = document.all.item(checkName);
		if(o.checked)
		{
			o.value = 1;
		}
		else
		{
			o.value = 0;
		}
	}
	function adduser(){
	    	window.open('<%=rootpath%>/sysmanager/remindmanager/selectexecutor.jsp?displayNameInput=partner&displayValueInput=executors','newWin','scrollbars=no,status=no,titlebar=no,toolbar=no,z-lock=yes,width=616,height=500,top=150,left=250')
		}
		
	function check(totalCheck,checkName){
	
		var o = document.all.item(checkName);
		if(o.checked)
		{
			o.value = 1;
		}
		else
		{
			o.value = 0;
		}
	}
</script>
	<body class="info">

		<form name="remindinfoForm" action="" method="post">
			<pg:beaninfo requestKey="newSchedular">
				<div align="center" class="detailtitle style3">
					<p class="style4">
						<br>
						<span class="style2">发送提醒</span>
						<br>
						<br>
					</p>
				</div>
				<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolorlight="#3B4D61" bordercolordark="#ffffff" style="padding:2">
					<tr>
						<td align="right">
							<INPUT type="button" name="se" value="发送" class="input" onclick="send()">
						</td>
					</tr>
				</table>
				<table width="150%" cellpadding="0" cellspacing="0" border="1" bordercolorlight="#3B4D61" bordercolordark="#ffffff" style="padding:2">
					<tr>
						<td height="23" width="5%">
							<strong> 提醒方式</strong>
						</td>
						<td height="23" width="30%">
							<input type="checkBox" name="isSys" onClick="check('checkBox','isSys')" >
							<strong> 系统</strong>
							<input type="checkBox" name="isEmail" onClick="check('checkBox','isEmail')" >
							<strong> 邮件</strong>
							<input type="checkBox" name="isMessage" onClick="check('checkBox','isMessage')" >
							<strong> 短信</strong>
						</td>
					</tr>
					<tr>
						<td height="23" width="5%">
							<strong>内容</strong>
						</td>
						<td height="23" width="30%">
							<textarea name="content" cols="25" rows="6"></textarea>
						</td>
					</tr>
					<tr>
						<td height="23" width="5%">
							<strong> 接收人</strong>
						</td>
						<td height="23" width="30%">
							<input name="executors" type="hidden" value="<pg:cell colName="executors"  defaultValue=""/>" />
							<textarea name="partner" cols="25" rows="6" value="<pg:cell colName="partner"  defaultValue=""/>" readonly="true"></textarea>
							<br>
							<INPUT type="button" class="input" value="选接收人" onclick="adduser()">
						</td>
					</tr>
					<tr>
						<td height="23" width="5%">
							<strong> 消息类型</strong>
						</td>
						<td height="23" width="30%">
							<input name="source" type="text" value="" />
						</td>
					</tr>
				</table>
			</pg:beaninfo>
			<hr width="98%">
		</form>
	</body>
</html>


