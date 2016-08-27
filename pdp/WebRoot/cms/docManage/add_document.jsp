<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*" %>
<%@page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@page import="com.frameworkset.platform.cms.container.Template"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.bean.*"%>
<%@page import="com.frameworkset.platform.util.RemoteFileHandle"%>
<%@page import="com.frameworkset.platform.cms.customform.*"%>
<%@page import="com.frameworkset.platform.cms.driver.htmlconverter.*"%>
<%@page import="com.frameworkset.platform.cms.driver.i18n.*"%>
<%@page import="com.frameworkset.platform.cms.docsourcemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    String userId = accesscontroler.getUserID();
	
	String dprior = request.getParameter("documentprior");
	Document doc= new Document();

	ChannelManager cm = new ChannelManagerImpl();
	SiteManager siteManager = new SiteManagerImpl();
	LogManager logManager = null;
	
	Site site = siteManager.getSiteInfo(request.getParameter("siteid"));
	Channel channel = cm.getChannelInfo(request.getParameter("channelId"));

	String sitedir = site.getSiteDir();//频道相对路径
	String relativePath = channel.getChannelPath();//站点相对路径

	doc.setChanel_id(Integer.parseInt(request.getParameter("channelId")));
	doc.setUser_id(Integer.parseInt(request.getParameter("userid")));
	doc.setCreateUser(Integer.parseInt(request.getParameter("userid")));
	doc.setTitle(request.getParameter("title"));
	doc.setTitlecolor(request.getParameter("titlecolor"));
	doc.setSubtitle(request.getParameter("subtitle"));
	doc.setSecondtitle(request.getParameter("secondtitle"));
	doc.setPicPath(request.getParameter("picpath"));
	doc.setMediapath(request.getParameter("mediapath"));
    doc.setPublishfilename(request.getParameter("publishfilename"));
    doc.setDoc_class(request.getParameter("doc_class"));
    HashMap map = new HashMap();
    map.put("ext_org",request.getParameter("ext_org"));
    map.put("ext_index",request.getParameter("ext_index"));
    map.put("ext_wh",request.getParameter("ext_wh"));
    map.put("ext_class",request.getParameter("ext_class"));
    map.put("ext_djh",request.getParameter("ext_djh"));
    doc.setExtColumn(map);
    if(request.getParameter("seq")!=null&&!(request.getParameter("seq").equals("")))
    {
      doc.setSeq(Integer.parseInt(request.getParameter("seq")));
    }
    else
    {
      doc.setSeq(999999);
    }
  
    if(request.getParameter("isnew") != null)
    {
    	doc.setIsNew(1);
    	if(request.getParameter("newpicpath") == null || "".equals(request.getParameter("newpicpath")))
    		doc.setNewPicPath("image/new.gif");//使用默认值
    	else
    		doc.setNewPicPath(request.getParameter("newpicpath"));
    }
    else
    {
    	doc.setIsNew(0);
    	doc.setNewPicPath(request.getParameter("newpicpath"));
    }
    //System.out.println(request.getParameter("publishfilename"));
    String isnewdocsource=request.getParameter("isnewdocsource");
    String docwtime = request.getParameter("docwtime");
    String ordertime = request.getParameter("ordertime");
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	java.util.Date date = formatter.parse(docwtime);
	java.util.Date orderDate = formatter.parse(ordertime);
	doc.setDocwtime(date);
    doc.setOrdertime(orderDate);
    
    String docsourceName=request.getParameter("inputdocsource");
    
    //去掉空格
    if(docsourceName != null){
    	docsourceName = docsourceName.trim();
    }
    
    
    DocsourceManagerImpl docsourcemanager=new DocsourceManagerImpl();
    Docsource docsource_1=new Docsource();
    docsource_1 = docsourcemanager.getDsrcListBy(docsourceName);
    
    if((docsource_1.getSRCNAME()==null)||(docsource_1.getSRCNAME().equals("")))//判定是否新的稿源
    { 
    
       //新的稿源名称
       
       //System.out.println("new docsourceName is:"+docsourceName);
       //获取稿源ID
       Docsource docsource=new Docsource();
       docsource.setSRCNAME(docsourceName);
       
       boolean createflag=false;
       createflag=docsourcemanager.creatorDsrc(docsource);
       if(createflag)
       {
      
         docsource=docsourcemanager.getDsrcListBy(docsourceName);
         doc.setDocsource_id(docsource.getDOCSOURCE_ID());
         //System.out.println(docsource.getDOCSOURCE_ID());
       }
       else
       {
          doc.setDocsource_id(1);
       }
    }
    else{
	   
		//if(request.getParameter("docsource_id")==null){
		//	doc.setDocsource_id(1);
		//}else{
		doc.setDocsource_id(docsource_1.getDOCSOURCE_ID());
			//System.out.println(request.getParameter("docsource_id"));
		//}
	}
	
	
	doc.setKeywords(request.getParameter("keywords"));
	if(request.getParameter("author")==null||("".equals(request.getParameter("author")))){
		doc.setAuthor("不详");
	}else{
		doc.setAuthor(request.getParameter("author"));
	}
	
	doc.setDocabstract("".equals(request.getParameter("docabstract"))?"无":request.getParameter("docabstract"));
	if("1".equals(request.getParameter("parentDetailTpl")))
	{
		Template detpl = (Template)cm.getDetailTemplateOfChannel(request.getParameter("channelId"));
		if(detpl != null )
		{
			doc.setDetailtemplate_id(detpl.getTemplateId());
		}
		doc.setParentDetailTpl("1");
	}
	else
	{
		if(request.getParameter("detailtemplate_id")!=null&&!"".equals(request.getParameter("detailtemplate_id")))
		{		
			
			doc.setDetailtemplate_id(Integer.parseInt(request.getParameter("detailtemplate_id")));
			
		}
		doc.setParentDetailTpl("0");
	}
	
