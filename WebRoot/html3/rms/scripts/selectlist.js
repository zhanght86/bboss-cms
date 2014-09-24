/*function selectList(n)
{
	var inp=getid("input"+n);
	var con=getid("content"+n);
	resize(inp,con);
	if(con.style.display=="none")
	{
	   for(var i=1;i<3;i++)
	   {
		   getid("content"+i).style.display="none"
	   }
	   con.style.display="block";
	}
	else
	{
	   con.style.display="none";
	}	
}*/
function selectList(n)
{
	$("#content"+n).width($("#input"+n).parent().width()-2)
	
	$("#content"+n).toggle();
}


function bClick()
{
	for(var i=1;i<3;i++)
	   {
		  $("body").not($("content"+i)).click(function(){
			  if($("content"+i).css("display")=="block"){$("content"+i).css("display","none")}
		  })
	   }
	
}


function check(n)
{
	var nodes=getid("content"+n).getElementsByTagName("input")
	var v="";
	
	for(var i=0;i<nodes.length;i++)
	{   
		if(nodes[i].checked)
		{
			 v+=nodes[i].value+","	
		}		
	}
	getid("input"+n).value=v;	 	
}

function read(n)
{
	var list=getid("content"+n).getElementsByTagName("input");
	var c="";
	for(var i=0;i<list.length;i++)
	{
		
		
		if(list[i].value!="台数" && list[i].value!="" && list[i].value!=0)
		{
			
			
			c+=list[i].parentNode.parentNode.innerText+list[i].value+'台,'
			//alert()
			//c=list[i].parentNode.preSibling.innerHTML+list[i].value+","
		}
	}
	getid("input"+n).value=c;	
	
}

function getid(ele)
{
	return document.getElementById(ele);
}
function resize(inp,con){
    con.style.width=(inp.offsetWidth-2)+'px';
}
function outover(n)
{
	
	
}



