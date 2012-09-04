<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.util.*" %>
<%@page import="com.frameworkset.platform.cms.sitemanager.*" %>
<%@page import="com.frameworkset.platform.cms.flowmanager.*" %>
<%@page import="com.frameworkset.common.poolman.DBUtil" %>
<%@page import="com.frameworkset.platform.framework.*"%>
<%@page import="java.util.*,com.frameworkset.platform.cms.util.CMSUtil" %>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %> 
<%@ page import="com.frameworkset.platform.cms.searchmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.bean.*"%>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	CMSSearchManager searchManager = new CMSSearchManager();
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<script src="../inc/js/func.js"></script>	
	<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
	<tab:tabConfig/>
	<title>新建站点</title>
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
function onSiteNameChange(value){
	if(value.search(/[^A-Za-z0-9]/g) == -1){
		document.all.siteForm.secondName.value = value;
	}
	document.all.siteForm.siteDesc.value = value;
}

//ftp连接检测
function testFtpLink(){
	var siteForm = document.all.siteForm;
	if(siteForm.ftpIp.value.search(/\S/g)==-1){
		alert("请输入ftp地址!");
		siteForm.ftpIp.focus();
		return false;
	}

	var port = siteForm.ftpPort.value;
	if(port.search(/\D/g)!=-1 || isNaN(port) || parseInt(port)<0 || parseInt(port)!=parseFloat(port)){
		alert("请输入正确ftp端口!");
		siteForm.ftpPort.focus();
		return false;
	}else{
		siteForm.ftpPort.value = parseInt(port);
	}
	if(siteForm.ftpUser.value.search(/\W/g)!=-1 ){
		alert("ftp用户只能为数字、字母和下划线!");
		siteForm.ftpUser.focus();
		return false;
	}
	if(siteForm.ftpPassword.value.search(/\W/g)!=-1){
		alert("ftp密码只能为数字、字母和下划线!");
		siteForm.ftpPassword.focus();
		return false;
	}

	//替换开头和结尾的空字符replace(/(^\s+)|(\s+$)/g,"")
	var url="test_ftp.jsp?ftpip="+
			SiteForm.FtpIp.value.replace(/\s/g,"")+
			"&ftpport="+
			SiteForm.FtpPort.value.replace(/\s/g,"")+
			"&ftpuser="+
			SiteForm.FtpUser.value.replace(/\s/g,"")+
			"&ftppasswd="+
			SiteForm.FtpPassword.value.replace(/\s/g,"");
	alert(url);
	window.open(url,"exeman");
}

