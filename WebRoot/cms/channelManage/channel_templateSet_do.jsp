<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.util.StringOperate"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*" %>
<%@ page import="com.frameworkset.platform.cms.container.Template" %>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*" %>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*" %>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%
try{
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	//String siteName = request.getParameter("siteName");
	String siteId   = request.getParameter("siteId");
	String channelId = request.getParameter("channelId");
	//String channelName = request.getParameter("channelName");
	String oId = request.getParameter("oId");
	String dId = request.getParameter("dId");
	String tId = request.getParameter("tId");
	String type = request.getParameter("type");
	//String tName = request.getParameter("tName");
	//System.out.println(tId+"    "+channelId+"    type: "+type);
	SiteManager sm = new SiteManagerImpl();
		Site site = sm.getSiteInfo(siteId) ;
	 ChannelManager cm = new ChannelManagerImpl();
	 RecursivePublishManagerImpl imp =new RecursivePublishManagerImpl();
	if(type.equals("1"))//为模板修改和设置操作
	{
		if (oId != null && !oId.equals("")) {
		    Channel channel=cm.getChannelInfo(channelId);
			int orachanneltemplateid=channel.getOutlineTemplateId();
			int newoid = Integer.parseInt(oId);
			if(orachanneltemplateid != newoid)
			{
				cm.updateChannelOutputTemplateId(Integer.parseInt(channelId),newoid);
				//频道概览模板更新
				if(orachanneltemplateid!=0)
				{
				  imp.deleteChannelOutRelation(channelId,site);
				}
			}
			
		} 
		if (dId != null && !dId.equals("")) {
		//频道细览模板更新
		    Channel channel=cm.getChannelInfo(channelId);
			int oradetailTemplateId=channel.getDetailTemplateId();
			int newdid = Integer.parseInt(dId);
			if(oradetailTemplateId != newdid)
			{
			    cm.updateChannelDetailTemplateId(Integer.parseInt(channelId),newdid);
			    if(oradetailTemplateId!=0)
				{
				  imp.deleteChannelDetailRelation(channelId,site);
				}
			}
		    
		} 
		if(null!=tId && !tId.equals(""))
		{
			int templateId = -1;
			 templateId = Integer.parseInt(tId);
			int oraIndexTemplateId=site.getIndexTemplateId();//获取原来站点的模板id
			site.setIndexTemplateId(templateId);
			sm.updateSite(site);
			if(oraIndexTemplateId!=0 && templateId!= oraIndexTemplateId)
		  {
		    //如果不为空且修改前后模板不同,调用删除站点在递归发布关系表中的关系方法
		     imp.deletesiteRelation(siteId);
		  }
		}
	}
	else//为模板清空操作
	{
		if (oId != null && !oId.equals("")) 
		{
			Channel channel=cm.getChannelInfo(channelId);
			int orachanneltemplateid=channel.getOutlineTemplateId();
			
			cm.updateChannelOutputTemplateId(Integer.parseInt(channelId),0);
			//频道概览模板更新
			if(orachanneltemplateid!=0)
			{
			  imp.deleteChannelOutRelation(channelId,site);
			}
			
		}
		
		if (dId != null && !dId.equals("")) 
		{
			Channel channel=cm.getChannelInfo(channelId);
			int oradetailTemplateId=channel.getDetailTemplateId();			 
		    cm.updateChannelDetailTemplateId(Integer.parseInt(channelId),0);
		    if(oradetailTemplateId!=0)
			{
			  imp.deleteChannelDetailRelation(channelId,site);
			}
			 
			
		}
		
		if(null!=tId && !tId.equals(""))
		{
			int templateId = -1;
			 
			int oraIndexTemplateId=site.getIndexTemplateId();//获取原来站点的模板id
			site.setIndexTemplateId(0);
			sm.updateSite(site);
			if(oraIndexTemplateId!=0  )
		  {
		    //如果不为空且修改前后模板不同,调用删除站点在递归发布关系表中的关系方法
		     imp.deletesiteRelation(siteId);
		  }
		}
	}
 	 
%>
	<script>
		alert("模板更新成功！");
		parent.close();
	</script>
<%
 	}catch(Exception e){
 		e.printStackTrace();
 	}
%>
// 	TemplateManager tm = new TemplateManagerImpl();
// 	Template template = tm.getTemplateInfo(tId);
// 	String tName = template.getName();
%>
