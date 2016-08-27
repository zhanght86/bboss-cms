<%
	/**
	  * 站内文档查询
	  */
%>
<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ include file="/sysmanager/include/global1.jsp"%>
<%@ include file="/sysmanager/base/scripts/panes.jsp"%>
<%@page import="com.frameworkset.platform.cms.util.CMSUtil"%>
<%@ page import="com.frameworkset.platform.cms.CMSManager"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.customform.*"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuTag"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenu"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuImpl"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.Menu"%>
<%@ page import="com.frameworkset.platform.cms.util.CMSUtil,com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

	CMSManager cmsManager = new CMSManager();
	cmsManager.init(request,session,accesscontroler);
	Site site = cmsManager.getCurrentSite();
	
	//外部注入发布管理
    String uuid = CMSUtil.getUUID();
    PublishMonitor monitor = PublishMonitor.createPublishMonitor();
    session.setAttribute(uuid,monitor);

	//String channelName = request.getParameter("channelName");
	String siteid = site.getSiteId() + "";//request.getParameter("siteid");
	request.setAttribute("siteid",siteid);
	//String channelId = request.getParameter("channelId");
	String flag = request.getParameter("flag");
	//SiteManager siteManager = new SiteManagerImpl();
	String sitename = site.getName();;//siteManager.getSiteInfo(siteid).getName();

	DocumentManager docManager = new DocumentManagerImpl();
	
	String docsorid = request.getParameter("docsorid");
	String title = request.getParameter("title");
	String status = request.getParameter("status");
	String userid = request.getParameter("userid");
	String doctype = request.getParameter("doctype");
	String docLevel = request.getParameter("docLevel");
	String author = request.getParameter("author");
	String docAbstract = request.getParameter("docAbstract");
	String keyword = request.getParameter("keyword");
	String createTimeBegin = request.getParameter("createTimeBegin");
	String createTimeEnd = request.getParameter("createTimeEnd");
	String content = request.getParameter("content");
	
	//取频道所设定的自定义表单
	CustomFormManager cfm = new CustomFormManagerImpl();
	
	
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="<%=request.getContextPath()%>/cms/inc/css/cms.css" rel="stylesheet" type="text/css">
		<title>内容管理主框架</title>
		<script src="${pageContext.request.contextPath}/include/jquery-1.4.2.min.js"></script>
		<script src="${pageContext.request.contextPath}/include/security.js"></script>
		<script src="<%=request.getContextPath()%>/cms/inc/js/func.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/include/pager.js" type="text/javascript"></script>
		<script language="javascript"> 	
	
	
	
	function subforms()
	{
		form1.submit();
	}
	
	//文档查看
	function viewDocInfo(docId,doctype)
	{
		if(doctype == 0)//普通
			openWin("<%=rootpath%>/cms/docManage/doc_view.jsp?docid=" + docId,screen.availWidth-20,screen.availHeight-50);
		if(doctype == 1)//链接文档
			openWin("<%=rootpath%>/cms/docManage/doc_view_links.jsp?docid=" + docId,screen.availWidth-20,screen.availHeight-50);
		if(doctype == 2)//下载文档
			openWin("<%=rootpath%>/cms/docManage/doc_view.jsp?docid=" + docId,screen.availWidth-20,screen.availHeight-50);
		if(doctype == 3)//聚合文档
			openWin("<%=rootpath%>/cms/docManage/doc_view_aggr.jsp?docid=" + docId,screen.availWidth-20,screen.availHeight-50);
	}
	//发布预览
	function publishPreview(docId){
		//form1.action = "<%=request.getContextPath()%>/cms/docManage/previewPubDocHandle.jsp?docId="+docId;
		//form1.target = "operIframe";
		//form1.submit();
		
		$.secutiry.dosubmit("form1",
						"<%=request.getContextPath()%>/cms/docManage/previewPubDocHandle.jsp?docId="+docId,
								"operIframe",
								"<%=rootpath%>");
	}
	//文档浏览
    function docUrlView(siteid,channelid,docId){
    //alert("siteid:" + siteid);
      //  alert("channelid:" + channelid);
            //alert("docId:" + docId);
           
		
       window.open("<%=rootpath%>/cms/docManage/doc_urlview.jsp?docid=" + docId + "&siteid=" + siteid + "&channelid=" + channelid,"_blank");
    }
     //保存当前页面URL 以便刷新
    function currentPageUrl(){
        var str = document.location.href;
        var end = str.indexOf("?");
        var strArray;
        if(end != -1)
            strArray= str.slice(0,end);
        else
            strArray = str; 
       document.all("url").value = strArray+"?" + document.all.queryString.value;
    }
    //单个文档发布
    var winOpen;
	function publishDoc(docId,docTypeFlag){
	
		var flag = 3;
		//if(docTypeFlag!=0)
		//	flag = 51
		form1.action = "<%=request.getContextPath()%>/cms/docManage/publishDocHandle.jsp?flag="+flag+"&docId="+docId;
		form1.target = "operIframe";
        currentPageUrl();
		//form1.submit();
        var path = "doc_publish_info.jsp?uuid=<%=uuid%>&flag="+flag+"&docId="+docId;
        var featrue = "dialogHeight:310px;dialogWidth:450px";
        winOpen = window.showModelessDialog(path,window,featrue);
		//document.all.divProcessing.style.display="";
	}
	//撤销发布
	function withdrawPublish(docId,siteid){
		form1.action = "<%=request.getContextPath()%>/cms/docManage/withdrawPublishHandle.jsp?siteId="+siteid+"&docId="+docId;
		form1.target = "operIframe";
		form1.submit();
	}
	//送审、提交发布、归档、撤销送审操作，传来的参数分别为送审、提交发布、归档、撤销送审(针对单个文档操作)
	function operateDoc(operType,docId,siteid,channelName,channelId){
		var tagetJsp = "<%=request.getContextPath()%>/cms/docManage/operate_doc.jsp?siteid="+siteid+"&channelName="+channelName+"&channelId="+channelId+"&operType="+operType+"&docId="+docId;	    
	    form1.action=tagetJsp;
	    form1.target="operIframe";
		form1.submit();
	}
	function audit(docId,docTpye,siteid,channelName,channelId){
		var taskidStr = ":" + docId + ":"+channelId+"";
		if(docTpye==0){
			openWin("<%=request.getContextPath()%>/cms/docManage/doc_gather_update.jsp?flag=audit&siteid="+siteid+"&channelName="+channelName+"&channelId="+channelId+"&docid=" + docId + "&taskidStr=" + taskidStr,screen.availWidth-20,screen.availHeight-50);
		}else if(docTpye==1){
			openWin("<%=request.getContextPath()%>/cms/docManage/doc_gather_links_update.jsp?flag=audit&siteid="+siteid+"&channelName="+channelName+"&channelId="+channelId+"&docid=" + docId + "&taskidStr=" + taskidStr,screen.availWidth-20,screen.availHeight-50);
		}else if(docTpye==2){
			openWin("<%=request.getContextPath()%>/cms/docManage/doc_gather_filedownload_update?flag=audit&siteid="+siteid+"&channelName="+channelName+"&channelId="+channelId+"&docid=" + docId + "&taskidStr=" + taskidStr,screen.availWidth-20,screen.availHeight-50);
		}else if(docTpye==3){
			openWin("<%=request.getContextPath()%>/cms/docManage/doc_gather_aggr_update.jsp?flag=audit&siteid="+siteid+"&channelName="+channelName+"&channelId="+channelId+"&docid=" + docId + "&taskidStr=" + taskidStr,screen.availWidth-20,screen.availHeight-50);
		}
	}
	//版本管理
	function manageDocVer(docid)
	{
		//打开模态窗口选择目的频道
		openWin("<%=rootpath%>/cms/docManage/dilogparent.jsp?docid=" + docid,800,650);
	}
	//设置置顶
	function addArrangeDoc(docid,siteid,channelId)
	{
		openWin("<%=rootpath%>/cms/docManage/docArrange_add.jsp?siteid="+siteid+"&channelid="+channelId+"&docid=" + docid,555,240);
	}
	//取消置顶
	function delDocArrange(docid,siteid,channelId)
	{
		if(window.confirm("您确定要取消该文档的置顶吗？"))
		{
			document.forms[0].action = "<%=rootpath%>/cms/docManage/docArrange_del_submit.jsp?siteid="+siteid+"&channelid="+channelId+"&docid=" + docid;
			document.forms[0].target="operIframe";
			document.forms[0].submit();
		}
	}
	//文档置顶管理
	function arrangeDocM(channelId)
	{
		openWin("<%=rootpath%>/cms/docManage/docArrange_manage.jsp?chnlid="+channelId+"",800,500);
	}
	/**
	 * 对文档排序
	 * docid 文档id
	 * docname 文档subtitle
	 */
	function orderDoc(docId,docName){
	    var win = openWin("update_ordertime.jsp?docId="+docId+"&docName="+docName,320,200);
	    if(win=="ok"){
	        window.location.href = window.location; 
	    }
	
	}
	function manageDocComment(docid){
		//打开模态窗口，并文档评论进行查看管理
		openWin("<%=rootpath%>/cms/docManage/doc_comment_manager.jsp?docId=" + docid,800,700);
	}
	//将处于假发布中的文档恢复为发布前的状态
	function recover(docId){
		form1.action = "<%=rootpath%>/cms/docManage/docPublishingHandle.jsp?action=recover&docId=" + docId;
		form1.target="operIframe";
		form1.submit();
	}
	//重发
	function publishAgain(docId){
		form1.action = "<%=rootpath%>/cms/docManage/publishDocHandle.jsp?flag=13&docId=" + docId;
		form1.target="operIframe";
		form1.submit();
	}
	function seeChnlOfDocCited(docId,docName){
		var url = "<%=rootpath%>/cms/docManage/see_chnl_docCited.jsp?docId="+docId+"&docName="+docName;
		openWin(url,750,550);
	}
	function closeSubWindow(){ 
        winOpen.close();
        document.all("cleanSession").src = "../cleanSession.jsp?uuid=<%=uuid%>";
   
    }
    
    //调用发布主页面的函数
    //url 为空的时候 直接alert(msg)
    //url 不为空的时候 confirm()选择msg
    function alertFun(msg,url){
        winOpen.alertMsg(msg,url);
    }
