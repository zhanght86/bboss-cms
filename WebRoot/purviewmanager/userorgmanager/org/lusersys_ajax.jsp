<%
/**
 * <p>Title: 机构岗位设置</p>
 * <p>Description: 机构岗位设置</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-17
 * @author da.wei
 * @version 1.0
 **/
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="java.util.List, java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.JobManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.form.OrgJobForm
				,com.frameworkset.platform.sysmgrcore.entity.Organization
				,com.frameworkset.platform.sysmgrcore.entity.Job
				,com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>

<%

	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	
	//要想使用ajax请把userjoborg.jsp里面的frame也没改掉。还有struts config里面的forward
	String orgId = request.getParameter("orgId");
	if(orgId == null){
		orgId=(String)request.getAttribute("orgId");
	}
	
	//OrgJobForm object = (OrgJobForm)session.getAttribute("orgjobForm");

	Organization organization = new Organization();
	organization = OrgCacheManager.getInstance().getOrganization(orgId);
    	String jobId=(String)request.getAttribute("curjobid");
    	if(jobId ==null){
    		jobId = "";
    	}
    JobManager jobManager = SecurityDatabase.getJobManager();
    
	//List joblist = jobManager.getJobList();
	
	
	
    
    //左边只出现有权限进行机构岗位授予的岗位 : 自己创造的岗位 和 授予机构的岗位
    //List list1 = new ArrayList();
    String popedomJobIds = "";
    //if(joblist != null)
    //{
    	//for(int i=0;i<joblist.size();i++)
    	//{
    		//Job job = (Job)joblist.get(i);
    		//if(accessControl.checkPermission(job.getJobId(), "jobset", AccessControl.JOB_RESOURCE))
    		//{
    			//list1.add(job);
    			//if("".equals(popedomJobIds)){
    				//popedomJobIds = job.getJobId();
    			//}else{
    				//popedomJobIds += "," + job.getJobId();
    			//}
    		//}
    	//}
    //}
    
    //对自己有权限的岗位进行设置
    List orgjoblist = jobManager.getJobList(organization);
    
    List list2 = new ArrayList();
    
    if(orgjoblist != null)
    {
    	for(int i=0; i<orgjoblist.size(); i++)
    	{
    		Job job =(Job) orgjoblist.get(i);
    		
    		if(accessControl.checkPermission(job.getJobId(), "jobset", AccessControl.JOB_RESOURCE))
    		{
    			list2.add(job);
    		}
    	}
    }
    
    //有权限的岗位ID
    String[] popedomJobId = popedomJobIds.split(",");
     
	//request.setAttribute("joblist1",list1);
	request.setAttribute("orgjoblist1",list2);
	
    
