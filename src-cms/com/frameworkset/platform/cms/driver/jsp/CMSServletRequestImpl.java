package com.frameworkset.platform.cms.driver.jsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.url.CMSURL;
import com.frameworkset.platform.cms.driver.url.CMSURLParameter;

public class CMSServletRequestImpl extends HttpServletRequestWrapper implements java.io.Serializable,CMSServletRequest {
	private static final Log LOG = LogFactory.getLog(CMSServletRequestImpl.class);
	/**
	 * jsp页面标识
	 */
	private JspletWindow jspletWindow;
	private CMSURL cmsUrl;
	private Map parameters;
	private boolean included;
	private Context context;
	private PageContext pageContext;

	public CMSServletRequestImpl(HttpServletRequest request,PageContext pageContext,
							 JspletWindow jspletWindow,
							 Context context) {
		super(request);
		this.pageContext = pageContext;
		this.jspletWindow = jspletWindow;
		try
		{
			cmsUrl =  CMSRequestContext.getContext(request).getRequestedCmsURL();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		this.context = context;
	}
	
	public CMSServletRequestImpl(HttpServletRequest request, PageContext pageContext2) {
		super(request);
	}

	public String getParameter(String name) {
        String[] values = (String[]) this.getParameterMap().get(name);
        if (values != null) {
            return values[0];
        }
        return null;
    }

    /**
     * Retreive the Parameters.
     * @return Map of parameters targeted to the window associated with this
     *         request.
     */
    public Map getParameterMap() {
        if (parameters == null) {
            initParameterMap();
        }
        return Collections.unmodifiableMap(parameters);
    }

    /**
     * Initialize parameters for this request.  We must be careful to make sure
     * that render parameters are only made available if they were targeted for
     * this specific window.
     */
    private void initParameterMap() {
        parameters = new HashMap();
        if(cmsUrl != null)
        {
	        Iterator iterator = cmsUrl.getParameters().iterator();
	        while (iterator.hasNext()) {
	            CMSURLParameter param = (CMSURLParameter) iterator.next();
	            String name = param.getName();
	            String[] values = param.getValues();
	            if (param.getWindowId().equals(jspletWindow.getJspletWindowID().getStringId())) {
	                parameters.put(name, values);
	            }
	        }
        }
        
        if(jspletWindow != null)
        {
	        String id = jspletWindow.getJspletWindowID().getStringId();
	        if (jspletWindow.getJspletWindowID().getStringId().equals(id)) {
	            Enumeration params = super.getParameterNames();
	            while (params.hasMoreElements()) {
	                String name = params.nextElement().toString();
	                String[] values = super.getParameterValues(name);
	                if (parameters.containsKey(name)) {
	                    String[] temp = (String[]) parameters.get(name);
	                    String[] all = new String[values.length + temp.length];
	                    System.arraycopy(values, 0, all, 0, values.length);
	                    System.arraycopy(temp, 0, all, values.length, temp.length);
	                }
	                parameters.put(name, values);
	            }
	        }
        }
        else
        {
        	 Enumeration params = super.getParameterNames();
            while (params.hasMoreElements()) {
                String name = params.nextElement().toString();
                String[] values = super.getParameterValues(name);
                if (parameters.containsKey(name)) {
                    String[] temp = (String[]) parameters.get(name);
                    String[] all = new String[values.length + temp.length];
                    System.arraycopy(values, 0, all, 0, values.length);
                    System.arraycopy(temp, 0, all, values.length, temp.length);
                }
                parameters.put(name, values);
            }
        }
    }

    /**
     * Get an enumeration which contains each of the names for which parameters
     * exist.
     * @return an enumeration of all names bound as parameters.
     */
    public Enumeration getParameterNames() {
        return Collections.enumeration(getParameterMap().keySet());
    }

    /**
     * Get the values associated with the given parameter key.
     * @param name the Parameter name used to key the parameter.
     * @return a String[] of all values bound to the given name as a parameter.
     */
    public String[] getParameterValues(String name) {
        return (String[]) getParameterMap().get(name);
    }

	public boolean isIncluded() {
		// TODO Auto-generated method stub
		return this.included;
	}

	public void setIncluded(boolean b) {
		// TODO Auto-generated method stub
		this.included = b;
	}

	public void setIncludedQueryString(String queryString)
    throws IllegalStateException {
    
		if (!included) {
    		throw new IllegalStateException("Parameters cannot be appended to "
    				+ "render request which is not included in a dispatch.");
    	}
    	if (queryString != null && queryString.trim().length() > 0) {
    		// Copy all the original render parameters.
    		parameters = new HashMap(super.getParameterMap());
    		// Merge the appended parameters to the render parameter map.
    		// The original render parameters should not be overwritten.
    		mergeQueryString(parameters, queryString);
    		// Log the new render parameter map.
    		if (LOG.isDebugEnabled()) {
    			LOG.debug("Merged parameters: " + parameters.toString());
    		}
    	} else {
    		if (LOG.isDebugEnabled()) {
    			LOG.debug("No query string appended to the included request.");
    		}
    	}
    }

	private void mergeQueryString(Map parameters2, String queryString) {
//		 Create the appended parameters map:
    	//   key is the parameter name as a string,
    	//   value is a List of parameter values (List of String).
        Map appendedParameters = new HashMap();
        
        // Parse the appended query string.
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("Parsing appended query string: " + queryString);
    	}
        StringTokenizer st = new StringTokenizer(queryString, "&", false);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            int equalIndex = token.indexOf("=");
            if (equalIndex > 0) {
                String key = token.substring(0, equalIndex);
                String value = null;
                if (equalIndex < token.length() - 1) {
                	value = token.substring(equalIndex + 1);
                } else {                	value = "";
                }
                List values = (List) appendedParameters.get(key);
                if (values == null) {
                	values = new ArrayList();
                }
                values.add(value);
                appendedParameters.put(key, values);
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(appendedParameters.size() + " parameters appended.");
        }
        
        // Merge the appended parameters and the original parameters.
        if (LOG.isDebugEnabled()) {
        	LOG.debug("Merging appended parameters and original parameters...");
        }
    	for (Iterator it = appendedParameters.keySet().iterator();
    			it.hasNext(); ) {
    		String key = (String) it.next();
    		List values = (List) appendedParameters.get(key);
    		// If the parameter name (key) exists, merge parameter values.
    		if (parameters.containsKey(key)) {
    			String[] originalValues = (String[]) parameters.get(key);
    			if (originalValues != null) {
    				for (int i = 0; i < originalValues.length; i++) {
    					values.add(originalValues[i]);
    				}
    			}
    		}
    		parameters.put(key, (String[]) values.toArray(new String[values.size()]));
    	}
		
	}

	public Context getContext() {
		return context;
	}

	public PageContext getPageContext() {
		return pageContext;
	}
	
	
	
	

}
