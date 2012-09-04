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
	int b = dm.resumeDocVersion(Integer.parseInt(docid),Integer.parseInt(version),Integer.parseInt(userid));
	//添加版本恢复日志
	dm.recordDocOperate(Integer.parseInt(docid),Integer.parseInt(userid),"版本恢复",-1,"版本恢复:恢复到版本"+version);
	
	if(b!=0){%>
		<SCRIPT LANGUAGE="JavaScript"> 
			alert("恢复文档版本成功！");
			var str = window.dialogArguments.dialogArguments.location.href;
			var strArray = str.slice(0,str.indexOf("?"));
			window.dialogArguments.dialogArguments.location.href = strArray+"?"+window.dialogArguments.dialogArguments.document.all.queryString.value;
			//window.close();
			window.dialogArguments.close(); 
		</script>
	
	<%}else{%>
		
		<SCRIPT LANGUAGE="JavaScript"> 
			alert("恢复文档版本失败！");
			window.close();
		</script>
		
	<%
		}
	%>			