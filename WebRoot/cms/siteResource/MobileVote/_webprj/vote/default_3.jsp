<%@ page contentType="text/html; charset=UTF-8"%><%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="cms"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><cms:siteinfo property="name"/></title>




<link rel="stylesheet"  href="../style/page.css">
<link rel="stylesheet" href="../style/jquery.mobile-1.4.2.min.css">
<script src="../script/jquery.js"></script>
<script src="../script/jquery.mobile-1.4.2.min.js"></script>
<script type="text/javascript" src="../script/nav.js"></script>
<script type="text/javascript" src="../script/bbscomment2.js"></script>	
<script type="text/javascript" src="../script/until.js"></script>	

<script type="text/javascript">
    var  selected_title_id=2;
    var  user_id=21018438;
    //接收参数
    function getParas(){
    	var strHref = window.document.location.href;
    	if(strHref.split("?").length>1){
    		selected_title_id = strHref.split("?")[1].split("&")[0].split("=")[1];
    		user_id = strHref.split("?")[1].split("&")[1].split("=")[1];
    	}
		
    }

	var commentlisturl = "<cms:siteparam name="appcontext"/>/vote/getTitleNList.freepage?jsonp_callback=?";		
	function showOrderBy(orderType) {
		
		document.getElementById(orderType).className = "select";
		var orderBy = document.getElementsByName("orderBy");
		for (var i=0; i<orderBy.length; i++) {
			if (orderBy[i].id != orderType) {
				document.getElementById(orderBy[i].id).className = "";
			}
		}
		
		document.getElementById("videosList").src = "<cms:siteparam name='appcontext'/>/document/showVideosList.freepage?siteId=<cms:siteinfo property='siteId'/>&channelId=<cms:channel property='channelId'/>&"+orderType+"=yes";
	}
	
	$(document).ready(function() {
		
		$(".re_explain").on("click",function()
				  {
				      $(".re_explain").find("div").toggleClass("small");
				      $("#expend").toggleClass("display");
					  $("#shrink").toggleClass("display");
				  }
				  );
		
		getParas();
		
 		showbbsvote(commentlisturl,'<cms:siteinfo property="siteId"/>','vote',<cms:siteparam name="bbsvotesize"/>); 
		
 		var siteId = '<cms:siteinfo property="siteId"/>';
		var channelId = '<cms:channel property="channelId"/>';
 		var pageURL = window.location.href;

		var siteName = '<cms:siteinfo property="name"/>';
 		var channelName = '<cms:channel property="name"/>';
 		var docrefer = document.referrer;
		var browserCounter =  '<cms:siteparam name="staticcontext"/>/counter/browserCounter.freepage?jsonp_callback=?';
		
		$.getJSON(browserCounter, { siteId:siteId, channelId:channelId, pageURL:pageURL,referer:docrefer, siteName:encodeURIComponent(siteName), channelName:encodeURIComponent(channelName) },
            function(data) {   
                 
            });
	 });
	var votedetailurl = "<cms:siteparam name="appcontext"/>/vote/getSurveyBy.freepage?jsonp_callback=?";		
	function getbbsvoteDetail(titleId){
		$(".vote_1").hide();
		  showbbsvoteDetail(votedetailurl,'<cms:siteinfo property="siteId"/>','vote',titleId,'<cms:uri link="components/score/gifs/rb_{0}.gif"/>');
		$("#vote_"+titleId).show();
		$(".c_1").attr("src","../images/cdown.jpg");
		$("#c_"+titleId).attr("src","../images/ctop.jpg");
	}
	var dovoteurl= "<cms:siteparam name="appcontext"/>/vote/doVote.freepage?jsonp_callback=?";
	var saveVoteDetailUrl = "<cms:siteparam name="appcontext"/>/voteMobile/saveVoteDetail.freepage?jsonp_callback=?";
function doVote(titleId,len){
	var id="";
	var questionId="";
	var dataDetail="";
	for(var k=0;k<len;k++){
		var obj=document.getElementsByName("RadioGroup"+titleId+"_"+k);
		for(var i=0;i<obj.length;i++){
			if(obj[i].checked==true){
			 	id= id+obj[i].value+";";
			 	var content = obj[i].parentNode.innerText;
			 	if(""==content||null ==content){
			 		content = obj[i].parentNode.childNodes[1].value;
			 	}/* else{
			 		content = obj[i].parentNode.childNodes[1].innerText;alert(content);
			 	} */
			 	dataDetail = dataDetail +"qid:"+obj[i].id+",type:0,oid:"+obj[i].value+",contents:"+content+";";
			}
		}
	}
	
	for(var k=0;k<len;k++){
		var obj=document.getElementsByName("checkboxGroup"+titleId+"_"+k);
		for(var i=0;i<obj.length;i++){
			if(obj[i].checked==true){
			 	id= id+obj[i].value+";";
			 	var content = obj[i].parentNode.innerText;
			 	if(""==content||null ==content){
			 		content = obj[i].parentNode.childNodes[1].value;
			 	}/* else{
			 		content = obj[i].parentNode.childNodes[1].innerText;alert(content);
			 	} */
			 	dataDetail = dataDetail +"qid:"+obj[i].id+",type:1,oid:"+obj[i].value+",contents:"+content+";";
			}
		}
	}	
	
	if(id==""){
	 alert("请选择投票项");
	 return;
	}

	var datas={
  		"strOptionID":id
  	};
  	var questionId="";
  	for(var k=0;k<len;k++){
		var obj=document.getElementsByName("text"+titleId+"_"+k);
		for(var i=0;i<obj.length;i++){
			if(obj[i].value!=""){
			 	datas[obj[i].id]=encodeURIComponent(obj[i].value);
			 	dataDetail = dataDetail +"qid:"+obj[i].id.substring(10,obj[i].id.length)+",type:2,oid:,contents:"+obj[i].value+";";
			}
		}
	}
		var details={
				"titleId":titleId,
				"detail":dataDetail,
				"userId":user_id
		}
	$.getJSON(saveVoteDetailUrl, details,
						function(data) {   
			             	
			        	}
					);
		$.getJSON(dovoteurl, datas,
				function(data) {   
	             	if (data == "success") {
		             	alert("投票成功");
						getbbsvoteDetail(titleId);
	             	} else {
						if(data!=""){
							alert(data);
						}
	             	}
	             	returnListPage();
	        	}
			);
		
		
		}
	var freeAnswersurl=	"<cms:siteparam name="appcontext"/>/vote/showVoteFreeAnswersList.freepage?";
	var pagesize=10;
	function openFreeAnswersPage(questionId){
		var siteId = '<cms:siteinfo property="siteId"/>';
		window.open(freeAnswersurl+"siteId="+siteId+"&questionID="+questionId+"&pagesize="+pagesize);
	}
	function returnListPage(){
		var returnUrl = '<cms:siteparam name="backpath"/>';
		window.location.href = returnUrl;
	}
</script>
<style>
.ui-input-text{margin:0px;}
textarea{width:100%}
</style>
</head>

<body>
<div data-role="page" id="page1" class="listView">
<div data-role="content" id="vote_list">
   
   
</div>


</div>


</body>
</html>
