<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="../../WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="../../WEB-INF/dictionary.tld" prefix="dict"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>稿源管理</title>
<script src="../inc/js/func.js"></script>
<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
<script type="text/javascript" src="../../public/datetime/calender_date.js" language="javascript"></script>
<script language="javascript" >
function checkAll(totalCheck,checkName){//复选框全部选中
	var selectAll = document.getElementsByName(totalCheck);
	var o = document.getElementsByName(checkName);
	if(selectAll[0].checked==true){
		for (var i=0; i<o.length; i++){
			if(!o[i].disabled){
				o[i].checked=true;
			}
		}
	}else{
		for (var i=0; i<o.length; i++){
			o[i].checked=false;
		}
	}
}

function checkOne(totalCheck,checkName,id){//单个选中复选框
	var selectAll = document.getElementsByName(totalCheck);
	var o = document.getElementsByName(checkName);
	var cbs = true;
	for (var i=0;i<o.length;i++){
		if(!o[i].disabled){
			if (o[i].checked==false){
				cbs=false;
			}
		}
	}
	if(cbs){
		selectAll[0].checked=true;
	}else{
		selectAll[0].checked=false;
	}
}		

function docsrcsearch(){
	var fromDate = document.all.fromDate.value;
	var toDate = document.all.toDate.value;
	if(toDate.length>0 && fromDate.length>0)
	if(toDate<fromDate)
	{
		alert("结束时间不能小于开始时间!");
		return false;
	}
	//bug432:结束时间不能小于开始时间,weida
	docsourceList.action="docsource_list.jsp"
	docsourceList.submit();
}

function docsrcsearchAll(){
	document.all.srcName.value = "";
	document.all.provider.value = "";
	document.all.fromDate.value = "";
	document.all.toDate.value = "";
	docsourceList.action="docsource_list.jsp"
	docsourceList.submit();
}//显示所有数据条目的函数,weida

