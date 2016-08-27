<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage=""%>
<%@page import="java.util.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor"%>
<%@page import="com.frameworkset.platform.cms.driver.publish.impl.APPPublish"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.net.URLDecoder" %>

<%
    AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    String uuid = request.getParameter("uuid");
    response.setHeader("Cache-Control", "no-cache"); 
    response.setHeader("Pragma", "no-cache"); 
    response.setDateHeader("Expires", -1);  
    response.setDateHeader("max-age", 0);
    
    String msgs = "";    
    PublishMonitor monitor = (PublishMonitor)session.getAttribute(uuid);
    //List allMsg = monitor.getAllMessages();
    //List failMsg = monitor.getAllFailedMessages();
    //List successMsg = monitor.getAllSuccessMessages();    
    
    List newestMsg = new ArrayList();
    Object obj = monitor.getNewestMessages();
    if(obj != null){
        newestMsg = (List)obj;        
    }
    int size  = newestMsg.size();
    String subMsgs = "";
    for(int i=0;i<size;i++){
        subMsgs = newestMsg.get(i).toString()+String.valueOf("\\r\\n");
        msgs += (subMsgs);
    } 
    String pageUrl = (String)session.getAttribute("pageUrl"+uuid);
    pageUrl = pageUrl==null?"":pageUrl;
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
     //var timer1 = window.setInterval("refresh()",500);
     var timer2 = window.setInterval("rollDown()",100);
     var msgs = "<%=msgs%>";
     //。，“　‘　、（ ）
     msgs = msgs.replace("\"","\'");
     msgs = msgs.replace("“","\'");
     msgs = msgs.replace("、",", "); 
     msgs = msgs.replace("（"," ("); 
     msgs = msgs.replace("）",") ");     
     var pageUrl = "<%=pageUrl%>";
     function clearTimer(){
         window.clearInterval(timer2);
     }
     function rollDown(){
         parent.document.all("publish_info").doScroll("down"); 
     }
     function refresh(){  
         //parent.document.all("publish_info").doScroll("down");         
         if(msgs.length>0){
             if(parent.document.all("publish_info")){
                 try{
                     parent.document.all("publish_info").innerText += msgs; 
                 }catch(err){
                     err.description();
                 }                
             }
         }
         //window.location.href="publish_info.jsp?uuid=<%=uuid%>";
     }
     refresh();
     function stop(){
         if("<%=pageUrl%>" != "null" && "<%=pageUrl%>".length > 0){
             try{
                 //parent.window.clearInterval(document.all("timer1"));
                 setTimeout("clearTimer()",1500);   
             }catch(err){
                 alert(err.description);
             }   
         }
     }
     stop(); 
     window.onerror=function(){return true} 
     
//-->
</SCRIPT>