//站点窗口输入参数校验
function checkForm(){
	var siteForm = document.all.siteForm;

	var siteName = siteForm.siteName.value;
	if(siteName.search(/\S/g)==-1){
		alert("请输入站点中文名称!");
		//siteForm.siteName.focus();
		return;
	}
	//特殊字符\/|:*?<>"'
	if(siteName.search(/[\\\/\|:\*\?<>"']/g)!=-1){
		alert("站点中文名称不能有\\/|:*?<>\"'!等特殊字符");
		//siteForm.siteName.focus();
		return;
	}

	var secondName = siteForm.secondName.value;
	if(secondName.search(/\S/g)==-1){
		alert("请输入站点英文名称!");
		//siteForm.secondName.focus();
		return;
	}
	if(secondName.search(/[^A-Za-z0-9]/g) !=-1){
		alert("站点英文名称只能包含字母和数字!");
		//siteForm.secondName.focus();
		return;
	}
	if(siteForm.indexFileName.value.search(/\S/g)==-1){
		alert("请输入首页文件名!");
		//siteForm.indexFileName.focus();
		return false;
	}
	if(siteForm.indexFileName.value.search(/[^A-Za-z0-9\-_.]/g) != -1){
		alert("首页文件名只能包含A-Za-z0-9-_.!");
		//siteForm.indexFileName.focus();
		return false;
	}
	/*
	if(siteForm.indexFileName.value.search() != -1){
		alert("首页文件名格式不对");
		siteForm.indexFileName.focus();
		return false;
	}
	*/
	if(siteForm.ftpIp.value.search(/[\\\/\|:\*\?<>"']/g)!=-1){
		alert("ftpip不能有\\/|:*?<>\"'!等特殊字符");
		//siteForm.ftpIp.focus();
		return false;
	}
	var port = siteForm.ftpPort.value;
	if(port.search(/\D/g)!=-1 || isNaN(port) || parseInt(port)<0 || parseInt(port)!=parseFloat(port)){
		alert("请输入正确ftp端口!");
		//siteForm.ftpPort.focus();
		return false;
	}else{
		siteForm.ftpPort.value = parseInt(port);
	}
	if(siteForm.ftpUser.value.search(/\W/g)!=-1){
		alert("用户名只能包含数字、字符和下划线!");
		//siteForm.ftpUser.focus();
		return false;
	}
	if(siteForm.ftpFolder.value.search(/[^A-Za-z0-9]/g) != -1){
		alert("目录中只能包含字母和数字!");
		//siteForm.ftpFolder.focus();
		return false;
	}

	if(siteForm.ftpFolder.value.search(/[\|:\*\?<>"']/g) != -1){
		alert("目录中不能包括|:*?<>\"'等特殊字符!");
		//siteForm.ftpFolder.focus();
		return false;
	}
	if(siteForm.ftpIp.value != "" || siteForm.localpublishpath.value != ""){
		if(siteForm.webHttp.value == ""){
			alert("指定了本地发布目录或设置了远程ftp，则发布页面只能通过域名访问！\n请填写域名设置！");
			return false;
		}
		//var reg = "http://([\w-]+\.)+[\w-]+(/[\w-./?%&=]*)?";
		if(!siteForm.webHttp.value.indexOf("http://") == 0){
			alert("域名设置不正确，请重新填写！");
			return false;
		}
	}
	var order = siteForm.order.value;
	if(order.search(/\D/g)!=-1){
		alert("请输入数字!");
		//siteForm.order.focus();
		return false;
	}else{
		siteForm.order.value = parseInt(order);
	}
	//本地发布和远程发布标识
	siteForm.publishDestination.value = "";
	if(siteForm.local.checked)
		siteForm.publishDestination.value = "0";
	if(siteForm.remote.checked)
		siteForm.publishDestination.value = "1";
	if(siteForm.local.checked&&siteForm.remote.checked)
		siteForm.publishDestination.value = "2";
	if(siteForm.publishDestination.value=="")
	{
		alert("请选择发布地点！");
		return false;
	}
	//布结果的分发方式数组
	siteForm.distributeManners.value = "0,";
	if(siteForm.MAIL_DISTRIBUTE.checked)
		siteForm.distributeManners.value += "2" + ",";
	if(siteForm.RSS_DISTRIBUTE.checked)
		siteForm.distributeManners.value += "1" + ",";
	document.all.siteForm.submit();
	//防止二次提交
	document.all("button")[0].disabled = true;
	document.all("button")[1].disabled = true;
}

function changeWorkFlowType(type){
	if(type == 1){
		document.all.siteForm.workFlow.disabled = true;
	}else{
		document.all.siteForm.workFlow.disabled = false;
	}
}

//默认输入焦点
function setFocus(){
	//document.all.siteForm.siteName.focus();
}
//set ftp的属性是否可见
function setftpstate()
{
	var pd = siteForm.publishDestination.value;
	if(pd==2)
		document.all.ftpstate.style.display = "";
	else
		document.all.ftpstate.style.display = "none";
}
function testFtp()
{   
    document.all.siteForm.ftpTestButton.value = "正在测试中....请稍后.";
	var ftpUser = document.all.siteForm.ftpUser.value;
	var ftpPassword = document.all.siteForm.ftpPassword.value;
	var ftpPort = document.all.siteForm.ftpPort.value;
	var ftpIp =document.all.siteForm.ftpIp.value;
	var urlString = "site_ftp_test.jsp?ftpUser="+ftpUser+"&ftpPassword="+ftpPassword+"&ftpPort="+ftpPort+"&ftpIp="+ftpIp;
	showModalDialog(urlString,"setChannelOutlineTemplateId","dialogWidth:400px;dialogHeight:200px;help:no;scroll:no;status:no");
	document.all.siteForm.ftpTestButton.value = "FTP连接测试";
}

function changeclass(){
		var id;
		id=document.siteForm.index_level.options[document.siteForm.index_level.selectedIndex].value;
		switch(id)
		{
			case '1':
			document.siteForm.index_day.options.length=31;
			document.siteForm.index_day.options[0].value='1';
			document.siteForm.index_day.options[0].text='每月1号';
			
			document.siteForm.index_day.options[1].value='2';
			document.siteForm.index_day.options[1].text='每月2号';
			
			document.siteForm.index_day.options[2].value='3';
			document.siteForm.index_day.options[2].text='每月3号';
			
			document.siteForm.index_day.options[3].value='4';
			document.siteForm.index_day.options[3].text='每月4号';
			
			document.siteForm.index_day.options[4].value='5';
			document.siteForm.index_day.options[4].text='每月5号';
			
			document.siteForm.index_day.options[5].value='6';
			document.siteForm.index_day.options[5].text='每月6号';
			
			document.siteForm.index_day.options[6].value='7';
			document.siteForm.index_day.options[6].text='每月7号';
			
			document.siteForm.index_day.options[7].value='8';
			document.siteForm.index_day.options[7].text='每月8号';
			
			document.siteForm.index_day.options[8].value='9';
			document.siteForm.index_day.options[8].text='每月9号';
			
			document.siteForm.index_day.options[9].value='10';
			document.siteForm.index_day.options[9].text='每月10号';
			
			document.siteForm.index_day.options[10].value='11';
			document.siteForm.index_day.options[10].text='每月11号';
			
			document.siteForm.index_day.options[11].value='12';
			document.siteForm.index_day.options[11].text='每月12号';
			
			document.siteForm.index_day.options[12].value='13';
			document.siteForm.index_day.options[12].text='每月13号';
			
			document.siteForm.index_day.options[13].value='14';
			document.siteForm.index_day.options[13].text='每月14号';
			
			document.siteForm.index_day.options[14].value='15';
			document.siteForm.index_day.options[14].text='每月15号';
			
			document.siteForm.index_day.options[15].value='16';
			document.siteForm.index_day.options[15].text='每月16号';
			
			document.siteForm.index_day.options[16].value='17';
			document.siteForm.index_day.options[16].text='每月17号';
			
			document.siteForm.index_day.options[17].value='18';
			document.siteForm.index_day.options[17].text='每月18号';
			
			document.siteForm.index_day.options[18].value='19';
			document.siteForm.index_day.options[18].text='每月19号';
			
			document.siteForm.index_day.options[19].value='20';
			document.siteForm.index_day.options[19].text='每月20号';
			
			document.siteForm.index_day.options[20].value='21';
			document.siteForm.index_day.options[20].text='每月21号';
			
			document.siteForm.index_day.options[21].value='22';
			document.siteForm.index_day.options[21].text='每月22号';
			
			document.siteForm.index_day.options[22].value='23';
			document.siteForm.index_day.options[22].text='每月23号';
			
			document.siteForm.index_day.options[23].value='24';
			document.siteForm.index_day.options[23].text='每月24号';
			
			document.siteForm.index_day.options[24].value='25';
			document.siteForm.index_day.options[24].text='每月25号';
			
			document.siteForm.index_day.options[25].value='26';
			document.siteForm.index_day.options[25].text='每月26号';
			
			document.siteForm.index_day.options[26].value='27';
			document.siteForm.index_day.options[26].text='每月27号';
			
			document.siteForm.index_day.options[27].value='28';
			document.siteForm.index_day.options[27].text='每月28号';
			
			document.siteForm.index_day.options[28].value='29';
			document.siteForm.index_day.options[28].text='每月29号';
			
			document.siteForm.index_day.options[29].value='30';
			document.siteForm.index_day.options[29].text='每月30号';
			
			document.siteForm.index_day.options[30].value='30';
			document.siteForm.index_day.options[30].text='每月31号';
			break;
		
		case '0':
			document.siteForm.index_day.options.length=7;
			document.siteForm.index_day.options[0].value='1';
			document.siteForm.index_day.options[0].text='星期日';
			
			document.siteForm.index_day.options[1].value='2';
			document.siteForm.index_day.options[1].text='星期一';
			
			document.siteForm.index_day.options[2].value='3';
			document.siteForm.index_day.options[2].text='星期二';
			
			document.siteForm.index_day.options[3].value='4';
			document.siteForm.index_day.options[3].text='星期三';
			
			document.siteForm.index_day.options[4].value='5';
			document.siteForm.index_day.options[4].text='星期四';
			
			document.siteForm.index_day.options[5].value='6';
			document.siteForm.index_day.options[5].text='星期五';
			
			document.siteForm.index_day.options[6].value='7';
			document.siteForm.index_day.options[6].text='星期六';
			
			break;
		
		case '2':
			document.siteForm.index_day.options.length=1;
			document.siteForm.index_day.options[0].value='1';
			document.siteForm.index_day.options[0].text='每天';
		
		break;
		default:
			document.siteForm.index_day.options.length=7;
			document.siteForm.index_day.options[0].value='1';
			document.siteForm.index_day.options[0].text='星期日';
			
			document.siteForm.index_day.options[1].value='2';
			document.siteForm.index_day.options[1].text='星期一';
			
			document.siteForm.index_day.options[2].value='3';
			document.siteForm.index_day.options[2].text='星期二';
			
			document.siteForm.index_day.options[3].value='4';
			document.siteForm.index_day.options[3].text='星期三';
			
			document.siteForm.index_day.options[4].value='5';
			document.siteForm.index_day.options[4].text='星期四';
			
			document.siteForm.index_day.options[5].value='6';
			document.siteForm.index_day.options[5].text='星期五';
			
			document.siteForm.index_day.options[6].value='7';
			document.siteForm.index_day.options[6].text='星期六';
			break;
		}
	}

</script>
</head>
<body bgcolor="#F3F4F9" onLoad="setFocus()">
<form id="siteForm" name="siteForm"  target="exeman" action="site_add_do.jsp" method="post">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
		<tr >
			<td colspan="2" class="topDIV"  align="left" valign="top">新建站点
			<div class="siteCTitle">站点基本信息</div></td>
		</tr>
		<tr >
		</tr>
		<tr>
			<td colspan="2">
				<tab:tabContainer id="foo-site-add" selectedTabPaneId="foo-site">
					<tab:tabPane id="foo-site" tabTitle="基本信息">
						<table width="100%" border="0" cellpadding="3" cellspacing="0" class="Templateedittable">
							<%
								String parentSiteId = request.getParameter("siteId");
								String parentSiteName = request.getParameter("siteName");
								if(parentSiteId == null || "".equals(parentSiteId)){
									parentSiteId = "";
									parentSiteName = "";
								}
							%>
							<tr <%="".equals(parentSiteId)?"style=\"display:none\"":""%>>
								<td width="100" height="24" align="right" nowrap>父站点:</td>
								<td height="24">
									<input name="parentSiteName" type="text" class="cms_text" id="parentSiteName" style="width:300px" value="<%=parentSiteName%>" readonly/>
									<input name="parentSiteId" type="hidden" value="<%=parentSiteId%>" />
								</td>
							</tr>
							<tr>
								<td width="100" height="24" align="right" nowrap bgcolor="#EDEFF6">站点中文名称:</td>
								<td height="24">
									<input type="text" id="siteName" name="siteName" onpropertychange="onSiteNameChange(this.value)" class="cms_text" style="width:300px"  maxlength="45"/>
									<span style="color: #FF0000">*</span>
								</td>
							</tr>
							<tr>
								<td width="100" height="24" align="right" nowrap bgcolor="#EDEFF6">站点英文名称:</td>
								<td height="24">
									<input type="text" id="secondName" name="secondName" class="cms_text" style="width:300px"  maxlength="45"/>
									<span style="color: #FF0000">*</span>
								</td>
							</tr>
							<tr>
								<td width="100" height="24" align="right" nowrap bgcolor="#EDEFF6">站点描述:</td>
								<td height="24">
									<input type="text" id="siteDesc" name="siteDesc" class="cms_text"  maxlength="70" style="width:300px" />
								</td>
							</tr>
							
							<tr>
								<td height="24" rowspan="2" align="right" nowrap bgcolor="#EDEFF6">工作流程:</td>
								<td height="24">
									<%
									String wfName = "";
									String wfId = "";
									if(parentSiteId!=null && parentSiteId.trim().length()!=0){
										SiteManager sm = new SiteManagerImpl();
										List workflowInfo = sm.getFlowInfo(parentSiteId);
										if(workflowInfo!=null && workflowInfo.size()>=2){
											if(workflowInfo.get(0)!=null && workflowInfo.get(1)!=null){
												wfId = ""+workflowInfo.get(0);
												wfName = ""+workflowInfo.get(1);
											}
										}

									%>
										<input type="radio" name="workFlowFrom" value="0" checked onClick="changeWorkFlowType(0)">
									<%}



									FlowManager fm = new FlowManagerImpl();
									List list = fm.getFlowList();
									%>
									<select id="workFlow" name="workFlow" class="cms_select" style="width:130px">
										<%= JSPFunc.getOption(list,"".equals(wfId)?"1":wfId) %>
									</select>
								</td>
							</tr>
							<tr>
								<%if(parentSiteId!=null && parentSiteId.trim().length()!=0){%>
								<td height="24">
									<input type="radio" name="workFlowFrom" value="1" onClick="changeWorkFlowType(1)">
									继承父站点的流程设置(<span style="color: #FF0000"><%=wfName%></span>)
								</td>
								<%}%>
							</tr>
							<tr>
								<td height="24" align="right" nowrap bgcolor="#EDEFF6">站点状态:</td>
								<td height="24">
									<%
										SiteManager siteManager = new SiteManagerImpl();
										List statusList = siteManager.getSiteStatusList();
									%>
									<select id="siteStatus" name="siteStatus" class="cms_select" style="width:130px">
										<%= JSPFunc.getOption(statusList,"0") %>
									</select>
								</td>
							</tr>
							<tr>
								<td width="100" height="24" align="right" nowrap bgcolor="#EDEFF6"> 站点数据库: </td>
								<td height="24">							
									<select id="dbName" name="dbName"  class="cms_select" style="width:130px">
									<%
								java.util.Enumeration pools = com.frameworkset.common.poolman.util.SQLManager.getInstance().getAllPoolnames() ;
								while(pools.hasMoreElements())
								{
									String pool = (String)pools.nextElement();
								%>					                    
								<option value="<%=pool%>"><%=pool%></option>
								<%	}%>								
									
									</select>
								</td>
							</tr>
							<tr>
								<td height="24" align="right" nowrap bgcolor="#EDEFF6">首页文件名:</td>
								<td height="24">
									<input name="indexFileName" type="text" id="indexFileName" class="cms_text"  maxlength="20"><span style="color: #FF0000">*</span>
								</td>
							</tr>
							<tr>
								<td width="100" height="24" align="right" nowrap bgcolor="#EDEFF6"> 站点的顺序: </td>
								<td height="24" align="left">
									<input type="text" name="order" id="order" class="cms_text" style="width:30px" value="0" maxlength="9">
								</td>
							</tr>
							<tr>
								<td colspan="2">
									
								</td>
							</tr>
					  </table>
					</tab:tabPane>
					<tab:tabPane id="pub-site-path" tabTitle="发布路径设置">
					<fieldset>
						<LEGEND align=left><FONT class="cms_title_blue">本地路径设置</FONT></LEGEND>
						<table align=left>
						<tr>
							<td width="100" height="24" align="right" nowrap> 本地发布路径: </td>
							<td height="24">
								<input type="text" name="localpublishpath" id="localpublishpath" class="cms_text" value=""  maxlength="45" style="width:300px" />
							</td>
						</tr>
						</table>
					
					</fieldset>
					<fieldset>
						<LEGEND align=left><FONT class="cms_title_blue">远程FTP设置</FONT></LEGEND>
						<table align=left>
						<tr>
								<td width="100" height="24" align="right" nowrap>ftp站点IP:</td>
								<td height="24" align="left">
									<input type="text" name="ftpIp" id="ftpIp" style="width:130px" class="cms_text"  maxlength="15">
								</td>
						  </tr>
							<tr>
								<td width="100" height="24" align="right" nowrap>ftp站点端口:</td>
								<td height="24" align="left">
									<input type="text" name="ftpPort" id="ftpPort" class="cms_text" value="21" maxlength="4" style="width:40px">(默认21)
								</td>
							</tr>
							<tr>
								<td width="100" height="24" align="right" nowrap> ftp站点用户: </td>
								<td height="24" align="left">
									<input type="text" name="ftpUser" id="ftpUser" class="cms_text" style="width:130px"  maxlength="20"/>
								</td>
							</tr>
							<tr>
								<td width="100" height="24" align="right" nowrap>ftp站点密码:</td>
								<td height="24" align="left">
									<input type="password" name="ftpPassword" id="ftpPassword" class="cms_text" style="width:130px"  maxlength="20"/>
								</td>
							</tr>
							<tr>
								<td width="100" height="24" align="right" nowrap> ftp站点路径: </td>
							  <td height="24" align="left">
									<input type="text" name="ftpFolder" id="ftpFolder" class="cms_text" style="width:130px"  maxlength="20"> <input name="ftpTestButton"  type="button" onClick="testFtp()" value="FTP连接测试" class="Channel_7wordsBtn">						</td>
							</tr>
						</table>
					</fieldset>
					<table>
						<tr>
							<td width="105" height="24" align="right" nowrap bgcolor="#EDEFF6"> 站点访问http: </td>
							<td height="24">
								<input type="text" name="webHttp" id="webHttp" class="cms_text" value="http://"  maxlength="45" style="width:300px" />
							</td>
						</tr>
					</table>
					
					</tab:tabPane>
					<tab:tabPane id="site-site-param" tabTitle="发布参数设置">
					<fieldset>
						<LEGEND align=left><FONT class="cms_title_blue">发布地点设置</FONT></LEGEND>
						<table align=left>
						<tr>
							<td width="100" height="24" align="right" nowrap> 发布地点: </td>
							<td height="24">
								<input type="hidden" name="publishDestination" value="" />
								<input hideFocus type="checkbox" name="local" value="0" checked/>本地
								<input hideFocus type="checkbox" name="remote" value="0"/>远程
							</td>
						</tr>
						</table>
					</fieldset>
					<fieldset>
						<LEGEND align=left><FONT class="cms_title_blue">分发方式设置</FONT></LEGEND>
						<table align=left>
						<tr>
							<td width="100" height="24" align="right" nowrap> 分发方式: </td>
							<td height="24">
								<input type="hidden" name="distributeManners" value="0," />
								<input hideFocus type="checkbox" name="HTML_DISTRIBUTE" value="0" checked/>HTML
								<input hideFocus type="checkbox" name="MAIL_DISTRIBUTE" value="2"/>邮件类型
								<input hideFocus type="checkbox" name="RSS_DISTRIBUTE" value="1"/>RSS
							</td>
						</tr>
						</table>
					</fieldset>
					</tab:tabPane>                    
    <%
    //---------------------------------------------------------------------------------------------------------------
        String parentPath = "cms::menu://sysmenu$root/appManager$module";
        SiteManagerImpl simpl = new SiteManagerImpl();
        List items = simpl.getSubItems(parentPath);    
    %>
                    <!-- 
                        add by ge.tao
                        2007-08-20
                           新增站点时设置站点的应用
                    -->
                    <tab:tabPane id="bar-site-add" tabTitle="站点应用设置">
                    <fieldset>
                        <LEGEND align=left><FONT class="cms_title_blue">已有应用</FONT></LEGEND>
                        
						<table width="100%" border="0" cellpadding="5" cellspacing="0" class="Datalisttable">
						<tr class="cms_report_tr">
						    <td><input type="checkbox" id="appId" value="null$null" class="checkbox" onclick="selectall()"></td>
						    <td>应用ID</td>
						    <td>应用名称</td>
						</tr>
					<%
						
						for(int i=0;i<items.size();i++){
							String flag = "";
							String itemId = "";
							String itemName = "";
							String itemPath = "";
							int dolarcount = 1;
							String type = "";
						    if(items.get(i)  instanceof  Item){
								Item item = (Item)items.get(i);
								
								itemId = item.getId();
								itemName = item.getName();
								itemPath = item.getPath();
								type = "item";
								dolarcount = CMSUtil.count(itemPath,'$') - 2;
								
							}else if(items.get(i) instanceof Module){
							    Module module = (Module)items.get(i);
								itemId = module.getId();
								itemName = module.getName();
								itemPath = module.getPath();
								type = "module";
								dolarcount = CMSUtil.count(itemPath,'$') - 2;
							}
							
							
							
				        %>
						<tr>
						    <td><input type="checkbox" id="appId" name="appId" value="<%=itemPath%>^<%=itemId%>"></td>
						    
							<td style="text-align:left"><%
							for(int j = 0; j < dolarcount; j ++)
							{
								
								out.println( "<img src='space.gif'/>");
							}
							out.println("<img src='" + type +".gif'/>");
							out.println(itemName);
							%></td>
							<td><%=itemId%></td>
						</tr>    
						<%
				        }
						%>              
						</table>
        
                    </fieldset>
                    
                    </tab:tabPane>
                    <%
                    	//lucene全文检索的整站索引开关
                    	if(CMSUtil.enableIndex()){
                    		if(CMSUtil.enableIndexType(CMSUtil.INDEX_ENGINE_TYPE_LUCENE)){
                    %>
                    <tab:tabPane id="bar-site" tabTitle="整站索引管理">
                   
                    	<fieldset>
                    	<LEGEND align=left><FONT class="cms_title_blue">索引信息设置</FONT></LEGEND>
                    	<table>
                    		<tr>
								<td width="100" height="24" align="right" nowrap>索引频率:</td>
								<td>
									<%
										List listL = null;
										listL = searchManager.getLDTList(4);
									%>
									<select name="index_level" onChange="changeclass()" class="cms_select">
										<%= JSPFunc.getOption(listL,"0") %> 
									</select>
								</td>
							</tr>
							<tr>
								<td width="100" height="24" align="right" nowrap>索引日期:</td>
								<td>
									<%
										List listD = null;
										listD = searchManager.getLDTList(0);
									%>
									<select name="index_day" class="cms_select">
										<%= JSPFunc.getOption(listD,"0") %>
									</select>
								</td>
							</tr>
                    		<tr>
								<td width="100" height="24" align="right" nowrap>开始时间:</td>
								<td>
									<%
										List listT = null;
										listT = searchManager.getLDTList(3);
									%>
									<select name="index_time" class="cms_select">
										<%= JSPFunc.getOption(listT,"0") %>
									</select>
								</td>
							</tr>                  		
						</table>
						 </fieldset>
					</tab:tabPane>
					<%
						}}
                    	//lucene全文检索的整站索引开关
                    %>
				</tab:tabContainer>
				
			</td>
		</tr>
  </table>
	<br>
	<div style="text-align:center">
		<INPUT name="button" type="button" class="cms_button" onClick="checkForm()"  value="保存">
		&nbsp;&nbsp;&nbsp;
		<INPUT name="button" type="button" class="cms_button" onClick="window.close()"  value="关闭">
	</div>
</form>
<iframe name="exeman" width="0" height="0" style="display:none"></iframe>
</body>
<SCRIPT LANGUAGE="JavaScript">
	<!--
	var checkflag = false;    
    //全选中复选框
	function selectall(){
		var o = document.getElementsByName("appId");
		if(checkflag==false){
			for (var i=0; i<o.length; i++){
				if(!o[i].disabled){
						o[i].checked=true;
				}
			}
			checkflag = true;
		}else{
			for (var i=0; i<o.length; i++){
				if(!o[i].disabled){
						o[i].checked=false;
				}
			}
			checkflag = false;
		}
	}
	//单个选中复选框
	function checkOne(id){
		var o = document.getElementsByName("appId");
		for (var i=0;i<o.length;i++){
			if(!o[i].disabled){
				if (o[i].checked==false){
					cbs=false;
				}
			}
		}
	}
</script>
</html>