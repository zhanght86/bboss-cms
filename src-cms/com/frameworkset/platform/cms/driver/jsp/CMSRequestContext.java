package com.frameworkset.platform.cms.driver.jsp;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import com.frameworkset.platform.cms.driver.config.DriverConfiguration;
import com.frameworkset.platform.cms.driver.url.CMSURL;
import com.frameworkset.platform.cms.driver.url.CMSURLParser;
import com.frameworkset.platform.cms.util.CMSUtil;

/**
 * Defines the context of the currentl cms request.
 * Allows for the retrieval of the original request
 * and response throughout the lifetime of the request.
 *
 * Provides a consistent interface for parsing/creating
 * CMSURLs to the outside world.
 * 
 * @author biaoping.yin
 */
public class CMSRequestContext implements java.io.Serializable {

    /**
     * The attribute key to bind the portal environment instance to servlet
     * request.
     */
    public final static String REQUEST_KEY =
            CMSRequestContext.class.getName();
    
    private DriverConfiguration config;
    
    

    /** The servletContext of execution. **/
    private ServletContext servletContext = null;
    
    private PageContext pageContext;

    /** The incoming servlet request. */
    private HttpServletRequest request = null;

    /** The incoming servlet response. */
    private HttpServletResponse response = null;

    /** The requested portal URL. */
    private CMSURL requestedCmsURL = null;



//	private CMSRequestContext parentRequestContext;


    // Constructor -------------------------------------------------------------

    /**
     * Creates a PortalRequestContext instance.
     * @param request  the incoming servlet request.
     * @param response  the incoming servlet response.
     */
    public CMSRequestContext(PageContext pageContext,
                                HttpServletRequest request,
                                HttpServletResponse response) {
    	this.pageContext = pageContext;
        this.servletContext = this.pageContext.getServletContext();
        this.request = request;
        this.response = response;
        this.config = CMSUtil.getCMSDriverConfiguration();
        request.setAttribute(REQUEST_KEY, this);
        //System.out.println("public CMSRequestContext(PageContext pageContext,HttpServletRequest request,HttpServletResponse response):"+request);
    }

//    public CMSRequestContext(CMSRequestContext requestContext, JspFile jspFile) {
//		this.parentRequestContext = requestContext;
//		this.servletContext = this.parentRequestContext.getServletContext();
//		this.request = this.parentRequestContext.getRequest();
//		this.response = this.parentRequestContext.getResponse();
//		
//	}

	private ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return this.servletContext;
	}

	/**
     * Returns the portal environment from the servlet request. The portal
     * envirionment instance is saved in the request scope.
     * @param request  the servlet request.
     * @return the portal environment.
     */
    public static CMSRequestContext getContext(
            HttpServletRequest request) {
    	//System.out.println("(CMSRequestContext) request.getAttribute(REQUEST_KEY):" + (CMSRequestContext) request.getAttribute(REQUEST_KEY));
    	//System.out.println("(CMSRequestContext) request.getAttribute(REQUEST_KEY):" + request);
        return (CMSRequestContext) request.getAttribute(REQUEST_KEY);
    }

    /**
     * Returns the servlet request.
     * @return the servlet request.
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * Returns the servlet response.
     * @return the servlet response.
     */
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * Returns the requested portal URL.
     * @return the requested portal URL.
     */
    public CMSURL getRequestedCmsURL() {
        if(requestedCmsURL == null) {
//            DriverConfiguration config = (DriverConfiguration)
//                servletContext.getAttribute(AttributeKeys.DRIVERCONFIGKEY);
            DriverConfiguration config = getDriverConfiguration();
            CMSURLParser parser = config.getCMSUrlParser();
            requestedCmsURL = parser.parse(request);
        }
        return requestedCmsURL;
    }
    
    

    public CMSURL createCMSURL() {
        return (CMSURL)getRequestedCmsURL().clone();
    }
    
    public DriverConfiguration getDriverConfiguration()
    {
    	return config;
    }

	public PageContext getPageContext() {
		return pageContext;
	}
    
}
