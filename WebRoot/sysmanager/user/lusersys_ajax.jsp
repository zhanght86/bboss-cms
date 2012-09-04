<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="java.util.List"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.JobManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Organization,com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*
				,com.frameworkset.platform.security.AccessControl
				,java.util.ArrayList"%>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(pageContext);
	
	//要想使用ajax请把userjoborg.jsp里面的frame也没改掉。还有struts config里面的forward
	String orgId = request.getParameter("orgId");
	if(orgId == null){
		orgId=(String)request.getAttribute("orgId");
	}
	Organization org = new Organization();
	org = OrgCacheManager.getInstance().getOrganization(orgId);
	

	JobManager jobManager = SecurityDatabase.getJobManager();
	List joblist = jobManager.getJobList();
	List orgjoblist = jobManager.getJobList(org);
	
	List list1 = new ArrayList();
    if(joblist != null)
    {
    	for(int i=0;i<joblist.size();i++)
    	{
    		Job job = (Job)joblist.get(i);
    		if(accessControl.checkPermission(job.getJobId(), "orgset", AccessControl.JOB_RESOURCE))
    		{
    			list1.add(job);
    		}
    	}
    }
    List list2 = new ArrayList();
    if(orgjoblist != null)
    {
    	for(int i=0;i<orgjoblist.size();i++)
    	{
    		Job job = (Job)orgjoblist.get(i);
    		if(accessControl.checkPermission(job.getJobId(), "orgset", AccessControl.JOB_RESOURCE))
    		{
    			list2.add(job);
    		}
    	}
    }
    
	request.setAttribute("joblist",list1);
	request.setAttribute("orgjoblist",list2);

	//request.setAttribute("orgId",list);
