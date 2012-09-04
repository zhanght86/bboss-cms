<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.cms.sitemanager.*"%>

<%  //站点删除
    String path=request.getRealPath("/cms/Pub");
	
	
		AccessControl control = AccessControl.getInstance();
		control.checkAccess(request, response);
		
    ChannelManagerImpl smi=new ChannelManagerImpl();
	//boolean ret=smi.deleteChannel(Integer.parseInt(request.getParameter("channelId")),path+"/");
	boolean ret=smi.logDeleteChannel(request.getParameter("channelId"),request, response);
%>
 <%if(ret==true){%>
	<script language="javascript">
		alert("恭喜,频道删除成功!");	
		parent.window.returnValue = true;
		parent.close(); 
	</script>
  <%}%>
  
  <%if(ret==false){%>
	<script language="javascript">
	   alert("抱歉,频道删除失败!");
	      
	 </script>
  <%}%> 