<%
/**
 * <p>Title: 删除角色类型页面</p>
 * <p>Description:删除角色类型类型列表页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-19
 * @author baowen.liu
 * @version 1.0
 **/
 %>
<%@ page contentType="text/html; charset=UTF-8" language="java" errorPage="" %>
<%@ taglib   uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ taglib   uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleTypeManager"%>
<%@ page import="java.util.*" %>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<html>
<head>
<title><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.remove"/></title>
<style type="text/css">
<!--
.STYLE2 {	color: #263F77;
	font-weight: bold;
}
.STYLE3 {color: #FF0000}
-->
</style>
</head>
<body>
<%			
			AccessControl accessControl = AccessControl.getInstance();
			accessControl.checkManagerAccess(request,response); //页面登录保护
			
			boolean tag = true;
			String notice = "";

			//删除操作
			RoleTypeManager rtm = new RoleTypeManager();
			String id = request.getParameter("roletype_id");
			if ((id != null) && (id.length() > 0)) {
				StringTokenizer stValues = new StringTokenizer(id, ";");
				
				while (stValues.hasMoreElements()) {
					String nextId = (String) stValues.nextElement();
					String name = rtm.getRoleType(nextId).getTypeName();
					if (rtm.hasRoles(nextId))//已经有角色的类别不能被删除
					{
						notice = notice + RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.type", request) +"‘" + name + "’"+  RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.type.remove.exist", request)+ "\\n";
					} 
					else
					{
						boolean delTag = rtm.delRoleType(nextId);
						if(!delTag)
						{							
							notice = notice + RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.type", request) +"‘" + name + "’"+ RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.type.remove.fail", request) +"\\n";
						}
						else
						{
							notice = notice + RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.type", request) +"‘" + name + "’"+ RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.role.type.remove.success", request) +"\\n";
						}
					}
				}
			}

		%>
<script language="javascript">
	var api = frameElement.api, W = api.opener;
	
	W.$.dialog.alert("<%=notice%>", function() {
		//W.document.all.roletypelist.src = 'roletypelist.jsp?typeName='+parent.document.all("typeName").value+'&typeDesc='+parent.document.all("typeDesc").value;
		W.location.reload();
	}, api);  
</script>
</body>
</html>
<SCRIPT>
    window.onload = function prompt()
    {
        //parent.divProcessing.style.display="none";
        //这样确保在删除完成后刷新列表
        //parent.document.all.roletypelist.src = 'roletypelist.jsp?typeName='+parent.document.all("typeName").value+'&typeDesc='+parent.document.all("typeDesc").value;
    }
</script>

