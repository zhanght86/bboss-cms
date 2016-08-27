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
body{ 
font-size:12px;
SCROLLBAR-HIGHLIGHT-COLOR: #ffffff;
SCROLLBAR-SHADOW-COLOR: #CCCCCC; /*右边框*/
COLOR: #000000;
SCROLLBAR-3DLIGHT-COLOR: #CCCCCC; /*左边框*/
LINE-HEIGHT: 100%;
SCROLLBAR-ARROW-COLOR: #999999; /*箭头*/
SCROLLBAR-TRACK-COLOR: #F1F1F1;
SCROLLBAR-DARKSHADOW-COLOR: #fefefe;
SCROLLBAR-BASE-COLOR:  ededed;
margin-left: 0px;
margin-top:0px;
}#Layer1 {
	position:absolute;
	left:0px;
	top:0px;
	width:113px;
	height:127px;
	z-index:1;
}
.STYLE9 {color: #FF9900}

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
<a id="reload" href="doc_audity.jsp" style="display:none">reload...</a>
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
              <td height="50" colspan="3" valign="top" background="../images/top_tile_short.jpg"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="200"><img src="../images/logo.jpg" width="200" height="50"></td>
                  <td>文档审核 [所属频道:频道全名]</td>
                </tr>
              </table></td>
            </tr>
            
            
            <tr>
              <td height="18" colspan="3"><table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="27" colspan="3" class="cms_title_blue"><table width="100%" border="0">
                      <tr>
                        <td width="1%" class="cms_title_blue STYLE9">&nbsp;</td>
                        <td width="3%" class="cms_title_blue STYLE9"><div align="center"><img src="../images/audity.gif" width="18" height="17"></div></td>
                        <td width="90%" class="cms_title_blue STYLE9">文档信息</td>
                        <td width="6%"><div align="right"></div></td>
                      </tr>
                    </table></td>
                </tr>
                
                <tr>
                  <td height="23" colspan="3"><table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="mhwz_table">
                    <tr>
                      <td valign="top"><table width="100%" border="0">
                          <tr>
                            <td width="1%">&nbsp;</td>
                            <td width="98%"><table width="100%" border="0" cellpadding="0" cellspacing="0">
                              <tr>
                                <td width="8%">文档类型:</td>
                                <td width="3%"><div align="left"><img src="../images/doc_html.gif" width="20" height="20"></div></td>
                                <td width="89%"><a href="doc_gather.jsp">普通文档</a></td>
                              </tr>
                            </table></td>
                            <td width="1%">&nbsp;</td>
                          </tr>
                          <tr>
                            <td>&nbsp;</td>
                            <td>文档标题:<span class="cms_title_blue">
                            <label>
                            <input name="textfield3" type="text" class="cms_text_fordisplay" size="99">
                            </label>
                            </span></td>
                            <td>&nbsp;</td>
                          </tr>
                          <tr>
                            <td>&nbsp;</td>
                            <td><label>文档来源:</label>
                              <span class="cms_title_blue">
                              <select name="select2" class="cms_select">
                                <option value="mainpage">新浪网</option>
                                <option value="list" selected="selected">网易 163.com</option>
                                <option value="detail">中华新闻网</option>
                              </select>
                              </span></td>
                            <td>&nbsp;</td>
                          </tr>
                          <tr>
                            <td>&nbsp;</td>
                            <td><span class="black">编稿时间: <span class="cms_title_blue">
                            <input name="textfield3222" type="text" class="cms_text_fordisplay" value="2006/12/11 12:55:05" size="20">
                            </span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;作者:<span class="cms_title_blue">
                            <input name="textfield322" type="text" class="cms_text_fordisplay" size="25">
                            &nbsp;&nbsp;&nbsp;&nbsp; </span></span>录入人:<span class="black"><span class="cms_title_blue">
                             <input name="textfield3224" type="text" class="cms_text_fordisplay" size="23">
                             </span></span></td>
                            <td>&nbsp;</td>
                          </tr>
                          <tr>
                            <td>&nbsp;</td>
                            <td><table width="100%" border="0" cellspacing="0">
                              <tr>
                                <td>摘要内容:
                                  <label></label>                                  <input name="textfield32" type="text" class="cms_text_fordisplay" size="99"></td>
                                </tr>
                              <tr>
                                <td>文档内容:</td>
                              </tr>
                              <tr>
                                <td><DIV  id="dataContainer2" style="Z-INDEX:1;OVERFLOW-y:scroll;POSITION:relative;HEIGHT:450px;WIDTH:100%">
								<table width="100%" height="450"  border="0" align="center" cellpadding="0" cellspacing="0" class="mhwz_table">
                                  <tr>
                                    <td valign="top">正文内容</td>
                                  </tr>
                                </table>
								</div></td>
                              </tr>
                              <tr>
                                <td height="11"><div align="center">结论:
                                      <select name="select" class="cms_select">
                                    <option value="pass" selected="selected">审核通过</option>
                                    <option value="reback">需返工</option>
                                    <option value="nopass">审核不通过</option>
                                    </select>
                                      审核意见:<span class="cms_title_blue">
                                      <input name="textfield3223" type="text" class="cms_text_fordisplay" size="44">
                                      </span>
                                      <input name="btn_audity" type="submit" class="cms_button" id="btn_audity"  onClick="javascript:window.close();" value="确定" />
                                  <input name="btn_close" type="button" class="cms_button"  onClick="javascript:window.close();" value="关闭" />
                                </div></td>
                              </tr>
                            </table></td>
                            <td>&nbsp;</td>
                          </tr>
                        </table></td>
                    </tr>
                  </table></td>
                  </tr>
                
                
                
                <tr>
                  <td width="60%" height="23"><label>
                    &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  &nbsp;&nbsp;</label></td>
                  <td width="14%">&nbsp;</td>
                  <td width="22%" height="23"><div align="right">
                    <label></label>
                    &nbsp;&nbsp; 
                  </div></td>
                </tr>
                <tr>
                  <td height="23" colspan="3"><div align="center"></div></td>
                  </tr>
                <tr>
                  <td height="11" colspan="3">&nbsp;</td>
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