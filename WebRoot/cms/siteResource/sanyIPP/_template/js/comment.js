function showcomment(url,docId,n)
{
	$.getJSON(url, { docId:docId,n:n },
			function(data) {   
             	if (data == null) {
             		
             	} else {
             		
             		var comm = "<div class=\"comment_title\">";
             		comm = comm + "<div class=\"comment_num\"><a href=\"#\">已有<span class=\"red_num\">";
             		comm = comm +data.total
             		comm = comm +"</span></a>条评论</div>视频评论</div>";
             		comm = comm +"<ul class=\"comment_content\">";
             		try
             		{
	             		if(data.comments)
	             		{
		             		for (var i=0; i<data.comments.length; i++) {
			             		comm = comm +"<li>";
			             		comm = comm +"<div class=\"comment_user\">";
			             		comm = comm +data.comments[i].userName
			             		comm = comm +"</div>";
			             		comm = comm +"<div class=\"comment\">";
			             		comm = comm +data.comments[i].docComment
			             		comm = comm +"</div>"
			             		comm = comm +"<div class=\"comment_date\">IP:";
			             		comm = comm +data.comments[i].userIP +"&nbsp;"
			             		comm = comm +data.comments[i].str_subTime
			             		comm = comm +"</div>";
			             		comm = comm +"<div class=\"operation\"><a href=\"javascript:void(0)\" onclick=\"document.getElementById('docComment').focus()\">评论</a></div>";			             		
			             		comm = comm +"</li>";
		             		}
	             		}
             		}
             		catch(e)
             		{
             			alert(e);
             		}
             		comm = comm +"</ul>";
             		$("#comments").empty(); 
             		$("#comments").append(comm); 
             		
             	}
             	
             	
        	}
		);
}
function sub(commentposturl,docId,channelId,commenturl,n,isall) {
				if (!validate()) {
					return;
				}
				
				var isGuest = document.getElementById("isGuest").checked;
				var commentUser = document.getElementById("commentUser").value;
				var docComment = document.getElementById("docComment").value;
				
				//var url =  'addNewComment.freepage?jsonp_callback=?';
		
				$.getJSON(commentposturl, { docId:docId, channelId:channelId, commentUser:encodeURIComponent(commentUser), isGuest:isGuest, docComment:encodeURIComponent(docComment) },
					function(data) {
						if(data.msg == "success")
						{							
							if (data.aduitSwitchFlag == "0") {
		                 		
		                 		alert("评论已提交审核,审核通过才会发布！");
		                 		
		                 	} 
							else{
		             			alert("评论成功！");
		             		}
							document.getElementById("docComment").value = "";
						}
	                 	
	                 	else {
	                 		alert(data.error);
	                 	}
	                 	if(isall)
                 		{
	                 		showAllcomment(commenturl,docId)
                 		}
	                 	else
                 		{
	                 		showcomment(commenturl,docId,n);
                 		}
	                 	
	            	}
				);
		  }

		 function showAllcomment(allcommenturl,docId)
		 {
			 $("#allcomments").load(allcommenturl + " #customContent", { docId:docId }, function(data){
					
				});
		 }
			
			function validate() {
				var isGuest = document.getElementById("isGuest");
				if (!isGuest.checked) {
					if (document.getElementById("commentUser").value == "") {
						alert("用户名不能为空！");
						document.getElementById("commentUser").focus();
						return false;
					} else if (document.getElementById("commentUser").value.search(/[\\\/\|:\*\?<>"']/g) != -1) {
						alert("用户名不能包含特殊字符！");
						document.getElementById("commentUser").focus();
						return false;
					}
				}
				
				if (document.getElementById("docComment").value == "") {
					alert("评论不能为空！");
					document.getElementById("docComment").focus();
					return false;
				} else if (document.getElementById("docComment").value.search(/[\\\/\|:\*\?<>"']/g) != -1) {
					alert("评论不能包含特殊字符！");
					document.getElementById("docComment").focus();
					return false;
				}
				
				return true;
			}