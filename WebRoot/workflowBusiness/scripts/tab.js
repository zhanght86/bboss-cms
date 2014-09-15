/**选项卡tab***/
function setTab(m,n){
 var lit = document.getElementById("menu"+m).getElementsByTagName("a");
 var lim = document.getElementById("main"+m).getElementsByTagName("ul");
 document.getElementById("main"+m).style.display="block";
 for (var i=0;i<lit.length-2;i++){
	   lit[i].className=i==n?"current":"spread";
	   lim[i].style.display=i==n?"block":"none";
	
 }
}
function setTabC(m,n){
 var lit = document.getElementById("menu"+m).getElementsByTagName("a");
 var lim = document.getElementById("main"+m).getElementsByTagName("ul");
 document.getElementById("main"+m).style.display="block";
 for (var i=0;i<lit.length;i++){
	   lit[i].className=i==n?"current":"spread";
	   lim[i].style.display=i==n?"block":"none";
	
 }
}

/***调度表单显示*****/
$(document).ready(function() {
    $("#dispatch").css("border","none");
	$(".dispatch_content").css("display","none");
	$("#dispatch_btn").click(function(){
		$("#dispatch").css("border","2px solid #ff6000");
		$(".dispatch_content").css("display","block");
		$(document).scrollTop(300);
		$("#isAdjust").val("是");
		var meetingType = $("#meetingType").val();
		getConferenceStard(meetingType);
		calculateMoney();
	});
    $(".dispatch_close").click(function(){
		  $("#dispatch").css("border","none");
	      $(".dispatch_content").css("display","none");
	      $("#isAdjust").val("");
	      $("#meetingFee2").val($("#meetingFee").val());
	      var money = parseFloat($("#meetingHours").val() * $("#meetingFee2").val());
		  $("#meetingPayment").val(money);
		  $("#meetingPaymentSpan").html(money);
		  calculateTotalPay();
		});
    var s_l_length=$(".s_l_module").length;
    $(".s_l_module").hover(function(){ 
	   for(var i=0;i<s_l_length;i++)
	    {
		 $(".s_l_module").css("border","1px solid #dbdbdb");
		 $(".nav").css("color","#333");
	     $(this).css("border","1px solid #fe8336");
		 $(this).children(".nav").css("color","#ff6000");
		}
    });	
});



 
 
