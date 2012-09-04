<%@page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/jsp/importAndTaglib.jsp"%>
<%@page import="com.frameworkset.platform.framework.MenuHelper"%>
<%@page import="com.frameworkset.platform.framework.ModuleQueue"%>
<%@page import="com.frameworkset.platform.framework.Module"%>
<%@page import="com.frameworkset.platform.framework.Item"%>
<%@page import="com.frameworkset.platform.framework.ItemQueue"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>应用开发基础平台</title>
		<pg:config enablecontextmenu="false"/>
		<link href="html/stylesheet/common.css" rel="stylesheet" type="text/css" />
		<!-- <script type="text/javascript" src="common/scripts/menu.js"></script>  -->
		<script language="javascript" src="sysmanager/scripts/toolbar.js"></script>
		<script language="javascript" src="sysmanager/common/CommonFunc.js"></script>

	</HEAD>
	<script type="text/javascript">

$(document).ready(function() {
	//让第一个一级菜单默认打开
	var firstItem = $("#mainMenu li:first");	
	//alert(firstItem.attr('id'));
	var moduleId=firstItem.attr('id');
	var cuerrentMenu = $('#' + moduleId);

$("#mainMenu li").mouseover(function(){
	$(this).find("a").addClass("fcurrent");
}).mouseout(function(){
	$(this).find("a").removeClass("fcurrent");
})



	//当前选中的一级菜单切换显示样式
<%--	cuerrentMenu.parent().find("li").removeClass("on");--%>
<%--	cuerrentMenu.parent().find("span").removeClass("nav_01");--%>
<%--	cuerrentMenu.addClass("on");--%>
<%--	cuerrentMenu.find("span").addClass("nav_01");--%>
	
});


function alertp(){
	alert('ppp');
}



/**
 * 显示二级菜单
 * @param moduleId 一级菜单id
 */
function showitem(moduleId,moduleName) {
	
	//一级菜单链接所在li
	var cuerrentMenu = $('#' + moduleId);

	//当前选中的一级菜单切换显示样式
	cuerrentMenu.parent().find("a").removeClass("select");
	cuerrentMenu.find('a').addClass("select");
	var contentWindow = parent.perspective_main.perspective_content.base_actions_container.base_properties_container.base_properties_content;	//content.jsp
	contentWindow.showitem(moduleId,moduleName);
			
}

	



</script>
	</head>
	<body id="home">
	<div  class="top">
	  <div class="logo_top"><img src="html/images/top_logo.jpg"  /></div>
	  <div class="log_message">	
	<jsp:include page="accountbox.jsp"	flush="true"></jsp:include>
	</div>
	</div>

<div id="menubar" class="ddsmoothmenu">
  <ul  id="mainMenu" class="menus">
  <%
	ModuleQueue moduleQueue = (ModuleQueue) request
			.getAttribute("moduleQueue");
	for (int i = 0; i < moduleQueue.size(); i++) {
		Module module = moduleQueue.getModule(i);
		String moduleName = module.getName();
		String moduleId = module.getId();
		out
				.println("<li id=\""
						+ moduleId
						+ "\" name=\""+moduleName+"\"><a href=\"javascript:showitem('"
						+ moduleId + "','"+moduleName+"');\" title=\"" + moduleName
						+ "\">" + moduleName + "</a></li>");
	}
	String hasrootitems = (String)request.getAttribute("hasrootitems");
	if(hasrootitems != null&&hasrootitems.equals("true")){
		out
		.println("<li id=\"rootitems\" name=\"个人设置\"><a href=\"javascript:showitem('rootitems','个人设置');\" title=\"个人设置\">个人设置</a></li>");
		
	}
	
%>    
  </ul>
</div>


		
	</body>

</html>
