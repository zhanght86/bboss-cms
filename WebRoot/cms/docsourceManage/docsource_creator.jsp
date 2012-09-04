<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script language="javascript" src="../../sysmanager/scripts/selectTime.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>内容管理主框架</title>
<script src="../inc/js/func.js"></script>
<script language="javascript" >
function validBox(eleName){
	var ele = document.getElementsByName(eleName); //add by eyes
	//alert(ele[2].mis);
	//alert("ele length:"+ele.length);
	//   eval("var chks="+name+"."+ele.name);
	var selectCount=0;
	for(var i=0;i<ele.length;i++){
		if(ele[i].checked){
			selectCount++;
		}
	}
	if(ele[0].mis!=null && !isNaN(ele[0].mis)){//注意这里用ele[0]，而不是ele.minsel
 		//alert("有mis");
		if(selectCount<parseInt(ele[0].mis)){
			alert(convertNullToSpace(ele.on)+"至少选择"+ele[0].mis+"项");
			ele[0].focus();
			return false;
		}
	}
	if(ele[0].mas!=null && !isNaN(ele[0].mas)){
		if(selectCount>parseInt(ele[0].mas)){
			alert(convertNullToSpace(ele[0].on)+"至多选择"+ele[0].mas+"项");
			ele[0].focus();
			return false;
		}
	}
	return true;
}

function selectAll(form){
	for (var i=0;i<form.elements.length;i++){
		var e = form.elements[i];
		e.checked = 1;
	}
	div_all.style.display="none";
	div_no.style.display="block";
}

function selectNo(form){
	for (var i=0;i<form.elements.length;i++){
		var e = form.elements[i];
		e.checked = 0;
	}
	div_all.style.display="block";
	div_no.style.display="none";
}

function creatordocsrc(){
	if(srccreator.srcname.value == "")
	{
		alert("请填写来源名称！");
		srccreator.srcname.focus();
		return false;
	}
	srccreator.action="docsource_add.jsp";
	srccreator.submit();
}

function docsourcelist(){
	getPropertiesContent().location.href="docsource_list.jsp";
}


function clean(){
	document.srccreator.srcname.value = "";
	document.srccreator.srclink.value = "";
	document.srccreator.srcdesc.value = "";
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

<body topmargin="3" rightmargin="0" bgcolor="#FFFFFF">
<form id="srccreator" name="srccreator" method="post" action="">
<table width="810" height="565" border="0"   cellpadding="0" align="center" cellspacing="0">
  <tr>
    <td height="82" valign=top background="../images/pagebar.jpg" style="background-repeat:no-repeat"><table width="100%" border="0">
      <tr>
        <td height="23">&nbsp;&nbsp;&nbsp;&nbsp; 当前位置:文档来源－新增文档来源</td>
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
				<td width="10%">&nbsp;</td>
				<td width="40%">&nbsp;</td>
				<td width="10%">&nbsp;</td>
				<td width="48%">&nbsp;</td>
			</tr>
			
			<tr>
				<td width="10%"><div align="right">来源名称：</div></td>
				<td colspan="5"><input name="srcname" type="text" size="30">&nbsp;&nbsp;&nbsp;<span class="red_star">*</span></td>
			</tr>
			<tr>
				<td width="10%"><div align="right">来源链接：</div></td>
				<td colspan="5"><input name="srclink" type="text" size="30"></td>
			</tr>
			<tr>
				<td width="10%"><div align="right">来源说明：</div></td>
				<td colspan="5"><input name="srcdesc" type="text" size="70"></td>
			</tr>
			<tr>
        		<td colspan="5" align="left">
					<input type="button" name="submita" class="cms_button" value="提交" onClick="creatordocsrc()">
					<input type="button" name="submitb" class="cms_button" value="重置" onClick="clean()">
					<input type="button" name="submitc" class="cms_button" value="返回" onClick="docsourcelist()">
				</td>
			</tr>
			<tr>
				<td colspan="5"></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>
