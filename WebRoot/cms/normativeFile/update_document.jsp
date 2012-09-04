<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*" %>
<%@ page import="com.frameworkset.platform.comm.infoClass.InfoClassImp"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);

	String userid = accesscontroler.getUserID();
	String channelname = request.getParameter("channelName");
	String siteid = request.getParameter("siteid");
	String channelId = request.getParameter("channelId");
	String docid = request.getParameter("docid");
	
	//是否提示成功，并关闭窗口（因为返工文档等保存后还要送审，所以无需关闭窗口）
	String closeFlag = request.getParameter("closeFlag");

	Document doc= new Document(); 

	ChannelManager cm = new ChannelManagerImpl();
	SiteManager siteManager = new SiteManagerImpl();
	LogManager logManager = null;
	
	Site site = siteManager.getSiteInfo(request.getParameter("siteid"));
	Channel channel = cm.getChannelInfo(request.getParameter("channelId"));

	String sitedir = site.getSiteDir();//频道相对路径
	String relativePath = channel.getChannelPath();//站点相对路径

	if(request.getParameter("status")==null){
		doc.setStatus(0);
	}else{
		doc.setStatus(Integer.parseInt(request.getParameter("status")));
	}
	doc.setDocument_id(Integer.parseInt(request.getParameter("docid")));
	doc.setTitle(request.getParameter("title"));
	doc.setTitlecolor(request.getParameter("titlecolor"));
	//doc.setDocumentprior(Integer.parseInt(request.getParameter("documentprior")));
	doc.setSubtitle(request.getParameter("subtitle"));
	doc.setSecondtitle(request.getParameter("secondtitle"));
	doc.setPicPath(request.getParameter("picpath"));
	doc.setMediapath(request.getParameter("mediapath"));
	InfoClassImp infoClassImp = new InfoClassImp();
	HashMap map = new HashMap();
    map.put("ext_org",request.getParameter("ext_org"));
    map.put("ext_index",infoClassImp.getIndexNumber(request.getParameter("ext_org"),request.getParameter("ext_class")));
    map.put("ext_wh",request.getParameter("ext_wh"));
    map.put("ext_class",request.getParameter("ext_class"));
    map.put("ext_djh",request.getParameter("ext_djh"));
    doc.setExtColumn(map);
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
	//String isnewdocsource=request.getParameter("isnewdocsource");
	String docsourceName=request.getParameter("inputdocsource");
    DocsourceManagerImpl docsourcemanager=new DocsourceManagerImpl();
    Docsource docsource_1=new Docsource();
    docsource_1 = docsourcemanager.getDsrcListBy(docsourceName);
	if((docsource_1.getSRCNAME()==null)||(docsource_1.getSRCNAME().equals("")))//判定是否新的稿源
    { 
       //新的稿源名称
       //String docsourceName=request.getParameter("inputdocsource");
       //System.out.println("new docsourceName is:"+docsourceName);
       //获取稿源ID
       Docsource docsource=new Docsource();
       docsource.setSRCNAME(docsourceName);
       //DocsourceManagerImpl docsourcemanager=new DocsourceManagerImpl();
       boolean createflag=false;
       createflag=docsourcemanager.creatorDsrc(docsource);
       if(createflag)
       {
           docsource=docsourcemanager.getDsrcListBy(docsourceName);
           doc.setDocsource_id(docsource.getDOCSOURCE_ID());
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
		//doc.setDetailtemplate_id(0);
		doc.setDetailtemplate_id(((Template)cm.getDetailTemplateOfChannel(request.getParameter("channelId"))).getTemplateId());
		doc.setParentDetailTpl("1");
	}
	else
	{
		if(request.getParameter("detailtemplate_id")!=null&&!"".equals(request.getParameter("detailtemplate_id")))
		{	//System.out.println("else if:"+request.getParameter("detailtemplate_id"));
			doc.setDetailtemplate_id(Integer.parseInt(request.getParameter("detailtemplate_id")));
		}
		doc.setParentDetailTpl("0");
	}
	if(request.getParameter("doclevel")==null){
		doc.setDoclevel(0);
	}else{
		doc.setDoclevel(Integer.parseInt(request.getParameter("doclevel")));
	}
	//doc.setContent(request.getParameter("content"));
	doc.setDoctype(Integer.parseInt(request.getParameter("doctype")));
	doc.setLinktarget(request.getParameter("linktarget"));
	java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//doc.setCreateTime(sf.parse(request.getParameter("createtime")));
	doc.setDocwtime(sf.parse(request.getParameter("docwtime")));
	doc.setOrdertime(sf.parse(request.getParameter("ordertime")));
	if(request.getParameter("seq")!=null&&!(request.getParameter("seq").equals("")))
    {
      doc.setSeq(Integer.parseInt(request.getParameter("seq")));
    }
    else
    {
      doc.setSeq(999999);
    }
	doc.setDoc_level(Integer.parseInt(request.getParameter("doc_level")));//文档级别
	doc.setPublishfilename(request.getParameter("publishfilename"));
	DocumentManager dmi = new DocumentManagerImpl();
	//获取原文档对象
	boolean isdeleterelation=false; //设置删除关联关系标记
	if(request.getParameter("docid")!=null && !"".equals(request.getParameter("docid"))){
	  Document oradocument=dmi.getDoc(request.getParameter("docid"));
	  System.out.println(oradocument.getStatus());
	  if(oradocument.getStatus()==5)//已发布
	  {//表示该文档已经发布
	  
	     int oratemplateid=oradocument.getDetailtemplate_id();
	     if(oratemplateid!=doc.getDetailtemplate_id() && !doc.getParentDetailTpl().equals("1"))
	     {
	     isdeleterelation=true ;
	     System.out.println("isdeleterelation1-----");
	     }
	  }
	}
	boolean b = false;
	boolean flag = true;

	//处理文档内容
	if(Integer.parseInt(request.getParameter("doctype"))==0)
	{//只有普通文档才要分析content
		CmsLinkProcessor processor = new CmsLinkProcessor(request,relativePath,sitedir);
		processor.setHandletype(CmsLinkProcessor.PROCESS_EDITCONTENT);
		try {
			String content = processor.process(request.getParameter("content"),CmsEncoder.ENCODING_UTF_8);
			doc.setContent(content);//
			b = dmi.updateDoc(doc);
			logManager = SecurityDatabase.getLogManager();
			logManager.log(accesscontroler.getUserAccount(),"修改文档.文档标题:【" + site.getName() + " 站点，" + channel.getDisplayName() + " 频道】" + doc.getSubtitle(),"文档管理",request.getRemoteAddr(),"");
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

					Attachment attachment = new Attachment();
						
					attachment.setDocumentId(Integer.parseInt(docid));
					attachment.setDescription(remoteAddr);
					attachment.setUrl(contentPath.substring(contentPath.lastIndexOf("/") + 1,contentPath.length()));
					attachment.setType(10);//文档内容中的图片等(remote的图片等)
					attachment.setOriginalFilename(remoteAddr);

					flag = dmi.createAttachment(attachment);
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
		b = dmi.updateDoc(doc);
		logManager = SecurityDatabase.getLogManager();
		logManager.log(accesscontroler.getUserAccount(),"修改文档.文档标题:【" + site.getName() + " 站点，" + channel.getDisplayName() + " 频道】" + doc.getSubtitle(),"文档管理",request.getRemoteAddr(),"");
	}

	//更新文档的相关文档 start
	flag = dmi.deleteDocRelated(Integer.parseInt(request.getParameter("docid")));//先删除已有的信息
	if(request.getParameter("doclist")!=null&&!"".equals((String)request.getParameter("doclist")))
	{
		String[] doclist = (request.getParameter("doclist")).split("№");
		
		int[] relatedDocIds = new int[doclist.length];
		if(doclist.length>0)//这个判断条件？
		{
			//update 聚合文档的相关 start 
			if(doc.getDoctype() == 3)
			{
				dmi.delAggrDoc(Integer.parseInt(request.getParameter("docid")));
				String[] titles = (request.getParameter("titles")).split("№");
				for(int i=0;i<doclist.length;i++)
				{

					DocAggregation aggrDoc = new DocAggregation();

					aggrDoc.setAggrdocid(Integer.parseInt(request.getParameter("docid")));
					aggrDoc.setSeq(i + 1);
					aggrDoc.setIdbyaggr(Integer.parseInt(doclist[i]));
					aggrDoc.setTitle(titles[i]);
					aggrDoc.setType("1");

					flag = dmi.addAggrDoc(aggrDoc);
				}
			}
			//update 聚合文档的相关 end
			else
			{
				for(int i=0;i<doclist.length;i++)
				{
					relatedDocIds[i] = Integer.parseInt(doclist[i]);
				}
				
				DocRelated docrelated = new DocRelated();
				docrelated.setDocId(Integer.parseInt(request.getParameter("docid")));
				docrelated.setOpUserId(Integer.parseInt(userid));
				flag = dmi.creatorDocRelated(docrelated,relatedDocIds);
			}
		}
	}
	////更新文档的相关文档 end
	//更新文档的附件 start 
	//flag = dmi.deleteAttachment(Integer.parseInt(request.getParameter("docid")),2);
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
				
				attachment.setDocumentId(Integer.parseInt(request.getParameter("docid")));
				attachment.setDescription(descriptions[i]);
				attachment.setUrl(urls[i]);
				attachment.setType(2);//
				attachment.setOriginalFilename(originalFilenames[i]);

				flag = dmi.createAttachment(attachment);
			}
		}
	}
	String tempurl = (request.getParameter("url"))==null?"":(request.getParameter("url"));
	String sqlurl = "";
	if(tempurl.length()>1)
	{
		sqlurl = "'" + (tempurl.substring(0,tempurl.length()-1)).replaceAll("№","','") + "'";
	}
	else
	{
		sqlurl = "'1'";
	}
	flag = dmi.UpdateAttachmentState(Integer.parseInt(request.getParameter("docid")),2,sqlurl);
	//更新文档的附件 end
	//更新文档的图片 start 
	//flag = dmi.deleteAttachment(Integer.parseInt(request.getParameter("docid")),3);
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
				
				attachment.setDocumentId(Integer.parseInt(request.getParameter("docid")));
				attachment.setDescription(descriptions[i]);
				attachment.setUrl(urls[i]);
				attachment.setType(3);//
				attachment.setOriginalFilename(originalFilenames[i]);

				flag = dmi.createAttachment(attachment);
			}
		}
	}
	String tempurl2 = (request.getParameter("url2"))==null?"":(request.getParameter("url2"));
	String sqlurl2 = "";
	if(tempurl2.length()>1)
	{
		sqlurl2 = "'" + (tempurl2.substring(0,tempurl2.length()-1)).replaceAll("№","','") + "'";
	}
	else
	{
		sqlurl2 = "'1'";
	}
	flag = dmi.UpdateAttachmentState(Integer.parseInt(request.getParameter("docid")),3,sqlurl2);
	//更新文档的图片 end
	//save 扩展字段内容
	if(doc.getDoctype()==0)
	{
		String[] ids = (request.getParameter("fieldids")).split("№");
		String[] values = (request.getParameter("extfieldvalues")).split("№");
		String[] types = (request.getParameter("extfieldtypes")).split("№");
		CustomFormManager cf = new CustomFormManagerImpl();
		flag = cf.saveDocExtFieldValues(docid,ids,values,types);
	}
	//新增文档专题报道
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
          int[] intDocid=new int[]{Integer.parseInt(request.getParameter("docid"))};
		  dmi.copyDocs(request,intDocid,channelids,siteids,1,Integer.parseInt(userid));//1为新稿
		}
		//新增文档专题报道结束
	
	//对于已发的文档，更改状态未待发
	//（不属于正常的流程状态更改，所以没经过是否有迁移的判断，同时为所有有发布权限的人分配任务）
	//：：：：：目前只针对已发文档。
	int curStatus = dmi.getDocStatus(Integer.parseInt(docid));
	//int docType = dmi.getDocType(Integer.parseInt(docid));
	ChannelManager cmi=new ChannelManagerImpl();
	if(curStatus==5){
		List list = cmi.getPublisherList(channelId);
		int[] executers = new int[list.size()];
		for(int i=0;i<list.size();i++){
			User user =(User)list.get(i);
			executers[i] = user.getUserId().intValue();
		}
		dmi.updateDocumentStatus(Integer.parseInt(docid),11);
		dmi.addOneTask(Integer.parseInt(docid),0,11,Integer.parseInt(userid),executers);
	}
	/*if(docType != 0 && curStatus==5){
		List list = cmi.getAuditorList(channelId);
		int[] executers = new int[list.size()];
		for(int i=0;i<list.size();i++){
			User user =(User)list.get(i);
			executers[i] = user.getUserId().intValue();
		}
		dmi.updateDocumentStatus(Integer.parseInt(docid),2);
		dmi.addOneTask(Integer.parseInt(docid),0,2,Integer.parseInt(userid),executers);
	}*/
	if(b&&flag){
	   if(isdeleterelation)//修改了模板
	   {
	     RecursivePublishManagerImpl imp =new RecursivePublishManagerImpl();
	     imp.deleteDocumentDetailRelation(request.getParameter("docid"),site);
	     System.out.println("isdeleterelation-----");
	   }
	%>
	<SCRIPT LANGUAGE="JavaScript"> 
			<%if(closeFlag.equals("1")){%>
			parent.win.alert("修改文档成功!!!");
			var str = parent.window.dialogArguments.location.href;
			if(str.indexOf("?")==-1){
				parent.window.dialogArguments.location.href = parent.window.dialogArguments.location.href;
			}else{
				var strArray = str.slice(0,str.indexOf("?"));
				parent.window.dialogArguments.location.href = strArray+"?"+parent.window.dialogArguments.document.all.queryString.value;
			}
			parent.closewin();
			parent.window.close();
			<%}%>
	</script>
	<%}else{
		%>
		<SCRIPT LANGUAGE="JavaScript"> 
			parent.win.alert("修改文档失败!!!");
			var buttons = parent.document.getElementsByTagName("input");
			for(var i=0;i<buttons.length;i++)
			{
				buttons[i].disabled = false;
			}
			parent.closewin();
		</script>
	<%
	}
	%>
		
		
		
		
		
		