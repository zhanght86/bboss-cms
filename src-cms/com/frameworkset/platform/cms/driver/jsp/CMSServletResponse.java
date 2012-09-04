package com.frameworkset.platform.cms.driver.jsp;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.frameworkset.platform.cms.driver.context.Context;

/**
 * 
 * <p>Title: com.frameworkset.platform.cms.driver.jsp.CMSServletResponse.java</p>
 *
 * <p>Description: 对内容管理系统中的请求响应进行封装，以便拦截浏览器端的输出</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: iSany</p>
 * @Date 2007-1-11
 * @author biaoping.yin
 * @version 1.0
 */
public class CMSServletResponse extends HttpServletResponseWrapper implements java.io.Serializable {
	private StringWriter buffer = null;

	private PrintWriter writer = null;

	private String contentType = "text/html;charset=UTF-8";
	
	private Context context;
	
	ServletOutputStream out;
	
	    	
	public CMSServletResponse(HttpServletResponse response,Context context) { 
		super(response);
		response.setContentType(this.contentType);	
//		this.setCharacterEncoding("GBK");
		
		CMSServletResponse temp = null;
		try
		{
			 temp = InternalImplConverter.getInternalResponse(response);
		}
		catch(Exception e)
		{
			
		}
		if(temp == null)
		{
			buffer = new StringWriter();
			writer = new PrintWriter(buffer);
			
			
			this.out = new PrintWriterServletOutputStream(writer);
			this.context = context;
			
		}
		else
		{
			this.buffer = temp.getInternalBuffer();
			this.writer = temp.getWriter();
			try {
				this.out = temp.getOutputStream();
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	
	
	
	
	public ServletOutputStream getOutputStream() throws IOException
	{
		return this.out;
	}

	public CMSServletResponse(HttpServletResponse response) {
		super(response);
		
		buffer = new StringWriter();
		writer = new PrintWriter(buffer);
		
		this.out = new PrintWriterServletOutputStream(writer);		
	}
	

//	public String getContentType() {
//		return contentType;
//	}

	public PrintWriter getWriter() {
		
		return writer;
	}

	public StringWriter getInternalBuffer() {
		
		return buffer;
	}

	public PrintWriter getInternalResponseWriter() throws IOException {
		return getWriter();
	}

	public boolean isIncluded() {
		return false;
	}

	public void setIncluded(boolean b) {
		
	}

	public Context getContext() {
		return context;
	}
	
//	private static final String pattern = "text/html";
//	public void setContentType(String contentType)
//	{
//		if(contentType.toLowerCase().startsWith(pattern))
//		{
//			super.setContentType(this.contentType);
//		}
//		else
//		{
//			super.setContentType(contentType);
//		}
//	}
	
	
	
	
	


}


