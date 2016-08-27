function tableColor()
{
	$("table").find("tr:even").not($("table tr").eq(0)).addClass("table_trColor");
	$("table tr").not($("table tr").eq(0)).hover(
	    function(){
		  $(this).addClass("tr_hover");
		  },
	  function(){
		  $(this).removeClass("tr_hover").find("tr:even").addClass("table_trColor");
		  } 
	);
	$("table tr").not($("table tr").eq(0)).click(
	
		function()
		{
			$(this).addClass("tr_click").siblings().removeClass("tr_click");
		}
	
	)
	$("table tr").eq(0).find("td").hover(
	function(){
		$(this).addClass("tr_hover");
	})	
}
