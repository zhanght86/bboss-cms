<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.io.*" %>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%@ page import="com.frameworkset.platform.cms.*"%>
<%@ page import="com.frameworkset.platform.cms.util.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*" %>
<%@ page import="com.frameworkset.platform.cms.imagemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*" %>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuTag"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenu"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuImpl"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.Menu"%>
<jsp:useBean id="date" scope="page" class="net.fiyu.edit.TimeStamp"/>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>文件列表</title>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);
    ImageManagerImpl impl = new ImageManagerImpl();
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	//上传文档默选中
	String fileName = request.getParameter("fileName");
	fileName = fileName == null ? "" : fileName ;
	
	String fileFlag = request.getParameter("fileFlag");
	String uri = request.getParameter("uri");
	if(uri != null && !uri.endsWith("/")&&!uri.endsWith("\\")){
		uri += "/";
	}
	if(fileFlag!=null && fileFlag.equals("1"))
	{
    	uri = uri==null?"/":uri;
	}
	else
	{
		uri = uri==null?"uploadfiles/"+(String)date.Time_YMD().substring(0,6)+"/":uri;
	}
    uri = uri.replaceAll("\\\\","/");
	//fileFlag为"1"表示首页文件的选择，为null或""表示为图片的选择
	
	//fileFlag = fileFlag == null?"pic":fileFlag;//默认为选择图片
	//图片浏览视图：list，列表；ppt，幻灯片；thumbnail，缩略图
	//session设置来自点击视图切换的页面chooseImageTools.jsp
	String viewertype = (String)session.getAttribute("viewertype");//request.getParameter("viewertype");

	CMSManager cmsmanager = new CMSManager();
	cmsmanager.init(request,session,response,control);

	String siteId =  cmsmanager.getSiteID();
	String siteSecondName = cmsmanager.getCurrentSite().getSiteDir();
	
    /*根路径*/
	String pathContext = null;
	if(siteId!=null && siteId.trim().length()!=0){
		String temp = (new SiteManagerImpl()).getSiteAbsolutePath(siteId);
		if(temp!=null && temp.trim().length()!=0){
			pathContext = new File(temp,"_template").getAbsolutePath();
		}
	}
	if(pathContext==null || pathContext.trim().length()==0){
%>
		<script language="javascript">
			alert("没有找到站点所在路径.");
		</script>
<% 
		return;
	}else{
        pathContext = pathContext.replaceAll("\\\\","/");
    }
%>
<%
response.setContentType("text/html;charset=UTF-8");
 %>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style>
