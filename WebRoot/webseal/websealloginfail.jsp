<%@page import="com.frameworkset.util.StringUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%String userName = request.getParameter("userName");
String errormsg  = request.getParameter("errormsg");
 String ip = request.getParameter("ip");
 if(ip == null)
 {
 	ip = StringUtil.getClientIP(request);
 }
 String info = "用户["+userName +"]登录失败-Client IP["+ip+"],错误信息:"+errormsg ;
 %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/sany-taglib.tld" prefix="sany"%>
<!--Quirks Mode-->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=info %></title>

<link href="../html/stylesheet/common.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
function logout()
{
	top.location='../logout.jsp';
	flag = false;
}
</script>

<style>
html,body {
 margin:0;
 padding:0; 
 height:100%;
 overflow: hidden; 
}
#dyhead,#dyfoot {
 width:100%;
 position:absolute;
 z-index:100;
}
#dyhead {
 top:0;
 height:40px;
}
#dyfoot {
 bottom:0;
 height:20px;
 background-color: #FFFFFF
}
#dycontent {
 width:100%;
 overflow:auto;
 top:70px;
 bottom:45px;
 position:absolute;
 z-index:99;
 _height:100%;
 _border-top:70px solid white;
 _border-bottom:30px solid white;
 _top:0;
 background-color:white
}
#dycontent iframe{ width:100%; height:100%; overflow-x:scroll;}
</style>
</head>
<body>

<div id="dyhead">
	<div  class="top">
    <div class="logo_top"><img src="../html/images/top_logo.jpg" width="300" height="31" /></div>
    <div class="log_message">
	<span class="blue1"></div>
	</div>
</div>

<div id="dycontent">
<%=info %>  <a href="#" class="zhuxiao" onclick="logout()">注销</a> 
</div>

<div id="dyfoot">
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

