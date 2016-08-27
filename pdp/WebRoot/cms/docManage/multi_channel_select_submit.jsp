<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ include file="/sysmanager/include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	String taskType = request.getParameter("taskType");
	String channelid = request.getParameter("channelid");  //只有频道引用处理时才传来此参数
	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	com.frameworkset.platform.cms.CMSManager cmsManager  = new com.frameworkset.platform.cms.CMSManager();
 	cmsManager.init(request,session,response,accesscontroler);
 	String currentSiteid = cmsManager.getSiteID();
%>
<html>
	<head>
		<title>:::多频道选择::::::</title>
		<script language="JavaScript" src="changeView.js" type="text/javascript"></script>
  		<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/treeview.css">
  		<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/windows.css">
  		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
  		<script language="javascript">
  			function sub(taskType){
  				var flag = true;
	  			var allChannelInfos = parent.multi_chn_slt_top.document.getElementsByName("channelInfo");
	  				
	  			if(allChannelInfos == null){
	  				alert("暂时没有频道可供选择!");
	  				return;
	  			}
	  			var selChannelNames = "";
	  			var selChannelids = "";
	  			var selchnlSiteIds = "";
	  			for(var i=0;i<allChannelInfos.length;i++){
	  				var oneChnlInfo;
	  				if(allChannelInfos[i].checked == true){
	  					oneChnlInfoArray = allChannelInfos[i].value.split(":");
	  					selChannelNames = selChannelNames + oneChnlInfoArray[oneChnlInfoArray.length-2] + ";";
	  					selChannelids = selChannelids + oneChnlInfoArray[oneChnlInfoArray.length-1] + ",";
	  					selchnlSiteIds = selchnlSiteIds + oneChnlInfoArray[oneChnlInfoArray.length-3] + ",";
	  					if(oneChnlInfoArray[oneChnlInfoArray.length-1] == <%=channelid%>)
	  						flag = false;                    //引用频道时不能选择当前频道
	  				}
	  			}
	  			if(taskType!="cite"){			//任务文档查询时的频道选择
	  				window.returnValue = selChannelNames + ":" + selChannelids + ":" + selchnlSiteIds;
	  				window.close();
	  			}else{						//选择引用频道，并处理
	  				if(flag){
		  				if(selChannelids == "")
						{
							alert("请选择频道！");
							return false;
						}
						var urls = "<%=rootpath%>/cms/docManage/citeHandle.jsp?flag=3&docsiteid=" + selchnlSiteIds + "&channelid=<%=channelid%>&docidStr=" + selChannelids + "&dstSiteId=<%=currentSiteid%>";
						window.urlstr = urls;
						showModalDialog("../doing_iframe.html",window,"dialogWidth:"+(screen.availWidth-200)+"px;dialogHeight:"+(screen.availHeight-150)+"px;help:no;scroll:auto;status:no");
					}else{
						alert("对不起，不能选择当前频道！");
					}			
	  			}	  			
  			}
  		</script>
	</head>
	<body class="contentbodymargin" scrolling="no">
		<form name="multiqueryForm" method="post">
			<table class="table" width="80%">
				<tr class="tr">
					<td align="center">
						<input type="button" value="确定" class="cms_button" onClick="sub('<%=taskType%>')">
						<input type="button" value="返回" class="cms_button" onClick="window.close()">
					</td>
				</tr>
			</table>
		</form>
	</body>
	<div height=0 width=0 style="display:none">
		<iframe name="multiquery"> </iframe>
	</div>
</html>