<script type="text/javascript">
var uri = "";
<%
if(uri!=null && uri.trim().length()!=0){
	out.println("uri = '"+uri+"';");
}
%>
function setImage(name){
	var url = uri.replace("\\","/");
	
	if(url.charAt(0) == '/'){
		url = url.substr(1); 
	}
	if(url.charAt(url.length-1) != '/' && url.search(/\S+/img)!=-1){
		url += "/";
	}
	
	url += name;

	parent.parent.theOpener.setImage(url);
	
	top.close();
}
function preview(fileName,abstractPth){
	var uri = "<%=uri==null?"/":uri%>";
	var url = uri.replace("\\","/");
	if(url.charAt(0) == '/'){
		url = url.substr(1); 
	}
	if(url.charAt(url.length-1) != '/' && url.search(/\S+/img)!=-1){
		url += "/";
	}
   window.open("<%=rootpath%>/cms/siteResource/<%=siteSecondName%>/_template/"+ url + fileName);
}
function preview1(fileName,abstractPth){
   var uri = "<%=uri==null?"/":uri%>";
	var url = uri.replace("\\","/");
	if(url.charAt(0) == '/'){
		url = url.substr(1); 
	}
	if(url.charAt(url.length-1) != '/' && url.search(/\S+/img)!=-1){
		url += "/";
	}
     var path="<%=rootpath%>/cms/siteResource/<%=siteSecondName%>/_template/"+ url + fileName;
     window.open("showimg.jsp?filepath="+path);
	
}
function high(ob,fileName){
    //ID使用冲突,ID要作为右键显示的ID
    //modify by ge.tao 2007-6-25
    //var tds = document.getElementsByName("picName");   
	
    var tds = document.getElementsByTagName("td");
	for(var i=0;i<tds.length;i++){
		unhigh(tds[i]);
	}
	ob.bgColor="#242464";
	parent.ToolsFrm.selectedFileName=fileName;
	
}
//用于幻灯片和缩略图模式
function highOther(ob,fileName){
    //ID使用冲突,ID要作为右键显示的ID
    //modify by ge.tao 2007-6-25
    //var tds = document.getElementsByName("picName");   
	
    var tds = document.getElementsByTagName("td");
	for(var i=0;i<tds.length;i++){
		unhigh(tds[i]);
	}
	ob.bgColor="#C3D2E5";
	//parent.ToolsFrm.selectedFileName=fileName;
}
function unhigh(ob){
	ob.bgColor="";
}
function small(fileName){
	var liwei_name = '<%=fileFlag%>';
	if((liwei_name=='pic')||(liwei_name=='picOrFlash'))
	{
		//var img_id = document.getElementById(fileName);
		//img_id.width="40";
		//img_id.height="30";
		document.getElementById(fileName).style.display="none";
	}
}
function big(fileName){
	var liwei_name = '<%=fileFlag%>';
	if((liwei_name=='pic')||(liwei_name=='picOrFlash'))
	{
		//var img_id = document.getElementById(fileName);
		//img_id.width="200";
		//img_id.height="200";
		document.getElementById(fileName).style.display="";
	}
}
function test(o)
{
var uri = "<%=uri==null?"/":uri%>";
	var url = uri.replace("\\","/");
	if(url.charAt(0) == '/'){
		url = url.substr(1); 
	}
	if(url.charAt(url.length-1) != '/' && url.search(/\S+/img)!=-1){
		url += "/";
	}
     var path="<%=rootpath%>/cms/siteResource/<%=siteSecondName%>/_template/"+ url + o.filename;
     window.open(path);
}
/**
* filename 图片名称
* ob 选中对象
* 对选中图片加水印.
* @author ge.tao
* @date 2007-6-25
*/
function addWaterImage(ob,filename){
    //选中 高亮显示
    high(ob,filename);
    var uri = "<%=uri==null?"/":uri%>";
    var url = uri.replace("\\","/");
    if(url.charAt(0) == '/'){
        url = url.substr(1); 
    }
    if(url.charAt(url.length-1) != '/' && url.search(/\S+/img)!=-1){
        url += "/";
    }
    uri = url;
    var swidth = 300;
    var sheight = 300;
    var path="<%=rootpath%>/cms/siteResource/<%=siteSecondName%>/_template/"+ url + filename;
    url = "../imageManager/setWaterImage.jsp?path="+path+"&pathContext=<%=pathContext%>&uri="+uri;
    var returnVal;
    returnVal = openWin(url,swidth,sheight);
    if(returnVal=="True"){
        //alert("refresh");
        var param = window.location.search;
        window.location.href="chooseImageList.jsp"+param; 
    }
    
}
/**
* filename 图片名称
* ob 选中对象
* 对选中图片恢复至原始图片.
* 实现:用备份的原始图片替换被加水印的图片
* @author ge.tao
* @date 2007-6-25
*/
function recoverWaterImage(ob,filename){
    var swidth = 1;
    var sheight = 1; 
    var uri = "<%=uri==null?"/":uri%>";
    var url = uri.replace("\\","/");
    if(url.charAt(0) == '/'){
        //url = url.substr(1); 
    }
    if(url.charAt(url.length-1) != '/' && url.search(/\S+/img)!=-1){
        url += "/";
    }
    var path = "../imageManager/recoverImage.jsp?filename="+filename+"&url="+url;
    var returnVal;
    returnVal = openWin(path,swidth,sheight);
    if(returnVal=="True"){
        //alert("refresh");
        var param = window.location.search;
        window.location.href="chooseImageList.jsp"+param; 
    }
}
/**
* filename 图片名称
* ob 选中对象
* 对选中图片重加水印. 原来的水印抹掉 
* 实现:先恢复至原始图片 然后加水印
* @author ge.tao
* @date 2007-6-25
*/
function reAddWaterImage(ob,filename){
    //选中 高亮显示
    high(ob,filename);
    var uri = "<%=uri==null?"/":uri%>";
    var url = uri.replace("\\","/");
    if(url.charAt(0) == '/'){
        url = url.substr(1); 
    }
    if(url.charAt(url.length-1) != '/' && url.search(/\S+/img)!=-1){
        url += "/";
    }
    uri = url;
    var swidth = 300;
    var sheight = 300;
    var path="<%=rootpath%>/cms/siteResource/<%=siteSecondName%>/_template/"+ url + filename;    
    url = "../imageManager/setWaterImage.jsp?path="+path+"&opt=readd&pathContext=<%=pathContext%>&uri="+uri;;
    var returnVal;
    returnVal = openWin(url,swidth,sheight);
    if(returnVal=="True"){
        var param = window.location.search;
        //alert("refresh");
        window.location.href="chooseImageList.jsp"+param; 
    }
}
function openWin(url,swidth,sheight){
     var w = showModalDialog(url,window,"dialogWidth:"+swidth+"px;dialogHeight:"+sheight+"px;help:no;scroll:auto;status:no");
     return w;
}
var temobj = null;
var temptr = null;
function showpic(obj,fname)
{
	if(temobj)
		temobj.bgColor = "white";
	obj.parentElement.parentElement.parentElement.bgColor = "#E9EDF3";//"#EEEEEE";
	temobj = obj.parentElement.parentElement.parentElement;
	document.all("indeximg").src = obj.src;
	parent.ToolsFrm.selectedFileName = fname;
}
function choosepic(obj,fname)
{
	if(temobj)
		temobj.bgColor = "white";
	obj.parentElement.parentElement.parentElement.bgColor = "#E9EDF3";//"#EEEEEE";
	temobj = obj.parentElement.parentElement.parentElement;
	parent.ToolsFrm.selectedFileName = fname;
}
</script>

