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
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
	Date date = new Date();
	String dateStr = StringUtil.getFormatDate(date,"yyyy-MM-dd HH:mm:ss");
	String path = request.getParameter("path");
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
	font-weight: bold;
}
-->
        </style>
	</head>

	<script language="JavaScript">
	var path = "<%=path%>";
	function goBack(){		
		
		if(path == "arrangeSDList")	
			document.location.href="../schedularmanage/arrangeSDList.jsp";
		else 
			document.location.href="../schedularmanage/noPassList.jsp";
	}
	function arrange()
	{
		if(document.all.item("topic").value == "" )
		{
			alert("主题不能为空!!!");
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
		
		if(document.all.item("beginTime").value >= document.all.item("endTime").value)
		{
			alert("开始时间晚于结束时间");
    		return;
    	}
    	if(document.all.item("beginTime").value <= "<%=dateStr%>")
		{
			alert("开始时间早于当前时间");
    		return;
    	}
    	
		document.forms[0].action="../schedularmanage/schManager.do?method=modifySchedular&path=<%=path%>";
		document.forms[0].submit();	
	}
</script>

	<body class="info"  scrolling="no">

		<form name="SchedularForm" method="post" action="">

			<p align="center" class="detailtitle style2">
				<br>
				安排日程
			</p>
			<pg:beaninfo requestKey="schedular">
				<input type="hidden" name="schedularID" value="<pg:cell colName="schedularID"  defaultValue=""/>" >
				<input type="hidden" name="status" value="2" >
				
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
							<input type="text" name="beginTime" readonly="true" value="<pg:cell colName="beginTime"  defaultValue=""/>">
							<INPUT type="button" class="input" value="时间" onclick="selectTime('SchedularForm.beginTime',0)">
						</td>

						<td height="25" class="detailtitle" width="25%">
							<strong> 结束时间</strong>
						</td>
						<td height="25" width="25%">
							<input type="text" name="endTime" readonly="true" value="<pg:cell colName="endTime"  defaultValue=""/>">
							<INPUT type="button" class="input" value="时间" onclick="selectTime('SchedularForm.endTime',0)">
						</td>
					
					</tr>
					<tr>
						<td height="25" class="detailtitle" width="25%">
							<strong> 重要性</strong>
						</td>
						<td height="25" width="25%">
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
						<td height="25" class="detailtitle" width="25%">
							<strong> 是否空闲</strong>
						</td>
						<td height="25" width="25%">
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
						<td height="25" class="detailtitle" width="25%">
							<strong> 是否公事</strong>
						</td>
						<td height="25" width="25%">
							<select name="isPublicAffair" class="select">
								<option value="0" <pg:equal colName="isPublicAffair" value="0">selected</pg:equal>>
									公事
								</option>
								<option value="1" <pg:equal colName="isPublicAffair" value="1">selected</pg:equal>>
									私事
								</option>
							</select>
						</td>
						<td height="24" class="detailtitle">
							<strong> 部门公开</strong>
						</td>
						<td height="24" width="30%">
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
						<td height="25" class="detailtitle" width="25%">
							<strong> 日程描述</strong>
						</td>
						<td height="25" width="25%">
							<TEXTAREA name= "content" cols="25" rows="6"><pg:cell colName="content" defaultValue="" /></TEXTAREA>
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
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="thin">
			<tr>
				<td>

					<INPUT type="button" class="input" value="安排" onclick="arrange()">

				</td>
			</tr>
		</table>
		<%@include file="../sysMsg.jsp" %>
	</body>
</html>

