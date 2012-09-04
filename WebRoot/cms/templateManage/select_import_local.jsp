<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
    AccessControl control = AccessControl.getInstance();
    control.checkAccess(request, response);
    response.setHeader("Cache-Control","no-cache");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader ("Expires", -1);
    response.setDateHeader("max-age", 0);
    
    String siteId = request.getParameter("siteId");
    siteId = siteId==null?"":siteId;
%>
<html>
<head>
<title>本地模板包导入</title>
<base target="_self">
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="../inc/js/func.js"></script>
<script language="javascript">
window.resizeTo(400,350);
</script>
</head>
<body>   
<form name="importForm" enctype="multipart/form-data" method="POST">
    <table width="98%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" align="center">    
        <tr>
            <td width="4%" height="30" align="right">
                <img src="../images/ico_point.gif" width="25" height="25">
            </td>
            <td height="30" class="cms_title_blue">
                  本地模板包导入
            </td>
        </tr>
        <tr>
            <td colspan="2" style="height:9px; background-color:#3266B1"></td>
        </tr>
        <tr>
            <td colspan="2" style="height:10px; background-color:#fff"></td>
        </tr>
        <tr>
            <td colspan="2">
                <table width="100%" border="1" cellpadding="0" cellspacing="0" bordercolor="#f0f0f0">
                    <tr>
			            <td colspan="2" style="height:10px; background-color:#fff"></td>
			        </tr>
                    <tr>
                        
                        <td height="24">
                            <input hideFocus type="file" id="impfile" name="impfile" /><span class="red_star">*</span> (zip文件)
                        </td>
                    </tr>
                    
                </table>
            </td>
        </tr>
        <tr>
            <td colspan="2" style="height:20px; background-color:#fff"></td>
        </tr>
        <tr>       
            <td colspan="2" align="center">
                <input name="button" onClick="preStep()" type="button" class="cms_button" value="上一步" />&nbsp;&nbsp;
                <input name="button" onClick="nextStep()" type="button" class="cms_button" value="下一步" />
            </td>
        </tr>
    </table>
</form>
<div id=divProcessing style="width:200px;height:30px;position:absolute;left:70px;top:80px;display:none">
	<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
	    <tr>
		    <td bgcolor=#3A6EA5>
			    <marquee align="middle" behavior="alternate" scrollamount="5">
				    <font color=#FFFFFF>...处理中...请等待...</font>
				</marquee>
			</td>
		</tr>
	</table>
</div>
</body>
<iframe id="hidFrm" name="hidFrm" src="" style="display:none"></iframe>
<script>
    //add by ge.tao
    //date 2008-01-15
    //判断是否上传了文件,并且判断文件的类型必须是zip的压缩文件格式.
    function nextStep(){
        var impfile = document.all("impfile").value;
        if(impfile.length<=0){
            alert("请选择要上传的模板zip文件！");
            return false;
        }else{
            var oExtPattern   =   /.+\\.([Z][z][I][i][P][p])$/i;   
          	if(impfile.search(/\.([Z]|[z])([I]|[i])([P]|[p])$/img)<=0){
          	    alert("请选择要上传的模板zip文件！");
            	return false;
          	}   
            
        }        
		document.importForm.action = "select_import_local_second.jsp";		
		document.importForm.submit();
         
    }
    function preStep(){
        document.importForm.action = "select_import_first.jsp?siteId=<%=siteId%>";
        document.importForm.submit();
    }
</script>
</html>

