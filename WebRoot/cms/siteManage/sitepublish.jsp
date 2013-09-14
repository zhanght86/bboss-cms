<%
/**
  *	选择发布参数
  * 
  */
%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.cms.sitemanager.*" %>
<%@page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@page import="com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor,com.frameworkset.platform.cms.util.CMSUtil"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
    
    
	
	String siteId = request.getParameter("siteId");
	String channelId = request.getParameter("channelId");
	
	String title = "";
	
	SiteManager sm = new SiteManagerImpl();
	Site site = sm.getSiteInfo(siteId);
	
	ChannelManager cm = new ChannelManagerImpl();
	Channel channel = null;
	if(channelId==null||"".equals(channelId))
	{
		title = "【" + site.getName() + "】站点";
	}
	else
	{
		channel = cm.getChannelInfo(channelId);
		title = "【" + channel.getDisplayName() + "】频道";
	}
    
    String uuid = CMSUtil.getUUID();
    PublishMonitor monitor = PublishMonitor.createPublishMonitor();
    session.setAttribute(uuid,monitor);

	String local2ndRemote = String.valueOf(site.getPublishDestination());
	String distributeManners = site.getDistributeManners();
	int indexTemplateId = site.getIndexTemplateId();//首页模板id, 0表示无意义
	String indexFileName = site.getIndexFileName();//首页文件名
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="../inc/js/func.js"></script>
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
.STYLE1 {color: blue}
-->
</style>
<SCRIPT LANGUAGE="JavaScript">
	//
    var timer;
    var timer1;
	function subform()
	{
		//发布范围数组
		publishForm.publishScope.value = "";
		if(publishForm.channelId.value=="null")
		{
			if(publishForm.PUBLISH_SITE_INDEX.checked)
				publishForm.publishScope.value += "1" + ",";
		}
		if(publishForm.PUBLISH_CHANNEL_INDEX.checked)
		{
			publishForm.publishScope.value += "2" + ",";
			if(publishForm.channelId.value=="null"||publishForm.PUBLISH_CHANNEL_RECINDEX.checked)
				publishForm.publishScope.value += "7" + ",";
		}
		//if(publishForm.PUBLISH_CHANNEL_RECINDEX.checked)
			//publishForm.publishScope.value += "7" + ",";
		if(publishForm.PUBLISH_CHANNEL_CONTENT.checked)
		{
			publishForm.publishScope.value += "3" + ",";
			if(publishForm.channelId.value=="null"||publishForm.PUBLISH_CHANNEL_RECCONTENT.checked)
				publishForm.publishScope.value += "5" + ",";
		}
		//if(publishForm.PUBLISH_CHANNEL_RECCONTENT.checked)
			//publishForm.publishScope.value += "5" + ",";
		
		if(publishForm.publishScope.value=="")
		{
			alert("请选择发布内容！");
			return false;
		}

		//标识增量发布，还是完全发布
		publishForm.increament.value = "";
		if(publishForm.increamentall.checked)
			publishForm.increament.value = "false" ;
		if(publishForm.increamentno.checked)
			publishForm.increament.value = "true";

		var distributeManners = publishForm.distributeManners.value;
		if(distributeManners==null||distributeManners=="")
		{
			alert("error:没有设置好分发方式！\n请先在站点管理中设置好分发方式！");
			return false;
		}
		var local2ndRemote = publishForm.local2ndRemote.value;
		if(local2ndRemote==null||local2ndRemote=="")
		{
			alert("error:没有设置好发布目的地！\n请先在站点管理中设置好发布目的地！");
			return false;
		}
              
        var isRecordValue = "";
        if(document.all("isRecordMsg1").checked) 
            isRecordValue = document.all("isRecordMsg1").value;
        if(document.all("isRecordMsg2").checked)
            isRecordValue = document.all("isRecordMsg2").value;
        document.all("isRecordValue").value = isRecordValue;  
        if(isRecordValue == "true"){
            if(document.getElementById("waiting"))
			    //document.getElementById("waiting").style.display="";
            if(document.getElementById("infoMsg"))
                document.getElementById("infoMsg").style.display="";
            document.all('showinfo').value = "查看日志";
        }else{
            if(document.getElementById("waiting_marquee"))
                document.getElementById("waiting_marquee").style.display=""; 
            //document.all("info").src = "publish_info.jsp?uuid=<%=uuid%>";   
        }
        if(document.getElementById("waiting_marquee"))
            document.getElementById("waiting_marquee").style.display=""; 
        
            
		if(document.getElementById("publishButton"))
			document.getElementById("publishButton").disabled="disabled";
		if(parent.document.getElementById("closeButton"))
			document.getElementById("closeButton").disabled="disabled";   
		if(parent.document.getElementById("increamentall"))
			document.getElementById("increamentall").disabled="disabled";
		if(parent.document.getElementById("increamentno"))
			document.getElementById("increamentno").disabled="disabled";
		if(parent.document.getElementById("clearCacheYes"))
			document.getElementById("clearCacheYes").disabled="disabled";
		if(parent.document.getElementById("clearCacheNo"))
			document.getElementById("clearCacheNo").disabled="disabled";
        if(document.getElementById("isRecordMsg1"))
            document.getElementById("isRecordMsg1").disabled="disabled";
        if(document.getElementById("isRecordMsg2"))
            document.getElementById("isRecordMsg2").disabled="disabled";    
              
		if(parent.document.getElementById("PUBLISH_SITE_INDEX"))
			document.getElementById("PUBLISH_SITE_INDEX").disabled="disabled";
		if(parent.document.getElementById("PUBLISH_CHANNEL_INDEX"))
			document.getElementById("PUBLISH_CHANNEL_INDEX").disabled="disabled";
		if(parent.document.getElementById("PUBLISH_CHANNEL_RECINDEX"))
			document.getElementById("PUBLISH_CHANNEL_RECINDEX").disabled="disabled";  
		if(parent.document.getElementById("PUBLISH_CHANNEL_CONTENT"))
			document.getElementById("PUBLISH_CHANNEL_CONTENT").disabled="disabled";
		if(parent.document.getElementById("PUBLISH_CHANNEL_RECCONTENT"))
			document.getElementById("PUBLISH_CHANNEL_RECCONTENT").disabled="disabled"; 
		//add by ge.tao
        //date 2007-07-23
        if(isRecordValue == "true")
        {
        	updateMsg();
        }		
        publishForm.submit();
	}    
    function updateMsg(){
        document.all("info").src="publish_info.jsp?uuid=<%=uuid%>";
    }
    
    
	//
	function changeChlIndex()
	{
		if(publishForm.channelId.value=="null")
		{
			if(publishForm.PUBLISH_CHANNEL_INDEX.checked)
				publishForm.PUBLISH_CHANNEL_RECINDEX.checked = true;
			else
				publishForm.PUBLISH_CHANNEL_RECINDEX.checked = false;
		}
		else
		{
			publishForm.PUBLISH_CHANNEL_RECINDEX.checked = false;
		}
	}
	//
	function changeChlContent()
	{
		if(publishForm.channelId.value=="null")
		{
			if(publishForm.PUBLISH_CHANNEL_CONTENT.checked)
				publishForm.PUBLISH_CHANNEL_RECCONTENT.checked = true;
			else
				publishForm.PUBLISH_CHANNEL_RECCONTENT.checked = false;
		}
		else
		{
			publishForm.PUBLISH_CHANNEL_RECCONTENT.checked = false;
		}
	}
    function showMsg(){
        if(document.all("showinfo").value == "查看日志") 
            document.all("showinfo").value = "隐藏日志";
        else document.all("showinfo").value = "查看日志";
        
        if(document.getElementById("waiting")){
            if(document.getElementById("waiting").style.display==""){
                document.getElementById("waiting").style.display="none";
            }else{
                document.getElementById("waiting").style.display="";
            }
        }
    }
    window.onunload = function clrSession(){
        document.all("cleanSession").src = "../cleanSession.jsp?uuid=<%=uuid%>";            
    }
    window.onerror=function(){return true} 
	
