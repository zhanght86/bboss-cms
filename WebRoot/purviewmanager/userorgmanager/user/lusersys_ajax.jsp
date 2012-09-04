<%
/*
 * <p>Title: 机构设置岗位处理页面</p>
 * <p>Description: 机构设置岗位处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-22
 * @author liangbing.tao
 * @version 1.0
 */
 %>
<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>

<%@ page import="java.util.List"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.JobManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Organization,
				com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager,
				com.frameworkset.platform.sysmgrcore.entity.*,
				com.frameworkset.platform.security.AccessControl,
				java.util.ArrayList"%>
				
				
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	String curUserId = accessControl.getUserID();
	//要想使用ajax请把userjoborg.jsp里面的frame也没改掉。还有struts config里面的forward
	String orgId = request.getParameter("orgId");
	if(orgId == null){
		orgId=(String)request.getAttribute("orgId");
	}
	Organization org = new Organization();
	org = OrgCacheManager.getInstance().getOrganization(orgId);
	System.out.println(">>>"+orgId);

	JobManager jobManager = SecurityDatabase.getJobManager();
	List joblist = null;
	List orgjoblist = null;
	
	List list1 = new ArrayList();
    if(joblist != null)
    {
    	for(int i=0;i<joblist.size();i++)
    	{
    		Job job = (Job)joblist.get(i);
    		String ownerId = String.valueOf(job.getOwner_id());
    		if(accessControl.checkPermission(job.getJobId(), "jobset", AccessControl.JOB_RESOURCE))
    		{
    			list1.add(job);
    		}
    		//if(ownerId.equals(curUserId)){
    			//list1.add(job);
    		//}
    	}
    }
    List list2 = new ArrayList();
    if(orgjoblist != null)
    {
    	for(int i=0;i<orgjoblist.size();i++)
    	{
    		Job job = (Job)orgjoblist.get(i);
    		//String ownerId = String.valueOf(job.getOwner_id());
    		if(accessControl.checkPermission(job.getJobId(), "jobset", AccessControl.JOB_RESOURCE))
    		{
    			list2.add(job);
    		}
    		//if(ownerId.equals(curUserId)){
    			//list2.add(job);
    		//}
    		
    	}
    }
    
	request.setAttribute("joblist",list1);
	request.setAttribute("orgjoblist",list2);

	//request.setAttribute("orgId",list);
