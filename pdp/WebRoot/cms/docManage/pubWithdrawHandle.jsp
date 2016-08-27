<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%
	//待发文档的退回操作，将待发文档退回给提交人，文档变为返工文档

	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request,response);
	DocumentManager idm = new DocumentManagerImpl();
	int userId = Integer.parseInt(accesscontroler.getUserID());
	int docId = Integer.parseInt(request.getParameter("docId"));
	int taskId = Integer.parseInt(request.getParameter("taskId"));	
	try{
		int executer = idm.getPreSubmitterId(taskId);
		idm.toPubWithdraw(docId,taskId,userId,new int[]{executer});
		%>
			<script language = "javascript">
				alert("操作成功");
				var str = parent.document.location.href;
				var end = str.indexOf("?");
				var strArray;
				if(end != -1)
					 strArray= str.slice(0,end);
				else
					strArray = str;
				parent.document.location.href = strArray+"?" + parent.document.all.queryString.value;
			</script>
		<%
	}catch(DocumentManagerException de){
		de.printStackTrace();
		%>
		<script language="javascript">
			alert("数据库操作失败");
		</script>
		<%
	}
%>