<%@page import="java.util.*,com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%//AccessControl accesscontroler1 = AccessControl.getInstance();
            //accesscontroler.checkAccess(request, response);
            String userId1 = accesscontroler.getUserID();

            %>

<script language="JavaScript">
		var remind_divTop,remind_divLeft,remind_divWidth,remind_divHeight,remind_docHeight,remind_docWidth,remind_objTimer,remind_i = 1; 
		var remind_http_request = false;
		window.onload = remind_showmessage; 
        window.onresize = remind_resizeDiv; 
        window.onerror = function(){} 
		function remind_send_request(url){ 
                
			remind_http_request = false;
			
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
				
				return false;
			}
			remind_http_request.onreadystatechange = remind_processRequest;
			//alert(url);
			remind_http_request.open("GET",url,true);
			remind_http_request.send(null);
		}
			
		function remind_processRequest(){
			
			if(remind_http_request.readyState == 4){
				if(remind_http_request.status == 200){
					var result = remind_http_request.responseText
					//alert("result:" + result + "&&&");
					if(result == "")
					{
						if(remind_objTimer)
							remind_clearwindowInterval();
						remind_objTimer = window.setInterval("remind_showmessage()",10000); 
					}
					else
					{
						content.innerHTML = result; 
					    window.setTimeout("remind_getMsg()",5000);		   		    
			   		} 
				}
				else{
					//alert("?????????");
				}
			}
		}
	
        //??????(asilas??) 
        
        
        function remind_getMsg() 
        { 
	        try{ 
	            remind_divTop = parseInt(document.getElementById("eMeng").style.top,10) 
	            remind_divLeft = parseInt(document.getElementById("eMeng").style.left,10) 
	            remind_divHeight = parseInt(document.getElementById("eMeng").offsetHeight,10) 
	            remind_divWidth = parseInt(document.getElementById("eMeng").offsetWidth,10) 
	            remind_docWidth = document.body.clientWidth; 
	            remind_docHeight = document.body.clientHeight; 
	            document.getElementById("eMeng").style.top = 0;
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
       		
       		remind_http_request = false;
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
	            document.getElementById("eMeng").style.top = 0;
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
             	if(remind_objTimer)
	              window.clearInterval(remind_objTimer) ;
	            remind_objTimer = null;
	            remind_resizeDiv();
	            //window.setTimeout("remind_resizeDiv()",1);
	            window.setTimeout("remind_closeDiv()",8000) ;//?????????
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
	    remind_objTimer = window.setInterval("remind_showmessage()",8000) ;
      } 
</script>

<div id=eMeng
	style="BORDER-RIGHT: #455690 1px solid; BORDER-TOP: #a6b4cf 1px solid; Z-INDEX:0; LEFT: 0px; VISIBILITY: hidden; 
			BORDER-LEFT: #a6b4cf 1px solid; WIDTH: 180px; BORDER-BOTTOM: #455690 1px solid; POSITION: absolute; TOP: 0px; HEIGHT: 46px; BACKGROUND-COLOR: #c9d3f3">
	<table style="BORDER-TOP: #ffffff 1px solid; BORDER-LEFT: #ffffff 1px solid" cellspacing=0 cellpadding=0 width="100%" bgcolor=#E7EFF7 border=0>
		<tbody>
			<tr>
				<td style="FONT-SIZE: 12px; BACKGROUND-IMAGE: url(msgTopBg.gif); COLOR: #0f2c8c" width=30 height=24></td>
				<td width="100%" align=center style="FONT-WEIGHT: normal; FONT-SIZE: 14px;BACKGROUND-IMAGE: url(msgTopBg.gif); COLOR: #0855AD; PADDING-TOP:4px;PADDING-left: 4px">
					<div id="content" align="center" >
						
					</div>
				</td>
				<td style="BACKGROUND-IMAGE: url(msgTopBg.gif); PADDING-TOP:2px;PADDING-right:2px" align=center align=right width=19>
					<span title=close style="CURSOR:hand;color:#FF8A00;font-size:12px;font-weight:bold;margin-right:4px;" onClick=remind_closeDiv()>×</span>
					
				</td>
		</tbody>
	</table>
</div>




