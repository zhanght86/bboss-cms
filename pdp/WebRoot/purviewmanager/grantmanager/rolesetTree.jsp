<%
/**
 * 
 * <p>Title: "角色授予"权限设置</p>
 *
 * <p>Description: "角色授予"权限设置页面</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: bboss</p>
 * @Date 2006-9-15
 * @author gao.tang
 * @version 1.0
 */
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.resource.ResourceManager,
				com.frameworkset.platform.config.model.Operation"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkManagerAccess(request,response);
    //当前用户ID
    String curUserId = accesscontroler.getUserID(); 
    String opId = "roleset";//角色设置
	String resTypeId = request.getParameter("resTypeId");
	String currRoleId = request.getParameter("currRoleId");
	String role_type = request.getParameter("role_type");
	String currOrgId = "";
	if(role_type.equals("user")){
		currOrgId = request.getParameter("currOrgId");
	}
	String isBatch = request.getParameter("isBatch");
	
	//是否需要资源来源
	boolean isResPath = (isBatch.equals("false") && role_type.equals("user"));
	
	ResourceManager resManager = new ResourceManager();

	Operation operation = resManager.getOperation(resTypeId,"roleset");
	String description = operation==null?"":operation.getDescription();
	
	
	//角色查询条件
	String roleName = ""; 
	roleName = request.getParameter("roleName");
	if(roleName == null)
	{
		roleName = "";
	}
	
%>
<html>
	<head>
		<title>属性容器</title>
		<script language="JavaScript" src="../scripts/common.js" type="text/javascript"></script>
<script>
function queryUser()
{	
	userList.action="rolesetTree.jsp";
	userList.submit();	
}

function turnPageSumbmitSet(){
	document.userList.target = "hiddenFrame";
	var checkValues = "";
	var un_checkValues = "";
	var arr = document.getElementsByName("checkBoxOne");
	if(arr){
		for(var i = 0; i < arr.length; i++){
			if(arr[i].checked){
				if(checkValues==""){
					checkValues = arr[i].value;
				}else{
					checkValues += "," + arr[i].value;
				}
			}else{
				if(un_checkValues==""){
					un_checkValues = arr[i].value;
				}else{
					un_checkValues += "," + arr[i].value;
				}
			}
		}
	}
	document.userList.checkValues.value = checkValues;
	document.userList.un_checkValues.value = un_checkValues;
	return true;
}

function saveReadorgname(){
	document.userList.target = "hiddenFrame";
	var checkValues = "";
	var un_checkValues = "";
	var arr = document.getElementsByName("checkBoxOne");
	if(arr){
		for(var i = 0; i < arr.length; i++){
			if(arr[i].checked){
				if(checkValues==""){
					checkValues = arr[i].value;
				}else{
					checkValues += "," + arr[i].value;
				}
			}else{
				if(un_checkValues==""){
					un_checkValues = arr[i].value;
				}else{
					un_checkValues += "," + arr[i].value;
				}
			}
		}
	}
	document.userList.checkValues.value = checkValues;
	document.userList.un_checkValues.value = un_checkValues;
	document.userList.action = "saveRolesetTree.jsp";
	document.userList.submit();
}
	
