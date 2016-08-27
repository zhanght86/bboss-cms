// JavaScript Document
;(function($){
	
	$.fn.extend({
		pop_Content:function(options){
			var defaults={
			}
			$
			
			
			
		}
		
		
		
		
		
		
		})
	
	
	
	
})(jQuery)
$.fn.popContext=function(p){
	var defaults={
		width:0,
		height:0,
		posX:0,
		posY:0,
		
		target:0,
		context:'',
	};
	p=$.extend({},$.fn.popContext.defaults,p);
	var $this=$(this);
	$this.width($(p.context).parent().width()-2);
	$this.toggle();
};
$(".input1").popContext({context:pop_content})
