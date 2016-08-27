<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
    AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
    
    String orgId = (String)request.getParameter("orgId");
    String dicttypeId = (String)request.getParameter("dicttypeId");
    String datas = (String)request.getParameter("dicttypeValues");        
    String[] dictdataValues = null;
    if(datas!=null){
    	dictdataValues = datas.split(",");
    }
    DictManager dictManager = new DictManagerImpl();    
    if(dictdataValues!=null){
        System.out.println(dictdataValues.length);
    	dictManager.storeUsualOrgTaxcode(orgId,dicttypeId,dictdataValues);
    }
    
%>
	<script language="javascript">		
		parent.win.alert("操作成功!");
		parent.win.close();
	</script>
