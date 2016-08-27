<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.ResManager,com.frameworkset.platform.resource.ResourceManager"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/include/jquery-1.4.2.min.js"/>
<%
		AccessControl control = AccessControl.getInstance();
		boolean b = control.checkAccess(request,response);
		String userId = control.getUserID();
		String restypeId=request.getParameter("restypeId");
		if(restypeId == null || "".equals(restypeId)){
		%>
		<script language="javascript">
		var operselect = parent.document.getElementById("operategroup");
		var options_ = operselect.options;
		var length = options_.length;
		try
		{
			
			for(var i = 0; i < length - 1; i ++)
			{
				operselect.removeChild(options_(1));
				

			}

		}
		catch(e)
		{
			alert(e);
		}
		</script>
		<%
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
		
		
%>

		<script language="javascript">
	
		var operselect = parent.document.getElementById("operategroup");
		
		var options_ = operselect.options;
		var length = options_.length;
		try
		{
			
			for(var i = 0; i < length - 1; i ++)
			{
				operselect.removeChild(options_(1));
				

			}

		}
		catch(e)
		{
			alert(e);
		}
		var option = null;
			<pg:list requestKey="Operationslist" >
			    option = new Option();
				option.value = '<pg:cell colName="id"/>';
			    option.text = '<pg:cell colName="name"/>';
				operselect.add(option);
			</pg:list>
			<pg:list requestKey="globalOperationlist" >
			     option = new Option();
				option.value = '<pg:cell colName="id"/>';
			    option.text = '<pg:cell colName="name"/>';
				operselect.add(option);
			</pg:list>
			
			
		</script>
<% 
	}
%>
		
