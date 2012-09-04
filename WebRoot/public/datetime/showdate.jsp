<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<HTML>
<HEAD>
<TITLE>选择时间</TITLE>
<%
	String retObj = request.getParameter( "retObj" );
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
var obj=window.dialogArguments.document.all( '<%=retObj%>' );
//-->
</SCRIPT>
<script language="javascript" src="meizzdate.js"></script>

</HEAD>


<BODY onload="setday(obj);" scroll="no">
</BODY>
</HTML>

