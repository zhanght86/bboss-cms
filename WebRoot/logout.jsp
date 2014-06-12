<%@page session="false" contentType="text/html;charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl
                ,com.frameworkset.platform.security.authorization.AccessException,com.frameworkset.platform.synchronize.httpclient.*"%>
<%@page import="com.frameworkset.platform.config.ConfigManager"%>
<%
  HttpSession session = request.getSession(false);
  boolean isCasServer = ConfigManager.getInstance().getConfigBooleanValue("isCasServer",false);
  String _redirectPath = request.getParameter("_redirectPath");
  if(isCasServer){
  	String logoutUrl = (String)session.getAttribute("edu.yale.its.tp.cas.client.filter.logout");
  	response.sendRedirect(logoutUrl);
  }
  else{
  	   
	  AccessControl accesscontroler = AccessControl.getInstance();
	  try
	  {
		 
		  //if(success)
		  boolean isWebSealServer = ConfigManager.getInstance()
			.getConfigBooleanValue("isWebSealServer", false);
		  String webseallogout = ConfigManager.getInstance()
			.getConfigValue("webseallogout", "http://uimweb.sany.com.cn:8080/pkmslogout");
		  //正式登出地址：http://uimweb.sany.com.cn:8080/pkmslogout
		  //测试地址：http://10.0.6.191/pkmslogout
			String user_name = request.getHeader("iv-user");
			
		  
		  if(isWebSealServer && user_name!= null && !user_name.equals(""))
	  	  {
	  	  		 boolean success = accesscontroler.checkAccess(request, response,false);
	  	  		accesscontroler.logout(webseallogout);
	  	  }
	  	  else
	  	  {
	  			
	  	  		boolean success = accesscontroler.checkAccess(request, response,false);
	  	  		String logoutpage = null;
	  	  		if(success)
	  	  		{	  	  			
	  	  			logoutpage = accesscontroler.getSubSystemLogoutRedirect();
	  	  		}
	  	  		else
	  	  		{
	  	  			if(_redirectPath != null && !_redirectPath.equals(""))
	  	  			{
	  	  				logoutpage = _redirectPath;
	  	  			}
	  	  			else
	  	  				logoutpage = accesscontroler.getSubSystemLogoutRedirect(request,null,false);
	  	  		}
	  	  		
	  	  		accesscontroler.logout(logoutpage);
	  	  }
  	  	
		  
	  }
	  catch(Exception e)
	  {
	  	
	  }
	 
  }

%>


