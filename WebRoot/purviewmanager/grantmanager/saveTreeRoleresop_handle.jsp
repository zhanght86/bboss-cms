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
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@page import="com.frameworkset.platform.security.AccessControl,
			com.frameworkset.platform.sysmgrcore.purviewmanager.db.PurviewManagerImpl,
			com.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	
	String resTypeId = request.getParameter("resTypeId");
	String currRoleId = request.getParameter("currRoleId");
	String role_type = request.getParameter("role_type");
	String[] checkedValues = request.getParameter("checkValues")==null?null:request.getParameter("checkValues").split(",");
	String[] un_checkValues = request.getParameter("un_checkValues")==null?null:request.getParameter("un_checkValues").split(",");
	String opId = request.getParameter("opId");
	String isBatch = request.getParameter("isBatch");
	
	PurviewManager purviewManager = new PurviewManagerImpl();
	
	boolean state = false;
	if(isBatch.equals("false")){
		
		if(checkedValues!=null&&checkedValues[0].equals("")){
			checkedValues=null;
		}
		state = purviewManager.saveTreeRoleresop(opId,resTypeId,checkedValues,currRoleId,role_type,resTypeId,un_checkValues);
	}else{
		state = purviewManager.batchSaveTreeRoleresop(opId,resTypeId,checkedValues,currRoleId,role_type);
	}
%>		
<%
	if(state){
%>
<script language="Javascript">
	var api = parent.parent.parent.frameElement.api, W = api.opener;
	W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.success'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
</script>
<%}else{%>
<script language="Javascript">
	W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.fail.nocause'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
</script>
<%}%>
