
<%@ include file="../include/global1.jsp"%> 
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%> 
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %> 
<%@ page import="java.util.List" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.JobManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Job" %>
<%
	//String orgid = request.getParameter("orgId");
	//String orgName = request.getParameter("orgName");
	Integer uid = (Integer)session.getAttribute("currUserId");
	UserManager userManager = SecurityDatabase.getUserManager();
	User user = userManager.getUserById(uid.toString());
	
	
	JobManager jobManager = SecurityDatabase.getJobManager();
	String jobid = "";
	String jobName = "";
	Job job = jobManager.getJobById("jobId","1");
	if(job != null){
		jobid = job.getJobId();
		jobName = job.getJobName();
	}
	
%>
<html>

<head>
<title>属性容器</title>
<script language="JavaScript"> 
var waitjobid = "<%=job.getJobId()%>"
function addorg(){	

   window.open('<%=rootpath%>/sysmanager/user/orgTree.jsp','newWin','scrollbars=no,status=no,titlebar=no,toolbar=no,z-lock=yes,width=616,height=500,top=150,left=250')
}

function changeorg(){	
	var all = document.all("orgName").value;
	var orgs = all.split("&");
	//因为后面还有一个&,所以长度减一
	for(var i = 0;i <orgs.length-1;i++){
		var orgidname = orgs[i];
		var all2 = orgidname.split(";");
		var op = new Option(all2[1]+"(<%=jobName%>)",all2[0]+";"+waitjobid);
		var isadd = 1;
		for(var j = 0;j<document.all("orgList").length;j++){
			if(op.value ==	document.all("orgList").options[j].value){	
				isadd = 0;
			}
		}
		if(isadd == 1)
		document.all("orgList").add(op);
	}
   
}

function deleteorg(){
var isselected = 0;
 for (var m=document.all("orgList").options.length-1;m>=0;m--){
	     if(document.all("orgList").options[m].selected){
         	var isselected = 1;
         	}
 }
   if(isselected == 1){
   	document.forms[0].action="../user/userOrg.do?method=deleteUserOrg";
   	document.forms[0].submit();
   }
   else{
   		alert("请选择要调出的机构");
   }
} 
 function saveorg(){

 for (var m=document.all("orgList").options.length-1;m>=0;m--){
	     document.all("orgList").options[m].selected =true         
 }
   	document.forms[0].action="../user/userOrg.do?method=storeUserOrg";
   	document.forms[0].submit();
  
}    

</script>
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../css/treeview.css">
</head>

<body class="contentbodymargin" >

<div id="contentborder">
	<center>
	<form name="OrgJobForm" action method="post">
		<table width="31%" border="0" cellpadding="0" cellspacing="1">
			<tr class="tabletop">
				<td colspan="3" align="center">&nbsp;</td>
			</tr>
			<tr class="tabletop">
				<td colspan="3"  align="center"><%=user.getUserRealname()%>(<%=user.getUserName()%>)已授予机构</td>
			</tr>
			<tr class="tabletop">
				<td colspan="3" width="100%" align="center">&nbsp;
				<input name="orgName" type="hidden" onpropertychange="changeorg()"  value="ss" onChange="changeorg()" >				
				</td>
			</tr>
			<tr>
				<td colspan="3">
				<p align="right">
				<select name="orgList" multiple style="width:100%" ondblclick="deleteorg()" size="18">
				  <pg:list requestKey="existOrg" needClear="false">
					<option value="<pg:cell colName="orgId"/>"><pg:cell colName="orgName"/>
				</option>
				  </pg:list>			
	 			</select>
				</p>
				</td>
			</tr>
			<tr class="tabletop">
				<td colspan="3"  align="center">&nbsp;</td>
			</tr>
			<tr>
				<td  align="center">　<input name="add" type="button" class="input" value="调入" onclick="addorg()"></td>
				<td  align="center">　<input name="add1" type="button" class="input" value="调出" onclick="deleteorg()"></td>
				<td  align="center">　<input name="add1" type="button" class="input" value="保存" onclick="saveorg()"></td>
				
			</tr>
		</table>
	</form>
	</center></div>
<%@ include file="../sysMsg.jsp"%>
</body>

</html>

