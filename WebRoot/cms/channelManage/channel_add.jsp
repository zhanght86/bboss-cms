<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*" %>

<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%@ page import="com.frameworkset.platform.cms.container.Template" %>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*" %>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*" %>
<%@ page import="com.frameworkset.common.poolman.DBUtil" %>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*" %>
<%@ page import="com.frameworkset.platform.cms.flowmanager.*" %>
<%@ page import="com.frameworkset.platform.util.*" %>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(pageContext);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String siteName = request.getParameter("siteName");
	String siteId   = request.getParameter("siteId");
	if(siteId==null || siteId.trim().length()==0){
	%>
		<script language="javascript" type="text/javascript">
			alert("站点id为空,无法建立不属于任何站点的频道.");
			self.location.href = "about:blank";
		</script>
	<%
		return;
	}
	String parentChannelId   = request.getParameter("channelId");
	String parentChannelName = request.getParameter("channelName");
	if(parentChannelId == null || parentChannelId.trim().length()==0){
		parentChannelId = "";
		parentChannelName = "";
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script src="../inc/js/func.js"></script>
<script>
function setChannelOutlineTemplateId()
{
	var w = "";
	if(channelForm.outlineTemplateId.value != "" 
		&& channelForm.outlineTemplateId.value != null
		&& channelForm.otName.value != ""
		&& channelForm.otName.value != null )
	{
		w = showModalDialog("channel_templateSet_frameset.jsp?siteId=<%=siteId%>&type=1&action=search&isSite=doc&outlineTemplateName="+channelForm.otName.value+"&otId="+channelForm.outlineTemplateId.value,"setChannelOutlineTemplateId","dialogWidth:800px;dialogHeight:505px;help:no;scroll:yes;status:no");
	}
 	else
 	{
 		w = showModalDialog("channel_templateSet_frameset.jsp?siteId=<%=siteId%>&type=1&action=search&isSite=doc","setChannelOutlineTemplateId","dialogWidth:800px;dialogHeight:505px;help:no;scroll:yes;status:no");
 	}
	//alert(w[0]+"    "+w[1]);
	if(w!=undefined && w.length>0)
	{
		channelForm.outlineTemplateId.value = w[0];
		channelForm.otName.value = w[1];
	}
}

function setChannelDetailTemplateId()
{
	var w = "";
	if(channelForm.detailTemplateId.value != "" 
		&& channelForm.detailTemplateId.value != null
		&& channelForm.dtName.value != ""
		&& channelForm.dtName.value != null )
	{
		w = showModalDialog("channel_templateSet_frameset.jsp?siteId=<%=siteId%>&type=2&action=search&isSite=doc&outlineTemplateName="+channelForm.dtName.value+"&otId="+channelForm.detailTemplateId.value,"setChannelOutlineTemplateId","dialogWidth:800px;dialogHeight:505px;help:no;scroll:yes;status:no");
	}
 	else
 	{
 		w = showModalDialog("channel_templateSet_frameset.jsp?siteId=<%=siteId%>&type=2&action=search&isSite=doc","setChannelOutlineTemplateId","dialogWidth:800px;dialogHeight:505px;help:no;scroll:yes;status:no");
 	}
		
	if(w!=undefined && w.length>0)
	{
		channelForm.detailTemplateId.value = w[0];
		channelForm.dtName.value = w[1];
	}

}
</script>
<tab:tabConfig/>
<title>添加频道</title>
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<style type="text/css">
	.topDIV {
		background-image: url(../images/toolbar_common_func_site.jpg);
		height: 82px;
		padding-top: 7px;
		padding-left: 55px;
	}

	body {
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
	}
</style>
<script language="javascript">
function changeWorkFlowType(type){
	if(type == 1){
		document.all.channelForm.workFlow.disabled = true;
	}else{
		document.all.channelForm.workFlow.disabled = false;
	}
}

//默认输入焦点
function setFocus(){
	//document.all.channelForm.channelName.focus();
}

function onNameChange(value){
	//document.all.channelForm.displayName.value = value;
	document.all.channelForm.channelName.value = value;
}


function checkForm(){
	var channelForm = document.all.channelForm;

	var channelName = channelForm.channelName.value;
	if(channelName.search(/\S/g)==-1){
		alert("请输入频道中文名称!");
		//channelForm.channelName.focus();
		return false;
	}

	//特殊字符\/|:*?<>"'
	if(channelName.search(/[\\\/\|:\*\?<>"']/g)!=-1){
		alert("频道中文名称不能有\\/|:*?<>\"'!等特殊字符");
		//channelForm.channelName.focus();
		return false;
	}

	var displayName = channelForm.displayName.value;
	if(displayName.search(/\S/g)==-1){
		alert("请输入频道显示名称!");
		//channelForm.displayName.focus();
		return false;
	}

	if(displayName.search(/[\\\/\|:\*\?<>"']/g)!=-1){
		alert("频道显示名称不能有\\/|:*?<>\"'!等特殊字符");
		//channelForm.displayName.focus();
		return false;
	}

	var channelPath = channelForm.channelPath.value;
	if(channelPath.search(/\S/g)==-1){
		alert("请输入频道路径!");
		///channelForm.channelPath.focus();
		return false;
	}
	if(channelPath.search(/[^A-Za-z0-9]/g) !=-1){
		alert("频道路径只能包含字母和数字!");
		//channelForm.channelPath.focus();
		return false;
	}
	var order = channelForm.order.value;
	if(order.search(/\D/g)!=-1){
		alert("顺序只能是数字!");
		//channelForm.order.focus();
		return false;
	}
	var pubType = document.getElementsByName("pubType");
	if(pubType[0].checked==true){
		var pubFileName = channelForm.pubFileName.value;
		if(pubFileName.search(/\S/g)==-1){
			alert("按模板发布，发布文件名不许为空!");
			//channelForm.pubFileName.focus();
			return false;
		}
		if(pubFileName.search(/^.*\.[html|htm]*$/g)==-1){
			alert("文件名格式不对!请输入以html或htm后缀结尾的文件名。");
			//channelForm.pubFileName.focus();
			return false;
		}
	}
	if(pubType[1].checked==true){
		var chnlIndexPage = channelForm.chnlIndexPage.value;
		if(chnlIndexPage.search(/\S/g)==-1){
			alert("按首页文件发布，文件首页不许为空!");
			//channelForm.chnlIndexPage.focus();
			return false;
		}
		//if(chnlIndexPage.search(/^.*\.[html|htm]*$/g)==-1){
			//alert("首页文件格式不对!请输入以html或htm后缀结尾的文件名。");
			//channelForm.pubFileName.focus();
			//return false;
		//}
	}
	if(pubType[2].checked==true){
		var chnlIndexDoc = channelForm.chnlIndexDoc.value;
		var docId = channelForm.docId.value; 
		if(chnlIndexDoc.search(/\S/g)==-1 || docId.length==0){
			alert("按文档发布，文档名不许为空!");
			//channelForm.chnlIndexPage.focus();
			return false;
		}
	}
	if(pubType[3].checked==true){
		var chnlIndexChnl = channelForm.chnlIndexChnl.value;
		var chnlId = channelForm.chnlId.value; 
		if(chnlIndexChnl.search(/\S/g)==-1 || chnlId.length==0){
			alert("引用频道首页，频道名不许为空!");
			//channalForm.chnlIndexPage.focus();
			return false;
		}
	}
	var navigatorlevel = channelForm.navigatorlevel.value;
	if(navigatorlevel.search(/[^0-9]/g) !=-1){
		alert("频道路径只能是数字!");
		//channelForm.navigatorlevel.focus();
		return false;
	}
	document.all.channelForm.submit();
	//防止二次提交
	document.all("button")[0].disabled = true;
	document.all("button")[1].disabled = true;
}
//选择文件
function setCustomForm(type)
{
	//openWin("../docManage/select_custom_form.jsp",400,400);
	openWin("../docManage/formfile_choise_frames.jsp?type=" + type,600,480);
}
//使用默认值
function setDefault(type)
{
	if(type=="add")
		channelForm.add.value = "doc_gather.jsp";
	if(type=="update")
		channelForm.update.value = "doc_gather_update.jsp";
	if(type=="list")
		channelForm.list.value = "doc_list.jsp";
}
function preview1(){
	var channelForm = document.all.channelForm;
	var siteId = "<%=siteId%>";
	var templateId = channelForm.outlineTemplateId[channelForm.outlineTemplateId.selectedIndex].value;
	//alert("预览站点id为:"+siteId+",模板id为"+templateId+"的模板!");
	window.open("../templateManage/template_previewbyid.jsp?tplId=" + templateId + "&siteId=<%=siteId%>");
}
function preview2(){
	var channelForm = document.all.channelForm;
	var siteId = "<%=siteId%>";
	var templateId = channelForm.detailTemplateId[channelForm.detailTemplateId.selectedIndex].value;
	//alert("预览站点id为:"+siteId+",模板id为"+templateId+"的模板!");
	window.open("../templateManage/template_previewbyid.jsp?tplId=" + templateId + "&siteId=<%=siteId%>");
}

function shownavigatorMoreInfo(b){
	if(b){
		document.all.navigatorMoreInfo1.style.display="block";
		document.all.navigatorMoreInfo2.style.display="block";
		document.all.navigatorMoreInfo3.style.display="block";
		document.all.navigatorMoreInfo4.style.display="block";
		document.all.navigatorMoreInfo5.style.display="block";
	}else{
		document.all.navigatorMoreInfo1.style.display="none";
		document.all.navigatorMoreInfo2.style.display="none";
		document.all.navigatorMoreInfo3.style.display="none";
		document.all.navigatorMoreInfo4.style.display="none";
		document.all.navigatorMoreInfo5.style.display="none";
	}
}

var currFile;
function chooseImage(str){
	currFile = eval("document.all."+str);
	openWin("chooseImageFrameset.jsp",700,580);
}

function setImage(uri){
	currFile.value  = uri;
}
function chooseChnlIndexPage(str){	
	currFile = eval("document.all."+str);
	openWin("chooseImageFrameset.jsp?fileFlag=1",520,580);
}
function changePupType(ob1,ob2){
	ob1.style.display = "";
	ob2.style.display = "none";
}
//模板预览
function templatePreview(obj){
	var tplId = obj.value;
	window.open("<%=request.getContextPath()%>/cms/templateManage/template_previewbyid.jsp?tplId=" + tplId + "&siteId=<%=siteId%>");
}
function changePupType(flag){
	if(flag==1){
		document.all.pubFileName.disabled="disabled";
		document.all.pubFileName.value="";
		document.all.outlineTemplateId.disabled="disabled";
		document.all.outlineTemplatePreview.disabled="disabled";
		document.all.chnlIndexPage.disabled="";
		document.all.setChnlIndexPage.disabled="";
		document.all.setChnlIndexDoc.disabled="disabled";
		document.all.previewChnlIndexDoc.disabled="disabled";
		
		document.all.outlineTemplatePreview.disabled="disabled";
		document.all.outlineTemplateChoice.disabled="disabled";
		
		document.all.chnlIndexChnl.disabled="disabled";
		document.all.setChnlIndexChnl.disabled="disabled";
		document.all.previewChnlIndexChnl.disabled="disabled";
	}else if(flag==0){
		document.all.pubFileName.disabled="";
		document.all.outlineTemplateId.disabled="";
		document.all.outlineTemplatePreview.disabled="";
		document.all.chnlIndexPage.disabled="disabled";
		document.all.setChnlIndexPage.disabled="disabled";
		document.all.pubFileName.value="default.htm";
		document.all.setChnlIndexDoc.disabled="disabled";
		document.all.previewChnlIndexDoc.disabled="disabled";
		
		document.all.outlineTemplatePreview.disabled="";
		document.all.outlineTemplateChoice.disabled="";
		
		document.all.chnlIndexChnl.disabled="disabled";
		document.all.setChnlIndexChnl.disabled="disabled";
		document.all.previewChnlIndexChnl.disabled="disabled";
	}else if(flag==2){
		document.all.pubFileName.disabled="disabled";
		document.all.outlineTemplateId.disabled="disabled";
		document.all.outlineTemplatePreview.disabled="disabled";
		document.all.chnlIndexPage.disabled="disabled";
		document.all.setChnlIndexPage.disabled="disabled";
		document.all.chnlIndexDoc.disabled="";
		document.all.setChnlIndexDoc.disabled="";
		document.all.previewChnlIndexDoc.disabled="";
		
		document.all.outlineTemplatePreview.disabled="disabled";
		document.all.outlineTemplateChoice.disabled="disabled";
		
		document.all.chnlIndexChnl.disabled="disabled";
		document.all.setChnlIndexChnl.disabled="disabled";
		document.all.previewChnlIndexChnl.disabled="disabled";
	}else if(flag==3){
		document.all.pubFileName.disabled="disabled";
		document.all.outlineTemplateId.disabled="disabled";
		document.all.outlineTemplatePreview.disabled="disabled";
		document.all.chnlIndexPage.disabled="disabled";
		document.all.setChnlIndexPage.disabled="disabled";
		document.all.chnlIndexDoc.disabled="disabled";
		document.all.setChnlIndexDoc.disabled="disabled";
		document.all.previewChnlIndexDoc.disabled="disabled";
		
		document.all.outlineTemplatePreview.disabled="disabled";
		document.all.outlineTemplateChoice.disabled="disabled";
		
		document.all.chnlIndexChnl.disabled="";
		document.all.setChnlIndexChnl.disabled="";
		document.all.previewChnlIndexChnl.disabled="";
	}
}	
function chooseDoc(){
	var re = openWin("channel_doc_xz.jsp?siteid=<%=siteId%>&flag=5",750,550);
	if(re!=undefined){
		var vl = re.split(":");
		document.all.docId.value=vl[0];
		document.all.chnlIndexDoc.value=vl[1] + "/" + vl[2];
	}
}
//发布文档预览
function previewDoc(){
	var docId = document.all.docId.value;
	if(docId!=""){
		channelForm.action="../docManage/previewPubDocHandle.jsp?docId="+docId;
		channelForm.target="exeman";
		channelForm.submit();
	}else{
		alert("请先选择文档！");
	}
}	
//选择频道
function chooseChnl(){
	var re = openWin("../docManage/multi_channel_select_frame.jsp?siteid=<%=siteId%>&type=single&taskType=chnlIndexPage&siteid=",400,500);	
	if(re != undefined){
		var chnlInfo = re.split(":");
		var chnlId = chnlInfo[1].substring(0,chnlInfo[1].indexOf(","));
		var chnlName = chnlInfo[0].substring(0,chnlInfo[0].indexOf(";"));
		var chnlSiteId = chnlInfo[2].substring(0,chnlInfo[2].indexOf(","));
		if(chnlId != ""){ 
			document.all.chnlId.value = chnlSiteId + ":" + chnlId;
			document.all.chnlIndexChnl.value = chnlName;
		}else{
			alert("对不起，不能选择当前频道！");
		}
	}
}
//频道预览
function previewChnl(){
	var chnlId = document.all.chnlId.value;
	var url = "../siteManage/siteview_do.jsp?siteId=" + chnlId.split(":")[0] + "&channelId=" +  chnlId.split(":")[1];
	var w = openWin(url,480,320);
}
</script>
</head>
<body  bgcolor="#F7F8FC"  onLoad="setFocus();shownavigatorMoreInfo(false)">
<form action="channel_add_do.jsp" method="post"  name="channelForm" target="exeman" id="channelForm">
	<INPUT TYPE="hidden" NAME="siteId" value="<%=siteId%>">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
		<tr >
			<td colspan="2" class="topDIV"  align="left" valign="top">&nbsp;<div class="siteCTitle">新建频道</div></td>
		</tr>
		<tr >
			<td width="4%" height="20" align="right">
				<img src="../images/ico_point.gif" width="25" height="25">
			</td>
			<td height="20" class="cms_title_blue">
				频道基本信息
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<tab:tabContainer id="foo-channel-add" selectedTabPaneId="foo-channel">
					<tab:tabPane id="foo-channel" tabTitle="基本信息">
						<table width="98%" border="0" cellpadding="3" cellspacing="0" class="Templateedittable">
							<tr>
								<td width="130" height="24" align="right" nowrap>所属站点:</td>
								<td height="24">
									<%=siteName%>
								</td>
							</tr>
							<tr <%="".equals(parentChannelId)?"style=\"display:none\"":""%>>
								<td width="130" height="24" align="right" nowrap>父频道:</td>
								<td height="24">
									<%=parentChannelName%>
									<input name="parentChannelId" type="hidden" value="<%=parentChannelId%>"/>
								</td>
							</tr>
							<tr>
								<td width="130" height="24" align="right" nowrap>频道调用名称:</td><!--为了频道显示名称能去其它路径名称，将原来的displayName和channelName进行对调 -->
								<td height="24">
									<input type="text" id="displayName" name="displayName" onpropertychange="onNameChange(this.value)" class="cms_text" style="width:300px"  maxlength="20"/>
									<span style="color: #FF0000">*此名称用于前台调用，请不要随意修改</span>
								</td>
							</tr>
							<tr>
								<td width="130" height="24" align="right" nowrap>显示名称:</td>
								<td height="24">
									<input type="text" id="channelName" name="channelName"  class="cms_text" style="width:300px"  maxlength="20" />
									<span style="color: #FF0000">*</span>
								</td>
								
							</tr>
							<tr>
								<td width="130" height="24" align="right" nowrap>频道路径:</td>
								<td height="24">
									<input type="text" id="channelPath" name="channelPath" class="cms_text"  maxlength="20" style="width:300px"/>
									<span style="color: #FF0000">*</span>
								</td>
							</tr>
							<tr>
								<td width="130" rowspan="2" align="right" nowrap>工作流程:</td>
								<td height="24">
								<%
									String wfName = "";
									String wfId = "";
									if(!"".equals(parentChannelId)){
										ChannelManager chnlMng = new ChannelManagerImpl();
										List workflowInfo = chnlMng.getFlowInfo(parentChannelId);
										if(workflowInfo!=null && workflowInfo.size()>=2){
											if(workflowInfo.get(0)!=null && workflowInfo.get(1)!=null){
												wfName = workflowInfo.get(1).toString();
												wfId = workflowInfo.get(0).toString();
											}
										}
									}else{
										SiteManager sm = new SiteManagerImpl();
										List workflowInfo = sm.getFlowInfo(siteId);
										if(workflowInfo!=null && workflowInfo.size()>=2){
											if(workflowInfo.get(0)!=null && workflowInfo.get(1)!=null){
												wfName = workflowInfo.get(1).toString();
												wfId = workflowInfo.get(0).toString();
											}
										}
									}
									%>
										<input type="radio" name="workFlowFrom" value="0" checked onClick="changeWorkFlowType(0)">
									<%
									FlowManager fm = new FlowManagerImpl();
									List list = fm.getFlowList();
									%>
									<select id="workFlow" name="workFlow" class="cms_select" style="width:130px">
										<%= JSPFunc.getOption(list,"".equals(wfId)?"1":wfId) %>
									</select>
								</td>
							</tr>
							<tr>
								<td height="24">
									<input type="radio" name="workFlowFrom" value="1" onClick="changeWorkFlowType(1)">
									继承<%="".equals(parentChannelId)?"父站点":"父频道"%>的流程设置(<span style="color: #FF0000"><%=wfName%></span>)
								</td>
							</tr>
							<tr>
								<td width="130" height="24" align="right" nowrap>
									专题报道:
								</td>
								<td height="24">
									<input name="specialflag" id="specialflag" type="checkbox"> 设置为专题报道
								</td>
							</tr>
							<tr>
								<td width="130" height="24" align="right" nowrap>
									频道排列的顺序:
								</td>
								<td height="24">
									<input name="order" id="order" type="text" size="8"	maxlength="10" class="cms_text" value="0">
								</td>
							</tr>
							<tr>
								<td width="130" height="24" align="right" nowrap>
									频道描述:
								</td>
								<td height="24">
									<textarea rows="10" cols="80" name="channel_desc"></textarea>
								</td>
							</tr>
							
					  </table>
					</tab:tabPane>
					<tab:tabPane id="bar-channel-publish" tabTitle="发布相关设置">
							<fieldset>
								<LEGEND align=left><input type="radio" id="pubType" name="pubType" value="0"  checked="checked" onClick="changePupType(0)">按模板发布	</LEGEND>
								<table>
									<tr>
										<td width="123" height="24" align="right" nowrap>发布文件名:</td>
										<td height="24">
											<input type="text" id="pubFileName" name="pubFileName" class="cms_text" style="width:300px" maxlength="20" value="default.htm"/>
										</td>
									</tr>
									<tr>
										<td width="123" height="24" align="right" nowrap> 概览模板:</td>
										<td height="24">
											<input name="outlineTemplateId" id="outlineTemplateId" type="hidden" value="" />
											<input name="otName" id="otName" type="text" style="width:130px" value=""  class="cms_text" disabled />
											<input class="Channel_5wordsBtn" type="button" value="选 择" id="outlineTemplateChoice" name="outlineTemplateChoice" onClick="setChannelOutlineTemplateId()" >
											<input class="Channel_5wordsBtn" type="button" value="预 览" id="outlineTemplatePreview" name="outlineTemplatePreview" onClick="templatePreview(outlineTemplateId)">
										</td>
									</tr>
								</table>
							</fieldset>
							<fieldset>
								<LEGEND align=left><input type="radio" id="pubType" name="pubType" value="1" onClick="changePupType(1)">自定义首页</LEGEND>
								<table>
									<tr>
										<td width="123" height="24" align="right" nowrap>设置频道首页:	</td>
										<td height="24">
											<input type="text" id="chnlIndexPage" name="chnlIndexPage" class="cms_text" style="width:200px"  disabled="disabled">
											<input name="setChnlIndexPage" id="setChnlIndexPage" type="button" onClick="chooseChnlIndexPage('chnlIndexPage')" value="选择" class="Channel_5wordsBtn"  disabled="disabled">
										</td>	
									</tr> 
								</table>
							</fieldset>
							<fieldset>
								<LEGEND align=left><input type="radio" id="pubType" name="pubType" value="2" onClick="changePupType(2)" />引用文档首页</LEGEND>
								<table border="0" cellpadding="3" cellspacing="0">
									<tr>
										<td width="123" height="24" align="right" nowrap>设置文档:</td>
										<td height="24">
											<input type="text" id="chnlIndexDoc" name="chnlIndexDoc" class="cms_text" style="width:200px" maxlength="20"  disabled="disabled"/> 
											<input class="Channel_5wordsBtn" name="setChnlIndexDoc" id="setChnlIndexDoc" type="button" onClick="chooseDoc()" value="选 择" disabled="disabled">
											<input type="hidden" id="docId" name="docId">
											<input class="Channel_5wordsBtn" name="previewChnlIndexDoc" id="previewChnlIndexDoc" type="button" onClick="previewDoc()" value="预 览" disabled="disabled">
										</td>	
									</tr> 
								</table>
							</fieldset>
							<fieldset>
								<LEGEND align=left><input type="radio" id="pubType" name="pubType" value="3" onClick="changePupType(3)" />引用频道首页</LEGEND>
								<table border="0" cellpadding="3" cellspacing="0">
									<tr>
										<td width="123" height="24" align="right" nowrap>设置频道:	</td>
										<td height="24">
											<input type="text" id="chnlIndexChnl" name="chnlIndexChnl" class="cms_text" style="width:200px" maxlength="20"  disabled="disabled"/> 
											<input class="Channel_5wordsBtn" name="setChnlIndexChnl" id="setChnlIndexChnl" type="button" onClick="chooseChnl()" value="选 择" disabled="disabled">
											<input type="hidden" id="chnlId" name="chnlId" >
											<input class="Channel_5wordsBtn" name="previewChnlIndexChnl" id="previewChnlIndexChnl" type="button" onClick="previewChnl()" value="预 览" disabled="disabled">
										</td>	
									</tr> 
								</table>
							</fieldset>
							<fieldset>
								<LEGEND align=left>其他相关设置	</LEGEND>
								<table width="98%" border="0" cellpadding="3" cellspacing="0"  class="Templateedittable">
								<tr>
									<td width="130" height="24" align="right" nowrap> 细览模板: </td>
									<td height="24">
                                        <!-- add by ge.tao 2007/07/08 -->
                                       	<input name="detailTemplateId" id="detailTemplateId" type="hidden" value="" />
										<input name="dtName" id="dtName" type="text" style="width:130px" value=""  class="cms_text" disabled />
										<input class="Channel_5wordsBtn" type="button" value="选 择" id="detailTemplateChoice" name="detailTemplateChoice" onClick="setChannelDetailTemplateId()">
										<input class="Channel_5wordsBtn" type="button" value="预 览" onClick="templatePreview(outlineTemplateId)">
									</td>
								</tr>
<!--								<tr>-->
<!--									<td width="130" height="24" align="right" nowrap> 评论模板: </td>-->
<!--									<td height="24">-->
<!--										<select name="commentTemplateId" id="commentTemplateId" style="width:130px">-->
										<%
											//List templateList3 = tpltMng.getTemplateInfoListofSite(Integer.parseInt(siteId),3);
											//for(int i=0;templateList3!=null && i<templateList3.size();i++){
											//	Template t = (Template)templateList3.get(i);
											//	out.println("<option value=\"" + t.getTemplateId() + "\"" +
											//		" templatePath=\"" + t.getTemplatePath() + "\"  templateFileName=\"" + t.getTemplateFileName() + "\" >"
											//		+ t.getName() + "</option>");
											//}
										%>
<!--										</select>-->
<!--										<input type="button" class="Channel_5wordsBtn" value="预览" onClick="templatePreview(detailTemplateId)">-->
<!--									</td>-->
<!--								</tr>-->
								<tr>
									<td width="130" height="24" align="right" nowrap>文档评论页面:	</td>
									<td height="24">
											<input type="text" id="commentPagePath" name="commentPagePath" class="cms_text" style="width:200px" /> 
											<input class="Channel_5wordsBtn" name="setCommentPagePath" id="setCommentPagePath" type="button" onClick="chooseChnlIndexPage('commentPagePath')" value="选 择">
									</td>
								</tr>
								<tr>
									<td width="130" height="24" align="right" nowrap>频道首页发布模式</td>
									<td height="24">
										<input type="radio" name="outlineIsDynamic" value="0" checked>静态
										<input name="outlineIsDynamic" type="radio" value="1">动态
									</td>
								</tr>
								<tr>
									<td width="130" height="24" align="right" nowrap>频道首页否受保护</td>
									<td height="24">
										<input type="radio" name="outlineIsProtect" value="0" checked>不受保护
										<input name="outlineIsProtect" type="radio" value="1">受保护
									</td>
								</tr>
								<tr>
									<td width="130" height="24" align="right" nowrap>频道文档发布模式</td>
									<td height="24">
										<input type="radio" name="docIsDynamic" value="0" checked>静态
										<input name="docIsDynamic" type="radio" value="1">动态
										<input name="docIsDynamic" type="radio" value="2">发布附件
									</td>
								</tr>
								<tr>
									<td width="130" height="24" align="right" nowrap>频道文档是否受保护</td>
									<td height="24">
										<input type="radio" name="docIsProtect" value="0" checked>不受保护
										<input name="docIsProtect" type="radio" value="1">受保护
									</td>
								</tr>
								<tr id="outlinePictureInfo">
									<td width="130" height="24" align="right" nowrap>概览图片:</td>
									<td height="24">
										<input name="outlinePicture" type="text" id="outlinePicture" size="10" value="" style="width:200px" class="cms_text" readonly>
										<input type="button"  onClick="chooseImage('outlinePicture')"  value="选择" class="Channel_5wordsBtn" >
									</td>
								</tr>
						  </table>
						</fieldset>
					</tab:tabPane>
					<tab:tabPane id="bar-channel-customForm" tabTitle="自定义表单">
						<table width="98%" border="0" cellpadding="3" cellspacing="0"  class="Templateedittable">
							<tr>
								<td height="24" align="right">设置自定义表单:</td>
								<td height="24">
									新增文档表单<input name="add" type="text" style="width:200px" class="cms_text" value="">
									<input name="CustomForm" type="button"  onClick="setCustomForm('add')"  value="选择" class="Channel_5wordsBtn" >
									<input name="CustomForm" type="button"  onClick="setDefault('add')"  class="Channel_7wordsBtn" value="使用默认值">
									<br>
									修改文档表单<input name="update" type="text" style="width:200px" class="cms_text" value="">
									<input name="CustomForm" type="button"  onClick="setCustomForm('update')"  value="选择" class="Channel_5wordsBtn" >
									<input  name="CustomForm" type="button"  onClick="setDefault('update')"  class="Channel_7wordsBtn" value="使用默认值">
									<br>
									文档列表表单<input name="list" type="text" style="width:200px" class="cms_text" value="">
									<input name="CustomForm" type="button"  onClick="setCustomForm('list')"  value="选择" class="Channel_5wordsBtn" >
									<input name="CustomForm" type="button"  onClick="setDefault('list')"  class="Channel_7wordsBtn" value="使用默认值">
								</td>
							</tr>
					  </table>
					</tab:tabPane>
					<tab:tabPane id="bar-channel-nav" tabTitle="导航频道设置">
						<table width="98%" border="0" cellpadding="3" cellspacing="0"  class="Templateedittable">
							<tr>
								<td width="130" height="24" align="right" nowrap>频道是否作为导航频道</td>
								<td height="24">
									<input type="radio" id="isnavigator" name="isnavigator" value="0"  onclick="shownavigatorMoreInfo(false)" checked>
									不是
									<input  id="isnavigator" name="isnavigator" type="radio" value="1" onClick="shownavigatorMoreInfo(true)">
									是
								</td>
							</tr>
							<tr>
								<td width="130" height="24" align="right" nowrap>频道打开模式</td>
								<td height="24">
									<input type="radio" id="openTarget" name="openTarget" value="_self"   checked>
									_self
									<input  id="openTarget" name="openTarget" type="radio" value="_blank" >
									_blank
									<input  id="openTarget" name="openTarget" type="radio" value="_parent" >
									_parent
									<input  id="openTarget" name="openTarget" type="radio" value="_top" >
									_top
								</td>
							</tr>
							<tr id="navigatorMoreInfo1">
								<td width="130" height="24" align="right" nowrap>频道导航级别</td>
								<td height="24"><input name="navigatorlevel" type="text" id="navigatorlevel" size="10" class="cms_text"></td>
							</tr>
							<tr  id="navigatorMoreInfo2">
								<td width="130" height="24" align="right" nowrap>鼠标进入时的图片</td>
								<td height="24">
									<input name="mouseInImage" type="text" id="mouseInImage" size="10" style="width:200px" class="cms_text" readonly>
									<input type="button"  onClick="chooseImage('mouseInImage')"  value="选择" class="Channel_5wordsBtn" >
								</td>
							</tr>
							<tr  id="navigatorMoreInfo3">
								<td width="130" height="24" align="right" nowrap>鼠标离开时的图片</td>
								<td height="24">
									<input name="mouseOutImage" type="text" id="mouseOutImage" size="10" style="width:200px" class="cms_text" readonly>
									<input type="button"  onClick="chooseImage('mouseOutImage')"  value="选择" class="Channel_5wordsBtn" >
								</td>
								
							</tr>
							<tr  id="navigatorMoreInfo4">
								<td width="130" height="24" align="right" nowrap>鼠标点击时的图片</td>
								<td height="24">
									<input name="mouseClickImage" type="text" id="mouseClickImage" size="10" style="width:200px" class="cms_text" readonly>
									<input type="button"  onClick="chooseImage('mouseClickImage')"  value="选择" class="Channel_5wordsBtn" >	
								</td>
							</tr>
							<tr  id="navigatorMoreInfo5">
								<td width="130" height="24" align="right" nowrap>鼠标弹起时的图片</td>
								<td height="24">
									<input name="mouseUpImage" type="text" id="mouseUpImage" size="10" style="width:200px" class="cms_text" readonly>
									<input type="button"  onClick="chooseImage('mouseUpImage')"  value="选择" class="Channel_5wordsBtn" >	
								</td>
							</tr>
					  </table>
					</tab:tabPane>
				</tab:tabContainer>
			</td>
		</tr>
  </table>
	<br>
	<div style="text-align:center">
		<INPUT name="button" type="button" class="cms_button" onClick="checkForm()"  value="保存">
		&nbsp;&nbsp;&nbsp;
		<INPUT name="button" type="button" class="cms_button" onClick="window.close()"  value="关闭">
		<br><br><br>
	</div>
</form>
<iframe name="exeman" width="0" height="0" style="display:none"></iframe>
</body>
</html>