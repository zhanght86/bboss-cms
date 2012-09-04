<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	
	DocumentManager dm = new DocumentManagerImpl();
	String userid = accesscontroler.getUserID();
	String docid = request.getParameter("docid");
	String version = request.getParameter("version");
	int b = dm.delDocVersion(Integer.parseInt(docid),Integer.parseInt(version));
	
	if(b!=0){%>
		<SCRIPT LANGUAGE="JavaScript"> 
			alert("删除文档版本成功！");
			var str = window.dialogArguments.location.href;
			var strArray = str.slice(0,str.indexOf("?"));
			window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
			window.close();
		</script>
	
	<%}else{%>
		
		<SCRIPT LANGUAGE="JavaScript"> 
			alert("删除文档版本失败！");
			window.close();
		</script>
		
	<%
		}
	%>			