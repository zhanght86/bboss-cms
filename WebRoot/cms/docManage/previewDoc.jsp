<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*"%>
<%@page import="com.frameworkset.platform.cms.docsourcemanager.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	
	String dprior = request.getParameter("documentprior");
	Document doc= new Document();

	ChannelManager cm = new ChannelManagerImpl();
	
	String siteId= request.getParameter("siteid");
	String chnlId = request.getParameter("channelId");

	doc.setChanel_id(Integer.parseInt(request.getParameter("channelId")));
	doc.setUser_id(Integer.parseInt(request.getParameter("userid")));
	doc.setCreateUser(Integer.parseInt(request.getParameter("userid")));
	doc.setTitle(request.getParameter("title"));
	doc.setTitlecolor(request.getParameter("titlecolor"));
	doc.setSubtitle(request.getParameter("subtitle"));
	doc.setPicPath(request.getParameter("picpath"));
	doc.setMediapath(request.getParameter("mediapath"));
    doc.setPublishfilename(request.getParameter("publishfilename"));
    doc.setDoc_class(request.getParameter("doc_class"));
    String isnewdocsource=request.getParameter("isnewdocsource");
   
    if(isnewdocsource.equals("1"))//判定是否新的稿源
    { 
       //新的稿源名称
       String docsourceName=request.getParameter("inputdocsource");
       //获取稿源ID
       Docsource docsource=new Docsource();
       docsource.setSRCNAME(docsourceName);
       DocsourceManagerImpl docsourcemanager=new DocsourceManagerImpl();
       boolean createflag=false;
       createflag=docsourcemanager.creatorDsrc(docsource);
       if(createflag)
       {
      
         docsource=docsourcemanager.getDsrcListBy(docsourceName);
         doc.setDocsource_id(docsource.getDOCSOURCE_ID());
       }
       else
       {
          doc.setDocsource_id(0);
       }
    }
    else{
	if(request.getParameter("docsource_id")==null){
		doc.setDocsource_id(0);
	}else{
		doc.setDocsource_id(Integer.parseInt(request.getParameter("docsource_id")));
	}
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
		doc.setDetailtemplate_id(0);
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
	doc.setDoctype(Integer.parseInt(request.getParameter("doctype")));
	doc.setLinktarget(request.getParameter("linktarget"));
	doc.setDoc_level(Integer.parseInt(request.getParameter("doc_level")));//文档级别
	doc.setContent(request.getParameter("content")==null?"":request.getParameter("content"));
	//String[] ids = (request.getParameter("fieldids")).split("№");
	String[] values = (request.getParameter("extfieldvalues")).split("№");
	//String[] types = (request.getParameter("extfieldtypes")).split("№");
	String[] names = (request.getParameter("extfieldnames")).split("№");
	Map map = new HashMap();
	for(int i=0;i<names.length;i++)
	{
		map.put(names[i],values[i]);
	}
	doc.setDocExtField(map);System.out.println(cm.hasSetDetailTemplate(chnlId));
	if(cm.hasSetDetailTemplate(chnlId) && (Integer.parseInt(request.getParameter("doctype"))==0 || Integer.parseInt(request.getParameter("doctype"))==1)){		
		WEBPublish publish = new WEBPublish();
		publish.init(request,response,pageContext,accesscontroler);
		PublishCallBack callback = new PublishCallBackImpl();
		publish.setPublishCallBack(callback);

		publish.viewDocument(siteId,chnlId,doc);
		String viewUrl = callback.getViewUrl();
		if(viewUrl!=null){
			viewUrl = request.getContextPath() + "/" + viewUrl;
		%>
			<script language="javascript">
				try
				{
					var buttons = parent.document.getElementsByTagName("input");
					for(var i=0;i<buttons.length;i++)
					{
						buttons[i].disabled = false;
					}
					parent.closewin();
					window.open("<%=viewUrl%>");
				}
				catch(e)
				{}
			</script>
		<%
		}else{
		%>
			<script language="javascript">
				parent.win.alert("预览失败，可能当前频道未设置细览模板或者模板文件不存在！！");
				var buttons = parent.document.getElementsByTagName("input");
				for(var i=0;i<buttons.length;i++)
				{
					buttons[i].disabled = false;
				}
				parent.closewin();
			</script>
		<%
		}
	}else{
	%>
		<script language="javascript">
			parent.win.alert("预览失败，可能当前频道未设置细览模板、模板文件不存在或该文件无需发布！！");
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
