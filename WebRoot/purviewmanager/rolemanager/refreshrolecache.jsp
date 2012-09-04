<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.RoleCacheManager"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%
/**
 * 
 * <p>Title: 角色刷新缓冲处理页面</p>
 *
 * <p>Description: 角色刷新缓冲处理页面</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: chinacreator</p>
 * @Date 2008-10-14
 * @author gao.tang
 * @version 1.0
 */
 %>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request, response);
	
	String errorMessage = "";
	RoleCacheManager roleCacheManager = RoleCacheManager.getInstance();
	try{
		roleCacheManager.reset();
	}catch(Exception e){
		errorMessage = e.getMessage();
	}
%>
<script language="javascript">
<%if("".equals(errorMessage)){ %>
	$.dialog.alert('<pg:message code="sany.pdp.common.operation.success"/>');
<%}else{ %>
	$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.type" arguments="\\n<%=errorMessage %>"/>');
<%} %>
</script>