</head>
<body>
<table id="imgs_table" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolordark="#FFFFFF" bordercolorlight="#eeeeee">
<%
	FileFilter fileFilter = null;//System.out.println(fileFlag);
	if(fileFlag != null && fileFlag.equals("media"))
	{
		fileFilter = new MediaFileFilter();
	}
	if(fileFlag != null && fileFlag.equals("pic"))
	{
		fileFilter = new ImageFileFilter();
	}
	if(fileFlag != null && fileFlag.equals("1"))
	{
       fileFilter = new IndexPageFilter(siteId,uri);
	}
	if(fileFlag != null && fileFlag.equals("file"))
	{
       fileFilter = new ResourceFilter();
	}
	if(fileFlag != null && fileFlag.equals("flash"))
	{
       fileFilter = new FlashFileFilter();
	}
	if(fileFlag != null && fileFlag.equals("picOrFlash"))
	{
       fileFilter = new ImageOrFlashFileFilter();
	}
	File[] files = FileUtil.getSubFiles(pathContext,uri,fileFilter);
    ContextMenu contextmenu = null;
    Menu menu = null;
    if(fileFlag != null && ("pic".equals(fileFlag)||fileFlag.equals("picOrFlash")))//是图片才要加水印，才有右键菜单
    {
    	contextmenu = new ContextMenuImpl();
    }
    
	if(fileFlag != null && (fileFlag.equals("pic")||fileFlag.equals("picOrFlash")) && viewertype != null && viewertype.equals("ppt"))//ppt，幻灯片
	{
		String indeximg = "";
		if(files != null && files.length > 0)
		{
			indeximg = files[0].getName();
%>
	<tr>
		<td height="320" align="center">
		<script type="text/javascript">
        	var uri = "<%=uri==null?"/":uri%>";
	        var url = uri.replace("\\","/");
	       	if(url.charAt(0) == '/')
	    	{
				url = url.substr(1); 
			}
			if(url.charAt(url.length-1) != '/' && url.search(/\S+/img)!=-1)
			{
				url += "/";
			}
			var path = "<%=rootpath%>/cms/siteResource/<%=siteSecondName%>/_template/"+ url + "<%=indeximg%>";
			var filename = "<%=indeximg%>";
			var imgstr = '';
			//if(filename.substring(filename.indexOf('.')+1)=='swf')
			//{
			 // imgstr = '<object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" \n';
			 // imgstr += 'codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0\" \n';
			  //imgstr += 'width=\"220\"  height=\"260\"> \n';
			 // imgstr += '<param name=\"movie\" value=\"'+ path +'\"> \n';
			  //imgstr += '<param name=\"quality\" value=\"high\"> \n';
			  //imgstr += '<embed src=\"'+ path +'\" width=\"220\"  height=\"260\" quality=\"high\" \n';
			  //imgstr += 'pluginspage=\"http://www.macromedia.com/go/getflashplayer\" type=\"application/x-shockwave-flash\"> \n';
			  //imgstr += '</embed> \n';
			 // imgstr += '</object> \n';
			//}
			//else{
			imgstr='<image src="'+path+'" filename="'+filename+'" style=\"cursor:hand\" name=\"indeximg\" id=\"indeximg\" width=\"220\" height=\"260\" alt=\"预览\"></image>';
            //} 
			document.write(imgstr);
		</script>
		</td>
	</tr>
	<tr>
		<td>
			<div style="overflow-x:auto;overflow-y:hidden;width:480px;height:165px;">
				<table id=imgtable height="160" width="480">
					<tr align="left" id=imgtr></tr>
				</table>
			</div>
		</td>
	</tr>
<%
		}
	}
    if(fileFlag != null && (fileFlag.equals("pic")||fileFlag.equals("picOrFlash")) && viewertype != null && viewertype.equals("thumbnail"))//thumbnail，缩略图
	{
%>
	
<%
	}           
	for(int i=0;files!=null&&i<files.length;i++){
		String docId = files[i].getName();
		if(fileFlag != null && ("pic".equals(fileFlag)||fileFlag.equals("picOrFlash")))//是图片才要加水印，才有右键菜单
		{
	        //右键设置 step 1
	        menu = new Menu();
	        menu.setIdentity("opdoc_" + i);//对应TD的ID
	        //右键设置 内容设置
	        String path = CMSUtil.getPath(CMSUtil.getPath(pathContext,uri),docId);
	        path = path.replaceAll("\\\\","/");
	        path = path.replaceAll("//","/");
	        int flag = impl.isAddedWaterImage(path.toLowerCase());
	        if(flag == 0){
	            //未加水印
	            Menu.ContextMenuItem menuitem1 = new Menu.ContextMenuItem();
	            menuitem1.setName("加水印");
	            menuitem1.setLink("javascript:addWaterImage(this,'" + docId + "')");
	            menuitem1.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_fbyl.gif");
	            menu.addContextMenuItem(menuitem1);
	        }else if(flag == 1){
	            //已加水印 无备份文件
	        }else{
	            //已加水印 有备份文件
	            Menu.ContextMenuItem menuitem2 = new Menu.ContextMenuItem();
	            menuitem2.setName("恢复原始图片");
	            menuitem2.setLink("javascript:recoverWaterImage(this,'" + docId + "')");
	            menuitem2.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_fbyl.gif");
	            menu.addContextMenuItem(menuitem2);
	            
	            Menu.ContextMenuItem menuitem3 = new Menu.ContextMenuItem();
	            menuitem3.setName("重加水印");
	            menuitem3.setLink("javascript:reAddWaterImage(this,'" + docId + "')");
	            menuitem3.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_fbyl.gif");
	            menu.addContextMenuItem(menuitem3);
	        }
	        contextmenu.addContextMenu(menu);
        }
%>
	<%
		if(viewertype == null || viewertype.equals("null") || viewertype.equals("list") || (fileFlag != null && !(fileFlag.equals("pic")||fileFlag.equals("picOrFlash"))))
		{
	%>
	<tr onselectstart="return false" style="cursor:hand;height:25px"  class="cms_data_tr">
		<td  onclick="high(this,'<%=docId%>')" onmouseover="big('<%=docId%>')" onmouseout="small('<%=docId%>')" name="picName" id="opdoc_<%=i%>"  width="80%">
		    <%=docId%>
		    <div id="<%=docId%>" STYLE="position:absolute;display:none;z-index:100;top:150px;left:120px;background: #fff; width:200px; height:200px"><image src="<%=request.getContextPath()%>/cms/siteResource/<%=siteSecondName%>/_template/<%=(uri==null?"/":uri).replace('\\','/')%><%=docId%>" width="200" height="200"/></div>
		</td>
		
		<%
			if(docId.equals(fileName))
			{
		%>
			<script language="javascript">high(document.getElementById("opdoc_<%=i%>"),'<%=docId%>')</script>
		<%
			}
			if(fileFlag != null && (fileFlag.equals("pic")||fileFlag.equals("picOrFlash")))//图片
			{
		%>
			<td width="20%">
			<script type="text/javascript">
		        var uri = "<%=uri==null?"/":uri%>";
				var url = uri.replace("\\","/");
				if(url.charAt(0) == '/')
				{
					url = url.substr(1); 
				}
				if(url.charAt(url.length-1) != '/' && url.search(/\S+/img)!=-1)
				{
					url += "/";
				}
				var path="<%=rootpath%>/cms/siteResource/<%=siteSecondName%>/_template/"+ url + "<%=docId%>";
				var filename="<%=docId%>";
				var imgstr = "";
		        if(filename.substring(filename.indexOf('.')+1)=='swf')
		        {
			       imgstr = '<object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" \n';
			       imgstr += 'codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0\" \n';
			       imgstr += 'width=\"40\"  height=\"30\"> \n';
			       imgstr += '<param name=\"movie\" value=\"'+ path +'\"> \n';
			       imgstr += '<param name=\"quality\" value=\"high\"> \n';
			       imgstr += '<embed src=\"'+ path +'\" width=\"40\"  height=\"30\" quality=\"high\" \n';
			       imgstr += 'pluginspage=\"http://www.macromedia.com/go/getflashplayer\" type=\"application/x-shockwave-flash\"> \n';
			       imgstr += '</embed> \n';
			       imgstr += '</object> ';
		        }
		        else{   
				 imgstr='<image src="'+path+'"  filename="'+filename+'" style=\"cursor:hand\" onclick=\"test(this)\" name=\"previewPic\" width=\"40\" height=\"30\" alt=\"预览\"></image>';
		        }     
				document.write(imgstr);
		             
			</script>
			</td>
		<%
			}
			if(fileFlag != null && fileFlag.equals("1"))//首页
			{
		%>
			<td width="20%">
				<a style="cursor:hand" onclick="preview('<%=files[i].getName()%>')">预览</a>
			</td>
		<%
			}
		%>
	</tr>
	<%
		}
		if(fileFlag != null && (fileFlag.equals("pic")||fileFlag.equals("picOrFlash")) && viewertype != null && viewertype.equals("ppt"))//ppt，幻灯片
		{
	%>
	
	<script type="text/javascript">
        var uri = "<%=uri==null?"/":uri%>";
		var url = uri.replace("\\","/");
		if(url.charAt(0) == '/')
		{
			url = url.substr(1); 
		}
		if(url.charAt(url.length-1) != '/' && url.search(/\S+/img)!=-1)
		{
			url += "/";
		}
		var path = "<%=rootpath%>/cms/siteResource/<%=siteSecondName%>/_template/"+ url + "<%=docId%>";
		var filename = "<%=docId%>";
		var table_start = "<table width=120 height=120 onMouseOver=highOther(this) onMouseOut=unhigh(this) align=center><tr align=center><td height=85 align=center>";
		var imgstr = "";
		if(filename.substring(filename.indexOf('.')+1)=='swf')
		{
			  imgstr = '<object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" \n';
			  imgstr += 'codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0\" \n';
			  imgstr += 'width=\"80\"  height=\"80\"> \n';
			  imgstr += '<param name=\"movie\" value=\"'+ path +'\"> \n';
			  imgstr += '<param name=\"quality\" value=\"high\"> \n';
			  imgstr += '<embed src=\"'+ path +'\" width=\"80\"  height=\"80\" quality=\"high\" \n';
			  imgstr += 'pluginspage=\"http://www.macromedia.com/go/getflashplayer\" type=\"application/x-shockwave-flash\"> \n';
			  imgstr += '</embed> \n';
			  imgstr += '</object> \n';
		}
		else{
		 imgstr = '<image src="'+path+'" filename="'+filename+'" style=\"cursor:hand\" onclick=\"showpic(this,\'' + filename + '\');\" name=\"previewPic\" width=\"80\" height=\"80\" alt=\"预览\"></image>';
        }
        var table_end = "</td></tr><tr><td id='opdoc_<%=i%>' align=center style='word-break:break-all;cursor:hand'>" + filename + "</td></tr></table>";
             
		var td1 = document.createElement("td");
		td1.innerHTML = table_start + imgstr + table_end;
		document.all("imgtr").appendChild(td1);
		     
	</script>
	<%
		if(i == files.length-1)
		{
	%>
	<script type="text/javascript">
		var len = "<%=(files.length-1)%>";
        var td1 = document.createElement("td");
		len = 480 - len*80;
		if(len > 0)
		{
			td1.width = len;
			td1.innerHTML = "&nbsp;";
			document.all("imgtr").appendChild(td1);
		}
		    
	</script>
	<%
		}
		}
	%>
	<%
		if(fileFlag != null && (fileFlag.equals("pic")||fileFlag.equals("picOrFlash")) && viewertype != null && viewertype.equals("thumbnail"))//thumbnail，缩略图
		{
			if(i % 4 == 0)
			{
	%>
	<script type="text/javascript">
		var id_str = "tr_" + "<%=i%>";
		document.write("<tr id=" + id_str + "></tr>");
        var uri = "<%=uri==null?"/":uri%>";
		var url = uri.replace("\\","/");
		if(url.charAt(0) == '/')
		{
			url = url.substr(1); 
		}
		if(url.charAt(url.length-1) != '/' && url.search(/\S+/img)!=-1)
		{
			url += "/";
		}
		var path = "<%=rootpath%>/cms/siteResource/<%=siteSecondName%>/_template/"+ url + "<%=docId%>";
		var filename = "<%=docId%>";
		var table_start = "<table width=85 height=120 onMouseOver=highOther(this) onMouseOut=unhigh(this) align=center><tr onselectstart=\"return false\" align=center><td height=85 align=center>";
		var imgstr = "";
		if(filename.substring(filename.indexOf('.')+1)=='swf')
		{
			  imgstr = '<object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" \n';
			  imgstr += 'codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0\" \n';
			  imgstr += 'width=\"100\"  height=\"100\"> \n';
			  imgstr += '<param name=\"movie\" value=\"'+ path +'\"> \n';
			  imgstr += '<param name=\"quality\" value=\"high\"> \n';
			  imgstr += '<embed src=\"'+ path +'\" width=\"100\"  height=\"100\" quality=\"high\" \n';
			  imgstr += 'pluginspage=\"http://www.macromedia.com/go/getflashplayer\" type=\"application/x-shockwave-flash\"> \n';
			  imgstr += '</embed> \n';
			  imgstr += '</object>';
		}
		else{
		 imgstr = '<image src="'+path+'" filename="'+filename+'" style=\"cursor:hand\" onclick=\"choosepic(this,\'' + filename + '\')\" name=\"previewPic\" width=\"100\" height=\"100\" alt=\"预览\"></image>';
        }
        var table_end = "</td></tr><tr><td id='opdoc_<%=i%>' style='width:80px;word-break:break-all;cursor:hand' align=center>" + filename + "</td></tr></table>";
             
		var tr1 = document.createElement("td");
		tr1.innerHTML = table_start + imgstr + table_end;
		document.all(id_str).appendChild(tr1);
		temptr = document.all(id_str);  
	</script>
	<%
		}
		else
		{
	%>
	<script type="text/javascript">
        var uri = "<%=uri==null?"/":uri%>";
		var url = uri.replace("\\","/");
		if(url.charAt(0) == '/')
		{
			url = url.substr(1); 
		}
		if(url.charAt(url.length-1) != '/' && url.search(/\S+/img)!=-1)
		{
			url += "/";
		}
		var path = "<%=rootpath%>/cms/siteResource/<%=siteSecondName%>/_template/"+ url + "<%=docId%>";
		var filename = "<%=docId%>";
		var table_start = "<table width=85 height=120 onMouseOver=highOther(this) onMouseOut=unhigh(this) align=center><tr align=center><td height=85 align=center>";
		var imgstr = '<image src="'+path+'" filename="'+filename+'" style=\"cursor:hand\" onclick=\"choosepic(this,\'' + filename + '\');\" name=\"previewPic\" width=\"1000\" height=\"100\" alt=\"预览\"></image>';
        var table_end = "</td></tr><tr><td id='opdoc_<%=i%>' style='width:80px;word-break:break-all;cursor:hand' align=center>" + filename + "</td></tr></table>";
             
		var tr1 = document.createElement("td");
		tr1.innerHTML = table_start + imgstr + table_end;
		temptr.appendChild(tr1);   
	</script>
	<%
		}
		if(i == files.length-1 && (files.length%4) != 0)
		{
	%>
	<script type="text/javascript">
		var tmp_i = "<%=i%>";
		var len = "<%=(files.length%4)%>";
        var td1 = document.createElement("td");
		var wlen = 480 - (4-len)*120;
		if(tmp_i < 4)
		{
			wlen = (4-len)*120;
		}
		if(wlen > 0)
		{
			td1.width = wlen;
			td1.innerHTML = "&nbsp;";
			temptr.appendChild(td1);
		} 
	</script>
	<%
		}
		}
	%>
<%		
	}
	if(fileFlag != null && ("pic".equals(fileFlag)||fileFlag.equals("picOrFlash")))//是图片才要加水印，才有右键菜单
	{
    	request.setAttribute("opdoc",contextmenu);
%>	
    <pg:contextmenu context="opdoc" enablecontextmenu="true" scope="request"/>
<%
	}
%>
</table>
<div id="divProcessing" style="width:280px;height:50px;position:absolute;left:50px;top:150px;display:none">
        <table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
            <tr>
                <td bgcolor=#3A6EA5>
                    <marquee align="middle" behavior="alternate" scrollamount="5">
                        <font color=#FFFFFF>...文件上传中...请等待...</font>
                    </marquee>
                </td>
            </tr>
        </table>
    </div>
</body>
</html>