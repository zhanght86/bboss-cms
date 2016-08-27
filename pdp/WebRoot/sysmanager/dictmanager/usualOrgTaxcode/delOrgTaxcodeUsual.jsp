<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.net.URLDecoder"%>
<%
    //可见关系维护-----------------------------------------------    
    AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	DictManager dictManager = new DictManagerImpl();
    
    String dicttypeId = request.getParameter("dicttypeId");
    String orgId = request.getParameter("orgId");
    String state = request.getParameter("state");
    String hintStr = "";
    if(state.equals("usual")){
    	String dictdataValues = request.getParameter("dictdataValues");
    	String[] dictdataValue = dictdataValues.split(",");
    	dictManager.delete_UsualOrgTaxcode(orgId,dicttypeId,dictdataValue);
    	hintStr = "delUsual";
    }
    if(state.equals("allUsual")){
    	String identifier = request.getParameter("identifier");
    	String[] dictAlldataValue = null;
    	dictAlldataValue = dictManager.getUsualDictdata(dicttypeId,orgId,identifier);
    	dictManager.delete_UsualOrgTaxcode(orgId,dicttypeId,dictAlldataValue);
    	hintStr = "delAllUsual";
    }
%>
<script language = "javascript">
<%if(hintStr.equals("delUsual")){%>
	alert("删除已设置项成功！");
	parent.document.location.href = parent.document.location.href;
	parent.parent.refurbishIframeSelected();  
<%}%>

<%if(hintStr.equals("delAllUsual")){%>
	alert("删除全部已设置项成功！");
	parent.document.location.href = parent.document.location.href;
	parent.parent.refurbishIframeSelected();  
<%}%>

<%if(hintStr.equals("")){%>
	alert("操作失败！");
<%}%>
</script>
