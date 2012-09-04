<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	
	DocumentManager dm = new DocumentManagerImpl();
	String userid = accesscontroler.getUserID();

	String docidStr = request.getParameter("docidStr");
	String verLable = request.getParameter("verLable");
	String verComment = request.getParameter("verComment");
	
	//得到文档id数据,为字符串数组
	String[] docid;
	int[] intDocid = null;
	if(docidStr != null){
		docid = docidStr.split(":");
		//得到文档id的整型数组
		intDocid = new int[docid.length];
		for(int i=0;i<docid.length;i++){
			intDocid[i] = Integer.parseInt(docid[i]);
		}
	}
	try{
		dm.addDocVersions(intDocid,Integer.parseInt(userid),verLable,verComment);
	%>
		<SCRIPT LANGUAGE="JavaScript"> 
			alert("保存文档版本成功！");
			window.close();
		</script>
	<%}catch(DocumentManagerException de){%>
		<SCRIPT LANGUAGE="JavaScript"> 
			alert("保存文档版本失败！");
			window.close();
		</script>
	<%
		}
	%>			