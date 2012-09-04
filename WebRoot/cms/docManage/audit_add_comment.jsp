<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.frameworkset.platform.cms.util.CMSUtil,com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor"%>
<%	
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String auditerName = accesscontroler.getUserName();
	ChannelManager cm = new ChannelManagerImpl();
	DocumentManager dm = new DocumentManagerImpl();
    
    //外部注入发布管理
    String uuid = CMSUtil.getUUID();
    PublishMonitor monitor = PublishMonitor.createPublishMonitor();
    session.setAttribute(uuid,monitor);
	
	String idStr = request.getParameter("idStr");
	String auditFlag = request.getParameter("auditFlag");
	String comment = "";
	if(auditFlag.equals("1")){
		comment = "同意";
	}else if(auditFlag.equals("0")){ 
		comment = "不同意";
	}
	//将字符串数组转换为整形数组
	int intTaskid;
	int intDocid;
	int intDocChnlid;
	String[] idstr = idStr.split(":");
	intDocid = Integer.parseInt(idstr[1].trim());
	intDocChnlid = Integer.parseInt(idstr[2].trim());
	//频道列表出来的参数中没有审核任务的id
	if(!idstr[0].trim().equals(""))
		intTaskid = Integer.parseInt(idstr[0].trim());
	else intTaskid = dm.hasTask(Integer.parseInt(accesscontroler.getUserID()),intDocid,1);
	int docType = dm.getDocType(intDocid);
	Date sysDate = new Date();
	SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	String date = format.format(sysDate);
	int channelId = dm.getDocChnlId(intDocid);
	int siteid = dm.getDocSiteId(intDocid);
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<title>增加审核意见</title>
	<script src="../inc/js/func.js"></script>
	<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
	<style type="text/css">
		.STYLE1 {color: blue}
	</style>
	<script language="javascript">
		function sub(){
			window.returnValue = "cf";
			form1.action="<%=rootpath%>/cms/docManage/auditDocHandle.jsp?auditFlag=<%=auditFlag%>&idStr=<%=idStr%>";
			form1.target="addCommentIframe";
			form1.submit();
		}
		function deliver1(){
			window.returnValue = "cf";
			var reValue;
			reValue = showModalDialog("doc_AuditorList.jsp?flag=3&docid=<%=intDocid%>&taskid=<%=intTaskid%>&channelId=<%=intDocChnlid%>",window,"dialogWidth:400px;dialogHeight:500px;help:no;scroll:auto;status:no");
			if(reValue == "cf"){
				window.close();	
				var str = window.dialogArguments.location.href;
				var end = str.indexOf("?");
				var strArray;
				if(end != -1)
					strArray= str.slice(0,end);
				else
					strArray = str;
				window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
			}
		}
		function subPublish1(){
			window.returnValue = "cf";
			var reValue;
			reValue = showModalDialog("doc_PublisherList.jsp?flag=2&docid=<%=intDocid%>&taskid=<%=intTaskid%>&channelId=<%=intDocChnlid%>",window,"dialogWidth:400px;dialogHeight:500px;help:no;scroll:auto;status:no");
			if(reValue == "cf"){
				window.close();	
				var str = window.dialogArguments.location.href;
				var end = str.indexOf("?");
				var strArray;
				if(end != -1)
					strArray= str.slice(0,end);
				else
					strArray = str;
				window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
			}
		}
        var timer;
        var winOpen;
		function publish1(){
			form1.action="<%=rootpath%>/cms/docManage/publishDocHandle.jsp?flag=2&docid=<%=intDocid%>&taskid=<%=intTaskid%>";
			form1.target="addCommentIframe";
            var path = "doc_publish_info.jsp?uuid=<%=uuid%>&refresh=false";
            var featrue = "dialogHeight:310px;dialogWidth:450px";
            winOpen = window.showModelessDialog(path,window,featrue);
            window.setInterval("listenClosed()",500)            
			//form1.submit();
			//document.all.divProcessing.style.display="";
			window.returnValue = "cf";
            
		}
        function closeSubWindow(){ 
            winOpen.close();
            document.all("cleanSession").src = "../cleanSession.jsp?uuid=<%=uuid%>";
       
        }
        
        function listenClosed(){
            if(winOpen.closed==true){
                parentPageUrl();
                window.clearInterval(timer);
                
            }
        }
        
        //父页面 刷新
        function parentPageUrl(){
            window.close(); 
            var str = window.dialogArguments.location.href;
            var end = str.indexOf("?");
            var strArray;
            if(end != -1)
                strArray= str.slice(0,end);
            else
                strArray = str;
            window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
        }
        //调用发布主页面的函数
	    function lastRefreshSubPage(){        
	        winOpen.updateMsg();
	    }
	    //调用发布主页面的函数
	    //url 为空的时候 直接alert(msg)
	    //url 不为空的时候 confirm()选择msg
	    function alertFun(msg,url){
	        winOpen.alertMsg(msg,url);
	    }
	</script>
