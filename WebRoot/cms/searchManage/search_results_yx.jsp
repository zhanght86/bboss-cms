<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.io.BufferedReader"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.FileReader"%>
<%@ page import="java.io.FileWriter"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.handler.ContentHandler"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="cms"%>
<% 
	String siteId = (String)session.getAttribute("siteId");
	String searchType = (String)session.getAttribute("searchType");
	
	String key=(String)session.getAttribute("qStr");							//查询条件,and 或 or 连接的字符串
	if(key==null)
	{
		key = "";
	}
	String strKey=(String)session.getAttribute("keyWord");						//查询条件,& 或 || 连接的字符串
	if(strKey==null)
	{
		strKey = "";
	}
	String queryString=(String)session.getAttribute("queryString");				//原查询字符串
	if(queryString==null)
	{
		queryString = "";
	}
	String indexId = (String)session.getAttribute("indexId");
	CMSSearchManager sm = new CMSSearchManager();
	String searchTypeDes = "";
	if(indexId == null || "all".equals(indexId))
		searchTypeDes = sm.getSearchButtonValue(searchType);
	else
		searchTypeDes = sm.getSearchButtonValue("");
%>

<%
	int intPageSize;           //每页页显示的记录数
	//设置一页显示的记录数
	String strPageSize=(String)session.getAttribute("hitsPerSet");
	if(strPageSize==null||strPageSize=="")
	{
		strPageSize = "10";//默认设置为10页
	}	
	intPageSize = java.lang.Integer.parseInt(strPageSize);
%>
<html>
<head>
<title>中国银都·永兴县政府门户网站  [搜索引擎]</title>

