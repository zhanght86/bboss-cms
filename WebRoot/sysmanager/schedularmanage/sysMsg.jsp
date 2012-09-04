<%@page import="java.util.*,com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%          
	AccessControl accesscontroler1 = AccessControl.getInstance();
	accesscontroler1.checkAccess(request, response);
	String userId1 = accesscontroler1.getUserID();

%>

		<script language="JavaScript">
		var remind_http_request = false;
		function remind_processRequest(url){
                
			//http_request = false;
			//?????XMLHttpRequest??
			if(window.XMLHttpRequest){//Mozilla
				remind_http_request = new XMLHttpRequest();
				if(remind_http_request.overrideMimeType){//??MIME??
					remind_http_request.overrideMimeType("text/xml");						
				}
			}
			else if(window.ActiveXObject){//IE
				try{
					remind_http_request = new ActiveXObject("Msxml2.XMLHTTP");
				}catch(e){
					try{
						remind_http_request = new ActiveXObject("Microsoft.XMLHTTP");							
					}catch(e){
					}
				}
			}
			if(!remind_http_request){
				alert("????XMLHttpRequest??");
				return false;
			}
			remind_http_request.onreadystatechange = remind_processRequest;
			remind_http_request.open("GET",url,true);
			remind_http_request.send(null);
		}
			
		function remind_processRequest(){
			
			if(remind_http_request.readyState == 4){
				if(remind_http_request.status == 200){
					var result = remind_http_request.responseText
					if(result == "")
					{
						objTimer = window.setInterval("remind_showmessage()",8000); 
					}
					else
					{
						content.innerHTML = result; 
					    window.setTimeout("remind_getMsg()",1000);		   		    
			   		}
				}
				else{
					alert("?????????");
				}
			}
		}
		window.onload = remind_showmessage; 
        window.onresize = remind_resizeDiv; 
        window.onerror = function(){} 
        //??????(asilas??) 
        var remind_divTop,remind_divLeft,remind_divWidth,remind_divHeight,remind_docHeight,remind_docWidth,remind_objTimer,remind_i = 1; 
        
        function remind_getMsg() 
        { 
	        try{ 
	            remind_divTop = parseInt(document.getElementById("eMeng").style.top,10) 
	            remind_divLeft = parseInt(document.getElementById("eMeng").style.left,10) 
	            remind_divHeight = parseInt(document.getElementById("eMeng").offsetHeight,10) 
	            remind_divWidth = parseInt(document.getElementById("eMeng").offsetWidth,10) 
	            remind_docWidth = document.body.clientWidth; 
	            remind_docHeight = document.body.clientHeight; 
	            document.getElementById("eMeng").style.top = parseInt(document.body.scrollTop,10) + remind_docHeight + 10;// remind_divHeight 
	            document.getElementById("eMeng").style.left =parseInt(document.body.scrollLeft,10) + remind_docWidth - remind_divWidth 
	            document.getElementById("eMeng").style.visibility="visible" 
	            if(remind_objTimer)
	            	remind_clearwindowInterval();
	            remind_objTimer = window.setInterval("remind_moveDiv()",10) 
	          
	       } 
	       catch(e){} 
       } 
       
       function remind_clearwindowInterval() 
       {
       		window.clearInterval(remind_objTimer) ;
       }

       function remind_showmessage()
       {        		
       		//????????????,???????flag=true
       		
   			remind_send_request('<%=request.getContextPath()%>/sysmanager/frame_daySchedularList.jsp?userId=<%=userId1%>');      		
   			
       		
       }
       
       function remind_resizeDiv() 
       {   
  	        remind_i+=1

        	try{
	            remind_divHeight = parseInt(document.getElementById("eMeng").offsetHeight,10) 
	            remind_divWidth = parseInt(document.getElementById("eMeng").offsetWidth,10) 
	            remind_docWidth = document.body.clientWidth; 
	            remind_docHeight = document.body.clientHeight; 
	            document.getElementById("eMeng").style.top = remind_docHeight - remind_divHeight + parseInt(document.body.scrollTop,10) 
	            document.getElementById("eMeng").style.left = remind_docWidth - remind_divWidth + parseInt(document.body.scrollLeft,10) 
            }
           catch(e){} 
       } 

      function remind_moveDiv() 
      { 
       try 
          { 
           if(parseInt(document.getElementById("eMeng").style.top,10) <= (remind_docHeight - remind_divHeight + parseInt(document.body.scrollTop,10))) 
             { 
	              window.clearInterval(remind_objTimer) 
	              remind_objTimer = window.setInterval("remind_resizeDiv()",1) 
	              //window.setTimeout("remind_closeDiv()",10000) ;
             } 
           remind_divTop = parseInt(document.getElementById("eMeng").style.top,10) 
           document.getElementById("eMeng").style.top = remind_divTop - 1 
          } 
          catch(e){} 
      } 
      
      function remind_closeDiv() 
      { 
	    document.getElementById('eMeng').style.visibility='hidden'; 
	    if(remind_objTimer) 
	    window.clearInterval(remind_objTimer) 
	    remind_objTimer = window.setInterval("showmessage()",10000) ;
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
							<span title=?? style="CURSOR:hand;color:#FF8A00;font-size:12px;font-weight:bold;margin-right:4px;" onClick=remind_closeDiv()>×</span>
							<!-- <IMG title=?? style="CURSOR: hand" onclick=remind_closeDiv() hspace=3
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




