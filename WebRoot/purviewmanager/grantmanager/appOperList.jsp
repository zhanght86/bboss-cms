
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OperManager,
				 com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*" %>
<%@ page import="com.frameworkset.platform.framework.*"%>		
<%@ page import="com.frameworkset.platform.cms.util.CMSUtil" %>		
				
<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkManagerAccess(request,response);
    
    //是否是批量授予权限 true：批量授予权限；false：单个授予权限
    String isBatch = request.getParameter("isBatch");
    //得到需要授予权限的用户ID或角色ID
    String currRoleId = (String)request.getParameter("currRoleId");
    
    //得到的授权类型  user或role
    String role_type = (String)request.getParameter("role_type");
    
    //如果授权类型为"user"的
    String orgId = (String)request.getParameter("currOrgId");
	
	//资源类型 role,organization,column....
	String resTypeId = request.getParameter("resTypeId");
	
	//资源ID
	String resId = request.getParameter("resId");
	//资源名称或描述
	String resName = request.getParameter("resName");
	
	
	
	
	
%>

<html>
<head>    
  <title>属性容器</title>
  <%@ include file="/include/css.jsp"%>
  <script language="JavaScript" src="../scripts/func.js" type="text/javascript"></script>
 <%@ include file="/include/css.jsp"%>
<body class="contentbodymargin">
<div id="contentborder">
	
<center>

<form name="Form1" target="group"  method="post" >
	<fieldset>
	    <LEGEND align=left><strong><FONT size=2>权限设置</FONT></strong></LEGEND>
	    <table width="100%" height="22" border="0" cellpadding="0" cellspacing="1" class="thin">
	    <tr class="tr">
	        <td class="td"  height="22" align="center"><strong>应用名称<strong></td>
	        <td class="td"  height="22" align="center" ><strong>应用ID</strong></td>
	        <td class="td"  height="22" align="center" ><strong>授予权限</strong></td>
	      </tr>
	      <%
	
		String parentPath = "cms::menu://sysmenu$root/appManager$module";
		SiteManagerImpl simpl = new SiteManagerImpl();
		List items = simpl.getSubItems(parentPath);   
		List isSetedList = simpl.getSetedAppInSite(resId); //resid 即为 siteid
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
					out.println( "<img src='../images/site/space_br.gif'/>");
				}
				out.println("<img src='../images/site/" + type +".gif'/>");
				out.println(itemName);
			%>
			</td>
			
			<td><%=itemId%></td>
			
			<%
				if(flag.length()>0)
				{
			%>
			<td onclick="openWin('operList_siteapp_tab.jsp?currRoleId=<%=currRoleId%>&role_type=<%=role_type%>&resId=<%=resId+"$$"+itemId%>&resTypeId=<%=resTypeId%>&resTypeName=<%=itemId%>&title=<%=itemName%>&username=<%=itemName%>&currOrgId=<%=orgId %>',600,500)">
				<img src="../images/site/new_doc.gif" border="0"  style="cursor:hand">
			</td>
			<%
				}
				else
				{
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
    </fieldset>
</form>

<IFRAME name="group" width="0" height="0"></IFRAME>

</center>
</div>
</body>
</html>
