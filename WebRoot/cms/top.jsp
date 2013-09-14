<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.frameworkset.platform.framework.*" %>
<%@ page import="com.frameworkset.platform.security.AccessControl,java.util.List,com.frameworkset.platform.framework.MenuHelper"%>
<%@page  import="com.frameworkset.platform.cms.sitemanager.SiteManager,com.frameworkset.platform.cms.sitemanager.SiteManagerImpl"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<link href="inc/css/cms.css" rel="stylesheet" type="text/css">
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<%
    AccessControl accesscontroler = AccessControl.getAccessControl();
   
    com.frameworkset.platform.cms.CMSManager cmsManager  = new com.frameworkset.platform.cms.CMSManager();
	cmsManager.init(request,session,response,accesscontroler);
	
	String currentSiteid = cmsManager.getSiteID();
	String userId = accesscontroler.getUserID();

	String modulePath = cmsManager.getCurrentModule();
	
	String welcomepath = "";
    
    String subsystem = FrameworkServlet.getSubSystem(request,response,accesscontroler.getUserAccount());
    MenuHelper menuHelper =  new MenuHelper(subsystem,accesscontroler);
    Item publicitem = menuHelper.getPublicItem();
    welcomepath = MenuHelper.getMainUrl(publicitem);
    
    String viewPath = "cms::menu://sysmenu$root/viewManager$module";
    ItemQueue views = menuHelper.getSubItems(viewPath);
    
    //System.out.println("============"+welcomepath);
    //System.out.println("============"+filesPath);
%>
<script language="javascript">
	function list()
	{
   		window.open('../sysmanager/onLineUser.jsp','onlineUser','menubar=no,status=no,toolbar=no,width=800,height=400');
  	}
  	function setup()
	{
   		window.open('../cms/siteManage/defaultSite.jsp?userId=<%=userId%>','onlineUser','menubar=no,status=no,toolbar=no,width=800,height=400');
   		//window.open('../cms/siteManage/defaultSite.jsp?userId=<%=userId%>','onlineUser');
  	}
  	
  	function maintain()
	{
  		var url="<%=request.getContextPath()%>/cms/maintainInfo.jsp?userId=<%=userId%>";
	 	 $.dialog({title:'系统维护',width:screen.availWidth,height:650, content:'url:'+url});  
		
	 	 //window.open('../cms/maintainInfo.jsp?userId=<%=userId%>','onlineUser','menubar=no,status=no,toolbar=no,left=0,top=0,width='+ screen.availWidth +',height=650');
  	}
	function block2(selObj,restore)
	{   
		window.open(selObj.options[selObj.selectedIndex].value,'perspective_main');
		if(document.forms[0].modulepath.value.indexOf("siteManageItem")!=-1)
		{
			document.forms[0].site_id.disabled = true;
		}
		else
		{
			document.forms[0].site_id.disabled = false;
			var siteid = document.all.item("site_id").value;
			document.forms[0].action="<%=request.getContextPath()%>/cms/frame_bridge.jsp?siteid="+siteid + "&selObj=";
			document.forms[0].submit();
		}
	}
	function block1(selObj,restore)
	{
		var siteid = document.all.item("site_id").value;
		if(document.forms[0].modulepath.value.indexOf("siteManageItem")==-1)
		{
			document.forms[0].action="<%=request.getContextPath()%>/cms/frame_bridge.jsp?siteid="+siteid + "&selObj=";
			document.forms[0].submit();
		}
	}
	function init()
	{
		if(document.forms[0].modulepath.value.indexOf("siteManageItem")!=-1)
		{
			document.forms[0].site_id.disabled = true;
		}
		else
			document.forms[0].site_id.disabled = false;
	}
</script>
<style type="text/css">
body{ font-family: Verdana, "宋体", Vrinda, serif}
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>三一集团内容管理系统</title>
</head>

<body leftmargin="0" topmargin="0" rightmargin="0" onLoad="init()" scroll=no>
<form target="site" name="Form1" action="" method="post" >
<table width="100%" height="58"  cellpadding="0" cellspacing="0"  border="0">
<input type="hidden" name="userId" value="<%=userId%>" />
  <tr>
    <td height="42" valign="top" background="images/top_tile.jpg"><table width="100%" height="58" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="200" height="58" valign="top" background="images/logo.jpg"  style="background-repeat:repeat-y"><a href="../index.jsp?subsystem_id=cms" target="_parent" class="pic"><img src="images/logo.jpg" alt="返回系统首页" width="200" height="50" border="0"></a></td>
        <td background="images/top_tile_short.jpg">&nbsp;</td>
        <td valign="top" background="images/top_tile_short.jpg" >
		<table width="100%" border="0" cellpadding="0" cellspacing="0"  style="color:#37516E">
          <tr>
            <td height="15" colspan="3" valign="bottom"></td>
            </tr>
          <tr>
            <td width="32%" valign="bottom">            </td>
            </tr>
          <tr>
            <td height="21">&nbsp;&nbsp;<font color="#BC2224">站点选择:</font><select name="site_id" class="cms_select" style="width:200px" onChange="block1(this,0)">
		                  	<%
		                      List sitelist = cmsManager.getSiteAllRuningList();
							  request.setAttribute("sitelist",sitelist);

							%>
		          			<pg:list requestKey="sitelist">
		          			<option <pg:equal colName="siteId" value="<%=currentSiteid%>">selected</pg:equal>
		          				value="<pg:cell colName="siteId"/>">
		         			<pg:cell colName="name"/>  
		          			</pg:list>
		        	</select></td>
            <td colspan="2"  valign="bottom"><font color="#BC2224">系统管理:</font><select id="modulepath" name="modulepath" style="width:100px;" onChange="block2(this,0)"  >
								<option value="<%=welcomepath%>" <%if(modulePath.equals(publicitem.getPath()))out.print("selected");%>>欢迎								</option>
								<%for(int i = 0; views != null && i < views.size(); i ++)
								{
									Item view = views.getItem(i);
									String viewUrl = menuHelper.getMainUrl(view);
								%>
								<option value="<%=viewUrl%>" <%if(modulePath.equals(view.getPath()))out.print("selected");%>><%=view.getName()%>								</option>
								<%}%>
								
						</select>
			<a  style="cursor:hand" onclick='setup()' target="_top"><font style="color:#F86805; text-decoration:underline">个性设置</font></a> |
			<a  style="cursor:hand" onclick='maintain()' target="_top"><font style="color:#4E69B0; text-decoration:underline">系统维护</font></a> |
			<!-- <a  style="cursor:hand" onclick='list()' target="_top"><font style="color:#019B00; text-decoration:underline">在线人数<font style="color:blue; font-weight:bold"><%=accesscontroler.getLoginUserCount()%></font>(人)</font></a> --> |
			<a  style="cursor:hand" onclick='' target="_top"><font style="color:#4E69B0; text-decoration:underline">当前用户：<font style="color:blue;"><%=accesscontroler.getUserName()%></font></font></a> |
            <a  style="cursor:hand" onclick='if(confirm("您确实要退出内容管理系统吗？")){location.href="../logout.jsp";}' target="_top"><font style="color:#F10A65; text-decoration:underline">注销</font></a></td>
          </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td height="1" bgcolor="#6382C2" ></td>
  </tr>
</table>
</form>
<div style="display:none">
	<IFRAME name="site" width="0" height="0"></IFRAME>
</div> 
</body>
</html>