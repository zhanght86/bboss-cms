
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

function add2Row(objIdStr1,objIdStr2){
	addRow(objIdStr1);
	addRow(objIdStr2);
	
}


function deleteCurrentRow(obj,tableId)
{	
	if(obj)
	{		
		$(obj).parent().parent().remove();
		calculateRow(tableId);		
	}	
}
function deleteSubCurrentRow(trNo,tableId)
{	
	if(trNo)
	{		
		$("#"+tableId).find("tr").eq(trNo).remove();
		calculateRow(tableId);		
	}	
}

function delete2CurrentRow(obj,tableId1,tableId2)
{
	var trNo=$(obj).parent().parent().parent().find("tr").index($(obj).parent().parent());
	
	deleteSubCurrentRow(trNo,tableId1);
	deleteSubCurrentRow(trNo,tableId2)
}