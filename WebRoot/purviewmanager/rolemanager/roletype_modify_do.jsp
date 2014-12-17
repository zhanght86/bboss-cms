<%
/**
 * <p>Title: 角色类型修改处理页面</p>
 * <p>Description: 角色类型修改处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-19
 * @author baowen.liu
 * @version 1.0
 **/
 %>
 
<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.sql.*" errorPage="" %>
<%@ taglib   uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.RoleType"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleTypeManager
				,com.frameworkset.util.StringUtil"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<html>
<head>
<title>

</title>

<style type="text/css">
<!--
.STYLE2  {	color: #263F77;
	font-weight: bold;
}
.STYLE3 {color: #FF0000}
-->
</style>
</head>
<body>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);  //页面登录保护
%>
<% 
		RoleType rt = new RoleType();
		rt.setRoleTypeID(StringUtil.replaceNull(request.getParameter("typeId")));
		rt.setTypeName(StringUtil.replaceNull(request.getParameter("typeName")));
		rt.setTypeDesc(StringUtil.replaceNull(request.getParameter("typeDesc")));
		RoleTypeManager rtm = new RoleTypeManager();
		boolean flag = rtm.isRoleTypeExistForUpdate(rt);
		if(!flag)
		{
		if(rtm.modifyRoleType(rt))
		{
%>
		<script language="javascript">
			parent.W.$.dialog.alert('<pg:message code="sany.pdp.common.operation.success"/>', function(){parent.api.close();}, parent.api);
		</script>
<%
		}
		}
		else
		{
%>
<script language="javascript">
		parent.W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.name.exist"/>', function(){parent.api.close();}, parent.api);  
		</script>
		<%
		}
		%>
</body>
</html>
<script>
    window.onload = function prompt()
    {   
        parent.document.all.Submit4.disabled=false;
	    parent.document.all.btn_close.disabled=false;
        parent.divProcessing.style.display="none";
    }
</script>

