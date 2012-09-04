<%
/**
  *	文档相关附件信息列表
  */
%>
<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.security.*"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

	String type = request.getParameter("type");
	String typeName = "";
	if("2".equals(type))
		typeName = "附件";
	if("3".equals(type))
		typeName = "图片";
	String docpath = request.getParameter("docpath");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>:::::相关<%=typeName%>的选择::::::::::::::::::::::.</title>
<style type="text/css">
	.STYLE1 {color: #0000FF}
	.STYLE2 {color: #000099}
	.operStyle{
	width:17;
	height:16;
	}
.style7 {font-size: 5px}
</style>
<script src="../inc/js/func.js"></script>
<SCRIPT LANGUAGE="JavaScript">
	var checkflag = false;
	function add(){
		//openWin("channel_doc_xz.jsp",screen.availWidth-20,screen.availHeight-50);
	}

	function deltrr(){
		var tableatt = document.all("doc_attach_list");
	
		var o = document.getElementsByName("id");
		for(var i=o.length-1;i>=0;i--){
			if(o[i].checked){
				tableatt.childNodes[0].removeChild(o[i].parentElement.parentElement);
			}
		}
	}

	function selectall(){
		var o = document.getElementsByName("id");
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
		var o = document.getElementsByName("id");
		for (var i=0;i<o.length;i++){
			if(!o[i].disabled){
				if (o[i].checked==false){
					cbs=false;
				}
			}
		}
	}
	//响应附件下载事件
	function downloadattrs(url)
	{
		window.open(url,"newwin","");
	}
	//自定义模态窗口打开模式
	function openDlg(url,swidth,sheight){
		//var w = showModalDialog(url,window,"dialogWidth:"+swidth+"px;dialogHeight:"+sheight+"px;help:no;scroll:no;status:no");
		//return w;
		openWin(url,swidth,sheight);
	}
</SCRIPT>
</head>
<body>
<form>
	<table class="Datalisttable" cellpadding="3" cellspacing="0" id="tables" width="95%" border="0">
		<tr>
			<td width="80%">
				<table id="doc_attach_list" width="95%" class="Datalisttable" cellpadding="3" cellspacing="0" border="0">
					  <tr class="cms_report_tr">
						<td align="center">选择</td>
						<td align="center"><%=typeName%>说明</td>
						<td align="center"><%=typeName%>存放位置</td>
					  </tr>
					  <tr>
						<td width="5%">&nbsp;</td>
						<td width="40%">&nbsp;</td>
						<td width="45%">&nbsp;</td>
					  </tr>
					  <tr>
						<td width="5%">&nbsp;</td>
						<td width="40%">&nbsp;</td>
						<td width="45%">&nbsp;</td>
					  </tr>
				</table>
			</td>
			<td width="20%">
				<table id="buttons">
					<tr>&nbsp;</tr>
					<tr><input name="add" type="button" class="cms_button" value="添加" onclick="openDlg('doc_attach_choose.jsp?type=<%=type%>&docpath=<%=docpath%>', 260, 172)"></tr>
					<tr>&nbsp;</tr>
					<tr><input name="del" type="button" class="cms_button" value="删除" onclick="deltrr();"></tr>
					<tr>&nbsp;</tr>
					<tr><input name="all" type="button" class="cms_button" value="全选" onclick="selectall();"></tr>
					<tr>&nbsp;</tr>
				</table>
			</td>
		</tr>
	</table>
</form>
</body>
</html>