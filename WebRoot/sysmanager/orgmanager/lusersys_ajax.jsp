<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="java.util.List, java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.JobManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.form.OrgJobForm
				,com.frameworkset.platform.sysmgrcore.entity.Organization
				,com.frameworkset.platform.sysmgrcore.entity.Job
				,com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%

	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(pageContext);
	
	//要想使用ajax请把userjoborg.jsp里面的frame也没改掉。还有struts config里面的forward
	String orgId = request.getParameter("orgId");
	if(orgId == null){
		orgId=(String)request.getAttribute("orgId");
	}
	
	OrgJobForm object = (OrgJobForm)session.getAttribute("orgjobForm");

	Organization organization = new Organization();
	organization = OrgCacheManager.getInstance().getOrganization(object.getOrgId());
    	String jobId=(String)request.getAttribute("curjobid");
    	if(jobId ==null){
    		jobId = "";
    	}
    JobManager jobManager = SecurityDatabase.getJobManager();
    
	List joblist = jobManager.getJobList();
	List orgjoblist = jobManager.getJobList(organization);
    
    //左边只出现有权限进行机构岗位授予的岗位
    List list1 = new ArrayList();
    String popedomJobIds = "";
    if(joblist != null)
    {
    	for(int i=0;i<joblist.size();i++)
    	{
    		Job job = (Job)joblist.get(i);
    		if(accessControl.checkPermission(job.getJobId(), "orgset", AccessControl.JOB_RESOURCE))
    		{
    			list1.add(job);
    			if("".equals(popedomJobIds)){
    				popedomJobIds = job.getJobId();
    			}else{
    				popedomJobIds += "," + job.getJobId();
    			}
    		}
    	}
    }
    //有权限的岗位ID
    String[] popedomJobId = popedomJobIds.split(",");
    
	request.setAttribute("joblist1",list1);
	request.setAttribute("orgjoblist1",orgjoblist);
	
    