%>
<html>
<head>    
  	<title>属性容器</title>
	<SCRIPT LANGUAGE="JavaScript">
	
	var api = parent.frameElement.api, W = api.opener;
	
	var http_request = false;
	//初始化，指定处理的函数，发送请求的函数
	function send_request(url){
		
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
	   //已设置的岗位
	   var alreadyJobArr = document.all("jobId") ;
	   var n=document.all("jobId").options.length-1;
	   var orgId = document.all("orgId").value;	
	   var jobSn = n+2;
	   var jobIds = "";
	   var alreadyJobs = "";   	 	
	   for(var i=0;i<document.all("allist").options.length;i++){
		   var op=document.all("allist").options[i];
		   if(op.selected){	
		   	   var flag = false;
		   	   for(var j = 0; j < alreadyJobArr.length; j++){
		   	   	   if(op.text==alreadyJobArr.options[j].text){
		   	   	   		flag = true;
		   	   	   }
			   }
			   if(!flag){
			   		addone(op.text,op.value,n,orgId);
					if(jobIds=="") jobIds = jobIds + op.value;
					else jobIds = jobIds + "," + op.value;
			   }else{
			   		if(alreadyJobs=="") alreadyJobs = alreadyJobs + op.text;
					else alreadyJobs = alreadyJobs + "\n" + op.text;
			   }
		   }
		   
	  }
	  if(jobIds != ""){
	  	send_request('orgjobchange.jsp?jobId='+jobIds+'&orgId='+orgId+'&jobSn='+jobSn);
	  }else if(alreadyJobs != ""){
	  	$.dialog.alert("<pg:message code='sany.pdp.yxgwyjsz'/>：\n" + alreadyJobs,function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	  	return;
	  }else{
	  	$.dialog.alert("<pg:message code='sany.pdp.to.choose.jobs'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	  	return;
	  }
	  
	}	
	
	function deleteall()
	{
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
					jobIds += op.value + ",";
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
	   		
	   		if(returnJobInfo!="")
	   		{
	   			$.dialog.alert("<pg:message code='sany.pdp.yxgwyjsz'/>：\n" + returnJobInfo,function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	        	return false;
	        }
	        
	         if(jobIds != "")
	        {
	        	 $.dialog.confirm("<pg:message code='sany.pdp.userorgmanager.org.post.delete.all.warning'/>", function() {
	        		 for (var n=parent.frames[1].document.all("userList").options.length-1;n>=0;n--)
			         	{
			    			parent.frames[1].document.all("userList").options[n]=null;
			    		}
			    		
			    		for (var m=document.all("jobId").options.length-1;m>=0;m--)
			    		{
			    			document.all("jobId").options[m] = null;
			    		}
			    		
				        send_request('orgjobchange.jsp?jobId='+jobIds+'&orgId='+orgId+'&jobSn=-1');	    	
	        	 });
	        }
	        else
	        {
	        	$.dialog.alert("<pg:message code='sany.pdp.to.choose.jobs'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
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
        	$.dialog.alert("<pg:message code='sany.pdp.yxgwmyqx'/>:\n"+returnJobInfo,function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
        }
        if(!isSelectedJob)
        {
        	$.dialog.alert("<pg:message code='sany.pdp.userorgmanager.org.authorize.post.select'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
        	return false;
        }
        if(jobIds!=""){
			send_request("orgjobchange.jsp?orgId=" + orgId + "&jobIds=" + jobIds);
		}else{
			$.dialog.alert("<pg:message code='sany.pdp.to.choose.jobs'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		}
	}
	
	     
	function addall(){
		//已设置的岗位
	   var alreadyJobArr = document.all("jobId") ;
		var orgId = document.all("orgId").value;
		var jobSn=document.all("jobId").options.length+1;
		var n=document.all("jobId").options.length-1;
		var p=document.all("allist").options.length-1;	
		var jobIds = "";
		var alreadyJobs = "";	  
	    for(var i=0;i<document.all("allist").options.length;i++){
	         var op=document.all("allist").options[i];
	         var jobId = op.value;
	         var flag = false;
		   	   for(var j = 0; j < alreadyJobArr.length; j++){
		   	   	   if(op.text==alreadyJobArr.options[j].text){
		   	   	   		flag = true;
		   	   	   }
			   }
			   if(!flag){
			   		addone(op.text,op.value,n,orgId); 
			         if(jobIds=="") jobIds = jobIds + jobId;
					 else jobIds = jobIds + "," + jobId;
			   }else{
			   		if(alreadyJobs=="") alreadyJobs = alreadyJobs + op.text;
					else alreadyJobs = alreadyJobs + "\n" + op.text;
			   }
	   }
	   if(jobIds !=""){
		   send_request('orgjobchange.jsp?jobId='+jobIds+'&orgId='+orgId+'&jobSn='+jobSn); 
	   }else if(alreadyJobs != ""){
	   		$.dialog.alert("<pg:message code='sany.pdp.yxgwyjsz'/>：\n" + alreadyJobs,function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	  		return;
	   }
	}
	function deletejob(){
		
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
						//document.all("jobId").options[m]=null;
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
	        	$.dialog.alert("<pg:message code='sany.pdp.yxgwmyqx'/>:\n"+returnJobInfo,function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	        	return false;
	        }
	        
	        if(jobIds != "")
	        {
	        	 $.dialog.confirm("<pg:message code='sany.pdp.userorgmanager.org.post.delete.all.warning'/>", function() {
	        		 for (var n=parent.frames[1].document.all("userList").options.length-1;n>=0;n--)
			         	{
			    			parent.frames[1].document.all("userList").options[n]=null;
			    		}
			    		
			    		for (var m=document.all("jobId").options.length-1;m>=0;m--)
			    		{
			    			if(document.all("jobId").options[m].selected)
			    			{
			    				document.all("jobId").options[m] = null;
			    			}
			    		}
		    		
				         send_request('orgjobchange.jsp?jobId='+jobIds+'&orgId='+orgId+'&jobSn=-1');	 
	        	 });
	        }
	        else
	        {
	        	$.dialog.alert("<pg:message code='sany.pdp.to.choose.jobs'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	        }
	}
	
	function deletesuborg(){
		
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
	        	$.dialog.alert("<pg:message code='sany.pdp.yxgwmyqx'/>:\n"+returnJobInfo,function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	        }
	        if(!isSelectedJob){
	        	$.dialog.alert("<pg:message code='sany.pdp.userorgmanager.org.authorize.post.select'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	        	return false;
	        }
         	for (var n=parent.frames[1].document.all("userList").options.length-1;n>=0;n--)
    			parent.frames[1].document.all("userList").options[n]=null;
    		if(jobIds!=""){
    			$.dialog.confirm("<pg:message code='sany.pdp.userorgmanager.org.post.delete.all.warning'/>", function() {
    				send_request('orgjobchange.jsp?jobId='+jobIds+'&orgId='+orgId+'&jobSn=-2');
    			})
            }else{
            	$.dialog.alert("<pg:message code='sany.pdp.to.choose.jobs'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
            }
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
	   		$.dialog.alert("<pg:message code='sany.pdp.to.choose.jobs'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
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
	   		$.dialog.alert("<pg:message code='sany.pdp.to.choose.jobs'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
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
	   		$.dialog.alert("<pg:message code='sany.pdp.to.choose.jobs'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
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
	   		W.$.dialog.alert("<pg:message code='sany.pdp.to.choose.jobs'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
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
		//已设置岗位总数
		var len=document.all("jobId").options.length;
		var isnew = 0;
		var orgId = document.all("orgId").value;
		var jobSn=0;
		for (var i=0;i<len;i++){
	      	if(document.all("jobId").options[i].selected){      		
	        	curjobId = document.all("jobId").options[i].value;
	        	jobSn++;
	        	//var strs = curjobId.split(";");
	        	//没有保存的岗位
	        	//if(strs.length == 2){
	        	//	isnew = "1";
	        	//	parent.frames[1].document.forms[0].button1.disabled=true;
	        	//	parent.frames[1].document.forms[0].button2.disabled=true;
	        	//	parent.frames[1].document.forms[0].button3.disabled=true;
	        	//	parent.frames[1].document.forms[0].button4.disabled=true;
	        		//parent.frames[1].document.forms[0].button11.disabled=true;
	        		//parent.frames[1].document.forms[0].button22.disabled=true;
	        		//parent.frames[1].document.forms[0].button33.disabled=true;
	        		//parent.frames[1].document.forms[0].button44.disabled=true;
	        		//parent.frames[1].document.forms[0].button55.disabled=true;
	        		//parent.frames[1].document.forms[0].button111.disabled=true;
	        		//parent.frames[1].document.forms[0].button222.disabled=true;
	        		//parent.frames[1].document.forms[0].button333.disabled=true;	        		
	        	//	for (var n=parent.frames[1].document.all("userList").options.length-1;n>=0;n--)
	    		//		parent.frames[1].document.all("userList").options[n]=null;
	        	//	alert("请先保存");
	        	//	return;
	        	//}
	        }
	    }
	    if(jobSn == 1){
			parent.frames[1].location.href="rusersys_ajax.jsp?curjobid="+curjobId+"&orgId="+orgId+"&jobSn="+jobSn;
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
			W.$.dialog.alert("<pg:message code='sany.pdp.to.choose.jobs'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			return;
		}
		var jobIdValue = document.all("jobId").value;
		var url = "../purviewmanager/userorgmanager/org/jobList_ajax.jsp?orgId=<%=organization.getOrgId()%>&jobId=" + jobIdValue;
		parent.parent.parent.$.dialog({title:'<pg:message code="sany.pdp.role.setting"/>',width:760,height:560, content:'url:'+url,lock: true});
		showUserList();
	}
	 function query(){
	 	var jobname=document.getElementById("jobname").value;
	 	var jobnubmer = document.getElementById("jobnumber").value;
	 	$("#dd_1").load("selectjoblist.page?jobname="+jobname+"&jobnubmer="+jobnubmer);
      	//alert(document.getElementById("selectRoles").value);
      }
    </SCRIPT>
    

	
    <script language="javascript" src="../../scripts/dragdiv.js"></script>
    <script language="JavaScript" src="../../../include/querySelect.js" type="text/javascript"></script>
<body onload="loadDragDiv();">
<form name="OrgJobForm" target="hiddenFrame" action="" method="post" >
<div id="searchblock">
				<div class="search_top">
					<div class="right_top"></div>
					<div class="left_top"></div>
				</div>
				<div class="search_box">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="left_box"></td>
								<td>
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="table2">
										<tr>
											<th><pg:message code="sany.pdp.role.organization.name"/>：</th>
											<td><input type="text" id="jobname"/></td>
											<th><pg:message code="sany.pdp.role.organization.number"/>：</th>
											<td><input type="text" id="jobnumber"/></td>
											<td>
											<a class="bt_1" onclick="query()" ><span><pg:message code="sany.pdp.common.operation.search"/></span></a>
											</td>
										</tr>
									</table>
								</td>
								<td class="right_box"></td>
							</tr>
					 </table>
				</div>
				<div class="search_bottom">
					<div class="right_bottom"></div>
					<div class="left_bottom"></div>
				</div>
	</div>			
<table width="100%" border="0" cellpadding="1" cellspacing="1">
	<input name="popedomJobIds" value="<%=popedomJobIds%>" type="hidden">
  <tr >
    <td width="40%" align="left" ><pg:message code="sany.pdp.choose.job"/></td>
    <td width="3%" align="center" >&nbsp;</td>
    <td width="37%" align="right" ><pg:message code="sany.pdp.exist.job"/></td>
    <td width="30%" align="center" >&nbsp;</td>
  </tr>
  <tr >
     <td class="detailcontent" align="left">
     <div class="win" id="dd_1" align="left" style="width:400">
      <script type="text/javascript">
       $(document).ready(function() {
    	  $("#dd_1").load("selectjoblist.page #customContent");
      });
      </script>
	   
	</div></td>				  
		  	
    <td align="center" class="detailcontent"><table width="80%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center" class="detailcontent"><a class="bt_2" onClick="addjob()"><span>&gt;</span></a></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><a class="bt_2"  onClick="addall()"><span>&gt;&gt;</span></a></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
      <tr>
        <td align="center" class="detailcontent"><a class="bt_2"  onClick="deleteall()"><span>&lt;&lt;</span></a></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
      <tr>
        <td align="center" class="detailcontent"><a class="bt_2"  onClick="deletejob()"><span>&lt;</span></a></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
    </table></td>
    <td class="detailcontent" align="right"><div class="win" id="dd_2" align="left" style="width:200">
     <select name="jobId"  multiple style="width:98%" onChange="showUserList()" onDBLclick="deletejob()" size="18">
	  <pg:list requestKey="orgjoblist1">
		<option value="<pg:cell colName="jobId"/>"><pg:cell colName="jobName"/></option>
	  </pg:list>			
 	</div></select>
	</td>
	<td>
	<table align="center" width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
        <td align="center" class="detailcontent"><a class="bt_1" onClick="roleset()"><span><pg:message code="sany.pdp.role.setting"/></span></a></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
      <tr>
        <td align="center" class="detailcontent"><a class="bt_2" onClick="upall()"><span><pg:message code="sany.pdp.top"/></span></a></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><a class="bt_2" onClick="up1()"><span><pg:message code="sany.pdp.up"/></span></a></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
      <tr>
        <td align="center" class="detailcontent"><a class="bt_2"  onClick="down1()"><span><pg:message code="sany.pdp.down"/></span></a></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
      <tr>
        <td align="center" class="detailcontent"><a class="bt_2"  onClick="downall()"><span><pg:message code="sany.pdp.bottom"/></span></a></td>
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
    <td align="center" colspan="4"><a class="bt_1"  onClick="addsuborg();"><span><pg:message code="sany.pdp.resursion.set.job"/></span></a></td>
  </tr>		
  <tr>
  	<td>&nbsp;</td>
  </tr>	
  <tr>
    <td align="center" colspan="4"><a class="bt_1"  onClick="deletesuborg();"><span><pg:message code="sany.pdp.cancel.resursion.set.job"/></span></a></td>
  </tr> 
  
  </tr>
  </table>
<pg:beaninfo sessionKey="orgjobForm">
<input type="hidden"  name="orgId" id="orgId" value="<%=orgId %>"/>
</pg:beaninfo>
</form>
<div id=divProcessing style="width:200px;height:30px;position:absolute;left:175px;top:420px;display:none">
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
