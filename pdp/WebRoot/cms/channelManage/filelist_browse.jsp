<%
/**
 * 选择导入的zip文件
 * (用于图片等可能有问题)
 * xinwang.jiao
 * 2007-9-29
 */
%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.FileFilter" %>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%@ page import="com.frameworkset.platform.cms.templatemanager.ZipFileFilter" %>
<%@ page import="com.frameworkset.platform.cms.util.FileUtil"%>
<%@ page import="com.frameworkset.platform.cms.util.CMSUtil"%>

<%
	//AccessControl control = AccessControl.getInstance();
	//control.checkAccess(request, response);	

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String path = request.getServletPath();
	path = path.substring(0, path.lastIndexOf("/"));
	path = path.substring(0, path.lastIndexOf("/"));
	path = application.getRealPath(path) + "/docManage/temp/";

	FileFilter fileFilter = new ZipFileFilter();
	File[] files = FileUtil.getSubFiles(path,"",fileFilter);
	
%>
<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href='../inc/css/dialog.css' type='text/css' rel='stylesheet'>

<script language=javascript>

var sType = "image";
var sReturnFlag = "input";
var sTitle = "";
var sUploadTitle = "";

var sCurrCacheName = "";
var sCurrSortFlag = "nameasc";
var sCurrViewFlag = "thumbnail";
var sCurrSelectItemIndex="";
var sCurrSelectFileName="";

var arr_t = new Array();
<%
if(files != null)
{
	for(int i = 0;i < files.length;i ++)
	{
		out.println("arr_t[" + i + "] = new Array(\"" + files[i].getName() + "\", \"" + CMSUtil.convertFileSize(files[i].length()) + "\",\"" + CMSUtil.formatDate(new Date(files[i].lastModified())) + "\");");
	}
}
%>

switch(sType){
case "image":
	sTitle = "图片";
	sUploadTitle = "上传图片库";
	break;
case "flash":
	sTitle = "Flash";
	sUploadTitle = "上传Flash库";
	break;
case "media":
	sTitle = "媒体";
	sUploadTitle = "上传媒体库";
	break;
default:
	sType = "file";
	sTitle = "所有文件";
	sUploadTitle = "上传的文件";
	break;
}


var oCurrSelected = null;

