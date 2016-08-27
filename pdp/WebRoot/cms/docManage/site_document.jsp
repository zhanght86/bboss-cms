<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>

<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
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
function factum()
	{
		getPropertiesContent().location.href="../docManage/document_factum.jsp";   
	}
function again()
	{
		getPropertiesContent().location.href="../docManage/document_again.jsp";   
	}

</script>
<style type="text/css">
<!--
body {
	background-color: #ffffff;
}
.STYLE1 {color: #0000FF}
.STYLE2 {color: #000099}
-->
</style></head>

<body topmargin="3" rightmargin="0">
<form id="form1" name="form1" method="post" action="">
<table width="100%" height="565" border="0"   cellpadding="0" align="center" cellspacing="0">
  <tr>
    <td height="82" valign=top background="../images/pagebar.jpg" style="background-repeat:no-repeat"><table width="100%" border="0">
      <tr>
        <td height="23">&nbsp;&nbsp;&nbsp;&nbsp; 当前位置:我的工作－工作列表</td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
	   <tr>
       <td >
	     <div align="right">
	     <div align="right"><a href="site_document.jsp">工作列表 |</a>  <a href="site_document_submit.jsp">已呈文档</a> |  <a href="site_document_%20again.jsp">返工文档 |</a>  <a href="site_document_each.jsp">个人文档</a> |  <a href="site_document_draft.jsp">草稿箱</a> |  <a href="site_document_dustbin.jsp"></a>  <a href="../main.jsp"></a></div></td>
       </tr>
    </table></td>
  </tr>
  <tr>
    <td  height="483" valign=top background="../images/tile.jpg" bgcolor="#FFFFFF"><table width="98%" border="0" align="center">

      <tr>
        <td width="18%">&nbsp; 标题：
          <input name="textfield" type="text" class="cms_text" size="10"></td>
        <td width="21%">&nbsp; 正文类容：
          <input name="textfield" type="text" class="cms_text" size="10"></td>
        <td width="18%">&nbsp; 作者：
          <input name="textfield" type="text" class="cms_text" size="10"></td>
        <td width="18%">              <div align="left"> 关键词：
            <input name="textfield" type="text" class="cms_text" size="10">
        </div></td>
        <td width="25%"> &nbsp; 发稿人：<input name="textfield" type="text" class="cms_text" size="10"></td>
      </tr>
	    <tr>
        <td width="18%">&nbsp; 状态：          
          <select name="select" class="cms_select">
            <option>全部</option>
            <option>已发</option>
            <option>归档</option>
            <option>正审</option>
            <option>已签</option>
            <option>已否</option>
            <option>返工</option>
            <option>已编</option>
            <option>新稿</option>
          </select></td>
        <td width="21%">&nbsp; 呈送时间：          
          <select name="select" class="cms_select">
            <option>不限</option>
            <option>最近一天</option>
            <option>最近一周</option>
            <option>最近一月</option>
            <option>最近三月</option>
            <option>最近一年</option>
          </select></td>
        <td width="18%">&nbsp;</td>
        <td>              <div align="left">
          </div></td>
        <td><div align="center">
            <input name="Submit" type="submit" class="cms_button" value="查询">
            <input name="Submit" type="submit" class="cms_button" value="显示所有"> 
          </div></td>
      </tr>
      <tr>
        <td colspan="5"><table width="98%" border="0" align="center">
          <tr class="cms_report_tr">
            <td width="6%" ><div  id="div_all" onClick="selectAll(form1)" align="center" style="cursor:hand">
              <div align="center">全选</div>
            </div><div  style='display:none;cursor:hand' id='div_no' onClick='selectNo(form1)' align='center' >
              <div align="left">全不选</div>
            </div></td>
            <td width="7%" ><div align="center">状态</div></td>
            <td width="42%" height="19"><div align="center">文档标题</div></td>
            <td width="11%"><div align="center">呈送人</div></td>
            <td width="17%"><div align="center">所属频道</div></td>
            <td width="17%"><div align="center">呈送时间</div></td>
          </tr>
          <tr class="cms_data_tr">
            <td>
                
                  <div align="center">
                    <input type="checkbox" name="checkbox" value="checkbox" />
                  <span class="STYLE2">1</span></div></td>
            <td><div onClick="openWin('template_edit.jsp',900,800)" style="cursor:hand">
              <div align="center">新稿</div>
            </div></td>
          
			 <td><div onClick="openWin('../templateManage/Managetemplate_edit.jsp',900,800)" style="cursor:hand"><u>中南大学首页模板</u></div></td>
            <td><div align="center">admin</div></td>
            <td><div align="center">新闻</div></td>
            <td><div align="center">2006/11/27 10:36 </div></td>
          </tr>
          <tr class="cms_data_tr">
            <td>
              
                <div align="center">
                  <input type="checkbox" name="checkbox2" value="checkbox" />
                  <span class="STYLE2">2</span> </div></td>
            <td><div onClick="openWin('template_edit.jsp',900,800)" style="cursor:hand">
              <div align="center">新稿</div>
            </div></td>
            <td><div align="left"><u><a href="../templateManage/template_preview.jsp">校内新闻细览模板</a></u></div></td>
            <td><div align="center">admin</div></td>
            <td><div align="center">娱乐</div></td>
            <td><div align="center">2006/11/27 10:36 </div></td>
          </tr>
          <tr class="cms_data_tr">
            <td>
              
                <div align="center">
                  <input type="checkbox" name="checkbox22" value="checkbox" />
                  <span class="STYLE2">3</span></div></td>
            <td><div align="center">新稿</div></td>
            <td><div align="left"><a href="../templateManage/template_preview.jsp">办事指南概览</a></div></td>
            <td><div align="center">admin</div></td>
            <td><div align="center">体育</div></td>
            <td><div align="center">2006/11/25 08:56 </div></td>
          </tr>
          <tr class="cms_data_tr">
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
            <td><div align="center"></div></td>
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
          </tr>
          <tr class="cms_data_tr">
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
          </tr>
          <tr class="cms_data_tr">
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
          </tr>
          <tr class="cms_data_tr">
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
          </tr>
          <tr class="cms_data_tr">
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
          </tr>
          <tr class="cms_data_tr">
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
            <td><div align="left"></div></td>
          </tr>
        </table> </td>
      </tr>
      <tr>
        <td colspan="4"><div align="center">第1页 共1页| 上一页 | 下一页 | 尾页<span class="STYLE1"> Go</span>
          <label>
          <select name="select" class="cms_select">
            <option value="1">1</option>
            <option value="2">2</option>
          </select>
          </label> 
          页
</div></td>
        <td><div align="center">
         
            <input name="Submit" type="button" class="cms_button" value="呈送文档" onClick="factum()">
            <input name="Submit" type="button" class="cms_button" value="要求返工" onClick="again()">
        </div></td>
      </tr>
    </table></td>
  </tr>
 
</table> </form>
</body>
</html>
