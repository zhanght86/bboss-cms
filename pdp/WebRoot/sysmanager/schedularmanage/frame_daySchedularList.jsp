<%@ 
    page language="java" contentType="text/html; charset=UTF-8"%><%@ page import="com.frameworkset.platform.mq.MQ"%><%@ page import="com.frameworkset.platform.mq.MQMessage"%><%@page import="java.util.*"%><%
	String userId = request.getParameter("userId");
	MQ mq = new MQ();
	MQMessage mqMessage = null;
	Set set = mq.getMQMessages(userId);
	String str = "";
    if(set != null && set.size() != 0)
    {   
        int k = set.size();   
        MQMessage[] messages = new MQMessage[set.size()];
        set.toArray(messages);
        String schId = "";
		for (int kk = 0; kk <  messages.length; kk ++)
		{ 						   
			  mqMessage = messages[kk];	
			  String content = mqMessage.getContent(); 
			  schId += mqMessage.getSchId()+",";
		      str = content;		    		      
		}
		out.print("<a  style='cursor:hand;' onclick=window.open('schedularRemind.jsp?schId="+schId+"',null,'height=600,width=800,status=yes,toolbar=no,location=no')>您有 "+k+" 条日程提醒消息</a><br>");       
        
	}		
%>
