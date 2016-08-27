<%
/*
 * <p>Title: 岗位新增处理页面</p>
 * <p>Description: 岗位新增处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-19
 * @author baowen.liu
 * @version 1.0
 */
%>
<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.sql.*" errorPage="" %>
<%@ taglib   uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.RoleType"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleTypeManager"%>

<% 
	AccessControl accessControl =AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	
	String curUserId = accessControl.getUserID();
	String curOrgId = accessControl.getChargeOrgId();
%>
<script language="javascript">
	var api = parent.frameElement.api, W = api.opener;
</script>	
<% 
		RoleType rt = new RoleType();
		rt.setTypeName(request.getParameter("roletype").trim());
		rt.setTypeDesc(request.getParameter("typedesc"));
		rt.setCreatorOrgId(curOrgId);
		rt.setCreatorUserId(curUserId);
		RoleTypeManager rtm = new RoleTypeManager();
		boolean flag = rtm.isRoleTypeExist(rt);
		//System.out.println(flag);
		if(!flag)
		{
		if(rtm.addRoleType(rt))
		{
%>
		<script language="javascript">
		W.$.dialog.alert('<pg:message code="sany.pdp.common.operation.success"/>', function() {
			var win=api.config["currentwindow"];
			win.location.reload();
			api.close();
			//W.location.reload();
		}, api);
		
		</script>
<%
		}
		}
		else
		{
%>
<script language="javascript">
	W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.name.exist"/>');  
		</script>
		<%
		}
		%>

