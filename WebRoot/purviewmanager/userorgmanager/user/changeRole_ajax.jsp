
<%@page import="com.frameworkset.platform.sysmgrcore.manager.RoleTypeManager"%><%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,com.frameworkset.platform.config.*" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%@ page import="java.util.*
				,com.frameworkset.platform.sysmgrcore.web.struts.form.UserRoleManagerForm
				,com.frameworkset.platform.sysmgrcore.manager.GroupManager
				,com.frameworkset.platform.sysmgrcore.manager.OrgManager
				,com.frameworkset.platform.sysmgrcore.entity.Organization
				,com.frameworkset.platform.sysmgrcore.entity.Group" %>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	
	String uid2 = request.getParameter("userId");
	String uid = request.getParameter("userId");
	request.setAttribute("userId", uid2);
	String orgId = request.getParameter("orgId");
	request.setAttribute("orgId", orgId);

	String orgid = request.getParameter("orgId");
	UserRoleManagerForm userRoleForm = new UserRoleManagerForm();
	userRoleForm.setOrgId(orgid);
	userRoleForm.setUserId(uid.toString());
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	UserManager userManager = SecurityDatabase.getUserManager();
	User user = userManager.getUserById(uid);
	List existRole = roleManager.getRoleListByUserRole(user);
	
	//角色类别Map
	Map roleTypeMap = new RoleTypeManager().getRoleTypeMap();
	
	String sql = "";
	if ("1".equals(accessControl.getUserID())) {
		sql = "select * from td_sm_role t where t.role_id not in('2','3','4') order by t.role_type,t.role_name";
	} else {
		sql = "select * from td_sm_role t where t.role_id not in('1','2','3','4') order by t.role_type,t.role_name";
	}	
	List list = roleManager.getRoleList(sql);
	List allRole = null;
	
	// 角色列表加权限
	for (int i = 0; list != null && i < list.size(); i++) {
		Role role = (Role) list.get(i);
		if (accessControl.checkPermission(role.getRoleId(), "roleset",
		AccessControl.ROLE_RESOURCE)) {
			if (allRole == null){
				allRole = new ArrayList();
			}
			if (AccessControl.isAdministratorRole(role.getRoleName())) {
				allRole.add(role);
			}else{
				if (!role.getRoleName().equals(
						AccessControl.getEveryonegrantedRoleName())) {
					allRole.add(role);
				}
			}
		}
	}
	//System.out.println("allRole = " + allRole.size());
	
	//得到用户所属组得到用户所属角色
	GroupManager groupManager = SecurityDatabase.getGroupManager();
	OrgManager orgManager = SecurityDatabase.getOrgManager();
	List groupList = groupManager.getGroupList(user);
	List orgList = orgManager.getOrgList(user);

	//得到用户岗位对应的角色
	List jobRole = roleManager.getJobRoleByList(uid2);
	if (ConfigManager.getInstance().getConfigBooleanValue(
			"enableorgrole", true)) {
		Set userOrgRole = new HashSet();
		for (int i = 0; orgList != null && i < orgList.size(); i++) {
			Organization org = (Organization) orgList.get(i);
			List role = roleManager.getRoleList(org);
			if(role != null && role.size() > 0){
				userOrgRole.addAll(role);
			}
		}
		request.setAttribute("orgRole", new ArrayList(userOrgRole));
	}
	
	if (ConfigManager.getInstance().getConfigBooleanValue(
			"enablergrouprole", true)) {
		Set userGroupRole = new HashSet();
		for (int i = 0; groupList != null && i < groupList.size(); i++) {
			Group group = (Group) groupList.get(i);
			List role = roleManager.getRoleList(group);
			if(role != null && role.size() > 0){
				userGroupRole.addAll(role);
			}
		}
		request.setAttribute("groupRole", new ArrayList(userGroupRole));
	}
	request.setAttribute("allRole", allRole);
	request.setAttribute("existRole", existRole);
	request.setAttribute("userRoleForm", userRoleForm);
	request.setAttribute("jobRole", jobRole);
	
	String userName = user.getUserRealname();
	String currentlyUserId = accessControl.getUserID();
	
	//用户有权限的角色ID--gao.tang 2008.01.28
	List list1 = new ArrayList();
	//list1 = (List) request.getAttribute("allRole");
	list1 = allRole;
	String popedomRoleIds = "";
	Role popedomRole = new Role();
	if(list1 != null)
	{
		for (int i = 0; i < list1.size(); i++) {
			popedomRole = (Role) list1.get(i);
			if ("".equals(popedomRoleIds)) {
				popedomRoleIds = popedomRole.getRoleId();
			} else {
				popedomRoleIds += "," + popedomRole.getRoleId();
			}
		}
	}

