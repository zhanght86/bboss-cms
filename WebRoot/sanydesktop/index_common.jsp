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
<title><%=AccessControl.getAccessControl().getCurrentSystemName() %></title>
<link href="../html3/stylesheet/basic.css" rel="stylesheet" type="text/css" />

<link href="../html3/stylesheet/menu.css" rel="stylesheet" type="text/css" />

<pg:true  requestKey="showboot" >
<link href="../html3/stylesheet/top.css" rel="stylesheet" type="text/css" />
</pg:true>
<pg:false  requestKey="showboot" >
<link href="../html3/stylesheet/top2.css" rel="stylesheet" type="text/css" />
</pg:false>
<script type="text/javascript" src="${pageContext.request.contextPath}/include/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/include/js/dialog/lhgdialog.js?skin=chrome"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/include/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
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
	resizemainFrame();
	$(window).resize(function(){
		resizemainFrame();
	});
	//window.onresize=resizemainFrame();
	
});
function logout()
{
	$.dialog.confirm("<pg:message code='sany.pdp.index.check.signout'/>",
		function(){
			parent.location='../logout.jsp';
		    flag = false;
		 },
		function(){},'','<pg:message code="sany.pdp.common.confirm"/>'
	)
}

function resizemainFrame()
{
	var  screenHeight=document.documentElement.clientHeight;
	var topHead=$(".top").css("height").substring(0,$(".top").css("height").length-2);
	var menuHead=$("#menubar").css("height").substring(0,$("#menubar").css("height").length-2);
	var footerHead=0;
	if($(".footer").css("height")!=undefined){
		 footerHead=$(".footer").css("height").substring(0,$(".footer").css("height").length-2);
	}
	<pg:true  requestKey="showboot" >
	var frameHead=screenHeight -topHead -menuHead -footerHead-5;
	</pg:true>
	<pg:false  requestKey="showboot" >
	var frameHead=screenHeight -topHead -menuHead -footerHead;
	</pg:false>
	
	$("#mainFrame").css("height",frameHead+"px");
}

</script>
<style>
html{overflow:hidden}
</style>
</head>
<body>
<div id="wrap">
<div  class="top">
  <div class="logo_top" ><%=AccessControl.getAccessControl().getCurrentSystemName() %></div>
      <div class="right_info">审批事项：<a href="javascript:void" >10</a>条 | 
      通知：<a href="javascript:void" >2</a>条 | </div>
  <div class="info">
    <ul>
      <li class="info-i has-pulldown"> <em class="f-icon pull-arrow"></em> 
	<sany:accesscontrol userattribute="userName"/>-<sany:accesscontrol userattribute="userAccount"/>[<sany:accesscontrol userattribute="orgjob"/>]，<pg:message code="sany.pdp.module.welcome"/>　
		
        <div class="pulldown user-info"> <em class="arrow"></em>
          <div class="content"> <span class="li"><a href="">个人资料</a></span> <span class="li"><a href="">修改密码</a></span> <span class="li"><a href="">快捷修改</a></span> 
          
           </div>
        </div>
      </li> <li class="info-i"><pg:false actual="${fromwebseal}">
			<pg:empty actual="<%=specialuser %>">
				<span class="li"><a href="javascript:;" onclick="logout()" id="signout"><pg:message code="sany.pdp.module.logout"/></a></span>
			</pg:empty>
		</pg:false></li>
      <li class="info-i no-separate default-text has-pulldown"> <em class="f-icon pull-arrow"></em> <span class="more" hideFocus="hideFocus">更多</span>
        <div class="pulldown more-info"> <em class="arrow"></em>
          <div class="content"> <span class="li"><a href="">版本更新</a></span> <span class="li"><a href="">帮助中心</a></span> <span class="li"><a href="">问题反馈</a></span> <span class="li"><a href="">联系方式</a></span> <span class="li"><a href="">权利声明</a></span> </div>
        </div>
      </li>
    </ul>
  </div>
</div>
<sany:menus level="3" enableindex="true"/>
<div class="content_box"><iframe id="mainFrame" name="mainFrame" src="${mainurl}" width="100%" scrolling="auto" frameborder="0"></iframe></div>
<pg:true  requestKey="showboot" >
<div class="footer">
  <div class="left_footer"> <a href="ghp/index.html">全球招聘平台</a> | <a href="http://olm.sany.com.cn/SanyOLM/login.do?method=login">在线学习平台</a> | <a href="javascript:void" id="contact">联系我们 </a>| <a href="javascript:void">调查问卷</a> 
  </div>
 <span class="copy_right"> <script type="text/javascript">
		copyright=new Date();
		update=copyright.getFullYear();
		document.write( "&copy;" + "1989-"+ update );
	</script> 
  三一集团有限公司 版权所有</span></div>
    
  </pg:true>  
</body>
</html>
