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
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import org.apache.log4j.Logger;

/**
 * jasperreport打印的服务器端
 * @author sxb
 *
 */
public class JasperPrintServlet extends HttpServlet {

	private  Logger logger = Logger.getLogger(JasperPrintServlet.class
			.getName());
	/**
	 * 
	 */
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		ServletContext context = this.getServletConfig().getServletContext();
		JasperPrint jasperPrint = (JasperPrint) request.getSession().getAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE);
		logger.info("打印jasperreport报表！");
		if (jasperPrint != null) {
			response.setContentType("application/octet-stream");
			ServletOutputStream ouputStream = response.getOutputStream();

			ObjectOutputStream oos = new ObjectOutputStream(ouputStream);
			oos.writeObject(jasperPrint);
			oos.flush();
			oos.close();

			ouputStream.flush();
			ouputStream.close();
		} else {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<head>");
			out
					.println("<title>JasperReports - Web Application Sample</title>");
			out
					.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../stylesheet.css\" title=\"Style\">");
			out.println("</head>");

			out.println("<body bgcolor=\"white\">");

			out.println("<span class=\"bold\">Empty response.</span>");

			out.println("</body>");
			out.println("</html>");
		}
	}

}
