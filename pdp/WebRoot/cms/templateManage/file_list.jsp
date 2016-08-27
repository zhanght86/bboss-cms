<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.*"%>
<%@ page import="com.frameworkset.platform.cms.util.*"%>
<%@ page import="com.frameworkset.platform.cms.container.*"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuTag"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenu"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.ContextMenuImpl"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.Menu"%>
<%@ page import="org.apache.oro.text.regex.PatternMatcher"%>
<%@ page import="org.apache.oro.text.regex.Perl5Matcher" %>
<%@ page import="org.apache.oro.text.regex.Pattern" %>
<%@ page import="org.apache.oro.text.regex.Perl5Compiler" %>
<%@ page import="org.apache.oro.text.regex.PatternCompiler" %>
<%@ page import="com.frameworkset.platform.cms.imagemanager.*"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<% 

	AccessControl control = AccessControl.getAccessControl();
    
    ImageManagerImpl impl = new ImageManagerImpl();
    
	String userId = control.getUserID();
	boolean isAdmin = control.isAdmin();
	String uri = request.getParameter("uri");
    uri = uri==null?"/":uri;
    uri = uri.replaceAll("\\\\","/");
    
	CMSManager cmsmanager = new CMSManager();
	cmsmanager.init(request,session,response,control);

	String siteId =  cmsmanager.getSiteID();
	String siteSecondName = cmsmanager.getCurrentSite().getSiteDir();
	String pathContext = null;
	if(siteId!=null && siteId.trim().length()!=0){
		String temp = (new SiteManagerImpl()).getSiteAbsolutePath(siteId);
		if(temp!=null && temp.trim().length()!=0){
			pathContext = new File(temp,"_template").getAbsolutePath();
		}
	}
	if(pathContext==null || pathContext.trim().length()==0){
		//session.setAttribute("pathContext",null);
%>
		<script language="javascript">
			alert("没有找到上下文路径,无法对资源进行管理.");
		</script>
<% 
		return;
	}else{
        pathContext = pathContext.replaceAll("\\\\","/");
    }	
	//设置这个属性供上传文件,新建文件等
	//如果不设置这个属性,要传递参数;难得统一管理
	//session.setAttribute("pathContext",pathContext);	
	
%>
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
a:link {
	color: #000000;
	text-decoration: none;
}
a:visited {
	text-decoration: none;
	color: #000000;
}
a:hover {
	text-decoration: none;
	color: #000000;
}
a:active {
	text-decoration: none;
	color: #000000;
}
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	scrollbar-face-color: #C9D1E4; 
    scrollbar-shadow-color: #6B74B7; 
    scrollbar-highlight-color: white; 
    scrollbar-3dlight-color: #E100E1; 
    scrollbar-darkshadow-color:#E100E1; 
    scrollbar-arrow-color:#003492; 
    scrollbar-base-color: #E100E1; 
    scrollbar-track-color: #E9EDF3;
}
.list {
	font-family: Verdana, "宋体";
	font-size: 12px;
	font-weight: lighter;
}
-->
</style>
<script type="text/javascript" src="js/fileList.js"></script>
<script src="../inc/js/func.js"></script>
<script type="text/javascript">
var theURI = "<%=uri==null?"":uri%>";
var siteId = '<%=siteId%>';

//虽然toolbar.jsp中也接受uri这个参数，但是考虑刷新了这个页面
//而没有刷新那个页面的这种情况
//所有的uri都应该以这边的theURI为准
if(parent.toolFrm.uri!=null){
	parent.toolFrm.uri = theURI;
}

function setAsTemplate(name){
	myOpen("setAsTemplate.jsp?name="+name+"&uri="+theURI+"&pathContext=<%=pathContext%>");
}
function cancelATemplate(templateId){
	if(window.confirm("你确实要取消这个模板?")){
		myOpen("cancelATemplate_do.jsp?siteId="+siteId+"&templateId="+templateId);
	}
}

