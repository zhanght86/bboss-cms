<%@ page language="java" session="false"  import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/sany-taglib.tld" prefix="sany"%>
<%@page import="com.frameworkset.platform.sysmgrcore.authenticate.LoginUtil"%>
<%@page import="com.frameworkset.platform.framework.MenuHelper"%>
<%@page import="com.frameworkset.platform.framework.ModuleQueue"%>
<%@page import="com.frameworkset.platform.framework.Module"%>
<%@page import="com.frameworkset.platform.framework.Item"%>
<%@page import="com.frameworkset.platform.framework.ItemQueue"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<% String userName = AccessControl.getAccessControl().getUserAccount();
 String appName = AccessControl.getAccessControl().getCurrentSystemID();
 String specialuser = LoginUtil.isSpesialUser(request);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>三一人力资源管理系统--首页</title>
<link href="../html3/stylesheet/basic.css" rel="stylesheet" type="text/css" />
<link href="../html3/stylesheet/menu.css" rel="stylesheet" type="text/css" />
<link href="../html3/stylesheet/top.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/include/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?skin=sany"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/common/scripts/menubase.js?userName=<%= appName%>&appName =<%=userName %>&selecemenu=${selectedmenuid}"></script>
<script type="text/javascript" src="../html3/js/menu.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/include/js/disablebaskspace.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	
	$(".def-nav,.info-i").hover(function(){
		$(this).find(".pulldown-nav").addClass("hover");
		$(this).find(".pulldown").show();
	},function(){
		$(this).find(".pulldown").hide();
		$(this).find(".pulldown-nav").removeClass("hover");
	});
	
});
</script>
</head>
<body>
<div id="wrap">
<div  class="top">
  <div class="logo_top" >三一集团人力资源管理系统</div>
  <div class="info">
    <ul>
      <li class="info-i has-pulldown"> <em class="f-icon pull-arrow"></em> 欢迎您 ,李四
        <div class="pulldown user-info"> <em class="arrow"></em>
          <div class="content"> <span class="li"><a href="">个人资料</a></span> <span class="li"><a href="">修改密码</a></span> <span class="li"><a href="">快捷修改</a></span> <span class="li"><a href="javascript:;" id="signout">注销</a></span> </div>
        </div>
      </li>
      <li class="info-i">审批事项：<a href="#">10</a>条</li>
      <li class="info-i">通知：<a href="#">2</a>条</li>
      <li class="info-i no-separate default-text has-pulldown"> <em class="f-icon pull-arrow"></em> <span class="more" hideFocus="hideFocus">更多</span>
        <div class="pulldown more-info"> <em class="arrow"></em>
          <div class="content"> <span class="li"><a href="">版本更新</a></span> <span class="li"><a href="">帮助中心</a></span> <span class="li"><a href="">问题反馈</a></span> <span class="li"><a href="">联系方式</a></span> <span class="li"><a href="">权利声明</a></span> </div>
        </div>
      </li>
    </ul>
  </div>
</div>
<sany:menus level="3" enableindex="true" style="common"/>
<div class="content_box"><iframe id="mainFrame" name="mainFrame" src="${mainurl}" width="100%" height="612" scrolling="auto" frameborder="0"></iframe></div>
<div class="footer">
  <div class="left_footer"> <a href="ghp/index.html">全球招聘平台</a> | <a href="http://olm.sany.com.cn/SanyOLM/login.do?method=login">在线学习平台</a> | <a href="#" id="contact">联系我们 </a>| <a href="#">调查问卷</a> 
  </div>
 <span class="copy_right"> <script type="text/javascript">
		copyright=new Date();
		update=copyright.getFullYear();
		document.write( "&copy;" + "1989-"+ update );
	</script> 
  三一集团有限公司 版权所有</span></div>
</body>
</html>
