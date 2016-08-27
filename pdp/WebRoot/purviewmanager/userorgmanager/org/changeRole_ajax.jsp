<%
/*
 * <p>Title: 机构下的角色处理页面</p>
 * <p>Description:机构下的角色处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-26
 * @author liangbing.tao
 * @version 1.0
 */
 %>
<%     
  response.setHeader("Cache-Control", "no-cache"); 
  response.setHeader("Pragma", "no-cache"); 
  response.setDateHeader("Expires", -1);  
  response.setDateHeader("max-age", 0); 
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.*" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>

<%	
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	
	
	String orgId = (String) request.getParameter("orgId");
	
	
	OrgManager orgManager = SecurityDatabase.getOrgManager();
	Organization org = orgManager.getOrgById(orgId);
	String orgName = org.getRemark5();
	
	RoleManager roleManager = SecurityDatabase.getRoleManager();	
	
	
	List existRole = roleManager.getRoleList(org);//获取机构已有的角色列表
	
	List list = roleManager.getRoleList();//获取所有角色的列表
	
	
	
	
	//角色列表加权限判断，筛选有权限的角色列表
	List allRole = new ArrayList();
	
	for (int i = 0; list != null && i < list.size(); i++) 
	{
		Role role = (Role) list.get(i);		
		if (accessControl.checkPermission(role.getRoleId(),
							"roleset",
							AccessControl.ROLE_RESOURCE)) 
		{

				//不能出现默认角色：admin,roleforeveryone,orgmanager/ admin能看见admin角色
				if(Integer.parseInt(role.getRoleId()) == 1 
							&& "1".equals(accessControl.getUserID()))
				{
					allRole.add(role);
				}
				
				if(Integer.parseInt(role.getRoleId()) > 4)
				{
					allRole.add(role);
				}				
		}
	}
		
	request.setAttribute("allRole", allRole);
	request.setAttribute("existRole", existRole);
