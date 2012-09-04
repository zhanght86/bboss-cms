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
                <option value="left.jsp" selected="selected">中南大学信息港</option>
                <option value="left1.jsp" >省政府门户</option>
            </select></td>
            <td width="24%"><img style="cursor:hand" onClick="openWinCommon('siteManage/site_add.jsp');" src="images/site.gif" width="25" height="17" /></td>
          </tr>
        </table></td>
      </tr>     
      <tr>
        <td height="460" valign="top" bgcolor="#F6F6F6"><table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td height="3" colspan="2"></td>
          </tr>
          <tr>
            <td height="22" colspan="2">&nbsp; </td>
            </tr>
          <tr>
            <td height="3" colspan="2"></td>
          </tr>
          <tr>
            <td width="16%" height="18"><div align="right"><img src="images/ico_site.gif" width="17" height="16" /></div></td>
            <td width="84%" style="padding-top:6px" id="ss"><div onMouseup="javascript:showTip(ss);"
      onmouseout="javascript:hideTip();"><strong>中南大学信息港</strong> </div></td>
          </tr>
<SCRIPT language=JavaScript>
<!--
var ss="<table width=130><tr><td height=18 rowspan='8' width='15' valign=top><img src='images/menu_logo.jpg'></td><td><a onClick=\"openWin('channelManage/channel_add.jsp',600,640);\" style='cursor:hand;color:3B51AD'> 增加频道</a></td></tr>"+"<tr><td height=18><a class=blue href=\"javascript:openWin('siteManage/site_edit.jsp',500,520);\"  style='cursor:hand'> 编辑站点</a></td></tr>"
                                         +"<tr><td height=18><a class=blue href=\"javascript:openWin('siteManage/site_preview.jsp',900,800);\"  style='cursor:hand'> 站点预览</a></td></tr>"
										    +"<tr><td height=18><a class=blue href=\"javascript:openWin('rightManage/site_right.jsp',800,600);\"  style='cursor:hand'> 站点授权</a></td></tr>"	
											    +"<tr><td height=18><a class=blue href=\"javascript:openWin('deployManage/site_deploy.jsp',600,250);\"  style='cursor:hand'> 站点增量发布</a></td></tr>"																							    +"<tr><td height=18><a class=blue href=\"javascript:openWin('deployManage/site_deploy.jsp',600,250);\"  style='cursor:hand'> 站点完全发布</a></td></tr>"
										 +"<tr><td height=18><a   class=blue href=\"javascript:if(confirm('您真的要删除该站点 吗?\\n如果删除它,那么它下面的频道以及文档也相应清除,是否继续?')){openWin('siteManage/site_del.jsp',600,220);}\"  style='cursor:hand'> 删除该站点</a></td></tr>"+"<tr><td height=18><a class=blue href=\"javascript:openWinCommon('templateManage/template_main.jsp');\"  style='cursor:hand'> 模板管理</a></td></tr>"
										 +"</table>";
-->
</SCRIPT>
          <tr>
            <td><div align="right"><img src="images/node1.gif" width="17" height="16" /></div></td>
            <td><img src="images/single_doc.gif" width="15" height="16" />校园简介</td>
          </tr>
          
          <tr>
            <td><div align="right"><img src="images/node1.gif" width="17" height="16" /></div></td>
            <td><img src="images/single_doc.gif" width="15" height="16" />组织机构</td>
          </tr>
          <tr>
            <td><div align="right"><img src="images/node1.gif" width="17" height="16" /></div></td>
            <td><img src="images/single_doc.gif" width="15" height="16" />领导致词</td>
          </tr>
          <tr>
            <td><div align="right"><img src="images/open_node.gif" width="18" height="21" /></div></td>
            <td id="xx"><div onMouseup="javascript:showTip(xx);"
      onmouseout="javascript:hideTip();"><img src="images/open_folder.gif" width="19" height="18" /><a href="channelManage/channel_doc_list.jsp" target="perspective_content">院系概况</a></div></td>
          </tr><SCRIPT language=JavaScript>
