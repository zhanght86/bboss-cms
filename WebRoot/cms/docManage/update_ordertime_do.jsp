<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*" %>
<%@page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="java.util.*"%>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    
    
    String docId = request.getParameter("docId");
    docId = docId==null?"":docId;
    
    Date ordertime = null;
    java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    if(request.getParameter("ordertime") != null)
        ordertime = sf.parse(request.getParameter("ordertime"));
    
    DocumentManagerImpl impl = new DocumentManagerImpl();
    impl.updateDocOrdertime(docId,ordertime);
%>

<script>
    alert("文档排序更新成功!");
    window.parent.setReturnValue();
</script>
    
    