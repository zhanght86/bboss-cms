<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*
				,com.frameworkset.platform.security.AccessControl
				,java.util.List
				,java.util.ArrayList,
				com.frameworkset.common.poolman.DBUtil,
				com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl
				" %>
<%	
	 AccessControl accessControl = AccessControl.getInstance();
     accessControl.checkAccess(pageContext);
     String[] id =(String[])request.getAttribute("id");
     String orgId = request.getParameter("orgId");
     UserManager userManager = SecurityDatabase.getUserManager();
	 OrgManagerImpl orgImpl = new OrgManagerImpl();	
     String idStr = "";
     String usern = "";
     String userna = "";
     for(int i = 0;i < id.length; i++)
     {
     	idStr += id[i]+",";
     	User user = userManager.getUserById(id[i]);
     	usern= user.getUserRealname();
     	//System.out.println(usern);
     	userna += usern + ",";
     }
     if(idStr.length() > 1)
     idStr = idStr.substring(0,idStr.length()-1);
     if(userna.length() > 1)
     userna = userna.substring(0,userna.length()-1);
     //System.out.println(userna);
     
     List allJob = (List)request.getAttribute("allJob"); 
     
     //权限过滤， 能进行岗位授予的岗位必须是：当前登录用户（包括部门管理员）必须对这些岗位有机构设置的权限。
     List myAllJob = new ArrayList();
     if(allJob != null)
     {
    	for(int i=0;i<allJob.size();i++)
    	{
    		Job job = (Job)allJob.get(i);
    		if(accessControl.checkPermission(job.getJobId(), "orgset", AccessControl.JOB_RESOURCE))
    		{
    			myAllJob.add(job);
    		}
    	}
     }
 	 request.setAttribute("myAllJob", myAllJob);
 	
 	 //管理员与自己不能授予角色
     DBUtil db = new DBUtil();
    
     boolean tag = false;
     boolean tagSelf = false;
     String adminUsers = "以下用户不能进行岗位授予:\\n";
     for(int j=0;j<id.length; j++){
     	String sqlUser ="select count(*) from td_sm_userrole where user_id ="+ id[j] +" and role_id='1'";
		db.executeSelect(sqlUser);
		User adminUser = userManager.getUserById(id[j]);
		
		if(db.getInt(0,0)>0){//有管理员角色		    
			tag = true;						
			adminUsers += adminUser.getUserName() + " 是超级管理员\\n";
		}
		
		//没有管理员角色, 但是给自己授权
		if(accessControl.getUserID().equals(id[j])){
			tag = true;
			adminUsers += adminUser.getUserName() + " 不能给自己授予角色\\n";
		}
		
		 //是部门管理员, 也不允许授权
	     List managerOrgs = orgImpl.getUserManageOrgs(id[j]);
	     if(managerOrgs.size()>0){
	         tag = true;
		     adminUsers += adminUser.getUserName() + " 是部门管理员\\n";
	     }
     }
     
     if(tag)
     {
     	%>
     	<SCRIPT LANGUAGE="JavaScript">
		alert("<%=adminUsers%>"); 
		getPropertiesContent().location.href="../user/userManager.do?method=getUserList&orgId=<%=orgId%>";
		</script>
		<%
     }
%>
<html>
<head>    
  <title>属性容器</title>
<script language="JavaScript" src="../scripts/ajax.js" type="text/javascript"></script>  
<SCRIPT LANGUAGE="JavaScript">
var id = "<%=idStr%>";   
function addRole(){	
	   var n=document.all("roleId").options.length-1;
	   	 	
	   for(var i=0;i<document.all("allist").options.length;i++)
	   {
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
	var length=document.all("roleId").options.length;
	var i = 0;
	var orgId = <%=orgId%>;
	var roleDelId=new Array(length);
				
	for (var m=length-1;m>=0;m--)
	{
	      roleDelId[i++]=document.all("roleId").options[m].value;
          document.all("roleId").options[m]=null;
    } 
  	send_request('delUserJobAlot.jsp?roleDelId='+roleDelId+'&id='+id+'&orgId='+orgId);    
	
}
      
function addall(flag){
		var n=document.all("roleId").options.length-1;
		var p=document.all("allist").options.length-1;		  
	     for(var i=0;i<document.all("allist").options.length;i++){
	     var op=document.all("allist").options[i];
	     addone(op.text,op.value,n);  
	   }
	    changebox();  
}
function deleterole(){
	var leng=document.all("roleId").options.length;
	var i = 0;
	var orgId = <%=orgId%>;
	var roleDelId=new Array();
		for (var m=leng-1;m>=0;m--){
		    if(document.all("roleId").options[m].selected)
		    {
		    roleDelId[i++]=document.all("roleId").options[m].value;
	        document.all("roleId").options[m]=null;
	        }
	 	}
	 	//alert("liumeiyu" + roleDelId[0]);
	 	send_request('delUserJobAlot.jsp?roleDelId='+roleDelId+'&id='+id+'&orgId='+orgId); 
}


function changebox(){				 
	var len=document.all("roleId").options.length;			  	 	
    var roleId=new Array(len);
    var orgId = <%=orgId%>;
    for (var i=0;i<len;i++){	      
        roleId[i]=document.all("roleId").options[i].value;
    }  
          		
	send_request('saveUserJobAlot.jsp?roleId='+roleId+'&id='+id+'&orgId='+orgId);
}
</SCRIPT>
  <%@ include file="/include/css.jsp"%>
 
<body class="contentbodymargin" scroll="no">
<div id="contentborder">
<center>
<form name="AlotUserRoleForm" action="" method="post" >
<table width="80%" border="0" cellpadding="0" cellspacing="1">
<tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">可选岗位</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">给：<%=userna%>授予岗位</td>
  </tr>
  
  <tr >
     <td  align="right" >
     <select name="allist"  multiple style="width:80%" 
     
                 onDBLclick="addRole(1)"
     size="15">
     
				  <pg:list requestKey="myAllJob">
					<option value="<pg:cell colName="jobId"/>"><pg:cell colName="jobName"/></option>
				  </pg:list>			
	</select>
	</td>				  
		  	
    <td align="center"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center"><input name="button1" type="button" class="input" value="&gt;" onclick="addRole()">              
            </td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><input name="button2" type="button" class="input" value="&gt;&gt;" 
			 onclick="addall()">  </td>
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
        <td align="center"><input name="button4" type="button" class="input" value="&lt;" onclick="deleterole()">  
</td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
    </table></td>
    <td >
     <select name="roleId"  multiple style="width:80%" onDBLclick="deleterole()"  size="15">
	 </select>
	 	 						
	</td>				 
	<tr>
        <td align="center">&nbsp;</td>
      </tr>
      		  
  <tr>
    <td align="center">&nbsp;</td>	
     <td align="center">&nbsp;</td>						
     <td>
		<div align="center">
			<input name="button32" type="button" class="input" value="返回" onclick="javaScript:history.back()">
		</div>
	</td>							
  </tr>

 </table>
</form>
</center>
</div>
</body>
</html>

