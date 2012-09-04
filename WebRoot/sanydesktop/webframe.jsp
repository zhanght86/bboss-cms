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
<HEAD>
<TITLE>GCP</TITLE>
<META content="text/html; charset=utf-8" http-equiv=Content-Type>
<%@ include file="/common/jsp/cms_css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html2/js/menu.js"></script>
</HEAD>
<BODY style="background-color: white;">
<DIV class="order">
  <sany:webleftmenu target="rightFrame"/>
  <DIV class="order_r" style="overflow:hidden">
    <iframe src="${selectUrl }" name="rightFrame" id="rightFrame" frameborder="0" width="100%" height="100%" ></iframe>
  </DIV>
  <div style="clear:both"></div>
</DIV>
<script>
loadContent("rightFrame")
</script>
</BODY>


</HTML>
