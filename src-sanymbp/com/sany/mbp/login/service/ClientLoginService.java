package com.sany.mbp.login.service;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.frameworkset.platform.config.ConfigManager;


public class ClientLoginService {

	private static final Logger logger = Logger.getLogger(ClientLoginService.class.getName());

	public String httpclientLogin(String pdhsessionid, String pdid) {
		HttpClient httpclient = new HttpClient();
		//		int httptimeout = Properties.getString("HTTPTIMEOUT");
		httpclient.getHttpConnectionManager().getParams()   
		.setConnectionTimeout(2000);   
		httpclient.getHttpConnectionManager().getParams().setSoTimeout(2000); 
		GetMethod getMethod = new GetMethod(ConfigManager.getInstance().getConfigValue("UIM_SSO_URL"));
		if (pdhsessionid == null || pdid == null) {
			return null;
		}

		try {
			logger.info("user start logining into uim " );
			getMethod.setRequestHeader("Cookie", "PD-H-SESSION-ID="+pdhsessionid+";PD-ID="+pdid);
			getMethod.setRequestHeader("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)"); 

			int statusCode = httpclient.executeMethod(getMethod);
			System.out.println(statusCode);  
			// 301或者302  
			if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY  
					|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {  
				Header locationHeader = getMethod.getResponseHeader("location");  
				String location = null;  
				if (locationHeader != null) {  
					location = locationHeader.getValue();  
					System.out.println("The page was redirected to:" + location);  
				} else {  
					System.err.println("Location field value is null.");  
				}  
				return null;  
			}  
			System.out.println(getMethod.getResponseBodyAsString());
		} catch ( Exception ex) {
			logger.error("user login into sap error " , ex);
		} 
		return null;  
	}

}
