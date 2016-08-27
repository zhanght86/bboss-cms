<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);	
	
	String did = request.getParameter("did");
	String dictdataValue = request.getParameter("dictdata");
	String docid = request.getParameter("docid");
	String[] doc = docid.split(",");
	DictManager dictManager = new DictManagerImpl();
	boolean state = dictManager.updateDictArrTree(did, dictdataValue, doc);
%>

<script>
	if(<%=state%>){
		alert("保存排序成功！");
		parent.afterAddRefresh();
	}else{
		alert("保存排序失败！");
	}
</script>
