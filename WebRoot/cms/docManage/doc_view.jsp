<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.common.poolman.DBUtil"%>
<%@page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.templatemanager.*
,com.frameworkset.platform.cms.container.Template,com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@page import="com.frameworkset.platform.cms.driver.htmlconverter.*"%>
<%@page import="com.frameworkset.platform.cms.driver.i18n.*"%>
<%@page import="java.util.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl,java.util.List"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>

<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	String userid = accesscontroler.getUserID();
   
	String docid = request.getParameter("docid");
	String channelname = "";//request.getParameter("channelName");
	String siteid = "";//request.getParameter("siteid");
	String channelId = "";//request.getParameter("channelId");
	
	DocumentManager dmi = new DocumentManagerImpl();
	ChannelManager cm = new ChannelManagerImpl();
	SiteManager siteManager = new SiteManagerImpl();

	Document document = dmi.getDoc(docid);
	channelId = String.valueOf(document.getChanel_id());
	Channel channel = cm.getChannelInfo(channelId);
	channelname = channel.getName();
	siteid = String.valueOf(channel.getSiteId());
	
	//在返工文档编辑时传来rebound，在审核时传来audit，其他出来为空(返工文档是有任务id的)
   	String flag =  request.getParameter("flag");
   	int taskid = 0;
   	String taskidStr="";
   	if(flag!=null && flag.equals("rebound")){
   		taskid = dmi.hasTask(Integer.parseInt(userid),Integer.parseInt(docid),2);
   	}
   	else if(flag!=null && flag.equals("audit")){
   		taskid = dmi.hasTask(Integer.parseInt(userid),Integer.parseInt(docid),1);
   		taskidStr = request.getParameter("taskidStr");
   	}

	String status = String.valueOf(document.getStatus());
	
	String sitename = siteManager.getSiteInfo(siteid).getName();
	String docsource = String.valueOf(document.getDocsource_id());
	String tid = String.valueOf(document.getDetailtemplate_id());
	String docLevelId = String.valueOf(document.getDoc_level());//文档级别
	//图片，附件等的保存路径 docpath
	String sitedir = siteManager.getSiteInfo(siteid).getSiteDir();//频道相对路径
	String relativePath = cm.getChannelInfo(channelId).getChannelPath();//站点相对路径
	String docpath = sitedir + "/_webprj/" + relativePath + "/content_files";

	//处理文档内容
	CmsLinkProcessor processor = new CmsLinkProcessor(request,relativePath,sitedir);
	processor.setHandletype(CmsLinkProcessor.PROCESS_READCONTENT);
	try {
		String content = processor.process(document.getContent(),CmsEncoder.ENCODING_UTF_8);
		document.setContent(content);
	} catch (Exception e) {
		e.printStackTrace();
	}

	//get文档的相关文档的信息
	String partId = "";
	String partName = "";
	String[] doclists =  dmi.getDocRelatedString(Integer.parseInt(docid));
	for(int i=0;i<doclists.length;i++)
	{
		String[] tem = doclists[i].split("№");
		partId += tem[0] + "№";
		partName += tem[1] + "№";
	}
	//get文档的相关附件的信息
	String attId = "";
	String attDesc = "";
	String attUrl = "";
	String ofilename = "";
	String[] atts = dmi.getAllRelationOfDocument2String(Integer.parseInt(docid),2);
	for(int i=0;i<atts.length;i++)
	{
		String[] tem = atts[i].split("№");//对应关系
		attId += tem[0] + "№";
		attDesc += tem[4] + "№";
		attUrl += tem[2] + "№";
		ofilename += tem[5] + "№";
	}
	//get文档的相关图片的信息
	String attId2 = "";
	String attDesc2 = "";
	String attUrl2 = "";
	String ofilename2 = "";
	String[] atts2 = dmi.getAllRelationOfDocument2String(Integer.parseInt(docid),3);
	for(int i=0;i<atts2.length;i++)
	{
		String[] tem = atts2[i].split("№");//对应关系
		attId2 += tem[0] + "№";
		attDesc2 += tem[4] + "№";
		attUrl2 += tem[2] + "№";
		ofilename2 += tem[5] + "№";
	}
