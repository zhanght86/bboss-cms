<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%
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
.STYLE4 {color: #799BFF}

-->
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>CMS 站点授权</title>
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
<a id="reload" href="site_add.jsp" style="display:none">reload...</a>
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
                  <td height="28">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 新建站点</td>
                </tr>
                <tr>
                  <td height="48" valign="bottom" style="padding-bottom:0px"><div align="right"><img src="../images/createsite1.jpg" width="223" height="19" border="0" usemap="#Map"></div></td>
                </tr>
              </table></td>
              </tr>
            <tr >
              <td>&nbsp;</td>
              <td height="20"><img src="../images/ico_point.gif" width="25" height="25"></td>
              <td height="20">&nbsp;<span class="cms_title_blue">工作流类权限</span></td>
            </tr>
            <tr>
              <td height="8" colspan="3">&nbsp;</td>
            </tr>
            <tr style="display:none">
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                父站点:
                <input  readonly name="parentsiteid" type="text" value="" class="cms_text" id="parentsiteid" />              </td>
            </tr>
            <!--来源于别的网站才有父站点-->
            <tr>
              <td width="3%">&nbsp;</td>
              <td width="3%">&nbsp;</td>
              <td width="95%" height="22">站点中文名称:
                <input name="sitename" type="text" class="cms_text" id="sitename" size="45" />
                  <span class="red_star">*</span></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22">站 点 描&nbsp; 述:
                <input name="sitedesc" type="text" class="cms_text" id="sitedesc" size="45" />
                  <span class="red_star">*</span></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22">站点存放路径:
                <input name="sitepath" type="text" class="cms_text" id="sitepath" size="30" />
                  <span class="red_star">*</span> (该处指定站点存放的英文路径名)</td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22">站点首页模板:
                <select name="hometemplateid" class="cms_select">
                    <option value="">--请选择模板--</option>
					  <option value="">模板1</option>
					  <option value="">模板2</option>
					  <option value="">模板3</option>
                </select></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22" nowrap>站点访问http:
                <input name="webhttp" type="text" class="cms_text" id="webhttp" value="http://" size="45" />
                &nbsp;</td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22" nowrap>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(如:http://www.cms.com 或者 http://ip) </td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22">&nbsp;</td>
            </tr>
            <tr>
              <td class="cms_title_bar">&nbsp;</td>
              <td height="20" class="cms_title_bar"><img src="../images/ico_point.gif" width="25" height="25"></td>
              <td height="20" class="cms_title_blue">ftp信息设置</td>
            </tr>
            <tr>
              <td></td>
              <td height="9"></td>
              <td height="9"></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22">ftp站点IP:      
                
                &nbsp;
             <input name="ftpip" type="text" class="cms_text" id="ftpip" /></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22">ftp站点端口:
                <input name="ftpport" type="text" class="cms_text"  onFocus="this.select();" value="21" size="4" maxlength="4" />
                (默认21)</td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22">ftp站点用户:
                <input name="ftpuser" type="text" class="cms_text" id="ftpuser" /></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22">ftp站点密码:
                <input name="ftppasswd" type="password" class="cms_text" id="ftppasswd" /></td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22">ftp站点路径:
                <input name="ftpdir" type="text" class="cms_text" id="ftpdir" /></td>
            </tr>
            <tr>
              <td height="8" colspan="3">&nbsp;</td>
              </tr>
            <tr>
              <td height="22" colspan="3"><div align="center">
                <input name="btn_link" type="button" class="cms_button"  onClick="javascript:ftplink(fm_site.ftpip.value,fm_site.ftpport.value,fm_site.ftpuser.value,fm_site.ftppasswd.value)" value="连接测试" />
                <input name="btn_preview"  onClick="openWin('siteftp_preview.jsp',500,550);" type="button" class="cms_button" id="btn_preview" value="预览" />
                <input name="btn_ok" type="submit" class="cms_button"  value="保存" />
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


<map name="Map"><area shape="rect" coords="1,4,80,25" href="site_right.jsp" >
<area shape="rect" coords="80,-2,81,3" href="javascript:void(0)"><area shape="rect" coords="87,3,153,23" href="site_right1.jsp">
<area shape="rect" coords="160,3,224,32" href="site_right2.jsp" >
<area shape="rect" coords="160,3,224,32" href="site_right3.jsp" >
<area shape="rect" coords="160,3,224,32" href="site_right4.jsp" >
</map></body>
</html>