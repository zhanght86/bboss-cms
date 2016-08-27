<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
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
   var op=new Option(name,value+";1");
   document.all("jobId").add(op);
   
}
function deleteall(){
	if(window.confirm("你确定要删除岗位吗？(删除后将影响该岗位下的所有用户!)")){
		for (var m=document.all("jobId").options.length-1;m>=0;m--)
    		document.all("jobId").options[m]=null;
    	for (var n=parent.frames[1].document.all("userList").options.length-1;n>=0;n--)
    		parent.frames[1].document.all("userList").options[n]=null;
    	
	}
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
	if(window.confirm("你确定要删除岗位吗？(删除后将影响该岗位下的所有用户！)")){
 		for (var m=document.all("jobId").options.length-1;m>=0;m--)
	    	if(document.all("jobId").options[m].selected){
         		document.all("jobId").options[m]=null;
         	}
         	for (var n=parent.frames[1].document.all("userList").options.length-1;n>=0;n--)
    			parent.frames[1].document.all("userList").options[n]=null;
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
   				var dest = document.all("jobId").options[i-1];
   				document.all("jobId").options[i-1] = tmp;
   				document.all("jobId").options[i] = dest;
   				document.all("jobId").options[i-1].selected=true;
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
   		}
    }
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
   				document.all("jobId").options[i+1] = tmp;
   				document.all("jobId").options[i] = dest;   				
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
		parent.frames[1].location.href="<%=rootpath%>/orgmanager/orgoperjob.do?method=getUserList&curjobid="+curjobId+"&orgId="+orgId+"&jobSn="+jobSn;
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
<form name="OrgJobForm" action="" method="post" >

<table width="100%" border="0" cellpadding="1" cellspacing="1">
  <tr >
    <!--<td width="40%" align="center" class="detailtitle">可选岗位</td>-->
    <td width="40%" align="center" >可选岗位</td>
    <td width="20%" align="center" >&nbsp;</td>
    <td width="40%" align="center" >已授予岗位</td>
    <td width="20%" align="center" >&nbsp;</td>
  </tr>
  <tr >
     <td class="detailcontent" align="center">
     <select name="allist"  multiple style="width:80%" onDBLclick="addjob()" size="18">
				  <pg:list sessionKey="joblist">
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
    <td class="detailcontent">
     <select name="jobId"  multiple style="width:80%" onChange="showUserList()" onDBLclick="deletejob()" size="18">
	  <pg:list sessionKey="orgjoblist">
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
        <td align="center"><input name="button2" type="button" class="input" value="上一级" onClick="up1()"></td>
      </tr>
      <tr>
        <td align="center" class="detailcontent">&nbsp;</td>
      </tr>
      <tr>
        <td align="center" class="detailcontent"><input name="button3" type="button" class="input" value="下一级" onClick="down1()"></td>
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
	
	 
				  
  </tr>
  <tr><td height="50" colspan="3" align="center" class="detailcontent">
    
    <p>
      <input name="add" type="button" class="input" value="保存设置" onClick="okadd()" >
      
    </p></td> </tr>
</table>
<pg:beaninfo sessionKey="orgjobForm">
<input type="hidden"  name="orgId" value="<pg:cell colName="orgId"  defaultValue=""/>"/>
</pg:beaninfo>
</form>
<%@ include file="../sysMsg.jsp"%>
</body>
</html>

