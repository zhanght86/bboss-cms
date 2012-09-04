<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Job,
			com.frameworkset.platform.security.AccessControl,
			com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
			com.frameworkset.platform.sysmgrcore.manager.JobManager" %>
<%@ include file="../../include/global1.jsp"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	String fromtype = request.getParameter("fromtype") == null?"":request.getParameter("fromtype");
	
	String jobId = request.getParameter("jobId");
	if(jobId != null){
		JobManager jobManager = SecurityDatabase.getJobManager();
		Job job = jobManager.getJobById(jobId);
		request.setAttribute("jobForm",job);
	}
	
	String status = request.getParameter("status");
	String orgId = request.getParameter("orgId");
	String isJobExist = "false";
	if ( request.getAttribute("isJobExist") != null){
		isJobExist = "true";
	}
	String isJobNumber = "false";
	if ( request.getAttribute("isJobNumber") != null){
		isJobNumber = "true";
	}
	
%><head>
<title>系统管理</title>
<style type="text/css">
<!--
.style1 {color: #CC0000}
-->
</style>
<%@ include file="/include/css.jsp"%>
<link href="../../css/tab.winclassic.css" rel="stylesheet" type="text/css">
<link href="../../css/contentpage.css" rel="stylesheet" type="text/css">
</head>
<html:html>
<SCRIPT LANGUAGE="JavaScript"> 

	var isJobExist = "<%=isJobExist%>";
	var isJobNumber = "<%=isJobNumber%>";
	var status = "<%=status%>";
	
	function trim(string){
	  var temp="";
	  string = ''+string;
	  splitstring = string.split(" ");
	  for(i=0;i<splitstring.length;i++){
	    temp += splitstring[i];
	  } 
	  return temp;
	}
	
	if ( isJobExist == "true" || isJobNumber == "true" ){
		alert("岗位名称或岗位编号已存在，请重新输入!");
	}
	
	if (status == "ok"){
		alert("操作成功！");
		window.dialogArguments.parent.document.location.href = window.dialogArguments.parent.document.location.href;
		window.close();
	}
	
	function savejob() {
				var form = document.forms[0];
				var jobname=form.jobName.value;
				var jobNumber=form.jobNumber.value;
				var jobRank=form.jobRank.value;
				var jobAmount=form.jobAmount.value;
				var jobFunction=form.jobFunction.value;
				var jobDesc=form.jobDesc.value;
				var jobCondition=form.jobCondition.value;
				if (trim(jobname).length == 0 ){
			    alert("请录入岗位名称！"); 
			    return false;
			    } 
			    if(jobname.search(/[\\\/\|:\*\?<>"']/g)!=-1){
					alert("岗位名称不能有\\/|:*?<>\"'!等特殊字符");
					form.jobname.focus();
					return;
				}
				if(jobNumber.search(/\W/g)!=-1 ){
					alert("岗位编号只能为数字、字母和下划线!");
					form.jobNumber.focus();
					return false;
				}
				if(jobRank.search(/[\\\/\|:\*\?<>"']/g)!=-1){
					alert("岗位等级不能有\\/|:*?<>\"'!等特殊字符");
					form.jobRank.focus();
					return;
				}
				if(jobAmount.search(/[\\\/\|:\*\?<>"']/g)!=-1){
					alert("岗位编制不能有\\/|:*?<>\"'!等特殊字符");
					form.jobAmount.focus();
					return;
				}
				
				if(jobname.length>100)
				{
					alert("岗位名称过长,限制在100字符以内!");
					return;
				}
				if(jobNumber.length>100)
				{
					alert("岗位编号过长,限制在100字符以内!");
					return;
				}
				if(jobRank.length>100)
				{
					alert("岗位等级过长,限制在100字符以内!");
					return;
				}
				if(jobAmount.length>100)
				{
					alert("岗位编制过长,限制在100字符以内!");
					return;
				}
				if(jobFunction.length>200)
				{
					alert("岗位职责过长,限制在200字符以内!");
					return;
				}
				if(jobDesc.length>200)
				{
					alert("岗位描述过长,限制在200字符以内!");
					return;
				}
				if(jobCondition.length>200)
				{
					alert("岗位任职条件过长,限制在200字符以内!");
					return;
				}
			    
				var fromtype = "<%=fromtype%>";
				var form = document.forms[0];
				form.target = "addjob";
				form.action = "saveJob.jsp";
				form.submit();
			}
			
		function back()
		{
			var fromtype = "<%=fromtype%>";
			if(fromtype != "")
			{
				window.close();
			}
			else
			{
				document.forms[0].action="<%=request.getContextPath()%>/sysmanager/jobmanager/A03/jobinfo.jsp"; 
				document.forms[0].submit();  
			}
		}
</SCRIPT>
<body class="contentbodymargin" scroll="no">
<div id="contentborder" align="center">
<form action="" name="jobform" method="post">  
<pg:beaninfo requestKey="jobForm">
<input type="hidden" name="fromtype" value="<%=fromtype%>">
<input type="hidden" name="orgId" value="<%=orgId%>">
<table width="100%" height="223" border="0" align="center" cellpadding="0" cellspacing="1" class="thin">
  <tr>
    <td height="35" align=center colspan=5><b>新增岗位</b></td>
  </tr>
  <tr>
    <td width="" height="35" class="detailtitle"> 岗位名称 <span class="style1">*</span></td>
    <td width="" class="detailcontent">
    	<input name="jobName" value='<pg:cell colName="jobName" defaultValue="" />' type="text" />
    </td>
  </tr>
  <tr>
    <td height="35" class="detailtitle"> 岗位编号 </td>
    <td class="detailcontent">
    	<input name="jobNumber" value='<pg:cell colName="jobNumber" defaultValue="" />' type="text" size="40" />
	</td>
  </tr>
   <tr>
    <td height="35" class="detailtitle"> 岗位等级 </td>
    <td class="detailcontent">
    	<input name="jobRank" value='<pg:cell colName="jobRank" defaultValue="" />' type="text" size="40" />
    </td>
  </tr>
   <tr>
    <td height="35" class="detailtitle"> 岗位编制 </td>
    <td class="detailcontent">
    	<input name="jobAmount" value='<pg:cell colName="jobAmount" defaultValue="" />' type="text" size="40" />
    </td>
  </tr>
  <tr>
    <td height="34" class="detailtitle"> 岗位职责 </td>
    <td class="detailcontent">
    	<textarea cols="50" rows="4" name="jobFunction"><pg:cell colName="jobFunction" defaultValue="" /></textarea>
    </td>
  </tr>
  <tr>
    <td height="35" class="detailtitle"> 岗位描述 </td>
    <td class="detailcontent">
    	<textarea cols="50" rows="4" name="jobDesc"><pg:cell colName="jobDesc" defaultValue="" /></textarea>
    </td>
  </tr>
  <tr>
    <td height="35" class="detailtitle"> 岗位任职条件 </td>
    <td class="detailcontent">
    	<textarea cols="50" rows="4" name="jobCondition"><pg:cell colName="jobCondition" defaultValue="" /></textarea>
    </td>
     </tr>
  <div align="center">
    <table width="35%" border="0" align="right" cellpadding="0" cellspacing="0">
      <tr>
        <td>
	        <div align="center">
	            <input name="Submit" type="button" class="input" value="保存" onClick="savejob()">
	        </div>
        </td>
        <td>
	        <div align="left">
	         <html:reset styleClass="input" value="重置" /> 
	        </div>
        </td>
        <td>
	        <div align="left"> 
	        	<input name="Submit" type="button" class="input" value="返回" onClick="back()"> 
	        </div>
        </td>
      </tr>
    </table>
  </div>
</table>
</form>
</pg:beaninfo>
<iframe name="addjob" width="0" height="0"></iframe>
<p>&nbsp;</p>
</div>
</body>
</html:html>

