<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.*"%>
<%@ page import="java.io.*" %>
<%@ page import="com.frameworkset.platform.cms.util.*"%>
<%@ page import="com.frameworkset.platform.cms.imagemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*" %>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*" %>
<%
    AccessControl control = AccessControl.getInstance();
    control.checkAccess(request, response);
    ImageManagerImpl impl = new ImageManagerImpl();
    CMSManager cmsmanager = new CMSManager();
    cmsmanager.init(request,session,response,control);
    String siteId =  cmsmanager.getSiteID();

    response.setHeader("Cache-Control", "no-cache"); 
    response.setHeader("Pragma", "no-cache"); 
    response.setDateHeader("Expires", -1);  
    response.setDateHeader("max-age", 0);
    
    String uri = request.getParameter("uri");
    
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
    pathContext = pathContext==null?"":pathContext;
    uri = uri==null?"":uri;
    
    FileFilter fileFilter = null;
    fileFilter = new ImageFileFilter();
    
    File[] files = FileUtil.getSubFiles(pathContext,CMSUtil.getPath(uri,impl.getWATERIMAGE_FORDER()),fileFilter);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
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
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
</head>
<script src="<%=request.getContextPath()%>/cms/inc/js/func.js"></script>
<script language="javascript">
function sub(){
    var backup = getCheckBoxValue(backup);
    var pattern=/\.([Jj][Pp][Gg]|[Bb][Mm][Pp]|[Gg][Ii][Ff]|[Jj][Pp][Ee][Gg])$/i;   
    var filename = document.all.form1.files.value;
    if(filename.search(/\S/)==-1){
        alert("请选择一个图片文件!");
        return;
    }
    if(!pattern.test(filename)){
        alert("这里只能上传后缀名为.gif,.bmp,.jpeg,.jpg图片!");
        return;
    }      
    document.all.form1.action="saveWebprjImage.jsp?backup="+backup;
    document.frames[0].src = "saveWebprjImage.jsp?backup="+backup;
    document.all.form1.submit();
    document.all.divProcessing.style.display = "";
}
function cancel(){
    window.close();
}
function getCheckBoxValue(name){
    var items = document.getElementsByName(name);
    for(var i=0;i<items.length;i++){                
        if(items[i].checked==true){
            flag = true;
            return items[i].value;
            //break;
        }else{
            return "";
        }
    }
}
function showwater(e){
    var flag = false;
    var items = document.getElementsByName("addwater");
    for(var i=0;i<items.length;i++){                
        if(items[i].checked==true){
            flag = true;                    
            break;
        }
    }
    if(flag){               
        document.all("water").style.display = "block";
    }else{
        document.all("water").style.display = "none";
        document.all("waterimage").value = "";
        
    }           
}
function chooseWatepic(){
    openWin("../channelManage/chooseWaterImageFrameset.jsp?fileFlag=pic",520,580);
}
//回写函数 必须
function setImage(uri){
    document.all("waterimage").value = uri;
}

</script>
<body>
<form method="post" enctype="multipart/form-data" name="form1" target="hiddenFrm">
<input type="hidden" name="uri" value="<%=uri%>">
<table border=0 cellpadding=0 cellspacing=0 width=90% align=center>
<tr>
    <td>
        <fieldset>
            <legend>图片来源</legend>
            <table border=0 cellpadding=0 cellspacing=0>
                <tr><td colspan=9 height=5></td></tr>
                <tr>
                    <td width=2></td>
                    <td width=80 align=left>
                        <label>上传:</label>
                    </td>
                    <td align=right>
                        <input type="file" name="files" width=60%>
                    </td>
                </tr>
                <tr><td width=2></td>
                    <td width=60 align=left>
                    </td><td align=left><input type="checkbox" id="coverFlag" name="coverFlag">&nbsp;覆盖重名文件</td></tr>                
            </table>
        </fieldset>
    </td>
</tr>
<tr>
    <td>
        <fieldset>
            <legend>加水印</legend>
            <table border=0 cellpadding=0 cellspacing=0>
                <tr><td colspan=9 height=5></td></tr>
                <tr><td>
                    <input type="checkbox" name="addwater" value="selected" onclick="showwater(this)">加水印   
                    <div id=water style="display:none">
                    <table border=0 cellpadding=0 cellspacing=0>
                        <tr>
                        <td width=7></td>
                        <td>
                        选择图片:
                        <!-- input type=text id="waterimage" name="waterimage" style="width:80px"  size=30 value=""-->
                        <!-- input type=button id="waterpicbutton" onclick="chooseWatepic()" value="选择" /-->
                        <select name="waterimage" style="width:180px">
                        <%                                        
                            for(int i=0;files!=null&&i<files.length;i++){
                        %>
                            <option value="<%=files[i].getName()%>"><%=files[i].getName()%></option>
                        <%    
                            }
                        %>
                        </select>
                        </td></tr>
                        <tr><td height=5></td></tr>
                        <tr>
                        <td width=7></td>
                        <td>
                        粘贴位置:
                        <select style="width:180px" name="position">
                            <option value="0" selected>居中
                            <option value="1">左上
                            <option value="2">右上
                            <option value="3">左下
                            <option value="4">右下                               
                        </select>
                        </td></tr>
                        <tr><td height=5></td></tr>
                        <tr>
                        <td width=7></td>
                        <td>
                        水印文字:
                        <input type="text" style="width:180px" name="waterStr" value="">
                        </td></tr>
                        <tr><td height=5></td></tr>
                    </table>
                    
                    <br>
                    <font color="#000000">
                    <input type="checkbox" name="backup" value="selected" checked="true">备份</font>
                    <br><br>
                    </div>                    
                </td></tr>
            </table>
        </fieldset>
    </td></tr>
    <tr>
        <td align=center>
            <input type="button" value="上传" onClick="sub()">
            <input type="button" value="取消" onClick="cancel()">
        </td>
    </tr>
</table>
</form>
    <div id="divProcessing" style="width:180px;height:30px;position:absolute;left:50px;top:150px;display:none">
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
<iframe src="" id="hiddenFrm" name="hiddenFrm" width=0 height=0 style="display:none"></iframe>
</body>
</html>