%>
<html>
<head>    
  	<title>设置机构[<%=org.getOrgName()%>]岗位</title>
  	<script language="JavaScript" src="../../../include/querySelect.js" type="text/javascript"></script>
	<SCRIPT LANGUAGE="JavaScript">
	//初始化，指定处理的函数，发送请求的函数
	function send_request(url){
	
		document.all.divProcessing.style.display = "block";

		document.all("button1").disabled = true;
		document.all("button2").disabled = true;
		document.all("button3").disabled = true;
		document.all("button4").disabled = true;

		//document.all("button11").disabled = true;
		//document.all("button22").disabled = true;
		//document.all("button33").disabled = true;
		//document.all("button44").disabled = true;
		
		
		document.all("butn").disabled = true;

		document.OrgJobForm.action = url;		
		document.OrgJobForm.target = "hiddenFrame";
		document.OrgJobForm.submit();
	}
	function addone(name,value,n,orgId){
	   var jobSn=document.all("jobId").options.length+1;
	   for(var i=n;i>=0;i--){
			if(value==document.all("jobId").options[i].value){
			  return;
			}
		}
	   var op=new Option(name,value);
	   document.all("jobId").add(op);
	}
	
	function addjob(){	
	   //已经设置的岗位
	   var alreadyJobArr = document.all("jobId");
	   
	   var n=document.all("jobId").options.length-1;
	   var jobSn = n+2;
	   var jobIds = "";
	   var orgId = document.all("orgId").value;   	 	
	   var alreadyJobs = "";
	   for(var i=0;i<document.all("allist").options.length;i++){
		   var op=document.all("allist").options[i];
		   //if(op.selected){	
		   //   if(jobIds=="") jobIds = jobIds + op.value;
		   //    else jobIds = jobIds + "," + op.value;
			//   addone(op.text,op.value,n,orgId);
		   //}
		   if(op.selected){
		   		var flag2 = false;
		   		for(var j = 0; j < alreadyJobArr.length; j++){
		   			if(op.text==alreadyJobArr.options[j].text){
		   				flag2 = true;
		   			}
		   		}
		   		if(!flag2){
			   		if(jobIds==""){ 
			   			jobIds = op.value;
			   		}else {
			   			jobIds += "," + op.value;
			   		}
			   		addone(op.text,op.value,n,orgId);	
		   		}else{
		   			if(alreadyJobs == ""){
		   				alreadyJobs = op.text;
		   			}else{
		   				alreadyJobs += "\n" + op.text;
		   			}
		   		}   	
		   }	
	   }
	   if(jobIds != "")
	   {
	   	send_request('orgjobchange.jsp?jobId='+jobIds+'&orgId='+orgId+'&jobSn='+jobSn);
	   }else if(alreadyJobs != ""){
	   		alert("以下岗位已经设置：\n" + alreadyJobs);
  			return;
		}else{
			alert("请选择要添加的岗位名称!");
	   		return ;
	   }
	}
	function addall(){
		//已经设置的岗位
	   var alreadyJobArr = document.all("jobId");
		var orgId = document.all("orgId").value;
		var jobIds = "";
		var n=document.all("jobId").options.length-1;
		var jobSn = n+2;
		var p=document.all("allist").options.length-1;		
		var alreadyJobs = "";  
	    for(var i=0;i<document.all("allist").options.length;i++){
	        var op=document.all("allist").options[i];
	        //if(jobIds=="") jobIds = jobIds + op.value;
		    //else jobIds = jobIds + "," + op.value;
	        //addone(op.text,op.value,n,orgId);
	        var flag2 = false;
	   		for(var j = 0; j < alreadyJobArr.length; j++){
	   			if(op.text==alreadyJobArr.options[j].text){
	   				flag2 = true;
	   			}
	   		}
	   		if(!flag2){
		   		if(jobIds==""){ 
		   			jobIds = op.value;
		   		}else {
		   			jobIds += "," + op.value;
		   		}
		   		addone(op.text,op.value,n,orgId);	
	   		}else{
	   			if(alreadyJobs == ""){
	   				alreadyJobs = op.text;
	   			}else{
	   				alreadyJobs += "\n" + op.text;
	   			}
	   		} 
	   }
	   
	   if(jobIds != "")
	   {
	   		send_request('orgjobchange.jsp?jobId='+jobIds+'&orgId='+orgId+'&jobSn='+jobSn);		
	   }else if(alreadyJobs != ""){
	   		alert("以下岗位已经设置：\n" + alreadyJobs);
  			return;
		}else{
			alert("请选择要添加的岗位名称!");
	   		return ;
	   }
	   
	}	
	function deletejob()
	{
		var jobId = false;
		var jobIds = "";
		var orgId = document.all("orgId").value;
 		for (var m=document.all("jobId").options.length-1;m>=0;m--)
 		{
	    	if(document.all("jobId").options[m].selected)
	    	{
         		jobId = document.all("jobId").options[m].value;
         		
         		if(jobIds=="") 
         		{
         			jobIds = jobIds + jobId;
         		}
	    		else
	    		{
	    			 jobIds = jobIds + "," + jobId;
	    		}
				//document.all("jobId").options[m]=null;					
         	}
	    }
	    
	    if(jobIds !="")
	    {
	    	if(window.confirm("你确定要删除吗？(删除后将影响该岗位下的所有用户！)"))
	    	{
	    		for (var m=document.all("jobId").options.length-1;m>=0;m--)
 				{
	    			if(document.all("jobId").options[m].selected)
	    			{
	    				document.all("jobId").options[m]=null;					
	    			}
	    		}
	    		send_request('orgjobchange.jsp?jobId='+jobIds+'&orgId='+orgId+'&jobSn=-1');
	    	}
	    }
	    else
	    {
	    	alert("请选择要删除的岗位名称!");
	    	return ;
	    }
	}
	function deleteall()
	{
		var orgId = document.all("orgId").value;
		var jobIds = "";
		for (var m=document.all("jobId").options.length-1;m>=0;m--)
		{
    		jobId = document.all("jobId").options[m].value;
    		if(jobIds=="")
    		{
    		 jobIds = jobIds + jobId;
    		}
	    	else
	    	{
	    		 jobIds = jobIds + "," + jobId;
	    	}
			//document.all("jobId").options[m]=null;				
   		}
   		if(jobIds != "")
   		{
   			if(window.confirm("你确定要删除所有岗位吗？(删除后将影响该岗位下的所有用户!)"))
   			{
   				for (var m=document.all("jobId").options.length-1;m>=0;m--)
				{
					document.all("jobId").options[m]=null;				
				}
	    		send_request('orgjobchange.jsp?jobId='+jobIds+'&orgId='+orgId+'&jobSn=-1');
	    	}
    	}
    	else
    	{
    		return ;
    	}
	}
	
	function ok(){
		//window.dialogArguments.parent.document.location.href = window.dialogArguments.parent.document.location.href;
		window.returnValue = "ok";
		window.close(); 
	}
	
	</SCRIPT>