%>
<html>
<head>    
<title>用户【<%=userName%>】角色授予</title>
<!--script language="JavaScript" src="../scripts/ajax.js" type="text/javascript"></script-->  
<script language="JavaScript" src="../../../include/querySelect.js" type="text/javascript"></script>
<SCRIPT LANGUAGE="JavaScript"> 

var api = frameElement.api, W = api.opener;

function send_request(url){
	//document.all.divProcessing.style.display = "block";
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
	if(document.all("allist").length==0){
		W.$.dialog.alert("<pg:message code='sany.pdp.no.role'/>！");
		return false;
	}
	var existrole = document.all("roleId");
	var exist = "";
	if(flag == 1){
	      var n=document.all("roleId").options.length-1;
	      var orgId = document.all("orgId").value;
	      var roleIds = "";	   	 	
	      for(var i=0;i<document.all("allist").options.length;i++){
	          var op=document.all("allist").options[i];
	          if(op.selected){
	        	  var flag2 = false;
			   		for(var j = 0; j < existrole.length; j++){
			   			if(op.text==existrole.options[j].text){
			   				flag2 = true;
			   			}
			   		}
			   		if(!flag2){
				   		if(roleIds==""){ 
				   			roleIds = op.value;
				   		}else {
				   			roleIds += "," + op.value;
				   		}
				   		addone(op.text,op.value,n);		
			   		}else{
			   			if(exist == ""){
			   				exist = op.text;
			   			}else{
			   				exist += "\n" + op.text;
			   			}
			   		}   
	              //addone(op.text,op.value,n);
	            //  if(roleIds=="") roleIds = roleIds + op.value;
	            //  else roleIds = roleIds + "," + op.value;
	          }
	      }	  	       
	      if(roleIds!=""){
    	      send_request('saveUserRole.jsp?userId=<%=uid%>&orgId='+orgId+'&roleId='+roleIds+'&op=add');
    	  }else if(exist != ""){
		   		W.$.dialog.alert("<pg:message code='sany.pdp.user.role'/>：\n" + exist);
	  			return;
		   }else{
		   		W.$.dialog.alert("<pg:message code='sany.pdp.to.choose.roles'/>!");
		   		return;
		   }	  
	  }else{
   		W.$.dialog.alert("<pg:message code='sany.pdp.no.authority'/>");   		
   	  } 
   	  
}
function addall(flag){
	if(document.all("allist").length==0){
		W.$.dialog.alert("<pg:message code='sany.pdp.no.role'/>！");
		return false;
	}
	var existrole = document.all("roleId");
	var exist = "";
	if(flag == 1){
		var n=document.all("roleId").options.length-1;
		var p=document.all("allist").options.length-1;		
		var roleIds = "";	 
		var orgId = document.all("orgId").value;  	   
	    for(var i=0;i<document.all("allist").options.length;i++){
	        var op=document.all("allist").options[i];
	        var flag2 = false;
	   		for(var j = 0; j < existrole.length; j++){
	   			if(op.text==existrole.options[j].text){
	   				flag2 = true;
	   			}
	   		}
	   		if(!flag2){
		   		if(roleIds==""){ 
		   			roleIds = op.value;
		   		}else {
		   			roleIds += "," + op.value;
		   		}
		   		addone(op.text,op.value,n);		
	   		}else{
	   			if(exist == ""){
	   				exist = op.text;
	   			}else{
	   				exist += "\n" + op.text;
	   			}
	   		}   
	       // addone(op.text,op.value,n);  
	       // if(roleIds=="") roleIds = roleIds + op.value;
	       // else roleIds = roleIds + "," + op.value;
	   } 
	   if(roleIds!="")
	   {
    	   send_request('saveUserRole.jsp?userId=<%=uid%>&orgId='+orgId+'&roleId='+roleIds+'&op=add'); 
	   }
	   else if(exist != ""){
	   		W.$.dialog.alert("<pg:message code='sany.pdp.user.role'/>：\n" + exist);
 			return;
	   }
    }
    else{
   		W.$.dialog.alert("<pg:message code='sany.pdp.no.authority'/>");   		
   	}
}

