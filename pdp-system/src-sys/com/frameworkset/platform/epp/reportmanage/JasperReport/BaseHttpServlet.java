/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package com.frameworkset.platform.epp.reportmanage.JasperReport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JasperPrint;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: BaseHttpServlet.java 1229 2006-04-19 10:27:35Z teodord $
 */
public abstract class BaseHttpServlet extends HttpServlet
{


	/**
	 *
	 */
	public static final String DEFAULT_JASPER_PRINT_LIST_SESSION_ATTRIBUTE = "net_sf_jasperreports_j2ee_jasper_print_list";
	public static final String DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE = "net_sf_jasperreports_j2ee_jasper_print";

	public static final String JASPER_PRINT_LIST_REQUEST_PARAMETER = "jrprintlist";
	public static final String JASPER_PRINT_REQUEST_PARAMETER = "jrprint";

	public static final String BUFFERED_OUTPUT_REQUEST_PARAMETER = "buffered"; 
	
	public static final String REPORT_FILE_NAME="filename";
	
//	protected  static final ThreadLocal THREAD_FILE_NAME=new ThreadLocal();
	
	public static void toSession(HttpServletRequest request,String key,JasperPrint jasperPrint) throws IOException
	{
		request.getSession().setAttribute(key, jasperPrint);
//		java.io.ByteArrayOutputStream out = null; 
//		java.io.ObjectOutputStream output = null;
//		try
//		{
//			out = new ByteArrayOutputStream(); 
//			output = new java.io.ObjectOutputStream(out); 
//			output.writeObject(jasperPrint);
//			output.flush();
//			request.getSession().setAttribute(key, out.toByteArray());
//		}
//		finally
//		{
//			try {
//				if(out != null)
//				{
//					out.close();
//					out = null;
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			try {
//				if(output != null)
//				{
//					output.close();
//					output = null;
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
	}
	
	public static JasperPrint getJasperPrint(HttpServletRequest request,String key)
	{
		return (JasperPrint)request.getSession().getAttribute(key);
//		JasperPrint jasperPrint = null;
//	
//		java.io.ObjectInputStream output = null;
//		java.io.ByteArrayInputStream intput = null;
//		try
//		{
//			byte[] b = (byte[])request.getSession().getAttribute(key); 
//			intput = new ByteArrayInputStream(b);
//			output = new java.io.ObjectInputStream(intput); 
//			jasperPrint = (JasperPrint)output.readObject();
//			return jasperPrint;
//			
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//			return null;
//		}
//		finally
//		{
//			try {
//				if(intput != null)
//				{
//					intput.close();
//					intput = null;
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			try {
//				if(output != null)
//				{
//					output.close();
//					output = null;
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}
			
	/**
	 *
	 */
	public static List getJasperPrintList(HttpServletRequest request)
	{
		//通过参数获得文件名，绑定到该线程
//		THREAD_FILE_NAME.set(request.getParameter("filename"));
		
		String jasperPrintListSessionAttr = request.getParameter(JASPER_PRINT_LIST_REQUEST_PARAMETER);
		if (jasperPrintListSessionAttr == null)
		{
			jasperPrintListSessionAttr = DEFAULT_JASPER_PRINT_LIST_SESSION_ATTRIBUTE;
		}

		String jasperPrintSessionAttr = request.getParameter(JASPER_PRINT_REQUEST_PARAMETER);
		if (jasperPrintSessionAttr == null)
		{
			jasperPrintSessionAttr = DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE;
		}
		
//		List jasperPrintList = (List)request.getSession().getAttribute(jasperPrintListSessionAttr);
		List jasperPrintList = null;
		if (jasperPrintList == null)
		{
//			JasperPrint jasperPrint = (JasperPrint)request.getSession().getAttribute(jasperPrintSessionAttr);
			JasperPrint jasperPrint = getJasperPrint(request,jasperPrintSessionAttr);
			if (jasperPrint != null)
			{
				jasperPrintList = new ArrayList();
				jasperPrintList.add(jasperPrint);
			}
		}
		
		return jasperPrintList;
	}


}
