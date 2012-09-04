package com.frameworkset.platform.cms.driver.jsp;

import java.io.IOException;




/**
 * 
 * <p>Title: CMSRequestDispatcher</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-3-12 18:05:35
 * @author biaoping.yin
 * @version 1.0
 */
public interface CMSRequestDispatcher extends java.io.Serializable {
	
    /**
    *
    * Includes the content of a resource (servlet, JSP page,
    * HTML file) in the response. In essence, this method enables 
    * programmatic server-side includes.
    * <p>
    * The included servlet cannot set or change the response status code
    * or set headers; any attempt to make a change is ignored.
    *
    *
    * @param request 			a {@link CMSServletRequest} object 
    *					that contains the client request
    *
    * @param response 			a {@link CMSServletResponse} object 
    * 					that contains the render response
    *
    * @exception PortletException 	if the included resource throws a ServletException, 
    *                                  or other exceptions that are not Runtime-
    *                                  or IOExceptions.
    *
    * @exception java.io.IOException	if the included resource throws this exception
    *
    *
    */
    
   public void include(CMSServletRequest request, CMSServletResponse response)
	throws CMSException, java.io.IOException;
   
   public void commoninclude(CMSServletRequest internalRequest, CMSServletResponse internalResponse) throws CMSException, IOException;

}
