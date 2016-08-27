
<%@page import="java.util.*,com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%          
	AccessControl accesscontroler1 = AccessControl.getInstance();
	accesscontroler1.checkManagerAccess(request,response);
	String userId1 = accesscontroler1.getUserID();
%>

		<script language="JavaScript">
		var http_request = false;
		function send_request(url){
                
			//http_request = false;
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
				alert("????XMLHttpRequest??");
				return false;
			}
			http_request.onreadystatechange = processRequest;
			http_request.open("GET",url,true);
			http_request.send(null);
		}
			
		function processRequest(){
			
			if(http_request.readyState == 4){
				if(http_request.status == 200){
					var result = http_request.responseText
					if(result == "")
					{
						objTimer = window.setInterval("showmessage()",8000); 
					}
					else
					{
						content.innerHTML = result; 
					    window.setTimeout("getMsg()",1000);		   		    
			   		}
				}
				else{
					alert("?????????");
				}
			}
		}
		window.onload = showmessage; 
        window.onresize = resizeDiv; 
        window.onerror = function(){} 
        //??????(asilas??) 
        var divTop,divLeft,divWidth,divHeight,docHeight,docWidth,objTimer,i = 1; 
        
        function getMsg() 
        { 
	        try{ 
	            divTop = parseInt(document.getElementById("eMeng").style.top,10) 
	            divLeft = parseInt(document.getElementById("eMeng").style.left,10) 
	            divHeight = parseInt(document.getElementById("eMeng").offsetHeight,10) 
	            divWidth = parseInt(document.getElementById("eMeng").offsetWidth,10) 
	            docWidth = document.body.clientWidth; 
	            docHeight = document.body.clientHeight; 
	            document.getElementById("eMeng").style.top = parseInt(document.body.scrollTop,10) + docHeight + 10;// divHeight 
	            document.getElementById("eMeng").style.left =parseInt(document.body.scrollLeft,10) + docWidth - divWidth 
	            document.getElementById("eMeng").style.visibility="visible" 
	            if(objTimer)
	            	clearwindowInterval();
	            objTimer = window.setInterval("moveDiv()",10) 
	          
	       } 
	       catch(e){} 
       } 
       
       function clearwindowInterval() 
       {
       		window.clearInterval(objTimer) ;
       }

       function showmessage()
       {        		
       		//????????????,???????flag=true
       		
   			send_request('frame_daySchedularList.jsp?userId=<%=userId1%>');      		
   			
       		
       }
       
       function resizeDiv() 
       {   
  	        i+=1 

        	try{ 
	            divHeight = parseInt(document.getElementById("eMeng").offsetHeight,10) 
	            divWidth = parseInt(document.getElementById("eMeng").offsetWidth,10) 
	            docWidth = document.body.clientWidth; 
	            docHeight = document.body.clientHeight; 
	            document.getElementById("eMeng").style.top = docHeight - divHeight + parseInt(document.body.scrollTop,10) 
	            document.getElementById("eMeng").style.left = docWidth - divWidth + parseInt(document.body.scrollLeft,10) 
            } 
           catch(e){} 
       } 

      function moveDiv() 
      { 
       try 
          { 
           if(parseInt(document.getElementById("eMeng").style.top,10) <= (docHeight - divHeight + parseInt(document.body.scrollTop,10))) 
             { 
	              window.clearInterval(objTimer) 
	              objTimer = window.setInterval("resizeDiv()",1) 
	              //window.setTimeout("closeDiv()",10000) ;
             } 
           divTop = parseInt(document.getElementById("eMeng").style.top,10) 
           document.getElementById("eMeng").style.top = divTop - 1 
          } 
          catch(e){} 
      } 
      
      function closeDiv() 
      { 
	    document.getElementById('eMeng').style.visibility='hidden'; 
	    if(objTimer) 
	    window.clearInterval(objTimer) 
	    objTimer = window.setInterval("showmessage()",10000) ;
      } 
</script>
	
		<div id=eMeng
			style="BORDER-RIGHT: #455690 1px solid; BORDER-TOP: #a6b4cf 1px solid; Z-INDEX:0; LEFT: 0px; VISIBILITY: hidden; 
			BORDER-LEFT: #a6b4cf 1px solid; WIDTH: 180px; BORDER-BOTTOM: #455690 1px solid; POSITION: absolute; TOP: 0px; HEIGHT: 116px; BACKGROUND-COLOR: #c9d3f3">
			<table style="BORDER-TOP: #ffffff 1px solid; BORDER-LEFT: #ffffff 1px solid" cellspacing=0 cellpadding=0 width="100%" bgcolor=#E7EFF7 border=0>
				<tbody>
					<tr>
						<td style="FONT-SIZE: 12px; BACKGROUND-IMAGE: url(msgTopBg.gif); COLOR: #0f2c8c" width=30 height=24></td>
						<td width="100%" align=center style="FONT-WEIGHT: normal; FONT-SIZE: 14px;BACKGROUND-IMAGE: url(msgTopBg.gif); COLOR: #0855AD; PADDING-TOP:4px;PADDING-left: 4px">
							<div align="center">
								&nbsp;&nbsp;&nbsp;&nbsp;
							</div>
						</td>
						<td style="BACKGROUND-IMAGE: url(msgTopBg.gif); PADDING-TOP:2px;PADDING-right:2px" align=center align=right width=19>
							<span title=?? style="CURSOR:hand;color:#FF8A00;font-size:12px;font-weight:bold;margin-right:4px;" onClick=closeDiv()>×</span>
							<!-- <IMG title=?? style="CURSOR: hand" onclick=closeDiv() hspace=3
src="msgClose.jpg"> -->
						</td>
					</tr>
					<tr>
						<td style="PADDING-RIGHT: 1px; BACKGROUND-IMAGE:
url(/oblog/1msgBottomBg.jpg); PADDING-BOTTOM: 1px" colspan=3 height=90 align="center" >
					<div  id="content" align=left style="BORDER-RIGHT: #9C9E9C 1px solid; PADDING-RIGHT: 13px;
BORDER-TOP: #9C9E9C 1px solid ; PADDING-LEFT: 13px; FONT-SIZE: 12px;
PADDING-BOTTOM: 13px; BORDER-LEFT: #9C9E9C 1px solid; WIDTH: 95%; COLOR:
#1f336b; PADDING-TOP: 18px; BORDER-BOTTOM: #9C9E9C 1px solid; HEIGHT:
100%;background-color:white">
						</div>		
						</td>
					</tr>
					<tr>
						<td height=3 colspan=3></td>
					</tr>
				</tbody>
			</table>
		</div>
	
</html>