%>
<html>
<head>    
  	<title>属性容器</title>
	<SCRIPT LANGUAGE="JavaScript">
	
	var http_request = false;
	//初始化，指定处理的函数，发送请求的函数
	function send_request(url){
	    document.all.divProcessing.style.display="block";
		document.OrgJobForm.action = url;
		document.OrgJobForm.target = "hiddenFrame";
		document.OrgJobForm.submit();
	}
	
	/*
	function processRequest(){
		if(http_request.readyState == 4){
			if(http_request.status == 200){
				//alert(http_request.responseText);
			}
			else{
				alert("对不起，服务器错误");
			}
		}
	} 
	*/
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
	   var n=document.all("jobId").options.length-1;
	   var orgId = document.all("orgId").value;	
	   var jobSn = n+2;
	   var jobIds = "";   	 	
	   for(var i=0;i<document.all("allist").options.length;i++){
		   var op=document.all("allist").options[i];
		   if(op.selected){	
			   addone(op.text,op.value,n,orgId);
			   if(jobIds=="") jobIds = jobIds + op.value;
			   else jobIds = jobIds + "," + op.value;
		   }
		   
	  }
	  send_request('orgjobchange.jsp?jobId='+jobIds+'&orgId='+orgId+'&jobSn='+jobSn);
	}	
	
	function deleteall(){
		if(window.confirm("你确定要删除所有岗位吗？(删除后将影响该岗位下的所有角色和用户!)")){
			var orgId = document.all("orgId").value;
			var jobIds = "";
			var returnJobInfo = "";
			var flag = false;   	 
			for (var m=document.all("jobId").options.length-1;m>=0;m--){
	    		op = document.all("jobId").options[m];
	    		<%for(int i = 0; i < popedomJobId.length; i++){
	        		String popedomJobIdStr = popedomJobId[i];
	        	%>
	        	if(op.value=="<%=popedomJobIdStr%>"){
	        		flag = true;
					document.all("jobId").options[m]=null;
					if(jobIds==""){
						jobIds = op.value;
					}else{
						jobIds += "," + op.value;
					}
				}
				<%}%>
				if(!flag){
		        	if(returnJobInfo==""){
						returnJobInfo = op.text;
					}else{
						returnJobInfo += "\n" + op.text;
					}
				}
				flag = false;
	   		}
	   		if(returnJobInfo!=""){
	        	alert("对以下岗位没有权限。\n"+returnJobInfo);
	        }
	    	for (var n=parent.frames[1].document.all("userList").options.length-1;n>=0;n--){
	    		parent.frames[1].document.all("userList").options[n]=null;
	    	}
	    	if(jobIds!=""){
	    		send_request('orgjobchange.jsp?jobId='+jobIds+'&orgId='+orgId+'&jobSn=-1');
	    	}
		}
		
	}
	function addsuborg()
	{
		var orgId = document.all("orgId").value;
		var joblen = document.all("jobId").options.length;
		var jobIds = "";
		var returnJobInfo = "";
		var flag = false;
		var isSelectedJob = false;
		for(var i=0;i<joblen;i++)
		{       
        	if(document.all("jobId").options[i].selected){ 
        		var op = document.all("jobId").options[i];
        		isSelectedJob = true;
        	<%for(int i = 0; i < popedomJobId.length; i++){
        		String popedomJobIdStr = popedomJobId[i];
        	%>
        		if(op.value=="<%=popedomJobIdStr%>"){
        			jobIds += op.value + ",";
        			flag = true;
        		}
        	<%}%>
        		if(!flag){
		        	if(returnJobInfo==""){
						returnJobInfo = op.text;
					}else{
						returnJobInfo += "\n" + op.text;
					}
				}
				flag = false;
	   		}
        }
        if(jobIds.indexOf(",") != -1)
        	jobIds = jobIds.substring(0,jobIds.length - 1);
        if(returnJobInfo!=""){
        	alert("对以下岗位没有权限。\n"+returnJobInfo);
        }
        if(!isSelectedJob)
        {
        	alert("请从“已设置岗位”选择要递归授予子机构的岗位！");
        	return false;
        }
        if(jobIds!=""){
			send_request("orgjobchange.jsp?orgId=" + orgId + "&jobIds=" + jobIds);
		}
	}
	
	     
	function addall(){
		var orgId = document.all("orgId").value;
		var jobSn=document.all("jobId").options.length+1;
		var n=document.all("jobId").options.length-1;
		var p=document.all("allist").options.length-1;	
		var jobIds = "";	  
	    for(var i=0;i<document.all("allist").options.length;i++){
	         var op=document.all("allist").options[i];
	         var jobId = op.value;
	         addone(op.text,op.value,n,orgId); 
	         if(jobIds=="") jobIds = jobIds + jobId;
			 else jobIds = jobIds + "," + jobId;
	   }
	   send_request('orgjobchange.jsp?jobId='+jobIds+'&orgId='+orgId+'&jobSn='+jobSn); 
	}
	function deletejob(){
		if(window.confirm("你确定要删除吗？(删除后将影响该岗位下的所有角色和用户！)")){
			var jobId = false;
			var jobIds = "";
			var orgId = document.all("orgId").value;
			var returnJobInfo = "";
			var flag = false;
	 		for (var m=document.all("jobId").options.length-1;m>=0;m--){
		    	if(document.all("jobId").options[m].selected){
	         		op = document.all("jobId").options[m];
	         		<%for(int i = 0; i < popedomJobId.length; i++){
		        		String popedomJobIdStr = popedomJobId[i];
		        	%>
		        	if(op.value=="<%=popedomJobIdStr%>"){
		        		flag = true;
						document.all("jobId").options[m]=null;
						if(jobIds==""){
							jobIds = op.value;
						}else{
							jobIds += "," + op.value;
						}
					}
					<%}%>
					if(!flag){
						if(returnJobInfo==""){
							returnJobInfo = op.text;
						}else{
							returnJobInfo += "\n" + op.text;
						}
					}
					flag = false;
	         	}
	        }
	        if(returnJobInfo!=""){
	        	alert("对以下岗位没有权限。\n"+returnJobInfo);
	        }
         	for (var n=parent.frames[1].document.all("userList").options.length-1;n>=0;n--)
    			parent.frames[1].document.all("userList").options[n]=null;
    		if(jobIds!=""){
            	send_request('orgjobchange.jsp?jobId='+jobIds+'&orgId='+orgId+'&jobSn=-1');	    	
            }
		}
		
	}
	
	function deletesuborg(){
		if(document.all("jobId").options.length<1){
			alert('"已设置岗位"中没有可选的岗位！');
			return false;
		}
		if(window.confirm("你确定要删除吗？(删除后将影响该岗位下的所有角色和用户！)")){
			var jobId = false;
			var jobIds = "";
			var orgId = document.all("orgId").value;
			var returnJobInfo = "";
			var flag = false;
			var isSelectedJob = false;
	 		for (var m=document.all("jobId").options.length-1;m>=0;m--){
		    	if(document.all("jobId").options[m].selected){
		    		var op = document.all("jobId").options[m];
		    		isSelectedJob = true;
		    		<%for(int i = 0; i < popedomJobId.length; i++){
		        		String popedomJobIdStr = popedomJobId[i];
		        	%>
		        	if(op.value=="<%=popedomJobIdStr%>"){
		        		flag = true;
						document.all("jobId").options[m]=null;
						if(jobIds==""){ 
							jobIds = op.value;
						}else{
							jobIds += "," + op.value;
						}
					}
					<%}%>
					if(!flag){
						if(returnJobInfo==""){
							returnJobInfo = op.text;
						}else{
							returnJobInfo += "\n" + op.text;						
						}
					}
					flag = false;
	         	}
	         }
	        if(returnJobInfo!=""){
	        	alert("对以下岗位没有权限。\n"+returnJobInfo);
	        }
	        if(!isSelectedJob){
	        	alert('请从"已设置岗位"中选择要递归取消的已设置的岗位！');
	        	return false;
	        }
         	for (var n=parent.frames[1].document.all("userList").options.length-1;n>=0;n--)
    			parent.frames[1].document.all("userList").options[n]=null;
    		if(jobIds!=""){
            	send_request('orgjobchange.jsp?jobId='+jobIds+'&orgId='+orgId+'&jobSn=-2');	    	
            }
		}
		
	}
	
	function okadd(){
	var len=document.all("jobId").options.length;
		for (var i=0;i<len;i++){	      
	         document.all("jobId").options[i].selected="true";
	     }
	         document.forms[0].action="<%=rootpath%>/orgmanager/orgoperjob.do?method=addorgandjob";
	         document.forms[0].submit();
	}
	
	function sendJob(){     
       var orgId = document.all("orgId").value;
       var len=document.all("jobId").options.length;
       var jobId=new Array()
       for (var i=0;i<len;i++){       
         jobId[i]=document.all("jobId").options[i].value;
        }   
	   send_request('jobsnchange.jsp?orgId='+orgId+'&jobId0='+jobId);
   }
	
	
	
	function up1() {
		var len=document.all("jobId").options.length;
		var isselected = 0;
		for (var i=0;i<len;i++){
			var op=document.all("jobId").options[i];
	   		if(op.selected){   			
	   			isselected = 1;
	   		}
	   	}
	   	if(isselected == 0){
	   		alert("请先选择一个岗位再进行此操作");
	   		return;
	   	}
		for (var i=0;i<len;i++){
			var op=document.all("jobId").options[i];
	   		if(op.selected){   			
	   			var tmp = new Option(op.text,op.value);
	   			if(i>0){
	   				var jobId1 = tmp.value;	   				
	   				var orgId = document.all("orgId").value;
	   				var dest = document.all("jobId").options[i-1];
	   				var jobId2 = dest.value;
	   				var jobSn = i;
	   				document.all("jobId").options[i-1] = tmp;
	   				document.all("jobId").options[i] = dest;
	   				document.all("jobId").options[i-1].selected=true;
	   				send_request('jobsnchange.jsp?jobId1='+jobId1+'&jobId2='+jobId2+'&orgId='+orgId+'&jobSn='+jobSn);
	   			}
	   		}
	    }
	}
	function upall() {
		var len=document.all("jobId").options.length;	
		var isselected = 0;
		for (var i=0;i<len;i++){
			var op=document.all("jobId").options[i];
	   		if(op.selected){   			
	   			isselected = 1;
	   		}
	   	}
	   	if(isselected == 0){
	   		alert("请先选择一个岗位再进行此操作");
	   		return;
	   	}
		for (var i=0;i<len;i++){
			var op=document.all("jobId").options[i];
	   		if(op.selected){   			
	   			var tmp = new Option(op.text,op.value);
	   			op.selected = false;   			
	   			var j=i;   			
	   			for(;j>=1;j--){     				
	   				var atmp =  document.all("jobId").options[j-1];   				
	   				var btmp = new Option(atmp.text,atmp.value);   				
	   				document.all("jobId").options[j] = btmp;
	   			}   			
	   			document.all("jobId").options[0] = tmp;
	   			break;	
	   		}
	    }  
	    sendJob();
	    document.all("jobId").options[0].selected=true;
	}
	
	function down1() {
		var len=document.all("jobId").options.length;
		var isselected = 0;
		for (var i=0;i<len;i++){
			var op=document.all("jobId").options[i];
	   		if(op.selected){   			
	   			isselected = 1;
	   		}
	   	}
	   	if(isselected == 0){
	   		alert("请先选择一个岗位再进行此操作");
	   		return;
	   	}
		var flag = 0;
		for (var i=0;i<len;i++){
			var op=document.all("jobId").options[i];
	   		if(op.selected){   			
	   			var tmp = new Option(op.text,op.value);   			
	   			op.selected=false;
	   			if(i == len-1){
	   				flag = len-2;
	   			}
	   			else {
	   				flag = i;
	   			}
	   			if(i<len-1){
	   				var dest = document.all("jobId").options[i+1];
	   				var jobId2 = tmp.value;	   				
	   				var orgId = document.all("orgId").value;
	   				var jobId1 = dest.value;
	   				var jobSn = i;
	   				document.all("jobId").options[i+1] = tmp;
	   				document.all("jobId").options[i] = dest;
	   				send_request('jobsnchange.jsp?jobId1='+jobId1+'&jobId2='+jobId2+'&orgId='+orgId+'&jobSn='+jobSn);
	   			}
	   		}
	    }
	    document.all("jobId").options[flag+1].selected=true;
	}
	
	function downall() {
		var len=document.all("jobId").options.length;
		var isselected = 0;
		for (var i=0;i<len;i++){
			var op=document.all("jobId").options[i];
	   		if(op.selected){   			
	   			isselected = 1;
	   		}
	   	}
	   	if(isselected == 0){
	   		alert("请先选择一个岗位再进行此操作");
	   		return;
	   	}	
		for (var i=0;i<len;i++){
			var op=document.all("jobId").options[i];
	   		if(op.selected){   			
	   			var tmp = new Option(op.text,op.value);
	   			op.selected = false;   			
	   			var j=i;   			
	   			for(;j<len-1;j++){     				
	   				var atmp =  document.all("jobId").options[j+1];   				
	   				var btmp = new Option(atmp.text,atmp.value);   				
	   				document.all("jobId").options[j] = btmp;   				
	   			}   			
	   			document.all("jobId").options[len-1] = tmp;	
	   		}
	    }
	    sendJob();
	    document.all("jobId").options[len-1].selected=true;
	}
	//显示右边框架的用户列表
	function showUserList(){
			
		var curjobId;
		
		var len=document.all("jobId").options.length;
		var isnew = 0;
		var orgId = document.all("orgId").value;
		var jobSn=0;
		for (var i=0;i<len;i++){
	      	if(document.all("jobId").options[i].selected){      		
	        	curjobId = document.all("jobId").options[i].value;
	        	jobSn = i;
	        	var strs = curjobId.split(";"); 
	        	//没有保存的岗位
	        	if(strs.length == 2){
	        		isnew = "1";
	        		//alert(parent.frames.length);
	        		//parent.frames[1].document.forms[0].b1new.disabled=true;
	        		//parent.frames[1].document.forms[0].b2save.disabled=true;
	        		//parent.frames[1].document.forms[0].b3del.disabled=true;
	        		parent.frames[1].document.forms[0].button1.disabled=true;
	        		parent.frames[1].document.forms[0].button2.disabled=true;
	        		parent.frames[1].document.forms[0].button3.disabled=true;
	        		parent.frames[1].document.forms[0].button4.disabled=true;
	        		
	        		for (var n=parent.frames[1].document.all("userList").options.length-1;n>=0;n--)
	    				parent.frames[1].document.all("userList").options[n]=null;
	        		alert("请先保存");
	        		return;
	        	}
	        }
	    }
	    if(isnew == 0){
			parent.frames[1].location.href="<%=rootpath%>/orgmanager/orgoperjob.do?method=getUserList&curjobid="+curjobId+"&orgId="+orgId+"&jobSn="+jobSn;
		}
		
	}
	
	//机构岗位角色设置
	function roleset(){
		var len=document.all("jobId").options.length;
		var isselected = 0;
		for (var i=0;i<len;i++){
			var op=document.all("jobId").options[i];
		   	if(op.selected){   			
		   		isselected = 1;
		   	}
		}
		if(isselected == 0){
			alert("请先选择一个岗位再进行此操作");
			return;
		}
		var jobIdValue = document.all("jobId").value;
		var url = "<%=rootpath%>/sysmanager/jobmanager/A03/jobList_ajax.jsp?orgId=<%=organization.getOrgId()%>&jobId=" + jobIdValue;
		var roleVale = window.showModalDialog(url,"","dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no;maximize=yes;minimize=0");
		if(roleVale == "ok"){
			window.location.reload();
		}
	}
    </SCRIPT>
    <%@ include file="/include/css.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/sysmanager/css/contentpage.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/sysmanager/css/tab.winclassic.css">
    <script language="javascript" src="<%=request.getContextPath()%>/include/dragdiv.js"></script>
    <script language="javascript" src="<%=request.getContextPath()%>/include/querySelect.js"></script>
