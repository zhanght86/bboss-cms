<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>

<%
			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkAccess(request, response);
			String operName = accesscontroler.getUserName();

			DocumentManager dm = new DocumentManagerImpl();
			String userid = accesscontroler.getUserID();
			String docidStr = request.getParameter("docidStr");

			String docTitles = "";
			//得到文档id数据,为字符串数组
			if (docidStr != null) {
				String[] docid = docidStr.split(":");
				for (int i = 0; i < docid.length; i++) {
					docTitles = docTitles + dm.getDoc(docid[i]).getSubtitle()
							+ ";";
				}
			}
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<title>版本注释</title>
		<script src="../inc/js/func.js"></script>
		<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
		<script language="javascript">
			function sub(){
				addverLableForm.action="add_doc_version_handle.jsp?docidStr=<%=docidStr%>";
				addverLableForm.target="verCommentFrame";
				addverLableForm.submit();
			}
		</script>
	</head>
	<body scroll=no leftmargin=0>
		<form action="" method="post" name="addverLableForm">
			<table width="100%" height="100%" border="0" align="center" bgcolor="#FFFFFF">
				<tr>
					<td align="left" height="20">
						文档标题：
						<input value=<%=docTitles%> disabled type="text" name="docTitles" size="40">
					</td>
				</tr>
				<tr>
					<td align="left" height="20">
						版本标签：
						<input  type="text" name="verLable" size="40">
					</td>
				</tr>
				<tr>
					<td align="left" height="20">
						 版本注释：
					</td>
				</tr>
				<tr>
					<td align="center" height="150">
						<textarea name="verComment" cols="55" rows="20"></textarea>
					</td>
				</tr>
				<tr>
					<td align="left" height="20">
						操作人：<%=operName%>
					</td>
				</tr>
				<tr>
					<td align="center">
						<input type="button" name="subButton" value="确定" onClick="sub()">
						<input type="button" name="cnlButton" value="返回" onClick="window.close()">
					</td>
				</tr>
			</table>	
		</form>
	</body>
	<div style="display:none" height=0 width=0>
		<iframe name="verCommentFrame"></iframe>
	</div>
</html>