function editTemplate(templateId){
	myOpen("editTemplate_Info.jsp?templateId="+templateId);
}

function editFileContent(fileName){
	var url = "editFileContent.jsp?fileName="+fileName+"&uri="+theURI+"&pathContext=<%=pathContext%>";
	var width = screen.availWidth;
	var height = screen.availHeight;
	var f = "dialogWidth:"+width+"px;dialogHeight:"+height+"px;center:yes;resizable:no;scroll:auto";
	window.showModalDialog(url,window,f);

}

function exportTemplate(templateId){
	hidFrm.location.href = "exportTemplate.jsp?siteId="+siteId+"&templateId="+templateId;
}

function checkAll(totalCheck,checkName){	//复选框全部选中
   var selectAll = document.getElementsByName(totalCheck);
   var o = document.getElementsByName(checkName);
   if(selectAll[0].checked==true){
	   for (var i=0; i<o.length; i++){
      	  if(!o[i].disabled){
      	  	o[i].checked=true;
      	  }
	   }
   }else{
	   for (var i=0; i<o.length; i++){
   	  	  o[i].checked=false;
   	   }
   }
}
function checkoutFile(filename){
	var u = theURI.replace("\\", "/").replace(/^\s+$/img,"");
	
	if(u.charAt(u.length - 1)!="/"){
		u = u+"/";
	}
	if(u=="/"){
		u = "";
	}
	//alert("checkOutInFile.jsp?uri="+(u+filename)+"&siteId="+siteId+"&action=checkout");
	hidFrm.location.href = "checkOutInFile.jsp?uri="+(u+filename)+"&siteId="+siteId+"&action=checkout";
}

function checkinFile(filename){
	var u = theURI.replace("\\", "/").replace(/^\s+$/img,"");
	if(u.charAt(u.length - 1)!="/"){
		u = u+"/";
	}
	if(u=="/"){
		u = "";
	}
	//alert("checkOutInFile.jsp?uri="+(u+filename)+"&siteId="+siteId);
	hidFrm.location.href = "checkOutInFile.jsp?uri="+(u+filename)+"&siteId="+siteId+"&action=checkin";
}

function deleteFile(checkboxId){
	parent.toolFrm.deleteFiles(checkboxId);
}

