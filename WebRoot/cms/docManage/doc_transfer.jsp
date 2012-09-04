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
<a id="reload" href="doc_transfer.jsp" style="display:none">reload...</a>
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
                  <td height="28">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 文档分发</td>
                </tr>
                <tr>
                  <td valign="middle" style="padding-bottom:0px">&nbsp;</td>
                </tr>
              </table></td>
              </tr>
            
            
            <tr>
              <td  width="766" colspan="3"  align="center" valign="top"><table width="100%" border="0" align="center">
                <tr class="cms_report_tr">
                  <td width="31%" height="19" ><div   align="center" style="cursor:hand">网站频道</div></td>
                  </tr>
                <tr class="cms_data_tr">
                  <td height="430" valign="top"><div align="left">
                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td height="3" colspan="2"></td>
                      </tr>
                      <tr>
                        <td height="22" colspan="2"><div align="center">请选择要分发的栏目<img src="../images/tan.gif" width="11" height="11"></div></td>
                      </tr>
                      <tr>
                        <td height="3" colspan="2"></td>
                      </tr>
                      <tr>
                        <td width="16%" height="18"><div align="right"><img src="../images/ico_site.gif" width="17" height="16" /></div></td>
                        <td width="84%" style="padding-top:6px"><div ><strong>中南大学信息港</strong> </div></td>
                      </tr>
                      
                      <tr>
                        <td><div align="right"><img src="../images/node1.gif" width="17" height="16" /></div></td>
                        <td><img src="../images/single_doc.gif" width="15" height="16" />校园简介
                          <label>
                          <input type="checkbox" name="checkbox" value="checkbox">
                          </label></td>
                      </tr>
                      <tr>
                        <td><div align="right"><img src="../images/node1.gif" width="17" height="16" /></div></td>
                        <td><img src="../images/single_doc.gif" width="15" height="16" />组织机构
                          <input type="checkbox" name="checkbox2" value="checkbox"></td>
                      </tr>
                      <tr>
                        <td><div align="right"><img src="../images/node1.gif" width="17" height="16" /></div></td>
                        <td><img src="../images/single_doc.gif" width="15" height="16" />领导致词
                          <input type="checkbox" name="checkbox3" value="checkbox"></td>
                      </tr>
                      <tr>
                        <td><div align="right"><img src="../images/open_node.gif" width="18" height="21" /></div></td>
                        <td><div><img src="../images/open_folder.gif" width="19" height="18" /><a href="channelManage/channel_doc_list.jsp" target="perspective_content">院系概况</a>
                          <input type="checkbox" name="checkbox4" value="checkbox">
                        </div></td>
                      </tr>
                     
                      <tr>
                        <td><div align="right"><img src="../images/line_verti.gif" width="18" height="21" /></div></td>
                        <td><img src="../images/node1.gif" width="17" height="16" /><img src="../images/open_folder.gif" width="19" height="18" /><a href="channelManage/channel_doc_list.jsp" target="perspective_content">机电系
                          <input type="checkbox" name="checkbox5" value="checkbox">
                        </a></td>
                      </tr>
                      <tr>
                        <td><div align="right"><img src="../images/line_verti.gif" width="18" height="21" /></div></td>
                        <td><img src="../images/node1.gif" width="17" height="16" /><img src="../images/open_folder.gif" width="19" height="18" />矿业冶金研究院
                          <input type="checkbox" name="checkbox6" value="checkbox"></td>
                      </tr>
                      <tr>
                        <td><div align="right"><img src="../images/line_verti.gif" width="18" height="21" /></div></td>
                        <td><img src="../images/node1.gif" width="17" height="16" /><img src="../images/open_folder.gif" width="19" height="18" />中南美院
                          <input type="checkbox" name="checkbox7" value="checkbox"></td>
                      </tr>
                      <tr>
                        <td><div align="right"><img src="../images/open_node.gif" width="18" height="21" /></div></td>
                        <td><img src="../images/open_folder.gif" width="19" height="18" />校园新闻
                          <input type="checkbox" name="checkbox8" value="checkbox"></td>
                      </tr>
                      <tr>
                        <td><div align="right"><img src="../images/line_verti.gif" width="18" height="21" /></div></td>
                        <td><img src="../images/node1.gif" width="17" height="16" /><img src="../images/open_folder.gif" width="19" height="18" /><a href="channelManage/channel_doc_list.jsp" target="perspective_content">系部动态
                          <input type="checkbox" name="checkbox9" value="checkbox">
                        </a></td>
                      </tr>
                      <tr>
                        <td><div align="right"><img src="../images/line_verti.gif" width="18" height="21" /></div></td>
                        <td><img src="../images/node1.gif" width="17" height="16" /><img src="../images/open_folder.gif" width="19" height="18" /><a href="channelManage/channel_doc_list.jsp" target="perspective_content">中南快讯
                          <input type="checkbox" name="checkbox10" value="checkbox">
                        </a></td>
                      </tr>
                      <tr>
                        <td><div align="right"><img src="../images/line_verti.gif" width="18" height="21" /></div></td>
                        <td><img src="../images/node1.gif" width="17" height="16" /><img src="../images/open_folder.gif" width="19" height="18" />校内公告
                          <input type="checkbox" name="checkbox11" value="checkbox"></td>
                      </tr>
                      <tr>
                        <td><div align="right"><img src="../images/line_verti.gif" width="18" height="21" /></div></td>
                        <td><img src="../images/node1.gif" width="17" height="16" /><img src="../images/open_folder.gif" width="19" height="18" />校内通知
                          <input type="checkbox" name="checkbox12" value="checkbox"></td>
                      </tr>
                      <tr>
                        <td><div align="right"><img src="../images/line_verti.gif" width="18" height="21" /></div></td>
                        <td><img src="../images/node1.gif" width="17" height="16" /><img src="../images/open_folder.gif" width="19" height="18" />新闻动态
                          <input type="checkbox" name="checkbox13" value="checkbox"></td>
                      </tr>
                      <tr>
                        <td><div align="right"><img src="../images/close_node.gif" width="18" height="21" /></div></td>
                        <td><img src="../images/close_folder.gif" width="18" height="16" />院士风采
                          <input type="checkbox" name="checkbox14" value="checkbox"></td>
                      </tr>
                    </table>
                  </div></td>
                  </tr>
                <tr class="cms_data_tr">
                  <td height="18" valign="top" background="../images/bottom_gray.jpg"><div align="center">
                    <input name="btn_close2" type="button" class="cms_button"  onclick="javascript:window.close();" value="保存" />
                    <input name="btn_close" type="button" class="cms_button"  onclick="javascript:window.close();" value="关闭" />
                  </div></td>
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