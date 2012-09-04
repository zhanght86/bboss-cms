<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>内容管理主框架</title>
<script src="../inc/js/func.js"></script>
<script language="javascript" src="../inc/js/showPopMenu.js"></script>
<!--下面代码屏蔽ie右键-->
<script language="JavaScript" id="clientEventHandlersJS">
<!--
function document_oncontextmenu() {
   return false;//你可以在这里添加自定义代码
}
//-->
</script>
<script language="Javascript" event="oncontextmenu" for="document">
<!--
   return document_oncontextmenu()
//-->
</script>
<!--上面代码屏蔽ie右键-->

<style type="text/css">
<!--
body {
	background-color: #003399;
}
.STYLE4 {color: #8999D7}
-->
</style></head>

<body topmargin="3" rightmargin="0">
<form id="form1" name="form1" method="post" action="">
<table width="810" height="565" border="0"   cellpadding="0" align="center" cellspacing="0">
  <tr>
    <td height="82" valign=top background="../images/pagebar.jpg" style="background-repeat:no-repeat"><table width="100%" border="0">
      <tr>
        <td height="23">&nbsp;&nbsp;&nbsp;&nbsp; 当前位置:图片库</td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td  height="483" valign=top background="../images/tile.jpg" bgcolor="#FFFFFF"><table width="98%" border="0" align="center">
      
      <tr>
        <td width="100%" colspan="3"><table width="100%" border="0" align="center">
          <tr class="cms_report_tr">
            <td width="31%" height="19" ><div   align="center" style="cursor:hand">图片库目录</div></td>
            <td width="69%" ><div align="center">
              <table width="100" border="0" align="center" cellpadding="0" cellspacing="0">
                <tr>
                  <td width="24%"><div align="right"><img src="../images/preview.gif" width="19" height="18"></div></td>
                  <td width="76%" style="color:#FFFFFF">图片浏览器</td>
                </tr>
              </table>
            </div></td>
            </tr>
          <tr class="cms_data_tr">
            <td height="430" valign="top">
              <div align="left">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td height="3" colspan="2"></td>
                  </tr>
                  
                  <tr>
                    <td height="3" colspan="2"></td>
                  </tr>
                  <tr>
                    <td width="16%" height="18"><div align="right"><img src="../images/ico_site.gif" width="17" height="16" /></div></td>
                    <td width="84%" style="padding-top:6px" id="ss"><div onMouseup="javascript:showTip(ss);"
      onmouseout="javascript:hideTip();"><strong>中南大学信息港</strong> </div></td>
                  </tr>
                  <SCRIPT language=JavaScript>
<!--
var ss="<table width=130><tr><td height=18 rowspan='1' width='15' valign=top><img src='../images/menu_logo.jpg'></td><td><a onClick=\"openWin('pic_kind_add.jsp',600,280);\" style='cursor:hand;color:3B51AD'> 增加图库分类</a></td></tr>"
                                         
										 +"</table>";
-->
                </SCRIPT>
                  <tr>
                    <td><div align="right"><img src="../images/node1.gif" width="17" height="16" /></div></td>
                    <td id="xx"><div onMouseup="javascript:showTip(xx);"
      onmouseout="javascript:hideTip();"><img src="../images/open_folder.gif" width="19" height="18" />网页常用素材</div></td>
                  </tr>
				  <SCRIPT language=JavaScript>
<!--
var xx="<table width=130><tr><td height=18 rowspan='2' width='15' valign=top><img src='../images/menu_logo.jpg'></td><td><a onClick=\"openWin('pic_kindchild_add.jsp',600,290);\" style='cursor:hand;color:3B51AD'> 增加下级图库分类</a></td></tr>"
+"<tr><td height=18><a class=blue href=\"javascript:openWin('add_gallery.jsp',600,290);\"  style='cursor:hand'> 加入图片</a></td></tr>"
                                         
										 +"</table>";
-->
                </SCRIPT>
                  <tr>
                    <td><div align="right"><img src="../images/node1.gif" width="17" height="16" /></div></td>
                    <td><img src="../images/open_folder.gif" width="19" height="18" />企业略图集</td>
                  </tr>
                  <tr>
                    <td><div align="right"><img src="../images/node1.gif" width="17" height="16" /></div></td>
                    <td><img src="../images/open_folder.gif" width="19" height="18" />风光图库</td>
                  </tr>
                  <tr>
                    <td><div align="right"><img src="../images/open_node.gif" width="18" height="21" /></div></td>
                    <td><img src="../images/open_folder.gif" width="19" height="18" />小图标类</td>
                  </tr>
                  <tr>
                    <td><div align="right"><img src="../images/line_verti.gif" width="18" height="21" /></div></td>
                    <td><img src="../images/node1.gif" width="17" height="16" /><img src="../images/open_folder.gif" width="19" height="18" />办公图标</td>
                  </tr>
                  <tr>
                    <td><div align="right"><img src="../images/line_verti.gif" width="18" height="21" /></div></td>
                    <td><img src="../images/node1.gif" width="17" height="16" /><img src="../images/open_folder.gif" width="19" height="18" />科技图标</td>
                  </tr>
                  <tr>
                    <td><div align="right"><img src="../images/line_verti.gif" width="18" height="21" /></div></td>
                    <td><img src="../images/node1.gif" width="17" height="16" /><img src="../images/open_folder.gif" width="19" height="18" />电脑图标</td>
                  </tr>
                  <tr>
                    <td><div align="right"><img src="../images/close_node.gif" width="18" height="21" /></div></td>
                    <td><img src="../images/close_folder.gif" width="18" height="16" />人物图库</td>
                  </tr>
                </table>
              </div></td>
            <td valign="middle">
			<div align="center">
			<iframe width="560" height="98%" src="gallery_browse.jsp" scrolling="no" frameborder="0"></iframe>
			</div></td>
            </tr>
          <tr class="cms_data_tr">
            <td height="18" colspan="2" valign="top" background="../images/bottom_gray.jpg">&nbsp;</td>
            </tr>
          
        </table> </td>
      </tr>
      
    </table></td>
  </tr>
 
</table> </form>
</body>
</html>
