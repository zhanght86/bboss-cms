<%@ page language="java"  pageEncoding="UTF-8"%>
	<%--
	描述：桌面窗口大小设置
	作者：qian.wang
	版本：1.0
	日期：2011-09-20
	 --%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<frameset cols="40%,60%" border=0 name="custom_container">
	<frame name="workarea" frameborder=0 src="deskitemcustomtree.jsp" scrolling="auto" >
	<frame name="sizewin" src="../../desktop/getMenuCustom.page" marginwidth=0 marginheight=0 scrolling="no" frameborder=0 noresize>
</frameset>
