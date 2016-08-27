<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	DocumentManager dm = new DocumentManagerImpl();
	String action = request.getParameter("action");
	String recoverall = request.getParameter("recoverall");
	if("recover".equals(action)){
		try
		{
			int temp = 1;
			if("recoverall".equals(recoverall))
			{
				dm.recoverAllPublishingDoc();
			}
			else
			{
				String docId = request.getParameter("docId");
				String[] docIds = request.getParameterValues("ID");
				
				if(docId != null && docId.length()>0)
					temp = dm.recoverPublishingDoc(docId);
				else
					dm.recoverPublishingDocs(docIds);
			}
			if(temp == 1){
				out.println("<script language='javascript'>alert('恭喜，操作成功！');</script>");
			}else{
				out.println("<script language='javascript'>alert('正在发布中,不能恢复！！');</script>");
			}
		}catch(Exception e){
			out.println("<script language='javascript'>alert('操作失败！');</script>");
		}
	}
%>
<script language="javascript">
	var str = parent.document.location.href;
	var end = str.indexOf("?");
	var strArray;
	if(end != -1)
		strArray= str.slice(0,end);
	else
		strArray = str;
	parent.document.location.href = strArray+"?"+parent.document.all.queryString.value;
</script>