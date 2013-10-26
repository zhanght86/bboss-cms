package com.frameworkset.platform.cms.driver.jsp;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.frameworkset.common.tag.pager.tags.LoadDataException;
import com.frameworkset.common.tag.pager.tags.PagerDataSet;
import com.frameworkset.platform.cms.driver.context.CMSContext;
import com.frameworkset.platform.cms.driver.context.ChannelContext;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.dataloader.CMSDetailDataLoader;
import com.frameworkset.platform.cms.driver.dataloader.DefaultDetailDataLoader;

/**
 * <p>Title: CMSRequestDispatcherImpl</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-3-12 18:01:50
 * @author biaoping.yin
 * @version 1.0
 */
public class CMSRequestDispatcherImpl implements CMSRequestDispatcher {
    private static Logger LOG = Logger.getLogger(CMSRequestDispatcherImpl.class);;
 // Private Member Variables ------------------------------------------------
    
    /** The nested servlet request dispatcher instance. */
    private RequestDispatcher requestDispatcher = null;
    
    /** The included query string. */
    private String queryString = null;
    /**
     * 存放旧的pager页面对象，以便进行恢复
     */
    private Object oldObject = null;
    
    /**
	 * 保存先前的pagerContext，以便标签执行完毕后清除
	 */
	private Object oldPagerContext;
    
    
    // Constructors ------------------------------------------------------------
    
    /**
     * Creates an instance. This constructor should be called to construct a
     * named dispatcher.
     * @param requestDispatcher  the servlet request dispatcher.
     * @see javax.portlet.PortletContext#getNamedDispatcher(String)
     */
    public CMSRequestDispatcherImpl(RequestDispatcher requestDispatcher) {
        this.requestDispatcher = requestDispatcher;
        if (LOG.isDebugEnabled()) {
        	LOG.debug("Named dispatcher created.");
        }
    }
    
    /**
     * Creates an instance. This constructor should be called to construct a
     * portlet request dispatcher.
     * @param requestDispatcher  the servlet request dispatcher.
     * @param queryString  the included query string.
     * @see javax.portlet.PortletContext#getRequestDispatcher(String)
     */
    public CMSRequestDispatcherImpl(RequestDispatcher requestDispatcher,
                                        String queryString) {
        this(requestDispatcher);
        this.queryString = queryString;
        if (LOG.isDebugEnabled()) {
        	LOG.debug("Request dispatcher created.");
        }
    }
    
    
    // PortletRequestDispatcher Impl -------------------------------------------
    
    public void include(CMSServletRequest internalRequest, CMSServletResponse internalResponse) throws CMSException, IOException {

//        InternalRenderRequest internalRequest = (InternalRenderRequest)
//                InternalImplConverter.getInternalRequest(request);
//        InternalRenderResponse internalResponse = (InternalRenderResponse)
//                InternalImplConverter.getInternalResponse(response);
        
        boolean isIncluded = (internalRequest.isIncluded()
        		|| internalResponse.isIncluded());
        String pagerContextid = "pagerContext." + ((Context)internalRequest.getContext()).getID();
        String objectid =  "dataset." + ((Context)internalRequest.getContext()).getID();
        PagerDataSet dataSet = null;
        boolean flag = false;
        try {
        	internalRequest.setIncluded(true);
        	internalRequest.setIncludedQueryString(queryString);
        	internalResponse.setIncluded(true);
        	
        	CMSDetailDataLoader dataLoader = null;

        	if(internalRequest.getContext() instanceof ContentContext)
        	{	      
        		dataLoader =  ((Context)internalRequest.getContext()).getCMSDetailDataLoader();
        		if(dataLoader == null)
        			dataLoader = new DefaultDetailDataLoader();
        		
        		
        	}
        	else if(internalRequest.getContext() instanceof ChannelContext)
        	{
        		
        	}
        	else if(internalRequest.getContext() instanceof CMSContext)
        	{
        		
        	}
	        		
	        		

        	if(dataLoader != null)
        	{
        		 ((Context)internalRequest.getContext()).setCMSDetailDataLoader(dataLoader);
        		
        		this.oldPagerContext = internalRequest.getAttribute(pagerContextid);
        		dataSet = new PagerDataSet((CMSServletRequestImpl)internalRequest,(CMSServletResponseImpl)internalResponse,internalRequest.getPageContext());
//            	dataSet.setPageContext(internalRequest.getPageContext());
//            	dataSet.origineTag = this;
        		
            	try {
    				dataSet.init();
    		
    				dataSet.push();
    				dataSet.doDataLoading();
    				dataSet.setVariable();
    				flag = true;
//    				dataSet.setVariable();    				
    			} catch (LoadDataException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    			this.oldObject = internalRequest.getAttribute(objectid);
	        	internalRequest.setAttribute(objectid,dataSet);
	        	
	        	
        	}
            requestDispatcher.include(
            		(HttpServletRequest) internalRequest,
            		(HttpServletResponse) internalResponse);
        } catch (IOException ex) {
        	ex.printStackTrace();
            throw ex;
        } catch (ServletException ex) {
        	ex.printStackTrace();
            if (ex.getRootCause() != null) {
            	
                throw new CMSException(ex.getRootCause());
            } else {
                throw new CMSException(ex);
            }
        } finally {
        	try
        	{
	        	if(flag)
	        	{
	        		dataSet.pop();  
	        		dataSet.cmsClear();
	        	}
	        	internalRequest.setIncluded(isIncluded);
	        	internalResponse.setIncluded(isIncluded);
	        	/**
	        	 * 恢复数据获取接口
	        	 */
	        	if(this.oldObject != null)
		        	internalRequest.setAttribute(objectid,
		        								 this.oldObject);
	        	if(this.oldPagerContext != null)
	        		internalRequest.setAttribute(pagerContextid,this.oldPagerContext);

        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        	}
        	
        }
    }
    
// PortletRequestDispatcher Impl -------------------------------------------
    
    public void commoninclude(CMSServletRequest internalRequest, CMSServletResponse internalResponse) throws CMSException, IOException {

//        InternalRenderRequest internalRequest = (InternalRenderRequest)
//                InternalImplConverter.getInternalRequest(request);
//        InternalRenderResponse internalResponse = (InternalRenderResponse)
//                InternalImplConverter.getInternalResponse(response);
        
        boolean isIncluded = (internalRequest.isIncluded()
        		|| internalResponse.isIncluded());
       
        boolean flag = false;
        try {
        	
            requestDispatcher.include(
            		(HttpServletRequest) internalRequest,
            		(HttpServletResponse) internalResponse);
        } catch (IOException ex) {
        	ex.printStackTrace();
            throw ex;
        } catch (ServletException ex) {
        	ex.printStackTrace();
            if (ex.getRootCause() != null) {
            	
                throw new CMSException(ex.getRootCause());
            } else {
                throw new CMSException(ex);
            }
        } finally {
        	
        	internalRequest.setIncluded(isIncluded);
        	internalResponse.setIncluded(isIncluded);
        	
        	
        }
    }


}
