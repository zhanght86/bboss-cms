<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.util.StringOperate"%>
<%@ page import="com.frameworkset.platform.cms.customform.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*" %>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*" %>
<%
try{
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	Channel chnlobj=new Channel();

	
	String parentChannelId = request.getParameter("parentChannelId");
	if(parentChannelId == null || parentChannelId.trim().length()==0){
		parentChannelId = "0";
	}
	chnlobj.setParentChannelId(Integer.parseInt(parentChannelId));

	chnlobj.setSiteId(Integer.parseInt(request.getParameter("siteId")));
	
	String channelId = request.getParameter("channelId");
	if(channelId==null || channelId.trim().length()==0){
		throw new ChannelManagerException("频道id为null,无法更新频道.");
	}
	chnlobj.setChannelId(Long.parseLong(channelId));
	chnlobj.setChannel_desc(request.getParameter("channel_desc"));
	String channelName = request.getParameter("channelName");
	if(channelName==null || channelName.trim().length()==0){
		throw new ChannelManagerException("频道名字不能为空.");
	}
	chnlobj.setName(channelName);

	chnlobj.setDisplayName(request.getParameter("displayName"));

	String outlineTemplateId = request.getParameter("outlineTemplateId");
	if(outlineTemplateId!=null && outlineTemplateId.trim().length()!=0){
		System.out.println("-----------"+outlineTemplateId);
		chnlobj.setOutlineTemplateId(Integer.parseInt(outlineTemplateId));
	}

	String detailTemplateId = request.getParameter("detailTemplateId");
	if(detailTemplateId!=null && detailTemplateId.trim().length()!=0){
		chnlobj.setDetailTemplateId(Integer.parseInt(detailTemplateId));
	}

	//String commentTemplateId = request.getParameter("commentTemplateId");
	//if(commentTemplateId!=null && commentTemplateId.trim().length()!=0){
	//	chnlobj.setCommentTemplateId(Integer.parseInt(commentTemplateId));
	//}	
	String commentPagePath = request.getParameter("commentPagePath");
	if(commentPagePath!=null && commentPagePath.trim().length()!=0){
		chnlobj.setCommentPagePath(commentPagePath);
	}

	String outlineIsDynamic = request.getParameter("outlineIsDynamic");
	if(outlineIsDynamic!=null && outlineIsDynamic.trim().length()!=0){
		chnlobj.setOutlineIsDynamic(Integer.parseInt(outlineIsDynamic));
	}

	String docIsDynamic = request.getParameter("docIsDynamic");
	if(docIsDynamic!=null && docIsDynamic.trim().length()!=0){
		chnlobj.setDocIsDynamic(Integer.parseInt(docIsDynamic));
	}


	String outlineIsProtect = request.getParameter("outlineIsProtect");
	if(outlineIsProtect!=null && outlineIsProtect.trim().length()!=0){
		chnlobj.setOutlineIsProtect(Integer.parseInt(outlineIsProtect));
	}

	String docIsProtect = request.getParameter("docIsProtect");
	if(docIsProtect!=null && docIsProtect.trim().length()!=0){
		chnlobj.setDocIsProtect(Integer.parseInt(docIsProtect));
	}

	String order = request.getParameter("order");
	if(order!=null && order.trim().length()!=0){
		chnlobj.setOrderNo(Integer.parseInt(order));
	}
	
	String outlinePicture = request.getParameter("outlinePicture");
	if(order!=null && order.trim().length()!=0){
		chnlobj.setOutlinepicture(outlinePicture);
	}
	
	String isnavigator = request.getParameter("isnavigator");
	boolean isNav = "1".equals(isnavigator);
	chnlobj.setNavigator(isNav);
	if(isNav){
		String navigatorlevel = request.getParameter("navigatorlevel");
		if(navigatorlevel!=null && navigatorlevel.trim().length()!=0){
			chnlobj.setNavigatorLevel(Integer.parseInt(navigatorlevel));
		}
		String mouseInImage = request.getParameter("mouseInImage");
		if(mouseInImage!=null && mouseInImage.trim().length()!=0){
			chnlobj.setMouseInImage(mouseInImage);
		}
		String mouseOutImage = request.getParameter("mouseOutImage");
		if(mouseOutImage!=null && mouseOutImage.trim().length()!=0){
			chnlobj.setMouseOutImage(mouseOutImage);
		}
		String mouseClickImage = request.getParameter("mouseClickImage");
		if(mouseClickImage!=null && mouseClickImage.trim().length()!=0){
			chnlobj.setMouseClickImage(mouseClickImage);
		}
		String mouseUpImage = request.getParameter("mouseUpImage");
		if(mouseUpImage!=null && mouseUpImage.trim().length()!=0){
			chnlobj.setMouseUpImage(mouseUpImage);
		}								
	}
	
	String navigatorlevel = request.getParameter("navigatorlevel");
	if(navigatorlevel!=null && navigatorlevel.trim().length()!=0){
		chnlobj.setNavigatorLevel(Integer.parseInt(navigatorlevel));
	}

	String workFlowFrom = request.getParameter("workFlowFrom");
	if ("1".equals(workFlowFrom)) {
		chnlobj.setWorkflowIsFromParent(1);
		chnlobj.setWorkflow(0);
	} else {
		chnlobj.setWorkflowIsFromParent(0);
		String workflow = request.getParameter("workFlow");
		if (workflow != null && !"".equals(workflow)) {
			chnlobj.setWorkflow(Integer.parseInt(workflow));
			chnlobj.setWorkflowIsFromParent(0);
		}else{
			chnlobj.setWorkflow(0);
			chnlobj.setWorkflowIsFromParent(1);		
		}
	}

	//更新自定义表单 add by jxw 2007-3-20
	CustomFormManager cm = new CustomFormManagerImpl();
	cm.updateCustomFormSet(channelId,"2","1",request.getParameter("add"));
	cm.updateCustomFormSet(channelId,"2","2",request.getParameter("update"));
	cm.updateCustomFormSet(channelId,"2","3",request.getParameter("list"));
	
	//发布首页
	String chnlIndexPage = request.getParameter("chnlIndexPage");
	String docId = request.getParameter("docId");
	String chnlId = request.getParameter("chnlId");
	String pubType = request.getParameter("pubType");
	if(pubType!=null && pubType.length()>0){
		chnlobj.setPageflag(Integer.parseInt(pubType));
		if("0".equals(pubType)){			//为o则按文件名发布
			String pubFileName = request.getParameter("pubFileName");
			if(pubFileName!=null && pubFileName.length()>0){
				String[] pubFileNames =  StringOperate.getFileNameAndExtName(pubFileName);
				chnlobj.setPubFileName(pubFileName);
				chnlobj.setPubFileNameSuffix(pubFileNames[1]);
				//System.out.println(pubFileNames[0]);
				//System.out.println(pubFileNames[1]);
			}
		}
		if("1".equals(pubType)){			//为1则按首页发布
			if(chnlIndexPage!=null && chnlIndexPage.length()>0)
				chnlobj.setIndexpagepath(chnlIndexPage);
		}
		if("2".equals(pubType)){			//为2则按文档发布，但是文档和首页共用同一个频道属性chnlIndexPage
			if(docId!=null && docId.length()>0)
				chnlobj.setIndexpagepath(docId);
		}
		if("3".equals(pubType)){			//为3则按频道首页发布，indexpagepath存的是站点id：频道id
			if(chnlId!=null && chnlId.length()>0)
				chnlobj.setIndexpagepath(chnlId);
		}
	}
	
	ChannelManager smi=new ChannelManagerImpl();
	SiteManager sm = new SiteManagerImpl();
	Site site = sm.getSiteInfo(request.getParameter("siteId")) ;
	Channel orachannel=smi.getChannelInfo(channelId);
	int oraOutTemplateId=orachannel.getOutlineTemplateId();
	int oraDetailTemplateId=orachannel.getDetailTemplateId();
	if(request.getParameter("specialflag")==null)
	{
	chnlobj.setSpecialflag(0);
	}
	else
	{
	chnlobj.setSpecialflag(1);
	}
	smi.logUpdateChannel(chnlobj,request,response);
	 RecursivePublishManagerImpl imp =new RecursivePublishManagerImpl();
	//修改概览模板
	if(outlineTemplateId!=null && outlineTemplateId!="")//没有修改模板
	{
	  if(oraOutTemplateId!=0 && oraOutTemplateId!=Integer.parseInt(outlineTemplateId))
	  {
	    imp.deleteChannelOutRelation(channelId,site);
	  }
	}
	//修改细览模板	
	if(detailTemplateId!=null && detailTemplateId!="")//没有修改模板
	{
	  if(oraDetailTemplateId!=0 && oraDetailTemplateId!=Integer.parseInt(detailTemplateId))
	  {
	    imp.deleteChannelDetailRelation(channelId,site);
	  }
	}
%>
	<script language="javascript">
		alert("编辑频道成功!");
		if(parent.window.document.forms[0].odisplayName.value != parent.window.document.forms[0].displayName.value)
			parent.window.returnValue = true;//用于判断是否要刷新
		if(parent.window.document.forms[0].oorder.value != parent.window.document.forms[0].order.value)
			parent.window.returnValue = true;//用于判断是否要刷新
		parent.close();
		</script>
<%	}catch(ChannelManagerException e){%>
		<script language="javascript">
		alert('编辑频道出错,请检查!原因是:\n<%=e.getMessage().replaceAll("\n","").replaceAll("'","")%>');
		//防止二次提交
		
		parent.window.document.all("button")[0].disabled = false;
		parent.window.document.all("button")[1].disabled = false;
		</script>
<%}%>
