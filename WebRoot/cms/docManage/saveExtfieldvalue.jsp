<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>


<%	  
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request,response);
	
		String docid = request.getParameter("docid");
		String fieldid = request.getParameter("fieldid");
		String clobvalue = request.getParameter("clobvalue");
		String checked = request.getParameter("checked");
		String flag = request.getParameter("flag");
		//System.out.println("========"+docid);
		//System.out.println("========"+fieldid);
		//System.out.println("========"+clobvalue);
		//System.out.println("========"+checked);
		//System.out.println("======--=="+flag);
		DocumentManager dmi2=new DocumentManagerImpl();
		dmi2.saveExtFieldMap(docid,fieldid,clobvalue,checked,flag);
	
			
		
%>

