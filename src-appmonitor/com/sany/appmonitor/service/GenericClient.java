/*
 * @(#)PendingHandler.java
 * 
 * Copyright @ 2001-2011 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于三一集团有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一集团有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.sany.appmonitor.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

/**
 * Generic HttpCliemt.
 * @author xusy3	
 * @since 2012-02-24
 */
public class GenericClient {

	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Use HttpClient Post Method to request, use response as stream;
	 * @param url
	 * @param data
	 * @param timeOut
	 * @return response body
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public String requestAsStream(String url, String data,  int timeOut) throws Exception {
		
		long start = System.currentTimeMillis();
		
		HttpClient httpClient = new HttpClient();
		
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeOut);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeOut);
		
		PostMethod postMethod = null;
		String responseBody = null;
		BufferedReader reader = null;
		
		try {
			postMethod = new PostMethod(url);
			postMethod.addRequestHeader("Content-Type","text/html;charset=UTF-8");
			
			if (data != null && !data.equals("")) {
				postMethod.setRequestBody(data);
			}
			
			int statusCode = httpClient.executeMethod(postMethod);
			
			if (statusCode != 200) {
				logger.info("Status Code:" + statusCode);
			}
			
			reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
			
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			responseBody = buffer.toString();
			
		} catch (Exception e) {
			throw e;
		} finally {
			try{
				if (postMethod != null) {
					postMethod.releaseConnection();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			long end = System.currentTimeMillis();
			logger.info("Get URL: " +url +" spent " + (end-start)/1000+"s");
		}
		
		return responseBody;
	}
	
	/**
	 * Use HttpClient Post Method to request, use response as String;
	 * @param url
	 * @param data
	 * @param timeOut
	 * @return response body
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public String requestAsString(String url, String data,  int timeOut) throws Exception {
		
		long start = System.currentTimeMillis();
		
		
		HttpClient httpClient = new HttpClient();
		
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeOut);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeOut);
		
		PostMethod postMethod = null;
		String responseBody = null;
		
		try {
			postMethod = new PostMethod(url);
			postMethod.addRequestHeader("Content-Type","text/html;charset=UTF-8");
			
			if (data != null && !data.equals("")) {
				postMethod.setRequestBody(data);
			}
			
			int statusCode = httpClient.executeMethod(postMethod);
			
			if (statusCode != 200) {
				logger.info("Status Code:" + statusCode);
			}
			
			responseBody = postMethod.getResponseBodyAsString();
			
		} catch (Exception e) {
			throw e;
		} finally {
			try{
				if (postMethod != null) {
					postMethod.releaseConnection();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			long end = System.currentTimeMillis();
			logger.info("Get URL: " +url +" spent " + (end-start)/1000+"s");
		}
		
		return responseBody;
	}
}
