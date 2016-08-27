$(function(){
     var len  = $(".num_ad > a").length;
	 var index = 0;
	 var adTimer;
	 $(".num_ad a").mouseover(function(){
		index  =   $(".num_ad a").index(this);
		showImg(index);
	 }).eq(0).mouseover();	
	 //滑入 停止动画，滑出开始动画.
	 $('.adimg').hover(function(){
			 clearInterval(adTimer);
		 },function(){
			 adTimer = setInterval(function(){
			    showImg(index)
				index++;
				if(index==len){index=0;}
			  } , 3000);
		 }).trigger("mouseleave");
	})
	// 通过控制top ，来显示不同的幻灯片
	function showImg(index){
			var adHeight = $(".img_box .adimg").height();			
			$(".upmove").stop(true,false).animate({top : -adHeight*index},1000);			
			$(".num_ad a").removeClass("select")
				.eq(index).addClass("select");
	}