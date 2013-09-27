<%@ page language="java"  pageEncoding="utf-8" session="false"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
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
			//	showAllcomment("queryDocumentCommentList.freepage",${docId});
			 	loadMoreAnswer();
			 
			 });
			 var moreAnswerurl='getAnswersOfQstionListInfo.freepage?jsonp_callback=?';
			var offset=0;
			var pagesize=${pagesize};
			function loadMoreAnswer(){
			 	var html=$("#comment_content").html();
			 	var datas={
				  		"questionID":${questionID},
				  		"offset":offset,
				  		"pagesize":pagesize
				  	};
			 		$.getJSON(moreAnswerurl, datas,
						function(data) {
			             	if (data != "") {
			             		
			             	   var moreHtml="";
				             	for (var i=0; i<data.length; i++) {
				             		moreHtml=moreHtml+"<li onclick=\"this.className==''?this.className='select_comment':this.className=''\" ";
				             		if((i+offset)%2==1){
				             			moreHtml=moreHtml+" style='background-color:#f2f1e9' ";
				             		}
				             		moreHtml=moreHtml+" ><div class=\"comment_user\">"+data[i].when.substring(0,16)+"</div><div class='comment' style='margin-left:10px;'>"+data[i].answer+"</div></li>";
				             	}
				             	offset=offset+pagesize;
				             	$("#comment_content").html(html+moreHtml);
				             	if(offset>=${votecount}){
				             		$("#more").hide();
				             	}
			             	} else {
								
			             	}
			        	}
					);
		}

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
        <DIV id=videoTitle class=video_bt>
        <pg:beaninfo requestKey="question">
		<pg:cell colName="title"></pg:cell>
		</pg:beaninfo>
		</DIV>
    </DIV>
    <div class="comment_box">
        <div id="allcomments">
      	  <div id="customContent">
      	  <ul class="comment_content" id="comment_content">
	        <pg:list requestKey="answersList">
	        	<li onclick="this.className==''?this.className='select_comment':this.className=''">
	        	<div class="comment_user"><pg:cell colName="when" /></div>
	        	 <div class="comment" ><pg:cell colName="answer" htmlDecode="true"/>
	        	</div>
	        	</li>
	        </pg:list>
	        </ul>	
	        </div>
	    </div>   
      </div>

        <div class="page" style="margin-left:0px;" id="more">
         <div align="center"><a  href="javascript:void(0);" onclick="loadMoreAnswer();">更多</a></div>
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
