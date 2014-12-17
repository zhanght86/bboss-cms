package com.frameworkset.platform.portal;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import com.frameworkset.platform.cms.util.FileUtil;

/** 
 * <p>类说明:</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: 三一集团</p>
 * @author  gao.tang 
 * @version V1.0  创建时间：Oct 19, 2009 4:42:55 PM 
 */
public class PortalIssueManagerImpl implements PortalIssueManager {

	public boolean appIssueIframePlugin(int portletCount,
			HttpServletRequest request) throws PortalIssueException {
		return issueIframePlugin(portletCount,request,true);
	}
	
	public boolean definedIssueIframePlugin(int portletCount,
			HttpServletRequest request) throws PortalIssueException {
		// TODO Auto-generated method stub
		return issueIframePlugin(portletCount,request,false);
	}
	
	/**
	 * 检查文件是否存在
	 * @param portletName
	 * @return
	 */
	private boolean checkPortletName(String portletName){
		File t_file = new File(PortalProperties.PORTAL_ISSUEPATH_WAR+"/"+portletName+".war");
		if (t_file.exists()){
			return true;
		}
		return false;
	}

	/**
	 * 将配置好的iframe portlet发布成portal模板插件war包
	 * @param portletCount	发布的portlet数
	 * @param request
	 * @param state			是否是应用模块发布：
	 * 						true为应用模块配置发布，false为自定义配置发布
	 * @return
	 */
	private boolean issueIframePlugin(int moduleCount,
			HttpServletRequest request, boolean state) throws PortalIssueException {
		
		String portletIframeName = request.getParameter("portletIframeName");
		if(portletIframeName == null || "".equals(portletIframeName)){
			throw new PortalIssueException("portlet war包名称 不能为空！");
		}
		
		if(checkPortletName(portletIframeName)){
			throw new PortalIssueException("已经存在名为["+portletIframeName+"] war包名称！");
		}
		// WEB-INF/liferay-display.xml文件配置
		StringBuffer liferay_display = new StringBuffer()
			.append("<?xml version=\"1.0\"?>\n")
			.append("<!DOCTYPE display PUBLIC \"-//Liferay//DTD Display 5.2.0//EN\" \"http://www.liferay.com/dtd/liferay-display_5_2_0.dtd\">\n")
			.append("<display>\n")
			.append("\t<category name=\"category.creatorepp\">\n");
		// WEB-INF/liferay-portlet.xml文件配置
		StringBuffer liferay_portlet = new StringBuffer()
			.append("<?xml version=\"1.0\"?>\n")
			.append("<!DOCTYPE liferay-portlet-app PUBLIC \"-//Liferay//DTD Portlet Application 5.2.0//EN\" \"http://www.liferay.com/dtd/liferay-portlet-app_5_2_0.dtd\">\n")
			.append("<liferay-portlet-app>\n");
		// WEB-INF/portlet.xml文件配置
		StringBuffer portlet = new StringBuffer()
			.append("<?xml version=\"1.0\" encoding=\"GBK\" ?>\n")
			.append("<portlet-app xmlns=\"http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd\" version=\"2.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd\">\n");
			
		String portalId = null;
		String portalName = null;
		String subsystemid = null;
		String appModuleSrc = null;
		String httpcontext = null;
		String issueArea = null;
		
		//add by jianfeng 20100118
		String MAXsrc = null;
		//add end
		
		for(int i = 0; i < moduleCount; i++){
			portalId = request.getParameter("portalId"+i);
			portalName = request.getParameter("portalName"+i);
			portalName = PortalUtil.getBytes(portalName);
			subsystemid = request.getParameter("subsystemid"+i);
			appModuleSrc = request.getParameter("appModuleSrc"+i);
			httpcontext = request.getParameter("httpcontext"+i);
			issueArea = request.getParameter("issueArea"+i);
			//add by jianfeng 20100118
			MAXsrc = request.getParameter("MAXsrc"+i);;
			//add end
			
			liferay_display.append("\t\t<portlet id=\"").append(portalId).append("\" />\n");
			
			liferay_portlet.append("\t<portlet>\n")
				.append("\t\t<portlet-name>").append(portalId).append("</portlet-name>\n")
				.append("\t\t<icon>/icon.png</icon>\n")
				.append("\t\t<configuration-action-class>com.liferay.plugin.action.ConfigurationActionImpl</configuration-action-class>\n")
				.append("\t\t<instanceable>")
				.append("1".equals(request.getParameter("instanceable"+i))?"true":"false")
				.append("</instanceable>\n")
				.append("\t\t<render-weight>1</render-weight>\n")
				.append("\t\t<ajaxable>false</ajaxable>\n")
				.append("\t\t<css-class-wrapper>creator-iframe-portlet</css-class-wrapper>\n")
				.append("\t</portlet>\n");
				
			portlet.append("\t<portlet>\n")
				.append("\t\t<portlet-name>").append(portalId).append("</portlet-name>\n")
				.append("\t\t<display-name>creator iframe</display-name>\n")
				.append("\t\t<portlet-class>com.liferay.plugin.portlet.IframeAppPortlet</portlet-class>\n")
				.append("\t\t<init-param>\n")
				.append("\t\t\t<name>view-jsp</name>\n")
				.append("\t\t\t<value>/view.jsp</value>\n")
				.append("\t\t</init-param>\n")
				.append("\t\t<expiration-cache>0</expiration-cache>\n")
				.append("\t\t<supports>\n")
				.append("\t\t\t<mime-type>text/html</mime-type>\n")
				.append("\t\t</supports>\n")
				.append("\t\t<portlet-info>\n")
				.append("\t\t\t<title>").append(portalName).append("</title>\n")
				.append("\t\t\t<short-title>").append(portalName).append("</short-title>\n")
				.append("\t\t\t<keywords>").append(portalName).append("</keywords>\n")
				.append("\t\t</portlet-info>\n")
				.append("\t\t<portlet-preferences>\n")
				.append("\t\t\t<preference>\n")
				.append("\t\t\t\t<name>form-method</name>\n")
				.append("\t\t\t\t<value>post</value>\n")
				.append("\t\t\t\t<read-only>true</read-only>\n")
				.append("\t\t\t</preference>\n")
				
				.append("\t\t\t<preference>\n")
				.append("\t\t\t\t<name>src</name>\n")
				.append("\t\t\t\t<value><![CDATA[").append(
						state?PortalUtil.getRealMenuPath(appModuleSrc, issueArea):appModuleSrc
						).append("]]></value>\n")
				.append("\t\t\t\t<read-only>")
				.append("1".equals(request.getParameter("appModuleSrcstate"+i))?"true":"false")
				.append("</read-only>\n")
				.append("\t\t\t</preference>\n")
				
				.append("\t\t\t<preference>\n")
				.append("\t\t\t\t<name>MAXsrc</name>\n")
				.append("\t\t\t\t<value><![CDATA[").append(
						MAXsrc
						).append("]]></value>\n")
				.append("\t\t\t\t<read-only>")
				.append("1".equals(request.getParameter("MAXsrcstate"+i))?"true":"false")
				.append("</read-only>\n")
				.append("\t\t\t</preference>\n")
				
				.append("\t\t\t<preference>\n")
				.append("\t\t\t\t<name>httpcontext</name>\n")
				.append("\t\t\t\t<value><![CDATA[").append(httpcontext).append("]]></value>\n")
				.append("\t\t\t\t<read-only>")
				.append("1".equals(request.getParameter("httpcontextstate"+i))?"true":"false")
				.append("</read-only>\n")
				.append("\t\t\t</preference>\n")
				.append("\t\t\t<preference>\n")
				.append("\t\t\t\t<name>APP_ID</name>\n")
				.append("\t\t\t\t<value><![CDATA[").append(subsystemid).append("]]></value>\n")
				.append("\t\t\t\t<read-only>")
				.append("1".equals(request.getParameter("appModuleSrcstate"+i))?"true":"false")
				.append("</read-only>\n")
				.append("\t\t\t</preference>\n")
				.append("\t\t</portlet-preferences>\n");
			
			String[] roles = request.getParameterValues("role"+i);
			for(int j = 0; j < roles.length; j++){
				portlet.append("\t\t<security-role-ref>\n")
					.append("\t\t\t<role-name>").append(roles[j]).append("</role-name>\n")
					.append("\t\t</security-role-ref>\n");
			}
			portlet.append("\t</portlet>\n");
				
		}

		liferay_display.append("\t</category>\n")
			.append("</display>");

		liferay_portlet.append("\t<role-mapper>\n")
			.append("\t\t<role-name>administrator</role-name>\n")
			.append("\t\t<role-link>Administrator</role-link>\n")
			.append("\t</role-mapper>\n")
			.append("\t<role-mapper>\n")
			.append("\t\t<role-name>guest</role-name>\n")
			.append("\t\t<role-link>Guest</role-link>\n")
			.append("\t</role-mapper>\n")
			.append("\t<role-mapper>\n")
			.append("\t\t<role-name>power-user</role-name>\n")
			.append("\t\t<role-link>Power User</role-link>\n")
			.append("\t</role-mapper>\n")
			.append("\t<role-mapper>\n")
			.append("\t\t<role-name>user</role-name>\n")
			.append("\t\t<role-link>User</role-link>\n")
			.append("\t</role-mapper>\n")
			.append("</liferay-portlet-app>");
			
		portlet.append("</portlet-app>");

		String msg = null;
		try{
			//temp目录
			String tmepPath = PortalProperties.APPROOT + "/portal/temp";
			//iframe模板文件所在目录
			String templateZip = PortalProperties.APPROOT + "/portal/template/web_iframe_plugin.zip";
			//删除temp目录下的文件和文件夹
			FileUtil.deleteSubfiles(tmepPath);
			//将iframe模板文件解压到temp目录
			FileUtil.unzip(templateZip,tmepPath);
			
			FileUtil.writeFile(PortalProperties.APPROOT + "/portal/temp/WEB-INF/liferay-display.xml",
					liferay_display.toString(),false);
			FileUtil.writeFile(PortalProperties.APPROOT + "/portal/temp/WEB-INF/liferay-portlet.xml", 
					liferay_portlet.toString(),false);
			FileUtil.writeFile(PortalProperties.APPROOT + "/portal/temp/WEB-INF/portlet.xml", 
					portlet.toString(),false);
			
			//将临时目录生成的文件打包成war包
			//System.out.println(System.currentTimeMillis());
			PortalUtil.jar(tmepPath,
				PortalProperties.PORTAL_ISSUEPATH_WAR+"/"+request.getParameter("portletIframeName")+".war");
		}catch(Exception e){
			throw new PortalIssueException(e);
		}
		return true;
	}

}
