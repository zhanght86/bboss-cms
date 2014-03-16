<%@ page language="java"  pageEncoding="utf-8" import="java.util.*" session="false"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ page import="java.io.BufferedReader"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.FileReader"%>
<%@ page import="java.io.FileWriter"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.handler.ContentHandler"%>

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
<title>搜索引擎--查询结果</title>
<link rel="stylesheet" href="inc/mycss.css" type="text/css">
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
  <tr>
    <td>
	<table width="98%" border="0" align="center" cellpadding="0" cellspacing="6">
	<form  action="search_process.jsp?indexId=<%=indexId%>&amp;siteId=<%=siteId%>&amp;searchType=<%=searchType%>" method="post" name="searchForm" id="searchForm">
      <tr>
        <td width="10%">关键字：</td>
        <td width="48%">
		<input type="text" name="queryString" size="38" maxlength="50" value="<%=queryString%>" />
                <input type="hidden" name="flag" value="1">	
			<input name="button" type="button" onClick="searchForm.submit()" value="<%=searchTypeDes%>" width="57" height="24" />					</td>
        <td width="16%"><input type="checkbox" name="isInResult" value="on" />
            结果中查询 </td>
	    <td width="14%"><input name="button" type="button" onClick="searchForm.action='search_process.jsp?indexId=<%=indexId%>&amp;siteId=<%=siteId%>&amp;searchType=<%=searchType%>&amp;sort=relevance';searchForm.submit()" value="按相关度排序" width="57" height="24" style="cursor:hand" /></td>
		<td width="12%"><input name="button" type="button" onClick="searchForm.action='search_process.jsp?indexId=<%=indexId%>&amp;siteId=<%=siteId%>&amp;searchType=<%=searchType%>&amp;sort=time';searchForm.submit()" value="按时间排序" width="57" height="24" style="cursor:hand" /></td>
      </tr>
	  </form>
    </table></td>
  </tr>
  <tr><td background="images/line.gif" height="1"></td>
  <tr>
    <td>&#12304;搜索结果&#12305;<font color="#FF9966">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          &#9679;关键字：&nbsp;&nbsp;<%=key%></font></td>
  </tr>
  <tr>
    <td><table width="98%" border="0" align="center" cellpadding="5" cellspacing="0">
      <pg:listdata dataInfo="CMSSearchResultList" keyName="CMSSearchResultList" />
      <!--分页显示开始,分页标签初始化-->
      <pg:pager maxPageItems="<%=intPageSize%>" scope="request" data="CMSSearchResultList" isList="false">
        <%
				boolean rowCountFlag = false;
			%>
        <!--检测当前页面是否有记录-->
        <pg:notify>
          <tr  class="labeltable_middle_tr_01">
            <td colspan="100" align='center' height="18px"><font color="red">对不起，没有找到您要的相关记录!</font></td>
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
              <br />
            </td>
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
      
      </pg:pager>
    </table></td>
  </tr>  
  <tr>
    <td><iframe src='../siteResource/site1/_template/index_foot.html' width="100%" height="100" scrolling=no marginwidth=0 marginheight=0 frameborder=0 vspace=0 hspace=0>
这里放页尾</iframe></td>
  </tr>
</table>
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><pg:siteinfo property="name"/></title>
<pg:config enablecontextmenu="false" enabletree="false"/>
<link href="<pg:sitedomain/>/css/main.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<pg:sitedomain/>/js/nav.js"></script>

<script type="text/javascript" src="<pg:sitedomain/>/js/comment.js"></script>


<script type="text/javascript">
			var commentlisturl = "queryDocumentCommentList.freepage";
			var n = 10;
			var commentposturl =  'addNewComment.freepage?jsonp_callback=?';
			$(document).ready(function() {
				showAllcomment("queryDocumentCommentList.freepage",${docId});
			 	
			 
			 });
</script>
<link href="<pg:sitedomain/>/css/foot2.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="wrapper">
  <div  class="top">
    <div class="logo_top"><img src="<pg:sitedomain/>/images/top_logo.jpg" width="303" height="31" /></div>    
  </div>
  <div class="positon2"><pg:position/>
  </div>
  <div class="container">
    <DIV class=video_k>
        <DIV id=videoTitle class=video_bt><a href="${docurl}">${docTitle}</a></DIV>
    </DIV>
    <div class="comment_box">
        
        <div id="allcomments">
	        
	    </div>   
        <pg:equal actual="${commentSwitch}" value="0">
			<div class="comment_1">
	          <div class="comment_title1">我要评论</div>
	          <table width="100%" border="0" cellspacing="3" cellpadding="0">
	            <tr>
	              <td width="12%"><div align="right">用户名：</div></td>
	              <td width="88%"><input id="commentUser" class="input1" type="text" name="commentUser" />
	                <input id="isGuest" type="checkbox" checked="checked" value="true" name="isGuest" />
	                匿名</td>
	            </tr>
	            <tr>
	              <td valign="top"><div align="right">评论内容：</div></td>
	              <td><textarea name="docComment" id="docComment" class="textarea1"></textarea></td>
	            </tr>
	            <tr>
	              <td>&nbsp;</td>
	              <td class="bt_position"><a class="bt_1" onclick="sub(commentposturl,${docId},${ channelId},commentlisturl,n,true)" href="javascript:void(0)"><span>评论</span></a></td>
	            </tr>
	          </table>
	        </div>
        </pg:equal>
      </div>
    </div>
</div>
<div class="footer">
  <div class="isany"></div>
  <div class="quality"></div>
  <div class="copy_right"> <a href="http://www.sanygroup.com/group/zh-cn/" >三一集团门户</a>|<a href="http://www.sanygroup.com/group/zh-cn/media/product_download.htm">资料下载</a>|<a href="http://www.sanygroup.com/group/zh-cn/foot/item.htm">使用条款</a>|<a href="http://www.sanygroup.com/group/zh-cn/foot/contact_us.htm">联系我们</a>|<a href="http://www.sanygroup.com/group/zh-cn/foot/sitemap.htm">网站地图</a><br/>
    &nbsp;
    <script type="text/javascript">
		copyright=new Date();
		update=copyright.getFullYear();
		document.write("1989-"+ update + " 三一集团有限公司 版权所有 ");
	  </script>
</div></div>
</body>
</html>
