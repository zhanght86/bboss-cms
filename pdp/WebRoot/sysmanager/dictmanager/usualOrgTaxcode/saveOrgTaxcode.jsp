<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.net.URLDecoder"%>
<%
    //常用关系维护-----------------------------------------------
    AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
    
     
    //直接保存 还是翻页保存 op=direct 直接保存
    String op = (String)request.getParameter("op")==null?"":(String)request.getParameter("op");
    String orgId = (String)request.getParameter("sendOrgId")==null?"":(String)request.getParameter("sendOrgId");
    String dicttypeId = (String)request.getParameter("sendDicttypeId")==null?"":(String)request.getParameter("sendDicttypeId");
    String datas = (String)request.getParameter("selectedNodeValue")==null?"":(String)request.getParameter("selectedNodeValue");
    String un_datas = (String)request.getParameter("un_selectedNodeValue")==null?"":(String)request.getParameter("un_selectedNodeValue");
    String PAGE_QUERY_STRING = (String)request.getParameter("pager.PAGE_QUERY_STRING")==null?"":request.getParameter("pager.PAGE_QUERY_STRING");
    
    //编码--解码  utf-8
    if(!"direct".equals(op)){//翻页的时候,才需要做解码, 直接保存, 不需要反编码
	    datas = URLDecoder.decode(datas,"utf-8");
	    un_datas = URLDecoder.decode(un_datas,"utf-8");
    }
    
    String[] dictdataValues = null;
    String[] un_dictdataValues = null;
    if(datas!=null){
    	dictdataValues = datas.split(",");
    }
    if(un_datas!=null){
    	un_dictdataValues = un_datas.split(",");
    }
    DictManager dictManager = new DictManagerImpl(); 
    dictManager.init(request, response);
    String errorMsg = "";
    if(dictdataValues!=null && un_dictdataValues!=null ){
        //dictManager.storeUsualOrgTaxcode(orgId,dicttypeId,dictdataValues);
        try{
    		dictManager.storeUsualOrgTaxcode(orgId,dicttypeId,dictdataValues,un_dictdataValues);
    	}catch(Exception e){
    		errorMsg = accesscontroler.formatErrorMsg(e.getMessage());
    		e.printStackTrace();
    	}
    }
    
%>
<script languange = "javascript">
<%    
    if(!"".equals(PAGE_QUERY_STRING)){
%>
        parent.location.href = "org_dictdataList.jsp?<%=PAGE_QUERY_STRING%>";        

<%
    }else{    
%>
		var errorMsg = "<%=errorMsg%>";
		if(errorMsg == ""){
			parent.win.alert("操作成功!");
		}else{
			parent.win.alert("操作失败!原因如下：\n"+errorMsg);
		}
		parent.win.close();
	    
<%
    }
%>
</script>
