<%@ page language="java"  pageEncoding="utf-8" session="false"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>三一集团内网宣传平台</title>
<pg:config enablecontextmenu="false"/>
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
        <DIV id=videoTitle class=video_bt><a href="${docurl}">凤凰卫视：日本福岛救援工程师感恩三一行</a></DIV>
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