function deleterole(flag){
	if(document.all("roleId").length==0){
		W.$.dialog.alert("<pg:message code='sany.pdp.no.role'/>！");
		return false;
	}
	if(flag == 1){
	    var roleIds = "";	 
	    var orgId = document.all("orgId").value;
	    
		for (var m=document.all("roleId").options.length-1;m>=0;m--){
		    if(document.all("roleId").options[m].selected){
		        var op = document.all("roleId").options[m];
			    if(op.value=="3"){
		    		W.$.dialog.alert("<pg:message code='sany.pdp.role.deptmanager'/> "+op.text+" <pg:message code='sany.pdp.not.delete'/>！");
		    		return;
		    	}
		    	if(op.value=="4"){
		    		W.$.dialog.alert("<pg:message code='sany.pdp.role.deptmanager.template'/> "+op.text+" <pg:message code='sany.pdp.not.delete'/>！");
		    		return;
		    	}
		    	if(op.value=="1" && "<%=uid%>"=="1"){
		    		W.$.dialog.alert("<pg:message code='sany.pdp.role.sysmanager'/> "+op.text+" <pg:message code='sany.pdp.not.delete'/>！");
		    		return;
		    	}
		    	if("<%=uid%>"=="<%=currentlyUserId%>" && "<%=uid%>" != "1"){
		    		W.$.dialog.alert("<pg:message code='sany.pdp.not.delte.yourself'/>！");
		    		return;
		    	}
	            if(roleIds=="") roleIds = roleIds + op.value;
	            else roleIds = roleIds + "," + op.value;
	            //document.all("roleId").options[m]=null;
	        }
	 	}
	    if(roleIds!=""){
    	   send_request('saveUserRole.jsp?userId=<%=uid%>&orgId='+orgId+'&roleId='+roleIds+'&op=delete');
    	}else{
    		W.$.dialog.alert("<pg:message code='sany.pdp.to.choose.roles'/>！");
    		return false;
    	}     
   	}
   	else{
   		W.$.dialog.alert("<pg:message code='sany.pdp.no.authority'/>");   		
   	}
}
function deleteall(flag){
	if(document.all("roleId").length==0){
		W.$.dialog.alert("<pg:message code='sany.pdp.no.role'/>！");
		return false;
	}
	if(flag == 1){
	    var roleIds = "";
	    var orgId = document.all("orgId").value;	
	    
		for (var m=document.all("roleId").options.length-1;m>=0;m--){
		    var op = document.all("roleId").options[m];
		    if(op.value=="3"){
	    		W.$.dialog.alert("<pg:message code='sany.pdp.role.deptmanager'/> "+op.text+" <pg:message code='sany.pdp.not.delete'/>！");
	    		return;
	    	}
	    	if(op.value=="1" && "<%=uid%>"=="1"){
		    	W.$.dialog.alert("<pg:message code='sany.pdp.role.sysmanager'/> "+op.text+" <pg:message code='sany.pdp.not.delete'/>！");
		    	return;
		    }
		    if("<%=uid%>"=="<%=currentlyUserId%>" && "<%=uid%>" != "1"){
	    		W.$.dialog.alert("<pg:message code='sany.pdp.not.delte.yourself'/>！");
	    		return;
	    	}
		    if(roleIds=="") roleIds = roleIds + op.value;
	        else roleIds = roleIds + "," + op.value;
	        //document.all("roleId").options[m]=null;
	    }
	    if(roleIds!="")
    	   send_request('saveUserRole.jsp?userId=<%=uid%>&orgId='+orgId+'&roleId='+roleIds+'&op=delete');
	}else{
   		W.$.dialog.alert("<pg:message code='sany.pdp.no.authority'/>");   		
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

function window.onhelp(){  
  //selectRole();
  return false;
}

function selectRole(){
	var url = "selectRoletree.jsp";
	var selectValue = "";
	selectValue = window.showModalDialog(url,window,"dialogWidth:"+(600)+"px;dialogHeight:"+(400)+"px;scroll=yes;status=no;titlebar=no;toolbar=no;maximize=yes;minimize=0;")
	if(selectValue != ""){
		document.all("selectRoles").value = selectValue;
	}
}
</SCRIPT>
<script language="javascript" src="../../scripts/dragdiv.js"></script>
</head>
<body class="contentbodymargin" scroll="yes" onload="loadDragDiv()" onunload="window.returnValue='ok';" >
<center>
<form name="OrgJobForm" target="hiddenFrame" action="" method="post" >
<input name="userId" value="<%=uid%>" type="hidden">
<input name="popedomRoleIds" value="<%=popedomRoleIds%>" type="hidden">


<table width="80%" border="0" cellpadding="0" cellspacing="1">

<tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
  <%
  	//RoleManager roleManager = SecurityDatabase.getRoleManager();
  	String everyoneRolename = AccessControl
  			.getEveryonegrantedRoleName();
  	Role role = roleManager.getRoleByName(everyoneRolename);
  	if (role != null) {
  %>
  <tr class="tabletop" cols="3">
    <td colspan="3"><font color="red">
	<pg:message code="sany.pdp.mrsy"/><%=everyoneRolename%></font></td>
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
    <td width="40%" align="left"><pg:message code="sany.pdp.choose.role"/></td>
    <td width="20%" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
    <td width="40%" align="right"><pg:message code="sany.pdp.exist.role"/></td>
  </tr>
  
  <tr>
  	<td width="40%" align="left">
  		<input name="selectRoles" type="text" value="" onkeydown="enterKeydowngo()" onpropertychange="onBlurryQueryChange()" ondblclick=""  />
  		<a class="bt_1" onclick="blurryQuary()"><span><pg:message code="sany.pdp.common.operation.search"/></span></a>
  		<input name="nextQue" style="display:none" type="button" value="查找下一个" onclick="nextQuery()" class="input" />
  	</td>
    <td width="20%" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
    <td width="40%" align="left">
    	
    </td>
  </tr>
  
  <tr >
     <td  align="left" >
     <div class="win" id="dd_1" align="left" >
     <select name="allist"  multiple style="width:98%" onDBLclick="addRole(1)" size="15">
		  <pg:list requestKey="allRole">
		  <% 
		  	String roleType = dataSet.getString("roleType");
		  %>
			<option value="<pg:cell colName="roleId"/>" rolename="<pg:cell colName="roleName"/>"><pg:cell colName="roleName"/> (<%=roleTypeMap.get(roleType) %>)</option>
		  </pg:list>			
	</select>
	</div>
	</td>				  
		  	
    <td align="center">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center">
        	<a name="button1"  class="bt_2"  onClick="addRole(1)"><span>&gt;</span></a>
        </td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center">
        	<a name="button2"  class="bt_2"  onClick="addall(1)"><span>&gt;&gt;</span></a>
        </td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center">
        	<a name="button3"  class="bt_2"  onClick="deleteall(1)"><span>&lt;&lt;</span></a>
        </td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center">
        	<a name="button4"  class="bt_2"  onClick="deleterole(1)"><span>&lt;</span></a>
        </td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
    </table>
    </td>
    <td align="right">
    <div class="win" id="dd_2" align="center">
     <select name="roleId"  multiple style="width:98%" onDBLclick="deleterole(1)" size="15" >
				  <pg:list requestKey="existRole">
				  <% 
				  	String roleType = dataSet.getString("roleType");
				  %>
					<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/> (<%=roleTypeMap.get(roleType) %>)</option>			
				  </pg:list>			
	 </select>
	 </div>					
	</td>				 
				  
  </tr>
  
  <tr class="tabletop">
    <td width="40%" align="left">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="right">&nbsp;</td>
  </tr>
 
  <tr class="tabletop" >
  
    <td width="40%" align="center"> 
    <%
     			if (ConfigManager.getInstance().getConfigBooleanValue(
     			"enableorgrole", true)) {
     %><pg:message code="sany.pdp.jgjuese"/><%
     }
     %></td>
  
    <td width="20%" align="center">&nbsp;</td>
    
    <td width="40%" align="center"> 
    <%
     			if (ConfigManager.getInstance().getConfigBooleanValue(
     			"enablergrouprole", true)) {
     %><pg:message code="sany.pdp.group.role"/><%
     }
     %></td>
    
  </tr>
  <tr >
     <td  align="left" >
     <%
     if (ConfigManager.getInstance().getConfigBooleanValue(
     			"enableorgrole", true)) {
     %>
     <div class="win" id="dd_3"  align="left">
     <select name="orgrolelist"  multiple style="width:98%" size="15" disabled="true">   
				  <pg:list requestKey="orgRole">
					<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/></option>
				  </pg:list>			
	</select>
	</div>
	<%
	}
	%>
	</td>		
	<td align="center">&nbsp;</td>
	 <td  align="right" >
	 <%
	 if (ConfigManager.getInstance().getConfigBooleanValue(
	 			"enablergrouprole", true)) {
	 %>
	 <div class="win" id="dd_4" align="left">
     <select name="grouprolelist"  multiple style="width:98%" size="15" disabled="true">   
				  <pg:list requestKey="groupRole">
					<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/></option>
				  </pg:list>			
	</select>
	</div>
	<%
	}
	%>
	</td>
 </tr>
 <%
 	if (ConfigManager.getInstance().getConfigBooleanValue(
 			"enablejobfunction", false)) {
 %>
 <tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    <td width="20%" align="center">&nbsp;</td>
    <td width="40%" align="center">&nbsp;</td>
  </tr>
 <tr>
 	<td align="left"><pg:message code="sany.pdp.yhgwdyjs"/></td>
 	<td align="center">-----------------></td>
 	<td align="right"><pg:message code="sany.pdp.userrole.organization.job"/></td>
 </tr>
 <tr>
 <td align="left"><div class="win" id="dd_5" align="left">
	<select name="jobrolelist" multiple style="width:98%" size="15" onchange="roleChangebox()">
				  <pg:list requestKey="jobRole">
				  	<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/></option>
				  </pg:list>
	</select></div>
 </td>
 <td>&nbsp;</td>
 <td align="right">
 	<div class="win" id="dd_6" align="left">
 	<select name="userjobList" multiple style="width:98%" size="15">
		
	</select>
	</div>
 </td>
 </tr>
 <%
 }
 %>
 <tr>
 	<td align="center" colspan="4">
 		<a class="bt_2" onclick="closeDlg();"><span><pg:message code="sany.pdp.common.operation.close"/></span></a>
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
				    <font color=#FFFFFF><pg:message code="sany.pdp.common.operation.processing"/></font>
				</marquee>
			</td>
		</tr>
	</table>
</div>	
</body>
<iframe name="orgjoblist" src="org_job.jsp?" width=0 height=0></iframe>
<iframe name="hiddenFrame" width=0 height=0></iframe>
</html>