function reName(currFileName){
var url = "reName.jsp?uri="+theURI+"&tempuri="+currFileName;
	myOpen(url);
}
function openview(filename,uri,siteId,isfile)
{
   if(isfile)
   {
   pubPreviewFile(filename,'hidFrm',uri,siteId);
   }
}
function seeTemplateCited(templateId,siteId,templateType,indexPagePath){
	var url = "see_template_cited.jsp?siteId="+siteId+"&templateId="+templateId+"&templateType="+templateType+"&indexPagePath="+indexPagePath;
	openWin(url,600,500);
}
/**
* 
* filename 图片名称
* 对选中图片加水印.
* @author ge.tao
* @date 2007-7-6
*/
function addWaterImage(filename){
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
    var path = "/cms/siteResource/<%=siteSecondName%>/_template/"+ url + filename;
    url = "../imageManager/setWaterImage.jsp?path="+path+"&pathContext=<%=pathContext%>&uri="+uri;
    var returnVal;
    returnVal = openWin(url,swidth,sheight);
    if(returnVal=="True"){
        var param = window.location.search;
        window.location.href="file_list.jsp"+param; 
    }
    
}
/**
* filename 图片名称
* 对选中图片恢复至原始图片.
* 实现:用备份的原始图片替换被加水印的图片
* @author ge.tao
* @date 2007-7-6
*/
function recoverWaterImage(filename){
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
        var param = window.location.search;
        window.location.href="file_list.jsp"+param; 
    }
}
/**
* filename 图片名称
* 对选中图片重加水印. 原来的水印抹掉 
* 实现:先恢复至原始图片 然后加水印
* @author ge.tao
* @date 2007-7-6
*/
function reAddWaterImage(filename){
    //选中 高亮显示
    //high(ob,filename);
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
    var path="/cms/siteResource/<%=siteSecondName%>/_template/"+ url + filename;    
    url = "../imageManager/setWaterImage.jsp?path="+path+"&opt=readd&pathContext=<%=pathContext%>&uri="+uri;
    var returnVal;
    returnVal = openWin(url,swidth,sheight);
    if(returnVal=="True"){
        var param = window.location.search;
        window.location.href="file_list.jsp"+param; 
    }
}
/**
* 
* 实现:打开模态窗口
* @author ge.tao
* @date 2007-7-6
*/
function openWin(url,swidth,sheight){
     var w = showModalDialog(url,window,"dialogWidth:"+swidth+"px;dialogHeight:"+sheight+"px;help:no;scroll:auto;status:no");
     return w;
}
function editByDreamweaver(url,siteId,uri,fileName) 
{
   var templateUrl=url.substring(0,url.lastIndexOf("/cms/"))+"/cms/siteResource/<%=siteSecondName%>/_template/" + uri + "/" +fileName;
   try
   {
   	 form1.dwEditObject.DreamweaverEditByUrl(templateUrl);
   }
   catch(e)
   {
     alert(e.message);
   }
}
</script>
</head>
<body>
<form id="form1" name="form1">
<table width="100%" border="0" cellspacing="0" cellpadding="3" class="Datalisttable">
<input type="hidden" name="pathContext" id="pathContext" value="<%=pathContext %>">
	<tr class="cms_report_tr">
		<td height="30" width="20px" class="list">
<!-- 		<object id="dwEditObject" classid="clsid:E7566055-CDA0-4a52-B649-20B42C45677A" codebase="../../InstallFile/DWActiveX.CAB#Version=1,0,0,0"></object> -->
			<input type="checkBox"  class="checkbox" name="checkBoxAll" onClick="checkAll('checkBoxAll','multiFiles')">	  </td>
		<td width="18px" class="list">&nbsp;
			
	  </td>
		<td width="18px" class="list">
			<img src="images/lock.gif">
		</td>		
		<td width="120px" class="list">
			文件名
		</td>
		<td class="list" width="80px">
			模板名称
		</td>
		<td class="list" width="50px">
			类型
		</td>
		<td class="list" width="60px">
			创建人
		</td>
		<td class="list"  width="80px">
			模板描述
		</td>
		<td class="list" width="40px">检出人</td>
		<td class="list" width="120px">检出时间</td>
	</tr>

