<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="java.util.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.customform.*"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.*"%>



<%	
	AccessControl accesscontroler = AccessControl.getAccessControl();
	String curUserid = accesscontroler.getUserID();
	request.setAttribute("curUserid",curUserid);
	String flag = request.getParameter("flag");
	String subTitle = request.getParameter("title");
	String keyword = request.getParameter("keyword");
	String docAbstract = request.getParameter("docAbstract");
	String docType = request.getParameter("doctype");
	String docLevel = request.getParameter("docLevel");
	String creatTime = request.getParameter("creatTime");
	String channelIds = request.getParameter("channelIds");
	String rootpath = request.getContextPath();
	//取频道所设定的自定义表单
	CustomFormManager cfm = new CustomFormManagerImpl();
%>
<html>
<head>
<title>
	新文档列表
</title>
<link  href="../inc/css/cms.css" rel="stylesheet" type="text/css"></link>
<pg:config enablecontextmenu="true" enabletree="false"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>

<script src="${pageContext.request.contextPath}/include/security.js"></script>
<script language="javascript" src="../inc/js/func.js"></script>

<script type="text/javascript">
/****************************************************************/

//operType为:送审、提交发布、归档、撤销送审(针对单个文档操作)
function operateDoc(operType,docId,channelId,siteId,channelName){
    //form1.action = "operate_doc.jsp?siteid="+siteId+"&channelName="+channelName+"&channelId="+channelId+"&operType="+operType+"&docId="+docId;
   // form1.target="operIframe";
	  //form1.submit();
	  
	  $.secutiry.dosubmit("form1",
						"operate_doc.jsp?siteid="+siteId+"&channelName="+channelName+"&channelId="+channelId+"&operType="+operType+"&docId="+docId,
								"operIframe",
								"<%=rootpath%>");
}

//回收一篇文章
function garbageOneDoc(docId){
	if(window.confirm("您确定要回收吗？")){
		//form1.action="docGarbageHandle.jsp?docidStr=:" + docId + ";";
		//form1.target="operIframe";
		//form1.submit();
		
		$.secutiry.dosubmit("form1",
						"docGarbageHandle.jsp?docidStr=:" + docId + ";",
								"operIframe",
								"<%=rootpath%>");
	}
}

//删除一篇文档
function delOneDoc(docId,siteId){
   if(confirm('您确定要删除编号为'+docId+'的文档吗？'))
   {
	//form1.action="document_del_submit.jsp?siteId=" + siteId + "&docidStr="+docId+";";
	//form1.target="operIframe";
	//form1.submit();
	$.secutiry.dosubmit("form1",
						"document_del_submit.jsp?siteId=" + siteId + "&docidStr="+docId+";",
								"operIframe",
								"<%=rootpath%>");
	}
}


