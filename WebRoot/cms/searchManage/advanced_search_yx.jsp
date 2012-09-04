<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.frameworkset.platform.cms.searchmanager.*"%>
<%@ page import="java.util.List"%>
<%@ page autoFlush="true" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
	//当前站点
	String siteId = request.getParameter("siteId");   //标签传来的参数
	if(siteId == null || siteId.length() <= 0)
		siteId = (String)session.getAttribute("siteId");
	
	session.removeAttribute("chnlId");
	session.removeAttribute("indexId");
	session.removeAttribute("hitsPerSet");
	session.removeAttribute("fromDays");
	session.removeAttribute("fileFormat");
	session.removeAttribute("keyWord");
	session.removeAttribute("sort");
	session.removeAttribute("field");
	
	CMSSearchManager sm = new CMSSearchManager();
	String searchTypeDes = sm.getSearchButtonValue("");
%>
<html>
<head>
<title>搜索引擎--高级搜索</title>
<link rel="stylesheet" href="inc/mycss.css" type="text/css">
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<script language="JavaScript" src="../../sysmanager/scripts/selectTime.js" type="text/javascript"></script>
<script type="text/javascript" src="../../public/datetime/calender.js" language="javascript"></script>
<script type="text/javascript" src="../../public/datetime/calender_date.js" language="javascript"></script>

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
    <td style="padding:20 0"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="86%">&nbsp;</td>
        <td width="5%"><a href="help.jsp" style="cursor:hand">帮助</a></td>
        <td width="9%"><a href="../siteResource/site1/_template/index.html">返回首页</a></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td colspan="2">&nbsp;</td>
      </tr>
    </table>
    <table width="80%" border="0" align="center" cellpadding="0" cellspacing="0" style="border:6px solid #E1E1E1">
      <tr>
        <td style="padding:20">
		 <FORM name="searchForm" method="post" action="search_process.jsp" style="margin:0">
		<table width="100%" border="0" cellspacing="0" cellpadding="10" align="center" style="border:1px solid #e1e1e1">
		<tr><td colspan="2" style="font-size:14px; font-weight:bold"><img src="images/dot_red.gif" width="7" height="7" hspace="6" align="absmiddle"><font color="#DF0218">高级检索</font></td>
		</tr>
		<tr><td colspan="2" background="images/line.gif" height="1"></td>
		</tr>
          <tr>
            <td align="right"><b>关键字：</b></td>
            <td><input type="text" name="queryString" size="45" maxlength="50">
                <input type="hidden" name="flag" value="0">
            </td>
          </tr>
		 <tr><td colspan="2" background="images/line.gif" height="1"></td>
          <tr>
            <td align="right"><strong>关键字逻辑：</strong></td>
            <td><input type="radio" name="andor" value="and" checked>
              与　
              <input type="radio" name="andor" value="or">
              或 </td>
          </tr>
		  <tr><td colspan="2" background="images/line.gif" height="1"></td>
          <tr>
            <td align="right"><strong>每页显示的结果数：</strong></td>
            <td><select name="hitsPerSet">
                <option value="5">5</option>
                <option value="10" selected>10</option>
                <option value="15">15</option>
                <option value="20">20</option>
                <option value="50">50</option>
                <option value="100">100</option>
                <option value="200">200</option>
              </select>
            </td>
          </tr>
		  <tr><td colspan="2" background="images/line.gif" height="1"></td>
          <tr>
            <td align="right"><strong>搜索站点：</strong></td>
            <td><select name="indexId" class="cms_select" style="width:120">
                <option value="all_advanced">所有站点</option>
                <%
					List list = null;
					list = sm.getIndexListOfSite(siteId);
					request.setAttribute("list", list);
				%>
                <pg:list requestKey="list">
                  <option value="<pg:cell colName="id"/>        
                  ">
                  <pg:cell colName="indexName" />
                </pg:list>
              </select>
            </td>
          </tr>
		 <tr><td colspan="2" background="images/line.gif" height="1"></td>
          <tr>
            <td align="right"><strong>文件格式：</strong></td>
            <td><select name="fileFormat">
                <option value="all">所有格式</option>
                <option value="text/html">网页</option>
                <option value="pdf">pdf文档</option>
                <option value="word">word文档</option>
                <option value="excel">excel文档</option>
                <option value="ppt">powerpoint文档</option>
                <option value="rtf">rtf文档</option>
              </select>
            </td>
          </tr>
		  <tr><td colspan="2" background="images/line.gif" height="1"></td>
          <tr>
            <td align="right"><strong>排序：</strong></td>
            <td><select name="sort">
                <option value="time">时间</option>
                <option value="relevance">相关度</option>
              </select>
            </td>
          </tr>
		 <tr><td colspan="2" background="images/line.gif" height="1"></td>
          <tr>
            <td align="right"><strong>搜索字段：</strong></td>
            <td><select name="field">
                <option value="all">所有字段</option>
                <option value="content">内容</option>
                <option value="title">标题</option>
                <option value="description">描述</option>
                <option value="keyword">关键字</option>
              </select>
            </td>
          </tr>
		 <tr><td colspan="2" background="images/line.gif" height="1"></td>
          <tr>
            <td align="right"><!--          	网页发布起始时间：-->
                <!--            <select name="fromDays">-->
                <!--              <option value="~" selected>任何时间</option>-->
                <!--              <option value="7">最近一周</option>-->
                <!--              <option value="32">最近一个月</option>-->
                <!--              <option value="183">最近六个月</option>-->
                <!--              <option value="366">最近一年</option>-->
                <!--            </select>-->
                <strong>              起始时间:</strong></td>
            <td>
              <input type="text" name="dayBegin"  size="18"  class="cms_text" >
              <input name="button1" type="button" onClick="showdate(document.all('dayBegin'))" value="时间" style="cursor:hand">
              &nbsp;&nbsp;
              <strong>结束时间：</strong>
              <input type="text" name="dayEnd"  size="18"  class="cms_text" >
                  <input name="button2" type="button" onClick="showdate(document.all('dayEnd'))" value="时间" style="cursor:hand">
            </td>
          </tr>
		   <tr><td colspan="2" background="images/line.gif" height="1"></td>
		  <tr><td colspan="2" align="center"><input name="button" type="button" onClick="searchForm.submit()" value="<%=searchTypeDes%>" style="width:60; height:25;cursor:hand" >
		  <input name="button" type="button" onClick="reset()" value="重置" style="width:60; height:25;cursor:hand"></td></tr>
        </table>
		</FORM>
		  </td>
      </tr>
    </table>
 
	</td>
  </tr>
  <tr>
    <td><iframe src='../siteResource/site1/_template/index_foot.html' width="100%" height="100" scrolling=no marginwidth=0 marginheight=0 frameborder=0 vspace=0 hspace=0>
这里放页尾</iframe></td>
  </tr>
</table>
</body>
</html>

