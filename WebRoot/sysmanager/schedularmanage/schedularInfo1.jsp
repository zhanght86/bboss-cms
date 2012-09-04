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
<%@ page import="com.frameworkset.platform.schedularmanage.Schedular"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.SchedularManagerImpl"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.util.StringUtil"%>
<%@page import="java.util.*,com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>
<%AccessControl accesscontroler = AccessControl.getInstance();
            accesscontroler.checkAccess(request, response);
            String userId = accesscontroler.getUserID();
            Date date = new Date();
            String dateStr = StringUtil.getFormatDate(date,
                    "yyyy-MM-dd HH:mm:ss");
            String curDate = request.getParameter("date");
            String curDate1 = request.getParameter("date1");
            String weekday = request.getParameter("weekday");
            String beginTime = request.getParameter("beginTime");
            String endTime = request.getParameter("endTime");
            if (curDate == null)
                curDate = "";
            if (beginTime == null)
                beginTime = "";
            if (endTime == null)
                endTime = "";

            String schedularID = request.getParameter("schedularID");
            String curDate2 = request.getParameter("curDate");

            SchedularManagerImpl smi = new SchedularManagerImpl();
            Schedular sch = smi.getModifySchedular(Integer
                    .parseInt(schedularID));
            String topic = sch.getTopic();
            String place = sch.getPlace();
            Date beginTime1 = sch.getBeginTime();
            Date endTime1 = sch.getEndTime();
            Date remindBeginTime = sch.getRemindBeginTime();
            Date remindEndTime = sch.getRemindEndTime();
            String essentiality = sch.getEssentiality();
            String isPublicAffair = String.valueOf(sch.getIsPublicAffair());
            String isLeisure = String.valueOf(sch.getIsLeisure());
            String content = sch.getContent();
            String intervalType = String.valueOf(sch.getIntervalType());
            String isSys = String.valueOf(sch.getIsSys());
            String isEmail = String.valueOf(sch.getIsEmail());
            String isMessage = String.valueOf(sch.getIsMessage());
            request.setAttribute("newSchedular", sch);
%>
<html>
	<head>
		<script language="JavaScript" src="common.js" type="text/javascript"></script>
		<script language="javascript" src="../scripts/selectTime.js"></script>
		<SCRIPT language="JavaScript" SRC="validateForm.js"></SCRIPT>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>浏览日程</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		<style type="text/css">

