<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%	
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String commenterName = accesscontroler.getUserName();
	
	String docId = request.getParameter("docId");
	DocumentManager dm = new DocumentManagerImpl();
	String docTitle = dm.getDoc(docId.trim()).getTitle();
	//System.out.println(request.getRemoteAddr());
	//System.out.println(request.getRemoteHost());

	Date sysDate = new Date();
	SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	String date = format.format(sysDate);
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<title>增加审核意见</title>
	<script src="../inc/js/func.js"></script>
	<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
	<style type="text/css">
		.STYLE1 {color: blue}
	</style>
	<script language="javascript">
		function sub(){
			addCommentForm.action="docCommentHandle.jsp?docId=<%=docId%>&action=add";
			addCommentForm.target="addCommentIframe";
			addCommentForm.submit();
		}
	</script>
</head>
<body scroll=no leftmargin=0>
	<form action="" method="post" name="addCommentForm">
		<table width="100%" height="100%" border="0" align="center" bgcolor="#FFFFFF">	 
		  <tr>
			<td align="left" height="20">
				文档标题：<input value=<%=docTitle%> disabled type="text" name="docTitle">
			</td>
		  </tr> 
		  <tr>
			<td align="left" height="20">
				时间：<input value=<%=date%> disabled type="text" name="subtime">
			</td>
		  </tr>
		  <tr>
			<td align="left" height="20">
				评论发表人：<input value=<%=commenterName%> disabled type="text" name="userName">
			</td>
		  </tr>
		  <tr>
			<td align="left" height="20">
				请填写评论：
			</td>
		  </tr>
		  <tr>
			<td align="left" height="150">		
				<textarea name="docComment" cols="60" rows="25"></textarea>
			</td>
		  </tr>
		  <tr>
			  <td align="center" height="20">
					<input name="submitForm" type="button" value="确定" class="cms_button" onClick="sub()" />
					<input name="cancelForm" type="button" value="返回" class="cms_button" onClick="window.close()"/>
			  </td>
		  </tr>
		  <tr>
		  </tr>
		</table>
	</form>
	<div height=0 width=0 style="display:none">
		<iframe name="addCommentIframe"></iframe>
	</div>
</body>
</html>