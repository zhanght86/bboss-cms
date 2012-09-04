<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ include file="/sysmanager/include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%
	String docidStr = request.getParameter("docidStr");
	String srcchannelid = request.getParameter("channelid");
	String newDoc = request.getParameter("newDoc");
	String docsiteid = request.getParameter("docsiteid");
	
%>
<html>
	<head>
		<title>文档转发：：</title>
		<script language="JavaScript" src="changeView.js" type="text/javascript"></script>
  		<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/treeview.css">
  		<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/windows.css">
  		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
  		<script language="javascript">
  			function sub(flag){
  				var allChannelInfos = parent.chn_slt_top.document.getElementsByName("channelInfo");
  				if(allChannelInfos == null){
  					alert("暂时没有频道可供选择！");
  					return;
  				}
  				var oneChannelInfo;
  				for(var i=0;i<allChannelInfos.length;i++){
  					if(allChannelInfos[i].checked == true){
  						oneChannelInfo = allChannelInfos[i].value; 
  					}
  				}
  				if(oneChannelInfo == null){
  					alert("请选择除当前频道外的另外一个频道！");
  					return;
  				}
  				var ChannelInfoArray = oneChannelInfo.split(":");
  				var channelid = ChannelInfoArray[ChannelInfoArray.length-1];
  				var siteid = ChannelInfoArray[ChannelInfoArray.length-2];  
  				if(channelid == <%=srcchannelid%>){
  					alert("不能选择当前频道！");
  				return;
  				}
  				var to;
  				if(flag == 1)
  					to = "moveHandle.jsp?siteId=<%=docsiteid%>&docidStr=<%=docidStr%>&channelid=" + channelid;
  				else if(flag == 2)
  					to = "copyHandle.jsp?newDoc=<%=newDoc%>&docidStr=<%=docidStr%>&channelid=" + channelid;
  				else if(flag == 3)
  					to = "citeHandle.jsp?docidStr=<%=docidStr%>&docsiteid=<%=docsiteid%>&channelid=" + channelid + "&dstSiteId=" + siteid;
  				if(confirm("确定进行操作?"))
  				{
  					transmitForm.action = to;
  					transmitForm.target = "transmitFrame";
  					transmitForm.submit();
  				}
				else return;
  			}
  		</script>
	</head>
	<body class="contentbodymargin" scrolling="no">
		<form name="transmitForm" method="post">
			<table class="table" width="80%">
				<tr class="tr">
					<td align="center">
						<input type="button" value="移动" class="cms_button" onClick="sub('1')">
						<input type="button" value="复制" class="cms_button" onClick="sub('2')">
						<input type="button" value="引用" class="cms_button" onClick="sub('3')">
						<input type="button" value="返回" class="cms_button" onClick="window.close()">
					</td>
				</tr>
			</table>
		</form>
	</body>
	<div height=0 width=0 style="display:none">
		<iframe name="transmitFrame"> </iframe>
	</div>
</html>