
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.frameworkset.platform.search.SearchService"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> New Document </TITLE>
<SCRIPT LANGUAGE="JavaScript">
var keyCode = 0;
var cacheResults = null;

function keyup(name){	 
	  //alert(event.keyCode);
	  if(event.keyCode == 40 ||
		event.keyCode == 39 
		|| event.keyCode == 38 
		|| event.keyCode == 37 
		)
	  {	  	
	  	  if(name == "selectName")
	  	  {	  	  	 
	  	  	keyCode = 0;  
	  	  }
	  } 
	  else if(event.keyCode == 13)
	  {
	      if(name == "selectName")
	  	  {	  	  	 
	  	      keyCode = 0;  
	  	  }
	  	  
	  	  
	  	  
	  }
	  else
	  { 
	      var keywords = textfield.value;
	      var result = lookup(keywords);	      
	      if(!result)
		      send_request('search.jsp?keywords='+keywords);
		  else
		  {
		  	 var results = result.split(";");
			//alert("results.length:"+results.length);
			addall(results,"selectName")
			changestatus();
			document.all("selectName").focus();
		    document.all("textfield").focus();
		  }
	  }
}

function lookup(key)
{	
	if(!cacheResults || !cacheResults.length)
		return ;
	for(i = 0; i < cacheResults.length; i ++)
	{
		var cacheResult = cacheResults[i];
		if(cacheResult.key == key)
			return cacheResult.value;
	}
	return ;
}

function CacheResult(key,value)
{
	this.key = key;
	this.value = value;
}


function keydown()
{
	//alert("aaaa");
	//alert(event.keyCode); 
	if(event.keyCode == 40 ||
		event.keyCode == 39 
		|| event.keyCode == 38 
		|| event.keyCode == 37
		 )
	{
		changestatus();
		document.all("selectName").focus();
		
		keyCode = event.keyCode;
		
	}
	else if(event.keyCode == 13 )
	{
		document.all("selectName").style.display="none";
		document.all("textfield").focus();
		keyCode = 0;
	}
}
var http_request = false;
function send_request(url){
                
				//http_request = false;
				//开始初始化XMLHttpRequest对象
				if(window.XMLHttpRequest){//Mozilla
					http_request = new XMLHttpRequest();
					if(http_request.overrideMimeType){//设置MIME类别
						http_request.overrideMimeType("text/xml");						
					}
				}
				else if(window.ActiveXObject){//IE
					try{
						http_request = new ActiveXObject("Msxml2.XMLHTTP");
					}catch(e){
						try{
							http_request = new ActiveXObject("Microsoft.XMLHTTP");							
						}catch(e){
						}
					}
				}
				if(!http_request){
					alert("不能创建XMLHttpRequest对象");
					return false;
				}
				http_request.onreadystatechange = processRequest;
				http_request.open("GET",url,true);
				http_request.send(null);
			}
			
function processRequest(){
	
	if(http_request.readyState == 4){
		if(http_request.status == 200){
			
			var result = http_request.responseText;
			
						//alert(http_request.responseText);
						var temp = result.split("$~$");						
						var results = temp[1].split(";");
						if(!cacheResults)
							cacheResults = new Array();
						cacheResults.push(new CacheResult(temp[0],temp[1]))	;
						//alert("results.length:"+results.length);
						addall(results,"selectName")
						changestatus();
						document.all("selectName").focus();
					    document.all("textfield").focus();
			
		}
		else{
			alert("对不起，服务器错误");
		}
	}
}
			
function addone(selectName,name,value){

   
   var op=new Option(name,value);
   document.all(selectName).add(op);
   
}

function deleteall(selectName){
	for (var m = document.all(selectName).options.length-1; m >= 0; m--)
	{
    	document.all(selectName).options[m] = null
    }
    
}

function addall(results,selectName){
	
	 deleteall(selectName);		  
     for(var i = 0; i < results.length; i++)
     {	     
     
	     addone(selectName,results[i], results[i]);  
	 }
	 document.all(selectName).click();
	 //点击   
}
function changestatus(){
   document.all("selectName").style.display="block";
}
function change(){

  textfield.value=selectName.value;
  //alert(keyCode);
  if(keyCode == 40 ||
		keyCode == 39 
		|| keyCode == 38 
		|| keyCode == 37 )
  {
  	
  }
  else
  {
	  document.all("selectName").style.display="none";
  }
}
  
function checkselectStatus()
{
	var eventName = event.srcElement.name ;
	if(eventName == "selectName" || eventName == "textfield" || eventName == "selecticon")
		return;
	if(document.all("selectName").style.display == "block")
		document.all("selectName").style.display = "none"; 
	
}

</SCRIPT>
</HEAD>
<BODY onclick="checkselectStatus()" name="aaa">
<table>
<tr><td height="10px">
<select name="selectName"  multiple  style="position:absolute; left: 10px; top: 30px; width: 120px;display:none" id="selectName"  onchange="change()" onkeydown="keydown()" onkeyup='keyup("selectName");'>
	<%
	
    String[] resultall = SearchService.search("");
	for(int i=0;i<resultall.length;i++)
	{%>
	
    <option value='<%=resultall[i]%>'><%=resultall[i]%></option>
    
    <%}%>
</select>
<input type="text" style="position:absolute; left: 10px; top: 10px; width: 116px; height: 
20px" name="textfield"  onkeyup="keyup();" onkeydown="keydown()">
<img src="cc.jpg" name="selecticon" onclick="changestatus()" style="position:absolute; left: 110px; top: 10px;">
</td></tr>
<tr><td>asdfasdf</td></tr></table>
</BODY>
</HTML>
