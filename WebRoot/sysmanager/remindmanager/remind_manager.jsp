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
<%@page import="java.util.*,com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>
<%AccessControl accesscontroler = AccessControl.getInstance();
            accesscontroler.checkAccess(request, response);
            String username = accesscontroler.getUserAccount();

            %>
<html>
	<head>
		<script language="JavaScript" src="common.js" type="text/javascript"></script>
		<script language="javascript" src="../scripts/selectTime.js"></script>
		<SCRIPT language="JavaScript" SRC="validateForm.js"></SCRIPT>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>查看提醒</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/tab.winclassic.css">
		<style type="text/css">
<!--
.STYLE1 {color: #FF0000}
.style2 {font-size: medium}
-->
        </style>
	</head>

	<script language="JavaScript">
function validatecheck(checkName)
	{
			 var o = document.all.item(checkName);
		for (var i=0; o && o.length && i<o.length; i++){
	   	  	  if(o[i].checked)
	   	  	  	return true;
	   	   }
	   	   if(o && !o.length )
			{
				if(o.checked)
					return true;
			}
			return false;
	}
	function validateOnlyCheck(checkName)
	{
		 var o = document.all.item(checkName);
		 for (var i=0,j=0; o && o.length && i<o.length; i++){
	   	  	if(o[i].checked)
	   	  	{
	   	  		j++;
	   	   	}
	   	   	if(j == 2)
	   	   	{
	   	   		return false;
	   	   	}
	   	   }
		 return true;
	}
	

function query(){
	document.RemindForm.submit();
		}	

function checkAll(totalCheck,checkName){	//复选框全部选中
	   var selectAll = document.all.item(totalCheck);
	   
	   var o = document.all.item(checkName);
	   
	   if(selectAll.checked==true){
		   for (var i=0; o && o.length && i<o.length; i++){
	      	  if(!o[i].disabled){
	      	  	o[i].checked=true;
	      	  }
		   }
		   if(o && !o.length && !o.checked)
			{
				o.checked=true;
			}
		   
	   }else{
		   for (var i=0; o && o.length && i<o.length; i++){
	   	  	  o[i].checked=false;
	   	   }
	   	   if(o && !o.length && !o.checked)
			{
				o.checked=false;
			}
	   	   
	   }
	}
	//单个选中复选框
function checkOne(totalCheck,checkName){
	   var selectAll = document.all.item(totalCheck);
	   var o = document.all.item(checkName);
		var cbs = true;
		
		for (var i=0;o && o.length && i < o.length;i++){
			if(!o[i].disabled){
				if (o[i].checked==false){
					cbs=false;
				}
			}
		}
		if(o && !o.length && !o.checked)
		{
			cbs = false;
		}
		if(cbs){
			selectAll.checked=true;
		}
		else{
			selectAll.checked=false;
		}
	}	
	
	function dealRecord(checkName,dealType) {
	    if(!validatecheck(checkName))
		{
			alert("请选择要删除的记录");
		}
		else
		{	    
	    	if (dealType==1){
	    		outMsg = "你确定要删除吗？(删除后是不可以再恢复的)。";
	        	if (confirm(outMsg)){
		        	document.RemindForm.action="../remindinfomanage/remindinfomanage.do?method=deleteRemindInfo";
					document.RemindForm.submit();
			 		return true;
				}
			} 
		}
	}	
	function del()
	{
		outMsg = "你确定要删除吗？(删除后是不可以再恢复的)。";
    	if (confirm(outMsg)){
        	document.RemindForm.action="../remindinfomanage/remindinfomanage.do?method=deleteAllRemindInfoByUser";
			document.RemindForm.submit();
	 		return true;
		}
	}
</script>

	<body class="contentbodymargin" scroll="no">
		<div id="contentborder">
			<form name="RemindForm" action="remind_manager.jsp" method="post">
				<table width="100%" height="25" border="1" cellpadding="1" cellspacing="2">
					<tr>
						<td height="23" class="detailtitle">
							<strong> 类型</strong>
						</td>
						<td height="23" width="30%">
							<input name="source" type="text" >
						</td>
						<td height="23" class="detailtitle">
							<strong> 内容</strong>
						</td>
						<td height="23" width="30%">
							<input name="content"  type="text">
						</td>
					</tr>
					<tr>
						<td height="23" class="detailtitle">
							<strong> 时间从</strong>
						</td>
						<td height="23" width="30%">
							<input name="beginTime" readonly="true" type="text" >
							<INPUT type="button" class="input" value="时间" onclick="selectTime('RemindForm.beginTime',0)">
						</td>
						<td height="23" class="detailtitle">
							<strong> 至</strong>
						</td>
						<td height="23" width="30%">
							<input name="endTime" readonly="true" type="text">
							<INPUT type="button" class="input" value="时间" onclick="selectTime('RemindForm.endTime',0)">
						</td>
					</tr>
					<%
						if(accesscontroler.isAdmin())
						{
					%>
					<tr>
						<td height="23" class="detailtitle">
							<strong> 用户名</strong>
						</td>
						<td height="23" width="30%">
							<input name="username"  type="text" >
						</td>
						<td height="23" >
							&nbsp
						</td>
						<td>
							<input type="submit" class="input" value="查询" onClick="query()">
							<input type="reset" class="input" value="重置">
						</td>
						
					</tr>
					<%} else 
						{
						%>
				<input type="hidden" name="username" value="<%=username%>" />
					<tr>
						<td>&nbsp
						</td>
						<td>&nbsp
						</td>
						<td>&nbsp
						</td>
						<td>
							<input type="submit" class="input" value="查询" onClick="query()">
							<input type="reset" class="input" value="重置">
						</td>
					</tr>
					<%}%>
					
				</table>
				<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolorlight="#3B4D61" bordercolordark="#ffffff" style="padding:2">
					<!--分页显示开始,分页标签初始化-->
					<TR>
					<%
						if(accesscontroler.isAdmin())
						{
					%>
						<TD height="30" class="detailtitle" align="center" colspan="5">
					<%} else 
						{
					%>
					<TD height="30" class="detailtitle" align="center" colspan="4">
					<%} %>
							<B><div align="center" class="detailtitle style2">
									<strong>提醒信息</strong>
							</B>
						</TD>
					</TR>
					<pg:listdata dataInfo="RemindInfoList" keyName="RemindInfoList" />

					<pg:pager maxPageItems="12" scope="request" data="RemindInfoList" isList="false">
						
						<td class="headercolor" width="10">
							<input type="checkBox" name="checkBox" onClick="checkAll('checkBox','ID')">
						</td>
						<td class="headercolor">
							提醒时间
						</td>
						<%
						if(accesscontroler.isAdmin())
						{
						%>
						<Td class="headercolor" nowrap="nowrap">
							用户名(真实姓名)
						</Td>
						<%} %>
						<Td class="headercolor">
							类型
						</Td>
						<Td class="headercolor">
							内容
						</Td>
						<pg:param name="content"/>
						<pg:param name="username"/>
						<pg:param name="source"/>
						<pg:param name="beginTime"/>
						<pg:param name="endTime"/>						
						<pg:list>
							<tr class="labeltable_middle_tr_01" onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'">
								<td class="tablecells" width="5%" nowrap="nowrap">
									<input type="checkBox" name="ID" onClick="checkOne('checkBox','ID')" value="<pg:cell colName="id" defaultValue=""/>">
								</td>
								<td nowrap="nowrap" width="5%" class="tablecells">
									<pg:equal colName="time" value="">&nbsp</pg:equal>
									<pg:cell colName="time" dateformat="yyyy-MM-dd HH:mm" defaultValue="" />
								</td>
								<%
								if(accesscontroler.isAdmin())
								{
								%>
								<td  class="tablecells" width="5%" nowrap="nowrap">
									<pg:equal colName="userName" value="">&nbsp</pg:equal>
									<pg:cell colName="userName" defaultValue="" />(<pg:cell colName="UserRealName" defaultValue="" />)
								</td>
								<%} %>
								<td  class="tablecells" width="5%" nowrap="nowrap">
									<pg:equal colName="source" value="">&nbsp</pg:equal>
									<pg:cell colName="source" defaultValue="" />
								</td>
								<td  class="tablecells"  >
									<pg:equal colName="content" value="">&nbsp</pg:equal>
									<pg:cell colName="content" defaultValue="" />
								</td>

							</tr>
						</pg:list>
						<tr height="18px" class="labeltable_middle_tr_01">
						<%
						if(accesscontroler.isAdmin())
						{
						%>	
						<td colspan=5 align='center' nowrap="nowrap">
						
						<%} else 
						{
						%>
						<td colspan=4 align='center' nowrap="nowrap">
						<%}%>
								<pg:index />
								<input type="submit" class="input" value="删除" onclick="dealRecord('ID',1)">
						<%
						if(accesscontroler.isAdmin())
						{
						%>			
								<input type="button" class="input" value="删除本人全部消息" onclick="del()">
						<%} else 
						{
						%>		
								<input type="button" class="input" value="删除全部" onclick="del()">
						<%}%>
							</td>
						</tr>
						<input name="queryString" value="<pg:querystring/>" type="hidden">
					</pg:pager>
				</TABLE>
			</form>
		</div>
	</body>
</html>

