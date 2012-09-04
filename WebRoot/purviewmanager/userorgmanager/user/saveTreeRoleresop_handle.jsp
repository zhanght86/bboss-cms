<%
/**
 * 
 * <p>Title: 树形--保存资源</p>
 *
 * <p>Description: 树形--保存资源处理页面</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: chinacreator</p>
 * @Date 2006-9-15
 * @author gao.tang
 * @version 1.0
 */
 %>
 <%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%@page import="com.frameworkset.common.poolman.DBUtil,
			com.frameworkset.platform.security.AccessControl,
			com.frameworkset.platform.sysmgrcore.purviewmanager.db.PurviewManagerImpl,
			com.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager"%>
			
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	
	String resTypeId = request.getParameter("resTypeId");
	String currRoleId = request.getParameter("currRoleId");
	String role_type = request.getParameter("role_type");
	String[] checkedValues = request.getParameterValues("checkboxValue");
	String opId = request.getParameter("opId");
	
	PurviewManager purviewManager = new PurviewManagerImpl();
	
	boolean state = purviewManager.batchSaveTreeRoleresop(opId,resTypeId,checkedValues,currRoleId,role_type);
%>		
<%
	if(state){
%>
<script language="Javascript">
	alert("操作成功！");
</script>
<%}else{%>
<script language="Javascript">
	alert("授权失败！");
</script>
<%}%>
