package com.frameworkset.platform.security;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void sessionCreated(HttpSessionEvent event) {

	}

	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session =  (HttpSession)event.getSource();

		Map principals = (Map) session
        .getAttribute(AccessControl.PRINCIPAL_INDEXS);
		if(principals != null)
		{
			AccessControl.logoutdirect(session);
		}
	}

}
