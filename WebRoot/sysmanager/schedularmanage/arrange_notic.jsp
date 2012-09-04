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
<%Date date = new Date();
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
	font-family: Tahoma, Verdana, Arial, Helvetica;
}
-->
        </style>
	</head>

	<script language="JavaScript">
	function goBack(){	
		document.location.href="../schedularmanage/noticList.jsp";
	}
	function arrange(){	
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
	    
		if(document.all.item("topic").value == "" )
		{
			alert("请填写主题!!!");
			return;
		}
		if(document.all.item("beginTime").value == " " )
		{
			alert("请选择开始时间!!!");
			return;
		}
		if(document.all.item("endTime").value == " " )
		{
			alert("请选择结束时间!!!");
			return;
		}
		
		if(document.all.item("beginTime").value >= document.all.item("endTime").value)
		{
			alert("开始时间大于结束时间");
    		return;
    	}
    	if(document.all.item("beginTime").value <= "<%=dateStr%>")
		{
			alert("开始时间小于当前时间");
    		return;
    	}
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
    	document.forms[0].action="../schedularmanage/schManager.do?method=arrangeNotic";
		document.forms[0].submit();	
	}
	
	function impactCheck(){
		if(document.all.item("beginTime").value == " " )
		{
			alert("请选择开始时间!!!");
			return;
		}
		if(document.all.item("endTime").value == " " )
		{
			alert("请选择结束时间!!!");
			return;
		}
		
		if(document.all.item("beginTime").value >= document.all.item("endTime").value)
		{
			alert("开始时间大于结束时间");
    		return;
    	}
    	if(document.all.item("beginTime").value <= "<%=dateStr%>")
		{
			alert("开始时间小于当前时间");
    		return;
    	}
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

		<form name="SchedularForm" method="post" action="">
			<p align="center" class="detailtitle">
				<br>
				<span class="style2"><strong> 安排通知</strong></span>
			</p>
			<pg:beaninfo requestKey="notic">
				<input type="hidden" name="schedularID" value="<pg:cell colName="noticID"  defaultValue=""/>">
				<input type="hidden" name="plannerID" value="<pg:cell colName="noticPlannerID"  defaultValue=""/>">
				<table width="100%" height="25" border="0" cellpadding="0" cellspacing="2" class="thin">
					<tr>
						<td height="25" class="detailtitle" width="20%">
							<strong> 主题</strong>
						</td>
						<td height="25" width="25%">
							<input type="text" name="topic" value="<pg:cell colName="topic"  defaultValue=""/>">
						</td>

						<td height="25" class="detailtitle" width="20%">
							<strong> 地点</strong>
						</td>
						<td height="25" width="25%">
							<input type="text" name="place" value="<pg:cell colName="place"  defaultValue=""/>">
						</td>
						<td>
							&nbsp;

						</td>
					</tr>
					<tr>
						<td height="25" class="detailtitle" width="20%">
							<strong> 开始时间</strong>
						</td>
						<td height="25" width="25%">
							<input type="text" name="beginTime" readonly="true" value="<pg:cell colName="beginTime"  defaultValue=""/>">
							<INPUT type="button" class="input" value="时间"  onclick="selectTime('SchedularForm.beginTime',0)">
						</td>

						<td height="25" class="detailtitle" width="20%">
							<strong> 结束时间</strong>
						</td>
						<td height="25" width="25%">
							<input type="text" name="endTime" readonly="true" value="<pg:cell colName="endTime"  defaultValue=""/>">
							<INPUT type="button" class="input" value="时间" onclick="selectTime('SchedularForm.endTime',0)">
						</td>
						<td>
							&nbsp;

						</td>
					</tr>
					<tr>
						<td height="24" width="20%"  class="detailtitle">
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
						<td height="24" width="20%" class="detailtitle">
							<strong> 是否公事</strong>
						</td>
						<td height="24"  width="30%">
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
					</tr>
					<tr>
						<td height="24" width="20%" class="detailtitle">
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

						<td height="23" class="detailtitle" width="20%">
							<strong> 开始提醒时间</strong>
						</td>
						<td width="20%">
							<input name="remindBeginTime" type="text" value="<pg:cell colName="remindBeginTime"  defaultValue=""/>" readonly="true">
							<INPUT type="button" class="input" value="时间" onclick="selectTime('SchedularForm.remindBeginTime',0)">
						</td>
						<td height="23" class="detailtitle" width="20%">
							<strong> 结束提醒时间</strong>
						</td>
						<td width="20%">
							<input name="remindEndTime" type="text" value="<pg:cell colName="remindEndTime"  defaultValue=""/>" readonly="true">
							<INPUT type="button" class="input" value="时间" onclick="selectTime('SchedularForm.remindEndTime',0)">
						</td>
					</tr>
					<tr>
						<td height="23" class="detailtitle" width="10%">
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
						</tr>
					
					<tr>
						<td  width="25%" class="detailtitle">
							<strong> 提醒方式</strong>
						</td>
						<td width="30%" class="detailtitle">
							<input type="checkBox" name="isSys" onClick="check('checkBox','isSys')" >
							<strong> 系统</strong>
							<input type="checkBox" name="isEmail" onClick="check('checkBox','isEmail')">
							<strong> 邮件</strong>
							<input type="checkBox" name="isMessage" onClick="check('checkBox','isMessage')">
							<strong> 短信</strong>
						</td>
						<td>
						</td>
						<td>
						</td>
					</tr>
					
					<tr>
						<td height="55" width="22%" class="detailtitle">
							<strong> 日程描述</strong>
						</td>
						<td height="30" width="30%">
							<textarea name="content" cols="30" rows="6"></textarea>
						</td>
						<td>
							&nbsp;
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
					<INPUT type="button" class="input" value="检测冲突" onclick="impactCheck()">
					<INPUT type="button" class="input" value="安排" onclick="arrange()">
					<INPUT type="button" class="input" value="取消" onclick="goBack()">
				</td>
			</tr>
		</table>
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
		<%@include file="sysMsg.jsp" %>
	</body>
</html>

