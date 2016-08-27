<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page
	import="com.frameworkset.platform.security.AccessControl,
	com.frameworkset.platform.sysmgrcore.entity.Job,
	com.frameworkset.platform.sysmgrcore.manager.JobManager,
	com.frameworkset.platform.sysmgrcore.entity.Organization,
	com.frameworkset.platform.sysmgrcore.manager.OrgManager,
	com.frameworkset.platform.sysmgrcore.manager.RoleManager,
	com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
	java.util.*"%>
<%	
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(request, response);
	
	String jobId = request.getParameter("jobId");
	String orgId = request.getParameter("orgId");
	//System.out.println("orgId === " + orgId);
	//System.out.println("jobId === " + jobId);
	JobManager jobManager = SecurityDatabase.getJobManager();
	OrgManager orgManager = SecurityDatabase.getOrgManager();
	String jobName = null;
	String orgName = null;
	Job job = null;
	Organization org = null;
	if(jobId != null && !jobId.equals("")){
		job = jobManager.getJobById(jobId);
		org = orgManager.getOrgById(orgId);
		jobName = job.getJobName();
		orgName = org.getOrgName();
	}
	
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	List existRole = roleManager.getJobListByRoleJob(jobId, orgId);
	//List allRole = roleManager.getJobOrgAllRoleList(accessControl,jobId, orgId);
	List allRole = roleManager.userHasPermissionRole(orgId,accessControl);
	//if(existRole != null)
		//System.out.println("existRole succeed!");
	Map roleTypeMap = new com.frameworkset.platform.sysmgrcore.manager.RoleTypeManager().getRoleTypeMap();
	request.setAttribute("allRole", allRole);
	request.setAttribute("existRole", existRole);
%>
<html>
<head>    
  <title>岗位【<%=jobName%>】的角色设置</title>
  		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
	<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
  
<SCRIPT LANGUAGE="JavaScript"> 
function addRole(){	
      var n=document.all("roleId").options.length-1;
   	 	
   for(var i=0;i<document.all("allist").options.length;i++){
   var op=document.all("allist").options[i];
   if(op.selected)
   addone(op.text,op.value,n);
  }
  changebox();
}
function addone(name,value,n){

   for(var i=n;i>=0;i--){
		if(value==document.all("roleId").options[i].value){
		  return;
		}
	}
   var op=new Option(name,value);
   document.all("roleId").add(op);
   
}
function deleteall(){
	for (var m=document.all("roleId").options.length-1;m>=0;m--)
    document.all("roleId").options[m]=null
    changebox();
}
      
function addall(){
	var n=document.all("roleId").options.length-1;
	var p=document.all("allist").options.length-1;		  
     for(var i=0;i<document.all("allist").options.length;i++){
     var op=document.all("allist").options[i];
     addone(op.text,op.value,n);  
   }
   changebox();
}
function deleterole(){
 for (var m=document.all("roleId").options.length-1;m>=0;m--)
	      if(document.all("roleId").options[m].selected)
         document.all("roleId").options[m]=null;
         changebox();
}

//---------------------------------------
var http_request = false;
function send_request(url){
	alert("caozuo");
				document.all.divProcessing.style.display="block";
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
			   	 var len=document.all("roleId").options.length;			  	 	
        		 //var roleId = document.all("roleId").value;
        		 var jobId="<%=jobId%>";
        		 var orgId="<%=orgId%>"
        		 var roleId=new Array(len)
        		 for (var i=0;i<len;i++){	      
        		   roleId[i]=document.all("roleId").options[i].value;
         		 }   
         		
				 send_request('<%=rootpath%>/sysmanager/jobmanager/A03/saveJobRole.jsp?jobId='+jobId+'&roleId='+roleId+'&orgId='+orgId);
				
			}
</SCRIPT>
  <%@ include file="/include/css.jsp"%>
	<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
	<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
  <script language="javascript" src="<%=request.getContextPath()%>/include/dragdiv.js"></script>
  <script language="javascript" src="<%=request.getContextPath()%>/include/querySelect.js"></script>
<body class="contentbodymargin" scroll="no" onload="loadDragDiv();">
<div id="contentborder">
<center>
<form name="RoleOrgForm" action="" target="hiddenFrame" method="post" >
<table width="80%" border="0" cellpadding="0" cellspacing="1">
<tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="left">可设置的角色</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="right">机构下的岗位(<%=jobName%>)<br/>已设置的角色</td>
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
  <tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr >
     <td  align="left"><div class="win" id="dd_1" align="left" title="下拉选择框的右边可以左右拖动">
     <select name="allist"  multiple style="width:98%" onDBLclick="addRole()" size="18" title="下拉选择框的右边可以左右拖动">
		  <pg:list requestKey="allRole">
		  <%
		  	String roleType = dataSet.getString("roleType");
		  %>
			<option value="<pg:cell colName="roleId"/>">
				<pg:cell colName="roleName"/> (<%=roleTypeMap.get(roleType) %>)
			</option>
		  </pg:list>			
	</select></div>
	</td>				  
		  	
    <td align="center"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center"><input name="button1" type="button" class="input" value="&gt;" onclick="addRole()"></td>
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
        <td align="center"><input name="button4" type="button" class="input" value="&lt;" onclick="deleterole()"></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
    </table></td>
    <td align="right"><div class="win" id="dd_2" align="left" title="下拉选择框的右边可以左右拖动">
     <select name="roleId"  multiple style="width:98%" onDBLclick="deleterole()" size="18" title="下拉选择框的右边可以左右拖动">
				  <pg:list requestKey="existRole">
				  <% 
				  	String exitsRoleType = dataSet.getString("roleType");
				  %>
					<option value="<pg:cell colName="roleId"/>">
						<pg:cell colName="roleName"/> (<%=roleTypeMap.get(exitsRoleType) %>)
					</option>
				  </pg:list>			
	 </select>				
	</div>				
	</td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    
  </tr>
  <tr><td colspan="3" align="center">
  
  
  </td> </tr>
</table>
<br/>
<div align="center">
<input type="button" name="bac" onclick="cos()" class="input" value="返回"/>
</div>
<script language="Javascript">
function cos(){
	window.close();
	window.returnValue='ok';
}
</script>
<input type="hidden" name="jobId" value="<%=jobId%>"/>
<input type="hidden" name="orgId" value="<%=orgId%>"/>
<iframe name="hiddenFrame" width="0" height="0"></iframe>
</form>
</center>
</div>
<div id="divProcessing" style="width:200px;height:30px;position:absolute;left:135px;top:450px;display:none">
	<table border="0" cellpadding="0" cellspacing="1" bgcolor="#000000" width="100%" height="100%">
	    <tr>
		    <td bgcolor="#3A6EA5">
			    <marquee align="middle" behavior="alternate" scrollamount="5">
				    <font color="#FFFFFF">...处理中...请等待...</font>
				</marquee>
			</td>
		</tr>
	</table>
</div>
</body>
</html>
