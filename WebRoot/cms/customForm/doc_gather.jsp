<%@ page contentType="text/html; charset=UTF-8" language="java" 
import="java.util.Date,java.text.SimpleDateFormat,java.util.StringTokenizer"%>
<%@page import="com.frameworkset.common.poolman.DBUtil"%>
<%@page import="com.frameworkset.platform.security.AccessControl,java.util.List"%>
<%@page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.templatemanager.*
,com.frameworkset.platform.cms.container.Template,com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.cms.channelmanager.*"%>
<jsp:useBean id="date" scope="page" class="net.fiyu.edit.TimeStamp"/>
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
	//当前时间
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	java.util.Date currentTime = new java.util.Date(); 
	String riqi = formatter.format(currentTime); 
	
	String channelname = request.getParameter("channelName");
	String siteid = request.getParameter("siteid");
	String channelId = request.getParameter("channelId");
	
	ChannelManager cm = new ChannelManagerImpl();
	//获取所选频道的细缆模板ID
	String selecttemplateid="-1";
	
	Template selecttemplate = cm.getDetailTemplateOfChannel(channelId);
	if(selecttemplate != null)
	{
		selecttemplateid = String.valueOf(selecttemplate.getTemplateId());
	}
	
	SiteManager siteManager = new SiteManagerImpl();
	String sitename = siteManager.getSiteInfo(siteid).getName();//站点名称
	//老方法
	//String docpath = cm.getFilePath(channelId);
	////根据频道id，站点id获取文档保存路径(新方法) 保存路径=站点路径+频道路径
	String docpath = siteManager.getSiteInfo(siteid).getSiteDir() + "/_webprj/" + cm.getChannelInfo(channelId).getChannelPath() + "/content_files";
	//System.out.println("jxw==="+docpath);
	
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<link href="<%=request.getContextPath()%>/cms/inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath()%>/cms/inc/js/func.js"></script>
<script src="<%=request.getContextPath()%>/cms/inc/js/killerror.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/datetime/calender.js" language="javascript"></script>
<style type="text/css">
<!--
#Layer1 {
	position:absolute;
	left:0px;
	top:0px;
	width:113px;
	height:127px;
	z-index:1;
}
.STYLE1 {color: red}
-->
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>CMS 文档管理</title>
</head>
<script language="javascript">
	//动画窗口对象
	var win = null;
	//关闭动画窗口
	function closewin()
	{
		win.close();
	}
	//字符转换
	function HTMLEncodeCMS(text){
		//text = text.replace(/"/g, "&quot;");
		return text;
	}
	//选择图片新闻的概览图片
	var fileFlag;
	function chooseImage(){
		fileFlag = "pic";
		openWin("<%=request.getContextPath()%>/cms/channelManage/chooseImageFrameset.jsp?fileFlag=picOrFlash",700,580);
		//openWin("<%=request.getContextPath()%>/cms/newsimage/chooseImageFrameset.jsp",700,580);
		//window.open("<%=request.getContextPath()%>/cms/channelManage/chooseImageTree.jsp");
	}
	function setImage(uri){
		if(fileFlag == "pic")
			document.all("picpath").value  = uri;
		if(fileFlag == "media")
			document.all("mediapath").value  = uri;
		if(fileFlag == "newpic")
			document.all("newpicpath").value  = uri;
	}
	//选择多媒体文件
	function choosemedia()
	{
		fileFlag = "media";
		openWin("<%=request.getContextPath()%>/cms/channelManage/chooseImageFrameset.jsp?fileFlag=media",620,580);
	}
	//选择new图标文件
	function choosenewpic()
	{
		fileFlag = "newpic";
		openWin("<%=request.getContextPath()%>/cms/channelManage/chooseImageFrameset.jsp?fileFlag=pic",620,580);
	}
	//预览模板
	function preview()
	{
		var tplId = document.all.detailtemplate_id.value;
		window.open("<%=request.getContextPath()%>/cms/templateManage/template_previewbyid.jsp?tplId=" + tplId + "&siteId=<%=siteid%>");
	}
	//静态分页置标
	/**将图片转换成分页置标**/
	function setPageTagNone(){
		var arPageTags = eWebEditor1.eWebEditor.document.all("_CMSPage");
		if(arPageTags == null)return;
		if(arPageTags.length){
			for(var i =arPageTags.length-1; i>=0; i--){
				var objPageTag = arPageTags[i];
				if(objPageTag == null)continue;

				var pageTag = eWebEditor1.eWebEditor.document.createElement("CMS_PAGE_SEPARATOR");		
				//pageTag.innerHTML = "TRS静态分页置标";
				objPageTag.replaceNode(pageTag);
			}
		}else{
			var pageTag = eWebEditor1.eWebEditor.document.createElement("CMS_PAGE_SEPARATOR");		
			//pageTag.innerHTML = "TRS静态分页置标";
			arPageTags.replaceNode(pageTag);
		}

	}
	//站点窗口输入参数校验,closeFlag为10表示保存后将送审，为11表示保存后提交发布，为12表示保存后立即发布
	function saveform(closeFlag){
		//编辑器保持在设计模式
		eWebEditor1.setMode('EDIT');
		
		var inputsource=document.all.inputdocsource.value;
		if(myform.title.value==""){
			alert("请输入职位!");
			document.all.auditorDiv.innerHTML="";
			document.all.publisherDiv.innerHTML="";
			myform.title.focus();
			return false;
		}
		if(myform.subtitle.value==""){
			alert("请输入姓名!");
			document.all.auditorDiv.innerHTML="";
			document.all.publisherDiv.innerHTML="";
			myform.subtitle.focus();
			return false;
		}
		if(inputsource==null||inputsource=="")
		{
		  alert('请选择或者填写文档来源');
		  document.all.auditorDiv.innerHTML="";
		  document.all.publisherDiv.innerHTML="";
		  document.all.inputdocsource.focus();
		  return false;
		}
		else{
			//判定是否新加的文档来源，是设置
		    if(inputsource!=document.all.docsource_id.options(document.all.docsource_id.selectedIndex).text)
				document.all.isnewdocsource.value="1";
			//取相关文档列表
			var arr = new Array();
			arr = docxz.document.getElementsByName("docid");
			for(var i=0;i<arr.length;i++)
			{
				myform.doclist.value += arr[i].value +"№";
			}
			//取相关附件列表
			var arr2 = new Array();
			var arr3 = new Array();
			var arr4 = new Array();
			arr2 = docattachlist.document.getElementsByName("description");
			arr3 = docattachlist.document.getElementsByName("originalFilename");
			arr4 = docattachlist.document.getElementsByName("url");
			for(var i=0;i<arr2.length;i++)
			{
				myform.description.value += arr2[i].value + "№";
				myform.originalFilename.value += arr3[i].value + "№";
				myform.url.value += arr4[i].value + "№";
			}
			//取相关图片列表
			var arr22 = new Array();
			var arr32 = new Array();
			var arr42 = new Array();
			arr22 = docattachlist2.document.getElementsByName("description");
			arr32 = docattachlist2.document.getElementsByName("originalFilename");
			arr42 = docattachlist2.document.getElementsByName("url");
			for(var i=0;i<arr22.length;i++)
			{
				myform.description2.value += arr22[i].value + "№";
				myform.originalFilename2.value += arr32[i].value + "№";
				myform.url2.value += arr42[i].value + "№";
			}
			var tempcontent = eWebEditor1.eWebEditor.document.body.innerHTML;
			setPageTagNone();
			myform.content.value = HTMLEncodeCMS(eWebEditor1.eWebEditor.document.body.innerHTML);
			
			if(myform.content.value=="")
			{
				alert("请输入个人简历!");
				document.all.auditorDiv.innerHTML="";
				document.all.publisherDiv.innerHTML="";
				return false;
			}
			if(document.all.selecttpl[1].checked)
				myform.parentDetailTpl.value = "1";
			else
				myform.parentDetailTpl.value = "0";
			
			
			//扩展字段
			//var extfieldvalues = docextfielslist.document.getElementsByName("extfielvalue");
			var fieldis = docextfielslist.document.getElementsByName("fieldId");
			var fldtypes = docextfielslist.document.getElementsByName("fieldType");
			var fldnames = docextfielslist.document.getElementsByName("fieldName");
			myform.extfieldvalues.value = "";
			myform.fieldids.value = "";
			myform.extfieldtypes.value = "";
			myform.extfieldnames.value = "";
			for(var ef=0;ef<fieldis.length;ef++)
			{
				
				var tmp = new Array();
				tmp = docextfielslist.document.getElementsByName(fieldis[ef].value+"_value");
				//alert(tmp.length);
				if (tmp.length<=0)
					continue;
					
				myform.extfieldvalues.value += tmp[0].value + "№";
				myform.fieldids.value += fieldis[ef].value + "№";
				myform.extfieldtypes.value += fldtypes[ef].value + "№";
				myform.extfieldnames.value += fldnames[ef].value + "№";
			}
			//特殊要求(便于当值为空时java 的split（）方法)
			myform.extfieldvalues.value += "null№";

			myform.method="post";
			if(closeFlag==5)
			{
				myform.target = "addgather";
				myform.action="<%=request.getContextPath()%>/cms/docManage/previewDoc.jsp";
				win = window.open("<%=request.getContextPath()%>/cms/doing.html","doinghtml","height="+(screen.availHeight-200)+",width="+(screen.availWidth-300)+",top=100,left=150,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no");
			}
			else
			{
				myform.target = "addgather";
				myform.action = "<%=request.getContextPath()%>/cms/docManage/add_document.jsp?flag=" + closeFlag;
				win = window.open("<%=request.getContextPath()%>/cms/doing.html","doinghtml","height="+(screen.availHeight-200)+",width="+(screen.availWidth-300)+",top=100,left=150,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no");
			}
			myform.submit();
			//eWebEditor1.eWebEditor.document.body.innerHTML = tempcontent;
			//主要是用静态分页
			eWebEditor1.setHTML(tempcontent);
			var buttons = document.getElementsByTagName("input");
			for(var i=0;i<buttons.length;i++)
			{
				buttons[i].disabled = true;
			}
		}
	}
	//返回
	function back() {
		window.close();
	}
	
	function isnum()
	{
	  var k = window.event.keyCode;
	  if (k < 48 || k > 57){
	     alert("你输入的不是数字！")
	     window.event.keyCode = 0 ;}
	}
	
	function init()
	{
		changtpl(1);
		myform.title.focus();
		document.all.tab1.style.color = "#FF6600";
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
	//响应rodio事件
	function changtpl(type)
	{
		if(type==0)
			document.all.detailtemplate_id.disabled = false;
		else
			document.all.detailtemplate_id.disabled = true;
	}
	function changSelect(o)
	{
	   document.all.inputdocsource.value=document.all.docsource_id.options(document.all.docsource_id.selectedIndex).text;
	   document.all.inputdocsource.focus();
	   document.all.inputdocsource.select();
	}	
	function subpublish(){
		showModalDialog("doc_PublisherList.jsp?flag=4&channelId=<%=channelId%>",window,"dialogWidth:400px;dialogHeight:500px;help:no;scroll:auto;status:no");
	}
	function publish(){
		//document.all.divProcessing.style.display="";
		saveform(12);
	}
	function deliver(){
		showModalDialog("doc_AuditorList.jsp?flag=4&channelId=<%=channelId%>",window,"dialogWidth:400px;dialogHeight:500px;help:no;scroll:auto;status:no");
	}
	//将编辑器中的代码保存为文档模板
	function saveAsTPL(){
		var tempcontent = eWebEditor1.eWebEditor.document.body.innerHTML;
		setPageTagNone();
		myform.content.value = HTMLEncode(eWebEditor1.eWebEditor.document.body.innerHTML);
		eWebEditor1.eWebEditor.document.body.innerHTML = tempcontent;
		if(myform.content.value==""){
			alert("请输入文档模板内容!");
			document.all.docTplName.value="";
			document.all.docTplDescription.value="";
			return false;
		}	
		var re = showModalDialog("add_one_doctpl.jsp",window,"dialogWidth:350px;dialogHeight:300px;help:no;scroll:auto;status:no");
		if(re == "cf"){
			myform.action = "doc_tpl_handle.jsp?action=add&closeRefresh=0";
			myform.target = "addgather";
			myform.submit();
		}
	}
</script>
<!--下面的代码 使得按 F5会刷新modal页面-->
<base target="_self">
<body  bottommargin="0" bgcolor="#F3F4F9"  onkeydown="if(event.keyCode==116){reload.click()}" rightmargin="0" leftmargin="0" topmargin="0" onLoad="init();">
<script language="javascript">
	document.write("<a id='reload' href='" + document.location.href + "' style='display:none'>reload...</a>");
</script>
<!--上面的代码 使得按 F5会刷新modal页面-->
<!--发布跑马灯 -->
<div id="divProcessing" style="position:absolute;width:40%;height:20px;z-index:1;background-color:#F0f0f0;layer-background-color:#F0f0f0;left:22%;top:80%;display:none;"  class="font">   
	<marquee direction="left" width="250" behavior="alternate"><span class=STYLE1>正在发布中，请稍等……</span></marquee>
</div>

<form target="addgather" name="myform" method="post">
	<!--文档送审和提交时审核人发布人列表，回填-->
	<div id="auditorDiv" name="auditorDiv" style="display:none;"></div>
	<div id="publisherDiv" name="publisherDiv" style="display:none;"></div>
	
	<!--文档模板保存时的相关信息，模板名和模板描述，回填-->
	<input name="docTplName" value="" type=hidden>
	<input name="docTplDescription" value="" type=hidden>
	
	<input name="doctype" value="0" type=hidden>
  	<input name="userid" value="<%=userid%>" type=hidden>
  	<input name="channelId" value="<%=channelId%>" type=hidden>
  	<input name="siteid" value="<%=siteid%>" type=hidden>
  	<input name="createUser" value="<%=userid%>" type=hidden>
  	<input name="isnewdocsource" value="0" type="hidden"><!--是否新稿源-->
	<input name="filepaths" value="" type=hidden><!--文件的新增时间，作为文件名，拼成一个串-->
	<input name="ofilepaths" value="" type=hidden><!--原本的url-->
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<td height="50" valign="top" background="<%=request.getContextPath()%>/cms/images/top_tile_short.jpg">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
                	<tr>
	                    <td width="200"><img src="<%=request.getContextPath()%>/cms/images/logo.jpg" width="200" height="50"></td>
	                    <td>&nbsp;&nbsp;&nbsp;&nbsp;内容采集 [<%=sitename%> 站点  :  <%=channelname%> 频道]</td>
	                    <td width="174"><img src="<%=request.getContextPath()%>/cms/images/Gather_top_LeftDot.jpg" width="174" height="50"></td>
                    </tr>
                </table>
            </td>
		</tr>
		<tr>
			<td height="18">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr background="<%=request.getContextPath()%>/cms/images/Gather_Content_topbg.gif">
						<td width="4%" height="35" align="right" background="<%=request.getContextPath()%>/cms/images/Gather_Content_topbg.gif">
							<img src="<%=request.getContextPath()%>/cms/images/ico_point.gif" width="25" height="25" vspace="0">						</td>
						<td colspan="3" class="cms_title_blue">
							<table width="100%" border="0" cellpadding="0" cellspacing="0" background="<%=request.getContextPath()%>/cms/images/Gather_Content_topbg.gif">
								<tr>
									<td width="100" height="35" class="cms_title_blue">
									领导信息填写									</td>
									<td >
									<DIV style="text-align:left">
									(<span class="red_star">*</span>为必填项<span class="red_star"><img src="<%=request.getContextPath()%>/cms/images/tan.gif" width="11" height="11"></span>)									</DIV>									</td>
								</tr>
							</table>						</td>
					</tr>
					<tr style="display:none">
						<td height="26">&nbsp;</td>
						<td height="23" colspan="3">
							<table border="0" cellpadding="3" cellspacing="0">
								<tr>
									<td>
									  <strong>文档类型:</strong></td>
									<td>
										<div align="center">
											<img src="<%=request.getContextPath()%>/cms/images/doc_html.gif" width="20" height="20">										</div>									</td>
									<td align="right">
										<a  href="<%=request.getContextPath()%>/cms/docManage/doc_gather.jsp?siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>">
										<font color="#FF6600">普通文档</font></a>									</td>
									<td>
										<div align="center">
											<img src="<%=request.getContextPath()%>/cms/images/doc_link.gif" width="20" height="20">										</div>									</td>
									<td align="right">
										<a href="<%=request.getContextPath()%>/cms/docManage/doc_gather_links.jsp?siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>">
										<font color="#FF6600">外部链接</font></a>									</td>
									<td>
										<div align="center">
										<img src="<%=request.getContextPath()%>/cms/images/doc_aggr.gif" width="20" height="20">										</div>														  </td>
									<td>
										<a href="<%=request.getContextPath()%>/cms/docManage/doc_gather_aggr.jsp?siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>">
										<font color="#FF6600">聚合文档</font></a>									</td>
								</tr>
							</table>						</td>
					</tr>
					<tr>
						<td height="26">&nbsp;</td>
						<td width="6%" height="23"><strong>职位:</strong></td>
						<td width="43%">
							<span class="cms_title_blue">
								<input name="title" type="text" class="cms_text" size="60" />
							</span>
							<span class="red_star">*</span>
							<span>&nbsp;&nbsp;</span>						</td>
						<td height="23"> 
						    <strong>编稿时间:</strong>
						    <span class="black">
								<span class="cms_title_blue">
									<input name="docwtime" type="text" class="cms_text" onpropertychange="ordertime.value=this.value"  value="<%=riqi%>"  size="22">
								</span>							</span>
							<input type="button" class="Channel_5wordsBtn" value="时间" onClick="showdatetime(document.all('docwtime'))">						</td>						
						
					</tr>
					<tr>
						<td height="26">&nbsp;</td>
						<td height="23"><strong>姓名:</strong></td>
						<td height="23">
							<input name="subtitle" type="text" class="cms_text" size="60">
							<span class="red_star">*</span>						</td>
					<td height="23"> 
						    <strong>排序时间:</strong>
						    <span class="black">
								<span class="cms_title_blue">
									<input name="ordertime" type="text" class="cms_text" value="<%=riqi%>"  size="22">
								</span>							</span>
							<input type="button" class="Channel_5wordsBtn" value="时间" onClick="showdatetime(document.all('ordertime'))">						</td>	
					</tr>
					
					<tr style="display:block">
					    <td height="26">&nbsp;</td>
					    <td height="26">&nbsp;</td>
					    <td height="26">&nbsp;</td>
					    <td height="23"> 
						    <strong>排序号:</strong>
						    <span class="black">
								<span class="cms_title_blue">
									<input name="seq" type="text" class="cms_text" value=""  size="10">
								</span>							</span>
											</td>
					</tr>
					<tr style="display:block">
					    <td height="26">&nbsp;</td>
					    <td height="26">&nbsp;</td>
					    <td height="26">&nbsp;</td>
					    <td height="23" style="display:none">
							<label><strong>文档来源:</strong></label>
							<span class="cms_title_blue">
							<input type="text" style="width:100px;height:21px;font-size:10pt;" id="inputdocsource" name="inputdocsource">
							<span style="width:18px;border:0px solid red;">
							<select name="docsource_id" style="margin-left:-100px;width:118px;" onChange="changSelect(this)" id="docsource_id">
								<%
									List dslist=null;
									DocumentManager dmi=new DocumentManagerImpl();
									DocumentSource ds=new DocumentSource();
									dslist=dmi.getDocSourceList();
									request.setAttribute("dslist",dslist);
								%>
								<pg:list requestKey="dslist">
									<option value="<pg:cell colName="docsource_id"/>" >
							    	<pg:cell colName="srcname"/>
								</pg:list>
							</select>
							</span>							</span>						</td>
					    
					</tr>
					<tr>
						<td height="26">&nbsp;</td>
						<td height="23">
							<strong>发布模板:</strong>						</td>
						<td height="23">
							<table>
								<tr>
									<td>
										<table>
											<tr>
												<td>
												<span class="cms_title_blue">
													<input type="radio" name="selecttpl" value="0"  onClick="changtpl(0)">
													<select name="detailtemplate_id" class="cms_select" style="width:130px">
														<%
														  List list=null;
														  TemplateManager tmi=new TemplateManagerImpl();
														  Template template=new Template();
														  list=tmi.getTemplateInfoListofSite(Integer.parseInt(siteid), 2);
														  request.setAttribute("tpllist",list);
														%>
														<pg:list requestKey="tpllist">
															<option value="<pg:cell colName="templateId"/>" >
															<pg:cell colName="name"/> 
														</pg:list>
													</select>
													<script type="text/javascript">
													var selectvalue="<%=selecttemplateid%>";
													var selectElement=document.getElementsByName("detailtemplate_id");
													var selectel=selectElement[0];
													for(var i=0;i<selectel.options.length;i++)
													{
													   if(selectvalue==selectel.options(i).value)
													   {
													      selectel.selectedIndex=i;
													      break;
													   }
													}
													</script>
												</span>												</td>
											</tr>
											<tr>
												<td>
												<input type="radio" name="selecttpl" value="1" checked="true" onClick="changtpl(1)">
												<input type="hidden" name="parentDetailTpl"/>
												<%
												ChannelManager chnl = new ChannelManagerImpl();
												Template tp = chnl.getDetailTemplateOfChannel(channelId);
												String tname = (tp==null?"还没有设置":tp.getName());
												%>
												继承所属频道的细览模板(<span class="red_star"><%=tname%></span>)												</td>
											</tr>
										</table>									</td>
									<td>
										<table>
											<tr>
												<td>
												<a href="javascript:void(0)" onClick="preview()" ><u>预览</u></a>												</td>
											</tr>
										</table>									</td>
								</tr>
							</table>						</td>
						<td style="display:none">
							<span class="black">
							    <strong>作　　者:</strong>
							    <span class="cms_title_blue">
									<input name="author" type="text" class="cms_text" size="17">
								</span>							</span>
							&nbsp;&nbsp; <strong>级别:</strong>
						  	<span class="cms_title_blue">
								<select name="doc_level" class="cms_select">
									<%
									  List doclevellist = null;
									  DocumentManager impl = new DocumentManagerImpl();
									  doclevellist = impl.getDocLevelList();
									  request.setAttribute("doclevellist",doclevellist);
									%>
									<pg:list requestKey="doclevellist">
										<option value="<pg:cell colName="id"/>">
										<pg:cell colName="name"/> 
									</pg:list>
								</select>
					  	    </span>						</td>
					</tr>
					<tr>
						<td height="26">&nbsp;</td>
						<td><strong>照片:</strong></td>
						<td>
							<input type="text" name="picpath" value="" class="cms_text" readonly size="30"/>
							<input type="button" class="Channel_5wordsBtn" name="addpic" onClick="chooseImage()" value="选择图片"/>						</td>
						<td style="display:none"> 
							<strong>发布名称:</strong>
						    <input type="text" name="publishfilename" value="" class="cms_text" size="30"/>					  	</td>
					</tr>
					<tr style="display:none">
					    <td height="26">&nbsp;</td>
					    <td colspan="3">
					    	<strong>多媒体文件:</strong>
					    	<input type="text" name="mediapath" value="" class="cms_text" readonly size="30"/>
					  		<input type="button" class="Channel_5wordsBtn" name="addmedia" onClick="choosemedia()" value="选择文件"/>					    </td>
					</tr>
					<tr style="display:none">
					    <td height="26">&nbsp;</td>
					    <td colspan="3">
					    	<strong>是否标记为new:</strong>&nbsp;&nbsp;
					    	<input type="checkbox" hideFocus name="isnew" value="1" class="cms_text"/>&nbsp;&nbsp;
					    	<input type="text" name="newpicpath" value="" class="cms_text" readonly size="30"/>
					  		<input type="button" class="Channel_5wordsBtn" name="default" onClick="document.all('newpicpath').value='image/new.gif'" value="使用默认值"/>
					  		<input type="button" class="Channel_7wordsBtn" name="addnewpic" onClick="choosenewpic()" value="选择new图标"/>					    </td>
					</tr>
					<tr style="display:none">
						<td height="26">&nbsp;</td>
						<td height="23" colspan="3">
							<table width="100%" border="0" cellspacing="0">
	                        	<tr>
	                            	<td width="7%"><strong>摘要内容:</strong></td>
	                            	<td>
	                            	<textarea name="docabstract" cols="70" rows="5" class="cms_textarea"></textarea>	                            	</td>
	                        	</tr>
	                        </table>                        </td>
					</tr>
					<tr>
						<td height="26">&nbsp;</td>
						<td height="23" colspan="3">
							<table width="100%" border="0" cellspacing="0">
	                        	<tr>
	                            	<td width="7%"><strong>工作分工:</strong></td>
	                            	<td>
	                            	<textarea name="keywords" cols="70" rows="5" class="cms_textarea"></textarea></td>
	                        	</tr>
	                        </table>                        </td>
					</tr>
					<tr>
						<td height="26">&nbsp;</td>
						<td height="23" colspan="2">
							<img align=absMiddle border=0 height=12 src="<%=request.getContextPath()%>/cms/images/text-edit.gif" width=14 >
							<a id=tab1 href="javascript:void(0)" onclick=tabClick(1) ><u>[内容编辑]</u></a>&nbsp;&nbsp; 
							<img align=absMiddle border=0 height=12 src="<%=request.getContextPath()%>/cms/images/text-correlated.gif" width=14 >
							<a id=tab2 href="javascript:void(0)" onclick=tabClick(2) ><u>[相关文档]</u></a>&nbsp;&nbsp;
							<img align=absMiddle border=0 height=12 src="<%=request.getContextPath()%>/cms/images/text-addons.gif" width=14 >
							<a id=tab3 href="javascript:void(0)" onclick=tabClick(3) ><u>[文档附件]</u></a>&nbsp;&nbsp;
							<img align=absMiddle border=0 height=12 src="<%=request.getContextPath()%>/cms/images/text-img.gif" width=14 >
							<a id=tab4 href="javascript:void(0)" onclick=tabClick(4) ><u>[文档图片]</u></a>&nbsp;&nbsp;
							<img align=absMiddle border=0 height=15 src="<%=request.getContextPath()%>/cms/images/text-text.gif" width=15 >
							<a id=tab5 href="javascript:void(0)" onclick=tabClick(5) ><u>[扩展字段]</u></a>						</td>
						<td width="30%" height="23" colspan="1">
							<div align="center">
								<input name="button" type="button" onClick="saveform(1)" class="cms_button"  value="保存">
								<input type="button" value="发布预览" class="cms_button" onClick="saveform(5);">								
								<input type="button" value="返回" class="cms_button" onClick="back()">
							</div>						</td>
					</tr>
					<tr>
					  <td height="26">&nbsp;</td>
					  <td height="23" colspan="2"><strong>个人简历：</strong></td>
					  <td height="23" colspan="1">&nbsp;</td>
				  </tr>
				</table>
			</td>
		</tr>
		            
		<tr>
			<td  width="95%" height="22"  align="center" valign="top">
  				<div>
					<input type="hidden" name="content">
					<input type="hidden" name="pics">
					<input type="hidden" name="flashs">
					<input type="hidden" name="medias">
					<input type="hidden" name="files"><br/>
					<iframe id="eWebEditor1" src="<%=request.getContextPath()%>/cms/editor/eWebEditor48/ewebeditor.htm?id=content&style=coolblue&cusdir=<%=docpath%>" frameborder="0" scrolling="no" width="95%" height="460">
					</iframe>
				</div>
				<div>
					<input type="hidden" name="doclist">
					<br/><iframe id="docxz" src="<%=request.getContextPath()%>/cms/channelManage/chn_doc_xzlist.jsp?flag=1&siteid=<%=siteid%>" frameborder="0" scrolling="auto" width="0%" height="0%">
					</iframe>
				</div>
				<div>
					<input type="hidden" name="originalFilename">
					<input type="hidden" name="description">
					<input type="hidden" name="url">
					<iframe id="docattachlist" src="<%=request.getContextPath()%>/cms/docManage/doc_attach_list.jsp?type=2&docpath=<%=docpath%>" frameborder="0" scrolling="auto" width="0%" height="0%">
					</iframe>
				</div>
				<div>
					<input type="hidden" name="originalFilename2">
					<input type="hidden" name="description2">
					<input type="hidden" name="url2">
					<iframe id="docattachlist2" src="<%=request.getContextPath()%>/cms/docManage/doc_attach_list.jsp?type=3&docpath=<%=docpath%>" frameborder="0" scrolling="auto" width="0%" height="0%">
					</iframe>
				</div>
				<div>
					<input type="hidden" name="fieldids">
					<input type="hidden" name="extfieldvalues">
					<input type="hidden" name="extfieldtypes">
					<input type="hidden" name="extfieldnames">
					<iframe id="docextfielslist" src="<%=request.getContextPath()%>/cms/docManage/doc_extfield_list.jsp?type=2&id=<%=channelId%>" frameborder="0" scrolling="auto" width="0%" height="0%">
					</iframe>
				</div>
			</td>
		</tr>
	</table>
	<script type="text/javascript">
	document.all.inputdocsource.value=document.all.docsource_id.options(document.all.docsource_id.selectedIndex).text;
	</script> 
</form>

<iframe name="addgather" width="0" height="0">
</iframe>
 
</body>
</html>