<!--
var xx="<table width=130><tr><td height=18 rowspan='8' width='15' valign=top><img src='images/menu_logo.jpg'></td><td><a onClick=\"openWin('channelManage/channel_add.jsp',600,640);\" style='cursor:hand;color:3B51AD'> 增加子频道</a></td></tr>"+"<tr><td height=18><a class=blue href=\"javascript:openWin('siteManage/site_edit.jsp',500,520);\"  style='cursor:hand'> 编辑频道</a></td></tr>"
                                         +"<tr><td height=18><a class=blue href=\"javascript:openWin('siteManage/site_preview.jsp',900,800);\"  style='cursor:hand'> 频道预览</a></td></tr>"
										    +"<tr><td height=18><a class=blue href=\"javascript:openWin('rightManage/channel_right.jsp',800,600);\"  style='cursor:hand'> 频道授权</a></td></tr>"	
											    +"<tr><td height=18><a class=blue href=\"javascript:openWin('deployManage/site_deploy.jsp',600,250);\"  style='cursor:hand'> 频道增量发布</a></td></tr>"																							    +"<tr><td height=18><a class=blue href=\"javascript:openWin('deployManage/site_deploy.jsp',600,250);\"  style='cursor:hand'> 频道完全发布</a></td></tr>"
										 +"<tr><td height=18><a   class=blue href=\"javascript:if(confirm('您真的要删除该频道 吗?\\n如果删除它,那么它下面的文档以及图片等也相应清除,是否继续?')){openWin('siteManage/site_del.jsp',600,220);}\"  style='cursor:hand'> 删除该频道</a></td></tr>"+"<tr><td height=18><a class=blue href=\"javascript:openWinCommon('templateManage/template_main.jsp');\"  style='cursor:hand'> 模板管理</a></td></tr>"
										 +"</table>";
-->
</SCRIPT>
          <tr>
            <td><div align="right"><img src="images/line_verti.gif" width="18" height="21" /></div></td>
            <td><img src="images/node1.gif" width="17" height="16" /><img src="images/open_folder.gif" width="19" height="18" /><a href="channelManage/channel_doc_list.jsp" target="perspective_content">机电系</a></td>
          </tr>
          <tr>
            <td><div align="right"><img src="images/line_verti.gif" width="18" height="21" /></div></td>
            <td><img src="images/node1.gif" width="17" height="16" /><img src="images/open_folder.gif" width="19" height="18" />矿业冶金研究院</td>
          </tr>
          <tr>
            <td><div align="right"><img src="images/line_verti.gif" width="18" height="21" /></div></td>
            <td><img src="images/node1.gif" width="17" height="16" /><img src="images/open_folder.gif" width="19" height="18" />中南美院</td>
          </tr>
          <tr>
            <td><div align="right"><img src="images/open_node.gif" width="18" height="21" /></div></td>
            <td><img src="images/open_folder.gif" width="19" height="18" />校园新闻</td>
          </tr>
          <tr>
            <td><div align="right"><img src="images/line_verti.gif" width="18" height="21" /></div></td>
            <td><img src="images/node1.gif" width="17" height="16" /><img src="images/open_folder.gif" width="19" height="18" /><a href="channelManage/channel_doc_list.jsp" target="perspective_content">系部动态</a></td>
          </tr>
          <tr>
            <td><div align="right"><img src="images/line_verti.gif" width="18" height="21" /></div></td>
            <td><img src="images/node1.gif" width="17" height="16" /><img src="images/open_folder.gif" width="19" height="18" /><a href="channelManage/channel_doc_list.jsp" target="perspective_content">中南快讯</a></td>
          </tr>
          <tr>
            <td><div align="right"><img src="images/line_verti.gif" width="18" height="21" /></div></td>
            <td><img src="images/node1.gif" width="17" height="16" /><img src="images/open_folder.gif" width="19" height="18" />校内公告</td>
          </tr>
          <tr>
            <td><div align="right"><img src="images/line_verti.gif" width="18" height="21" /></div></td>
            <td><img src="images/node1.gif" width="17" height="16" /><img src="images/open_folder.gif" width="19" height="18" />校内通知</td>
          </tr>
          <tr>
            <td><div align="right"><img src="images/line_verti.gif" width="18" height="21" /></div></td>
            <td><img src="images/node1.gif" width="17" height="16" /><img src="images/open_folder.gif" width="19" height="18" />新闻动态</td>
          </tr><tr>
            <td><div align="right"><img src="images/close_node.gif" width="18" height="21" /></div></td>
            <td><img src="images/close_folder.gif" width="18" height="16" />院士风采</td>
          </tr>
        </table>          </td>
      </tr>
      <tr>
	 
        <td height="21" background="images/middle.jpg" style="background-repeat: no-repeat"><table width="100%" border="0">
          <tr>
            <td width="19%"><div align="right"><span style="padding-top:3px"><img src="images/trash.gif" width="17" height="14"></span></div></td>
            <td width="81%" style="padding-top:3px"><a href="trashManage/trash_main.jsp" target="perspective_content">回收站</a> </td>
          </tr>
        </table></td>
      </tr>
      <tr>
        <td height="21" background="images/middle.jpg" style="background-repeat: no-repeat"><table width="100%" border="0">
          <tr>
            <td width="19%"><div align="right"><span style="padding-top:3px"><img src="images/pic_lib.gif" width="17" height="14"></span></div></td>
            <td width="81%" style="padding-top:3px"><a href="pictureManage/picture_main.jsp" target="perspective_content">图片库</a> </td>
          </tr>
        </table></td>
      </tr>
      <tr>
        <td height="105" valign="top" bgcolor="#FCFCFC">&nbsp;</td>
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