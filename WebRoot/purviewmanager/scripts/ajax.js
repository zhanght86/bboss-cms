
function send_request(url){
	http_request = false;
	//?????XMLHttpRequest??
	if(window.XMLHttpRequest){//Mozilla
			http_request = new XMLHttpRequest();
			if(http_request.overrideMimeType){//??MIME??
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
			//	alert("?????");
				return false;
			}
			http_request.onreadystatechange = processRequest;
			http_request.open("GET",url,true);
			http_request.send(null);
			}
			
function processRequest(){
	if(http_request.readyState == 4){				
		if(http_request.status == 200){
		//	alert(http_request.responseText);
		}
		else{
			alert("???,?????");
		}
	}
}
			
			