<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%
		AccessControl accesscontroler = AccessControl.getInstance();
	    accesscontroler.checkAccess(request, response);

		response.setHeader("Cache-Control", "no-cache"); 
		response.setHeader("Pragma", "no-cache"); 
		response.setDateHeader("Expires", -1);  
		response.setDateHeader("max-age", 0);

		String channelname = request.getParameter("channelName");
		String siteid = request.getParameter("siteid");
		String channelId = request.getParameter("channelId");
		String[] docid = request.getParameterValues("ID");
		
		String flag = request.getParameter("flag");
		String list = request.getParameter("list");
		//System.out.println("/////////"+flag);
		DocumentManager dm = new DocumentManagerImpl();
		if(flag.equals("1")){
			if (docid != null) {
				for (int i = 0; i < docid.length; i++) {
					//System.out.println(">>"+docid[i]);
					
					dm.deleteDoc(Integer.parseInt(docid[i]));
				
				}
				if(list.equals("1")){
					%>
					<font size=2 color=blue>删除文档成功,正在处理您的信息，稍后自动返回！</font><meta http-equiv='refresh' content='2;url=/creatorcms/cms/docManage/garbage_list.jsp?siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>&title=null&doclevel=null&status=null&user=null&doctype=null&flag=2'>
				
					<%
				}else{
					%>
					<font size=2 color=blue>删除文档成功,正在处理您的信息，稍后自动返回！</font><meta http-equiv='refresh' content='2;url=/creatorcms/cms/channelManage/channel_doc_list.jsp?siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>'>
				
					<%
				}
				
			}
		}else if(flag.equals("0")){
			if (docid != null) {
				for (int i = 0; i < docid.length; i++) {
					dm.GarbageDoc(Integer.parseInt(docid[i]));
				
				}
				%>
				<font size=2 color=blue>删除文档成功,文档已被放入回收站,正在处理您的信息，稍后自动返回！</font><meta http-equiv='refresh' content='2;url=/creatorcms/cms/channelManage/channel_doc_list.jsp?siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>'>
				
				<%
			}
		}else{
			if (docid != null) {
				for (int i = 0; i < docid.length; i++) {
					dm.RecoverDoc(Integer.parseInt(docid[i]));
			
				}
				%>
				<font size=2 color=blue>恢复文档成功,正在处理您的信息，稍后自动返回！</font><meta http-equiv='refresh' content='2;url=/creatorcms/cms/channelManage/channel_doc_list.jsp?siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>'>
				
				<%
			}
		
		}

		
%>


