<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,com.frameworkset.platform.config.*" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%@ page import="java.util.*" %>
<%	
	//Integer userId = (Integer) session.getAttribute("currUserId");
	String uid = request.getParameter("userId");
	String orgId = request.getParameter("orgId");
	UserManager userManager = SecurityDatabase.getUserManager();
	User user = userManager.getUserById(uid);
	String userName = user.getUserRealname();
	AccessControl accessControl = AccessControl.getInstance();
    accessControl.checkAccess(pageContext);
    
    String currentlyUserId =  accessControl.getUserID();
    
    Map roleTypeMap = (Map)request.getAttribute("roleTypeMap");
    
    //RoleManager roleManager = SecurityDatabase.getRoleManager();
	//List orgJob = null;
	//if(uid != null && roleId != null && !uid.equals("") && !roleId.equals("")){
		//orgJob = roleManager.getOrgJobList(uid, orgId);
	//}
	
	
	//用户有权限的角色ID--gao.tang 2008.01.28
    List list = new ArrayList();
    list = (List)request.getAttribute("allRole");
    String popedomRoleIds = "";
    Role popedomRole = new Role();
    for(int i = 0; list != null && i < list.size(); i++){
    	popedomRole = (Role)list.get(i);
    	if("".equals(popedomRoleIds)){
    		popedomRoleIds = popedomRole.getRoleId();
    	}else{
    		popedomRoleIds += "," + popedomRole.getRoleId();
    	}
    }
