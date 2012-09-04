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
  		<script src="<%=request.getContextPath()%>/cms/inc/js/func.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/include/pager.js" type="text/javascript"></script>
  		<script language="javascript">
  		    //0: 站点ID
  		    //1: 频道ID
  		    //参数形式: ids: 200:150, type: 0
  		    function getId(ids,type){
  		        var arrid = new Array();
  		        arrid = ids.split(":");
  		        if(type==1) return arrid[0];
  		        else return arrid[1];
  		    }
  		    
  			function sub(flag){
  				var allChannelInfos = parent.chn_slt_top.document.getElementsByName("channelInfo");
  				if(allChannelInfos == null){
  					alert("暂时没有频道可供选择！");
  					return;
  				}
  				var channelInfos = "";
  				var selected = false;
  				for(var i=0;i<allChannelInfos.length;i++){
  					if(allChannelInfos[i].checked == true){  					    
  					    if(getId(allChannelInfos[i].value,0)==<%=srcchannelid%>){
  					        alert("不能选择当前频道！");
  					        break;
  					    }
  					    selected = true;
  					    if(channelInfos == "") channelInfos = channelInfos + allChannelInfos[i].value; 
  					    else channelInfos = channelInfos + ","  + allChannelInfos[i].value; 
  					}
  				}  				
  				if(channelInfos == null){
  					alert("请选择除当前频道外的另外一个频道！");
  					return;
  				}
  				if(channelInfos==""){
  				    alert("请至少选择一个频道！");
  					return;
  				}
  				//复制和移动 选择文档状态
  				var statutype;
  				if(flag == 1 || flag == 2){
  				    statutype = openWin("select_doc_type.jsp",400,400);  
  				    if(statutype==""){
  				        alert("请选择转发后文档的状态！");
  					    return;
  				    }				
  				}
  				
  				var siteid = "<%=docsiteid%>";  
  				
  				
  				var to;
  				if(flag == 1)
  					to = "moveHandle.jsp?siteId=<%=docsiteid%>&docidStr=<%=docidStr%>&channelid=" + channelInfos+"&statutype="+statutype;
  				else if(flag == 2)
  					to = "copyHandle.jsp?newDoc=<%=newDoc%>&docidStr=<%=docidStr%>&channelid=" + channelInfos+"&statutype="+statutype;
  				else if(flag == 3)//引用
  					to = "mutiCiteHandle.jsp?docidStr=<%=docidStr%>&docsiteid=<%=docsiteid%>&channelid=" + channelInfos;
  				if(confirm("确定进行操作?"))
  				{
  				    
  					transmitForm.action = to;
  					transmitForm.target = "transmitFrame";
  					transmitForm.submit();
  				}
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