%>
<html>
<head>    
  	<title>设置机构[<%=org.getOrgName()%>]岗位</title>
  	<script language="javascript" src="<%=request.getContextPath()%>/include/querySelect.js"></script>
	<SCRIPT LANGUAGE="JavaScript">
	//初始化，指定处理的函数，发送请求的函数
	function send_request(url){
		document.all.divProcessing.style.display = "block";
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
	   //send_request('<%=rootpath%>/sysmanager/orgmanager/orgjobchange.jsp?jobId='+value+'&orgId='+orgId+'&jobSn='+jobSn);
	   
	}
	
	function addjob(){	
	   var n=document.all("jobId").options.length-1;
	   var jobSn = n+2;
	   var jobIds = "";
	   var orgId = document.all("orgId").value;   	 	
	   for(var i=0;i<document.all("allist").options.length;i++){
		   var op=document.all("allist").options[i];
		   if(op.selected){	
		       if(jobIds=="") jobIds = jobIds + op.value;
		       else jobIds = jobIds + "," + op.value;
			   addone(op.text,op.value,n,orgId);
		   }
	   }
	   send_request('<%=rootpath%>/sysmanager/orgmanager/orgjobchange.jsp?jobId='+jobIds+'&orgId='+orgId+'&jobSn='+jobSn);
	}
	function addall(){
		var orgId = document.all("orgId").value;
		var jobIds = "";
		var n=document.all("jobId").options.length-1;
		var jobSn = n+2;
		var p=document.all("allist").options.length-1;		  
	    for(var i=0;i<document.all("allist").options.length;i++){
	        var op=document.all("allist").options[i];
	        if(jobIds=="") jobIds = jobIds + op.value;
		    else jobIds = jobIds + "," + op.value;
	        addone(op.text,op.value,n,orgId);
	   }
	   send_request('<%=rootpath%>/sysmanager/orgmanager/orgjobchange.jsp?jobId='+jobIds+'&orgId='+orgId+'&jobSn='+jobSn);
	}	
	function deletejob(){
		if(window.confirm("你确定要删除吗？(删除后将影响该岗位下的所有用户！)")){
			var jobId = false;
			var jobIds = "";
			var orgId = document.all("orgId").value;
	 		for (var m=document.all("jobId").options.length-1;m>=0;m--){
		    	if(document.all("jobId").options[m].selected){
	         		jobId = document.all("jobId").options[m].value;
	         		if(jobIds=="") jobIds = jobIds + jobId;
		    		else jobIds = jobIds + "," + jobId;
					document.all("jobId").options[m]=null;					
	         	}
		    }
		    send_request('<%=rootpath%>/sysmanager/orgmanager/orgjobchange.jsp?jobId='+jobIds+'&orgId='+orgId+'&jobSn=-1');
		}
	}
	function deleteall(){
		if(window.confirm("你确定要删除所有岗位吗？(删除后将影响该岗位下的所有用户!)")){
			var orgId = document.all("orgId").value;
			var jobIds = "";
			for (var m=document.all("jobId").options.length-1;m>=0;m--){
	    		jobId = document.all("jobId").options[m].value;
	    		if(jobIds=="") jobIds = jobIds + jobId;
		    	else jobIds = jobIds + "," + jobId;
				document.all("jobId").options[m]=null;				
	   		}
	    	send_request('<%=rootpath%>/sysmanager/orgmanager/orgjobchange.jsp?jobId='+jobIds+'&orgId='+orgId+'&jobSn=-1');
		}
	}
	function ok(){
		//window.dialogArguments.parent.document.location.href = window.dialogArguments.parent.document.location.href;
		window.returnValue = "ok";
		window.close(); 
	}
	
	function okadd(){
	var len=document.all("jobId").options.length;
		for (var i=0;i<len;i++){	      
	         document.all("jobId").options[i].selected="true";
	     }
	         document.forms[0].action="<%=rootpath%>/sysmanager/orgmanager/orgoperjob.do?method=addorgandjob";
	         document.forms[0].submit();
	}
	
	function sendJob(){     
       var orgId = document.all("orgId").value;
       var len=document.all("jobId").options.length;
       var jobId=new Array()
       for (var i=0;i<len;i++){       
         jobId[i]=document.all("jobId").options[i].value;
        }   
	   send_request('<%=rootpath%>/sysmanager/orgmanager/jobsnchange.jsp?orgId='+orgId+'&jobId='+jobId);
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
	   				send_request('<%=rootpath%>/sysmanager/orgmanager/jobsnchange.jsp?jobId1='+jobId1+'&jobId2='+jobId2+'&orgId='+orgId+'&jobSn='+jobSn);
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
	   				send_request('<%=rootpath%>/sysmanager/orgmanager/jobsnchange.jsp?jobId1='+jobId1+'&jobId2='+jobId2+'&orgId='+orgId+'&jobSn='+jobSn);
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
	        		parent.frames[1].document.forms[0].b1new.disabled=true;
	        		parent.frames[1].document.forms[0].b2save.disabled=true;
	        		parent.frames[1].document.forms[0].b3del.disabled=true;
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
			parent.frames[1].location.href="<%=rootpath%>/sysmanager/orgmanager/orgoperjob.do?method=getUserList&curjobid="+curjobId+"&orgId="+orgId+"&jobSn="+jobSn;
		}
		
	}
	</SCRIPT>
   <%@ include file="/include/css.jsp"%>
   <link rel="stylesheet" type="text/css" href="../css/contentpage.css">
   <link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
   <%@ include file="/include/css.jsp"%>
   <link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
   <link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
	
<body >
<form name="OrgJobForm" target="hiddenFrame" action="" method="post" >

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
  	<td width="20%" align="center" >&nbsp;</td>
    <td width="20%" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
    <td width="40%" align="left">
    	
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
	<td>
	<table align="center" width="100%" border="0" cellspacing="0" cellpadding="0">
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
    <td align="center" colspan="4"><input name="butn" type="button" class="input" value="确定" onClick="ok();"></td>
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
