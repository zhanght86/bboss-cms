<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role" %>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ include file="../../include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%  
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
    String rolecopyId = request.getParameter("roleId");
    if(rolecopyId == null)
    	rolecopyId = (String) request.getAttribute("roleId");
    
    RoleManager roleManager = SecurityDatabase.getRoleManager();
	Role role = roleManager.getRoleById(rolecopyId);
	String roleName = role.getRoleName();
	session.setAttribute("roleTabId", "5"); 
	String admin = "false";
	if ( role.getRoleName().equals("administrator") || role.getRoleName().equals("admin")){
		admin = "true";
	}
%>


<html>
	<head>
	    <script language="JavaScript" src="../../../scripts/common.js" type="text/javascript"></script>
	    <script language="javascript" src="../scripts/selectTime.js"></script>
		<SCRIPT language="JavaScript" SRC="validateForm.js"></SCRIPT>
		<link href="Images/style.css" rel="stylesheet" type="text/css">
        <%@ include file="/include/css.jsp"%>
        <link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
        <link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
        <%@ include file="/include/css.jsp"%>
        <link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
        <link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
        
</head>

<script language="javascript">
var isExist = "<%=admin%>";

	
function checkAll(totalCheck,checkName){
   var selectAll = document.getElementsByName(totalCheck);
   var o = document.getElementsByName(checkName);
   if(selectAll[0].checked==true){
	   for (var i=0; i<o.length; i++){
      	  if(!o[i].disabled){
      	  	o[i].checked=true;
      	  }
	   }
   }else{
	   for (var i=0; i<o.length; i++){
   	  	  o[i].checked=false;
   	   }
   }
}

//????????????????
function checkOne(totalCheck,checkName){
   var selectAll = document.getElementsByName(totalCheck);
   var o = document.getElementsByName(checkName);
	var cbs = true;
	for (var i=0;i<o.length;i++){
		if(!o[i].disabled){
			if (o[i].checked==false){
				cbs=false;
			}
		}
	}
	if(cbs){
		selectAll[0].checked=true;
	}else{
		selectAll[0].checked=false;
	}
}

function copyroleres(copyType) {
		if ( isExist == "true"){
			alert("系统管理员的权限不能复制！");
			return;
		}
	    var checks = "";
	    var arr = new Array();
	    arr = document.getElementsByName("checkBoxOne");
	    for(var i = 0; i < arr.length; i++){
	    	if(arr[i].checked){
	    		if(checks == ""){
	    			checks = arr[i].value;
	    		}else{
	    			checks += "," + arr[i].value;
	    		}
	    	}
	    }
	    var outMsg;
	    if (checks != ""){
	    	if (copyType==1){
	    		outMsg = "你确定要复制所选的角色的权限 给角色<%=roleName%>吗？";
	        	if (confirm(outMsg)){
					rolecopyList.action="roleCopySelf_do.jsp?roleName=<%=roleName%>&checks="+checks;
					rolecopyList.target = "hiddenFrame";
				    rolecopyList.submit();
				}
			} 
	    }else{
	    	alert("至少要选择一条记录！");
	    	return false;
	    }
		return false;
	}
</script>

<body>

<form name="rolecopyList" method="post" >
					<input name="rolecopyId" value="<%=rolecopyId%>" type="hidden">
					<table cellspacing="1" cellpadding="0" border="0" width=98% class="thin">
         				<tr valign='top'>
           					<td align="center" colspan="5">&nbsp;<strong>将以下角色权限赋给角色：<%=roleName%></strong></td>
         				</tr>
         			<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.RoleCopy" keyName="RoleCopy" />
                        <!--分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="15" scope="request" data="RoleCopy" isList="false">
							<tr>
                        <!--设置分页表头-->
						<td class="headercolor" width="10">
									<input type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','checkBoxOne')" width="10">
							  </td>
								<td class="headercolor">角色名称</td>
								<td class="headercolor">角色类型名称</td>
						    </tr>
						    <pg:param name="rolecopyId" />
						    <pg:param name="roleId" />
			            <!--检测当前页面是否有记录-->
							<pg:notify>
								<tr height="18px" class="labeltable_middle_tr_01">
									<td colspan=100 align='center'>
										该角色暂时没有权限
									</td>
								</tr>
							</pg:notify>
							

                        <!--list标签循环输出每条记录-->
							<pg:list>								
								<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'" onclick="" >
									<td class="tablecells" nowrap="nowrap">
										<input type="checkBox" name="checkBoxOne" onClick="checkOne('checkBoxAll','checkBoxOne')" value="<pg:cell colName="roleId" defaultValue=""/>"  width="10">
									</td>
									<td class="tablecells" nowrap="nowrap" height='20'>
										<pg:cell colName="roleName" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap" height='20'>
										<pg:cell colName="roleTypeName" defaultValue="" />
									</td>
						        </tr>
							</pg:list>
			               <tr height="18px" class="labeltable_middle_tr_01">
								<td colspan=4 align='center'>
									<pg:index /><input type="submit" value="确定" class="input" onclick="javascript:copyroleres(1); return false;">	
									
								</td>						
							
						  </tr>
							
						
							<input name="queryString" value="<pg:querystring/>" type="hidden">
							
						</pg:pager>

  </table>
</form>
<iframe width="0" height="0" name="hiddenFrame"></iframe>
<%@ include file="../../sysMsg.jsp"%>
			</body>
</html>
