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
	
	String siteid = request.getParameter("siteid");
	String channelid = request.getParameter("channelid");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<title>内容管理主框架</title>
		<script src="../inc/js/func.js"></script>
		<script src="<%=rootpath%>/include/jquery-1.4.2.min.js"></script>
		<script src="<%=rootpath%>/include/security.js"></script>
		<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
		<script language="JavaScript" src="../../sysmanager/scripts/selectTime.js" type="text/javascript"></script>
		<script type="text/javascript" src="../../public/datetime/calender.js" language="javascript"></script>
		<script type="text/javascript" src="../../public/datetime/calender_date.js" language="javascript"></script>
		<script language="javascript">
			function queryCitedDoc(queryFlag){
				$.secutiry.dosubmit("queryForm",
						"<%=rootpath%>/cms/docManage/cited_doc_list.jsp?siteid=<%=siteid%>&channelid=<%=channelid%>&queryFlag=" + queryFlag,
								"citedDocListF",
								"<%=rootpath%>");
				//queryForm.action = "<%=rootpath%>/cms/docManage/cited_doc_list.jsp?siteid=<%=siteid%>&channelid=<%=channelid%>&queryFlag=" + queryFlag;
				//queryForm.target = "citedDocListF";
				//queryForm.submit();
			}
		</script>
	</head>
	<body leftmargin="0" topmargin="0">
	 <form name="queryForm" id="queryForm" method="post">
		<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="5" valign="top"><img src="../images/querybox_left.gif" width="5" height="62"></td>
    <td  style="background:url(../images/querybox_bg.gif) repeat-x top">
	<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0"  style="margin-top:5px"   class="query_table">
		  <tr><td colspan="7" height="60%">&nbsp;</td></tr>
		  <tr>
		  	<td height="30" align="right">引 用 人：</td>
			<td>
				<select name="citeUserid" class="cms_select" style="width:120">
					<option value="">
						请选择引用人					</option>
					<%
						List list = null;
						DocumentManager dmi = new DocumentManagerImpl();
					 	list = dmi.getCiteUserList(channelid);
						request.setAttribute("dblist", list);
					%>
					<pg:list requestKey="dblist">
						<option value="<pg:cell colName="user_id"/>">
						<pg:cell colName="user_name" />
					</pg:list>
			</select>			</td>
			
			<td align="right">引用时间开始：</td>
			<td>
				<input type="text" name="citeTimeBgin"  size="18"  class="cms_text" >
				<!-- <input name="button1" type="button" onClick="selectTime('queryForm.citeTimeBgin',0)" value=".">-->
			<input name="button1" type="button" onClick="showdate(document.all('citeTimeBgin'))" value="时间" >			</td>
			<td align="right">结束：</td>
			<td>
				<input type="text" name="citeTimeEnd"  size="18"  class="cms_text" >
				<!-- <input name="button2" type="button" onClick="selectTime('queryForm.citeTimeEnd',0)" value=".">	 -->
			<input name="button2" type="button" onClick="showdate(document.all('citeTimeEnd'))" value="时间">			</td>	
		  </tr>
		  <tr>
			<td  height="30" align="right">文档名称：</td>
			<td><input type="text" name="docName"  size="18"  class="cms_text"></td>
			<td align="right">文档源频道：</td>
			<td>
				<select name="srcChnlid" class="cms_select" style="width:123">
					<option value="">
						请选择文档频道名					</option>
					<%
						List list1 = null;
					 	list1 = dmi.getCiteSrcChnlList(channelid);
						request.setAttribute("dblist", list1);
					%>
					<pg:list requestKey="dblist">
						<option value="<pg:cell colName="channelid"/>">
						<pg:cell colName="channelName" />
					</pg:list>
		    </select>			</td>
			<td  height="30" align="right">频道名称：</td>
			<td><input type="text" name="chnlName"  size="18"  class="cms_text"></td>
		  </tr>
		  <tr>
		  	<td colspan="6" align="right">
				<input type="button" name="query" class = "cms_button" value="查询" onClick="queryCitedDoc('part')">
				<input type="button" name="back" class = "cms_button" value="查询所有" onClick="queryCitedDoc('all')">
			<input type="button" name="back" class = "cms_button" value="返回" onClick="window.close()">			</td>
		  </tr>
	   </table></td>
    <td width="6" valign="top"><img src="../images/querybox_right.gif" width="6" height="62"></td>
  </tr>
</table>

	 </form>
</body>
</html>