function showFileList(){
	sCurrSelectFileName = "";
	sCurrSelectItemIndex = "";

	var arr = arr_t;
	var l = arr.length;
	if (l==0){
		divFile.innerHTML = "";
		return;
	}

	switch(sCurrSortFlag){
	case "nameasc":
		arr.sort(function(x,y){return x[0].localeCompare(y[0])});
		break;
	case "namedesc":
		arr.sort(function(x,y){return y[0].localeCompare(x[0])});
		break;
	case "sizeasc":
		arr.sort(function(x,y){return parseFloat(x[1])-parseFloat(y[1])});
		break;
	case "sizedesc":
		arr.sort(function(x,y){return parseFloat(y[1])-parseFloat(x[1])});
		break;
	case "typeasc":
		arr.sort(function(x,y){var s1=x[0].substr(x[0].lastIndexOf(".")+1);s1=s1+"          ";s1=s1.substr(0,10);s1=s1+x[0];var s2=y[0].substr(y[0].lastIndexOf(".")+1);s2=s2+"          ";s2=s2.substr(0,10);s2=s2+y[0];return s1.localeCompare(s2)});
		break;
	case "typedesc":
		arr.sort(function(x,y){var s1=y[0].substr(y[0].lastIndexOf(".")+1);s1=s1+"          ";s1=s1.substr(0,10);s1=s1+y[0];var s2=x[0].substr(x[0].lastIndexOf(".")+1);s2=s2+"          ";s2=s2.substr(0,10);s2=s2+x[0];return s1.localeCompare(s2)});
		break;
	case "timeasc":
		arr.sort(function(x,y){return x[2].localeCompare(y[2])});
		break;
	case "timedesc":
		arr.sort(function(x,y){return y[2].localeCompare(x[2])});
		break;
	}

	var html = "";
	var m,n;
	
	//var sCurrViewFlag = "detail";
	
	switch (sCurrViewFlag){
	case "detail":
		var s_CurrSortField, s_CurrSortWard;
		if (sCurrSortFlag){
			s_CurrSortField = sCurrSortFlag.substr(0,4);
			s_CurrSortWard = sCurrSortFlag.substr(4);	
		}else{
			s_CurrSortField = "name";
			s_CurrSortWard = "asc";
		}

		var s_ArrowImg="";
		var s_ArrowHtmlName="";
		var s_ArrowHtmlSize="";
		var s_ArrowHtmlType="";
		var s_ArrowHtmlTime="";

		if (s_CurrSortWard=="asc"){
			s_ArrowImg = "../images/browse_img/tree/arrowup.gif";
		}else{
			s_ArrowImg = "../images/browse_img/tree/arrowdown.gif";
		}
		s_ArrowImg = "&nbsp;<img border=0 align=absmiddle src='"+s_ArrowImg+"'>";
		if (s_CurrSortField=="name"){s_ArrowHtmlName=s_ArrowImg}
		if (s_CurrSortField=="size"){s_ArrowHtmlSize=s_ArrowImg}
		if (s_CurrSortField=="type"){s_ArrowHtmlType=s_ArrowImg}
		if (s_CurrSortField=="time"){s_ArrowHtmlTime=s_ArrowImg}

		html = "<table border=0 cellpadding=1 cellspacing=1 width='100%'>";
		html += "<tr style='BACKGROUND-COLOR: #f0f0f0'>"
			+"<td width='20' align=center>&nbsp;</td>"
			+"<td width='185' align=left onclick=\"doClickSort('name')\">文件名称"+s_ArrowHtmlName+"</td>"
			+"<td width='75' align=left onclick=\"doClickSort('type')\">文件类型"+s_ArrowHtmlType+"</td>"
			+"<td width='130' align=center onclick=\"doClickSort('time')\">修改时间"+s_ArrowHtmlTime+"</td>"
			+"<td width='85' align=right onclick=\"doClickSort('size')\">文件大小"+s_ArrowHtmlSize+"</td>"
			+"</tr>";
		for (var i=0; i<l; i++){
			html += "<tr align=center id='item_tr_"+i+"' onclick=\"doClickDetail('"+i+"')\" onmouseover=\"doMouseOverDetail('"+i+"')\" onmouseout=\"doMouseOutDetail('"+i+"')\">"
				+"<td><img border=0 src='../images/browse_img/icon16/"+getFilePic(arr[i][0])+"' align=absmiddle></td>"
				+"<td align=left id='item_filename_"+i+"'>"+arr[i][0]+"</td>"
				+"<td align=left>"+arr[i][0].substr(arr[i][0].lastIndexOf(".")+1)+"</td>"
				+"<td align=center>"+arr[i][2]+"</td>"
				+"<td align=right>"+arr[i][1]+"</td>"
				+"</tr>";
		}
		html += "</table>";

		break;
	case "thumbnail":
		html = "<table border=0 cellpadding=0 cellspacing=0 width='100%'>";
		for (var i=0; i<l; i++){
			m = (i+1) % 4;
			if (m==1){
				html += "<tr>";
			}
			html += "<td align=center valign=top width='25%'>"
				+"<table border=0 cellpadding=0 cellspacing=0 onclick=\"doClickThumbnail('"+i+"')\" style='table-layout:fixed;word-wrap:break-word;'><tr><td>"
					+"<table border=0 cellpadding=1 cellspacing=3 id='item_table_"+i+"'><tr><td bgcolor=#ffffff>"
						+"<table border=0 cellspacing=1 cellpadding=0 width=120 height=120 style='border:1px solid #808080; table-layout:fixed;word-wrap:break-word;'><tr><td align=center valign=middle>"+getThumbnailHTML(arr[i][0])+"</td></tr></table>"
					+"</td></tr></table>"
				+"</td></tr>"
				+"<tr><td align=center><span id='item_span_"+i+"'>"+arr[i][0]+"</span></td></tr>"
				+"</table>"
				+"</td>";
			if (m==0){
				html += "</tr>";
			}
		}
		if (m!=0){
			for (var i=0; i<(4-m); i++){
				html += "<td width='25%'></td>";
			}
			html += "</tr>";
		}

		html += "</table>";

		break;
	case "icon":
		html = "<table border=0 cellpadding=0 cellspacing=5 width='100%'>";
		for (var i=0; i<l; i++){
			m = (i+1) % 5;
			if (m==1){
				html += "<tr>";
			}
			html += "<td valign=top width='20%'>"
				+"<table border=0 cellpadding=0 cellspacing=1 onclick=\"doClickIcon('"+i+"')\" style='table-layout:fixed;'>"
				+"<tr><td align=center><img id='item_img_"+i+"' border=0 align=absmiddle src='../images/browse_img/icon32/"+getFilePic(arr[i][0])+"'></td></tr>"
				+"<tr><td align=center height=30 valign=top id='item_td_"+i+"' style='word-wrap:break-word;line-height:1'>"+arr[i][0]+"</td></tr>"
				+"</table>"
				+"</td>";
			if (m==0){
				html += "</tr>";
			}
		}
		if (m!=0){
			for (var i=0; i<(5-m); i++){
				html += "<td width='20%'></td>";
			}
			html += "</tr>";
		}

		html += "</table>";

		break;
	case "list":
		html = "<table border=0 cellpadding=0 cellspacing=3 width='100%'>";
		for (var i=0; i<l; i++){
			m = (i+1) % 3;
			if (m==1){
				html += "<tr>";
			}
			html += "<td valign=top width='33%' onclick=\"doClickList('"+i+"')\">"
				+"<table border=0 cellpadding=0 cellspacing=0 id='item_table_"+i+"'><tr><td><img border=0 align=absmiddle src='../images/browse_img/icon16/"+getFilePic(arr[i][0])+"'></td><td width=2></td><td id='item_td_"+i+"'>"+arr[i][0]+"</td></tr></table>"
				+"</td>";
			if (m==0){
				html += "</tr>";
			}
		}
		if (m!=0){
			for (var i=0; i<(3-m); i++){
				html += "<td width='33%'></td>";
			}
			html += "</tr>";
		}

		html += "</table>";

		break;
	}

	divFile.innerHTML = html;
}

