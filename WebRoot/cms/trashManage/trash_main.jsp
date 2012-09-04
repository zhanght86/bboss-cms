<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>内容管理主框架</title>
<script src="../inc/js/func.js"></script>
<script language="javascript" >
 function validBox(eleName)
{
        var ele = document.getElementsByName(eleName); //add by eyes
        //alert(ele[2].mis);
        //alert("ele length:"+ele.length);
        //   eval("var chks="+name+"."+ele.name);
        var selectCount=0;
        for(var i=0;i<ele.length;i++)
        {
                if(ele[i].checked)
                {
                        selectCount++;
                }
        }

        if(ele[0].mis!=null && !isNaN(ele[0].mis))//注意这里用ele[0]，而不是ele.minsel
        {
                //alert("有mis");
                if(selectCount<parseInt(ele[0].mis))
                {
                        alert(convertNullToSpace(ele.on)+"至少选择"+ele[0].mis+"项");
                        ele[0].focus();
                        return false;
                }
        }
        if(ele[0].mas!=null && !isNaN(ele[0].mas))
        {
                if(selectCount>parseInt(ele[0].mas))
                {
                        alert(convertNullToSpace(ele[0].on)+"至多选择"+ele[0].mas+"项");
                        ele[0].focus();
                        return false;
                }
        }
        return true;
}
function selectAll(form)
{
  for (var i=0;i<form.elements.length;i++)
    {
     var e = form.elements[i];
     e.checked = 1;
    }
	div_all.style.display="none";
	div_no.style.display="block";
	
}

function selectNo(form)
{
  for (var i=0;i<form.elements.length;i++)
    {
     var e = form.elements[i];
     e.checked = 0;
    }
	div_all.style.display="block";
	div_no.style.display="none";
	
}


</script>
<style type="text/css">
<!--
body {
	background-color: #003399;
}
.STYLE1 {color: #0000FF}
-->
</style></head>

<body topmargin="3" rightmargin="0">
<form id="form1" name="form1" method="post" action="">
<table width="810" height="565" border="0"   cellpadding="0" align="center" cellspacing="0">
  <tr>
    <td height="82" valign=top background="../images/pagebar.jpg" style="background-repeat:no-repeat"><table width="100%" border="0">
      <tr>
        <td height="23">&nbsp;&nbsp;&nbsp;&nbsp; 当前位置:回收站</td>
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
        <td width="48%">&nbsp; <span class="STYLE1">站点</span>[中南大学信息港 ]</td>
        <td width="51%">
          <div align="right">
            <input name="Submit"  onClick="openWin('template_import.jsp',500,400)" type="button" class="cms_button" value="还原" />  
            <input name="Submit2" type="button" class="cms_button" onClick="openWin('template_export.jsp',500,300)" value="彻底清除" />
          </div></td>
        <td width="1%">&nbsp;</td>
      </tr>
      <tr>
        <td colspan="3"><table width="98%" border="0" align="center">
          <tr class="cms_report_tr">
            <td width="5%" ><div  id="div_all" onClick="selectAll(form1)" align="center" style="cursor:hand">全选</div><div  style='display:none;cursor:hand' id='div_no' onClick='selectNo(form1)' align='center' >全不选</div></td>
            <td width="59%" ><div align="center">资源名</div></td>
            <td width="10%">回收人</td>
            <td width="8%" height="19"><div align="center">类型</div></td>
            <td width="18%"><div align="center">回收时间</div></td>
            </tr>
          <tr class="cms_data_tr">
            <td>
              <div align="center">
                <input type="checkbox" name="checkbox" value="checkbox" />
              </div></td>
            <td><div ><u><img src="../images/open_folder.gif" width="19" height="18" />校园卡办理</u></div></td>
            <td><div align="center">admin</div></td>
            <td><div align="center">频道</div></td>
            <td><div align="center">2006/11/10 05:30 </div></td>
            </tr>
          <tr >
            <td><div align="center">
              <input type="checkbox" name="checkbox3" value="checkbox" />
            </div></td>
            <td><img src="../images/node1.gif" width="17" height="16" /><img src="../images/single_doc.gif" width="14" height="16" /><a onClick="openWin('../docManage/doc_preview.jsp',900,800)" style="cursor:hand"><U>校园卡办理手续说明</U></a></td>
            <td><div align="center">guest</div></td>
            <td><div align="center">文档</div></td>
            <td><div align="center">2006/11/10 05:30 </div></td>
          </tr>
          <tr >
            <td><div align="center">
              <input type="checkbox" name="checkbox4" value="checkbox" />
            </div></td>
            <td><img src="../images/node1.gif" width="17" height="16" /><img src="../images/single_doc.gif" width="14" height="16" />校园卡遗失怎么处理</td>
            <td><div align="center">guest</div></td>
            <td><div align="center">文档</div></td>
            <td><div align="center">2006/11/10 05:30 </div></td>
          </tr>
         
          <tr class="cms_data_tr">
            <td><div align="center">
              <input type="checkbox" name="checkbox2" value="checkbox" />
            </div></td>
            <td><div style="cursor:hand"><u><img src="../images/open_folder.gif" width="19" height="18" />学生园地</u></div></td>
            <td><div align="center">admin</div></td>
            <td><div align="center">频道</div></td>
            <td><div align="center">2006/11/08 15:30 </div></td>
            </tr>
          <tr class="cms_data_tr">
            <td><div align="center">
              <input type="checkbox" name="checkbox22" value="checkbox" />
            </div></td>
            <td><u><img src="../images/open_folder.gif" width="19" height="18" /></u>院士风采</td>
            <td><div align="center">admin</div></td>
            <td><div align="center">频道</div></td>
            <td><div align="center">2006/11/01 10:30 </div></td>
            </tr>
          <tr class="cms_data_tr">
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            </tr>
          <tr class="cms_data_tr">
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            </tr>
          <tr class="cms_data_tr">
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            </tr>
          <tr class="cms_data_tr">
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            </tr>
          <tr class="cms_data_tr">
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            </tr>
          <tr class="cms_data_tr">
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            </tr>
        </table> </td>
      </tr>
      <tr>
        <td colspan="3"><div align="center">首页 | 上一页 | 下一页 | 尾页<span class="STYLE1"> Go</span>
          <label>
          <select name="select" class="cms_select">
            <option value="1">1</option>
            <option value="2">2</option>
          </select>
          </label> 
          页
</div></td>
      </tr>
    </table></td>
  </tr>
 
</table> </form>
</body>
</html>
