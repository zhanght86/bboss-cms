<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="java.util.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.customform.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.*"%>
<%@ page import="com.frameworkset.platform.cms.util.CMSUtil,com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>


<%	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String flag =request.getParameter("flag");
	String publisher =request.getParameter("publisher");
	String docTitle =request.getParameter("docTitle");
	String channelIds =request.getParameter("channelIds");
    
    //外部注入发布管理
    String uuid = CMSUtil.getUUID();
    PublishMonitor monitor = PublishMonitor.createPublishMonitor();
    session.setAttribute(uuid,monitor);
%>
<html>
<head>
<title>
	新文档列表
</title>
<link  href="../inc/css/cms.css" rel="stylesheet" type="text/css"></link>
<script language="javascript" src="../inc/js/func.js"></script>
<script language="javascript" src=src="../../sysmanager/include/pager.js" type="text/javascript"></script>
<script type="text/javascript">
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
	//删除一篇文档
	function delDoc1(docId,siteid){
		if(confirm("您确定要删除文档吗？")){
			docidStr = docId + ";";
			form1.action="document_del_submit.jsp?docidStr=" + docidStr + "&siteid=" + siteid;
		    form1.target="operIframe";
			form1.submit();
		} 
	}
	//回收一篇文章
	function garbageOneDoc(docId){
		if(confirm("您确定要回收文档吗？")){
			form1.action="docGarbageHandle.jsp?docidStr=:" + docId + ";";
			form1.target="operIframe";
			form1.submit();
		}
	}
	//恢复
	function recover(docId){
		form1.action = "docPublishingHandle.jsp?action=recover&docId=" + docId;
		form1.target="operIframe";
		form1.submit();
	}
	//批量恢复
	function recoverAll(){
		if(haveSelect('ID')){
			form1.action = "docPublishingHandle.jsp?action=recover";
			form1.target="operIframe";
			form1.submit();
		}else{
			alert("请至少要选择一篇文档！");
		}
	}
	//全部恢复
	function recoverAllDoc()
	{
		if(window.confirm("你确定要全部恢复吗？"))
		{
			form1.action = "docPublishingHandle.jsp?action=recover&recoverall=recoverall";
			form1.target="operIframe";
			form1.submit();
		}
	}
	//重发
	function publishAgain(docId){
		form1.action = "publishDocHandle.jsp?flag=13&docId=" + docId;
		form1.target="operIframe";
        currentPageUrl();
        var path = "doc_publish_info.jsp?uuid=<%=uuid%>";
        var featrue = "dialogHeight:310px;dialogWidth:450px";
        winOpen = window.showModelessDialog(path,window,featrue);
		//form1.submit();
	}
    function closeSubWindow(){ 
        winOpen.close();
        document.all("cleanSession").src = "../cleanSession.jsp?uuid=<%=uuid%>";
   
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
</script>
<style type="text/css">
body {
	background-color: #ffffff;
}
</style>
</head>
<body topmargin="2" rightmargin="0" leftmargin="1" righttmargin="1">
<div id="divProcessing" style="position:absolute;width:40%;height:20px;z-index:1;
			background-color:#F0f0f0;layer-background-color:#F0f0f0;left:22%;top:80%;display:none;"  class="font">   
	    <marquee direction="left" width="250" behavior="alternate"><span class=STYLE1>正在发布中，请稍等……</span></marquee>
</div>

<form name="form1" action="" method="post" >
    <input type="hidden" name="uuid" value="<%=uuid%>">
    <input type="hidden" name="isRecordValue">
    <input type="hidden" name="recursionPublish">
    <input type="hidden" name="clearCache">
    <input type="hidden" name="url">
<table width="100%" border="1" align="left" cellpadding="3" cellspacing="0" bordercolor="#B7CBE4" class="Datalisttable" id="docListTable">
	 <tr>
			<td height="25"  colspan=9  background="../images/data_list_tHeadbg.jpg" style="text-align:left; background:url(../images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7">
				<div class="DocumentOperT">文档操作：</div><div class="DocumentOperbox">
				<a style="cursor:hand" onClick="recoverAll()"><div class="DocumentOper">恢复</div></a> 
				<div class="DocumentOperbox">
				<a style="cursor:hand" onClick="recoverAllDoc()"><div class="DocumentOper">全部恢复</div></a>
				</div>	
			</td>
	</tr>
	<pg:listdata dataInfo="PublishingDocList" keyName="PublishingDocList" />
	<pg:pager maxPageItems="10" scope="request" data="PublishingDocList" isList="false">
	<tr class="cms_report_tr">
		<td width="2%" height="30" align="center">
			<input  class="checkbox" type="checkBox" hideFocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')"></td>
		<td width="8%" height="30">
			文档类型		</td>
		<td width="34%" height="30">
			文档标题		</td>
		<td width="8%" height="30">
			所属频道		</td>
		<td width="20%" height="30">
			所属站点		</td>	
		<td width="10%" height="30">
			发布之前状态		</td>	
		<td width="10%" height="30">
			发布人		</td>	
		<td width="18%" height="30">
			发稿日期		</td>
	</tr>
	
	<pg:param name="flag" />
	<pg:param name="docTitle" />
	<pg:param name="publisher" />
	<pg:param name="docTitle" />
	<pg:param name="channelIds" />

	<pg:notify>
		<tr class="labeltable_middle_tr_01">
			<td colspan=100 align='center' height="30">
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
			int doctype = dataSet.getInt("doctype");
			int chanel_id = dataSet.getInt("chanel_id");
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
			
			Menu menu = new Menu();
			menu.setIdentity("doc"+docid);
		
			Menu.ContextMenuItem menuitem1 = new Menu.ContextMenuItem();
			menuitem1.setName("恢复");										//无需权限控制，任何人都可以恢复，这是一个发布异常的处理操作。
			menuitem1.setLink("javascript:recover('" + docid + "',form1,'operIframe')");
			menuitem1.setIcon(request.getContextPath() +"/sysmanager/images/actions.gif");
			menu.addContextMenuItem(menuitem1);
			
			if(accesscontroler.checkPermission(chanel_id+"",AccessControl.DOCPUBLISH_PERMISSION,AccessControl.CHANNELDOC_RESOURCE)||
				 accesscontroler.checkPermission(siteid+"",AccessControl.DOCPUBLISH_PERMISSION,AccessControl.SITEDOC_RESOURCE)){
				Menu.ContextMenuItem menuitem3 = new Menu.ContextMenuItem();
				menuitem3.setName("重发");
				menuitem3.setLink("javascript:publishAgain('" + docid + "')");
				menuitem3.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_bbgl.gif");
				menu.addContextMenuItem(menuitem3);
			}
			
			if(accesscontroler.checkPermission(chanel_id+"",AccessControl.TRASH_PERMISSION,AccessControl.CHANNEL_RESOURCE)||
				accesscontroler.checkPermission(siteid+"",AccessControl.TRASH_PERMISSION,AccessControl.SITECHANNEL_RESOURCE)){
				Menu.ContextMenuItem menuitem4 = new Menu.ContextMenuItem();
				menuitem4.setName("回收");
				menuitem4.setLink("javascript:garbageOneDoc('" + docid + "')");
				menuitem4.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/trash_small.gif");
				menu.addContextMenuItem(menuitem4);
			}
			if(accesscontroler.checkPermission(chanel_id+"",AccessControl.DELETE_PERMISSION,AccessControl.CHANNEL_RESOURCE)
				|| accesscontroler.checkPermission(siteid+"",AccessControl.DELETE_PERMISSION,AccessControl.SITECHANNEL_RESOURCE)){
				Menu.ContextMenuItem menuitem6 = new Menu.ContextMenuItem();
				menuitem6.setName("删除");
				menuitem6.setLink("javascript:delDoc1('" + docid + "','" + siteid + "')");
				menuitem6.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_del.gif");
				menu.addContextMenuItem(menuitem6);
			}
		
			contextmenu.addContextMenu(menu);
	%>
		<input name="docid" value="<pg:cell colName="document_id" defaultValue=""/>" type="hidden">
		<tr class="cms_data_tr" id="<pg:cell colName="document_id" defaultValue=""/>" onMouseOver="high(this)" onMouseOut="unhigh(this)">
			<td class="tablecells" height='30'>
				<input hideFocus onClick="checkOne('checkBoxAll','ID')" type="checkbox"  class="checkbox" name="ID" 
					value="<%=docid%>">			</td>
			<td class="tablecells" height='30'>
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
			<pg:equal colName="doctype" value="0">
				<td id="doc<%=docid%>" class="tablecells"  style="cursor:hand" 
					onClick="viewDocInfo(<pg:cell colName="document_id" defaultValue=""/>,<pg:cell colName="doctype" defaultValue=""/>);">
					<pg:cell colName="subtitle" defaultValue="" />
				</td>
			</pg:equal>
			<pg:equal colName="doctype" value="1">
				<td id="doc<%=docid%>" class="tablecells"   style="cursor:hand" 
					onMouseOver="high(this)" onMouseOut="unhigh(this)"
					onclick="viewDocInfo(<pg:cell colName="document_id" defaultValue=""/>,<pg:cell colName="doctype" defaultValue=""/>);">
					<pg:cell colName="subtitle" defaultValue="" />
				</td>
			</pg:equal>
			<pg:equal colName="doctype" value="2">
				<td id="doc<%=docid%>" class="tablecells"  style="cursor:hand" 
					onMouseOver="high(this)" onMouseOut="unhigh(this)"
					onclick="viewDocInfo(<pg:cell colName="document_id" defaultValue=""/>,<pg:cell colName="doctype" defaultValue=""/>);">
					<pg:cell colName="subtitle" defaultValue="" />
				</td>
			</pg:equal>
			<pg:equal colName="doctype" value="3">
				<td id="doc<%=docid%>" class="tablecells"   style="cursor:hand" 
					onMouseOver="high(this)" onMouseOut="unhigh(this)"
					onclick="viewDocInfo(<pg:cell colName="document_id" defaultValue=""/>,<pg:cell colName="doctype" defaultValue=""/>);">
					<pg:cell colName="subtitle" defaultValue="" />
				</td>
			</pg:equal>
			<td class="tablecells" >
				<pg:cell colName="channelName" defaultValue="" />
			</td>
			<td class="tablecells" >
				<pg:cell colName="siteName" defaultValue="" />
			</td>
			
			<td class="tablecells" >
				<pg:cell colName="docOldStatus" defaultValue="" />
			</td>
			<td class="tablecells" >
				<pg:cell colName="username" defaultValue="" />
			</td>
			<td class="tablecells" >
				<pg:cell colName="publishTime" dateformat="yyyy/MM/dd HH:mm:ss" defaultValue="" />
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
<iframe name="cleanSession" id="cleanSession"  width="0" height="0"  style="display:none" ></iframe>
</body>
</html>