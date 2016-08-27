<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleTypeManager"%>
<%@ page import="java.util.List"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.RoleType"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.sysmgrcore.manager.*,
com.frameworkset.platform.sysmgrcore.entity.Role,com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
com.frameworkset.common.poolman.DBUtil"%>
<%			
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", -1);
			response.setDateHeader("max-age", 0);

			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkAccess(request, response);
			String roleId = request.getParameter("roleId");
			String flag = request.getParameter("flag");	
								
			//当修改角色时，如果角色id为1，2，3，4时禁止修改
			//2008-4-18  baowen.liu
			String isDisabled="";
			if(flag.equals("1")){
				if(roleId.equals("1") || roleId.equals("2") || roleId.equals("3") || roleId.equals("4"))
					{
					  	isDisabled="disabled='true'";
					}
			  }
			
			//System.out.println("..........."+roleId);
			Role role = null;
			String rolename ="";
			String roledesc ="";
			String roletypeid="";
			if(roleId!=null){
				RoleManager roleManager = SecurityDatabase.getRoleManager();
				role = new Role();
				role = roleManager.getRoleById(roleId);
				rolename = role.getRoleName();
				roledesc = role.getRoleDesc();
			    roletypeid = role.getRoleType();
				
			}
	String roleTypeid = String.valueOf(session.getAttribute("roleTypeid"));
	session.removeAttribute("roleTypeid");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<script src="../inc/js/func.js"></script>
	<style type="text/css">

