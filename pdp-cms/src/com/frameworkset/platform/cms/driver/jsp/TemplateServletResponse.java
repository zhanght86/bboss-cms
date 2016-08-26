package com.frameworkset.platform.cms.driver.jsp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class TemplateServletResponse extends HttpServletResponseWrapper implements java.io.Serializable {
	private boolean usingWriter;
    private boolean usingStream;
    HttpServletRequest httpServletRequest;
    private ServletOutputStream wrappedWriter;
	public TemplateServletResponse(HttpServletResponse response,HttpServletRequest httpServletRequest) {
		super(response);
		this.httpServletRequest = httpServletRequest;
		
	}
	
	  /**
     * Returns the nested HttpServletRequest instance.
     * @return the nested HttpServletRequest instance.
     */
    protected HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }
	
    /**
     * TODO: javadoc about why we are using a wrapped writer here.
     * @see org.apache.pluto.util.PrintWriterServletOutputStream
     */
    public ServletOutputStream getOutputStream()
    throws IllegalStateException, IOException {
        if (usingWriter) {
            throw new IllegalStateException(
            		"getPortletOutputStream can't be used "
            		+ "after getWriter was invoked.");
        }
        if (wrappedWriter == null) {
            wrappedWriter = new PrintWriterServletOutputStream(
            		getHttpServletResponse().getWriter());
        }
        usingStream = true;
        return wrappedWriter;
    }
    
    public PrintWriter getWriter()
    throws IllegalStateException, IOException {
        if (usingStream) {
            throw new IllegalStateException(
            		"getWriter can't be used "
            		+ "after getOutputStream was invoked.");
        }
        usingWriter = true;
        return getHttpServletResponse().getWriter();
    }
    
    public HttpServletResponse getHttpServletResponse() {
        return (HttpServletResponse) super.getResponse();
    }


}
