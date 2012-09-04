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
    String curOrg = accesscontroler.getChargeOrgId();
    String hintStr = "";
    if(state.equals("read")){
    	String dictdataValues = request.getParameter("dictdataValues");
    	String[] dictdataValue = dictdataValues.split(",");
    	dictManager.deleteReadOrgTaxcode(orgId,dicttypeId,dictdataValue);
    	hintStr = "delRead";
    }
    if(state.equals("allRead")){
    	String identifier = request.getParameter("identifier");
    	String[] dictAlldataValue = null;
    	if(accesscontroler.isAdmin()){
    		dictAlldataValue = dictManager.getDictdataValues(dicttypeId,orgId,identifier,true,"");
    	}else{
    		if(orgId.equals(curOrg)){
	    		dictAlldataValue = dictManager.getCurOrgSelfGatherDictDataValues(curOrg,dicttypeId,identifier);
	    	}else{
    			dictAlldataValue = dictManager.getDictdataValues(dicttypeId,orgId,identifier,false,accesscontroler.getUserID());
    		}
    	}
    	dictManager.deleteReadOrgTaxcode(orgId,dicttypeId,dictAlldataValue);
    	hintStr = "delAllRead";
    }
%>
<script language = "javascript">
<%if(hintStr.equals("delRead")){%>
	alert("删除已设置项成功！");
	parent.document.location.href = parent.document.location.href;
	parent.parent.refurbishIframeSelected();  
<%}%>

<%if(hintStr.equals("delAllRead")){%>
	alert("删除全部已设置项成功！");
	parent.document.location.href = parent.document.location.href;
	parent.parent.refurbishIframeSelected();  
<%}%>

<%if(hintStr.equals("")){%>
	alert("操作失败！");
<%}%>
</script>
