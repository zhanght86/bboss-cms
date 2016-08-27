<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.*"%>
<%
	String searchType = (String)session.getAttribute("searchType");
	CMSSearchManager sm = new CMSSearchManager();
	String searchTypeDes = sm.getSearchButtonValue(searchType);
%>
<html>
<head>
<title>搜索引擎--搜索帮助</title>
<link rel="stylesheet" href="inc/mycss.css" type="text/css">
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
    <td><form action="search_process.jsp" method="post" name="searchForm" id="searchForm">
      <p>&nbsp;</p>
      <table width="50%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
          <td align="center" valign="middle">关键字：
            <input type="text" name="queryString" size="38" maxlength="50" />          </td>
          <td width="11%" align="center" valign="middle"><input name="button" type="button" onclick="searchForm.submit()" value="<%=searchTypeDes%>"  style="width:60; height:25;cursor:hand" />          </td>
          <td width="19%" align="center" valign="middle">&nbsp;</td>
          </tr>
      </table>
    </form></td>
  </tr>
   <tr><td background="images/line.gif" height="1"></td>
  <tr>
    <td style="padding:20 0"><table width="80%" border="0" align="center" cellpadding="0" cellspacing="0" style="border:6px solid #E1E1E1">
      <tr>
        <td style="padding:20"><table width="100%" border="0" cellspacing="0" cellpadding="10" align="center" style="border:1px solid #e1e1e1">
          <tr><td style="font-size:14px; font-weight:bold"><img src="images/dot_red.gif" width="7" height="7" hspace="6" align="absmiddle"><font color="#DF0218">帮助</font></td>
		</tr>
        <tr><td background="images/line.gif" height="1"></td>
          <tr>
            <td style="padding:10 20"><p><b>a.	一般搜索</b>
              </p>
              <p>可以对中英文进行检索。多个单词可以通过逻辑&quot;与&quot;、&quot;或&quot;,&quot;+&quot;(包含)，&quot;-&quot;(非)关系进行组合检索。缺省定为：与关系。</p>
              <p> 如：在界面输入：&quot;黄伯云 材料&quot;,搜索语句自动转为&quot; 黄伯云 and 材料&quot;
                用户可以自己输入由自定义的逻辑关系组合搜索。                </p>
              <p>如：在界面输入：&quot; 黄伯云 and 材料&quot;，搜索包含&quot; 黄伯云&quot;和&quot;材料&quot;的页面。                </p>
              <p>输入：&quot;三一集团 or 杰瑞&quot; ，搜索包含&quot; 搜索包含&quot; 黄伯云&quot;或&quot;材料&quot;的页面。</p>
              <p> 输入：(三一集团 OR 杰瑞) AND 铁道校区，搜索包含&quot;三一集团&quot;和&quot;铁道校区&quot;或&quot;杰瑞&quot;和&quot;铁道校区&quot;的页面。                </p>
              <p>注意：组合所用的括号是英文的括号。&quot;and&quot;和&quot;or&quot;可以任意大小写。 </p>
              <p>&nbsp;</p>
              <p><b>b.	高级搜索</b></p>
              <p> 选择关键字链接的逻辑运算苻,如果选&quot;or&quot;，则单词间用&quot;or&quot;链接。          </p>
              <p>通过选择每页显示结果数，用户可以自定义页面每页显示的结果条数。
                通过选择搜索站点，用户可以自定义搜索的范围。</p>
              <p>如：在中南大学站点搜索&quot;黄伯云&quot;，则要在&quot;搜索站点&quot;栏选择&quot;中南大学&quot;.
                通过选择网页发布时间，用户可以选择在指定时间段发布的页面。 </p></td>
          </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td><iframe src='../siteResource/site1/_template/index_foot.html' width="100%" height="100" scrolling=no marginwidth=0 marginheight=0 frameborder=0 vspace=0 hspace=0>
这里放页尾</iframe></td>
  </tr>
</table>
</body>     
</html> 




