<%
/**
 * 
 * <p>Title: 高级字段排序页面</p>
 *
 * <p>Description: 高级字段排序页面</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: chinacreator</p>
 * @Date 2008-4-6
 * @author gao.tang
 * @version 1.0
 */ 
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	DictManager dictManager = new DictManagerImpl();
	
	String dicttypeId = request.getParameter("dicttypeId");
	String docid = request.getParameter("docid");
	boolean state = dictManager.storeAdvanceFieldArr(dicttypeId, docid);
%>

<%
	if(state){
%>
<script language="Javascript">
	alert("保存排序成功！");
	parent.document.location = parent.document.location;
</script>
<% 
	}else{
%>
	alert("保存失败！");
<%
	} 
%>
