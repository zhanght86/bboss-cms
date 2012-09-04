package com.frameworkset.platform.epp.reportmanage.JasperReport;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.util.FileBufferedOutputStream;


public abstract class AbstractXlsServlet extends BaseHttpServlet
{


	public void service(
		HttpServletRequest request,
		HttpServletResponse response
		) throws IOException, ServletException
	{
		List jasperPrintList = BaseHttpServlet.getJasperPrintList(request);

		if (jasperPrintList == null)
		{
			throw new ServletException("未找到jasper打印文档！！");
		}
		
		Boolean isBuffered = Boolean.valueOf(request.getParameter(BaseHttpServlet.BUFFERED_OUTPUT_REQUEST_PARAMETER));
		if (isBuffered.booleanValue())
		{
			FileBufferedOutputStream fbos = new FileBufferedOutputStream();
			JRXlsAbstractExporter exporter = getXlsExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, fbos);
			exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);

			try 
			{
				exporter.exportReport();
				fbos.close();

				if (fbos.size()> 0)
				{
					response.setContentType("application/xls");
					response.setHeader("Content-Disposition", "inline; filename="+THREAD_FILE_NAME.get()+".xls");
					response.setContentLength(fbos.size());
					ServletOutputStream ouputStream = response.getOutputStream();
					try
					{
						fbos.writeData(ouputStream);
						fbos.dispose();
						ouputStream.flush();				
					}
					finally
					{
						if (ouputStream != null)
						{
							try
							{
								ouputStream.close();
							}
							catch (IOException ex)
							{
							}
						}
					}
				}
			} 
			catch (JRException e) 
			{
				throw new ServletException(e);
			}
			finally
			{
				fbos.close();
				fbos.dispose();
			}
//			else
//			{
//				response.setContentType("text/html");
//				PrintWriter out = response.getWriter();
//				out.println("<html>");
//				out.println("<body bgcolor=\"white\">");
//				out.println("<span class=\"bold\">Empty response.</span>");
//				out.println("</body>");
//				out.println("</html>");
//			}
		}
		else
		{
			response.setContentType("application/xls");
			response.setHeader("Content-Disposition", "inline; filename="+THREAD_FILE_NAME.get()+".xls");

			JRXlsAbstractExporter exporter = getXlsExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			
			OutputStream ouputStream = response.getOutputStream();
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);

			exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);

			try 
			{
				exporter.exportReport();
			} 
			catch (JRException e) 
			{
				throw new ServletException(e);
			}
			finally
			{
				if (ouputStream != null)
				{
					try
					{
						ouputStream.close();
					}
					catch (IOException ex)
					{
					}
				}
			}
		}
	}

	
	/**
	 *
	 */
	protected abstract JRXlsAbstractExporter getXlsExporter();

	
}
