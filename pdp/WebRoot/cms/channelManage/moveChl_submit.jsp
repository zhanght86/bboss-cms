<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ include file="/sysmanager/include/global1.jsp"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
%>
<html>
	<head>
		<title>文档转发：：</title>
		<script language="JavaScript" src="changeView.js" type="text/javascript"></script>
  		<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/treeview.css">
  		<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/windows.css">
  		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
  		<script language="javascript">
  			function sub(){
  				var fromchl = parent.from_chl.document.getElementsByName("channelInfo");
				var tochl = parent.to_chl.document.getElementsByName("channelInfo");
  				if(fromchl == null){
  					alert("暂时没有频道可供选择！");
  					return;
  				}
  				var fromid,toid;
  				for(var i=0;i<fromchl.length;i++){
  					if(fromchl[i].checked == true){
  						fromid = fromchl[i].value; 
  					}
  				}
				for(var i=0;i<tochl.length;i++){
  					if(tochl[i].checked == true){
  						toid = tochl[i].value; 
  					}
  				}
  				if(fromid == null){
  					alert("请选择源频道！");
  					return;
  				}
				if(toid == null){
  					alert("请选择目的频道！");
  					return;
  				}
  				var fromids = fromid.split(":");
				var toids = toid.split(":");
  				var fid = fromids[fromids.length-1]; 
				var tid = toids[toids.length-1];
  				if(fid == tid){
  					alert("源频道和目的频道不能为同一频道！");
  					return;
  				}
  				transmitForm.action = "moveChl_action.jsp?fid=" + fid + "&tid=" + tid;
  				transmitForm.target = "movechl";
  				transmitForm.submit();
  			}
			function sub2(){
  				var fromchl = parent.from_chl.document.getElementsByName("channelInfo");
				if(fromchl == null){
  					alert("暂时没有频道可供选择！");
  					return;
  				}
  				var fromid;
  				for(var i=0;i<fromchl.length;i++){
  					if(fromchl[i].checked == true){
  						fromid = fromchl[i].value; 
  					}
  				}
				if(fromid == null){
  					alert("请选择源频道！");
  					return;
  				}
				
  				var fromids = fromid.split(":");
				var fid = fromids[fromids.length-1]; 
				if(fromids.length == 2){
  					alert("源频道已经是一级频道！");
  					return;
  				}
  				transmitForm.action = "moveChl_action.jsp?fid=" + fid;
  				transmitForm.target = "movechl";
  				transmitForm.submit();
  			}
  		</script>
	</head>
	<body class="contentbodymargin" scrolling="no">
		<form name="transmitForm" method="post">
			<table class="table" width="100%">
				<tr class="tr">
					<td align="center">
						<input type="button" value="移动" class="cms_button" onClick="sub()">
						<input type="button" value="移为一级频道" class="cms_button" style="width:90px;" onClick="sub2()">
						<input type="button" value="返回" class="cms_button" onClick="window.close()">
					</td>
				</tr>
			</table>
		</form>
	</body>
	<div height=0 width=0 style="display:none">
		<iframe name="movechl"> </iframe>
	</div>
</html>