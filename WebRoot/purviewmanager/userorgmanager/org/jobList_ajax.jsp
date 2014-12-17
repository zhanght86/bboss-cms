<%
/**
 * <p>Title: 机构岗位下的角色设置</p>
 * <p>Description: 机构岗位下的角色设置</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-18
 * @author da.wei
 * @version 1.0
 **/
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page
	import="com.frameworkset.platform.security.AccessControl,
	com.frameworkset.platform.sysmgrcore.entity.Job,
	com.frameworkset.platform.sysmgrcore.entity.Role,
	com.frameworkset.platform.sysmgrcore.manager.JobManager,
	com.frameworkset.platform.sysmgrcore.entity.Organization,
	com.frameworkset.platform.sysmgrcore.manager.OrgManager,
	com.frameworkset.platform.sysmgrcore.manager.RoleManager,
	com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
	java.util.*"%>
	
<%
	
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>
	
	
	
<%	

	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	
	String jobId = request.getParameter("jobId");
	String orgId = request.getParameter("orgId");
	
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
		
		request.setAttribute("orgName", orgName);
		request.setAttribute("jobName", jobName);
	}
	
	
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	//List allRole = roleManager.getJobOrgAllRoleList(accessControl,jobId, orgId);
	
	
	//只显示用户本身创建的角色和授予该用户的角色
	List rolelist = roleManager.getRoleList() ;
	List allRole = new ArrayList();
	if(rolelist != null)
	{
		for(int i=0; i<rolelist.size(); i++)
		{
			Role role = (Role) rolelist.get(i) ;
			//超级管理员不能将默认的角色授予岗位
			if(accessControl.isAdmin())
			{
				String roleId = role.getRoleId() ;
				if(!roleId.equals("1") && !roleId.equals("2")
					&& !roleId.equals("3") && !roleId.equals("4"))
					{
						allRole.add(role);	
					}
			}
			else
			{
				if(accessControl.checkPermission(role.getRoleId(),"roleset",AccessControl.ROLE_RESOURCE))
				{
					allRole.add(role);	
				}
			}
		}
	}
	
	//不能删除没有授予权限的角色
	List existRole = roleManager.getJobListByRoleJob(jobId, orgId);
	
	
	
	//List allRole = roleManager.userHasPermissionRole(orgId,accessControl);
	//if(existRole != null)
		//System.out.println("existRole succeed!");
	request.setAttribute("allRole", allRole);
	request.setAttribute("existRole", existRole);
%>
<html>
<head>    
  <title>岗位【<%=jobName%>】的角色设置</title>
  
