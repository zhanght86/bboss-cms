
function calculateRow(objIdStr)
{
	var tbody = $("#"+objIdStr+" tbody tr");
	$.each(tbody,function(i,tr){
		if(i>=2)
		{
			$(tr).find("SPAN:first-child").text(i-1);
		}

	})


}


function addRow(objIdStr)
{

	//最后一个tr
	var lastTR = $("#"+objIdStr+" tr:last-child");
	var selectedObj = $("#"+objIdStr+" tr:nth-child(2)").clone();
	var trArr = document.getElementById(objIdStr).getElementsByTagName("TR");	
	var trInnerHTML ;
	for(var i=0;i<trArr.length;i++)
	{
		if(trArr[i].style.display.toLowerCase()=="none")
		{
			trInnerHTML = trArr[i].innerHTML;
			break;
		}
	}
	if(trInnerHTML=="" || trInnerHTML.length==0)
	{
		return;
	}

	lastTR.after("<tr>"+trInnerHTML+"</tr>");
	calculateRow(objIdStr);
}



function deleteRow(objIdStr)
{
	var selObj = getSelectObj(objIdStr);
	if(selObj)
	{
		selObj.parent().parent().remove();
		calculateRow(objIdStr);
	}

	
}

function deleteCurrentRow(obj,tableId)
{	
	if(obj)
	{		
		$(obj).parent().parent().remove();
		calculateRow(tableId);		
	}	
}