<!--.STYLE1 {color: #FF0000}-->
<!--.style2 {-->
<!--	font-family: Tahoma, Verdana, Arial, Helvetica;-->
<!--	font-weight: bold;-->
<!--	font-size: medium;-->
<!--}-->
<!--.style3 {font-size: medium}-->

        </style>
	</head>
	<script language="JavaScript">

//self.moveTo(0,0) 
//self.resizeTo(screen.availWidth,screen.availHeight) 
function remove_remind()
	{
		document.all.item("remindBeginTime").value = "";
		document.all.item("remindEndTime").value = "";
		document.all.item("interval").value = 0;
		document.all.item("isSys").value = 0;
		document.all.item("isSys").checked = false;
		document.all.item("isSys").value = 0;
		document.all.item("isEmail").checked = false;
		document.all.item("isEmail").value = 0;
		document.all.item("isMessage").checked = false;
		document.all.item("isMessage").value = 0;
	}
function motifySD()
    {   
    	if (document.all.item("remindBeginTime").value != "")
		{
		    if (document.all.item("remindEndTime").value == "")
			{
				alert("请填写提醒结束时间");
	    		return;
			}
	    }
	    
	    if (document.all.item("remindEndTime").value != "")
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
		if(document.all.item("beginTime").value == "" )
		{
			alert("请选择开始时间!!!");
			return;
		}
		if(document.all.item("endTime").value == "" )
		{
			alert("请选择结束时间!!!");
			return;
		}
		
		if(document.all.item("beginTime").value > document.all.item("endTime").value)
		{
			alert("开始时间晚于结束时间");
    		return;
    	}
    	
    	if(document.all.item("remindBeginTime").value > document.all.item("remindEndTime").value)
		{
			alert("提醒开始时间晚于提醒结束时间");
    		return;
    	}
    	
    	if(document.all.item("remindEndTime").value > document.all.item("endTime").value)
		{
			alert("提醒结束时间晚于日程结束时间");
    		return;
    	}
    	check('checkBox','isSys');
    	check('checkBox','isEmail');
    	check('checkBox','isMessage');
	    document.forms[0].action="../schedularmanage/schManager.do?method=modifySchedular&path=daySchedularList&curDate=<%=curDate%>&date1=<%=curDate1%>&weekday=<%=weekday%>";
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
	function goback()
	{
	    document.forms[0].action="daySchedularList.jsp?curDate=<%=curDate%>&date1=<%=curDate1%>&weekday=<%=weekday%>";
		document.forms[0].submit();
		return true;
	}
	
	function addRemind(){
		if(document.all.item("remindTime").value == "" )
		{
			alert("请填写提醒时间!!!");
			return;
		}
		var remindBeginTime =document.all.item("remindTime").value;
		var remindEndTime =document.all.item("beginTime").value;
  		var linkurl = "remindList.jsp" ;  	
  		document.resource_bridge.location = linkurl + "?remindBeginTime=" + remindBeginTime + "&remindEndTime=" + remindEndTime;
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

	<body class="info" scrolling="no">
		<form name="schedularForm" action="" method="post">
			<pg:beaninfo requestKey="newSchedular">
				<div align="center" class="detailtitle style3">
					<p class="style4">
						<br>
						<span class="style2">个人日程</span>
					</p>
				</div>
				<input type="hidden" name="schedularID" value="<%=schedularID%>" />
				<input type="hidden" name="plannerID" value="<pg:cell colName="plannerID"  defaultValue="<%=userId%>"/>" />
				<input type="hidden" name="executorID" value="<pg:cell colName="executorID"  defaultValue="<%=userId%>"/>" />
				<input type="hidden" name="requestID" value="<pg:cell colName="requestID"  defaultValue="0"/>" />
				<input type="hidden" name="type" value="<pg:cell colName="type"  defaultValue="自己安排"/>" />
				<input type="hidden" name="status" value="<pg:cell colName="status"  defaultValue="0"/>" />
				<input type="hidden" name="isHistory" value="<pg:cell colName="isHistory"  defaultValue="0"/>" />
				<input type="hidden" name="data1" value="<pg:cell colName="data1"  defaultValue="<%=curDate1%>"/>" />
				<input type="hidden" name="weekday" value="<pg:cell colName="weekday"  defaultValue="<%=weekday%>"/>" />
				<table width="100%" height="206" border="0" cellpadding="0" cellspacing="2" class="thin">
					
					<tr>
						<td height="23" class="detailtitle" width="20%">
							<strong> 主题</strong>
						</td>
						<td>
							<input name="topic" type="text" value="<%=topic%>">
						</td>
						<td height="23" class="detailtitle" width="30%">
							<strong> 地点</strong>
						</td>
						<td height="23" width="29%">
							<input name="place" type="text" value="<%=place%>">
						</td>
						
					</tr>
					<tr>
						<td height="23" class="detailtitle">
							<strong> 开始时间</strong>
						</td>
						<td height="23" width="30%">
							<input name="beginTime" type="text" readonly="true" value="<pg:cell colName="beginTime"  dateformat="yyyy-MM-dd HH:mm:ss" defaultValue="<%=beginTime1%>"/>">
							<INPUT type="button" class="input" value="时间" onclick="selectTime('schedularForm.beginTime',0)">
						</td>
						<td height="23" class="detailtitle" nowrap="nowrap">
							<strong> 结束时间</strong>
						</td>
						<td height="23" width="30%" nowrap="nowrap">
							<input name="endTime" type="text" readonly="true" value="<pg:cell colName="endTime"  dateformat="yyyy-MM-dd HH:mm:ss" defaultValue="<%=endTime1%>"/>">
							<INPUT type="button" class="input" value="时间" onclick="selectTime('schedularForm.endTime',0)">

						</td>
					</tr>

					<tr>
						<td height="24" class="detailtitle">
							<strong> 重要性</strong>
						</td>
						<td height="24" width="30%">
							<select name="essentiality" class="select">
								<option value="1" <pg:equal colName="essentiality" value="1">selected</pg:equal>>
									一般
								</option>
								<option value="0" <pg:equal colName="essentiality" value="0">selected</pg:equal>>
									重要
								</option>
								<option value="2" <pg:equal colName="essentiality" value="2">selected</pg:equal>>
									不重要
								</option>
							</select>
						</td>
						<td height="24" class="detailtitle">
							<strong> 是否空闲</strong>
						</td>
						<td height="24" width="30%">
							<select name="isLeisure" class="select">
								<option value="0" <pg:equal colName="isLeisure" value="0">selected</pg:equal>>
									忙
								</option>
								<option value="1" <pg:equal colName="isLeisure" value="1">selected</pg:equal>>
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
								<option value="0" <pg:equal colName="isPublicAffair" value="0">selected</pg:equal>>
									公事
								</option>
								<option value="1" <pg:equal colName="isPublicAffair" value="1">selected</pg:equal>>
									私事
								</option>
							</select>
						</td>
						<td height="25" class="detailtitle" width="25%">
							<strong> 部门公开</strong>
						</td>
						<td height="25" width="30%">
							<select name="isOpen" class="select">
								<option value="0" <pg:equal colName="isOpen" value="0">selected</pg:equal>>
									保密
								</option>
								<option value="1" <pg:equal colName="isOpen" value="1">selected</pg:equal>>
									公开
								</option>
							</select>
						</td>
					</tr>
					<tr>

						<td height="23" class="detailtitle" width="20%">
							<strong> 开始提醒时间</strong>
						</td>
						<td width="20%">
							<input name="remindBeginTime" type="text" readonly="true" value="<pg:cell colName="remindBeginTime" dateformat="yyyy-MM-dd HH:mm:ss" defaultValue="<%=remindBeginTime%>"/>">
							<INPUT type="button" class="input" value="时间" onclick="selectTime('schedularForm.remindBeginTime',0)">
						</td>
						<td height="23" class="detailtitle" width="30%">
							<strong> 结束提醒时间</strong>
						</td>
						<td width="20%">
							<input name="remindEndTime" type="text" readonly="true" value="<pg:cell colName="remindEndTime" dateformat="yyyy-MM-dd HH:mm:ss" defaultValue="<%=remindEndTime%>"/>">
							<INPUT type="button" class="input" value="时间" onclick="selectTime('schedularForm.remindEndTime',0)">
						</td>
					</tr>
					<tr>

						<td height="23" class="detailtitle" width="20%">
							<strong> 提醒间隔</strong>
						</td>
						<td height="23" class="detailtitle" width="30%">
							<input name="interval" type="text" value="<pg:cell colName="interval"  defaultValue=""/>">
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
						<td width="20%" class="detailtitle">
							<strong> 提醒方式</strong>
						</td>
						<td width="30%">
							<input type="checkBox" name="isSys" <pg:equal colName="isSys" value="1">checked</pg:equal> onClick="check('checkBox','isSys')">
							<strong> 系统</strong>
							<input type="checkBox" name="isEmail" <pg:equal colName="isEmail" value="1">checked</pg:equal> onClick="check('checkBox','isEmail')">
							<strong> 邮件</strong>
							<input type="checkBox" name="isMessage" <pg:equal colName="isMessage" value="1">checked</pg:equal> onClick="check('checkBox','isMessage')">
							<strong> 短信</strong>
						</td>

					</tr>
					<tr>
						<td height="55" class="detailtitle">
							<strong> 日程描述</strong>
						</td>
						<td height="55" width="30%">
							<textarea name="content" cols="25" rows="6" defaultValue=""><%=content%></textarea>
						</td>
						<td height="55" class="detailtitle">
							<strong> </strong>
						</td>
						<td height="55" class="detailtitle">
							<strong> </strong>
						</td>
					</tr>
				</table>
			</pg:beaninfo>
			<hr width="98%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<INPUT type="button" class="input" value="检测冲突" onclick="impactCheck()">
						<input type="button" class="input"  value="取消提醒" onclick="remove_remind()">
						<INPUT type="button" class="input" value="修改" onclick="motifySD()">
						<INPUT type="button" class="input" value="返回" onclick="goback()">
					</td>
				</tr>
				<tr>
			</table>
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
	</body>
	<%@include file="../sysMsg.jsp"%>
</html>