</style>
	<head>

		<title>
		<%if(flag.equals("1")){%>
			CMS 修改角色
		<%}else{%>
			CMS 新建角色
		<%}%>
		</title>
	</head>
	<script language="javascript">
		function trim(string){
		  var temp="";
		  string = ''+string;
		  splitstring = string.split(" ");
		  for(i=0;i<splitstring.length;i++){
		    temp += splitstring[i];
		  } 
		  return temp;
		}
		
		function storeRole()
		{	
			var roleName= document.forms[0].roleName.value;
				if (trim(roleName).length == 0 ){
		    	alert("角色名不能为空！"); 
		    	return false;
		    } 
		    if (document.forms[0].roleDesc.value.length < 1 || document.forms[0].roleDesc.value.replace(/\s/g,"")=="") {			
					alert("角色描述必须填写");
					return;
				}	
			if (document.forms[0].roleName.value.length == "adminstrator") {			
					alert("系统管理员不能修改。谢谢合作:)");
					return;
				}	
				
			if ((document.forms[0].roleType.value==""))
  	  			{
		  	  	    alert("请选择角色类别");		  	  	
		  	  	    return;
  	  	        }
  	  	        
  	  	        var rn = document.forms[0].roleName.value;
				var rd = document.forms[0].roleDesc.value;
				if(rn.search(/[\\\/\|:\*\?!<>"']/g)!=-1){
					alert("角色名称不能有\\/|:*?!<>\"'!等特殊字符");
					return;
				}	
				if(rd.search(/[\\\/\|:\*\?<>"']/g)!=-1){
					alert("角色描述不能有\\/|:*?<>\"'!等特殊字符");
					return;
				}
				
				if(rn.length>100)
				{
					alert("角色名称过长,限制在100字符以内!");
					return;
				}
				if(rd.length>100)
				{
					alert("角色描述过长,限制在100字符以内!");
					return;
				}
  	  	        			
			roleform.action="../userManage/addRole_do.jsp";
			roleform.submit();	
		}
		
		function updateRole()
		{	
			var form = document.forms[0];
				if (form.roleName.value.length < 1 || form.roleName.value.replace(/\s/g,"")=="") 
				{			
					alert("角色名称必须填写");
					return;
				}
				if (form.roleDesc.value.length < 1 || form.roleDesc.value.replace(/\s/g,"")=="") 
				{			
					alert("角色描述必须填写");
					return;
				}				
				
				if (form.roleName.value.length == "adminstrator") 
				{			
					alert("系统管理员不能修改。谢谢合作:)");
					return;
				}
				
				var rn = form.roleName.value;
				var rd = form.roleDesc.value;
				if(rn.search(/[\\\/\|:\*\?<>"']/g)!=-1){
					alert("角色名称不能有\\/|:*?<>\"'!等特殊字符");
					return;
				}	
				if(rd.search(/[\\\/\|:\*\?<>"']/g)!=-1){
					alert("角色描述不能有\\/|:*?<>\"'!等特殊字符");
					return;
				}
				
				if(rn.length>100)
				{
					alert("角色名称过长,限制在100字符以内!");
					return;
				}
				if(rd.length>100)
				{
					alert("角色描述过长,限制在100字符以内!");
					return;
				}
				if ((form.roleType.value==""))
  	  			{
		  	  	    alert("请选择角色类别");		  	  	
		  	  	    return;
  	  	        }
				
			roleform.action="../userManage/updateRole_do.jsp";
			roleform.submit();	
		}
		
		function back()
		{	
			window.close();
		}
	</script>
	<!--下面的代码 使得按 F5会刷新modal页面-->
	<base target="_self">
	<body bgcolor="#F7F8FC">
	<script language="javascript">
	
	</script>

		<!--上面的代码 使得按 F5会刷新modal页面-->
		<form target="addgather" name="roleform">
			<input name="doctype" value="0" type=hidden>
			<input name="userid" value="" type=hidden>
			
			<table width="100%" border="0" cellpadding="0" cellspacing="0" background="../images/Gather_Content_topbg.gif">
				<tr>
					<td width="100" height="35" class="cms_title_blue">
					<%if(flag.equals("1")){%>
					<input type="hidden" name="roleId" value="<%=roleId%>"/>
					CMS 修改角色
					<%}else{%>
					CMS 新建角色
					<%}%>
					</td>
				</tr>
			</table>
					
			<table width="100%" border="0" cellpadding="3" cellspacing="0">
					</br>
					
					<tr class="tr">
						<td height="25" >
							<strong>角色名称：*</strong>
						</td>
						<td height="25" align="left">
							
							<input type="text" name="roleName" style="width:300"  class="cms_text" value="<%=rolename%>" <%if(flag.equals("1")){%> readonly=readonly <%}%> <%=isDisabled%>>
						</td>
					</tr>
					<tr class="tr">
						<td height="25">
                        	<strong>角色类别:*</strong>
                         </td>
                         <td height="25" align="left">
                         <%if(flag.equals("0")){%>
		                        <select name="roleType" class="cms_select" >
								<option value="">--请选择角色类别--</option>
								<%
								List typenamelist = null;
								RoleTypeManager rtm = new RoleTypeManager();
								typenamelist = rtm.getTypeNameList();
								request.setAttribute("typenamelist",typenamelist);
								%>
								<pg:list requestKey="typenamelist">
								<option value="<pg:cell colName="roleTypeID"/>"
								    <pg:equal colName="roleTypeID" value="<%=roleTypeid%>">
										selected
										</pg:equal>
								 >
								<pg:cell colName="typeName"/> 
								</pg:list>
								</select>	
						
						<%} else {%>	
						<select name="roleType" class="cms_select" <%=isDisabled%> >						
						<%
						List typenamelist = null;
						RoleTypeManager rtm = new RoleTypeManager();
						typenamelist = rtm.getTypeNameList();
						if(typenamelist != null)
						{
							for(int i=0;i<typenamelist.size();i++)
							{
								RoleType rt = (RoleType)typenamelist.get(i);
								if(roletypeid.equals(rt.getRoleTypeID()))
								{
						%>
									<option value="<%=rt.getRoleTypeID()%>" selected> <%=rt.getTypeName()%></option>
									<%
								}
								else
								{
									%>
									<option value="<%=rt.getRoleTypeID()%>"><%=rt.getTypeName()%></option>
									<%
								}
							}
						}
						}%>												
						</select>		
						
                         </td>					
					
					</tr>
					<tr class="tr">
						<td height="25" >
							<strong>描述：*</strong>
						</td>
						<td height="25"  align="left">
							<textarea name="roleDesc" style="width:300" cols="50" rows="4" defaultValue="" <%=isDisabled%> ><%=roledesc%></textarea>
						</td>
						
					</tr>
				</table>
				
				<table width="100%" border="0" cellpadding="3" cellspacing="0">

					<tr>
						<td>
							<div align="center">
								<%if(flag.equals("1")){ %>
																									   
									<input name="update" type="button" onClick="updateRole()" class="cms_button" value="修改" <%=isDisabled%> >
								
								 <%    
								   }else{%>
									<input name="save" type="button" onClick="storeRole()" class="cms_button" value="保存">
								<%}%>
								
								
								<input name="delete1" type="button" onClick="back()" class="cms_button" value="关闭">
							</div>
						</td>
					</tr>
				</table>

			
		</form>

		<iframe name="addgather" width="0" height="0">
		</iframe>

	</body>
</html>
