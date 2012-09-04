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
    
    String dicttypeId = request.getParameter("sendDicttypeId");
    String orgId = request.getParameter("sendOrgId");
    String readOp = request.getParameter("readOp");
    String data_org = request.getParameter("data_org");
    //用户所属机构ID
    String curOrg = accesscontroler.getChargeOrgId();
    String PAGE_QUERY_STRING = (String)request.getParameter("pager.PAGE_QUERY_STRING")==null?"":request.getParameter("pager.PAGE_QUERY_STRING");
    
    if(readOp.equals("save")){
    	String dictdataValues = request.getParameter("selectedNodeValue");
    	dictdataValues = URLDecoder.decode(dictdataValues,"utf-8");
    	String[] dictdataValue = dictdataValues.split(",");
    	if(!data_org.equals("") && data_org != null){
    		if(accesscontroler.isAdmin()){
	    		dictManager.storeReadOrgTaxcodeorAdmin(orgId,dicttypeId,dictdataValue);
	    	}else{
	    		dictManager.storeReadOrgTaxcode(curOrg,orgId,dicttypeId,dictdataValue);
	    	}
    	}else{
    		dictManager.storeReadOrgTaxcode(orgId,dicttypeId,dictdataValue);
    	}
    }
    if(readOp.equals("del")){
    	String dictdataValues = request.getParameter("selectedNodeValue");
    	dictdataValues = URLDecoder.decode(dictdataValues,"utf-8");
    	String[] dictdataValue = dictdataValues.split(",");
    	dictManager.deleteReadOrgTaxcode(orgId,dicttypeId,dictdataValue);
    }
%>
<script language="javascript">
<%if(readOp.equals("save")){%>
	parent.document.location.href = "org_dictdataList.jsp?<%=PAGE_QUERY_STRING%>";
	parent.parent.refurbishIframe(); 
<%}%>

<%if(readOp.equals("del")){%>
	parent.document.location.href = "org_dictdataSelectedList.jsp?<%=PAGE_QUERY_STRING%>";
	parent.parent.refurbishIframeSelected(); 
<%}%>

<%if(readOp.equals("")){%>
	var str = parent.document.location.href;
	str = str.substring(0,str.indexOf("?"));
	parent.document.location.href = str+"?<%=PAGE_QUERY_STRING%>";
<%}%>
</script>

