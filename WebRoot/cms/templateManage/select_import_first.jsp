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
<title>模板导入</title>
<base target="_self">
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="../inc/js/func.js"></script>
<script language="javascript">
//window.dialogTop = "px"; 
//window.dialogLeft = "px"; 
window.dialogHeight = "300px"; 
window.dialogWidth = "400px";
</script>
</head>
<base target="_self">
<body>   

<form name="importForm" method="post">
    <table width="98%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" align="center">    
        <tr>
            <td width="4%" height="30" align="right">
                <img src="../images/ico_point.gif" width="25" height="25">
            </td>
            <td height="30" class="cms_title_blue">
                  导入模板
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
                <table width="100%" align="center" border="1"  bordercolor="#f0f0f0">
                    <tr>                        
                        <td rowspan=2 align="center" width="30%">
                            <label>选择模板源:
                        </td> 
                        <td height="24" align="left">
                            <input type="radio" name="impType" value="localImp" checked>从本地导入 
                        </td>  
                    </tr>
                    <tr>
                        <td height="24" align="left">
                            <input type="radio" name="impType" value="remoteImp">从服务器导入
                        </td>
                    </tr>
                    
                </table>
            </td>
        </tr>
        
        <tr>       
            <td colspan=2 height="54" align="center">
                <input name="button" onClick="nextStep()" type="button" class="cms_button" value="下一步" />
            </td>
        </tr>
    </table>
</form>

</body>
<iframe id="hidFrm" name="hidFrm" src="" style="display:none"></iframe>
<script>
    /**
     * type两个值:
     * localImp:从本地导入,缺省值
     * remoteImp:从服务器导入
     */
    function nextStep(){
        var actionstr = "select_import_local.jsp?siteId=<%=siteId%>";
        var type = "localImp";
        var imptype = new Array(); 
        imptype = document.all("impType");        
        for(var i=0;i<imptype.length;i++){
        	if(imptype[i].checked){
        	    type = imptype[i].value;
        	    break;
        	}	
        }        
        //window.returnValue = type;
        //window.close();
        if(type!="localImp"){
            actionstr = "select_import_remote.jsp?siteId=<%=siteId%>";
        }
        document.importForm.action=actionstr;
        document.importForm.submit();
    }
</script>
</html>

