<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.GroupManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager" %>
<%@ page import="java.util.List"%>
<%@ page import="com.frameworkset.platform.security.AccessControl, java.util.ArrayList;"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    //对组ID进行获取
    String groupId=request.getParameter("groupId");
    //实例化接口进行组角色的获取
	GroupManager groupManager=SecurityDatabase.getGroupManager();
	List containRoleList=groupManager.getGroupRolesByGroupId(groupId);
	RoleManager roleManager=SecurityDatabase.getRoleManager();
	List roleList=new ArrayList();
	String sql = "";
	if("1".equals(accesscontroler.getUserID()))
	{
		sql = "select * from td_sm_role r where r.role_Id <> '2' and r.role_Id <> '3' order by r.role_Name";
	}
	else
	{
		sql = "select * from td_sm_role r where r.role_Id <> '1' and r.role_Id <> '2' and r.role_Id <> '3' order by r.role_Name";
	}
	roleList=roleManager.getRoleList(sql);
	
	//将返回的角色集合存储至页面缓存供列表框标签获取数据源使用
	request.setAttribute("existRole",containRoleList);//已授予角色
	request.setAttribute("allRole",roleList);//所有角色
%>
<html>
	<head>    
  		<title>属性容器</title>
  		<%@ include file="/include/css.jsp"%>
  		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/sysmanager/css/contentpage.css">
  		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/sysmanager/css/tab.winclassic.css">
  	<%@ include file="/include/css.jsp"%>
  		<script language="JavaScript">
  		var allowSubmit=false; 
		//添加选中的角色
		function addRole()
		{	
   			var selectRoleIds="";
   			var selectedDrop=document.all("roleId");
   			var count=document.all("allist").options.length;
   			for(var i=0;i<count;i++)
   			{
   				//
   				var op=document.all("allist").options[i];
   				if(op.selected)
   				{
   					//	
					addone(op.text,op.value);
					//
					if(allowSubmit)selectRoleIds+=","+op.value;
				}
   			}
   			if(selectRoleIds.length>0)send_request(selectRoleIds,'add');
		}
		//添加所有的角色
		function addall()
		{
    		var selectRoleIds="";
			var count=document.all("allist").options.length;	  
    		for(var i=0;i<count;i++)
    		{
     			var op=document.all("allist").options[i];
     			addone(op.text,op.value);  
     			if(allowSubmit)selectRoleIds+=","+op.value;
    		}
   			if(selectRoleIds.length>0)send_request(selectRoleIds,'add');
		}
		//将列表框中的选项进行追加
		function addone(name,value)
		{
   			allowSubmit=true;
   			for(var i=0;i<document.all("roleId").options.length;i++)
  			{
	 			if(value==document.all("roleId").options[i].value)
	 			{
	 				allowSubmit=false;
	 				break;
	 			}
  			}
   			if(allowSubmit)document.all("roleId").add(new Option(name,value));
		}
		//删除选中的角色
		function deleteRole()
		{
    		var selectRoleIds="";
 			for(var m=document.all("roleId").options.length-1;m>=0;m--)
 			{
	    		if(document.all("roleId").options[m].selected)
	    		{
	    			selectRoleIds+=","+document.all("roleId").options[m].value;
	    			document.all("roleId").options.remove(m);
	    		}
			}
			if(selectRoleIds.length>0)send_request(selectRoleIds,'delete');
		}
		//删除所有的角色
		function deleteall()
		{
    		var selectRoleIds="";
    		var count=document.all("roleId").options.length;
			for (var m=0;m<count;m++)
			{
	    		selectRoleIds+=","+document.all("roleId").options[m].value;
			}
			if(selectRoleIds.length>0)
			{
				document.all("roleId").innerHTML="";
    			send_request(selectRoleIds,'delete');
   			}	
		}
		//将参数提交至隐藏帧进行处理
		function send_request(selectRoleIds,flag)
		{
    		selectRoleIds=selectRoleIds.substring(1,selectRoleIds.length);
			document.all.divProcessing.style.display = "block";
			document.RoleOrgForm.action = '<%=rootpath%>/sysmanager/groupmanager/editgrouprole_ajax.jsp?roleId='+selectRoleIds+'&groupId='+document.all("groupId").value+'&flag='+flag;
			document.RoleOrgForm.target = "hiddenFrame";
			document.RoleOrgForm.submit();
		}
		</script>
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
    	<td width="40%" align="center">可选角色</td>
    	<td width="20%" align="center">&nbsp;</td>
    	<td width="40%" align="center">已授予角色</td>
  	</tr>
  	<tr class="tabletop">
   	 	<td width="40%" align="center">&nbsp;</td>
    	<td width="20%" align="center">&nbsp;</td>
    	<td width="40%" align="center">&nbsp;</td>
  	</tr>
  	<tr >
     	<td  align="right">
      	<select name="allist"  multiple style="width:80%" onDBLclick="addRole()" size="18">
				  <pg:list requestKey="allRole">
					<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/></option>
				  </pg:list>			
		</select>
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
        <td align="center"><input name="button4" type="button" class="input" value="&lt;" onclick="deleteRole()"></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
    </table></td>
    <td >
    <select name="roleId"  multiple style="width:80%" onDBLclick="deleterole()" size="18">
				  <pg:list requestKey="existRole">
					<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/></option>
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
<input type="hidden" name="groupId" value="<%=groupId%>"/>
</form>
</center>
</div>
<div id=divProcessing style="width:200px;height:30px;position:absolute;left:230px;top:450px;display:none">
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
