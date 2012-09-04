<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.frameworkset.platform.cms.searchmanager.*"%>
<%

	String siteId = (String)session.getAttribute("siteId");
	String searchType = (String)session.getAttribute("searchType");
	String indexId = (String)session.getAttribute("indexId");
	CMSSearchManager sm = new CMSSearchManager();
	String searchTypeDes = "";
	if(indexId == null || "all".equals(indexId))
		searchTypeDes = sm.getSearchButtonValue(searchType);
	else
		searchTypeDes = sm.getSearchButtonValue("");
%>
<html>
<head>
	<title>搜索引擎</title>
	<link rel="stylesheet" href="inc/mycss.css" type="text/css">
	<link href="<%=request.getContextPath()%>/cms/inc/css/cms.css" rel="stylesheet" type="text/css">
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
</head>
<body>
<table width="800" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
  <tr>
    <td><iframe src='../siteResource/site1/_template/detail_top.html' width="100%" height="168" scrolling=no marginwidth=0 marginheight=0 frameborder=0 vspace=0 hspace=0>
这里放页头</iframe>
	</td>
  </tr>
  <tr>
    <td height="4" bgcolor="#0073B3"></td>
  </tr>
  <tr>
    <td height="10"></td>
  </tr>
 <form action="search_process.jsp?flag=1" method="post" name="searchForm" id="searchForm">
  <tr>
    <td height="40" style="padding-left:20">关键字：
            <input type="text" name="queryString" size="38" maxlength="50" />
                <input type="hidden" name="flag" value="1" />
				<input name="button" type="button" onClick="searchForm.submit()" style="cursor:hand" value="<%=searchTypeDes%>" width="57" height="24" />				</td>
  </tr>
  </form>
  <tr><td background="images/line.gif" height="1"></td>
  <tr>
    <td style="padding-left:20">&#12304;搜索结果&#12305;<font color="#FF9966">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&#9679;关键字：</font></td>
  </tr>
  <tr>
    <td height="400" style="font-size:14px; color:#FF6600" align="center"><b>请输入关键字！</b></td>
  </tr>
  <tr>
    <td><iframe src='../siteResource/site1/_template/index_foot.html' width="100%" height="100" scrolling=no marginwidth=0 marginheight=0 frameborder=0 vspace=0 hspace=0>
这里放页尾</iframe></td>
  </tr>
</table>
</body>
</html>