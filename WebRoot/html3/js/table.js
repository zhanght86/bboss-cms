;(function($) {       
   $.fn.tableScroll = function(){  
	  
	     
       var opts=$.extends( $.fn.tableScroll.defaults,options); 
       
       return this.each(function(){
           
    	      var $this=$(this);
    	      
    	      $.fn.tableScroll.ThClone($(this).table,$(this).num);
    	   
       })
       
       
       
       
}; 
$.fn.tableScroll.defaults=
   {
		   table:"table",
           num:num
   };

$.fn.tableScroll.ThClone=function(table,num)
{
	var table=$("#"+table);
	var num=num;
	
	
	var tableClone=$('<table id="'+table+'Clone" border="0" cellspacing="0" cellpadding="0" class="tableClone"></table>');
	
	for(var i=0;i<num;i++)
	{
		var tableThClone=table.find("table").find("tr").eq(i).clone();
		tableThClone.appendTo(tableClone);	
	}
	tableClone.appendTo(table);	
	tableClone.addClass("table1");
	
	table.height($(window).height-500);
	
	if ($.browser.msie&&$.browser.version=="7.0")
	{
        tableClone.css({
		         'position':'absolute',	
				  'top':table.offset().top+'px',
				  'left':0+'px',
				 'background-color':'#fff',
				 'word-break':'keep-all'		 
		});  
	     
    }	
	else
	{
		 tableClone.css({
		         'position':'fixed',
				  'top':table.offset().top+'px',
				 'background-color':'#fff',
				 'word-break':'keep-all'			 
		});
		
	}			
	tableThClone.css({'word-break':'keep-all'});
	var tableTh=table.find("table").find("tr").eq(2);
	var tableTd=table.find("table");
	
    table.scroll(function()
    {
            tableClone.scrollLeft(table.scrollLeft());
          
    });
    
    tableClone.width(tableTd.width());
    for(var i=0;i<tableTh.find("td").length;i++)
     {
		tableClone.find("th").eq(i).width(tableTh.find("td").eq(i).width());		
     }
    
}

})(jQuery);
