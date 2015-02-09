function showcomment(url,docId,n)
{
	$.getJSON(url, { docId:docId,n:n },
			function(data) {   
             	if (data == null) {
             		
             	} else {
             		
             		var comm = "<div class=\"comment_title\">";
             		comm = comm + "<div class=\"comment_num\"><a href=\"#\">已有<span class=\"red_num\">";
             		comm = comm +data.total;
             		comm = comm +"</span></a>条留言</div>员工互动</div>";
             		comm = comm +"<ul class=\"comment_content\">";
             		try
             		{
	             		if(data.comments)
	             		{
		             		for (var i=0; i<data.comments.length; i++) {
			             		comm = comm +"<li>";
			             		comm = comm +"<div class=\"comment_user\">";
			             		if(data.comments[i].userName=="__quest"){
			             			comm = comm + "匿名用户";
			             		}else{
			             			comm = comm +data.comments[i].userName;
			             		}
			             		comm = comm +"<br/> "+data.comments[i].str_subTime.substring(0,16);
			             		comm = comm +"</div>";
			             		comm = comm +"<div class=\"comment\">";
			             		comm = comm +data.comments[i].docComment;
			             		comm = comm +"<div class=\"operation\"><a href=\"javascript:void(0)\" onclick=\"document.getElementById('docComment').focus()\">评论</a></div>";			             		
			             		comm = comm +"</div>";
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
	                 		showAllcomment(commenturl,docId);
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
			
			function showbbscomment(url,site,channel,n)
		{
			$.getJSON(url, {site:site,channel:channel,n:n },
					function(data) {   
		             	if (data == null) {
		             	} else {
		             		var comm="";
		             		try
		             		{
			             		if(data.comments)
			             		{
				             		for (var i=0; i<data.comments.length; i++) {
					             		comm = comm +"<li>";
					             		comm = comm +"<span>";
					             		if(data.comments[i].userName=="__quest"){
					             			comm = comm + "匿名用户";
					             		}else{
					             			comm = comm +data.comments[i].userName;
					             		}
					             		comm = comm +"：</span>";
					             		comm = comm +"  <a title='' target='_blank' href='"+data.comments[i].docUrl+"' class='linkTagClass '>";
					             		if(data.comments[i].docComment.length>10){
					             			comm = comm +data.comments[i].docComment.substring(0,10)+"...";
					             		}else{
					             			comm = comm +data.comments[i].docComment;
					             		}
					             		comm = comm +" </a>";
					             		comm = comm +"</li>";
				             		}
			             		}
		             		}
		             		catch(e)
		             		{
		             			alert(e);
		             		}
		             		$("#scrollbox").empty(); 
		             		$("#scrollbox").append(comm); 
			 				startmarquee(46,20,1000);
		             	}
		        	}
				);
		}
	  function startmarquee(lh,speed,delay){
			var t; 
			var p=false; 
			var o=document.getElementById("marqueebox"); 
			 o.innerHTML+=o.innerHTML; 
			 o.onmouseover=function(){p=true;} 
			o.onmouseout=function(){p=false;} 
			o.scrollTop = 0; 
			function start(){ 
			
				t=setInterval(scrolling,speed); 
				if(!p)
				{ 
					o.scrollTop += 1;
				} 
			} 
			function scrolling(){ 
				if(o.scrollTop%lh!=0){ 
					o.scrollTop += 1;
					if(o.scrollTop>=((o.scrollHeight/2)-1)){
						o.scrollTop = 0; 
						clearInterval(t); 
						setTimeout(start,delay); 
					}
				}else{ 
					clearInterval(t); 
					setTimeout(start,delay); 
				} 
			} 
		    setTimeout(start,delay);  
		}
	  
	  
		function showbbsvote(url,site,channelName,count)
		{
			$.getJSON(url, {site:site,count:count,channelName:channelName },
					function(data) {   
		             	if (data == null) {
		             	} else {
		             		var comm="";
		             		try
		             		{
			             		if(data)
			             			
			             		{
				             		for (var i=0; i<data.length; i++) {
					             		
					             		if(selected_title_id == data[i].id){
					             			comm = comm +  "<div class=\"re_title\">"+data[i].name+" ["+data[i].foundDate+"]";
					             			comm = comm + "<br/><h4> "+data[i].content+" </h4></div> " ;
						             		comm = comm + "<div id=\"vote_"+data[i].id+"\"> </div> " ;
					             		}
					             		
				             		}
			             		}
		             		}
		             		catch(e)
		             		{
		             			alert(e);
		             		}
		             		$("#vote_list").empty(); 
		             		$("#vote_list").append(comm);
		             		if(data){
		             			getbbsvoteDetail(selected_title_id);
		             		}
		             	}
		        	}
				);
		}
		
		function showbbsvoteDetail(url,site,channelName,titleId,picpath)
		{
			$.getJSON(url, {titleId:titleId,picpath:picpath},
					function(data) {   
		             	if (data == null) {
		             	} else {
		             		var comm="";
		             		try
		             		{
			             		if(data)
			             		{
			             			
			             			comm = comm +"<div class=\"re_explain\"><div class=\"small\"><p>";
			             			comm = comm + "离投票结束还有："+data.endTime+"| 发起者："+data.foundername;
			             			comm = comm + "</p></div></div>";
			             			comm = comm + "<div class=\"re_topic\">";
					             		if(data.questions && data.questions.length >0 ){
					             			for(var q=0;q<data.questions.length;q++)
					             			{
					             				comm = comm + " <ul> ";
					             				if(data.questions[q].style=="2"){
					             					comm = comm +"<div class=\"topic_title\" style=\"border-bottom:none\">"+ data.questions[q].title+"</div>";//问题
					             				}else{
					             					comm = comm +"<div class=\"topic_title\">"+ data.questions[q].title+"</div>";//问题
					             				}
					             				
					             				var qid = data.questions[q].id;
					             				if(data.questions[q].style=="0"){
						             				for(var i=0;i<data.questions[q].items.length;i++){//遍历选项
						             					
						             					comm = comm + "<li><input type=\"radio\" name=\"RadioGroup"+titleId+"_"+q+"\" value="+data.questions[q].items[i].id+" id='"+qid+"'/><span>"+data.questions[q].items[i].options+"</span>";
									             		comm = comm +"<span>"+data.questions[q].items[i].score ;
									             		comm = comm +data.questions[q].items[i].count +"票</span></li>";
									             		
						             				}
						             				comm = comm + "  </ul>";
					             				}else if(data.questions[q].style=="1"){
					             					for(var i=0;i<data.questions[q].items.length;i++){
						             					comm = comm + "  <li><input type=\"checkbox\" name=\"checkboxGroup"+titleId+"_"+q+"\" value="+data.questions[q].items[i].id+"  id='"+qid+"'/><span>"+data.questions[q].items[i].options+"</span>";
									             		comm = comm +"<span>"+data.questions[q].items[i].score ;
									             		comm = comm +data.questions[q].items[i].count + "票</span></li> ";
									             		
						             				}
					             					comm = comm + "  </ul>";
					             				}else if(data.questions[q].style=="2"){
					             					//comm = comm +" <a herf='javascript:void(0);' onclick='openFreeAnswersPage("+data.questions[q].id+")')>自由答案</a>";
					             					//comm = comm + "  ";
					             					comm = comm + "<div class=\"ml_10\"><textarea  name=\"text"+titleId+"_"+q+"\" id=questionId"+data.questions[q].id+" ></textarea><\div>";
								             		//comm = comm + "    <br/>  ";
								             		comm = comm + "  </ul> ";
					             				}else if(data.questions[q].style=="3"){
						             				for(var i=0;i<data.questions[q].items.length;i++){
						             					if(data.questions[q].items[i].options.indexOf("other")>=0){
						             						comm = comm + "  <li><input type=\"radio\" name=\"RadioGroup"+titleId+"_"+q+"\" value="+data.questions[q].items[i].id+" id='"+qid+"' /><input type=\"text\" value=\"请输入您的选项内容                              \" /></li> ";
						             					}else{
						             						comm = comm + "  <li><input type=\"radio\" name=\"RadioGroup"+titleId+"_"+q+"\" value="+data.questions[q].items[i].id+" id='"+qid+"' />"+data.questions[q].items[i].options+"</li> ";
						             					}
						             					
									             		
						             				}
						             				comm = comm + "  </ul> ";
					             				}else if(data.questions[q].style=="4"){
					             					for(var i=0;i<data.questions[q].items.length;i++){
						             					if(data.questions[q].items[i].options.indexOf("other")>=0){
						             						comm = comm + "  <li><input type=\"checkbox\" name=\"checkboxGroup"+titleId+"_"+q+"\" value="+data.questions[q].items[i].id+" id='"+qid+"' /><input type=\"text\" value=\"请输入您的选项内容                              \" /></li> ";
						             					}else{
						             						comm = comm + "  <li><input type=\"checkbox\" name=\"checkboxGroup"+titleId+"_"+q+"\" value="+data.questions[q].items[i].id+" id='"+qid+"' />"+data.questions[q].items[i].options+"</li> ";
						             					}
						             					
									             		
						             				} 
					             					comm = comm + "  </ul>";
					             				}
					             			}
					             		}
					             		
					             		comm = comm + " </div> "; 
					             		comm = comm + "<div class=\"re_button\">" +
					             				      "<a class=\"cancel\" href=\"javascript:void(0);\" onclick='returnListPage()'>取消</a>" +
					             					  "<a class=\"submit\" href=\"javascript:void(0);\" onclick='doVote("+titleId+","+data.questions.length+")'>提交</a>" +
					             					  "</div>";
			             		}
		             		}
		             		catch(e)
		             		{
		             			alert(e);
		             		}
		             		$("#vote_"+titleId).empty(); 
		             		$("#vote_"+titleId).append(comm); 
		             	}
		        	}
				);
		}