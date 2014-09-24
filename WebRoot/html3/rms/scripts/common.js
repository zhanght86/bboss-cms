function Focus(obj)
{
	obj.style.border="1px solid #777";
    obj.style.color="#333";
	if(obj.value!="" && obj.value==obj.defaultValue )
	  {
		  obj.value="";	  
	  }	
	 else if(obj.value!="" && obj.value!=obj.defaultValue )
	 { 
	     obj.value!=""; 
	 }
}

function Blur(obj)
{
	if(obj.value=="")
	{
		obj.value=obj.defaultValue;//defaultValue获取本来的value值
		obj.style.border="1px solid #c2c2c2"
		obj.style.color="#ccc"
	}
	else
	{
		obj.style.border="1px solid #c2c2c2"
		obj.style.color="#000"
	}
}

function setCheckTd()
	{
		$checkbox=$("input:checkbox");
	    $checkbox.attr("checked",false);
		$checkbox.bind("click", function () 
		{
           $(this).closest("tr").removeClass("tr_checkover")
		   if($(this).attr("checked"))
		   {
			 $(this).closest("tr").addClass("tr_checkover"); 
			 $(this).closest("tr").children("td").last().replaceWith("<td><input type='text' class='w30' value='1'/></td>")
		   }
        });
		
	}
function setTableRowColor(table)
{
	if(table==null&&table=='')
	{
	  $("table tr").hover(function()
		{   
			var mintab=$(this).find("table")
			if(mintab.length<=0)
			{
			$(this).addClass("tr_over");
			}
			else
			{
				setTableRowColor(mintab)
			}
		},
		function()
		{
			$(this).removeClass("tr_over");
		})
	}
	else
	{
		$(table).find("tr").hover(
		 function()
		{   
			var mintab=$(this).find("table")
			if(mintab.length<=0)
			{
			$(this).addClass("tr_over");
			}
			else
			{
				setTableRowColor(mintab)
			}
		},
		function()
		{
			$(this).removeClass("tr_over");
		})
	}
}
function topNav()
{
	 $(".right_nav li").hover(function(){
	   $("a",this).not(".second a").addClass("current");
	   $("a",this).children(".shrink").addClass("spread") 
	   $(".detail",this).show();
	 },
	 function(){
	   $("a",this).not(".second a").removeClass("current");
	   $("a",this).children(".shrink").removeClass("spread") 
	   $(".detail",this).hide();
	 })
	
	$(".entry a").hover(
	  function(){
		$(this).children("em").addClass("hover");
	   },
	   function(){
		$(this).children("em").removeClass("hover");
	   }
	   
	)
}



