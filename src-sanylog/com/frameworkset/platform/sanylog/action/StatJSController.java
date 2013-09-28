package com.frameworkset.platform.sanylog.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;

import com.frameworkset.platform.framework.FrameworkServlet;
import com.frameworkset.util.VelocityUtil;

public class StatJSController {
	public void statjs(HttpServletRequest request,HttpServletResponse response,String appname) throws IOException
	{
		//插入应用
		//获取应用对应的日志服务器
		//生成js返回到客户端
		Template template = VelocityUtil
				.getTemplate("log/log.vm");

		VelocityContext context = new VelocityContext();
		String server = request.getServerName();
		int port = request.getServerPort();
		String protocol = request.getProtocol();
		if(protocol.startsWith("HTTP/"))
			protocol = "http";
		else
			protocol = "https";
		String appcontext = request.getContextPath();
		StringBuffer logserver = new StringBuffer(); 
		logserver.append(protocol).append("://").append(server).append(":").append(port).append(appcontext);
		context.put("logserver", logserver.toString());
		response.setContentType(FrameworkServlet.CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		template.merge(context, out);
		out.flush();
		
	}

}
