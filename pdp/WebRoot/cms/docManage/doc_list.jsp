<%@page import="com.frameworkset.orm.transaction.TransactionManager"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>


<%@ page import="com.frameworkset.platform.cms.customform.*"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuTag"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenu"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuImpl"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.Menu"%>
<%@ page import="com.frameworkset.platform.cms.util.CMSUtil,com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="org.frameworkset.web.servlet.support.WebApplicationContextUtils"%>
<%@ page import="com.frameworkset.platform.cms.countermanager.CounterManager"%>
<%@ page import="com.frameworkset.platform.cms.countermanager.bean.VideoHitsCounter"%>

<%
TransactionManager tm = new TransactionManager();
try
{
	
	tm.begin();
	AccessControl accesscontroler = AccessControl.getAccessControl();
	String siteid = request.getParameter("siteid");
	String channelId = request.getParameter("channelId");
	
	ChannelCacheManager cachem = (ChannelCacheManager)SiteCacheManager.getInstance().getChannelCacheManager(siteid);
	Channel channel = cachem.getChannel(channelId);
	String channelName = channel.getDisplayName();
	String flag = request.getParameter("flag");
	SiteManager siteManager = new SiteManagerImpl();
	String sitename = siteManager.getSiteInfo(siteid).getName();
	DocumentManager docManager = new DocumentManagerImpl();
	String rootpath = request.getContextPath();
	CounterManager counterManager = (CounterManager)WebApplicationContextUtils.getWebApplicationContext().getBeanObject("counterManager");
	VideoHitsCounter counter = new VideoHitsCounter();
	counter.setSiteId(Integer.valueOf(siteid));
	counter.setChannelId(Integer.valueOf(channelId));
	
    //外部注入发布管理
    //String uuid = CMSUtil.getUUID();
    //PublishMonitor monitor = PublishMonitor.createPublishMonitor();
    //session.setAttribute(uuid,monitor);
	
	String docsorid = request.getParameter("docsorid");
	String title = request.getParameter("title");
	String status = request.getParameter("status");
	String userid = request.getParameter("userid");
	String doctype = request.getParameter("doctype");
	String docLevel = request.getParameter("docLevel");
	String docClass = request.getParameter("docClass");
	String author = request.getParameter("author");
	String docAbstract = request.getParameter("docAbstract");
	String keyword = request.getParameter("keyword");
	String createTimeBegin = request.getParameter("createTimeBegin");
	String createTimeEnd = request.getParameter("createTimeEnd");
	String content = request.getParameter("content");
	
	String iscomment = request.getParameter("iscomment");

	//取频道所设定的自定义表单
	CustomFormManager cfm = new CustomFormManagerImpl();
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
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="<%=request.getContextPath()%>/cms/inc/css/cms.css" rel="stylesheet" type="text/css">
		<title>内容管理主框架</title>
		<pg:config enablecontextmenu="true" enabletree="false"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/include/js/disablebaskspace.js"></script>
				
		<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
		<script src="<%=request.getContextPath()%>/cms/inc/js/func.js"></script>

		<script language="javascript">
		var form1;
		$(document).ready(function(){ 
			form1 = document.getElementById("form1");
			
		});  
	//新增文档
	function adddoc(url, channelName,height,width) {
		
		$.dialog({ title:"新增文档-"+channelName,width:width,height:600, content:'url:'+url,lock: true,maxState:true}); 	
		
	}
	function maintainext(docid,docname)
	{
		
		$.dialog({ title:"管理文档扩展字段-"+docname,width:800,height:600, content:'url:<%=request.getContextPath()%>/cms/docManage/doc_extfieldofsiteorchl_list.jsp?type=3&id='+docid,lock: true,maxState:true}); 	
	}
	function modifyQueryData()
	{
		window.location.reload();
	}
	//送审、提交发布、归档、撤销送审操作，传来的参数分别为送审、提交发布、归档、撤销送审(针对单个文档操作)
	function operateDoc(operType,docId){
		var tagetJsp = "<%=request.getContextPath()%>/cms/docManage/operate_doc.jsp?docId="+docId;
		$("#operType").val(operType);
	    form1.action=tagetJsp;
	    form1.target="operIframe";
		form1.submit();
	}
	//对送审以及提交发布进行批量操作
	function operateDocs(operType){
		
		if(haveSelect('ID')){
			var tagetJsp = "<%=request.getContextPath()%>/cms/docManage/operate_doc.jsp";
			$("#operType").val(operType);
		    form1.action=tagetJsp;
		    form1.target="operIframe";
			form1.submit();
		}else{
			alert("请至少要选择一篇文档！");
		}
	}
	function garbage(){
		//将选中的文档id拼成字符串
		var all = document.getElementsByName("ID");
		var docidStr="";
		for(var i=0;i<all.length;i++){
			if(all[i].checked == true){
				docidStr = docidStr + ":" + all[i].value + ";";
			}
		} 
		if(haveSelect('ID')){
			if(window.confirm("您确定要回收吗？")){
				var urls = "<%=request.getContextPath()%>/cms/docManage/docGarbageHandle.jsp?flag=recursive&siteId=<%=siteid%>&docidStr="+docidStr;
				window.urlstr = urls;
				showModalDialog("../doing_iframe.html",window,"dialogWidth:"+(screen.availWidth-10)+"px;dialogHeight:"+(screen.availHeight-20)+"px;help:no;scroll:auto;status:no");
			}
		}else{
		    alert("请至少要选择一篇文档！");
		}
	}
	function subforms()
	{
		form1.submit();
	}
	function moveDoc(){
		//将选中的文档id拼成字符串
		var all = document.getElementsByName("ID");
		var docidStr="";
		for(var i=0;i<all.length;i++){
			if(all[i].checked == true){
				docidStr = docidStr + all[i].value + ":";
			}
		}
		if(haveSelect('ID')){
			//打开模态窗口选择目的频道
			openWin("<%=request.getContextPath()%>/cms/docManage/channel_select_frame.jsp?flag=1&siteid=<%=siteid%>&docidStr=" + docidStr + "&channelid=<%=channelId%>",400,500);
			return true;
		}else{
	    	alert("请至少要选择一篇文档！");
	    	return false;
	   	}
  		return false;
	}
	function copyDoc(){
		//打开模态窗口选择目的频道
		//openWin("<%=rootpath%>/cms/channelManage/channel_doc_xz.jsp?siteid=<%=siteid%>&flag=2&channelid=<%=channelId%>",800,550);
		//将选中的文档id拼成字符串
		var all = document.getElementsByName("ID");
		var docidStr="";
		for(var i=0;i<all.length;i++){
			if(all[i].checked == true){
				docidStr = docidStr + all[i].value + ":";
			}
		}
		if(haveSelect('ID')){
			//打开模态窗口选择目的频道
			openWin("<%=request.getContextPath()%>/cms/docManage/channel_select_frame.jsp?flag=2&siteid=<%=siteid%>&docidStr=" + docidStr + "&channelid=<%=channelId%>",400,500);
			return true;
		}else{
	    	alert("请至少要选择一篇文档！");
	    	return false;
	   	}
  		return false;
	}
	function citeDoc(){
		//打开模态窗口选择引用文档
		//openWin("<%=rootpath%>/cms/channelManage/channel_doc_xz.jsp?siteid=<%=siteid%>&flag=3&channelid=<%=channelId%>",800,550);
		var all = document.getElementsByName("ID");
		var docidStr="";
		for(var i=0;i<all.length;i++){
			if(all[i].checked == true){
				docidStr = docidStr + all[i].value + ":";
			}
		}
		if(haveSelect('ID')){
			//打开模态窗口选择目的频道
			openWin("<%=request.getContextPath()%>/cms/docManage/channel_select_frame.jsp?flag=3&siteid=<%=siteid%>&docidStr=" + docidStr + "&channelid=<%=channelId%>",400,500);
			return true;
		}else{
	    	alert("请至少要选择一篇文档！");
	    	return false;
	   	}
  		return false;
	}
	//取代前面的moveDoc()，copyDoc()和citeDoc()
	function transmitDoc(){
		//将选中的文档id拼成字符串
		var all = document.getElementsByName("ID");
		var docidStr="";
		for(var i=0;i<all.length;i++){
			if(all[i].checked == true){
				docidStr = docidStr + all[i].value + ":";
			}
		}
		if(haveSelect('ID')){
			//打开模态窗口选择目的频道
			openWin("<%=request.getContextPath()%>/cms/docManage/channel_select_frame.jsp?siteid=<%=siteid%>&docidStr=" + docidStr + "&channelid=<%=channelId%>&opt=transmitDoc",400,500);
		}else{
	    	alert("请至少要选择一篇文档！");
	   	}
	}
	//转发一篇文档
	//function transmitDoc1(docId){
		//将选中的文档id拼成字符串
	//	var docidStr=docId + ":";
		//打开模态窗口选择目的频道
	//	openWin("channel_select_frame.jsp?siteid=<%=siteid%>&docidStr=" + docidStr + "&channelid=<%=channelId%>",400,500);
	//}
	function citeDocManager(){
		//打开模态窗口查看引用文档，并对其进行管理
		openWin("<%=rootpath%>/cms/docManage/cited_doc_manager.jsp?channelid=<%=channelId%>",800,700);
	}
	function manageDocComment(docid){
		//打开模态窗口，并文档评论进行查看管理
		openWin("<%=rootpath%>/cms/docManage/doc_comment_manager.jsp?docId=" + docid,800,700);
	}
	
	function manageDocPlayed(docid) {
		openWin("<%=rootpath%>/cms/docManage/doc_played_manager.jsp?docId=" + docid,800,700);
	}
	
	//保存版本
	function addDocVer()
	{
		//将选中的文档id拼成字符串
		var all = document.getElementsByName("ID");
		var docidStr="";
		for(var i=0;i<all.length;i++){
			if(all[i].checked == true){
				docidStr = docidStr + all[i].value + ":";
			}
		}
		if(haveSelect('ID')){
			openWin("<%=rootpath%>/cms/docManage/add_document_version.jsp?docidStr=" + docidStr,400,500);
			return true;
		}else{
	    	alert("请选择要求保存版本的文档！");
	    	return false;
	   	}
	}
	//对一篇文档保存版本
	//function addDocVer1(docId){
		//将选中的文档id拼成字符串
	//	var docidStr=docId + ":";
	//	openWin("<%=rootpath%>/cms/docManage/add_document_version.jsp?docidStr=" + docidStr,400,500);
	//}
	//版本管理
	function manageDocVer(docid)
	{
		//打开模态窗口选择目的频道
		openWin("<%=rootpath%>/cms/docManage/dilogparent.jsp?docid=" + docid,800,650);
	}
	//导出
	function exportDoc()
	{
		//将选中的文档id拼成字符串
		var all = document.getElementsByName("ID");
		var docidStr="";
		for(var i=0;i<all.length;i++){
			if(all[i].checked == true){
				docidStr = docidStr + all[i].value + ",";
			}
		}
		if(haveSelect('ID')){
			var zipname = window.prompt("请输入指定的导出文件名，不指定将使用系统默认值！","在这输入指定的导出文件名");
			if(zipname != null && zipname != "")
				$("#expzipname").val(zipname);
			
			form1.action="<%=request.getContextPath()%>/cms/docManage/doc_export.jsp?docids=" + docidStr;
	        form1.target="operIframe";
			form1.submit();
			//打开模态窗口选择目的频道
			//window.open("doc_export.jsp?siteid=<%=siteid%>&docids=" + docidStr + "&channelId=<%=channelId%>","newwin","");
		}else{
	    	alert("请至少要选择一篇文档！");
	   	}
	}
	//导入
	function importDoc()
	{
		openWin("<%=request.getContextPath()%>/cms/docManage/doc_load.jsp?siteid=<%=siteid%>&channelId=<%=channelId%>",380,210);
		
	}
	//设置置顶
	function addArrangeDoc(docid)
	{
		openWin("<%=rootpath%>/cms/docManage/docArrange_add.jsp?siteid=<%=siteid%>&channelid=<%=channelId%>&docid=" + docid,555,240);
	}
	//取消置顶
	function delDocArrange(docid)
	{
		if(window.confirm("您确定要取消该文档的置顶吗？"))
		{
			document.forms[0].action = "<%=rootpath%>/cms/docManage/docArrange_del_submit.jsp?siteid=<%=siteid%>&channelid=<%=channelId%>&docid=" + docid;
			document.forms[0].target="operIframe";
			document.forms[0].submit();
		}
	}
	//文档置顶管理
	function arrangeDocM()
	{
		openWin("<%=rootpath%>/cms/docManage/docArrange_manage.jsp?chnlid=<%=channelId%>",800,500);
	}
	//扩展字段
	function extfieldmanage()
	{
		parent.document.location.href = "<%=request.getContextPath()%>/cms/docManage/doc_extfieldofsiteorchl_list.jsp?type=2&id=<%=channelId%>";
	}
	//删除文档
	function delDoc()
	{
		//将选中的文档id拼成字符串
		var all = document.getElementsByName("ID");
		var docidStr="";
		for(var i=0;i<all.length;i++){
			if(all[i].checked == true){
				docidStr = docidStr + all[i].value + ";";
			}
		}
		if(haveSelect('ID')){
		if(confirm('您确定要删除所选文档吗？')){
			form1.action="<%=request.getContextPath()%>/cms/docManage/document_del_submit.jsp?flag=recursive&siteId=<%=siteid%>&docidStr=" + docidStr;
	        form1.target="operIframe";
			form1.submit();
			}
			else
			{
			  return false;
			}
		}else{
	    	alert("请选择要求删除的文档！");
	    	return false;
	   	}
	}
	//批量修改文档
	function updateDocs()
	{
		//将选中的文档id拼成字符串
		var all = document.getElementsByName("ID");
		var docidStr="";
		for(var i=0;i<all.length;i++){
			if(all[i].checked == true){
				docidStr = docidStr + all[i].value + ";";
			}
		}
		if(haveSelect('ID')){
			openWin("<%=request.getContextPath()%>/cms/docManage/updateDocs.jsp?docidStr="+docidStr+"&channelName=<%=channelName%>",600,500);
		}else{
	    	alert("请选择要求修改的文档！");
	    	return false;
	   	}
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
	//删除一篇文档
	//function delDoc1(docId){
	//	docidStr = docId + ";";
	//	form1.action="document_del_submit.jsp?docidStr=" + docidStr;
	//    form1.target="operIframe";
	//	form1.submit();
	//}
	function auditAgreeDoc(docId){
		var idStr = ":" + docId + ":<%=channelId%>";
		//参数auditFlag为审核意见，0表不同意，1表同意
		openWin("<%=rootpath%>/cms/docManage/audit_add_comment.jsp?idStr=" + idStr + "&auditFlag=1",300,300);
	}
	function auditUnagreeDoc(docId){
		var idStr = ":" + docId + ":<%=channelId%>";
		var addComment = window.confirm("要增加审核意见吗？");
		if(addComment==false){
			//参数auditFlag为审核意见，0表不同意，1表同意
			form1.action = "<%=rootpath%>/cms/docManage/auditDocHandle.jsp?idStr=" + idStr + "&auditFlag=0";
			form1.target = "operIframe";
			form1.submit();
		}
		else{
			openWin("<%=rootpath%>/cms/docManage/audit_add_comment.jsp?idStr=" + idStr + "&auditFlag=0",300,300);
		}	
	}
	function audit(docId,docTpye,docName){
		var taskidStr = ":" + docId + ":<%=channelId%>";
		if(docTpye==0){
			openDialog("<%=request.getContextPath()+updatepath%>?flag=audit&siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=channelId%>&docid=" + docId + "&taskidStr=" + taskidStr,screen.availWidth-20,screen.availHeight-50,docName);
		}else if(docTpye==1){
			openDialog("<%=request.getContextPath()%>/cms/docManage/doc_gather_links_update.jsp?flag=audit&siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=channelId%>&docid=" + docId + "&taskidStr=" + taskidStr,screen.availWidth-20,screen.availHeight-50,docName);
		}else if(docTpye==2){
			openWin("<%=request.getContextPath()%>/cms/docManage/doc_gather_filedownload_update?flag=audit&siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=channelId%>&docid=" + docId + "&taskidStr=" + taskidStr,screen.availWidth-20,screen.availHeight-50);
		}else if(docTpye==3){
			openDialog("<%=request.getContextPath()%>/cms/docManage/doc_gather_aggr_update.jsp?flag=audit&siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=channelId%>&docid=" + docId + "&taskidStr=" + taskidStr,screen.availWidth-20,screen.availHeight-50,docName);
		}
	}
    var winOpen;
    
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
	function publishDoc(docId,docTypeFlag){
		var flag = 3;
		//if(docTypeFlag!=0)
		//	flag = 51
		form1.action = "<%=request.getContextPath()%>/cms/docManage/publishDocHandle.jsp?flag="+flag+"&docId="+docId;
		form1.target = "operIframe";
        currentPageUrl();
		//form1.submit();
        var path = "doc_publish_info.jsp?flag="+flag+"&docId="+docId;
        var featrue = "dialogHeight:310px;dialogWidth:450px";
        winOpen = window.showModelessDialog(path,window,featrue);
		//document.all.divProcessing.style.display="";
	}
    
	//批量发布
	function publishDocs(){
		if(haveSelect('ID')){
			form1.action = "<%=request.getContextPath()%>/cms/docManage/publishDocHandle.jsp?flag=4&siteId=<%=siteid%>&chnlId=<%=channelId%>";
			form1.target = "operIframe";
            currentPageUrl();
			//form1.submit();    
            var path = "doc_publish_info.jsp";
            var featrue = "dialogHeight:310px;dialogWidth:450px";            
            winOpen = window.showModelessDialog(path,window,featrue);
			//document.all.divProcessing.style.display="";
		}else{
			alert("请至少选择一篇需发布的文档！");
		}
	}
	//发布预览
	function publishPreview(docId){
		form1.action = "<%=request.getContextPath()%>/cms/docManage/previewPubDocHandle.jsp?docId="+docId;
		form1.target = "operIframe";
		form1.submit();
	}
	//撤销发布
	function withdrawPublish(docId){
		form1.action = "<%=request.getContextPath()%>/cms/docManage/withdrawPublishHandle.jsp?siteId=<%=siteid%>&docId="+docId;
		form1.target = "operIframe";
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
	function seeChnlOfDocCited(docId,docName){
		var url = "<%=rootpath%>/cms/docManage/see_chnl_docCited.jsp?docId="+docId+"&docName="+docName;
		openWin(url,750,550);
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
    
    //文档浏览
    function docUrlView(siteid,channelid,docId){
    //alert("siteid:" + siteid);
      //  alert("channelid:" + channelid);
            //alert("docId:" + docId);
           
		
       window.open("<%=rootpath%>/cms/docManage/doc_urlview.jsp?docid=" + docId + "&siteid=" + siteid + "&channelid=" + channelid,"_blank");
    }
    
    function closeSubWindow(uuid){ 
        winOpen.close();
        document.all("cleanSession").src = "../cleanSession.jsp?uuid=" + uuid;
   
    }
    //调用发布主页面的函数
    function lastRefreshSubPage(multipub,pageurl){        
        winOpen.updateMsg(multipub,pageurl);
    }
    //调用发布主页面的函数
    //url 为空的时候 直接alert(msg)
    //url 不为空的时候 confirm()选择msg
    function alertFun(msg,url){
        winOpen.alertMsg(msg,url);
    }
    
    function orderByHit()
	{	
		//查询
		var flag = <%=request.getParameter("orderByHit")%>;
		if(flag==true)
		{
			form1.action="<%=rootpath%>/cms/docManage/doc_list.jsp?flag=query&orderByHit=false"
		}
		else
		{
			form1.action="<%=rootpath%>/cms/docManage/doc_list.jsp?flag=query&orderByHit=true"
		}
		form1.target="forDocList";
		form1.submit();	
	}
	
	function orderByDocWtime()
	{	
		//查询
		var flag = <%=request.getParameter("orderByDocWtime")%>;
		if(flag==true)
		{
			form1.action="<%=rootpath%>/cms/docManage/doc_list.jsp?flag=query&orderByDocWtime=false"
		}
		else
		{
			form1.action="<%=rootpath%>/cms/docManage/doc_list.jsp?flag=query&orderByDocWtime=true"
		}
		form1.target="forDocList";
		form1.submit();	
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
		<!-- div id="divProcessing" style="position:absolute;width:40%;height:20px;z-index:1;
			background-color:#F0f0f0;layer-background-color:#F0f0f0;left:22%;top:80%;display:none;"  class="font">   
	    <marquee direction="left" width="250" behavior="alternate"><span class=STYLE1>正在发布中，请稍等……</span></marquee>
    	</div--> 
           
         
		<form name="form1" id="form1" action="" method="post">
            <input type="hidden" name="uuid">
            <input type="hidden" name="isRecordValue">
            <input type="hidden" name="recursionPublish">
            <input type="hidden" name="clearCache">
            <input type="hidden" name="url">            
            <input type="hidden" name="expzipname" id="expzipname" value=""><!-- 指定文档导出name-->
            <input type="hidden" name="siteid" value="<%=siteid%>">
            <input type="hidden" name="channelName" value="<%=channelName%>">
            <input type="hidden" name="channelId" value="<%=channelId%>">
            <input type="hidden" name="operType" id="operType">
            
			<table width="100%" border="1" align=center cellpadding="3" cellspacing="0" bordercolor="#B7CBE4"  class="Datalisttable" id="docListTable">
				<tr>
					<td height='25' colspan="13" background="<%=request.getContextPath()%>/cms/images/data_list_tHeadbg.jpg" style="text-align:left; background:url(<%=request.getContextPath()%>/cms/images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7">
					<div  class="DocumentOperT">文档操作：</div>
				
						<%if(accesscontroler.checkPermission(channelId,AccessControl.CHANNEL_ADDDOC_PERMISSION,AccessControl.CHANNELDOC_RESOURCE)
							|| accesscontroler.checkPermission(siteid,AccessControl.CHANNEL_ADDDOC_PERMISSION,AccessControl.SITEDOC_RESOURCE)
						){%>
						<a style="cursor:hand" onClick="javascript:adddoc('<%=rootpath%><%=addpath%>?siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=channelId%>','<%=channelName%>',screen.height,screen.width)"><div class="DocumentOper"><img 
									src="../images/new_doc.gif" class="operStyle">内容采集</div></a>
						<%}if(accesscontroler.checkPermission(channelId,AccessControl.DELIVER_PERMISSION,AccessControl.CHANNELDOC_RESOURCE)
							|| accesscontroler.checkPermission(siteid,
											AccessControl.DELIVER_PERMISSION,
											AccessControl.SITEDOC_RESOURCE)
						){%>
						<a style="cursor:hand" onClick="operateDocs('送审')"><div class="DocumentOper"><img 
									class="operStyle" src="<%=request.getContextPath()%>/cms/images/ico_point.gif">送审</div></a> 
						<%}if(accesscontroler.checkPermission(channelId,AccessControl.SUBPUBLISH_PERMISSION,AccessControl.CHANNELDOC_RESOURCE)
							|| accesscontroler.checkPermission(siteid,
											AccessControl.SUBPUBLISH_PERMISSION,
											AccessControl.SITEDOC_RESOURCE)
						){%>
						<a style="cursor:hand" onClick="operateDocs('提交发布')"><div class="DocumentOper"><img 
									class="operStyle" src="<%=request.getContextPath()%>/cms/images/folder.gif">提交发布</div></a> 
						<%}if(accesscontroler.checkPermission(channelId,AccessControl.DOCPUBLISH_PERMISSION,AccessControl.CHANNELDOC_RESOURCE)
								|| accesscontroler.checkPermission(siteid,
											AccessControl.DOCPUBLISH_PERMISSION,AccessControl.SITEDOC_RESOURCE)
						){%>
						<a style="cursor:hand" onClick="publishDocs()"><div class="DocumentOper"><img 
									class="operStyle" src="<%=request.getContextPath()%>/cms/images/folder.gif">批量发布</div></a> 
<!--						<a style="cursor:hand" onClick="operateDoc('撤销送审')"><div class="DocumentOper"><img-->
<!--									class="operStyle" src="../images/audity.gif">撤销送审</div></a>-->
<!--						<a style="cursor:hand" onClick="operateDoc('归档')"><div class="DocumentOper"><img -->
<!--									class="operStyle" src="../images/trash.gif">归档</div></a> -->
						<%}if(accesscontroler.checkPermission(channelId,AccessControl.TRASH_PERMISSION,AccessControl.CHANNEL_RESOURCE)
							|| accesscontroler.checkPermission(siteid,
											AccessControl.TRASH_PERMISSION,
											AccessControl.SITECHANNEL_RESOURCE)
						){%>
						<a style="cursor:hand; filter:dropshadow(color=#ffffff,offx=1,offy=1,positive=2);" onClick="garbage()"><img 
									class="operStyle" src="<%=request.getContextPath()%>/cms/images/trash_small.gif"><div class="DocumentOper">回收</div></a>
						<%}if(accesscontroler.checkPermission(channelId,AccessControl.TRANSMIT_PERMISSION,AccessControl.CHANNELDOC_RESOURCE)
							|| accesscontroler.checkPermission(siteid,
											AccessControl.TRANSMIT_PERMISSION,
											AccessControl.SITEDOC_RESOURCE)
						){%>
						<a style="cursor:hand" onClick="transmitDoc()"><div class="DocumentOper"><img 
									class="operStyle" src="<%=request.getContextPath()%>/sysmanager/images/plan.gif">转发</div></a>
<!--						<a style="cursor:hand" onClick="moveDoc()"><div class="DocumentOper"><img -->
<!--									class="operStyle" src="../../sysmanager/images/move.gif">移动</div></a>-->
<!--						<a style="cursor:hand" onClick="copyDoc()"><div class="DocumentOper"><img -->
<!--									class="operStyle" src="../../sysmanager/images/copy.gif">复制</div></a>-->
<!--						<a style="cursor:hand" onClick="citeDoc()"><div class="DocumentOper"><img -->
<!--									class="operStyle" src="../../sysmanager/images/plan.gif">引用</div></a>-->
						<%}if(accesscontroler.checkPermission(channelId,AccessControl.ADD_DOCVER_PERMISSION,AccessControl.CHANNELDOC_RESOURCE)
							|| accesscontroler.checkPermission(siteid,
											AccessControl.ADD_DOCVER_PERMISSION,
											AccessControl.SITEDOC_RESOURCE))
						{%>
						<a style="cursor:hand" onClick="addDocVer()"><div class="DocumentOper"><img 
									class="operStyle" src="<%=request.getContextPath()%>/sysmanager/images/plan.gif">保存版本</div></a>
						<%}%>
<!--						<a style="cursor:hand" onClick="manageDocVer()"><div class="DocumentOper"><img -->
<!--									class="operStyle" src="../../sysmanager/images/plan.gif">版本管理</div></a>-->
						<%if(accesscontroler.checkPermission(channelId,AccessControl.EXPORTDOC_PERMISSION,AccessControl.CHANNELDOC_RESOURCE)
							|| accesscontroler.checkPermission(siteid,
											AccessControl.EXPORTDOC_PERMISSION,
											AccessControl.SITEDOC_RESOURCE))
						{%>
						<a style="cursor:hand" onClick="exportDoc()"><div class="DocumentOper"><img 
									class="operStyle" src="<%=request.getContextPath()%>/sysmanager/images/plan.gif">导出</div></a>
						<%}if(accesscontroler.checkPermission(channelId,AccessControl.IMPORTDOC_PERMISSION,AccessControl.CHANNELDOC_RESOURCE)
							|| accesscontroler.checkPermission(siteid,
											AccessControl.IMPORTDOC_PERMISSION,
											AccessControl.SITEDOC_RESOURCE))
						{%>
						<a style="cursor:hand" onClick="importDoc()"><div class="DocumentOper"><img 
									class="operStyle" src="<%=request.getContextPath()%>/sysmanager/images/plan.gif">导入</div></a>
						<%}if(accesscontroler.checkPermission(channelId,AccessControl.DELETE_PERMISSION,AccessControl.CHANNELDOC_RESOURCE)
							|| accesscontroler.checkPermission(siteid,
											AccessControl.DELETE_PERMISSION,
											AccessControl.SITEDOC_RESOURCE))
						{%>
						<a style="cursor:hand" onClick="delDoc()"><div class="DocumentOper"><img 
									class="operStyle" src="<%=request.getContextPath()%>/sysmanager/images/plan.gif">删除</div></a>
						
						<%}%>
						<a style="cursor:hand" onClick="updateDocs()"><div class="DocumentOper"><img 
									class="operStyle" src="<%=request.getContextPath()%>/sysmanager/images/plan.gif">批量修改</div></a>
<!--						<a style="cursor:hand" onClick="addArrangeDoc()"><div class="DocumentOper"><img -->
<!--									class="operStyle" src="../../sysmanager/images/plan.gif">设置置顶</div></a>-->
<!--						<a style="cursor:hand" onClick="arrangeDocM()"><div class="DocumentOper"><img -->
<!--									class="operStyle" src="../../sysmanager/images/plan.gif">置顶管理</div></a>-->					
<!--						<a style="cursor:hand" onClick="citeDocManager()"><div class="DocumentOper"><img -->
<!--									class="operStyle" src="../../sysmanager/images/renyuan.gif">引用文档管理</div></a>-->
<!--						<a style="cursor:hand" onClick="extfieldmanage()"><div class="DocumentOper"><img 
									class="operStyle" src="../../sysmanager/images/plan.gif">扩展字段</div></a>-->
					    
					
					</td>
				</tr>
				<pg:listdata dataInfo="com.frameworkset.platform.cms.documentmanager.tag.DocumentList" keyName="DocumentList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="15" scope="request" data="DocumentList" isList="false">
					<tr class="cms_report_tr">
						<!--设置分页表头-->

						<td align=center style="width:5%"><input class="checkbox" type="checkBox" hidefocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')"></td>
						<td>
							文档类型						</td>
						<td>
							是否置顶						</td>
						<td>
							文档标题</td>
						<!--<td width="12%">
							所属频道</td>-->
						<!--<td width="6%">
							查看</td>-->
						<td>分类</td>
						<td>
							状态						</td>
						<td>
							发稿人						</td>
						<td>
							<%if(channelName.equals("领导讲话")||channelName.equals("领导活动")){out.print("领导名称");}else{out.print("作者");}%>						</td>
						<td style="cursor:hand" onclick="orderByHit()">
							点击					</td>
						<td style="cursor:hand" onclick="orderByDocWtime()">
							发稿日期						</td>
						<td nowrap="nowrap">
							流程
						</td>
						<td  height='30'>评论</td>
						<td  height='30'>播放</td>
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
					<pg:param name="docClass" />
					<pg:param name="orderByHit"/>
					<pg:param name="iscomment"/>
					<!--检测当前页面是否有记录-->
					<pg:notify>
						<tr  class="labeltable_middle_tr_01">
							<td colspan=100 align='center' height="18px">
								该频道暂时没有文档							</td>
						</tr>
					</pg:notify>

					<!--list标签循环输出每条记录-->
					<%
						ContextMenu contextmenu = new ContextMenuImpl();
					%>
					<pg:list>
					<%
						int docId = dataSet.getInt("document_id");	
						if((iscomment!=null&&iscomment.equals("0")&&(docManager.getDocCommentByDocId(docId, 1)>0))||(iscomment==null||iscomment.equals("")))
						{
						int docTypeFlag = dataSet.getInt("doctype");
						int docStatusFlag = dataSet.getInt("status");
											
						String docName = dataSet.getString("subtitle");
						
						//对文档标题中的'进行转义,防止在拼凑字符串时,形成''问题,导致右键菜单消失 注意java \\\\ 代表一个\
						docName = docName.replaceAll("'","\\\\\\\\'") ;
						
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
							menuitem.setLink("javascript:operateDoc('送审','" + docId + "','" + docName + "')");
							menuitem.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_ss.gif");
							menu.addContextMenuItem(menuitem);
						}
						if(docOpers.get("withdrawdeliver")!=null)
						{
							Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
							menuitem.setName("撤销送审");
							menuitem.setLink("javascript:operateDoc('撤销送审','" + docId + "')");
							menuitem.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_cxss.gif");
							menu.addContextMenuItem(menuitem);
						}
						if(docOpers.get("audit")!=null)
						{
							Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
							menuitem.setName("审核");
							menuitem.setLink("javascript:audit('" + docId + "','" +docTypeFlag + "','" + docName + "')");
							menuitem.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_sh.gif");
							menu.addContextMenuItem(menuitem);
						}
						if(docOpers.get("subpublish")!=null)
						{
							Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
							menuitem.setName("提交发布");
							menuitem.setLink("javascript:operateDoc('提交发布','" + docId + "')");
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
							menuitem.setLink("javascript:operateDoc('归档','" + docId + "')");
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
								menuitem.setLink("javascript:addArrangeDoc('" + docId + "')");
								menuitem.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_szzd.gif");
								menu.addContextMenuItem(menuitem);
							}
							else
							{
								Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
								menuitem.setName("取消置顶");
								menuitem.setLink("javascript:delDocArrange('" + docId + "')");
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
						
						
						if(docTypeFlag == 0)//管理文档扩展字段
						{
							Menu.ContextMenuItem menuitemext = new Menu.ContextMenuItem();
							menuitemext.setName("扩展字段管理");
							menuitemext.setLink("javascript:maintainext('" + docId + "','" + docName +"')");
							menuitemext.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_bbgl.gif");
							menu.addContextMenuItem(menuitemext);
						}
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
							<td>
							<%
							if(isArrangeDoc)
							{
								if(!isArrangeOverTime)
								{
							%>
							<span class="red_star">置顶</span>
							<%
								}
								else
								{
							%>							
							<span class="red_star">置顶过期</span>
							<%
								}
							}
							%>
							</td>
							<%
							if(accesscontroler.checkPermission(channelId,AccessControl.CHANNEL_UPDATEDOC_PERMISSION,AccessControl.CHANNELDOC_RESOURCE)|| accesscontroler.checkPermission(siteid,AccessControl.CHANNEL_UPDATEDOC_PERMISSION,AccessControl.SITEDOC_RESOURCE))
							{
							%>
							<pg:equal colName="doctype" value="0">
								<td id="opdoc_<%=docId%>" class="tablecells" style="cursor:hand" 
									onclick="openDialog('<%=rootpath%><%=updatepath%>?docid=<pg:cell colName="document_id" defaultValue=""/>&siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=channelId%>',screen.availWidth-20,screen.availHeight-50,'<pg:cell colName="subtitle" defaultValue=""/>')">
								<pg:cell colName="subtitle" defaultValue="" />
							</pg:equal>
							<pg:equal colName="doctype" value="1">
							<%
								if(docManager.ext_orgByDocId(docId+"").equals(""))
								{
							%>
									<td id="opdoc_<%=docId%>" class="tablecells" style="cursor:hand" onclick=" openDialog('<%=rootpath%>/cms/docManage/doc_gather_links_update.jsp?siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=channelId%>&docid=<pg:cell colName="document_id" defaultValue=""/>',screen.availWidth-20,screen.availHeight-50,'<pg:cell colName="subtitle" defaultValue=""/>')">
							<%
								}
								else
								{
							%>
							<td id="opdoc_<%=docId%>" class="tablecells" style="cursor:hand" 
									
									onclick=" openDialog('<%=rootpath%>/cms/zwgkml/doc_gather_links_update.jsp?siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=channelId%>&docid=<pg:cell colName="document_id" defaultValue=""/>',screen.availWidth-20,screen.availHeight-50,'<pg:cell colName="subtitle" defaultValue=""/>')">
							<%}%>
								<pg:cell colName="subtitle" defaultValue="" />
							</pg:equal>
							<pg:equal colName="doctype" value="2">
							<td id="opdoc_<%=docId%>" class="tablecells" style="cursor:hand" 
									
									onclick="openWin('<%=rootpath%>/cms/docManage/doc_gather_filedownload_update.jsp?docid=<pg:cell colName="document_id" defaultValue=""/>&siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=channelId%>',screen.availWidth-20,screen.availHeight-50)">
								<pg:cell colName="subtitle" defaultValue="" />
							</pg:equal>
							<pg:equal colName="doctype" value="3">
							<td id="opdoc_<%=docId%>" class="tablecells" style="cursor:hand" 
									
									onclick="openDialog('<%=rootpath%>/cms/docManage/doc_gather_aggr_update.jsp?docid=<pg:cell colName="document_id" defaultValue=""/>&siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=channelId%>',screen.availWidth-20,screen.availHeight-50,'<pg:cell colName="subtitle" defaultValue=""/>')">
								<pg:cell colName="subtitle" defaultValue="" />
							</pg:equal>
							</td>
							<td><pg:cell colName="doc_class" /></td>
							<%
								}
								else
								{
							%>
							<td id="opdoc_<%=docId%>" class="tablecells" onclick="alert('对不起，您没有权限修改文档！');" style="cursor:hand">
								<pg:cell colName="subtitle" defaultValue="" />
							</td>
							<%
								}
							%>
							<!--<td class="tablecells">
								<%
								if(accesscontroler.checkPermission(channelId,AccessControl.CHANNEL_VIEWDOC_PERMISSION,AccessControl.CHANNELDOC_RESOURCE)|| accesscontroler.checkPermission(siteid,AccessControl.CHANNEL_VIEWDOC_PERMISSION,AccessControl.SITEDOC_RESOURCE))
								{
								%>
								<a href="javascript:void(0)" onclick="viewDocInfo(<%=docId%>,<pg:cell colName="doctype" defaultValue=""/>);" ><u>查看</u></a>
								<%
								}
								else
								{
								%>
								<a href="javascript:void(0)" onclick="alert('对不起，您没有权限查看文档！');" ><u>查看</u></a>
								<%
								}
								%>
							</td>-->
							<td class="tablecells">

							  <pg:cell colName="statusname" defaultValue="" />							</td>

							<td class="tablecells">
							  <pg:cell colName="username" defaultValue="" />							</td>
							<td class="tablecells">
							  <pg:cell colName="author" defaultValue="" />							</td>
							<td class="tablecells">
							  <pg:cell colName="count" defaultValue="0" />							</td>
							<td class="tablecells">
							  <pg:cell colName="docwtime" dateformat="yyyy/MM/dd HH:mm:ss" defaultValue="" />							</td>
							<td class="tablecells"  width="10%">
								<pg:cell colName="flowName" defaultValue="未知流程"/>
							</td>
							<td style="color:red;cursor:hand" onclick="javascript:manageDocComment('<%=docId%>')"><%=docManager.getDocCommentByDocId(docId, null)%></td>
							<td style="color:red;cursor:hand" onclick="javascript:manageDocPlayed('<%=docId%>')"><%counter.setDocId(Integer.valueOf(docId));%><%=counterManager.getVideoHitsCount(Integer.valueOf(siteid), Integer.valueOf(channelId), docId)%></td>
						</tr>
						<%}%>
					</pg:list>
					<%
							request.setAttribute("opdoc",contextmenu);
					%>
					<pg:contextmenu context="opdoc" enablecontextmenu="true" scope="request"/>
					<tr class="labeltable_middle_tr_01">
						<td colspan=13 ><div class="Data_List_Table_Bottom"> 
							
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
<%
	tm.commit();
}
finally
{
	tm.release();
}
%>