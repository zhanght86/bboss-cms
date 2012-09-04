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
    	dictManager.store_UsualOrgTaxcode(orgId,dicttypeId,dictdataValue);
    	hintStr = "saveUsual";
    }
    if(state.equals("allUsual")){
    	String identifier = request.getParameter("identifier");
    	String[] dictAlldataValue = null;
    	if(dictManager.getDicttype_type(dicttypeId) == DictManager.ALLREAD_BUSINESS_DICTTYPE){
    		dictAlldataValue = dictManager.getUsualDictdata(dicttypeId,orgId,identifier);
    	}else{
    		dictAlldataValue = dictManager.getUsuslSealedReadDictdataValus(dicttypeId,orgId);
    	}
    	dictManager.store_UsualOrgTaxcode(orgId,dicttypeId,dictAlldataValue);
    	hintStr = "saveAllUsual";
    }
%>
<script language = "javascript">
<%if(hintStr.equals("saveUsual")){%>
	alert("保存未设置项成功！");
	parent.document.location.href = parent.document.location.href;
	parent.parent.refurbishIframe();  
<%}%>

<%if(hintStr.equals("saveAllUsual")){%>
	alert("保存全部未设置项成功！");
	parent.document.location.href = parent.document.location.href;
	parent.parent.refurbishIframe();  
<%}%>

<%if(hintStr.equals("")){%>
	alert("操作失败！");
<%}%>
</script>
