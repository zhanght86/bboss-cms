<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.frameworkset.platform.cms.container.Template,com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.cms.sitemanager.*,java.util.List,com.frameworkset.platform.cms.templatemanager.*,com.frameworkset.common.poolman.DBUtil"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="inc/css/cms.css" rel="stylesheet" type="text/css">
<title>内容管理主框架</title>
<style type="text/css">
<!--
body {
	background-color: #003399; 
}
-->
</style></head>

<body topmargin="0" rightmargin="0" scroll="no">

<table width="810" height="626" border="0"   cellpadding="0" align="center" cellspacing="0">
  <tr>
    <td height="104" valign=middle style="background-repeat:no-repeat"><img src="images/main_top.gif" width="810" height="104" border="0" usemap="#Map"></td>
  </tr>
  <tr>
    <td valign=top style="background-repeat:no-repeat"><img src="images/main_body.jpg" width="810" height="435" border="0" usemap="#Map2"></td>
  </tr>
</table>
<% //取频道可发布的状态
     //  List list=null;
		//SiteManagerImpl tmi=new SiteManagerImpl();
		//Site site=new Site();
		//list=tmi.getSupportedSitePublishStatus(String.valueOf(18));
		//out.print("状态数:"+list.size()+"个");
		//for(int i=0;i<list.size();i++){
		//site = (Site)list.get(i);
		//out.print(site.getSingleEnabledPublishStatus()+"<br>");
		// }
					    %>

<map name="Map"><area shape="rect" coords="632,50,702,69" href="addressManage/address_list.jsp">
<area shape="rect" coords="717,51,780,66" href="address_list.jsp">
</map>
<map name="Map2">
<area shape="rect" coords="231,57,312,111" href="../testcontentpublish.jsp?actionType=view">
<area shape="rect" coords="349,9,440,66" href="docManage/site_document.jsp"><area shape="rect" coords="500,54,579,113" href="javascript:void">
<area shape="rect" coords="537,140,606,191" href="javascript:void"><area shape="rect" coords="204,154,269,209" href="../testcontentpublish.jsp"><area shape="rect" coords="24,324,97,402" href="../testsitepublish.jsp">
<area shape="rect" coords="100,323,175,404" href="../testchannelpublish.jsp">
<area shape="rect" coords="179,324,249,406" href="../testchannelpublish.jsp">
</map></body>
</html>
