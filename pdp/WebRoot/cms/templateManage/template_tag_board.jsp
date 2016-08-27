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
.STYLE6 {color: #FF6600}

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
<a id="reload" href="template_tag_board.jsp" style="display:none">reload...</a>
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
            
            <tr>
              <td width="234" valign="top"><table width="100%" border="0">
                <tr>
                  <td height="33"><table width="98%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td width="4%" height="27"><img src="../images/ico_point.gif" width="25" height="25"></td>
                          <td width="96%"><span class="cms_title_blue">模板基本信息1</span></td>
                        </tr>
                    </table></td>
                    </tr>
                <tr>
                  <td>模板文件名:
                    <label>
                      <input name="textfield3" type="text" class="cms_text" value=".htm" size="19">
                      <span class="red_star">*</span></label>&nbsp;&nbsp;&nbsp;<label></label></td>
                    </tr>
                
                <tr>
                  <td>模板类别:
                    <label>
                      <select name="select" class="cms_select">
                        <option value="mainpage">首页</option>
                        <option value="list" selected="selected">概览</option>
                        <option value="detail">细览</option>
                        </select>
                      </label></td>
                    </tr>
                <tr>
                  <td>模板描述:
                    <input name="textfield2" type="text" class="cms_text" size="22">
                    <span class="red_star">*</span></td>
                    </tr>
                
                <tr>
                  <td>模板头:</td>
                    </tr>
                <tr>
                  <td><textarea name="textarea" cols="33" rows="3" class="cms_textarea">
<% out.print("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>无标题文档</title></head><body></body>"); %></textarea></td>
                  </tr>
                <tr>
                  <td><table width="98%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td width="4%"><img src="../images/ico_point.gif" width="25" height="25"></td>
                        <td width="96%"><span class="cms_title_blue">模板置标</span></td>
                      </tr>
                    </table></td>
                  </tr>
                
                <tr>
                  <td valign="top"><table width="99%" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
                    <!--来源于别的网站才有父站点-->
                    
                    
                    <tr>
                      <td width="4%" height="22">&nbsp;</td>
                          <td width="94%"><FIELDSET>
                            <LEGEND><span class="STYLE6">普通标签</span></LEGEND>
                            <table width="99%" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
                              <!--来源于别的网站才有父站点-->
                              <tr>
                                <td width="1%"><div align="center"></div></td>
                                <td width="19%"><div align="center"></div></td>
                                <td width="5%" height="25"><div align="center"><span class="STYLE5">&nbsp;</span><img src="../images/point_tag.gif" width="21" height="15"></div></td>
                                <td width="3%">&nbsp;</td>
                                <td colspan="2">通用概览</td>
                              </tr>
                              <!--来源于别的网站才有父站点-->
                              <tr>
                                <td><div align="center"></div></td>
                                <td><div align="center"></div></td>
                                <td height="22"><div align="center">
                                  <div align="center"><span class="STYLE5">&nbsp;</span><img src="../images/point_tag.gif" width="21" height="15"></div>
                                </div></td>
                                <td>&nbsp;</td>
                                <td colspan="2">图片新闻</td>
                              </tr>
                              <tr>
                                <td><div align="center"></div></td>
                                <td><div align="center"></div></td>
                                <td height="22" nowrap><div align="center">
                                  <div align="center"><span class="STYLE5">&nbsp;</span><img src="../images/point_tag.gif" width="21" height="15"></div>
                                </div></td>
                                <td nowrap>&nbsp;</td>
                                <td colspan="2" nowrap>头条新闻</td>
                              </tr>
                              <tr>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td height="22" nowrap><div align="center">
                                  <div align="center"><span class="STYLE5">&nbsp;</span><img src="../images/point_tag.gif" width="21" height="15"></div>
                                </div></td>
                                <td nowrap>&nbsp;</td>
                                <td colspan="2" nowrap>相关新闻</td>
                              </tr>
                              <tr>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td height="22" nowrap><div align="center">
                                  <div align="center"><span class="STYLE5">&nbsp;</span><img src="../images/point_tag.gif" width="21" height="15"></div>
                                </div></td>
                                <td nowrap>&nbsp;</td>
                                <td colspan="2" nowrap>频道导航列表</td>
                              </tr>
                              <tr>
                                <td height="2" colspan="6"><hr width="80%"  size="1"></td>
                              </tr>
                              <tr>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td height="22" nowrap><div align="center">
                                  <div align="center"><span class="STYLE5">&nbsp;</span><img src="../images/point_tag.gif" width="21" height="15"></div>
                                </div></td>
                                <td nowrap>&nbsp;</td>
                                <td colspan="2" nowrap>通用细览</td>
                              </tr>
                              <tr>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td height="22" nowrap><div align="center">
                                  <div align="center"><span class="STYLE5">&nbsp;</span><img src="../images/point_tag.gif" width="21" height="15"></div>
                                </div></td>
                                <td nowrap>&nbsp;</td>
                                <td colspan="2" nowrap>发布日期</td>
                              </tr>
                              <tr>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td height="22" nowrap><div align="center">
                                  <div align="center"><span class="STYLE5">&nbsp;</span><img src="../images/point_tag.gif" width="21" height="15"></div>
                                </div></td>
                                <td nowrap>&nbsp;</td>
                                <td colspan="2" nowrap>作者</td>
                              </tr>
                              <tr>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td height="22" nowrap><div align="center">
                                  <div align="center"><span class="STYLE5">&nbsp;</span><img src="../images/point_tag.gif" width="21" height="15"></div>
                                </div></td>
                                <td nowrap>&nbsp;</td>
                                <td colspan="2" nowrap>概览图片</td>
                              </tr>
                              <tr>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td height="22" nowrap><div align="center">
                                  <div align="center"><span class="STYLE5">&nbsp;</span><img src="../images/point_tag.gif" width="21" height="15"></div>
                                </div></td>
                                <td nowrap>&nbsp;</td>
                                <td colspan="2" nowrap>记录路径</td>
                              </tr>
                              <tr>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td height="22" nowrap><div align="center">
                                  <div align="center"><span class="STYLE5">&nbsp;</span><img src="../images/point_tag.gif" width="21" height="15"></div>
                                </div></td>
                                <td nowrap>&nbsp;</td>
                                <td colspan="2" nowrap>图标位置</td>
                              </tr>
                              <tr>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td height="22" nowrap><div align="center">
                                  <div align="center"><span class="STYLE5">&nbsp;</span><img src="../images/point_tag.gif" width="21" height="15"></div>
                                </div></td>
                                <td nowrap>&nbsp;</td>
                                <td colspan="2" nowrap>上一条新闻</td>
                              </tr>
                              <tr>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td height="22" nowrap><div align="center">
                                  <div align="center"><span class="STYLE5">&nbsp;</span><img src="../images/point_tag.gif" width="21" height="15"></div>
                                </div></td>
                                <td nowrap>&nbsp;</td>
                                <td colspan="2" nowrap>下一条新闻</td>
                              </tr>
                              <tr>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td height="22" nowrap><div align="center">
                                  <div align="center"><span class="STYLE5">&nbsp;</span><img src="../images/point_tag.gif" width="21" height="15"></div>
                                </div></td>
                                <td nowrap>&nbsp;</td>
                                <td colspan="2" nowrap>当前位置</td>
                              </tr>
                              <tr>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td height="22" nowrap><div align="center">
                                  <div align="center"><span class="STYLE5">&nbsp;</span><img src="../images/point_tag.gif" width="21" height="15"></div>
                                </div></td>
                                <td nowrap>&nbsp;</td>
                                <td width="54%" nowrap>细览图片</td>
                                <td width="18%" nowrap>更多...</td>
                              </tr>
                              </table>
                          </FIELDSET></td>
                          <td width="2%" nowrap>&nbsp;</td>
                        </tr>
                    <tr>
                      <td height="22"><div align="center"></div></td>
                          <td height="22"><div align="center">
                            <FIELDSET>
                              <div align="left">
                                <LEGEND><span class="STYLE6">组件标签</span></LEGEND>
                              </div>
                              <table width="99%" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
                                <!--来源于别的网站才有父站点-->
                                <tr>
                                  <td width="1%"><div align="center"></div></td>
                                  <td width="19%"><div align="center"></div></td>
                                  <td width="5%" height="25"><div align="center"><span class="STYLE5">&nbsp;</span><img src="../images/com_tag_point.gif" width="13" height="13"></div></td>
                                  <td width="3%">&nbsp;</td>
                                  <td width="72%" colspan="2">会员登录 <img src="../images/com_tag_point.gif" width="13" height="13"> 访问计数器</td>
                                </tr>
                                <!--来源于别的网站才有父站点-->
                                <tr>
                                  <td><div align="center"></div></td>
                                  <td><div align="center"></div></td>
                                  <td height="22"><div align="center">
                                    <div align="center"><span class="STYLE5">&nbsp;</span><img src="../images/com_tag_point.gif" width="13" height="13"></div>
                                  </div></td>
                                  <td>&nbsp;</td>
                                  <td colspan="2">站内搜索 <img src="../images/com_tag_point.gif" width="13" height="13"> 网上调查</td>
                                </tr>
                                <tr>
                                  <td><div align="center"></div></td>
                                  <td><div align="center"></div></td>
                                  <td height="22" nowrap><div align="center">
                                    <div align="center"><span class="STYLE5">&nbsp;</span><img src="../images/com_tag_point.gif" width="13" height="13"></div>
                                  </div></td>
                                  <td nowrap>&nbsp;</td>
                                  <td colspan="2" nowrap>站点广告</td>
                                </tr>
                                </table>
                              </FIELDSET>                        </td>
                          <td height="22">&nbsp;</td>
                        </tr>
                    
                    </table></td>
                    </tr>
              </table></td>
            </tr>
            
            <tr>
              <td valign="top">&nbsp;</td>
            </tr>
            
            
            <!--来源于别的网站才有父站点-->
            
            <tr>
              <td background="../images/bottom_gray.jpg">&nbsp;</td>
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