<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	String flag = request.getParameter("flag");
	String siteid = request.getParameter("siteid");
	String channelid = request.getParameter("channelid");
	//ChannelManager cdm = new ChannelManagerImpl();
	//String chnlName = cdm.getChannelInfo(channelid).getName();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/windows.css">
<title>::::::文档选择::::::::::::::::::::::.</title>
<style type="text/css">
	.STYLE1 {color: #0000FF}
	.STYLE2 {color: #000099}
	.operStyle{
	width:17;
	height:16;
	}
.style7 {font-size: 5px}
</style>
<SCRIPT LANGUAGE="JavaScript">
	function getSubtitle(){
		var subtitle;    //频道名加文档名
		var subtitles = window.parent.chn_doc_top.chn_doc_list.document.getElementsByName("subtitle");
		var channelNames = window.parent.chn_doc_top.chn_doc_list.document.getElementsByName("channelName");
		var o = window.parent.chn_doc_top.chn_doc_list.document.getElementsByName("ID");
		for (var i=0; i<o.length; i++){
				if((!o[i].disabled)&&o[i].checked){
					subtitle = channelNames[i].value + ":" + subtitles[i].value;
				}
			}
		return subtitle;
	}
	//获取选择的文档数组
	function getSelectedDocs(){
		var arr = new Array();
		var obj = new Array();
		//var p = window.parent.chn_doc_top.chn_doc_list.getArr();
		
		
		//var flag = window.parent.chn_doc_top.chn_doc_list.document.getElementsByName("checkBoxAll");
		//if(flag[0].checked){
		//	arr = p;
		//}else{
			var o = window.parent.chn_doc_top.chn_doc_list.document.getElementsByName("ID");
			var docid = window.parent.chn_doc_top.chn_doc_list.document.getElementsByName("docid");
			var channelId = window.parent.chn_doc_top.chn_doc_list.channelId;
			var docSiteId = window.parent.chn_doc_top.chn_doc_list.docSiteId;
			var docListTable = window.parent.chn_doc_top.chn_doc_list.document.all.docListTable;
			var tablerows = docListTable.rows;
			var j=0;//用于初始化arr
			for (var i=0; i<o.length; i++){
				if((!o[i].disabled)&&o[i].checked){
					var tempobj = new Array();
					tempobj[0] = docid[i].value;
					tempobj[1] = channelId;
					tempobj[2] = (tablerows[i+1].cells)[2].innerText;
					tempobj[3] = docSiteId;
					arr[j] = tempobj;
					j++;
				}
			}
		//}
		return 	arr;
	}
	//将选择的所有的文档id拼成字符串 
	function getSelDocStr(){
		var arr = getSelectedDocs();
		var docidStr = "";
		for(var i=0;i<arr.length;i++){
			docidStr = docidStr + arr[i][0] + ":";
		}
		return docidStr;
	}
	function subform(){

		var flag = <%=flag%>;
		var arr = getSelectedDocs();
		if(flag == 1||flag == 11){					//转向相关文档列表
			var docids = window.dialogArguments.document.getElementsByName("docid");
			var tableatt = window.dialogArguments.document.all("chndoclist");
			var selfdocid = "";
			if(window.dialogArguments.parent.document.all("docid")!=null)
			{
				selfdocid = window.dialogArguments.parent.document.all("docid").value;
			}
			if(docids!=null){
				for(var a=0;a<arr.length;a++)
				{
					var temp = arr[a];
					if(temp[0]==selfdocid)
					{
						alert("不能引用自己！");
						return false;
					}
					for(var b=0;b<docids.length;b++)
					{
						if(temp[0]==docids[b].value)
						{
							alert("存在重复引用，请重新选择！");
							return false;
						}
					}
				}
			}
			
			for(var i=0;i<arr.length;i++)
			{
				obj = arr[i];		
				var row1 = tableatt.insertRow(i+1);
				var td1 = window.dialogArguments.document.createElement("td");
				var td2 = window.dialogArguments.document.createElement("td");
				var td3 = window.dialogArguments.document.createElement("td");
				
				td1.innerHTML="<input type='checkBox' name='id' value="+(i+1)+" hideFocus=true onclick='checkOne(id)'/>";		 
				var docid = "<input type='hidden' name='docid' value='"+obj[0]+"' />";
				var channelId = "<input type='hidden' name='channelId' value='"+obj[1]+"' />";
				td2.innerHTML = docid + channelId + obj[2];
				td3.innerHTML = window.parent.chn_doc_top.chn_doc_list.document.forms[0].chlName.value;
				row1.appendChild(td1);
				row1.appendChild(td2);
				row1.appendChild(td3);
				if(flag == 11)
				{
					//var td3 = window.dialogArguments.document.createElement("td");
					var td4 = window.dialogArguments.document.createElement("td");
					var td5 = window.dialogArguments.document.createElement("td");
					//td3.innerHTML = window.parent.chn_doc_top.chn_doc_list.document.forms[0].chlName.value;
					td4.innerHTML = "<input type='text' name=aggrtitle value="+obj[2]+" />";
					var up = "<a href='#' onclick='upRow(this)'><u>上移</u></a>&nbsp;";
					var down = "<a href='#' onclick='downRow(this)'><u>下移</u></a>";
					td5.innerHTML = up + down;
					//row1.appendChild(td3);
					row1.appendChild(td4);
					row1.appendChild(td5);
				}
			}
			window.close();
		}
		else if(flag == 2){				//转向复制处理
			var channelid = <%=channelid%>;
			
			if(arr.length == 0){
				alert("至少选择一个文档");
				return;
			}
			if(arr[0][1] == channelid){
				alert("不能从当前频道复制文档");
				return;
			}
			docidStr = getSelDocStr();
			myform.action = "copyHandle.jsp?channelid=<%=channelid%>&docidStr=" + docidStr;
			myform.target = "handleFrame" ;
			myform.submit();		
		}
		else if(flag == 3){				//转向引用处理
			var channelid = <%=channelid%>;
			if(arr.length == 0){
				alert("至少选择一个文档");
				return;
			}
			if(arr[0][1] == channelid){
				alert("不能从当前频道引用文档");
				return;
			}
			docidStr = getSelDocStr();
			//flag为1表示从其他频道引用文档过来，为2表示向其他频道推送引用文档
			var urls = "<%=rootpath%>/cms/docManage/citeHandle.jsp?siteid=<%=siteid%>&dstSiteId=<%=siteid%>&flag=1&docsiteid=" + arr[0][3] + "&channelid=<%=channelid%>&docidStr=" + docidStr;
			window.urlstr = urls;
			showModalDialog("../doing_iframe.html",window,"dialogWidth:"+(screen.availWidth-300)+"px;dialogHeight:"+(screen.availHeight-150)+"px;help:no;scroll:auto;status:no");
			//window.close();
		}
		else if(flag == 4){				//转向移动剪切处理
			var channelid = <%=channelid%>;
			if(arr.length == 0){
				alert("至少选择一个文档");
				return;
			}
			if(arr[0][1] == channelid){
				alert("不能从当前频道剪切文档");
				return;
			}
			docidStr = getSelDocStr();
			myform.action = "moveHandle.jsp?siteId=" + arr[0][3] + "&channelid=<%=channelid%>&docidStr=" + docidStr;
			myform.target = "handleFrame" ;
			myform.submit();
		}else if(flag == 5){				//频道编辑页面用到，选择一个文档作为频道首页
			if(arr.length == 0){
				alert("至少选择一个文档");
				return;
			}
			docidStr = getSelDocStr();
			if(arr.length>1){
				alert("只能选择一个文档");
				return;
			}
			var docid = docidStr.split(":")[0];
			returnValue = docid + ":" + getSubtitle();
			window.close();
		}
	}
</SCRIPT>
</head>
<body>
	<form name="myform" method="post">
	<pg:dtoken/>
		<table align="center">
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<input type="button" name="sub" class="cms_button" value="确定" onclick="subform();">
					<input type="button" name="winclose" class="cms_button" value="取消" onclick="window.close()">
				</td>
			</tr>
		</table>
	</form>
	<div heigth=0 width=0 style="display:none">
		<iframe name="handleFrame">
		</iframe>
	</div>
</body>
</html>