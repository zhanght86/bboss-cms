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
<%@ page import="com.frameworkset.util.StringUtil"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%AccessControl accesscontroler = AccessControl.getInstance();
            accesscontroler.checkAccess(request, response);
            String userId = accesscontroler.getUserID();
            Date date = new Date();
            String dateStr = StringUtil.getFormatDate(date,
                    "yyyy-MM-dd HH:mm:ss");
%>

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
	font-weight: bold;
	font-family: Tahoma, Verdana, Arial, Helvetica;
}
.style3 {font-weight: bold}
.style4 {font-family: Tahoma, Verdana, Arial, Helvetica}
-->
        </style>
	</head>

	<script language="JavaScript">
	function goBack(){		
	document.location.href="../schedularmanage/auditingSDList.jsp";
	}
	function agree(){
		if(document.all.item("remindBeginTime").value > document.all.item("remindEndTime").value)
		{
			alert("提醒开始时间晚于提醒结束时间");
    		return;
    	}
    	if(document.all.item("remindBeginTime").value > document.all.item("beginTime").value)
		{
			alert("提醒开始时间晚于日程开始时间");
    		return;
    	}
    	if(document.all.item("remindEndTime").value > document.all.item("endTime").value)
		{
			alert("提醒结束时间晚于日程结束时间");
    		return;
    	}
    	if (document.all.item("remindBeginTime").value != "")
		{
		    if("<%=dateStr%>">document.all.item("remindBeginTime").value  )
			{
				alert("提醒开始时间早于当前时间");
	    		return;
	    	}
	    	if (document.all.item("remindEndTime").value == "")
			{
				alert("请填写提醒结束时间");
	    		return;
			}			
	    }
	    
	    if (document.all.item("remindBeginTime").value != "")
		{
			if (document.all.item("remindBeginTime").value == "")
			{
				alert("请填写提醒开始时间");
	    		return;
			}
		}
	    
    	
		document.forms[0].action="../schedularmanage/schManager.do?method=agreeSchedular";
		document.forms[0].submit();	
	}
	function refuse(){
		
	    document.forms[0].action="../schedularmanage/schManager.do?method=refuseSchedular";
		document.forms[0].submit();	
	}
	function impactCheck(){
		var beginTime =document.all.item("beginTime").value;
		var endTime =document.all.item("endTime").value;
  		var linkurl = "impactSchedular.jsp" ;  	
  		document.resource_bridge.location = linkurl + "?beginTime=" + beginTime + "&endTime=" + endTime;
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
	<body class="info"  scrolling="no">
		<form name="schedularForm" method="post" action="">
			<pg:beaninfo requestKey="schedular">
				<div align="center" class="detailtitle style3">
					<p class="style4">
						<br>
						<span class="style2">日程审批</span>
					</p>
				</div>
				<input type="hidden" name="schedularID" value="<pg:cell colName="schedularID"  defaultValue=""/>" >
				<input type="hidden" name="executorID" value="<pg:cell colName="executorID"  defaultValue="<%=userId%>"/>">
				<table width="100%" height="25" border="0" cellpadding="0" cellspacing="2" class="thin">
					<tr>
						<td height="25" class="detailtitle" width="25%">
							<strong> 主题</strong>
						</td>
						<td height="25" width="25%">
							<input type="text" name="topic" value="<pg:cell colName="topic"  defaultValue=""/>" readonly="true">
						</td>

						<td height="25" class="detailtitle" width="25%">
							<strong> 地点</strong>
						</td>
						<td height="25" width="25%">
							<input type="text" name="place" value="<pg:cell colName="place"  defaultValue=""/>" readonly="true">
						</td>
						
					</tr>
					<tr>
						<td height="25" class="detailtitle" width="25%">
							<strong> 开始时间</strong>
						</td>
						<td height="25" width="25%">
							<input type="text" name="beginTime" onClick="selectTime('SchedularForm.beginTime',0)" value="<pg:cell colName="beginTime"  defaultValue=""/>" readonly="true">
						</td>

						<td height="25" class="detailtitle" width="25%">
							<strong> 结束时间</strong>
						</td>
						<td height="25" width="25%">
							<input type="text" name="endTime" onClick="selectTime('SchedularForm.beginTime',0)" value="<pg:cell colName="endTime"  defaultValue=""/>" readonly="true">
						
							<INPUT type="button" class="input" value="检测冲突" onclick="impactCheck()">
						</td>
					</tr>
					<tr>
						<td height="25" class="detailtitle" width="25%">
							<strong> 重要性</strong>
						</td>
						<td height="25" width="25%">
							<pg:equal colName="essentiality" value="0">
								<input type="text" name="type" readonly="true" value="重要">
							</pg:equal>
							<pg:equal colName="essentiality" value="1">
								<input type="text" name="type" readonly="true" value="一般">
							</pg:equal>
							<pg:equal colName="essentiality" value="2">
								<input type="text" name="type" readonly="true" value="不重要">
							</pg:equal>
						</td>

						<td height="25" class="detailtitle" width="25%">
							<strong> 是否空闲</strong>
						</td>
						<td height="25" width="25%">
							<pg:equal colName="isLeisure" value="0">
								<input type="text" name="type" readonly="true" value="忙">
							</pg:equal>
							<pg:equal colName="isLeisure" value="1">
								<input type="text" name="type" readonly="true" value="空闲">
							</pg:equal>
						</td>
					</tr>
					<tr>
						<td height="25" class="detailtitle" width="25%">
							<strong> 是否公事</strong>
						</td>
						<td height="25" width="25%">
							<pg:equal colName="isPublicAffair" value="0">
								<input type="text" name="type" readonly="true" value="公事">
							</pg:equal>
							<pg:equal colName="isPublicAffair" value="1">
								<input type="text" name="type" readonly="true" value="私事">
							</pg:equal>
						</td>
						<td height="25" class="detailtitle" width="25%">
							<strong> 部门公开</strong>
						</td>
						<td height="25" width="25%">
							<pg:equal colName="isOpen" value="1">
						<input type="text" name="type" readonly="true" value="公开">
					</pg:equal>
							<pg:equal colName="isOpen" value="0">
						<input type="text" name="type" readonly="true" value="保密">
					</pg:equal>
						</td>
					</tr>
					<tr>
						<td height="25" class="detailtitle" width="25%">
							<strong> 日程描述</strong>
						</td>
						<td height="25" width="25%">
							<TEXTAREA cols="30" readonly="true" rows="4"><pg:cell colName="content" defaultValue="" /></TEXTAREA>
						</td>
					
						<td height="25" class="detailtitle" width="25%">
							<strong> 审核意见</strong>
						</td>
						<td height="25" width="25%">
							<textarea name="advice" cols="30" rows="4"></textarea>
						</td>
						
					</tr>
			
					<tr>
						<td height="23" class="detailtitle" width="20%">
							<strong> 开始提醒时间</strong>
						</td>
						<td width="20%">
							<input name="remindBeginTime" readonly="true" type="text" value="<pg:cell colName="remindBeginTime"  defaultValue=""/>">
							<INPUT type="button" class="input" value="时间" onclick="selectTime('schedularForm.remindBeginTime',0)">
						</td>
						<td height="23" class="detailtitle" width="20%">
							<strong> 结束提醒时间</strong>
						</td>
						<td width="20%">
							<input name="remindEndTime" readonly="true" type="text" value="<pg:cell colName="remindEndTime"  defaultValue=""/>">
							<INPUT type="button" class="input" value="时间" onclick="selectTime('schedularForm.remindEndTime',0)">
						</td>
					</tr>
					<tr>
						<td height="23" class="detailtitle" width="10%" nowrap="nowrap">
							<strong> 提醒间隔</strong>
						</td>
						<td>
							<input name="interval" type="text" value="<pg:cell colName="interval"  defaultValue="30"/>">
							<select name="intervalType" class="select">
								<option value="0" <pg:equal colName="intervalType" value="0">selected</pg:equal>>
									分
								</option>
								<option value="1" <pg:equal colName="intervalType" value="1">selected</pg:equal>>
									小时
								</option>
								<option value="2" <pg:equal colName="intervalType" value="2">selected</pg:equal>>
									天
								</option>
							</select>
						</td>
						<td height="23" class="detailtitle" width="10%" nowrap="nowrap">
							<strong> 提醒方式</strong>
						</td>
						<td height="23" class="detailtitle" width="30%">
							<input type="checkBox" name="isSys" onClick="check('checkBox','isSys')">
							<strong> 系统</strong>
							<input type="checkBox" name="isEmail" onClick="check('checkBox','isEmail')">
							<strong> 邮件</strong>
							<input type="checkBox" name="isMessage" onClick="check('checkBox','isMessage')">
							<strong> 短信</strong>
						</td>
					</tr>
				</table>
				<table>
					<tr>
						<td height="23" class="detailtitle" width="30%">
							<INPUT type="button" class="input" value="同意日程" onclick="agree()">
							<INPUT type="button" class="input" value="拒绝日程" onclick="refuse()">
							<INPUT type="button" class="input" value="取消审核" onclick="goBack()">
						</td>
					</tr>
				
				</table>
			</pg:beaninfo>

			<hr width="98%">
		</form>
		<table height="40%" width="100%">
			<tr height="10%" width="100%">
				<td width="100%"></td>
			</tr>
			<tr height="100%" width="100%">
				<td height="100%" width="100%">
					<iframe id="resource_bridge" FRAMEBORDER="0" name="resource_bridge" src="" height="100%" width="100%" />
				</td>
			</tr>
		</table>
		<%@include file="../sysMsg.jsp" %>
	</body>
</html>