/****************************************************************/
function adddoc(siteId,channelId,channelName){
	form1.action="doc_gather.jsp?siteid="+siteId+"&channelName="+channelName+"&channelId="+channelId;
	alert("新增文档："+form1.action);
	//form1.submit();
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
				//form1.action="docGarbageHandle.jsp?docidStr="+docidStr;
				//form1.target="operIframe";
				//form1.submit();
				
				$.secutiry.dosubmit("form1",
						"docGarbageHandle.jsp?docidStr="+docidStr,
								"operIframe",
								"<%=rootpath%>");
			}
		}else{
		    alert("请至少要选择一篇文档！");
		}
	}
	function moveDoc(siteId,channelId){
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
			openWin("channel_select_frame.jsp?flag=1&siteid="+siteId+"&docidStr=" + docidStr + "&channelid="+channelId,400,500);
			return true;
		}else{
	    	alert("请至少要选择一篇文档！");
	    	return false;
	   	}
  		return false;
	}
	function copyDoc(siteId,channelId){
		//打开模态窗口选择目的频道
		//openWin(../channelManage/channel_doc_xz.jsp?siteid="+siteId+"&flag=2&channelid="+channelId,800,550);
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
			openWin("channel_select_frame.jsp?flag=2&siteid="+siteId+"&docidStr=" + docidStr + "&channelid="+channelId,400,500);
			return true;
		}else{
	    	alert("请至少要选择一篇文档！");
	    	return false;
	   	}
  		return false;
	}
	function citeDoc(siteId,channelId){
		//打开模态窗口选择引用文档
		//openWin("../channelManage/channel_doc_xz.jsp?siteid="+siteId+"&flag=3&channelid="+channelId,800,550);
		var all = document.getElementsByName("ID");
		var docidStr="";
		for(var i=0;i<all.length;i++){
			if(all[i].checked == true){
				docidStr = docidStr + all[i].value + ":";
			}
		}
		if(haveSelect('ID')){
			//打开模态窗口选择目的频道
			openWin("channel_select_frame.jsp?flag=3&siteid="+siteId+"&docidStr=" + docidStr + "&channelid="+channelId,400,500);
			return true;
		}else{
	    	alert("请至少要选择一篇文档！");
	    	return false;
	   	}
  		return false;
	}
	//转发
	function transmitDoc(siteId,channelId,docId){ 
		//将选中的文档id拼成字符串
		docidStr = docId + ":";
		//打开模态窗口选择目的频道
		openWin("channel_select_frame.jsp?newDoc=true&siteid="+siteId+"&docidStr=" + docidStr + "&channelid="+channelId,400,500);
	}
	function citeDocManager(channelId){
		//打开模态窗口查看引用文档，并对其进行管理
		openWin("cited_doc_manager.jsp?channelid="+channelId,800,700);
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
			openWin("add_document_version.jsp?docidStr=" + docidStr,400,500);
			return true;
		}else{
	    	alert("请选择要求保存版本的文档！");
	    	return false;
	   	}
	}
	//对一篇文档保存版本
	function addDocVer1(docId){
		//将选中的文档id拼成字符串
		var docidStr=docId + ":";
		openWin("add_document_version.jsp?docidStr=" + docidStr,400,500);
	}
	//版本管理
	function manageDocVer(docid)
	{
		//打开模态窗口选择目的频道
		openWin("dilogparent.jsp?docid=" + docid,800,500);
	}
	//导出单个文档
	function exportDoc(siteId,channelId,docId)
	{
		//将选中的文档id拼成字符串
		var docidStr=docId + ",";
		//form1.action="doc_export.jsp?siteid="+siteId+"&docids=" + docidStr;
	  //  form1.target="operIframe";
		//form1.submit();
		$.secutiry.dosubmit("form1",
						"doc_export.jsp?siteid="+siteId+"&docids=" + docidStr,
								"operIframe",
								"<%=rootpath%>");
		//打开模态窗口选择目的频道
		//window.open("doc_export.jsp?siteid="+siteId+"&docids=" + docidStr,"newwin","");
	}
	//设置置顶
	function addArrangeDoc(docid)
	{
		openWin("docArrange_add.jsp?docid=" + docid,555,240);
	}
	//文档置顶管理
	function arrangeDocM(channelId)
	{
		openWin("docArrange_manage.jsp?chnlid="+channelId,800,500);
	}
	//扩展字段
	function extfieldmanage(channelId)
	{
		parent.document.location.href = "doc_extfieldofsiteorchl_list.jsp?type=2&id="+channelId;
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
			//form1.action="document_del_submit.jsp?docidStr=" + docidStr;
	    //    form1.target="operIframe";
			//form1.submit();
			$.secutiry.dosubmit("form1",
						"document_del_submit.jsp?docidStr=" + docidStr,
								"operIframe",
								"<%=rootpath%>");
		}else{
	    	alert("请选择要求删除的文档！");
	    	return false;
	   	}
	}
	function auditAgreeDoc(docId,channelId){
		var idStr = ":" + docId + ":"+channelId;
		//参数auditFlag为审核意见，0表不同意，1表同意
		openWin("audit_add_comment.jsp?idStr=" + idStr + "&auditFlag=1",300,300);
	}
	function auditUnagreeDoc(docId,channelId){
		var idStr = ":" + docId + ":"+channelId;
		var addComment = window.confirm("要增加审核意见吗？");
		if(addComment==false){
			//参数auditFlag为审核意见，0表不同意，1表同意
			//form1.action = "auditDocHandle.jsp?idStr=" + idStr + "&auditFlag=0";
			//form1.target = "operIframe";
			//form1.submit();
			
			$.secutiry.dosubmit("form1",
						"auditDocHandle.jsp?idStr=" + idStr + "&auditFlag=0",
								"operIframe",
								"<%=rootpath%>");
		}
		else{
			openWin("audit_add_comment.jsp?idStr=" + idStr + "&auditFlag=0",300,300);
		}	
	}
	function publishDoc(docId){
		//form1.action = "publishDocHandle.jsp?flag=3&docId="+docId;
		//form1.target = "operIframe";
		//form1.submit();
		
		$.secutiry.dosubmit("form1",
						"publishDocHandle.jsp?flag=3&docId="+docId,
								"operIframe",
								"<%=rootpath%>");
	}
	function publishPreview(docId){
		//form1.action = "previewPubDocHandle.jsp?docId="+docId;
		//form1.target = "operIframe";
		//form1.submit();
		
		$.secutiry.dosubmit("form1",
						"previewPubDocHandle.jsp?docId="+docId,
								"operIframe",
								"<%=rootpath%>");
	}
	function edit(editLink,docid,siteid,channelName,channelId){
			openWin(editLink + "?docid=" + docid + "&siteid=" + siteid + "&channelName=" + channelName + "&channelId=" + channelId, screen.availWidth-20,screen.availHeight-50);
	}
