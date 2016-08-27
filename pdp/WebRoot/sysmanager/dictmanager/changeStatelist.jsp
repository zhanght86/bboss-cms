<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);	
	String did = request.getParameter("did");
	String flag = request.getParameter("flag");
	String docid = request.getParameter("docid");
	String[] doc = docid.split(",");
	DictManager dictManager = new DictManagerImpl();
	boolean state = dictManager.changeState(did, doc, flag);
%>

<script>
	if(<%=state%>){
		alert("操作成功！");
  		parent.dictList.document.location.href = parent.dictList.document.location.href;
	}else{
		alert("操作失败！");
	}
</script>
