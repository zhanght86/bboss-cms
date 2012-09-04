<%@page language="java" contentType="text/html; charset=UTF-8"%><%@ page import="com.frameworkset.platform.mq.MQ"%><%@ page import="com.frameworkset.platform.mq.MQMessage"%><%@page import="java.util.*"%><%@ page import="com.frameworkset.util.StringUtil" %><%
	String userId = request.getParameter("userId");
	//System.out.println("asdf");
	Map map = MQ.getMQMessages(userId);
	if(map == null)
	{
		//out.print("");
		return;
	}
	Set set = map.entrySet();
	StringBuffer ret = new StringBuffer("");
	
   
    boolean flag = false;
	for (Iterator it = set.iterator(); it.hasNext();)
	{ 			
		  Map.Entry entry = (Map.Entry)it.next();
		  MQ.MQSet values = (MQ.MQSet)entry.getValue();
		  String desc = values.getTypeDesc();
		  String ids = values.getMsgids();
		  String type = values.getType();
		  String url = values.getUrl();
		  if(url == null) url = "/sysmanager/remind_list.jsp";
		  int k = values.size();
		  if(!flag)
		  {
		  		
		  		if(!url.equals("null"))
		  		{	
		  			url = StringUtil.getRealPath(request,url);
			  		int idx = url.indexOf('?');
			  		if(idx == -1)
			  		{
			  			url += "?id=" + ids;
			  		}
			  		else
			  		{
			  			url += "&id=" + ids;
			  		}
			  	}
		  		
		  		
		  		ret.append("您有").append(k+"条新的").append(desc).append("消息！").append("@@").append(url).append("@@").append(type);
		  		 flag = true;
		  }
		  else
		  {
		  
		  		if(!url.equals("null"))
		  		{	
		  			url = StringUtil.getRealPath(request,url);
			  		int idx = url.indexOf('?');
			  		if(idx == -1)
			  		{
			  			url += "?id=" + ids;
			  		}
			  		else
			  		{
			  			url += "&id=" + ids;
			  		}
			  	}
		  		ret.append("$$").append("您有").append(k+"条新的").append(desc).append("@@").append(url).append("@@").append(type);
		  }
		 	      
	}
		
		
	
	//System.out.println("ret.toString():"+ret.toString());
	String contents = ret.toString();	
	if(contents.length() > 0)
	{
	%>
	<script language="javascript">
		var popup_iframe = parent.popup_iframe;	
		popup_iframe.addPopupbyIframe('<%=contents%>');
	</script>
	<%}%>