<link rel="stylesheet" href="inc/mycss.css" type="text/css">
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
</head>
<body>
<table width="1004" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" align="center">
  <tr>
    <td height="4"><iframe frameborder="0" scrolling="no" src="/sitepublish/site1/index_top.html" width="100%" height="168"></iframe></td>
  </tr>
  <tr>
    <td height="34" colspan="3"  bgcolor="#0074B3"><cms:simpleNavigator totalWidth="1000" subColWidth="65" site="yongxinggov" subWidth="330" column_num="4" height="30"  bgColor="#0074B3"/></td>
  </tr>  
  <tr>
    <td>
	<table width="1004" border="0" align="center" cellpadding="0" cellspacing="0" >
	<form  action="search_process_yx.jsp?indexId=<%=indexId%>&amp;siteId=<%=siteId%>&amp;searchType=<%=searchType%>" method="post" name="searchForm" id="searchForm">
      <tr>
        <td height="26" colspan="5">
          <table width="1004" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
            <tr>
              <td  background="images/enteryx_02.gif" width="36" height="30" align="center"><img src="images/enteryx_11.gif" width="14" height="13" border="0" /></td>
              <td  background="images/enteryx_02.gif" width="221" class="f5">您所在的位置 ：<a href="/" target="_self">首页</a> &gt; 站内搜索</td>
              <td  background="images/enteryx_02.gif" class="f5"><script language="JavaScript" src="../nl.js"></script></td>
              </tr>
          </table></td>
        </tr>
      <tr>
        <td height="7" colspan="5"></td>
        </tr>
      <tr>
        <td width="10%"><div align="right">关键字：</div></td>
        <td width="54%">
		<input type="text" name="queryString" size="38" maxlength="50" value="<%=queryString%>" />
                <input type="hidden" name="flag" value="1">	
				<input name="button" type="button" onClick="searchForm.submit()" value="<%=searchTypeDes%>" width="57" height="24" />					</td>
        <td width="12%"><input type="checkbox" name="isInResult" value="on" />
            结果中查询 </td>
	    <td width="13%"><input name="button" type="button" onClick="searchForm.action='search_process_yx.jsp?indexId=<%=indexId%>&amp;siteId=<%=siteId%>&amp;searchType=<%=searchType%>&amp;sort=relevance';searchForm.submit()" value="按相关度排序" width="57" height="24" style="cursor:hand" /></td>
		<td width="11%"><input name="button" type="button" onClick="searchForm.action='search_process_yx.jsp?indexId=<%=indexId%>&amp;siteId=<%=siteId%>&amp;searchType=<%=searchType%>&amp;sort=time';searchForm.submit()" value="按时间排序" width="57" height="24" style="cursor:hand" /></td>
      </tr><tr>
        <td height="7" colspan="5"></td>
        </tr>
	  </form>
    </table></td>
  </tr>
  <tr><td background="images/line.gif" height="1"></td>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&#12304;搜索结果&#12305;<font color="#FF9966">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          &#9679;关键字：&nbsp;&nbsp;<%=key%></font></td>
  </tr>
  <tr>
    <td><table width="99%" border="0" align="center" cellpadding="5" cellspacing="0">
      <pg:listdata dataInfo="CMSSearchResultList" keyName="CMSSearchResultList" />
      <!--分页显示开始,分页标签初始化-->
      <pg:pager maxPageItems="<%=intPageSize%>" scope="request" data="CMSSearchResultList" isList="false">
        <%
				boolean rowCountFlag = false;
			%>
        <!--检测当前页面是否有记录-->
        <pg:notify>
          <tr  class="labeltable_middle_tr_01">
            <td colspan="100" align='center' height="232px"><font color="red">对不起，没有找到您要的相关记录!</font></td>
          </tr>
          <%
					rowCountFlag = true;
				%>
        </pg:notify>
        <%
				if(!rowCountFlag){
			%>
        <tr class="labeltable_middle_tr_01">
          <td colspan="100"><div class="Data_List_Table_Bottom">本次检索耗时：<%=CMSSearchManager.searchTime%>秒，共检索出
            <pg:rowcount />
            条记录
            <pg:index />
          </div></td>
        </tr>
        <%
				}
			%>
        <!--list标签循环输出每条记录-->
        <pg:list>
          <%
				String content = dataSet.getString("content");
				CMSKeyWord kw = new CMSKeyWord(strKey);
				String str1 = sm.getInterceptContent(key,content);
				String outString = kw.display(str1);
				
				String contentType = dataSet.getString("contentType");
				String contentTypeDes = "[HTM]";
				if(ContentHandler.EXCEL_FILEFOMAT.equals(contentType))
				{
					contentTypeDes = "[XLS]";
				}
				else if(ContentHandler.PDF_FILEFOMAT.equals(contentType))
				{
					contentTypeDes = "[XLS]";
				}
				else if(ContentHandler.PPT_FILEFOMAT.equals(contentType))
				{
					contentTypeDes = "[PPT]";
				}
				else if(ContentHandler.RTF_FILEFOMAT.equals(contentType))
				{
					contentTypeDes = "[PPT]";
				}
				else if(ContentHandler.WORD_FILEFOMAT.equals(contentType))
				{
					contentTypeDes = "[DOC]";
				}
				else if(ContentHandler.TEXT_HTML_FILEFOMAT.equals(contentType))
				{
					contentTypeDes = "[HTM]"; 
				}
				
				int rank = dataSet.getRowid();					//编号
				String bgcolor = (rank%2==0)?"#F3FBFB":"#EEF5FA";
				
				String href = dataSet.getString("href");
			%>
          <tr>
            <td valign="top" bgcolor="<%= bgcolor %>"><b>
              <pg:rowid increament="1"/>
            </b></td>
            <td bgcolor="<%= bgcolor %>"><a href="<%= response.encodeURL(href) %>" style="cursor:hand" ><%=contentTypeDes%> &nbsp; <b>
              <pg:cell colName="title" defaultValue="" />
              </b>
                  <pg:cell colName="score" defaultValue="" />
              %</a> <br />
              描述: <%=outString%> <br />
              <a href="<%= response.encodeURL(href) %>" style="cursor:hand"><font color="black">
                <pg:cell colName="url" defaultValue="" />
                &nbsp;
                <pg:cell colName="published" dateformat="yyyy/MM/dd HH:mm:ss" defaultValue="" />
                </font></a> <br />
              <br />            </td>
          </tr>
        </pg:list>
        <%
				if(!rowCountFlag){
			%>
        <tr class="labeltable_middle_tr_01">
          <td colspan="100"><div class="Data_List_Table_Bottom">本次检索耗时：<%=CMSSearchManager.searchTime%>秒，共检索出
            <pg:rowcount />
            条记录
            <pg:index />
          </div></td>
        </tr>
        <%
				}
			%>
        <input id="queryString" name="queryString2" value="&lt;pg:querystring/&gt;" type="hidden" />
      </pg:pager>
    </table></td>
  </tr>  
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
  <tr>
    <td height="3" bgcolor="#0074B3"></td>
  </tr>
  <tr>
    <td align="center"><table cellspacing="0" cellpadding="0">
      <tr align="middle">
        <td colspan="2" align="center"> <a onclick="this.style.behavior='url(#default#homepage)';this.setHomePage('http://www.yxx.gov.cn/');" href="#">设为首页</a> | <a href="javascript:window.external.addFavorite('http://www.yxx.gov.cn/','中国银都-永兴县政府门户网站');">加入收藏 </a>| <cms:outline channel="关于我们" count="1" site="yongxinggov"><cms:link target="_blank" colName="title">关于我们</cms:link></cms:outline> | <cms:outline channel="联系我们" count="1" site="yongxinggov"><cms:link target="_blank" colName="title">联系我们</cms:link></cms:outline> | <cms:outline channel="网站声明" count="1" site="yongxinggov"><cms:link target="_blank" colName="title">版权申明</cms:link></cms:outline> | <a href="/login.jsp" target="_blank">管理员登录</a>  </td>
      </tr>
      <tr>
        <td><div align="center">
          <p>主办单位：永兴县人民政府　<a href="/sitepublish/site2/index.htm" target="_blank">承办单位：永兴县人民政府信息化工作办公室（县经济信息中心）</a><br />
            Copyright2002-2008   版权所有　文章及图片未经许可不得复制、转载
            <br />
			湘ICP备05010515号</p>
        </div></td>
      </tr>
    </table></td>
  </tr>
  <tr><td height="40">&nbsp;</td></tr>
</table></td>
  </tr>
</table>
</body>
</html>