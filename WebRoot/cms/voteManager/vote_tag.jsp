<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="cms"%>


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>无标题文档</title>
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	</head>
	<style type="text/css">
		body {
			background-color: #F3F4F9;
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

	<body>
	<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F3F4F9" width="100%">
	<tr><td align="left">
	<a href="votelist.jsp"><<返回</a>
	</td></tr>
	</table>
		
		<cms:vote>
			<TABLE border="0" cellpadding="0" cellspacing="0" bgcolor="#F3F4F9" align="center">
				
					<tr>
						<td>
							<cms:question optionstyle="list-style:none;" textareastyle="width:300;height:100"/>
							<p>
						</td>
					</tr>
				<TR><td>
			<P align="center"><cms:votebutton classname="cms_button"/></P>
				</td></TR>
			</TABLE>
		</cms:vote>
</html>
