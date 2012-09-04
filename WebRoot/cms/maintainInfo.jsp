<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.framework.MenuHelper"%> 
<%@ page import="com.frameworkset.platform.framework.ItemQueue"%> 
<%@ page import="com.frameworkset.platform.framework.Item"%> 
<%@ page import="java.util.ArrayList"%> 
<%@ page import="java.util.List"%> 

<%
	AccessControl controler = AccessControl.getInstance();
	controler.checkAccess(request, response);
	
	MenuHelper menuHelper = new MenuHelper(controler);
	
	List itemList = new ArrayList();
	
	String defalutItemsPath = "module::menu://sysmenu$root";
	
	String itemsPath = "module::menu://sysmenu$root/sysmanager$module";
	
	ItemQueue defaultItems = menuHelper.getSubItems(defalutItemsPath);
	
	//加入默认模块下的菜单项信息: 个人用户信息修改 系统帐户修改 个人密码修改
	for(int i=0;  defaultItems != null && i<defaultItems.size(); i++)
	{
		Item item = defaultItems.getItem(i);
		itemList.add(item);
	}
	
	//加入系统下的菜单项信息 : 用户查询 权限查询 日志查询 数据库管理
	ItemQueue items = menuHelper.getSubItems(itemsPath);
	for(int i=0;  items != null && i< items.size(); i++)
	{
		Item item = items.getItem(i);
		String id = item.getId();
		
		if("userquery".equals(id) || "resquery".equals(id) 
					|| "logquery".equals(id) || "dbmanager".equals(id) )
		{
			itemList.add(item);	
		}
	}	
%>

<html>
<head>
<tab:tabConfig/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/sysmanager/css/treeview.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/sysmanager/css/windows.css">
<title>系统维护</title>
</head> 
<body>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">

		<tr>
			<td colspan="2">
				<tab:tabContainer id="maintainInfo" skin="bluesky">
<!-- ------------------------------------------------------------------------------------------------------------------------------------------>
					<%
						for(int i=0; i<itemList.size();i++)
						{
							Item item = (Item) itemList.get(i);
							
							String id = item.getId();
							String title = "title-" + id;
							String url = request.getContextPath() + "/" + item.getWorkspaceContent();
					%>
						<tab:tabPane id="<%=title%>" tabTitle="<%=item.getName()%>" lazeload="true">
							<tab:iframe id="<%=id%>" src="<%=url%>" frameborder="0" 
								scrolling="auto" width="98%" height="600">
						</tab:iframe>
					</tab:tabPane>
					<%
						}
					%>
<!-------------------------------------------------------------------------------------------------------------------------------->
					
				</tab:tabContainer>			
			</td>
		</tr>
  </table>	
</body>

</html>