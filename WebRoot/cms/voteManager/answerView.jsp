<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.cms.votemanager.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<html>
	<head>
		<title>选项查看</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css"/>
		<META HTTP-EQUIV="pragma" CONTENT="no-cache">
		<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
		<META HTTP-EQUIV="expires" CONTENT="Mon, 23 Jan 1978 20:52:30 GMT">
		<script language="javascript" src="../inc/js/func.js"></script>
  </head>
  <%
    AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request,response);
    if (request.getParameter("answerIDs")!=null&&!"".equals(request.getParameter("answerIDs"))){
    	VoteManager voteMgr  = new VoteManagerImpl();
  		if(voteMgr.passAnswers(request.getParameter("answerIDs"),accesscontroler.getUserAccount(),accesscontroler.getUserName(),request.getRemoteAddr())==1){
  				%><script language="javascript">alert("审核成功!");</script><%
  			}
  	}
    String answerId = request.getParameter("answerId");
    VoteManager impl = new VoteManagerImpl();
    Answer answer = impl.getAnswerByAnswerId(answerId);%>
  <body>
  <form action="answerView.jsp?answerId=<%=answerId%>&answerIDs=<%=answerId%>" target="form_target" method="post">
	<table id="tag_attach_list" width="100%" border="1" align=center cellpadding="3" cellspacing="0" bordercolor="#B7CBE4"  class="Datalisttable" id="docListTable">
			<tr>
				<td height="25" colspan="10" background="../images/data_list_tHeadbg.jpg" style="text-align:left; background:url(../images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7">
					<div  class="DocumentOperT">回答内容查看：</div>
				</td>
		    </tr> 
	</table>
   <table width="100%" border="0" cellpadding="5" cellspacing="0" class="Datalisttable">
	    <TR class="cms_report_tr">
		  <td class="headercolor"  align="left">问答题目</td>
		</TR>
		<TR class="cms_data_tr">
		  <td class="tablecells"><%=answer.getQtitle()%></td>
		</TR>
		<TR class="cms_report_tr">
		  <td class="headercolor"  align="left">回答内容</td>	
		</TR>
		<TR class="cms_data_tr">
		  <td class="tablecells"><%=answer.getAnswer()%></td>
		</TR>
	</table>
	<TABLE width="100%" border="0" cellpadding="5" cellspacing="0">
	   <tr>
	     <td width="50%" align="right"><input type="submit" value="审核"></td><td><input type="button" value="关闭" onclick="window.close();"></td>
	   </tr>
	</TABLE>
	</form>	
  </body>
  <iframe name="form_target" width="0" height="0"></iframe>
</html>
