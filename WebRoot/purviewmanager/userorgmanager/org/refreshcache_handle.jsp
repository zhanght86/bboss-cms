<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgAdminCache"%>
 <%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
 <%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%
/**
 * 
 * <p>Title: 机构,部门管理员可管理机构刷新缓冲处理页面</p>
 *
 * <p>Description: 机构,部门管理员可管理机构刷新缓冲处理页面</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: chinacreator</p>
 * @Date 2008-10-08
 * @author gao.tang
 * @version 1.0
 */
 %>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request, response);
	//flag值为1，刷新机构缓冲；flag值为2，刷新部门管理员可管理机构缓冲
	String flag = request.getParameter("flag");
	String errorMessage = "";
	if("1".equals(flag)){
		OrgCacheManager orgCacheManager = OrgCacheManager.getInstance();
		
		try{
			orgCacheManager.reset();
		}catch(Exception e){
			errorMessage = control.formatErrorMsg(e.getMessage());
		}
	}else if("2".equals(flag)){
		OrgAdminCache orgAdminCache = OrgAdminCache.getOrgAdminCache();
		try{
			orgAdminCache.reset();
		}catch(Exception e){
			errorMessage = control.formatErrorMsg(e.getMessage());
		} 
	}else if("3".equals(flag)){
		try{
			AccessControl.resetAuthCache();
		}catch(Exception e){
			errorMessage = control.formatErrorMsg(e.getMessage());
		}
		try{
			AccessControl.resetPermissionCache();
		}catch(Exception e){
			errorMessage += control.formatErrorMsg(e.getMessage());
		}
	}
%>
<script language="javascript">
<%if("".equals(errorMessage)){ %>
	$.dialog.alert('<pg:message code="sany.pdp.common.operation.success"/>', function() {
		parent.parent.location.href = parent.parent.location.href;
	});
<%}else{ %>
	$.dialog.alert('<pg:message code="sany.pdp.common.operation.fail" arguments="<%=errorMessage %>"/>');
<%} %>
</script>
