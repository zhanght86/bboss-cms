<%--
	描述：文档评论列表
	作者：gw_hel
	版本：1.0
	日期：2012-08-07
--%>
<%@ page language="java"  pageEncoding="utf-8" session="false"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<pg:config enablecontextmenu="false"/>

<html>
	<head>
		<link href="<pg:sitedomain/>/css/main.css" rel="stylesheet" type="text/css" />
		<link href="<pg:sitedomain/>/css/comment.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<pg:sitedomain/>/js/jquery-1.7.2.js"></script>
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/commontool.js"></script>
		
		<script type="text/javascript">
		
			$(document).ready(function() {
				$("#custombackContainer").load("queryDocumentCommentList.freepage #customContent", { docId:${docId} }, function(data){
					loadjs();
				});
			 	
			 	parent.document.getElementById("commentList").height=10*85+"px";
			 });
			 
			function sub() {
				if (!validate()) {
					return;
				}
				
				var isGuest = document.getElementById("isGuest").checked;
				var commentUser = document.getElementById("commentUser").value;
				var docComment = document.getElementById("docComment").value;
				
				var url =  'addNewComment.freepage?jsonp_callback=?';
		
				$.getJSON(url, { docId:${docId}, channelId:${channelId}, commentUser:encodeURIComponent(commentUser), isGuest:isGuest, docComment:encodeURIComponent(docComment) },
				function(data) {   
                 	if (data = "success") {
                 		if ("${aduitSwitchFlag}" == "0") {
                 			alert("评论成功，已提交评论审核！");
                 		} else {
                 			alert("评论成功！");
                 		}
                 	} else {
                 		alert(data);
                 	}
                 	
                 	parent.document.getElementById("commentList").src = parent.document.getElementById("commentList").src;
            	});
		  }
			
			function validate() {
				var isGuest = document.getElementById("isGuest");
				if (!isGuest.checked) {
					if (document.getElementById("commentUser").value == "") {
						alert("用户名不能为空！");
						return false;
					} else if (document.getElementById("commentUser").value.search(/[\\\/\|:\*\?<>"']/g) != -1) {
						alert("用户名不能包含特殊字符！");
						return false;
					}
				}
				
				if (document.getElementById("docComment").value == "") {
					alert("评论不能为空！");
					return false;
				} else if (document.getElementById("docComment").value.search(/[\\\/\|:\*\?<>"']/g) != -1) {
					alert("评论不能包含特殊字符！");
					return false;
				}
				
				return true;
			}
			
			function showAll() {
				window.open("showAllCommentList.freepage?docId=${docId}");
			}
		</script>
	</head>
	<body>
		<div class="comment_box">
			<div class="comment_title">
	 		 <div class="comment_num"></div>
          	 	视频评论
         	</div>
         	<ul class="comment_content">
         		<div id="custombackContainer"></div>
         	</ul>
		
		<pg:equal actual="${commentSwitch}" value="0">
			 <div class="comment_1">
	          <div class="comment_title1">我要评论</div>
	          <table width="100%" border="0" cellspacing="3" cellpadding="0">
	            <tr>
	              <td width="12%"><div align="right">用户名：</div></td>
	              <td width="88%"><input id="commentUser" name="commentUser" type="text" class="input1" />
	                <input id="isGuest" name="isGuest" type="checkbox" value="true"  checked="checked"/>
	                匿名</td>
	            </tr>
	            <tr>
	              <td valign="top"><div align="right">评论内容：</div></td>
	              <td><textarea id="docComment"  name="docComment" class="textarea1"></textarea></td>
	            </tr>
	            <tr>
	              <td>&nbsp;</td>
	              <td class="bt_position"><a href="#" class="bt_1" onClick='sub()'><span>评论</span></a></td>
	            </tr>
	          </table>
	        </div>
        </pg:equal>
       </div>
	</body>
</html>
		
