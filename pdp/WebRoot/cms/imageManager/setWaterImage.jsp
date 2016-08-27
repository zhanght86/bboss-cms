<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="java.io.*" %>
<%@ page import="com.frameworkset.platform.cms.util.*"%>
<%@ page import="com.frameworkset.platform.cms.imagemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*" %>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.imagemanager.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);	
    ImageManagerImpl impl = new ImageManagerImpl();
	String filename = request.getParameter("path");
    filename = filename==null?"":filename;
    //区别加水印 或者 重加水印
    String opt = request.getParameter("opt");
    String pathContext = request.getParameter("pathContext");
    String uri = request.getParameter("uri");

    opt = opt==null?"":opt;
    pathContext = pathContext==null?"":pathContext;
    uri = uri==null?"":uri;
    
    FileFilter fileFilter = null;
    
    fileFilter = new ImageFileFilter();

    File[] files = FileUtil.getSubFiles(pathContext,CMSUtil.getPath(uri,impl.getWATERIMAGE_FORDER()),fileFilter);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>:::::设置水印图片的选择::::::::::::::::::::::.</title>
<style type="text/css">
	.STYLE1 {color: #0000FF}
	.STYLE2 {color: #000099}
	.operStyle{
	width:17;
	height:16;
	}
.style7 {font-size: 5px}
</style>
</head>
<body bgcolor="#e7e7f7">
	<form  method="post" name="uploadform" >		
		<table width="100%" bgcolor="#e7e7f7">
			<tr>
				<td width="100%">
					<fieldset>
                        <legend>添加图片水印</legend>
                        <table border=0 cellpadding=0 cellspacing=0>
                            <tr><td colspan=9 height=5></td></tr>
                            <tr><td>                                
                                <table border=0 cellpadding=0 cellspacing=0>
                                    <tr>
                                    <td width=7></td>
                                    <td>
                                    选择水印:
                                    <!-- input type="text" name="waterimage" style="width:180px"  readonly="true" value=""-->
                                    <!--  input type=button id="waterpicbutton" onclick="chooseWatepic()" value="选择" /-->
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
                                <input type="checkbox" name="backup" value="selected" checked="true">备份
                                <br><br>
                                </font>
                            </td></tr>
                        </table>
                    </fieldset>
				</td>                
			</tr>
            <tr>
                <td align="center">
                    <input type=button value='确定' id=Ok onclick="subform()">&nbsp;&nbsp;
                    <input type=button value='取消' onclick="window.close();">
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
	<iframe name="uploadattach" src="" height="0" width="0"></iframe> 
</body>
<script src="../inc/js/func.js"></script>
<SCRIPT LANGUAGE="JavaScript">
        function chooseWatepic(){
            openWin("../channelManage/chooseWaterImageFrameset.jsp?fileFlag=pic",520,580);
        }
    	function setImage(uri){
            document.all("waterimage").value = uri;
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
		function subform(){  
            var srcimg = "<%=filename%>";   
            var opt = "<%=opt%>" ;
            var waterimage = document.all("waterimage").value;     
		    var backup = getCheckBoxValue("backup");
			if(waterimage.length<=0){
				alert("请选择水印图片！");
				return false;
			}else{
			    var reg =/.+\.([Jj][Pp][Gg]|[Bb][Mm][Pp]|[Gg][Ii][Ff]|[Jj][Pp][Ee][Gg])\b/;
				if(!reg.test(waterimage)){
					alert("提示：\n请选择一个有效的图片格式，\n支持的图片格式有（img,jpg,bmp）！");
					return false;
				}
			}
			var actionStr = "addWaterImage.jsp?srcimg="+srcimg;			
			if(backup.length>0) actionStr = actionStr + ("&backup="+backup);
            if(opt.length>0) actionStr = actionStr + ("&opt="+opt);

			uploadform.target="uploadattach"
			document.frames.uploadattach.src = actionStr;
			uploadform.action = actionStr;
            uploadform.submit();
            document.all.divProcessing.style.display = "";
        }
		
</SCRIPT>	

</html>