</script>
<style type="text/css">
body {
	background-color: #ffffff;
scrollbar-face-color: #C9D1E4; 
scrollbar-shadow-color: #6B74B7; 
scrollbar-highlight-color: white; 
scrollbar-3dlight-color: #E100E1; 
scrollbar-darkshadow-color:#E100E1; 
scrollbar-arrow-color:#003492; 
scrollbar-base-color: #E100E1; 
scrollbar-track-color: #E9EDF3; 
}
</style>
</head>
<body topmargin="2" rightmargin="0" leftmargin="1" righttmargin="1">
<form name="form1" action="" method="post" >
<table width="100%" border="0" align="left" cellpadding="3" cellspacing="0" bordercolor="#B7CBE4" class="Datalisttable" id="docListTable">
<pg:listdata dataInfo="com.frameworkset.platform.cms.documentmanager.tag.NewDocList" keyName="NewDocList" />
<pg:pager maxPageItems="10" scope="request" data="NewDocList" isList="false">
	<tr class="cms_report_tr">
<!--		<td height='30' align=center style="width:5%">-->
<!--			<input  class="checkbox" type="checkBox" hideFocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')">		</td>-->
		<td width="8%">
			文档类型		</td>
		<td width="8%" height='30'>
			是否置顶		</td>
		<td width="36%">
			文档标题		</td>
		<td width="5%">
			所属频道		</td>
		<td width="20%">
			所属站点		</td>		
		<td width="33%">
			发稿日期		</td>
	</tr>
	<pg:param name="flag" />
	<pg:param name="subTitle" />	
	<pg:param name="keyword" />
	<pg:param name="docAbstract" />
	<pg:param name="docType" />
	<pg:param name="docLevel" />
	<pg:param name="creatTime" />
	<pg:param name="channelIds" />	
	<pg:notify>
		<tr class="labeltable_middle_tr_01">
			<td colspan=100 align='center'  height="18px">
				暂时没有新增文档
			</td>
		</tr>
	</pg:notify>
	<%
			ContextMenu contextmenu = new ContextMenuImpl();
	%>
	<pg:list>
	<%	
			int docid = dataSet.getInt("document_id");
			int chanel_id = dataSet.getInt("chanel_id");
			
			String addpath = cfm.getCustomFormFilename(chanel_id+"","2","1");
			String updatepath = cfm.getCustomFormFilename(chanel_id+"","2","2");
			if("doc_gather.jsp".equals(addpath)||"".equals(addpath))
				addpath = "/cms/docManage/doc_gather.jsp";
			else
				addpath = addpath;
			if("doc_gather_update.jsp".equals(updatepath)||"".equals(updatepath))
				updatepath = "/cms/docManage/doc_gather_update.jsp";
			else
				updatepath = updatepath;
			
			int doctype = dataSet.getInt("doctype");
			String channelName = dataSet.getString("channelName");
			int siteid = dataSet.getInt("siteid");
			
			String editLink = "doc_gather_update.jsp";
			switch(doctype){
				case 0 :
						break;
				case 1:
						editLink = "doc_gather_links_update.jsp?";
						break;
				case 2:
						editLink = "doc_gather_filedownload_update.jsp";
						break;
				case 3:
						editLink = "doc_gather_aggr_update.jsp";
						break;
			}
			DocumentManager dm = new DocumentManagerImpl();
			Map docOpers = dm.getDocOpers(docid,accesscontroler); 							
			boolean isArrangeDoc = dm.ArrangeDocExist(String.valueOf(docid));//是否是置顶
			
			Menu menu = new Menu();
			menu.setIdentity("opdoc_"+docid);
		
			//Menu.ContextMenuItem menuitem1 = new Menu.ContextMenuItem();
			//menuitem1.setName("编辑");
			//menuitem1.setLink("javascript:edit('" + editLink + "','" + docid + "','" + siteid + "','" + channelName + "','" + chanel_id + "')");
			//menuitem1.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_edit.gif");
			//menu.addContextMenuItem(menuitem1);
			
			//if(doctype == 0 || doctype == 1){
				//Menu.ContextMenuItem menuitem6 = new Menu.ContextMenuItem();
				//menuitem6.setName("预览");
				//menuitem6.setLink("javascript:viewDoc('" + docid + "',form1,'operIframe')");
				//menuitem6.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_fbyl.gif");
				//menu.addContextMenuItem(menuitem6);
			//}
			
			if(docOpers.get("deliver")!=null){
				Menu.ContextMenuItem menuitem2 = new Menu.ContextMenuItem();
				menuitem2.setName("送审");
				menuitem2.setLink("javascript:operateDoc('送审','" + docid + "','" + chanel_id + "','" + siteid + "','" + channelName + "')");
				menuitem2.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_ss.gif");
				menu.addContextMenuItem(menuitem2);
			}
			if(docOpers.get("subpublish")!=null && doctype==0){
				Menu.ContextMenuItem menuitem6 = new Menu.ContextMenuItem();
				menuitem6.setName("提交发布");
				menuitem6.setLink("javascript:operateDoc('提交发布','" + docid + "','" + chanel_id + "','" + siteid + "','" + channelName + "')");
				menuitem6.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_tjfb.gif");
				menu.addContextMenuItem(menuitem6);
			}
			if(accesscontroler.checkPermission(chanel_id+"",AccessControl.ADD_DOCVER_PERMISSION,AccessControl.CHANNELDOC_RESOURCE)||
				accesscontroler.checkPermission(siteid+"",AccessControl.ADD_DOCVER_PERMISSION,AccessControl.SITEDOC_RESOURCE)){
				Menu.ContextMenuItem menuitem8 = new Menu.ContextMenuItem();
				menuitem8.setName("保存版本");
				menuitem8.setLink("javascript:addDocVer1('" + docid + "')");
				menuitem8.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/plan.gif");
				menu.addContextMenuItem(menuitem8);
			}
			if(docOpers.get("manageDocVer")!=null){
				Menu.ContextMenuItem menuitem3 = new Menu.ContextMenuItem();
				menuitem3.setName("版本管理");
				menuitem3.setLink("javascript:manageDocVer('" + docid + "')");
				menuitem3.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_bbgl.gif");
				menu.addContextMenuItem(menuitem3);
			}
			if(docOpers.get("addArrangeDoc")!=null&&!isArrangeDoc){
				Menu.ContextMenuItem menuitem7 = new Menu.ContextMenuItem();
				menuitem7.setName("设置置顶");
				menuitem7.setLink("javascript:addArrangeDoc('" + docid + "')");
				menuitem7.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_szzd.gif");
				menu.addContextMenuItem(menuitem7);
			}
			if(accesscontroler.checkPermission(chanel_id+"",AccessControl.TRANSMIT_PERMISSION,AccessControl.CHANNELDOC_RESOURCE)||
				accesscontroler.checkPermission(siteid+"",AccessControl.TRANSMIT_PERMISSION,AccessControl.SITEDOC_RESOURCE)){
				Menu.ContextMenuItem menuitem9 = new Menu.ContextMenuItem();
				menuitem9.setName("转发");
				menuitem9.setLink("javascript:transmitDoc('" + siteid + "','" + chanel_id + "','" + docid + "')");
				menuitem9.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_tjfb.gif");
				menu.addContextMenuItem(menuitem9);
			}
			if(accesscontroler.checkPermission(chanel_id+"",AccessControl.EXPORTDOC_PERMISSION,AccessControl.CHANNELDOC_RESOURCE)||
				accesscontroler.checkPermission(siteid+"",AccessControl.EXPORTDOC_PERMISSION,AccessControl.SITEDOC_RESOURCE)){
				Menu.ContextMenuItem menuitem9 = new Menu.ContextMenuItem();
				menuitem9.setName("导出");
				menuitem9.setLink("javascript:exportDoc('" + siteid + "','" + chanel_id + "','" + docid + "')");
				menuitem9.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/tpl_reimport.gif");
				menu.addContextMenuItem(menuitem9);
			}
			if(accesscontroler.checkPermission(chanel_id+"",AccessControl.TRASH_PERMISSION,AccessControl.CHANNEL_RESOURCE)||
				accesscontroler.checkPermission(siteid+"",AccessControl.TRASH_PERMISSION,AccessControl.SITECHANNEL_RESOURCE)){
				Menu.ContextMenuItem menuitem4 = new Menu.ContextMenuItem();
				menuitem4.setName("回收");
				menuitem4.setLink("javascript:garbageOneDoc('" + docid + "')");
				menuitem4.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/trash_small.gif");
				menu.addContextMenuItem(menuitem4);
			}
			
			if(accesscontroler.checkPermission(chanel_id+"",AccessControl.DELETE_PERMISSION,AccessControl.CHANNEL_RESOURCE)||
				accesscontroler.checkPermission(siteid+"",AccessControl.DELETE_PERMISSION,AccessControl.SITECHANNEL_RESOURCE)){
				Menu.ContextMenuItem menuitem5 = new Menu.ContextMenuItem();
				menuitem5.setName("删除");
				menuitem5.setLink("javascript:delOneDoc('" + docid + "','" + siteid + "')");
				menuitem5.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_del.gif");
				menu.addContextMenuItem(menuitem5);	
			}					
	
			contextmenu.addContextMenu(menu);
	%>
		<input name="docid" value="<pg:cell colName="document_id" defaultValue=""/>" type="hidden">
		<tr class="cms_data_tr" id="<pg:cell colName="document_id" defaultValue=""/>" onMouseOver="high(this)" onMouseOut="unhigh(this)">
