package com.sany.mbp.pending.service;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

public class PendingService {

	private static final Logger logger = Logger.getLogger(PendingService.class.getName());
	
	public String getPending(String userName) {
		HttpClient httpclient = new HttpClient();
		//		int httptimeout = Properties.getString("HTTPTIMEOUT");
		httpclient.getHttpConnectionManager().getParams()   
		.setConnectionTimeout(2000);   
		httpclient.getHttpConnectionManager().getParams().setSoTimeout(2000); 
//		GetMethod getMethod = new GetMethod(Properties.getString("UIMURL"));
		GetMethod getMethod = new GetMethod("http://uimweb.sany.com.cn/WSS/supervision/main/indexMain.action");
		if (userName == null ) {
			return null;
		}

		try {
			logger.info("start getPending , user : "+ userName);

			int statusCode = httpclient.executeMethod(getMethod);
			System.out.println(statusCode);  
			 
			System.out.println(getMethod.getResponseBodyAsString());
		} catch ( Exception ex) {
			logger.error("user login into sap error " , ex);
		} 
		return null;  
	}
	
}
