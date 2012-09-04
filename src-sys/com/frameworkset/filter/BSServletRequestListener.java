package com.frameworkset.filter;

import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.frameworkset.platform.ca.CAManager;
import com.frameworkset.platform.ca.CaProperties;
import com.frameworkset.platform.ca.CookieProperties;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.orm.transaction.TransactionManager;

/**
 * 
 * 
 * <p>Title: BSServletRequestListener.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: 三一集团</p>
 * @Date Jul 24, 2008 11:24:52 AM
 * @author biaoping.yin,尹标平
 * @version 1.0
 */
public class BSServletRequestListener implements javax.servlet.ServletRequestListener
{
	private static final Logger log = Logger.getLogger(BSServletRequestListener.class);
	  
	public void requestDestroyed(ServletRequestEvent requestEvent) {
//		System.out.println("requestDestroyed Thread.currentThread():" + Thread.currentThread());
//		System.out.println("requestDestroyed tx before:" + TransactionManager.getTransaction());
		
		if(requestEvent.getServletRequest() instanceof HttpServletRequest )
		{
			HttpServletRequest request = (HttpServletRequest)requestEvent.getServletRequest();
			String uri = request.getRequestURI();

			//是否启用了CA认证中心
			if(CaProperties.CA_LOGIN_SERVER){
				try {
					String cert_sn = (String)request.getSession().getAttribute(CookieProperties.CERT_SN);
					if(cert_sn != null){
						if(!cert_sn.equals(CAManager.getCookieValue(request, CookieProperties.CERT_SN))){
							request.getSession().invalidate();
						}	
					}
				} catch (Exception e) {
//					e.printStackTrace();
				}
			}
			
			if(com.frameworkset.listener.BSServletRequestListener.isInterceptResource(uri))
			{
//				
				boolean state = TransactionManager.destroyTransaction();
				if(state){
					log.debug("A DB transaction leaked in Page ["+ uri +"] has been forcibly destoried. ");
					System.out.println("A DB transaction leaked in Page ["+ uri +"] has been forcibly destoried. ");
				}
				AccessControl.init(null);

			}
			else if(uri.endsWith(".frame"))
			{
				AccessControl.init(null);
			}
				
		}		
//		System.out.println("requestDestroyed tx after:" + TransactionManager.getTransaction());
//		
//		System.out.println("requestDestroyed tx after DBUtil.getNumActive():" +DBUtil.getNumActive());
//		System.out.println("requestDestroyed tx after DBUtil.getNumIdle():" +DBUtil.getNumIdle());
	}

	public void requestInitialized(ServletRequestEvent requestEvent) {
		if(requestEvent.getServletRequest() instanceof HttpServletRequest )
		{
			HttpServletRequest request = (HttpServletRequest)requestEvent.getServletRequest();
			String uri = request.getRequestURI();

			
			
			if(com.frameworkset.listener.BSServletRequestListener.isInterceptResource(uri))
			{	
				boolean state = TransactionManager.destroyTransaction();
				if(state){
					log.debug("A DB transaction leaked before Page ["+ uri +"] has been forcibly destoried. ");
					System.out.println("A DB transaction leaked before Page ["+ uri +"] has been forcibly destoried. ");
				}
				AccessControl.init(null);

			}
			else if(uri.endsWith(".frame"))
			{
				AccessControl.init(null);
			}
				
				
		}	
		
	}
	
	
	

}
