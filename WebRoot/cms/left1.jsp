<%@ page contentType="text/html; charset=UTF-8" language="java"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<link href="inc/css/cms.css" rel="stylesheet" type="text/css">
<script language="javascript" src="inc/js/showPopMenu.js"></script>
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
<script src="inc/js/func.js"></script>
<style type="text/css">
<!--
body {
	background-color: #003398;
}
.STYLE1 {color: #003398}
-->
</style><head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>三一集团内容管理系统</title>
</head>
<body scroll=no rightmargin="0" topmargin="3" leftmargin="3">
<table width="192" height="620"  cellpadding="0" cellspacing="0"  border="0">
  
  <tr>
    <td width="150" height="521" valign="top" style="background-repeat:repeat-y"><table width="192" height="572" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="192" height="21" valign="top"  style="background-repeat: no-repeat" background="images/up.jpg"><table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td width="11%">&nbsp;</td>
            <td width="65%" style="padding-top:2px"><select  onchange="location.href=this.options[this.selectedIndex].value"  name="select" class="cms_select" style="background-color:#e5e5e5">
                <option value="left.jsp">中南大学信息港</option>
                <option value="left1.jsp" selected="selected">省政府门户</option>
            </select></td>
            <td width="24%"><img style="cursor:hand" onClick="openWin('siteManage/site_add.jsp',560,500);" src="images/site.gif" width="25" height="17" /></td>
          </tr>
        </table></td>
      </tr>     
      <tr>
        <td height="450" valign="top" bgcolor="#F6F6F6"><table width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td height="5" colspan="2"></td>
              </tr>
            <tr>
              <td height="18">&nbsp;</td>
              <td style="padding-top:6px">&nbsp;</td>
            </tr>
            <tr>
              <td width="16%" height="18"><div align="right"><img src="images/ico_site.gif" width="17" height="16" /></div></td>
              <td width="84%" style="padding-top:6px"><strong>湖南省政府门户</strong> </td>
            </tr>
           
            <tr>
              <td><div align="right"><img src="images/open_node.gif" width="18" height="21" /></div></td>
              <td><img src="images/open_folder.gif" width="19" height="18" />政务公开</td>
            </tr>
            <tr>
              <td><div align="right"><img src="images/line_verti.gif" width="18" height="21" /></div></td>
              <td><img src="images/node1.gif" width="17" height="16" /><img src="images/open_folder.gif" width="19" height="18" />政策法规</td>
            </tr>
            <tr>
              <td><div align="right"><img src="images/line_verti.gif" width="18" height="21" /></div></td>
              <td><img src="images/node1.gif" width="17" height="16" /><img src="images/open_folder.gif" width="19" height="18" />办事指南</td>
            </tr>
            <tr>
              <td><div align="right"><img src="images/line_verti.gif" width="18" height="21" /></div></td>
              <td><img src="images/node1.gif" width="17" height="16" /><img src="images/open_folder.gif" width="19" height="18" />民众信访</td>
            </tr>
            <tr>
              <td><div align="right"><img src="images/close_node.gif" width="18" height="21" /></div></td>
              <td><img src="images/close_folder.gif" width="18" height="16" />政府新闻</td>
            </tr>
            <tr>
              <td><div align="right"><img src="images/close_node.gif" width="18" height="21" /></div></td>
              <td><img src="images/close_folder.gif" width="18" height="16" />招商投资</td>
            </tr>
			 
            <tr>
              <td><div align="right"><img src="images/node1.gif" width="17" height="16" /></div></td>
              <td><img src="images/single_doc.gif" width="15" height="16" />省政府机构设置</td>
            </tr>
            <tr>
              <td><div align="right"><img src="images/node1.gif" width="17" height="16" /></div></td>
              <td><img src="images/single_doc.gif" width="15" height="16" />政府职能</td>
            </tr>
            <tr>
              <td height="153">&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
          </table></td>
      </tr>
      <tr>
        <td height="21" background="images/middle.jpg" style="background-repeat: no-repeat"><table width="100%" border="0">
          <tr>
            <td width="19%"><div align="right"><span style="padding-top:3px"><img src="images/trash.gif" width="17" height="14"></span></div></td>
            <td width="81%" style="padding-top:3px">回收站 </td>
          </tr>
        </table></td>
      </tr>
      <tr>
        <td height="21" background="images/middle.jpg" style="background-repeat: no-repeat"><table width="100%" border="0">
          <tr>
            <td width="19%"><div align="right"><span style="padding-top:3px"><img src="images/pic_lib.gif" width="17" height="14"></span></div></td>
            <td width="81%" style="padding-top:3px">图片库 </td>
          </tr>
        </table></td>
      </tr>
      <tr>
        <td height="105" valign="top" bgcolor="#FCFCFC"><table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td width="9%">&nbsp;</td>
            <td width="91%">&nbsp;</td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
        </table></td>
      </tr>
    </table></td>
    <td width="53" valign="top">&nbsp;</td>
  </tr>
  <tr>
    <td height="1" colspan="2" bgcolor="#6382C2" ></td>
  </tr>
</table>
</body>
</html>