<!--			<td class="tablecells" align=center height='30' width="5%">-->
<!--				<input hideFocus class="checkbox" type="checkbox" name="ID" value="<pg:cell colName="document_id" defaultValue=""/>">-->
<!--			</td>-->
			<td class="tablecells" height='30' width="5%">
				<pg:equal colName="doctype" value="0">
					<img title="HTML文档" src="../images/doc_html.gif" width="20" height="20">				</pg:equal>
				<pg:equal colName="doctype" value="1">
					<img title="外部链接类型文档" src="../images/doc_link.gif" width="20" height="20">
				</pg:equal>
				<pg:equal colName="doctype" value="2">
					<img title="下载文件类型" src="../images/doc_downloadfile.gif" width="15" height="16">
				</pg:equal>
				<pg:equal colName="doctype" value="3">
					<img title="聚合文件类型" src="../images/doc_aggr.gif" width="15" height="16">
				</pg:equal>
			</td>
<!--			<td id="doc<%=docid%>" class="tablecells" align="left" height="25" style="cursor:hand"  -->
<!--					onClick="viewDoc(<pg:cell colName="document_id" defaultValue=""/>,form1,'operIframe')" >-->
<!--				<pg:cell colName="subtitle" defaultValue="" />-->
<!--			</td>-->
			<td>
				<%if(isArrangeDoc){%><span class="red_star">置顶</span><%}%>
			</td>
			<pg:equal colName="doctype" value="0">
			<td id="opdoc_<%=docid%>" class="tablecells" style="cursor:hand" 

									onclick="openWin('<%=rootpath%><%=updatepath%>?docid=<pg:cell colName="document_id" defaultValue=""/>&siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=chanel_id%>',screen.availWidth-20,screen.availHeight-50)">
				<pg:cell colName="subtitle" defaultValue="" />
			</pg:equal>
			<pg:equal colName="doctype" value="1">
			<td id="opdoc_<%=docid%>" class="tablecells" style="cursor:hand" 
					
					onclick=" openWin('<%=rootpath%>/cms/docManage/doc_gather_links_update.jsp?siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=chanel_id%>&docid=<pg:cell colName="document_id" defaultValue=""/>',screen.availWidth-20,screen.availHeight-50)">
				<pg:cell colName="subtitle" defaultValue="" />
			</pg:equal>
			<pg:equal colName="doctype" value="2">
			<td id="opdoc_<%=docid%>" class="tablecells" style="cursor:hand" 
					
					onclick="openWin('<%=rootpath%>/cms/docManage/doc_gather_filedownload_update.jsp?docid=<pg:cell colName="document_id" defaultValue=""/>&siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=chanel_id%>',screen.availWidth-20,screen.availHeight-50)">
				<pg:cell colName="subtitle" defaultValue="" />
			</pg:equal>
			<pg:equal colName="doctype" value="3">
			<td id="opdoc_<%=docid%>" class="tablecells" style="cursor:hand" 
					
					onclick="openWin('<%=rootpath%>/cms/docManage/doc_gather_aggr_update.jsp?docid=<pg:cell colName="document_id" defaultValue=""/>&siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=chanel_id%>',screen.availWidth-20,screen.availHeight-50)">
				<pg:cell colName="subtitle" defaultValue="" />
			</pg:equal>
			</td>

			<td class="tablecells" width="10%">
				<pg:cell colName="channelName" defaultValue="" />
			</td>
			<td class="tablecells" width="8%">
				<pg:cell colName="siteName" defaultValue="" />
			</td>
			<td class="tablecells" width="10%">
				<pg:cell colName="createTime" dateformat="yyyy/MM/dd HH:mm:ss" defaultValue="" />
			</td>
			
		</tr>	
	</pg:list>
	<%
			request.setAttribute("opdoc",contextmenu);
	%>
	<pg:contextmenu context="opdoc" enablecontextmenu="true" scope="request"/>
	<tr>
		<td colspan=10><div class="Data_List_Table_Bottom">共
			  <pg:rowcount />条记录<pg:index /></div></td>
	</tr>
	<input id="queryString" name="queryString" value="<pg:querystring/>" type="hidden">
</pg:pager>
</table>
</form>
<iframe height="0" width="0" name="operIframe"></iframe>
</body>
</html>