%>
<html>
<head>    
  <title>角色授予</title>
	<SCRIPT LANGUAGE="JavaScript">   
	function send_request(url)
	{
	    //document.all.divProcessing.style.display = "block";
	    
	   // document.all("button1").disabled = true;
	   // document.all("button2").disabled = true;
	   /// document.all("button3").disabled = true;
	   // document.all("button4").disabled = true;
	    //document.all("back").disabled = true;
	    
		document.OrgJobForm.action = url;
		document.OrgJobForm.target="hiddenFrame";
		document.OrgJobForm.submit();
	}
	
	function isRecursive()
	{	    
		var ischeck = document.all.item("recursive").checked;
		if(ischeck)
			return "1";
		else
			return "0";	
	}
	function addone(name,value,n)
	{
	   for(var i=n;i>=0;i--)
	   {
			if(value==document.all("roleId").options[i].value)
			{
			  return;
			}
		}
	   var op=new Option(name,value);
	   document.all("roleId").add(op);
	}
	
	function addRole()
	{	
	    var n=document.all("roleId").options.length-1;
	    var roles = ""; 
	    var orgId = document.all("orgId").value;
		var flag = isRecursive();	
		
		if(document.all("allist").options.length < 1)
	    {
	    	$.dialog.alert("<pg:message code='sany.pdp.purviewmanager.rolemanager.role.authorize.to.role.select'/>！",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	    	return;
	    }
	    	
	   	for(var i=0;i<document.all("allist").options.length;i++)
	   	{
		    var op=document.all("allist").options[i];
		    if(op.selected)
		    {
		        addone(op.text,op.value,n);
		        if(roles =="") 
		        {
		        	roles = roles + op.value;
		        }
		        else 
		        {
		        	roles = roles + "," + op.value;
		        }
		    }
		} 
		
		if(roles != "")
		{		
			send_request('storeorgrole.jsp?roleId='+roles+'&orgId='+orgId+'&flag='+flag);  
		}
		else
		{
			$.dialog.alert("<pg:message code='sany.pdp.purviewmanager.rolemanager.role.authorize.to.role.select'/>！",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			return ;
		}
	}
	
	function addall()
	{		
	    var roles = "";
		var n=document.all("roleId").options.length-1;
		var p=document.all("allist").options.length-1;	
		
		if(document.all("allist").options.length < 1)
	    {
	    	$.dialog.alert("<pg:message code='sany.pdp.purviewmanager.rolemanager.role.authorize.to.role.select'/>！",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	    	return;
	    }
			  
	    for(var i=0;i<document.all("allist").options.length;i++)
	    {
	        var op=document.all("allist").options[i];
	        addone(op.text,op.value,n);  
	        if(roles =="") 
	        {
	        	roles = roles + op.value;
	        }
		    else
		    {
		       roles = roles + "," + op.value;
		    }
	    }
	    
	    if(roles != "")
		{		
			var orgId = document.all("orgId").value;
			var flag = isRecursive();
			send_request('storeorgrole.jsp?roleId='+roles+'&orgId='+orgId+'&flag='+flag);  
	    }
	    else
	    {
	    	$.dialog.alert("<pg:message code='sany.pdp.purviewmanager.rolemanager.role.authorize.to.role.select'/>！",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			return ;
	    }	    
	}
	
	function deleterole()
	{
	    var roles = "";
	    var unroles = "";
		var orgId = document.all("orgId").value;
		var flag = isRecursive();
		var count = 0;
		if(document.all("roleId").options.length < 1)
		{
			$.dialog.alert("<pg:message code='sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.remove.select'/>！",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	    	return;
	    }
		var autorole = document.all("allist");
	    for (var m=document.all("roleId").options.length-1;m>=0;m--)
	    {
		    if(document.all("roleId").options[m].selected)
		    {
		    	count ++;
	      	    var op = document.all("roleId").options[m];
	      	    var isrole = true;
	      	    for(var n = 0; n < autorole.options.length; n++){
	      	    	if(autorole.options[n].value == op.value){
			      	    if(roles =="") 
			      	    {
			      	    	roles = roles + op.value;
			      	    }
				        else
				        {
				        	roles = roles + "," + op.value;
				        }
				        document.all("roleId").options[m]=null;
				        isrole = false;
			        }
		        }
		        if(isrole){
		        	if(unroles == ""){
		        		unroles = op.text;
		        	}else{
		        		unroles += "\n" + op.text;
		        	}
		        }
	        }
	    }
	    if(unroles != ""){
	    	
	    	$.dialog.alert("<pg:message code='sany.pdp.purviewmanager.rolemanager.role.authorize.to.role.nopurview'/>\n"+unroles,function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	    }
	    if(roles != "")
	    {
	    	send_request('deleteorgrole.jsp?roleId='+roles+'&orgId='+orgId+'&flag='+flag);
	    }
		if(count < 1)
	    {
	    	$.dialog.alert("<pg:message code='sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.remove.select'/>！",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	    	return ;
	    }
	    
	 }
	 
	function deleteall()
	{
		var roles = "";
		var unroles = "";
		var orgId = document.all("orgId").value;
		var flag = isRecursive();
		
		if(document.all("roleId").options.length < 1)
		{
	    	$.dialog.alert("<pg:message code='sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.remove.select'/>！",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	    	return;
	    }
	    var autorole = document.all("allist");
		for (var m=document.all("roleId").options.length-1;m>=0;m--)
		{
		    var op = document.all("roleId").options[m];
		    var isrole = true;
		    for(var n = 0; n < autorole.options.length; n++){
	      	    if(autorole.options[n].value == op.value){
		      	    if(roles =="") 
		      	    {
		      	    	roles = roles + op.value;
		      	    }
			        else 
			        {
			        	roles = roles + "," + op.value;	    	
			        }
			    	document.all("roleId").options[m]=null;
			    	isrole = false;
			    }
			 }
			 if(isrole){
		        	if(unroles == ""){
		        		unroles = op.text;
		        	}else{
		        		unroles += "\n" + op.text;
		        	}
		        }
	    }
	    
	    if(unroles != ""){
	    	$.dialog.alert("<pg:message code='sany.pdp.purviewmanager.rolemanager.role.authorize.to.role.nopurview'/>\n"+unroles,function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	    }
	    if(roles != "")
	    {
	    	send_request('deleteorgrole.jsp?roleId='+roles+'&orgId='+orgId+'&flag='+flag);
	    }
	} 
	
  </SCRIPT>
</head>
 
<body class="contentbodymargin" scroll="no">
	<div id="">
		<center>
			<form name="OrgJobForm" action="" method="post" >
				<table width="80%" border="0" cellpadding="0" cellspacing="1">
					<tr class="tabletop">
					    <td width="40%" align="center">&nbsp;</td>
					    <td width="20%" align="center">&nbsp;</td>
					    <td width="40%" align="center">&nbsp;</td>
					 </tr>
					 <tr>
					  	<td width="40%" align="center">
							<input type="checkBox" name="recursive" id="recursive" >
							<pg:message code="sany.pdp.resourcemanage.recursion.confer"/>
						</td>
					  </tr>
					  <tr>
					  <td></td>
					  </tr>
					  <tr class="tabletop">
					    <td width="40%" align="center"><pg:message code="sany.pdp.choose.role"/></td>
					    <td width="20%" align="center">&nbsp;</td>
					    <td width="40%" align="center"><pg:message code="sany.pdp.personcenter.person.organization"/>&nbsp;(<%=orgName%>) &nbsp;<pg:message code="sany.pdp.exist.role"/></td>
					  </tr>
					  <tr class="tabletop">
					    <td width="40%" align="center">&nbsp;</td>
					    <td width="20%" align="center">&nbsp;</td>
					    <td width="40%" align="center">&nbsp;</td>
					  </tr>
	  				  <tr >
						     <td  align="right" >
							     <select name="allist"  multiple style="width:100%" onDBLclick="addRole()" size="18" >
											  <pg:list requestKey="allRole">
												<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/></option>
											  </pg:list>			
								</select>
							</td>				  
			  	
	    					<td align="center">
	    						<table width="100%" border="0" cellspacing="0" cellpadding="0">
								      <tr>
								        <td align="center">
								        	<a class="bt_2"  onclick="addRole()"><span>&gt;</span></a>
								        </td>
								      </tr>
								      <tr>
								        <td align="center">&nbsp;</td>
								      </tr>
								      <tr>
								        <td align="center">
								        	<a class="bt_2"  onclick="addall()"><span>&gt;&gt;</span></a>
								        </td>
								      </tr>
								      <tr>
								        <td align="center">&nbsp;</td>
								      </tr>
								      <tr>
								        <td align="center">
								        	<a  class="bt_2"  onclick="deleteall()"><span>&lt;&lt;</span></a>
								        </td>
								      </tr>
								      <tr>
								        <td align="center">&nbsp;</td>
								      </tr>
								      <tr>
								        <td align="center">
								        	<a class="bt_2"  onclick="deleterole()"><span>&lt;</span></a>
								        </td>
								      </tr>
								      <tr>
								        <td align="center">&nbsp;</td>
								      </tr>
	    							</table>
	    						</td>
	    						
							    <td >
								    <div style="overflow:auto;">
								     	<select name="roleId"  multiple style="width:100%" onDBLclick="deleterole()"size="18">
										  <pg:list requestKey="existRole">
											<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/></option>
										  </pg:list>			
									 	</select>				
									</div>				
								</td>				 
	 						 </tr>
	 						
						</table>
	   				 <input type="hidden" name="orgId" value="<%=orgId%>"/>
				</form>
			</center>
	</div>
	<div id=divProcessing style="width:200px;height:30px;position:absolute;left:300px;top:400px;display:none">
		<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
		    <tr>
			    <td bgcolor=#3A6EA5>
					    <font color=#FFFFFF>...处理中...请等待...</font>
				</td>
			</tr>
		</table>
	</div>
	</body>
	<iframe name="hiddenFrame" width=0 height=0></iframe>
	</html>
