<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.frameworkset.platform.cms.docsourcemanager.*"%>
<%
	String[] docsrcid = request.getParameterValues("checkBoxOne");
	boolean b = false;
	if (docsrcid != null) {
		for (int i = 0; i < docsrcid.length; i++) {
			DocsourceManager addm = new DocsourceManagerImpl();
			b = addm.deleteDsrc(Integer.parseInt(docsrcid[i]));
		}
	}
	if(b == true)
	{
%>
<font size=2 color=blue>删除该文档来源成功,正在处理您的信息，稍后自动返回</font><meta http-equiv='refresh' content='2;url=<%=request.getContextPath()%>/cms/docsourceManage/docsource_list.jsp'>
<%
	}
	else{
	%>
<font size=2 color=blue>删除该文档来源失败,文档被关联引用，稍后自动返回</font><meta http-equiv='refresh' content='2;url=<%=request.getContextPath()%>/cms/docsourceManage/docsource_list.jsp'>
	<%
	}
%>