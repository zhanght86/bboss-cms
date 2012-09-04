<%
/*
 * <p>Title: 将其它角色复制给该角色</p>
 * <p>Description: 将其它角色复制给该角色</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-19
 * @author liangbing.tao
 * @version 1.0
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>

<%  
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
	
    String rolecopyId = request.getParameter("roleId");
    
    
    if(rolecopyId == null) 
    {
    	rolecopyId = (String) request.getAttribute("roleId");
    }
    
    
    RoleManager roleManager = SecurityDatabase.getRoleManager();

    
	Role role = roleManager.getRoleById(rolecopyId);
	
	String roleName = role.getRoleName();
	
	
	
	session.setAttribute("roleTabId", "5"); 
	String admin = "false";
	
	if ( role.getRoleName().equals("administrator") || role.getRoleName().equals("admin"))
	{
		admin = "true";
	}
%>


<html>
	<head>
        <script language="javascript">
			var isExist = "<%=admin%>";
			
			/*
			function checkAll(totalCheck,checkName)
			{
			   var selectAll = document.getElementsByName(totalCheck);
			   var o = document.getElementsByName(checkName);
			   if(selectAll[0].checked==true)
			   {
				   for (var i=0; i<o.length; i++)
				   {
			      	  if(!o[i].disabled)
			      	  {
			      	  	o[i].checked=true;
			      	  }
				   }
			   }
			   else
			   {
				   for (var i=0; i<o.length; i++)
				   {
			   	  	  o[i].checked=false;
			   	   }
			   }
			}
			
			function checkOne(totalCheck,checkName)
			{
			   var selectAll = document.getElementsByName(totalCheck);
			   var o = document.getElementsByName(checkName);
				var cbs = true;
				for (var i=0;i<o.length;i++)
				{
					if(!o[i].disabled)
					{
						if (o[i].checked==false)
						{
							cbs=false;
						}
					}
				}
				if(cbs)
				{
					selectAll[0].checked=true;
				}
				else
				{
					selectAll[0].checked=false;
				}
			}
			*/
			var api = parent.frameElement.api, W = api.opener;
			
			function copyroleres(copyType) 
			{
				if ( isExist == "true")
				{
					W.$.dialog.alert("<pg:message code='sany.pdp.purviewmanager.rolemanager.role.purview.copy.cannot'/>");
					return;
				}
			    var checks = "";
			    var arr = new Array();
			    arr = document.getElementsByName("CK");
			    for(var i = 0; i < arr.length; i++)
			    {
			    	if(arr[i].checked)
			    	{
			    		if(checks == "")
			    		{
			    			checks = arr[i].value;
			    		}
			    		else
			    		{
			    			checks += "," + arr[i].value;
			    		}
			    	}
			    }
			    var outMsg;
			    if (checks != "")
			    {
			    	if (copyType==1)
			    	{
			    		outMsg = '<pg:message code="sany.pdp.purviewmanager.rolemanager.role.purview.copy.to.role.confirm"  arguments="<%=roleName%>"/>';
			        	W.$.dialog.confirm(outMsg, function() {
			        		rolecopyList.action="roleCopySelf_do.jsp?roleName=<%=roleName%>&checks="+checks;
							rolecopyList.target="copySeftFrame" ;
						    rolecopyList.submit();
			        	});
			        		//document.getElementById("copy").disabled = true ;
							//document.getElementById("back").disabled = true ;
							//document.all.divProcessing.style.display = "block";
					} 
			    }
			    else
			    {
			    	W.$.dialog.alert('<pg:message code="sany.pdp.common.operation.select.one" />');
			    	return false;
			    }
				//return false;
			}
		</script>
	</head>
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
						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb" >
							<pg:header>
								<th width="30px"><input id="CKA" name="CKA" type="checkbox" 
							onClick="checkAll('CKA','CK')"></th>
								<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.name" /></td>
								<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.name" /></td>
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
		</div>
	
		</form>
	<br/>
	<iframe name="copySeftFrame" width="0" height="0"></iframe>
</body>
</html>