/*	if(request.getParameter("content")==null){
		doc.setContent("");
	}else{
		doc.setContent(request.getParameter("content"));
	}
*/	doc.setDoctype(Integer.parseInt(request.getParameter("doctype")));
	doc.setLinktarget(request.getParameter("linktarget"));
	doc.setDoc_level(Integer.parseInt(request.getParameter("doc_level")));//文档级别
	DocumentManager dmi = new DocumentManagerImpl();
	int b = 0;
	boolean flag = true;

	if(Integer.parseInt(request.getParameter("doctype"))==0)
	{//只有普通文档才要分析content
	
		CmsLinkProcessor processor = new CmsLinkProcessor(request,relativePath,sitedir);
		processor.setHandletype(CmsLinkProcessor.PROCESS_EDITCONTENT);
		try {
			String content = processor.process(request.getParameter("content"),CmsEncoder.ENCODING_UTF_8);
			doc.setContent(content);
			b = dmi.creatorDoc(doc);//保存文档
			//保存日志
			//modify by xinwang.jiao 2007.07.25 替换成系统管理的日志接口。
			//dmi.recordDocOperate(b,doc.getUser_id(),"新增",0,"新增");
			logManager = SecurityDatabase.getLogManager();
			logManager.log(accesscontroler.getUserAccount(),"新增文档.文档标题:【" + site.getName() + " 站点，" + channel.getDisplayName() + " 频道】" + doc.getSubtitle(),"文档管理",com.frameworkset.util.StringUtil.getClientIP(request),"");
			
			CmsLinkTable linktable = processor.getExternalPageLinkTable();
			Iterator it = linktable.iterator();
			while(it.hasNext())
			{
				CmsLinkProcessor.CMSLink link = (CmsLinkProcessor.CMSLink)it.next();
				String remoteAddr = link.getOrigineLink().getHref();
				String contentPath = link.getRelativeFilePath();
				String localPath = config.getServletContext().getRealPath("/") + "cms/siteResource/" + sitedir + "/_webprj/" + contentPath;
				try {
					RemoteFileHandle rf = new RemoteFileHandle(remoteAddr,localPath);
					flag = rf.download();

					//Attachment attachment = new Attachment();
						
					//attachment.setDocumentId(b);
					//attachment.setDescription(remoteAddr);
					//attachment.setUrl(contentPath.substring(contentPath.lastIndexOf("/") + 1,contentPath.length()));
					//attachment.setType(10);//文档内容中的图片等(remote的图片等)
					//attachment.setOriginalFilename(remoteAddr);

					//flag = dmi.createAttachment(attachment);
				} catch (Exception e) {
					System.out.println("取远程图片时出错！");
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	else
	{
	
		doc.setContent(request.getParameter("content")==null?"":request.getParameter("content"));
		b = dmi.creatorDoc(doc);
		logManager = SecurityDatabase.getLogManager();
		logManager.log(accesscontroler.getUserAccount(),"新增文档.文档标题:【" + site.getName() + " 站点，" + channel.getDisplayName() + " 频道】" + doc.getSubtitle(),"文档管理",com.frameworkset.util.StringUtil.getClientIP(request),"");
	}



	
	if(b!=0)
	{
		//新增文档的相关文档 start
		if(request.getParameter("doclist")!=null&&!"".equals((String)request.getParameter("doclist")))
		{
			//System.out.println(request.getParameter("doclist"));
			String[] doclist = (request.getParameter("doclist")).split("№");
			int[] relatedDocIds = new int[doclist.length];
			if(doclist.length>0)//这个判断条件？
			{
				//新增聚合文档的相关 start 
				if(doc.getDoctype() == 3)
				{
					String[] titles = (request.getParameter("titles")).split("№");
					for(int i=0;i<doclist.length;i++)
					{

						DocAggregation aggrDoc = new DocAggregation();

						aggrDoc.setAggrdocid(b);
						aggrDoc.setSeq(i + 1);
						aggrDoc.setIdbyaggr(Integer.parseInt(doclist[i]));
						aggrDoc.setTitle(titles[i]);
						aggrDoc.setType("1");

						flag = dmi.addAggrDoc(aggrDoc);
					}
				}
				//新增聚合文档的相关 end
				else
				{
					for(int i=0;i<doclist.length;i++)
					{
						relatedDocIds[i] = Integer.parseInt(doclist[i]);
					}
					DocRelated docrelated = new DocRelated();
					docrelated.setDocId(b);
					docrelated.setOpUserId(Integer.parseInt(request.getParameter("userid")));
					flag = dmi.creatorDocRelated(docrelated,relatedDocIds);
				}
			}
		}
		////新增文档的相关文档 end
		//新增文档的附件 start 
		if(request.getParameter("url")!=null&&!"".equals((String)request.getParameter("url")))
		{
			String[] descriptions = ((String)request.getParameter("description")).split("№");
			String[] originalFilenames = ((String)request.getParameter("originalFilename")).split("№");
			String[] urls = ((String)request.getParameter("url")).split("№");
			if(urls.length>0)
			{
				for(int i=0;i<urls.length;i++)
				{
					Attachment attachment = new Attachment();
					
					attachment.setDocumentId(b);
					attachment.setDescription(descriptions[i]);
					attachment.setUrl(urls[i]);
					attachment.setType(2);//
					attachment.setOriginalFilename(originalFilenames[i]);

					flag = dmi.createAttachment(attachment);
				}
			}
		}
		//新增文档的附件 end
		//新增文档的图片 start 
		if(request.getParameter("url2")!=null&&!"".equals((String)request.getParameter("url2")))
		{
			String[] descriptions = ((String)request.getParameter("description2")).split("№");
			String[] originalFilenames = ((String)request.getParameter("originalFilename2")).split("№");
			String[] urls = ((String)request.getParameter("url2")).split("№");
			if(urls.length>0)
			{
				for(int i=0;i<urls.length;i++)
				{
					Attachment attachment = new Attachment();
					
					attachment.setDocumentId(b);
					attachment.setDescription(descriptions[i]);
					attachment.setUrl(urls[i]);
					attachment.setType(3);//
					attachment.setOriginalFilename(originalFilenames[i]);

					flag = dmi.createAttachment(attachment);
				}
			}
		}
		//新增文档的图片 end
		//save 扩展字段内容
		if(doc.getDoctype()==0)
		{
			String[] ids = (request.getParameter("fieldids")).split("№");
			String[] values = (request.getParameter("extfieldvalues")).split("№");
			String[] types = (request.getParameter("extfieldtypes")).split("№");
			CustomFormManager cf = new CustomFormManagerImpl();
			flag = cf.saveDocExtFieldValues(String.valueOf(b),ids,values,types);
		}
		//新增文档专题报道
		System.out.println(request.getParameter("specialsiteid"));
		if(request.getParameter("specialsiteid")!=null && !"".equals(request.getParameter("specialsiteid")))
		{
		  String[] specialsiteid=(request.getParameter("specialsiteid").split("&"));
		  String[] specialchannelid=(request.getParameter("sepcialchannelid").split("&"));
		  int[] channelids=new int[specialchannelid.length];
          int[] siteids=new int[specialsiteid.length];
          for(int specialsize=0;specialsize<siteids.length;specialsize++)
          {
             channelids[specialsize]=Integer.parseInt(specialchannelid[specialsize]);
             siteids[specialsize]=Integer.parseInt(specialsiteid[specialsize]); 
          }
          int[] intDocid=new int[]{b};
		  dmi.copyDocs(request,intDocid,channelids,siteids,1,Integer.parseInt(userId));//1为新稿
		}
		//新增文档专题报道结束

	}
	
	//保存完文档后，进行送审、提交发布、立即发布等操作。
	//operFlag为10表示保存后将送审，为11表示保存后提交发布，为12表示保存后立即发布
	String operFlag = request.getParameter("flag");
	String pageUrl = ""; 
	if("10".equals(operFlag)){			//送审
		String[] auditors = request.getParameterValues("auditor");
		//System.out.println("auditors: "+ auditors.length);
		int[] intAuditors = null;
		if(auditors!=null&& auditors.length>0){
		 	intAuditors = new int[auditors.length];
			for(int i=0;i<auditors.length;i++){
				intAuditors[i] = Integer.parseInt(auditors[i]);
			}
		}
		int tanid = dmi.canTransition(b,2);
	    if(tanid >= 0){
		    dmi.deliverDoc(b,intAuditors,Integer.parseInt(userId),0); 
		    //dmi.recordDocOperate(b,Integer.parseInt(userId),"送审",tanid,"送审");
		    logManager = SecurityDatabase.getLogManager();
			logManager.log(accesscontroler.getUserAccount(),"文档送审.文档标题:【" + site.getName() + " 站点，" + channel.getDisplayName() + " 频道】" + doc.getSubtitle(),"文档管理",com.frameworkset.util.StringUtil.getClientIP(request),"");
		 }
	}else if("11".equals(operFlag)){			//提交发布
		String[] publishers = request.getParameterValues("publisher");
		int[] intPublishers = null;
		if(publishers!=null&& publishers.length>0){
		 	intPublishers = new int[publishers.length];
			for(int i=0;i<publishers.length;i++){
				intPublishers[i] = Integer.parseInt(publishers[i]);
			}
		}	
		//System.out.println("publisher: "+ publishers.length);
		int tranid = dmi.canTransition(b,11);
		if(tranid >=0){
			dmi.subPublishDoc(b,intPublishers,Integer.parseInt(userId),0);   
			//dmi.recordDocOperate(b,Integer.parseInt(userId),"提交发布",tranid,"提交发布");
			logManager = SecurityDatabase.getLogManager();
			logManager.log(accesscontroler.getUserAccount(),"文档提交发布.文档标题:【" + site.getName() + " 站点，" + channel.getDisplayName() + " 频道】" + doc.getSubtitle(),"文档管理",com.frameworkset.util.StringUtil.getClientIP(request),"");
		}
	}else if("12".equals(operFlag)){				//立即发布	
			//初始化发布引擎
		 	WEBPublish publish = new WEBPublish();
			publish.init(request,response,pageContext,accesscontroler);
			PublishCallBack callback = new PublishCallBackImpl();
			publish.setPublishCallBack(callback);
		
			int tranID = dmi.canTransition(b,5);   
			String chnlId = doc.getChanel_id()+"";
			int docType = doc.getDoctype();
			if(tranID >0){
				String siteId = request.getParameter("siteid");
				boolean[] publishWay = siteManager.getSitePublishDestination(siteId);
				int[] distributeManners = siteManager.getSiteDistributeManners(siteId);
				//if(docType == 0){biaoping.yin 注释 2007.10.20
					publish.publishDocument(siteId,chnlId,b+"",publishWay, distributeManners);
					pageUrl = callback.getPageUrl();
					flag = callback.getPublishMonitor().isAllFailed()==true?false:true;
					//biaoping.yin 注释开始
				//}else{biaoping.yin 注释 2007.10.20			
				//	dmi.updateDocumentStatus(b,5);biaoping.yin 注释 2007.10.20
				//	flag = true;biaoping.yin 注释 2007.10.20
				//}biaoping.yin 注释 2007.10.20
				//dmi.recordDocOperate(b,Integer.parseInt(userId),"发布",tranID,"发布");
				logManager = SecurityDatabase.getLogManager();
				logManager.log(accesscontroler.getUserAccount(),"文档发布.文档标题:【" + site.getName() + " 站点，" + channel.getDisplayName() + " 频道】" + doc.getSubtitle(),"文档管理",com.frameworkset.util.StringUtil.getClientIP(request),"");
			}
	}
	
	if(b!=0&&flag){%>
		<SCRIPT LANGUAGE="JavaScript"> 
			
			if(parent.window.confirm("操作成功！\n\n您还要继续采集吗？"))
			{
				parent.window.opener.parent.window.location.reload();
				window.close();
			}
			else
			{
				parent.window.opener.parent.window.opener.window.location.reload();
				parent.window.opener.parent.window.close();
				window.close();
			}
		</script>
	
	<%}else{%>
		
		<SCRIPT LANGUAGE="JavaScript"> 
			parent.window.alert("操作失败！");
			var buttons = parent.window.opener.parent.document.getElementsByTagName("input");
			for(var i=0;i<buttons.length;i++)
			{
				buttons[i].disabled = false;
			}
			window.close();
			//parent.document.all.divProcessing.style.display="none";
		</script>
		
	<%
	
		//window.opener.location.href=window.opener.location.href
	}%>			