</script>
	</head>
	<body>
		<div class="mcontent">
				<div id="searchblock">
					<div  class="search_top" >
	    				<div class="right_top"></div>
	    				<div class="left_top"></div>
     				</div>
     				<div class="search_box">
     					<form name="userList" action="savePage.jsp" method="post" >
     						<input name="checkValues" type="hidden" value="" />
							<input name="un_checkValues" type="hidden" value="" />
							<input name="resTypeId" type="hidden" value="<%=resTypeId%>" />
							<input name="currRoleId" type="hidden" value="<%=currRoleId%>" />
							<input name="role_type" type="hidden" value="<%=role_type%>" />
							<input name="currOrgId" type="hidden" value="<%=currOrgId%>" />
							<input name="opId" type="hidden" value="<%=opId%>" />
							<input name="isBatch" type="hidden" value="<%=isBatch%>" />
							
							<table width="98.4%" border="0" cellspacing="0" cellpadding="0">
								<tr>
	      							<td class="left_box"></td>
	      							<td>
	      								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
	      									<tr>
	      										<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.name"/>：</th>
	      										<td><input type="text" name="roleName" value="<%=roleName%>" class="w120" /></td>
	      										<th>&nbsp;</th>
	      										<td>
           											<a href="javascript:void(0)" class="bt_1" name="search"  onclick="queryUser()"><span><pg:message code="sany.pdp.common.operation.search" /></span> </a>
           											<%if(isBatch.equals("false")){ %>   
           												<a href="javascript:void(0)" class="bt_2" name="sx"  onclick="parent.window.location.href = parent.window.location.href;"><span><pg:message code="sany.pdp.common.operation.refresh" /></span> </a>
           											<%} %>
           											<a href="javascript:void(0)" class="bt_1" name="saveButton"  onclick="saveReadorgname()"><span><pg:message code="sany.pdp.common.operation.save" /></span> </a>
	      										</td>
	      									</tr>
	      								</table>
	      							</td>
	      							<td class="right_box"></td>
	      						</tr>
							</table>
						</form>
     				</div>
     				<div class="search_bottom">
						<div class="right_bottom"></div>
						<div class="left_bottom"></div>
					</div>
				</div>
				
				<div id="changeColor">
					<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.purviewmanager.tag.RoleSearchList" keyName="RoleSearchList" />
					<pg:pager maxPageItems="12" scope="request" data="RoleSearchList"  isList="false">
						<pg:param name="roleId" />
						<pg:param name="roleName" />
						<pg:param name="resTypeId" />
						<pg:param name="currRoleId" />
						<pg:param name="role_type" />
						<pg:param name="currOrgId" />
						<pg:param name="isBatch" />
						
						<pg:equal actual="${UserSearchList.itemCount}" value="0" >
							<div class="nodata">
							<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
						</pg:equal>
						<pg:notequal actual="${UserSearchList.itemCount}"  value="0">
							<table width="98.5%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
								<pg:header>
									<th width="30px"><INPUT type="checkbox" name="checkBoxAll" onclick="checkAll('checkBoxAll','checkBoxOne')" value="on"></th>
									<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.name" /></th>
									<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.description" /></th>
									<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type" /></th>
									<%if(isResPath){ %>
									<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.resource.source" /></th>
									<%} %>							
								</pg:header>
								
								<pg:list>
							
							
								<tr
								<%
								  String resName = dataSet.getString("roleName");
								  String resId = dataSet.getString("roleId");
								  String ownerId = String.valueOf(dataSet.getInt("owner_id"));
								  String noCheck = "";
								  boolean isRoleSet = curUserId.equals(ownerId) || accesscontroler.checkPermission(resId,"roleset",AccessControl.ROLE_RESOURCE);
								  
							      if((!isRoleSet&&!accesscontroler.isAdmin()) || "1".equals(resId) || "2".equals(resId) || "3".equals(resId) || "4".equals(resId)){
							      	out.print(" disabled=\"true\"");
							      	noCheck = "noCheck";
							      }
							      
								%>
								>
									<td class="td_center">
												<INPUT type="checkbox" name="checkBoxOne<%=noCheck %>" onclick="checkOne('checkBoxAll','checkBoxOne')" value='<pg:cell colName="roleId" defaultValue=""/>#<pg:cell colName="roleName" defaultValue=""/>'
												<% 
													if(AccessControl.hasGrantedRole(currRoleId,role_type,resId,resTypeId)){
														out.println("checked");
													}
													String returnStrResouce = "";
										            if(isResPath){
										            	String returnStr = accesscontroler.getUserRes_jobRoleandRoleandSelf(currOrgId,currRoleId,resName,resTypeId,resId,opId);
										                returnStrResouce = accesscontroler.getSourceUserRes_jobRoleandRoleandSelf(currRoleId,currRoleId,resName,resTypeId,resId,opId);
										                if(returnStr.equals("1")){
										                    out.println(" disabled=\"true\" checked ");
										                }
										            }
												%>
												 >
										</P>
									</td>
									<td>
										<pg:cell colName="roleName" defaultValue="" />
									</td>
									<td>
										<pg:cell colName="roleDesc" defaultValue=""  maxlength="30"  replace="..."/>
									</td>
									<td>
										<pg:cell colName="roleType" defaultValue="" />
									</td>
									<%if(isResPath){ %>
									<td>
										<%=returnStrResouce %>
									</td>
									<%} %>
								</tr>
							</pg:list>
							</table>
							<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
						</pg:notequal>
					</pg:pager>
				</div>
		</div>
		
		<iframe height="0" width="0" name="hiddenFrame"></iframe>
	</body>
</html>

