package com.sany.workflow.test;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.frameworkset.web.servlet.ModelMap;

public class GetUimCookiesTest {

	private static String WebSealURL="http://uimweb.sany.com.cn/pkmslogin.form";
	private static int 	WebSealTimeout = 1500;
	
	
	public String getCookie(HttpServletRequest request,HttpServletResponse response)throws Exception{

		HttpClient httpclient = new HttpClient();
		httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(WebSealTimeout);
		httpclient.getHttpConnectionManager().getParams().setSoTimeout(WebSealTimeout);
		javax.servlet.http.Cookie[] jcookies = null;
		PostMethod post = new PostMethod(WebSealURL);
			     NameValuePair[] data = {
					new NameValuePair("username", "hanyh"),
					new NameValuePair("password", "Traning6^"),
					new NameValuePair("login-form-type", "pwd") };
			     
			        post.setRequestBody(data);
					post.setHttp11(true);

					int returncode = httpclient.executeMethod(post);

					System.out.print(post.getResponseBodyAsString());

					Cookie[] cookies = httpclient.getState().getCookies();
					if (null != cookies && cookies.length != 0) {
						request.getSession().setAttribute("UIMCookies", cookies);
						jcookies = new javax.servlet.http.Cookie[cookies.length];
						
						javax.servlet.http.Cookie jcookie = null;
						int i = 0;
						for (Cookie cookie : cookies) {

							jcookie = new javax.servlet.http.Cookie(cookie.getName(),cookie.getValue());
							jcookie.setMaxAge(500000);
							jcookie.setDomain(".sany.com.cn");
							jcookie.setPath(cookie.getPath());
							jcookie.setVersion(cookie.getVersion());
                            response.addCookie(jcookie);
							jcookies[i] = jcookie;
							i++;
						}
					}

	
		return null;
	}
	/**
	 * @param args
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public static void main(String[] args) throws HttpException, IOException {
		HttpClient httpclient = new HttpClient();
		httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(WebSealTimeout);
		httpclient.getHttpConnectionManager().getParams().setSoTimeout(WebSealTimeout);
		PostMethod post = new PostMethod(WebSealURL);
		
		
		
		
			     NameValuePair[] data = {
					new NameValuePair("username", "qingl2"),
					new NameValuePair("password", "1qaz@WSX"),
					new NameValuePair("login-form-type", "pwd") };
			     
			     post.setRequestBody(data);
					post.setHttp11(true);

					int returncode = httpclient.executeMethod(post);

					System.out.print(post.getResponseBodyAsString());

					Cookie[] cookies = httpclient.getState().getCookies();
					for(Cookie cookie:cookies){
						System.out.println(cookie.getName()+"---------------"+cookie.getValue());
					}
	}

}