<%
	if(uri!=null && uri.trim().length()!=0 && !uri.equals("/")){
%>
	<tr  onMouseOver="this.style.background='#E9EDF3'" onmouseenter="onMsEnter(this)" onMouseOut="onMsOut(this)" onClick="goBack()" onselectstart="return false" class="cms_data_tr">
		<td height="25" width="20" class="tablecells">&nbsp;
			
	  </td>
		<td height="25" width="18px" class="tablecells">&nbsp;
			
	  </td>
		<td width="18px" class="list">&nbsp;
			
	  </td>
		<td class="tablecells" colspan="8">返回上一级目录</td>
	</tr>
<%
	}
	FileFilter fileFilter = new ResourceFilter();
	java.util.Set fileresources = (new FileManagerImpl()).getDirectoryAndFileResource(siteId,uri,fileFilter);
	ContextMenu contextmenu = new ContextMenuImpl();
	java.util.Iterator it = fileresources.iterator();
	java.text.SimpleDateFormat smf = new java.text.SimpleDateFormat("yyyy-MM-dd H:mm:ss");
	while(it.hasNext()){
		FileResource fr = (FileResource)it.next();
		
		String theId = "T"+((int)(Math.random()*100000));
%>
	<tr onMouseOver="this.style.background='#E9EDF3'" <%=fr.isLock()?"style='color:#FF0000'":"" %> onmouseenter="onMsEnter(this)" onMouseOut="this.style.background='#FFFFFF'" onDblClick="onDBClick(this,'<%=fr.getName()%>',<%=fr.isDirectory()%>)" onselectstart="return false" class="cms_data_tr">

	
		<td  height="25" width="20" class="tablecells">
			<input name="multiFiles" class="checkbox" type="checkbox"  id="checkbox<%=theId%>" value="<%=fr.getName()%>">
		</td>
		<td height="25" width="18px" class="tablecells">
			<img src="images/<%=fr.getType()%>.gif">
		</td>
		<td width="18px" class="list">
			<%if(fr.isLock()){%>
			<img src="images/lock.gif">
			<%}else{%>
			&nbsp;
			<%}%>
		</td>
		<td width="120px" class="tablecells" id="<%=theId%>" onClick="openview('<%=fr.getName()%>','<%=uri%>','<%=siteId%>',<%=!fr.isDirectory()%>)"><%=fr.getName()%></td>
			<%
			if(fr.isTemplate()){
			Template t = fr.getTemplate();
			%>
			
			<td  height="25" width="80px" class="tablecells"><%=t.getName()%></td>
			<td  height="25" width="50px" class="tablecells">
			<%if(t.getType()==0)
				out.print("首页模板");
			if(t.getType()==1)
				out.print("概览模板");
			if(t.getType()==2)
				out.print("细览模板");
			if(t.getType()==3)
				out.print("评论模板");
			%></td>
			<td  height="25" width="60px" class="tablecells"><%=t.getCreateUserName()%></td>
			<td  height="25" width="180px" class="tablecells"><%=t.getDescription()%></td>								
			<%	
			}else out.print("<td width=\"80px\" >&nbsp;</td><td width=\"50px\" >&nbsp;</td><td width=\"60px\" >&nbsp;</td><td width=\"180px\">&nbsp;</td>");

			Menu menu = new Menu();
			menu.setIdentity(theId);
                       
			if(!fr.isLock()){
				Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
				menuitem.setName("锁定");
				menuitem.setLink("javascript:checkoutFile('"+fr.getName()+"')");
				menuitem.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/tpl_lock.gif");
				menu.addContextMenuItem(menuitem);
				
				//没有被锁定的话，都可以删除，被锁定了的话，只有被锁定的用户才能删除
				Menu.ContextMenuItem menuitem2 = new Menu.ContextMenuItem();
				menuitem2.setName("删除");
				menuitem2.setLink("javascript:deleteFile('checkbox"+theId+"')");
				menuitem2.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/tpl_del.gif");
				menu.addContextMenuItem(menuitem2);					

				//没有被锁定的话，都可以改名字；锁定了的话，只有被锁定的用户才能改名字
				Menu.ContextMenuItem menuitem3 = new Menu.ContextMenuItem();
				menuitem3.setName("重命名");
				menuitem3.setLink("javascript:reName('"+fr.getName()+"')");
				//menuitem3.setLink("javascript:reName('"+fr.getName()+"','"+fr.getTemplateId()+"')");
				menuitem3.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/tpl_rename.gif");
				menu.addContextMenuItem(menuitem3);					
			}else{
                /* 判断图片格式 加水印图片*/
                String fileName = fr.getName();
                String extType = "";                      
                if(fileName.lastIndexOf(".")>-1){            
                    extType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());            
                    String regStr = "[Gg][Ii][Ff]|[Jj][Pp][Gg]|[Bb][Mm][Pp]|[Jj][Pp][Ee][Gg]";
                    PatternCompiler compiler = new Perl5Compiler();
                    Pattern pattern = compiler.compile(regStr); 
                    PatternMatcher pm = new Perl5Matcher();  
                    if(pm.matches(extType,pattern)){
                        String path = CMSUtil.getPath(CMSUtil.getPath(pathContext,uri),fileName);
                        path = path.replaceAll("\\\\","/");
                        path = path.replaceAll("//","/");
                        int flag = impl.isAddedWaterImage(path.toLowerCase());
                        if(flag == 0){
                        //tupian
                            Menu.ContextMenuItem water = new Menu.ContextMenuItem();
                            water.setName("加水印");
                            water.setLink("javascript:addWaterImage('"+fr.getName()+"')");
                            water.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/tpl_lock.gif");
                            menu.addContextMenuItem(water);
                        }else if(flag == 1){
                        //已加水印 无备份文件
                        }else{
                            //已加水印 有备份文件
                            Menu.ContextMenuItem menuitem2 = new Menu.ContextMenuItem();
                            menuitem2.setName("恢复原始图片");
                            menuitem2.setLink("javascript:recoverWaterImage('" + fr.getName() + "')");
                            menuitem2.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_fbyl.gif");
                            menu.addContextMenuItem(menuitem2);
                            
                            Menu.ContextMenuItem menuitem3 = new Menu.ContextMenuItem();
                            menuitem3.setName("重加水印");
                            menuitem3.setLink("javascript:reAddWaterImage('" + fr.getName() + "')");
                            menuitem3.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_fbyl.gif");
                            menu.addContextMenuItem(menuitem3);
                        }
                    }
                } 
            
				if(userId.equals(fr.getCheckoutUser())){
					Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
					menuitem.setName("解锁");
					menuitem.setLink("javascript:checkinFile('"+fr.getName()+"')");
					menuitem.setIcon(request.getContextPath() +"/sysmanager/images/actions.gif");
					menu.addContextMenuItem(menuitem);					
				}else if(isAdmin){
					//管理员对文件的强制解锁,服务器端的处理还没有实现
					//Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
					//menuitem.setName("强制解锁");
					//menuitem.setLink("javascript:checkinFile('"+fr.getName()+"')");
					//menuitem.setIcon(request.getContextPath() +"/sysmanager/images/actions.gif");
					//menu.addContextMenuItem(menuitem);					
				}
				if(fr.canEdit() && userId.equals(fr.getCheckoutUser())){
					Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
					menuitem.setName("编辑文件内容");
					menuitem.setLink("javascript:editFileContent('"+fr.getName()+"')");
					menuitem.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_edit.gif");
					menu.addContextMenuItem(menuitem);	
					Menu.ContextMenuItem menuitem1 = new Menu.ContextMenuItem();
					menuitem1.setName("在Dreamweaver中编辑文件");
					menuitem1.setLink("javascript:editByDreamweaver('"+request.getRequestURL()+"','"+siteId+"','"+uri+"','"+fr.getName()+"')");
					menuitem1.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_editbydw.gif");
					menu.addContextMenuItem(menuitem1);									
				}
				
				//没有被锁定的话，都可以删除，被锁定了的话，只有被锁定的用户才能删除
				if(userId.equals(fr.getCheckoutUser())){
					Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
					menuitem.setName("删除");
					menuitem.setLink("javascript:deleteFile('checkbox"+theId+"')");
					menuitem.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/tpl_del.gif");
					menu.addContextMenuItem(menuitem);					
				}
				
				
				//没有被锁定的话，都可以改名字；锁定了的话，只有被锁定的用户才能改名字
				if(userId.equals(fr.getCheckoutUser())){
					Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
					menuitem.setName("重命名");
					menuitem.setLink("javascript:reName('"+fr.getName()+"')");
					//menuitem.setLink("javascript:reName('"+fr.getName()+"','"+fr.getTemplateId()+"')");
					menuitem.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/tpl_rename.gif");
					menu.addContextMenuItem(menuitem);					
				}				
				if(userId.equals(fr.getCheckoutUser()) && fr.canbeTemplate() && !fr.isTemplate()){
					Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
					menuitem.setName("设置为模板");
					menuitem.setLink("javascript:setAsTemplate('"+fr.getName()+"')");
					menuitem.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/chl_tpl.gif");
					menu.addContextMenuItem(menuitem);				
				}
				if(userId.equals(fr.getCheckoutUser()) && fr.isTemplate()){
					Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
					menuitem.setName("取消模板");
					menuitem.setLink("javascript:cancelATemplate('"+fr.getTemplateId()+"')");
					menuitem.setIcon(request.getContextPath() +"/sysmanager/images/actions.gif");
					menu.addContextMenuItem(menuitem);	
					
					
					Menu.ContextMenuItem menuitem2 = new Menu.ContextMenuItem();
					menuitem2.setName("修改模板属性");
					menuitem2.setLink("javascript:editTemplate('"+fr.getTemplateId()+"')");
					menuitem2.setIcon(request.getContextPath() +"/sysmanager/images/actions.gif");
					menu.addContextMenuItem(menuitem2);														
				}

			}
			if(fr.isTemplate()){
				//不管有没有被锁定，都可以导出
				Menu.ContextMenuItem menuitem3 = new Menu.ContextMenuItem();
				menuitem3.setName("导出");
				menuitem3.setLink("javascript:exportTemplate('"+fr.getTemplateId()+"')");
				menuitem3.setIcon(request.getContextPath() +"/sysmanager/images/actions.gif");
				menu.addContextMenuItem(menuitem3);	
			}
			if(!fr.isDirectory()){
				//预览键
				Menu.ContextMenuItem menuitemPre = new Menu.ContextMenuItem();
				menuitemPre.setName("预览");
				menuitemPre.setLink("javascript:pubPreviewFile('"+fr.getName()+"',hidFrm,'"+uri+"','"+siteId+"')");
				menuitemPre.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/tpl_view.gif");
				menu.addContextMenuItem(menuitemPre);	
			}
			if(!fr.isTemplate() && !fr.isDirectory()){
				//发布键
				Menu.ContextMenuItem menuitemPub = new Menu.ContextMenuItem();
				menuitemPub.setName("发布");
				menuitemPub.setLink("javascript:publishFile('"+fr.getName()+"',hidFrm,'"+uri+"','"+siteId+"')");
				menuitemPub.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/tpl_publish.gif");
				menu.addContextMenuItem(menuitemPub);
			}	
			if(fr.getName().endsWith("jsp") || fr.getName().endsWith("html") || fr.getName().endsWith("htm")){
				int templateId = -1;
				int templateType = -1;
				String  indexPagePath = "";
				if(fr.isTemplate()){
					Template t = fr.getTemplate();
					templateId = t.getTemplateId();
					templateType = t.getType();
				} else {
					indexPagePath = uri + (uri.length()>0?("/" + fr.getName()):fr.getName());
				}
				Menu.ContextMenuItem menuitem9 = new Menu.ContextMenuItem();
				menuitem9.setName("引用查看");
				menuitem9.setLink("javascript:seeTemplateCited('" + templateId + "','" + siteId + "','" + templateType + "','" + indexPagePath + "')");
				menuitem9.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/tpl_edit.gif");
				menu.addContextMenuItem(menuitem9);
				
			}
			contextmenu.addContextMenu(menu);
				
		%>
		<td><%=fr.getCheckoutUserName()==null?"&nbsp;":fr.getCheckoutUserName()%></td>
		<td><%=fr.getCheckoutTime()==null?"&nbsp;":""+smf.format(fr.getCheckoutTime())%></td>
		
	</tr>
<%
	}
%>

<%request.setAttribute("opdoc",contextmenu);%>
	<pg:contextmenu context="opdoc" enablecontextmenu="true" scope="request"/>

</table>
</form>
<iframe id="hidFrm" name="hidFrm" style="display:none"></iframe>
</body>
</html>