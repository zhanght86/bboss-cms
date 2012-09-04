<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ include file="../sysmanager/include/global1.jsp"%>

<%@ include file="../sysmanager/base/scripts/panes.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="inc/css/cms.css" rel="stylesheet" type="text/css">
<title>内容管理主框架</title>
<script src="inc/js/func.js"></script>
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
function addresslist(){
	getPropertiesContent().location.href="../cms/address_list.jsp";
}

</script>
<style type="text/css">
<!--
body {
	background-color: #003399;
}
.STYLE1 {color: #0000FF}
.STYLE2 {color: #000099}
-->
</style></head>

<body topmargin="3" rightmargin="0">
<form id="form1" name="form1" method="post" action="">
<table width="810" height="565" border="0"   cellpadding="0" align="center" cellspacing="0">
  <tr>
    <td height="82" valign=top background="images/pagebar.jpg" style="background-repeat:no-repeat"><table width="100%" border="0">
      <tr>
        <td height="23">&nbsp;&nbsp;&nbsp;&nbsp; 当前位置:通讯录－新增用户</td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
	   <tr>
       <td >
	   
	      <div align="right">  </div></td>
       </tr>
    </table></td>
  </tr>
  <tr>
    <td  height="483" valign=top background="../images/tile.jpg" bgcolor="#FFFFFF"><table width="98%" border="0" align="center">

      <tr>
        <td width="25%">&nbsp;</td>
        <td width="25%">&nbsp; </td>
        <td width="13%">&nbsp; &nbsp; </td>
        <td width="18%">              <div align="left"></div></td>
        <td width="22%">&nbsp;  </td>
      </tr>
	    <tr>
        <td colspan="5">&nbsp; <div align="left">登陆名：          
              <input name="textfield" type="text" size="12">
         </div></td>
        </tr>
		   <tr>
        <td colspan="5">&nbsp; <div align="left">实 名 ：          
              <input name="textfield" type="text" size="12">
         </div></td>
        </tr>
		   <tr>
        <td colspan="5">&nbsp; <div align="left">办公电话：          
              <input name="textfield" type="text" size="12">
         </div></td>
        </tr>
		   <tr>
        <td colspan="5">&nbsp; <div align="left">移动电话：          
              <input name="textfield" type="text" size="12">
         </div></td>
        </tr>
		   <tr>
        <td colspan="5">&nbsp; <div align="left">Email ：          
              <input name="textfield" type="text" size="12">
         </div></td>
        </tr>
		   <tr>
        <td colspan="5">&nbsp; <div align="left">传 真 ：          
              <input name="textfield" type="text" size="12">
         </div></td>
        </tr>
		   <tr>
        <td colspan="5">&nbsp; <div align="left">邮 编 ：          
              <input name="textfield" type="text" size="12">
         </div></td>
        </tr>
		   <tr>
        <td colspan="5">&nbsp; <div align="left">联系地址：          
              <textarea name="textfield" cols="20"></textarea>
         </div></td>
        </tr>
		 <tr>
        <td colspan="5">&nbsp; <div align="left">简单描述：          
              <textarea name="textfield" cols="20"></textarea>
         </div></td>
        </tr>
		 <tr>
        <td colspan="5">
			  <input type="submit" name="Submit" class="cms_button" value="添加">
			  <input type="reset" name="Submit" class="cms_button" value="重置">
			
			 <input name="Submit" type="button" class="cms_button" value="取消" onClick="addresslist()">
         </td>
        </tr>
      <tr>
        <td colspan="5"> </td>
      </tr>
    </table></td>
  </tr>
 
</table> </form>
</body>
</html>
