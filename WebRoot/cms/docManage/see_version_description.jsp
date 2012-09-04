<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>

<%
			response.setHeader("Cache-Control", "no-cache"); 
			response.setHeader("Pragma", "no-cache"); 
			response.setDateHeader("Expires", -1);  
			response.setDateHeader("max-age", 0);

			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkAccess(request, response);
			
			String docid = request.getParameter("docid");	
			String version = request.getParameter("version");
			
			DocumentManager dm = new DocumentManagerImpl();
			Document document = dm.getDocVerInfo(docid,version);
			
			String verDiscription = document.getVerDiscription();	
			String verLable = document.getVerLable();	
			String subtitle = document.getSubtitle();	
			String opername = request.getParameter("opername");			
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<title>版本注释</title>
		<script src="../inc/js/func.js"></script>
		<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
		<style type="text/css">
			body{
				background-color:#003399;
			}
		</style>
	</head>
	<body scroll=no leftmargin=0>
		<form action="" method="post" name="addverLableForm">
			<table width="100%" height="100%" border="0" align="center" bgcolor="#FFFFFF">
				<tr>
					<td align="left" height="20">
						文档标题：<input value=<%=subtitle%> disabled type="text" name="docTitles" size="30">
					</td>
				</tr>
				<tr>
					<td align="left" height="20">
						版 本 号：<input value=<%=version%> disabled type="text" name="docTitles" size="5">
					</td>
				</tr>
				<tr>
					<td align="left" height="20">
						版本标签：<input value=<%=verLable%> disabled type="text" name="docTitles" size="20">
					</td>
				</tr>
				<tr>
					<td align="left" height="20">
						 版本注释：
					</td>
				</tr>
				<tr>
					<td align="center" height="150">
						<textarea name="verComment" cols="55" rows="20" ><%=verDiscription%></textarea>
					</td>
				</tr>
				<tr>
					<td align="left" height="20">
						操作人：<%=opername%>
					</td>
				</tr>
				<tr>
					<td align="center">
						<input type="button" name="cnlButton" value="返回" onclick="window.close()">
					</td>
				</tr>
			</table>	
		</form>
	</body>
</html>