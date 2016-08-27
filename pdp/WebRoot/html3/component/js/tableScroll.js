(function($){
	$.extend({
		sanyTable:function(options){
			var defaults={
				tableDiv:"#tableDiv",
				tableLeftDiv:"#tableLeftDiv",
				tableRightDiv:"#tableRightDiv",
				fixWidth:0,
				fixRow:0
			};
			var opts=$.extend({},defaults,options);
			
			var $tableDiv=$(opts.tableDiv);
			var $tableLeftDiv=$(opts.tableLeftDiv);
			var $tableRightDiv=$(opts.tableRightDiv);
			var $fixWidth=opts.fixWidth;
			var $fixRow=opts.fixRow;
			
			/*****初始化设置表格固定区域宽度******/
			$tableLeftDiv.width($fixWidth+"px");
			$tableRightDiv.css("marginLeft",$fixWidth+"px");
			
			setTableThClone($tableLeftDiv,$fixRow,$tableRightDiv);
			setTableThClone($tableRightDiv,$fixRow,$tableRightDiv);

			var $tableDivTop=$tableDiv.offset().top;
			$tableRightDiv.jScrollPane({
			   //showArrows: true,  //显示自动箭头
				mouseWheelSpeed:40, //鼠标速度(高度)
				scrollPagePercent:0.8 //page按钮翻页速度				  
			 });
			 
			 $(".tableClone").css({"top": $tableDiv.offset().top})
		     $(".jspHorizontalBar").css({"top":$tableDiv.height()+$tableDiv.offset().top});	
		}		
  });
  
  function setTableThClone(tableDiv,num,other)
  {
	 
	  var tableDiv=tableDiv;
	  var num=num;
	  var other=other;
	  var table=tableDiv.find("table");
	  var otherTable=other.find("table");
	  
	  var tableClone=$('<table id="'+table+'Clone" border="0" cellspacing="0" cellpadding="0" class="tableClone"></table>');
	  
	  for(var i=0;i<num;i++)
	  {
		  var tableThClone=table.find("tr").eq(i).clone();
		
		  tableThClone.appendTo(tableClone);	
	  }
	  tableClone.appendTo(tableDiv);	
	  tableClone.addClass("table_basic table_bline");

	  if ($.browser.msie&&$.browser.version=="7.0")
	  {
		  tableClone.css({
				   'position':'absolute',	
				   'top':table.offset().top+'px',
				   'background-color':'#fff',
				   'word-break':'keep-all'		 
				 })      
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
	  var tableTh=table.find("tr").eq(2);
	  for(var i=0;i<tableTh.find("td").length;i++)
	   {
		  tableClone.find("th").eq(i).width(table.find("td").eq(i).width());		
	   } 
	   
	  if(tableThClone.find(":checkbox"))
	   {
		   tableThClone.find(":checkbox").click(function(){ 
			   if(tableThClone.find(":checkbox").attr("checked"))
			   {
				   table.find(":checkbox").each(function(){
					   $(this).attr("checked","true"); 
					   $(this).closest("tr").addClass("tr_click"); 
				   });
				  
				   otherTable.find("tr").addClass("tr_click");   
			   }
			   else
			   {
				   table.find(":checkbox").each(function(){
					   $(this).removeAttr("checked");
					   $(this).closest("tr").removeClass("tr_click");
				    });
					otherTable.find("tr").removeClass("tr_click");   
			   }
		   });
	   }  
  }	
})(jQuery);
 // JavaScript Document
function scrollTableColor(tableA,tableB)
	{
		
		tableA=$("#"+tableA+" tr");
		tableB=$("#"+tableB+" tr");
		
		tableA.not($(this).eq(0)).hover(
		function()
		{
			$(this).addClass("tr_hover");
			relexTable();
		},
		function()
		{
			$(this).removeClass("tr_hover").find("tr:even").addClass("table_trColor");
			relexTable();
		});
		
		tableA.find(":checkbox").each(function() {
            $(this).bind('click',function(e)
			{
				e.stopPropagation();
				checkBoxClick(this);
			    relexTable(); 
			});	
        });
		
		
		tableA.not("tr:first").click( function(e)
		{
			e.stopPropagation();
			if($(this).find(":checkbox") || tableB.eq($(this).index()).find(":checkbox"))
			{
				if($(this).find(":checkbox").attr("checked")|| tableB.eq($(this).index()).find(":checkbox").attr("checked"))
				{
					$(this).removeClass("tr_click");
				    tableB.eq($(this).index()).removeClass("tr_click");
				    $(this).find(":checkbox").removeAttr("checked");
			        tableB.eq($(this).index()).find(":checkbox").removeAttr("checked");	
					
					$(".tableClone").find(":checkbox").removeAttr("checked");
				}
				else
				{
					$(this).addClass("tr_click");
				    tableB.eq($(this).index()).addClass("tr_click");
				    $(this).find(":checkbox").attr("checked","checked");
			        tableB.eq($(this).index()).find(":checkbox").attr("checked","checked");			
				}	
			}
			else
			{
				   $(this).addClass("tr_click").siblings().removeClass("tr_click");
			       tableB.eq($(this).index()).addClass("tr_click").siblings().removeClass("tr_click");
			}	
		});

	   function relexTable()
	   {
		tableA.each(function(index, element) {
		if($(this).hasClass("tr_hover"))
		{
			tableB.eq($(this).index()).addClass("tr_hover");
		}else{
			tableB.eq($(this).index()).removeClass("tr_hover");
		}
		if($(this).hasClass("tr_click"))
		{
			tableB.eq($(this).index()).addClass("tr_click");
		}else{
			tableB.eq($(this).index()).removeClass("tr_click");
		}	
        }); 
	   };
	    function checkBoxClick(obj)
	    {
	     var oCk = $(obj),
	     parentTr = oCk.closest("tr");
         oCk.prop('checked') ? parentTr.addClass("tr_click") : parentTr.removeClass("tr_click");
	     relexTable(); 
	    };		
	}

   
    
   