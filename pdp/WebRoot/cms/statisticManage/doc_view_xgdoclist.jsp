<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String flag = request.getParameter("flag");
	String siteid = request.getParameter("siteid");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>:::::相关文档的选择::::::::::::::::::::::.</title>
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
		var tableatt = document.all("chndoclist");
	
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
	//表格row上移
	function upRow(e){
		var _row=e.parentElement.parentElement;
		if(_row.previousSibling.previousSibling)
			swapNode(_row,_row.previousSibling);
	}
	//表格row下移
	function downRow(e)
	{
		//通过链接对象获取表格行的引用
		var _row=e.parentElement.parentElement;
		//如果不是最后一行，则与下一行交换顺序
		if(_row.nextSibling.nextSibling.nextSibling)
			swapNode(_row,_row.nextSibling);

	}
	//
	function swapNode(node1,node2){
		//获取父结点
		var _parent=node1.parentNode;
		//获取两个结点的相对位置
		var _t1=node1.nextSibling;
		var _t2=node2.nextSibling;
		//将node2插入到原来node1的位置
		if(_t1)_parent.insertBefore(node2,_t1);
		else _parent.appendChild(node2);
		//将node1插入到原来node2的位置
		if(_t2)_parent.insertBefore(node1,_t2);
		else _parent.appendChild(node1);
	}
	//初始化相关table
	function document.onreadystatechange()
	{
		if (document.readyState!="complete") 
			return;
		addCols();
	}
	//
	function addCols()
	{
		var flag = "<%=flag%>";
		if(flag ==11)
			{
				//var cell0 = chndoclist.rows[0].insertCell();
				var cell1 = chndoclist.rows[0].insertCell();
				var cell2 = chndoclist.rows[0].insertCell();
				//cell0.innerHTML = "<td>所属频道</td>";
				cell1.innerHTML = "<td>显示标题</td>";
				cell2.innerHTML = "<td>操作</td>";
				for(var a=1;a<3;a++)
				{
					//var cell3 = chndoclist.rows[a].insertCell();
					var cell4 = chndoclist.rows[a].insertCell();
					var cell5 = chndoclist.rows[a].insertCell();
					//cell3.innerHTML = "<td width='10%'>&nbsp;</td>";
					cell4.innerHTML = "<td width='25%'>&nbsp;</td>";
					cell5.innerHTML = "<td width='15%'>&nbsp;</td>";
				}
			}
	}

</SCRIPT>
</head>
<body>
<form>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" id="tables">
		<tr>
			<td valign="top">
				<table id="chndoclist" width="100%" class="Datalisttable" cellpadding="3" cellspacing="0" border="0">
					  <tr class="cms_report_tr">
						<td height="30" align="center">选择</td>
						<td align="center">文档标题</td>
						<td>所属频道</td>
					  </tr>
					  <tr>
						<td width="5%" height="25">&nbsp;</td>
						<td width="40%">&nbsp;</td>
						<td width='10%'>&nbsp;</td>
					  </tr>
					  <tr>
						<td width="5%" height="25">&nbsp;</td>
						<td width="40%">&nbsp;</td>
						<td width='10%'>&nbsp;</td>
					  </tr>
			  </table>
			</td>
			<td width="15%" align="center">
				
		  </td>
		</tr>
  </table>
</form>
</body>
</html>