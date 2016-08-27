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
    String templateIds = request.getParameter("templateIds");
    templateIds = templateIds==null?"":templateIds;
%>
<html>
<head>
<title>模板导出向导</title>
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
                  导出模板
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
                            <label>模板出为:
                        </td> 
                        <td height="24" align="left">
                            <input type="radio" name="expType" value="0" checked>公共模板 
                        </td>  
                    </tr>
                    <tr>
                        <td height="24" align="left">
                            <input type="radio" name="expType" value="1">私有模板
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
     * 0:公共模板,缺省值
     * 1:私有模板
     */
    function nextStep(){
        var type = "0";
        var imptype = new Array(); 
        imptype = document.all("expType");        
        for(var i=0;i<imptype.length;i++){
        	if(imptype[i].checked){
        	    type = imptype[i].value;
        	    break;
        	}	
        }         
        var actionstr = "exportTemplate.jsp?siteId=<%=siteId%>&templateIds=<%=templateIds%>&expType="+type;
        document.importForm.action=actionstr;
        document.importForm.submit();
    }
</script>
</html>

