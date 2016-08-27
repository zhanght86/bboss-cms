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
.STYLE5 {color: #0000FF}

-->
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>CMS 模板管理</title>
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
<a id="reload" href="template_tag_wizard.jsp" style="display:none">reload...</a>
<!--上面的代码 使得按 F5会刷新modal页面-->
<form action="../siteManage/site_createdo.jsp" method="post" enctype="multipart/form-data"  name="fm_site"  onsubmit="return check_form()">
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
              <td width="100%" height="77" colspan="3" valign="top" background="../images/toolbar_common_func_template.jpg"><table width="96%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="28">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 添加CMS置标向导</td>
                </tr>
                <tr>
                  <td height="48" valign="bottom" style="padding-bottom:0px"><div align="right"></div></td>
                </tr>
              </table></td>
              </tr>
            
            <tr>
              <td height="8" colspan="3"></td>
            </tr>
            
            
            <!--来源于别的网站才有父站点-->
            
            
            <tr>
              <td height="25" colspan="3" valign="top"><table width="98%" border="0">
                  
                  <tr>
                    <td width="47%"><div align="center"></div>
                      <label>
                      <div align="center">选择置标名称<span class="red_star">*</span></div>
                      </label>
                      <div align="center">&nbsp;</div></td>
                    <td><select name="select" class="cms_select" multiple="multiple" style="height:160">
                      <option value="mainpage" selected="selected">首页</option>
                      <option value="list" >通用概览</option>
                      <option value="detail">图片新闻</option>
                      <option value="detail">头条新闻</option>
                      <option value="detail">相关新闻</option>
                      <option value="detail">频道导航列表</option>
                      <option value="detail">通用细览</option>
                      <option value="detail">发布日期</option>
                      <option value="detail">作者</option>
                      <option value="detail">概览图片</option>
                      <option value="detail">记录路径</option>
                      <option value="detail">图标位置</option>
                      <option value="detail">上一条新闻</option>
                      <option value="detail">下一条新闻</option>
                      <option value="detail">当前位置</option>
                    </select></td>
                    </tr>
                </table>
                <label></label></td>
              </tr>
            <tr>
              <td height="30" colspan="3"><div align="center">
                <a href="template_tag_wizard_step1.jsp"><u><font color="#0000FF" style="font-weight:bold; cursor:hand">下一步</font></u></a>  &nbsp;<a onClick="javascript:window.close();"><u><font color="#0000FF" style="font-weight:bold; cursor:hand">关闭</font></u></a>
              </div>                </td>
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