<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OperManager,com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.entity.Roleresop"%>
<%@ page import="java.util.List,com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@page import="com.frameworkset.platform.cms.sitemanager.*" %>
<%@page import="com.frameworkset.platform.framework.*"%>
<%@page import="java.util.*,com.frameworkset.platform.cms.util.CMSUtil" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);

	 //判断操作是否成功
	String isok =  (String)request.getAttribute("isOk");
	String resTypeId = request.getParameter("resTypeId");
	String resid = request.getParameter("siteid");
	String roleid = (String)session.getAttribute("currRoleId");//System.out.println(roleid);
	String resname = request.getParameter("sitename");
	String role_type = (String)session.getAttribute("role_type");
	if(resTypeId == null){
		resTypeId = (String)request.getAttribute("resTypeId");
	}
	if(resid == null){
		resid = (String)request.getAttribute("resid");
	}
	
%>
<html>
<head>    
<title>属性容器</title>
  <link rel="stylesheet" type="text/css" href="../../sysmanager/css/windows.css">
  <link rel="stylesheet" type="text/css" href="../../sysmanager/css/contentpage.css">
  <link rel="stylesheet" type="text/css" href="../../sysmanager/css/tab.winclassic.css">
  <tab:tabConfig/>
<SCRIPT LANGUAGE="JavaScript"> 
	//模态窗口
	function openWin(url,swidth,sheight){
		var w = showModalDialog(url,window,"dialogWidth:"+swidth+"px;dialogHeight:"+sheight+"px;help:no;scroll:auto;status:no");
	 	return w;
	}
	//
	function back(){
		parent.window.close();
	}
</SCRIPT>
</head>

<body class="contentbodymargin">
<div id="contentborder">
	<table border="0" cellpadding="3" cellspacing="0">
		<tr>
			<td align="right">
				<strong>权限设置:</strong>
			</td>
			<td>
				<div align="center">
					<img src="../images/doc_aggr.gif" width="20" height="20">
				</div>
			</td>
			<td align="right">
				<a href="usersitePurview.jsp?siteid=<%=resid%>&sitename=<%=resname%>"> 
				站点类权限</a>
			</td>
			<td>
				<div align="center">
					<img src="../images/doc_aggr.gif" width="20" height="20">
				</div>
			</td>
			<td align="right">
				<a href="usertplPurview.jsp?siteid=<%=resid%>&sitename=<%=resname%>">
				 模板类权限 </a>
			</td>
			<td>
				<div align="center">
					<img src="../images/doc_aggr.gif" width="20" height="20">
				</div>
			</td>
			<td>
				<a href="userfilePurview.jsp?siteid=<%=resid%>&sitename=<%=resname%>">
				 文件视图类权限 </a>
			</td>
			<td>
				<div align="center">
					<img src="../images/doc_aggr.gif" width="20" height="20">
				</div>
			</td>
			<td>
				<a href="userchannelPurview.jsp?siteid=<%=resid%>&sitename=<%=resname%>">
				 频道类权限 </a>
			</td>
			<td>
				<div align="center">
					<img src="../images/doc_aggr.gif" width="20" height="20">
				</div>
			</td>
			<td>
				<a href="userdocPurview.jsp?siteid=<%=resid%>&sitename=<%=resname%>">
				 文档类权限 </a>
			</td>
			<td>
				<div align="center">
					<img src="../images/doc_aggr.gif" width="20" height="20">
				</div>
			</td>
			<td>
				<a href="usersiteappPurview.jsp?siteid=<%=resid%>&sitename=<%=resname%>">
				 应用类权限 </a>
			</td>
		</tr>
	</table>
	<%
		String parentPath = "cms::menu://sysmenu$root/appManager$module";
		SiteManagerImpl simpl = new SiteManagerImpl();
		List items = simpl.getSubItems(parentPath);   
		List isSetedList = simpl.getSetedAppInSite(resid); //resid 即为 siteid
	%>
	<table width="100%" border="0" cellpadding="5" cellspacing="0" border="1" class="thin">
		<tr class="cms_report_tr">
			<td align="center">应用名称</td>
			<td align="center">应用ID</td>
			<td align="center">授予授予</td>
		</tr>
		<%
		
		for(int i=0;i<items.size();i++){
			String flag = "";
			String itemId = "";
			String itemName = "";
			String itemPath = "";
			int dolarcount = 1;
			String type = "";
			if(items.get(i)  instanceof  Item){
				Item item = (Item)items.get(i);
				
				itemId = item.getId();
				itemName = item.getName();
				itemPath = item.getPath();
				type = "item";
				dolarcount = CMSUtil.count(itemPath,'$') - 2;
				
			}else if(items.get(i) instanceof Module){
				Module module = (Module)items.get(i);
				itemId = module.getId();
				itemName = module.getName();
				itemPath = module.getPath();
				type = "module";
				dolarcount = CMSUtil.count(itemPath,'$') - 2;
			}
			String setId = itemPath + "^" + itemId;
			for(int j=0;j<isSetedList.size();j++){							    
				String sid = (String)isSetedList.get(j);					
				if(setId.equals(sid)) {
					flag = "checked";
					break;
				}
			}
			if(!flag.equals("checked"))
			{
				continue;
			}
		%>
		<tr>
			<td style="text-align:left">
			<%
			for(int j = 0; j < dolarcount; j ++)
			{
				
				out.println( "<img src='../siteManage/space.gif'/>");
			}
			out.println("<img src='../siteManage/" + type +".gif'/>");
			out.println(itemName);
			%></td>
			<td><%=itemId%></td>
			<%
				if(flag.length()>0){
			%>
			<td onclick="openWin('operList_siteapp_tab.jsp?resId2=<%=resid%>&resTypeId2=<%=itemId%>&resTypeName=<%=itemId%>&title=<%=itemName%>&username=<%=itemName%>',600,500)">
				<img src="../siteManage/new_doc.gif" border="0"  style="cursor:hand">
			</td>
			<%
				}else{
			%>
			<td>&nbsp;</td>
			<%
				}
			%>
		</tr>    
		<%
		}
		%>        
	</table>	
</div>
</body>

</html>
