
<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.JobManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Job" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%
	JobManager jobManager = SecurityDatabase.getJobManager();
	Job noJob = jobManager.getJobById("1");
	String jobName = "";
	String jobid = "";
	if(noJob != null){
		jobid = noJob.getJobId();
		jobName = noJob.getJobName();
	}
	Integer userId = (Integer) session.getAttribute("currUserId");
	UserManager userManager = SecurityDatabase.getUserManager();
	User user = userManager.getUserById(userId.toString());
%>
<html>
<head>    
  <title>属性容器</title>
<SCRIPT LANGUAGE="JavaScript"> 
function addjob(){	
   var n=document.all("jobId").options.length-1;	 	
   for(var i=0;i<document.all("allist").options.length;i++){
   var op=document.all("allist").options[i];
   if(op.selected)
   addone(op.text,op.value,n);
  }
}
function addone(name,value,n){

   for(var i=n;i>=0;i--){
		if(value==document.all("jobId").options[i].value){
		  return;
		}
	}
   var op=new Option(name,value);
   document.all("jobId").add(op);
   
}
function deleteall(){
	for (var m=document.all("jobId").options.length-1;m>=0;m--)
    document.all("jobId").options[m]=null;
    //自动添加待岗
    var op=new Option("<%=jobName%>","<%=jobid%>");
   	document.all("jobId").add(op);
}
      
function addall(){
	var n=document.all("jobId").options.length-1;
	var p=document.all("allist").options.length-1;		  
     for(var i=0;i<document.all("allist").options.length;i++){
     var op=document.all("allist").options[i];
     addone(op.text,op.value,n);  
   }
}
function deletejob(){
	var l=document.all("jobId").options.length;	
	//没有岗位了就添加待岗
	if(l ==1){
		var op=new Option("<%=jobName%>","<%=jobid%>");
   		document.all("jobId").add(op);
	}
	
	//控制至少要一个岗位
	//if(l == 1){
	  // 	alert("至少要有一个岗位");
	   //	return;
     //}
 for (var m=document.all("jobId").options.length-1;m>=0;m--)
	     
	      if(document.all("jobId").options[m].selected)
         document.all("jobId").options[m]=null;
}
function okadd(){
var len=document.all("jobId").options.length;
	for (var i=0;i<len;i++){	      
         document.all("jobId").options[i].selected="true";
         }
         document.forms[0].action="../user/userJob.do?method=storeUserJob";
         document.forms[0].submit();
}
</SCRIPT>
  <%@ include file="/include/css.jsp"%>
 
<body class="contentbodymargin">
<div id="contentborder">
<center>
<form name="OrgJobForm" action="" method="post" >
<table width="80%" border="0" cellpadding="0" cellspacing="1">
<tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">机构已设岗位</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center"><%=user.getUserRealname()%>(<%=user.getUserName()%>)已授予岗位</td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr >
     <td align="right" >
     <select name="allist"  multiple style="width:80%" onDBLclick="addjob()" size="18">
				  <pg:list requestKey="allJob">
					<option value="<pg:cell colName="jobId"/>"><pg:cell colName="jobName"/></option>
				  </pg:list>			
	</select>
	</td>				  
		  	
    <td align="center"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center"><input name="button1" type="button" class="input" value="&gt;" onClick="addjob()"></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><input name="button2" type="button" class="input" value="&gt;&gt;" onClick="addall()"></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><input name="button3" type="button" class="input" value="&lt;&lt;" onClick="deleteall()"></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><input name="button4" type="button" class="input" value="&lt;" onClick="deletejob()"></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
    </table></td>
    <td >
     <select name="jobId"  multiple style="width:80%" onDBLclick="deletejob()" size="18">
				  <pg:list requestKey="existJob">
					<option value="<pg:cell colName="jobId"/>"><pg:cell colName="jobName"/></option>
				  </pg:list>			
	 </select>				
					
	</td>				 
				  
  </tr>
  <tr class="tabletop">
    
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr><td colspan="3" align="center">
  <input name="add" type="button" class="input" value="确定" onClick="okadd()" >
  
  </td> </tr>
</table>
<pg:beaninfo requestKey="userJobForm">
<input type="hidden"  name="orgId" value="<pg:cell colName="orgId"  defaultValue=""/>"/>
<input type="hidden"  name="uid" value="<pg:cell colName="userId"  defaultValue=""/>"/>
</pg:beaninfo>
</form>
</center>
</div>
<%@ include file="../sysMsg.jsp"%>
</body>
</html>

