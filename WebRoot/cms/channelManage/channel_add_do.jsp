<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.util.StringOperate"%>
<%@ page import="com.frameworkset.platform.cms.customform.*"%>
<%@ page import="java.util.*"%>
<%
try{
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);
	Channel chnlobj=new Channel();

	String parentChannelId = request.getParameter("parentChannelId");
	if(parentChannelId!=null && parentChannelId.trim().length()!=0){
		chnlobj.setParentChannelId(Long.parseLong(parentChannelId));
	}
	String userId = control.getUserID();
	if(userId == null || userId.trim().length()==0){
		throw new ChannelManagerException("创建者不能为空.");
	}
	chnlobj.setCreateUser(Long.parseLong(userId));

	String siteId = request.getParameter("siteId");
	if(siteId==null || siteId.trim().length()==0){
		throw new ChannelManagerException("频道不能不属于任何站点.");
	}
	chnlobj.setSiteId(Long.parseLong(siteId));

	String channelName = request.getParameter("channelName");
	if(channelName==null || channelName.trim().length()==0){
		throw new ChannelManagerException("频道名字不能为空.");
	}
	chnlobj.setName(channelName);
	chnlobj.setChannel_desc(request.getParameter("channel_desc"));
	chnlobj.setDisplayName(request.getParameter("displayName"));

	String channelPath = request.getParameter("channelPath");
	if(channelPath==null || channelPath.trim().length()==0){
		throw new ChannelManagerException("频道路径不能为空.");
	}
	chnlobj.setChannelPath(channelPath);

	String outlineTemplateId = request.getParameter("outlineTemplateId");
	if(outlineTemplateId!=null && outlineTemplateId.trim().length()!=0){
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
	
	
	chnlobj.setPath(application.getRealPath("/"));


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

	ChannelManager smi=new ChannelManagerImpl();
	//自定义表单信息add by jxw 2007-3-20
	CustomForm cf1 = new CustomForm();
	cf1.setTargetType("2");
	cf1.setFormType("1");
	cf1.setFileName("".equals(request.getParameter("add"))?"doc_gather.jsp":request.getParameter("add"));

	CustomForm cf2 = new CustomForm();
	cf2.setTargetType("2");
	cf2.setFormType("2");
	cf2.setFileName("".equals(request.getParameter("update"))?"doc_gather_update.jsp":request.getParameter("update"));

	CustomForm cf3 = new CustomForm();
	cf3.setTargetType("2");
	cf3.setFormType("3");
	cf3.setFileName("".equals(request.getParameter("list"))?"doc_list.jsp":request.getParameter("list"));

	List list = new ArrayList();
	list.add(cf1);list.add(cf2);list.add(cf3);
	chnlobj.setCustomforms(list);
	//end  自定义表单信息

	String pubType = request.getParameter("pubType");
	if(pubType!=null && pubType.length()>0){
		chnlobj.setPageflag(Integer.parseInt(pubType));
		if("0".equals(pubType)){			//为o则按文件名发布
			String pubFileName = request.getParameter("pubFileName");
			if(pubFileName!=null && !"".equals(pubFileName)){
				String[] pubFileNames =  StringOperate.getFileNameAndExtName(pubFileName);
				chnlobj.setPubFileName(pubFileName);
				chnlobj.setPubFileNameSuffix(pubFileNames[1]);
			}
		}
		if("1".equals(pubType)){			//为1则按首页发布
			//发布首页
			String chnlIndexPage = request.getParameter("chnlIndexPage");
			if(chnlIndexPage!=null && chnlIndexPage.length()>0)
				chnlobj.setIndexpagepath(chnlIndexPage);
		}
		if("2".equals(pubType)){			//为2则按文档发布，但是文档和首页共用同一个频道属性chnlIndexPage
			String docId = request.getParameter("docId");
			if(docId!=null && docId.length()>0)
				chnlobj.setIndexpagepath(docId);
		}
		if("3".equals(pubType)){			//为3则按频道首页发布，indexpagepath存的是站点id：频道id
			String chnlId = request.getParameter("chnlId");
			if(chnlId!=null && chnlId.length()>0)
				chnlobj.setIndexpagepath(chnlId);
		}
	}
	if(request.getParameter("specialflag")==null)
	{
	chnlobj.setSpecialflag(0);
	}
	else
	{
	chnlobj.setSpecialflag(1);
	}
	smi.logCreateChannel(chnlobj,request,response);
%>
	<script language="javascript">
	
		alert("新建频道成功!");
		parent.window.returnValue = true;
		parent.close();
		</script>
<%	}catch(ChannelManagerException e){%>
		<script language="javascript">
		alert('新建频道出错,请检查!原因是:\n<%=e.getMessage().replaceAll("\n","").replaceAll("'","")%>');
		//防止二次提交
		if(parent.window.document.all("button")[0]) parent.window.document.all("button")[0].disabled = false;
		if(parent.window.document.all("button")[1]) parent.window.document.all("button")[1].disabled = false;
		</script>
<%}%>
