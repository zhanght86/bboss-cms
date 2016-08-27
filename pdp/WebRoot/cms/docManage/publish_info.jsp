<%@page import="com.frameworkset.platform.cms.util.CMSUtil"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="java.util.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor"%>
<%@page import="com.frameworkset.platform.cms.driver.publish.impl.APPPublish"%>
<!--文档发布 -->
<%
    AccessControl accesscontroler = AccessControl.getAccessControl();
    String uuid = request.getParameter("uuid");
    response.setHeader("Cache-Control", "no-cache"); 
    response.setHeader("Pragma", "no-cache"); 
    response.setDateHeader("Expires", -1);  
    response.setDateHeader("max-age", 0);
    
    String msgs = "";
    PublishMonitor monitor = CMSUtil.getCMSDriverConfiguration().getPublishEngine().getPublishMonitor(uuid);
    //List allMsg = monitor.getAllMessages();
    //List failMsg = monitor.getAllFailedMessages();
    //List successMsg = monitor.getAllSuccessMessages();    
    if(monitor != null)
    {
	    List newestMsg = monitor.getNewestMessages();
	    int size = newestMsg.size();
	    for(int i=0;i<size;i++){
	        msgs += (newestMsg.get(i).toString()+String.valueOf("\\r\\n"));
	   } 
    }
    //String pageUrl = (String)session.getAttribute("pageUrl"+uuid);
    String pageUrl = "";
    msgs = msgs.replaceAll("\"","\'");
    msgs = msgs.replaceAll("“","\'");
    msgs = msgs.replaceAll("、",", "); 
    msgs = msgs.replaceAll("（"," ("); 
    msgs = msgs.replaceAll("）",") "); 
    msgs = msgs.replaceAll("\"","' ");
%>
<!--文档发布 -->
<SCRIPT LANGUAGE="JavaScript">
<!--
     
     //var timer1 = window.setInterval("refresh()",500);
    
     var msgs = "<%=msgs%>";
     //。，“　‘　、（ ） "
     msgs = msgs.replace("\"","\'");
     msgs = msgs.replace("“","\'");
     msgs = msgs.replace("、",", "); 
     msgs = msgs.replace("（"," ("); 
     msgs = msgs.replace("）",") "); 
     msgs = msgs.replace("\"","' ");  
     function clearTimer(){
         window.clearInterval(timer2);
     }
     function rollDown(){
         parent.document.all("publish_info").doScroll('down'); 
     }
     function refresh(){   
         if(msgs.length>0){
             if(parent.document.all("publish_info")){
                 parent.document.all("publish_info").innerText += msgs; 
             }               
         }
        
     } 
     refresh();
     
     
     
//-->
</SCRIPT>