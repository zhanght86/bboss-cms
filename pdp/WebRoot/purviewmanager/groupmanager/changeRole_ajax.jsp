<%
/*
 * <p>Title: 关联角色</p>
 * <p>Description: 关联角色</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-18
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
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.GroupManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Group" %>
<%@ page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.sysmgrcore.entity.Role"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkManagerAccess(request,response);

	String curUserId = accesscontroler.getUserID(); 
    
    //对组ID进行获取
    String groupId=request.getParameter("groupId");
    
    //实例化接口进行组角色的获取
	GroupManager groupManager=SecurityDatabase.getGroupManager();

	List containRoleList=groupManager.getGroupRolesByGroupId(groupId);
	Group group=groupManager.getGroupByID(groupId);
	String groupName=group.getGroupName();	
	RoleManager roleManager=SecurityDatabase.getRoleManager();
	List roleList=new ArrayList();
	String sql = "";
	if("1".equals(accesscontroler.getUserID()))
	{
		sql = "select * from td_sm_role r where r.role_Id <> '2' and r.role_Id <> '3' and r.role_ID <> '4' order by r.role_Name";
	}
	else
	{
		sql = "select * from td_sm_role r where r.role_Id <> '1' and r.role_Id <> '2' and r.role_Id <> '3'and r.role_Id <> '4' order by r.role_Name";
	}
	roleList=roleManager.getRoleList(sql);
	List allRole = null;
	
	// 角色列表加权限
	for (int i = 0; roleList != null && i < roleList.size(); i++) {
		Role role = (Role) roleList.get(i);
		if (String.valueOf(role.getOwner_id()).equals(curUserId)) {
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
	
	//将返回的角色集合存储至页面缓存供列表框标签获取数据源使用
	request.setAttribute("existRole",containRoleList);//已授予角色
	if(accesscontroler.isAdmin()){
		request.setAttribute("allRole",roleList);//所有角色
	}else{
		request.setAttribute("allRole",allRole);//所有角色
	}
%>
<html>
	<head>    
  		<title>【<%=groupName%>】用户组关联角色</title>

<%@ include file="/common/jsp/css-lhgdialog.jsp"%>		
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
    		
			document.RoleOrgForm.action = 'editgrouprole_ajax.jsp?roleId='+selectRoleIds+'&groupId='+document.all("groupId").value+'&flag='+flag;
			document.RoleOrgForm.target = "hiddenFrame";
			document.RoleOrgForm.submit();
		}
		</script>
	<body class="contentbodymargin">
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
    	<td width="40%" align="center"><pg:message code="sany.pdp.choose.role"/></td>
    	<td width="20%" align="center">&nbsp;</td>
    	<td width="40%" align="center"><pg:message code="sany.pdp.exist.role"/></td>
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
        <td align="center"><a class="bt_2"  onclick="addRole()"><span>&gt;</span></a></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><a class="bt_2"  onclick="addall()"><span>&gt;&gt;</span></a></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><a class="bt_2"  onclick="deleteall()"><span>&lt;&lt;</span></a></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><a class="bt_2"  onclick="deleteRole()"><span>&lt;</span></a></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
    </table></td>
    <td >
    <select name="roleId"  multiple style="width:80%" onDBLclick="deleteRole()" size="18">
				  <pg:list requestKey="existRole">
					<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/></option>
				  </pg:list>			
	 </select>			
					
	</td>				 
				  
  </tr>
  <tr class="tabletop">
    <td width="40%" align="center">&nbsp;</td>
    
  </tr>
  <tr>
  	<td colspan="3" align="center">
  
  		<a class="bt_2" onclick="closeDlg();"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
  	</td> 
  </tr>
</table>
<input type="hidden" name="groupId" value="<%=groupId%>"/>
</form>
</center>
</div>
<div id=divProcessing style="width:200px;height:30px;position:absolute;left:230px;top:450px;display:none">
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
