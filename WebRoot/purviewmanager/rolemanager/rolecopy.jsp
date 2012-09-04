<%
/**
 * <p>Title: 角色复制</p>
 * <p>Description: 角色复制</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-4-10
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
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>


<%  
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
    String rolecopyId = request.getParameter("roleId");
    if(rolecopyId == null)
    {
    	rolecopyId = (String) request.getAttribute("roleId");
    }
    
    session.setAttribute("roleTabId", "5"); 
    
    
    String roleName = "";
    String admin = "false";
    RoleManager roleManager = SecurityDatabase.getRoleManager();
	Role role = roleManager.getRoleById(rolecopyId);
	if(role !=null)
	{
		roleName = role.getRoleName();	
		if ( role.getRoleName().equals("administrator") || role.getRoleName().equals("admin"))
		{
			admin = "true";
		}
	}
%>


<html>
	<head>
	</head>

<script language="javascript">
var isExist = "<%=admin%>";

/*
function myCheckAll(totalCheck,checkName){
	
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
function myCheckOne(totalCheck,checkName){
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
*/

var api = frameElement.api, W = api.opener;

function copyroleres(copyType)
 {
	if ( isExist == "true")
	{
		W.$.dialog.alert("<pg:message code='sany.pdp.purviewmanager.rolemanager.role.purview.copy.cannot' />");
		return;
	}
	
	var isSelect = false;
	var outMsg;
	var rolecopyList = document.rolecopyList;    
	
	for (var i=0;i<rolecopyList.elements.length;i++) 
	{
		var e = rolecopyList.elements[i];		
		if (e.name == 'CK')
		{
			if (e.checked)
			{
	       		isSelect=true;
	       		break;
		    }
		}
	}
	
    if (isSelect)
    {
    	if (copyType==1)
    	{
    		outMsg = '<pg:message code="sany.pdp.purviewmanager.rolemanager.role.purview.copy.confirm" arguments="<%=roleName%>"/>';
        	W.$.dialog.confirm(outMsg, function() {
        		rolecopyList.target="copyFrame" ;
				rolecopyList.action = "rolecopy_do.jsp";
			    rolecopyList.submit();
        	});
				//cument.location.href="accessmanager/roleManager.do?method=saveCopy";
				//lecopyList.submit();
				//rolecopyList.action="../../accessmanager/roleManager.do?method=saveCopy";
				
				//document.getElementById("copy").disabled = true ;
				//document.getElementById("back").disabled = true ;
				//document.all.divProcessing.style.display = "block";
				
				//rolecopyList.target="copyFrame" ;
				//rolecopyList.action = "rolecopy_do.jsp";
			    //rolecopyList.submit();
			    //alert("权限复制成功！");
	 			//return true;
			}
    }
    else
    {
    	W.$.dialog.alert('<pg:message code="sany.pdp.common.operation.select.one" />');
    	return false;
    }
	return false;
	}
</script>
<body>
<div id="customContent" >	

	<form name="rolecopyList" method="post" >
	<input name="rolecopyId" value="<%=rolecopyId%>" type="hidden">
	
	<div id="changeColor">
		<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.purviewmanager.tag.RoleCopy" keyName="RoleCopy" />
		<pg:pager maxPageItems="15" scope="request" data="RoleCopy" isList="false">
			<pg:param name="rolecopyId" />
			 <pg:param name="roleId" />
			 
			 <pg:equal actual="${RoleCopy.itemCount}" value="0" >
				<div class="nodata">
				<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
			</pg:equal>
			<pg:notequal actual="${RoleCopy.itemCount}"  value="0">
				<table width="98%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb" >
					 <pg:header>
					 	<th width="30px"><input id="CKA" name="CKA" type="checkbox" 
							onClick="checkAll('CKA','CK')"></th>
					 	<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.name" /></th>
					 	<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.name" /></th>
					 </pg:header>
					 <pg:list>
					 	<tr>
						<td class="td_center">
							<input id="CK" type="checkbox" name="CK" onClick="checkOne('CKA','CK')" value="<pg:cell colName="roleId" />"/>
						</td>
						<td>
							<pg:cell colName="roleName" defaultValue="" />
						</td>
						<td>
							<pg:cell colName="roleTypeName" defaultValue="" />
						</td>
			        </tr>
					 </pg:list>
				</table>
				<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
				<div class="btnarea" >
					<a href="javascript:void(0)" class="bt_1" id="add" name="add" onclick="copyroleres(1)"><span><pg:message code="sany.pdp.common.operation.ok"/></span></a>
					<a href="javascript:void(0)" class="bt_2" id="cancel" name="cancel" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.cancel"/></span></a>
				</div>
			</pg:notequal>
		</pg:pager>
	</div>
	</form>
	
	<br/>
	<iframe name="copyFrame" width="0" height="0"></iframe>
</div>
</body>
</html>
