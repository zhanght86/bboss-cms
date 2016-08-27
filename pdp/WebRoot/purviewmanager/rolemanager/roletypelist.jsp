
<%@page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%><%
/**
 * <p>Title: 显示角色类型列表页面</p>
 * <p>Description:显示角色类型列表页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-19
 * @author baowen.liu
 * @version 1.0
 **/
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenu"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuImpl"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.Menu"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
 			AccessControl accesscontroler = AccessControl.getInstance();
	        accesscontroler.checkManagerAccess(request,response);
	        //String userId = accesscontroler.getUserID();	
	        String typeName = ""; 
			typeName = request.getParameter("typeName");
			if(typeName == null)
			{
				typeName = "";
			} 
			String typeDesc = ""; 
			typeDesc = request.getParameter("typeDesc");
			if(typeDesc == null)
			{
				typeDesc = "";
			}     
			
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			UserManager userManager = SecurityDatabase.getUserManager();  
			
			boolean isAdmin = accesscontroler.isAdmin();
			String curOrgId = accesscontroler.getChargeOrgId();
%>
<html >
<head>				
	
	<title><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.manager"/></title>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

	<link rel="stylesheet" type="text/css" href="../css/treeview.css">
	<!-- 
	<script language="JavaScript" src="common.js" type="text/javascript"></script>
	 -->
	<script language="JavaScript" src="../scripts/func.js" type="text/javascript"></script>
	<SCRIPT language="javascript">
	
	
    function roleTypeInfo(typeId)
    {
    	var url="<%=request.getContextPath()%>/purviewmanager/rolemanager/roletype_modify.jsp?typeId="+typeId;
	 	 parent.W.$.dialog({title:'<pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.modfiy"/>',width:280,height:200, content:'url:'+url, parent:parent.api,currentwindow:this}); 
	 	
	 	 /*
    	var url = "roletype_modify.jsp?typeId="+typeId;
		var winValue = window.showModalDialog(url,window,"dialogWidth:"+(500)+"px;dialogHeight:"+(440)+"px;help:no;scroll:auto;status:no");
		if(winValue)
		{
			var typeName = parent.document.all("typeName").value;
			var typeDesc = parent.document.all("typeDesc").value;
			parent.document.all("roletypelist").src = 'roletypelist.jsp';
		}
		*/
    }
	</SCRIPT>	
</head>
<body>

	 <form name="RoleTypeList" method="post" >
	 <div id="changeColor">
	 	<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.RoleTypeList" keyName="RoleTypeList" />
	 	<pg:pager maxPageItems="10" id="RoleTypeList" scope="request" data="RoleTypeList" isList="false">
	 		<pg:param name="typeName1" value="typeName1"/>
	 		<pg:param name="typeDesc1" value="typeDesc1"/>
	 		<pg:param name="typeName" /> 
			<pg:param name="typeDesc" />    
			
			<pg:equal actual="${RoleTypeList.itemCount}" value="0" >
				<div class="nodata">
				<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
			</pg:equal> 
			<pg:notequal actual="${RoleTypeList.itemCount}"  value="0">
	 		
	 			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
	 				<pg:header>
			            <th align=center><input type="checkbox" name="checkBoxAll" onclick="checkAll('checkBoxAll','ID')"></th>
						
			       		<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.name"/></th>
			       		<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.description"/></th>
			       		<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.organization"/></th>
			       		<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.creator"/></th>
			       	</pg:header>
			       	
			       	<%ContextMenu contextmenu = new ContextMenuImpl();%>
			       	<pg:list autosort="false">
			       		<%
							String orgId = dataSet.getString("creatorOrgId");
							String userId = dataSet.getString("creatorUserId");
							String typeId = dataSet.getString("roleTypeID");
							Menu menu = new Menu();
							menu.setIdentity("oproletype_" + typeId);
							Menu.ContextMenuItem menuitem1 = new Menu.ContextMenuItem();
							menuitem1.setName(RequestContextUtils.getI18nMessage("sany.pdp.sys.info", request));
							menuitem1.setLink("javascript:roleTypeInfo('" + typeId + "')");
							menuitem1.setIcon("../images/rightmenu_images/doc_fbyl.gif");
							menu.addContextMenuItem(menuitem1);
							contextmenu.addContextMenu(menu);
						%>
						<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'">
							<td class="td_center">
									<input type="checkbox" name="ID" onclick="checkOne('checkBoxAll','ID')" value='<pg:cell colName="roleTypeID" defaultValue=""/>' 
									<%
										if(!isAdmin && !orgId.equals(curOrgId)){
											out.print(" disabled ");
										} 
									%>
									/>						
							</td>
							<td id="oproletype_<%=typeId%>" height='20'  bgcolor="#F6FFEF">
								<pg:cell colName="typeName" defaultValue="" />
							</td>		
							<td>
								<pg:cell colName="typeDesc" defaultValue="" />
							</td>
							<td>
								<% 
									if(orgManager.getOrgById(orgId) != null){
										out.print(orgId + " " + orgManager.getOrgById(orgId).getRemark5());
									}else{
										out.print(RequestContextUtils.getI18nMessage("sany.pdp.sys.unknow", request));
									}
								%>
							</td>
							<td>
								<% 
									if(userManager.getUserById(userId) != null){
										out.print(userManager.getUserById(userId).getUserName() + "(" + userManager.getUserById(userId).getUserRealname() + ")");
									}else{
										out.print(RequestContextUtils.getI18nMessage("sany.pdp.sys.unknow", request));
									}
								%>
							</td>
						</tr>
			       	</pg:list>
	 			</table>
	 			<%request.setAttribute("oproletype", contextmenu);%>
				<pg:contextmenu enablecontextmenu="true" context="oproletype" scope="request" />
	 		</div>
	 		<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
	 	</pg:notequal>	
	 	</pg:pager>
	 </form>
</body>
</html>

