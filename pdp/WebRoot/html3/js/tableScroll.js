// JavaScript Document
function ThClone(table,num)
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

function fixLeftWidth(tableLeft,tableRight,leftWidth)
{
	var leftWidth=leftWidth;
	var leftTable=$("#"+tableLeft);
	var rightTable=$("#"+tableRight)
	leftTable.width(leftWidth);
	rightTable.css("marginLeft",leftWidth);
	
}

function setTableRowColor(table)
{
	var table=$("#"+table);
	table.find("tr:odd").addClass("table_trColor")
}

/*function setTableRowHover(table)
{
	
	
	/*if(table==null&&table=='')
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
	    var table=$("#"+table);
		table.find("tr").hover(
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
	}*/
	
/*}*/

