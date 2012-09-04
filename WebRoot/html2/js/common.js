function loadFrame(frameId){
	var mainheight = $("#"+frameId).contents().find	("body").height()+20;
	$("#"+frameId).height(mainheight);		
}

function loadMain(frameId){
	$("#"+frameId).load(function(){
		loadFrame(frameId);
	}); 
}
function loadContent(frameId){
	$("#"+frameId).load(function(){
		loadFrame(frameId);
		window.parent.loadFrame("indexFrame");
	}); 
}

function suitHeight(){
	if(parent.loadFrame)
		parent.loadFrame("rightFrame");
	if(window.top.loadFrame)
	window.top.loadFrame("indexFrame");
}


$(function(){	
	$(".leftmenu h4").each(function(index, element) {
		$(this).click(function(){
			$(".leftmenu ul").eq(index).slideToggle("normal",function(){suitHeight();})
		})        
	});
	$(".leftmenu li a").each(function(index, element) {
		$(this).click(function(){
			$(".leftmenu li a").removeClass("current");
			$(this).addClass("current");
		})
	});		
})
