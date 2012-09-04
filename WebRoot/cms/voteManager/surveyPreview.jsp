<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="cms"%>


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>无标题文档</title>
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<META HTTP-EQUIV="pragma" CONTENT="no-cache">
		<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
		<META HTTP-EQUIV="expires" CONTENT="Mon, 23 Jan 1978 20:52:30 GMT">
	</head>
	<style type="text/css">
		body {
			background-color: #ffffff;
		}
		.STYLE1 {color: #0000FF}
		.STYLE2 {color: #000099}
		.style3 {
			font-size: 14px;
			font-weight: bold;
			color: #3300FF;
		}
		.operStyle{
		width:17;
		height:16;
		}
</style>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	%>
	<body  style="background-color: #F3F4F9;">
	<cms:vote id="<%=request.getParameter("id")%>">
		<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F3F4F9" align="center">
	<p>
		<tr><td align="center"><div style="font-family: '黑体';font-weight: bold;font-size: 24px;">
		<cms:survey /></div><p></td></tr>
		<tr><td style="color: #0000CC;" align="center">
		<cms:survey type="content"/></td></tr>
		</table>
		<p>
			<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F3F4F9" align="center">
				<cms:outline datatype="questions" channel="<%=request.getParameter("id")%>">
					<tr>
						<td>
							<strong><cms:rowid increament="1"/>. </strong><cms:question optionstyle="list-style:none;" textareastyle="width:300;height:100"/>
							<p>
						</td>
					</tr>
				</cms:outline>
				<TR><td align="center">
				<input type="button" value="关闭" onclick="window.close();"/>
				</td></TR>
			</TABLE>
		</cms:vote>
</html>
