<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page
	import="com.frameworkset.platform.security.AccessControl,
	com.frameworkset.platform.sysmgrcore.entity.Job,
	com.frameworkset.platform.sysmgrcore.manager.JobManager,
	com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
	com.frameworkset.platform.sysmgrcore.manager.UserManager,java.util.*"%>
<%	
	String jobId = (String)request.getParameter("jobId");
	JobManager jm = SecurityDatabase.getJobManager();
	Job job = jm.getJobById(jobId);
	String jobName = job.getJobName();
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
  
<SCRIPT LANGUAGE="JavaScript"> 
function addOrg(){	
      var n=document.all("orgId").options.length-1;
   	 	
   for(var i=0;i<document.all("allist").options.length;i++){
   var op=document.all("allist").options[i];
   if(op.selected)
   addone(op.text,op.value,n);
  }
  changebox();
}
function addone(name,value,n){

   for(var i=n;i>=0;i--){
		if(value==document.all("orgId").options[i].value){
		  return;
		}
	}
   var op=new Option(name,value);
   document.all("orgId").add(op);
   
}
function deleteall(){
	for (var m=document.all("orgId").options.length-1;m>=0;m--)
    document.all("orgId").options[m]=null
    changebox();
}
      
function addall(){
	var n=document.all("orgId").options.length-1;
	var p=document.all("allist").options.length-1;		  
     for(var i=0;i<document.all("allist").options.length;i++){
     var op=document.all("allist").options[i];
     addone(op.text,op.value,n);  
   }
   changebox();
}
function deleteorg(){
 for (var m=document.all("orgId").options.length-1;m>=0;m--)
	      if(document.all("orgId").options[m].selected)
         document.all("orgId").options[m]=null;
         changebox();
}
function okadd(){
	var len=document.all("orgId").options.length;
	for (var i=0;i<len;i++){	      
         document.all("orgId").options[i].selected="true";
         }
         
         document.forms[0].action="../accessmanager/roleorg.do?method=storeRoleOrg";
         document.forms[0].submit();
}
//---------------------------------------
var http_request = false;
function send_request(url){
				http_request = false;
				//开始初始化XMLHttpRequest对象
				if(window.XMLHttpRequest){//Mozilla
					http_request = new XMLHttpRequest();
					if(http_request.overrideMimeType){//设置MIME类别
						http_request.overrideMimeType("text/xml");						
					}
				}
				else if(window.ActiveXObject){//IE
					try{
						http_request = new ActiveXObject("Msxml2.XMLHTTP");
					}catch(e){
						try{
							http_request = new ActiveXObject("Microsoft.XMLHTTP");							
						}catch(e){
						}
					}
				}
				if(!http_request){
					alert("不能创建XMLHttpRequest对象");
					return false;
				}
				http_request.onreadystatechange = processRequest;
				http_request.open("GET",url,true);
				http_request.send(null);
			}
			
			function processRequest(){
				if(http_request.readyState == 4){
					//alert(http_request.status);
					if(http_request.status == 200){
						//alert(http_request.responseText);
					}
					else{
						alert("对不起，服务器错误");
					}
				}
			}
			
			function changebox(){				 
			   	 var len=document.all("orgId").options.length;			  	 	
        		 //var roleId = document.all("roleId").value;
        		 var jobId="<%=jobId%>";
        	
        		 var orgId=new Array(len)
        		 for (var i=0;i<len;i++){	      
        		   orgId[i]=document.all("orgId").options[i].value;
         		 }   
         		
				 send_request('<%=rootpath%>/sysmanager/jobmanager/A03/savaJobOrg.jsp?jobId='+jobId+'&orgId='+orgId);
			}
</SCRIPT>
  <%@ include file="/include/css.jsp"%>
 
<body class="contentbodymargin">
<div id="contentborder">
<center>
<form name="RoleOrgForm" action="" method="post" >
<table width="80%" border="0" cellpadding="0" cellspacing="1">
<tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">可选机构</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">(<%=jobName%>)已授予的机构</td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr >
     <td  align="right">
     <select name="allist"  multiple style="width:80%" onDBLclick="addOrg()" size="18">
		  <pg:list requestKey="allOrg">
			<option value="<pg:cell colName="orgId"/>">
			<pg:null colName="remark5"><pg:cell colName="remark5"/></pg:null>
			<pg:notnull colName="remark5">
				<pg:equal colName="remark5" value=""><pg:cell colName="remark5"/></pg:equal>
				<pg:notequal colName="remark5" value=""><pg:cell colName="remark5"/></pg:notequal>
			</pg:notnull>
			</option>
		  </pg:list>			
	</select>
	</td>				  
		  	
    <td align="center"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center"><input name="button1" type="button" class="input" value="&gt;" onclick="addOrg()"></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><input name="button2" type="button" class="input" value="&gt;&gt;" onclick="addall()"></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><input name="button3" type="button" class="input" value="&lt;&lt;" onclick="deleteall()"></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><input name="button4" type="button" class="input" value="&lt;" onclick="deleteorg()"></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
    </table></td>
    <td >
     <select name="orgId"  multiple style="width:80%" onDBLclick="deleteorg()" size="18">
				  <pg:list requestKey="existOrg">
					
					<option value="<pg:cell colName="orgId"/>">
					<pg:null colName="remark5"><pg:cell colName="remark5"/></pg:null>
					<pg:notnull colName="remark5">
					<pg:equal colName="remark5" value=""><pg:cell colName="remark5"/></pg:equal>
					<pg:notequal colName="remark5" value=""><pg:cell colName="remark5"/></pg:notequal>
					</pg:notnull>
					</option>
				  </pg:list>			
	 </select>				
					
	</td>				 
				  
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    
  </tr>
  <tr><td colspan="3" align="center">
  
  
  </td> </tr>
</table>

<input type="hidden" name="jobId" value="<%=jobId%>"/>

</form>
</center>
</div>
</body>
</html>
