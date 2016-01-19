<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.config.model.Operation"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.ResManager,com.frameworkset.platform.resource.ResourceManager"%>
<%
		AccessControl control = AccessControl.getInstance();
		boolean b = control.checkAccess(request,response);
		String userId = control.getUserID();
		String restypeId=request.getParameter("restypeId");
		if(restypeId == null || "".equals(restypeId)){
		out.print("<option value=''>--请选择--</option>");
		}else{
		
		
		ResourceManager resManager = new ResourceManager();
		
		
		List Operationslist = null; 
		Operationslist = resManager.getOperations(restypeId);
		
		List globalOperationlist = null;
		globalOperationlist = resManager.getGlobalOperations(restypeId);
		
		
		if(Operationslist == null)
		{
			Operationslist = new ArrayList();
		}
		
		if(globalOperationlist == null)
		{
			//Operationslist.addAll(globalOperationlist);
			globalOperationlist = new ArrayList();
		}
		request.setAttribute("Operationslist",Operationslist);
		request.setAttribute("globalOperationlist",globalOperationlist);
		

		StringBuilder options = new StringBuilder();
		options.append("<option value=''>--请选择--</option>");
		for(int i = 0; i < Operationslist.size(); i ++)
		{
			Operation op = (Operation)Operationslist.get(i);
			options.append("<option value='"+op.getId()+"'>").append(op.getName()).append("</option>");
		}
		
		for(int i = 0; i < globalOperationlist.size(); i ++)
		{
			Operation op = (Operation)globalOperationlist.get(i);
			options.append("<option value='"+op.getId()+"'>").append(op.getName()).append("</option>");
		}
		out.print(options.toString());
			
	}
%>
		