<SCRIPT LANGUAGE="JavaScript"> 
function addRole(){	
      var m=0;
      
      var n=document.all("roleId").options.length-1;
   	 	
   for(var i=0;i<document.all("allist").options.length;i++){
   var op=document.all("allist").options[i];
   if(op.selected){
	   addone(op.text,op.value,n);
	   m++
     }
     
  }
  if(m<1){
  $.dialog.alert("<pg:message code='sany.pdp.common.operation.select.one'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
  return false;
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
function deleteall()
{
	//for (var m=document.all("roleId").options.length-1;m>=0;m--)
	//{
    	//document.all("roleId").options[m]=null
    //}
    //changebox();
    //授权的个数 只能删除所授权的角色的
	 var havePurviewRoleCount = document.all("allist").options.length-1 ;
	 
	 var roleIds = "";
	 
	 var noPurviewRoles = "";
	 
	 for (var m=document.all("roleId").options.length-1;m>=0;m--)
	 {
		var option = document.all("roleId").options[m];
	   
    	var flag1 = false;
    	for(var n=havePurviewRoleCount; n>=0; n--)
    	{
    		if(document.all("allist").options[n].value==option.value)
    		{
		      	roleId = option.value;
		      	if(roleIds == "")
		      	{
		      		roleIds = roleId;
		      	}
		      	else
		      	{
		      		roleIds = roleIds + "," + roleId;
		      	}
		        document.all("roleId").options[m]=null;
		        flag1 = true;
		    }
        }
        if(!flag1)
        {
        	if(noPurviewRoles == "")
        	{
	    		noPurviewRoles = "<pg:message code='sany.pdp.jobmanage.no.delete.authoration.for.role'/>"+"：\n" + option.text;
	    	}
	    	else
	    	{
	    		noPurviewRoles += "\n" + option.text;
	    	}
        }
	}
	 
	if(roleIds != "")
	{
		changebox();	
	}
	 else
	 {
	    if(noPurviewRoles != "")
	    {
	    	$.dialog.alert(noPurviewRoles,function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	    }
	    else
	    {
	    	//alert("至少选择一个角色!");
    		return ;
    	}
	}   
    
    
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
function deleterole()
{
	 
	 //授权的个数 只能删除所授权的角色
	 var havePurviewRoleCount = document.all("allist").options.length-1 ;
	 
	 var roleIds = "";
	 
	 var noPurviewRoles = "";
	 
	 for (var m=document.all("roleId").options.length-1;m>=0;m--)
	 {
		var option = document.all("roleId").options[m];
	    if(document.all("roleId").options[m].selected)
	    {
	    	var flag1 = false;
	    	for(var n=havePurviewRoleCount; n>=0; n--)
	    	{
	    		if(document.all("allist").options[n].value==option.value)
	    		{
			      	roleId = option.value;
			      	if(roleIds == "")
			      	{
			      		roleIds = roleId;
			      	}
			      	else
			      	{
			      		roleIds = roleIds + "," + roleId;
			      	}
			        document.all("roleId").options[m]=null;
			        flag1 = true;
			    }
	        }
	        if(!flag1)
	        {
	        	if(noPurviewRoles == "")
	        	{
		    		noPurviewRoles = "<pg:message code='sany.pdp.jobmanage.no.delete.authoration.for.role'/>"+"：\n" + option.text;
		    	}
		    	else
		    	{
		    		noPurviewRoles += "\n" + option.text;
		    	}
	        }
	    }
	}
	 
	 if(roleIds != "")
	 {
		changebox();	
	 }
	 else
	 {
	    if(noPurviewRoles != "")
	    {
	    	$.dialog.alert(noPurviewRoles,function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	    }
	    else
	    {
	    	$.dialog.alert("<pg:message code='sany.pdp.common.operation.select.one'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
    		return ;
    	}
	}     
}

//---------------------------------------
var http_request = false;
			function send_request(url){				
				document.RoleOrgForm.action = url;
				document.RoleOrgForm.target = "hiddenFrame";
				document.RoleOrgForm.submit();
			}
			
			function processRequest(){
				if(http_request.readyState == 4){
					//alert(http_request.status);
					if(http_request.status == 200){
						//alert(http_request.responseText);
					}
					else{
						$.dialog.alert("<pg:message code='sany.pdp.server.error'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
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
         		
				 send_request('saveJobRole.jsp?jobId='+jobId+'&roleId='+roleId+'&orgId='+orgId);
				
			}
</SCRIPT>
<script language="javascript" src="../../scripts/dragdiv.js"></script>
<body class="contentbodymargin" scroll="no" onload="loadDragDiv();">
<div id="">
<center>
<form name="RoleOrgForm" action="" method="post" >
<table width="80%" border="0" cellpadding="0" cellspacing="1">
<tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center"><pg:message code="sany.pdp.userorgmanager.user.job.role.can.set"/></td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center"><pg:message code="sany.pdp.userorgmanager.user.org.jobs" arguments="${orgName},${jobName}"/><br/><pg:message code="sany.pdp.userorgmanager.user.job.role.can.had"/></td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr >
     <td  align="right"><div class="win" id="dd_1" align="left">
     <select name="allist"  multiple style="width:98%" onDBLclick="addRole()" size="18">
		  <pg:list requestKey="allRole">
		  <%
		  	
		  %>
			<option value="<pg:cell colName="roleId"/>">
				<pg:cell colName="roleName"/>
			</option>
		  </pg:list>			
	</select></div>
	</td>				  
		  	
    <td align="center"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center"><a class="bt_2"  onclick="addRole()"><span>&gt;</span></a></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><a class="bt_2" onclick="addall()"><span>&gt;&gt;</span></a></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><a class="bt_2" onclick="deleteall()"><span>&lt;&lt;</span></a></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><a class="bt_2" onclick="deleterole()"><span>&lt;</span></a></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
    </table></td>
    <td ><div class="win" id="dd_2" align="left">
     <select name="roleId"  multiple style="width:98%" onDBLclick="deleterole()" size="18">
				  <pg:list requestKey="existRole">
					<option value="<pg:cell colName="roleId"/>">
						<pg:cell colName="roleName"/>
					</option>
				  </pg:list>			
	 </select>				
	</div>				
	</td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    
  </tr>
  </tr>
</table>

<input type="hidden" name="jobId" value="<%=jobId%>"/>
<input type="hidden" name="orgId" value="<%=orgId%>"/>

</form>
<div id=divProcessing style="width:200px;height:30px;position:absolute;left:300px;top:450px;display:none">
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
</center>
</div>
</body>
<iframe name="hiddenFrame" width=0 height=0></iframe>
</html>
