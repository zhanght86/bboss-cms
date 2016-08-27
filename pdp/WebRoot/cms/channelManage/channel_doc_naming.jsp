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
.STYLE5 {color: #666666}
.STYLE7 {color: #999999}

-->
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>CMS 频道管理</title>
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
<a id="reload" href="channel_doc_naming.jsp" style="display:none">reload...</a>
<!--上面的代码 使得按 F5会刷新modal页面-->
<form  name="fm_site" method="post" action="site_createdo.jsp"  onsubmit="return check_form()">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  
   
	<!--来源于别的网站才有父站点-->
    <tr >
      <td height="22" style="padding-top:3px"><table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
          <td height="2" bgcolor="D7D7D7"></td>
        </tr>
        <tr >
          <td bgcolor="D7D7D7"><table width="99%" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
            
            <!--来源于别的网站才有父站点-->
            
            <tr >
              <td height="77" colspan="4" valign="top" background="../images/toolbar_common_func_channel.jpg"><table width="96%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="32">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 文档命名规则</td>
                </tr>
                <tr>
                  <td height="48" valign="bottom" style="padding-bottom:0px"><div align="right">&nbsp; </div></td>
                </tr>
              </table></td>
              </tr>
            <tr >
              <td>&nbsp;</td>
              <td height="20"><img src="../images/ico_point.gif" width="25" height="25"></td>
              <td height="20" class="cms_title_blue">文档发布存放路径命名策略  &nbsp;&nbsp;  &nbsp;  &nbsp;&nbsp;  &nbsp;  &nbsp;&nbsp;  &nbsp;  &nbsp;&nbsp;  &nbsp;  &nbsp;  &nbsp;   &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td height="8" colspan="4">&nbsp;</td>
            </tr>
            
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="27"><label>
                <input name="method" type="radio" value="1" checked>
              </label>
              不再设立子目录</td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="27"><label>
                <input type="radio" name="method" value="2">
              </label> 
                按年分，每年一个目录 如:/2006</td>
              <td>&nbsp;</td>
            </tr>
            <!--来源于别的网站才有父站点-->
            <tr>
              <td width="1%">&nbsp;</td>
              <td width="4%">&nbsp;</td>
              <td width="91%" height="27"><label>
                <input type="radio" name="method" value="3">
              按年月分，每月一个目录 如:/2006/11
</label></td>
              <td width="4%">&nbsp;</td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="27"><label>
                <input type="radio" name="method" value="4">
按年月日分，每天一个目录 如:/2006/11/1 </label></td>
              <td>&nbsp;</td>
            </tr>
            
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="27"><label>
                <input type="radio" name="method" value="5">
按日分，每天一个目录 如:/2006-11-1 </label></td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="27"><label>
                <input type="radio" name="method" value="6">
按作者分，每个作者一个目录 如:/author </label></td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22">&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
            <tr  style="display:none">
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22">文件后缀名:<span class="STYLE5">
                <input name="sitename22" type="text" class="cms_text" id="sitename22" value=".html" size="22" />
              </span></td>
              <td>&nbsp;</td>
            </tr>
            <tr  style="display:none">
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22">命名规则:<span class="STYLE5">
               &nbsp;
               <label>
               <select name="select" class="cms_select">
                 <option value="{docid}">默认以文档编号命名</option>
				 <option value="{byuser}">用户在内容采集时手工命名</option>
               </select>
               </label>
              </span></td>
              <td>&nbsp;</td>
            </tr>
            
            
            <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td height="22"><span class="STYLE7">(页面最终发布名即:{docid}.html)</span></td>
              <td>&nbsp;</td>
            </tr>
            
            <tr>
              <td height="8" colspan="4" class="cms_title_bar"></td>
              </tr>
            

            <tr>
              <td height="22" colspan="4"><div align="center">
                <input name="btn_ok" type="submit" class="cms_button"  value="确定" />
                <input name="btn_close" type="button" class="cms_button"  onclick="javascript:window.close();" value="关闭" />
              </div></td>
              </tr>
            <tr>
              <td height="22" colspan="4">&nbsp;</td>
            </tr>
            <tr>
              <td height="11" colspan="4" background="../images/bottom_gray.jpg">&nbsp;</td>
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