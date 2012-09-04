<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Organization"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Job"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="java.util.List"%>

<%
		String orgId = request.getParameter("orgId");
		if(orgId == null){
			orgId=(String)request.getAttribute("orgId");
		}
    	String jobId=(String)request.getAttribute("curjobid");
    	if(jobId ==null){
    		jobId = "";
    	}

%>


<html>
<head>    
  <title>属性容器</title>
<SCRIPT LANGUAGE="JavaScript">
	var gov= "<%=orgId%>";
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
   document.forms[0].action="<%=rootpath%>/orgmanager/orgoperjob.do?method=addJob&orgId="+gov+"&jobId="+value;
   document.forms[0].submit();
   
}
function deleteall(){
	for(var i=0;i<document.all("jobId").options.length;i++){
   		document.all("jobId").options[i].selected = true;
   }
   if(window.confirm("你确定要删除吗？(删除后是不可以再恢复的)")){	
   		document.forms[0].action="<%=rootpath%>/orgmanager/orgoperjob.do?method=deleteJob&all=1&orgId="+gov;
   		document.forms[0].submit();
   }
}
      
function addall(){
   for(var i=0;i<document.all("allist").options.length;i++){
   		document.all("allist").options[i].selected = true;
   }
   document.forms[0].action="<%=rootpath%>/orgmanager/orgoperjob.do?method=addJob&all=1&orgId="+gov;
   document.forms[0].submit();
}
function deletejob(){
	var j;
	 for (var m=document.all("jobId").options.length-1;m>=0;m--)
	      if(document.all("jobId").options[m].selected){
	      	j = document.all("jobId").options[m].value;
	      	if(window.confirm("你确定要删除吗？(删除后是不可以再恢复的)")){	
	      		document.forms[0].action="<%=rootpath%>/orgmanager/orgoperjob.do?method=deleteJob&curjobid="+j+"&orgId="+gov;
				document.forms[0].submit();
			}
         }
        
}

function showUserList(){//传岗位ID
		
	var curjobId;
	var count = 0;

	for (var s=document.all("jobId").options.length-1;s>=0;s--)
      	if(document.all("jobId").options[s].selected){
        curjobId = document.all("jobId").options[s].value;
        count++;
    }
    if(count <1){
    	alert("至少要选择一个用户");
    	return;
	}
	document.forms[0].action="<%=rootpath%>/orgmanager/orgoperjob.do?method=getUserList&curjobid="+curjobId+"&orgId="+gov;
	document.forms[0].submit();	         
}

function okadd(){
var len=document.all("jobId").options.length;
	for (var i=0;i<len;i++){	      
         document.all("jobId").options[i].selected="true";
         }
         document.forms[0].action="<%=rootpath%>/orgmanager/orgoperjob.do?method=addorgandjob";
         document.forms[0].submit();
}
function createuser()
{
	var jobId="<%=jobId%>";	
	getPropertiesContent().location.href="../sysmanager/orgmanager/userselect.jsp?orgId=<%=orgId%>&jobId=<%=jobId%>";   
}
function createuser1() {
    window.showModalDialog("../sysmanager/orgmanager/userselect.jsp?orgId=<%=orgId%>&jobId=<%=jobId%>",null,"dialogWidth:600px;dialogHeight:500px;status:no;scroll:no;help:no");
}

function up1() {
	var len=document.all("jobId").options.length;
	for (var i=0;i<len;i++){
		var op=document.all("jobId").options[i];
   		if(op.selected){   			
   			var tmp = new Option(op.text,op.value);
   			if(i>0){
   				var dest = document.all("jobId").options[i-1];
   				document.all("jobId").options[i-1] = tmp;
   				document.all("jobId").options[i] = dest;
   				document.all("jobId").options[i-1].selected=true;
   			}
   		}
         
    }
        
}

function deleteUser() {
    var len=document.all("userList").options.length;
    var len2=document.all("jobId").options.length;
    var selectCount = 0;
    var selJobId = "";
	for (var i=0;i<len;i++){	      
         if(document.all("userList").options[i].selected)
         	selectCount++;
    }
    
    for (var j=0;j<len2;j++){	      
         if(document.all("jobId").options[j].selected){
         	selJobId = document.all("jobId").options[j].value;
         }
         
    }
    if(selectCount <1){
    	alert("至少要选择一个用户");
    	return;
    }else{
    	if(window.confirm("你确定要删除吗？(删除后是不可以再恢复的)")){	
			document.forms[0].action="<%=rootpath%>/orgmanager/orgoperjob.do?method=deleteUser&orgId="+gov+"&jobId="+selJobId;
   			document.forms[0].submit();
   		}
    }
    
}
</SCRIPT>
   <%@ include file="/include/css.jsp"%>
   <link rel="stylesheet" type="text/css" href="../css/contentpage.css">
   <link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
   
<body class="contentbodymargin">

<center>
<form name="OrgJobForm" action="" method="post" >

<table width="80%" border="0" cellpadding="1" cellspacing="1">
  <tr >
    <td width="35%" align="center" class="detailtitle">可选岗位</td>
    <td width="10%" align="center" class="detailtitle">&nbsp;</td>
    <td width="35%" align="center" class="detailtitle">已授予岗位</td>
    <td width="30%" align="center" class="detailtitle">该岗位下用户</td>
	<td width="40%" align="center" class="detailtitle"><div align="right">
	  <input name="Submit" type="button" class="input" value="添加用户" onClick="createuser()">
      </div></td>
  </tr>
  <tr >
     <td class="detailcontent" align="right" >
     <select name="allist"  multiple style="width:80%" onDBLclick="addjob()" size="18">
				  <pg:list requestKey="joblist">
					<option value="<pg:cell colName="jobId"/>"><pg:cell colName="jobName"/></option>
				  </pg:list>			
	</select>	</td>				  
		  	
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
    <td class="detailcontent">
     <select name="jobId"  multiple style="width:80%" onDBLclick="deletejob()" onChange="showUserList()" size="18">
				  <pg:list requestKey="orgjoblist">
					<option value="<pg:cell colName="jobId"/>"
					<%
						String cjob = dataSet.getString(Integer.parseInt(rowid),"jobId");
		        		if(cjob.equals(jobId))
		        		{
		        			out.println("selected");							        			
		        		}
					%>
					><pg:cell colName="jobName"/></option>
				  </pg:list>			
	 </select>	</td>				 
	 <td class="detailcontent">
	  <select calss="select" name="userList"  multiple style="width:80%"  size="18">
				<pg:list requestKey="userList"><option value="<pg:cell colName="userId"/>"> <pg:cell colName="userName" /></pg:list>			
	</select>  </tr>
  <tr>
    <td colspan="5" align="center" class="detailcontent">
	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input name="upjob" type="button" class="input" value="up" onClick="up1()">
        <input name="upalljob" type="button" class="input" value="upall" onClick="upall()">
        <input name="downjob" type="button" class="input" value="DOWN1" onClick="down1()">
        <input name="downalljob" type="button" class="input" value="downall" onClick="downall()">
        <input name="delu" type="button" class="input" value="删除选中用户" onClick="deleteUser()"></td> 
  </tr>
</table>
<pg:beaninfo requestKey="orgjobForm">
<input type="hidden"  name="orgId" value="<pg:cell colName="orgId"  defaultValue=""/>"/>
</pg:beaninfo>
</form>
</center>

</body>
</html>

