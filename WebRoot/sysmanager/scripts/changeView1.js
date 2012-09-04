function changeView(selectName){
	try
	{
  		var len =document.all(selectName).options.length;
  		var linkurl ;
  		var restype ; 
  		for(var i=0;i<len;i++){
   			var op=document.all(selectName).options[i];
   			if(op.selected)
   			{
   				restype = op.value;
   				linkurl = op.link;
   				break;
   			}
  		}
		var idx = linkurl.indexOf("?");

		
		if(idx == -1)
		{
			document.resource_bridge.location = linkurl + "?resTypeId=" + restype;
		}
		else
		{
			document.resource_bridge.location = linkurl + "&resTypeId=" + restype;
		}
		//alert(document.resource_bridge.location)
		parent.globalOperList.location = "../role/operList_global.jsp?resTypeId=" + restype;
		parent.operList.location = "../role/operdefault.jsp?resTypeId=" + restype;
		
		//add by ge.tao
		//date 2007-10-09
		//if(restype == "orgunit"){
			//parent.operList.location.href = "../accessmanager/role/operList_orgs_ajax.jsp?resTypeId=orgunit&classType=org";
		//}else{
		    //parent.operList.location.href = "user_operdefault.jsp";
		//} 		
  	}
	catch(e)
	{
	}
}