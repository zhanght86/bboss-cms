<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="java.util.*,com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>

<%Date date = new Date();%>
<html>
	<head>
		<title></title>

		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
		<script language="JavaScript" src="../scripts/common.js" type="text/javascript"></script>

		<script language="JavaScript">

	
	var jsAccessControl = new JSAccessControl("#ff0000","#ffffff","#eeeeee");
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
	function viewAdvice(checkName){
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
		
		document.forms[0].action="../schedularmanage/schManager.do?method=viewAdvice";
		document.forms[0].submit();
	
	}
	function reArrSD(checkName){
		if(!validatecheck(checkName))
		{
			alert("请选择要安排的日程");
			return;
		}
		if(!validateOnlyCheck(checkName))
		{
			alert("一次只能选择一个日程进行安排");
			return;
		}
		
		document.forms[0].action="../schedularmanage/schManager.do?method=getArrangeSchedular&path=noPassList";
		document.forms[0].submit();
	}
	function goBack(){		
	document.location.href="../schedularmanage/daySchedularList.jsp";
	}
	function view(checkName){		
		if(!validatecheck(checkName))
		{
			alert("请选择要查看的日程");
			return;
		}
		if(!validateOnlyCheck(checkName))
		{
			alert("一次只能选择一个日程进行修改");
			return;
		}
		
		document.forms[0].action="../schedularmanage/schManager.do?method=getSchedular&path=noPassList";
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
	
	function dealRecord(checkName,dealType) {
	    if(!validatecheck(checkName))
		{
			alert("请选择要删除的的日程");
			return false;
		}
	    
	    	if (dealType==1){
	    		outMsg = "你确定要删除吗？(删除后是不可以再恢复的)。";
	        	if (confirm(outMsg)){
		        	Org.action="../schedularmanage/schManager.do?method=deleteSchedular&path=noPassList";
					Org.submit();
			 		return true;
				}
			} 
	    return false;
	}
</script>
	<body class="contentbodymargin" scrolling="no">
		<div id="contentborder" align="center">

			<FORM name="Org" action="" method="post">
				<TABLE width="100%" cellpadding="1" cellspacing="1" bordercolor="#EEEEEE" class="thin">
					<!--分页显示开始,分页标签初始化-->
					<TR>
						<TD height="30" class="detailtitle" align="center" colspan="8">
							<B>审批未通过日程列表</B>
						</TD>
					</TR>
					<pg:listdata dataInfo="NoPassList" keyName="NoPassList" />

					<pg:pager maxPageItems="15" scope="request" data="NoPassList" isList="false">
						<td class="headercolor" width="5%">
							<input type="checkBox" name="checkBox" onClick="checkAll('checkBox','ID')">
						</td>
						<td class="headercolor" width="10%" >
							主题
						</td>
						<td class="headercolor" width="10%" >
							审批人 
						</Td>
						<Td class="headercolor" width="15%" >
							日程开始时间
						</Td>
						<Td class="headercolor" width="15%" >
							日程结束时间
						</Td>
						<Td class="headercolor" width="10%" >
							地点
						</Td>
						
					<pg:list>
							<tr class="labeltable_middle_tr_01" onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'">
								<td class="tablecells" width="5%" nowrap="nowrap">
									<input type="checkBox" name="ID" onClick="checkOne('checkBox','ID')" value="<pg:cell colName="schedularID" defaultValue=""/>">
								</td>
								<td class="tablecells" width="10%" nowrap="nowrap">
									<pg:equal colName="topic" value="">无主题</pg:equal>
									<pg:cell colName="topic" defaultValue="无主题" />
								</td>
								<td class="tablecells" width="10%" nowrap="nowrap">
									<pg:equal colName="userName" value=""> </pg:equal>
									<pg:cell colName="userName" defaultValue=" " />(<pg:cell colName="userRealName" defaultValue="无" />)
								</td>
								<td class="tablecells" width="15%" nowrap="nowrap">
									<pg:equal colName="beginTime" value="">未安排</pg:equal>
									<pg:cell colName="beginTime" dateformat="yyyy-MM-dd HH:mm:ss" defaultValue="未安排" />
								</td>
								<td class="tablecells" width="15%" nowrap="nowrap">
									<pg:equal colName="endTime" value="">未安排</pg:equal>
									<pg:cell colName="endTime" dateformat="yyyy-MM-dd HH:mm:ss" defaultValue="未安排" />
								</td>
								<td class="tablecells" width="10%" nowrap="nowrap">
									<pg:equal colName="place" value="">无地点</pg:equal>
									<pg:cell colName="place" defaultValue="无地点" />
								</td>
								
							</tr>
						</pg:list>
						<tr height="18px" class="labeltable_middle_tr_01">
							<td colspan=6 align='center' nowrap="nowrap">
								<pg:index />
								<input type="submit" class="input" value="删除记录" onclick="javascript:dealRecord('ID',1); return false;">
								<INPUT type="button" class="input" name="viewSD" value="查看日程" onclick="view('ID')">
								<INPUT type="button" class="input" name="viewRatifyAdvice" value="查看审批意见" onclick="viewAdvice('ID')">
								<INPUT type="button" class="input" name="reArr" value="重新安排" onclick="reArrSD('ID')">
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