<body onload="loadDragDiv();">
<form name="OrgJobForm" action="" target="hiddenFrame" method="post" >

<table width="100%" border="0" cellpadding="1" cellspacing="1">
	<input name="popedomJobIds" value="<%=popedomJobIds%>" type="hidden">
  <tr >
    <!--<td width="40%" align="center" class="detailtitle">可选岗位</td>-->
    <td width="40%" align="center" >可选岗位</td>
    <td width="20%" align="center" >&nbsp;</td>
    <td width="40%" align="center" >[<%=organization.getRemark5()%>]已设置岗位</td>
    <td width="20%" align="center" >&nbsp;</td>
  </tr>
    <tr>
  	<td width="40%" align="left">
  		<input name="selectRoles" type="text" value="" onkeydown="enterKeydowngo()" onpropertychange="onBlurryQueryChange()" ondblclick=""  />
  		<input name="que" type="button" value="查询" onclick="blurryQuary()" class="input" />
  		<input name="nextQue" style="display:none" type="button" value="查找下一个" onclick="nextQuery()" class="input" />
  	</td>
    <td width="20%" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
    <td width="40%" align="left">
    	
    </td>
  </tr>
  <tr >
     <td class="detailcontent" align="center"><div class="win" id="dd_1" align="left" style="width:150" title="下拉选择框的右边可以左右拖动">
     <select name="allist"  multiple style="width:98%" onDBLclick="addjob()" size="18" title="下拉选择框的右边可以左右拖动">
		 <pg:list requestKey="joblist1">
			<option value="<pg:cell colName="jobId"/>"><pg:cell colName="jobName"/></option>
		 </pg:list>			
	</select>
	</div></td>				  
		  	
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
    <td class="detailcontent" align="center"><div class="win" id="dd_2" align="left" style="width:150" title="下拉选择框的右边可以左右拖动">
     <select name="jobId"  multiple style="width:98%" onChange="showUserList()" onDBLclick="deletejob()" size="18" title="下拉选择框的右边可以左右拖动">
	  <pg:list requestKey="orgjoblist1">
		<option value="<pg:cell colName="jobId"/>"><pg:cell colName="jobName"/></option>
	  </pg:list>			
 	</div></select>
	</td>
	<td>
	<table align="center" width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
        <td align="center" class="detailcontent"><input name="setrole" type="button" class="input" value="角色设置" onClick="roleset()"></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
      <tr>
        <td align="center" class="detailcontent"><input name="button1" type="button" class="input" value="到顶部" onClick="upall()"></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><input name="button2" type="button" class="input" value="向上移" onClick="up1()"></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
      <tr>
        <td align="center" class="detailcontent"><input name="button3" type="button" class="input" value="向下移" onClick="down1()"></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
      <tr>
        <td align="center" class="detailcontent"><input name="button4" type="button" class="input" value="到底部" onClick="downall()"></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
    </table>
	
	</td>
  <tr>
  	<td>&nbsp;</td>
  </tr>	
  <tr>
    <td align="center" colspan="4"><input name="butn" type="button" class="input" value="递归设置子机构岗位" onClick="addsuborg();"></td>
  </tr>		
  <tr>
  	<td>&nbsp;</td>
  </tr>	
  <tr>
    <td align="center" colspan="4"><input name="butn" type="button" class="input" value="取消本级和子机构的岗位" onClick="deletesuborg();"></td>
  </tr>  
  </tr>
  </table>
<pg:beaninfo sessionKey="orgjobForm">
<input type="hidden"  name="orgId" value="<pg:cell colName="orgId"  defaultValue=""/>"/>
</pg:beaninfo>
</form>
<div id=divProcessing style="width:200px;height:30px;position:absolute;left:135px;top:450px;display:none">
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
