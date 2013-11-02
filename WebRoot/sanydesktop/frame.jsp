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
<title>三一集团开发平台</title>
<%  String userName = AccessControl.getAccessControl().getUserAccount();
 String appName = accesscontroler.getCurrentSystemID();
%>
<script type="text/javascript" src="${pageContext.request.contextPath}/include/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/include/js/disablebaskspace.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
<link href="${pageContext.request.contextPath}/html/stylesheet/common.css" rel="stylesheet" type="text/css" />
<style>
html,body{ height:100%;}
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/menu.js?userName=<%= appName%>&appName =<%=userName %>"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/log.js"></script>
<script>
$(document).ready(function(){ 
	$(".left_menu li > a").click(function(){ 		
		var ulNode = $(this).next("ul"); 
		ulNode.slideToggle("fast",function(){
			if($(this).parent().hasClass("select_links")){$(this).parent().removeClass("select_links");}
			else{$(this).parent().addClass("select_links");}
		}); 
	}); 
	$(".left_menu li li > a").click(function(){
		$(".left_menu  li  li").removeClass("select_links");
		$(this).parent().addClass("select_links");										 
	})
}); 
</script>
</head>
<body>
  <div class="main_contain" style="height:100%">
    <table width="100%"  height="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="170" valign="top" id="Tbasic2" height="100%">
        <sany:leftmenu target="left_rightFrame"/>        
        </td>
        <td width="9" class="more2"><table  border="0" cellspacing="0" cellpadding="0" height="100%">
            <tr>
              <td valign="middle"><a href="javascript:togglevisible('basic2')"><img src="${pageContext.request.contextPath}/html/images/expand.gif"  name="Ibasic2"/></a></td>
            </tr>
          </table></td>
        <td valign="top" height="100%">
        <iframe src="${selectUrl }" name="left_rightFrame" id="left_rightFrame" frameborder="0" style="width:100%; height:99% "></iframe>        
        </td>
      </tr>
    </table>
  </div>
</body>
<script type="text/javascript">

function getobj(menu)
{
return (navigator.appName == "Microsoft Internet Explorer")?this[menu]:document.getElementById(menu);
}
function togglevisible(treepart)
{
if (this.getobj("T"+treepart).style.visibility == "hidden")
{
this.getobj("T"+treepart).style.position="";
this.getobj("T"+treepart).style.visibility="";
document["I"+treepart].src="${pageContext.request.contextPath}/html/images/expand.gif";
}
else
{
this.getobj("T"+treepart).style.position="absolute";
this.getobj("T"+treepart).style.visibility="hidden";
document["I"+treepart].src="${pageContext.request.contextPath}/html/images/shrink.gif";
}
}
</script>
</html>