function getFlashCode(url, w, h) {
	var str = '<object width="'+ w +'" height="'+ h +'" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0">'
		+ '<param name="movie" value="'+ url +'">'
		+ '<param name="wmode" value="opaque">'
		+ '<param name="quality" value="autohigh">'
		+ '<embed width="'+ w +'" height="'+ h +'" src="'+ url +'" quality="autohigh" wmode="opaque" type="application/x-shockwave-flash" plugspace="http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash"></embed>'
		+ '</object>';
	return str;
}

function doClickDetail(s_Index){
	var elTR, elFileName;
	if (sCurrSelectItemIndex!=""){
		elTR = document.getElementById("item_tr_"+sCurrSelectItemIndex);
		elTR.className = "detailout";
	}

	elTR = document.getElementById("item_tr_"+s_Index);
	elTR.className = "detailselected";
	elFileName = document.getElementById("item_filename_"+s_Index);
	
	sCurrSelectFileName = elFileName.innerHTML;
	sCurrSelectItemIndex = s_Index;
}

function doMouseOverDetail(s_Index){
	if (sCurrSelectItemIndex==s_Index){
		return;
	}else{
		elTR = document.getElementById("item_tr_"+s_Index);
		elTR.className = "detailover";
	}
}

function doMouseOutDetail(s_Index){
	if (sCurrSelectItemIndex==s_Index){
		return;
	}else{
		elTR = document.getElementById("item_tr_"+s_Index);
		elTR.className = "detailout";
	}
}


function doClickIcon(s_Index){
	var elTD, elImg;
	if (sCurrSelectItemIndex!=""){
		elTD = document.getElementById("item_td_"+sCurrSelectItemIndex);
		elTD.className = "iconitem";
		elImg = document.getElementById("item_img_"+sCurrSelectItemIndex);
		elImg.style.filter = "";
	}

	elTD = document.getElementById("item_td_"+s_Index);
	elTD.className = "iconitemselected";
	elImg = document.getElementById("item_img_"+s_Index);
	elImg.style.filter = "gray";
	
	sCurrSelectFileName = elTD.innerText;
	sCurrSelectItemIndex = s_Index;
}

function doClickList(s_Index){
	var elTable, elTD;
	if (sCurrSelectItemIndex!=""){
		elTable = document.getElementById("item_table_"+sCurrSelectItemIndex);
		elTable.className = "listitem";
	}

	elTable = document.getElementById("item_table_"+s_Index);
	elTable.className = "listitemselected";
	elTD = document.getElementById("item_td_"+s_Index);
	sCurrSelectFileName = elTD.innerText;
	sCurrSelectItemIndex = s_Index;
}

function doClickThumbnail(s_Index){
	var elTable, elSpan;
	if (sCurrSelectItemIndex!=""){
		elTable = document.getElementById("item_table_"+sCurrSelectItemIndex);
		elTable.className = "thumbnailitem";
		elSpan = document.getElementById("item_span_"+sCurrSelectItemIndex);
		elSpan.className = "thumbnailitem";
	}

	elTable = document.getElementById("item_table_"+s_Index);
	elTable.className = "thumbnailitemselected";
	elSpan = document.getElementById("item_span_"+s_Index);
	elSpan.className = "thumbnailitemselected";
	
	sCurrSelectFileName = elSpan.innerHTML;
	sCurrSelectItemIndex = s_Index;
}

