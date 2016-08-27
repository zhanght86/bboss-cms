<%@include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>

<title>导航器工具栏</title>
<link rel="stylesheet" type="text/css" href="../css/toolbar.css">
<jsp:include page="../base/scripts/panes.jsp" />
<script language="javascript" src="../scripts/toolbar.js"></script>
<script language="javascript">
function showToday()
{
var day="";
var month="";
var ampm="";
var ampmhour="";
var myweekday="";
var year="";
mydate=new Date();
myweekday=mydate.getDay();
mymonth=mydate.getMonth()+1;
myday= mydate.getDate();
myyear= mydate.getYear();
year=(myyear > 200) ? myyear : 1900 + myyear;
if(myweekday == 0)
weekday="日";
else if(myweekday == 1)
weekday="一";
else if(myweekday == 2)
weekday="二";
else if(myweekday == 3)
weekday="三";
else if(myweekday == 4)
weekday="四";
else if(myweekday == 5)
weekday="五";
else if(myweekday == 6)
weekday="六";
document.write("<font color= #0C3866 size=2 align=middle>"+year+"年"+mymonth+"月"+myday+"日 星期"+weekday+"</font>");
}
</script>
<style type="text/css">
<!--
.style3 {color: #FFFFFF}
-->
</style>
</head>
<body class="toolbarbodymargin">
<div id="toolbarborder">
<div id="toolbar" ondblclick="switchFrameworks(<%=Framework.SWITCH_NAVIGATOR%>,<%=Framework.SWITCH_SCOPE_PERSPECTIVEMAIN%>,document.all.doubleclickcolumn);">

<table width="100%" height=25 cellpadding=0 cellspacing=0 border=0>
	<tr>
	<td valign="middle" align="center" width=25 height=25><img
			class="normal" src="../base/images/base_perspective_enabled.gif" width=16 height=16></td>
	<td id="doubleclickcolumn" recover="双击恢复" maxtitle="双击最大化" title="双击最大化" valign="middle" align="left"
			width="*" height=25 nowrap class="text">
					  
			<script language="javascript">showToday();</script>
			
		</td>
</tr>
	
</table>
</div>
</div>
</body>
</html>

