<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>

<html>
  <head>
  </head>
<frameset rows="*" cols="510, *" framespacing="3" frameborder="yes" border="3" >
<frame src="../sysmanager/orgmanager/lusersys_ajax.jsp" name="lusersys" id="lusersys">  
<!--<frame src="../sysmanager/orgmanager/lusersys.jsp" name="lusersys" id="lusersys">  -->
<!-- <frame src="../sysmanager/orgmanager/rusersys.jsp" name="rusersys" id="rusersys"> -->

<!-- 屏蔽岗位用户列表功能，weida200711061114 -->
<frame src="../sysmanager/orgmanager/rusersys_ajax.jsp" name="rusersys" id="rusersys">

</frameset>
<body>
</body>
</html>

