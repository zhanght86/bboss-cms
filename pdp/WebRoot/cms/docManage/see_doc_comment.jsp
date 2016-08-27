<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.docCommentManager.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%	
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	
	String docTitle = request.getParameter("docTitle");
	String subtime = request.getParameter("subtime");
	String userName = request.getParameter("userName");
	String docComment = request.getParameter("docComment");
	String userIP = request.getParameter("userIP");
	String commentId = request.getParameter("commentId");
	
	//System.out.println(request.getRemoteAddr());
	//System.out.println(request.getRemoteHost());
	
	DocCommentManager ddm = new DocCommentManagerImpl();
	int status = ddm.getCommentByComId(Integer.parseInt(commentId.trim())).getStatus();
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<title>:::查看评论内容::::::::::</title>
	<script src="../inc/js/func.js"></script>
	<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
	<style type="text/css">
		.STYLE1 {color: blue}
	</style>
	<script language="javascript">
		function publishComments(){
			seeCommentForm.action="docCommentHandle.jsp?commentId=<%=commentId%>&action=publish"; 
			seeCommentForm.submit();
		}
		function deleteC(){	
			seeCommentForm.action="docCommentHandle.jsp?commentId=<%=commentId%>&action=delete"; 
			seeCommentForm.submit();
		}
		function withdrawPubComments(){
			seeCommentForm.action="docCommentHandle.jsp?commentId=<%=commentId%>&action=withdrawPub"; 
			seeCommentForm.submit();
		}	
	</script>
</head>
<body scroll=no leftmargin=0>
	<div id="divProcessing" style="position:absolute;width:40%;height:20px;z-index:1;
			background-color:#F0f0f0;layer-background-color:#F0f0f0;left:22%;top:80%;display:none;"  class="font">   
	    <marquee direction="left" width="250" behavior="alternate"><span class=STYLE1>正在发布中，请稍等……</span></marquee>
    </div>
	<form action="" method="post" name="seeCommentForm" target="seeCommentIframe">
		<table width="100%" height="100%" border="0" align="center" bgcolor="#FFFFFF">	 
		  <tr>
			<td align="left" height="20">
				文档标题：<input value="<%=docTitle%>" disabled type="text" name="docTitle">
			</td>
		  </tr> 
		  <tr>
			<td align="left" height="20">
				时间：<input value="<%=subtime%>" disabled type="text" name="subtime">
			</td>
		  </tr>
		  <tr>
			<td align="left" height="20">
				评论发表人：<input value="<%=userName%>" disabled type="text" name="userName">
			</td>
		  </tr>
		  <tr>
			<td align="left" height="20">
				评论发表人IP：<input value="<%=userIP%>" disabled type="text" name="userName">
			</td>
		  </tr>
		  <tr>
			<td align="left" height="20">
				评论内容：
			</td>
		  </tr>
		  <tr>
			<td align="left" height="150">		
				<textarea name="docComment" cols="60" rows="23"><%=docComment%></textarea>
			</td>
		  </tr>
		  <tr>
			  <td align="center" height="20">
			  	<%if(status==0 || status==2){%>
					<input name="cancelForm" type="button" value="发布" class="cms_button" onClick="publishComments()"/>
				<%}else if(status==1){%>
					<input name="cancelForm" type="button" value="撤发" class="cms_button" onClick="withdrawPubComments()"/>
				<%}%>
					<input name="cancelForm" type="button" value="删除" class="cms_button" onClick="deleteC()"/>
					<input name="cancelForm" type="button" value="返回" class="cms_button" onClick="window.close()"/>
			  </td>
		  </tr>
		  <tr>
		  </tr>
		</table>
	</form>
	<div height=0 width=0 style="display:none">
		<iframe name="seeCommentIframe"></iframe>
	</div>
</body>
</html>