/**
  * 获取cms的根目录 
  * “d:\creatorcms\cms\creatorcms\cms\”
 */
function getCmsRootPath(){
	var url = "/" + document.location.pathname;
	url = url.substring(0,url.lastIndexOf("dialog/"));
	url = url.substring(0,url.lastIndexOf("/"));
	url = url.substring(0,url.lastIndexOf("/"));
	url = url.substring(0,url.lastIndexOf("/"));
	return url;
}

function getThumbnailHTML(s_FileName){
	var s_CurrPath = "";
	
	if (s_CurrPath.substr(0,1)!="/"){
		//modify by xinwang.jiao 2007-9-12
		//s_CurrPath = "../" + s_CurrPath;
		s_CurrPath = getCmsRootPath() + "/" + s_CurrPath;
	}
	var s_TypePic = "../images/browse_img/icon16/" + getFilePic(s_FileName);
	var s_Ext = s_FileName.substr(s_FileName.lastIndexOf(".")+1).toLowerCase();
	var html = "";
	var s_Src = s_CurrPath+s_FileName;
	if ((s_Ext=="gif")||(s_Ext=="jpg")||(s_Ext=="jpeg")||(s_Ext=="bmp")||(s_Ext=="png")){
		html = "<img border=0 src='"+s_Src+"' onload='doThumbnailLoad(this)'>";
	}else if (s_Ext=="swf"){
		html = getFlashCode(s_Src, 115, 115);
	}else{
		html = "<img border=0 src='"+s_TypePic+"' style='position:relative;left:50px;top:50px'>";
	}
	return html;
}

function doThumbnailLoad(el){
	var w = el.width;
	var h = el.height;
	var bw = 115;
	var bh = 115;
	var sw,sh;

	if ((w>bw)||(h>bh)){
		var nw = bw/w;
		var nh = bh/h;
		if (nw>nh){
			sh = bh;
			sw = w*nh;
		}else{
			sw = bw;
			sh = h*nw;
		}
	}else{
		sw = w;
		sh = h;
	}
	
	el.style.width = sw;
	el.style.height = sh;
}

function doChangeView(){
	sCurrViewFlag = d_view.options[d_view.selectedIndex].value;
	showFileList();
}

function doChangeSort(){
	sCurrSortFlag = d_sort.options[d_sort.selectedIndex].value + d_sortward.options[d_sortward.selectedIndex].value;
	showFileList();
}

function doClickSort(flag){
	var s_CurrSortField = sCurrSortFlag.substr(0,4);
	var s_CurrSortWard = sCurrSortFlag.substr(4);

	var s_SortField = flag;
	var s_SortWard = "";

	if (flag==s_CurrSortField){
		if (s_CurrSortWard=="desc"){
			s_SortWard = "asc";
		}else{
			s_SortWard = "desc";
		}
	}else{
		s_SortWard = "asc"
	}

	SearchSelectValue(d_sort, s_SortField);
	SearchSelectValue(d_sortward, s_SortWard);

	sCurrSortFlag = s_SortField + s_SortWard;
	showFileList();
}

function SearchSelectValue(o_Select, s_Value){
	for (var i=0;i<o_Select.length;i++){
		if (o_Select.options[i].value == s_Value){
			o_Select.selectedIndex = i;
			return true;
		}
	}
	return false;
}

function getFilePic(url){
	var sExt;
	sExt=url.substr(url.lastIndexOf(".")+1);
	sExt=sExt.toUpperCase();
	var sPicName;
	switch(sExt){
	case "TXT":
		sPicName = "txt.gif";
		break;
	case "CHM":
		sPicName = "chm.gif";
		break;
	case "HLP":
		sPicName = "hlp.gif";
		break;
	case "DOC":
		sPicName = "doc.gif";
		break;
	case "PDF":
		sPicName = "pdf.gif";
		break;
	case "MDB":
		sPicName = "mdb.gif";
		break;
	case "GIF":
		sPicName = "gif.gif";
		break;
	case "JPG":
	case "JPEG":
		sPicName = "jpg.gif";
		break;
	case "BMP":
		sPicName = "bmp.gif";
		break;
	case "PNG":
		sPicName = "png.gif";
		break;
	case "ICO":
		sPicName = "ico.gif";
		break;
	case "ASP":
	case "JSP":
	case "PHP":
	case "PHP3":
		sPicName = "code.gif";
		break;
	case "JS":
	case "VBS":
		sPicName = "js.gif";
		break;
	case "ASPX":
		sPicName = "aspx.gif";
		break;
	case "XML":
		sPicName = "xml.gif";
		break;
	case "HTM":
	case "HTML":
	case "SHTML":
		sPicName = "htm.gif";
		break;
	case "ZIP":
		sPicName = "zip.gif";
		break;
	case "RAR":
		sPicName = "rar.gif";
		break;
	case "EXE":
		sPicName = "exe.gif";
		break;
	case "AVI":
		sPicName = "avi.gif";
		break;
	case "MPG":
	case "MPEG":
	case "ASF":
		sPicName = "mp.gif";
		break;
	case "RA":
	case "RM":
		sPicName = "rm.gif";
		break;
	case "MP3":
		sPicName = "mp3.gif";
		break;
	case "MID":
	case "MIDI":
		sPicName = "mid.gif";
		break;
	case "WAV":
		sPicName = "audio.gif";
		break;
	case "XLS":
		sPicName = "xls.gif";
		break;
	case "PPT":
	case "PPS":
		sPicName = "ppt.gif";
		break;
	case "SWF":
		sPicName = "swf.gif";
		break;
	default:
		sPicName = "unknow.gif";
		break;
	}
	return sPicName;

}