<%@ include file="/include/css.jsp"%>
   <link rel="stylesheet" type="text/css" href="../../css/treeview.css">	
<body >
<form name="OrgJobForm" action="" target="hiddenFrame" method="post" >

<table width="100%" border="0" cellpadding="1" cellspacing="1">
  <tr >
    <!--<td width="40%" align="center" class="detailtitle">可选岗位</td>-->
    <td width="40%" align="left" >可选岗位</td>
    <td width="20%" align="center" >&nbsp;</td>
    <td width="40%" align="right" >已设置岗位</td>
    <td width="20%" align="center" >&nbsp;</td>
  </tr>
   <tr>
  	<td width="40%" align="left">
  		<input name="selectRoles" type="text" value="" onkeydown="enterKeydowngo()" onpropertychange="onBlurryQueryChange()" ondblclick=""  />
  		<input name="que" type="button" value="查询" onclick="blurryQuary()" class="input" />
  		<input name="nextQue" style="display:none" type="button" value="查找下一个" onclick="nextQuery()" class="input" />
  	</td>
    <td width="20%" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
    <td width="40%" align="right">
    	
    </td>
  </tr>
  <tr >
     <td class="detailcontent" align="left">
     <select name="allist"  multiple style="width:100%" onDBLclick="addjob()" size="18">
				  <pg:list requestKey="joblist">
					<option value="<pg:cell colName="jobId"/>"><pg:cell colName="jobName"/></option>
				  </pg:list>			
	</select>
	</td>				  
		  	
    <td align="center" class="detailcontent"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center" class="detailcontent"><input name="button1" type="button" class="input" value="&gt;" onClick="addjob()"></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><input name="button2" type="button" class="input" value="&gt;&gt;" onClick="addall()"></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
      <tr>
        <td align="center" class="detailcontent"><input name="button3" type="button" class="input" value="&lt;&lt;" onClick="deleteall()"></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
      <tr>
        <td align="center" class="detailcontent"><input name="button4" type="button" class="input" value="&lt;" onClick="deletejob()"></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
    </table></td>
    <td class="detailcontent" align="right">
     <select name="jobId"  multiple style="width:100%" onDBLclick="deletejob()" size="18">
		  <pg:list requestKey="orgjoblist">
			<option value="<pg:cell colName="jobId"/>"><pg:cell colName="jobName"/></option>
		  </pg:list>			
 	 </select>								
	</td>
  <tr>
  	<td>&nbsp;</td>
  </tr>	
  <tr>
    <td align="center" colspan="4"><input name="butn" type="button" class="input" value="返回" onClick="ok();"></td>
  </tr>		  
  
  </table>

<input type="hidden"  name="orgId" value="<%=orgId%>"/>

</form>
<div id=divProcessing style="width:200px;height:30px;position:absolute;left:160px;top:350px;display:none">
	<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
	    <tr>
		    <td bgcolor=#3A6EA5>
			    <marquee align="middle" behavior="alternate" scrollamount="5">
				    <font color=#FFFFFF>...处理中...请等待...</font>
				</marquee>
			</td>
		</tr>
	</table>
</div>
</body>
<iframe name="hiddenFrame" width=0 height=0></iframe>
</html>
