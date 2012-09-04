<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String docid = request.getParameter("docid");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<title>内容管理主框架</title>
		<script src="../inc/js/func.js"></script>
		<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
		<script language="JavaScript" src="../../sysmanager/scripts/selectTime.js" type="text/javascript"></script>
		<script type="text/javascript" src="../../public/datetime/calender.js" language="javascript"></script>
		<script type="text/javascript" src="../../public/datetime/calender_date.js" language="javascript"></script>
		<script language="javascript">
			function queryDocVer(queryFlag){
				queryForm.action = "manage_doc_version.jsp?docid=<%=docid%>&queryFlag=" + queryFlag;
				queryForm.target = "docVerListFrame";
				queryForm.submit();
			}
		</script>
	</head>
	<body leftmargin="0" topmargin="0">
	 <form name="queryForm" method="post">
	 <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="5" valign="top"><img src="../images/querybox_left.gif" width="5" height="62"></td>
    <td  style="background:url(../images/querybox_bg.gif) repeat-x top">
	<table width="100%" border="0" cellpadding="3" cellspacing="0" style="margin-top:5px"   class="query_table">
		  <tr>
			<td height="30" align="right">保存人：</td>
			<td>
				<select name="docVerUser" class="cms_select" style="width:120">
					<option value="">
						请选择保存人					</option>
					<%
						List list = null;
						DocumentManager dmi = new DocumentManagerImpl();
					 	list = dmi.getDocVerUserList(docid);
						request.setAttribute("dblist", list);
					%>
					<pg:list requestKey="dblist">
						<option value="<pg:cell colName="user_id"/>">
						<pg:cell colName="user_name" />
					</pg:list>
				</select>			</td>
			<td align="right">版本标签：</td>
			<td>
				<input type="text" name="docVerLable"  size="18"  class="cms_text">			</td>
			<td rowspan=2 valign="bottom">
				<input type="button" name="query" class = "cms_button" value="查询" onClick="queryDocVer('part')">
				<input type="button" name="all" class = "cms_button" value="查询所有" onClick="queryDocVer('all')">
				<input type="button" name="back" class = "cms_button" value="返回" onClick="window.close()">			</td>
		  </tr>
		  <tr>
			<td align="right"  height="30">保存时间开始：</td>
			<td  height="30">
				<input type="text" name="docVerTimeBgin"  size="18"  class="cms_text" >
				<input name="button1" type="button" onClick="showdate(document.all('docVerTimeBgin'))" value="选择时间" >			</td>
			<td align="right"  height="30">结&nbsp; &nbsp;&nbsp;束：</td>
			<td  height="30">
				<input type="text" name="docVerTimeEnd"  size="18"  class="cms_text" >
				<input name="button2" type="button" onClick="showdate(document.all('docVerTimeEnd'))" value="选择时间">			</td>	
		  </tr>
		</table></td>
    <td width="6" valign="top"><img src="../images/querybox_right.gif" width="6" height="62"></td>
  </tr>
</table>

		
	 </form>
</body>
</html>