</head>
<body scroll=no leftmargin=0>
	<div id="divProcessing" style="position:absolute;width:40%;height:20px;z-index:1;
			background-color:#F0f0f0;layer-background-color:#F0f0f0;left:22%;top:80%;display:none;"  class="font">   
	    <marquee direction="left" width="250" behavior="alternate"><span class=STYLE1>正在发布中，请稍等……</span></marquee>
    </div>
	<form action="" method="post" name="form1">
        <input type="hidden" name="uuid" value="<%=uuid%>">
        <input type="hidden" name="isRecordValue">
        <input type="hidden" name="recursionPublish">
        <input type="hidden" name="clearCache">
        <input type="hidden" name="url">
		<table width="100%" height="100%" border="0" align="center" bgcolor="#FFFFFF">	  
		  <tr>
			<td align="left" height="20">
				时间：<input value=<%=date%> disabled type="text" name="time">
			</td>
		  </tr>
		  <tr>
			<td align="left" height="20">
				请增加<%=comment%>的审核意见：
			</td>
		  </tr>
		  <tr>
			<td align="left" height="150">		
				<textarea name="auditComment" cols="60" rows="25"><%=comment%></textarea>
			</td>
		  </tr>
		  <tr>
			<td align="left" height="20">
				审核人：<input value=<%=auditerName%> disabled type="text" name="time">
			</td>
		  </tr>
		  <tr>
			  <td align="center" height="20">
			  		<%if(docType==0 || docType==1){
			  		  	if(auditFlag.equals("0")){%>
						<input name="submitForm" type="button" value="退回" class="cms_button" onClick="sub()" />
					<%}else if(auditFlag.equals("1")){%>
						<input name="subPublish" type="button" value="提交发布" class="cms_button" onClick="subPublish1()" />
						<input name="deliver" type="button" value="继续送审" class="cms_button" onClick="deliver1()" />
						<%if(accesscontroler.checkPermission(channelId+"",AccessControl.DOCPUBLISH_PERMISSION,AccessControl.CHANNELDOC_RESOURCE)
								|| accesscontroler.checkPermission(siteid+"",AccessControl.DOCPUBLISH_PERMISSION,AccessControl.SITEDOC_RESOURCE)){%>
						<input name="publish" type="button" value="发布" class="cms_button" onClick="publish1()" />
						<%}%>				
					<%}
					}else{
						if(auditFlag.equals("0")){%>
						<input name="submitForm" type="button" value="退回" class="cms_button" onClick="sub()" />
					<%}else if(auditFlag.equals("1")){
					%>
						<input name="publish" type="button" value="通过" class="cms_button" onClick="sub()"/>
						<input name="deliver" type="button" value="继续送审" class="cms_button" onClick="deliver1()" />
					<%	}
					}%>
					<input name="cancelForm" type="button" value="返回" class="cms_button" onClick="window.close()"/>
			  </td>
		  </tr>
		  <tr>
		  </tr>
		</table>
	</form>
	<div height=0 width=0 style="display:none">
		<iframe name="addCommentIframe"></iframe>
	</div>
    <iframe name="cleanSession" id="cleanSession"  width="0" height="0"  style="display:none" ></iframe>
</body>
</html>