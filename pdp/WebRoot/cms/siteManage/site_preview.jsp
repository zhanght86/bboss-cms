<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.common.poolman.DBUtil"%>
<%
	AccessControl control = AccessControl.getInstance();
    //页面保护
    control.checkAccess(request,response);//
    response.setHeader("Cache-Control","no-cache"); //HTTP 1.1 
	response.setHeader("Pragma","no-cache"); //HTTP 1.0 
	response.setDateHeader ("Expires", -1);
	//prevents caching at the proxy server
	response.setDateHeader("max-age", 0); 
%>
<html>
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="../inc/js/func.js"></script>
<style type="text/css">
<!--
.STYLE5 {
	font-size: 14px;
	color: #0000FF;
}

-->
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>CMS 站点管理</title>
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

 //ftp连接检测
  function ftplink(ftpip,ftpport,ftpuser,ftppasswd){
        var ret=false;
		
		if(fm_site.ftpip.value==""){
	     alert("您还没输入ftp地址呢!");
		 fm_site.ftpip.focus();
		ret=false;
	    }
		if(fm_site.ftpport.value==""){
	     alert("您还没输入ftp端口呢!");
		 fm_site.ftpport.focus();
		ret=false;
	    }
		if(fm_site.ftpuser.value==""){
	     alert("您还没输入ftp用户呢!");
		 fm_site.ftpuser.focus();
		 ret=false;
	    }
		if(fm_site.ftppasswd.value==""){
	     alert("您还没输入ftp密码呢!");
		 fm_site.ftppasswd.focus();
		 ret=false;
	    }
		
		if(fm_site.ftpip.value!=null&&fm_site.ftpport.value!=null&&fm_site.ftpuser.value!=null&&fm_site.ftppasswd.value!=null){
		   window.open("site_ftplink.jsp?ftpip="+ftpip+"&ftpport="+ftpport+"&ftpuser="+ftpuser+"&ftppasswd="+ftppasswd);  
         }
  
  }
</script>
<!--下面的代码 使得按 F5会刷新modal页面-->
<base target="_self">
<body  bottommargin="0"  background="../images/grayline_bg.jpg" onkeydown="if(event.keyCode==116){reload.click()}" rightmargin="0" leftmargin="0" topmargin="0">
<a id="reload" href="siteftp_preview.jsp" style="display:none">reload...</a>
<!--上面的代码 使得按 F5会刷新modal页面-->
<form  name="fm_site" method="post" action="site_createdo.jsp"  onsubmit="return check_form()">
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
              <td height="77" colspan="3" valign="top" background="../images/toolbar_common_func_site.jpg"><table width="96%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="28">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 站点预览</td>
                </tr>
                <tr>
                  <td height="48" valign="bottom" style="padding-bottom:0px"><div align="right"></div></td>
                </tr>
              </table></td>
              </tr>
            <tr >
              <td>&nbsp;</td>
              <td height="20"><img src="../images/ico_point.gif" width="25" height="25"></td>
              <td height="20">&nbsp;<span class="cms_title_blue">站点预览</span></td>
            </tr>
            <tr>
              <td height="8" colspan="3">&nbsp;</td>
            </tr>
            <tr style="display:none">
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
            </tr>
            <!--来源于别的网站才有父站点-->
            <tr>
              <td width="3%">&nbsp;</td>
              <td width="3%">&nbsp;</td>
              <td width="95%" height="22">&nbsp;</td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22">&nbsp;</td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22">&nbsp;</td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22"><span class="STYLE5">此处解析出站点的首页页面！</span></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22" nowrap>&nbsp;</td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22" nowrap>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
            </tr>
            
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22">&nbsp;</td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22">&nbsp;</td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22">&nbsp;</td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22">&nbsp;</td>
            </tr>
            <tr>
              <td height="8" colspan="3">&nbsp;</td>
              </tr>
            <tr>
              <td height="22" colspan="3"><div align="center">
                <input name="btn_close" type="button" class="cms_button"  onclick="javascript:window.close();" value="关闭" />
              </div></td>
              </tr>
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