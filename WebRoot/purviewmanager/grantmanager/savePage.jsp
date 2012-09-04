<%
/**
 * 
 * <p>Title: "角色授予"权限翻页处理页面</p>
 *
 * <p>Description: "角色授予"权限翻页处理页面</p>
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
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManager,
				com.frameworkset.platform.sysmgrcore.purviewmanager.db.PurviewManagerImpl"%>
				
<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkManagerAccess(request,response);
    PurviewManager purviewManager = new PurviewManagerImpl();
    
    String opId = request.getParameter("opId");
    String resTypeId = request.getParameter("resTypeId");
	String currRoleId = request.getParameter("currRoleId");
	String role_type = request.getParameter("role_type");
	String currOrgId = request.getParameter("currOrgId");
	
	String PAGE_QUERY_STRING = (String)request.getParameter("pager.PAGE_QUERY_STRING")==null?"":request.getParameter("pager.PAGE_QUERY_STRING");
	
	//选中的
    String checkValue = request.getParameter("checkValues");
    //System.out.println("checkValue = " + checkValue);
    String[] checkValues = null;
    if(!"".equals(checkValue)){
    	checkValues = checkValue.split(",");
    }
    //未选中的
    String un_checkValue = request.getParameter("un_checkValues");
    //System.out.println("un_checkValue = " + un_checkValue);
    String[] un_checkValues = null;
    if(!"".equals(un_checkValues)){
    	un_checkValues = un_checkValue.split(",");
    }
    
    boolean state = purviewManager.saveRoleListRoleresop(opId,resTypeId,checkValues,un_checkValues,currRoleId,role_type);
    //System.out.println("state = " + state);
%>				
<%
	if(state){
%>
	<script language="Javascript">
		//alert(parent.document.location.href);
		parent.document.location.href = "rolesetTree.jsp?<%=PAGE_QUERY_STRING%>";
	</script>
<%
	}else{
%>
	<script language="Javascript">
		alert("保存失败");
	</script>
<%
	}
%>