function docsrccreator(){
	document.location.href="docsource_creator.jsp";
}
function docsrcupdate(){
	var docId = document.getElementsByName("checkBoxOne");
	if(docId.length){
		var count = 0 ;
		var index = -1;
		for(var i = 0; i < docId.length; i ++){
			if(!docId[i].checked){
				continue;
			}else{				
				count ++;
				index = i;
			}
		}
		if(count == 0){
			alert("至少选择一个文档来源进行修改！");
			return;
		}else if(count == 1){
			getPropertiesContent().location.href="docsource_update.jsp?docId="+docId[index].value;
		}else{
			alert("只能选择一个文档来源进行修改！");
			return;
		}
	}
}
function docsrcdelete(){
	var isSelect = false;
	var outMsg;
	    
    for (var i=0;i<docsourceList.elements.length;i++) {
		var e = docsourceList.elements[i];
		if (e.name == 'checkBoxOne'){
			if (e.checked){
	       		isSelect=true;
	       		break;
		    }
		}
    }
    if (isSelect){
		outMsg = "你确定要删除吗？(删除后是不可以再恢复的)。";
    	if (confirm(outMsg)){
    		docsourceList.action="docsource_delete.jsp"
			docsourceList.submit();
		}
		return true;
    }else{
    	alert("至少选择一个文档来源进行删除！");
	    return false;
    }
	return false;
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
<form id="docsourceList" name="docsourceList" method="post" action="">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td style="background:url(../images/querybox_bg.gif) repeat-x top">
			<table width="100%" height="100%" border="0" cellpadding="0" align="center" cellspacing="0" class="query_box">
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp; 当前位置：稿源管理&nbsp;>>&nbsp;稿源管理</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="query_table">
							<tr>
								<td height='30' align="right">来源名称：</td>
								<td height='30' align="left" valign='middle'>
									<input type="text" size="16" class="cms_text" name="srcName" value="">
								</td>
								<td height='30' align="right">创建者：</td>
								<td height='30' align="left" valign='middle'>
									<input type="text" size="16" class="cms_text" name="provider" value="">
								</td>
								<td height='30' align="right">&nbsp;</td>
							</tr>
							<tr>
								<td height='30' align="right">创建时间从：</td>
								<td height='30' align="left" valign='middle'>	
									<input type="text" size="16" class="cms_text" name="fromDate" value=""  readonly= "true">
									<input type="button" value="..." onclick="showdate(document.all('fromDate'))"/>
								</td>
								<td height='30' align="right">到：</td>
								<td height='30' align="left" valign='middle'>	
									<input type="text" size="16" class="cms_text" name="toDate" value=""  readonly= "true">
									<input type="button" value="..." onclick="showdate(document.all('toDate'))"/>
								</td>
								<td height='30' colspan="6" align="right">
									<input name="search" type="button" class="cms_button" value="查询" onClick="docsrcsearch()">
									<input name="search" type="button" class="cms_button" value="显示所有" onClick="docsrcsearchAll()">
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%" border="1" align="center" class="Datalisttable">
				<tr>
					<td height='25' colspan="9" background="<%=request.getContextPath()%>/cms/images/data_list_tHeadbg.jpg" style="text-align:left; background:url(<%=request.getContextPath()%>/cms/images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7">
						<a style="cursor:hand" onClick="docsrccreator()">
						<div class="DocumentOper">新增</div></a>
						<a style="cursor:hand" onClick="docsrcupdate()">
						<div class="DocumentOper">修改</div></a>
						<a style="cursor:hand" onClick="docsrcdelete()">
						<div class="DocumentOper">删除</div></a>
					</td>
				</tr>
 				<pg:listdata dataInfo="DocsourceList" keyName="DocsourceList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="10" scope="request" data="DocsourceList" isList="false">
				<tr class="cms_report_tr">
					<!--设置分页表头-->
					<td width="5%" align="center" height='30'>
						<P align="left">
							<input type="checkbox" class="checkbox" hideFocus name="checkBoxAll" onclick="checkAll('checkBoxAll','checkBoxOne')">
						</P>
					</td>
					<td width="10%" align="center">来源编号</td>
					<td width="15%" align="center">来源名称</td>
					<td width="30%" align="center">来源说明</td>
					<td width="15%" align="center">来源链接</td>
					<td width="10%" align="center">创建者</td>
					<td width="10%" align="center">创建时间</td>
				</tr>
				<pg:param name="srcName" />
				<!--检测当前页面是否有记录-->
				<pg:notify>
				<tr height="18px" class="labeltable_middle_tr_01">
					<td colspan=100 align='center'>
						暂时没有稿源
					</td>
				</tr>
				</pg:notify>
				<!--list标签循环输出每条记录-->
				<pg:list>
				<tr>
					<td class="tablecells" nowrap="true" height='30'>
					<P align="left">
						<INPUT type="checkbox" class="checkbox" hideFocus name="checkBoxOne" onclick="checkOne('checkBoxAll','checkBoxOne','<pg:cell colName="DOCSOURCE_ID" defaultValue=""/>')" value='<pg:cell colName="DOCSOURCE_ID" defaultValue=""/>' >
					</P>
					</td>
					<td class="tablecells" nowrap="nowrap" width="10%" height='30'>
						<pg:cell colName="DOCSOURCE_ID" defaultValue="" />
					</td>
					<td class="tablecells" nowrap="nowrap" width="15%" height='30'>
						<pg:cell colName="SRCNAME" defaultValue="" />
					</td>
					<td class="tablecells" nowrap="nowrap" width="30%" height='30'>
						<pg:cell colName="SRCDESC" defaultValue="" />
					</td>
					<td class="tablecells" nowrap="nowrap" width="15%" height='30'>
						<pg:cell colName="SRCLINK" defaultValue="" />
					</td>
					<td class="tablecells" nowrap="nowrap" width="10%" height='30'>
						<pg:cell colName="username" defaultValue="" />
					</td>
					<td class="tablecells" nowrap="nowrap" width="10%" height='30'>
						<pg:cell colName="CRTIME" defaultValue="" />
					</td>
				</tr>
				</pg:list>
				<tr class="labeltable_middle_tr_01">
					<td colspan=10 >
						<div class="Data_List_Table_Bottom">共<pg:rowcount />条记录
						<pg:index /></div>
					</td>
				</tr>
					<input id="queryString" name="queryString" value="<pg:querystring/>" type="hidden">
				</pg:pager>
			</table>
 		</td>
	</tr>
</table>
</form>
</body>
</html>
