<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%
	
	
%>
<html>
	<head>
		<title>属性容器</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
	<%@ include file="/include/css.jsp"%>
  		<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
  		<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
		
<script language="JavaScript" src="common.js" type="text/javascript"></script>
<script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>
<SCRIPT language="javascript">	



function dealRecord(dealType) {
    var isSelect = false;
    var outMsg;
	    
    for (var i=0;i<userList.elements.length;i++) {
		var e = userList.elements[i];
			
		if (e.name == 'checkBoxOne'){
			if (e.checked){
	       		isSelect=true;
	       		break;
		    }
		}
    }
    if (isSelect){
    	if (dealType==1){
    		outMsg = "你确定要删除吗？(删除后是不可以再恢复的)。";
        	if (confirm(outMsg)){
				userList.action="../user/userManager.do?method=deleteUser&disuser=1";
				userList.submit();
	 			return true;
			}
		} 
    }else{
    	alert("至少要选择一条记录！");
    	return false;
    }
	return false;
}
	

function actionOnmouseover(e){	
	e.style.backgroundColor = "#8080FF";
}

function actionOnmouseup(e){
	e.style.backgroundColor = "#BEC0C2";
}


function queryUser()
{	
	//if(document.userList.userName.value.length < 1 && document.userList.userRealname.value.length < 1){
	//	alert("用户名称和用户实名必须输入一个!!!");
	//	return;
	//}
	userList.action="../../jobmanager/A03/jobseeList.jsp"
	userList.submit();	
}


</SCRIPT>		
			<body>
				<form name="userList" method="post" >
					<table cellspacing="0" cellpadding="0" border="1" bordercolor="#EEEEEE" width=98% >
         				<tr valign='top'>
           					<td height='30'valign='middle' colspan="5">&nbsp;<strong>用户任职情况列表</strong></td>
         				</tr>
         				<tr >
           					
           					<td height='30'valign='middle' align="center">用户姓名：<input type="text" name="userName" ></td>
           					<td height='30'valign='middle' align="center">所在机构：<input type="text" name="orgname" ></td>
           					<td height='30'valign='middle' align="center" colspan="2">所属岗位：<input type="text" name="jobname" >
           				    </td>
           					<td height='30'valign='middle' align="center" colspan="2">
           						<input name="search" type="button" class="input" value="查询" onClick="queryUser()">
           					</td>
           				</tr>
						<pg:listdata dataInfo="JobSeeList" keyName="JobSeeList" />
						<!--分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="15" scope="request" data="JobSeeList" isList="false">
							<tr>
								<!--设置分页表头-->
								
								<th class="headercolor" height='30'>用户姓名</th>
								<th class="headercolor">所属岗位</th>
								<th class="headercolor">所在机构</th>
								<th class="headercolor">上岗时间</th>								
								<th class="headercolor">岗位状态</th>
							</tr>
							<pg:param name="orgname" />
							<pg:param name="userName" />
							<pg:param name="jobname" />

							<!--检测当前页面是否有记录-->
							<pg:notify>
								<tr height="18px" class="labeltable_middle_tr_01">
									<td colspan=100 align='center'>
										暂时没有用户
									</td>
								</tr>
							</pg:notify>

							<!--list标签循环输出每条记录-->
							<pg:list>								
								<tr onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'" onclick="" >
									
									<td class="tablecells" nowrap="nowrap" height='20'>
										<pg:cell colName="userRealname" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:cell colName="jobName" defaultValue="" />
									</td>									
									<td class="tablecells" nowrap="nowrap" >
										<pg:cell colName="orgName" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:cell colName="jobStartTime" defaultValue="未知" dateformat="yyyy-MM-dd HH:mm:ss"/>
									</td>
									
									<td class="tablecells" nowrap="nowrap">
										
										<pg:equal colName="fettle" value="1">在岗</pg:equal>
					  					<pg:equal colName="fettle" value="0">未知</pg:equal>																										    					    							    				    		
					  					<pg:equal colName="fettle" value=" ">未知</pg:equal>
									</td>	
								</tr>
							</pg:list>
							<tr height="18px" class="labeltable_middle_tr_01">
								<td colspan=4 align='center'>
									<pg:index />
								</td>						
							</tr>
							<input name="queryString" value="<pg:querystring/>" type="hidden">
						</pg:pager>

					</table>
				</form>
<%@ include file="../../sysMsg.jsp"%>
			</body>
			<center>
</html>