function ok(){
	if (!sCurrSelectFileName){
		alert("请先选择一个文件！");
		return;
	}

	window.dialogArguments.setImage(sCurrSelectFileName);
	window.close();
}

function InitDocument(){
	showFileList();
}

</script>

<style>
div.nodediv {padding:0px;margin:0px;WHITE-SPACE: nowrap;}
div.node {padding:0px;margin:0px;WHITE-SPACE: nowrap;}
.nodetext {color:#000000;background-color:#ffffff}
.nodetextselected {color:#ffffff;background-color:#0A246A}
.thumbnailitem {color:#000000;background-color:#ffffff}
.thumbnailitemselected {color:#ffffff;background-color:#0A246A}
.listitem {color:#000000;background-color:#ffffff}
.listitemselected {color:#ffffff;background-color:#0A246A}
.iconitem {color:#000000;background-color:#ffffff}
.iconitemselected {color:#ffffff;background-color:#0A246A}

.detailout {}
.detailover {background-color:#eeeeee}
.detailover td {color:#000000;}
.detailselected {background-color:#0A246A}
.detailselected td {color:#ffffff;}
</style>
<title>导入压缩文件包浏览</title>
</HEAD>

<BODY onload="InitDocument()">
<table border=0 cellpadding=0 cellspacing=5 id=tabDialogSize><tr><td>

<table border=0 cellpadding=0 cellspacing=0 width="100%" align=center>
<tr><td>

<table border=0 cellpadding=0 cellspacing=0 width="" align=center>
<tr>
	<td noWrap width="20px"></td>
	<td noWrap width="100%"></td>
	<td noWrap width="" align="right">
		<table border=0 cellpadding=0 cellspacing=0>
		<tr>
			<td noWrap>排序:</td>
			<td><select id=d_sort size=1 style="width:80px" onchange="doChangeSort()"><option value="name">文件名称</option><option value="size">文件大小</option><option value="type">文件类型</option><option value="time">修改时间</option></select></td>
			<td><select id=d_sortward size=1 style="width:50px" onchange="doChangeSort()"><option value="asc">正序</option><option value="desc">倒序</option></select></td>
			<td>&nbsp;</td>
			<td noWrap>查看:</td>
			<td><select id=d_view size=1 style="width:80px" onchange="doChangeView()"><option value="detail">详细资料</option><option value="thumbnail" selected>缩略图</option><option value="icon">图标</option><option value="list">列表</option></select></td>
		</table>
	</td>
</tr>
</table>

</td></tr>
<tr><td>

<table border=0 cellpadding=0 cellspacing=0 align=center>
<tr>
<td vAlign=top noWrap>
	
</td>
<td width=10>&nbsp; </td>
<td vAlign=top>
	<DIV id=divFile style="BORDER-RIGHT: 1.5pt inset; PADDING-RIGHT: 0px; BORDER-TOP: 1.5pt inset; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; VERTICAL-ALIGN: top; OVERFLOW: auto; BORDER-LEFT: 1.5pt inset; WIDTH: 540px; PADDING-TOP: 0px; BORDER-BOTTOM: 1.5pt inset; HEIGHT: 350px; BACKGROUND-COLOR: white">
	
	</DIV>
</td>
</tr>
</table>

</td></tr>
<tr><td height=5></td></tr>
<tr><td noWrap align=right><input type=submit class="dlgBtnCommon" value='确定' id=Ok onclick="ok()">&nbsp;&nbsp;<input type=button class="dlgBtnCommon" value='取消' onclick="window.close();"></td></tr>

</td></tr></table>


</BODY></HTML>