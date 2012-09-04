<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page
	import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.security.authorization.AccessException,com.frameworkset.common.poolman.DBUtil,com.frameworkset.platform.config.ConfigManager,com.liferay.portlet.iframe.action.SSOUserMapping,com.liferay.portlet.iframe.action.UserMapping"%>
<%@page import="com.frameworkset.platform.framework.Framework"%>
<%
	//System.out.println(session.getSessionContext());

	response.setHeader("P3P", "CP=CAO PSA OUR");
	AccessControl control = AccessControl.getInstance();
	String approot = request.getContextPath();
	//登陆名称的长度
	//String userNamelength = ConfigManager.getInstance().getConfigValue("userNameLength");

	//系统管理版本号，2.0和2.0以上的版本,默认版本为1.0
	String systemVersion = ConfigManager.getInstance().getConfigValue(
			"system.version", "1.0");

	/**
	 * #从portal中传过来的参数，在portal中进行单点登录。此处逻辑只适合通过portal登录到应用 start
	 */
	String url = request.getParameter("OUTER_URL");
	String userName = request.getParameter("OUT_USER_ACCOUNT_KEY");
	String subsystem = request.getParameter("subsystem_id");
	//明码密码
	String password = request.getParameter("OUTER_USER_PASSWORD_KEY");
	boolean isCasServer = ConfigManager.getInstance()
			.getConfigBooleanValue("isCasServer", false);
	
	if(isCasServer){
		userName = (String) session.getAttribute("edu.yale.its.tp.cas.client.filter.user");
		
		boolean state = false;
        if (userName != null && !"".equals(userName))
        {
            state = SSOUserMapping.isIncludeUser(userName);
            //应用支撑平台使用该方法
            //state = SSOUserMapping.isAppIncludeUser(userName,subsystem);
			
        }
        if (state)
        {
            //系统管理版本号，2.0和2.0以上的版本,默认版本为1.0
            if(password == null || "".equals(password) || "null".equals(password)){
            	password = SSOUserMapping.getUserPassword(userName);
            	//应用支撑平台使用该方法
            	//password = SSOUserMapping.getAppUserPassword(userName,subsystem);
            }
            if (subsystem == null)
                subsystem = "module";
            
            
            if (systemVersion.compareTo("1.0") > 0)
            {
                AccessControl.getInstance().login(request, response, userName, password);

                //  if(subsystem == null) subsystem = "module";
                /** 
                需要全屏时，将response.sendRedirect("index.jsp");注释掉，
                将response.sendRedirect("sysmanager/refactorwindow.jsp");打开
                 */
                if(url == null)
	            {
	            	url = approot + "/index.jsp?subsystem_id=" + subsystem;
	            }
                response.sendRedirect(response
								.encodeRedirectURL(url));
                return;
            }
            else
            {
                control.checkAccess(request, response, false);
                String user = control.getUserAccount();
                if (user == null || "".equals(user) || !userName.equals(user))
                {
                    AccessControl.getInstance().login(request, response, userName, password);
                    if (subsystem == null)
                        subsystem = "module";
                    if(url == null)
		            {
		            	url = approot + "/index.jsp?subsystem_id=" + subsystem;
		            }
                    response.sendRedirect(response
								.encodeRedirectURL(url));
                    return;
                }
                else
                {
                    if (subsystem == null)
                        subsystem = "module";
                    if(url == null)
		            {
		            	url = approot + "/index.jsp?subsystem_id=" + subsystem;
		            }
                    response.sendRedirect(response
								.encodeRedirectURL(url));
                    return;
                }
            }
        }
        else
        {
        	if(userName == null || userName.equals(""))
        	{
            	out.print("系统启用了cas单点登录功能，请在web.xml的CAS Filte中拦截/sso/sso.jsp页面");
            	return;
            }
            else
            {
            	out.print("用户【" + userName + "】在此应用中没有开通！ ");
            	return;
            }
        }
	}

	String faileurl = request.getParameter("OUTER_FAILED_URL");

	String failedmessage = request.getParameter("OUTER_FAILED_MESSAGE");

	//System.out.println("failedmessage:" + failedmessage);

	//  System.out.print("subsystem_id:" + subsystem);

	if (failedmessage != null && failedmessage.trim().length() > 0) {
		faileurl = faileurl + "?OUTER_FAILED_MESSAGE=" + failedmessage;
	}

	//String subsystem_id = request.getParameter("subsystem_id");
	if (userName != null) {
		System.out.println("通过sso登录系统～～～～～～～～～～～～～～～～～～～～～～～～～～～"
				+ userName);
		//判断用户是否存在
		boolean state = SSOUserMapping.isIncludeUser(userName);
		//应用支撑平台使用该方法
		//boolean state = SSOUserMapping.isAppIncludeUser(userName,subsystem);
		try {

			System.out.println(state + "------------------------2.0");
			if (state) {
				if (systemVersion.compareTo("1.0") > 0) {
					System.out.println("------------------------2.0");
					AccessControl.getInstance().login(request,
							response, userName, password);

					//  if(subsystem == null) subsystem = "module";
					/** 
					需要全屏时，将response.sendRedirect("index.jsp");注释掉，
					将response.sendRedirect("sysmanager/refactorwindow.jsp");打开
					 */
					response.sendRedirect(response
							.encodeRedirectURL(url));
					//response.sendRedirect("sysmanager/refactorwindow.jsp?subsystem_id=" + subsystem);
				} else {
					System.out.println("-------------------------1.0");
					control.checkAccess(request, response, false);
					String user = control.getUserAccount();
					if (user == null || "".equals(user)
							|| !userName.equals(user)) {
						AccessControl.getInstance().login(request,
								response, userName, password);
						response.sendRedirect(response
								.encodeRedirectURL(url));
					} else {
						String old = (String) session
								.getAttribute(Framework.SUBSYSTEM);
						if (old != null && !old.equals(subsystem))
							session.setAttribute(Framework.SUBSYSTEM,
									subsystem);
						response.sendRedirect(response
								.encodeRedirectURL(url));
					}
				}
			} else {
				out.print("用户【" + userName + "】在此应用中没有开通！ ");
			}
		} catch (AccessException ex) {
			//ex.printStackTrace();    
			//System.out.println(faileurl); 
			response.sendRedirect(response.encodeRedirectURL(faileurl));

		} catch (Exception ex) {
			//ex.printStackTrace();
			// System.out.println(faileurl);       
			response.sendRedirect(response.encodeRedirectURL(faileurl));
		}
	}
	/**
	 * #从portal中传过来的参数，在portal中进行单点登录。此处逻辑只适合通过portal登录到应用 end
	 */
%>

