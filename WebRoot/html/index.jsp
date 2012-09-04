<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.frameworkset.platform.framework.MenuHelper"%>
<%@page import="com.frameworkset.platform.framework.ModuleQueue"%>
<%@page import="com.frameworkset.platform.framework.Module"%>
<%@page import="com.frameworkset.platform.framework.Item"%>
<%@page import="com.frameworkset.platform.framework.ItemQueue"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ include file="/common/jsp/accessControl.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/sany-taglib.tld" prefix="sany"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=accesscontroler.getCurrentSystemName() %></title>
<pg:config enablecontextmenu="false"/>
<link href="../html/stylesheet/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?skin=sany"></script>
<script type="text/javascript" src="../html/js/menu.js"></script>
<script type="text/javascript">
function logout()
{
	$.dialog.confirm("您确定注销吗？",
		function(){
			parent.location='../logout.jsp';
		    flag = false;
		 },
		function(){}
	)
}
</script>
<style type="text/css">
*{ margin:0; padding:0;}
html{ padding:0; _padding:75px 0 44px 0; width:100%;  overflow:hidden;}
body{ padding:75px 0 44px 0; _padding:0; height:100%; overflow: hidden;}
.l_top { position:absolute; top:0; width:100%; height:75px;z-index: 100}
.l_body { position: absolute; _position: relative; top:75px; _top:0; bottom:44px; width:100%; height:auto; _height:100%;}
.l_bottom { position:absolute; bottom:0; width:100%; height:44px; }
.l_main { height:100%; float:left;}
.l_content { position:absolute; _position:relative; width:auto; left:0; top:0; bottom:0; _left:0; right:0; height:auto; _height:100%; overflow:auto;}
</style>
</head>
<body>
<div class="l_top">
	<div  class="top">
    <div class="logo_top"><img src="${logoimage}" width="300" height="31" /></div>
    <div class="log_message">
	<span class="blue1"> <%=accesscontroler.getUserName()%></span>，欢迎您　<a href="#" class="zhuxiao" onclick="logout()">注销</a></div>
	</div>
	<sany:menus/>
</div>

<%-- <div class="l_body">
  <div class="l_main">
    <div class="l_content">
    	<iframe id="mainFrame" name="mainFrame" src="${mainurl}" scrolling="auto" frameborder="0" style="width:100%;height:100%;position:absolute; left:0; top:0; right:0; bottom:0;"></iframe>
    </div>
  </div>
</div> --%>

<div style="height: 100%">
	<div class="l_body">
		<iframe id="mainFrame" name="mainFrame" src="${mainurl}" scrolling="auto" frameborder="0" style="width:100%;height:100%;position:absolute; left:0; top:0; right:0; bottom:0;"></iframe>
	</div>
</div>

<div class="l_bottom">
    <div class="footer">
      <div class="left_footer"><a href="ghp/index.html">全球招聘平台</a> | <a href="http://olm.sany.com.cn/SanyOLM/login.do?method=login">在线学习平台</a> | <a href="contact.html">联系我们</a> | <a href="vote.html">调查问卷</a> | <a href="system.html">系统管理</a> | <br />
        <script type="text/javascript"> 
    copyright=new Date();
    update=copyright.getFullYear();
    document.write( "&copy;" + "1989-"+ update + " 三一集团有限公司 版权所有");
    </script>
    </div>
    </div>
</div>
</body>
</html>

