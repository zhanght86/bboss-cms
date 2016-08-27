<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);	
	
	String did = request.getParameter("did");
	String docid = request.getParameter("docid");
	String[] doc = docid.split(",");
	DictManager dictManager = new DictManagerImpl();
	boolean state = dictManager.updateDictArr(did, doc);
%>

<script>
	if(<%=state%>){
		$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.sort.save.success"/>');
		parent.dictList.document.location.href = parent.dictList.document.location.href;
	}else{
		$.dialog.alert('<pg:message code="sany.pdp.dictmanager.dict.sort.save.fail"/>');
		parent.dictList.document.location.href = parent.dictList.document.location.href;
	}
</script>