%>
<%!/**
   * 针对HTML的特殊字符转义
   * @param value String
   * @return String
   */
  String filterStr(String value) {
    if (value == null) {
      return (null);
    }
    char content[] = new char[value.length()];
    value.getChars(0, value.length(), content, 0);
    StringBuffer result = new StringBuffer(content.length + 50);
    for (int i = 0; i < content.length; i++) {
      switch (content[i]) {
        case '<':
          result.append("&lt;");
          break;
        case '>':
          result.append("&gt;");
          break;
        case '&':
          result.append("&amp;");
          break;
        case '"':
          result.append("&quot;");
          break;
        case '\'':
          result.append("&#39;");
          break;
        case '\\':
          result.append("\\\\");
          break;
        default:
          result.append(content[i]);
      }
    }
	return (result.toString());
  }
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="../inc/js/func.js"></script>
<script type="text/javascript" src="../../public/datetime/calender.js" language="javascript"></script>
<style type="text/css">
<!--
.STYLE7 {color:#003398; font-size: 12px;}
#Layer1 {
	position:absolute;
	left:0px;
	top:0px;
	width:113px;
	height:127px;
	z-index:1;
}
.STYLE8 {color: #0000FF}

-->
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>CMS 文档管理</title>
</head>
<script language="javascript">
	
	//字符转换
	function HTMLEncode(text){
		//text = text.replace(/"/g, "&quot;");
		return text;
	}
	//选择图片新闻的概览图片
	var fileFlag;
	function chooseImage(){
		fileFlag = "pic";
		openWin("../channelManage/chooseImageFrameset.jsp",520,580);
	}
	function setImage(uri){
		if(fileFlag == "pic")
			document.all("picpath").value  = uri;
		if(fileFlag == "media")
			document.all("mediapath").value  = uri;
	}
	//选择多媒体文件
	function choosemedia()
	{
		fileFlag = "media";
		openWin("../channelManage/chooseImageFrameset.jsp?fileFlag=media",520,580);
	}
	//预览模板
	function preview()
	{
		var tplId = document.all.detailtemplate_id.value;
		window.open("../templateManage/template_previewbyid.jsp?tplId=" + tplId + "&siteId=<%=siteid%>");
	}
	//静态分页置标
	/**将图片转换成分页置标**/
	function setPageTagNone(){
		var arPageTags = eWebEditor1.eWebEditor.document.all("_TRSPage");
		if(arPageTags == null)return;
		if(arPageTags.length){
			for(var i =arPageTags.length-1; i>=0; i--){
				var objPageTag = arPageTags[i];
				if(objPageTag == null)continue;

				var pageTag = eWebEditor1.eWebEditor.document.createElement("TRS_PAGE_SEPARATOR");		
				//pageTag.innerHTML = "TRS静态分页置标";
				objPageTag.replaceNode(pageTag);
			}
		}else{
			var pageTag = eWebEditor1.eWebEditor.document.createElement("TRS_PAGE_SEPARATOR");		
			//pageTag.innerHTML = "TRS静态分页置标";
			arPageTags.replaceNode(pageTag);
		}

	}
	
	//返回
	function back() {
		window.close();
	}

	//焦点放在第一个text
	function init(){
		var ptpl = "<%=document.getParentDetailTpl()%>";
		myform.title.focus();
		document.all.tab1.style.color = "#FF6600";
		if(ptpl==0)
		{
			document.all.selecttpl[0].checked = true;
		}
		if(ptpl==1)
		{
			document.all.selecttpl[1].checked = true;
			document.all.detailtemplate_id.disabled = true;
		}
	}
	//光标是停在文本框文字的最后
	function focusend() 
	{ 
		var e = event.srcElement; 
		var r =e.createTextRange(); 
		r.moveStart("character",e.value.length); 
		r.collapse(true); 
		r.select(); 
	}
	//tab切换
	function tabClick(i){
		if(i==1){
			document.all.docxz.style.display = "none";
			document.all.docattachlist.style.display = "none";
			document.all.docattachlist2.style.display = "none";
			document.all.docextfielslist.style.display = "none";
			document.all.eWebEditor1.style.display = "";
			document.all.tab1.style.color = "#FF6600";
			document.all.tab2.style.color = "";
			document.all.tab3.style.color = "";
			document.all.tab4.style.color = "";
			document.all.tab5.style.color = "";
		}
		if(i==2){
			document.activeElement.blur();
			//myform.title.focus();//解决iframe焦点问题
			document.all.eWebEditor1.style.display = "none";
			document.all.docattachlist.style.display = "none";
			document.all.docattachlist2.style.display = "none";
			document.all.docextfielslist.style.display = "none";
			document.all.docxz.style.display = "";
			document.all.docxz.style.width = "95%";
			document.all.docxz.style.height = "300px";
			document.all.tab1.style.color = "";
			document.all.tab2.style.color = "#FF6600";
			document.all.tab3.style.color = "";
			document.all.tab4.style.color = "";
			document.all.tab5.style.color = "";
		}
		if(i==3){
			document.activeElement.blur();
			//myform.title.focus();//解决iframe焦点问题
			document.all.eWebEditor1.style.display = "none";
			document.all.docxz.style.display = "none";
			document.all.docattachlist2.style.display = "none";
			document.all.docextfielslist.style.display = "none";
			document.all.docattachlist.style.display = "";
			document.all.docattachlist.style.width = "95%";
			document.all.docattachlist.style.height = "300px";
			document.all.tab1.style.color = "";
			document.all.tab2.style.color = "";
			document.all.tab3.style.color = "#FF6600";
			document.all.tab4.style.color = "";
			document.all.tab5.style.color = "";
		}
		if(i==4){
			document.activeElement.blur();
			//myform.title.focus();//解决iframe焦点问题
			document.all.eWebEditor1.style.display = "none";
			document.all.docxz.style.display = "none";
			document.all.docattachlist.style.display = "none";
			document.all.docextfielslist.style.display = "none";
			document.all.docattachlist2.style.display = "";
			document.all.docattachlist2.style.width = "95%";
			document.all.docattachlist2.style.height = "300px";
			document.all.tab1.style.color = "";
			document.all.tab2.style.color = "";
			document.all.tab3.style.color = "";
			document.all.tab4.style.color = "#FF6600";
			document.all.tab5.style.color = "";
		}
		if(i==5){
			document.activeElement.blur();
			//myform.title.focus();//解决iframe焦点问题
			document.all.eWebEditor1.style.display = "none";
			document.all.docxz.style.display = "none";
			document.all.docattachlist.style.display = "none";
			document.all.docattachlist2.style.display = "none";
			document.all.docextfielslist.style.display = "";
			document.all.docextfielslist.style.width = "95%";
			document.all.docextfielslist.style.height = "300px";
			document.all.tab1.style.color = "";
			document.all.tab2.style.color = "";
			document.all.tab3.style.color = "";
			document.all.tab4.style.color = "";
			document.all.tab5.style.color = "#FF6600";
		}
	}
	//初始化相关文档table表的数据
	function initRelatedDoc()
	{
		docxz.document.all("buttons").parentElement.style.display = "none";
		var part_id = "<%=partId%>";
		var part_name = "<%=filterStr(partName)%>";
		
		if(part_id!=""&&part_name!="")
		{
			var ids = new Array();
			var names = new Array();
			ids = part_id.split("№");
			names = part_name.split("№");
			var tableatt = docxz.document.all("chndoclist");
			for(var i=0;i<ids.length-1;i++)
			{
				var row1 = tableatt.insertRow(i+1);
				var td1 = docxz.document.createElement("td");
				var td2 = docxz.document.createElement("td");

				td1.innerHTML="<input type='checkBox' name='id' value="+(i+1)+" hideFocus=true onclick='checkOne(id)'/>";		 
				var docid = "<input type='hidden' name='docid' value='"+ids[i]+"' />";
				//var channelId = "<input type='hidden' name='channelId' value='"+obj[1]+"' />";
				td2.innerHTML = docid + names[i];
				row1.appendChild(td1);
				row1.appendChild(td2);
			}
		}
	}
	//初始化相关附件table表的数据
	function initAtt()
	{
		docattachlist.document.all("buttons").parentElement.style.display = "none";
		var att_id = "<%=attId%>";
		var att_desc = "<%=attDesc%>";
		var att_url = "<%=attUrl%>";
		var att_ofilename = "<%=ofilename%>";
		
		if(att_id!=""&&att_url!="")
		{
			var ids = new Array();
			var descs = new Array();
			var urls = new Array();
			var ofilenames = new Array();

			ids = att_id.split("№");
			descs = att_desc.split("№");
			urls = att_url.split("№");
			ofilenames = att_ofilename.split("№");

			var tableatt = docattachlist.document.all("doc_attach_list");
			for(var i=0;i<ids.length-1;i++)
			{
				var row1 = tableatt.insertRow(i+1);
				var td1 = docattachlist.document.createElement("td");
				var td2 = docattachlist.document.createElement("td");
				var td3 = docattachlist.document.createElement("td");

				td1.innerHTML="<input type='checkBox' name='id' value="+ids[i]+" hideFocus=true onclick='checkOne(id)'/>";		 
				td2.innerHTML = "<input type='text' name='description' value='"+descs[i]+"'/>" +
					"<input type='hidden' name='originalFilename' value='"+ofilenames[i]+"'/>"
					+ "<input type='hidden' name='url' value='"+ urls[i]+"'/>";
				td3.innerHTML = "<a href='#' onclick=downloadattrs('../siteResource/<%=docpath%>/"+urls[i]+"')>"+urls[i]+"</a>";
				row1.appendChild(td1);
				row1.appendChild(td2);
				row1.appendChild(td3);
			}
		}
	}
	//初始化相关图片table表的数据
	function initAttPic()
	{
		//docattachlist2.document.all("buttons").style.display = "none";
		docattachlist2.document.all("buttons").parentElement.style.display = "none";
		var att_id = "<%=attId2%>";
		var att_desc = "<%=attDesc2%>";
		var att_url = "<%=attUrl2%>";
		var att_ofilename = "<%=ofilename2%>";
		
		if(att_id!=""&&att_url!="")
		{
			var ids = new Array();
			var descs = new Array();
			var urls = new Array();
			var ofilenames = new Array();

			ids = att_id.split("№");
			descs = att_desc.split("№");
			urls = att_url.split("№");
			ofilenames = att_ofilename.split("№");

			var tableatt = docattachlist2.document.all("doc_attach_list");
			for(var i=0;i<ids.length-1;i++)
			{
				var row1 = tableatt.insertRow(i+1);
				var td1 = docattachlist2.document.createElement("td");
				var td2 = docattachlist2.document.createElement("td");
				var td3 = docattachlist2.document.createElement("td");

				td1.innerHTML="<input type='checkBox' name='id' value="+ids[i]+" hideFocus=true onclick='checkOne(id)'/>";		 
				td2.innerHTML = "<input type='text' name='description' value='"+descs[i]+"'/>" +
					"<input type='hidden' name='originalFilename' value='"+ofilenames[i]+"'/>"
					+ "<input type='hidden' name='url' value='"+ urls[i]+"'/>";
				//td3.innerHTML = "<a href='../siteResource/<%=docpath%>/"+urls[i]+"'>"+urls[i]+"</a>";
				td3.innerHTML = "<a href='#' onclick=downloadattrs('../siteResource/<%=docpath%>/"+urls[i]+"')>"+urls[i]+"</a>";
				row1.appendChild(td1);
				row1.appendChild(td2);
				row1.appendChild(td3);
			}
		}
	}
	//初始化相关table
	function document.onreadystatechange()
	{
		if (document.readyState!="complete") 
			return;
		initRelatedDoc();
		initAtt();
		initAttPic();
	}
	//响应rodio事件
	function changtpl(type)
	{
		if(type==0)
			document.all.detailtemplate_id.disabled = false;
		else
			document.all.detailtemplate_id.disabled = true;
	}
	function publishPreview(){
		myform.method="post";
		myform.action="previewPubDocHandle.jsp?docId=<%=docid%>";
		myform.submit();
	}
	function seeAdvice(channelId,docid,taskid){
		openWin("<%=rootpath%>/cms/docManage/see_audit_advice.jsp?channelId=<%=channelId%>&docid=<%=docid%>&taskid=<%=taskid%>" ,400,550);
	}
	function delive(){
		saveform("0");
		//flag为1表示新稿文档的称颂,为2表示返工文档的呈送
		openWin("<%=rootpath%>/cms/docManage/doc_AuditorList.jsp?flag=2&channelId=<%=channelId%>&docid=<%=docid%>&taskid=<%=taskid%>");
		var str = window.dialogArguments.location.href;
		var end = str.indexOf("?");
		var strArray;
		if(end != -1)
			strArray= str.slice(0,end);
		else
			strArray = str;
		window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
		window.close();
	}
	function agree(){
		var re;
		//参数auditFlag为审核意见，0表不同意，1表同意
		re = openWin("<%=rootpath%>/cms/docManage/audit_add_comment.jsp?idStr=<%=taskidStr%>&auditFlag=1",400,550);
		if(re == "cf"){ 
			var str = window.dialogArguments.location.href;
			var end = str.indexOf("?");
				var strArray;
				if(end != -1)
					strArray= str.slice(0,end);
				else
					strArray = str;
			window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
			window.close();
		}
	}
	function disagree(){
		var re;
		//var addComment = window.confirm("要增加审核意见吗？");
		//if(addComment==false){
		//	re = "cf";
		//	myform.method="post";
		//	//参数auditFlag为审核意见，0表不同意，1表同意
		//	myform.action = "<%=rootpath%>/cms/docManage/auditDocHandle.jsp?idStr=<%=taskidStr%>&auditFlag=0";
		//	myform.submit();
		//}
		//else{
			re = openWin("<%=rootpath%>/cms/docManage/audit_add_comment.jsp?idStr=<%=taskidStr%>&auditFlag=0",400,550);
		//}
		if(re == "cf"){
			var str = window.dialogArguments.location.href;
			var end = str.indexOf("?");
				var strArray;
				if(end != -1)
					strArray= str.slice(0,end);
				else
					strArray = str;
			window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
			window.close();	
		}
	}
	function changSelect(o)
	{
	   document.all.inputdocsource.value=document.all.docsource_id.options(document.all.docsource_id.selectedIndex).text;
	   document.all.inputdocsource.focus();
	   document.all.inputdocsource.select();
	}
</script>
<!--下面的代码 使得按 F5会刷新modal页面-->
<base target="_self">
<body  bottommargin="0"   bgcolor="#F3F4F9" onkeydown="if(event.keyCode==116){reload.click()}" rightmargin="0" leftmargin="0" topmargin="0" onload="init()">
<script language="javascript">
	document.write("<a id='reload' href='" + document.location.href + "' style='display:none'>reload...</a>");
</script>
<!--上面的代码 使得按 F5会刷新modal页面-->
	<form target="updategather" name="myform" >
		<input name="doctype" value="0" type=hidden>
	  	<input name="docid" value="<%=docid%>" type=hidden>
	  	<input name="siteid" value="<%=siteid%>" type=hidden>
	  	<input name="channelName" value="<%=channelname%>" type=hidden>
	  	<input name="channelId" value="<%=channelId%>" type=hidden>
	  	<input name="isnewdocsource" value="0" type="hidden"><!--是否新稿源-->
		<input name="filepaths" value="" type=hidden><!--文件的新增时间，作为文件名，拼成一个串-->
		<input name="otherfilepaths" value="" type=hidden><!--用于维护更新remote图片的状态-->
		<input name="ofilepaths" value="" type=hidden><!--原本的url-->
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
							        <tr>
							            <td height="50" colspan="3" valign="top" background="../images/top_tile_short.jpg">
								            <table width="100%" border="0" cellpadding="0" cellspacing="0">
								                <tr>
								                	<td width="200"><img src="../images/logo.jpg" width="200" height="50"></td>
								                    <td>&nbsp; 	内容编辑 [<%=sitename%> 站点  :  <%=channelname%> 频道]</td>
								                    <td width="53"><img src="../images/Gather_top_LeftDot.jpg" width="174" height="50"></td>
								                </tr>
								            </table>
							            </td>
						            </tr>
						            
						            <tr>
							            <td height="18" colspan="3">
								            <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
								            	<tr background="../images/Gather_Content_topbg.gif" >
								                	<td width="4%" height="35" align="right" background="../images/Gather_Content_topbg.gif">
								                  		<img src="../images/ico_point.gif" width="25" height="25">								                  	</td>
								                  	<td colspan="3" background="../images/Gather_Content_topbg.gif" class="cms_title_blue">
									                  	<table width="100%" border="0" cellpadding="0" cellspacing="0">
										                    <tr>
										                        <td height="35" class="cms_title_blue">
										                          文档信息</td>
										                    	<td width="120">
										                    	</td>
										                	</tr>
								                      </table>
							                      </td>
							                  </tr>
								                <tr>
								                	<td height="26">&nbsp;								                	</td>
								                	<td height="23" colspan="3">
								                		<table border="0" cellpadding="3" cellspacing="0">
								                    		<tr>
								                      			<td align="right">
								                      			  <strong>文档类型:</strong> </td>
								                      			<td>
								                      				<img src="../images/doc_html.gif" width="20" height="20">								                      			</td>
								                      			<td>
								                      			普通文档								                      			</td>
							                      			</tr>
							                  		  </table>

								                    </td>
								                </tr>
								                <tr>
								                	<td height="26">&nbsp;								                	</td>
								                  	<td width="8%" height="23" align="right">
								                  	  <strong>文档标题:</strong> </td>
								                    <td width="43%">
								                    	<span class="cms_title_blue">
								                    		<label>
								                    			<input name="title" type="text" class="cms_text" size="60" value="<%=filterStr(document.getTitle())%>" readonly>
								                    		</label>
								                  		</span>
								                  		<span class="STYLE7">
								                  			<label>
								                  				<span class="red_star">
								                  				*
								                  				</span>
								                  			</label>
								                  		</span>
								                  		<span>
								                  		
								                  		</span>
								                  	</td>
								                  	<td height="23">
									                  	<span> 
									                  	<strong>标题颜色:</strong>									                  	</span>
								                  		<span>
								                      		<input name="titlecolor"  value="<%=document.getTitlecolor()%>" readonly type="text" class="cms_text" size="8" style="color:<%=document.getTitlecolor()%>" maxlength="50">
							                  		  </span>
							                  	  </td>
								                </tr>
								                <tr>
								                	<td height="26">&nbsp;								                	</td>
								                  	<td height="23" align="right">
							                  	    <strong>显示标题:</strong>							                      </td>
								                  	<td height="23">
								                  		<input name="subtitle" type="text" class="cms_text" size="60" value="<%=filterStr(document.getSubtitle())%>" readonly>
												  		<span class="STYLE7">
									                  		<label>
									                  			<span class="red_star">
									                  			*
									                  			</span>
									                  		</label>
								                  		</span>
								                  	</td>
								                  	<td height="23">
									                  	<label>
									                  	<strong>文档来源:</strong>									                  	</label>
								                    	<span class="cms_title_blue">
								                    	<input type="text" style="width:100px;height:21px;font-size:10pt;" id="inputdocsource" name="inputdocsource" readonly><span style="width:18px;border:0px solid red;">
								                    	<select name="docsource_id"  style="margin-left:-100px;width:118px;" onChange="changSelect(this)" id="docsource_id" disabled>
										                      	<%
										                          List dslist=null;
															      DocumentManager dmimpl=new DocumentManagerImpl();
															      dslist=dmimpl.getDocSourceList();
																  request.setAttribute("dslist",dslist);
																%>
								                      			<pg:list requestKey="dslist">
								                      			<option value="<pg:cell colName="docsource_id"/>"<pg:equal colName="docsource_id" value="<%=docsource%>">selected</pg:equal>>
								                      			<pg:cell colName="srcname" defaultValue="" /> 
								                      			</pg:list>
								                    		</select>
								                    		</span>
								                    	</span>
								                    </td>
								                </tr>
								                <tr>
								                	<td height="26">&nbsp;								                	</td>
								                  	<td height="23" align="right">
								                  		<span class="black">
								                  		<strong>关 键 词:</strong></span>								                  	</td>
								                  	<td height="23">
								                  		<span class="black">
								                  			<span class="cms_title_blue">
								                    			<input name="keywords" type="text" class="cms_text" size="26" value="<%=document.getKeywords()%>" readonly>
								                  			</span>
															(<span class="red_star">多个关键词之间用;分隔</span>)
								                  		</span>
								                  		
								                  	</td>
								                  	<td height="23">
								                  		<div align="left">
								                  			<strong>编稿时间:</strong>							                  			  <span class="black">
								                  				<span class="cms_title_blue">
																	<input name="docwtime" dateformat="yyyy-MM-dd HH:mm:ss" type="text" value="<%=document.getDocwtime()%>" readonly="true">
																</span>
															</span>								                    	</div>
								                    </td>
								                </tr>
												<tr>
								                	<td height="26">&nbsp;								                	</td>
								                  	<td height="23" align="right">
								                  	  <strong>发布模板:</strong> </td>
								                  	<td height="23">
														<table border="0" cellpadding="0" cellspacing="0">
															<tr>
																<td>
																	<table>
																		<tr>
																			<td>
																			<span class="cms_title_blue">
																				<input type="radio" name="selecttpl" value="0"
																				onclick="changtpl(0)" disabled>
																				<span class="cms_title_blue">
																					<select name="detailtemplate_id" class="cms_select" style="width:130px" disabled>
																						<%
																						  List list=null;
																						  TemplateManager tmi=new TemplateManagerImpl();
																						  Template template=new Template();
																						  list=tmi.getTemplateInfoListofSite(Integer.parseInt(siteid), 2);
																						  request.setAttribute("tpllist",list);
																						%>
																						<pg:list requestKey="tpllist">
																							<option value="<pg:cell colName="templateId"/>"<pg:equal colName="templateId" value="<%=tid%>">selected</pg:equal>> 
																							<pg:cell colName="name"/> 
																						</pg:list>
																					</select>
																				</span>
																		  </td>
																		</tr>
																		<tr>
																			<td>
																			<input type="radio" name="selecttpl" value="1" onClick="changtpl(1)" disabled>
																			<input type="hidden" name="parentDetailTpl"/>
																			<%
																			ChannelManager chnl = new ChannelManagerImpl();
																			Template tp = chnl.getDetailTemplateOfChannel(channelId);
																			String tname = (tp==null?"还没有设置":tp.getName());
																			%>
																			继承所属频道的细览模板(<span class="red_star"><%=tname%></span>)
																			</td>
																		</tr>
																	</table>
																</td>
																<td>
																	<!--<table>
																		<tr>
																			<td>
																			<a href="javascript:void(0)" onClick="preview()" ><u>预览</u></a>
																			</td>
																		</tr>
																	</table>-->
																</td>
															</tr>
													  </table>
													</td>
													<td>
														<span class="black">
								                  		    <strong>作　　者:</strong>								                  		<span class="cms_title_blue">
									                  			<input name="author" type="text" class="cms_text" size="17" value="<%=document.getAuthor()%>" readonly>
								                  		</span>							                  		  </span>
														&nbsp;&nbsp;<strong>级别:</strong>
														<span class="cms_title_blue">
															<select name="doc_level" class="cms_select" disabled>
																<%
																  List doclevellist = null;
																  DocumentManager impl = new DocumentManagerImpl();
																  doclevellist = impl.getDocLevelList();
																  request.setAttribute("doclevellist",doclevellist);
																%>
																<pg:list requestKey="doclevellist">
																	<option value="<pg:cell colName="id"/>"<pg:equal colName="id" value="<%=docLevelId%>">selected</pg:equal>>
																	<pg:cell colName="name"/> 
																</pg:list>
															</select>
														&nbsp; 
													  </span>
											  	  </td>
								                </tr>
												<tr>
													<td height="26">&nbsp;													</td>
													<td height="23" align="right">
														<strong>主题图片:</strong> </td>
													<td height="23">
														<input type="text" name="picpath" value="<%=document.getPicPath()%>" class="cms_text" readonly size="30"/>
														<!--<input type="button" name="addpic" onClick="chooseImage()" value="选择图片"/>-->
													</td>
													<td height="23"> 
														<strong>多媒体文件:</strong>
													  <input type="text" name="mediapath" value="<%=document.getMediapath()%>" class="cms_text" readonly size="30"/>
														<!--<input type="button" name="addmedia" onClick="choosemedia()" value="选择多媒体文件"/>-->
												  </td>
												</tr>
												<tr>
											        <td height="26">&nbsp;												</td>
											        <td width="10%" align="right"><strong>文档发布名称:</strong></td>
											        <td><input type="text" name="publishfilename" value="<%=document.getPublishfilename()%>"  class="cms_text"  size="30" readonly/></td>
											    </tr>
								                <tr>
								                	<td height="26">&nbsp;								                	</td>
								                  	<td height="23" colspan="3">
								                  		<table width="100%" border="0" cellpadding="3" cellspacing="0">
								                    		<tr>
										                    	<td width="8%" align="right"><strong>摘要内容:</strong></td>
										                      	<td width="92%">
									                      		<textarea name="docabstract" cols="70" rows="5" class="cms_textarea" readonly><%=document.getDocabstract()%></textarea>										                      	</td>
									                      	</tr>
								                  		</table>
								                  	</td>
								                </tr>
								                <tr>
								                	<td height="26">&nbsp;								                	</td>
								                  	<td height="23" colspan="2">
														<img align=absMiddle border=0 height=12 src="../images/text-edit.gif" width=14 >
														<a id=tab1 href="javascript:void(0)" onclick=tabClick(1) ><u>[内容编辑]</u></a>&nbsp;&nbsp; 
														<img align=absMiddle border=0 height=12 src="../images/text-correlated.gif" width=14 >
														<a id=tab2 href="javascript:void(0)" onclick=tabClick(2) ><u>[相关文档]</u></a>&nbsp;&nbsp;
														<img align=absMiddle border=0 height=12 src="../images/text-addons.gif" width=14 >
														<a id=tab3 href="javascript:void(0)" onclick=tabClick(3) ><u>[文档附件]</u></a>&nbsp;&nbsp;
														<img align=absMiddle border=0 height=12 src="../images/text-img.gif" width=14 >
														<a id=tab4 href="javascript:void(0)" onclick=tabClick(4) ><u>[文档图片]</u></a>&nbsp;&nbsp;
														<img align=absMiddle border=0 height=15 src="../images/text-text.gif" width=15 >
														<a id=tab5 href="javascript:void(0)" onclick=tabClick(5) ><u>[扩展字段]</u></a>
								                  	</td>
								                    <td width="30%" height="23">
										<div align="center">
											<input type="button" value="返回" class="cms_button" onClick="back()">
										</div>
									</td>
								                </tr>
						            	  </table>
						            	</td>
						            </tr>
						            <tr>
										<td  width="95%" height="22" colspan="3"  align="center" valign="top">
											<div>
												<input type="hidden" name="content" value="<%=filterStr(document.getContent())%>">
												<input type="hidden" name="pics">
												<input type="hidden" name="flashs">
												<input type="hidden" name="medias">
												<input type="hidden" name="files"><br/>
												 <iframe id="eWebEditor1" src="/cms/editor/eWebEditor48/ewebeditor.htm?id=content&style=coolblue&cusdir=<%=docpath%>" frameborder="0" scrolling="no" width="95%" height="460">
												</iframe>
											</div>
											<div>
												<input type="hidden" name="doclist">
												<br/><iframe id="docxz" src="../channelManage/chn_doc_xzlist.jsp?flag=1&siteid=<%=siteid%>" frameborder="0" scrolling="auto" width="0%" height="0%">
												</iframe>
											</div>
											<div>
												<input type="hidden" name="originalFilename">
												<input type="hidden" name="description">
												<input type="hidden" name="url">
												<iframe id="docattachlist" src="../docManage/doc_attach_list.jsp?type=2&docpath=<%=docpath%>" frameborder="0" scrolling="auto" width="0%" height="0%">
												</iframe>
											</div>
											<div>
												<input type="hidden" name="originalFilename2">
												<input type="hidden" name="description2">
												<input type="hidden" name="url2">
												<iframe id="docattachlist2" src="../docManage/doc_attach_list.jsp?type=3&docpath=<%=docpath%>" frameborder="0" scrolling="auto" width="0%" height="0%">
												</iframe>
											</div>
											<div>
												<input type="hidden" name="fieldids">
												<input type="hidden" name="extfieldvalues">
												<input type="hidden" name="extfieldtypes">
												<iframe id="docextfielslist" src="../docManage/doc_extfield_list.jsp?type=2&id=<%=channelId%>&docid=<%=docid%>" frameborder="0" scrolling="auto" width="0%" height="0%">
												</iframe>
											</div>
										</td>
						            </tr>						            					            	          
								  <tr>
									<td>&nbsp;</td>
								  </tr>
						          <!--来源于别的网站才有父站点-->
						          <tr>
						              <td height="11" colspan="3" background="../images/bottom_gray.jpg">&nbsp;</td>
					              </tr>
      </table>
      <script type="text/javascript">
  document.all.inputdocsource.value=document.all.docsource_id.options(document.all.docsource_id.selectedIndex).text;
  </script> 
	</form>
	<div style="display:none">
		<iframe name="updategather" width="0" height="0"></iframe>
	</div> 
</body>
</html>