<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.framework.MenuHelper,
				com.frameworkset.platform.framework.MenuItem,
				com.frameworkset.platform.framework.*"%>
<%@page import="com.frameworkset.platform.framework.Framework"%>

<%
	AccessControl  control = AccessControl.getInstance();
	control.checkAccess(request,response);
	
	String areaType = request.getParameter("areaType");
	
	String menuPath = "module::menu://sysmenu$root/sysmanager$module/testFrame$item";
	String contextPath = request.getContextPath();
	MenuHelper menuHelper = new MenuHelper(control);
	menuPath = "module::menu://sysmenu$root";
	String modulePath = "module::menu://sysmenu$root/sysmanager$module";
	//menuPath = menuHelper.getPublicItem().getPath();
	ModuleQueue moduleQueue = menuHelper.getSubModules(menuPath);
	for(int i = 0;  i < moduleQueue.size(); i ++)
	{
		Module module = moduleQueue.getModule(i);
		ItemQueue itemQueue = module.getItems();
		Item item = itemQueue.getItem(0);
		item.getName();
		String url = MenuHelper.getMainUrl(contextPath,item,(java.util.Map)null);
		module.getName();
		ModuleQueue moduleQueue_ = module.getSubModules();
		
	}
	ItemQueue itemQueueSub = menuHelper.getSubItems(modulePath);
	ItemQueue itemQueue = menuHelper.getItems();
	ModuleQueue moduleQueueF = Framework.getInstance().getModules();
	ItemQueue itemQueueF = Framework.getInstance().getItems();
	if(moduleQueue != null){
		System.out.println("moduleQueue size = " + moduleQueue.size());
		Module m = moduleQueue.getModule(0);
		System.out.println("m[0].isUsed = " + m.isUsed());
	}
	if(itemQueue != null){
		System.out.println("itemQueue size = " + itemQueue.size());
	}
	if(moduleQueueF != null){
		System.out.println("moduleQueueF size = " + moduleQueueF.size());
	}
	if(itemQueueF != null){
		System.out.println("itemQueueF size = " + itemQueueF.size());
	}
	if(itemQueueSub != null){
		System.out.println("itemQueueSub size = " + itemQueueSub.size());
	}
	//MenuItem menu = Framework.getInstance().getItem(menuPath);
	String url = "";
	if(areaType.equals("root")){ 
		url = MenuHelper.getRootUrl(contextPath,menuPath,(java.util.Map)null);
	}else if(areaType.equals("main")){
		url = MenuHelper.getMainUrl(contextPath,menuPath,(java.util.Map)null);
		//url = MenuHelper.getMainUrl(menu);
	}else if(areaType.equals("actionContainer")){
		url = MenuHelper.getActionContainerUrl(contextPath,menuPath,(java.util.Map)null);
	}else if(areaType.equals("navigatorContainer")){
		url = MenuHelper.getNavigatorContainerUrl(contextPath,menuPath,(java.util.Map)null);
	}else if(areaType.equals("status")){
		url = MenuHelper.getStatusUrl(contextPath,menuPath,(java.util.Map)null);
	}else if(areaType.equals("workspace")){
		url = MenuHelper.getWorkspaceUrl(contextPath,menuPath,(java.util.Map)null);
	}else if(areaType.equals("perspectiveContent")){
		//url = MenuHelper.getPerspectiveContentUrl(contextPath,menuPath,(java.util.Map)null);
	}
	System.out.println("url = " + url);
	
%>

<html>

<head><title>testMain</title></head>

<body>
<form action="" method="post">
<table>

</table>
<table >
<tr>
<td>
<iframe src="<%=url %>" height="600" width="1024"></iframe>
</td>
</tr>
</table>
</form>
</body>

</html>
