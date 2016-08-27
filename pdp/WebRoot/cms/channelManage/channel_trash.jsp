<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<% ChannelManagerImpl smi=new ChannelManagerImpl();
	boolean ret=smi.trashChannel(Integer.parseInt(request.getParameter("channelId")));
%>
 <%if(ret==true){%>
	<script language="javascript">
	   alert("恭喜,频道回收成功!");	
	   window.open("navigator_content.jsp","perspective_toolbar"); 
	  
	 </script>
  <%}%>
  
  <%if(ret==false){%>
	<script language="javascript">
	   alert("抱歉,频道回收失败!");
	      
	 </script>
  <%}%> 