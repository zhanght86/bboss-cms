<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%AccessControl accesscontroler = AccessControl.getInstance();
            accesscontroler.checkAccess(request, response);
            String userId = accesscontroler.getUserID();
%>
<html>
	<head>
		<title></title>

		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		<script language="javascript" src="../scripts/selectTime.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
		<script language="JavaScript" src="../scripts/common.js" type="text/javascript"></script>
		<script language="JavaScript">


	var jsAccessControl = new JSAccessControl("#ff0000","#ffffff","#eeeeee");
	var schedularID;
	
	
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
	function submitSD(){
		document.SchedularForm.submit();
		}
	function view(checkName){		
		if(!validatecheck(checkName))
		{
			alert("请选择要查看的日程");
			return;
		}
		if(!validateOnlyCheck(checkName))
		{
			alert("一次只能选择一个日程进行查看");
			return;
		}
		document.forms[0].action="../schedularmanage/schManager.do?method=getSchedular&path=departmentSchedularList";
		document.forms[0].submit();
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
	function actionOnmouseover(e){	
		e.style.backgroundColor = "#8080FF";
	}

	function actionOnmouseup(e){
		e.style.backgroundColor = "#BEC0C2";
	}
</script>
	<body class="contentbodymargin" scrolling="no">
		<div id="contentborder" align="center">
			<FORM name="SchedularForm" action="departmentSchedularList.jsp" method="post">
				<table width="100%" height="106" border="0" cellpadding="0" cellspacing="2" class="thin">
					<tr>
						<td height="23" class="detailtitle">
							<strong> 执行人</strong>
						</td>
						<td height="23" width="29%">
							<input name="userName" type="text">
						</td>
						<td height="23" class="detailtitle">
							<strong> 主题</strong>
						</td>
						<td height="23" width="29%">
							<input name="topic" type="text">
						</td>
					</tr>
					<tr>
						<td height="23" class="detailtitle">
							<strong> 开始时间从</strong>
						</td>
						<td height="23" width="30%">
							<input name="beginTime" readonly="true" type="text">
							<INPUT type="button" class="input" value="时间" onclick="selectTime('SchedularForm.beginTime',0)">
						</td>
						<td height="23" class="detailtitle">
							<strong> 至</strong>
						</td>
						<td height="23" width="30%">
							<input name="endTime" readonly="true" type="text">
							<INPUT type="button" class="input" value="时间" onclick="selectTime('SchedularForm.endTime',0)">
						</td>
					</tr>
					<tr>
						<td height="23" class="detailtitle">
							
						</td>
						<td height="23" width="29%">
							
						</td>
						<td height="23" class="detailtitle">
							
						</td>
						<td height="23" width="29%">
							<input type="button" class="input" value="查询" onClick="submitSD()">
							<input type="reset" class="input" value="重置">
						</td>
					</tr>
				

				</table>

				<TABLE width="100%" cellpadding="1" cellspacing="1" bordercolor="#EEEEEE" class="thin">
					<!--分页显示开始,分页标签初始化-->
					<TR>
						<TD height="30" class="detailtitle" align="center" colspan="8">
							<B>部门他人日程列表</B>
						</TD>
					</TR>
					<pg:listdata dataInfo="DepartmentSchedularList" keyName="DepartmentSchedularList" />
					<pg:pager maxPageItems="11" scope="request" data="DepartmentSchedularList" isList="false">
						<pg:param name="userName" />
						<pg:param name="topic" />
						<pg:param name="beginTime" />
						<pg:param name="endTime" />
						<td class="headercolor" width="10">
							<input type="checkBox" name="checkBox" onClick="checkAll('checkBox','ID')">
						</td>
						<td class="headercolor">
							主题
						</td>
						<td class="headercolor">
							地点
						</td>
						<td class="headercolor">
							日程开始时间
						</td>
						<td class="headercolor">
							日程结束时间
						</td>
						<td class="headercolor">
							日程执行人
						</td>
						<pg:list>
							<tr class="labeltable_middle_tr_01" onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'">
								<td class="tablecells" width="5%" nowrap="nowrap">
									<input type="checkBox" name="ID" onClick="checkOne('checkBox','ID')" value="<pg:cell colName="schedularID" defaultValue=""/>">
								</td>
								<td class="tablecells" width="20%" nowrap="nowrap">
									<pg:equal colName="topic" value="">无主题</pg:equal>
									<pg:cell colName="topic" defaultValue="无主题" />
								</td>
								<td class="tablecells" width="20%" nowrap="nowrap">
									<pg:equal colName="place" value="">无地点</pg:equal>
									<pg:cell colName="place" defaultValue="无地点" />
								</td>
								<td class="tablecells" width="20%" nowrap="nowrap">
									<pg:equal colName="beginTime" value="">未安排</pg:equal>
									<pg:cell colName="beginTime" dateformat="yyyy-MM-dd HH:mm:ss" defaultValue="未安排" />
								</td>
								<td class="tablecells" width="20%" nowrap="nowrap">
									<pg:equal colName="endTime" value="">未安排</pg:equal>
									<pg:cell colName="endTime" dateformat="yyyy-MM-dd HH:mm:ss" defaultValue="未安排" />
								</td>
								<td class="tablecells" width="20%" nowrap="nowrap">
									<pg:cell colName="userName" defaultValue="" />(<pg:cell colName="userRealName" defaultValue="无" />)
								</td>
							</tr>
						</pg:list>
						<tr height="18px" class="labeltable_middle_tr_01">
							<td colspan=6 align='center' nowrap="nowrap">
								<pg:index />
								<INPUT type="button" class="input" name="viewSD" value="查看日程" onclick="view('ID')">
							</td>
						</tr>
						<input name="queryString" value="<pg:querystring/>" type="hidden">
					</pg:pager>
				</TABLE>
		</FORM>
		</div>
		<%@include file="../sysMsg.jsp" %>
	</body>
</html>

