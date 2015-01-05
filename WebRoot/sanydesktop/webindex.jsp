<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.frameworkset.platform.framework.MenuHelper"%>
<%@page import="com.frameworkset.platform.framework.ModuleQueue"%>
<%@page import="com.frameworkset.platform.framework.Module"%>
<%@page import="com.frameworkset.platform.framework.Item"%>
<%@page import="com.frameworkset.platform.framework.ItemQueue"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/sany-taglib.tld" prefix="sany"%>
<%@ include file="/common/jsp/accessControl.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD><title><%=accesscontroler.getCurrentSystemName() %></title>
<META content="text/html; charset=utf-8" http-equiv=Content-Type>
<%@ include file="/common/jsp/cms_css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html2/js/menu.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?skin=sany"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
<script type="text/javascript">

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

</script>
</HEAD>
<BODY>
<DIV id=wrap>
<DIV id=div_top>
 <div id="wrapper" style="border:0;">
  <div class="top_contain">
    <div class="logo"><img src="${logoimage}" width="219" height="33" /></div>
    <br /><pg:message code="sany.pdp.module.welcome"/>　，<a class="name" href="javascript:void"><%=accesscontroler.getUserName()%></a><span class="cart"><a href="../html2/cart.html">Cart (1)</a></span> <span class="logout">
         <pg:false actual="${fromwebseal}">
			<a href="javascript:void" class="zhuxiao" onclick="logout()"><pg:message code="sany.pdp.module.logout"/></a>
		</pg:false></span></div>
  <sany:webmenus level="2"/>
 </div></DIV>
<div style="padding:0 10px;">
	<iframe id="indexFrame" name="indexFrame"" src="${mainurl}" frameborder="0" width="100%" height="100%" scrolling="no" ></iframe>
</div>
<DIV class=line></DIV>
</DIV>
 <DIV id="div_bottom">
   <IFRAME height="57" src="../html2/bottom.html" frameBorder="0" width="1000" scrolling=no></IFRAME>
 </DIV>
<script>
loadMain("indexFrame")
</script>
</BODY></HTML>
