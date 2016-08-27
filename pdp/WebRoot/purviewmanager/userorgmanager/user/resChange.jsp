<%
/*
 * <p>Title: 获取机构资源操作</p>
 * <p>Description: 获取机构资源操作</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-26
 * @author liangbing.tao
 * @version 1.0
 *
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>

<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>


<%
		AccessControl control = AccessControl.getInstance();
		control.checkManagerAccess(request,response);
		
		String restypeId=request.getParameter("restypeId");
		ResourceManager resManager = new ResourceManager();
		List Operationslist = resManager.getOperations(restypeId);
		
		List globalOperationlist = resManager.getGlobalOperations(restypeId);
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
	     
	<pg:list requestKey="Operationslist" >
	    var option = new Option();
		option.value = '<pg:cell colName="id"/>';
	    option.text = '<pg:cell colName="name"/>';
		operselect.add(option);
	</pg:list>			
	<pg:list requestKey="globalOperationlist" >
	    var option = new Option();
		option.value = '<pg:cell colName="id"/>';
	    option.text = '<pg:cell colName="name"/>';
		operselect.add(option);
	</pg:list>
</script>
		
