<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.frameworkset.platform.cms.docsourcemanager.*"%>
<%
	Docsource docsource= new Docsource();
	docsource.setSRCNAME(request.getParameter("srcname"));
	docsource.setSRCDESC(request.getParameter("srcdesc"));
	docsource.setSRCLINK(request.getParameter("srclink"));
	docsource.setDOCSOURCE_ID(Integer.parseInt(request.getParameter("docId")));


	if(!request.getParameter("srcname").equals("")){
		DocsourceManager dm = new DocsourceManagerImpl();
		boolean b=dm.updateDsrc(docsource);
		if(b==false){%>
			<font size=2 color=blue>该文档来源已存在!</font><meta http-equiv='refresh' content='2;url=<%=request.getContextPath()%>/cms/docsourceManage/docsource_creator.jsp'>
		<%}else{%>
			<font size=2 color=blue>文档来源信息修改成功,正在处理您的提交信息，稍后自动返回!</font><meta http-equiv='refresh' content='2;url=<%=request.getContextPath()%>/cms/docsourceManage/docsource_list.jsp'>
		<%}
	}else{
		if(request.getParameter("srcname").equals("") && request.getParameter("cruser").equals("")){%>
			<font size=2 color=blue>文档名称处和创建者处不能为空!</font><meta http-equiv='refresh' content='2;url=<%=request.getContextPath()%>/cms/docsourceManage/docsource_creator.jsp'>
		<%}else{
			if(request.getParameter("srcname").equals("")){%>
				<font size=2 color=blue>文档名称处不能为空!</font><meta http-equiv='refresh' content='2;url=<%=request.getContextPath()%>/cms/docsourceManage/docsource_creator.jsp'>
			<%}else{%>
				<font size=2 color=blue>创建者处不能为空!</font><meta http-equiv='refresh' content='2;url=<%=request.getContextPath()%>/cms/docsourceManage/docsource_creator.jsp'>
			<%}%>
		<%}%>
	<%}
%>