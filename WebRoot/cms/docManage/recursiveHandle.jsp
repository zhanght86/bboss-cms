<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%
/*
	说明参数：
	递归发布分四种情况，1、递归发布单个文档；2、递归发布批量文档；3、递归发布频道；4、重新发布文档（设置重新发布自动递归）
	1、2、3都必须传参数siteId，action
	1必须传参数docId
	2必须传参数docs，因为docs为一个list对象，所以通过session传
	3必须传参数channelId
	4必须传参数docInfos，因为docInfos是个二维数组，所以通过session传
*/
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	
	String siteId = request.getParameter("siteId");
	String action = request.getParameter("action");
	
	String docId = request.getParameter("docId");
	
	String channelId = request.getParameter("channelId");
	
	String flag = request.getParameter("flag");
	
	Object o = session.getAttribute("docs");
	session.removeAttribute("docs");
	List docs = null;
	if(o != null)
	 	docs = (List)o;
	
	Object oDocInfos = session.getAttribute("docInfos");
	session.removeAttribute("docInfos");
	String[][] docInfos = null;
	if(oDocInfos != null)
	 	docInfos = (String[][])oDocInfos;
	
	DocumentManager idm = new DocumentManagerImpl();
	
	WEBPublish publish = new WEBPublish();
	publish.init(request,response,pageContext,accesscontroler);
	if(docInfos != null)
	{
		//在发布文档时，设置强制执行已发布文档的递归发布标识为true			
		publish.forceStatusPublished(true);
	}
	PublishCallBack callback = new PublishCallBackImpl();
	publish.setPublishCallBack(callback);
	if(flag!=null&&flag.equals("channel"))
	{
		publish.recursivePubObjectOfChannelAncester(siteId,channelId,Integer.parseInt(action));
	}
	else if(docId != null && !docId.equals(""))	//递归发布单个文档
	{
		publish.recursivePubObjectOfDocument(siteId,idm.getPartDocInfoById(docId+""),Integer.parseInt(action));
	}
	else if(docs != null)					//递归发布批量文档
	{
		publish.recursivePubObjectOfDocuments(siteId,docs,Integer.parseInt(action));
	}
	else if(channelId != null)				//递归发布频道
	{
		publish.recursivePubObjectOfChannelAncester(siteId,channelId,Integer.parseInt(action));
	}
	else if(docInfos != null)				//重新发布文档。在回收文档恢复时，重新发布文档		
	{
		publish.publishBatchDocument(docInfos,new boolean[] {true,false},new int[] {0});
	}
%>