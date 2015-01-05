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
a:link {
}
a:hover {
	font-size: 12px; position:relative; top:1px; left:1px;
}
-->
</style>
</head>

<body topmargin="0" rightmargin="0" scroll="no">

<table width="810" height="626" border="0" align="center"   cellpadding="0" cellspacing="0" bgcolor="#E6E6F0">
  <tr>
    <td height="104" valign=middle><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="58"><img src="images/main_banner_left.jpg" width="586" height="120"></td>
        <td background="images/main_banner_right.gif">
		<div style=" float:left; width:auto;margin:10px; padding:4px;background-color:#7596BF;  font-size:12px;"><a href="javascript:void"><font style="color:#FFFFFF;">个人通信录</font></a></div>
		<div style=" float:left; width:auto;margin:10px; padding:4px;background-color:#7596BF;font-size:12px;"><a href="javascript:void"><font style="color:#FFFFFF;">个人资料</font></a></div>
		</td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td valign=top style="background-repeat:no-repeat"><table width="100%" border="0" cellpadding="0" cellspacing="0" background="images/main_menu_Midd_BG.gif" style="text-align:center">
      <tr>
        <td width="22"><img src="images/main_menu_left_BG.gif" width="22" height="52"></td>
        <td><a href="javascript:void"><img src="images/Main_menu01.gif" width="60" height="52" border="0"></a></td>
        <td><a href="javascript:void"><img src="images/Main_menu02.gif" width="60" height="52" border="0"></a></td>
        <td><a href="javascript:void"><img src="images/Main_menu03.gif" width="60" height="52" border="0"></a></td>
        <td><a href="javascript:void"><img src="images/Main_menu01.gif" width="60" height="52" border="0"></a></td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td width="20"><img src="images/main_menu_R_BG.gif" width="20" height="52"></td>
      </tr>
    </table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>    <img src="images/main_M_ball.jpg" width="810" height="267"></td>
  </tr>
</table>

    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="22"><img src="images/Main_B_left.gif" width="22" height="79"></td>
        <td background="images/main_B_bg.gif">
		<table width="auto" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><a href="javascript:void"><img src="images/Main_B_botton1.gif" width="65" height="79" hspace="5" border="0"></a></td>
            <td><a href="javascript:void"><img src="images/Main_B_botton2.gif" width="65" height="79" hspace="5" border="0"></a></td>
            <td><a href="javascript:void"><img src="images/Main_B_botton3.gif" width="65" height="79" hspace="5" border="0"></a></td>
            <td><a href="javascript:void"><img src="images/Main_B_botton1.gif" width="65" height="79" hspace="5" border="0"></a></td>
            <td>&nbsp;</td>
          </tr>
        </table></td>
        <td width="20"><img src="images/Main_B_right.gif" width="20" height="79"></td>
      </tr>
    </table>
    <img src="images/main_B_bg.jpg" width="810" height="23"></td>
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

</map></body>
</html>