%>
<html>
<head>    
<title>用户【<%=userName%>】角色授予</title>
<!--script language="JavaScript" src="../scripts/ajax.js" type="text/javascript"></script-->  
<SCRIPT LANGUAGE="JavaScript"> 
function send_request(url){
	document.all.divProcessing.style.display = "block";
	document.OrgJobForm.action = url;
	document.OrgJobForm.target = "hiddenFrame";
	document.OrgJobForm.submit();
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
function addRole(flag){	
	if(flag == 1){
	      var n=document.all("roleId").options.length-1;
	      var orgId = document.all("orgId").value;
	      var roleIds = "";	   	 	
	      for(var i=0;i<document.all("allist").options.length;i++){
	          var op=document.all("allist").options[i];
	          if(op.selected){
	              addone(op.text,op.value,n);
	              if(roleIds=="") roleIds = roleIds + op.value;
	              else roleIds = roleIds + "," + op.value;
	          }
	      }	  	       
	      if(roleIds!="")
    	      send_request('saveUserRole.jsp?userId=<%=uid%>&orgId='+orgId+'&roleId='+roleIds+'&op=add');    
	  }else{
   		alert("对不起，您没有权限，请联系系统管理员");   		
   	  } 
   	  
}
function addall(flag){
	if(flag == 1){
		var n=document.all("roleId").options.length-1;
		var p=document.all("allist").options.length-1;		
		var roleIds = "";	 
		var orgId = document.all("orgId").value;  	   
	    for(var i=0;i<document.all("allist").options.length;i++){
	        var op=document.all("allist").options[i];
	        addone(op.text,op.value,n);  
	        if(roleIds=="") roleIds = roleIds + op.value;
	        else roleIds = roleIds + "," + op.value;
	   } 
	   if(roleIds!="")
    	   send_request('saveUserRole.jsp?userId=<%=uid%>&orgId='+orgId+'&roleId='+roleIds+'&op=add'); 
    }
    else{
   		alert("对不起，您没有权限，请联系系统管理员");   		
   	}
}

function deleterole(flag){
	if(flag == 1){
	    var roleIds = "";	 
	    var orgId = document.all("orgId").value;
	    
		for (var m=document.all("roleId").options.length-1;m>=0;m--){
		    if(document.all("roleId").options[m].selected){
		        var op = document.all("roleId").options[m];
			    if(op.value=="3"){
		    		alert("部门管理员角色"+op.text+"不能删除！");
		    		return;
		    	}
		    	if(op.value=="4"){
		    		alert("部门管理员角色模板角色"+op.text+"不能删除！");
		    		return;
		    	}
		    	if(op.value=="1" && "<%=uid%>"=="1"){
		    		alert("系统管理员角色"+op.text+"不能删除！");
		    		return;
		    	}
		    	if("<%=uid%>"=="<%=currentlyUserId%>" && "<%=uid%>" != "1"){
		    		alert("警告：不能删除自己的角色！");
		    		return;
		    	}
	            if(roleIds=="") roleIds = roleIds + op.value;
	            else roleIds = roleIds + "," + op.value;
	            //document.all("roleId").options[m]=null;
	        }
	 	}
	    if(roleIds!="")
    	   send_request('saveUserRole.jsp?userId=<%=uid%>&orgId='+orgId+'&roleId='+roleIds+'&op=delete');     
   	}
   	else{
   		alert("对不起，您没有权限，请联系系统管理员");   		
   	}
}
function deleteall(flag){
	if(flag == 1){
	    var roleIds = "";
	    var orgId = document.all("orgId").value;	
	    
		for (var m=document.all("roleId").options.length-1;m>=0;m--){
		    var op = document.all("roleId").options[m];
		    if(op.value=="3"){
	    		alert("部门管理员角色"+op.text+"不能删除！");
	    		return;
	    	}
	    	if(op.value=="1" && "<%=uid%>"=="1"){
		    	alert("系统管理员角色"+op.text+"不能删除！");
		    	return;
		    }
		    if("<%=uid%>"=="<%=currentlyUserId%>" && "<%=uid%>" != "1"){
	    		alert("警告：不能删除自己的角色！");
	    		return;
	    	}
		    if(roleIds=="") roleIds = roleIds + op.value;
	        else roleIds = roleIds + "," + op.value;
	        //document.all("roleId").options[m]=null;
	    }
	    if(roleIds!="")
    	   send_request('saveUserRole.jsp?userId=<%=uid%>&orgId='+orgId+'&roleId='+roleIds+'&op=delete');
	}else{
   		alert("对不起，您没有权限，请联系系统管理员");   		
   	}
   	
}

function changebox(){				 
	var len=document.all("roleId").options.length;			  	 	
    var roleId=new Array(len);
    var orgId = document.all("orgId").value;
    for (var i=0;i<len;i++){	      
        roleId[i]=document.all("roleId").options[i].value;
    }           		
	
}

function roleChangebox(){
	var tablesFrame= document.getElementsByName("orgjoblist");
	//var roleIdValue = document.all.jobrolelist.value;
	var roleIdValue = document.all("jobrolelist").value;
	tablesFrame[0].src = "org_job.jsp?userId=<%=uid%>&roleId=" + roleIdValue;
}

//关闭窗口 gao.tang 2007.11.16
function closed(){
	parent.window.close();
	parent.window.returnValue="ok";
}
</SCRIPT>
<script language="javascript" src="<%=request.getContextPath()%>/include/dragdiv.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/include/querySelect.js"></script>
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/sysmanager/css/contentpage.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/sysmanager/css/tab.winclassic.css">
<body class="contentbodymargin" scroll="yes" onload="loadDragDiv()" >
<center>
<form name="OrgJobForm" action="" target="hiddenFrame" method="post" >
<input name="userId" value="<%=uid%>" type="hidden">
<input name="popedomRoleIds" value="<%=popedomRoleIds%>" type="hidden">


<table width="80%" border="0" cellpadding="0" cellspacing="1">

<tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <%
  RoleManager roleManager = SecurityDatabase.getRoleManager();
  String everyoneRolename = AccessControl.getEveryonegrantedRoleName();
  Role role = roleManager.getRoleByName(everyoneRolename);
  if(role != null)
  {
  %>
  <tr class="tabletop" cols="3">
    <td colspan="3"><font color="red">
	提示：用户默认授予（<%=everyoneRolename%>）角色</font></td>
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <%
  }
  %>
  <tr class="tabletop">
    <td width="40%" align="left">可选角色</td>
    <td width="20%" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
    <td width="40%" align="center"><%=user.getUserRealname()%>(<%=user.getUserName()%>)已授予角色</td>
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
     <td  align="left" ><div class="win" id="dd_1" align="left" title="下拉选择框的右边可以左右拖动" >
     <select name="allist"  multiple style="width:98%;overflow:auto" onDBLclick="addRole(1)" size="15" title="下拉选择框的右边可以左右拖动" >
		  <pg:list requestKey="allRole">
		  <% 
		  	String roleType = dataSet.getString("roleType");
		  %>
			<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/> (<%=roleTypeMap.get(roleType) %>)</option>
		  </pg:list>			
	</select></div>
	</td>				  
		  	
    <td align="center"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center"><input name="button1" type="button" class="input" value="&gt;" onclick="addRole(1)"> </td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><input name="button2" type="button" class="input" value="&gt;&gt;" onclick="addall(1)"> </td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><input name="button3" type="button" class="input" value="&lt;&lt;" onclick="deleteall(1)"> </td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><input name="button4" type="button" class="input" value="&lt;" onclick="deleterole(1)"> </td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
    </table></td>
    <td align="right"><div class="win" id="dd_2"  title="下拉选择框的右边可以左右拖动">
     <select name="roleId"  multiple style="width:98%" onDBLclick="deleterole(1)" size="15" title="下拉选择框的右边可以左右拖动">
				  <pg:list requestKey="existRole">
				  <% 
				  	String existRoleType = dataSet.getString("roleType");
				  %>
					<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/> (<%=roleTypeMap.get(existRoleType) %>)</option>			
				  </pg:list>			
	 </select></div>
	 	 						
	</td>				 
				  
  </tr>
  
  <tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
 
  <tr class="tabletop" >
  
    <td width="40%" align="left"> 
    <%if(ConfigManager.getInstance().getConfigBooleanValue("enableorgrole",true))
  {
  %>机构已授予该用户角色<%}%></td>
  
    <td width="20%" align="center">&nbsp;</td>
    
    <td width="40%" align="center"> 
    <%if(ConfigManager.getInstance().getConfigBooleanValue("enablergrouprole",true))
  {
  %>用户组已授予该用户角色<%}%></td>
    
  </tr>
  <tr >
     <td  align="right" ><%if(ConfigManager.getInstance().getConfigBooleanValue("enableorgrole",true))
  {
  %><div class="win" id="dd_3"  align="left" title="下拉选择框的右边可以左右拖动">
     <select name="orgrolelist"  multiple style="width:98%" size="15" disabled="true" title="下拉选择框的右边可以左右拖动">   
				  <pg:list requestKey="orgRole">
					<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/></option>
				  </pg:list>			
	</select></div>
	<%}%>
	</td>		
	<td align="center">&nbsp;</td>
	 <td  align="left" ><%if(ConfigManager.getInstance().getConfigBooleanValue("enablergrouprole",true))
  {
  %><div class="win" id="dd_4" align="left" title="下拉选择框的右边可以左右拖动">
     <select name="grouprolelist"  multiple style="width:98%" size="15" disabled="true" title="下拉选择框的右边可以左右拖动">   
				  <pg:list requestKey="groupRole">
					<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/></option>
				  </pg:list>			
	</select></div>
	<%}%>
	</td>
 </tr>	
 <tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
 <tr>
 	<td align="left">用户岗位对应的角色</td>
 	<td align="center">-----------------></td>
 	<td align="right">用户岗位角色所隶属的<font color="#CC9900">机构</font>-<font color="#996600">岗位</font></td>
 </tr>
 <tr>
 <td align="left"><div class="win" id="dd_5" align="left" title="下拉选择框的右边可以左右拖动">
	<select name="jobrolelist" style="width:98%" size="15" onchange="roleChangebox()" title="下拉选择框的右边可以左右拖动">
				  <pg:list requestKey="jobRole">
				  <% 
				  	String jobRoleType = dataSet.getString("roleType");
				  %>
				  	<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/> (<%=roleTypeMap.get(jobRoleType) %>)</option>
				  </pg:list>
	</select></div>
 </td>
 <td>&nbsp;</td>
 <td align="right">
 	<div class="win" id="dd_6" align="left" title="下拉选择框的右边可以左右拖动">
 	<select name="userjobList" multiple style="width:98%" size="15" title="下拉选择框的右边可以左右拖动">
		
	</select>
	</div>
 </td>
 </tr>
 
 <tr>
 	<td align="center" colspan="4">
 		<input name="but4" type="button" class="input" value="关闭" onclick="closed();"/>
 	</td>
 </tr>
  
</table>
<pg:beaninfo requestKey="userRoleForm">
<input type="hidden"  name="orgId" value="<pg:cell colName="orgId"  defaultValue=""/>"/>
<input type="hidden"  name="uid" value="<pg:cell colName="userId"  defaultValue=""/>"/>
</pg:beaninfo>
</form>
</center>
</div>
<div id=divProcessing style="width:200px;height:25px;position:absolute;left:245px;top:298px;display:none">
	<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
	    <tr>
		    <td bgcolor=#3A6EA5>
			    <marquee align="middle" behavior="alternate" scrollamount="5">
				    <font color=#FFFFFF>...处理中...请等待...</font>
				</marquee>
			</td>
		</tr>
	</table>
</body>
<iframe name="orgjoblist" src="org_job.jsp?" width=0 height=0></iframe>
<iframe name="hiddenFrame" width=0 height=0></iframe>
</html>