</script>
		<style type="text/css">
		body {
			background-color: #ffffff;
		}
		.STYLE1 {color: #0000FF}
		.STYLE2 {color: #000099}
		.style3 {
			font-size: 14px;
			font-weight: bold;
			color: #3300FF;
		}
		.operStyle{ display:none;
		width:17;
		height:16;
		}

</style>
	</head>

	<body topmargin="1" rightmargin="1" scroll=auto leftmargin="1">
		<div id="divProcessing" style="position:absolute;width:40%;height:20px;z-index:1;
			background-color:#F0f0f0;layer-background-color:#F0f0f0;left:22%;top:80%;display:none;"  class="font">   
	    <marquee direction="left" width="250" behavior="alternate"><span class=STYLE1>正在发布中，请稍等……</span></marquee>
    	</div>
		<form name="form1" action="" method="post">
		<input type="hidden" name="url">
		<input type="hidden" name="isRecordValue">
		<input type="hidden" name="recursionPublish">
        <input type="hidden" name="clearCache">
		<input type="hidden" name="uuid" value="<%=uuid%>">
		<input type="hidden" name="expzipname" value=""><!-- 指定文档导出name-->
			<table width="100%" border="1" align=center cellpadding="3" cellspacing="0" bordercolor="#B7CBE4"  class="Datalisttable" id="docListTable">
				<pg:listdata dataInfo="SiteDocumentList" keyName="SiteDocumentList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="20" scope="request" data="SiteDocumentList" isList="false">
					<tr class="cms_report_tr">
						<!--设置分页表头-->

						<td width="2%" align=center style="width:5%"><input class="checkbox" type="checkBox" hidefocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')"></td>
						<td width="8%">
							文档类型						</td>
						<td width="34%">
							文档标题</td>
						<td width="12%">
							所属频道</td>
						<td width="6%">
							状态						</td>
						<td width="9%">
							发稿人						</td>
						<td width="8%">
							作者						</td>
						<td width="19%">
							发稿日期						</td>
						<!--<td width="10%" height='30' nowrap="nowrap">
							操作
						</td>-->
					</tr>
				
					<pg:param name="channelId" />
					<pg:param name="channelName" />
					<pg:param name="siteid" />
					<pg:param name="flag" />
					<pg:param name="title" />
					<pg:param name="status" />
					<pg:param name="userid" />
					<pg:param name="doctype" />
					<pg:param name="createTimeBegin" />
					<pg:param name="createTimeEnd" />
					<pg:param name="docsorid" />
					<pg:param name="keyword" />
					<pg:param name="author" />
					<pg:param name="docAbstract" />
					<pg:param name="content" />
					<pg:param name="docLevel" />
					<!--检测当前页面是否有记录-->
					<pg:notify>
						<tr  class="labeltable_middle_tr_01">
							<td colspan=100 align='center' height="18px">
								请输入相关条件进行查询							</td>
						</tr>
					</pg:notify>

					<!--list标签循环输出每条记录-->
					<%
						ContextMenu contextmenu = new ContextMenuImpl();
					%>
					<pg:list>
					<%
						String channelId = dataSet.getString("chanel_id");
						
						String channelName = dataSet.getString("channelName");
						String addpath = cfm.getCustomFormFilename(channelId,"2","1");
						String updatepath = cfm.getCustomFormFilename(channelId,"2","2");
						if("doc_gather.jsp".equals(addpath)||"".equals(addpath))
							addpath = "/cms/docManage/doc_gather.jsp";
						else
							addpath = addpath;
						if("doc_gather_update.jsp".equals(updatepath)||"".equals(updatepath))
							updatepath = "/cms/docManage/doc_gather_update.jsp";
						else
							updatepath = updatepath;
							
						int docTypeFlag = dataSet.getInt("doctype");
						int docStatusFlag = dataSet.getInt("status");
						int docId = dataSet.getInt("document_id");
						
						//int docTypeFlag = dataSet.getInt("doctype");
						//int docStatusFlag = dataSet.getInt("status");
											
						String docName = dataSet.getString("subtitle");
						boolean isArrangeDoc = dataSet.getBoolean("arrangeDoc");//docManager.ArrangeDocExist(String.valueOf(docId));//是否是置顶
						boolean isArrangeOverTime = dataSet.getBoolean("arrangeOverTime");
						ChannelManager cm = new ChannelManagerImpl();
						Map docOpers = docManager.getDocOpers(docId,accesscontroler);

						Menu menu = new Menu();
						menu.setIdentity("opdoc_" + docId);
                        
                        
						
						if(docOpers.get("pubPrevie")!=null && docStatusFlag!=10)
						{
							Menu.ContextMenuItem menuitem1 = new Menu.ContextMenuItem();
							menuitem1.setName("发布预览");
							menuitem1.setLink("javascript:publishPreview('" + docId + "')");
							menuitem1.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_fbyl.gif");
							menu.addContextMenuItem(menuitem1);
						}
						
						Menu.ContextMenuItem menuitem11 = new Menu.ContextMenuItem();
						menuitem11.setName("文档浏览");
						menuitem11.setLink("javascript:docUrlView('" + siteid + "','" + channelId + "','" + docId + "')");
						menuitem11.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_fbyl.gif");
						menu.addContextMenuItem(menuitem11);

						if(docOpers.get("deliver")!=null)
						{
							Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
							menuitem.setName("送审");
							menuitem.setLink("javascript:operateDoc('送审','" + docId + "','"+siteid+"','"+channelName+"','"+channelId+"')");
							menuitem.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_ss.gif");
							menu.addContextMenuItem(menuitem);
						}
						if(docOpers.get("withdrawdeliver")!=null)
						{
							Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
							menuitem.setName("撤销送审");
							menuitem.setLink("javascript:operateDoc('撤销送审','" + docId + "','"+siteid+"','"+channelName+"','"+channelId+"')");
							menuitem.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_cxss.gif");
							menu.addContextMenuItem(menuitem);
						}
						if(docOpers.get("audit")!=null)
						{
							Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
							menuitem.setName("审核");
							menuitem.setLink("javascript:audit('" + docId + "','" +docTypeFlag + "','"+siteid+"','"+channelName+"','"+channelId+"')");
							menuitem.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_sh.gif");
							menu.addContextMenuItem(menuitem);
						}
						if(docOpers.get("subpublish")!=null)
						{
							Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
							menuitem.setName("提交发布");
							menuitem.setLink("javascript:operateDoc('提交发布','" + docId + "','"+siteid+"','"+channelName+"','"+channelId+"')");
							menuitem.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_tjfb.gif");
							menu.addContextMenuItem(menuitem);
						}
						if(docOpers.get("publish")!=null)
						{
							Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
							menuitem.setName("发布");
							menuitem.setLink("javascript:publishDoc('" + docId + "','" + docTypeFlag + "')");
							menuitem.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_fb.gif");
							menu.addContextMenuItem(menuitem);
						}
						if(docOpers.get("withdrawPublish")!=null)  
						{
							Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
							menuitem.setName("撤销发布");
							menuitem.setLink("javascript:withdrawPublish('" + docId + "')");
							menuitem.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_fb.gif");
							menu.addContextMenuItem(menuitem);
						}
						if(docOpers.get("archive")!=null)
						{
							Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
							menuitem.setName("归档");
							menuitem.setLink("javascript:operateDoc('归档','" + docId + "','"+siteid+"','"+channelName+"','"+channelId+"')");
							menuitem.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_gd.gif");
							menu.addContextMenuItem(menuitem);
						}
						if(docOpers.get("manageDocVer")!=null)
						{
							Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
							menuitem.setName("版本管理");
							menuitem.setLink("javascript:manageDocVer('" + docId + "')");
							menuitem.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_bbgl.gif");
							menu.addContextMenuItem(menuitem);
						}
						if(docOpers.get("addArrangeDoc")!=null && (docStatusFlag!=7))
						{//docStatusFlag=7为归档文档，不能设置置顶标记
							if(!isArrangeDoc)
							{
								Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
								menuitem.setName("设置置顶");
								menuitem.setLink("javascript:addArrangeDoc('" + docId + "','"+siteid+"','"+channelId+"')");
								menuitem.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_szzd.gif");
								menu.addContextMenuItem(menuitem);
							}
							else
							{
								Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
								menuitem.setName("取消置顶");
								menuitem.setLink("javascript:delDocArrange('" + docId + "','"+siteid+"','"+channelId+"')");
								menuitem.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_szzd.gif");
								menu.addContextMenuItem(menuitem);
							}
						}
						
						//排序
						Menu.ContextMenuItem menuitem13 = new Menu.ContextMenuItem();
						menuitem13.setName("文档排序");
						menuitem13.setLink("javascript:orderDoc('" + docId + "','" + docName + "')");
						menuitem13.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_szzd.gif");
						menu.addContextMenuItem(menuitem13);
						
						if(docOpers.get("manageDocCommnet")!=null)
						{
							Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
							menuitem.setName("评论管理");
							menuitem.setLink("javascript:manageDocComment('" + docId + "')");
							menuitem.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_bbgl.gif");
							menu.addContextMenuItem(menuitem);
						}
						//发布中的文档可以有恢复的功能，没加权限控制
						if(docStatusFlag==10)
						{
							Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
							menuitem.setName("恢复");
							menuitem.setLink("javascript:recover('" + docId + "')");
							menuitem.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_bbgl.gif");
							menu.addContextMenuItem(menuitem);
						}
						//发布中的文档可以有重发功能，
						if((accesscontroler.checkPermission(channelId,AccessControl.DOCPUBLISH_PERMISSION,AccessControl.CHANNELDOC_RESOURCE)||
				 			accesscontroler.checkPermission(siteid,AccessControl.DOCPUBLISH_PERMISSION,AccessControl.SITEDOC_RESOURCE)) && 
				 			docStatusFlag==10)
						{
							Menu.ContextMenuItem menuitem3 = new Menu.ContextMenuItem();
							menuitem3.setName("重发");
							menuitem3.setLink("javascript:publishAgain('" + docId + "')");
							menuitem3.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_bbgl.gif");
							menu.addContextMenuItem(menuitem3);
						}
						
						Menu.ContextMenuItem menuitem10 = new Menu.ContextMenuItem();
						menuitem10.setName("引用查看");
						menuitem10.setLink("javascript:seeChnlOfDocCited('" + docId + "','" + docName +"')");
						menuitem10.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_bbgl.gif");
						menu.addContextMenuItem(menuitem10);
						
						
						
						contextmenu.addContextMenu(menu);
                        int num = dataSet.getRowid();
						
					%>
						
                        <tr class="cms_data_tr" id="<pg:cell colName="document_id" defaultValue=""/>" onMouseOver="high(this)" onMouseOut="unhigh(this)">
							<input name="docid" value="<pg:cell colName="document_id" defaultValue=""/>" type=hidden>
							<td class="tablecells" align=center height='30'>
							  <input class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="<pg:cell colName="document_id" defaultValue=""/>">							</td>

							<td class="tablecells" align=center height='30'>
								<pg:equal colName="doctype" value="0">
									<div align="center">
										<img title="HTML文档" src="<%=request.getContextPath()%>/cms/images/doc_html.gif" width="20" height="20">									</div>
								</pg:equal>
								<pg:equal colName="doctype" value="1">
									<div align="center">
										<img title="外部链接类型文档" src="<%=request.getContextPath()%>/cms/images/doc_link.gif" width="20" height="20">									</div>
								</pg:equal>
								<pg:equal colName="doctype" value="2">
									<div align="center">
										<img title="下载文件类型" src="<%=request.getContextPath()%>/cms/images/doc_downloadfile.gif" width="15" height="16">									</div>
								</pg:equal>
								<pg:equal colName="doctype" value="3">
									<div align="center">
										<img title="聚合文件类型" src="<%=request.getContextPath()%>/cms/images/doc_aggr.gif" width="15" height="16">									</div>
								</pg:equal>							</td>
							<%
							if(accesscontroler.checkPermission(channelId,AccessControl.CHANNEL_UPDATEDOC_PERMISSION,AccessControl.CHANNELDOC_RESOURCE)|| accesscontroler.checkPermission(siteid,AccessControl.CHANNEL_UPDATEDOC_PERMISSION,AccessControl.SITEDOC_RESOURCE))
							{
							%>
							<pg:equal colName="doctype" value="0">
							<td id="opdoc_<%=docId%>" class="tablecells" style="cursor:hand" 

									onclick="openWin('<%=rootpath%><%=updatepath%>?docid=<pg:cell colName="document_id" defaultValue=""/>&siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=channelId%>',screen.availWidth-20,screen.availHeight-50)">
								<pg:cell colName="subtitle" defaultValue="" />
							</pg:equal>
							<pg:equal colName="doctype" value="1">
							<td id="opdoc_<%=docId%>" class="tablecells" style="cursor:hand" 
									
									onclick=" openWin('<%=rootpath%>/cms/docManage/doc_gather_links_update.jsp?siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=channelId%>&docid=<pg:cell colName="document_id" defaultValue=""/>',screen.availWidth-20,screen.availHeight-50)">
								<pg:cell colName="subtitle" defaultValue="" />
							</pg:equal>
							<pg:equal colName="doctype" value="2">
							<td id="opdoc_<%=docId%>" class="tablecells" style="cursor:hand" 
									
									onclick="openWin('<%=rootpath%>/cms/docManage/doc_gather_filedownload_update.jsp?docid=<pg:cell colName="document_id" defaultValue=""/>&siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=channelId%>',screen.availWidth-20,screen.availHeight-50)">
								<pg:cell colName="subtitle" defaultValue="" />
							</pg:equal>
							<pg:equal colName="doctype" value="3">
							<td id="opdoc_<%=docId%>" class="tablecells" style="cursor:hand" 
									
									onclick="openWin('<%=rootpath%>/cms/docManage/doc_gather_aggr_update.jsp?docid=<pg:cell colName="document_id" defaultValue=""/>&siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=channelId%>',screen.availWidth-20,screen.availHeight-50)">
								<pg:cell colName="subtitle" defaultValue="" />
							</pg:equal>
							</td>
							<td class="tablecells">
								<pg:cell colName="channelName" defaultValue="" />
							</td>
							<%}%>
							<td class="tablecells">

							  <pg:cell colName="statusname" defaultValue="" />							</td>

							<td class="tablecells">
							  <pg:cell colName="username" defaultValue="" />							</td>
							<td class="tablecells">
							  <pg:cell colName="author" defaultValue="" />							</td>
							<td class="tablecells">
							  <pg:cell colName="docwtime" dateformat="yyyy/MM/dd HH:mm:ss" defaultValue="" />							</td>
							<!--<td class="tablecells"  width="10%">
								
							</td>-->
						</tr>
					</pg:list>
					<%
							request.setAttribute("opdoc",contextmenu);
					%>
					<pg:contextmenu context="opdoc" enablecontextmenu="true" scope="request"/>
					<tr class="labeltable_middle_tr_01">
						<td colspan=10 ><div class="Data_List_Table_Bottom"> 
							
							<pg:index />					</div>  </td>
					</tr>
					<input id="queryString" name="queryString" value="<pg:querystring/>" type="hidden">
					<tr></tr>
				</pg:pager>
		  </table>
		</form>
		<iframe height="0" width="0" name="operIframe"></iframe>
		<iframe name="cleanSession" id="cleanSession"  width="0" height="0"  style="display:none" ></iframe>
	</body>
</html>

