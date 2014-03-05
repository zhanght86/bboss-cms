
<%@ page session="true" language="java"
	contentType="text/html; charset=utf-8"%>
	<%@page import="com.frameworkset.platform.framework.*"%>
<%@page import="com.liferay.portlet.iframe.action.DESCipher,com.sany.common.menu.tag.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.security.authorization.AccessException,com.frameworkset.platform.config.ConfigManager,com.liferay.portlet.iframe.action.SSOUserMapping"%>
<%@ page import="com.frameworkset.platform.ca.CaProperties"%>
<%@ page import="com.frameworkset.platform.ca.CAManager"%>
<%@ page import="com.frameworkset.platform.ca.CookieProperties"%>
<%@ page import="com.sany.webseal.LoginValidate.*"%>
	
<%@ page import="java.util.*"%>
<%@page import="com.frameworkset.util.StringUtil"%>
<%@page import="java.net.URLDecoder"%>
<%

	String u = "", p = "", ck = "";

	String successRedirect = request.getParameter("successRedirect");
	if(!StringUtil.isEmpty(successRedirect))
	{
		successRedirect = StringUtil.getRealPath(request, successRedirect,true);
	}
    String userName = request.getParameter("userId");
	
	String loginType = request.getParameter("loginType");
	String loginMenu = request.getParameter("loginMenu");
	String contextpath = request.getContextPath();
	String menuid = "newGetDoc";
	if(loginMenu != null)
	{
		
		menuid = loginMenu;
		
	}
	
			
	boolean isWebSealServer = ConfigManager.getInstance()
			.getConfigBooleanValue("isWebSealServer", false);
			

	if(isWebSealServer && userName == null)
	{
		
		
           String subsystem = "sany-mms";
           
           try//uim检测
            {
             CommonInfo info = new CommonInfo(); 
             UimUserInfo userinfo = null;
             String ip = "";
             userinfo = info.validateUIM(request);
             ip = userinfo.getUser_ip();		             
             userName = userinfo.getUser_name();
             AccessControl control = AccessControl.getInstance();
			control.checkAccess(request, response, false);
			String user = control.getUserAccount();		
			 request.setAttribute("fromsso","true");
			
			if (user == null || "".equals(user) || !userName.equals(user)) {
			 
             
				 try
		         {
					 if(!userName.equals(user))
					 	control.resetSession(session);
		             String password = SSOUserMapping.getUserPassword(userName);
		             control = AccessControl.getInstance();
		             control.login(request,
								response, userName, password);
						
					
					 if(StringUtil.isEmpty(successRedirect))
					 {
					 	 Framework framework = Framework.getInstance(control.getCurrentSystemID());
					 	MenuItem menuitem = framework.getMenuByID(menuid);
					 	if(menuitem instanceof Item)
					 	{
							
							Item menu = (Item)menuitem;
							successRedirect = MenuHelper.getRealUrl(contextpath, Framework.getWorkspaceContent(menu,control),MenuHelper.sanymenupath_menuid,menu.getId());
					 	}
					 	else
					 	{
					 	
					 		Module menu = (Module)menuitem;
					 		String framepath = contextpath + "/sanydesktop/singleframe.page?" + MenuHelper.sanymenupath + "=" + menu.getPath();
							successRedirect = framepath;
					 	}
					 	AccessControl.recordIndexPage(request, successRedirect);
					 }
					 else
					 {
					      successRedirect = URLDecoder.decode(successRedirect);
					 }
				 	response.sendRedirect(successRedirect);
					return;
				}
				catch(Exception e)
				{
					e.printStackTrace();
					response.sendRedirect(contextpath + "/webseal/websealloginfail.jsp?userName=" + userName + "&ip=" + ip);
					return;
				}	
	             
	         
	        
	         
			} else {
				control.resetUserAttributes();
				 if(StringUtil.isEmpty(successRedirect))
				 {
					Framework framework = Framework.getInstance(control.getCurrentSystemID());
					MenuItem menuitem = framework.getMenuByID(menuid);
				 	if(menuitem instanceof Item)
				 	{
						
						Item menu = (Item)menuitem;
						successRedirect = MenuHelper.getRealUrl(contextpath, Framework.getWorkspaceContent(menu,control),MenuHelper.sanymenupath_menuid,menu.getId());
				 	}
				 	else
				 	{
				 	
				 		Module menu = (Module)menuitem;
				 		String framepath = contextpath + "/sanydesktop/singleframe.page?" + MenuHelper.sanymenupath + "=" + menu.getPath();
						successRedirect = framepath;
				 	}
				 	AccessControl.recordIndexPage(request, successRedirect);
				 }
				 else
				 {
				 	successRedirect = URLDecoder.decode(successRedirect);
				 }
				response.sendRedirect(successRedirect);
				return;
			}
            
            } 
           catch(Exception e)//检测失败,继续平台登录
         {
         		
         }
	           
			
         
				
	} 
	
	else
	{
		try {
            String subsystem = "sany-mms";
             AccessControl control = AccessControl.getInstance();
			control.checkAccess(request, response, false);
			String user = control.getUserAccount();
			DESCipher des = new DESCipher();
			userName = des.decrypt(userName);
			String worknumber = control.getUserAttribute("userWorknumber");
			boolean issameuser = false;
			 if(loginType.equals("2") )
			 {
				 if(worknumber != null && !worknumber.equals(""))
				 	issameuser = userName.equals(worknumber);
			 }
			 else
			 {
				 if(user != null && !user.equals(""))
				 	issameuser = userName.equals(user);
			 }
				 
				 
				 
			if (user == null || "".equals(user) || !issameuser) {
			 
             	if(!issameuser)
             	{
             		control.resetSession(session);
             	}
			
				
	             try
		         {
	            	 //1-域账号登录  2-工号登录
		             String password = null;
	            	 if(loginType.equals("1"))
	            	 {
	            		 
	            		 password = SSOUserMapping.getUserPassword(userName);
	            	 }
	            	 else
	            	 {
	            		 java.util.Map data = SSOUserMapping.getUserNameAndPasswordByWorknumber(userName);
	            		 userName = (String)data.get("USER_NAME");
	            		 password = (String)data.get("USER_PASSWORD");
	            	 }
	            	 control = AccessControl.getInstance();
	            	 request.setAttribute("fromsso","true");
	            	 control.login(request,
								response, userName, password);
					 if(StringUtil.isEmpty(successRedirect))
					 {	
					 	Framework framework = Framework.getInstance(control.getCurrentSystemID());
					 	MenuItem menuitem = framework.getMenuByID(menuid);
					 	if(menuitem instanceof Item)
					 	{
							
							Item menu = (Item)menuitem;
							successRedirect = MenuHelper.getRealUrl(contextpath, Framework.getWorkspaceContent(menu,control),MenuHelper.sanymenupath_menuid,menu.getId());
					 	}
					 	else
					 	{
					 	
					 		Module menu = (Module)menuitem;
					 		String framepath = contextpath + "/sanydesktop/singleframe.page?" + MenuHelper.sanymenupath + "=" + menu.getPath();
							successRedirect = framepath;
					 	}
					 	AccessControl.recordIndexPage(request, successRedirect);
					 }
					 else
					 {
					 		successRedirect = URLDecoder.decode(successRedirect);
					 }
					response.sendRedirect(successRedirect);
					return;
				}
				catch(Exception e)
				{
					e.printStackTrace();
					response.sendRedirect(contextpath + "/webseal/websealloginfail.jsp?userName=" + userName );
					return;
				}	
	        
	         
			} else {
				control.resetUserAttributes();
				 if(StringUtil.isEmpty(successRedirect))
				 {
					Framework framework = Framework.getInstance(control.getCurrentSystemID());
					MenuItem menuitem = framework.getMenuByID(menuid);
				 	if(menuitem instanceof Item)
				 	{
						
						Item menu = (Item)menuitem;
						successRedirect = MenuHelper.getRealUrl(contextpath, Framework.getWorkspaceContent(menu,control),MenuHelper.sanymenupath_menuid,menu.getId());
				 	}
				 	else
				 	{
				 	
				 		Module menu = (Module)menuitem;
				 		String framepath = contextpath + "/sanydesktop/singleframe.page?" + MenuHelper.sanymenupath + "=" + menu.getPath();
						successRedirect = framepath;
				 	}
				 	AccessControl.recordIndexPage(request, successRedirect);
				 }
				 else
				 {
				 	successRedirect = URLDecoder.decode(successRedirect);
				 }
				response.sendRedirect(successRedirect);
				return;
			}
		
     }
     catch(Throwable ex)
     {    	 
               String errorMessage = ex.getMessage();               
               if(errorMessage == null)
            	   errorMessage = "";
				errorMessage = errorMessage.replaceAll("\\n",
							"\\\\n");
					errorMessage = errorMessage.replaceAll("\\r",
							"\\\\r");
				out.print(errorMessage+"登陆失败，请确保输入的用户名和口令是否正确！");
     }
	}

%>