</SCRIPT>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>发布<%=title%></title>
</head>

<body background="../images/grayline_bg.jpg">
<form action="sitepublish_do.jsp" method="post"  name="publishForm" target="publishexeman" id="publishForm">
	<input type="hidden" name="siteId" value="<%=siteId%>"/>
	<input type="hidden" name="channelId" value="<%=channelId%>"/>
    <input type="hidden" name="uuid" value="<%=uuid%>"/>
    <input type="hidden" name="isRecordValue" />
	<input type="hidden" name="publishScope" value="" /><!--发布范围数组-->
	<input type="hidden" name="local2ndRemote" value="<%=local2ndRemote%>" /><!--本地发布和远程发布标识boolean二维数组-->
	<input type="hidden" name="increament" value="" /><!--标识增量发布，还是完全发布-->
	<input type="hidden" name="distributeManners" value="<%=distributeManners%>" />
	<!--存放发布结果的分发方式数组，可以同时有多种方式，
	包括DistributeManager.HTML_DISTRIBUTE,DistributeManager.RSS_DISTRIBUTE,DistributeManager.MAIL_DISTRIBUTE-->
	<table width="98%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" align="center">
		<tr >
			<td width="4%" height="20" align="right">
				<img src="../images/ico_point.gif" width="25" height="25">
			</td>
			<td height="20" class="cms_title_blue">
				发布参数
			</td>
		</tr>
		<tr>
			<td colspan="2" style="height:9px; background-color:#3266B1"></td>
		</tr>
		<tr>
			<td colspan="2">
				<table width="100%" border="1" cellpadding="0" cellspacing="0" bordercolor="#f0f0f0">
					<tr>
						<td width="100" height="24" align="right" nowrap>发布内容:</td>
						<td height="24">
							<table width="100%" border="1" cellpadding="0" cellspacing="0" bordercolor="#f0f0f0">
								<%
								if(channelId==null||"".equals(channelId))
								{
								%>
								<tr>
									<input type="checkbox" id="PUBLISH_SITE_INDEX" name="PUBLISH_SITE_INDEX" value="1" checked hideFocus />发布站点首页
								</tr>
								<%
								}
								%>
								<tr>
									<input hideFocus type="checkbox" id="PUBLISH_CHANNEL_INDEX" name="PUBLISH_CHANNEL_INDEX" value="2" onclick="changeChlIndex()" 
									<%
									if(channelId!=null&&!"".equals(channelId))
									{
									%>
									checked
									<%
									}
									%>
									/>发布频道首页
									<input hideFocus type="checkbox" id="PUBLISH_CHANNEL_RECINDEX" name="PUBLISH_CHANNEL_RECINDEX" value="7" onclick="publishForm.PUBLISH_CHANNEL_INDEX.checked=true" 
									<%
									if(channelId==null||"".equals(channelId))
									{
									%>
									<%
									}
									%>
									/>递归发布所有子频道首页
								</tr>
								<tr>
									<input hideFocus type="checkbox" id="PUBLISH_CHANNEL_CONTENT" name="PUBLISH_CHANNEL_CONTENT" value="3" onclick="changeChlContent();"/>发布频道内容
									<input hideFocus type="checkbox" id="PUBLISH_CHANNEL_RECCONTENT" name="PUBLISH_CHANNEL_RECCONTENT" value="5" onclick="publishForm.PUBLISH_CHANNEL_CONTENT.checked=true"
									<%
									if(channelId==null||"".equals(channelId))
									{
									%>
									<%
									}
									%>
									/>递归发布所有子频道内容
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td width="100" height="24" align="right" nowrap>发布方式:</td>
						<td height="24">
							<input hideFocus type="checkbox" id="increamentno" name="increamentno" onclick="publishForm.increamentall.checked=false" value="true" checked/>增量发布
							<input hideFocus type="checkbox" id="increamentall" name="increamentall" value="false" onclick="publishForm.increamentno.checked=false" />完全发布
						</td>
					</tr>
                    <tr>
                        <td width="100" height="24" align="right" nowrap>是否记录日志:</td>
                        <td height="24">
                            <input hideFocus type="checkbox" id="isRecordMsg1" name="isRecordMsg1" value="false" onclick="publishForm.isRecordMsg2.checked=false"  checked/>不记录
                            <input hideFocus type="checkbox" id="isRecordMsg2" name="isRecordMsg2" value="true" onclick="publishForm.isRecordMsg1.checked=false"/>记录
                            <span id="infoMsg" style="display:none;">
                                <INPUT name="button" id="showinfo" type="button" class="cms_button" onClick="showMsg()" >
                                <INPUT  type="button" class="cms_button" value="刷新信息" onClick="updateMsg()" >
                            </span>
                        </td>
                    </tr>
                    <tr>
                    	<td width="100" height="24" align="right" nowrap>是否清除缓存:</td>
                    	<td height="24">
                    		<input hideFocus type="checkbox" id="clearCacheNo" name="clearCacheNo" onclick="publishForm.clearCacheYes.checked=false;publishForm.clearCache.value=false" value="false" checked/>不清除
							<input hideFocus type="checkbox" id="clearCacheYes" name="clearCacheYes" value="true" onclick="publishForm.clearCacheNo.checked=false;publishForm.clearCache.value=true" />清除
							<input id="clearCache" name="clearCache" type="hidden" value="false" />
						</td>
                    </tr>
					<!--<tr>
						<td width="100" height="24" align="right" nowrap>发布地点:</td>
						<td height="24">
							<input hideFocus type="checkbox" name="local" value="true" checked/>远程发布<input hideFocus type="checkbox" name="remote" value="false"/>本地发布
						</td>
					</tr>
					<tr>
						<td width="100" height="24" align="right" nowrap>分发方式:</td>
						<td height="24">
							<input hideFocus type="checkbox" name="HTML_DISTRIBUTE" value="0" checked/>HTML
							<input hideFocus type="checkbox" name="MAIL_DISTRIBUTE" value="2"/>邮件类型
							<input hideFocus type="checkbox" name="RSS_DISTRIBUTE" value="1"/>RSS
						 </td>
					</tr>-->
				</table>
			</td>            
		</tr>        
	</table>
	<br>
	<div style="text-align:center">
		<INPUT name="button" id="publishButton" type="button" class="cms_button" onClick="subform()"  value="发布">
		&nbsp;&nbsp;&nbsp;
		<INPUT name="button" id="closeButton" type="button" class="cms_button" onClick="window.close()"  value="关闭">
            
	</div>
	<div id="waiting_marquee" style="position:absolute;width:40%;height:20px;z-index:1;
			background-color:#F0f0f0;layer-background-color:#F0f0f0;left:22%;top:92%;display:none;"  class="font">   
	    <marquee direction="left" width="250" behavior="alternate"><span class=STYLE1>正在发布中，请稍等……</span></marquee>
    </div>
   
    <iframe name="info" src="" width="0" height="0"  style="display:none" ></iframe>
    <div id="waiting" style="text-align:center;display:none">    
        <span>
            <textarea name="publish_info" style="width:400" rows="8"></textarea>
        </span>        
    </div>
    <div id="linkInfo"  style="text-align:center">
    </div>
           
    
</form>
<iframe name="publishexeman" width="0" height="0" style="display:none"></iframe>
<iframe name="cleanSession" width="0" height="0" style="display:none"></iframe>
</body>

</html>