<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1 
	response.setHeader("Pragma","no-cache"); //HTTP 1.0 
	response.setDateHeader ("Expires", -1);
	//prevents caching at the proxy server
	response.setDateHeader("max-age", 0); 
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="../inc/js/func.js"></script>
<style type="text/css">
<!--
.STYLE7 {color:#003398; font-size: 12px;}
#Layer1 {
	position:absolute;
	left:0px;
	top:0px;
	width:113px;
	height:127px;
	z-index:1;
}
.STYLE8 {color: #0000FF}

-->
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>CMS 文档管理</title>
</head>
<script language="javascript">
//站点窗口输入参数校验
function check_form(){
	
	if(fm_site.sitename.value==""){
		alert("请输入站点中文名称!");
		fm_site.sitename.focus();
		return false
	}
    if(fm_site.sitedesc.value==""){
		alert("请输入站点描述!");
		fm_site.sitedesc.focus();
		return false
	}
   if(fm_site.sitepath.value==""){
		alert("请输入站点存放路径!");
		fm_site.sitepath.focus();
		return false
	}
	
	 // if(fm_site.hometemplateid.value==""){
	//	alert("请选择站点首页模板!");
	//	fm_site.hometemplateid.focus();
	//	return false
	 //}
	return true
}

</script>
<!--下面的代码 使得按 F5会刷新modal页面-->
<base target="_self">
<body  bottommargin="0"  background="../images/grayline_bg.jpg" onkeydown="if(event.keyCode==116){reload.click()}" rightmargin="0" leftmargin="0" topmargin="0">
<a id="reload" href="doc_gather.jsp" style="display:none">reload...</a>
<!--上面的代码 使得按 F5会刷新modal页面-->
<form action="" method="post" enctype="multipart/form-data"  name="myform"  onsubmit="return check_form()">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  
   
	<!--来源于别的网站才有父站点-->
    <tr >
      <td height="22" style="padding-top:3px"><table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
          <td height="2" bgcolor="D7D7D7"></td>
        </tr>
        <tr>
          <td bgcolor="D7D7D7"><table width="99%" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
            
            <!--来源于别的网站才有父站点-->
            
            <tr >
              <td height="47" colspan="3" valign="top" background="../images/toolbar_common_func_template.jpg"><table width="96%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="28">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 新增文档 [所属频道:频道全名]</td>
                </tr>
                <tr>
                  <td valign="middle" style="padding-bottom:0px">&nbsp;</td>
                </tr>
              </table></td>
              </tr>
            
            
            <tr>
              <td height="18" colspan="3"><table width="98%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td width="4%" height="27">&nbsp;</td>
                  <td colspan="2" class="cms_title_blue">&nbsp;</td>
                </tr>
                <tr>
                  <td height="23">&nbsp;</td>
                  <td height="23" colspan="2">&nbsp;</td>
                </tr>
                <tr>
                  <td height="23">&nbsp;</td>
                  <td height="23" colspan="2">&nbsp;</td>
                </tr>
                <tr>
                  <td height="23">&nbsp;</td>
                  <td height="23" colspan="2">&nbsp;</td>
                </tr>
                
                <tr>
                  <td height="23">&nbsp;</td>
                  <td height="23" colspan="2">&nbsp;</td>
                </tr>
                
                <tr>
                  <td height="23">&nbsp;</td>
                  <td width="51%" height="23">&nbsp;</td>
                  <td width="45%" height="23">&nbsp;</td>
                </tr>
                <tr>
                  <td height="23">&nbsp;</td>
                  <td height="333">&nbsp;</td>
                  <td height="23">&nbsp;</td>
                </tr>
              </table></td>
              </tr>
            
            <tr>
              <td  width="766" height="22" colspan="3"  align="center" valign="top">&nbsp;</td>
            </tr>
            
            <tr>
              <td height="22" colspan="3"><table width="98%" border="0">
                
                <tr>
                  <td width="11%">&nbsp;</td>
                  <td width="89%">&nbsp;</td>
                </tr>
              </table></td>
              </tr>
            
            
            <!--来源于别的网站才有父站点-->
            
            <tr>
              <td height="11" colspan="3" background="../images/bottom_gray.jpg">&nbsp;</td>
              </tr>
          </table></td>
        </tr>
        <tr>
          <td height="3" bgcolor="D7D7D7"></td>
        </tr>
      </table></td>
    </tr>
    
    
  <!--来源于别的网站才有父站点-->
</table>   
</form>

</body>
</html>